package io.github.amichailides.merimna.employee.systemaccess;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.employee.systemaccess.dto.EmployeeAccessDTO;
import io.github.amichailides.merimna.employee.systemaccess.exception.EmployeeAccountAlreadyExistsException;
import io.github.amichailides.merimna.employee.systemaccess.exception.NoPendingEmployeeInvitationException;
import io.github.amichailides.merimna.security.invitation.UserInvitation;
import io.github.amichailides.merimna.security.invitation.UserInvitationRepository;
import io.github.amichailides.merimna.security.invitation.UserInvitationService;
import io.github.amichailides.merimna.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeAccessServiceImpl implements EmployeeAccessService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserInvitationRepository userInvitationRepository;
    private final UserInvitationService userInvitationService;

    @Override
    @Transactional(readOnly = true)
    public EmployeeAccessDTO getAccessStatus(UUID employeePublicId) {
        Employee employee = getEmployeeOrThrow(employeePublicId);

        Optional<User> user =
                userRepository.findByEmployeePublicId(employeePublicId);

        if (user.isPresent()) {
            User existingUser = user.get();

            EmployeeAccessStatus status = existingUser.isActive()
                    ? EmployeeAccessStatus.ACTIVE
                    : EmployeeAccessStatus.SUSPENDED;

            return new EmployeeAccessDTO(
                    status,
                    existingUser.getEmail(),
                    null
            );
        }

        Instant now = Instant.now();

        Optional<UserInvitation> latest =
                userInvitationRepository
                        .findFirstByEmployeeOrderByCreatedAtDesc(employee);

        if (latest.isEmpty()) {
            return new EmployeeAccessDTO(
                    EmployeeAccessStatus.NO_ACCESS,
                    null,
                    null
            );
        }

        UserInvitation invitation = latest.get();

        if (invitation.isValid(now)) {
            return new EmployeeAccessDTO(
                    EmployeeAccessStatus.INVITATION_PENDING,
                    invitation.getAccountEmail(),
                    invitation.getExpiresAt()
            );
        }

        if (
                invitation.isExpired(now)
                        && !invitation.isAccepted()
                        && !invitation.isRevoked()
        ) {
            return new EmployeeAccessDTO(
                    EmployeeAccessStatus.INVITATION_EXPIRED,
                    invitation.getAccountEmail(),
                    invitation.getExpiresAt()
            );
        }

        return new EmployeeAccessDTO(
                EmployeeAccessStatus.NO_ACCESS,
                null,
                null
        );
    }

    @Override
    @Transactional
    public void resendInvitation(UUID employeePublicId) {
        EmployeeAccessDTO current =
                getAccessStatus(employeePublicId);

        if (
                current.status() == EmployeeAccessStatus.ACTIVE
                        || current.status() == EmployeeAccessStatus.SUSPENDED
        ) {
            throw new EmployeeAccountAlreadyExistsException(
                    employeePublicId
            );
        }

        if (current.status() == EmployeeAccessStatus.NO_ACCESS) {
            throw new NoPendingEmployeeInvitationException(
                    employeePublicId
            );
        }

        userInvitationService.createForEmployee(
                employeePublicId,
                current.accountEmail()
        );
    }

    @Override
    @Transactional
    public void cancelInvitation(UUID employeePublicId) {
        Employee employee = getEmployeeOrThrow(employeePublicId);
        Instant now = Instant.now();

        UserInvitation invitation =
                userInvitationRepository
                        .findFirstByEmployeeOrderByCreatedAtDesc(employee)
                        .filter(candidate -> candidate.isValid(now))
                        .orElseThrow(
                                () ->
                                        new NoPendingEmployeeInvitationException(
                                                employeePublicId
                                        )
                        );

        invitation.revoke(now);
    }

    private Employee getEmployeeOrThrow(UUID employeePublicId) {
        return employeeRepository
                .findByPublicId(employeePublicId)
                .orElseThrow(
                        () ->
                                new EmployeeNotFoundByPublicIdException(
                                        employeePublicId
                                )
                );
    }
}