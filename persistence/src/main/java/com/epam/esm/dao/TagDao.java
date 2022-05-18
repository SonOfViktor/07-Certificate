package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

/**
 * The interface provided methods to control data in database table related with tags.
 */
public interface TagDao {
    /**
     * Add specified tag to database.
     *
     * @param tag the tog to add
     * @return generated int id of added tag
     */
    int createTag(Tag tag);

    /**
     * Add specified tags to database.
     *
     * @param tags the set with tags to add
     * @return the int array where element 1 if the entry was added 0 otherwise
     */
    int[] addTags(Set<Tag> tags);

    /**
     * Read all tag from database.
     *
     * @return the set with all tags in database
     */
    Set<Tag> readAllTag();

    /**
     * Read all tag related with specified gift certificate.
     *
     * @param certificateId the gift certificate id
     * @return the set tags related with specified gift certificate
     */
    Set<Tag> readAllTagByCertificateId(int certificateId);

    /**
     * Read specified tag in database.
     *
     * @param id the id of specified tag
     * @return the optional with tag if it exists in database or optional empty otherwise
     */
    Optional<Tag> readTag(int id);

    /**
     * Delete tag from database.
     *
     * @param id the id of deleted tag
     * @return 1 if specified gift certificate was deleted, 0 otherwise
     */
    int deleteTag(int id);
}
