package ru.mai.lessons.rpks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mai.lessons.rpks.model.Filter;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {

    List<Filter> findAllByFilterId(long id);

    Filter findFilterByFilterIdAndRuleId(long filterId, long ruleId);

    void deleteFilterByFilterIdAndRuleId(long filterId, long ruleId);

    boolean existsByFilterIdAndRuleId(long filterId, long ruleId);

}
