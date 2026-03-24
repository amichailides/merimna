package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.domain.UserRole;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepositoryImplementation<Role, Long> {
    Optional<Role> findByName(UserRole name);

}
