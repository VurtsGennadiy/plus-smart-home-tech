package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.delivery.dal.Address;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;

public interface AddressService {
    Address findOrCreateAddress(AddressDto addressDto);
}
