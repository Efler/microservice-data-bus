package ru.mai.lessons.rpks.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.mai.lessons.rpks.MongoDBClientEnricher;
import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

import static com.mongodb.client.model.Sorts.descending;

@Slf4j
public class EnrichmentMongoDBClientEnricher implements MongoDBClientEnricher {

    private final String mongoDatabaseName;
    private final String mongoCollectionName;
    private final MongoClient mongoClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public EnrichmentMongoDBClientEnricher(Config config) {
        var mongoConfig = config.getConfig("mongo");
        this.mongoClient = MongoClients.create(mongoConfig.getString("connectionString"));
        this.mongoDatabaseName = mongoConfig.getString("database");
        this.mongoCollectionName = mongoConfig.getString("collection");
    }

    @Override
    public Message enrich(Message message, Rule rule) {
        log.debug("Enriching message: {}", message.getValue());

        try {

            var msgJsonNode = mapper.readTree(message.getValue());

            var mongoCollection = mongoClient.getDatabase(mongoDatabaseName).getCollection(mongoCollectionName);
            var document = mongoCollection.find(
                            Filters.eq(rule.getFieldNameEnrichment(), rule.getFieldValue()))
                    .sort(descending("_id"))
                    .first();
            if (document != null) {
                log.debug("Document(s) found for rule: {}", rule);
                if (msgJsonNode.isObject()) {
                    ((ObjectNode) msgJsonNode).set(
                            rule.getFieldName(),
                            mapper.readTree(document.toJson()));
                }
            } else {
                log.debug("No documents found for rule: {}", rule);
                if (msgJsonNode.isObject()) {
                    ((ObjectNode) msgJsonNode).put(
                            rule.getFieldName(),
                            rule.getFieldValueDefault());
                }
            }
            message.setValue(mapper.writeValueAsString(msgJsonNode));
            return message;

        } catch (JsonProcessingException e) {
            log.warn("Invalid message json format: {}", message.getValue());
            return message;
        }
    }

    @Override
    public void close() {
        log.info("Closing Enrichment MongoDB Client Enricher");
        mongoClient.close();
    }
}
