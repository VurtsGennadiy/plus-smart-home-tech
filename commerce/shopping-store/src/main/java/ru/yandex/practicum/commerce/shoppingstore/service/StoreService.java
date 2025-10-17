package ru.yandex.practicum.commerce.shoppingstore.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.interaction.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductDto;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductCategory;

import java.util.UUID;

public interface StoreService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProduct(UUID productId);

    PageDto<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProduct(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest request);
}
