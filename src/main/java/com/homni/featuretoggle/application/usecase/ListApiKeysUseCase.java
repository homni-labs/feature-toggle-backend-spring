package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.ApiKeyRepositoryPort;
import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.domain.model.ApiKey;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.ProjectId;

import java.util.List;

/**
 * Lists API keys belonging to a project with pagination.
 */
public final class ListApiKeysUseCase {

    private final ApiKeyRepositoryPort apiKeys;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates a list-api-keys use case.
     *
     * @param apiKeys      the API key persistence port
     * @param callerAccess resolves the caller's project access
     */
    public ListApiKeysUseCase(ApiKeyRepositoryPort apiKeys,
                               CallerProjectAccessPort callerAccess) {
        this.apiKeys = apiKeys;
        this.callerAccess = callerAccess;
    }

    /**
     * Lists API keys for the specified project.
     *
     * @param projectId the owning project identity
     * @param page      the zero-based page number
     * @param size      the maximum number of items per page
     * @return a page of API keys
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks MANAGE_MEMBERS
     *
     * <pre>{@code
     * ApiKeyPage page = listApiKeys.execute(projectId, 0, 20);
     * }</pre>
     */
    public ApiKeyPage execute(ProjectId projectId, int page, int size) {
        callerAccess.resolve(projectId).ensure(Permission.MANAGE_MEMBERS);
        int offset = page * size;
        List<ApiKey> items = apiKeys.findAllByProject(projectId, offset, size);
        long totalElements = apiKeys.countByProject(projectId);
        return new ApiKeyPage(items, totalElements);
    }
}
