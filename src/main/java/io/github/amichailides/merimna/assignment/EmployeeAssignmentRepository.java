package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}

