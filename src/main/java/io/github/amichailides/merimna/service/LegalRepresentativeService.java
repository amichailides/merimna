package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.model.Beneficiary;

public interface LegalRepresentativeService {
    LegalRepresentativeReadOnlyDTO addLegalRepresentative(Long beneficiaryId, LegalRepresentativeDTO dto);
     void removeLegalRepresentative(Long beneficiaryId);
    LegalRepresentativeReadOnlyDTO updateLegalRepresentative(Long beneficiaryId, LegalRepresentativeUpdateDTO dto);
}
