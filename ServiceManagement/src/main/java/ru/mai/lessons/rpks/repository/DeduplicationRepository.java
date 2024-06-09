package ru.mai.lessons.rpks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mai.lessons.rpks.model.Deduplication;

import java.util.List;

@Repository
public interface DeduplicationRepository extends JpaRepository<Deduplication, Long> {

    List<Deduplication> findAllByDeduplicationId(long id);

    Deduplication findDeduplicationByDeduplicationIdAndRuleId(long deduplicationId, long ruleId);

    void deleteDeduplicationByDeduplicationIdAndRuleId(long deduplicationId, long ruleId);

    boolean existsByDeduplicationIdAndRuleId(long deduplicationId, long ruleId);

}
