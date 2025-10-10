package ru.yandex.practicum.commerce.shoopingcart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shoopingcart.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.shoopingcart.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto getCart(@RequestParam(required = false) String username) {
        if (username == null) {
            throw new NotAuthorizedUserException("Authentication required");
        }
        return cartService.getCart(username);
    }

    @PutMapping
    public CartDto putProduct(@RequestParam(required = false) String username,
                              @RequestBody Map<UUID, Integer> productQuantityMap) {
        if (username == null) {
            throw new NotAuthorizedUserException("Authentication required");
        }
        return cartService.putProduct(username, productQuantityMap);
    }

    @DeleteMapping
    public void deleteCart(@RequestParam(required = false) String username) {
        if (username == null) {
            throw new NotAuthorizedUserException("Authentication required");
        }
        cartService.deleteCart(username);
    }

    @PostMapping("/remove")
    public CartDto remove(@RequestParam(required = false) String username,
                          @RequestBody List<UUID> products) {
        if (username == null) {
            throw new NotAuthorizedUserException("Authentication required");
        }
        return cartService.remove(username, products);
    }

    @PostMapping("/change-quantity")
    public CartDto changeQuantity(@RequestParam(required = false) String username,
                                  @RequestBody @Valid ChangeProductQuantityRequest request) {
        if (username == null) {
            throw new NotAuthorizedUserException("Authentication required");
        }
        return cartService.changeQuantity(username, request);
    }
}
