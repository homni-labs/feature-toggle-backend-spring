package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.model.AppUser;

import java.util.List;

/**
 * Lists projects visible to the calling user.
 */
public final class ListProjectsUseCase {

    private final ProjectRepositoryPort projects;
    private final CallerPort callerPort;

    /**
     * Creates a list-projects use case.
     *
     * @param projects   the project persistence port
     * @param callerPort provides the authenticated caller
     */
    public ListProjectsUseCase(ProjectRepositoryPort projects, CallerPort callerPort) {
        this.projects = projects;
        this.callerPort = callerPort;
    }

    /**
     * Returns projects accessible to the caller: all projects for platform admins
     * (with {@code myRole = null}), or only projects where the caller is a member
     * for regular users (with the caller's project role).
     *
     * @return the list of accessible projects with the caller's role
     *
     * <pre>{@code
     * List<ProjectWithRole> visible = listProjects.execute();
     * }</pre>
     */
    public List<ProjectWithRole> execute() {
        AppUser caller = callerPort.get();
        if (caller.isPlatformAdmin()) {
            return projects.findAll().stream()
                    .map(project -> new ProjectWithRole(project, null))
                    .toList();
        }
        return projects.findByMemberWithRole(caller.id);
    }
}
