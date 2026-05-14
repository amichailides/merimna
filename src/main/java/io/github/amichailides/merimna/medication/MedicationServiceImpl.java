package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.access.HouseUnitAccessService;
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
public class MedicationServiceImpl implements MedicationService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final MedicationMapper medicationMapper;
    private final MedicationRepository medicationRepository;
    private final HouseUnitAccessService houseUnitAccessService;

    @Override
    @Transactional
    public MedicationReadOnlyDTO addMedication(UUID beneficiaryPublicId, MedicationCreateDTO dto) {
        Beneficiary beneficiary = getAccessibleBeneficiaryOrThrow(beneficiaryPublicId);

        Medication medication = medicationMapper.toEntity(dto);
        beneficiary.addMedication(medication);
        medicationRepository.save(medication);

        return medicationMapper.toDTO(medication);
    }

    @Override
    @Transactional
    public MedicationReadOnlyDTO updateMedication(
            UUID beneficiaryPublicId,
            UUID medicationPublicId,
            MedicationUpdateDTO dto) {

        getAccessibleBeneficiaryOrThrow(beneficiaryPublicId);
        Medication existing = getMedicationOrThrow(medicationPublicId, beneficiaryPublicId);

        // TODO(#14): MedicationValidator - business rules (e.g. drug interactions,
        // max dosage based on age/weight, or inactive beneficiary restrictions)
        medicationMapper.updateEntity(existing, dto);

        return medicationMapper.toDTO(existing);
    }

    @Override
    @Transactional
    public void deleteMedication(UUID beneficiaryPublicId, UUID medicationPublicId) {
        Beneficiary beneficiary = getAccessibleBeneficiaryOrThrow(beneficiaryPublicId);
        Medication medication = getMedicationOrThrow(medicationPublicId, beneficiaryPublicId);

        beneficiary.removeMedication(medication);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicationReadOnlyDTO getMedicationByPublicId(UUID beneficiaryPublicId, UUID medicationPublicId) {
        getAccessibleBeneficiaryOrThrow(beneficiaryPublicId);

        return medicationMapper.toDTO(getMedicationOrThrow(medicationPublicId, beneficiaryPublicId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(UUID beneficiaryPublicId) {
        getAccessibleBeneficiaryOrThrow(beneficiaryPublicId);

        return medicationRepository.findAllByBeneficiaryPublicId(beneficiaryPublicId)
                .stream()
                .map(medicationMapper::toDTO)
                .toList();
    }

    private Beneficiary getBeneficiaryOrThrow(UUID beneficiaryPublicId) {
        return beneficiaryRepository.findByPublicId(beneficiaryPublicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(beneficiaryPublicId));
    }

    private Beneficiary getAccessibleBeneficiaryOrThrow(UUID beneficiaryPublicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);
        houseUnitAccessService.ensureCanAccess(beneficiary);

        return beneficiary;
    }

    private Medication getMedicationOrThrow(UUID medicationPublicId, UUID beneficiaryPublicId) {
        return medicationRepository.findMedicationByPublicIdAndBeneficiaryPublicId(medicationPublicId, beneficiaryPublicId)
                .orElseThrow(() -> {
                    if (!medicationRepository.existsByPublicId(medicationPublicId)) {
                        return new MedicationNotFoundException(medicationPublicId);
                    }
                    return new MedicationNotOwnedByBeneficiaryException(medicationPublicId, beneficiaryPublicId);
                });
    }
}
