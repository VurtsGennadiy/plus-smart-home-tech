package ru.yandex.practicum.commerce.shoppingstore.dal;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.model.QuantityState;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    String productId;
    String productName;
    String description;
    String imageSrc;
    QuantityState quantityState;
    ProductState productState;
    ProductCategory productCategory;
    BigDecimal price;
}
