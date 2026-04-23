package io.github.amichailides.merimna.service.validation;

import io.github.amichailides.merimna.beneficiary.BeneficiaryValidator;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryCreateDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryValidatorTest {

    private static final UUID TEST_PUBLIC_ID = UUID.randomUUID();

    @Mock
    private  BeneficiaryRepository repository;

    private BeneficiaryValidator validator;


    @BeforeEach
    void setUp() {
        validator = new BeneficiaryValidator(repository);
    }

    @Test
    void shouldThrowExceptionWhenAmkaAlreadyExistsOnSave() {

        BeneficiaryCreateDTO dto = BeneficiaryCreateDTO.builder()
                .amka("06046678912")
                .dateOfBirth(LocalDate.of(1966, 4, 6))
                .build();

        when(repository.existsByAmka(dto.amka())).thenReturn(true);

        ConflictValidationException ex = assertThrows(
                ConflictValidationException.class,
                () -> validator.validateForSave(dto)
        );

        assertEquals(
                ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()); // Map<String, List<String>>

    }


    @Test
    void shouldThrowExceptionWhenAmkaNotConsistentWithDobOnSave() {
        BeneficiaryCreateDTO dto = BeneficiaryCreateDTO.builder()
                .amka("06045678912")
                .dateOfBirth(LocalDate.of(1966, 4, 6))
                .build();

        when(repository.existsByAmka(dto.amka())).thenReturn(false);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> validator.validateForSave(dto)
        );

        assertEquals(ErrorCode.AMKA_DATE_MISMATCH.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst());
    }

    @Test
    void shouldNotThrowExceptionWhenDataIsValidOnSave() {
        BeneficiaryCreateDTO dto = BeneficiaryCreateDTO.builder()
                .amka("06046678912")
                .dateOfBirth(LocalDate.of(1966, 4, 6))
                .build();

        when(repository.existsByAmka(dto.amka())).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateForSave(dto));

    }

    @Test
    void shouldThrowWhenAmkaAlreadyExistsOnUpdate () {

        // @NonNull στο Entity
        Beneficiary existing = createDefaultBeneficiary(1L,true);
        BeneficiaryUpdateDTO dto =  BeneficiaryUpdateDTO.builder()
                .amka("06046678912")
                .build();

        when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId())).thenReturn(true);

        ConflictValidationException ex = assertThrows(
                ConflictValidationException.class,
                () -> validator.validateForUpdate(existing, dto)
        );

        assertEquals(
                ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()
        );

    }

    @Test
    void shouldThrowWhenAmkaChangedAndMismatchesDobOnUpdate () {

        Beneficiary existing = createDefaultBeneficiary(1L, true);

        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .amka("06045678912")
                .build();

        when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId())).thenReturn(false);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> validator.validateForUpdate(existing, dto)
        );

        assertEquals(
                ErrorCode.AMKA_DATE_MISMATCH.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()
        );
    }

    @Test
    void shouldThrowWhenDobChangedAndMismatchesAmkaOnUpdate () {
        Beneficiary existing = createDefaultBeneficiary(1L,true);

        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .dateOfBirth(LocalDate.of(1956,4, 6))
                .build();


        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> validator.validateForUpdate(existing, dto)
        );

        assertEquals(
                ErrorCode.AMKA_DATE_MISMATCH.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()
        );

    }

    @Test
    void shouldNotThrowExceptionWhenDataIsValidOnUpdate() {
        Beneficiary existing = createDefaultBeneficiary(1L,true);

        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .amka("06048678913")
                .build();

        when(repository.existsByAmkaAndPublicIdNot(dto.amka(), existing.getPublicId())).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));
    }

    @Test
    void shouldNotThrowWhenNothingChangedOnUpdate() {
        Beneficiary existing = createDefaultBeneficiary(1L, true);

        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .build();

        assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));
    }

    @Test
    void validateForUpdate_shouldNotCheckDuplicate_whenAmkaDidNotChange() {
        // arrange
        Beneficiary existing = createDefaultBeneficiary(1L,true);
        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .firstName("Jonathan")
                .build();

        // act
        assertDoesNotThrow(
                () -> validator.validateForUpdate(existing, updateDto)
        );

        //assert
        verify(repository, never()).existsByAmkaAndPublicIdNot(any(), any());

    }

    @Test
    void validateForUpdate_shouldNotCheckConsistency_whenDuplicateAmkaFound() {
        // arrange
        Beneficiary existing = createDefaultBeneficiary(1L, true);
        UUID publicId = existing.getPublicId();
        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .amka("06048612345")
                .dateOfBirth(LocalDate.of(1950, 1, 1)) // deliberately inconsistent
                .build();

        when(repository.existsByAmkaAndPublicIdNot(dto.amka(), publicId)).thenReturn(true);

        // act
        ConflictValidationException ex = assertThrows(
                ConflictValidationException.class,
                () -> validator.validateForUpdate(existing, dto)
        );

        // assert
        assertEquals(
                ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()
        );

        verify(repository).existsByAmkaAndPublicIdNot(dto.amka(), publicId);
        verifyNoMoreInteractions(repository);

    }


    private Beneficiary createDefaultBeneficiary(Long id, boolean isActive) {
        return Beneficiary.builder()
                .id(id)
                .publicId(TEST_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .amka("06048678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(createDefaultHouseUnit())
                .permanentAddress( Address.builder()
                        .street("Αγίου Μελέτιου")
                        .streetNumber("32")
                        .city("Αθήνα")
                        .zipCode("11361")
                        .build())
                .emergencyContact(EmergencyContact.builder()
                        .firstName("Γιάννης")
                        .lastName("Παπαδόπουλος")
                        .relationshipType(RelationshipType.FRIEND)
                        .address(Address.builder()
                                .street("Άγου Μελέτιου")
                                .streetNumber("32")
                                .city("Αθήνα")
                                .zipCode("11361")
                                .build())
                        .build())
                .isActive(isActive)
                .build();
    }

    private HouseUnit createDefaultHouseUnit() {
        return HouseUnit.builder()
                .id(1L)
                .code("UNIT_A")
                .displayName("Στέγη Α")
                .address("Ελπίδας 10, Μαρούσι")
                .build();
    }
}
