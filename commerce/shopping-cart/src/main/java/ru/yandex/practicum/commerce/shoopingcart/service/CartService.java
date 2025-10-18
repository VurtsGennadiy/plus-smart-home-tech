package ru.yandex.practicum.commerce.shoopingcart.service;

import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    CartDto putProduct(String username, Map<UUID, Integer> productQuantityMap);

    CartDto getCart(String username);

    void deleteCart(String username);

    CartDto remove(String username, List<UUID> products);

    CartDto changeQuantity(String username, ChangeProductQuantityRequest request);
}
