package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.medication.exception.MedicationNotFoundException;
import io.github.amichailides.merimna.medication.exception.MedicationNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.Medication;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService{
    private final BeneficiaryRepository beneficiaryRepository;
    private final MedicationMapper medicationMapper;
    private final MedicationRepository medicationRepository;

    @Transactional
    public MedicationReadOnlyDTO addMedication (UUID beneficiaryPublicId, MedicationCreateDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);

        Medication medication = medicationMapper.toEntity(dto);
        beneficiary.addMedication(medication);
        medicationRepository.save(medication); // για να πάρει id
        return medicationMapper.toDTO(medication);
    }

    @Transactional
    public MedicationReadOnlyDTO updateMedication(
            UUID beneficiaryPublicId,
            Long medicationId,
            MedicationUpdateDTO dto) {

        Medication existing = getMedicationOrThrow(medicationId, beneficiaryPublicId);

        // TODO(#14): MedicationValidator - business rules (e.g. drug interactions,
        // max dosage based on age/weight, or inactive beneficiary restrictions)
        medicationMapper.updateEntity(existing, dto);
        return medicationMapper.toDTO(existing); // managed λόγω @Transactional

    }

    @Transactional
    public void deleteMedication (UUID beneficiaryPublicId, Long medicationId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);
        Medication medication = getMedicationOrThrow(medicationId, beneficiaryPublicId);

        beneficiary.removeMedication(medication);
    }

    @Transactional(readOnly = true)
    public MedicationReadOnlyDTO getMedication(UUID beneficiaryPublicId, Long medicationId) {

        return medicationMapper.toDTO(getMedicationOrThrow(medicationId, beneficiaryPublicId));
    }

    @Transactional(readOnly = true)
    public List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(UUID beneficiaryPublicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);

        return beneficiary.getMedications().stream()
                .map(medicationMapper::toDTO)
                .toList();
    }

    private Beneficiary getBeneficiaryOrThrow (UUID beneficiaryPublicId) {
        return  beneficiaryRepository.findByPublicId(beneficiaryPublicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(beneficiaryPublicId));
    }

    private Medication getMedicationOrThrow(Long medicationId, UUID beneficiaryPublicId) {
        return medicationRepository.findByIdAndBeneficiaryPublicId(medicationId, beneficiaryPublicId)
                .orElseThrow(() -> {
                    if (!medicationRepository.existsById(medicationId)) {
                        return new MedicationNotFoundException(medicationId);
                    }
                    return new MedicationNotOwnedByBeneficiaryException(medicationId, beneficiaryPublicId);
                });
    }
}
