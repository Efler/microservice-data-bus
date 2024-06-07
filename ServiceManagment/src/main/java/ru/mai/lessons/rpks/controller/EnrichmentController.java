package ru.mai.lessons.rpks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mai.lessons.rpks.model.Enrichment;
import ru.mai.lessons.rpks.service.EnrichmentService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("enrichment")
@RequiredArgsConstructor
@Validated
@Tag(name = "Enrichment Controller", description = "API для работы с правилами обогащения")
public class EnrichmentController {

    private final EnrichmentService enrichmentService;

    @GetMapping("/findAll")
    @Operation(
            summary = "Получить информацию о всех правилах обогащения в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правилах обогащения успешно получена"
                    )
            }
    )
    public Iterable<Enrichment> getAllEnrichments() {
        return enrichmentService.getAllEnrichments();
    }

    @GetMapping("/findAll/{id}")
    @Operation(
            summary = "Получить информацию о всех правилах обогащения в БД по enrichment id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правилах обогащения успешно получена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный id обогащения"
                    )
            }
    )
    public Iterable<Enrichment> getAllEnrichmentsByEnrichmentId(
            @PathVariable @Min(1) long id
    ) {
        return enrichmentService.getAllEnrichmentsByEnrichmentId(id);
    }

    @GetMapping("/find/{enrichmentId}/{ruleId}")
    @Operation(
            summary = "Получить информацию о правиле обогащения по enrichment id и rule id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правиле обогащения успешно получена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные id обогащения и/или правила"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Правило обогащения не найдено"
                    )
            }
    )
    public Enrichment getEnrichmentById(
            @PathVariable @Min(1) long enrichmentId,
            @PathVariable @Min(1) long ruleId
    ) {
        try {
            return enrichmentService.getEnrichmentByEnrichmentIdAndRuleId(enrichmentId, ruleId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Удалить информацию о всех правилах обогащения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правилах обогащения успешно удалена"
                    )
            }
    )
    public void deleteEnrichment() {
        enrichmentService.deleteEnrichment();
    }

    @Transactional
    @DeleteMapping("/delete/{enrichmentId}/{ruleId}")
    @Operation(
            summary = "Удалить информацию по конкретному правилу обогащения с enrichment id и rule id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правиле обогащения успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные id обогащения и/или правила"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Правило обогащения не найдено"
                    )
            }
    )
    public void deleteEnrichmentById(
            @PathVariable @Min(1) long enrichmentId,
            @PathVariable @Min(1) long ruleId
    ) {
        try {
            enrichmentService.deleteEnrichmentById(enrichmentId, ruleId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Создать правило обогащения",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Правило обогащения успешно создано"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные правила обогащения"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Правило обогащения уже существует"
                    )
            }
    )
    public void save(
            @RequestBody @Valid Enrichment enrichment
    ) {
        try {
            enrichmentService.save(enrichment);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
