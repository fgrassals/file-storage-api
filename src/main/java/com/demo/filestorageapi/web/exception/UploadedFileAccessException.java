package com.demo.filestorageapi.web.exception;

/**
 * Uploaded file access exception - thrown when trying to read an uploaded file raises an {@link java.io.IOException}
 *
 * @author Franklin Grassals
 */
public class UploadedFileAccessException extends RuntimeException {
    public UploadedFileAccessException(Throwable cause) {
        super("An error occurred while reading the uploaded file", cause);
    }
}
