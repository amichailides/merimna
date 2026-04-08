package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionCreateDTO;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionReadOnlyDTO;

public interface EmployeePositionService {
    EmployeePositionReadOnlyDTO create(EmployeePositionCreateDTO dto);
}
