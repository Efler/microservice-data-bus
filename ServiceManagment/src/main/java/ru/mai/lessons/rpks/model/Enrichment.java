package ru.mai.lessons.rpks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "enrichment_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrichment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "enrichment_id")
    @Min(1)
    private long enrichmentId;

    @Column(name = "rule_id")
    @Min(1)
    private long ruleId;

    @Column(name = "field_name")
    @NotBlank
    private String fieldName;

    @Column(name = "field_name_enrichment")
    @NotBlank
    private String fieldNameEnrichment;

    @Column(name = "field_value")
    @NotBlank
    private String fieldValue;

    @Column(name = "field_value_default")
    @NotBlank
    private String fieldValueDefault;

}
