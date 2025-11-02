package ru.yandex.practicum.commerce.delivery.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    @Mapping(target = "addressId", ignore = true)
    Address toEntity(AddressDto addressDto);
}
