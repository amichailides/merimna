package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    @EntityGraph(attributePaths = {"houseUnits"})
    Optional<Employee> findWithDetailsById(Long id);

    @NonNull
    @EntityGraph(attributePaths = {"houseUnits"})
    Page<Employee> findAll(@NonNull Specification<Employee> spec, @NonNull Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);
}
