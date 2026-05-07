package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.beneficiary.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BeneficiaryService {

    BeneficiaryDetailsDTO create(BeneficiaryCreateDTO dto);

    BeneficiaryDetailsDTO findByPublicId(UUID publicId);

    BeneficiaryDetailsDTO discharge(UUID publicId, DischargeRequestDTO dto);

    public BeneficiaryDetailsDTO updateBeneficiary(UUID publicId, BeneficiaryUpdateDTO dto);

    Page<BeneficiaryListDTO> findBeneficiaries(BeneficiarySearchDTO criteria, Pageable pageable);

    BeneficiaryListDTO changeHouseUnit(UUID beneficiaryPublicId, UUID houseUnitPublicId);
}
