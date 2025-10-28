package ru.yandex.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {
    private final OrderService orderService;
    private final static String USERNAME_HEADER = "X-Auth-User";

    /**
     * Создание нового заказа
     */
    @Override
    @PutMapping
    public OrderDto createNewOrder(@RequestHeader(USERNAME_HEADER) String username,
                                   @Valid @RequestBody CreateNewOrderRequest request) {
        return orderService.createNewOrder(request, username);
    }

    /**
     * Получение списка всех заказов пользователя
     */
    @Override
    @GetMapping
    public List<OrderDto> getUserOrders(@RequestHeader(USERNAME_HEADER) String username) {
        return orderService.getUserOrders(username);
    }

    /**
     * Расчёт стоимости доставки
     * Обращение к сервису delivery: POST delivery/cost
     */
    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody String orderId) {
        UUID uuid = UUID.fromString(orderId.replace("\"", ""));
        return orderService.calculateDeliveryPrice(uuid);
    }


    /**
     * Расчёт стоимости товаров. Этого метода нет в описании openApi
     * Обращение к сервису payment: POST payment/productCost
     */
    @Override
    @PostMapping("/calculate/products")
    public OrderDto calculateProducts(@RequestBody UUID orderId) {
        return orderService.calculateProductsPrice(orderId);
    }

    /**
     * Расчёт полной стоимости: товары + доставка + налоги
     * Обращение к сервису payment: POST payment/totalCost
     */
    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody UUID orderId) {
        return orderService.calculateTotalPrice(orderId);
    }

    /**
     * Инициирование оплаты заказа
     * Обращение к сервису payment: POST payment/
     */
    @Override
    @PostMapping("/payment")
    public OrderDto paymentInit(@RequestBody UUID orderId) {
        return orderService.paymentInit(orderId);
    }

    /**
     * Оплата заказа прошла успешно
     * Вызов метода из сервиса payment: POST payment/refund
     * Устанавливаем статус PAID
     */
    @Override
    @PostMapping("/payment/success")
    public OrderDto paymentSuccess(@RequestBody UUID orderId) {
        return orderService.paymentSuccess(orderId);
    }

    /**
     * Неуспешная оплата заказа
     * Вызов метода из сервиса payment: POST payment/failed
     * Устанавливаем статус PAYMENT_FAILED
     */
    @Override
    @PostMapping("/payment/failed")
    public OrderDto paymentFail(@RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    /**
     * Инициировать сборку заказа
     * Обращение к сервису warehouse: POST warehouse/assembly
     * Устанавливаем статус ON_ASSEMBLY
     */
    @Override
    @PostMapping("/assembly")
    public OrderDto assemblyInit(@RequestBody UUID orderId) {
        return orderService.assemblyInit(orderId);
    }

    /**
     * Сборка заказа успешно завершена
     * Устанавливаем статус ASSEMBLED
     */
    @Override
    @PostMapping("/assembly/success")
    public OrderDto assemblySuccess(@RequestBody UUID orderId) {
        return orderService.assemblySuccess(orderId);
    }

    /**
     * Сборка заказа успешно завершена
     * Устанавливаем статус ASSEMBLY_FAILED
     */
    @Override
    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }

    /**
     * Инициировать доставку
     */
    @Override
    @PostMapping("/delivery")
    public OrderDto deliveryInit(@RequestBody UUID orderId) {
        return orderService.deliveryInit(orderId);
    }

    /**
     * Доставка успешно завершена
     * Метод вызывается из сервиса delivery: POST delivery/successful
     */
    @Override
    @PostMapping("/delivery/success")
    public OrderDto deliverySuccess(@RequestBody UUID orderId) {
        return orderService.deliverySuccess(orderId);
    }

    /**
     * Доставка не удалась
     * Метод вызывается из сервиса delivery: POST delivery/failed
     */
    @Override
    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {
        return orderService.deliveryFailed(orderId);
    }

    /**
     * Запрос на возврат товаров
     */
    @Override
    @PostMapping("/return")
    public OrderDto returnProducts(@RequestBody ProductReturnRequest request) {
        return orderService.returnProducts(request);
    }

    /**
     * Завершение заказа
     */
    @Override
    @PostMapping("/completed")
    public OrderDto orderCompleted(@RequestBody UUID orderId) {
        return orderService.orderCompleted(orderId);
    }
}
