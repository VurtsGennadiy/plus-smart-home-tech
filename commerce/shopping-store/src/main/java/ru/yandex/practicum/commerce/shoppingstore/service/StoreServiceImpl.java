package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.dto.PageDto;
import ru.yandex.practicum.commerce.interaction.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductDto;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.dal.Product;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductRepository;
import ru.yandex.practicum.commerce.shoppingstore.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    @Loggable
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Loggable
    public ProductDto getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public PageDto<ProductDto> getProducts(ProductCategory category,
                                           Pageable pageable) {
        Page<Product> entitiesPage = productRepository.findAll(pageable);
        return new PageDto<>(entitiesPage.map(productMapper::toDto));
    }

    @Override
    @Transactional
    @Loggable
    public ProductDto updateProduct(ProductDto productDto) {
        if (!productRepository.existsById(UUID.fromString(productDto.getProductId()))) {
            throw new ProductNotFoundException("Product with ID " + productDto.getProductId() + " not found");
        }

        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    @Loggable
    public boolean removeProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));
        try {
            product.setProductState(ProductState.DEACTIVATE);
            productRepository.save(product);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    @Loggable
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + request.getProductId() + " not found"));
        try {
            product.setQuantityState(request.getQuantityState());
            productRepository.save(product);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Map<UUID, BigDecimal> getProductsCost(Collection<UUID> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        return products.stream().collect(Collectors.toMap(Product::getProductId, Product::getPrice));
    }
}
