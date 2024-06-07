package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.model.Rule;


public interface DbReader extends AutoCloseable {

    Rule[] getRules();

}
