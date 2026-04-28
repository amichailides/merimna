package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryAlreadyInactiveException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitValidator;
import org.junit.jupiter.api.Nested;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryServiceImplTest {

    private static final UUID BENEFICIARY_PUBLIC_ID = UUID.randomUUID();
    private static final UUID HOUSE_UNIT_PUBLIC_ID = UUID.randomUUID();

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

    @Mock
    private BeneficiaryAccessService beneficiaryAccessService;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    @Nested
    class FindByPublicIdTests {

        @Test
        void shouldThrowException_whenBeneficiaryMissing() {
            when(beneficiaryRepository.findWithDetailsByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    BeneficiaryNotFoundByPublicIdException.class,
                    () -> beneficiaryService.findByPublicId(BENEFICIARY_PUBLIC_ID)
            );

            verify(beneficiaryRepository).findWithDetailsByPublicId(BENEFICIARY_PUBLIC_ID);
            verifyNoInteractions(beneficiaryMapper);
        }

        @Test
        void shouldReturnDetailsDto_whenBeneficiaryExists() {
            Beneficiary beneficiary = defaultBeneficiary().build();
            BeneficiaryDetailsDTO expectedDto = defaultDetailsDTO().build();

            when(beneficiaryRepository.findWithDetailsByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(beneficiary));
            when(beneficiaryMapper.toDetailsDTO(beneficiary))
                    .thenReturn(expectedDto);

            BeneficiaryDetailsDTO result = beneficiaryService.findByPublicId(BENEFICIARY_PUBLIC_ID);

            assertEquals(expectedDto, result);
            verify(beneficiaryRepository).findWithDetailsByPublicId(BENEFICIARY_PUBLIC_ID);
            verify(beneficiaryAccessService).checkCanAccess(beneficiary);
            verify(beneficiaryMapper).toDetailsDTO(beneficiary);
        }
    }

    @Nested
    class CreateTests {

        @Test
        void shouldNotMapOrPersist_whenValidationFails() {
            BeneficiaryCreateDTO dto = defaultCreateDTO().build();

            doThrow(new DomainValidationException(Map.of(
                    "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
            )))
                    .when(validator)
                    .validateForSave(dto);

            assertThrows(
                    DomainValidationException.class,
                    () -> beneficiaryService.create(dto)
            );

            verify(validator).validateForSave(dto);
            verifyNoInteractions(beneficiaryMapper);
            verify(beneficiaryRepository, never()).save(any());
        }

        @Test
        void shouldPersistAndReturnDto_whenValidInputProvided() {
            BeneficiaryCreateDTO createDto = defaultCreateDTO().build();
            HouseUnit houseUnit = defaultHouseUnit().build();
            Beneficiary entityFromMapper = defaultBeneficiary()
                    .isActive(false)
                    .build();
            Beneficiary savedEntity = defaultBeneficiary().build();
            BeneficiaryDetailsDTO expectedDto = defaultDetailsDTO().build();

            when(houseUnitRepository.findByPublicId(HOUSE_UNIT_PUBLIC_ID))
                    .thenReturn(Optional.of(houseUnit));
            when(beneficiaryMapper.toEntity(createDto, houseUnit))
                    .thenReturn(entityFromMapper);
            when(beneficiaryRepository.save(entityFromMapper))
                    .thenReturn(savedEntity);
            when(beneficiaryMapper.toDetailsDTO(savedEntity))
                    .thenReturn(expectedDto);

            BeneficiaryDetailsDTO result = beneficiaryService.create(createDto);

            assertEquals(expectedDto, result);
            verify(validator).validateForSave(createDto);
            verify(houseUnitRepository).findByPublicId(HOUSE_UNIT_PUBLIC_ID);
            verify(beneficiaryAccessService).checkCanAccess(houseUnit);
            verify(houseUnitValidator).validateAssignmentForBeneficiary(houseUnit);
            verify(beneficiaryMapper).toEntity(createDto, houseUnit);
            verify(beneficiaryRepository).save(entityFromMapper);
            verify(beneficiaryMapper).toDetailsDTO(savedEntity);
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void shouldFailFast_whenBeneficiaryNotFound() {
            BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder().build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    BeneficiaryNotFoundByPublicIdException.class,
                    () -> beneficiaryService.updateBeneficiary(BENEFICIARY_PUBLIC_ID, updateDto)
            );

            verify(beneficiaryRepository).findByPublicId(BENEFICIARY_PUBLIC_ID);
            verify(beneficiaryRepository, never()).save(any());
            verifyNoInteractions(validator);
            verifyNoInteractions(beneficiaryMapper);
        }

        @Test
        void shouldUpdateAndReturnDto_whenValidInputProvided() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                    .firstName("UpdatedName")
                    .build();
            BeneficiaryDetailsDTO expectedDto = defaultDetailsDTO().build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(existing));
            when(beneficiaryMapper.toDetailsDTO(existing))
                    .thenReturn(expectedDto);

            BeneficiaryDetailsDTO result = beneficiaryService.updateBeneficiary(BENEFICIARY_PUBLIC_ID, updateDto);

            assertEquals(expectedDto, result);
            verify(beneficiaryAccessService).checkCanAccess(existing);
            verify(validator).validateForUpdate(existing, updateDto);
            verify(beneficiaryMapper).updateEntity(existing, updateDto);
            verify(beneficiaryMapper).toDetailsDTO(existing);
            verify(beneficiaryRepository, never()).save(any());
        }

        @Test
        void shouldNotMapOrPersist_whenValidationFails() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryUpdateDTO updateDto = BeneficiaryUpdateDTO.builder()
                    .amka("06047678912")
                    .build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(existing));
            doThrow(new DomainValidationException(Map.of(
                    "amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey()
            )))
                    .when(validator)
                    .validateForUpdate(existing, updateDto);

            assertThrows(
                    DomainValidationException.class,
                    () -> beneficiaryService.updateBeneficiary(BENEFICIARY_PUBLIC_ID, updateDto)
            );

            verify(beneficiaryRepository).findByPublicId(BENEFICIARY_PUBLIC_ID);
            verify(validator).validateForUpdate(existing, updateDto);
            verify(beneficiaryMapper, never()).updateEntity(any(), any());
            verify(beneficiaryMapper, never()).toDetailsDTO(any());
            verify(beneficiaryRepository, never()).save(any());
        }
    }

    @Nested
    class DischargeTests {

        @Test
        void shouldThrowNotFound_whenPublicIdMissing() {
            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.empty());

            assertThrows(
                    BeneficiaryNotFoundByPublicIdException.class,
                    () -> beneficiaryService.discharge(BENEFICIARY_PUBLIC_ID)
            );

            verify(beneficiaryRepository).findByPublicId(BENEFICIARY_PUBLIC_ID);
            verifyNoInteractions(validator);
            verifyNoInteractions(beneficiaryMapper);
            verify(beneficiaryRepository, never()).save(any());
        }

        @Test
        void shouldSetInactiveAndReturnDto_whenBeneficiaryIsActive() {
            Beneficiary existing = defaultBeneficiary().build();
            BeneficiaryDetailsDTO expectedDto = defaultDetailsDTO()
                    .isActive(false)
                    .build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(existing));
            when(beneficiaryRepository.save(existing))
                    .thenReturn(existing);
            when(beneficiaryMapper.toDetailsDTO(existing))
                    .thenReturn(expectedDto);

            BeneficiaryDetailsDTO result = beneficiaryService.discharge(BENEFICIARY_PUBLIC_ID);

            assertFalse(existing.isActive(), "Beneficiary should be inactive after discharge");
            assertEquals(expectedDto, result);

            verify(beneficiaryRepository).findByPublicId(BENEFICIARY_PUBLIC_ID);
            verify(beneficiaryAccessService).checkCanAccess(existing);
            verify(validator).validateForDischarge(existing);
            verify(beneficiaryRepository).save(existing);
            verify(beneficiaryMapper).toDetailsDTO(existing);
        }

        @Test
        void shouldThrowExceptionAndNotPersist_whenAlreadyInactive() {
            Beneficiary inactiveBeneficiary = defaultBeneficiary()
                    .isActive(false)
                    .build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(inactiveBeneficiary));

            assertThrows(
                    BeneficiaryAlreadyInactiveException.class,
                    () -> beneficiaryService.discharge(BENEFICIARY_PUBLIC_ID)
            );

            verify(beneficiaryRepository).findByPublicId(BENEFICIARY_PUBLIC_ID);
            verify(validator).validateForDischarge(inactiveBeneficiary);
            verify(beneficiaryRepository, never()).save(any());
            verify(beneficiaryMapper, never()).toDetailsDTO(any());
        }
    }

    @Nested
    class FindBeneficiariesTests {

        // TODO: Move search filter behavior coverage to specification/repository integration tests.
        // Remaining scenarios: q only, amka only, houseUnit filter, blank criteria handling.

        @Test
        void shouldReturnMappedPage() {
            BeneficiarySearchDTO criteria = BeneficiarySearchDTO.builder().build();
            Pageable pageable = PageRequest.of(0, 10);

            Beneficiary beneficiary = defaultBeneficiary().build();
            BeneficiaryListDTO dto = defaultListDTO().build();
            Page<Beneficiary> entityPage = new PageImpl<>(List.of(beneficiary));

            when(beneficiaryRepository.findAll(
                    ArgumentMatchers.<Specification<Beneficiary>>any(),
                    eq(pageable)))
                    .thenReturn(entityPage);
            when(beneficiaryMapper.toListDTO(beneficiary))
                    .thenReturn(dto);

            Page<BeneficiaryListDTO> result = beneficiaryService.findBeneficiaries(criteria, pageable);

            assertEquals(1, result.getContent().size());
            assertSame(dto, result.getContent().getFirst());
            verify(beneficiaryRepository).findAll(
                    ArgumentMatchers.<Specification<Beneficiary>>any(),
                    eq(pageable)
            );
            verify(beneficiaryMapper).toListDTO(beneficiary);
        }
    }

    @Nested
    class ChangeHouseUnitTests {

        @Test
        void shouldChangeHouseUnit_whenDifferentHouseUnitProvided() {
            UUID newHouseUnitPublicId = UUID.randomUUID();

            HouseUnit newHouseUnit = defaultHouseUnit()
                    .id(2L)
                    .publicId(newHouseUnitPublicId)
                    .code("UNIT_B")
                    .displayName("Στέγη Β")
                    .address("Κάποια 5, Αθήνα")
                    .build();

            Beneficiary existing = defaultBeneficiary().build();

            BeneficiaryListDTO expectedDto = defaultListDTO()
                    .houseUnit("UNIT_B")
                    .build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(existing));
            when(houseUnitRepository.findByPublicId(newHouseUnitPublicId))
                    .thenReturn(Optional.of(newHouseUnit));
            when(beneficiaryMapper.toListDTO(existing))
                    .thenReturn(expectedDto);

            BeneficiaryListDTO result =
                    beneficiaryService.changeHouseUnit(BENEFICIARY_PUBLIC_ID, newHouseUnitPublicId);

            assertEquals(expectedDto, result);
            assertSame(newHouseUnit, existing.getHouseUnit());

            verify(beneficiaryAccessService).checkCanAccess(existing);
            verify(beneficiaryAccessService).checkCanAccess(newHouseUnit);
            verify(houseUnitValidator).validateAssignmentForBeneficiary(newHouseUnit);
            verify(beneficiaryMapper).toListDTO(existing);
        }

        @Test
        void shouldSkipHouseUnitChange_whenSameHouseUnitProvided() {
            HouseUnit sameHouseUnit = defaultHouseUnit().build();

            Beneficiary existing = defaultBeneficiary().build();
            HouseUnit originalHouseUnit = existing.getHouseUnit();

            BeneficiaryListDTO expectedDto = defaultListDTO().build();

            when(beneficiaryRepository.findByPublicId(BENEFICIARY_PUBLIC_ID))
                    .thenReturn(Optional.of(existing));
            when(houseUnitRepository.findByPublicId(HOUSE_UNIT_PUBLIC_ID))
                    .thenReturn(Optional.of(sameHouseUnit));
            when(beneficiaryMapper.toListDTO(existing))
                    .thenReturn(expectedDto);

            BeneficiaryListDTO result =
                    beneficiaryService.changeHouseUnit(BENEFICIARY_PUBLIC_ID, HOUSE_UNIT_PUBLIC_ID);

            assertEquals(expectedDto, result);
            assertSame(originalHouseUnit, existing.getHouseUnit());

            verify(beneficiaryAccessService).checkCanAccess(existing);
            verify(beneficiaryAccessService).checkCanAccess(sameHouseUnit);
            verifyNoInteractions(houseUnitValidator);
            verify(beneficiaryMapper).toListDTO(existing);
        }
    }

    private Beneficiary.BeneficiaryBuilder defaultBeneficiary() {
        return Beneficiary.builder()
                .publicId(BENEFICIARY_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(defaultHouseUnit().build())
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
                .isActive(true);
    }

    private BeneficiaryDetailsDTO.BeneficiaryDetailsDTOBuilder defaultDetailsDTO() {
        return BeneficiaryDetailsDTO.builder()
                .publicId(BENEFICIARY_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnitCode("UNIT_A")
                .houseUnitDisplayName("Στέγη Α")
                .isActive(true)
                .permanentAddress(defaultAddressDTO().build())
                .emergencyContact(defaultEmergencyContactDTO().build());
    }

    private BeneficiaryListDTO.BeneficiaryListDTOBuilder defaultListDTO() {
        return BeneficiaryListDTO.builder()
                .publicId(BENEFICIARY_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .houseUnit("UNIT_A")
                .isActive(true);
    }

    private BeneficiaryCreateDTO.BeneficiaryCreateDTOBuilder defaultCreateDTO() {
        return BeneficiaryCreateDTO.builder()
                .firstName("Joe")
                .lastName("Doe")
                .amka("12345678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnitPublicId(HOUSE_UNIT_PUBLIC_ID)
                .permanentAddress(defaultAddressDTO().build())
                .emergencyContact(defaultEmergencyContactDTO().build());
    }

    private AddressDTO.AddressDTOBuilder defaultAddressDTO() {
        return AddressDTO.builder()
                .street("Αγίου Μελετίου")
                .streetNumber("32")
                .city("Αθήνα")
                .zipCode("11361");
    }

    private EmergencyContactDTO.EmergencyContactDTOBuilder defaultEmergencyContactDTO() {
        return EmergencyContactDTO.builder()
                .firstName("Γιάννης")
                .lastName("Παπαδόπουλος")
                .relationshipType(RelationshipType.FRIEND)
                .address(defaultAddressDTO().build());
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