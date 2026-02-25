package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.model.HouseUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryService {
    Page<BeneficiaryReadOnlyDTO> findAllBeneficiaries(boolean includeInactive, HouseUnit houseUnit, Pageable pageable);
    BeneficiaryReadOnlyDTO findByAmka(String amka);
    boolean existsByAmka(String amka);
    BeneficiaryReadOnlyDTO save(BeneficiarySaveDTO dto);
    BeneficiaryReadOnlyDTO findById(Long id);
    BeneficiaryReadOnlyDTO deactivate(Long id);
    Page<BeneficiaryReadOnlyDTO> search(String term, Pageable pageable);
}
