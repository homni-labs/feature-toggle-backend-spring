package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.FeatureToggle;
import com.homni.featuretoggle.domain.model.FeatureToggleId;
import com.homni.featuretoggle.domain.model.ProjectId;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting feature toggles scoped to a project.
 *
 * <pre>{@code
 * FeatureToggle toggle = toggles.findById(toggleId).orElseThrow();
 * toggle.enable();
 * toggles.save(toggle);
 * }</pre>
 */
public interface FeatureToggleRepositoryPort {

    /**
     * Saves a feature toggle (insert or update).
     *
     * @param toggle the toggle to save
     *
     * <pre>{@code
     * toggles.save(newToggle);
     * }</pre>
     */
    void save(FeatureToggle toggle);

    /**
     * Finds a feature toggle by its identity.
     *
     * @param id the toggle identity
     * @return the toggle if found, or empty
     *
     * <pre>{@code
     * Optional<FeatureToggle> toggle = toggles.findById(toggleId);
     * }</pre>
     */
    Optional<FeatureToggle> findById(FeatureToggleId id);

    /**
     * Lists feature toggles belonging to a project with optional filtering and pagination.
     *
     * @param projectId   the owning project identity
     * @param enabled     filter by enabled status, or {@code null} for all
     * @param environment filter by environment name, or {@code null} for all
     * @param offset      the number of rows to skip
     * @param limit       the maximum number of rows to return
     * @return the matching toggles
     *
     * <pre>{@code
     * List<FeatureToggle> page = toggles.findAllByProject(projectId, true, "production", 0, 20);
     * }</pre>
     */
    List<FeatureToggle> findAllByProject(ProjectId projectId, Boolean enabled, String environment,
                                         int offset, int limit);

    /**
     * Counts feature toggles belonging to a project matching the given filters.
     *
     * @param projectId   the owning project identity
     * @param enabled     filter by enabled status, or {@code null} for all
     * @param environment filter by environment name, or {@code null} for all
     * @return the count of matching toggles
     *
     * <pre>{@code
     * long total = toggles.countByProject(projectId, null, null);
     * }</pre>
     */
    long countByProject(ProjectId projectId, Boolean enabled, String environment);

    /**
     * Deletes a feature toggle by its identity.
     *
     * @param id the toggle identity
     *
     * <pre>{@code
     * toggles.deleteById(toggleId);
     * }</pre>
     */
    void deleteById(FeatureToggleId id);
}
