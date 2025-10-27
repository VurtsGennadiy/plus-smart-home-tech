package ru.yandex.practicum.commerce.order.dal;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderState;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @UuidGenerator
    @Column(name = "order_id")
    UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    OrderState state = OrderState.NEW;

    @Column(name = "username", nullable = false)
    String username;

    @ElementCollection
    @CollectionTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products = new HashMap<>();

    @Column(name = "cart_id")
    UUID shoppingCartId;

    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "delivery_id")
    UUID deliveryId;

    @Column(name = "delivery_weight")
    Double deliveryWeight;

    @Column(name = "delivery_volume")
    Double deliveryVolume;

    @Column(name = "fragile")
    Boolean fragile;

    @Column(name = "delivery_price")
    BigDecimal deliveryPrice;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", referencedColumnName = "address_id")
    Address deliveryAddress;

    @Column(name = "total_price")
    BigDecimal totalPrice; // итоговая цена

    @Column(name = "product_price")
    BigDecimal productPrice; // цена товаров
}