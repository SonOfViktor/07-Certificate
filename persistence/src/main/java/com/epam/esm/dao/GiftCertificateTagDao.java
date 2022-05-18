package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Set;

/**
 * The interface provided methods to control data in join table related with gift certificates and tags.
 */
public interface GiftCertificateTagDao {
    /**
     * Add entries with gift certificate and tag id.
     *
     * @param certificateId the gift certificate id
     * @param tags          the tags related with specified certificate
     * @return the int array where element 1 if the entry was added 0 otherwise
     */
    int[] createGiftCertificateTagEntries(int certificateId, Set<Tag> tags);
}
