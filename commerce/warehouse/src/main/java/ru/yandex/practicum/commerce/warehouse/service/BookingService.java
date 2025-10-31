package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.warehouse.dal.entity.Booking;

import java.util.UUID;

public interface BookingService {
    Booking createNewBooking(AssemblyProductsForOrderRequest request);

    Booking setStatusAssemblySuccess(UUID bookingId);

    Booking setStatusAssemblyFailed(UUID bookingId);

    Booking shippedToDelivery(ShippedToDeliveryRequest request);
}
