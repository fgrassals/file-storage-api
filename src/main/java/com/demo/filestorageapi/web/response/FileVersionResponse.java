package com.demo.filestorageapi.web.response;

import com.demo.filestorageapi.core.model.FileVersion;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Contains information about a file version
 *
 * @author Franklin Grassals
 */
public class FileVersionResponse {
    private UUID version;
    private String filename;
    private String contentType;
    private Long sizeInBytes;
    private LocalDateTime createdAt;

    public FileVersionResponse() {}

    public FileVersionResponse(FileVersion fileVersion) {
        this.version = fileVersion.getUuid();
        this.filename = fileVersion.getFile().getFilename();
        this.contentType = fileVersion.getFile().getContentType();
        this.sizeInBytes = fileVersion.getSize();
        this.createdAt = fileVersion.getCreatedAt();
    }

    public UUID getVersion() {
        return version;
    }

    public void setVersion(UUID version) {
        this.version = version;
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
}
