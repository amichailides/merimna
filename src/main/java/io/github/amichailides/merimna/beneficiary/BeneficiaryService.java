package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.beneficiary.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeneficiaryService {

    boolean existsByAmka(String amka);

    BeneficiaryDetailsDTO save(BeneficiarySaveDTO dto);

    BeneficiaryDetailsDTO findById(Long id);

    BeneficiaryDetailsDTO discharge(Long id);

    public BeneficiaryDetailsDTO updateBeneficiary(Long id, BeneficiaryUpdateDTO dto);

    Page<BeneficiaryListDTO> findBeneficiaries(BeneficiarySearchDTO criteria, Pageable pageable);

    BeneficiaryListDTO changeHouseUnit(Long beneficiaryId, String code);
}
