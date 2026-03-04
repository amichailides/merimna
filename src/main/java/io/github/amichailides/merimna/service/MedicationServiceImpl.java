package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.exception.MedicationNotFoundById;
import io.github.amichailides.merimna.exception.MedicationNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.mapper.MedicationMapper;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.Medication;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import io.github.amichailides.merimna.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService{
    private final BeneficiaryRepository beneficiaryRepository;
    private final MedicationMapper medicationMapper;
    private final MedicationRepository medicationRepository;

    @Transactional
    public MedicationReadOnlyDTO addMedication (Long beneficiaryId, MedicationCreateDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        Medication medication = medicationMapper.toEntity(dto);
        beneficiary.addMedication(medication);
        medicationRepository.save(medication); // για να πάρει id
        return medicationMapper.toDTO(medication);
    }

    @Transactional
    public MedicationReadOnlyDTO updateMedication(
            Long beneficiaryId,
            Long medicationId,
            MedicationUpdateDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        Medication existing = getMedicationOrThrow(beneficiary, medicationId);

        // TODO: MedicationValidator - business rules π.χ. conflict μεταξύ φαρμάκων,
        //  max δόση ανά ηλικία/βάρος, ή αν ο ωφελούμενος είναι ανενεργός
        medicationMapper.updateEntity(existing, dto);
        return medicationMapper.toDTO(existing); // managed λόγω @Transactional

    }

    @Transactional
    public void deleteMedication (Long beneficiaryId, Long medicationId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        Medication medication = getMedicationOrThrow(beneficiary, medicationId);

        beneficiary.removeMedication(medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        return beneficiary.getMedications().stream()
                .map(medicationMapper::toDTO)
                .toList();
    }

    private Beneficiary getBeneficiaryOrThrow (Long beneficiaryId) {
        return  beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

    // TODO [Polish]: Consider lazy-safe retrieval for medications
    // Currently we stream the lazy-loaded set from beneficiary,
    // which triggers a SELECT for all medications.
    // consider using repository query:
    // findByIdAndBeneficiaryId(medicationId, beneficiaryId)
    private Medication getMedicationOrThrow (Beneficiary beneficiary, Long medicationId) {
        return  beneficiary.getMedications().stream()
                .filter(m -> m.getId().equals(medicationId))
                .findFirst()
                .orElseThrow(() ->
                        new MedicationNotOwnedByBeneficiaryException(medicationId, beneficiary.getId()));
    }
}
