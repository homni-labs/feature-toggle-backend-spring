package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.Email;
import com.homni.featuretoggle.domain.model.UserId;

/**
 * Thrown when attempting to disable a user that is already disabled.
 */
public final class UserAlreadyDisabledException extends DomainConflictException {

    /**
     * @param id    the user identity
     * @param email the user email
     */
    public UserAlreadyDisabledException(UserId id, Email email) {
        super("User [id=%s, email=%s] is already disabled".formatted(id.value, email.value()));
    }
}
