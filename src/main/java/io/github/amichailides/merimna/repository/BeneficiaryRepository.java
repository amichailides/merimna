package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.HouseUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepositoryImplementation<Beneficiary, Long>,
        JpaSpecificationExecutor<Beneficiary> {
    Optional<Beneficiary> findByAmka(String amka);

    boolean existsByAmka(String amka);

    List<Beneficiary> findAllByHouseUnit(HouseUnit houseUnit);

    Page<Beneficiary> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

}
