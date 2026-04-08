package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.ProjectAccess;
import com.homni.featuretoggle.domain.model.ProjectId;

/**
 * Resolves the currently authenticated caller's access level for a given project.
 * Handles both OIDC user sessions (membership-based roles) and API key authentication.
 *
 * <pre>{@code
 * callerAccess.resolve(projectId).ensure(Permission.WRITE_TOGGLES);
 * }</pre>
 */
public interface CallerProjectAccessPort {

    /**
     * Resolves the caller's project access for the given project.
     *
     * @param projectId the target project identity
     * @return the resolved project access
     * @throws com.homni.featuretoggle.domain.exception.NotProjectMemberException if the caller has no access
     */
    ProjectAccess resolve(ProjectId projectId);
}
