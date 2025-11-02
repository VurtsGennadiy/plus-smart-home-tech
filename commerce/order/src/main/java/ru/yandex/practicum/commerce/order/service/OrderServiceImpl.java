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
import ru.yandex.practicum.commerce.interaction.exception.EntityNotFoundException;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;
import ru.yandex.practicum.commerce.order.dal.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Transactional
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = getOrderById(orderId);
        OrderDto orderDto = orderMapper.toDto(order);

        // запрашиваем адрес склада у сервиса warehouse
        Address toAddress = order.getDeliveryAddress();
        AddressDto toAddressDto = addressMapper.toDto(toAddress);
        AddressDto fromAddressDto = warehouseClient.getAddress();

        // запрашиваем стоимость доставки у сервиса delivery
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
    @Transactional
    public OrderDto calculateProductsPrice(UUID orderId) {
        Order order = getOrderById(orderId);
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
    @Transactional
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = getOrderById(orderId);
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
        Order order = getOrderById(orderId);
        OrderDto orderDto = orderMapper.toDto(order);

        PaymentDto payment = paymentClient.createPayment(orderDto);

        transactionTemplate.executeWithoutResult(status -> {
            order.setPaymentId(payment.getPaymentId());
            order.setState(OrderState.ON_PAYMENT);
            orderRepository.save(order);
        });
        return orderMapper.toDto(order);
    }

    /**
     * Успешная оплата.
     * Метод вызывается из сервиса payment.
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto paymentSuccess(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.PAID);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Неудачная оплата
     * Метод вызывается из сервиса payment.
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto paymentFailed(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Инициализация процесса сборки заказа
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto assemblyInit(UUID orderId) {
        Order order = getOrderById(orderId);
        if (OrderState.PAID != order.getState()) {
            throw new IllegalStateException("Заказ должен быть оплачен перед сборкой");
        }

        order.setState(OrderState.ON_ASSEMBLY);
        orderRepository.save(order);

        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest();
        request.setOrderId(orderId);
        request.setCartId(order.getShoppingCartId());
        request.setProducts(order.getProducts());
        warehouseClient.assemblyOrder(request);

        return orderMapper.toDto(order);
    }

    /**
     * Сборка заказа успешно завершена
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto assemblySuccess(UUID orderId) {
        Order order = getOrderById(orderId);
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
        Order order = getOrderById(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Инициализация процесса доставки
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto deliveryInit(UUID orderId) {
        Order order = getOrderById(orderId);

        if (OrderState.ASSEMBLED != order.getState()) {
            throw new IllegalStateException("Заказ должен быть собран перед доставкой");
        }

        Address toAddress = order.getDeliveryAddress();
        AddressDto toAddressDto = addressMapper.toDto(toAddress);
        AddressDto fromAddressDto = warehouseClient.getAddress();
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .fromAddress(fromAddressDto)
                .toAddress(toAddressDto)
                .orderId(orderId)
                .deliveryVolume(order.getDeliveryVolume())
                .deliveryWeight(order.getDeliveryWeight())
                .fragile(order.getFragile())
                .build();

        deliveryDto = deliveryClient.createDelivery(deliveryDto);
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setState(OrderState.DELIVERY_CREATED);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Заказ передан в доставку
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto deliveryShipped(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.ON_DELIVERY);
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
        Order order = getOrderById(orderId);
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
        Order order = getOrderById(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Запрос на возврат товаров
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto returnProducts(ProductReturnRequest request) {
        Order order = getOrderById(request.getOrderId());
        if (OrderState.COMPLETED == order.getState()) {
            throw new IllegalStateException("Заказ завершен, возврат товаров невозможен");
        }
        order.setState(OrderState.PRODUCT_RETURNED);
        warehouseClient.returnProducts(request.getProducts());
        return orderMapper.toDto(order);
    }

    /**
     * Завершение заказа
     */
    @Override
    @Loggable
    @Transactional
    public OrderDto orderCompleted(UUID orderId) {
        Order order = getOrderById(orderId);

        Set<OrderState> validStates = Set.of(OrderState.DELIVERED, OrderState.PRODUCT_RETURNED, OrderState.ASSEMBLED);
        if (!validStates.contains(order.getState())) {
            throw new IllegalStateException(String.format("Заказ не может быть завершен в статусе %s", order.getState()));
        }

        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order", orderId.toString()));
    }
}
