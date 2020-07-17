package com.demo.filestorageapi.core.model;

import org.hibernate.annotations.Type;
import org.hibernate.engine.jdbc.BlobProxy;

import javax.persistence.*;
import java.io.InputStream;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * FileVersion entity - Represents a version of a stored file
 *
 * @author Franklin Grassals
 */
@Entity
@Table(name = "file_versions")
public class FileVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob content;
    private long size; // size in bytes

    @Column(updatable = false)
    @Type(type = "uuid-char") // to store as string and not binary
    private UUID uuid;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private File file;

    public FileVersion() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Creates an instance using an InputStream pointing to the file content
     * @param contentStream The input stream pointing to the contents of the file
     * @param size File content's size in bytes
     * @param file the File instance this object will be associated with
     */
    public FileVersion(InputStream contentStream, long size, File file) {
        // generate the uuid first
        this();

        // using a blob proxy allows streaming the binary data to the db, avoiding OutOfMemory errors
        this.content = BlobProxy.generateProxy(contentStream, size);
        this.size = size;
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileVersion that = (FileVersion) o;
        return id.equals(that.id) &&
                uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }
}
