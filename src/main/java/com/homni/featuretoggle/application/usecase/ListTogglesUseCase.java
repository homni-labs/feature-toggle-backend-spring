package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.FeatureToggleRepositoryPort;
import com.homni.featuretoggle.domain.model.FeatureToggle;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.ProjectId;

import java.util.List;

/**
 * Lists feature toggles for a project with optional filtering and pagination.
 */
public final class ListTogglesUseCase {

    private final FeatureToggleRepositoryPort toggles;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a list-toggles use case.
     *
     * @param toggles      the toggle persistence port
     * @param callerAccess resolves the caller's project access
     */
    public ListTogglesUseCase(FeatureToggleRepositoryPort toggles,
                               CallerProjectAccessPort callerAccess) {
        this.toggles = toggles;
        this.callerAccess = callerAccess;
    }

    /**
     * Lists feature toggles within a project, filtered by optional criteria.
     *
     * @param projectId   the owning project identity
     * @param enabled     filter by enabled status, or {@code null} for all
     * @param environment filter by environment name, or {@code null} for all
     * @param page        the zero-based page number
     * @param size        the maximum number of items per page
     * @return a page of matching toggles
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks READ_TOGGLES
     *
     * <pre>{@code
     * TogglePage page = listToggles.execute(projectId, true, "PRODUCTION", 0, 20);
     * }</pre>
     */
    public TogglePage execute(ProjectId projectId, Boolean enabled, String environment,
                              int page, int size) {
        callerAccess.resolve(projectId).ensure(Permission.READ_TOGGLES);
        int offset = page * size;
        List<FeatureToggle> items = toggles.findAllByProject(projectId, enabled, environment,
                offset, size);
        long totalElements = toggles.countByProject(projectId, enabled, environment);
        return new TogglePage(items, totalElements);
    }
}
