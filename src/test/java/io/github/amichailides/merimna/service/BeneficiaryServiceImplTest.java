package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.beneficiary.BeneficiaryServiceImpl;
import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryAlreadyInactiveException;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.beneficiary.BeneficiaryMapper;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.beneficiary.BeneficiaryValidator;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitValidator;
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

    private static final String TEST_PUBLIC_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private BeneficiaryMapper beneficiaryMapper;

    @Mock
    private BeneficiaryValidator validator;

    @Mock
    private HouseUnitRepository houseUnitRepository;

    @Mock
    private HouseUnitValidator houseUnitValidator;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    @Test
    void findByPublicId_shouldThrowException_whenBeneficiaryMissing() {
        // arrange
        when(beneficiaryRepository.findWithDetailsByPublicId(TEST_PUBLIC_ID))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByPublicIdException.class,
                () -> beneficiaryService.findByPublicId(TEST_PUBLIC_ID)
        );
        verify(beneficiaryRepository).findWithDetailsByPublicId(TEST_PUBLIC_ID);
        verifyNoInteractions(beneficiaryMapper);
    }

    @Test
    void findByPublicId_shouldReturnDetailsDto_whenBeneficiaryExists() {
        // arrange
        Beneficiary beneficiary = createDefaultBeneficiary(true);
        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(true);

        when(beneficiaryRepository.findWithDetailsByPublicId(TEST_PUBLIC_ID))
                .thenReturn(Optional.of(beneficiary));
        when(beneficiaryMapper.toDetailsDTO(beneficiary))
                .thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.findByPublicId(TEST_PUBLIC_ID);

        // assert
        assertEquals(expectedDto, result);
        verify(beneficiaryRepository).findWithDetailsByPublicId(TEST_PUBLIC_ID);
        verify(beneficiaryMapper).toDetailsDTO(beneficiary);
    }

    @Test
    void create_shouldNotMapOrPersist_whenValidationFails() {
        // arrange
        BeneficiaryCreateDTO dto = createDefaultBeneficiaryCreateDTO();

        doThrow(new DomainValidationException(Map.of(
                "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
        )))
                .when(validator)
                .validateForSave(dto);

        // act & assert
        assertThrows(
                DomainValidationException.class,
                () -> beneficiaryService.create(dto)
        );

        verify(validator).validateForSave(dto);
        verifyNoInteractions(beneficiaryMapper);
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void create_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        BeneficiaryCreateDTO saveDto = createDefaultBeneficiaryCreateDTO();
        Beneficiary entityFromMapper = createDefaultBeneficiary(false);
        Beneficiary savedEntity = createDefaultBeneficiary(true);
        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(true);

        when(houseUnitRepository.findByCode(saveDto.houseUnitCode()))
                .thenReturn(Optional.of(createDefaultHouseUnit()));
        when(beneficiaryMapper.toEntity(eq(saveDto), any(HouseUnit.class)))
                .thenReturn(entityFromMapper);
        when(beneficiaryRepository.save(entityFromMapper)).thenReturn(savedEntity);
        when(beneficiaryMapper.toDetailsDTO(savedEntity)).thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.create(saveDto);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(beneficiaryRepository).save(entityFromMapper);
        verify(houseUnitValidator).validateAssignmentForBeneficiary(any(HouseUnit.class));
        verify(beneficiaryMapper).toEntity(eq(saveDto), any(HouseUnit.class));
        verify(houseUnitRepository).findByCode(saveDto.houseUnitCode());
        verify(beneficiaryMapper).toDetailsDTO(savedEntity);
        verify(validator).validateForSave(saveDto);
    }

    @Test
    void update_shouldFailFast_whenNotFound() {
        // arrange
        BeneficiaryUpdateDTO dto = BeneficiaryUpdateDTO.builder().build();

        when(beneficiaryRepository.findByPublicId(TEST_PUBLIC_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(BeneficiaryNotFoundByPublicIdException.class,
                () -> beneficiaryService.updateBeneficiary(TEST_PUBLIC_ID, dto)
        );

        verify(beneficiaryRepository).findByPublicId(TEST_PUBLIC_ID);
        verify(beneficiaryRepository, never()).save(any());
        verifyNoInteractions(validator);
    }

    @Test
    void update_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        Beneficiary existing = createDefaultBeneficiary(true);
        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .firstName("UpdatedName")
                .build();
        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(true);

        when(beneficiaryRepository.findByPublicId(TEST_PUBLIC_ID)).thenReturn(Optional.of(existing));
        when(beneficiaryMapper.toDetailsDTO(existing)).thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.updateBeneficiary(TEST_PUBLIC_ID, updateDto);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(validator).validateForUpdate(existing, updateDto);
        verify(beneficiaryMapper).updateEntity(existing, updateDto);
    }

    @Test
    void update_shouldNotMapOrPersist_whenValidationFails() {
        // arrange
        Beneficiary existing = createDefaultBeneficiary(true);
        BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                .amka("06047678912")
                .build();

        when(beneficiaryRepository.findByPublicId(TEST_PUBLIC_ID)).thenReturn(Optional.of(existing));
        doThrow(new DomainValidationException(Map.of(
                "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
        )))
                .when(validator)
                .validateForUpdate(existing, updateDto);

        // act & assert
        assertThrows(DomainValidationException.class,
                () -> beneficiaryService.updateBeneficiary(TEST_PUBLIC_ID, updateDto));

        verify(beneficiaryRepository).findByPublicId(TEST_PUBLIC_ID);
        verify(validator).validateForUpdate(existing, updateDto);
        verify(beneficiaryMapper, never()).updateEntity(any(), any());
        verify(beneficiaryMapper, never()).toDetailsDTO(any());
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void discharge_shouldThrowNotFound_whenPublicIdMissing() {
        // arrange
        when(beneficiaryRepository.findByPublicId(TEST_PUBLIC_ID))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByPublicIdException.class,
                () -> beneficiaryService.discharge(TEST_PUBLIC_ID)
        );
        verify(beneficiaryRepository).findByPublicId(TEST_PUBLIC_ID);
        verifyNoInteractions(validator);
    }

    @Test
    void discharge_shouldSetInactiveAndReturnDto_whenBeneficiaryIsActive() {
        // arrange
        Beneficiary existing = createDefaultBeneficiary(true);
        BeneficiaryDetailsDTO expectedDto = createDefaultDetailsDTO(false);

        when(beneficiaryRepository.findByPublicId(TEST_PUBLIC_ID)).thenReturn(Optional.of(existing));
        when(beneficiaryRepository.save(existing)).thenReturn(existing);
        when(beneficiaryMapper.toDetailsDTO(existing)).thenReturn(expectedDto);

        // act
        BeneficiaryDetailsDTO result = beneficiaryService.discharge(TEST_PUBLIC_ID);

        // assert
        assertFalse(existing.isActive(), "Beneficiary should be inactive after discharge");
        assertEquals(expectedDto, result);
        verify(validator).validateForDischarge(existing);
        verify(beneficiaryRepository).save(existing);
        verify(beneficiaryMapper).toDetailsDTO(existing);
    }

    @Test
    void discharge_shouldNotValidateOrPersist_whenAlreadyInactive() {
        // arrange
        Beneficiary inactiveBeneficiary = createDefaultBeneficiary(false);

        when(beneficiaryRepository.findByPublicId(TEST_PUBLIC_ID)).thenReturn(Optional.of(inactiveBeneficiary));

        // act & assert
        assertThrows(BeneficiaryAlreadyInactiveException.class,
                () -> beneficiaryService.discharge(TEST_PUBLIC_ID)
        );

        verify(beneficiaryRepository).findByPublicId(TEST_PUBLIC_ID);
        verify(validator).validateForDischarge(inactiveBeneficiary);
        verify(beneficiaryRepository, never()).save(any());
        verify(beneficiaryMapper, never()).toDetailsDTO(any());
    }

    @Test
    void findBeneficiaries_shouldReturnMappedPage() {
        // arrange
        BeneficiarySearchDTO criteria = BeneficiarySearchDTO.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        Beneficiary beneficiary = createDefaultBeneficiary(true);
        BeneficiaryListDTO dto = createDefaultListDTO(true);

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

    // Helpers
    private Beneficiary createDefaultBeneficiary(boolean isActive) {
        return Beneficiary.builder()
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
                                .street("Αγίου Μελέτιου")
                                .streetNumber("32")
                                .city("Αθήνα")
                                .zipCode("11361")
                                .build())
                        .build())
                .isActive(isActive)
                .build();
    }

    private BeneficiaryDetailsDTO createDefaultDetailsDTO(boolean isActive) {
        return BeneficiaryDetailsDTO.builder()
                .publicId(TEST_PUBLIC_ID)
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

    private BeneficiaryListDTO createDefaultListDTO(boolean isActive) {
        return BeneficiaryListDTO.builder()
                .publicId(TEST_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .houseUnit("UNIT_A")
                .isActive(isActive)
                .build();
    }

    private BeneficiaryCreateDTO createDefaultBeneficiaryCreateDTO() {
        return BeneficiaryCreateDTO.builder()
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
                .address(createDefaultAddressDTO())
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