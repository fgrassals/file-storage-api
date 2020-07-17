package com.demo.filestorageapi.core.dao;

import com.demo.filestorageapi.core.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User File Repository
 *
 * @author Franklin Grassals
 */
public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    /**
     * Gets a list of {@link UserFile} objects matching the query
     *
     * @param userId The id of the user who owns the files
     * @param rank The rank of the files go get.
     *            A rank of 1 will only get the newest version of the given file.
     *            A rank of 2 will be the second newest version of a given file, and so on.
     *
     * @return A list of {@link UserFile} matching the query
     */
    List<UserFile> findAllByUserIdAndRankOrderByFilenameAsc(Long userId, int rank);

    /**
     * Gets a {@link UserFile} object matching the query
     *
     * @param id The id of the fie
     * @param userId The id of the user who owns the file
     * @param rank The rank of the files go get.
     *            A rank of 1 will only get the newest version of the given file.
     *            A rank of 2 will be the second newest version of a given file, and so on.
     *
     * @return An {@link Optional} with {@link UserFile} matching the query
     */
    Optional<UserFile> findByIdAndUserIdAndRank(Long id, Long userId, int rank);
}
