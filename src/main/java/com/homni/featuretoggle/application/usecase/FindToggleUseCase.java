package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.FeatureToggleRepositoryPort;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.model.FeatureToggle;
import com.homni.featuretoggle.domain.model.FeatureToggleId;
import com.homni.featuretoggle.domain.model.Permission;

/**
 * Finds a single feature toggle by its identity.
 */
public final class FindToggleUseCase {

    private final FeatureToggleRepositoryPort toggles;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a find-toggle use case.
     *
     * @param toggles      the toggle persistence port
     * @param callerAccess resolves the caller's project access
     */
    public FindToggleUseCase(FeatureToggleRepositoryPort toggles,
                              CallerProjectAccessPort callerAccess) {
        this.toggles = toggles;
        this.callerAccess = callerAccess;
    }

    /**
     * Finds a feature toggle by its identity and verifies caller read access.
     *
     * @param id the toggle identity
     * @return the found feature toggle
     * @throws EntityNotFoundException if no toggle exists with the given identity
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks READ_TOGGLES
     *
     * <pre>{@code
     * FeatureToggle toggle = findToggle.execute(toggleId);
     * }</pre>
     */
    public FeatureToggle execute(FeatureToggleId id) {
        FeatureToggle toggle = toggles.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Toggle", id.value));
        callerAccess.resolve(toggle.projectId).ensure(Permission.READ_TOGGLES);
        return toggle;
    }
}
