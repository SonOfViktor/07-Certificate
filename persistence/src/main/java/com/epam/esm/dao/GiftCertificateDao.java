package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import java.util.List;
import java.util.Optional;

/**
 * The interface provided methods to control data in database table related with gift certificates.
 */
public interface GiftCertificateDao {
    /**
     * Add gift certificate to database.
     *
     * @param certificate the certificate to add
     * @return the gift certificate with generated id by database
     */
    GiftCertificate createGiftCertificate(GiftCertificate certificate);

    /**
     * Read all certificate from database.
     *
     * @param offset initial offset in table with certificates
     * @param size amount certificates to extract from table with certificates
     * @return the list of all gift certificates in the database
     */
    List<GiftCertificate> readAllCertificate(int offset, int size);

    /**
     * Read gift certificates based on given sql script.
     *
     * @param params  the object with parameters to filter gift certificates
     * @param offset initial offset in table with certificates
     * @param size amount certificates to extract from table with certificates
     * @return the list of specific gift certificates by sql script
     */
    List<GiftCertificate> readGiftCertificateWithParam(SelectQueryParameter params, int offset, int size);

    /**
     * Read certain gift certificate.
     *
     * @param id the id of searched gift certificate
     * @return the optional with gift certificate if it exists in database or optional empty otherwise
     */
    Optional<GiftCertificate> readGiftCertificate(int id);

    /**
     * Find out amount of entries in table with gift certificates
     *
     * @param params the object with parameters to filter gift certificates
     * @return amount of entries in table with gift certificates
     */
    int countGiftCertificate(SelectQueryParameter params);

    /**
     * Update gift certificate with specified id.
     *
     * @param certificate the certificate with data for updating old entry in database
     * @return the optional with updated gift certificate if it exists in database or optional empty otherwise
     */
    Optional<GiftCertificate> updateGiftCertificate(GiftCertificate certificate);

    /**
     * Delete gift certificate with specified id.
     *
     * @param id the id of deleted gift certificate
     * @return 1 if specified gift certificate was deleted, 0 otherwise
     */
    int deleteGiftCertificate(int id);
}
