package com.homni.featuretoggle.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of a project membership record.
 */
public final class ProjectMembershipId {

    public final UUID value;

    /**
     * Creates a membership identity from an existing UUID.
     *
     * @param value the UUID value
     */
    public ProjectMembershipId(UUID value) {
        this.value = Objects.requireNonNull(value, "ProjectMembershipId must not be null");
    }

    /**
     * Generates a new random membership identity.
     */
    public ProjectMembershipId() {
        this(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof ProjectMembershipId that && value.equals(that.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
