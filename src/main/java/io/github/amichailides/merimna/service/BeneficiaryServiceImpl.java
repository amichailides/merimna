package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.common.ErrorCode;
import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByAmkaException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.exception.BeneficiaryValidationException;
import io.github.amichailides.merimna.mapper.BeneficiaryMapper;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import io.github.amichailides.merimna.service.validation.BeneficiaryValidator;
import io.github.amichailides.merimna.specification.BeneficiarySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * Υλοποίηση με business validation μέσω του {@link BeneficiaryValidator}
 * και βελτιστοποιημένα {@code readOnly = true} transactions.
 */
@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository repository;
    private final BeneficiaryMapper mapper;
    private final BeneficiaryValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> findAllBeneficiaries(
            boolean includeInactive,
            HouseUnit houseUnit,
            Pageable pageable) {

        Page<Beneficiary> beneficiariesPage;

        if (houseUnit != null) {
            beneficiariesPage = includeInactive
                    ? repository.findAllByHouseUnit(houseUnit, pageable)
                    : repository.findAllByHouseUnitAndIsActiveTrue(houseUnit, pageable);
        } else {
            beneficiariesPage = includeInactive
                    ? repository.findAll(pageable)
                    : repository.findAllByIsActiveTrue(pageable);
        }


        return beneficiariesPage.map(mapper::toReadOnlyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryReadOnlyDTO findByAmka(String amka) {
        return repository.findByAmka(amka)
                .map(mapper::toReadOnlyDTO)
                .orElseThrow(() -> new BeneficiaryNotFoundByAmkaException(amka));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAmka(String amka) {
        return repository.existsByAmka(amka);
    }

    @Transactional(readOnly = true)
    public BeneficiaryReadOnlyDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toReadOnlyDTO)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));
    }

    @Transactional
    public BeneficiaryReadOnlyDTO save(BeneficiarySaveDTO dto) {

        validator.validateForSave(dto);

        Beneficiary beneficiary = mapper.toEntity(dto);
        Beneficiary savedBeneficiary = repository.save(beneficiary);
        return mapper.toReadOnlyDTO(savedBeneficiary);
    }

    @Transactional
    public BeneficiaryReadOnlyDTO deactivate(Long id) {

        Beneficiary beneficiary = repository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));

        validator.validateForDeactivate(beneficiary);

        beneficiary.setIsActive(false);

        return mapper.toReadOnlyDTO(repository.save(beneficiary));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> search(String term, Pageable pageable) {
        //  Παίρνουμε το specification που φτιάξαμε
        Specification<Beneficiary> spec = BeneficiarySpecifications.globalSearch(term);

        // 2. Ζητάμε από το repository να κάνει το search με pagination
        // 3. Κάνουμε map κάθε Entity που βρέθηκε σε ReadOnlyDTO
        return repository.findAll(spec, pageable)
                .map(mapper::toReadOnlyDTO);
    }
}