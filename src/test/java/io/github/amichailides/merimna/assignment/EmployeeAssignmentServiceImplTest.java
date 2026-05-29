package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.assignment.event.EmployeeAssignmentCreatedEvent;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeAssignmentServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private HouseUnitRepository houseUnitRepository;

    @Mock
    private EmployeeAssignmentRepository assignmentRepository;

    @Mock
    private EmployeeAssignmentMapper assignmentMapper;

    @Mock
    private EmployeeAssignmentPolicy assignmentPolicy;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EmployeeAssignmentServiceImpl assignmentService;

    private static final UUID EMPLOYEE_PUBLIC_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID HOUSE_UNIT_PUBLIC_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID ASSIGNMENT_PUBLIC_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

    private static final LocalDate EMPLOYEE_HIRE_DATE =
            LocalDate.of(2026, 1, 10);

    private static final LocalDate START_DATE =
            LocalDate.of(2026, 6, 1);

    private static final LocalDate END_DATE =
            LocalDate.of(2026, 6, 30);

    private static final String DEFAULT_FIRST_NAME = "Γεώργιος";
    private static final String DEFAULT_LAST_NAME = "Παπαδόπουλος";
    private static final String DEFAULT_EMAIL = "g.papadopoulos@merimna.gr";
    private static final String DEFAULT_MOBILE = "+30690367123";

    private static final String DEFAULT_POSITION_CODE = "CAREGIVER";
    private static final String DEFAULT_POSITION_DISPLAY_NAME = "Caregiver";

    private static final String HOUSE_UNIT_CODE = "UNIT_A";
    private static final String HOUSE_UNIT_DISPLAY_NAME = "House Unit A";
    private static final String HOUSE_UNIT_ADDRESS = "Πατησίων 100";

    @Nested
    class CreateAssignmentTests {

        @Test
        void shouldCreateAssignment() {
            EmployeeAssignmentCreateDTO dto = defaultCreateDTO();

            Employee employee = defaultEmployee().build();
            HouseUnit houseUnit = defaultHouseUnit().build();
            EmployeeAssignmentReadOnlyDTO expected = defaultReadOnlyDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.of(employee));

            when(houseUnitRepository.findByPublicId(HOUSE_UNIT_PUBLIC_ID))
                    .thenReturn(Optional.of(houseUnit));

            when(assignmentMapper.toDTO(any(EmployeeAssignment.class)))
                    .thenReturn(expected);

            EmployeeAssignmentReadOnlyDTO result =
                    assignmentService.create(EMPLOYEE_PUBLIC_ID, dto);

            assertEquals(expected, result);

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);
            verify(houseUnitRepository).findByPublicId(HOUSE_UNIT_PUBLIC_ID);

            verify(assignmentPolicy).validateForCreate(
                    employee,
                    houseUnit,
                    START_DATE,
                    END_DATE
            );

            ArgumentCaptor<EmployeeAssignment> assignmentCaptor =
                    ArgumentCaptor.forClass(EmployeeAssignment.class);

            verify(assignmentRepository).saveAndFlush(assignmentCaptor.capture());

            EmployeeAssignment savedAssignment = assignmentCaptor.getValue();

            assertEquals(employee, savedAssignment.getEmployee());
            assertEquals(houseUnit, savedAssignment.getHouseUnit());
            assertEquals(EmployeeAssignmentStatus.ACTIVE, savedAssignment.getStatus());
            assertEquals(START_DATE, savedAssignment.getStartDate());
            assertEquals(END_DATE, savedAssignment.getEndDate());

            verify(assignmentMapper).toDTO(savedAssignment);

            ArgumentCaptor<EmployeeAssignmentCreatedEvent> eventCaptor =
                    ArgumentCaptor.forClass(EmployeeAssignmentCreatedEvent.class);

            verify(eventPublisher).publishEvent(eventCaptor.capture());

            EmployeeAssignmentCreatedEvent event = eventCaptor.getValue();

            assertEquals(savedAssignment.getPublicId(), event.assignmentPublicId());
            assertEquals(EMPLOYEE_PUBLIC_ID, event.employeePublicId());
            assertEquals(HOUSE_UNIT_PUBLIC_ID, event.houseUnitPublicId());
            assertEquals(START_DATE, event.startDate());
            assertEquals(END_DATE, event.endDate());
        }

        @Test
        void shouldThrowException_whenEmployeeMissing() {
            EmployeeAssignmentCreateDTO dto = defaultCreateDTO();

            when(employeeRepository.findByPublicId(EMPLOYEE_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    EmployeeNotFoundByPublicIdException.class,
                    () -> assignmentService.create(EMPLOYEE_PUBLIC_ID, dto)
            );

            verify(employeeRepository).findByPublicId(EMPLOYEE_PUBLIC_ID);

            verifyNoInteractions(houseUnitRepository);
            verifyNoInteractions(assignmentPolicy);
            verifyNoInteractions(assignmentRepository);
            verifyNoInteractions(assignmentMapper);
            verifyNoInteractions(eventPublisher);
        }
    }

    private EmployeeAssignmentCreateDTO defaultCreateDTO() {
        return new EmployeeAssignmentCreateDTO(
                HOUSE_UNIT_PUBLIC_ID,
                START_DATE,
                END_DATE
        );
    }

    private Employee.EmployeeBuilder defaultEmployee() {
        return Employee.builder()
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .contactEmail(DEFAULT_EMAIL)
                .mobileNumber(DEFAULT_MOBILE)
                .position(defaultEmployeePosition().build())
                .hireDate(EMPLOYEE_HIRE_DATE)
                .isActive(true);
    }

    private EmployeePosition.EmployeePositionBuilder defaultEmployeePosition() {
        return EmployeePosition.builder()
                .code(EmployeePositionCode.of(DEFAULT_POSITION_CODE))
                .displayName(DEFAULT_POSITION_DISPLAY_NAME)
                .requiresExclusivePlacement(false);
    }

    private HouseUnit.HouseUnitBuilder defaultHouseUnit() {
        return HouseUnit.builder()
                .publicId(HOUSE_UNIT_PUBLIC_ID)
                .code(HOUSE_UNIT_CODE)
                .displayName(HOUSE_UNIT_DISPLAY_NAME)
                .address(HOUSE_UNIT_ADDRESS)
                .maxCapacity(6);
    }

    private EmployeeAssignmentReadOnlyDTO defaultReadOnlyDTO() {
        return EmployeeAssignmentReadOnlyDTO.builder()
                .publicId(ASSIGNMENT_PUBLIC_ID)
                .houseUnitPublicId(HOUSE_UNIT_PUBLIC_ID)
                .houseUnitDisplayName(HOUSE_UNIT_DISPLAY_NAME)
                .status(EmployeeAssignmentStatus.ACTIVE)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
    }
}