package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.mapper.AllergyMapper;
import io.github.amichailides.merimna.model.*;
import io.github.amichailides.merimna.repository.AllergyRepository;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllergiesServiceImplTest {

    @Mock
    private AllergyRepository allergyRepository;

    @Mock
    private AllergyMapper allergyMapper;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private AllergiesServiceImpl allergiesService;

    @Test
    void addAllergy_shouldPersistAndReturnDto_whenBeneficiaryExists() {
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
        when(allergyRepository.save(any(Allergy.class))).thenReturn(savedAllergy); // any() γιατί το object αλλάζει state μετά το addAllergy()
        when(allergyMapper.toDTO(savedAllergy)).thenReturn(expectedDto);

        // act
        AllergyReadOnlyDTO result = allergiesService.addAllergy(beneficiaryId, createDTO);

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
    void getAllergy_shouldReturnDto_whenBeneficiaryAndAllergyExist() {
        // arrange
        Long beneficiaryId = 1L;
        Long allergyId = 1L;

        Allergy allergy = createDefaultAllergy(allergyId);
        AllergyReadOnlyDTO expectedDto = createDefaultReadOnlyDTO(allergyId);

        when(allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)).thenReturn(Optional.of(allergy));
        when(allergyMapper.toDTO(allergy)).thenReturn(expectedDto);

        // act
        AllergyReadOnlyDTO result = allergiesService.getAllergy(beneficiaryId, allergyId);

        // assert
        assertEquals(expectedDto, result);
        verify(allergyRepository).findByIdAndBeneficiaryId(allergyId, beneficiaryId);
        verify(allergyMapper).toDTO(allergy);

    }

    private Beneficiary createDefaultBeneficiary(Long id) {
        return Beneficiary.builder()
                .id(id)
                .firstName("Joe")
                .lastName("Doe")
                .amka(("06048678912"))
                .dateOfBirth(LocalDate.of(1986, 4, 6))
                .houseUnit(HouseUnit.UNIT_A)
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
}
