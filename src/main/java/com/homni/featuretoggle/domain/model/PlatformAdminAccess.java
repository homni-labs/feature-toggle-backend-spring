package com.homni.featuretoggle.domain.model;

/**
 * Access level for platform administrators who have unrestricted permissions
 * across all projects.
 *
 * <p>The {@link #ensure(Permission)} method is a no-op because platform admins
 * are never denied access.</p>
 */
public final class PlatformAdminAccess implements ProjectAccess {

    /**
     * No-op: platform administrators have all permissions by definition.
     *
     * @param permission the permission to verify (ignored)
     *
     * <pre>{@code
     * ProjectAccess admin = new PlatformAdminAccess();
     * admin.ensure(Permission.MANAGE_MEMBERS);  // always passes
     * }</pre>
     */
    @Override
    public void ensure(Permission permission) {
        // Platform admin has all permissions — nothing to check.
    }
}
