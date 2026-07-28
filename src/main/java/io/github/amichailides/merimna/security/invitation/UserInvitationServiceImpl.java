package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.security.event.UserInvitationCreatedEvent;
import io.github.amichailides.merimna.security.token.OpaqueTokenGenerator;
import io.github.amichailides.merimna.security.token.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInvitationServiceImpl implements UserInvitationService {

    private final EmployeeRepository employeeRepository;
    private final UserInvitationRepository userInvitationRepository;
    private final OpaqueTokenGenerator opaqueTokenGenerator;
    private final TokenHasher tokenHasher;
    private final SecurityProperties securityProperties;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createForEmployee(UUID employeePublicId) {
        Employee employee = employeeRepository.findByPublicId(employeePublicId)
                .orElseThrow(() ->
                        new EmployeeNotFoundByPublicIdException(employeePublicId)
                );

        Instant now = Instant.now();

        userInvitationRepository
                .findFirstByEmployeeOrderByCreatedAtDesc(employee)
                .filter(invitation -> invitation.isValid(now))
                .ifPresent(invitation -> invitation.revoke(now));

        String rawToken = opaqueTokenGenerator.generate();
        String tokenHash = tokenHasher.hash(rawToken);

        UserInvitation invitation = UserInvitation.createFor(
                employee,
                tokenHash,
                now,
                now.plus(securityProperties.getInvitation().getExpiration())
        );

        userInvitationRepository.save(invitation);

        eventPublisher.publishEvent(
                new UserInvitationCreatedEvent(
                        employee.getContactEmail(),
                        rawToken
                )
        );
    }
}