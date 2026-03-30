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
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO: Expand findBeneficiaries coverage
// Remaining scenarios: q only, amka only, houseUnit filter, blank criteria handling
@ExtendWith(MockitoExtension.class)
public class BeneficiaryServiceImplTest {
    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private BeneficiaryMapper beneficiaryMapper;

    @Mock
    private BeneficiaryValidator validator;

    @Mock
    private HouseUnitRepository houseUnitRepository;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    @Test
    void findById_shouldThrowException_whenBeneficiaryMissing() {
        // arrange
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.findWithDetailsById(beneficiaryId))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByIdException.class,
                () -> beneficiaryService.findById(beneficiaryId)
        );
        verify(beneficiaryRepository).findWithDetailsById(beneficiaryId);
        verifyNoInteractions(beneficiaryMapper);
    }

    @Test
    void save_shouldNotMapOrPersist_whenValidationFails() {
        // arrange
        BeneficiarySaveDTO dto = createDefaultBeneficiarySaveDTO();

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
    void findById_shouldReturnDetailsDto_whenBeneficiaryExists() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary beneficiary = createDefaultBeneficiary(beneficiaryId, true);

        BeneficiaryDetailsDTO expectedDto = BeneficiaryDetailsDTO.builder()
                .id(beneficiaryId)
                .build();

        when(beneficiaryRepository.findWithDetailsById(beneficiaryId))
                .thenReturn(Optional.of(beneficiary));

        when(beneficiaryMapper.toDetailsDTO(beneficiary))
                .thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.findById(beneficiaryId);

        // assert
        assertEquals(expectedDto, result);

        verify(beneficiaryRepository).findWithDetailsById(beneficiaryId);
        verify(beneficiaryMapper).toDetailsDTO(beneficiary);

    }

    @Test
    void save_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        BeneficiarySaveDTO saveDto = createDefaultBeneficiarySaveDTO();
        Beneficiary entityFromMapper = createDefaultBeneficiary(false);
        Beneficiary savedEntity = createDefaultBeneficiary(1L, true);

        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(1L, true);

        when(houseUnitRepository.findByCode(saveDto.houseUnitCode()))
                .thenReturn(Optional.of(createDefaultHouseUnit()));
        when(beneficiaryMapper.toEntity(eq(saveDto), any(HouseUnit.class)))
                .thenReturn(entityFromMapper);
        when(beneficiaryRepository.save(entityFromMapper)).thenReturn(savedEntity);
        when(beneficiaryMapper.toDetailsDTO(savedEntity)).thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.save(saveDto);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(beneficiaryRepository).save(entityFromMapper);
        verify(beneficiaryMapper).toEntity(eq(saveDto), any(HouseUnit.class));
        verify(houseUnitRepository).findByCode(saveDto.houseUnitCode());
        verify(beneficiaryMapper).toDetailsDTO(savedEntity);
        verify(validator).validateForSave(saveDto);
    }

    @Test
    void update_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary existing = createDefaultBeneficiary(beneficiaryId, true);

        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .firstName("UpdatedName")
                .build();

        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(beneficiaryId, true);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(existing));
        when(beneficiaryRepository.save(existing)).thenReturn(existing);
        when(beneficiaryMapper.toDetailsDTO(existing)).thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.updateBeneficiary(beneficiaryId, updateDto);

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
        Beneficiary existing = createDefaultBeneficiary(beneficiaryId, true);

        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(beneficiaryId, false);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(existing));
        when(beneficiaryRepository.save(existing)).thenReturn(existing);
        when(beneficiaryMapper.toDetailsDTO(existing)).thenReturn(expectedDto);

        //act
        BeneficiaryDetailsDTO result = beneficiaryService.discharge(beneficiaryId);

        //assert
        assertFalse(existing.isActive(), "Beneficiary should be inactive after discharge");
        assertEquals(expectedDto, result);
        verify(validator).validateForDischarge(existing);
        verify(beneficiaryRepository).save(existing);
        verify(beneficiaryMapper).toDetailsDTO(existing);
    }

    @Test
    void update_shouldNotMapOrPersist_whenValidationFails() {
        // arrange
        Long beneficiaryId = 1L;
        Beneficiary existing = createDefaultBeneficiary(beneficiaryId, true);

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
        verify(beneficiaryMapper, never()).toDetailsDTO(any());
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void discharge_shouldNotValidateOrPersist_whenAlreadyInactive() {
        Long id = 1L;
        Beneficiary inactiveBeneficiary = createDefaultBeneficiary(false);

        when(beneficiaryRepository.findById(id)).thenReturn(Optional.of(inactiveBeneficiary));

        // act & assert
        assertThrows(BeneficiaryAlreadyInactiveException.class,
                () -> beneficiaryService.discharge(id)
        );

        // verify
        verify(beneficiaryRepository).findById(id);
        verify(validator, never()).validateForDischarge(any());
        verify(beneficiaryRepository, never()).save(any());
        verify(beneficiaryMapper, never()).toDetailsDTO(any());

    }

    @Test
    void findBeneficiaries_shouldReturnMappedPage() {
        // arrange
        BeneficiarySearchDTO criteria = BeneficiarySearchDTO.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        Beneficiary beneficiary = createDefaultBeneficiary(true);
        BeneficiaryListDTO dto = createDefaultListDTO(1L, true);

        Page<Beneficiary> entityPage = new PageImpl<>(List.of(beneficiary));

        when(beneficiaryRepository.findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)))
                .thenReturn(entityPage);
        when(beneficiaryMapper.toListDTO(beneficiary)).thenReturn(dto);

        // act
        Page<BeneficiaryListDTO> result = beneficiaryService.findBeneficiaries(criteria, pageable);

        // assert
        assertEquals(1, result.getContent().size());
        assertSame(dto, result.getContent().getFirst());

        verify(beneficiaryRepository).findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)
        );
        verify(beneficiaryMapper).toListDTO(beneficiary);
    }

    @Test
    void findBeneficiaries_shouldUseAmkaPrecedenceOverQ() {
        // arrange
        BeneficiarySearchDTO criteria = BeneficiarySearchDTO.builder()
                .amka("12345678912")
                .q("joe")
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Beneficiary> entityPage = Page.empty(pageable);

        when(beneficiaryRepository.findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)))
                .thenReturn(entityPage);

        // act
        Page<BeneficiaryListDTO> result = beneficiaryService.findBeneficiaries(criteria, pageable);

        // assert
        assertTrue(result.isEmpty());
        verify(beneficiaryRepository).findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)
        );
        verifyNoInteractions(beneficiaryMapper);
    }

    @Test
    void findBeneficiaries_shouldIncludeInactive_whenRequested() {
        // arrange
        BeneficiarySearchDTO criteria = BeneficiarySearchDTO.builder()
                .includeInactive(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Beneficiary> entityPage = Page.empty(pageable);

        when(beneficiaryRepository.findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)))
                .thenReturn(entityPage);

        // act
        Page<BeneficiaryListDTO> result = beneficiaryService.findBeneficiaries(criteria, pageable);

        // assert
        assertTrue(result.isEmpty());

        verify(beneficiaryRepository).findAll(
                ArgumentMatchers.<Specification<Beneficiary>>any(),
                eq(pageable)
        );
        verifyNoInteractions(beneficiaryMapper);
    }


    private Beneficiary createDefaultBeneficiary(Long id, boolean isActive) {
        return Beneficiary.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(createDefaultHouseUnit())
                .permanentAddress(Address.builder()
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

    private Beneficiary createDefaultBeneficiary(boolean isActive) {
        return createDefaultBeneficiary(1L, isActive);
    }

    private BeneficiarySaveDTO createDefaultBeneficiarySaveDTO() {
        return BeneficiarySaveDTO.builder()
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnitCode("UNIT_A")
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

    private BeneficiaryDetailsDTO createDefaultDetailsDTO(Long id, boolean isActive) {
        return BeneficiaryDetailsDTO.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnitCode("UNIT_A")
                .houseUnitDisplayName("Στέγη Α")
                .isActive(isActive)
                .permanentAddress(createDefaultAddressDTO())
                .emergencyContact(createDefaultEmergencyContactDTO())
                .build();
    }

    private BeneficiaryListDTO createDefaultListDTO(Long id, boolean isActive) {
        return BeneficiaryListDTO.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .houseUnit("UNIT_A")
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
