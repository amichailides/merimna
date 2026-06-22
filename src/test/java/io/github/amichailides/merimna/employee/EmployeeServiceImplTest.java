package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.access.HouseUnitAccessService;
import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.audit.*;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.employee.audit.EmployeeChangeDetector;
import io.github.amichailides.merimna.employee.dto.*;
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
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private HouseUnitAccessService houseUnitAccessService;

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

    private static final String DEFAULT_FIRST_NAME = "Γεώργιος";
    private static final String DEFAULT_LAST_NAME = "Παπαδόπουλος";
    private static final String UPDATED_FIRST_NAME = "Κωνσταντίνος";

    private static final String DEFAULT_EMAIL = "g.papadopoulos@merimna.gr";
    private static final String DEFAULT_MOBILE = "+30690367123";

    private static final String DEFAULT_POSITION_CODE = "CAREGIVER";
    private static final String DEFAULT_POSITION_DISPLAY_NAME = "Caregiver";
    private static final String UPDATED_POSITION_CODE = "SOCIAL_WORKER";
    private static final String UPDATED_POSITION_DISPLAY_NAME = "Social Worker";

    private static final String DEFAULT_STREET = "Αγίου Μελετίου";
    private static final String DEFAULT_STREET_NUMBER = "23";
    private static final String DEFAULT_CITY = "Αθήνα";
    private static final String DEFAULT_ZIP_CODE = "11361";

    private static final String DEFAULT_USERNAME = "gpapadopoulos";
    private static final String DEFAULT_PASSWORD = "encoded-password";

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
                    .track("firstName", DEFAULT_FIRST_NAME, dto.firstName())
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
                    .displayName(UPDATED_POSITION_DISPLAY_NAME)
                    .build();

            Employee employee = defaultEmployee().build();
            EmployeeDetailsDTO expected = defaultEmployeeDetailsDTO();

            EntityChangeSet changeSet = EntityChangeSet.builder()
                    .track("positionCode", DEFAULT_POSITION_CODE, dto.positionCode())
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

    @Nested
    class GetAllEmployeesTests {

        @Test
        void shouldGetAllEmployees() {
            EmployeeSearchDTO criteria = new EmployeeSearchDTO(
                    null,
                    null,
                    null,
                    EmployeeStatusFilter.ACTIVE
            );

            Pageable pageable = PageRequest.of(0, 10);

            Employee employee = defaultEmployee().build();
            EmployeeListDTO listDTO = defaultEmployeeListDTO();

            Page<Employee> employeePage =
                    new PageImpl<>(List.of(employee), pageable, 1);

            when(employeeRepository.findAll(
                    ArgumentMatchers.<Specification<Employee>>any(),
                    eq(pageable)
            )).thenReturn(employeePage);

            when(employeeMapper.toListDTO(employee))
                    .thenReturn(listDTO);

            Page<EmployeeListDTO> result =
                    employeeService.getAllEmployees(criteria, pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals(listDTO, result.getContent().getFirst());

            verify(employeeRepository).findAll(
                    ArgumentMatchers.<Specification<Employee>>any(),
                    eq(pageable)
            );
            verify(employeeMapper).toListDTO(employee);
        }

        @Test
        void shouldReturnEmptyPage_whenNoEmployeesFound() {
            EmployeeSearchDTO criteria = new EmployeeSearchDTO(
                    null,
                    null,
                    null,
                    EmployeeStatusFilter.ACTIVE
            );

            Pageable pageable = PageRequest.of(0, 10);

            Page<Employee> emptyPage =
                    new PageImpl<>(List.of(), pageable, 0);

            when(employeeRepository.findAll(
                    ArgumentMatchers.<Specification<Employee>>any(),
                    eq(pageable)
            )).thenReturn(emptyPage);

            Page<EmployeeListDTO> result =
                    employeeService.getAllEmployees(criteria, pageable);

            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());

            verify(employeeRepository).findAll(
                    ArgumentMatchers.<Specification<Employee>>any(),
                    eq(pageable)
            );

            verifyNoMoreInteractions(employeeMapper);
        }
    }

    @Nested
    class GetEmployeeActivityTests {

        @Test
        void shouldGetEmployeeActivity() {
            Employee employee = defaultEmployee().build();

            Pageable pageable = PageRequest.of(
                    0,
                    5,
                    Sort.by(Sort.Direction.DESC, "occurredAt")
            );

            UUID olderActivityPublicId =
                    UUID.fromString("33333333-3333-3333-3333-333333333333");

            UUID newerActivityPublicId =
                    UUID.fromString("44444444-4444-4444-4444-444444444444");

            AuditLog olderLog = AuditLog.builder()
                    .publicId(olderActivityPublicId)
                    .action(AuditAction.EMPLOYEE_CREATED)
                    .entityType(AuditEntityType.EMPLOYEE)
                    .entityPublicId(EMPLOYEE_PUBLIC_ID)
                    .subjectEmployeePublicId(EMPLOYEE_PUBLIC_ID)
                    .occurredAt(Instant.parse("2026-06-20T10:00:00Z"))
                    .outcome(AuditOutcome.SUCCESS)
                    .metadata(Map.of(
                            "firstName", DEFAULT_FIRST_NAME,
                            "lastName", DEFAULT_LAST_NAME
                    ))
                    .build();

            AuditLog newerLog = AuditLog.builder()
                    .publicId(newerActivityPublicId)
                    .action(AuditAction.EMPLOYEE_UPDATED)
                    .entityType(AuditEntityType.EMPLOYEE)
                    .entityPublicId(EMPLOYEE_PUBLIC_ID)
                    .subjectEmployeePublicId(EMPLOYEE_PUBLIC_ID)
                    .occurredAt(Instant.parse("2026-06-21T10:00:00Z"))
                    .outcome(AuditOutcome.SUCCESS)
                    .metadata(Map.of(
                            "changes", List.of(Map.of(
                                    "fieldName", "mobileNumber",
                                    "oldValue", "+30690367123",
                                    "newValue", "+30690367124"
                            ))
                    ))
                    .build();

            Page<AuditLog> auditLogPage =
                    new PageImpl<>(List.of(newerLog, olderLog), pageable, 2);

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(auditLogRepository.findBySubjectEmployeePublicId(
                    EMPLOYEE_PUBLIC_ID,
                    pageable
            )).thenReturn(auditLogPage);

            Page<EmployeeActivityDTO> result =
                    employeeService.getEmployeeActivity(EMPLOYEE_PUBLIC_ID, pageable);

            assertEquals(2, result.getTotalElements());
            assertEquals(2, result.getContent().size());

            EmployeeActivityDTO first = result.getContent().getFirst();
            EmployeeActivityDTO second = result.getContent().get(1);

            assertEquals(newerActivityPublicId, first.publicId());
            assertEquals(AuditAction.EMPLOYEE_UPDATED, first.action());
            assertEquals(AuditEntityType.EMPLOYEE, first.entityType());
            assertEquals(EMPLOYEE_PUBLIC_ID, first.entityPublicId());
            assertEquals(Instant.parse("2026-06-21T10:00:00Z"), first.occurredAt());
            assertTrue(first.metadata().containsKey("changes"));

            assertEquals(olderActivityPublicId, second.publicId());
            assertEquals(AuditAction.EMPLOYEE_CREATED, second.action());
            assertEquals(AuditEntityType.EMPLOYEE, second.entityType());
            assertEquals(EMPLOYEE_PUBLIC_ID, second.entityPublicId());
            assertEquals(Instant.parse("2026-06-20T10:00:00Z"), second.occurredAt());

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(houseUnitAccessService).ensureCanAccess(employee);
            verify(auditLogRepository).findBySubjectEmployeePublicId(
                    EMPLOYEE_PUBLIC_ID,
                    pageable
            );
        }

        @Test
        void shouldThrowException_whenEmployeeMissing() {
            Pageable pageable = PageRequest.of(0, 5);

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeeNotFoundByPublicIdException.class,
                    () -> employeeService.getEmployeeActivity(EMPLOYEE_PUBLIC_ID, pageable)
            );

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);

            verifyNoInteractions(houseUnitAccessService);
            verifyNoInteractions(auditLogRepository);
        }
    }

    private EmployeeCreateDTO defaultEmployeeCreateDTO() {
        return EmployeeCreateDTO.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .contactEmail(DEFAULT_EMAIL)
                .mobileNumber(DEFAULT_MOBILE)
                .address(defaultAddressDTO())
                .positionCode(DEFAULT_POSITION_CODE)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .build();
    }

    private Employee.EmployeeBuilder defaultEmployee() {
        return Employee.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .contactEmail(DEFAULT_EMAIL)
                .mobileNumber(DEFAULT_MOBILE)
                .address(defaultAddress())
                .hireDate(EMPLOYEE_HIRE_DATE)
                .isActive(true);
    }

    private EmployeeDetailsDTO defaultEmployeeDetailsDTO() {
        return employeeDetailsDTO(true);
    }

    private EmployeeDetailsDTO terminatedEmployeeDetailsDTO() {
        return employeeDetailsDTO(false);
    }

    private EmployeeDetailsDTO employeeDetailsDTO(boolean active) {
        return EmployeeDetailsDTO.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .contactEmail(DEFAULT_EMAIL)
                .mobileNumber(DEFAULT_MOBILE)
                .positionCode(DEFAULT_POSITION_CODE)
                .positionDisplayName(DEFAULT_POSITION_DISPLAY_NAME)
                .assignments(List.of())
                .activePlacement(null)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .address(defaultAddressDTO())
                .active(active)
                .build();
    }

    private EmployeePosition.EmployeePositionBuilder defaultEmployeePosition() {
        return EmployeePosition.builder()
                .code(EmployeePositionCode.of(DEFAULT_POSITION_CODE))
                .displayName(DEFAULT_POSITION_DISPLAY_NAME)
                .requiresExclusivePlacement(false);
    }

    private AddressDTO defaultAddressDTO() {
        return AddressDTO.builder()
                .street(DEFAULT_STREET)
                .streetNumber(DEFAULT_STREET_NUMBER)
                .city(DEFAULT_CITY)
                .zipCode(DEFAULT_ZIP_CODE)
                .build();
    }

    private Address defaultAddress() {
        return Address.builder()
                .street(DEFAULT_STREET)
                .streetNumber(DEFAULT_STREET_NUMBER)
                .city(DEFAULT_CITY)
                .zipCode(DEFAULT_ZIP_CODE)
                .build();
    }

    private User.UserBuilder defaultUser() {
        return User.builder()
                .publicId(USER_PUBLIC_ID)
                .username(DEFAULT_USERNAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
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
                .firstName(UPDATED_FIRST_NAME)
                .build();
    }

    private EmployeeUpdateDTO noChangesEmployeeUpdateDTO() {
        return EmployeeUpdateDTO.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .contactEmail(DEFAULT_EMAIL)
                .mobileNumber(DEFAULT_MOBILE)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .build();
    }

    private EmployeeUpdateDTO defaultEmployeeUpdateDTOWithPosition() {
        return EmployeeUpdateDTO.builder()
                .positionCode(UPDATED_POSITION_CODE)
                .build();
    }

    private EmployeeListDTO defaultEmployeeListDTO() {
        return EmployeeListDTO.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .positionCode(DEFAULT_POSITION_CODE)
                .active(true)
                .build();
    }
}