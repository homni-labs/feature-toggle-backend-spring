package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.InsufficientPermissionException;

/**
 * Represents a user's access level within a project.
 *
 * <p>Sealed to exactly two strategies: platform administrators who bypass all checks,
 * and role-based access governed by a {@link ProjectRole}.</p>
 */
public sealed interface ProjectAccess permits PlatformAdminAccess, RoleBasedAccess {

    /**
     * Verifies that this access level includes the required permission.
     *
     * @param permission the permission to verify
     * @throws InsufficientPermissionException if the permission is not granted
     *
     * <pre>{@code
     * ProjectAccess access = new RoleBasedAccess(projectId, ProjectRole.READER);
     * access.ensure(Permission.READ_TOGGLES);   // passes
     * access.ensure(Permission.WRITE_TOGGLES);  // throws InsufficientPermissionException
     * }</pre>
     */
    void ensure(Permission permission);
}
