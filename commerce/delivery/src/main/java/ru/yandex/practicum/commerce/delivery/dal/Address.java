package ru.yandex.practicum.commerce.delivery.dal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @UuidGenerator
    @Column(name = "address_id")
    UUID addressId;

    String country;

    String city;

    String street;

    String house;

    String flat;
}
