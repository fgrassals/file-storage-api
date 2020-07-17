package com.demo.filestorageapi.core.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * User file - Represents a file with version information
 *
 * @author Franklin Grassals
 */
@Entity
@Immutable
@Table(name = "user_files_view")
public class UserFile {
    @Id
    private Long id;
    private String filename;
    private String contentType;
    @Type(type = "uuid-char")
    private UUID version;
    // marking this field lazy allows us to fetch the potentially heavy blob only when needed
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob content;
    private Long sizeInBytes;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Long userId;
    @Column(name = "date_created_rank")
    private int rank;

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

    public UUID getVersion() {
        return version;
    }

    public void setVersion(UUID version) {
        this.version = version;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFile userFile = (UserFile) o;
        return id.equals(userFile.id) &&
                version.equals(userFile.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }
}
