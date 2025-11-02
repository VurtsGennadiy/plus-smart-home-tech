package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.delivery.dal.Address;
import ru.yandex.practicum.commerce.delivery.dal.AddressMapper;
import ru.yandex.practicum.commerce.delivery.dal.AddressRepository;
import ru.yandex.practicum.commerce.interaction.dto.AddressDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    /**
     * Проверка существует ли адрес в базе.
     * Если нет, то будет создана новая запись.
     * Метод нужен для того, чтобы не дублировать одинаковые адреса в базе данных
     */
    @Override
    @Transactional
    public Address findOrCreateAddress(AddressDto addressDto) {
        Address addressRequest = addressMapper.toEntity(addressDto);
        Example<Address> example = Example.of(addressRequest, ExampleMatcher.matching().withIgnorePaths("address_id"));
        Optional<Address> addressOptional = addressRepository.findOne(example);
        return addressOptional.orElseGet(() -> addressRepository.save(addressRequest));
    }
}
