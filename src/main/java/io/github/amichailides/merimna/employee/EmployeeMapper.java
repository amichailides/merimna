package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.address.AddressMapper;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final AddressMapper addressMapper;

    public EmployeeDetailsDTO toDTO(Employee entity) {
        if (entity == null) return null;

        return EmployeeDetailsDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .mobileNumber(entity.getMobileNumber())
                .address(addressMapper.toDTO(entity.getAddress()))
                .position(entity.getPosition())
                .hireDate(entity.getHireDate())
                .houseUnitCodes(
                        entity.getHouseUnits() == null
                                ? Set.of()
                                :entity.getHouseUnits().stream()
                                .map(HouseUnit::getCode)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public Employee toEntity(EmployeeCreateDTO dto, Set<HouseUnit> houseUnits) {
        if (dto == null) return null;

        return Employee.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .mobileNumber(dto.mobileNumber())
                .address(addressMapper.toEntity(dto.address()))
                .position(dto.position())
                .hireDate(dto.hireDate())
                .houseUnits(houseUnits)
                .build();
    }
}
