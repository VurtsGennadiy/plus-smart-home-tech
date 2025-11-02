package ru.yandex.practicum.commerce.payment.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.interaction.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.client.ShoppingStoreClient;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Специальный слой приложения, необходимый для того, чтобы отделить вызовы внешних сервисов от сервисного слоя.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    /**
     * Сохранение информации о рассчитанной оплате для заказа в базу
     */
    public PaymentDto createPayment(OrderDto order) {
        return paymentService.createPayment(order);
    }

    /**
     * Расчёт полной стоимости заказа (товары + доставка + налог)
     */
    public BigDecimal calculateTotalCost(OrderDto order) {
        log.info("Калькуляция итоговой стоимости для заказа {}", order.getOrderId());

        BigDecimal total = paymentService.calculateTotalCost(order);
        BigDecimal fee = total.subtract(order.getProductPrice()).subtract(order.getDeliveryPrice());

        log.info("Итоговая стоимость для заказа {} составляет {}, из них товары = {}, доставка = {}, налог = {}",
                order.getOrderId(), total, order.getProductPrice(), order.getDeliveryPrice(), fee);
        return paymentService.calculateTotalCost(order);
    }

    /**
     * Расчёт стоимости товаров в заказе
     * Для получения цены каждого товара обращаемся к сервису shopping-store
     */
    public BigDecimal calculateProductsCost(OrderDto order) {
        log.info("Калькуляция стоимости товаров для заказа {}, корзина товаров: {}",
                order.getOrderId(), order.getProducts());

        Set<UUID> productIds = order.getProducts().keySet();
        Map<UUID, BigDecimal> productsCost = shoppingStoreClient.getProductsCost(productIds);
        BigDecimal total = paymentService.calculateProductsCost(order, productsCost);

        log.info("Итоговая стоимость товаров для заказа {} составит {}", order.getOrderId(), total);
        return total;
    }

    /**
     * Обработка успешной оплаты
     */
    public void handlePaymentSuccess(UUID paymentId) {
        PaymentDto paymentDto = paymentService.setPaymentSuccess(paymentId);
        orderClient.paymentSuccess(paymentDto.getOrderId());
    }

    /**
     * Обработка неудачной оплаты
     */
    public void handlePaymentFailed(UUID paymentId) {
        PaymentDto paymentDto = paymentService.setPaymentFailed(paymentId);
        orderClient.paymentFail(paymentDto.getOrderId());
    }
}
