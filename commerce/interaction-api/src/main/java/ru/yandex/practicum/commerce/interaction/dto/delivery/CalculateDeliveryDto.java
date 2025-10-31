package ru.yandex.practicum.commerce.interaction.dto.delivery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateDeliveryDto {
    OrderDto order;
    AddressDto fromAddress;
    AddressDto toAddress;
}
