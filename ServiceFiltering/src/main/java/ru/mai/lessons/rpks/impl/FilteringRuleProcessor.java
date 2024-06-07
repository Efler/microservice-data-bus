package ru.mai.lessons.rpks.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.mai.lessons.rpks.RuleProcessor;
import ru.mai.lessons.rpks.model.Message;
import ru.mai.lessons.rpks.model.Rule;

@Slf4j
public class FilteringRuleProcessor implements RuleProcessor {

    public static final String FILTER_FUNCTION_EQUALS = "equals";
    public static final String FILTER_FUNCTION_NOT_EQUALS = "not_equals";
    public static final String FILTER_FUNCTION_CONTAINS = "contains";
    public static final String FILTER_FUNCTION_NOT_CONTAINS = "not_contains";
    private final ObjectMapper mapper = new ObjectMapper();

    private boolean filterEquals(Rule expected, JsonNode actual) {
        if (!actual.hasNonNull(expected.getFieldName())) {
            return false;
        }
        return actual.get(expected.getFieldName()).asText().equals(expected.getFilterValue());
    }

    private boolean filterNotEquals(Rule expected, JsonNode actual) {
        if (!actual.hasNonNull(expected.getFieldName())) {
            return false;
        }
        return !actual.get(expected.getFieldName()).asText().equals(expected.getFilterValue());
    }

    private boolean filterContains(Rule expected, JsonNode actual) {
        if (!actual.hasNonNull(expected.getFieldName())) {
            return false;
        }
        return actual.get(expected.getFieldName()).asText().contains(expected.getFilterValue());
    }

    private boolean filterNotContains(Rule expected, JsonNode actual) {
        if (!actual.hasNonNull(expected.getFieldName())) {
            return false;
        }
        return !actual.get(expected.getFieldName()).asText().contains(expected.getFilterValue());
    }

    private String getLogsFilter(Rule rule) {
        return String.format("Filtering by %s: %s -> %s",
                rule.getFieldName(), rule.getFilterValue(), rule.getFilterFunctionName().toUpperCase()
        );
    }

    private String getLogsConclusion(Rule rule, JsonNode rootNode, boolean isValid) {
        return String.format("expecting [%s] <-?-> actual [%s]   ===> %s",
                rule.getFilterValue(),
                rootNode.hasNonNull(rule.getFieldName())
                        ? rootNode.get(rule.getFieldName()).asText()
                        : "<null>",
                isValid ? "SUCCESS" : "FAIL"
        );
    }

    @Override
    public Message processing(Message message, Rule[] rules) {
        if (message == null || rules == null) {
            throw new IllegalArgumentException("Message/rules must be not null");
        }
        log.debug("Filtering message: {}", message.getValue());

        try {
            JsonNode rootNode = mapper.readTree(message.getValue());

            for (Rule rule : rules) {
                boolean isValid;
                String lastFilter;
                switch (rule.getFilterFunctionName()) {
                    case FILTER_FUNCTION_EQUALS -> {
                        isValid = filterEquals(rule, rootNode);
                        lastFilter = FILTER_FUNCTION_EQUALS;
                    }
                    case FILTER_FUNCTION_NOT_EQUALS -> {
                        isValid = filterNotEquals(rule, rootNode);
                        lastFilter = FILTER_FUNCTION_NOT_EQUALS;
                    }
                    case FILTER_FUNCTION_CONTAINS -> {
                        isValid = filterContains(rule, rootNode);
                        lastFilter = FILTER_FUNCTION_CONTAINS;
                    }
                    case FILTER_FUNCTION_NOT_CONTAINS -> {
                        isValid = filterNotContains(rule, rootNode);
                        lastFilter = FILTER_FUNCTION_NOT_CONTAINS;
                    }
                    default -> {
                        log.warn("Unknown filter function: {}", rule.getFilterFunctionName());
                        isValid = false;
                        lastFilter = null;
                    }
                }
                message.setFilterState(isValid);
                if (lastFilter != null) {
                    log.debug(getLogsFilter(rule));
                    log.debug(getLogsConclusion(rule, rootNode, isValid));
                }
                if (!isValid) {
                    return message;
                }
            }

            return message;

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON-string in Message, exception message: {}", message.getValue());
            message.setFilterState(false);
            return message;
        }
    }

}
