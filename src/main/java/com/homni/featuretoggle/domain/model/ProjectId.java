package com.homni.featuretoggle.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of a project.
 */
public final class ProjectId {

    public final UUID value;

    /**
     * Creates a project identity from an existing UUID.
     *
     * @param value the UUID value
     */
    public ProjectId(UUID value) {
        this.value = Objects.requireNonNull(value, "ProjectId must not be null");
    }

    /**
     * Generates a new random project identity.
     */
    public ProjectId() {
        this(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ProjectId that && value.equals(that.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
