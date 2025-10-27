package ru.yandex.practicum.commerce.interaction.dto.delivery;

/**
 * Статус доставки
 */
public enum DeliveryState {
    CREATED,
    IN_PROGRESS,
    DELIVERED,
    FAILED,
    CANCELED
}
