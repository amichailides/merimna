package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepositoryImplementation<Beneficiary, Long>,
        JpaSpecificationExecutor<Beneficiary> {

    Page<Beneficiary> findAllByIsActiveTrue(Pageable pageable);

    Optional<Beneficiary> findByAmka(String amka);

    boolean existsByAmka(String amka);

    Page<Beneficiary> findAllByHouseUnit(HouseUnit houseUnit, Pageable pageable);

    Page<Beneficiary> findAllByHouseUnitAndIsActiveTrue(HouseUnit houseUnit, Pageable pageable);

    boolean existsByAmkaAndIdNot(String amka, Long id);



}
