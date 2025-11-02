package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentStatus;
import ru.yandex.practicum.commerce.interaction.exception.EntityNotFoundException;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;
import ru.yandex.practicum.commerce.payment.dal.Payment;
import ru.yandex.practicum.commerce.payment.dal.PaymentMapper;
import ru.yandex.practicum.commerce.payment.dal.PaymentRepository;
import ru.yandex.practicum.commerce.payment.exception.MissingProductCostException;
import ru.yandex.practicum.commerce.payment.exception.NotEnoughInfoInOrderToCalculateException;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    /**
     * Сохранение информации о рассчитанной оплате для заказа в базу.
     * Необходимо, чтобы в заказе уже были рассчитаны стоимость товаров и доставки, иначе будет выброшено исключение
     * {@link NotEnoughInfoInOrderToCalculateException}
     */
    @Override
    @Loggable
    @Transactional
    public PaymentDto createPayment(OrderDto order) {
        validateOrderPrices(order);
        Payment payment = paymentMapper.fromOrder(order);
        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Расчёт суммарной стоимости товаров в заказе.
     * В случае, если не будет найдена цена для всех товаров в заказе, то будет выброшено исключение {@link MissingProductCostException},
     * содержащее список Id товаров, для которых не найдена цена.
     */
    @Override
    @Loggable
    public BigDecimal calculateProductsCost(OrderDto order, Map<UUID, BigDecimal> productsCosts) {
        Set<UUID> productIds = order.getProducts().keySet();
        List<String> missingCosts = new ArrayList<>();

        BigDecimal totalProductsCost = BigDecimal.ZERO;
        for (UUID productId : productIds) {
            BigDecimal productCost = productsCosts.get(productId);
            if (productCost == null) {
                missingCosts.add(productId.toString());
            } else {
                BigDecimal productPrice = productCost.multiply(BigDecimal.valueOf(order.getProducts().get(productId)));
                totalProductsCost = totalProductsCost.add(productPrice);
            }
        }

        if (!missingCosts.isEmpty()) {
            throw new MissingProductCostException(missingCosts);
        }

        return totalProductsCost;
    }

    /**
     * Расчёт полной стоимости заказа.
     * Для расчёта необходимо, чтобы в заказе уже были рассчитаны стоимость товаров и доставки, иначе будет выброшено исключение
     * {@link NotEnoughInfoInOrderToCalculateException}
     */
    @Override
    @Loggable
    public BigDecimal calculateTotalCost(OrderDto order) {
        validateOrderPrices(order);
        BigDecimal VAT = order.getProductPrice().multiply(BigDecimal.valueOf(0.1)); // НДС 10%
        return order.getProductPrice().add(order.getDeliveryPrice()).add(VAT);
    }

    /**
     * Установить статус успешной оплаты для платежа
     */
    @Override
    @Loggable
    @Transactional
    public PaymentDto setPaymentSuccess(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Установить статус неуспешной оплаты для платежа
     */
    @Override
    @Loggable
    @Transactional
    public PaymentDto setPaymentFailed(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    private void validateOrderPrices(OrderDto order) {
        if (order.getProductPrice() == null && order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(true, true);
        } else if (order.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(true, false);
        } else if (order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(false, true);
        }
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment", paymentId.toString()));
    }
}
