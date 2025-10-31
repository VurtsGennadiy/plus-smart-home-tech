package ru.yandex.practicum.commerce.warehouse.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.client.OrderClient;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.*;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;
import ru.yandex.practicum.commerce.warehouse.dal.entity.*;
import ru.yandex.practicum.commerce.warehouse.dal.repository.ProductAttributesRepository;
import ru.yandex.practicum.commerce.warehouse.dal.repository.ProductStockRepository;
import ru.yandex.practicum.commerce.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final OrderClient orderClient;
    private final ProductStockRepository stockRepository;
    private final ProductAttributesRepository attributesRepository;
    private final BookingService bookingService;
    private final AddressService addressService;
    private Address address;

    @PostConstruct
    public void init() {
        address = addressService.getAddress();
    }

    @Override
    @Transactional
    @Loggable
    public void addNewProduct(NewProductInWarehouseRequest request) {
        if (attributesRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    String.format("Product '%s' already exists in the warehouse", request.getProductId()));
        }

        ProductAttributes productAttributes = ProductAttributes.builder()
                .productId(request.getProductId())
                .weight(request.getWeight())
                .fragile(request.isFragile())
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .build();

        attributesRepository.save(productAttributes);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public BookedProductsDto checkStocks(CartDto cart) {
        Map<UUID, Integer> cartProducts = cart.getProducts();
        List<ProductStockPK> productStockPKs = cartProducts.keySet().stream()
                .map(productId -> new ProductStockPK(productId, address.getId())).toList();
        Map<UUID, ProductStock> stocks = stockRepository.findAllById(productStockPKs).stream()
                .collect(Collectors.toMap( item -> item.getId().getProductId(), Function.identity()));

        // проверяем, что все товары есть в нужном количестве
        StringBuilder errorMessage = new StringBuilder();
        for (Map.Entry<UUID, Integer> cartEntry : cartProducts.entrySet()) {
            ProductStock stock = stocks.get(cartEntry.getKey());

            if (stock == null) {
                errorMessage.append(String.format("Product %s is not present in warehouse", cartEntry.getKey()));
                continue;
            }

            if (stock.getQuantity() < cartEntry.getValue()) {
                errorMessage.append(String.format("Product %s is not enough in warehouse, request: %d, balance: %d",
                        cartEntry.getKey(), cartEntry.getValue(), stock.getQuantity()));
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(errorMessage.toString());
        }

        // суммируем габариты доставки
        double deliveryWeight = 0.0;
        double deliveryVolume = 0.0;
        boolean fragile = false;

        Map<UUID, ProductAttributes> attributesMap = attributesRepository.findAllById(cartProducts.keySet()).stream()
                .collect(Collectors.toMap(ProductAttributes::getProductId, Function.identity()));

        for (Map.Entry<UUID, Integer> cartEntry : cartProducts.entrySet()) {
            ProductAttributes attributes = attributesMap.get(cartEntry.getKey());
            deliveryWeight += attributes.getWeight();
            deliveryVolume += (attributes.getHeight() * attributes.getWidth() * attributes.getDepth());
            if (attributes.getFragile()) {
                fragile = true;
            }
        }

        // округляем до 2 десятичных знаков
        deliveryWeight = (double) Math.round(100 * deliveryWeight) / 100;
        deliveryVolume = (double) Math.round(100 * deliveryVolume) / 100;

        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }

    @Override
    @Transactional
    @Loggable
    public void receiveProduct(AddProductToWarehouseRequest request) {
        if (!attributesRepository.existsById(request.getProductId())) {
            throw new NoSpecifiedProductInWarehouseException(
                    String.format("There is no description in warehouse for this product %s", request.getProductId()));
        }

        ProductStock stock = stockRepository.findById(new ProductStockPK(request.getProductId(), address.getId()))
                .orElse(new ProductStock(address, request.getProductId(), 0));
        stock.setQuantity(stock.getQuantity() + request.getQuantity());
        stockRepository.save(stock);
    }

    /**
     * Забронировать товары для сборки заказа
     */
    @Override
    @Loggable
    @Transactional
    public BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request) {
        BookedProductsDto bookedDto = checkStocks(new CartDto(request.getCartId(), request.getProducts()));
        bookingService.createNewBooking(request);

        // уменьшаем остатки запасов
        Map<UUID, Integer> products = request.getProducts();
        List<ProductStockPK> productsPKs = products.keySet().stream()
                .map(productId -> new ProductStockPK(productId, address.getId())).toList();

        List<ProductStock> stocks = stockRepository.findAllById(productsPKs);
        stocks.forEach(stock -> stock.setQuantity(
                stock.getQuantity() - products.get(stock.getId().getProductId())
        ));

        stockRepository.saveAll(stocks);
        return bookedDto;
    }

    /**
     * Сборка заказа успешно завершена
     */
    @Override
    public void assemblySuccess(UUID bookingId) {
        Booking booking = bookingService.setStatusAssemblySuccess(bookingId);
        orderClient.assemblySuccess(booking.getOrderId());
    }

    /**
     * Неудачная сборка заказа
     */
    @Override
    public void assemblyFailed(UUID bookingId) {
        Booking booking = bookingService.setStatusAssemblyFailed(bookingId);
        orderClient.assemblyFailed(booking.getOrderId());
    }

    /**
     * Передать товары в доставку
     */
    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        bookingService.shippedToDelivery(request);
    }

    /**
     * Принять возврат товаров на склад
     */
    @Override
    @Loggable
    @Transactional
    public void returnProducts(Map<UUID, Integer> products) {
        List<ProductStockPK> productsPKs = products.keySet().stream()
                .map(productId -> new ProductStockPK(productId, address.getId())).toList();

        List<ProductStock> stocks = stockRepository.findAllById(productsPKs);
        stocks.forEach(stock -> stock.setQuantity(
                stock.getQuantity() + products.get(stock.getId().getProductId())
        ));

        stockRepository.saveAll(stocks);
    }

    @Override
    @Loggable
    public AddressDto getAddress() {
        return address.toDto();
    }
}
