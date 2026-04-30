package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeAssignmentRepository
        extends JpaRepository<EmployeeAssignment, Long>,
        JpaSpecificationExecutor<EmployeeAssignment> {

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
        where a.employee.publicId = :employeePublicId
        order by a.startDate desc
        """)
    List<EmployeeAssignmentReadOnlyDTO> findAssignmentsByEmployeePublicId(UUID employeePublicId);

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
    where a.employee.publicId = :employeePublicId
      and a.status = :status
    order by a.startDate desc
    """)
    List<EmployeeAssignmentReadOnlyDTO> findAssignmentsByEmployeePublicIdAndStatus(UUID employeePublicId,
                                                                                   EmployeeAssignmentStatus status);

    Optional<EmployeeAssignment> findByIdAndEmployeePublicId(Long id, UUID employeePublicId);

    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM EmployeeAssignment a
    WHERE a.employee = :employee
      AND a.status = :status
      AND a.startDate <= :effectiveEndDate
      AND (a.endDate IS NULL OR a.endDate >= :startDate)
    """)
    boolean existsOverlappingAssignment(
            @Param("employee") Employee employee,
            @Param("status") EmployeeAssignmentStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("effectiveEndDate") LocalDate effectiveEndDate
    );

    boolean existsByEmployeeAndHouseUnitAndStatus(
            Employee employee,
            HouseUnit houseUnit,
            EmployeeAssignmentStatus status
    );
}

