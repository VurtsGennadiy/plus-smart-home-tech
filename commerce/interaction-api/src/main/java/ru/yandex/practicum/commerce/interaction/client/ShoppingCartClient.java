package ru.yandex.practicum.commerce.interaction.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    CartDto getCart(@RequestParam(required = false) String username);


    @PutMapping
    CartDto putProduct(@RequestParam(required = false) String username,
                       @RequestBody Map<UUID, Integer> productQuantityMap);

    @DeleteMapping
    void deleteCart(@RequestParam(required = false) String username);

    @PostMapping("/remove")
    CartDto remove(@RequestParam(required = false) String username,
                   @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    CartDto changeQuantity(@RequestParam(required = false) String username,
                           @RequestBody ChangeProductQuantityRequest request);
}
