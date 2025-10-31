package ru.yandex.practicum.commerce.interaction.dto.delivery;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    UUID deliveryId;

    UUID orderId;

    DeliveryState deliveryState;

    AddressDto fromAddress;

    AddressDto toAddress;

    Double deliveryVolume;

    Double deliveryWeight;

    Boolean fragile;
}
