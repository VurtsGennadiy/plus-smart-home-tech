package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.Product;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional
                .orElseThrow(() -> new RuntimeException("Товар UUID " + productId + " не найден"));
        return productMapper.toDto(product);
    }

    @Override
    public PageDto<ProductDto> getProducts(ProductCategory category,
                                           Pageable pageable) {
        Page<Product> entitiesPage = productRepository.findAll(pageable);
        return new PageDto<>(entitiesPage.map(productMapper::toDto));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (!productRepository.existsById(UUID.fromString(productDto.getProductId()))) {
            throw new RuntimeException("Product with ID " + productDto.getProductId() + " not found");
        }

        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public boolean removeProduct(UUID productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар UUID " + productId + " не найден"));
            product.setProductState(ProductState.DEACTIVATE);
            productRepository.save(product);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
