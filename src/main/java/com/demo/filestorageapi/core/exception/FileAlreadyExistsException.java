package com.demo.filestorageapi.core.exception;

/**
 * File already exists exception - thrown when trying to create a file that already exists
 *
 * @author Franklin Grassals
 */
public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
