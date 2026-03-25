package io.github.amichailides.merimna.service.validation;

import io.github.amichailides.merimna.beneficiary.BeneficiaryValidator;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryValidatorTest {
    @Mock
    private  BeneficiaryRepository repository;

    private BeneficiaryValidator validator;


    @BeforeEach
    void setUp() {
        validator = new BeneficiaryValidator(repository);
    }

    @Test
    void shouldThrowExceptionWhenAmkaAlreadyExistsOnSave() {

        BeneficiarySaveDTO dto = BeneficiarySaveDTO.builder()
                .amka("06046678912")
                .dateOfBirth(LocalDate.of(1966, 4, 6))
                .build();

        when(repository.existsByAmka(dto.amka())).thenReturn(true);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> validator.validateForSave(dto)
        );

        assertEquals(
                ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()); // Map<String, List<String>>

    }


    @Test
    void shouldThrowExceptionWhenAmkaNotConsistentWithDobOnSave() {
        BeneficiarySaveDTO dto = BeneficiarySaveDTO.builder()
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
        BeneficiarySaveDTO dto = BeneficiarySaveDTO.builder()
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

        when(repository.existsByAmkaAndIdNot(dto.amka(), existing.getId())).thenReturn(true);

        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
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

        when(repository.existsByAmkaAndIdNot(dto.amka(), existing.getId())).thenReturn(false);

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

        when(repository.existsByAmkaAndIdNot(dto.amka(), existing.getId())).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));
    }

    @Test
    void shouldNotThrowWhenNothingChangedOnUpdate() {
        Beneficiary existing = createDefaultBeneficiary(1L, true);

        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .houseUnit(HouseUnit.UNIT_B)
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
        verify(repository, never()).existsByAmkaAndIdNot(any(), any());

    }

    @Test
    void validateForUpdate_shouldNotCheckConsistency_whenDuplicateAmkaFound() {
        // arrange
        Beneficiary existing = createDefaultBeneficiary(1L, true);
        Long id = existing.getId();
        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder()
                .amka("06048612345")
                .dateOfBirth(LocalDate.of(1950, 1, 1)) // deliberately inconsistent
                .build();

        when(repository.existsByAmkaAndIdNot(dto.amka(), id)).thenReturn(true);

        // act
        DomainValidationException ex = assertThrows(
                DomainValidationException.class,
                () -> validator.validateForUpdate(existing, dto)
        );

        // assert
        assertEquals(
                ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey(),
                ex.getValidationErrors().get("amka").getFirst()
        );

        verify(repository).existsByAmkaAndIdNot(dto.amka(), existing.getId());
        verifyNoMoreInteractions(repository);

    }


    private Beneficiary createDefaultBeneficiary(Long id, boolean isActive) {
        return Beneficiary.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .amka(("12345678912"))
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(HouseUnit.UNIT_A)
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
}
