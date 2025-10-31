package ru.yandex.practicum.commerce.delivery.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.interaction.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction.dto.delivery.CalculateDeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveryFacade {
    private final DeliveryService deliveryService;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    /**
     * Создать новую доставку
     */
    public DeliveryDto createDelivery(DeliveryDto dto) {
        return deliveryService.createDelivery(dto);
    }

    /**
     * Рассчитать цену доставки
     */
    public BigDecimal calculateDeliveryPrice(CalculateDeliveryDto dto) {
        return deliveryService.calculateDeliveryPrice(dto);
    }

    /**
     * Обработка успешной доставки
     */
    public void handleDeliverySuccess(UUID deliveryId) {
        DeliveryDto delivery = deliveryService.setDeliverySuccess(deliveryId);
        orderClient.deliverySuccess(delivery.getOrderId());
    }

    /**
     * Обработка неудачной доставки
     */
    public void handleDeliveryFailed(UUID deliveryId) {
        DeliveryDto delivery = deliveryService.setDeliveryFailed(deliveryId);
        orderClient.deliveryFailed(delivery.getOrderId());
    }

    /**
     * Товары получены в доставку
     */
    public void deliveryPicked(UUID deliveryId) {
        DeliveryDto delivery = deliveryService.pickDelivery(deliveryId);
        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(delivery.getOrderId(), delivery.getDeliveryId()));
        orderClient.deliveryShipped(delivery.getOrderId());
    }
}
