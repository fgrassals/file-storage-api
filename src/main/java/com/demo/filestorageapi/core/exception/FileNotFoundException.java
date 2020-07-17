package com.demo.filestorageapi.core.exception;

/**
 * File not found exception - thrown when a file can't be found in the data store
 *
 * @author Franklin Grassals
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
    }

    public FileNotFoundException(Long fileId) {
        super(String.format("File with id '%s' not found", fileId));
    }
}
