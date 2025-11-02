package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto order);

    BigDecimal calculateProductsCost(OrderDto order, Map<UUID, BigDecimal> productsCost);

    BigDecimal calculateTotalCost(OrderDto order);

    PaymentDto setPaymentSuccess(UUID paymentId);

    PaymentDto setPaymentFailed(UUID paymentId);
}
