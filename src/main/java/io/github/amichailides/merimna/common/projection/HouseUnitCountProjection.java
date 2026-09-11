package io.github.amichailides.merimna.common.projection;

import java.util.UUID;

public interface HouseUnitCountProjection {

    UUID getHouseUnitPublicId();

    long getCount();
}
