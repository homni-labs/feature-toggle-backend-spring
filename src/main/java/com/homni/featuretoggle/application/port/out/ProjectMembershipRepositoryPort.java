package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.domain.model.ProjectMembership;
import com.homni.featuretoggle.domain.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting project memberships (user-to-project role assignments).
 *
 * <pre>{@code
 * Optional<ProjectMembership> membership = memberships.findByProjectAndUser(projectId, userId);
 * }</pre>
 */
public interface ProjectMembershipRepositoryPort {

    /**
     * Saves a project membership (insert or update).
     *
     * @param membership the membership to save
     *
     * <pre>{@code
     * memberships.save(newMembership);
     * }</pre>
     */
    void save(ProjectMembership membership);

    /**
     * Finds a membership by project and user combination.
     *
     * @param projectId the project identity
     * @param userId    the user identity
     * @return the membership if found, or empty
     *
     * <pre>{@code
     * Optional<ProjectMembership> m = memberships.findByProjectAndUser(projectId, userId);
     * }</pre>
     */
    Optional<ProjectMembership> findByProjectAndUser(ProjectId projectId, UserId userId);

    /**
     * Lists memberships for a project with pagination.
     *
     * @param projectId the project identity
     * @param offset    the number of rows to skip
     * @param limit     the maximum number of rows to return
     * @return the memberships for the project
     *
     * <pre>{@code
     * List<ProjectMembership> page = memberships.findByProject(projectId, 0, 20);
     * }</pre>
     */
    List<ProjectMembership> findByProject(ProjectId projectId, int offset, int limit);

    /**
     * Counts the total memberships for a project.
     *
     * @param projectId the project identity
     * @return the number of members in the project
     *
     * <pre>{@code
     * long memberCount = memberships.countByProject(projectId);
     * }</pre>
     */
    long countByProject(ProjectId projectId);

    /**
     * Removes a membership by project and user combination.
     *
     * @param projectId the project identity
     * @param userId    the user identity
     *
     * <pre>{@code
     * memberships.deleteByProjectAndUser(projectId, userId);
     * }</pre>
     */
    void deleteByProjectAndUser(ProjectId projectId, UserId userId);
}
