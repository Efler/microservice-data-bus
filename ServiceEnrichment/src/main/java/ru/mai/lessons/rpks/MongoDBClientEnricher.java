package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

public interface MongoDBClientEnricher extends AutoCloseable {

    Message enrich(Message message, Rule rule);

}
