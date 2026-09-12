package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementSearchDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementTerminateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeePlacementService {

    EmployeePlacementReadOnlyDTO create(EmployeePlacementCreateDTO dto);

    EmployeePlacementReadOnlyDTO getByPublicId(UUID publicId);

    void terminate(UUID publicId, EmployeePlacementTerminateDTO dto);

    Page<EmployeePlacementReadOnlyDTO> getAllPlacements(
            EmployeePlacementSearchDTO criteria,
            Pageable pageable);
}
