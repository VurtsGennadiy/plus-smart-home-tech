package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.interaction.client.DeliveryClient;
import ru.yandex.practicum.commerce.interaction.client.PaymentClient;
import ru.yandex.practicum.commerce.interaction.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.CalculateDeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderState;
import ru.yandex.practicum.commerce.interaction.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;
import ru.yandex.practicum.commerce.order.dal.*;
import ru.yandex.practicum.commerce.order.exception.OrderNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final TransactionTemplate transactionTemplate;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;

    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    @Loggable
    public OrderDto createNewOrder(CreateNewOrderRequest request, String username) {
        // обращаемся к сервису warehouse для проверки наличия товаров и получения габаритов
        BookedProductsDto bookedDto = warehouseClient.checkStocks(request.getShoppingCart());

        Order order = transactionTemplate.execute(status -> {
            Address addressRequest = addressMapper.toEntity(request.getDeliveryAddress());
            Example<Address> example = Example.of(addressRequest, ExampleMatcher.matching().withIgnorePaths("address_id"));
            Optional<Address> addressOptional = addressRepository.findOne(example);
            Address address = addressOptional.orElse(addressRequest);

            Order entity = orderMapper.toEntity(request.getShoppingCart(), username, address, bookedDto);
            return orderRepository.save(entity);
        });

        return orderMapper.toDto(order);
    }

    @Override
    @Loggable
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(String username) {
        List<Order> orders = orderRepository.getAllByUsername(username);
        return orderMapper.toDto(orders);
    }

    /**
     * Запрос цены доставки
     */
    @Override
    @Loggable
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        OrderDto orderDto = orderMapper.toDto(order);

        Address toAddress = order.getDeliveryAddress();
        AddressDto toAddressDto = addressMapper.toDto(toAddress);
        AddressDto fromAddressDto = warehouseClient.getAddress();

        CalculateDeliveryDto calculateDeliveryDto = new CalculateDeliveryDto();
        calculateDeliveryDto.setOrder(orderDto);
        calculateDeliveryDto.setToAddress(toAddressDto);
        calculateDeliveryDto.setFromAddress(fromAddressDto);

        BigDecimal deliveryPrice = deliveryClient.calculateDeliveryPrice(calculateDeliveryDto);
        order.setDeliveryPrice(deliveryPrice);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Запрос цены товаров
     */
    @Override
    @Loggable
    public OrderDto calculateProductsPrice(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        OrderDto orderDto = orderMapper.toDto(order);

        BigDecimal productsPrice = paymentClient.calculateProductsCost(orderDto);
        order.setProductPrice(productsPrice);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Запрос полной стоимости заказа
     */
    @Override
    @Loggable
    public OrderDto calculateTotalPrice(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        OrderDto orderDto = orderMapper.toDto(order);

        BigDecimal totalPrice = paymentClient.calculateTotalCost(orderDto);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Инициализация процесса оплаты
     */
    @Override
    @Loggable
    public OrderDto paymentInit(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        OrderDto orderDto = orderMapper.toDto(order);

        PaymentDto payment = paymentClient.payment(orderDto);
        order.setPaymentId(payment.getPaymentId());
        order.setState(OrderState.ON_PAYMENT);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Успешная оплата
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto paymentSuccess(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        order.setState(OrderState.PAID);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Неудачная оплата
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto paymentFailed(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Инициализация процесса сборки заказа
     */
    @Override
    @Loggable
    public OrderDto assemblyInit(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest();
        request.setOrderId(orderId);
        request.setProducts(order.getProducts());
        warehouseClient.assemblyOrder(request);

        order.setState(OrderState.ON_ASSEMBLY);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Сборка заказа успешно завершена
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto assemblySuccess(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        order.setState(OrderState.ASSEMBLED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Неудачная сборка заказа
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto assemblyFailed(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Инициализация процесса доставки
     */
    @Override
    @Loggable
    public OrderDto deliveryInit(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        Address toAddress = order.getDeliveryAddress();
        AddressDto toAddressDto = addressMapper.toDto(toAddress);
        AddressDto fromAddressDto = warehouseClient.getAddress();
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(fromAddressDto)
                .toAddress(toAddressDto)
                .orderId(orderId)
                .build();

        deliveryDto = deliveryClient.createDelivery(deliveryDto);
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setState(OrderState.DELIVERY_CREATED);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Успешная доставка заказа
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto deliverySuccess(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Неудачная доставка заказа
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto deliveryFailed(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Запрос на возврат товаров
     */
    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(request.getOrderId());
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(request.getOrderId().toString()));

        warehouseClient.returnProducts(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(order);
    }

    /**
     * Завершение заказа
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto orderCompleted(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
