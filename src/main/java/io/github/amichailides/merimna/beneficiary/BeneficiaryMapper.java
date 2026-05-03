package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.address.AddressMapper;
import io.github.amichailides.merimna.allergy.AllergyMapper;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryDetailsDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryListDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryCreateDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.legalrepresentative.LegalRepresentativeMapper;
import io.github.amichailides.merimna.medication.MedicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;


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

        Employee dischargedBy = entity.getDischargedBy();

        return BeneficiaryDetailsDTO.builder()
                .publicId(entity.getPublicId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .amka(entity.getAmka())
                .dateOfBirth(entity.getDateOfBirth())
                .isActive(entity.isActive())
                .dischargeDate(entity.getDischargeDate())
                .dischargeReason(entity.getDischargeReason())
                .dischargedByEmployeePublicId(
                        dischargedBy != null ? dischargedBy.getPublicId() : null
                )
                .dischargedByEmployeeFullName(
                        dischargedBy != null
                                ? dischargedBy.getFirstName() + " " + dischargedBy.getLastName()
                                : null
                )
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
                .publicId(entity.getPublicId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .isActive(entity.isActive())
                .houseUnit(entity.getHouseUnit().getCode())
                .build();
    }

    public Beneficiary toEntity(BeneficiaryCreateDTO dto, HouseUnit houseUnit) {
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
        Objects.requireNonNull(existing, "existing beneficiary must not be null");
        Objects.requireNonNull(dto, "beneficiary update dto must not be null");

        updateIfNotBlank(dto.firstName(), existing::setFirstName);
        updateIfNotBlank(dto.lastName(), existing::setLastName);
        updateIfNotBlank(dto.amka(), existing::setAmka);
        updateIfNotNull(dto.dateOfBirth(), existing::setDateOfBirth);

        updateIfNotNull(
                dto.permanentAddress(),
                addr -> addressMapper.updateEntity(existing.getPermanentAddress(), addr)
        );

        updateIfNotNull(
                dto.emergencyContact(),
                ec -> emergencyMapper.updateEntity(existing.getEmergencyContact(), ec)
        );
    }

    private void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }
}
