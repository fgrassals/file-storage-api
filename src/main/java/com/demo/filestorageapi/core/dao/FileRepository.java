package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.File;
import com.demo.filestorageapi.core.model.FileVersion;
import com.demo.filestorageapi.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * File Version Repository
 *
 * @author Franklin Grassals
 */
public interface FileRepository extends JpaRepository<File, Long> {
    /**
     * Finds a {@link File} object matching the query
     *
     * @param id Id of the file
     * @param user The the user who owns the files
     * @return an {@link Optional} of {@link File}
     */
    Optional<File> findByIdAndUser(Long id, User user);
}
