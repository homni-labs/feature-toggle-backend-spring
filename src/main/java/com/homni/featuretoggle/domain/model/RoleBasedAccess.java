package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.InsufficientPermissionException;

import java.util.Objects;

/**
 * Access level governed by a {@link ProjectRole} within a specific project.
 *
 * <p>The {@link #ensure(Permission)} method throws
 * {@link InsufficientPermissionException} when the role does not include
 * the required permission.</p>
 */
public final class RoleBasedAccess implements ProjectAccess {

    public final ProjectId projectId;
    private final ProjectRole role;

    /**
     * Creates a role-based access check bound to a project and role.
     *
     * @param projectId the project identity
     * @param role      the role assigned to the user in this project
     *
     * <pre>{@code
     * ProjectAccess access = new RoleBasedAccess(projectId, ProjectRole.EDITOR);
     * access.ensure(Permission.WRITE_TOGGLES);  // passes
     * }</pre>
     */
    public RoleBasedAccess(ProjectId projectId, ProjectRole role) {
        this.projectId = Objects.requireNonNull(projectId, "ProjectId must not be null");
        this.role = Objects.requireNonNull(role, "ProjectRole must not be null");
    }

    /**
     * Verifies that the assigned role grants the required permission.
     *
     * @param permission the permission to verify
     * @throws InsufficientPermissionException if the role does not include the permission
     *
     * <pre>{@code
     * ProjectAccess reader = new RoleBasedAccess(projectId, ProjectRole.READER);
     * reader.ensure(Permission.READ_TOGGLES);    // passes
     * reader.ensure(Permission.WRITE_TOGGLES);   // throws InsufficientPermissionException
     * }</pre>
     */
    @Override
    public void ensure(Permission permission) {
        if (!this.role.has(permission)) {
            throw new InsufficientPermissionException(this.projectId, permission);
        }
    }
}
