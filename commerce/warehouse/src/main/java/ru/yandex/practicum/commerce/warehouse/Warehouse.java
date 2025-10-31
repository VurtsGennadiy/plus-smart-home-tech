package ru.yandex.practicum.commerce.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.interaction.exception.ExceptionHandlingConfig;
import ru.yandex.practicum.commerce.interaction.logging.LoggingConfig;

@SpringBootApplication
@Import({ExceptionHandlingConfig.class, LoggingConfig.class})
@EnableFeignClients("ru.yandex.practicum.commerce.interaction")
public class Warehouse {
    public static void main(String[] args) {
        SpringApplication.run(Warehouse.class, args);
    }
}
