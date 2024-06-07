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

import static generated.jooqcodegen.Tables.FILTER_RULES;

@Slf4j
public class FilteringDbReader implements DbReader, AutoCloseable {

    private final long updateInterval;
    private final ScheduledExecutorService scheduler;
    private final HikariDataSource hikariDataSource;
    private final CountDownLatch latch = new CountDownLatch(1);
    private Rule[] rules;

    public FilteringDbReader(Config config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getString("db.jdbcUrl"));
        hikariConfig.setUsername(config.getString("db.user"));
        hikariConfig.setPassword(config.getString("db.password"));
        hikariConfig.setDriverClassName(config.getString("db.driver"));
        this.updateInterval = config.getLong("application.updateIntervalSec");
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
        log.info("Filtering Database Reader STARTS scheduling");
        DSLContext dsl = DSL.using(hikariDataSource.getConnection(), SQLDialect.POSTGRES);

        Runnable task = () -> {
            log.debug("Updating rules from DB");
            rules = dsl.selectFrom(FILTER_RULES)
                    .fetchStream()
                    .map(r -> new Rule(
                            r.getFilterId(),
                            r.getRuleId(),
                            r.getFieldName(),
                            r.getFilterFunctionName(),
                            r.getFilterValue()
                    ))
                    .toArray(Rule[]::new);
            latch.countDown();
        };

        scheduler.scheduleAtFixedRate(
                task, 0, updateInterval * 1000, TimeUnit.MILLISECONDS
        );

        return latch;
    }

    public void stopScheduling() {
        log.info("Filtering Database Reader STOPS scheduling");
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
        log.trace("Closing FilteringDbReader");
        stopScheduling();
        hikariDataSource.close();
    }

}
