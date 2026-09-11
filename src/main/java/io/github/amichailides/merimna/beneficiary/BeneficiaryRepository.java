package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.common.projection.HouseUnitCountProjection;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>,
        JpaSpecificationExecutor<Beneficiary> {

    boolean existsByAmka(String amka);

    boolean existsByAmkaAndPublicIdNot(String amka, UUID publicId);

    @EntityGraph(attributePaths = {
            "medications",
            "allergies",
            "legalRepresentatives"
    })
    Optional<Beneficiary> findWithDetailsByPublicId(UUID publicId);

    long countByHouseUnitAndIsActiveTrue(HouseUnit houseUnit);

    Optional<Beneficiary> findByPublicId(UUID publicId);

    boolean existsByPublicId(UUID publicId);

    @Query("""
    select b.houseUnit.publicId as houseUnitPublicId,
           count(b) as count
    from Beneficiary b
    where b.isActive = true
    group by b.houseUnit.publicId
    """)
    List<HouseUnitCountProjection> countActiveBeneficiariesByHouseUnit();

    long countByIsActiveTrue();
}
