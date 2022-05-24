package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagDtoService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateTagDtoServiceImpl implements GiftCertificateTagDtoService {
    private GiftCertificateService giftCertificateService;
    private TagService tagService;

    @Autowired
    public GiftCertificateTagDtoServiceImpl(GiftCertificateService giftCertificateService, TagService tagService) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addGiftCertificateTagDto(CertificateTagsDto certificateTagsDto) {
        GiftCertificate certificate = certificateTagsDto.certificate();
        List<Tag> tags = certificateTagsDto.tags();
        certificate.setTags(tags);

        int certificateId = giftCertificateService.addGiftCertificate(certificate);

        Optional.ofNullable(tags).ifPresent(tagService::addTags);

        return certificateId;
    }

    @Override
    @Transactional
    public List<CertificateTagsDto> findAllGiftCertificateTagDto() {
        List<GiftCertificate> certificates = giftCertificateService.findAllCertificates();

        return convertCertificateListToCertificateTagsDto(certificates);
    }

    @Override
    @Transactional
    public List<CertificateTagsDto> findGiftCertificateTagDtoByParam(SelectQueryParameter params) {
        List<GiftCertificate> certificates = giftCertificateService.findCertificatesWithParams(params);

        return convertCertificateListToCertificateTagsDto(certificates);
    }

    @Override
    @Transactional
    public CertificateTagsDto findGiftCertificateTagDto(int certificateId) {
        GiftCertificate certificate = giftCertificateService.findCertificateById(certificateId);
        List<Tag> tags = tagService.findTagsByCertificateId(certificateId);

        return new CertificateTagsDto(certificate, tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GiftCertificate updateGiftCertificateTagDto(CertificateTagsDto certificateTagsDto, int id) {
        GiftCertificate certificate = certificateTagsDto.certificate();
        List<Tag> tags = certificateTagsDto.tags();
        certificate.setTags(tags);

        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificate(certificate, id);

        Optional.ofNullable(tags).ifPresent(tagService::addTags);

        return updatedCertificate;
    }

    private List<CertificateTagsDto> convertCertificateListToCertificateTagsDto(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map(cert ->
                        new CertificateTagsDto(cert, tagService.findTagsByCertificateId(cert.getGiftCertificateId())))
                .toList();
    }
}
