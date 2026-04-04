package io.github.amichailides.merimna.address;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.address.dto.AddressUpdateDTO;
import io.github.amichailides.merimna.domain.Address;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

@Component
public class AddressMapper {

    public AddressDTO toDTO(Address address) {
        if (address == null) return null;

        return AddressDTO.builder()
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .build();
    }

    public Address toEntity(AddressDTO dto) {
        if (dto == null) return null;

        return Address.builder()
                .street(dto.street())
                .streetNumber(dto.streetNumber())
                .city(dto.city())
                .zipCode(dto.zipCode())
                .build();
    }

    public void updateEntity(Address existing, AddressUpdateDTO dto) {
        Objects.requireNonNull(existing, "existing address must not be null");
        Objects.requireNonNull(dto, "address update dto must not be null");

        updateIfNotBlank(dto.street(), existing::setStreet);
        updateIfNotBlank(dto.streetNumber(), existing::setStreetNumber);
        updateIfNotBlank(dto.city(), existing::setCity);
        updateIfNotBlank(dto.zipCode(), existing::setZipCode);
    }

    private void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }
}

