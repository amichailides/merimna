package io.github.amichailides.merimna.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlacementReason {
    TEMPORARY_COVERAGE("Temporary coverage");

    private final String displayName;
}

