package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.EnvironmentRepositoryPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.exception.EnvironmentInUseException;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.exception.ProjectArchivedException;
import com.homni.featuretoggle.domain.model.Environment;
import com.homni.featuretoggle.domain.model.EnvironmentId;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectId;

/**
 * Deletes a deployment environment from a project.
 */
public final class DeleteEnvironmentUseCase {

    private final EnvironmentRepositoryPort environments;
    private final ProjectRepositoryPort projects;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a delete-environment use case.
     *
     * @param environments the environment persistence port
     * @param projects     the project persistence port
     * @param callerAccess resolves the caller's project access
     */
    public DeleteEnvironmentUseCase(EnvironmentRepositoryPort environments,
                                    ProjectRepositoryPort projects,
                                    CallerProjectAccessPort callerAccess) {
        this.environments = environments;
        this.projects = projects;
        this.callerAccess = callerAccess;
    }

    /**
     * Deletes the specified environment from the project.
     *
     * @param id        the environment identity
     * @param projectId the owning project identity
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks WRITE_TOGGLES
     * @throws ProjectArchivedException if the project is archived
     * @throws EntityNotFoundException if the environment does not exist
     * @throws EnvironmentInUseException if the environment is referenced by feature toggles
     *
     * <pre>{@code
     * deleteEnvironment.execute(envId, projectId);
     * }</pre>
     */
    public void execute(EnvironmentId id, ProjectId projectId) {
        callerAccess.resolve(projectId).ensure(Permission.WRITE_TOGGLES);
        ensureProjectNotArchived(projectId);
        Environment environment = environments.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Environment", id.value));
        if (environments.isEnvironmentInUse(environment.name(), projectId)) {
            throw new EnvironmentInUseException(environment.name());
        }
        environments.deleteById(id);
    }

    private void ensureProjectNotArchived(ProjectId projectId) {
        Project project = projects.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project", projectId.value));
        if (project.isArchived()) {
            throw new ProjectArchivedException(projectId);
        }
    }
}
