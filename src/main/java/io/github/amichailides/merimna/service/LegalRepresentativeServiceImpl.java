package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.exception.*;
import io.github.amichailides.merimna.mapper.LegalRepresentativeMapper;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.LegalRepresentative;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import io.github.amichailides.merimna.repository.LegalRepresentativeRepository;
import io.github.amichailides.merimna.service.validation.LegalRepresentativeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LegalRepresentativeServiceImpl implements LegalRepresentativeService{
    private final BeneficiaryRepository beneficiaryRepository;
    private final LegalRepresentativeMapper legalRepresentativeMapper;
    private final LegalRepresentativeValidator legalRepresentativeValidator;
    private final LegalRepresentativeRepository legalRepresentativeRepository;

    @Transactional
    public LegalRepresentativeReadOnlyDTO createLegalRepresentative(LegalRepresentativeDTO dto) {
        LegalRepresentative legal = legalRepresentativeMapper.toEntity(dto);
        legalRepresentativeRepository.save(legal);
        return legalRepresentativeMapper.toReadOnlyDTO(legal);
    }

    @Transactional
    public void assignLegalRepresentative(Long beneficiaryId, Long legalRepresentativeId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        LegalRepresentative legal = getLegalRepresentativeOrThrow(legalRepresentativeId);

        if (beneficiary.getLegalRepresentatives().contains(legal)) {
            throw new LegalRepresentativeAlreadyAssignedException(legalRepresentativeId, beneficiaryId);
        }

        beneficiary.addLegalRepresentative(legal);
    }

    @Transactional
    public void unassignLegalRepresentative(Long beneficiaryId, Long legalRepresentativeId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        LegalRepresentative legalRepresentative = getLegalRepresentativeOrThrow(legalRepresentativeId, beneficiaryId);

        beneficiary.removeLegalRepresentative(legalRepresentative);
    }

    @Transactional
    public LegalRepresentativeReadOnlyDTO updateLegalRepresentative(
            Long legalRepresentativeId,
            LegalRepresentativeUpdateDTO dto) {

        LegalRepresentative existing = getLegalRepresentativeOrThrow(legalRepresentativeId);
        legalRepresentativeValidator.validateForUpdate(existing, dto);

        legalRepresentativeMapper.updateEntity(existing, dto);
        return legalRepresentativeMapper.toReadOnlyDTO(existing);
    }

    private Beneficiary getBeneficiaryOrThrow (Long beneficiaryId) {
        return  beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

    // remove - legalId + beneficiaryId check
    private LegalRepresentative getLegalRepresentativeOrThrow(Long legalRepresentativeId, Long beneficiaryId) {
        return legalRepresentativeRepository.findByIdAndBeneficiariesId(legalRepresentativeId, beneficiaryId)
                .orElseThrow(() -> {
                    if (!legalRepresentativeRepository.existsById(legalRepresentativeId)) {
                        return new LegalRepresentativeNotFoundByIdException(legalRepresentativeId);
                    }
                    return new LegalRepresentativeNotAssignedException(legalRepresentativeId, beneficiaryId);
                });
    }

    // Update μόνο legalId
    private LegalRepresentative getLegalRepresentativeOrThrow(Long legalRepresentativeId) {
        return legalRepresentativeRepository.findById(legalRepresentativeId)
                .orElseThrow(() -> new LegalRepresentativeNotFoundByIdException(legalRepresentativeId));
    }
}
