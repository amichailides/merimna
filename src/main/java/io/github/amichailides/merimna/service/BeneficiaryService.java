package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.*;
import io.github.amichailides.merimna.model.HouseUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeneficiaryService {

    boolean existsByAmka(String amka);

    BeneficiaryReadOnlyDTO save(BeneficiarySaveDTO dto);

    BeneficiaryReadOnlyDTO findById(Long id);

    BeneficiaryReadOnlyDTO discharge(Long id);

    public BeneficiaryReadOnlyDTO updateBeneficiary(Long id, BeneficiaryUpdateDTO dto);

    Page<BeneficiaryReadOnlyDTO> findBeneficiaries(BeneficiarySearchDTO criteria, Pageable pageable);

}
