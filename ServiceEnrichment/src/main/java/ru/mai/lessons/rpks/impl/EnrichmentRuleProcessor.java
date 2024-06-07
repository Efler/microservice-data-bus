package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.mai.lessons.rpks.MongoDBClientEnricher;
import ru.mai.lessons.rpks.RuleProcessor;
import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

@Slf4j
public class EnrichmentRuleProcessor implements RuleProcessor {

    private final MongoDBClientEnricher enricherClient;

    public EnrichmentRuleProcessor(Config config) {
        this.enricherClient = new EnrichmentMongoDBClientEnricher(config);
    }

    @Override
    public Message processing(Message message, Rule[] rules) {
        log.debug("Processing message with rules: {}", message.getValue());

        for (var rule : rules) {
            log.debug("Applying rule: {}", rule.toString());
            message = enricherClient.enrich(message, rule);
        }

        return message;
    }

    @Override
    public void close() throws Exception {
        log.info("Closing Enrichment Rule Processor");
        enricherClient.close();
    }

}
