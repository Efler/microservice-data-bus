package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.mai.lessons.rpks.KafkaWriter;
import ru.mai.lessons.rpks.model.Message;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EnrichmentKafkaWriter implements KafkaWriter {

    private final String outputTopic;
    private final KafkaProducer<String, String> kafkaProducer;

    public EnrichmentKafkaWriter(Config config) {
        var producerConfig = config.getConfig("kafka.producer");
        this.outputTopic = producerConfig.getString("topic");
        this.kafkaProducer = new KafkaProducer<>(
                producerConfig.entrySet().stream()
                        .filter(entry -> !entry.getKey().equals("topic"))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().unwrapped().toString()
                        )),
                new StringSerializer(), new StringSerializer()
        );
    }

    @Override
    public void processing(Message message) {
        log.info("Sending message to Kafka... Message: {}", message.getValue());
        kafkaProducer.send(new ProducerRecord<>(outputTopic, message.getValue()));
    }

}
