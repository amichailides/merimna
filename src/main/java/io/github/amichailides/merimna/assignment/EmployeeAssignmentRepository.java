package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeAssignmentRepository
        extends JpaRepository<EmployeeAssignment, Long>,
        JpaSpecificationExecutor<EmployeeAssignment> {

    List<EmployeeAssignment> findByEmployeeId(Long employeeId);

    List<EmployeeAssignment> findByHouseUnitId(Long houseUnitId);

    Optional<EmployeeAssignment> findByIdAndEmployeeId(Long id, Long employeeId);

    @Query("""
            select new io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO(
                a.id,
                    hu.code,
                    hu.displayName,
                    a.status,
                    a.startDate,
                    a.endDate
            )
            from EmployeeAssignment a
            join a.houseUnit hu
            where a.employee.id = :employeeId
            order by a.startDate desc
            """)
    List<EmployeeAssignmentReadOnlyDTO> findAssignmentsByEmployeeId(Long employeeId);

    @Query("""
        select new io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO(
            a.id,
            hu.code,
            hu.displayName,
            a.status,
            a.startDate,
            a.endDate
        )
        from EmployeeAssignment a
        join a.houseUnit hu
        where a.employee.id = :employeeId
          and a.status = :status
        order by a.startDate desc
        """)
    List<EmployeeAssignmentReadOnlyDTO> findAssignmentsByEmployeeIdAndStatus(Long employeeId,
                                                                             EmployeeAssignmentStatus status);
}

