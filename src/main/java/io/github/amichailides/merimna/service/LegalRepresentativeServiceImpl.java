package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.exception.BeneficiaryAlreadyHasLegalRepresentativeException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.LegalRepresentativeMapper;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LegalRepresentativeServiceImpl implements LegalRepresentativeService{
    private final BeneficiaryRepository beneficiaryRepository;
    private final LegalRepresentativeMapper legalRepresentativeMapper;

    public LegalRepresentativeReadOnlyDTO addLegalRepresentative (Long beneficiaryId, LegalRepresentativeDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        // Can only be added once, use Patch to update
        if (beneficiary.getLegalRepresentative() != null) {
            throw new BeneficiaryAlreadyHasLegalRepresentativeException(beneficiaryId);
        }
        beneficiary.addLegalRepresentative(legalRepresentativeMapper.toEntity(dto));

        beneficiaryRepository.save(beneficiary);
        return legalRepresentativeMapper.toDTO(beneficiary.getLegalRepresentative());
    }

    private Beneficiary getBeneficiaryOrThrow (Long beneficiaryId) {
        return  beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }
}
