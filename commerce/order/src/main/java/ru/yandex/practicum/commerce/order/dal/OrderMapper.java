package ru.yandex.practicum.commerce.order.dal;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.BookedProductsDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "shoppingCartId", source = "cart.shoppingCartId")
    @Mapping(target = "products", source = "cart.products")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "deliveryVolume", source = "bookedDto.deliveryVolume")
    @Mapping(target = "deliveryWeight", source = "bookedDto.deliveryWeight")
    @Mapping(target = "fragile", source = "bookedDto.fragile")
    Order toEntity(CartDto cart, String username, Address deliveryAddress, BookedProductsDto bookedDto);

    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);
}
