package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    @PutMapping
    public void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.addNewProduct(request);
    }

    @Override
    @PostMapping("/add")
    public void receiveProduct(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.receiveProduct(request);
    }

    @Override
    @PostMapping("/check")
    public BookedProductsDto checkStocks(@RequestBody CartDto cart) {
        return warehouseService.checkStocks(cart);
    }

    @Override
    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
