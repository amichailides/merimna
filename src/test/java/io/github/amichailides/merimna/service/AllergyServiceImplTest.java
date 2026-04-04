package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.allergy.AllergyServiceImpl;
import io.github.amichailides.merimna.domain.*;
import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.allergy.exception.AllergyNotFoundException;
import io.github.amichailides.merimna.allergy.exception.AllergyNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.allergy.AllergyMapper;
import io.github.amichailides.merimna.allergy.AllergyRepository;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllergyServiceImplTest {

    @Mock
    private AllergyRepository allergyRepository;

    @Mock
    private AllergyMapper allergyMapper;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private AllergyServiceImpl allergiesService;

    @Test
    void createAllergy_shouldPersistAndReturnDto_whenBeneficiaryExists() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Beneficiary beneficiary = createDefaultBeneficiary(beneficiaryId);

        AllergyCreateDTO createDTO = createDefaultAllergyCreateDTO();
        Allergy entityFromMapper = createDefaultAllergy(null);
        Allergy savedAllergy = createDefaultAllergy(allergyId);
        AllergyReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(allergyId);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));
        when(allergyMapper.toEntity(createDTO)).thenReturn(entityFromMapper);
        when(allergyRepository.save(any(Allergy.class))).thenReturn(savedAllergy); // any() γιατί το object αλλάζει state μετά το createAllergy()
        when(allergyMapper.toDTO(savedAllergy)).thenReturn(expectedDto);

        // act
        AllergyReadOnlyDTO result = allergiesService.createAllergy(beneficiaryId, createDTO);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(beneficiaryRepository).findById(beneficiaryId);
        verify(allergyMapper).toEntity(createDTO);
        verify(allergyRepository).save(any(Allergy.class));
        verify(allergyMapper).toDTO(savedAllergy);

    }

    @Test
    void deleteAllergy_shouldRemoveAllergy_whenBeneficiaryAndAllergyExist() {
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Beneficiary beneficiary = createDefaultBeneficiary(beneficiaryId);
        Allergy allergy = createDefaultAllergy(allergyId);
        beneficiary.addAllergy(allergy);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));
        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.of(allergy));

        // act
        allergiesService.deleteAllergy(beneficiaryId, allergyId);

        // assert
        assertTrue(beneficiary.getAllergies().isEmpty());
        verify(beneficiaryRepository).findById(beneficiaryId);
        verify(allergyRepository).findByIdAndBeneficiaryId(allergyId, beneficiaryId);

    }

    @Test
    void getAllergy_shouldReturnDto_whenBeneficiaryAndAllergyByIdExist() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Allergy allergy = createDefaultAllergy(allergyId);
        AllergyReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(allergyId);

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.of(allergy));
        when(allergyMapper.toDTO(allergy)).thenReturn(expectedDto);

        // act
        AllergyReadOnlyDTO result = allergiesService.getAllergyById(beneficiaryId, allergyId);

        // assert
        assertEquals(expectedDto, result);
        verify(allergyRepository).findByIdAndBeneficiaryId(allergyId, beneficiaryId);
        verify(allergyMapper).toDTO(allergy);

    }

    @Test
    void getAllergiesByBeneficiary_shouldReturnListDto_whenBeneficiaryExists() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Allergy allergy = createDefaultAllergy(allergyId);
        List<Allergy> allergiesList = (List.of(allergy));
        AllergyReadOnlyDTO dto = createDefaultReadOnlyDTO(allergyId);
        List<AllergyReadOnlyDTO> expectedDto = List.of(dto);

        when(beneficiaryRepository.existsById(beneficiaryId)).thenReturn(true);
        when(allergyRepository.findAllByBeneficiaryId(beneficiaryId)).thenReturn(allergiesList);
        when(allergyMapper.toDTO(allergy)).thenReturn(dto);

        // act
        List<AllergyReadOnlyDTO> result = allergiesService.getAllergiesByBeneficiary(beneficiaryId);

        // assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(beneficiaryRepository).existsById(beneficiaryId);
        verify(allergyRepository).findAllByBeneficiaryId(beneficiaryId);
        verify(allergyMapper).toDTO(allergy);
    }

    @Test
    void createAllergy_shouldThrowException_WhenBeneficiaryMissing() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;
        AllergyCreateDTO dto = createDefaultAllergyCreateDTO();

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.empty());
        // act & assert
        assertThrows(
                BeneficiaryNotFoundByIdException.class,
                () -> allergiesService.createAllergy(beneficiaryId, dto)
        );

        verify(allergyRepository, never()).save(any());
    }

    @Test
    void deleteAllergy_shouldThrowException_whenBeneficiaryMissing() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                BeneficiaryNotFoundByIdException.class,
                () -> allergiesService.deleteAllergy(beneficiaryId, allergyId)
        );

        verify(allergyRepository, never()).findByIdAndBeneficiaryId(any(), any());

    }

    @Test
    void deleteAllergy_shouldThrowException_whenAllergyMissing() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Beneficiary beneficiary = createDefaultBeneficiary(beneficiaryId);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));
        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.empty());
        when(allergyRepository.existsById(allergyId)).thenReturn(false);

        // act & assert
        assertThrows(
                AllergyNotFoundException.class,
                () -> allergiesService.deleteAllergy(beneficiaryId, allergyId)
        );

    }

    @Test
    void deleteAllergy_shouldThrowException_whenAllergyNotOwnedByBeneficiary() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Beneficiary beneficiary = createDefaultBeneficiary(beneficiaryId);

        when(beneficiaryRepository.findById(beneficiaryId)).thenReturn(Optional.of(beneficiary));
        when(allergyRepository.existsById(allergyId)).thenReturn(true);
        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(
                AllergyNotOwnedByBeneficiaryException.class,
                () -> allergiesService.deleteAllergy(beneficiaryId, allergyId)
        );

    }

    @Test
    void updateAllergy_shouldPersistAndReturnDto_whenValidInputProvided() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Allergy existed = createDefaultAllergy(allergyId);
        AllergyUpdateDTO updateDto = createDefaultUpdateDTO();
        AllergyReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(allergyId);

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.of(existed));
        when(allergyMapper.toDTO(existed)).thenReturn(expectedDto);

        // Act
        AllergyReadOnlyDTO result = allergiesService.updateAllergy(beneficiaryId, allergyId, updateDto);

        // assert
        assertEquals(expectedDto, result);
        verify(allergyRepository).findByIdAndBeneficiaryId(allergyId, beneficiaryId);
        verify(allergyMapper).updateEntity(existed, updateDto);
        verify(allergyMapper).toDTO(existed);

    }

    @Test
    void updateAllergy_shouldThrowException_whenAllergyNotFound() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;
        AllergyUpdateDTO updateDto = createDefaultUpdateDTO();

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.empty());
        when(allergyRepository.existsById(allergyId)).thenReturn(false);

        // act & assert
        assertThrows(
                AllergyNotFoundException.class,
                () -> allergiesService.updateAllergy(beneficiaryId, allergyId, updateDto)
        );

        verify(allergyRepository).existsById(allergyId);
        verify(allergyRepository).findByIdAndBeneficiaryId(allergyId, beneficiaryId);
        verify(allergyMapper, never()).updateEntity(any(), any());
        verify(allergyMapper, never()).toDTO(any());
    }

    @Test
    void updateAllergy_shouldThrowException_whenAllergyNotOwnedByBeneficiary() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;
        AllergyUpdateDTO updateDto = createDefaultUpdateDTO();

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.empty());
        when(allergyRepository.existsById(allergyId)).thenReturn(true);

        // act & assert
        assertThrows(
                AllergyNotOwnedByBeneficiaryException.class,
                () -> allergiesService.updateAllergy(beneficiaryId, allergyId, updateDto)
        );

        verify(allergyRepository).findByIdAndBeneficiaryId(allergyId, beneficiaryId);
        verify(allergyRepository).existsById(allergyId);
        verify(allergyMapper, never()).updateEntity(any(), any());
        verify(allergyMapper, never()).toDTO(any());
    }

    @Test
    void getAllergiesByBeneficiary_shouldThrowException_whenBeneficiaryMissing() {
        // arrange
        Long beneficiaryId = 1L;

        when(beneficiaryRepository.existsById(beneficiaryId)).thenReturn(false);

        // act & assert
        assertThrows(BeneficiaryNotFoundByIdException.class,
                () -> allergiesService.getAllergiesByBeneficiary(beneficiaryId));

        verify(allergyRepository, never()).findAllByBeneficiaryId(beneficiaryId);
        verify(allergyMapper, never()).toDTO(any());
    }

    @Test
    void getAllergy_shouldThrowException_whenAllergyByIdNotFound() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.empty());
        when(allergyRepository.existsById(allergyId)).thenReturn(false);

        // act & assert
        assertThrows(
                AllergyNotFoundException.class,
                () -> allergiesService.getAllergyById(beneficiaryId, allergyId)
        );

        verify(allergyMapper, never()).toDTO(any());
    }

    @Test
    void getAllergy_shouldThrowException_whenAllergyByIdNotOwnedByBeneficiary() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.empty());
        when(allergyRepository.existsById(allergyId)).thenReturn(true);

        // act & assert
        assertThrows(
                AllergyNotOwnedByBeneficiaryException.class,
                () -> allergiesService.getAllergyById(beneficiaryId, allergyId)
        );

        verify(allergyMapper, never()).toDTO(any());
    }




    private Beneficiary createDefaultBeneficiary(Long id) {
        return Beneficiary.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .amka(("06048678912"))
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(createDefaultHouseUnit())
                .permanentAddress( Address.builder()
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
                                .street("Αγου Μελετιου")
                                .streetNumber("32")
                                .city("Αθηνα")
                                .zipCode("11361")
                                .build())
                        .build())
                .build();
    }

    private Allergy createDefaultAllergy(Long allergyId) {
        return Allergy.builder()
                .id(allergyId)
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.MEDIUM)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα")
                .build();
    }

    private AllergyCreateDTO createDefaultAllergyCreateDTO() {
        return AllergyCreateDTO.builder()
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.MEDIUM)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα")
                .build();
    }

    private AllergyUpdateDTO createDefaultUpdateDTO() {
        return AllergyUpdateDTO.builder()
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.HIGH)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα")
                .build();
    }

    private AllergyReadOnlyDTO createDefaultReadOnlyDTO (Long allergyId) {
        return AllergyReadOnlyDTO.builder()
                .id(allergyId)
                .substance("Γύρη / Pollen")
                .severity(AllergySeverity.MEDIUM)
                .reaction("Δυσκολία στην αναπνοή και φτέρνισμα")
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
