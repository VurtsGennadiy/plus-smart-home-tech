package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.*;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest request);

    void receiveProduct(AddProductToWarehouseRequest request);

    BookedProductsDto checkStocks(CartDto cart);

    AddressDto getAddress();

    BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request);

    void assemblySuccess(UUID bookingId);

    void assemblyFailed(UUID bookingId);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void returnProducts(Map<UUID, Integer> products);
}
