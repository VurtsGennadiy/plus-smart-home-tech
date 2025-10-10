package ru.yandex.practicum.commerce.interaction.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ErrorResponse {
    Throwable cause;
    StackTraceElement[] stackTrace;
    Throwable[] suppressed;
    String message;
    String userMessage;
    String localizedMessage;
    int httpStatus;

    public ErrorResponse(Exception exception, int httpStatus, String userMessage) {
        cause = exception.getCause();
        stackTrace = exception.getStackTrace();
        suppressed = exception.getSuppressed();
        message = exception.getMessage();
        localizedMessage = exception.getLocalizedMessage();
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;
    }
}
