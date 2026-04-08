package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.InvalidStateException;
import com.homni.featuretoggle.domain.exception.NotProjectMemberException;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Platform user with OIDC authentication and platform-level role.
 * Access to individual projects is determined via {@link ProjectMembership}.
 */
public final class AppUser {

    public final UserId id;
    public final Email email;
    public final Instant createdAt;

    private String oidcSubject;
    private String name;
    private PlatformRole platformRole;
    private boolean active;
    private Instant updatedAt;

    /**
     * Creates a new active user with an OIDC subject (USER by default).
     *
     * @param oidcSubject the OIDC subject identifier
     * @param email       the user's email address (will be validated)
     * @param name        the display name, may be {@code null}
     * @throws com.homni.featuretoggle.domain.exception.DomainValidationException if email format is invalid
     */
    public AppUser(String oidcSubject, String email, String name) {
        Objects.requireNonNull(oidcSubject, "oidcSubject must not be null");
        this.id = new UserId();
        this.oidcSubject = oidcSubject;
        this.email = new Email(email);
        this.name = name;
        this.platformRole = PlatformRole.USER;
        this.active = true;
        this.createdAt = Instant.now();
        this.updatedAt = null;
    }

    /**
     * Creates a pre-provisioned user without an OIDC subject.
     *
     * @param email        the user's email address (will be validated)
     * @param name         the display name, may be {@code null}
     * @param platformRole the platform role
     * @throws com.homni.featuretoggle.domain.exception.DomainValidationException if email format is invalid
     */
    public AppUser(String email, String name, PlatformRole platformRole) {
        this.id = new UserId();
        this.oidcSubject = null;
        this.email = new Email(email);
        this.name = name;
        this.platformRole = Objects.requireNonNull(platformRole);
        this.active = true;
        this.createdAt = Instant.now();
        this.updatedAt = null;
    }

    /**
     * Restores an existing user from persistent storage.
     *
     * @param id           the user identity
     * @param oidcSubject  the OIDC subject, may be {@code null}
     * @param email        the validated email
     * @param name         the display name, may be {@code null}
     * @param platformRole the platform role
     * @param active       whether the user is active
     * @param createdAt    the creation timestamp
     * @param updatedAt    the last modification timestamp, may be {@code null}
     */
    public AppUser(UserId id, String oidcSubject, Email email, String name,
                   PlatformRole platformRole, boolean active,
                   Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.oidcSubject = oidcSubject;
        this.email = Objects.requireNonNull(email);
        this.name = name;
        this.platformRole = Objects.requireNonNull(platformRole);
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = updatedAt;
    }

    /**
     * Determines access level for this user to a project.
     * PLATFORM_ADMIN gets full access; USER gets role-based access via membership.
     *
     * @param projectId  the target project
     * @param membership the project membership, if any
     * @return the access level
     * @throws NotProjectMemberException if USER is not a member of the project
     */
    public ProjectAccess accessFor(ProjectId projectId,
                                   Optional<ProjectMembership> membership) {
        if (this.platformRole == PlatformRole.PLATFORM_ADMIN) {
            return new PlatformAdminAccess();
        }
        ProjectMembership m = membership
                .orElseThrow(() -> new NotProjectMemberException(projectId, this.id));
        return new RoleBasedAccess(projectId, m.currentRole());
    }

    /**
     * Promotes this user to PLATFORM_ADMIN.
     *
     * @throws InvalidStateException if already a platform admin
     */
    public void promoteToPlatformAdmin() {
        if (this.platformRole == PlatformRole.PLATFORM_ADMIN) {
            throw new InvalidStateException("User [id=%s, email=%s] is already a platform admin".formatted(this.id.value, this.email.value()));
        }
        this.platformRole = PlatformRole.PLATFORM_ADMIN;
        this.updatedAt = Instant.now();
    }

    /**
     * Demotes this user to USER.
     *
     * @throws InvalidStateException if already a regular user
     */
    public void demoteToUser() {
        if (this.platformRole == PlatformRole.USER) {
            throw new InvalidStateException("User [id=%s, email=%s] is not a platform admin".formatted(this.id.value, this.email.value()));
        }
        this.platformRole = PlatformRole.USER;
        this.updatedAt = Instant.now();
    }

    /**
     * Disables this user, preventing authentication.
     *
     * @throws InvalidStateException if the user is already disabled
     */
    public void disable() {
        if (!this.active) {
            throw new InvalidStateException("User [id=%s, email=%s] is already disabled".formatted(this.id.value, this.email.value()));
        }
        this.active = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Activates this user, allowing authentication.
     *
     * @throws InvalidStateException if the user is already active
     */
    public void activate() {
        if (this.active) {
            throw new InvalidStateException("User [id=%s, email=%s] is already active".formatted(this.id.value, this.email.value()));
        }
        this.active = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Binds an OIDC subject identifier to this user.
     *
     * @param oidcSubject the OIDC subject to bind
     * @throws InvalidStateException if an OIDC subject is already bound
     */
    public void bindOidcSubject(String oidcSubject) {
        if (this.oidcSubject != null) {
            throw new InvalidStateException("User [id=%s, email=%s] already has an OIDC subject bound".formatted(this.id.value, this.email.value()));
        }
        this.oidcSubject = Objects.requireNonNull(oidcSubject);
        this.updatedAt = Instant.now();
    }

    /**
     * Checks whether this user is a platform administrator.
     *
     * @return {@code true} if the user is a platform admin
     */
    public boolean isPlatformAdmin() {
        return this.platformRole == PlatformRole.PLATFORM_ADMIN;
    }

    /**
     * Checks whether this user is allowed to authenticate.
     *
     * @return {@code true} if the user is active
     */
    public boolean canAuthenticate() {
        return this.active;
    }

    /**
     * Checks whether this user can have an OIDC subject bound.
     *
     * @return {@code true} if no OIDC subject is currently bound
     */
    public boolean canBindOidc() {
        return this.oidcSubject == null;
    }

    /**
     * Returns the platform role of this user.
     *
     * @return the platform role
     */
    public PlatformRole platformRole() {
        return this.platformRole;
    }

    /**
     * Returns the OIDC subject identifier, if bound.
     *
     * @return the OIDC subject, or empty if not yet bound
     */
    public Optional<String> oidcSubject() {
        return Optional.ofNullable(this.oidcSubject);
    }

    /**
     * Returns the display name of this user.
     *
     * @return the user's name, or empty if not set
     */
    public Optional<String> displayName() {
        return Optional.ofNullable(this.name);
    }

    /**
     * Indicates whether this user account is active.
     *
     * @return {@code true} if the user is active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Returns the instant when this user was last modified.
     *
     * @return the last modification timestamp, or empty if never modified
     */
    public Optional<Instant> lastModifiedAt() {
        return Optional.ofNullable(this.updatedAt);
    }
}
