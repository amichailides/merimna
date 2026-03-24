package io.github.amichailides.merimna.service.validation;

import io.github.amichailides.merimna.common.ErrorCode;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.domain.LegalRepresentative;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LegalRepresentativeValidator {
    public void validateForUpdate(LegalRepresentative existing, LegalRepresentativeUpdateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        String finalMobileNumber = (dto.mobileNumber() != null) ? dto.mobileNumber() : existing.getMobileNumber();
        String finalLandLinePhone = (dto.landlinePhone() != null) ? dto.landlinePhone() : existing.getLandlinePhone();

        boolean hasMobile = finalMobileNumber != null && !finalMobileNumber.isBlank();
        boolean hasLandline = finalLandLinePhone != null && !finalLandLinePhone.isBlank();

        if (!hasMobile && !hasLandline)  {
            errors.put("contact", ErrorCode.AT_LEAST_ONE_PHONE_REQUIRED.getMessageKey());
        }

        if (!errors.isEmpty()) {
            throw new DomainValidationException(errors);
        }
    }
}