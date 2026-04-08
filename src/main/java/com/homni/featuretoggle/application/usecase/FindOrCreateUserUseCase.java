package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.AppUserRepositoryPort;
import com.homni.featuretoggle.domain.exception.AlreadyExistsException;
import com.homni.featuretoggle.domain.model.AppUser;
import com.homni.featuretoggle.domain.model.PlatformRole;

import java.util.Optional;

/**
 * Finds an existing user by OIDC subject, or binds/creates one during first login.
 * Also bootstraps the default platform administrator.
 */
public final class FindOrCreateUserUseCase {

    private final AppUserRepositoryPort users;
    private final String defaultAdminEmail;

    /**
     * Creates a find-or-create-user use case.
     *
     * @param users             the user persistence port
     * @param defaultAdminEmail the email address of the default platform administrator
     */
    public FindOrCreateUserUseCase(AppUserRepositoryPort users, String defaultAdminEmail) {
        this.users = users;
        this.defaultAdminEmail = defaultAdminEmail;
    }

    /**
     * Resolves a user for the given OIDC subject: looks up by subject, attempts
     * to bind a pre-provisioned account by email, or creates a new user. If the
     * resolved user's email matches the default admin email and they hold the USER role,
     * they are promoted to PLATFORM_ADMIN.
     *
     * @param oidcSubject the OIDC subject identifier
     * @param email       the user's email address
     * @param name        the display name, may be {@code null}
     * @return the resolved or created user
     * @throws com.homni.featuretoggle.domain.exception.DomainValidationException if the email is invalid
     *
     * <pre>{@code
     * AppUser user = findOrCreateUser.execute("oidc|123", "user@example.com", "Alice");
     * }</pre>
     */
    public AppUser execute(String oidcSubject, String email, String name) {
        AppUser user = resolveUser(oidcSubject, email, name);
        bootstrapDefaultAdmin(user);
        return user;
    }

    private AppUser resolveUser(String oidcSubject, String email, String name) {
        try {
            return findBySubjectOrBindOrCreate(oidcSubject, email, name);
        } catch (AlreadyExistsException ignored) {
            return users.findByOidcSubject(oidcSubject)
                    .orElseGet(() -> users.findByEmail(email).orElseThrow(() -> ignored));
        }
    }

    private AppUser findBySubjectOrBindOrCreate(String oidcSubject, String email, String name) {
        Optional<AppUser> bySubject = users.findByOidcSubject(oidcSubject);
        if (bySubject.isPresent()) {
            return bySubject.get();
        }
        return tryBindOrCreate(oidcSubject, email, name);
    }

    private AppUser tryBindOrCreate(String oidcSubject, String email, String name) {
        Optional<AppUser> byEmail = users.findByEmail(email);
        if (byEmail.isPresent() && byEmail.get().canBindOidc()) {
            AppUser existing = byEmail.get();
            existing.bindOidcSubject(oidcSubject);
            users.save(existing);
            return existing;
        }
        AppUser newUser = new AppUser(oidcSubject, email, name);
        users.save(newUser);
        return newUser;
    }

    private void bootstrapDefaultAdmin(AppUser user) {
        if (isDefaultAdmin(user) && user.platformRole() == PlatformRole.USER) {
            user.promoteToPlatformAdmin();
            users.save(user);
        }
    }

    private boolean isDefaultAdmin(AppUser user) {
        return defaultAdminEmail != null
                && defaultAdminEmail.equalsIgnoreCase(user.email.value());
    }
}
