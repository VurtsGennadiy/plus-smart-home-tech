package ru.yandex.practicum.commerce.warehouse.dal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @UuidGenerator
    @Column(name = "booking_id")
    UUID bookingId;

    @Column(name = "order_id")
    UUID orderId;

    @Column(name = "delivery_id")
    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    State status = State.ON_ASSEMBLY;

    @ElementCollection
    @CollectionTable(
            name = "booking_products",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products = new HashMap<>();

    public enum State {
        ON_ASSEMBLY, // сборка
        ASSEMBLED, // собран
        ASSEMBLY_FAILED, // ошибка сборки
        PASSED // передан на доставку
    }
}


