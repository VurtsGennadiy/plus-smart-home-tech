package ru.yandex.practicum.commerce.payment.exception;

/**
 * Будет выброшено при попытке рассчитать полную стоимость заказа или сохранить информацию об оплате.
 * В том случае, если отсутствует информация о стоимости товаров или стоимости доставки.
 */
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    private final boolean isMissingProductsCost;
    private final boolean isMissingDeliveryCost;

    public boolean isMissingProductsCost() {
        return isMissingProductsCost;
    }

    public boolean isMissingDeliveryCost() {
        return isMissingDeliveryCost;
    }

    public NotEnoughInfoInOrderToCalculateException(boolean isMissingProductsCost, boolean isMissingDeliveryCost) {
        super(buildMessage(isMissingProductsCost, isMissingDeliveryCost));
        this.isMissingProductsCost = isMissingProductsCost;
        this.isMissingDeliveryCost = isMissingDeliveryCost;
    }

    private static String buildMessage(boolean isMissingProductsCost, boolean isMissingDeliveryCost) {
        String what;
        if (isMissingProductsCost && isMissingDeliveryCost) {
            what = "products and delivery";
        } else if (isMissingProductsCost) {
            what = "products";
        } else if (isMissingDeliveryCost) {
            what = "delivery";
        } else {
            throw new IllegalArgumentException("At least one cost type must be missing");
        }
        return "Missing info about cost of " + what;
    }
}
