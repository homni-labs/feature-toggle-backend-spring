package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.application.usecase.ProjectWithRole;
import com.homni.featuretoggle.domain.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting projects.
 *
 * <pre>{@code
 * Project project = projects.findById(projectId).orElseThrow();
 * project.archive();
 * projects.save(project);
 * }</pre>
 */
public interface ProjectRepositoryPort {

    /**
     * Saves a project (insert or update).
     *
     * @param project the project to save
     *
     * <pre>{@code
     * projects.save(newProject);
     * }</pre>
     */
    void save(Project project);

    /**
     * Finds a project by its identity.
     *
     * @param id the project identity
     * @return the project if found, or empty
     *
     * <pre>{@code
     * Optional<Project> project = projects.findById(projectId);
     * }</pre>
     */
    Optional<Project> findById(ProjectId id);

    /**
     * Returns all projects ordered by name.
     *
     * @return all projects
     *
     * <pre>{@code
     * List<Project> all = projects.findAll();
     * }</pre>
     */
    List<Project> findAll();

    /**
     * Returns all projects where the given user is a member.
     *
     * @param userId the user identity
     * @return the projects the user belongs to
     *
     * <pre>{@code
     * List<Project> myProjects = projects.findByMember(currentUserId);
     * }</pre>
     */
    List<Project> findByMember(UserId userId);

    /**
     * Returns all non-archived projects where the given user is a member,
     * including the user's role in each project.
     *
     * @param userId the user identity
     * @return the projects with the user's role
     *
     * <pre>{@code
     * List<ProjectWithRole> myProjects = projects.findByMemberWithRole(currentUserId);
     * }</pre>
     */
    List<ProjectWithRole> findByMemberWithRole(UserId userId);
}
