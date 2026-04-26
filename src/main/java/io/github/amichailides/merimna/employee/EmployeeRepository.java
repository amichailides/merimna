package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            select e from Employee e
            left join fetch e.assignments
            left join fetch e.placements
            where e.id = :id
            """)
    Optional<Employee> findWithAccessDataById(@Param("id") Long id);
}
