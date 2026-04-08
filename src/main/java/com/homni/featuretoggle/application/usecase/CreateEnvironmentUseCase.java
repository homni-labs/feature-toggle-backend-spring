package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.EnvironmentRepositoryPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.exception.ProjectArchivedException;
import com.homni.featuretoggle.domain.model.Environment;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectId;

/**
 * Creates a new deployment environment within a project.
 */
public final class CreateEnvironmentUseCase {

    private final EnvironmentRepositoryPort environments;
    private final ProjectRepositoryPort projects;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a create-environment use case.
     *
     * @param environments the environment persistence port
     * @param projects     the project persistence port
     * @param callerAccess resolves the caller's project access
     */
    public CreateEnvironmentUseCase(EnvironmentRepositoryPort environments,
                                    ProjectRepositoryPort projects,
                                    CallerProjectAccessPort callerAccess) {
        this.environments = environments;
        this.projects = projects;
        this.callerAccess = callerAccess;
    }

    /**
     * Creates a new environment in the specified project.
     *
     * @param projectId the owning project identity
     * @param name      the environment name (will be normalized to uppercase)
     * @return the created environment
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks WRITE_TOGGLES
     * @throws ProjectArchivedException if the project is archived
     * @throws com.homni.featuretoggle.domain.exception.DomainValidationException if the name is invalid
     *
     * <pre>{@code
     * Environment env = createEnvironment.execute(projectId, "staging");
     * }</pre>
     */
    public Environment execute(ProjectId projectId, String name) {
        callerAccess.resolve(projectId).ensure(Permission.WRITE_TOGGLES);
        ensureProjectNotArchived(projectId);
        Environment environment = new Environment(projectId, name);
        environments.save(environment);
        return environment;
    }

    private void ensureProjectNotArchived(ProjectId projectId) {
        Project project = projects.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project", projectId.value));
        if (project.isArchived()) {
            throw new ProjectArchivedException(projectId);
        }
    }
}
