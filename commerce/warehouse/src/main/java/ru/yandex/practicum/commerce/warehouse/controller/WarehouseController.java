package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.*;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    /**
     * Завести новый товар на склад
     */
    @Override
    @PutMapping
    public void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.addNewProduct(request);
    }

    /**
     * Принять товары на склад
     */
    @Override
    @PostMapping("/add")
    public void receiveProduct(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.receiveProduct(request);
    }

    /**
     * Проверка наличия товаров
     */
    @Override
    @PostMapping("/check")
    public BookedProductsDto checkStocks(@RequestBody CartDto cart) {
        return warehouseService.checkStocks(cart);
    }

    /**
     * Получить адрес склада
     */
    @Override
    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    /**
     * Начать сборку заказа
     */
    @Override
    @PostMapping("/assembly")
    public BookedProductsDto assemblyOrder(@RequestBody AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyOrder(request);
    }

    /**
     * Успешная сборка
     */
    @Override
    @PostMapping("/assembly/success")
    public void assemblySuccess(@RequestBody UUID bookingId) {
        warehouseService.assemblySuccess(bookingId);
    }

    /**
     * Неудачная сборка
     */
    @Override
    @PostMapping("/assembly/failed")
    public void assemblyFailed(@RequestBody UUID bookingId) {
        warehouseService.assemblyFailed(bookingId);
    }

    /**
     * Передать товары в доставку
     */
    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    /**
     * Принять возврат товаров на склад
     */
    @Override
    @PostMapping("/return")
    public void returnProducts(@RequestBody Map<UUID, Integer> products) {
        warehouseService.returnProducts(products);
    }
}
