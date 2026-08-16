package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccountEmailAvailabilityValidator {

    private final UserRepository userRepository;
    private final UserInvitationRepository userInvitationRepository;

    public void validate(
            String accountEmail,
            Employee employee,
            Instant now
    ) {
        boolean conflict = userRepository.existsByEmail(accountEmail)
                || userInvitationRepository
                .existsByAccountEmailAndEmployeeNotAndAcceptedAtIsNullAndRevokedAtIsNullAndExpiresAtAfter(
                        accountEmail,
                        employee,
                        now
                );

        if (conflict) {
            throw new ConflictValidationException(
                    Map.of(
                            "systemAccess.accountEmail",
                            ErrorCode.EMAIL_ALREADY_EXISTS.getMessageKey()
                    )
            );
        }
    }
}