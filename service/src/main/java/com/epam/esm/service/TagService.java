package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

/**
 * The interface provide methods to control business logic related with tags
 */
public interface TagService {
    /**
     * Add specified tag.`
     *
     * @param tag the tag to add
     * @return the tag dto with generated id by database
     * @throws EntityExistsException if tag has already been created
     */
    Tag addTag(TagDto tag);

    /**
     * Add specified tags.
     *
     * @param tags set with tag dtos to add
     * @return the set of tags with generated id by database
     */
    Set<Tag> addTags(Set<TagDto> tags);

    /**
     * Find specified page with tags.
     *
     * @param pageable the pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return the page with tags
     * @throws ResourceNotFoundException if there are no tags on specified page
     */
    Page<Tag> findAllTags(Pageable pageable);

    /**
     * Find tags related to gift certificate with specified id.
     *
     * @param certificateId the gift certificate id
     * @return the set with tags related with specified gift certificate id
     */
    Set<Tag> findTagsByCertificateId(int certificateId);

    /**
     * Find page with tags related to gift certificate with specified id
     *
     * @param certificateId gift certificate id
     * @param pageable the pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return the page with tags related to gift certificate with specified id
     * @throws ResourceNotFoundException if there are no tags by specified certificate id or on specified page
     */
    Page<Tag> findTagsByCertificateId(int certificateId, Pageable pageable);

    /**
     * Find tag with specified id.
     *
     * @param tagId the tag id
     * @return the tag with specified id
     * @throws ResourceNotFoundException if tag with specified id wasn't found
     */
    Tag findTagById(int tagId);

    /**
     * Read the most widely used tag of a user with the highest cost of all orders
     *
     * @return list of most widely used tag with the highest cost
     */
    List<Tag> findMostPopularHighestPriceTag();

    /**
     * Delete tag with specified id.
     *
     * @param tagId the tag id
     * @throws EmptyResultDataAccessException if tag with specified id wasn't found
     */
    void deleteTag(int tagId);
}
