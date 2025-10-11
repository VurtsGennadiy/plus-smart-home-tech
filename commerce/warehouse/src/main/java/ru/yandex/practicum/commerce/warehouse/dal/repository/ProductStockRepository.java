package ru.yandex.practicum.commerce.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.dal.entity.ProductStock;
import ru.yandex.practicum.commerce.warehouse.dal.entity.ProductStockPK;

public interface ProductStockRepository extends JpaRepository<ProductStock, ProductStockPK> {
}
