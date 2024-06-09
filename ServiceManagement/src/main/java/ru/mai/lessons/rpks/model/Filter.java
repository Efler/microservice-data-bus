package ru.mai.lessons.rpks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "filter_id")
    @Min(1)
    private long filterId;

    @Column(name = "rule_id")
    @Min(1)
    private long ruleId;

    @Column(name = "field_name")
    @NotBlank
    private String fieldName;

    @Column(name = "filter_function_name")
    @NotBlank
    @Pattern(regexp = "equals|not_equals|contains|not_contains")
    private String filterFunctionName;

    @Column(name = "filter_value")
    @NotBlank
    private String filterValue;

}
