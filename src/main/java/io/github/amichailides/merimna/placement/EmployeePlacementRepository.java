package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.EmployeePlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePlacementRepository extends JpaRepository<EmployeePlacement, Long>,
        JpaSpecificationExecutor<EmployeePlacement> {
}
