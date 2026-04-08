package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.address.AddressMapper;
import io.github.amichailides.merimna.assignment.EmployeeAssignmentMapper;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeeHouseUnitAssignment;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeListDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final AddressMapper addressMapper;
    private final EmployeeAssignmentMapper assignmentMapper;

    public EmployeeDetailsDTO toDetailsDTO(Employee entity) {
        if (entity == null) return null;

        return EmployeeDetailsDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .mobileNumber(entity.getMobileNumber())
                .address(addressMapper.toDTO(entity.getAddress()))
                .positionCode(entity.getPosition().getCode().getValue())
                .positionDisplayName(entity.getPosition().getDisplayName())
                .hireDate(entity.getHireDate())
                .assignments(
                        entity.getAssignments().stream()
                                .sorted(Comparator.comparing(EmployeeHouseUnitAssignment::getStartDate,
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                                .map(assignmentMapper::toDTO)
                                .toList()
                )
                .active(entity.isActive())
                .build();
    }

    public EmployeeListDTO toListDTO(Employee entity) {
        if (entity == null) return null;

        return EmployeeListDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .positionCode(entity.getPosition().getCode().getValue())
                .active(entity.isActive())
                .build();
    }

    public Employee toEntity(EmployeeCreateDTO dto, EmployeePosition position) {
        if (dto == null) return null;

        return Employee.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .mobileNumber(dto.mobileNumber())
                .address(addressMapper.toEntity(dto.address()))
                .position(position)
                .hireDate(dto.hireDate())
                .build();
    }

    public void updateEntity(Employee existing, EmployeeUpdateDTO dto, EmployeePosition position) {
        Objects.requireNonNull(existing, "existing employee must not be null");
        Objects.requireNonNull(dto, "employee update dto must not be null");

        if (dto.firstName() != null) existing.setFirstName(dto.firstName());
        if (dto.lastName() != null) existing.setLastName(dto.lastName());
        if (dto.email() != null) existing.setEmail(dto.email());
        if (dto.mobileNumber() != null) existing.setMobileNumber(dto.mobileNumber());
        if (position != null) existing.changePosition(position);
        if (dto.hireDate() != null) existing.setHireDate(dto.hireDate());
        if (dto.address() != null) addressMapper.updateEntity(existing.getAddress(), dto.address());
    }
}
