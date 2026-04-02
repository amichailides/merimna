package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseUnitRepository extends JpaRepository<HouseUnit, Long> {
    Optional<HouseUnit> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long houseUnitId);
}