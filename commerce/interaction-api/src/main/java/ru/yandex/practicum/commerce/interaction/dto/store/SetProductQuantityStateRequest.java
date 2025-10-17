package ru.yandex.practicum.commerce.interaction.dto.store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Запрос на изменение статуса остатка товара
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class SetProductQuantityStateRequest {
    UUID productId;
    QuantityState quantityState;
}
