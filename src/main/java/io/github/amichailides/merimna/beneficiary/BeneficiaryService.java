package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiarySearchDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
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
