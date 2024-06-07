package ru.mai.lessons.rpks.impl;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.mai.lessons.rpks.DbReader;
import ru.mai.lessons.rpks.model.Rule;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static generated.jooqcodegen.Tables.ENRICHMENT_RULES;
import static org.jooq.impl.DSL.*;

@Slf4j
public class EnrichmentDbReader implements DbReader {

    private final long updateInterval;
    private final long enrichmentId;
    private final ScheduledExecutorService scheduler;
    private final HikariDataSource hikariDataSource;
    private final CountDownLatch latch = new CountDownLatch(1);
    private Rule[] rules;

    public EnrichmentDbReader(Config config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getString("db.jdbcUrl"));
        hikariConfig.setUsername(config.getString("db.user"));
        hikariConfig.setPassword(config.getString("db.password"));
        hikariConfig.setDriverClassName(config.getString("db.driver"));
        this.updateInterval = config.getLong("application.updateIntervalSec");
        this.enrichmentId = config.getLong("application.enrichmentId");
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.hikariDataSource = new HikariDataSource(hikariConfig);
        this.rules = null;
    }

    @Override
    public Rule[] getRules() {
        log.trace("Returning rules from DB");
        return rules == null ? null : rules;
    }

    public CountDownLatch startScheduling() throws SQLException {
        log.info("Enrichment Database Reader STARTS scheduling");
        DSLContext dsl = DSL.using(hikariDataSource.getConnection(), SQLDialect.POSTGRES);

        Runnable task = () -> {
            log.debug("Updating rules from DB");
            rules = dsl.select()
                    .from(
                            select(
                                    ENRICHMENT_RULES.ENRICHMENT_ID,
                                    ENRICHMENT_RULES.RULE_ID,
                                    ENRICHMENT_RULES.FIELD_NAME,
                                    ENRICHMENT_RULES.FIELD_NAME_ENRICHMENT,
                                    ENRICHMENT_RULES.FIELD_VALUE,
                                    ENRICHMENT_RULES.FIELD_VALUE_DEFAULT,
                                    rowNumber().over(
                                                    partitionBy(ENRICHMENT_RULES.FIELD_NAME)
                                                            .orderBy(ENRICHMENT_RULES.RULE_ID.desc()))
                                            .as("row_number"))
                                    .from(ENRICHMENT_RULES)
                                    .where(ENRICHMENT_RULES.ENRICHMENT_ID.eq(enrichmentId))
                                    .asTable("tmp"))
                    .where(field("tmp.row_number").eq(1))
                    .fetchStream()
                    .map(r -> new Rule(
                            r.get("enrichment_id", Long.class),
                            r.get("rule_id", Long.class),
                            r.get("field_name", String.class),
                            r.get("field_name_enrichment", String.class),
                            r.get("field_value", String.class),
                            r.get("field_value_default", String.class)))
                    .toArray(Rule[]::new);
            latch.countDown();
        };

        scheduler.scheduleAtFixedRate(
                task, 0, updateInterval * 1000, TimeUnit.MILLISECONDS
        );

        return latch;
    }

    public void stopScheduling() {
        log.info("Enrichment Database Reader STOPS scheduling");
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                    log.debug("Scheduler did not terminate in the specified time");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.debug("Scheduler was interrupted");
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void close() {
        log.trace("Closing EnrichmentDbReader");
        stopScheduling();
        hikariDataSource.close();
    }

}
