package ru.mai.lessons.rpks.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rule {

    private Long filterId;
    private Long ruleId;
    private String fieldName;
    private String filterFunctionName;
    private String filterValue;

}
