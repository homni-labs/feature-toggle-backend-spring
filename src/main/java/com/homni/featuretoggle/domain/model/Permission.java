package com.homni.featuretoggle.domain.model;

/**
 * Permissions that govern access to project resources.
 */
public enum Permission {

    /**
     * Allows reading feature toggles and their states within a project.
     */
    READ_TOGGLES,

    /**
     * Allows creating, updating, enabling, and disabling feature toggles within a project.
     */
    WRITE_TOGGLES,

    /**
     * Allows adding, removing, and changing roles of project members.
     */
    MANAGE_MEMBERS
}
