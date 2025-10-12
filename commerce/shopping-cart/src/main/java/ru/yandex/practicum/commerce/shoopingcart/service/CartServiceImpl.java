package ru.yandex.practicum.commerce.shoopingcart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.commerce.interaction.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction.dto.cart.CartDto;
import ru.yandex.practicum.commerce.interaction.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shoopingcart.dal.Cart;
import ru.yandex.practicum.commerce.shoopingcart.dal.CartRepository;
import ru.yandex.practicum.commerce.shoopingcart.exception.NoProductsInShoppingCartException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final WarehouseClient warehouseClient;
    private final TransactionTemplate transactionTemplate;

    @Override
    public CartDto getCart(String username) {
        Optional<Cart> cartOptional = cartRepository.findByUsername(username);
        Cart cart = cartOptional.orElse(new Cart(username));
        return new CartDto(cart.getId(), cart.getProducts());
    }

    @Override
    public CartDto putProduct(String username, Map<UUID, Integer> productQuantityMap) {
        Optional<Cart> cartOptional = cartRepository.findByUsername(username);
        Cart cartToUpdate = cartOptional.orElse(new Cart(username));

        warehouseClient.checkStocks(new CartDto(cartToUpdate.getId(), productQuantityMap));

        Map<UUID, Integer> cartProducts = cartToUpdate.getProducts();
        Cart updatedCart = transactionTemplate.execute(status -> {
            for(Map.Entry<UUID, Integer> entryForAdd : productQuantityMap.entrySet()) {
                cartProducts.compute(entryForAdd.getKey(),
                        (key, value) -> value == null ? entryForAdd.getValue() : value + entryForAdd.getValue());
            }
            return cartRepository.save(cartToUpdate);
        });

        return new CartDto(cartToUpdate.getId(), cartProducts);
    }

    @Override
    @Transactional
    public void deleteCart(String username) {
        cartRepository.deleteByUsername(username);
    }

    @Override
    @Transactional
    public CartDto remove(String username, List<UUID> products) {
        Optional<Cart> cartOptional = cartRepository.findByUsername(username);
        Cart cart = cartOptional.orElseThrow(() ->
                new NoProductsInShoppingCartException(String.format("Cart for user '%s' is not exist", username)));

        Map<UUID, Integer> cartProducts = cart.getProducts();
        products.forEach(id -> {
            if (cartProducts.remove(id) == null) {
                throw new NoProductsInShoppingCartException(String.format("Product '%s' is not exist in the cart", id));
            };
        });

        cartRepository.save(cart);
        return new CartDto(cart.getId(), cartProducts);
    }

    @Override
    @Transactional
    public CartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        Optional<Cart> cartOptional = cartRepository.findByUsername(username);
        Cart cart = cartOptional.orElseThrow(() ->
                new NoProductsInShoppingCartException(String.format("Cart for user '%s' is not exist", username)));

        Map<UUID, Integer> cartProducts = cart.getProducts();
        UUID productId = request.getProductId();
        if (!cartProducts.containsKey(productId)) {
            throw new NoProductsInShoppingCartException(String.format("Product '%s' is not exist in the cart", productId));
        } else {
            cartProducts.put(productId, request.getNewQuantity());
        }

        cartRepository.save(cart);
        return new CartDto(cart.getId(), cartProducts);
    }
}
