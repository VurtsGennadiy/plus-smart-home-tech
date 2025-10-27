package ru.yandex.practicum.commerce.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;

import java.math.BigDecimal;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto payment(OrderDto order);

    @PostMapping("/productCost")
    BigDecimal calculateProductsCost(OrderDto order);

    @PostMapping("/totalCost")
    BigDecimal calculateTotalCost(OrderDto order);
}
