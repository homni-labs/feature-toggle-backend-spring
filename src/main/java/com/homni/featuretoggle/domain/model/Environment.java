package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.DomainValidationException;

import java.time.Instant;
import java.util.Objects;

/**
 * Deployment environment scoped to a project.
 * Name is unique within the project and normalized to uppercase.
 */
public final class Environment {

    public final EnvironmentId id;
    public final ProjectId projectId;
    public final Instant createdAt;

    private String name;

    /**
     * Creates a new environment within a project.
     *
     * @param projectId the owning project
     * @param name      the environment name (1-50 non-blank characters)
     * @throws DomainValidationException if the name is invalid
     */
    public Environment(ProjectId projectId, String name) {
        this.id = new EnvironmentId();
        this.projectId = Objects.requireNonNull(projectId);
        this.name = validateAndNormalize(name);
        this.createdAt = Instant.now();
    }

    /**
     * Restores an environment from persistent storage.
     *
     * @param id        the environment identity
     * @param projectId the owning project
     * @param name      the environment name
     * @param createdAt the creation timestamp
     */
    public Environment(EnvironmentId id, ProjectId projectId, String name, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.projectId = Objects.requireNonNull(projectId);
        this.name = validateAndNormalize(name);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    /**
     * Returns the environment name.
     *
     * @return the uppercase environment name
     */
    public String name() {
        return this.name;
    }

    private String validateAndNormalize(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Invalid environment name '%s': must not be blank".formatted(name));
        }
        String normalized = name.trim().toUpperCase();
        if (normalized.length() > 50) {
            throw new DomainValidationException("Invalid environment name '%s': must not exceed 50 characters".formatted(name));
        }
        if (!normalized.matches("^[A-Z][A-Z0-9_]*$")) {
            throw new DomainValidationException("Invalid environment name '%s': must start with a letter and contain only letters, digits, and underscores".formatted(name));
        }
        return normalized;
    }
}
