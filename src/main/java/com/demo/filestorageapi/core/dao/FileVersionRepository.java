package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.FileVersion;
import com.demo.filestorageapi.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File Version Repository
 *
 * @author Franklin Grassals
 */
public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {
    /**
     * Finds a list of versions matching the query
     *
     * @param fileId Id of the file that owns the versions
     * @param user The the user who owns the files
     * @return List of {@link FileVersion} objects
     */
    List<FileVersion> findAllByFileIdAndFileUserOrderByCreatedAtDesc(Long fileId, User user);

    /**
     * Finds a file version matching the query
     *
     * @param uuid UUID of the file version
     * @param fileId Id of the file that owns the version
     * @param user The the user who owns the files
     * @return an {@link Optional} of {@link FileVersion}
     */
    Optional<FileVersion> findByUuidAndFileIdAndFileUser(UUID uuid, Long fileId, User user);
}
