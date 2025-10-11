package ru.yandex.practicum.commerce.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Описание нового товара для обработки складом.
 * Используется в запросе на добавление нового товара на склад
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    UUID productId;

    /**
     * Признак хрупкости
     */
    boolean fragile;

    DimensionDto dimension;

    @Min(1)
    Double weight;
}
