package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.EmployeePositionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {
    Optional<EmployeePosition> findByCode(EmployeePositionCode code);

    boolean existsByCode(EmployeePositionCode code);
}
