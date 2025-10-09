package ru.yandex.practicum.commerce.interaction.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для передачи пагинированных данных с поддержкой сортировки.
 * Используется в REST-контроллерах вместо прямого возврата {@link Page},
 * чтобы избежать зависимости от внутренней структуры {@code PageImpl}
 * и обеспечить предсказуемый JSON-формат с явной информацией о сортировке.
 */
@Getter
@EqualsAndHashCode
public class PageDto<T> {
    private final List<T> content;
    private final List<Sort.Order> sort;
    private final PageMetadata metadata;

    public PageDto(Page<T> page) {
        content = page.getContent();
        metadata = new PageMetadata(page);
        sort = new ArrayList<>();
        for (Sort.Order order : page.getSort()) {
            sort.add(order);
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    private static class PageMetadata {
        private final long size;
        private final long number;
        private final long totalElements;
        private final long totalPages;

        public PageMetadata(Page<?> page) {
            size = page.getSize();
            number = page.getNumber();
            totalElements = page.getTotalElements();
            totalPages = page.getTotalPages();
        }
    }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (!content.isEmpty() && content.get(0) != null) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances",
                metadata.getNumber() + 1, metadata.getTotalPages(), contentType);
    }
}
