package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.specification.GiftCertificateSpecification;
import com.epam.esm.dto.FileImageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String IMAGE_DIR = "certificate" + File.separator;
    private static final String DASH = "-";
    private static final String COLON = ":";
    private final GiftCertificateDao giftCertificateDao;
    private final ImageService imageService;
    private final GiftCertificateSpecification spec;

    @Override
    public GiftCertificate addGiftCertificate(GiftCertificate certificate, FileImageDto image) {
        LocalDateTime createTime = LocalDateTime.now(Clock.systemDefaultZone());
        certificate.setCreateDate(createTime);
        certificate.setLastUpdateDate(createTime);

        String fileName = createFileName(certificate.getName(), createTime, image.fileExtension());
        imageService.upload(fileName, image.fileContent());

        certificate.setImage(fileName);

        return giftCertificateDao.save(certificate);
    }

    @Override
    public Page<GiftCertificate> findAllCertificates(Pageable pageable) {
        Page<GiftCertificate> certificates = giftCertificateDao.findAll(pageable);

        if(certificates.isEmpty()) {
            throw new ResourceNotFoundException("There are no certificates on " + pageable.getPageNumber() + " page");
        }

        return certificates;
    }

    @Override
    public Page<GiftCertificate> findCertificatesWithParams(GiftCertificateFilter filter, Pageable pageable) {
        Specification<GiftCertificate> specification = where(spec.hasTags(filter.tagNames()))
                .and(spec.hasCategory(filter.category()))
                .and(where(spec.hasName(filter.certificateName()))
                        .or(spec.hasDescription(filter.certificateDescription())));

        Page<GiftCertificate> certificates = giftCertificateDao.findAll(specification, pageable);

        if (certificates.getContent().isEmpty()) {
            throw new ResourceNotFoundException("There is no certificates with such filter " + filter +
                    " in database");
        }

        return certificates;
    }

    @Override
    public GiftCertificate findCertificateById(int certificateId) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(certificateId);

        return certificateOptional.orElseThrow(() ->
                new ResourceNotFoundException("There is no certificate with Id " + certificateId + " in database"));
    }

    @Override
    public GiftCertificate updateGiftCertificate(int id, GiftCertificate newCertificate,
                                                 Optional<FileImageDto> optionalImage) {

        GiftCertificate oldCertificate = giftCertificateDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate with id " + id +
                        " can't be updated. It was not found"));

        String newFileName = optionalImage
                .map(image -> updateCertificateImage(oldCertificate, newCertificate, image))
                .orElse("");
        newCertificate.setImage(newFileName);

        return fillCertificateNewValues(oldCertificate, newCertificate);
    }

    @Override
    public Optional<byte[]> findCertificateImage(int id) {
        return giftCertificateDao.findById(id)
                .map(GiftCertificate::getImage)
                .filter(StringUtils::isNotBlank)
                .flatMap(imageService::getImage);
    }

    @Override
    public void deleteCertificate(int id) {
        giftCertificateDao.findById(id)
                .ifPresent(certificate -> imageService.deleteImage(certificate.getImage()));

        giftCertificateDao.deleteById(id);
    }

    private GiftCertificate fillCertificateNewValues(GiftCertificate updatedCertificate, GiftCertificate newCertificate) {
        if (isNotBlank(newCertificate.getName())) {
            updatedCertificate.setName(newCertificate.getName().trim());
        }
        if (isNotBlank(newCertificate.getDescription())) {
            updatedCertificate.setDescription(newCertificate.getDescription().trim());
        }
        if (newCertificate.getPrice() != null) {
            updatedCertificate.setPrice(newCertificate.getPrice());
        }
        if (newCertificate.getDuration() > 0) {
            updatedCertificate.setDuration(newCertificate.getDuration());
        }
        if (newCertificate.getCategory() != null) {
            updatedCertificate.setCategory(newCertificate.getCategory());
        }
        if (isNotBlank(newCertificate.getImage())) {
            updatedCertificate.setImage(newCertificate.getImage());
        }

        updatedCertificate.setTags(newCertificate.getTags());
        updatedCertificate.setLastUpdateDate(LocalDateTime.now(Clock.systemDefaultZone())
                .truncatedTo(ChronoUnit.MILLIS));

        return updatedCertificate;
    }

    private String createFileName(String title, LocalDateTime dateTime, String extension) {
        return (IMAGE_DIR + title + dateTime.truncatedTo(ChronoUnit.MILLIS) + extension).replace(COLON, DASH);
    }

    private String updateCertificateImage(GiftCertificate oldCertificate, GiftCertificate newCertificate, FileImageDto image) {
        String newFileName = createFileName(newCertificate.getName(), oldCertificate.getCreateDate(), image.fileExtension());

        if (!newFileName.equals(oldCertificate.getImage())) {
            imageService.deleteImage(oldCertificate.getImage());
        }
        imageService.upload(newFileName, image.fileContent());

        return newFileName;
    }
}
