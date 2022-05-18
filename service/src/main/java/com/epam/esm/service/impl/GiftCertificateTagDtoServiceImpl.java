package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateTagDao;
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
import java.util.Set;

@Service
public class GiftCertificateTagDtoServiceImpl implements GiftCertificateTagDtoService {
    private GiftCertificateTagDao giftCertificateTagDao;
    private GiftCertificateService giftCertificateService;
    private TagService tagService;

    @Autowired
    public GiftCertificateTagDtoServiceImpl(GiftCertificateTagDao giftCertificateTagDao,
                                            GiftCertificateService giftCertificateService, TagService tagService) {
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addGiftCertificateTagDto(CertificateTagsDto certificateTagsDto) {
        int certificateId = giftCertificateService.addGiftCertificate(certificateTagsDto.certificate());

        if (certificateTagsDto.tags() != null) {
            tagService.addTags(certificateTagsDto.tags());
            giftCertificateTagDao.createGiftCertificateTagEntries(certificateId, certificateTagsDto.tags());
        }

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
        Set<Tag> tags = tagService.findTagsByCertificateId(certificateId);

        return new CertificateTagsDto(certificate, tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int[] updateGiftCertificateTagDto(CertificateTagsDto certificateTagsDto, int id) {
        giftCertificateService.updateGiftCertificate(certificateTagsDto.certificate(), id);

        int[] affectedRows = new int[0];
        if (certificateTagsDto.tags() != null) {
            tagService.addTags(certificateTagsDto.tags());
            affectedRows = giftCertificateTagDao.createGiftCertificateTagEntries(id, certificateTagsDto.tags());
        }

        return affectedRows;
    }

    private List<CertificateTagsDto> convertCertificateListToCertificateTagsDto(List<GiftCertificate> certificates) {
        return certificates.stream()
                .map(cert ->
                        new CertificateTagsDto(cert, tagService.findTagsByCertificateId(cert.getGiftCertificateId())))
                .toList();
    }
}
