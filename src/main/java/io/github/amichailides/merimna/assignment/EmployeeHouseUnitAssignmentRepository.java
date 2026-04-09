package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.EmployeeHouseUnitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeHouseUnitAssignmentRepository
        extends JpaRepository<EmployeeHouseUnitAssignment, Long>,
        JpaSpecificationExecutor<EmployeeHouseUnitAssignment> {

    List<EmployeeHouseUnitAssignment> findByEmployeeId(Long employeeId);

    List<EmployeeHouseUnitAssignment> findByHouseUnitId(Long houseUnitId);

    @Query("""
            select a
            from EmployeeHouseUnitAssignment a
            where a.employee.id = :employeeId
              and a.startDate <= :date
              and (a.endDate is null or a.endDate >= :date)
            """)
    List<EmployeeHouseUnitAssignment> findActiveAssignmentsByEmployeeId(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date
    );

    @Query("""
            select a
            from EmployeeHouseUnitAssignment a
            where a.employee.id = :employeeId
              and a.assignmentType = :type
              and a.startDate <= :date
              and (a.endDate is null or a.endDate >= :date)
            """)
    Optional<EmployeeHouseUnitAssignment> findActiveAssignmentByType(
            @Param("employeeId") Long employeeId,
            @Param("type") AssignmentType type,
            @Param("date") LocalDate date
    );

    Optional<EmployeeHouseUnitAssignment> findByIdAndEmployeeId(Long id, Long employeeId);

    @Query("""
    select new io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO(
        a.id,
        hu.code,
        hu.displayName,
        a.assignmentType,
        a.startDate,
        a.endDate
    )
    from EmployeeHouseUnitAssignment a
    join a.houseUnit hu
    where a.employee.id = :employeeId
    order by a.startDate desc
    """)
    List<EmployeeAssignmentReadOnlyDTO> findAssignmentsByEmployeeId(Long employeeId);}

