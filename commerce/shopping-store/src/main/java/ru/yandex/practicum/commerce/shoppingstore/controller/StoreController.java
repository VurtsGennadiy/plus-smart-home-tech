package ru.yandex.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.interaction.dto.store.QuantityState;
import ru.yandex.practicum.commerce.interaction.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductDto;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingService;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Validated
public class StoreController {
    private final ShoppingService shoppingService;

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return shoppingService.createProduct(productDto);
    }

    // ошибка в openapi: id передаётся в параметре запроса, а не в теле
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingService.getProduct(productId);
    }

    @GetMapping
    public PageDto<ProductDto> getProducts(@RequestParam ProductCategory category,
                                           @PageableDefault(size = 20, sort = "productName") Pageable pageable) {
        return shoppingService.getProducts(category, pageable);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return shoppingService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody String productId) {
        UUID uuid = UUID.fromString(productId.replace("\"", ""));
        return shoppingService.removeProduct(uuid);
    }

    // ошибка в openapi: параметры передаются в запросе, а не в теле
    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestParam UUID productId,
                                           @RequestParam QuantityState quantityState) {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(productId, quantityState);
        return shoppingService.setProductQuantityState(request);
    }
}
