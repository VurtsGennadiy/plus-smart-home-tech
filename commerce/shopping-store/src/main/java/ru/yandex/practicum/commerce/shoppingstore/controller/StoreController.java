package ru.yandex.practicum.commerce.shoppingstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.ProductCategory;
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
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return shoppingService.createProduct(productDto);
    }

    // ошибка в API, id передаётся в параметр запроса а не в теле
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
}
