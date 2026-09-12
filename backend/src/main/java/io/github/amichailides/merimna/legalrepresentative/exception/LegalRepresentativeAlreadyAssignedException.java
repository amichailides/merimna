package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class LegalRepresentativeAlreadyAssignedException extends BaseDomainException {
    public LegalRepresentativeAlreadyAssignedException(Long legalRepresentativeId, UUID beneficiaryPublicId) {
        super(ErrorCode.LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED, Map.of(
                "legalRepresentativeId", legalRepresentativeId,
                "beneficiaryPublicId", beneficiaryPublicId
        ));
    }
}
