package ru.mai.lessons.rpks.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rule {

    private Long enricherId;
    private Long ruleId;
    private String fieldName;
    private String fieldNameEnrichment;
    private String fieldValue;
    private String fieldValueDefault;

}
