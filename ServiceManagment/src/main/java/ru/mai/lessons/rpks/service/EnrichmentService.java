package ru.mai.lessons.rpks.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mai.lessons.rpks.model.Enrichment;
import ru.mai.lessons.rpks.repository.EnrichmentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrichmentService {

    private final EnrichmentRepository enrichmentRepository;

    public List<Enrichment> getAllEnrichments() {
        log.debug("Getting all enrichments");
        return enrichmentRepository.findAll();
    }

    public List<Enrichment> getAllEnrichmentsByEnrichmentId(long id) {
        log.debug("Getting all enrichments by enrichment id");
        return enrichmentRepository.findAllByEnrichmentId(id);
    }

    public Enrichment getEnrichmentByEnrichmentIdAndRuleId(long enrichmentId, long ruleId) {
        log.debug("Getting enrichment by enrichment id and rule id");
        if (!enrichmentRepository.existsByEnrichmentIdAndRuleId(enrichmentId, ruleId)) {
            log.warn(
                    "Enrichment with enrichmentId {} and ruleId {} for getting not found",
                    enrichmentId, ruleId
            );
            throw new NoSuchElementException(
                    String.format("Enrichment with enrichmentId %d and ruleId %d for getting not found",
                            enrichmentId, ruleId)
            );
        }
        return enrichmentRepository.findEnrichmentByEnrichmentIdAndRuleId(enrichmentId, ruleId);
    }

    public void deleteEnrichment() {
        log.debug("Deleting all enrichments");
        enrichmentRepository.deleteAll();
    }

    public void deleteEnrichmentById(long enrichmentId, long ruleId) {
        log.debug("Deleting enrichment by enrichment id and rule id");
        if (!enrichmentRepository.existsByEnrichmentIdAndRuleId(enrichmentId, ruleId)) {
            log.warn(
                    "Enrichment with enrichmentId {} and ruleId {} for deletion not found",
                    enrichmentId, ruleId
            );
            throw new NoSuchElementException(
                    String.format("Enrichment with enrichmentId %d and ruleId %d for deletion not found",
                            enrichmentId, ruleId)
            );
        }
        enrichmentRepository.deleteEnrichmentByEnrichmentIdAndRuleId(enrichmentId, ruleId);
    }

    public void save(Enrichment enrichment) {
        log.debug("Saving enrichment");
        if (enrichmentRepository.existsByEnrichmentIdAndRuleId(
                enrichment.getEnrichmentId(), enrichment.getRuleId()
        )) {
            log.warn(
                    "Enrichment with enrichmentId {} and ruleId {} already exists",
                    enrichment.getEnrichmentId(), enrichment.getRuleId()
            );
            throw new IllegalArgumentException(
                    String.format("Enrichment with enrichmentId %d and ruleId %d already exists",
                            enrichment.getEnrichmentId(), enrichment.getRuleId())
            );
        }
        enrichmentRepository.save(enrichment);
    }

    public long countEnrichmentRules() {
        return enrichmentRepository.count();
    }

}
