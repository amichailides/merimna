package io.github.amichailides.merimna.domain;

// TODO(#11): Replace enum with entity for dynamic roles (multi-organization support)
public enum RelationshipType {
    PARENT,
    SIBLING,
    OTHER_RELATIVE,
    FRIEND,
    SOCIAL_WORKER,
    OTHER
}
