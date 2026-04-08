package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.AppUserRepositoryPort;
import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.ProjectMembershipRepositoryPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.exception.ProjectArchivedException;
import com.homni.featuretoggle.domain.model.AppUser;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.domain.model.ProjectMembership;
import com.homni.featuretoggle.domain.model.ProjectRole;
import com.homni.featuretoggle.domain.model.UserId;

import java.util.Optional;

/**
 * Adds a user to a project or updates their role if already a member.
 * Upsert semantics: PUT /projects/{id}/members/{userId}.
 */
public final class UpsertMemberUseCase {

    private final ProjectMembershipRepositoryPort memberships;
    private final AppUserRepositoryPort users;
    private final ProjectRepositoryPort projects;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates an upsert-member use case.
     *
     * @param memberships  the membership persistence port
     * @param users        the user persistence port
     * @param projects     the project persistence port
     * @param callerAccess resolves the caller's project access
     */
    public UpsertMemberUseCase(ProjectMembershipRepositoryPort memberships,
                               AppUserRepositoryPort users,
                               ProjectRepositoryPort projects,
                               CallerProjectAccessPort callerAccess) {
        this.memberships = memberships;
        this.users = users;
        this.projects = projects;
        this.callerAccess = callerAccess;
    }

    /**
     * Adds a user to the project with the given role, or updates the role if already a member.
     *
     * @param projectId the project identity
     * @param userId    the user identity
     * @param role      the project role to assign
     * @return the created or updated membership
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks MANAGE_MEMBERS
     * @throws ProjectArchivedException if the project is archived
     * @throws EntityNotFoundException if the user does not exist
     *
     * <pre>{@code
     * ProjectMembership m = upsertMember.execute(projectId, userId, ProjectRole.EDITOR);
     * }</pre>
     */
    public ProjectMembership execute(ProjectId projectId, UserId userId, ProjectRole role) {
        callerAccess.resolve(projectId).ensure(Permission.MANAGE_MEMBERS);
        ensureProjectNotArchived(projectId);
        AppUser user = users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId.value));
        Optional<ProjectMembership> existing = memberships.findByProjectAndUser(projectId, userId);
        ProjectMembership membership = existing.map(m -> {
            m.changeRole(role);
            return m;
        }).orElseGet(() -> new ProjectMembership(projectId, userId, role));
        memberships.save(membership);
        membership.enrichWithUserInfo(user.email.value(), user.displayName().orElse(null));
        return membership;
    }

    private void ensureProjectNotArchived(ProjectId projectId) {
        Project project = projects.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project", projectId.value));
        if (project.isArchived()) {
            throw new ProjectArchivedException(projectId);
        }
    }
}
