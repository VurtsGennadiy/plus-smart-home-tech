package ru.yandex.practicum.commerce.shoopingcart.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUsername(String username);

    void deleteByUsername(String username);
}
