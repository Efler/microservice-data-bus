-- liquibase formatted sql

-- changeset eflerrr:init_deduplication_rules_table
CREATE TABLE IF NOT EXISTS public.deduplication_rules
(
    id               serial NOT NULL PRIMARY KEY,
    deduplication_id bigint NOT NULL,
    rule_id          bigint NOT NULL,
    field_name       text   NOT NULL,
    time_to_live_sec bigint NOT NULL,
    is_active        bool   NOT NULL
);

-- rollback DROP TABLE "deduplication_rules";