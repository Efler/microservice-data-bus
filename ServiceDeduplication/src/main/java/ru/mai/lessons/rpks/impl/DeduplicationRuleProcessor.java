package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.mai.lessons.rpks.RedisClient;
import ru.mai.lessons.rpks.RuleProcessor;
import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

@Slf4j
public class DeduplicationRuleProcessor implements RuleProcessor {

    private final RedisClient redisClient;

    public DeduplicationRuleProcessor(Config config) {
        this.redisClient = new DeduplicationRedisClient(config);
    }

    @Override
    public Message processing(Message message, Rule[] rules) {
        log.debug("Deduplicating message: {}", message.getValue());
        var state = redisClient.checkDuplicates(message, rules);
        message.setDeduplicationState(state);
        return message;
    }

}
