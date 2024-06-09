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
import ru.mai.lessons.rpks.model.Deduplication;
import ru.mai.lessons.rpks.service.DeduplicationService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("deduplication")
@RequiredArgsConstructor
@Validated
@Tag(name = "Deduplication Controller", description = "API для работы с правилами дедубликации")
public class DeduplicationController {

    private final DeduplicationService deduplicationService;

    @GetMapping("/findAll")
    @Operation(
            summary = "Получить информацию о всех правилах дедубликации в БД",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правилах дедубликации успешно получена"
                    )
            }
    )
    public Iterable<Deduplication> getAllDeduplications() {
        return deduplicationService.getAllDeduplications();
    }

    @GetMapping("/findAll/{id}")
    @Operation(
            summary = "Получить информацию о всех правилах дедубликации в БД по deduplication id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правилах дедубликации успешно получена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный id дедубликации"
                    )
            }
    )
    public Iterable<Deduplication> getAllDeduplicationsByDeduplicationId(
            @PathVariable @Min(1) long id
    ) {
        return deduplicationService.getAllDeduplicationsByDeduplicationId(id);
    }

    @GetMapping("/find/{deduplicationId}/{ruleId}")
    @Operation(
            summary = "Получить информацию о правиле дедубликации по deduplication id и rule id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правиле дедубликации успешно получена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные id дедубликации и/или правила"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Правило дедубликации не найдено"
                    )
            }
    )
    public Deduplication getDeduplicationById(
            @PathVariable @Min(1) long deduplicationId,
            @PathVariable @Min(1) long ruleId
    ) {
        try {
            return deduplicationService.getDeduplicationByDeduplicationIdAndRuleId(deduplicationId, ruleId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Удалить информацию о всех правилах дедубликации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правилах дедубликации успешно удалена"
                    )
            }
    )
    public void deleteDeduplication() {
        deduplicationService.deleteDeduplication();
    }

    @Transactional
    @DeleteMapping("/delete/{deduplicationId}/{ruleId}")
    @Operation(
            summary = "Удалить информацию по конкретному правилу дедубликации с deduplication id и rule id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о правиле дедубликации успешно удалена"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные id дедубликации и/или правила"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Правило дедубликации не найдено"
                    )
            }
    )
    public void deleteDeduplicationById(
            @PathVariable @Min(1) long deduplicationId,
            @PathVariable @Min(1) long ruleId
    ) {
        try {
            deduplicationService.deleteDeduplicationById(deduplicationId, ruleId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Создать правило дедубликации",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Правило дедубликации успешно создано"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные правила дедубликации"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Правило дедубликации уже существует"
                    )
            }
    )
    public void save(
            @RequestBody @Valid Deduplication deduplication
    ) {
        try {
            deduplicationService.save(deduplication);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
