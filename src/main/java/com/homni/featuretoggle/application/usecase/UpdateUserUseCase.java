package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.AppUserRepositoryPort;
import com.homni.featuretoggle.application.port.out.CallerPort;
import com.homni.featuretoggle.domain.exception.CannotModifySelfException;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.model.AppUser;
import com.homni.featuretoggle.domain.model.PlatformRole;
import com.homni.featuretoggle.domain.model.UserId;

/**
 * Updates a user's platform role and active status.
 * A platform admin cannot modify their own account.
 */
public final class UpdateUserUseCase {

    private final AppUserRepositoryPort users;
    private final CallerPort callerPort;

    /**
     * Creates an update-user use case.
     *
     * @param users      the user persistence port
     * @param callerPort provides the authenticated caller
     */
    public UpdateUserUseCase(AppUserRepositoryPort users, CallerPort callerPort) {
        this.users = users;
        this.callerPort = callerPort;
    }

    /**
     * Updates the platform role and/or active status of a user.
     *
     * @param targetId  the user to update
     * @param newRole   the new platform role, or {@code null} to leave unchanged
     * @param newActive the new active status, or {@code null} to leave unchanged
     * @return the updated user
     * @throws CannotModifySelfException if the caller tries to modify themselves
     * @throws EntityNotFoundException if the user does not exist
     *
     * <pre>{@code
     * AppUser updated = updateUser.execute(targetId, PlatformRole.PLATFORM_ADMIN, null);
     * }</pre>
     */
    public AppUser execute(UserId targetId, PlatformRole newRole, Boolean newActive) {
        UserId callerId = callerPort.get().id;
        if (targetId.equals(callerId)) {
            throw new CannotModifySelfException(callerId);
        }
        AppUser user = users.findById(targetId)
                .orElseThrow(() -> new EntityNotFoundException("User", targetId.value));
        applyRoleChange(user, newRole);
        applyActiveChange(user, newActive);
        users.save(user);
        return user;
    }

    private void applyRoleChange(AppUser user, PlatformRole newRole) {
        if (newRole == null) return;
        if (newRole == PlatformRole.PLATFORM_ADMIN) {
            user.promoteToPlatformAdmin();
        } else {
            user.demoteToUser();
        }
    }

    private void applyActiveChange(AppUser user, Boolean newActive) {
        if (newActive == null) return;
        if (newActive && !user.isActive()) {
            user.activate();
        } else if (!newActive && user.isActive()) {
            user.disable();
        }
    }
}
