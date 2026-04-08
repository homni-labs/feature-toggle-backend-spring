package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.domain.model.UserId;

/**
 * Thrown when an operation requires project membership but the user is not a member.
 */
public final class NotProjectMemberException extends DomainAccessDeniedException {

    /**
     * @param projectId the project identity
     * @param userId    the user identity
     */
    public NotProjectMemberException(ProjectId projectId, UserId userId) {
        super("User [id=%s] is not a member of project [id=%s]".formatted(userId.value, projectId.value));
    }
}
