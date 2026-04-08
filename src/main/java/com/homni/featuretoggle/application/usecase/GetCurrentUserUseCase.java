package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.CallerPort;
import com.homni.featuretoggle.domain.model.AppUser;

/**
 * Returns the currently authenticated platform user.
 */
public final class GetCurrentUserUseCase {

    private final CallerPort callerPort;

    /**
     * Creates a get-current-user use case.
     *
     * @param callerPort provides the authenticated caller
     */
    public GetCurrentUserUseCase(CallerPort callerPort) {
        this.callerPort = callerPort;
    }

    /**
     * Returns the authenticated user for the current request.
     *
     * @return the authenticated platform user
     *
     * <pre>{@code
     * AppUser user = getCurrentUser.execute();
     * }</pre>
     */
    public AppUser execute() {
        return callerPort.get();
    }
}
