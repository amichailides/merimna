package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.security.event.UserInvitationCreatedEvent;
import io.github.amichailides.merimna.security.exception.InvalidUserInvitationException;
import io.github.amichailides.merimna.security.token.OpaqueTokenGenerator;
import io.github.amichailides.merimna.security.token.TokenHasher;
import io.github.amichailides.merimna.user.UserService;
import io.github.amichailides.merimna.user.dto.UserCreateDTO;
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
    private final UserService userService;
    private final AccountEmailAvailabilityValidator accountEmailAvailabilityValidator;

    @Override
    @Transactional
    public void createForEmployee(
            UUID employeePublicId,
            String accountEmail
    ) {
        Employee employee = employeeRepository.findByPublicId(employeePublicId)
                .orElseThrow(() ->
                        new EmployeeNotFoundByPublicIdException(employeePublicId)
                );

        Instant now = Instant.now();

        accountEmailAvailabilityValidator.validate(
                accountEmail,
                employee,
                now
        );

        userInvitationRepository
                .findFirstByEmployeeOrderByCreatedAtDesc(employee)
                .filter(invitation -> invitation.isValid(now))
                .ifPresent(invitation -> invitation.revoke(now));

        String rawToken = opaqueTokenGenerator.generate();
        String tokenHash = tokenHasher.hash(rawToken);

        UserInvitation invitation = UserInvitation.createFor(
                employee,
                accountEmail,
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

    @Override
    @Transactional
    public void acceptInvitation(
            String rawToken,
            String username,
            String password
    ) {
        String tokenHash = tokenHasher.hash(rawToken);

        UserInvitation invitation = userInvitationRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(InvalidUserInvitationException::new);

        Instant now = Instant.now();

        if (!invitation.isValid(now)) {
            throw new InvalidUserInvitationException();
        }

        Employee employee = invitation.getEmployee();

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .employeePublicId(employee.getPublicId())
                .username(username)
                .email(invitation.getAccountEmail())
                .password(password)
                .role(Role.STAFF)
                .build();

        userService.create(userCreateDTO);

        invitation.accept(now);
    }
}