package com.homni.featuretoggle.domain.model;

/**
 * Platform-wide roles assigned to application users.
 */
public enum PlatformRole {

    /**
     * Full platform administrator with unrestricted access to all projects and settings.
     */
    PLATFORM_ADMIN,

    /**
     * Regular user whose project access is governed by per-project role assignments.
     */
    USER
}
