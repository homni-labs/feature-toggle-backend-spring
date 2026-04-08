package com.homni.featuretoggle.domain.exception;

/**
 * Thrown when an operation is invalid for the current state of an entity
 * (e.g. enabling an already enabled toggle, disabling an already disabled user).
 */
public final class InvalidStateException extends DomainConflictException {

    /**
     * @param message descriptive message including entity identity and current state
     */
    public InvalidStateException(String message) {
        super(message);
    }
}
