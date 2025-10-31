package ru.yandex.practicum.commerce.interaction.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;

/**
 * DTO запроса на создание заказа
 */

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class CreateNewOrderRequest {
    @NotNull
    CartDto shoppingCart;

    @NotNull
    AddressDto deliveryAddress;
}
