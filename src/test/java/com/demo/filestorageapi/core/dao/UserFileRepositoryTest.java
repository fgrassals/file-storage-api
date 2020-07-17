package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.File;
import com.demo.filestorageapi.core.model.FileVersion;
import com.demo.filestorageapi.core.model.User;
import com.demo.filestorageapi.core.model.UserFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests to verify the interaction of the custom repository methods
 * of {@link UserFileRepository} and the database
 *
 * @author Franklin Grassals
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserFileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserRepository userRepository;

    File file;
    User user;
    UserFile userFile;
    int rank = 1;

    @BeforeEach
    void setUp() {
        //noinspection OptionalGetWithoutIsPresent
        this.user = userRepository.findByUsername("test").get();

        var filename = "FileRepositoryTest.class";
        var testFileStream = this.getClass().getResourceAsStream(filename);
        var file = new File(filename, "application/octet-stream", user);
        file.setVersions(Collections.singletonList(new FileVersion(testFileStream, 50, file)));
        this.file = fileRepository.save(file);
        this.userFile = userFileRepository.findAll().get(0);
    }

    @Test
    void findAllByUserIdAndRankOrderByFilenameAsc_correctParametersGiven_shouldReturnListOfUserFile() {
        assertEquals(Collections.singletonList(userFile),
                userFileRepository.findAllByUserIdAndRankOrderByFilenameAsc(user.getId(), rank));
    }

    @Test
    void findAllByUserIdAndRankOrderByFilenameAsc_nullUserIdGiven_shouldReturnEmptyList() {
        assertEquals(Collections.emptyList(),
                userFileRepository.findAllByUserIdAndRankOrderByFilenameAsc(null, rank));
    }

    @Test
    void findAllByUserIdAndRankOrderByFilenameAsc_nonExistingRankGiven_shouldReturnEmptyList() {
        assertEquals(Collections.emptyList(),
                userFileRepository.findAllByUserIdAndRankOrderByFilenameAsc(user.getId(), -1));
    }

    @Test
    void findByIdAndUserIdAndRank_correctParametersGiven_shouldReturnOptionalOfUserFile() {
        assertEquals(Optional.of(userFile), userFileRepository.findByIdAndUserIdAndRank(file.getId(), user.getId(), rank));
    }

    @Test
    void findByIdAndUserIdAndRank_nullFileIdGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), userFileRepository.findByIdAndUserIdAndRank(null, user.getId(), rank));
    }

    @Test
    void findByIdAndUserIdAndRank_nullUserIdGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), userFileRepository.findByIdAndUserIdAndRank(file.getId(), null, rank));
    }

    @Test
    void findByIdAndUserIdAndRank_nonExistingRankGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), userFileRepository.findByIdAndUserIdAndRank(file.getId(), user.getId(), -1));
    }
}