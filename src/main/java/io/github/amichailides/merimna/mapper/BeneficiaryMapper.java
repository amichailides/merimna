package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.model.Address;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.HouseUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BeneficiaryMapper {
    private final AddressMapper addressMapper;
    private final EmergencyContactMapper emergencyMapper;

    public BeneficiaryReadOnlyDTO fromEntity(Beneficiary entity) {
        if (entity == null) return null;

        return BeneficiaryReadOnlyDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .amka(entity.getAmka())
                .dateOfBirth(entity.getDateOfBirth())
                .houseUnit(entity.getHouseUnit().name())
                .permanentAddress(addressMapper.fromEntity(entity.getPermanentAddress()))
                .emergencyContact(emergencyMapper.fromEntity(entity.getEmergencyContact()))
                .build();
    }


    public Beneficiary toEntity(BeneficiarySaveDTO dto) {
        if (dto == null) return null;

        return Beneficiary.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .amka(dto.amka())
                .dateOfBirth(dto.dateOfBirth())
                .houseUnit(dto.houseUnit())
                .permanentAddress(addressMapper.toEntity(dto.permanentAddress()))
                .emergencyContact(emergencyMapper.toEntity(dto.emergencyContact()))
                .build();
    }

}
