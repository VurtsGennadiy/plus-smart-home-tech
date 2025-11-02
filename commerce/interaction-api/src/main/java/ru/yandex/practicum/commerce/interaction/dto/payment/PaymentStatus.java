package ru.yandex.practicum.commerce.interaction.dto.payment;

public enum PaymentStatus {
    PENDING, // Ожидает оплаты
    SUCCESS, // Успешно оплачен
    FAILED // Ошибка в процессе оплаты
}
