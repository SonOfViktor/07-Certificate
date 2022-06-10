package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagDtoService;
import com.epam.esm.service.TagService;
import com.epam.esm.validategroup.ForCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/certificates")
@Validated
public class GiftCertificateController {
    public static final String ALL_GIFT_CERTIFICATES = "all_gift_certificates";
    public static final String USERS = "users";
    private final GiftCertificateTagDtoService certificateTagService;
    private final TagService tagService;
    private final GiftCertificateService certificateService;
    private final GiftCertificateModelAssembler certificateAssembler;
    private final TagModelAssembler tagAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateTagDtoService certificateTagService, TagService tagService, TagModelAssembler tagAssembler,
                                     GiftCertificateService certificateService, GiftCertificateModelAssembler assembler) {
        this.certificateTagService = certificateTagService;
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.certificateAssembler = assembler;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> showCertificate(@PathVariable @Positive int id) {
        CertificateTagsDto giftCertificateTagDto = certificateTagService.findGiftCertificateTagDto(id);

        return certificateAssembler.toModel(giftCertificateTagDto)
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES));
    }

    @GetMapping("/{id}/tags")
    public Page<EntityModel<Tag>> showTagWithCertificateId(
            @PathVariable @Positive Integer id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<Tag> tags = tagService.findTagsByCertificateId(id, page, size);

        return tagAssembler.toPageModel(tags);
    }

    @PostMapping
    public Page<EntityModel<GiftCertificate>> showCertificateWithParameters(
            @Valid @RequestBody(required = false) SelectQueryParameter queryParam,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<CertificateTagsDto> certificates = (queryParam == null) ?
                certificateTagService.findAllGiftCertificateTagDto(page, size) :
                certificateTagService.findGiftCertificateTagDtoByParam(queryParam, page, size);

        return certificateAssembler.toPageModel(certificates)
                .add(linkTo(UserController.class).withRel(USERS));
    }

    @PostMapping("/creating")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftCertificate> addCertificate(@Validated({ForCreate.class, Default.class})
                                                       @RequestBody CertificateTagsDto certificateTagsDto) {

        CertificateTagsDto newCertificateTagsDto = certificateTagService.addGiftCertificateTagDto(certificateTagsDto);

        return certificateAssembler.toModel(newCertificateTagsDto)
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<GiftCertificate> updateCertificate(@Valid @RequestBody CertificateTagsDto certificateTagDto,
                                                          @PathVariable @Positive int id) {
        CertificateTagsDto updatedCertificateTagsDto =
                certificateTagService.updateGiftCertificateTagDto(certificateTagDto, id);

        return certificateAssembler.toModel(updatedCertificateTagsDto)
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable @Positive int id) {
        certificateService.deleteCertificate(id);
    }
}
