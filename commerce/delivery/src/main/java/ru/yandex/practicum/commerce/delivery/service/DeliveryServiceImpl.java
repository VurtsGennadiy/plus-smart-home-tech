package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.delivery.dal.*;
import ru.yandex.practicum.commerce.interaction.dto.delivery.CalculateDeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.interaction.dto.delivery.DeliveryState;
import ru.yandex.practicum.commerce.interaction.dto.order.OrderDto;
import ru.yandex.practicum.commerce.interaction.exception.EntityNotFoundException;
import ru.yandex.practicum.commerce.interaction.logging.Loggable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressService addressService;
    private final DeliveryMapper deliveryMapper;

    /**
     * Создание новой доставки
     */
    @Override
    @Loggable
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto dto) {
        Address fromAddress = addressService.findOrCreateAddress(dto.getFromAddress());
        Address toAddress = addressService.findOrCreateAddress(dto.getToAddress());
        Delivery delivery = deliveryMapper.toEntity(dto, fromAddress, toAddress);
        deliveryRepository.save(delivery);
        return deliveryMapper.toDto(delivery);
    }

    /**
     * Расчёт цены доставки
     */
    @Override
    @Loggable
    public BigDecimal calculateDeliveryPrice(CalculateDeliveryDto dto) {
        OrderDto order = dto.getOrder();

        final double base = 5d;
        BigDecimal total = BigDecimal.valueOf(base);

        if (dto.getFromAddress().getCity().contains("ADDRESS_1")) {
            total = total.add(total);
        } else if (dto.getFromAddress().getCity().contains("ADDRESS_2")) {
            total = total.add(total.multiply(BigDecimal.TWO));
        } else {
            throw new IllegalArgumentException("Неизвестный адрес склада");
        }

        if (order.getFragile()) {
            total = total.multiply(BigDecimal.valueOf(1.2));
        }

        total = total.add(BigDecimal.valueOf(order.getDeliveryWeight() * 0.3));
        total = total.add(BigDecimal.valueOf(order.getDeliveryVolume() * 0.2));

        if (!(dto.getFromAddress().getCity().equals(dto.getToAddress().getCity()) &&
                dto.getFromAddress().getStreet().equals(dto.getToAddress().getStreet()))) {
            total = total.multiply(BigDecimal.valueOf(1.2));
        }

        return total.setScale(2, RoundingMode.DOWN);
    }

    /**
     * Установить статус успешной доставки
     */
    @Override
    @Loggable
    @Transactional
    public DeliveryDto setDeliverySuccess(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        return deliveryMapper.toDto(delivery);
    }

    /**
     * Установить статус неудачной доставки
     */
    @Override
    @Loggable
    @Transactional
    public DeliveryDto setDeliveryFailed(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        return deliveryMapper.toDto(delivery);
    }

    /**
     * Товары получены в доставку
     */
    @Override
    @Loggable
    @Transactional
    public DeliveryDto pickDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        return deliveryMapper.toDto(delivery);
    }

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery", deliveryId.toString()));
    }
}
