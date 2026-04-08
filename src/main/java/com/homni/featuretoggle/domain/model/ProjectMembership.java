package com.homni.featuretoggle.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root representing a user's membership and role within a project.
 *
 * <p>Links a {@link UserId} to a {@link ProjectId} with a specific {@link ProjectRole}.
 * The role can be changed over time via {@link #changeRole(ProjectRole)}.</p>
 */
public final class ProjectMembership {

    public final ProjectMembershipId id;
    public final ProjectId projectId;
    public final UserId userId;
    public final Instant grantedAt;

    private ProjectRole role;
    private Instant updatedAt;
    private String email;
    private String displayName;

    /**
     * Creates a new project membership.
     *
     * @param projectId the project identity
     * @param userId    the user identity
     * @param role      the initial role assigned to the user
     *
     * <pre>{@code
     * ProjectMembership membership = new ProjectMembership(projectId, userId, ProjectRole.EDITOR);
     * }</pre>
     */
    public ProjectMembership(ProjectId projectId, UserId userId, ProjectRole role) {
        this.id = new ProjectMembershipId();
        this.projectId = Objects.requireNonNull(projectId, "ProjectId must not be null");
        this.userId = Objects.requireNonNull(userId, "UserId must not be null");
        this.role = Objects.requireNonNull(role, "ProjectRole must not be null");
        this.grantedAt = Instant.now();
        this.updatedAt = null;
    }

    /**
     * Restores a project membership from persistent storage.
     *
     * @param id        the membership identity
     * @param projectId the project identity
     * @param userId    the user identity
     * @param role      the assigned role
     * @param grantedAt the grant timestamp
     * @param updatedAt the last modification timestamp, may be {@code null}
     */
    public ProjectMembership(ProjectMembershipId id, ProjectId projectId, UserId userId,
                             ProjectRole role, Instant grantedAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "ProjectMembershipId must not be null");
        this.projectId = Objects.requireNonNull(projectId, "ProjectId must not be null");
        this.userId = Objects.requireNonNull(userId, "UserId must not be null");
        this.role = Objects.requireNonNull(role, "ProjectRole must not be null");
        this.grantedAt = Objects.requireNonNull(grantedAt, "grantedAt must not be null");
        this.updatedAt = updatedAt;
    }

    /**
     * Restores a project membership with user info from persistent storage (JOIN query).
     */
    public ProjectMembership(ProjectMembershipId id, ProjectId projectId, UserId userId,
                             ProjectRole role, Instant grantedAt, Instant updatedAt,
                             String email, String displayName) {
        this(id, projectId, userId, role, grantedAt, updatedAt);
        this.email = email;
        this.displayName = displayName;
    }

    /**
     * Changes the role assigned to this member within the project.
     *
     * @param newRole the new role to assign
     *
     * <pre>{@code
     * membership.changeRole(ProjectRole.ADMIN);
     * }</pre>
     */
    public void changeRole(ProjectRole newRole) {
        Objects.requireNonNull(newRole, "ProjectRole must not be null");
        this.role = newRole;
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the current role of this member.
     *
     * @return the assigned project role
     */
    public ProjectRole currentRole() {
        return this.role;
    }

    /**
     * Returns the instant when this membership was last modified.
     *
     * @return the last modification timestamp, or empty if never modified
     */
    public Optional<Instant> lastModifiedAt() {
        return Optional.ofNullable(this.updatedAt);
    }

    /**
     * Enriches this membership with user display info.
     * Called by use-cases that already have the user loaded.
     *
     * @param email       the user's email
     * @param displayName the user's display name, may be {@code null}
     */
    public void enrichWithUserInfo(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    /**
     * Returns the member's email, if loaded via JOIN.
     */
    public Optional<String> email() {
        return Optional.ofNullable(this.email);
    }

    /**
     * Returns the member's display name, if loaded via JOIN.
     */
    public Optional<String> displayName() {
        return Optional.ofNullable(this.displayName);
    }
}
