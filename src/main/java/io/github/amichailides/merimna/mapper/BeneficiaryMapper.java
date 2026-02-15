package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.model.Beneficiary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BeneficiaryMapper {
    private final AddressMapper addressMapper;
    private final EmergencyContactMapper emergencyMapper;

    public BeneficiaryReadOnlyDTO toReadOnlyDTO(Beneficiary entity) {
        if (entity == null) return null;

        return BeneficiaryReadOnlyDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .amka(entity.getAmka())
                .dateOfBirth(entity.getDateOfBirth())
                .isActive(entity.getIsActive())
                .houseUnit(entity.getHouseUnit())
                .permanentAddress(addressMapper.toDTO(entity.getPermanentAddress()))
                .emergencyContact(emergencyMapper.toDTO(entity.getEmergencyContact()))
                .build();
    }


    public Beneficiary toEntity(BeneficiarySaveDTO dto) {
        if (dto == null) return null;

        return Beneficiary.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .amka(dto.amka())
                .dateOfBirth(dto.dateOfBirth())
                .isActive(dto.isActive() != null ? dto.isActive() : true)
                .houseUnit(dto.houseUnit())
                .permanentAddress(addressMapper.toEntity(dto.permanentAddress()))
                .emergencyContact(emergencyMapper.toEntity(dto.emergencyContact()))
                .build();
    }

}
