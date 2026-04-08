package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.EnvironmentRepositoryPort;
import com.homni.featuretoggle.domain.model.Environment;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.ProjectId;

import java.util.List;

/**
 * Lists all deployment environments belonging to a project.
 */
public final class ListEnvironmentsUseCase {

    private final EnvironmentRepositoryPort environments;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a list-environments use case.
     *
     * @param environments the environment persistence port
     * @param callerAccess resolves the caller's project access
     */
    public ListEnvironmentsUseCase(EnvironmentRepositoryPort environments,
                                    CallerProjectAccessPort callerAccess) {
        this.environments = environments;
        this.callerAccess = callerAccess;
    }

    /**
     * Lists all environments for the specified project.
     *
     * @param projectId the owning project identity
     * @return the list of environments
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks READ_TOGGLES
     *
     * <pre>{@code
     * List<Environment> envs = listEnvironments.execute(projectId);
     * }</pre>
     */
    public List<Environment> execute(ProjectId projectId) {
        callerAccess.resolve(projectId).ensure(Permission.READ_TOGGLES);
        return environments.findAllByProject(projectId);
    }
}
