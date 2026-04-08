package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.ProjectId;

/**
 * Thrown when a user lacks a required permission on a project.
 */
public final class InsufficientPermissionException extends DomainAccessDeniedException {

    /**
     * @param projectId  the project identity
     * @param permission the permission that is missing
     */
    public InsufficientPermissionException(ProjectId projectId, Permission permission) {
        super("Insufficient permission '%s' on project [id=%s]".formatted(permission, projectId.value));
    }
}
