package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.domain.Address;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.EmployeePositionCode;
import io.github.amichailides.merimna.employee.audit.EmployeeChangeDetector;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.event.EmployeeCreatedEvent;
import io.github.amichailides.merimna.employeePosition.EmployeePositionRepository;
import io.github.amichailides.merimna.employeePosition.exception.EmployeePositionNotFoundByCodeException;
import io.github.amichailides.merimna.user.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeValidator employeeValidator;

    @Mock
    private EmployeePositionRepository employeePositionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private EmployeeChangeDetector employeeChangeDetector;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private static final UUID EMPLOYEE_PUBLIC_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final LocalDate EMPLOYEE_HIRE_DATE =
            LocalDate.of(2026, 2, 23);

    @Nested
    class CreateEmployeeTests {

        @Test
        void shouldCreateEmployee() {
            EmployeeCreateDTO dto = defaultEmployeeCreateDTO();

            EmployeePositionCode positionCode = EmployeePositionCode.of(dto.positionCode());

            EmployeePosition position = defaultEmployeePosition().build();

            Employee employee = defaultEmployee()
                    .position(position)
                    .build();

            Employee savedEmployee = defaultEmployee()
                    .position(position)
                    .build();

            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            when(employeePositionRepository.findByCode(positionCode))
                    .thenReturn(Optional.of(position));

            when(employeeMapper.toEntity(dto, position))
                    .thenReturn(employee);

            when(employeeRepository.save(employee))
                    .thenReturn(savedEmployee);

            when(employeeMapper.toDetailsDTO(savedEmployee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result = employeeService.createEmployee(dto);

            assertEquals(expected, result);

            verify(employeeValidator).validateForCreate(dto);
            verify(employeePositionRepository).findByCode(positionCode);
            verify(employeeMapper).toEntity(dto, position);
            verify(employeeRepository).save(employee);
            verify(employeeMapper).toDetailsDTO(savedEmployee);

            ArgumentCaptor<EmployeeCreatedEvent> eventCaptor =
                    ArgumentCaptor.forClass(EmployeeCreatedEvent.class);

            verify(eventPublisher).publishEvent(eventCaptor.capture());

            EmployeeCreatedEvent event = eventCaptor.getValue();

            assertEquals(savedEmployee.getPublicId(), event.employeePublicId());
        }

        @Test
        void shouldThrowException_whenEmployeePositionMissing() {
            EmployeeCreateDTO dto = defaultEmployeeCreateDTO();

            EmployeePositionCode positionCode = EmployeePositionCode.of(dto.positionCode());

            when(employeePositionRepository.findByCode(positionCode))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeePositionNotFoundByCodeException.class,
                    () -> employeeService.createEmployee(dto)
            );

            verify(employeeValidator).validateForCreate(dto);
            verify(employeePositionRepository).findByCode(positionCode);

            verifyNoInteractions(employeeMapper);
            verifyNoInteractions(employeeRepository);
            verifyNoInteractions(eventPublisher);
        }
    }

    private EmployeeCreateDTO defaultEmployeeCreateDTO() {
        return EmployeeCreateDTO.builder()
                .firstName("Γεώργιος")
                .lastName("Παπαδόπουλος")
                .contactEmail("g.papadopoulos@merimna.gr")
                .mobileNumber("+306942318223")
                .address(defaultAddressDTO())
                .positionCode("CAREGIVER")
                .hireDate(EMPLOYEE_HIRE_DATE)
                .build();
    }

    private Employee.EmployeeBuilder defaultEmployee() {
        return Employee.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName("Γιωργος")
                .lastName("Παπαδοπουλος")
                .contactEmail("g.papadopoulos@merimna.gr")
                .mobileNumber("+30690367123")
                .address(defaultAddress())
                .hireDate(EMPLOYEE_HIRE_DATE)
                .isActive(true);
    }

    private EmployeePosition.EmployeePositionBuilder defaultEmployeePosition() {
        return EmployeePosition.builder()
                .code(EmployeePositionCode.of("CAREGIVER"))
                .displayName("Caregiver")
                .requiresExclusivePlacement(false);
    }

    private EmployeeDetailsDTO defaultEmployeeDetailsDTO() {
        return EmployeeDetailsDTO.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName("Γιωργος")
                .lastName("Παπαδοπουλος")
                .contactEmail("g.papadopoulos@merimna.gr")
                .mobileNumber("+30690367123")
                .positionCode("CAREGIVER")
                .positionDisplayName("Caregiver")
                .assignments(List.of())
                .activePlacement(null)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .address(defaultAddressDTO())
                .active(true)
                .build();
    }

    private AddressDTO defaultAddressDTO() {
        return AddressDTO.builder()
                .street("Αγίου Μελετίου")
                .streetNumber("23")
                .city("Αθήνα")
                .zipCode("11361")
                .build();
    }

    private Address defaultAddress() {
        return Address.builder()
                .street("Αγίου Μελετίου")
                .streetNumber("23")
                .city("Αθήνα")
                .zipCode("11361")
                .build();
    }
}