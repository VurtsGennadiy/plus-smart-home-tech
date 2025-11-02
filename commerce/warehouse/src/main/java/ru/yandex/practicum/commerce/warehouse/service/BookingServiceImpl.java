package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interaction.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.interaction.exception.EntityNotFoundException;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;
import ru.yandex.practicum.commerce.warehouse.dal.entity.Booking;
import ru.yandex.practicum.commerce.warehouse.dal.repository.BookingRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    /**
     * Создание бронирования товаров
     */
    @Override
    @Transactional
    @Loggable
    public Booking createNewBooking(AssemblyProductsForOrderRequest request) {
        Booking booking = new Booking();
        booking.setOrderId(request.getOrderId());
        booking.setProducts(request.getProducts());
        return bookingRepository.save(booking);
    }

    /**
     * Установить статус бронирования собран
     */
    @Override
    @Transactional
    @Loggable
    public Booking setStatusAssemblySuccess(UUID bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(Booking.State.ASSEMBLED);
        bookingRepository.save(booking);
        return booking;
    }

    /**
     * Установить статус бронирования ошибка сборки
     */
    @Override
    @Transactional
    @Loggable
    public Booking setStatusAssemblyFailed(UUID bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(Booking.State.ASSEMBLY_FAILED);
        bookingRepository.save(booking);
        return booking;
    }

    /**
     * Передать товары в доставку
     */
    @Override
    @Transactional
    @Loggable
    public Booking shippedToDelivery(ShippedToDeliveryRequest request) {
        Booking booking = bookingRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Booking", null,
                        String.format("Booking with order id %s not found", request.getOrderId())));

        booking.setStatus(Booking.State.PASSED);
        booking.setDeliveryId(request.getDeliveryId());
        bookingRepository.save(booking);
        return booking;
    }

    private Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking", bookingId.toString()));
    }
}
