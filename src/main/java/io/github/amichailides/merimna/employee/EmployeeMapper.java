package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.address.AddressMapper;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeReadOnlyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final AddressMapper addressMapper;

    public EmployeeReadOnlyDTO toDTO(Employee entity) {
        if (entity == null) return null;

        return EmployeeReadOnlyDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .mobileNumber(entity.getMobileNumber())
                .address(addressMapper.toDTO(entity.getAddress()))
                .position(entity.getPosition())
                .hireDate(entity.getHireDate())
                .build();
    }

    public Employee toEntity(EmployeeCreateDTO dto) {
        if (dto == null) return null;

        return Employee.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .mobileNumber(dto.mobileNumber())
                .address(addressMapper.toEntity(dto.address()))
                .position(dto.position())
                .hireDate(dto.hireDate())
                .build();
    }
}
