package com.demo.filestorageapi.web.response;

import com.demo.filestorageapi.core.model.File;

import java.net.URI;

/**
 * Information about an action performed on a file
 *
 * @author Franklin Grassals
 */
public abstract class BaseFileActionResponse {
    protected String action;
    protected Long id;
    protected String filename;
    protected String contentType;
    protected String uri;

    public BaseFileActionResponse(String action, File file, URI uri) {
        this.action = action;
        this.id = file.getId();
        this.filename = file.getFilename();
        this.contentType = file.getContentType();
        this.uri = uri.toString();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
