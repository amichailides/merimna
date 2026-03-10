package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.exception.BeneficiaryAlreadyHasLegalRepresentativeException;
import io.github.amichailides.merimna.exception.BeneficiaryHasNoLegalRepresentativeException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.LegalRepresentativeMapper;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.LegalRepresentative;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
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

    @Transactional
    public LegalRepresentativeReadOnlyDTO addLegalRepresentative (Long beneficiaryId, LegalRepresentativeDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        // Can only be added once, use Patch to update
        if (beneficiary.getLegalRepresentative() != null) {
            throw new BeneficiaryAlreadyHasLegalRepresentativeException(beneficiaryId);
        }
        beneficiary.addLegalRepresentative(legalRepresentativeMapper.toEntity(dto));

        return legalRepresentativeMapper.toReadOnlyDTO(beneficiary.getLegalRepresentative());
    }

    @Transactional
    public void removeLegalRepresentative(Long beneficiaryId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);
        beneficiary.removeLegalRepresentative();
    }

    @Transactional
    public LegalRepresentativeReadOnlyDTO updateLegalRepresentative(
            Long beneficiaryId,
            LegalRepresentativeUpdateDTO dto) {
        Beneficiary existing = getBeneficiaryOrThrow(beneficiaryId);

        if (existing.getLegalRepresentative() == null) {
            throw new BeneficiaryHasNoLegalRepresentativeException(beneficiaryId);
        }

        LegalRepresentative existingLegal = existing.getLegalRepresentative();
        legalRepresentativeValidator.validateForUpdate(existingLegal, dto);

        legalRepresentativeMapper.updateEntity(existingLegal, dto);
        return legalRepresentativeMapper.toReadOnlyDTO(existingLegal);
    }

    private Beneficiary getBeneficiaryOrThrow (Long beneficiaryId) {
        return  beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }
}
