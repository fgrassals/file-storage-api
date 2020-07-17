package com.demo.filestorageapi.core.exception;

import java.util.UUID;

/**
 * File version not found exception - thrown when a file version can't be found in the data store
 *
 * @author Franklin Grassals
 */
public class FileVersionNotFoundException extends RuntimeException {
    public FileVersionNotFoundException() {
    }

    public FileVersionNotFoundException(UUID uuid, Long fileId) {
        super(String.format("File version '%s' for file id '%s' not found", uuid, fileId));
    }
}
