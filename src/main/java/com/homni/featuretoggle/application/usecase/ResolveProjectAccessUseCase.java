package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.ProjectMembershipRepositoryPort;
import com.homni.featuretoggle.domain.model.AppUser;
import com.homni.featuretoggle.domain.model.ProjectAccess;
import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.domain.model.ProjectMembership;

import java.util.Optional;

/**
 * Resolves the caller's access level for a specific project.
 *
 * <p>Loads the caller's project membership (if any) and delegates the
 * access decision to the domain model via {@link AppUser#accessFor}.</p>
 */
public final class ResolveProjectAccessUseCase {

    private final ProjectMembershipRepositoryPort memberships;

    /**
     * Creates a resolve-project-access use case.
     *
     * @param memberships the membership persistence port
     */
    public ResolveProjectAccessUseCase(ProjectMembershipRepositoryPort memberships) {
        this.memberships = memberships;
    }

    /**
     * Resolves the access level of the caller for the given project.
     *
     * @param caller    the authenticated user
     * @param projectId the target project identity
     * @return the resolved access level
     * @throws com.homni.featuretoggle.domain.exception.NotProjectMemberException if the user
     *         is not a platform admin and has no membership in the project
     *
     * <pre>{@code
     * ProjectAccess access = resolveProjectAccess.resolve(caller, projectId);
     * access.ensure(Permission.WRITE_TOGGLES);
     * }</pre>
     */
    public ProjectAccess resolve(AppUser caller, ProjectId projectId) {
        Optional<ProjectMembership> membership = memberships.findByProjectAndUser(
                projectId, caller.id);
        return caller.accessFor(projectId, membership);
    }
}
