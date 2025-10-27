package ru.yandex.practicum.commerce.order.service;

import ru.yandex.practicum.commerce.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto createNewOrder(CreateNewOrderRequest request, String username);

    List<OrderDto> getUserOrders(String username);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto calculateProductsPrice(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto paymentInit(UUID orderId);

    OrderDto paymentSuccess(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto assemblyInit(UUID orderId);

    OrderDto assemblySuccess(UUID orderId);

    OrderDto assemblyFailed(UUID orderId);

    OrderDto deliveryInit(UUID orderId);

    OrderDto deliverySuccess(UUID orderId);

    OrderDto deliveryFailed(UUID orderId);

    OrderDto returnProducts(ProductReturnRequest request);

    OrderDto orderCompleted(UUID orderId);
}
