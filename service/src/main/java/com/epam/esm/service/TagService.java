package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.Set;

/**
 * The interface provide methods to control business logic related with tags
 */
public interface TagService {
    /**
     * Add specified tag.
     *
     * @param tag the tag to add
     * @return generated id for added tag
     */
    int addTag(Tag tag);

    /**
     * Add specified tags.
     *
     * @param tags set with tags to add
     * @return the int array where element 1 if the tag was added 0 otherwise
     */
    int[] addTags(Set<Tag> tags);

    /**
     * Find all tags.
     *
     * @return the set with all tags
     */
    Set<Tag> findAllTags();

    /**
     * Find tags related with specified gift certificate id.
     *
     * @param certificateId the gift certificate id
     * @return the set with tags related with specified gift certificate id
     */
    Set<Tag> findTagsByCertificateId(int certificateId);

    /**
     * Find tag with specified id.
     *
     * @param tagId the tag id
     * @return the tag with specified id
     * @throws ResourceNotFoundException if tag with specified id wasn't found
     */
    Tag findTagById(int tagId);

    /**
     * Delete tag with specified id.
     *
     * @param tagId the tag id
     * @return 1 if specified tag was deleted
     * @throws ResourceNotFoundException if tag with specified id wasn't found
     */
    int deleteTag(int tagId);
}
