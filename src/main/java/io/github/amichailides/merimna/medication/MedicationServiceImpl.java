package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.medication.exception.MedicationNotFoundException;
import io.github.amichailides.merimna.medication.exception.MedicationNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.Medication;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
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

        Medication existing = getMedicationOrThrow(medicationId, beneficiaryId);

        // TODO: MedicationValidator - business rules π.χ. conflict μεταξύ φαρμάκων,
        //  max δόση ανά ηλικία/βάρος, ή αν ο ωφελούμενος είναι ανενεργός
        medicationMapper.updateEntity(existing, dto);
        return medicationMapper.toDTO(existing); // managed λόγω @Transactional

    }

    @Transactional
    public void deleteMedication (Long beneficiaryId, Long medicationId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        Medication medication = getMedicationOrThrow(medicationId, beneficiaryId);

        beneficiary.removeMedication(medication);
    }

    @Transactional(readOnly = true)
    public MedicationReadOnlyDTO getMedication(Long beneficiaryId, Long medicationId) {

        return medicationMapper.toDTO(getMedicationOrThrow(medicationId, beneficiaryId));
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

    private Medication getMedicationOrThrow (Long medicationId, Long beneficiaryId) {
        return  medicationRepository.findByIdAndBeneficiaryId(medicationId, beneficiaryId)
                .orElseThrow(() -> {
                    if (!medicationRepository.existsById(medicationId)) {
                        return new MedicationNotFoundException(medicationId);
                    }
                    return new MedicationNotOwnedByBeneficiaryException(medicationId, beneficiaryId);
                });

    }
}
