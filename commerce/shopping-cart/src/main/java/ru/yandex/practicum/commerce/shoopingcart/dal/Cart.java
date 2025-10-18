package ru.yandex.practicum.commerce.shoopingcart.dal;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @Column(name = "username")
    String username;

    @ElementCollection
    @CollectionTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products = new HashMap<>();

    public Cart() {
    }

    public Cart(String username) {
        this.username = username;
    }
}
