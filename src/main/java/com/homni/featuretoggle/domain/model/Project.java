package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.DomainValidationException;
import com.homni.featuretoggle.domain.exception.InvalidStateException;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root representing a project that groups feature toggles.
 *
 * <p>A project has a unique {@link ProjectSlug}, a human-readable name,
 * an optional description, and can be archived/unarchived.</p>
 */
public final class Project {

    public final ProjectId id;
    public final ProjectSlug slug;
    public final Instant createdAt;

    private String name;
    private String description;
    private boolean archived;
    private Instant updatedAt;

    /**
     * Creates a new active project.
     *
     * @param key         the unique project key
     * @param name        the human-readable name (1-255 non-blank characters)
     * @param description the project description, may be {@code null}
     * @throws DomainValidationException if the name is invalid
     *
     * <pre>{@code
     * Project project = new Project(new ProjectSlug("MY-APP"), "My Application", "Main backend");
     * }</pre>
     */
    public Project(ProjectSlug slug, String name, String description) {
        this.id = new ProjectId();
        this.slug = Objects.requireNonNull(slug, "ProjectSlug must not be null");
        this.name = validateName(name);
        this.description = description;
        this.archived = false;
        this.createdAt = Instant.now();
        this.updatedAt = null;
    }

    /**
     * Restores a project from persistent storage.
     *
     * @param id          the project identity
     * @param key         the unique project key
     * @param name        the project name
     * @param description the project description, may be {@code null}
     * @param archived    whether the project is archived
     * @param createdAt   the creation timestamp
     * @param updatedAt   the last modification timestamp, may be {@code null}
     */
    public Project(ProjectId id, ProjectSlug slug, String name, String description,
                   boolean archived, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "ProjectId must not be null");
        this.slug = Objects.requireNonNull(slug, "ProjectSlug must not be null");
        this.name = validateName(name);
        this.description = description;
        this.archived = archived;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = updatedAt;
    }

    /**
     * Updates the mutable fields of this project.
     * If both arguments equal the current values, this is a no-op.
     *
     * @param newName        the new name (1-255 non-blank characters)
     * @param newDescription the new description, may be {@code null}
     * @throws DomainValidationException if the new name is invalid
     *
     * <pre>{@code
     * project.update("New Name", "Updated description");
     * }</pre>
     */
    public void update(String newName, String newDescription) {
        if (this.name.equals(newName) && Objects.equals(this.description, newDescription)) {
            return;
        }
        this.name = validateName(newName);
        this.description = newDescription;
        this.updatedAt = Instant.now();
    }

    /**
     * Archives this project, marking it as inactive.
     *
     * @throws InvalidStateException if the project is already archived
     *
     * <pre>{@code
     * project.archive();
     * }</pre>
     */
    public void archive() {
        if (this.archived) {
            throw new InvalidStateException("Project [id=%s, slug=%s] is already archived".formatted(this.id.value, this.slug.value()));
        }
        this.archived = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Restores this project from archived state, marking it as active.
     *
     * @throws InvalidStateException if the project is not archived
     *
     * <pre>{@code
     * project.unarchive();
     * }</pre>
     */
    public void unarchive() {
        if (!this.archived) {
            throw new InvalidStateException("Project [id=%s, slug=%s] is not archived".formatted(this.id.value, this.slug.value()));
        }
        this.archived = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the current name of this project.
     *
     * @return the project name
     */
    public String name() {
        return this.name;
    }

    /**
     * Indicates whether this project is currently archived.
     *
     * @return {@code true} if the project is archived
     */
    public boolean isArchived() {
        return this.archived;
    }

    /**
     * Returns the project description.
     *
     * @return the description, or empty if not set
     */
    public Optional<String> description() {
        return Optional.ofNullable(this.description);
    }

    /**
     * Returns the instant when this project was last modified.
     *
     * @return the last modification timestamp, or empty if never modified
     */
    public Optional<Instant> lastModifiedAt() {
        return Optional.ofNullable(this.updatedAt);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank() || name.length() > 255) {
            throw new DomainValidationException("Invalid project name: " + name);
        }
        return name;
    }
}
