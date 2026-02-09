package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.model.Beneficiary;
import org.springframework.stereotype.Component;

@Component
public class BeneficiaryMapper {

    public BeneficiaryReadOnlyDTO fromEntity(Beneficiary beneficiary) {
        if (beneficiary == null) return null;

        return BeneficiaryReadOnlyDTO.builder()
                .id(beneficiary.getId())
                .firstName(beneficiary.getFirstName())
                .lastName(beneficiary.getLastName())
                .amka(beneficiary.getAmka())
                .houseUnit(beneficiary.getHouseUnit() != null ? beneficiary.getHouseUnit().name() : null)
                .build();
    }
}
