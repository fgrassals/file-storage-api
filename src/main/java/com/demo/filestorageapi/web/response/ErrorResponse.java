package com.demo.filestorageapi.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/**
 * Contains error information
 *
 * @author Franklin Grassals
 */
public class ErrorResponse {
    private int code;
    private String error;
    private String message;
    @JsonIgnore
    private HttpStatus httpStatus;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
