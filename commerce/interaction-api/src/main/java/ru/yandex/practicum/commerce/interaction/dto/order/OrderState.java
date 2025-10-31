package ru.yandex.practicum.commerce.interaction.dto.order;

public enum OrderState {
    NEW, // Новый
    ON_PAYMENT, // Ожидает оплаты
    PAID, // Оплачен
    PAYMENT_FAILED, // Неудачная оплата
    ON_ASSEMBLY, // Собирается
    ASSEMBLED, // Собран
    ASSEMBLY_FAILED, // Неудачная сборка
    DELIVERY_CREATED, // Создана доставка (товары ещё не передали)
    ON_DELIVERY, // Заказ передан в доставку
    DELIVERED, // Доставлен
    DELIVERY_FAILED, // Неудачная доставка
    DONE, // Выполнен
    COMPLETED, // Завершён
    PRODUCT_RETURNED, // Возврат товаров
    CANCELED // Отменён
}
