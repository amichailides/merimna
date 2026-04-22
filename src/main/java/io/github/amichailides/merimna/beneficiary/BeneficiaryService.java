package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.beneficiary.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeneficiaryService {

    boolean existsByAmka(String amka);

    BeneficiaryDetailsDTO create(BeneficiaryCreateDTO dto);

    BeneficiaryDetailsDTO findByPublicId(String publicId);

    BeneficiaryDetailsDTO discharge(String publicId);

    public BeneficiaryDetailsDTO updateBeneficiary(String publicId, BeneficiaryUpdateDTO dto);

    Page<BeneficiaryListDTO> findBeneficiaries(BeneficiarySearchDTO criteria, Pageable pageable);

    BeneficiaryListDTO changeHouseUnit(String publicId, String code);
}
