package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.ProjectMembershipRepositoryPort;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.domain.model.ProjectMembership;

import java.util.List;

/**
 * Lists project members with pagination.
 */
public final class ListMembersUseCase {

    private final ProjectMembershipRepositoryPort memberships;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a list-members use case.
     *
     * @param memberships  the membership persistence port
     * @param callerAccess resolves the caller's project access
     */
    public ListMembersUseCase(ProjectMembershipRepositoryPort memberships,
                               CallerProjectAccessPort callerAccess) {
        this.memberships = memberships;
        this.callerAccess = callerAccess;
    }

    /**
     * Returns a paginated list of members for the specified project.
     *
     * @param projectId the project identity
     * @param page      the zero-based page number
     * @param size      the page size
     * @return a page of memberships with total count
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks READ_TOGGLES
     *
     * <pre>{@code
     * MemberPage page = listMembers.execute(projectId, 0, 20);
     * }</pre>
     */
    public MemberPage execute(ProjectId projectId, int page, int size) {
        callerAccess.resolve(projectId).ensure(Permission.READ_TOGGLES);
        int offset = page * size;
        List<ProjectMembership> items = memberships.findByProject(projectId, offset, size);
        long total = memberships.countByProject(projectId);
        return new MemberPage(items, total);
    }
}
