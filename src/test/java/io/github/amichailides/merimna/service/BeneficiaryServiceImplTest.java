package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.common.ErrorCode;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByAmkaException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.exception.BeneficiaryValidationException;
import io.github.amichailides.merimna.mapper.BeneficiaryMapper;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import io.github.amichailides.merimna.service.validation.BeneficiaryValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeneficiaryServiceImplTest {
    @Mock
    private BeneficiaryRepository  beneficiaryRepository;

    @Mock
    private BeneficiaryMapper beneficiaryMapper;

    @Mock
    private BeneficiaryValidator validator;

    @Mock
    private AllergiesService allergiesService;

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    @Test
    @DisplayName("Should throw exception when beneficiary with given id does not exist")
    void findById_shouldThrowException_whenBeneficiaryMissing() {
        Long beneficiaryId = 1L;
        when(beneficiaryRepository.findById(beneficiaryId))
                .thenReturn(Optional.empty());

        assertThrows(
                BeneficiaryNotFoundByIdException.class,
                () -> beneficiaryService.findById(beneficiaryId)
        );
        verify(beneficiaryRepository).findById(beneficiaryId);

    }

    @Test
    @DisplayName("Should throw exception when beneficiary with given AMKA does not exist")
    void findByAmka_shouldThrowException_whenBeneficiaryMissing() {
        String amka = "12345678912";

        when(beneficiaryRepository.findByAmka(amka))
                .thenReturn(Optional.empty());

        assertThrows(
                BeneficiaryNotFoundByAmkaException.class,
                () -> beneficiaryService.findByAmka(amka)
        );
    }

    @Test
    @DisplayName("Should not save beneficiary when validation fails")
    void save_shouldNotPersist_whenValidationFails() {
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

        doThrow(new BeneficiaryValidationException(errors))
                .when(validator)
                .validateForSave(dto);

        assertThrows(
                BeneficiaryValidationException.class,
                () -> beneficiaryService.save(dto)
        );

        verify(beneficiaryRepository, never()).save(any());

    }
}
