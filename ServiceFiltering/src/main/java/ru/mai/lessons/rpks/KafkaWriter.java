package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.model.Message;

public interface KafkaWriter {

    void processing(Message message);

}
