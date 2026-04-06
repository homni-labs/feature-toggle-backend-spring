package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.Email;

/**
 * Thrown when attempting to create a user with an email that is already taken.
 */
public final class UserEmailAlreadyExistsException extends DomainConflictException {

    /**
     * @param email the conflicting email
     */
    public UserEmailAlreadyExistsException(Email email) {
        super("User with email [%s] already exists".formatted(email.value()));
    }
}
