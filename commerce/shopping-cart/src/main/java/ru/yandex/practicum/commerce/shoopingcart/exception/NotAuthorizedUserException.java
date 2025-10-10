package ru.yandex.practicum.commerce.shoopingcart.exception;

/**
 * Исключение, которое возникает при попытке выполнить операцию без надлежащей авторизации пользователя.
 */
public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
