package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.audit.event.EmployeeCreatedEvent;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.EmployeePositionCode;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.employee.dto.*;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.employeePosition.EmployeePositionRepository;
import io.github.amichailides.merimna.employeePosition.exception.EmployeePositionNotFoundByCodeException;
import io.github.amichailides.merimna.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final EmployeePositionRepository employeePositionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto) {
        employeeValidator.validateForCreate(dto);

        EmployeePositionCode code = EmployeePositionCode.of(dto.positionCode());
        EmployeePosition position = resolvePositionOrThrow(code);
        Employee employee = employeeMapper.toEntity(dto, position);
        Employee saved = employeeRepository.save(employee);

        eventPublisher.publishEvent(EmployeeCreatedEvent.from(saved));

        return employeeMapper.toDetailsDTO(saved);
    }


    @Override
    @Transactional
    public EmployeeDetailsDTO terminate(UUID publicId, LocalDate terminationDate) {
        Employee employee = getEmployeeOrThrow(publicId);

        employeeValidator.validateForTerminate(employee, terminationDate);
        employee.terminate(terminationDate);
        deactivateLinkedUser(publicId);

        return employeeMapper.toDetailsDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDetailsDTO reactivate(UUID publicId) {
        Employee employee = getEmployeeOrThrow(publicId);

        // TODO(#14): Add business validation for reactivation
        employee.reactivate();
        reactivateLinkedUser(publicId);

        return employeeMapper.toDetailsDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeListDTO> getAllEmployees(
            EmployeeSearchDTO criteria,
            Pageable pageable) {

        boolean includeInactive = Boolean.TRUE.equals(criteria.includeInactive());

        Specification<Employee> spec = Specification.where(
                        EmployeeSpecifications.globalSearch(criteria.q()))
                .and(EmployeeSpecifications.hasPositionCode(criteria.positionCode()))
                .and(EmployeeSpecifications.isActive(includeInactive ? null : true));

        if (criteria.houseUnit() != null && !criteria.houseUnit().isBlank()) {
            spec = spec.and(EmployeeSpecifications.belongsToHouseUnit(criteria.houseUnit()));
        }

        return employeeRepository.findAll(spec, pageable)
                .map(employeeMapper::toListDTO);
    }

    @Override
    @Transactional
    public EmployeeDetailsDTO updateEmployee(UUID publicId, EmployeeUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        EmployeePosition position = dto.positionCode() != null
                ? resolvePositionOrThrow(EmployeePositionCode.of(dto.positionCode()))
                : null;

        Employee employee = getEmployeeOrThrow(publicId);
        employeeValidator.validateForUpdate(employee, dto);

        employeeMapper.updateEntity(employee, dto, position);

        return employeeMapper.toDetailsDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDetailsDTO getEmployeeByPublicId(UUID publicId) {
        Employee employee = employeeRepository.findWithDetailsByPublicId(publicId)
                .orElseThrow(() -> new EmployeeNotFoundByPublicIdException(publicId));

        return employeeMapper.toDetailsDTO(employee);
    }

    private Employee getEmployeeOrThrow(UUID publicId) {
        return employeeRepository.findByPublicId(publicId)
                .orElseThrow(() -> new EmployeeNotFoundByPublicIdException(publicId));
    }

    private EmployeePosition resolvePositionOrThrow(EmployeePositionCode code) {
        return employeePositionRepository.findByCode(code)
                .orElseThrow(() -> new EmployeePositionNotFoundByCodeException(code.getValue()));
    }

    private void deactivateLinkedUser(UUID publicId) {
        Optional<User> user = userRepository.findByEmployeePublicId(publicId);
        user.ifPresent(User::deactivate);
    }

    private void reactivateLinkedUser(UUID publicId) {
        Optional<User> user = userRepository.findByEmployeePublicId(publicId);
        user.ifPresent(User::reactivate);
    }

}
