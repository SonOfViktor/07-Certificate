package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateTagsCreateEditDto;
import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.dto.FileImageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Category;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CategoryService;
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
    private final CategoryService categoryService;

    @Override
    public CertificateTagsDto addGiftCertificateTagDto(CertificateTagsCreateEditDto certificateTagsDto, FileImageDto image) {
        Set<String> tags = certificateTagsDto.tags();
        Set<Tag> createdTags = tagService.addTags(tags);

        GiftCertificate certificate = mapGiftCertificateTagDtoOnGiftCertificate(certificateTagsDto, createdTags);
        GiftCertificate createdCertificate = giftCertificateService.addGiftCertificate(certificate, image);

        return createCertificateTagsDto(createdCertificate);
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

        return createCertificateTagsDto(certificate);
    }

    @Override
    public CertificateTagsDto updateGiftCertificateTagDto(int id, CertificateTagsCreateEditDto certificateTagsDto,
                                                          Optional<FileImageDto> optionalImage) {
        Set<String> tags = certificateTagsDto.tags();
        Set<Tag> updatedTags = tagService.addTags(tags);

        GiftCertificate certificate = mapGiftCertificateTagDtoOnGiftCertificate(certificateTagsDto, updatedTags);
        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificate(id, certificate, optionalImage);

        return createCertificateTagsDto(updatedCertificate);
    }

    private Page<CertificateTagsDto> convertCertificateToCertificateTagsDtoInPage(Page<GiftCertificate> certificates) {
        return certificates.map(this::createCertificateTagsDto);
    }

    private GiftCertificate mapGiftCertificateTagDtoOnGiftCertificate(CertificateTagsCreateEditDto dto, Set<Tag> tags) {
        Category category = Optional.ofNullable(dto.category())
                .map(categoryService::findByName)
                .orElse(null);

        return GiftCertificate.builder()
                .name(dto.name())
                .description(dto.description())
                .duration(dto.duration() != null ? dto.duration() : 0)
                .price(dto.price())
                .category(category)
                .tags(tags)
                .build();
    }

    private CertificateTagsDto createCertificateTagsDto(GiftCertificate certificate) {
        return new CertificateTagsDto(
                certificate.getGiftCertificateId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getImage(),
                certificate.getCategory().getName(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                createSetTagDto(certificate.getTags()));
    }

    private Set<TagDto> createSetTagDto(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> new TagDto(tag.getTagId(), tag.getName()))
                .collect(Collectors.toSet());
    }
}
