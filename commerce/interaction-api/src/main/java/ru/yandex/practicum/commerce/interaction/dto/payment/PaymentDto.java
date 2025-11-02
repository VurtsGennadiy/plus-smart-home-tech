package ru.yandex.practicum.commerce.interaction.dto.payment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    UUID paymentId;

    UUID orderId;

    BigDecimal totalPayment;

    BigDecimal deliveryCost;

    BigDecimal productsCost;

    PaymentStatus status;
}
