package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.mai.lessons.rpks.Service;
import ru.mai.lessons.rpks.exception.DatabaseConnectionException;

@Slf4j
public class ServiceDeduplication implements Service {

    @Override
    public void start(Config config) {
        log.info("[ Starting Filtering Service ]");
        try {
            new DeduplicationKafkaReader(config).processing();
        } catch (DatabaseConnectionException e) {
            log.error("Database connection error: {}", e.getMessage());
        } finally {
            log.info("[ Filtering Service stopped ]");
        }
    }

}
