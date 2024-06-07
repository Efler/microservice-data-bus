package ru.mai.lessons.rpks.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.mai.lessons.rpks.service.DeduplicationService;

@Component
@RequiredArgsConstructor
public class DeduplicationMetrics implements InfoContributor {

    private final DeduplicationService deduplicationService;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("countDeduplications", deduplicationService.countDeduplicationRules());
    }

}
