package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagDtoService;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class GiftCertificateTagDtoServiceImpl implements GiftCertificateTagDtoService {
    private final GiftCertificateService giftCertificateService;
    private final TagService tagService;

    @Override
    public CertificateTagsDto addGiftCertificateTagDto(CertificateTagsDto certificateTagsDto) {
        GiftCertificate certificate = mapGiftCertificateTagDtoOnGiftCertificate(certificateTagsDto);
        Set<TagDto> tags = certificateTagsDto.tags();

        Set<Tag> createdTags = tagService.addTags(tags);

        certificate.setTags(createdTags);
        GiftCertificate createdCertificate = giftCertificateService.addGiftCertificate(certificate);

        return createCertificateTagsDto(createdCertificate, createdTags);
    }

    @Override
    public Page<CertificateTagsDto> findAllGiftCertificateTagDto(Pageable pageable) {
        Page<GiftCertificate> certificates = giftCertificateService.findAllCertificates(pageable);

        return convertCertificateToCertificateTagsDtoInPage(certificates);
    }

    @Override
    public Page<CertificateTagsDto> findGiftCertificateTagDtoByParam(GiftCertificateFilter filter, Pageable pageable) {
        Page<GiftCertificate> certificates = giftCertificateService.findCertificatesWithParams(filter, pageable);

        return convertCertificateToCertificateTagsDtoInPage(certificates);
    }

    @Override
    public CertificateTagsDto findGiftCertificateTagDto(int certificateId) {
        GiftCertificate certificate = giftCertificateService.findCertificateById(certificateId);
        Set<Tag> tags = tagService.findTagsByCertificateId(certificateId);

        return createCertificateTagsDto(certificate, tags);
    }

    @Override
    public CertificateTagsDto updateGiftCertificateTagDto(CertificateTagsDto certificateTagsDto, int id) {
        GiftCertificate certificate = mapGiftCertificateTagDtoOnGiftCertificate(certificateTagsDto);

        Set<TagDto> tags = createSetTagDto(tagService.findTagsByCertificateId(id));

        Optional.ofNullable(certificateTagsDto.tags())
                .ifPresent(tags::addAll);

        Set<Tag> updatedTags = tagService.addTags(tags);
        certificate.setTags(updatedTags);
        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificate(certificate, id);

        return createCertificateTagsDto(updatedCertificate, updatedTags);
    }

    private Page<CertificateTagsDto> convertCertificateToCertificateTagsDtoInPage(Page<GiftCertificate> certificates) {
        return certificates.map(cert ->
                createCertificateTagsDto(cert, tagService.findTagsByCertificateId(cert.getGiftCertificateId())));
    }

    private GiftCertificate mapGiftCertificateTagDtoOnGiftCertificate(CertificateTagsDto dto) {
        return GiftCertificate.builder()
                .name(dto.name())
                .description(dto.description())
                .duration(dto.duration())
                .price(dto.price())
                .build();
    }

    private CertificateTagsDto createCertificateTagsDto(GiftCertificate certificate, Set<Tag> tags) {
        return new CertificateTagsDto(certificate.getGiftCertificateId(), certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getCreateDate(), certificate.getLastUpdateDate(), createSetTagDto(tags));
    }

    private Set<TagDto> createSetTagDto(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> new TagDto(tag.getTagId(), tag.getName())).
                collect(Collectors.toSet());
    }
}
