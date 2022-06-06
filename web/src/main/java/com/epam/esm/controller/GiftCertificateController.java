package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagDtoService;
import com.epam.esm.validategroup.ForCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/certificates")
@Validated
public class GiftCertificateController {
    public static final String ALL_GIFT_CERTIFICATES = "all_gift_certificates";
    public static final String ALL_TAGS = "all_tags";
    public static final String USERS = "users";
    private final GiftCertificateTagDtoService certificateTagService;
    private final GiftCertificateService certificateService;
    private final GiftCertificateModelAssembler certificateAssembler;
    private final TagModelAssembler tagAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateTagDtoService certificateTagService, TagModelAssembler tagAssembler,
                                     GiftCertificateService certificateService, GiftCertificateModelAssembler assembler) {
        this.certificateTagService = certificateTagService;
        this.certificateService = certificateService;
        this.certificateAssembler = assembler;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> showCertificate(@PathVariable @Positive int id) {
        CertificateTagsDto giftCertificateTagDto = certificateTagService.findGiftCertificateTagDto(id);

        return certificateAssembler.toModel(giftCertificateTagDto.certificate())
                .add(linkTo(methodOn(GiftCertificateController.class)
                        .showCertificateWithParameters(null))
                        .withRel(ALL_GIFT_CERTIFICATES));
    }

    @GetMapping("/{id}/tags")
    public CollectionModel<EntityModel<Tag>> showTagWithCertificateId(@PathVariable @Positive int id) {
        CertificateTagsDto giftCertificateTagDto = certificateTagService.findGiftCertificateTagDto(id);

        return tagAssembler.toCollectionModel(giftCertificateTagDto.tags())
                .add(linkTo(methodOn(TagController.class).showAllTags()).withRel(ALL_TAGS));
    }

    @PostMapping
    public CollectionModel<EntityModel<GiftCertificate>> showCertificateWithParameters(
            @Valid @RequestBody(required = false) SelectQueryParameter queryParam) {
        List<GiftCertificate> giftCertificates = (queryParam == null) ?
                certificateService.findAllCertificates() :
                certificateService.findCertificatesWithParams(queryParam);

        return certificateAssembler.toCollectionModel(giftCertificates)
                .add(linkTo(methodOn(GiftCertificateController.class).showCertificateWithParameters(queryParam))
                        .withSelfRel())
                .add(linkTo(methodOn(UserController.class).showAllUsers()).withRel(USERS));
    }

    @PostMapping("/creating")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificate> addCertificate(@Validated({ForCreate.class, Default.class})
                                             @RequestBody CertificateTagsDto certificateTagsDto) {
        CertificateTagsDto newCertificateTagsDto = certificateTagService.addGiftCertificateTagDto(certificateTagsDto);

        return certificateAssembler.toModel(newCertificateTagsDto.certificate())
                .add(linkTo(methodOn(GiftCertificateController.class).showCertificateWithParameters(null))
                        .withRel(ALL_GIFT_CERTIFICATES));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<GiftCertificate> updateCertificate(@Valid @RequestBody CertificateTagsDto certificateTagDto,
                                                @PathVariable @Positive int id) {
        CertificateTagsDto updatedCertificateTagsDto =
                certificateTagService.updateGiftCertificateTagDto(certificateTagDto, id);

        return certificateAssembler.toModel(updatedCertificateTagsDto.certificate())
                .add(linkTo(methodOn(GiftCertificateController.class).showCertificateWithParameters(null))
                        .withRel(ALL_GIFT_CERTIFICATES));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable @Positive int id) {
        certificateService.deleteCertificate(id);
    }
}
