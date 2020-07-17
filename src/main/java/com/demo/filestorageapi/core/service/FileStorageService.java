package com.demo.filestorageapi.core.service;

import com.demo.filestorageapi.core.dao.FileRepository;
import com.demo.filestorageapi.core.dao.FileVersionRepository;
import com.demo.filestorageapi.core.dao.UserFileRepository;
import com.demo.filestorageapi.core.exception.*;
import com.demo.filestorageapi.core.model.File;
import com.demo.filestorageapi.core.model.FileVersion;
import com.demo.filestorageapi.core.model.User;
import com.demo.filestorageapi.core.model.UserFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * File Storage Service - handles all the file storage operations
 *
 * @author Franklin Grassals
 */
@Service
public class FileStorageService {
    private final FileRepository fileRepository;
    private final FileVersionRepository fileVersionRepository;
    private final UserFileRepository userFileRepository;

    @Autowired
    public FileStorageService(FileRepository fileRepository,
                              FileVersionRepository fileVersionRepository,
                              UserFileRepository userFileRepository) {
        this.fileRepository = fileRepository;
        this.fileVersionRepository = fileVersionRepository;
        this.userFileRepository = userFileRepository;
    }

    /**
     * Stores a file in the database
     *
     * @param filename Filename of the file to store
     * @param stream The input stream pointing to the contents of the file
     * @param size File content's size in bytes
     * @param contentType Content type of the file
     * @param user The owner of the file
     * @return The created {@link File}
     */
    public File storeFile(String filename, InputStream stream, long size, String contentType, User user) {
        if (StringUtils.isEmpty(filename)) {
            throw new InvalidArgumentException("The filename cannot be empty");
        }
        if (user == null) {
            throw new InvalidArgumentException("The user cannot be null");
        }

        try {
            var file = new File(filename, getContentTypeOrDefault(contentType), user);
            file.setVersions(Collections.singletonList(new FileVersion(stream, size, file)));

            return fileRepository.save(file);
        } catch (DataIntegrityViolationException e) {
            throw new FileAlreadyExistsException(String.format("The filename '%s' already exists", filename));
        }
    }

    /**
     * Updates a file, adding a version
     *
     * @param fileId Id of the file to be updated
     * @param user The owner of the file
     * @param stream The input stream pointing to the contents of the file
     * @param size File content's size in bytes
     * @param contentType The content type of the file
     * @return The updated {@link File}
     */
    public File updateFile(Long fileId, User user, InputStream stream, long size, String contentType) {
        var file = fileRepository
                .findByIdAndUser(fileId, user)
                .orElseThrow(() -> new FileNotFoundException(fileId));

        if (!file.getContentType().equalsIgnoreCase(contentType)) {
            throw new FileContentTypeMismatch(
                    String.format("The uploaded file's content type '%s' does not match. It must be '%s'",
                            contentType, file.getContentType())
            );
        }

        fileVersionRepository.save(new FileVersion(stream, size, file));

        return file;
    }

    /**
     * Deletes a file
     *
     * @param fileId Id of the file to be deleted
     * @param user The owner of the file
     * @return The deleted {@link File}
     */
    public File deleteFile(Long fileId, User user) {
        var file = fileRepository
                .findByIdAndUser(fileId, user)
                .orElseThrow(() -> new FileNotFoundException(fileId));

        fileRepository.delete(file);

        return file;
    }

    /**
     * Gets a list of files containing only the newest version information ordered by filename
     *
     * @param user The user who owns the files
     * @return List of {@link UserFile} objects
     */
    public List<UserFile> getFiles(User user) {
        return userFileRepository.findAllByUserIdAndRankOrderByFilenameAsc(getIdOrZero(user), 1);
    }

    /**
     * Gets a file containing only the newest version information
     *
     * @param fileId Id of the file
     * @param user The user who owns the file
     * @return {@link UserFile} object
     */
    public UserFile getFile(Long fileId, User user) {
        return userFileRepository
                .findByIdAndUserIdAndRank(fileId, getIdOrZero(user), 1)
                .orElseThrow(() -> new FileNotFoundException(fileId));
    }

    /**
     * Gets a list of file versions
     *
     * @param fileId Id of the file
     * @param user The user who owns the files
     * @return List of {@link FileVersion} objects
     */
    public List<FileVersion> getFileVersions(Long fileId, User user) {
        return fileVersionRepository.findAllByFileIdAndFileUserOrderByCreatedAtDesc(fileId, user);
    }

    /**
     * Gets a specific file version
     *
     * @param uuid UUID of the file version
     * @param fileId Id of the file that owns the version
     * @param user The user who owns the file
     * @return {@link FileVersion} object
     */
    public FileVersion getFileVersion(UUID uuid, Long fileId, User user) {
        return fileVersionRepository
                .findByUuidAndFileIdAndFileUser(uuid, fileId, user)
                .orElseThrow(() -> new FileVersionNotFoundException(uuid, fileId));
    }

    // gets the generic "binary" file content type if none was provided
    private String getContentTypeOrDefault(String contentType) {
        return contentType == null ? "application/octet-stream" : contentType;
    }

    // gets the user id or 0 if null
    private Long getIdOrZero(User user) {
        return user != null ? user.getId() : 0L;
    }

}
