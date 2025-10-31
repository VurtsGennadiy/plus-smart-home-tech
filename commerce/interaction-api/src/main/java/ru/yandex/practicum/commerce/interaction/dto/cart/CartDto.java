package ru.yandex.practicum.commerce.interaction.dto.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

/**
 * DTO класс для представления корзины покупок.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
public class CartDto {
    UUID shoppingCartId;

    /**
     * Отображение идентификатора продукта на количество единиц продукта в корзине.
     */
    Map<UUID, Integer> products;
}
