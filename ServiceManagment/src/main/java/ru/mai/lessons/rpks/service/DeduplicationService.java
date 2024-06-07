package ru.mai.lessons.rpks.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lessons.rpks.model.Deduplication;
import ru.mai.lessons.rpks.repository.DeduplicationRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeduplicationService {

    private final DeduplicationRepository deduplicationRepository;

    public List<Deduplication> getAllDeduplications() {
        log.debug("Getting all deduplications");
        return deduplicationRepository.findAll();
    }

    public List<Deduplication> getAllDeduplicationsByDeduplicationId(long id) {
        log.debug("Getting all deduplications by deduplication id");
        return deduplicationRepository.findAllByDeduplicationId(id);
    }

    public Deduplication getDeduplicationByDeduplicationIdAndRuleId(
            long deduplicationId, long ruleId
    ) {
        log.debug("Getting deduplication by deduplication id and rule id");
        if (!deduplicationRepository.existsByDeduplicationIdAndRuleId(deduplicationId, ruleId)) {
            log.warn(
                    "Deduplication with deduplicationId {} and ruleId {} for getting not found",
                    deduplicationId, ruleId
            );
            throw new NoSuchElementException(
                    String.format("Deduplication with deduplicationId %d and ruleId %d for getting not found",
                            deduplicationId, ruleId)
            );
        }
        return deduplicationRepository.findDeduplicationByDeduplicationIdAndRuleId(deduplicationId, ruleId);
    }

    public void deleteDeduplication() {
        log.debug("Deleting all deduplications");
        deduplicationRepository.deleteAll();
    }

    public void deleteDeduplicationById(long deduplicationId, long ruleId) {
        log.debug("Deleting deduplication by deduplication id and rule id");
        if (!deduplicationRepository.existsByDeduplicationIdAndRuleId(deduplicationId, ruleId)) {
            log.warn(
                    "Deduplication with deduplicationId {} and ruleId {} for deletion not found",
                    deduplicationId, ruleId
            );
            throw new NoSuchElementException(
                    String.format("Deduplication with deduplicationId %d and ruleId %d for deletion not found",
                            deduplicationId, ruleId)
            );
        }
        deduplicationRepository.deleteDeduplicationByDeduplicationIdAndRuleId(deduplicationId, ruleId);
    }

    public void save(Deduplication deduplication) {
        log.debug("Saving deduplication");
        if (deduplicationRepository.existsByDeduplicationIdAndRuleId(
                deduplication.getDeduplicationId(), deduplication.getRuleId()
        )) {
            log.warn("Deduplication with deduplicationId {} and ruleId {} already exists",
                    deduplication.getDeduplicationId(), deduplication.getRuleId()
            );
            throw new IllegalArgumentException(
                    String.format("Deduplication with deduplicationId %d and ruleId %d already exists",
                            deduplication.getDeduplicationId(), deduplication.getRuleId())
            );
        }
        deduplicationRepository.save(deduplication);
    }

    public long countDeduplicationRules() {
        return deduplicationRepository.count();
    }

}
