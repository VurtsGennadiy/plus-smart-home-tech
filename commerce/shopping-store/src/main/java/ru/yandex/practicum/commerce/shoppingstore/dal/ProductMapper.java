package ru.yandex.practicum.commerce.shoppingstore.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.Product;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "id", source = "productId")
    Product toEntity(ProductDto dto);

    @Mapping(target = "productId", source = "id")
    ProductDto toDto(Product entity);

    @Mapping(target = "productId", source = "id")
    List<ProductDto> toDto(List<Product> entities);
}
