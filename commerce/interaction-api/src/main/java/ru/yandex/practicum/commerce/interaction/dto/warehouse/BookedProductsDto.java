package ru.yandex.practicum.commerce.interaction.dto.warehouse;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Общие сведения о зарезервированных товарах по корзине.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookedProductsDto {

    /**
     * Суммарный вес доставки.
     */
    Double deliveryWeight;

    /**
     * Общий объём доставки.
     */
    Double deliveryVolume;

    /**
     * Есть ли хрупкие вещи в доставке.
     */
    Boolean fragile;
}
