package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmployeeId(Long employeeId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
    SELECT u FROM User u
    JOIN FETCH u.employee e
    JOIN FETCH e.position p
    JOIN FETCH p.permissions
    WHERE u.email = :email
""")
    Optional<User> findByEmailWithPermissions(String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    boolean existsByEmailAndIdNot(String email, Long userId);
}
