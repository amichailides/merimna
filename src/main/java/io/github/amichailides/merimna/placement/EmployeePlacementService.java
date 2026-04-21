package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;

public interface EmployeePlacementService {

    EmployeePlacementReadOnlyDTO create(EmployeePlacementCreateDTO dto);
}
