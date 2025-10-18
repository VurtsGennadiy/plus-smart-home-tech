package ru.yandex.practicum.commerce.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.dal.entity.ProductAttributes;

import java.util.UUID;

public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, UUID> {
}
