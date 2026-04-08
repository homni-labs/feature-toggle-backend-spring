package com.homni.featuretoggle.domain.exception;

import com.homni.featuretoggle.domain.model.UserId;

/**
 * Thrown when a platform admin attempts to modify their own account
 * (change role, disable, etc.).
 */
public final class CannotModifySelfException extends DomainConflictException {

    /**
     * @param id the identity of the user attempting self-modification
     */
    public CannotModifySelfException(UserId id) {
        super("Cannot modify own account [id=%s]".formatted(id.value));
    }
}
