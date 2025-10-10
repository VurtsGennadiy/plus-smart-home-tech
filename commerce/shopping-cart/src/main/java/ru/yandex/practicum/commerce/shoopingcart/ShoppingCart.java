package ru.yandex.practicum.commerce.shoopingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.interaction.exception.ExceptionHandlingConfig;

@SpringBootApplication
@Import(ExceptionHandlingConfig.class)
@EnableFeignClients
public class ShoppingCart {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCart.class);
    }
}
