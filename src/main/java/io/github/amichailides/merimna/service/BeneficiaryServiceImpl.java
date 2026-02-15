package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByAmkaException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.BeneficiaryMapper;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * Service layer για Beneficiary operations.
 *
 * Performance Notes:
 * - Όλα τα read methods χρησιμοποιούν @Transactional(readOnly = true)
 *   για optimization (skip dirty checking, reduce memory usage)
 */

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService{

    private final BeneficiaryRepository repository;
    private final BeneficiaryMapper mapper;
    private final MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> findAllBeneficiaries(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toReadOnlyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeneficiaryReadOnlyDTO> findBeneficiaryById(Long id) {
        return repository.findById(id)
                .map(mapper::toReadOnlyDTO);
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

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryReadOnlyDTO> findAllByHouseUnit(HouseUnit houseUnit) {
        return repository.findAllByHouseUnit(houseUnit).stream()
                .map(mapper::toReadOnlyDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable) {
        return repository.findByLastNameContainingIgnoreCase(lastName, pageable)
                .map(mapper::toReadOnlyDTO);
    }

    @Transactional(readOnly = true)
    public BeneficiaryReadOnlyDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toReadOnlyDTO)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));
    }

    @Transactional
    public BeneficiaryReadOnlyDTO save(BeneficiarySaveDTO dto) {
        Beneficiary beneficiary = mapper.toEntity(dto);
        Beneficiary savedBeneficiary =  repository.save(beneficiary);
        return mapper.toReadOnlyDTO(savedBeneficiary);
    }

}
