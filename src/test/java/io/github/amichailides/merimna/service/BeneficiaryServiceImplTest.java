package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.beneficiary.BeneficiaryServiceImpl;
import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryAlreadyInactiveException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.beneficiary.BeneficiaryMapper;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.beneficiary.BeneficiaryValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO: Add tests for findBeneficiaries(BeneficiarySearchDTO, Pageable)
// Scenarios: q only, amka only, amka+q precedence, includeInactive, houseUnit filter
@ExtendWith(MockitoExtension.class)
public class BeneficiaryServiceImplTest {
    @Mock
    private BeneficiaryRepository  beneficiaryRepository;

    @Mock
    private BeneficiaryMapper beneficiaryMapper;

    @Mock
    private BeneficiaryValidator validator;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    @Test
    //@DisplayName("Should throw exception when beneficiary with given id does not exist")
    void findById_shouldThrowException_whenBeneficiaryMissing() {
        // arrange
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.findById(beneficiaryId))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByIdException.class,
                () -> beneficiaryService.findById(beneficiaryId)
        );
        verify(beneficiaryRepository).findById(beneficiaryId);

    }

    @Test
    //@DisplayName("Should not save beneficiary when validation fails")
    void save_shouldNotMapOrPersist_whenValidationFails() {
        // arrange
        BeneficiarySaveDTO dto = BeneficiarySaveDTO.builder()
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(HouseUnit.UNIT_A)
                .build();

        Map<String, String> errors = Map.of(
                "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
        );

        // act & assert
        doThrow(new DomainValidationException(errors))
                .when(validator)
                .validateForSave(dto);

        assertThrows(
                DomainValidationException.class,
                () -> beneficiaryService.save(dto)
        );

        verify(validator).validateForSave(dto);
        verifyNoInteractions(beneficiaryMapper);
        verify(beneficiaryRepository, never()).save(any());

    }

    @Test
    //@DisplayName("Update should fail fast when beneficiary is missing")
    void update_shouldFailFast_whenNotFound() {
        // arrange
        Long beneficiaryId = 1L;
        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder().build();

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(BeneficiaryNotFoundByIdException.class,
                () -> beneficiaryService.updateBeneficiary(beneficiaryId, dto)
        );

        verify(beneficiaryRepository).findById(beneficiaryId);
        verify(beneficiaryRepository, never()).save(any());
        verifyNoInteractions(validator);
    }

    @Test
    //@DisplayName("Discharge: Fail when ID not found")
    void discharge_shouldThrowNotFound_whenIdMissing() {
        // arrange
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.findById(beneficiaryId))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByIdException.class,
                () -> beneficiaryService.discharge(beneficiaryId)
        );
        verify(beneficiaryRepository).findById(beneficiaryId);
        verifyNoInteractions(validator);
    }

    @Test
    //@DisplayName("Should throw exception when beneficiary is already inactive")
    void discharge_shouldThrowException_whenAlreadyInactive() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary beneficiary = createDefaultBeneficiary(false);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));

        // act & assert
        assertThrows(BeneficiaryAlreadyInactiveException.class,
                () -> beneficiaryService.discharge(beneficiaryId));

        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    //@DisplayName("getBeneficiaryById: Should return DTO when beneficiary exists")
    void getBeneficiaryById_shouldReturnDto_whenIdExists(){
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary beneficiary = createDefaultBeneficiary(true);
        beneficiary.setId(beneficiaryId);

        BeneficiaryReadOnlyDTO expectedDto = BeneficiaryReadOnlyDTO.builder()
                .id(beneficiaryId)
                .build();

        when(beneficiaryRepository.findById(beneficiaryId))
                .thenReturn(Optional.of(beneficiary));

        when(beneficiaryMapper.toReadOnlyDTO(beneficiary))
                .thenReturn(expectedDto);

        // act
        BeneficiaryReadOnlyDTO result = beneficiaryService.findById(beneficiaryId);

        // assert
        assertEquals(expectedDto, result);

        verify(beneficiaryRepository).findById(beneficiaryId);
        verify(beneficiaryMapper).toReadOnlyDTO(beneficiary);

    }

    @Test
    void save_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        BeneficiarySaveDTO saveDto = createDefaultBeneficiarySaveDTO();
        Beneficiary entityFromMapper = createDefaultBeneficiary(false);
        Beneficiary savedEntity = createDefaultBeneficiary(true);
        savedEntity.setId(1L);

        BeneficiaryReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(1L, true);

        when(beneficiaryMapper.toEntity(saveDto)).thenReturn(entityFromMapper);
        when(beneficiaryRepository.save(entityFromMapper)).thenReturn(savedEntity);
        when(beneficiaryMapper.toReadOnlyDTO(savedEntity)).thenReturn(expectedDto);

        // act
        BeneficiaryReadOnlyDTO result = beneficiaryService.save(saveDto);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(beneficiaryRepository).save(entityFromMapper);
        verify(beneficiaryMapper).toEntity(saveDto);
        verify(beneficiaryMapper).toReadOnlyDTO(savedEntity);
        verify(validator).validateForSave(saveDto);
    }

    @Test
    void update_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary existing = createDefaultBeneficiary(true);
        existing.setId(1L);

        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .houseUnit(HouseUnit.UNIT_B)
                .build();

        BeneficiaryReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(beneficiaryId, true);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(existing));
        when(beneficiaryRepository.save(existing)).thenReturn(existing);
        when(beneficiaryMapper.toReadOnlyDTO(existing)).thenReturn(expectedDto);

        // act
        BeneficiaryReadOnlyDTO result = beneficiaryService.updateBeneficiary(beneficiaryId, updateDto);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(validator).validateForUpdate(existing, updateDto);
        verify(beneficiaryMapper).updateEntity(existing, updateDto);
        verify(beneficiaryRepository).save(existing);

    }

    @Test
    void discharge_shouldSetInactiveAndReturnDto_whenBeneficiaryIsActive() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary existing = createDefaultBeneficiary(true);
        existing.setId(1L);

        BeneficiaryReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(beneficiaryId, false);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(existing));
        when(beneficiaryRepository.save(existing)).thenReturn(existing);
        when(beneficiaryMapper.toReadOnlyDTO(existing)).thenReturn(expectedDto);

        //act
        BeneficiaryReadOnlyDTO result = beneficiaryService.discharge(beneficiaryId);

        //assert
        assertFalse(existing.getIsActive());
        assertEquals(expectedDto, result);
        verify(validator).validateForDischarge(existing);
        verify(beneficiaryRepository).save(existing);
        verify(beneficiaryMapper).toReadOnlyDTO(existing);
    }

    @Test
    void update_shouldNotMapOrPersist_whenValidationFails() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary existing = createDefaultBeneficiary(true);
        existing.setId(beneficiaryId);

        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .amka("06047678912")
                .build();

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(existing));
        doThrow(new DomainValidationException(Map.of(
                "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
        )))
                .when(validator)
                .validateForUpdate(existing, updateDto);

        // act & assert
        assertThrows(DomainValidationException.class,
                () -> beneficiaryService.updateBeneficiary(beneficiaryId, updateDto));

        // verify flow
        verify(beneficiaryRepository).findById(beneficiaryId);
        verify(validator).validateForUpdate(existing, updateDto);

        // fail-fast
        verify(beneficiaryMapper, never()).updateEntity(any(), any());
        verify(beneficiaryMapper, never()).toReadOnlyDTO(any());
        verify(beneficiaryRepository, never()).save(any());
    }



    private Beneficiary createDefaultBeneficiary(boolean isActive) {
        return Beneficiary.builder()
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

    private BeneficiarySaveDTO createDefaultBeneficiarySaveDTO() {
        return BeneficiarySaveDTO.builder()
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(HouseUnit.UNIT_A)
                .permanentAddress(createDefaultAddressDTO())
                .emergencyContact(createDefaultEmergencyContactDTO())
                .build();
    }

    private AddressDTO createDefaultAddressDTO() {
        return AddressDTO.builder()
                .street("Αγίου Μελετίου")
                .streetNumber("32")
                .city("Αθήνα")
                .zipCode("11361")
                .build();
    }

    private EmergencyContactDTO createDefaultEmergencyContactDTO() {
        return EmergencyContactDTO.builder()
                .firstName("Γιάννης")
                .lastName("Παπαδόπουλος")
                .relationshipType(RelationshipType.FRIEND)
                .address(createDefaultAddressDTO()) // Επαναχρησιμοποίηση!
                .build();
    }

    private BeneficiaryReadOnlyDTO createDefaultReadOnlyDTO(Long id, boolean isActive) {
        return BeneficiaryReadOnlyDTO.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(HouseUnit.UNIT_A)
                .isActive(isActive)
                .permanentAddress(createDefaultAddressDTO())
                .emergencyContact(createDefaultEmergencyContactDTO())
                .build();
    }
}
