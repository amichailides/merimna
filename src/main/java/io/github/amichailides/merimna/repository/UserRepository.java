package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.model.Role;
import io.github.amichailides.merimna.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByLastNameContainingIgnoreCase(String lastName);

    boolean existsByLastNameContainingIgnoreCase(String lastName);

    List<User> findByRolesInAndActiveTrue(Collection<Role> roles);
}
