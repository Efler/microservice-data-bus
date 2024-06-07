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
import ru.mai.lessons.rpks.model.Filter;
import ru.mai.lessons.rpks.service.FilterService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("filter")
@RequiredArgsConstructor
@Validated
@Tag(name = "Filter Controller", description = "API для работы с фильтрами")
public class FilterController {

    private final FilterService filterService;

    @GetMapping("/findAll")
    @Operation(
            summary = "Получить информацию о всех фильтрах в БД",
            responses = {
                    @ApiResponse
                            (responseCode = "200", description = "Информация о фильтрах успешно получена")
            }
    )
    public Iterable<Filter> getAllFilters() {
        return filterService.getAllFilters();
    }

    @GetMapping("/findAll/{id}")
    @Operation(
            summary = "Получить информацию о всех фильтрах в БД по filter id",
            responses = {
                    @ApiResponse
                            (responseCode = "200", description = "Информация о фильтрах успешно получена"),
                    @ApiResponse
                            (responseCode = "400", description = "Некорректный id фильтра")
            }
    )
    public Iterable<Filter> getAllFiltersByFilterId(
            @PathVariable @Min(1) long id
    ) {
        return filterService.getAllFiltersByFilterId(id);
    }

    @GetMapping("/find/{filterId}/{ruleId}")
    @Operation(
            summary = "Получить информацию о фильтре по filter id и rule id",
            responses = {
                    @ApiResponse
                            (responseCode = "200", description = "Информация о фильтре успешно получена"),
                    @ApiResponse
                            (responseCode = "400", description = "Некорректные id фильтра и/или правила"),
                    @ApiResponse
                            (responseCode = "404", description = "Фильтр не найден")
            }
    )
    public Filter getFilterByFilterIdAndRuleId(
            @PathVariable @Min(1) long filterId,
            @PathVariable @Min(1) long ruleId
    ) {
        try {
            return filterService.getFilterByFilterIdAndRuleId(filterId, ruleId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Удалить информацию о всех фильтрах",
            responses = {
                    @ApiResponse
                            (responseCode = "200", description = "Информация о фильтрах успешно удалена")
            }
    )
    public void deleteFilter() {
        filterService.deleteFilter();
    }

    @Transactional
    @DeleteMapping("/delete/{filterId}/{ruleId}")
    @Operation(
            summary = "Удалить информацию по конкретному фильтру filter id и rule id",
            responses = {
                    @ApiResponse
                            (responseCode = "200", description = "Информация о фильтре успешно удалена"),
                    @ApiResponse
                            (responseCode = "400", description = "Некорректные id фильтра и/или правила"),
                    @ApiResponse
                            (responseCode = "404", description = "Фильтр не найден")
            }
    )
    public void deleteFilterById(
            @PathVariable @Min(1) long filterId,
            @PathVariable @Min(1) long ruleId
    ) {
        try {
            filterService.deleteFilterById(filterId, ruleId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Создать фильтр",
            responses = {
                    @ApiResponse
                            (responseCode = "201", description = "Фильтр успешно создан"),
                    @ApiResponse
                            (responseCode = "400", description = "Некорректные данные фильтра"),
                    @ApiResponse
                            (responseCode = "409", description = "Фильтр уже существует")
            }
    )
    public void save(
            @RequestBody @Valid Filter filter
    ) {
        try {
            filterService.save(filter);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
