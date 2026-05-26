package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.employee.audit.EmployeeChangeDetector;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import io.github.amichailides.merimna.employee.event.EmployeeCreatedEvent;
import io.github.amichailides.merimna.employee.event.EmployeeReactivatedEvent;
import io.github.amichailides.merimna.employee.event.EmployeeTerminatedEvent;
import io.github.amichailides.merimna.employee.event.EmployeeUpdatedEvent;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
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

import static org.junit.jupiter.api.Assertions.*;
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

    private static final LocalDate TERMINATION_DATE =
            LocalDate.of(2026, 5, 20);

    private static final UUID USER_PUBLIC_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

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

    @Nested
    class TerminateEmployeeTests {

        @Test
        void shouldTerminateEmployee() {

            Employee employee = defaultEmployee().build();
            EmployeeDetailsDTO expected = terminatedEmployeeDetailsDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(userRepository.findByEmployeePublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.terminate(EMPLOYEE_PUBLIC_ID, TERMINATION_DATE);

            assertEquals(expected, result);
            assertFalse(employee.isActive());

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeValidator).validateForTerminate(employee, TERMINATION_DATE);
            verify(userRepository).findByEmployeePublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeMapper).toDetailsDTO(employee);
            verify(eventPublisher).publishEvent(any(EmployeeTerminatedEvent.class));
        }

        @Test
        void shouldThrowException_whenEmployeeMissing() {
            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeeNotFoundByPublicIdException.class,
                    () -> employeeService.terminate(EMPLOYEE_PUBLIC_ID, TERMINATION_DATE)
            );

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);

            verifyNoInteractions(employeeValidator);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(employeeMapper);
            verifyNoInteractions(eventPublisher);
        }

        @Test
        void shouldDeactivateLinkedUser_whenEmployeeHasUserAccount() {
            Employee employee = defaultEmployee().build();

            User user = defaultUser()
                    .employee(employee)
                    .build();

            EmployeeDetailsDTO expected = terminatedEmployeeDetailsDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(userRepository.findByEmployeePublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(user));

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.terminate(EMPLOYEE_PUBLIC_ID, TERMINATION_DATE);

            assertEquals(expected, result);
            assertFalse(employee.isActive());
            assertFalse(user.isActive());

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeValidator).validateForTerminate(employee, TERMINATION_DATE);
            verify(userRepository).findByEmployeePublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeMapper).toDetailsDTO(employee);
            verify(eventPublisher).publishEvent(any(EmployeeTerminatedEvent.class));
        }
    }

    @Nested
    class ReactivateEmployeeTests {

        @Test
        void shouldReactivateEmployee() {
            Employee employee = terminatedEmployee();
            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(userRepository.findByEmployeePublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.reactivate(EMPLOYEE_PUBLIC_ID);

            assertEquals(expected, result);
            assertTrue(employee.isActive());

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(userRepository).findByEmployeePublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeMapper).toDetailsDTO(employee);
            verify(eventPublisher).publishEvent(any(EmployeeReactivatedEvent.class));
        }

        @Test
        void shouldReactivateLinkedUser_whenEmployeeHasUserAccount() {
            Employee employee = terminatedEmployee();

            User user = defaultUser()
                    .employee(employee)
                    .active(false)
                    .build();

            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(userRepository.findByEmployeePublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(user));

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.reactivate(EMPLOYEE_PUBLIC_ID);

            assertEquals(expected, result);
            assertTrue(employee.isActive());
            assertTrue(user.isActive());

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(userRepository).findByEmployeePublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeMapper).toDetailsDTO(employee);
            verify(eventPublisher).publishEvent(any(EmployeeReactivatedEvent.class));
        }

        @Test
        void shouldThrowException_whenEmployeeMissing() {
            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeeNotFoundByPublicIdException.class,
                    () -> employeeService.reactivate(EMPLOYEE_PUBLIC_ID)
            );

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);

            verifyNoInteractions(userRepository);
            verifyNoInteractions(employeeMapper);
            verifyNoInteractions(eventPublisher);
        }
    }

    @Nested
    class GetEmployeeByPublicIdTests {

        @Test
        void shouldGetEmployeeByPublicId() {
            Employee employee = defaultEmployee().build();
            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            when(employeeRepository.findWithDetailsByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.getEmployeeByPublicId(EMPLOYEE_PUBLIC_ID);

            assertEquals(expected, result);

            verify(employeeRepository).findWithDetailsByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeMapper).toDetailsDTO(employee);
        }

        @Test
        void shouldThrowException_whenEmployeeMissing() {
            when(employeeRepository.findWithDetailsByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeeNotFoundByPublicIdException.class,
                    () -> employeeService.getEmployeeByPublicId(EMPLOYEE_PUBLIC_ID)
            );

            verify(employeeRepository).findWithDetailsByPublicId(EMPLOYEE_PUBLIC_ID);
            verifyNoInteractions(employeeMapper);
        }
    }

    @Nested
    class UpdateEmployeeTests {

        @Test
        void shouldUpdateEmployee_whenChangesDetected() {
            EmployeeUpdateDTO dto = defaultEmployeeUpdateDTO();

            Employee employee = defaultEmployee().build();
            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            EntityChangeSet changeSet = EntityChangeSet.builder()
                    .track("firstName", "Γεώργιος", dto.firstName())
                    .build();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(employeeChangeDetector.detectChanges(employee, dto, null))
                    .thenReturn(changeSet);

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.updateEmployee(EMPLOYEE_PUBLIC_ID, dto);

            assertEquals(expected, result);

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeValidator).validateForUpdate(employee, dto);
            verify(employeeChangeDetector).detectChanges(employee, dto, null);
            verify(employeeMapper).updateEntity(employee, dto, null);
            verify(employeeMapper).toDetailsDTO(employee);
            verify(eventPublisher).publishEvent(any(EmployeeUpdatedEvent.class));

            verifyNoInteractions(employeePositionRepository);
        }

        @Test
        void shouldUpdateEmployee_withoutPublishingEvent_whenNoChangesDetected() {
            EmployeeUpdateDTO dto = noChangesEmployeeUpdateDTO();

            Employee employee = defaultEmployee().build();
            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            EntityChangeSet changeSet = EntityChangeSet.builder().build();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(employeeChangeDetector.detectChanges(employee, dto, null))
                    .thenReturn(changeSet);

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.updateEmployee(EMPLOYEE_PUBLIC_ID, dto);

            assertEquals(expected, result);

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeValidator).validateForUpdate(employee, dto);
            verify(employeeChangeDetector).detectChanges(employee, dto, null);
            verify(employeeMapper).updateEntity(employee, dto, null);
            verify(employeeMapper).toDetailsDTO(employee);

            verifyNoInteractions(eventPublisher);
            verifyNoInteractions(employeePositionRepository);
        }

        @Test
        void shouldUpdateEmployee_whenPositionCodeProvided() {
            EmployeeUpdateDTO dto = defaultEmployeeUpdateDTOWithPosition();

            EmployeePositionCode newPositionCode = EmployeePositionCode.of(dto.positionCode());

            EmployeePosition newPosition = defaultEmployeePosition()
                    .code(newPositionCode)
                    .displayName("Social Worker")
                    .build();

            Employee employee = defaultEmployee().build();
            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            EntityChangeSet changeSet = EntityChangeSet.builder()
                    .track("positionCode", "CAREGIVER", dto.positionCode())
                    .build();

            when(employeePositionRepository.findByCode(newPositionCode))
                    .thenReturn(Optional.of(newPosition));

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(employeeChangeDetector.detectChanges(employee, dto, dto.positionCode()))
                    .thenReturn(changeSet);

            when(employeeMapper.toDetailsDTO(employee))
                    .thenReturn(expected);

            EmployeeDetailsDTO result =
                    employeeService.updateEmployee(EMPLOYEE_PUBLIC_ID, dto);

            assertEquals(expected, result);

            verify(employeePositionRepository).findByCode(newPositionCode);
            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(employeeValidator).validateForUpdate(employee, dto);
            verify(employeeChangeDetector).detectChanges(employee, dto, dto.positionCode());
            verify(employeeMapper).updateEntity(employee, dto, newPosition);
            verify(employeeMapper).toDetailsDTO(employee);
            verify(eventPublisher).publishEvent(any(EmployeeUpdatedEvent.class));
        }

        @Test
        void shouldThrowException_whenNewPositionMissing() {
            EmployeeUpdateDTO dto = defaultEmployeeUpdateDTOWithPosition();

            EmployeePositionCode newPositionCode =
                    EmployeePositionCode.of(dto.positionCode());

            when(employeePositionRepository.findByCode(newPositionCode))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeePositionNotFoundByCodeException.class,
                    () -> employeeService.updateEmployee(EMPLOYEE_PUBLIC_ID, dto)
            );

            verify(employeePositionRepository).findByCode(newPositionCode);

            verifyNoInteractions(employeeRepository);
            verifyNoInteractions(employeeValidator);
            verifyNoInteractions(employeeChangeDetector);
            verifyNoInteractions(employeeMapper);
            verifyNoInteractions(eventPublisher);
        }

        @Test
        void shouldThrowException_whenEmployeeMissing() {
            EmployeeUpdateDTO dto = defaultEmployeeUpdateDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeeNotFoundByPublicIdException.class,
                    () -> employeeService.updateEmployee(EMPLOYEE_PUBLIC_ID, dto)
            );

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);

            verifyNoInteractions(employeeValidator);
            verifyNoInteractions(employeeChangeDetector);
            verifyNoInteractions(employeeMapper);
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
                .firstName("Γεώργιος")
                .lastName("Παπαδόπουλος")
                .contactEmail("g.papadopoulos@merimna.gr")
                .mobileNumber("+30690367123")
                .address(defaultAddress())
                .hireDate(EMPLOYEE_HIRE_DATE)
                .isActive(true);
    }

    private EmployeeDetailsDTO terminatedEmployeeDetailsDTO() {
        return EmployeeDetailsDTO.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName("Γεώργιος")
                .lastName("Παπαδόπουλος")
                .contactEmail("g.papadopoulos@merimna.gr")
                .mobileNumber("+306942318223")
                .positionCode("CAREGIVER")
                .positionDisplayName("Caregiver")
                .assignments(List.of())
                .activePlacement(null)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .address(defaultAddressDTO())
                .active(false)
                .build();
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

    private User.UserBuilder defaultUser() {
        return User.builder()
                .publicId(USER_PUBLIC_ID)
                .username("gpapadopoulos")
                .email("g.papadopoulos@merimna.gr")
                .password("encoded-password")
                .role(Role.ADMIN)
                .active(true);
    }

    private Employee terminatedEmployee() {
        Employee employee = defaultEmployee().build();
        employee.terminate(TERMINATION_DATE);
        return employee;
    }

    private EmployeeUpdateDTO defaultEmployeeUpdateDTO() {
        return EmployeeUpdateDTO.builder()
                .firstName("Κωνσταντίνος")
                .lastName(null)
                .contactEmail(null)
                .mobileNumber(null)
                .positionCode(null)
                .hireDate(null)
                .address(null)
                .build();
    }

    private EmployeeUpdateDTO noChangesEmployeeUpdateDTO() {
        return EmployeeUpdateDTO.builder()
                .firstName("Γεώργιος")
                .lastName("Παπαδόπουλος")
                .contactEmail("g.papadopoulos@merimna.gr")
                .mobileNumber("+30690367123")
                .positionCode(null)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .address(null)
                .build();
    }

    private EmployeeUpdateDTO defaultEmployeeUpdateDTOWithPosition() {
        return EmployeeUpdateDTO.builder()
                .positionCode("SOCIAL_WORKER")
                .build();
    }
}