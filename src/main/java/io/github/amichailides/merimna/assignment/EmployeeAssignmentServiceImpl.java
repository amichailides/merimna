package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.assignment.exception.AssignmentNotFoundException;
import io.github.amichailides.merimna.assignment.exception.AssignmentTerminationNotAllowedException;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmployeeAssignmentServiceImpl implements EmployeeAssignmentService{
    private final EmployeeRepository employeeRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeeAssignmentRepository assignmentRepository;
    private final EmployeeAssignmentMapper assignmentMapper;
    private final EmployeeAssignmentPolicy assignmentPolicy;

    @Override
    @Transactional
    public EmployeeAssignmentReadOnlyDTO create(UUID employeePublicId, EmployeeAssignmentCreateDTO dto) {
        Employee employee = getEmployeeOrThrow(employeePublicId);
        HouseUnit houseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

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
        return assignmentMapper.toDTO(assignment);
    }

    @Override
    @Transactional
    public void cancel(UUID employeePublicId, UUID assignmentPublicId) {
        EmployeeAssignment assignment = getAssignmentOrThrow(assignmentPublicId, employeePublicId);
        assignment.cancel(LocalDate.now());
    }

    @Override
    @Transactional
    public void terminate(UUID employeePublicId, UUID assignmentPublicId) {
        EmployeeAssignment assignment = getAssignmentOrThrow(assignmentPublicId, employeePublicId);

        if (!assignment.getEmployee().isActive()) {
            throw new AssignmentTerminationNotAllowedException();
        }

        assignment.terminate(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(UUID employeePublicId, EmployeeAssignmentView view) {
        getEmployeeOrThrow(employeePublicId);

        return switch (view) {
            case ALL -> assignmentRepository.findAssignmentsByEmployeePublicId(employeePublicId);
            case ACTIVE -> assignmentRepository.findAssignmentsByEmployeePublicIdAndStatus(employeePublicId, EmployeeAssignmentStatus.ACTIVE);
            case PAST -> assignmentRepository.findAssignmentsByEmployeePublicIdAndStatus(employeePublicId, EmployeeAssignmentStatus.TERMINATED);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeAssignmentReadOnlyDTO getAssignmentByPublicId(UUID employeePublicId, UUID assignmentPublicId) {
        getEmployeeOrThrow(employeePublicId);
        EmployeeAssignment assignment = getAssignmentOrThrow(assignmentPublicId, employeePublicId);

        return assignmentMapper.toDTO(assignment);
    }

    private Employee getEmployeeOrThrow(UUID employeePublicId) {
        return employeeRepository.findByPublicId(employeePublicId)
                .orElseThrow(() -> new EmployeeNotFoundByPublicIdException(employeePublicId));
    }

    private EmployeeAssignment getAssignmentOrThrow(UUID assignmentPublicId, UUID employeePublicId) {
        return assignmentRepository.findByPublicIdAndEmployee_PublicId(assignmentPublicId, employeePublicId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentPublicId));
    }
}
