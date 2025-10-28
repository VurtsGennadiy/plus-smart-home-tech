package ru.yandex.practicum.commerce.payment.exception;


import java.util.ArrayList;
import java.util.List;

/**
 * Выбрасывается при попытке рассчитать стоимость товаров заказа, в том случае если отсутствует цена одного или нескольких товаров
 */
public class MissingProductCostException extends RuntimeException {
    private static final String DEFAULT_MESSAGE_TEMPLATE = "Missing costs for products: %s";
    private final List<String> missingProductsList;

    public List<String> getMissingProductsList() {
        return missingProductsList;
    }

    public MissingProductCostException(List<String> missingProductsList) {
        super(String.format(DEFAULT_MESSAGE_TEMPLATE, missingProductsList));
        this.missingProductsList = new ArrayList<>(missingProductsList);
    }
}
