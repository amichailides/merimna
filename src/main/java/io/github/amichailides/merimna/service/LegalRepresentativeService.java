package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;

public interface LegalRepresentativeService {
    LegalRepresentativeReadOnlyDTO addLegalRepresentative(Long beneficiaryId, LegalRepresentativeDTO dto);
}
