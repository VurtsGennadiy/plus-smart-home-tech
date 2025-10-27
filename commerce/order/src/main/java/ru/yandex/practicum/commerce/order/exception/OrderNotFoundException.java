package ru.yandex.practicum.commerce.order.exception;

/**
 * Исключение, возникающее при попытке найти несуществующий заказ
 */

public class OrderNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Order %s not found";
    private final String orderId;

    public String getOrderId() {
        return orderId;
    }

    public OrderNotFoundException(String orderId) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, orderId));
        this.orderId = orderId;
    }

    public OrderNotFoundException(String orderId, String message) {
        super(message);
        this.orderId = orderId;
    }

    public OrderNotFoundException(String orderId, String message, Throwable cause) {
        super(message, cause);
        this.orderId = orderId;
    }
}
