package com.homni.featuretoggle.application.port.out;

import com.homni.featuretoggle.domain.model.ApiKey;
import com.homni.featuretoggle.domain.model.ApiKeyId;
import com.homni.featuretoggle.domain.model.ProjectId;
import com.homni.featuretoggle.domain.model.TokenHash;

import java.util.List;
import java.util.Optional;

/**
 * Output port for persisting API keys scoped to a project.
 *
 * <pre>{@code
 * Optional<ApiKey> key = apiKeys.findByTokenHash(hash);
 * }</pre>
 */
public interface ApiKeyRepositoryPort {

    /**
     * Saves an API key (insert or update).
     *
     * @param apiKey the API key to save
     *
     * <pre>{@code
     * apiKeys.save(newApiKey);
     * }</pre>
     */
    void save(ApiKey apiKey);

    /**
     * Finds an API key by its identity.
     *
     * @param id the API key identity
     * @return the API key if found, or empty
     *
     * <pre>{@code
     * Optional<ApiKey> key = apiKeys.findById(keyId);
     * }</pre>
     */
    Optional<ApiKey> findById(ApiKeyId id);

    /**
     * Finds an API key by the hash of its raw token.
     *
     * @param tokenHash the SHA-256 hash of the raw token
     * @return the API key if found, or empty
     *
     * <pre>{@code
     * Optional<ApiKey> key = apiKeys.findByTokenHash(TokenHash.of(rawToken));
     * }</pre>
     */
    Optional<ApiKey> findByTokenHash(TokenHash tokenHash);

    /**
     * Lists API keys belonging to a project with pagination.
     *
     * @param projectId the owning project identity
     * @param offset    the number of rows to skip
     * @param limit     the maximum number of rows to return
     * @return the matching API keys
     *
     * <pre>{@code
     * List<ApiKey> page = apiKeys.findAllByProject(projectId, 0, 20);
     * }</pre>
     */
    List<ApiKey> findAllByProject(ProjectId projectId, int offset, int limit);

    /**
     * Counts API keys belonging to a project.
     *
     * @param projectId the owning project identity
     * @return the total number of API keys in the project
     *
     * <pre>{@code
     * long total = apiKeys.countByProject(projectId);
     * }</pre>
     */
    long countByProject(ProjectId projectId);

    /**
     * Deletes an API key by its identity.
     *
     * @param id the API key identity
     *
     * <pre>{@code
     * apiKeys.deleteById(keyId);
     * }</pre>
     */
    void deleteById(ApiKeyId id);
}
