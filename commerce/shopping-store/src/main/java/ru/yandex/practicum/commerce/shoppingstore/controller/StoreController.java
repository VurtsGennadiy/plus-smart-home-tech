package ru.yandex.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.client.ShoppingStoreClient;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.interaction.dto.store.QuantityState;
import ru.yandex.practicum.commerce.interaction.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductDto;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.service.StoreService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreController implements ShoppingStoreClient {
    private final StoreService storeService;

    @Override
    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return storeService.createProduct(productDto);
    }

    // ошибка в openapi: id передаётся в параметре запроса, а не в теле
    @Override
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return storeService.getProduct(productId);
    }

    @Override
    @GetMapping
    public PageDto<ProductDto> getProducts(@RequestParam ProductCategory category,
                                           @PageableDefault(size = 20, sort = "productName") Pageable pageable) {
        return storeService.getProducts(category, pageable);
    }

    /**
     * Получить цену товаров по Id.
     * Метод вызывается из сервиса Payment.
     */
    @Override
    @PostMapping("/cost")
    public Map<UUID, BigDecimal> getProductsCost(@RequestBody Collection<UUID> productIds) {
        return storeService.getProductsCost(productIds);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return storeService.updateProduct(productDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody String productId) {
        UUID uuid = UUID.fromString(productId.replace("\"", ""));
        return storeService.removeProduct(uuid);
    }

    // ошибка в openapi: параметры передаются в запросе, а не в теле
    @Override
    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestParam UUID productId,
                                           @RequestParam QuantityState quantityState) {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(productId, quantityState);
        return storeService.setProductQuantityState(request);
    }
}
