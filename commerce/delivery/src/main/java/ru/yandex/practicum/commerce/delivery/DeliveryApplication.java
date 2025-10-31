package ru.yandex.practicum.commerce.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.interaction.exception.ExceptionHandlingConfig;
import ru.yandex.practicum.commerce.interaction.logging.LoggingConfig;

@Import({ExceptionHandlingConfig.class, LoggingConfig.class})
@EnableFeignClients("ru.yandex.practicum.commerce.interaction")
@SpringBootApplication
public class DeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }
}
