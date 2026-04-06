package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.Email;
import com.homni.featuretoggle.domain.model.UserId;

/**
 * Thrown when attempting to activate a user that is already active.
 */
public final class UserAlreadyActivatedException extends DomainConflictException {

    /**
     * @param id    the user identity
     * @param email the user email
     */
    public UserAlreadyActivatedException(UserId id, Email email) {
        super("User [id=%s, email=%s] is already active".formatted(id.value, email.value()));
    }
}
