package ru.mai.lessons.rpks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deduplication_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deduplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "deduplication_id")
    @Min(1)
    private long deduplicationId;

    @Column(name = "rule_id")
    @Min(1)
    private long ruleId;

    @Column(name = "field_name")
    @NotBlank
    private String fieldName;

    @Column(name = "time_to_live_sec")
    @Min(1)
    private long timeToLiveSec;

    @Column(name = "is_active")
    private boolean isActive;

}
