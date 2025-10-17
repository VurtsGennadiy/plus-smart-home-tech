package ru.yandex.practicum.commerce.interaction.dto.warehouse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
