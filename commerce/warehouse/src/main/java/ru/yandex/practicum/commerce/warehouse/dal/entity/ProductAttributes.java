package ru.yandex.practicum.commerce.warehouse.dal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributes {
    @Id
    UUID productId;

    @Positive
    Double width;

    @Positive
    Double height;

    @Positive
    Double depth;

    @Positive
    Double weight;

    Boolean fragile;
}
