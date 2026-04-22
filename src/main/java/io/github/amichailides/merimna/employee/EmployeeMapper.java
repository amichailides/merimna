package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.address.AddressMapper;
import io.github.amichailides.merimna.assignment.EmployeeAssignmentMapper;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeListDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final AddressMapper addressMapper;
    private final EmployeeAssignmentMapper assignmentMapper;

    public EmployeeDetailsDTO toDetailsDTO(Employee entity) {
        if (entity == null) return null;

        return EmployeeDetailsDTO.builder()
                .publicId(entity.getPublicId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .contactEmail(entity.getContactEmail())
                .mobileNumber(entity.getMobileNumber())
                .address(addressMapper.toDTO(entity.getAddress()))
                .positionCode(entity.getPosition().getCode().getValue())
                .positionDisplayName(entity.getPosition().getDisplayName())
                .hireDate(entity.getHireDate())
                .assignments(
                        entity.getAssignments().stream()
                                .sorted(Comparator.comparing(EmployeeAssignment::getStartDate,
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
                .publicId(entity.getPublicId())
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
                .contactEmail(dto.contactEmail())
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
        if (dto.contactEmail() != null) existing.setContactEmail(dto.contactEmail());
        if (dto.mobileNumber() != null) existing.setMobileNumber(dto.mobileNumber());
        if (position != null) existing.changePosition(position);
        if (dto.hireDate() != null) existing.setHireDate(dto.hireDate());
        if (dto.address() != null) addressMapper.updateEntity(existing.getAddress(), dto.address());
    }
}
