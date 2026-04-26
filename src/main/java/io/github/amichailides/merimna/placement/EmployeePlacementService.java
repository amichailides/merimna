package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementTerminateDTO;

import java.util.UUID;

public interface EmployeePlacementService {

    EmployeePlacementReadOnlyDTO create(EmployeePlacementCreateDTO dto);

    EmployeePlacementReadOnlyDTO getByPublicId(UUID publicId);

    void terminate(UUID publicId, EmployeePlacementTerminateDTO dto);
}
