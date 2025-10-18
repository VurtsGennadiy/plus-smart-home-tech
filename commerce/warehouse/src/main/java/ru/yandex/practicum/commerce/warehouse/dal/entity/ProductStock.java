package ru.yandex.practicum.commerce.warehouse.dal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStock {
    @EmbeddedId
    ProductStockPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    Address address;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    public ProductStock() {

    }

    public ProductStock(Address address, UUID productId, Integer quantity) {
        this.id = new ProductStockPK(productId, address.getId());
        this.address = address;
        this.quantity = quantity;
    }
}
