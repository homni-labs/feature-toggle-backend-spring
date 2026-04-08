package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.AppUser;

/**
 * Provides the currently authenticated platform user.
 *
 * <pre>{@code
 * AppUser caller = callerPort.get();
 * }</pre>
 */
public interface CallerPort {

    /**
     * Returns the authenticated user for the current request.
     *
     * @return the authenticated platform user
     */
    AppUser get();
}
