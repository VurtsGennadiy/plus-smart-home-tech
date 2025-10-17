package ru.yandex.practicum.commerce.interaction.dto.warehouse;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class AddProductToWarehouseRequest {
    UUID productId;

    @Positive
    Integer quantity;
}
