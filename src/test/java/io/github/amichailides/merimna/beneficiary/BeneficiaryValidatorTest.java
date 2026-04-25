package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryCreateDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryAlreadyInactiveException;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.Address;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.EmergencyContact;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.domain.RelationshipType;
import io.github.amichailides.merimna.exception.BaseValidationException;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.exception.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryValidatorTest {

    private static final UUID BENEFICIARY_PUBLIC_ID = UUID.randomUUID();
    private static final UUID HOUSE_UNIT_PUBLIC_ID = UUID.randomUUID();

    @Mock
    private BeneficiaryRepository repository;

    private BeneficiaryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BeneficiaryValidator(repository);
    }

    @Nested
    class ValidateForSaveTests {

        @Test
        void shouldThrowConflict_whenAmkaAlreadyExists() {
            BeneficiaryCreateDTO dto = defaultCreateDTO().build();

            when(repository.existsByAmka(dto.amka())).thenReturn(true);

            ConflictValidationException ex = assertThrows(
                    ConflictValidationException.class,
                    () -> validator.validateForSave(dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_ALREADY_EXISTS);
            verify(repository).existsByAmka(dto.amka());
        }

        @Test
        void shouldThrowDomainValidation_whenAmkaDoesNotMatchDateOfBirth() {
            BeneficiaryCreateDTO dto = defaultCreateDTO()
                    .amka("06045678912")
                    .dateOfBirth(LocalDate.of(1966, 4, 6))
                    .build();

            when(repository.existsByAmka(dto.amka())).thenReturn(false);

            DomainValidationException ex = assertThrows(
                    DomainValidationException.class,
                    () -> validator.validateForSave(dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_DATE_MISMATCH);
            verify(repository).existsByAmka(dto.amka());
        }

        @Test
        void shouldNotThrow_whenDataIsValid() {
            BeneficiaryCreateDTO dto = defaultCreateDTO().build();

            when(repository.existsByAmka(dto.amka())).thenReturn(false);

            assertDoesNotThrow(() -> validator.validateForSave(dto));

            verify(repository).existsByAmka(dto.amka());
        }

        @Test
        void shouldNotCheckAmkaConsistency_whenDuplicateAmkaFound() {
            BeneficiaryCreateDTO dto = defaultCreateDTO()
                    .amka("06048612345")
                    .dateOfBirth(LocalDate.of(1950, 1, 1))
                    .build();

            when(repository.existsByAmka(dto.amka())).thenReturn(true);

            ConflictValidationException ex = assertThrows(
                    ConflictValidationException.class,
                    () -> validator.validateForSave(dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_ALREADY_EXISTS);
            verify(repository).existsByAmka(dto.amka());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    class ValidateForUpdateTests {

        @Test
        void shouldThrowConflict_whenAmkaAlreadyExists() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                    .amka("06046678912")
                    .build();

            when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId()))
                    .thenReturn(true);

            ConflictValidationException ex = assertThrows(
                    ConflictValidationException.class,
                    () -> validator.validateForUpdate(existing, dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_ALREADY_EXISTS);
            verify(repository).existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId());
        }

        @Test
        void shouldThrowDomainValidation_whenAmkaChangedAndDoesNotMatchDateOfBirth() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                    .amka("06045678912")
                    .build();

            when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId()))
                    .thenReturn(false);

            DomainValidationException ex = assertThrows(
                    DomainValidationException.class,
                    () -> validator.validateForUpdate(existing, dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_DATE_MISMATCH);
            verify(repository).existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId());
        }

        @Test
        void shouldThrowDomainValidation_whenDateOfBirthChangedAndDoesNotMatchAmka() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                    .dateOfBirth(LocalDate.of(1956, 4, 6))
                    .build();

            DomainValidationException ex = assertThrows(
                    DomainValidationException.class,
                    () -> validator.validateForUpdate(existing, dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_DATE_MISMATCH);
            verifyNoInteractions(repository);
        }

        @Test
        void shouldNotThrow_whenAmkaChangedAndDataIsValid() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                    .amka("06048678913")
                    .build();

            when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId()))
                    .thenReturn(false);

            assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));

            verify(repository).existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId());
        }

        @Test
        void shouldNotThrow_whenNothingChanged() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder().build();

            assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));

            verifyNoInteractions(repository);
        }

        @Test
        void shouldNotCheckDuplicate_whenAmkaDidNotChange() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                    .firstName("Jonathan")
                    .build();

            assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));

            verify(repository, never()).existsByAmkaAndPublicIdNot(any(), any());
            verifyNoMoreInteractions(repository);
        }

        @Test
        void shouldNotCheckAmkaConsistency_whenDuplicateAmkaFound() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                    .amka("06048612345")
                    .dateOfBirth(LocalDate.of(1950, 1, 1))
                    .build();

            when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId()))
                    .thenReturn(true);

            ConflictValidationException ex = assertThrows(
                    ConflictValidationException.class,
                    () -> validator.validateForUpdate(existing, dto)
            );

            assertValidationError(ex, "amka", ErrorCode.AMKA_ALREADY_EXISTS);
            verify(repository).existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId());
            verifyNoMoreInteractions(repository);
        }
    }


    private void assertValidationError(
            BaseValidationException ex,
            String field,
            ErrorCode expectedErrorCode
    ) {
        assertEquals(
                expectedErrorCode.getMessageKey(),
                ex.getValidationErrors().get(field).getFirst()
        );
    }

    private Beneficiary.BeneficiaryBuilder defaultBeneficiary() {
        return Beneficiary.builder()
                .id(1L)
                .publicId(BENEFICIARY_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .amka("06048678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(defaultHouseUnit().build())
                .permanentAddress(defaultAddress().build())
                .emergencyContact(defaultEmergencyContact().build())
                .isActive(true);
    }

    private BeneficiaryCreateDTO.BeneficiaryCreateDTOBuilder defaultCreateDTO() {
        return BeneficiaryCreateDTO.builder()
                .amka("06046678912")
                .dateOfBirth(LocalDate.of(1966, 4, 6));
    }

    private Address.AddressBuilder defaultAddress() {
        return Address.builder()
                .street("Αγίου Μελετίου")
                .streetNumber("32")
                .city("Αθήνα")
                .zipCode("11361");
    }

    private EmergencyContact.EmergencyContactBuilder defaultEmergencyContact() {
        return EmergencyContact.builder()
                .firstName("Γιάννης")
                .lastName("Παπαδόπουλος")
                .relationshipType(RelationshipType.FRIEND)
                .address(defaultAddress().build());
    }

    private HouseUnit.HouseUnitBuilder defaultHouseUnit() {
        return HouseUnit.builder()
                .id(1L)
                .publicId(HOUSE_UNIT_PUBLIC_ID)
                .code("UNIT_A")
                .displayName("Στέγη Α")
                .address("Ελπίδας 10, Μαρούσι");
    }
}