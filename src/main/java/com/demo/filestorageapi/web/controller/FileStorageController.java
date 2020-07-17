package com.demo.filestorageapi.web.controller;

import com.demo.filestorageapi.core.model.User;
import com.demo.filestorageapi.core.service.FileStorageService;
import com.demo.filestorageapi.web.exception.UploadedFileAccessException;
import com.demo.filestorageapi.web.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * File Storage REST API
 *
 * @author Franklin Grassals
 */
@RestController
@RequestMapping("/files")
public class FileStorageController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * API method to upload and create a new file
     * @param file Multipart parameter that contains the uploaded file
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return {@link CreatedFileResponse} describing
     */
    @PostMapping
    public ResponseEntity<BaseFileActionResponse> createFile(@RequestParam MultipartFile file, Authentication authentication) {
        validateUploadedFile(file);
        try {
            var storedFile = fileStorageService.storeFile(file.getOriginalFilename(), file.getInputStream(),
                    file.getSize(), file.getContentType(), getCurrentUser(authentication));

            var uri = buildURI(String.valueOf(storedFile.getId()));
            return ResponseEntity
                    .created(uri)
                    .body(new CreatedFileResponse(storedFile, uri));
        } catch (IOException e) {
            throw new UploadedFileAccessException(e);
        }
    }

    /**
     * API method to upload a file and add is as a new version
     * @param fileId The Id of the file
     * @param file Multipart parameter that contains the uploaded file
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return {@link UpdatedFileResponse}
     */
    @PatchMapping("/{fileId}")
    public ResponseEntity<BaseFileActionResponse> updateFile(@PathVariable Long fileId,
                                                             @RequestParam MultipartFile file,
                                                             Authentication authentication) {
        validateUploadedFile(file);
        try {
            var updatedFile = fileStorageService.updateFile(fileId, getCurrentUser(authentication),
                    file.getInputStream(), file.getSize(), file.getContentType());

            var uri = buildURI(String.valueOf(updatedFile.getId()));
            return ResponseEntity.ok(new UpdatedFileResponse(updatedFile, uri));
        } catch (IOException e) {
            throw new UploadedFileAccessException(e);
        }
    }

    /**
     * API method to delete a file
     * @param fileId The Id of the file to be deleted
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return {@link DeletedFileResponse}
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<BaseFileActionResponse> deleteFile(@PathVariable Long fileId, Authentication authentication) {
        var deletedFile = fileStorageService.deleteFile(fileId, getCurrentUser(authentication));

        var uri = buildURI(String.valueOf(deletedFile.getId()));
        return ResponseEntity.ok(new DeletedFileResponse(deletedFile, uri));
    }

    /**
     * API method to get a list of files owned by the authenticated user
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return List of {@link FileResponse} objects
     */
    @GetMapping
    public ResponseEntity<List<FileResponse>> getFiles(Authentication authentication) {
        // convert the UserFile Objects to FileResponse
        var files = fileStorageService.getFiles(getCurrentUser(authentication))
                .stream()
                .map(FileResponse::new)
                .collect(toList());

        return ResponseEntity.ok(files);
    }

    /**
     * API method to get a file
     * @param fileId The Id of the file
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return {@link FileResponse}
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFile(@PathVariable Long fileId, Authentication authentication)  {
        var file = fileStorageService.getFile(fileId, getCurrentUser(authentication));
        return ResponseEntity.ok(new FileResponse(file));
    }

    /**
     * API method to get a list of versions details of a file
     * @param fileId The Id of the file
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return {@link FileVersionResponse}
     */
    @GetMapping("/{fileId}/versions")
    public ResponseEntity<List<FileVersionResponse>> getFileVersions(@PathVariable Long fileId,
                                                                     Authentication authentication)  {
        var versions = fileStorageService
                .getFileVersions(fileId, getCurrentUser(authentication))
                .stream()
                .map(FileVersionResponse::new)
                .collect(toList());

        return ResponseEntity.ok(versions);
    }

    /**
     * API method to get a specific version of a file
     * @param fileId The Id of the file
     * @param versionUUID The UUID of the file version
     * @param authentication Spring security's authentication object (injected at runtime)
     * @return {@link FileResponse}
     */
    @GetMapping("/{fileId}/versions/{versionUUID}")
    public ResponseEntity<FileVersionResponse> getFileVersions(@PathVariable Long fileId,
                                                               @PathVariable UUID versionUUID,
                                                               Authentication authentication)  {
        var version = fileStorageService.getFileVersion(versionUUID, fileId, getCurrentUser(authentication));
        return ResponseEntity.ok(new FileVersionResponse(version));
    }

    // gets the current authenticated user
    private User getCurrentUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    // validates whether an upload file is valid
    private void validateUploadedFile(MultipartFile file) {
        if (file == null || file.getSize() == 0 || StringUtils.isEmpty(file.getOriginalFilename())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please upload a valid file");
        }
    }

    // returns the url for a given resource specified in path
    private URI buildURI(String path) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(String.format("/files/%s", path))
                .build().toUri();
    }
}
