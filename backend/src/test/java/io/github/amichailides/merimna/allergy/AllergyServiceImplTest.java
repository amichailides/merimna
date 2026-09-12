package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.audit.AllergyChangeDetector;
import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.allergy.event.AllergyUpdatedEvent;
import io.github.amichailides.merimna.allergy.exception.AllergyNotFoundException;
import io.github.amichailides.merimna.allergy.exception.AllergyNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.domain.Address;
import io.github.amichailides.merimna.domain.Allergy;
import io.github.amichailides.merimna.domain.AllergySeverity;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.EmergencyContact;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.domain.RelationshipType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllergyServiceImplTest {

    private static final UUID OWNER_BENEFICIARY_PUBLIC_ID = UUID.randomUUID();
    private static final UUID NON_OWNER_BENEFICIARY_PUBLIC_ID = UUID.randomUUID();
    private static final UUID ALLERGY_PUBLIC_ID = UUID.randomUUID();

    @Mock
    private AllergyRepository allergyRepository;

    @Mock
    private AllergyMapper allergyMapper;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private AllergyValidator allergyValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AllergyChangeDetector allergyChangeDetector;

    @InjectMocks
    private AllergyServiceImpl allergyService;

    @Nested
    class CreateAllergyTests {

        @Test
        void shouldPersistAndReturnDto_whenBeneficiaryExists() {
            Beneficiary beneficiary = defaultBeneficiary().build();
            AllergyCreateDTO createDTO = defaultAllergyCreateDTO().build();
            Allergy allergyToSave = defaultAllergy().build();
            Allergy savedAllergy = defaultAllergy()
                    .beneficiary(defaultBeneficiary().build())
                    .build();
            AllergyReadOnlyDTO expectedDto = defaultReadOnlyDTO().build();

            when(beneficiaryRepository.findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(Optional.of(beneficiary));
            when(allergyMapper.toEntity(createDTO)).thenReturn(allergyToSave);
            when(allergyRepository.save(any(Allergy.class))).thenReturn(savedAllergy);
            when(allergyMapper.toDTO(savedAllergy)).thenReturn(expectedDto);

            AllergyReadOnlyDTO result = allergyService.createAllergy(OWNER_BENEFICIARY_PUBLIC_ID, createDTO);

            assertNotNull(result);
            assertEquals(expectedDto, result);

            InOrder inOrder = inOrder(allergyValidator, allergyRepository);
            inOrder.verify(allergyValidator).validateCreate(eq(beneficiary), any(Allergy.class));
            inOrder.verify(allergyRepository).save(any(Allergy.class));

            verify(beneficiaryRepository).findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID);
            verify(allergyMapper).toEntity(createDTO);
            verify(allergyMapper).toDTO(savedAllergy);
        }

        @Test
        void shouldThrowException_whenBeneficiaryNotFound() {
            AllergyCreateDTO dto = defaultAllergyCreateDTO().build();

            when(beneficiaryRepository.findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(Optional.empty());

            assertThrows(
                    BeneficiaryNotFoundByPublicIdException.class,
                    () -> allergyService.createAllergy(OWNER_BENEFICIARY_PUBLIC_ID, dto)
            );

            verify(allergyMapper, never()).toEntity(any());
            verify(allergyValidator, never()).validateCreate(any(), any());
            verify(allergyRepository, never()).save(any());
        }
    }

    @Nested
    class UpdateAllergyTests {

        @Test
        void shouldUpdateAndReturnDto_whenValidInputProvided() {
            Allergy existing = defaultAllergy()
                    .beneficiary(defaultBeneficiary().build())
                    .build();
            AllergyUpdateDTO updateDto = defaultAllergyUpdateDTO().build();
            AllergyReadOnlyDTO expectedDto = defaultReadOnlyDTO()
                    .severity(AllergySeverity.HIGH)
                    .build();
            EntityChangeSet changeSet = mock(EntityChangeSet.class);

            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.of(existing));
            when(allergyChangeDetector.detectChanges(existing, updateDto)).thenReturn(changeSet);
            when(changeSet.hasChanges()).thenReturn(true);
            when(allergyMapper.toDTO(existing)).thenReturn(expectedDto);

            AllergyReadOnlyDTO result = allergyService.updateAllergy(
                    OWNER_BENEFICIARY_PUBLIC_ID,
                    ALLERGY_PUBLIC_ID,
                    updateDto
            );

            assertEquals(expectedDto, result);
            verify(allergyRepository).findByPublicId(ALLERGY_PUBLIC_ID);
            verify(allergyValidator).validateForUpdate(existing, updateDto);
            verify(allergyChangeDetector).detectChanges(existing, updateDto);
            verify(allergyMapper).updateEntity(existing, updateDto);
            verify(eventPublisher).publishEvent(any(AllergyUpdatedEvent.class));
            verify(allergyMapper).toDTO(existing);
        }

        @Test
        void shouldThrowException_whenAllergyNotFound() {
            AllergyUpdateDTO updateDto = defaultAllergyUpdateDTO().build();

            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.empty());

            assertThrows(
                    AllergyNotFoundException.class,
                    () -> allergyService.updateAllergy(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID, updateDto)
            );

            verify(allergyValidator, never()).validateForUpdate(any(), any());
            verify(allergyMapper, never()).updateEntity(any(), any());
            verify(allergyMapper, never()).toDTO(any());
        }

        @Test
        void shouldThrowException_whenAllergyNotOwnedByBeneficiary() {
            Allergy existing = defaultAllergy()
                    .beneficiary(defaultBeneficiary().publicId(NON_OWNER_BENEFICIARY_PUBLIC_ID).build())
                    .build();
            AllergyUpdateDTO updateDto = defaultAllergyUpdateDTO().build();

            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.of(existing));

            assertThrows(
                    AllergyNotOwnedByBeneficiaryException.class,
                    () -> allergyService.updateAllergy(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID, updateDto)
            );

            verify(allergyValidator, never()).validateForUpdate(any(), any());
            verify(allergyMapper, never()).updateEntity(any(), any());
            verify(allergyMapper, never()).toDTO(any());
        }
    }

    @Nested
    class DeleteAllergyTests {

        @Test
        void shouldRemoveAllergy_whenBeneficiaryAndAllergyExist() {
            Beneficiary beneficiary = defaultBeneficiary().build();
            Allergy allergy = defaultAllergy().build();
            beneficiary.addAllergy(allergy);

            when(beneficiaryRepository.findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(Optional.of(beneficiary));
            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.of(allergy));

            allergyService.deleteAllergy(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID);

            assertTrue(beneficiary.getAllergies().isEmpty());
            verify(allergyRepository, never()).delete(any());
        }

        @Test
        void shouldThrowException_whenBeneficiaryNotFound() {
            when(beneficiaryRepository.findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(Optional.empty());

            assertThrows(
                    BeneficiaryNotFoundByPublicIdException.class,
                    () -> allergyService.deleteAllergy(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID)
            );

            verify(allergyRepository, never()).findByPublicId(any());
        }

        @Test
        void shouldThrowException_whenAllergyNotFound() {
            Beneficiary beneficiary = defaultBeneficiary().build();

            when(beneficiaryRepository.findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(Optional.of(beneficiary));
            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.empty());

            assertThrows(
                    AllergyNotFoundException.class,
                    () -> allergyService.deleteAllergy(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID)
            );
        }

        @Test
        void shouldThrowException_whenAllergyNotOwnedByBeneficiary() {
            Beneficiary beneficiary = defaultBeneficiary().build();
            Allergy allergy = defaultAllergy()
                    .beneficiary(defaultBeneficiary().publicId(NON_OWNER_BENEFICIARY_PUBLIC_ID).build())
                    .build();

            when(beneficiaryRepository.findByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(Optional.of(beneficiary));
            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.of(allergy));

            assertThrows(
                    AllergyNotOwnedByBeneficiaryException.class,
                    () -> allergyService.deleteAllergy(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID)
            );
        }
    }

    @Nested
    class GetAllergyByPublicIdTests {

        @Test
        void shouldReturnDto_whenAllergyExistsAndBelongsToBeneficiary() {
            Allergy allergy = defaultAllergy()
                    .beneficiary(defaultBeneficiary().build())
                    .build();
            AllergyReadOnlyDTO expectedDto = defaultReadOnlyDTO().build();

            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.of(allergy));
            when(allergyMapper.toDTO(allergy)).thenReturn(expectedDto);

            AllergyReadOnlyDTO result = allergyService.getAllergyByPublicId(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID);

            assertEquals(expectedDto, result);
            verify(allergyRepository).findByPublicId(ALLERGY_PUBLIC_ID);
            verify(allergyMapper).toDTO(allergy);
        }

        @Test
        void shouldThrowException_whenAllergyNotFound() {
            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.empty());

            assertThrows(
                    AllergyNotFoundException.class,
                    () -> allergyService.getAllergyByPublicId(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID)
            );

            verify(allergyMapper, never()).toDTO(any());
        }

        @Test
        void shouldThrowException_whenAllergyNotOwnedByBeneficiary() {
            Allergy allergy = defaultAllergy()
                    .beneficiary(defaultBeneficiary().publicId(NON_OWNER_BENEFICIARY_PUBLIC_ID).build())
                    .build();

            when(allergyRepository.findByPublicId(ALLERGY_PUBLIC_ID)).thenReturn(Optional.of(allergy));

            assertThrows(
                    AllergyNotOwnedByBeneficiaryException.class,
                    () -> allergyService.getAllergyByPublicId(OWNER_BENEFICIARY_PUBLIC_ID, ALLERGY_PUBLIC_ID)
            );

            verify(allergyMapper, never()).toDTO(any());
        }
    }

    @Nested
    class GetAllergiesByBeneficiaryTests {

        @Test
        void shouldReturnListDto_whenBeneficiaryExists() {
            Allergy allergy = defaultAllergy()
                    .beneficiary(defaultBeneficiary().build())
                    .build();
            AllergyReadOnlyDTO dto = defaultReadOnlyDTO().build();

            when(beneficiaryRepository.existsByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(true);
            when(allergyRepository.findAllByBeneficiaryPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(List.of(allergy));
            when(allergyMapper.toDTO(allergy)).thenReturn(dto);

            List<AllergyReadOnlyDTO> result = allergyService.getAllergiesByBeneficiary(OWNER_BENEFICIARY_PUBLIC_ID);

            assertNotNull(result);
            assertEquals(List.of(dto), result);
            verify(beneficiaryRepository).existsByPublicId(OWNER_BENEFICIARY_PUBLIC_ID);
            verify(allergyRepository).findAllByBeneficiaryPublicId(OWNER_BENEFICIARY_PUBLIC_ID);
            verify(allergyMapper).toDTO(allergy);
        }

        @Test
        void shouldReturnEmptyList_whenBeneficiaryHasNoAllergies() {
            when(beneficiaryRepository.existsByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(true);
            when(allergyRepository.findAllByBeneficiaryPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(List.of());

            List<AllergyReadOnlyDTO> result = allergyService.getAllergiesByBeneficiary(OWNER_BENEFICIARY_PUBLIC_ID);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(allergyMapper, never()).toDTO(any());
        }

        @Test
        void shouldThrowException_whenBeneficiaryNotFound() {
            when(beneficiaryRepository.existsByPublicId(OWNER_BENEFICIARY_PUBLIC_ID)).thenReturn(false);

            assertThrows(
                    BeneficiaryNotFoundByPublicIdException.class,
                    () -> allergyService.getAllergiesByBeneficiary(OWNER_BENEFICIARY_PUBLIC_ID)
            );

            verify(allergyRepository, never()).findAllByBeneficiaryPublicId(any());
            verify(allergyMapper, never()).toDTO(any());
        }
    }

    private Allergy.AllergyBuilder defaultAllergy() {
        return Allergy.builder()
                .publicId(ALLERGY_PUBLIC_ID)
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.MEDIUM)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα");
    }

    private Beneficiary.BeneficiaryBuilder defaultBeneficiary() {
        return Beneficiary.builder()
                .id(1L)
                .publicId(OWNER_BENEFICIARY_PUBLIC_ID)
                .firstName("Joe")
                .lastName("Doe")
                .amka("06048678912")
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(defaultHouseUnit())
                .permanentAddress(Address.builder()
                        .street("Αγιου Μελετιου")
                        .streetNumber("32")
                        .city("Αθηνα")
                        .zipCode("11361")
                        .build())
                .emergencyContact(EmergencyContact.builder()
                        .firstName("Γιαννης")
                        .lastName("Παπαδοπουλος")
                        .relationshipType(RelationshipType.FRIEND)
                        .address(Address.builder()
                                .street("Αγιου Μελετιου")
                                .streetNumber("32")
                                .city("Αθηνα")
                                .zipCode("11361")
                                .build())
                        .build());
    }

    private AllergyCreateDTO.AllergyCreateDTOBuilder defaultAllergyCreateDTO() {
        return AllergyCreateDTO.builder()
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.MEDIUM)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα");
    }

    private AllergyUpdateDTO.AllergyUpdateDTOBuilder defaultAllergyUpdateDTO() {
        return AllergyUpdateDTO.builder()
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.HIGH)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα");
    }

    private AllergyReadOnlyDTO.AllergyReadOnlyDTOBuilder defaultReadOnlyDTO() {
        return AllergyReadOnlyDTO.builder()
                .publicId(ALLERGY_PUBLIC_ID)
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.MEDIUM)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα");
    }

    private HouseUnit defaultHouseUnit() {
        return HouseUnit.builder()
                .id(1L)
                .code("UNIT_A")
                .displayName("Στέγη Α")
                .address("Ελπίδας 10, Μαρούσι")
                .build();
    }
}