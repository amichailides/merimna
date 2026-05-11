package io.github.amichailides.merimna.legalrepresentative;

import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeCreateDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.LegalRepresentative;
import io.github.amichailides.merimna.legalrepresentative.exception.LegalRepresentativeNotAssignedException;
import io.github.amichailides.merimna.legalrepresentative.exception.LegalRepresentativeNotFoundByIdException;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LegalRepresentativeServiceImpl implements LegalRepresentativeService{
    private final BeneficiaryRepository beneficiaryRepository;
    private final LegalRepresentativeMapper legalRepresentativeMapper;
    private final LegalRepresentativeValidator legalRepresentativeValidator;
    private final LegalRepresentativeRepository legalRepresentativeRepository;

    @Transactional
    public LegalRepresentativeReadOnlyDTO createLegalRepresentative(LegalRepresentativeCreateDTO dto) {
        LegalRepresentative legal = legalRepresentativeMapper.toEntity(dto);
        legalRepresentativeRepository.save(legal);
        return legalRepresentativeMapper.toReadOnlyDTO(legal);
    }

    @Transactional
    public void assignToBeneficiary(UUID beneficiaryPublicId, Long legalRepresentativeId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);
        LegalRepresentative legal = getLegalRepresentativeOrThrow(legalRepresentativeId);

        // TODO(#12): Add domain validation for legal representative assignment
        beneficiary.addLegalRepresentative(legal);
    }

    @Transactional
    public void unassignLegalRepresentative(UUID beneficiaryPublicId, Long legalRepresentativeId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);
        LegalRepresentative legalRepresentative = getLegalRepresentativeOrThrow(legalRepresentativeId, beneficiaryPublicId);

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

    @Override
    @Transactional(readOnly = true)
    public LegalRepresentativeReadOnlyDTO getLegalRepresentativeById(Long legalRepresentativeId) {
        LegalRepresentative legal = getLegalRepresentativeOrThrow(legalRepresentativeId);

        return legalRepresentativeMapper.toReadOnlyDTO(legal);
    }

    private Beneficiary getBeneficiaryOrThrow (UUID publicId) {
        return  beneficiaryRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));
    }

    // remove - legalId + beneficiaryId check
    private LegalRepresentative getLegalRepresentativeOrThrow(Long legalRepresentativeId, UUID beneficiaryPublicId) {
        return legalRepresentativeRepository.findByIdAndBeneficiariesPublicId(legalRepresentativeId, beneficiaryPublicId)
                .orElseThrow(() -> {
                    if (!legalRepresentativeRepository.existsById(legalRepresentativeId)) {
                        return new LegalRepresentativeNotFoundByIdException(legalRepresentativeId);
                    }
                    return new LegalRepresentativeNotAssignedException(legalRepresentativeId, beneficiaryPublicId);
                });
    }

    // Update μόνο legalId
    private LegalRepresentative getLegalRepresentativeOrThrow(Long legalRepresentativeId) {
        return legalRepresentativeRepository.findById(legalRepresentativeId)
                .orElseThrow(() -> new LegalRepresentativeNotFoundByIdException(legalRepresentativeId));
    }
}
