package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.EmployeePlacement;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeePlacementRepository extends JpaRepository<EmployeePlacement, Long>,
        JpaSpecificationExecutor<EmployeePlacement> {

    Optional<EmployeePlacement> findByPublicId(UUID publicId);

    @Query("""
                select count(p) > 0
                from EmployeePlacement p
                where p.employee.id = :employeeId
                  and p.startDate <= :effectiveEndDate
                  and (p.endDate is null or p.endDate >= :startDate)
            """)
    boolean existsOverlappingPlacementForEmployee(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("effectiveEndDate") LocalDate effectiveEndDate
    );
}
