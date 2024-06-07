package ru.mai.lessons.rpks.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lessons.rpks.model.Filter;
import ru.mai.lessons.rpks.repository.FilterRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterService {

    private final FilterRepository filterRepository;

    public List<Filter> getAllFilters() {
        log.debug("Getting all filters");
        return filterRepository.findAll();
    }

    public List<Filter> getAllFiltersByFilterId(long id) {
        log.debug("Getting all filters by filter id");
        return filterRepository.findAllByFilterId(id);
    }

    public Filter getFilterByFilterIdAndRuleId(long filterId, long ruleId) {
        log.debug("Getting filter by filter id and rule id");
        if (!filterRepository.existsByFilterIdAndRuleId(filterId, ruleId)) {
            log.warn("Filter with filterId {} and ruleId {} for getting not found", filterId, ruleId);
            throw new NoSuchElementException(
                    String.format("Filter with filterId %d and ruleId %d for getting not found",
                            filterId, ruleId)
            );
        }
        return filterRepository.findFilterByFilterIdAndRuleId(filterId, ruleId);
    }

    public void deleteFilter() {
        log.debug("Deleting all filters");
        filterRepository.deleteAll();
    }

    public void deleteFilterById(long filterId, long ruleId) {
        log.debug("Deleting filter by filter id and rule id");
        if (!filterRepository.existsByFilterIdAndRuleId(filterId, ruleId)) {
            log.warn("Filter with filterId {} and ruleId {} for deletion not found", filterId, ruleId);
            throw new NoSuchElementException(
                    String.format("Filter with filterId %d and ruleId %d for deletion not found",
                            filterId, ruleId)
            );
        }
        filterRepository.deleteFilterByFilterIdAndRuleId(filterId, ruleId);
    }

    public void save(Filter filter) {
        log.debug("Saving filter");
        if (filterRepository.existsByFilterIdAndRuleId(filter.getFilterId(), filter.getRuleId())) {
            log.warn("Filter with filterId {} and ruleId {} already exists",
                    filter.getFilterId(), filter.getRuleId()
            );
            throw new IllegalArgumentException(
                    String.format("Filter with filterId %d and ruleId %d already exists",
                            filter.getFilterId(), filter.getRuleId()
                    )
            );
        }
        filterRepository.save(filter);
    }

    public long countFilterRules() {
        return filterRepository.count();
    }

}
