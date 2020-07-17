package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests to verify the interaction of the custom repository methods
 * of {@link UserRepository} and the database
 *
 * @author Franklin Grassals
 */
@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        this.user = userRepository.findAll().get(0);
    }

    @Test
    void findByUsername_correctParametersGiven_shouldReturnOptionalOfUser() {
        assertEquals(Optional.of(user), userRepository.findByUsername("test"));
    }

    @Test
    void findByUsername_nonExistingUsernameGiven_shouldReturnEmptyOptional() {
        assertEquals(Optional.empty(), userRepository.findByUsername("idontexist"));
    }
}