package io.github.amichailides.merimna.legalrepresentative;

import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeUpdateDTO;

public interface LegalRepresentativeService {
    LegalRepresentativeReadOnlyDTO createLegalRepresentative(LegalRepresentativeDTO dto);

    void assignToBeneficiary(Long beneficiaryId, Long legalRepresentativeId);

    void unassignLegalRepresentative(Long beneficiaryId, Long legalRepresentativeId);

    LegalRepresentativeReadOnlyDTO updateLegalRepresentative(Long legalRepresentativeId, LegalRepresentativeUpdateDTO dto);

    LegalRepresentativeReadOnlyDTO getLegalRepresentativeById(Long legalRepresentativeId);
}
