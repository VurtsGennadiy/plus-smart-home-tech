package ru.yandex.practicum.commerce.interaction.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandlingConfig {
    @Bean
    public GlobalControllerAdvice globalExceptionHandler() {
        return new GlobalControllerAdvice();
    }
}
