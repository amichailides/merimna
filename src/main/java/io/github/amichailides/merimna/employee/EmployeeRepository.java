package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    @EntityGraph(attributePaths = {
            "assignments",
            "assignments.houseUnit"
    })
    Optional<Employee> findWithDetailsByPublicId(UUID publicId);

    Optional<Employee> findByPublicId(UUID publicId);

    boolean existsByContactEmailIgnoreCase(String email);
}
