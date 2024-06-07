package ru.mai.lessons.rpks.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rule {

    private Long deduplicationId;
    private Long ruleId;
    private String fieldName;
    private Long timeToLiveSec;
    private Boolean isActive;

}
