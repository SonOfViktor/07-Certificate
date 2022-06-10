package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
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
     * @return the tag with generated int by database
     */
    Tag createTag(Tag tag);

    /**
     * Add specified tags to database.
     *
     * @param tags the set with tags to add
     * @return the set of tags with generated id by database
     */
    Set<Tag> addTags(Set<Tag> tags);

    /**
     * Read tag from database.
     *
     * @param offset initial offset in table with tags
     * @param size amount tags to extract from table with tags
     * @return the list with tags in database
     */
    List<Tag> readAllTag(int offset, int size);

    /**
     * Read tags related with specified gift certificate.
     *
     * @param certificateId id of a gift certificate
     * @return the set tags related to gift certificate with specified id
     */
    Set<Tag> readTagByCertificateId(int certificateId);

    /**
     * Read tags related to gift certificate with specified id
     *
     * @param certificateId id of a gift certificate
     * @param offset initial offset in table with tags
     * @param size amount tags to extract from table with tags
     * @return the list with tags in database related to gift certificate with specified id
     */
    Set<Tag> readTagByCertificateId(int certificateId, int offset, int size);

    /**
     * Read specified tag in database.
     *
     * @param id the id of specified tag
     * @return the optional with tag if it exists in database or optional empty otherwise
     */
    Optional<Tag> readTag(int id);

    /**
     * Read the most widely used tag of a user with the highest cost of all orders
     *
     * @return list of most widely used tag with the highest cost
     */
    List<Tag> readMostPopularHighestPriceTag();

    /**
     * Find out amount of entries in table with tags
     *
     * @param params parameters that points criteria to count tags
     * @return amount of entries in table with tags
     */
    int countTags(Map<String, Integer> params);

    /**
     * Delete tag from database.
     *
     * @param id the id of deleted tag
     * @return 1 if specified gift certificate was deleted, 0 otherwise
     */
    int deleteTag(int id);
}
