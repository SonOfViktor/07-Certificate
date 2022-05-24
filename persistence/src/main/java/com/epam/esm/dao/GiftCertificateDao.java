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
     * @return generated int id of added certificate
     */
    int createGiftCertificate(GiftCertificate certificate);

    /**
     * Read all certificate from database.
     *
     * @return the list of all gift certificates in the database
     */
    List<GiftCertificate> readAllCertificate();

    /**
     * Read gift certificates based on given sql script.
     *
     * @param parameter  the object with parameters to filter gift certificates
     * @return the list of specific gift certificates by sql script
     */
    List<GiftCertificate> readGiftCertificateWithParam(SelectQueryParameter parameter);

    /**
     * Read certain gift certificate.
     *
     * @param id the id of searched gift certificate
     * @return the optional with gift certificate if it exists in database or optional empty otherwise
     */
    Optional<GiftCertificate> readGiftCertificate(int id);

    /**
     * Update gift certificate with specified id.
     *
     * @param certificate the certificate with data for updating old entry in database
     * @return the int id of updated gift certificate
     */
    GiftCertificate updateGiftCertificate(GiftCertificate certificate);

    /**
     * Delete gift certificate with specified id.
     *
     * @param id the id of deleted gift certificate
     * @return 1 if specified gift certificate was deleted, 0 otherwise
     */
    int deleteGiftCertificate(int id);
}
