package com.demo.filestorageapi.web.response;

import com.demo.filestorageapi.core.model.UserFile;

import java.time.LocalDateTime;

/**
 * Contains information about the file
 *
 * @author Franklin Grassals
 */
public class FileResponse {
    private Long id;
    private String filename;
    private String contentType;
    private Long sizeInBytes;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public FileResponse() {}

    public FileResponse(UserFile userFile) {
        this.id = userFile.getId();
        this.filename = userFile.getFilename();
        this.contentType = userFile.getContentType();
        this.sizeInBytes = userFile.getSizeInBytes();
        this.createdAt = userFile.getCreatedAt();
        this.lastModifiedAt = userFile.getLastModifiedAt();
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

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
