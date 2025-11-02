package ru.yandex.practicum.commerce.delivery.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {

    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "fromAddress", source = "fromAddress")
    @Mapping(target = "toAddress", source = "toAddress")
    Delivery toEntity(DeliveryDto dto, Address fromAddress, Address toAddress);

    DeliveryDto toDto(Delivery entity);
}
