package com.demo.filestorageapi.web.response;

import com.demo.filestorageapi.core.model.File;

import java.net.URI;

/**
 * Contains information about the file that was just Updated
 *
 * @author Franklin Grassals
 */
public class UpdatedFileResponse extends BaseFileActionResponse {
    public UpdatedFileResponse(File file, URI uri) {
        super("File updated", file, uri);
    }
}
