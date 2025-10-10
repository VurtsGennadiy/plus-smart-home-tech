package ru.yandex.practicum.commerce.interaction.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * DTO класс для изменения количества товара в корзине.
 * Используется для запроса на обновление количества конкретного товара.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProductQuantityRequest {
    @NotNull
    UUID productId;

    @Min(1)
    Integer newQuantity;
}
