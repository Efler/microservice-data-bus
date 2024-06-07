package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.mai.lessons.rpks.KafkaReader;
import ru.mai.lessons.rpks.KafkaWriter;
import ru.mai.lessons.rpks.RuleProcessor;
import ru.mai.lessons.rpks.exception.DatabaseConnectionException;
import ru.mai.lessons.rpks.model.Message;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class FilteringKafkaReader implements KafkaReader {

    private final Config config;
    private final KafkaConsumer<String, String> kafkaConsumer;
    private final KafkaWriter kafkaWriter;
    private final RuleProcessor ruleProcessor;

    public FilteringKafkaReader(Config config) {
        this.config = config;
        var consumerConfig = config.getConfig("kafka.consumer");
        this.kafkaConsumer = new KafkaConsumer<>(
                consumerConfig.entrySet().stream()
                        .filter(entry -> !entry.getKey().equals("topic"))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().unwrapped().toString()
                        )),
                new StringDeserializer(), new StringDeserializer()
        );
        this.kafkaWriter = new FilteringKafkaWriter(config);
        this.ruleProcessor = new FilteringRuleProcessor();
        kafkaConsumer.subscribe(Collections.singletonList(consumerConfig.getString("topic")));
    }

    @Override
    public void processing() {
        log.info("Starting Filtering Kafka Reader");
        try (FilteringDbReader dbReader = new FilteringDbReader(config)) {
            dbReader.startScheduling().await();
            while (true) {
                var messages = kafkaConsumer.poll(Duration.ofMillis(1000));
                for (var message : messages) {
                    var rules = dbReader.getRules();
                    log.debug("Processing message: {}", message.value().isBlank() ? "<invalid>" : message.value());
                    var filteredMessage = ruleProcessor.processing(
                            Message.builder()
                                    .value(message.value().isBlank() ? "<invalid>" : message.value())
                                    .filterState(false)
                                    .build(), rules);
                    if (filteredMessage.isFilterState()) {
                        log.debug("Message was accepted: {}", filteredMessage.getValue());
                        kafkaWriter.processing(filteredMessage);
                    } else {
                        log.debug("Message was rejected: {}", filteredMessage.getValue());
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Database Reader failed to connect to the database, message" + e.getMessage()
            );
        } catch (InterruptedException e) {
            log.error("Thread was interrupted while waiting for update!");
            Thread.currentThread().interrupt();
        } finally {
            log.info("Terminating Filtering Kafka Reader");
            kafkaConsumer.close();
        }
    }

}
