package ru.yandex.practicum.commerce.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * Описание габаритов товара
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class DimensionDto {
    @Min(1)
    Double width;

    @Min(1)
    Double height;

    @Min(1)
    Double depth;
}
