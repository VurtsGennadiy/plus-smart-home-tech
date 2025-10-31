package ru.yandex.practicum.commerce.payment.dal;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "totalPayment", source = "order.totalPrice")
    @Mapping(target = "deliveryCost", source = "order.deliveryPrice")
    @Mapping(target = "productsCost", source = "order.productPrice")
    @Mapping(target = "orderId", source = "order.orderId")
    Payment fromOrder(OrderDto order);

    PaymentDto toDto(Payment payment);
}
