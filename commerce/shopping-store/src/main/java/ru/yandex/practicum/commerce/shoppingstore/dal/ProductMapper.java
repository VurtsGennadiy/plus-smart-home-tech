package ru.yandex.practicum.commerce.shoppingstore.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.interaction.dto.store.ProductDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    Product toEntity(ProductDto dto);

    ProductDto toDto(Product entity);
}
