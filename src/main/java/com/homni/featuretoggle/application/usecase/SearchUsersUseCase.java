package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.AppUserRepositoryPort;
import com.homni.featuretoggle.domain.model.AppUser;

import java.util.List;

/**
 * Searches platform users by email or name substring.
 * Used by project admins to find users for membership assignment.
 */
public final class SearchUsersUseCase {

    private static final int MAX_RESULTS = 20;

    private final AppUserRepositoryPort users;

    /**
     * Creates a search-users use case.
     *
     * @param users the user persistence port
     */
    public SearchUsersUseCase(AppUserRepositoryPort users) {
        this.users = users;
    }

    /**
     * Searches users whose email or name contains the query string (case-insensitive).
     *
     * @param query the search query
     * @return up to 20 matching users
     *
     * <pre>{@code
     * List<AppUser> results = searchUsers.execute("ivan");
     * }</pre>
     */
    public List<AppUser> execute(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return users.search(query.trim(), MAX_RESULTS);
    }
}
