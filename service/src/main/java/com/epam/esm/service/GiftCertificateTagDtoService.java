package com.epam.esm.service;

import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;

/**
 * The interface provide methods to control business logic related with gift certificates together with tags.
 */
public interface GiftCertificateTagDtoService {
    /**
     * Add specified gift certificate with tags.
     *
     * @param certificateTagsDto the certificate tags dto
     * @return generated id for added certificate
     */
    int addGiftCertificateTagDto(CertificateTagsDto certificateTagsDto);

    /**
     * Find all gift certificate with tags.
     *
     * @return the list certificates with tags
     */
    List<CertificateTagsDto> findAllGiftCertificateTagDto();

    /**
     * Find gift certificates with tags according to specified parameters.
     *
     * @param params the parameters determinant search for gift certificates
     * @return the list certificates with tags
     * @throws ResourceNotFoundException if gift certificates wasn't found
     */
    List<CertificateTagsDto> findGiftCertificateTagDtoByParam(SelectQueryParameter params);

    /**
     * Find gift certificate and tags with specified gift certificate id.
     *
     * @param certificateId the gift certificate id
     * @return the certificate with tags
     * @throws ResourceNotFoundException if gift certificate with specified id wasn't found
     */
    CertificateTagsDto findGiftCertificateTagDto(int certificateId);

    /**
     * Update gift certificate with specified id and add new tags if they are.
     *
     * @param certificateTagsDto certificate with tags
     * @param id                 the gift certificate id
     * @return the int array where element 1 if the entry was added 0 otherwise
     * @throws ResourceNotFoundException if gift certificate with specified id wasn't found
     */
    int[] updateGiftCertificateTagDto(CertificateTagsDto certificateTagsDto, int id);
}
