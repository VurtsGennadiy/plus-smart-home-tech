package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction.client.PaymentClient;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.facade.PaymentFacade;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {
    private final PaymentFacade paymentFacade;

    /**
     * Сохранение информации о рассчитанной оплате в базу.
     * Метод вызывается из сервиса Order.
     */
    @Override
    @PostMapping
    public PaymentDto createPayment(@RequestBody OrderDto order) {
        return paymentFacade.createPayment(order);
    }

    /**
     * Расчёт стоимости товаров в заказе
     * Метод вызывается из сервиса Order.
     */
    @Override
    @PostMapping("/productCost")
    public BigDecimal calculateProductsCost(@RequestBody OrderDto order) {
        return paymentFacade.calculateProductsCost(order);
    }

    /**
     * Расчёт полной стоимости заказа (товары + доставка + налог)
     * Метод вызывается из сервиса Order
     */
    @Override
    @PostMapping("/totalCost")
    public BigDecimal calculateTotalCost(@RequestBody OrderDto order) {
        return paymentFacade.calculateTotalCost(order);
    }

    /**
     * Эмуляция успешной оплаты.
     * Метод вызывается из платежного шлюза.
     */
    @Override
    @PostMapping("/refund")
    public void paymentSuccess(UUID paymentId) {
        paymentFacade.handlePaymentSuccess(paymentId);
    }

    /**
     * Эмуляция неудачной оплаты.
     * Метод вызывается из платежного шлюза.
     */
    @Override
    @PostMapping("/failed")
    public void paymentFailed(@RequestBody UUID paymentId) {
        paymentFacade.handlePaymentFailed(paymentId);
    }
}
