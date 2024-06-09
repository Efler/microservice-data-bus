package ru.mai.lessons.rpks.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import ru.mai.lessons.rpks.service.FilterService;

@Component
@RequiredArgsConstructor
public class FilterMetrics implements InfoContributor {

    private final FilterService filterService;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("countFilters", filterService.countFilterRules());
    }

}
