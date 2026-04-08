package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.DomainValidationException;
import com.homni.featuretoggle.domain.exception.InvalidStateException;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Feature toggle scoped to a project, assigned to one or more environments.
 */
public final class FeatureToggle {

    public final FeatureToggleId id;
    public final ProjectId projectId;
    public final Instant createdAt;

    private String name;
    private String description;
    private boolean enabled;
    private Instant updatedAt;
    private final Set<String> environments;

    /**
     * Creates a new feature toggle in a disabled state within a project.
     *
     * @param projectId    the owning project
     * @param name         the toggle name (1-255 non-blank characters)
     * @param description  the human-readable description, may be {@code null}
     * @param environments the initial set of environment names, must not be empty
     * @throws DomainValidationException if the name is invalid
     * @throws DomainValidationException if environments is empty
     */
    public FeatureToggle(ProjectId projectId, String name, String description,
                         Set<String> environments) {
        this.id = new FeatureToggleId();
        this.projectId = Objects.requireNonNull(projectId);
        this.name = validateName(name);
        this.description = description;
        this.enabled = false;
        this.environments = validateEnvironments(name, environments);
        this.createdAt = Instant.now();
        this.updatedAt = null;
    }

    /**
     * Restores a feature toggle from persistent storage.
     *
     * @param id           the toggle identity
     * @param projectId    the owning project
     * @param name         the toggle name
     * @param description  the toggle description, may be {@code null}
     * @param enabled      whether the toggle is enabled
     * @param environments the assigned environment names
     * @param createdAt    the creation timestamp
     * @param updatedAt    the last modification timestamp, may be {@code null}
     */
    public FeatureToggle(FeatureToggleId id, ProjectId projectId, String name,
                         String description, boolean enabled, Set<String> environments,
                         Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.projectId = Objects.requireNonNull(projectId);
        this.name = validateName(name);
        this.description = description;
        this.enabled = enabled;
        this.environments = validateEnvironments(name, environments);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = updatedAt;
    }

    /**
     * Enables this feature toggle and records the modification time.
     *
     * @return this toggle for chaining
     * @throws InvalidStateException if the toggle is already enabled
     */
    public FeatureToggle enable() {
        if (this.enabled) {
            throw new InvalidStateException("Toggle [id=%s, name=%s] is already enabled".formatted(this.id.value, this.name));
        }
        this.enabled = true;
        this.updatedAt = Instant.now();
        return this;
    }

    /**
     * Disables this feature toggle and records the modification time.
     *
     * @return this toggle for chaining
     * @throws InvalidStateException if the toggle is already disabled
     */
    public FeatureToggle disable() {
        if (!this.enabled) {
            throw new InvalidStateException("Toggle [id=%s, name=%s] is already disabled".formatted(this.id.value, this.name));
        }
        this.enabled = false;
        this.updatedAt = Instant.now();
        return this;
    }

    /**
     * Updates the mutable fields of this toggle.
     * Each parameter may be {@code null} to skip updating that field.
     *
     * @param newName         the new name, or {@code null} to keep current
     * @param newDescription  the new description, or {@code null} to keep current
     * @param newEnvironments the new set of environment names, or {@code null} to keep current
     */
    public void update(String newName, String newDescription, Set<String> newEnvironments) {
        if (newName == null && newDescription == null && newEnvironments == null) {
            return;
        }
        if (newName != null) {
            this.name = validateName(newName);
        }
        if (newDescription != null) {
            this.description = newDescription;
        }
        if (newEnvironments != null) {
            LinkedHashSet<String> validated = validateEnvironments(this.name, newEnvironments);
            this.environments.clear();
            this.environments.addAll(validated);
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the current name of this toggle.
     *
     * @return the toggle name
     */
    public String name() {
        return this.name;
    }

    /**
     * Returns the toggle description text.
     *
     * @return the description, or empty if not set
     */
    public Optional<String> description() {
        return Optional.ofNullable(this.description);
    }

    /**
     * Indicates whether this toggle is currently enabled.
     *
     * @return {@code true} if the toggle is enabled
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Returns the assigned environment names as an immutable copy.
     *
     * @return the set of environment names
     */
    public Set<String> environments() {
        return Set.copyOf(this.environments);
    }

    /**
     * Returns the instant when this toggle was last modified.
     *
     * @return the last modification timestamp, or empty if never modified
     */
    public Optional<Instant> lastModifiedAt() {
        return Optional.ofNullable(this.updatedAt);
    }

    private LinkedHashSet<String> validateEnvironments(String toggleName,
                                                       Set<String> environments) {
        if (environments == null || environments.isEmpty()) {
            throw new DomainValidationException("Toggle '%s' must have at least one environment".formatted(toggleName));
        }
        return new LinkedHashSet<>(environments);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank() || name.length() > 255) {
            throw new DomainValidationException("Invalid toggle name: " + name);
        }
        return name;
    }
}
