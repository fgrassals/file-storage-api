package com.demo.filestorageapi.web.response;

import com.demo.filestorageapi.core.model.File;

import java.net.URI;

/**
 * Contains information about the file that was just deleted
 *
 * @author Franklin Grassals
 */
public class DeletedFileResponse extends BaseFileActionResponse {
    public DeletedFileResponse(File file, URI uri) {
        super("File deleted", file, uri);
    }
}
