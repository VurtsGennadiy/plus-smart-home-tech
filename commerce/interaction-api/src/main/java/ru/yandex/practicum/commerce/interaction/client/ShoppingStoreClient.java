package ru.yandex.practicum.commerce.interaction.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductDto;
import ru.yandex.practicum.commerce.interaction.dto.store.QuantityState;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid ProductDto productDto);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @GetMapping
    PageDto<ProductDto> getProducts(@RequestParam ProductCategory category,
                                    @PageableDefault(size = 20, sort = "productName") Pageable pageable);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody String productId);

    @PostMapping("/quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId,
                                    @RequestParam QuantityState quantityState);
}
