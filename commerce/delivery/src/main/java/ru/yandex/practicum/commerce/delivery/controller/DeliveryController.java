package ru.yandex.practicum.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.delivery.facade.DeliveryFacade;
import ru.yandex.practicum.commerce.interaction.client.DeliveryClient;
import ru.yandex.practicum.commerce.interaction.dto.delivery.CalculateDeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {
    private final DeliveryFacade facade;

    /**
     * Создание новой доставки
     */
    @Override
    public DeliveryDto createDelivery(DeliveryDto dto) {
        return facade.createDelivery(dto);
    }

    /**
     * Расчёт цены доставки
     */
    @Override
    @PostMapping("/cost")
    public BigDecimal calculateDeliveryPrice(@RequestBody CalculateDeliveryDto dto) {
        return facade.calculateDeliveryPrice(dto);
    }

    /**
     * Успешная доставка
     */
    @Override
    @PostMapping("/successful")
    public void deliverySuccess(@RequestBody UUID deliveryId) {
        facade.handleDeliverySuccess(deliveryId);
    }

    /**
     * Неудачная доставка
     */
    @Override
    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody UUID deliveryId) {
        facade.handleDeliveryFailed(deliveryId);
    }

    /**
     * Принять товар в доставку
     */
    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody UUID deliveryId) {
        facade.deliveryPicked(deliveryId);
    }
}
