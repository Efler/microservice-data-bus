package ru.mai.lessons.rpks.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;
import ru.mai.lessons.rpks.RedisClient;
import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

@Slf4j
public class DeduplicationRedisClient implements RedisClient {

    private final JedisPooled jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public DeduplicationRedisClient(Config config) {
        this.jedis = new JedisPooled(
                config.getString("redis.host"), config.getInt("redis.port")
        );
    }

    private String generateKey(Message message, Rule[] rules) throws JsonProcessingException {
        log.debug("Generating key for message: {}", message.getValue().isBlank() ? "<invalid>" : message.getValue());
        var sb = new StringBuilder();
        var jsonNode = mapper.readTree(message.getValue());

        for (var rule : rules) {
            if (Boolean.TRUE.equals(rule.getIsActive())) {
                if (!sb.isEmpty()) {
                    sb.append(':');
                }
                sb.append(jsonNode.hasNonNull(rule.getFieldName())
                        ? jsonNode.get(rule.getFieldName()).asText()
                        : "<null>"
                );
            }
        }
        log.debug("Generated key: {}", sb);
        return sb.toString();
    }

    @Override
    public boolean checkDuplicates(Message message, Rule[] rules) {
        log.info("Checking duplicates: {}", message.getValue().isBlank() ? "<invalid>" : message.getValue());
        try {
            var key = generateKey(message, rules);
            if (jedis.exists(key)) {
                return false;
            } else {
                if (!key.isBlank()) {
                    jedis.set(key, message.getValue());
                    var ttl = 0L;
                    for (var rule : rules) {
                        ttl = Math.max(ttl, rule.getTimeToLiveSec());
                    }
                    jedis.expire(key, ttl);
                }
            }
            return true;
        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON-string in Message, exception message: {}", message.getValue());
            return false;
        }
    }

}
