package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.File;
import com.demo.filestorageapi.core.model.FileVersion;
import com.demo.filestorageapi.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests to verify the interaction of the custom repository methods
 * of {@link FileVersionRepository} and the database
 *
 * @author Franklin Grassals
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class FileVersionRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileVersionRepository fileVersionRepository;

    @Autowired
    private UserRepository userRepository;

    File file;
    FileVersion fileVersion;
    User user;
    UUID uuid;

    @BeforeEach
    void setUp() {
        //noinspection OptionalGetWithoutIsPresent
        this.user = userRepository.findByUsername("test").get();

        var filename = "FileRepositoryTest.class";
        var testFileStream = this.getClass().getResourceAsStream(filename);
        var file = new File(filename, "application/octet-stream", user);
        file.setVersions(Collections.singletonList(new FileVersion(testFileStream, 50, file)));
        this.file = fileRepository.save(file);
        this.fileVersion = file.getVersions().get(0);
        this.uuid = this.fileVersion.getUuid();
    }

    @Test
    void findAllByFileIdAndFileUserOrderByCreatedAtDesc_correctParametersGiven_shouldReturnListOfFileVersion() {
        assertEquals(Collections.singletonList(fileVersion),
                fileVersionRepository.findAllByFileIdAndFileUserOrderByCreatedAtDesc(file.getId(), user));
    }

    @Test
    void findAllByFileIdAndFileUserOrderByCreatedAtDesc_nullFileIdGiven_shouldReturnEmptyList() {
        assertEquals(Collections.emptyList(),
                fileVersionRepository.findAllByFileIdAndFileUserOrderByCreatedAtDesc(null, user));
    }

    @Test
    void findAllByFileIdAndFileUserOrderByCreatedAtDesc_nullUserGiven_shouldReturnEmptyList() {
        assertEquals(Collections.emptyList(),
                fileVersionRepository.findAllByFileIdAndFileUserOrderByCreatedAtDesc(file.getId(), null));
    }

    @Test
    void findByUuidAndFileIdAndFileUser_correctParametersGiven_shouldReturnOptionalOfFileVersion() {
        assertEquals(Optional.of(fileVersion), fileVersionRepository.findByUuidAndFileIdAndFileUser(uuid, file.getId(), user));
    }

    @Test
    void findByUuidAndFileIdAndFileUser_nullUuidGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fileVersionRepository.findByUuidAndFileIdAndFileUser(null, file.getId(), user));
    }

    @Test
    void findByUuidAndFileIdAndFileUser_nullFileIdGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fileVersionRepository.findByUuidAndFileIdAndFileUser(uuid, null, user));
    }

    @Test
    void findByUuidAndFileIdAndFileUser_nullUserGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fileVersionRepository.findByUuidAndFileIdAndFileUser(uuid, file.getId(), null));
    }
}