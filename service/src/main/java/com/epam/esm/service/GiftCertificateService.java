package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;

/**
 * The interface provide methods to control business logic related with gift certificates
 */
public interface GiftCertificateService {
    /**
     * Add specified gift certificate.
     *
     * @param certificate the certificate to add
     * @return the gift certificate with generated id by database
     */
    GiftCertificate addGiftCertificate(GiftCertificate certificate);

    /**
     * Find all gift certificates.
     *
     * @return the list with all gift certificates
     */
    List<GiftCertificate> findAllCertificates();

    /**
     * Find gift certificates according to specified parameters.
     *
     * @param params the parameters determinant search for gift certificates
     * @return the list with gift certificates according to specified parameters
     * @throws ResourceNotFoundException if gift certificates wasn't found
     */
    List<GiftCertificate> findCertificatesWithParams(SelectQueryParameter params);

    /**
     * Find gift certificate with specified id.
     *
     * @param certificateId the certificate id
     * @return the gift certificate
     * @throws ResourceNotFoundException if gift certificate wasn't found
     */
    GiftCertificate findCertificateById(int certificateId);

    /**
     * Update gift certificate with specified id.
     *
     * @param certificate the certificate with new data
     * @param id          the id of updated gift certificate
     * @return 1 if specified gift certificate was deleted
     * @throws ResourceNotFoundException if gift certificate wasn't found
     */
    GiftCertificate updateGiftCertificate(GiftCertificate certificate, int id);

    /**
     * Delete gift certificate with specified id.
     *
     * @param certificateId the certificate id
     * @return 1 if specified gift certificate was deleted
     * @throws ResourceNotFoundException if gift certificate wasn't found
     */
    int deleteCertificate(int certificateId);
}
