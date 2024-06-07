package ru.mai.lessons.rpks.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.mai.lessons.rpks.service.EnrichmentService;

@Component
@RequiredArgsConstructor
public class EnrichmentMetrics implements InfoContributor {

    private final EnrichmentService enrichmentService;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("countEnrichments", enrichmentService.countEnrichmentRules());
    }

}
