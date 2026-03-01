package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.model.Allergy;
import io.github.amichailides.merimna.model.Beneficiary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BeneficiaryMapper {
    private final AddressMapper addressMapper;
    private final EmergencyContactMapper emergencyMapper;
    private final AllergyMapper allergyMapper;
    private final MedicationMapper medicationMapper;

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
                .medicalTreatment(entity.getMedicalTreatment().stream()
                        .map(medicationMapper::toDTO).toList())
                .allergies(entity.getAllergies().stream()
                        .map(allergyMapper::toDTO).toList())
                .build();
    }

    public Beneficiary toEntity(BeneficiarySaveDTO dto) {
        if (dto == null) return null;

        Beneficiary beneficiary = Beneficiary.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .amka(dto.amka())
                .dateOfBirth(dto.dateOfBirth())
                .isActive(dto.isActive() != null ? dto.isActive() : true)
                .houseUnit(dto.houseUnit())
                .permanentAddress(addressMapper.toEntity(dto.permanentAddress()))
                .emergencyContact(emergencyMapper.toEntity(dto.emergencyContact()))
                .build();

        //  Προσθήκη Φαρμάκων
        if (dto.medicalTreatment() != null) {
            dto.medicalTreatment().stream()
                    .map(medicationMapper::toEntity)
                    .forEach(beneficiary.getMedicalTreatment()::add);
        }

        //  Προσθήκη Αλλεργιών
        if (dto.allergies() != null) {
            dto.allergies().forEach(aDto -> {
                // Εδώ καλούμε τον allergyMapper
                Allergy allergy = allergyMapper.toEntity(aDto);
                beneficiary.addAllergy(allergy);
            });
        }

        return beneficiary;
    }

    public void updateEntity(BeneficiaryUpdateDTO dto, Beneficiary existing) {
        if (dto == null || existing == null) return;


        if (dto.firstName() != null) existing.setFirstName(dto.firstName());
        if (dto.lastName() != null) existing.setLastName(dto.lastName());
        if (dto.amka() != null) existing.setAmka(dto.amka());
        if (dto.dateOfBirth() != null) existing.setDateOfBirth(dto.dateOfBirth());
        if (dto.houseUnit() != null) existing.setHouseUnit(dto.houseUnit());

        if (dto.permanentAddress() != null) {
            existing.setPermanentAddress(addressMapper.toEntity(dto.permanentAddress()));
        }
        if (dto.emergencyContact() != null) {
            existing.setEmergencyContact(emergencyMapper.toEntity(dto.emergencyContact()));
        }
        // Update Medical Treatment (Embeddables - Replace list)
        if (dto.medicalTreatment() != null) {
            existing.getMedicalTreatment().clear();
            dto.medicalTreatment().stream()
                    .map(medicationMapper::toEntity)
                    .forEach(existing.getMedicalTreatment()::add);
        }

    }

}
