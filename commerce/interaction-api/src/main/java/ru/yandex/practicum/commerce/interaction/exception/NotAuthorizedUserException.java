package ru.yandex.practicum.commerce.interaction.exception;

/**
 * Исключение, которое возникает при попытке выполнить операцию без надлежащей авторизации пользователя.
 */
public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
