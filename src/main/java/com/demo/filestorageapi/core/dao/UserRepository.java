package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Users repository
 *
 * @author Franklin Grassals
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     *  Retrieves a user by it's username
     *
     * @param username the user's username
     * @return the user with the given username or {@literal Optional#empty()} if none found.
     */
    Optional<User> findByUsername(String username);
}
