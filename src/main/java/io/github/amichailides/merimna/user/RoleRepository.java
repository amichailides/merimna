package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.Role;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepositoryImplementation<Role, Long> {
}
