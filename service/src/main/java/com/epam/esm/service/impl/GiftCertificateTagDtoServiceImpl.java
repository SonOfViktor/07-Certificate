package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagDtoService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class GiftCertificateTagDtoServiceImpl implements GiftCertificateTagDtoService {
    private final GiftCertificateService giftCertificateService;
    private final TagService tagService;

    @Autowired
    public GiftCertificateTagDtoServiceImpl(GiftCertificateService giftCertificateService, TagService tagService) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
    }

    @Override
    public CertificateTagsDto addGiftCertificateTagDto(CertificateTagsDto certificateTagsDto) {
        GiftCertificate certificate = certificateTagsDto.certificate();
        Set<Tag> tags = certificateTagsDto.tags();
        certificate.setTags(tags);

        Set<Tag> createdTags = tagService.addTags(tags);
        GiftCertificate createdCertificate = giftCertificateService.addGiftCertificate(certificate);

        return new CertificateTagsDto(createdCertificate, createdTags);
    }

    @Override
    public Page<CertificateTagsDto> findAllGiftCertificateTagDto(int page, int size) {
        Page<GiftCertificate> certificates = giftCertificateService.findAllCertificates(page, size);

        return convertCertificateListToCertificateTagsDto(certificates);
    }

    @Override
    public Page<CertificateTagsDto> findGiftCertificateTagDtoByParam(SelectQueryParameter params, int page, int size) {
        Page<GiftCertificate> certificates = giftCertificateService.findCertificatesWithParams(params, page, size);

        return convertCertificateListToCertificateTagsDto(certificates);
    }

    @Override
    public CertificateTagsDto findGiftCertificateTagDto(int certificateId) {
        GiftCertificate certificate = giftCertificateService.findCertificateById(certificateId);
        Set<Tag> tags = tagService.findTagsByCertificateId(certificateId);

        return new CertificateTagsDto(certificate, tags);
    }

    @Override
    public CertificateTagsDto updateGiftCertificateTagDto(CertificateTagsDto certificateTagsDto, int id) {
        GiftCertificate certificate = certificateTagsDto.certificate();

        Set<Tag> tags = tagService.findTagsByCertificateId(id);
        if (certificateTagsDto.tags() != null) {
            tags.addAll(certificateTagsDto.tags());
        }

        Set<Tag> updatedTags = tagService.addTags(tags);
        certificate.setTags(updatedTags);
        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificate(certificate, id);

        return new CertificateTagsDto(updatedCertificate, updatedTags);
    }

    private Page<CertificateTagsDto> convertCertificateListToCertificateTagsDto(Page<GiftCertificate> certificates) {
        List<CertificateTagsDto> certificateTagsDtoList = StreamSupport
                .stream(certificates.getEntities().spliterator(), false)
                .map(cert -> new CertificateTagsDto(cert,
                        tagService.findTagsByCertificateId(cert.getGiftCertificateId())))
                .toList();

        return new Page<>(certificateTagsDtoList, certificates.getPageMeta());
    }
}
