package com.demo.filestorageapi.core.exception;

/**
 * File content type mismatch - thrown when the content type of an uploaded file
 * does not match the content type of the file to be updated
 *
 * @author Franklin Grassals
 */
public class FileContentTypeMismatch extends RuntimeException {
    public FileContentTypeMismatch(String message) {
        super(message);
    }
}
