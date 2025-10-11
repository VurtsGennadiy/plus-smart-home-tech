package ru.yandex.practicum.commerce.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.dal.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Short> {
}
