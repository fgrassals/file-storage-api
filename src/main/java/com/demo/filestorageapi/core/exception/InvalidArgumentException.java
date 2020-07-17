package com.demo.filestorageapi.core.exception;

/**
 * Invalid Argument Exception
 *
 * @author Franklin Grassals
 */
public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
