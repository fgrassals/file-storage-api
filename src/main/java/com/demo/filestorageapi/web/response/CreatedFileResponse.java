package com.demo.filestorageapi.web.response;

import com.demo.filestorageapi.core.model.File;

import java.net.URI;

/**
 * Contains information about the file that was just created
 *
 * @author Franklin Grassals
 */
public class CreatedFileResponse extends BaseFileActionResponse {
    public CreatedFileResponse(File file, URI uri) {
        super("File created", file, uri);
    }
}
