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
        Medication existing = getMedicationOrThrow(medicationId);

        if(!beneficiary.belongsToThisBeneficiary(existing)) {
            throw new MedicationNotOwnedByBeneficiaryException(medicationId, beneficiaryId);
        }

        // TODO: MedicationValidator - business rules π.χ. conflict μεταξύ φαρμάκων,
        //  max δόση ανά ηλικία/βάρος, ή αν ο ωφελούμενος είναι ανενεργός
        medicationMapper.updateEntity(existing, dto);
        return medicationMapper.toDTO(existing); // managed λόγω @Transactional

    }

    @Transactional
    public void deleteMedication (Long beneficiaryId, Long medicationId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        Medication medication = getMedicationOrThrow(medicationId);

        if (!beneficiary.belongsToThisBeneficiary(medication)) {
            throw new MedicationNotOwnedByBeneficiaryException(medicationId, beneficiaryId);
        }
        beneficiary.removeMedication(medication);
    }

    private Beneficiary getBeneficiaryOrThrow (Long beneficiaryId) {
        return  beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

    private Medication getMedicationOrThrow (Long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundById(medicationId));
    }
}
