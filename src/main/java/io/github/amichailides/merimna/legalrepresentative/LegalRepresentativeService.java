package io.github.amichailides.merimna.legalrepresentative;

import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeCreateDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeUpdateDTO;

import java.util.UUID;

public interface LegalRepresentativeService {
    LegalRepresentativeReadOnlyDTO createLegalRepresentative(LegalRepresentativeCreateDTO dto);

    void assignToBeneficiary(UUID beneficiaryPublicId, Long legalRepresentativeId);

    void unassignLegalRepresentative(UUID beneficiaryPublicId, Long legalRepresentativeId);

    LegalRepresentativeReadOnlyDTO updateLegalRepresentative(Long legalRepresentativeId, LegalRepresentativeUpdateDTO dto);

    LegalRepresentativeReadOnlyDTO getLegalRepresentativeById(Long legalRepresentativeId);
}
