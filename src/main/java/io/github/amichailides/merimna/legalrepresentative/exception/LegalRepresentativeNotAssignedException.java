package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class LegalRepresentativeNotAssignedException extends BaseDomainException {
    public LegalRepresentativeNotAssignedException(Long legalRepresentativeId, UUID beneficiaryPublicId) {
        super(ErrorCode.LEGAL_REPRESENTATIVE_NOT_ASSIGNED, Map.of(
                "legalRepresentativeId", legalRepresentativeId,
                "beneficiaryPublicId", beneficiaryPublicId
        ));
    }
}
