package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectId;

/**
 * Updates the mutable fields of an existing project.
 */
public final class UpdateProjectUseCase {

    private final ProjectRepositoryPort projects;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates an update-project use case.
     *
     * @param projects     the project persistence port
     * @param callerAccess resolves the caller's project access
     */
    public UpdateProjectUseCase(ProjectRepositoryPort projects,
                                 CallerProjectAccessPort callerAccess) {
        this.projects = projects;
        this.callerAccess = callerAccess;
    }

    /**
     * Updates a project's name, description, and/or archived status.
     *
     * @param id          the project identity
     * @param name        the new project name, or {@code null} to keep current
     * @param description the new project description, or {@code null} to keep current
     * @param archived    the new archived status, or {@code null} to keep current
     * @return the updated project
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks MANAGE_MEMBERS
     * @throws EntityNotFoundException if the project does not exist
     *
     * <pre>{@code
     * Project updated = updateProject.execute(projectId, "New Name", null, null);
     * }</pre>
     */
    public Project execute(ProjectId id, String name, String description, Boolean archived) {
        callerAccess.resolve(id).ensure(Permission.MANAGE_MEMBERS);
        Project project = projects.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project", id.value));
        project.update(name, description);
        applyArchivedChange(project, archived);
        projects.save(project);
        return project;
    }

    private void applyArchivedChange(Project project, Boolean archived) {
        if (Boolean.TRUE.equals(archived) && !project.isArchived()) {
            project.archive();
        } else if (Boolean.FALSE.equals(archived) && project.isArchived()) {
            project.unarchive();
        }
    }
}
