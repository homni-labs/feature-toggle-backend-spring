package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.Email;
import com.homni.featuretoggle.domain.model.UserId;

/**
 * Thrown when attempting to bind an OIDC subject to a user that already has one.
 */
public final class OidcSubjectAlreadyBoundException extends DomainConflictException {

    /**
     * @param id    the user identity
     * @param email the user email
     */
    public OidcSubjectAlreadyBoundException(UserId id, Email email) {
        super("User [id=%s, email=%s] already has an OIDC subject bound".formatted(id.value, email.value()));
    }
}
