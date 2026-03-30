package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.address.AddressMapper;
import io.github.amichailides.merimna.allergy.AllergyMapper;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryDetailsDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryListDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.legalrepresentative.LegalRepresentativeMapper;
import io.github.amichailides.merimna.medication.MedicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BeneficiaryMapper {
    private final AddressMapper addressMapper;
    private final EmergencyContactMapper emergencyMapper;
    private final AllergyMapper allergyMapper;
    private final MedicationMapper medicationMapper;
    private final LegalRepresentativeMapper legalRepresentativeMapper;

    public BeneficiaryDetailsDTO toDetailsDTO(Beneficiary entity) {
        if (entity == null) return null;

        return BeneficiaryDetailsDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .amka(entity.getAmka())
                .dateOfBirth(entity.getDateOfBirth())
                .isActive(entity.isActive())
                .houseUnitCode(entity.getHouseUnit().getCode())
                .houseUnitDisplayName(entity.getHouseUnit().getDisplayName())
                .permanentAddress(addressMapper.toDTO(entity.getPermanentAddress()))
                .emergencyContact(emergencyMapper.toDTO(entity.getEmergencyContact()))
                .medications(entity.getMedications()
                        .stream()
                        .map(medicationMapper::toDTO).toList())
                .allergies(entity.getAllergies().stream()
                        .map(allergyMapper::toDTO)
                        .toList())
                .legalRepresentatives(entity.getLegalRepresentatives().stream()
                        .map(legalRepresentativeMapper::toReadOnlyDTO)
                        .toList())
                .build();
    }

    public BeneficiaryListDTO toListDTO(Beneficiary entity) {
        if (entity == null) return null;

        return BeneficiaryListDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .isActive(entity.isActive())
                .houseUnit(entity.getHouseUnit().getCode())
                .build();
    }

    public Beneficiary toEntity(BeneficiarySaveDTO dto, HouseUnit houseUnit) {
        if (dto == null) return null;

        return Beneficiary.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .amka(dto.amka())
                .dateOfBirth(dto.dateOfBirth())
                .houseUnit(houseUnit)
                .permanentAddress(addressMapper.toEntity(dto.permanentAddress()))
                .emergencyContact(emergencyMapper.toEntity(dto.emergencyContact()))
                .build();
    }

    public void updateEntity(Beneficiary existing, BeneficiaryUpdateDTO dto) {
        if (dto == null || existing == null) return;


        if (dto.firstName() != null) existing.setFirstName(dto.firstName());
        if (dto.lastName() != null) existing.setLastName(dto.lastName());
        if (dto.amka() != null) existing.setAmka(dto.amka());
        if (dto.dateOfBirth() != null) existing.setDateOfBirth(dto.dateOfBirth());

        if (dto.permanentAddress() != null) {
            addressMapper.updateEntity(existing.getPermanentAddress(), dto.permanentAddress());
        }

        if (dto.emergencyContact() != null) {
            emergencyMapper.updateEntity(existing.getEmergencyContact(), dto.emergencyContact());
        }

    }

}
