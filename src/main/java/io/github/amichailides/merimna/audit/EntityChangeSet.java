package io.github.amichailides.merimna.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record EntityChangeSet(
        List<FieldChange> changes
) {
    public EntityChangeSet {
        changes = changes == null ? List.of() : List.copyOf(changes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean hasChanges() {
        return !changes.isEmpty();
    }

    public List<String> changedFieldNames() {
        return changes.stream()
                .map(FieldChange::fieldName)
                .toList();
    }

    public static class Builder {
        private final List<FieldChange> changes = new ArrayList<>();

        public <T> Builder track(String fieldName, T oldValue, T newValue) {
            if (!Objects.equals(oldValue, newValue)) {
                changes.add(new FieldChange(fieldName, oldValue, newValue));
            }
            return this;
        }

        public <T> Builder trackIfPresent(String fieldName, T oldValue, T newValue) {
            if (newValue != null) {
                track(fieldName, oldValue, newValue);
            }
            return this;
        }

        public EntityChangeSet build() {
            return new EntityChangeSet(changes);
        }
    }
}