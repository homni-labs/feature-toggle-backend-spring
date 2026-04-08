package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.Environment;
import com.homni.featuretoggle.domain.model.EnvironmentId;
import com.homni.featuretoggle.domain.model.ProjectId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Output port for persisting deployment environments scoped to a project.
 *
 * <pre>{@code
 * List<Environment> envs = environments.findAllByProject(projectId);
 * }</pre>
 */
public interface EnvironmentRepositoryPort {

    /**
     * Saves an environment (insert or update).
     *
     * @param environment the environment to save
     *
     * <pre>{@code
     * environments.save(newEnvironment);
     * }</pre>
     */
    void save(Environment environment);

    /**
     * Finds an environment by its identity.
     *
     * @param id the environment identity
     * @return the environment if found, or empty
     *
     * <pre>{@code
     * Optional<Environment> env = environments.findById(envId);
     * }</pre>
     */
    Optional<Environment> findById(EnvironmentId id);

    /**
     * Returns all environments belonging to a project, ordered by name.
     *
     * @param projectId the owning project identity
     * @return all environments for the project
     *
     * <pre>{@code
     * List<Environment> envs = environments.findAllByProject(projectId);
     * }</pre>
     */
    List<Environment> findAllByProject(ProjectId projectId);

    /**
     * Returns the set of environment names defined in a project.
     *
     * @param projectId the owning project identity
     * @return environment names for the project
     *
     * <pre>{@code
     * Set<String> names = environments.findNamesByProjectId(projectId);
     * }</pre>
     */
    Set<String> findNamesByProjectId(ProjectId projectId);

    /**
     * Deletes an environment by its identity.
     *
     * @param id the environment identity
     *
     * <pre>{@code
     * environments.deleteById(envId);
     * }</pre>
     */
    void deleteById(EnvironmentId id);

    /**
     * Checks whether any feature toggle in the given project references the environment name.
     *
     * @param name      the environment name to check
     * @param projectId the owning project identity
     * @return {@code true} if at least one toggle uses this environment in the project
     *
     * <pre>{@code
     * if (environments.isEnvironmentInUse("production", projectId)) {
     *     throw new EnvironmentInUseException("production");
     * }
     * }</pre>
     */
    boolean isEnvironmentInUse(String name, ProjectId projectId);
}
