package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.exception.EmployeeInactiveException;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.placement.exception.EmployeePlacementInvalidEndDate;
import io.github.amichailides.merimna.placement.exception.EmployeePlacementOverlapException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EmployeePlacementValidator {

    private final EmployeePlacementRepository placementRepository;

    private static final LocalDate OPEN_ENDED_END_DATE =
            LocalDate.of(9999, 12, 31);

    public void validateForCreate(Employee employee, EmployeePlacementCreateDTO dto) {
        validateEmployeeIsActive(employee);
        validateDateRange(dto.startDate(), dto.endDate());
        validateNoOverlappingPlacement(employee, dto.startDate(), dto.endDate());
    }

    private void validateEmployeeIsActive(Employee employee) {
        if (!employee.isActive()) {
            throw new EmployeeInactiveException(employee.getPublicId());
        }
    }

    private void validateDateRange(LocalDate start, LocalDate end) {
        if (end != null && end.isBefore(start)) {
            throw new EmployeePlacementInvalidEndDate(start, end);
        }
    }

    private void validateNoOverlappingPlacement(
            Employee employee,
            LocalDate start,
            LocalDate end
    ) {
        LocalDate effectiveEndDate = end != null
                ? end
                : OPEN_ENDED_END_DATE;

        boolean overlaps = placementRepository.existsOverlappingPlacementForEmployee(
                employee.getId(),
                start,
                effectiveEndDate
        );

        if (overlaps) {
            throw new EmployeePlacementOverlapException(
                    employee.getPublicId(),
                    start,
                    end
            );
        }
    }
}
