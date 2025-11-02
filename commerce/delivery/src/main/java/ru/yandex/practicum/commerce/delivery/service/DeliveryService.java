package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.interaction.dto.delivery.CalculateDeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto dto);

    BigDecimal calculateDeliveryPrice(CalculateDeliveryDto dto);

    DeliveryDto setDeliverySuccess(UUID deliveryId);

    DeliveryDto setDeliveryFailed(UUID deliveryId);

    DeliveryDto pickDelivery(UUID deliveryId);
}
