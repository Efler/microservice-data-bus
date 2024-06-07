package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.mai.lessons.rpks.Service;
import ru.mai.lessons.rpks.exception.DatabaseConnectionException;

@Slf4j
public class ServiceEnrichment implements Service {

    @Override
    public void start(Config config) {
        log.info("[ Starting Enrichment Service ]");
        try {
            new EnrichmentKafkaReader(config).processing();
        } catch (DatabaseConnectionException e) {
            log.error("Database connection error: {}", e.getMessage());
        } finally {
            log.info("[ Enrichment Service stopped ]");
        }
    }

}
