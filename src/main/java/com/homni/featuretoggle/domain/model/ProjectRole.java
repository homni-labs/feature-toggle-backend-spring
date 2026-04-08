package com.homni.featuretoggle.domain.model;

import static com.homni.featuretoggle.domain.model.Permission.MANAGE_MEMBERS;
import static com.homni.featuretoggle.domain.model.Permission.READ_TOGGLES;
import static com.homni.featuretoggle.domain.model.Permission.WRITE_TOGGLES;

import java.util.Set;

/**
 * Role assigned to a user within a specific project, defining permitted operations.
 */
public enum ProjectRole {

    /**
     * Project administrator with full control: read, write toggles, and manage members.
     */
    ADMIN(Set.of(READ_TOGGLES, WRITE_TOGGLES, MANAGE_MEMBERS)),

    /**
     * Editor who can read and write toggles but cannot manage project members.
     */
    EDITOR(Set.of(READ_TOGGLES, WRITE_TOGGLES)),

    /**
     * Read-only viewer who can only inspect toggle states.
     */
    READER(Set.of(READ_TOGGLES));

    private final Set<Permission> permissions;

    ProjectRole(Set<Permission> permissions) {
        this.permissions = Set.copyOf(permissions);
    }

    /**
     * Checks whether this role includes the given permission.
     *
     * @param permission the permission to check
     * @return {@code true} if this role grants the permission
     *
     * <pre>{@code
     * ProjectRole.ADMIN.has(Permission.MANAGE_MEMBERS);  // true
     * ProjectRole.READER.has(Permission.WRITE_TOGGLES);  // false
     * }</pre>
     */
    public boolean has(Permission permission) {
        return this.permissions.contains(permission);
    }
}
