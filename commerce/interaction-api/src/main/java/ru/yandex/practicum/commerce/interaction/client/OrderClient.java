package ru.yandex.practicum.commerce.interaction.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {
    String USERNAME_HEADER = "X-Auth-User";

    @PutMapping
    OrderDto createNewOrder(@RequestHeader(USERNAME_HEADER) String username,
                            @Valid @RequestBody CreateNewOrderRequest request);

    @GetMapping
    List<OrderDto> getUserOrders(@RequestHeader(USERNAME_HEADER) String username);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody String orderId);

    @PostMapping("/calculate/products")
    OrderDto calculateProducts(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody UUID orderId);

    @PostMapping("/payment")
    OrderDto paymentInit(@RequestBody UUID orderId);

    @PostMapping("/payment/success")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFail(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyInit(@RequestBody UUID orderId);

    @PostMapping("/assembly/success")
    OrderDto assemblySuccess(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryInit(@RequestBody UUID orderId);

    @PostMapping("/delivery/success")
    OrderDto deliverySuccess(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/return")
    OrderDto returnProducts(@RequestBody ProductReturnRequest request);

    @PostMapping("/completed")
    OrderDto orderCompleted(@RequestBody UUID orderId);
}
