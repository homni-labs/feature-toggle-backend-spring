package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.AppUserRepositoryPort;

/**
 * Lists all platform users with pagination.
 */
public final class ListUsersUseCase {

    private final AppUserRepositoryPort users;

    /**
     * Creates a list-users use case.
     *
     * @param users the user persistence port
     */
    public ListUsersUseCase(AppUserRepositoryPort users) {
        this.users = users;
    }

    /**
     * Returns a paginated list of all platform users.
     *
     * @param page the zero-based page number
     * @param size the page size
     * @return a page of users with total count
     *
     * <pre>{@code
     * UserPage page = listUsers.execute(0, 20);
     * }</pre>
     */
    public UserPage execute(int page, int size) {
        int offset = page * size;
        return new UserPage(users.findAll(offset, size), users.count());
    }
}
