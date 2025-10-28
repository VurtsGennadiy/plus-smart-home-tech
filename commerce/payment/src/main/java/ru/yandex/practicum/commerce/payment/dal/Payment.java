package ru.yandex.practicum.commerce.payment.dal;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.commerce.interaction.dto.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @UuidGenerator
    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @Column(name = "total_cost", nullable = false)
    BigDecimal totalPayment;

    @Column(name = "delivery_cost", nullable = false)
    BigDecimal deliveryCost;

    @Column(name = "products_cost", nullable = false)
    BigDecimal productsCost;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentStatus status = PaymentStatus.PENDING;
}
