package com.demo.filestorageapi.web.exception;

import com.demo.filestorageapi.core.exception.FileAlreadyExistsException;
import com.demo.filestorageapi.core.exception.FileContentTypeMismatch;
import com.demo.filestorageapi.core.exception.FileNotFoundException;
import com.demo.filestorageapi.core.exception.FileVersionNotFoundException;
import com.demo.filestorageapi.web.controller.FileStorageController;
import com.demo.filestorageapi.web.response.ErrorResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles exceptions for {@link FileStorageController}
 *
 * To keep things simple, this will override the default handler {@link ResponseEntityExceptionHandler} and any
 * unhandled exception will return a 500 code with a generic message
 *
 * @author Franklin Grassals
 */
@RestControllerAdvice
public class FileStorageExceptionHandler {
    protected final Log logger = LogFactory.getLog(getClass());

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    /**
     * Handles exceptions returning an {@link ErrorResponse} object with the exception information
     * @param e The exception thrown
     * @param request The web request
     * @return Response entity containing the error information
     */
    @ExceptionHandler({
            FileAlreadyExistsException.class,
            FileNotFoundException.class,
            FileVersionNotFoundException.class,
            TypeMismatchException.class,
            NoHandlerFoundException.class,
            FileContentTypeMismatch.class,
            ResponseStatusException.class,
            UploadedFileAccessException.class,
            MaxUploadSizeExceededException.class,
            Exception.class
    })
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        var message = e.getMessage();
        HttpStatus httpStatus;
        if (e instanceof FileNotFoundException || e instanceof FileVersionNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (e instanceof NoHandlerFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            message = "Resource not found";
        } else if (e instanceof FileAlreadyExistsException || e instanceof FileContentTypeMismatch) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e instanceof TypeMismatchException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Invalid arguments";
        } else if (e instanceof MaxUploadSizeExceededException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = String.format("Maximum upload size exceeded. Files cannot exceed %s", maxFileSize);
        } else if (e instanceof ResponseStatusException) {
            var ex = (ResponseStatusException) e;
            httpStatus = ex.getStatus();
            message = ex.getReason();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (e instanceof UploadedFileAccessException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "An error has occurred while processing your request.";
            logger.error(e.getMessage(), e);
        }

        return new ResponseEntity<>(new ErrorResponse(httpStatus, message), httpStatus);
    }
}
