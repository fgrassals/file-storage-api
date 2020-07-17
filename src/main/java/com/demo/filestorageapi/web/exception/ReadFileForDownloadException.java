package com.demo.filestorageapi.web.exception;

/**
 * Read file for download exception - thrown when trying to open and read the binary stream from the database
 * raises a {@link java.sql.SQLException}
 *
 * @author Franklin Grassals
 */
public class ReadFileForDownloadException extends RuntimeException {
    public ReadFileForDownloadException(Throwable cause) {
        super("An error occurred while reading file to download", cause);
    }
}
