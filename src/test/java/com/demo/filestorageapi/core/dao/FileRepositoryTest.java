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

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.Optional;

/**
 * Integration tests to verify the interaction of the custom repository methods
 * of {@link FileRepository} and the database
 *
 * @author Franklin Grassals
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    File file;
    User user;

    @BeforeEach
    void setUp() {
        //noinspection OptionalGetWithoutIsPresent
        this.user = userRepository.findByUsername("test").get();

        var filename = "FileRepositoryTest.class";
        var testFileStream = this.getClass().getResourceAsStream(filename);
        var file = new File(filename, "application/octet-stream", user);
        file.setVersions(Collections.singletonList(new FileVersion(testFileStream, 50, file)));
        this.file = fileRepository.save(file);
    }

    @Test
    void findByIdAndUser_correctParametersGiven_shouldReturnOptionalOfFile() {
        assertEquals(Optional.of(file), fileRepository.findByIdAndUser(file.getId(), user));
    }

    @Test
    void findByIdAndUser_nullFileIdGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fileRepository.findByIdAndUser(null, user));
    }

    @Test
    void findByIdAndUser_nullUserGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), fileRepository.findByIdAndUser(file.getId(), null));
    }

}