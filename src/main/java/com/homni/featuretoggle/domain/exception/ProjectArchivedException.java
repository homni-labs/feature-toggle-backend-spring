package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.ProjectId;

/**
 * Thrown when attempting to modify a project that has been archived.
 */
public final class ProjectArchivedException extends DomainConflictException {

    /**
     * @param id the identity of the archived project
     */
    public ProjectArchivedException(ProjectId id) {
        super("Project [id=%s] is archived and cannot be modified".formatted(id.value));
    }
}
