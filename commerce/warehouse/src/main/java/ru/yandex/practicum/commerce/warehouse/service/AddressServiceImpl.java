package ru.yandex.practicum.commerce.warehouse.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.warehouse.dal.entity.Address;
import ru.yandex.practicum.commerce.warehouse.dal.repository.AddressRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @PostConstruct
    public void init() {
        if (addressRepository.count() == 0) {
            Address address1 = Address.builder()
                    .country("ADDRESS_1")
                    .city("ADDRESS_1")
                    .street("ADDRESS_1")
                    .house("ADDRESS_1")
                    .flat("ADDRESS_1")
                    .build();

            Address address2 = Address.builder()
                    .country("ADDRESS_2")
                    .city("ADDRESS_2")
                    .street("ADDRESS_2")
                    .house("ADDRESS_2")
                    .flat("ADDRESS_2")
                    .build();

            addressRepository.saveAll(List.of(address1, address2));
        }
    }

    @Override
    public Address getAddress() {
        List<Address> addresses = addressRepository.findAll();
        int index = new Random().nextInt(addresses.size());
        return addresses.getFirst(); // убираю рандомность склада, чтобы не городить костыли
    }
}
