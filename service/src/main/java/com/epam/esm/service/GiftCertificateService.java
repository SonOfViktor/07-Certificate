package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;

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
     * Find gift certificates for specified page.
     *
     * @param page number of page with gift certificates
     * @param size amount objects in one page
     * @return the page with gift certificates
     * @throws ResourceNotFoundException if page with gift certificates doesn't exist
     */
    Page<GiftCertificate> findAllCertificates(int page, int size);

    /**
     * Find gift certificates for specified page according to specified parameters.
     *
     * @param params the parameters determinant search for gift certificates
     * @param page number of page with gift certificates
     * @param size amount objects in one page
     * @return the list with gift certificates according to specified parameters
     * @throws ResourceNotFoundException if page with gift certificates doesn't exist
     */
    Page<GiftCertificate> findCertificatesWithParams(SelectQueryParameter params, int page, int size);

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
