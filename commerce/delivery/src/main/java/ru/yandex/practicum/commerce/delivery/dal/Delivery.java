package ru.yandex.practicum.commerce.delivery.dal;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {

    @Id
    @UuidGenerator
    @Column(name = "delivery_id")
    UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    DeliveryState state = DeliveryState.CREATED;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_address_id", referencedColumnName = "address_id")
    Address fromAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_address_id", referencedColumnName = "address_id")
    Address toAddress;

    @Column(name = "delivery_volume", nullable = false)
    Double deliveryVolume;

    @Column(name = "delivery_weight", nullable = false)
    Double deliveryWeight;

    @Column(name = "fragile", nullable = false)
    Boolean fragile;
}
