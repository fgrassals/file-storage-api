package com.demo.filestorageapi.core.service;

import com.demo.filestorageapi.core.dao.FileRepository;
import com.demo.filestorageapi.core.dao.FileVersionRepository;
import com.demo.filestorageapi.core.dao.UserFileRepository;
import com.demo.filestorageapi.core.exception.*;
import com.demo.filestorageapi.core.model.File;
import com.demo.filestorageapi.core.model.FileVersion;
import com.demo.filestorageapi.core.model.User;
import com.demo.filestorageapi.core.model.UserFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link FileStorageService}
 *
 * @author Franklin Grassals
 */
@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock
    FileRepository fileRepositoryMock;

    @Mock
    FileVersionRepository fileVersionRepositoryMock;

    @Mock
    UserFileRepository userFileRepositoryMock;

    @InjectMocks
    FileStorageService fileStorageService;

    User user;
    File file;
    UserFile userFile;
    UUID uuid;
    FileVersion fileVersion;
    List<UserFile> userFileList;
    List<FileVersion> fileVersionList;

    static final String CONTENT_TYPE = "text/plain";
    static final String FILENAME = "test.txt";

    @BeforeEach
    void setUp() {
        this.user = new User(1L, "test", "test");
        this.file = new File(FILENAME, CONTENT_TYPE, user);
        this.file.setId(1L);

        this.uuid = UUID.randomUUID();

        this.userFile = new UserFile();
        this.userFile.setId(1L);
        this.userFile.setContentType(CONTENT_TYPE);
        this.userFile.setCreatedAt(LocalDateTime.now());
        this.userFile.setFilename(FILENAME);
        this.userFile.setVersion(this.uuid);
        this.userFile.setRank(1);
        this.userFile.setSizeInBytes(0L);
        this.userFile.setUserId(1L);
        this.userFile.setLastModifiedAt(LocalDateTime.now());

        this.fileVersion = new FileVersion(InputStream.nullInputStream(), 0, file);
        this.fileVersion.setUuid(this.uuid);

        this.userFileList = Collections.singletonList(userFile);
        this.fileVersionList = Collections.singletonList(fileVersion);
    }

    @Test
    void storeFile_correctParametersGiven_shouldReturnFile() {
        when(fileRepositoryMock.save(Mockito.any(File.class))).thenReturn(file);
        assertEquals(file, fileStorageService.storeFile(FILENAME, InputStream.nullInputStream(), 0, CONTENT_TYPE, user));
    }

    @Test
    void storeFile_nullFilenameGiven_shouldThrowInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class,
                () -> fileStorageService.storeFile(null, InputStream.nullInputStream(), 0, CONTENT_TYPE, user));
    }

    @Test
    void storeFile_nullUserGiven_shouldThrowInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class,
                () -> fileStorageService.storeFile(FILENAME, InputStream.nullInputStream(), 0, CONTENT_TYPE, null));
    }

    @Test
    void storeFile_existingFilenameGiven_shouldThrowFileAlreadyExistsException() {
        when(fileRepositoryMock.save(Mockito.any(File.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(FileAlreadyExistsException.class,
                () ->  fileStorageService.storeFile(FILENAME, InputStream.nullInputStream(), 0, CONTENT_TYPE, user));
    }

    @Test
    void updateFile_correctParametersGiven_shouldReturnFile() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), user)).thenReturn(Optional.of(file));
        when(fileVersionRepositoryMock.save(Mockito.any(FileVersion.class))).thenReturn(new FileVersion());
        assertEquals(file, fileStorageService.updateFile(file.getId(), user, InputStream.nullInputStream(), 0, CONTENT_TYPE));
    }

    @Test
    void updateFile_invalidFileIdGiven_shouldThrowFileNotFoundException() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), user)).thenReturn(Optional.empty());
        assertThrows(FileNotFoundException.class,
                () ->  fileStorageService.updateFile(file.getId(), user, InputStream.nullInputStream(), 0, CONTENT_TYPE));
    }

    @Test
    void updateFile_nullUserGiven_shouldThrowFileNotFoundException() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), null)).thenReturn(Optional.empty());
        assertThrows(FileNotFoundException.class,
                () ->  fileStorageService.updateFile(file.getId(), null, InputStream.nullInputStream(), 0, CONTENT_TYPE));
    }

    @Test
    void updateFile_differentContentTypeGiven_shouldThrowFileContentTypeMismatch() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), user)).thenReturn(Optional.of(file));
        assertThrows(FileContentTypeMismatch.class,
                () ->  fileStorageService.updateFile(file.getId(), user, InputStream.nullInputStream(), 0, "application/octet-stream"));
    }

    @Test
    void deleteFile_correctParametersGiven_shouldReturnFile() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), user)).thenReturn(Optional.of(file));
        doAnswer(a -> null).when(fileRepositoryMock).delete(file);
        assertEquals(file, fileStorageService.deleteFile(file.getId(), user));
    }

    @Test
    void deleteFile_invalidFileIdGiven_shouldThrowFileNotFoundException() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), user)).thenReturn(Optional.empty());
        assertThrows(FileNotFoundException.class,
                () ->  fileStorageService.deleteFile(file.getId(), user));
    }

    @Test
    void deleteFile_nullUserGiven_shouldThrowFileNotFoundException() {
        when(fileRepositoryMock.findByIdAndUser(file.getId(), null)).thenReturn(Optional.empty());
        assertThrows(FileNotFoundException.class,
                () ->  fileStorageService.deleteFile(file.getId(), null));
    }

    @Test
    void getFiles_correctParametersGiven_shouldReturnListOfUserFile() {
        when(userFileRepositoryMock.findAllByUserIdAndRankOrderByFilenameAsc(user.getId(), 1)).thenReturn(userFileList);
        assertEquals(userFileList, fileStorageService.getFiles(user));
    }

    @Test
    void getFiles_nullUserGiven_shouldReturnEmptyListOfUserFile() {
        when(userFileRepositoryMock.findAllByUserIdAndRankOrderByFilenameAsc(0L, 1)).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), fileStorageService.getFiles(null));
    }

    @Test
    void getFile_correctParametersGiven_shouldReturnUserFile() {
        when(userFileRepositoryMock.findByIdAndUserIdAndRank(file.getId(), user.getId(), 1)).thenReturn(Optional.of(userFile));
        assertEquals(userFile, fileStorageService.getFile(file.getId(), user));
    }

    @Test
    void getFile_nullFileIdGiven_shouldThrowFileNotFoundException() {
        when(userFileRepositoryMock.findByIdAndUserIdAndRank(null, user.getId(), 1)).thenReturn(Optional.empty());
        assertThrows(FileNotFoundException.class, () -> fileStorageService.getFile(null, user));
    }

    @Test
    void getFile_nullUserGiven_shouldThrowFileNotFoundException() {
        when(userFileRepositoryMock.findByIdAndUserIdAndRank(file.getId(), 0L, 1)).thenReturn(Optional.empty());
        assertThrows(FileNotFoundException.class, () -> fileStorageService.getFile(file.getId(), null));
    }

    @Test
    void getFileVersions_correctParametersGiven_shouldReturnListOfFileVersion() {
        when(fileVersionRepositoryMock.findAllByFileIdAndFileUserOrderByCreatedAtDesc(file.getId(), user)).thenReturn(fileVersionList);
        assertEquals(fileVersionList, fileStorageService.getFileVersions(file.getId(), user));
    }

    @Test
    void getFileVersions_nullUserGiven_shouldReturnEmptyListOfFileVersion() {
        when(fileVersionRepositoryMock.findAllByFileIdAndFileUserOrderByCreatedAtDesc(file.getId(), null)).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), fileStorageService.getFileVersions(file.getId(), null));
    }

    @Test
    void getFileVersions_nullFileIdGiven_shouldReturnEmptyListOfFileVersion() {
        when(fileVersionRepositoryMock.findAllByFileIdAndFileUserOrderByCreatedAtDesc(null, user)).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), fileStorageService.getFileVersions(null, user));
    }

    @Test
    void getFileVersion_correctParametersGiven_shouldReturnFileVersion() {
        when(fileVersionRepositoryMock.findByUuidAndFileIdAndFileUser(uuid, file.getId(), user)).thenReturn(Optional.of(fileVersion));
        assertEquals(fileVersion, fileStorageService.getFileVersion(uuid, file.getId(), user));
    }

    @Test
    void getFileVersion_nullUuidGiven_shouldThrowFileVersionNotFoundException() {
        when(fileVersionRepositoryMock.findByUuidAndFileIdAndFileUser(null, file.getId(), user)).thenReturn(Optional.empty());
        assertThrows(FileVersionNotFoundException.class, () -> fileStorageService.getFileVersion(null, file.getId(), user));
    }

    @Test
    void getFileVersion_nullFileIdGiven_shouldThrowFileVersionNotFoundException() {
        when(fileVersionRepositoryMock.findByUuidAndFileIdAndFileUser(uuid, null, user)).thenReturn(Optional.empty());
        assertThrows(FileVersionNotFoundException.class, () -> fileStorageService.getFileVersion(uuid, null, user));
    }

    @Test
    void getFileVersion_nullUserGiven_shouldThrowFileVersionNotFoundException() {
        when(fileVersionRepositoryMock.findByUuidAndFileIdAndFileUser(uuid, file.getId(), null)).thenReturn(Optional.empty());
        assertThrows(FileVersionNotFoundException.class, () -> fileStorageService.getFileVersion(uuid, file.getId(), null));
    }
}