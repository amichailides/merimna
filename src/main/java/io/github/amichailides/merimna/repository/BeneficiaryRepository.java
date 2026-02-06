package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.HouseUnit;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepositoryImplementation<Beneficiary, Long> {
    Optional<Beneficiary> findByAmka(String amka);

    boolean existsByAmka(String amka);

    List<Beneficiary> findAllByHouseUnit(HouseUnit houseUnit);

    List<Beneficiary> findByLastNameContainingIgnoreCase(String lastName);

}
