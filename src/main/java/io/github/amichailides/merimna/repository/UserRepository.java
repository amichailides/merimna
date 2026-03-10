package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.model.Role;
import io.github.amichailides.merimna.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Page<User> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    boolean existsByLastNameContainingIgnoreCase(String lastName);

    Page<User> findByRolesInAndActiveTrue(Collection<Role> roles, Pageable pageable);
}
