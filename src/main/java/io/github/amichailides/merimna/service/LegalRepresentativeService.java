package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;

public interface LegalRepresentativeService {
    LegalRepresentativeReadOnlyDTO createLegalRepresentative(LegalRepresentativeDTO dto);
    void assignLegalRepresentative(Long beneficiaryId, Long legalRepresentativeId);
    void unassignLegalRepresentative(Long beneficiaryId, Long legalRepresentativeId);
    LegalRepresentativeReadOnlyDTO updateLegalRepresentative(Long legalRepresentativeId, LegalRepresentativeUpdateDTO dto);
}
