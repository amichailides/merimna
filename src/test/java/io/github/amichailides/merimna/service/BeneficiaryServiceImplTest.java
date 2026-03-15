package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.common.ErrorCode;
import io.github.amichailides.merimna.dto.*;
import io.github.amichailides.merimna.exception.BeneficiaryAlreadyInactiveException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByAmkaException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.exception.BeneficiaryValidationException;
import io.github.amichailides.merimna.mapper.BeneficiaryMapper;
import io.github.amichailides.merimna.model.*;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import io.github.amichailides.merimna.service.validation.BeneficiaryValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    //@DisplayName("Should throw exception when beneficiary with given AMKA does not exist")
    void findByAmka_shouldThrowException_whenBeneficiaryMissing() {
        // arrange
        String amka = "12345678912";

        when(beneficiaryRepository.findByAmka(amka))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByAmkaException.class,
                () -> beneficiaryService.findByAmka(amka)
        );
    }

    @Test
    //@DisplayName("Should not save beneficiary when validation fails")
    void save_shouldNotPersist_whenValidationFails() {
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
        doThrow(new BeneficiaryValidationException(errors))
                .when(validator)
                .validateForSave(dto);

        assertThrows(
                BeneficiaryValidationException.class,
                () -> beneficiaryService.save(dto)
        );

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

    @ParameterizedTest
    @MethodSource("provideFilterCombinations")
    //@DisplayName("Should call correct repository method based on filters")
    void findAll_shouldUseCorrectRepositoryMethod(
            boolean includeInactive,
            HouseUnit houseUnit) {
        // arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Beneficiary> mockPage = new PageImpl<>(List.of(createDefaultBeneficiary(true)));

        lenient().when(beneficiaryRepository.findAll(pageable)).thenReturn(mockPage);
        lenient().when(beneficiaryRepository.findAllByIsActiveTrue(pageable)).thenReturn(mockPage);
        lenient().when(beneficiaryRepository.findAllByHouseUnit(any(), any())).thenReturn(mockPage);
        lenient().when(beneficiaryRepository.findAllByHouseUnitAndIsActiveTrue(any(), any())).thenReturn(mockPage);

        // act
        beneficiaryService.findAllBeneficiaries(includeInactive, houseUnit, pageable);

        // assert
        if (houseUnit != null) {
            if (includeInactive) {
                verify(beneficiaryRepository).findAllByHouseUnit(houseUnit, pageable);
            } else {
                verify(beneficiaryRepository).findAllByHouseUnitAndIsActiveTrue(houseUnit, pageable);
            }
        } else {
            if (includeInactive) {
                verify(beneficiaryRepository).findAll(pageable);
            } else {
                verify(beneficiaryRepository).findAllByIsActiveTrue(pageable);
            }
        }
    }

    @Test
    //@DisplayName("Should return a mapped page of DTOs when searching")
    void search_shouldReturnMappedPage_whenValidTermProvided() {
        // arrange
        String term = "Παπαδόπουλος";
        Pageable pageable = PageRequest.of(0, 10);

        Beneficiary beneficiary = createDefaultBeneficiary(true);
        Page<Beneficiary> entityPage = new PageImpl<>(List.of(beneficiary));

        BeneficiaryReadOnlyDTO expectedDto = BeneficiaryReadOnlyDTO.builder().build();

        when(beneficiaryRepository.findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)
        )).thenReturn(entityPage);

        when(beneficiaryMapper.toReadOnlyDTO(beneficiary)).thenReturn(expectedDto);

        // act
        Page<BeneficiaryReadOnlyDTO> result = beneficiaryService.search(term, pageable);

        // assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedDto, result.getContent().getFirst());


        verify(beneficiaryRepository)
                .findAll(ArgumentMatchers.<Specification<Beneficiary>>any(), eq(pageable));
        verify(beneficiaryMapper).toReadOnlyDTO(beneficiary);

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
    void getBeneficiaryByAmka_shouldReturnDto_whenAmkaExists() {
        // arrange
        String amka = "12345678912";
        Beneficiary beneficiary = createDefaultBeneficiary(true);

        BeneficiaryReadOnlyDTO expectedDto = BeneficiaryReadOnlyDTO.builder().build();

        when(beneficiaryRepository.findByAmka(amka)).thenReturn(Optional.of(beneficiary));
        when(beneficiaryMapper.toReadOnlyDTO(beneficiary)).thenReturn(expectedDto);

        // act
        BeneficiaryReadOnlyDTO result = beneficiaryService.findByAmka(amka);

        // assert
        assertEquals(expectedDto, result);
        verify(beneficiaryRepository).findByAmka(amka);
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
    void search_shouldReturnEmptyPage_whenNoMatchesFound() {
        // arrange
        String term = "Παπαχαραλάμπους";
        Pageable pageable = PageRequest.of(0, 10);

        when(beneficiaryRepository.findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)
        )).thenReturn(Page.empty());

        // act
        Page<BeneficiaryReadOnlyDTO> result = beneficiaryService.search(term, pageable);

        // assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        verifyNoInteractions(beneficiaryMapper);

    }

    @Test
    void update_shouldNotPersist_whenValidationFails() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary existing = createDefaultBeneficiary(true);
        existing.setId(beneficiaryId);

        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .amka("06047678912")
                .build();

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(existing));
        doThrow(new BeneficiaryValidationException(Map.of(
                "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
        )))
                .when(validator)
                .validateForUpdate(existing, updateDto);

        // act & assert
        assertThrows(BeneficiaryValidationException.class,
                () -> beneficiaryService.updateBeneficiary(beneficiaryId, updateDto));

        verify(beneficiaryRepository, never()).save(any());
        verify(beneficiaryMapper, never()).toReadOnlyDTO(existing);
    }

    @Test
    void findAll_shouldReturnMappedPage_whenRepositoryReturnsEntities() {
        // arrange
        Pageable pageable = PageRequest.of(0, 10);
        Beneficiary beneficiary = createDefaultBeneficiary(true);
        BeneficiaryReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(1L, true);

        when(beneficiaryRepository.findAllByIsActiveTrue(pageable))
                .thenReturn(new PageImpl<>(List.of(beneficiary)));
        when(beneficiaryMapper.toReadOnlyDTO(beneficiary)).thenReturn(expectedDto);

        // act
        Page<BeneficiaryReadOnlyDTO> result = beneficiaryService.findAllBeneficiaries(
                false, null, pageable);

        // assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedDto, result.getContent().getFirst());
        verify(beneficiaryMapper).toReadOnlyDTO(beneficiary);
    }


    private static Stream<Arguments> provideFilterCombinations() {
        return Stream.of(
                Arguments.of(true, HouseUnit.UNIT_A),
                Arguments.of(false, HouseUnit.UNIT_A),
                Arguments.of(true, null),
                Arguments.of(false, null)
        );
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
