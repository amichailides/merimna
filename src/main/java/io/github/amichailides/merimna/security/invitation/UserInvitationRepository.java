package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInvitationRepository
        extends JpaRepository<UserInvitation, Long> {

    Optional<UserInvitation> findByTokenHash(String tokenHash);

    Optional<UserInvitation> findFirstByEmployeeOrderByCreatedAtDesc(
            Employee employee
    );
}