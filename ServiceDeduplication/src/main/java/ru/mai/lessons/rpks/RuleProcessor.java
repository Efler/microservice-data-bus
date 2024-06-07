package ru.mai.lessons.rpks;

import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

public interface RuleProcessor {

    Message processing(Message message, Rule[] rules);

}
