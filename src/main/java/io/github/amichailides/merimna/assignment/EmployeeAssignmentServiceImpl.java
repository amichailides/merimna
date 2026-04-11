package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.assignment.exception.AssignmentNotFoundException;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundByCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class EmployeeAssignmentServiceImpl implements EmployeeAssignmentService{
    private final EmployeeRepository employeeRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeeAssignmentRepository assignmentRepository;
    private final EmployeeAssignmentMapper mapper;
    private final EmployeeAssignmentPolicy assignmentPolicy;

    @Override
    @Transactional
    public EmployeeAssignmentReadOnlyDTO create(Long employeeId, EmployeeAssignmentCreateDTO dto) {
        Employee employee = getEmployeeOrThrow(employeeId);
        HouseUnit houseUnit = houseUnitRepository.findByCode(dto.houseUnitCode())
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(dto.houseUnitCode()));

        // TODO(#18): Enforce role-based PRIMARY assignment constraints and revisit assignment type semantics.

        assignmentPolicy.validateForCreate(
                employee,
                houseUnit,
                dto.startDate(),
                dto.endDate()
        );

        EmployeeAssignment assignment = employee.assignToHouseUnit(
                houseUnit,
                dto.startDate(),
                dto.endDate()
        );

        assignmentRepository.saveAndFlush(assignment);
        return mapper.toDTO(assignment);
    }

    @Override
    @Transactional
    public void cancel(Long employeeId, Long assignmentId) {
        EmployeeAssignment assignment = getAssignmentOrThrow(assignmentId, employeeId);
        assignment.cancel(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeAssignmentReadOnlyDTO> getAssignments(Long employeeId, EmployeeAssignmentView view) {
        getEmployeeOrThrow(employeeId);

        return switch (view) {
            case ALL -> assignmentRepository.findAssignmentsByEmployeeId(employeeId);
            case ACTIVE -> assignmentRepository.findAssignmentsByEmployeeIdAndStatus(employeeId, EmployeeAssignmentStatus.ACTIVE);
            case PAST -> assignmentRepository.findAssignmentsByEmployeeIdAndStatus(employeeId, EmployeeAssignmentStatus.TERMINATED);
        };
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(employeeId));
    }

    private EmployeeAssignment getAssignmentOrThrow (Long assignmentId, Long employeeId) {
        return assignmentRepository.findByIdAndEmployeeId(assignmentId, employeeId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));
    }
}
