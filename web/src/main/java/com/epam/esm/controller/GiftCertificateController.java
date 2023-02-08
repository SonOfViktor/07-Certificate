package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateModelAssembler;
import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.dto.CertificateTagsCreateEditDto;
import com.epam.esm.dto.CertificateTagsDto;
import com.epam.esm.dto.FileImageDto;
import com.epam.esm.entity.GiftCertificateFilter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.GiftCertificateTagDtoService;
import com.epam.esm.service.TagService;
import com.epam.esm.validation.group.ForCreate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/certificates")
@Validated
@RequiredArgsConstructor
public class GiftCertificateController {
    public static final String ALL_GIFT_CERTIFICATES = "all_gift_certificates";
    public static final String CERTIFICATE = "certificate";
    public static final String USERS = "users";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    private final GiftCertificateTagDtoService certificateTagService;
    private final TagService tagService;
    private final GiftCertificateService certificateService;
    private final GiftCertificateModelAssembler certificateAssembler;
    private final TagModelAssembler tagAssembler;
    private final PagedResourcesAssembler<CertificateTagsDto> pagedResourcesGiftCertificateAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesTagAssembler;

    @GetMapping("/{id}")
    public EntityModel<CertificateTagsDto> showCertificate(@PathVariable @Positive int id) {
        CertificateTagsDto giftCertificateTagDto = certificateTagService.findGiftCertificateTagDto(id);

        return certificateAssembler.toModel(giftCertificateTagDto)
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES))
                .add(linkTo(methodOn(GiftCertificateController.class).updateCertificate(null, id)).withRel(UPDATE))
                .add(linkTo(methodOn(GiftCertificateController.class).deleteCertificate(id)).withRel(DELETE));
    }

    @GetMapping("/{id}/tags")
    public CollectionModel<EntityModel<Tag>> showTagWithCertificateId(@PathVariable @Positive Integer id,
                                                                      Pageable pageable) {
        Page<Tag> tags = tagService.findTagsByCertificateId(id, pageable);

        return pagedResourcesTagAssembler.toModel(tags, tagAssembler)
                .add(linkTo(methodOn(GiftCertificateController.class).showCertificate(id)).withRel(CERTIFICATE));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> findCertificateImage(@PathVariable @Positive Integer id) {
        return certificateService.findCertificateImage(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping
    public CollectionModel<EntityModel<CertificateTagsDto>> showCertificateWithFilter(
            Pageable pageable,
            @Valid @RequestBody(required = false) GiftCertificateFilter queryParam) {

        Page<CertificateTagsDto> certificates = (queryParam == null) ?
                certificateTagService.findAllGiftCertificateTagDto(pageable) :
                certificateTagService.findGiftCertificateTagDtoByParam(queryParam, pageable);

        return pagedResourcesGiftCertificateAssembler.toModel(certificates, certificateAssembler)
                .add(linkTo(UserController.class).withRel(USERS))
                .add(linkTo(methodOn(GiftCertificateController.class).addCertificate(null)).withRel(CREATE));
    }

    @PostMapping(value = "/creating", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<CertificateTagsDto> addCertificate(@Validated({ForCreate.class, Default.class})
                                                       @ModelAttribute CertificateTagsCreateEditDto certificateTagsDto) {

        FileImageDto image = transformMultipartFile(certificateTagsDto.image());

        CertificateTagsDto newCertificateTagsDto = certificateTagService.addGiftCertificateTagDto(certificateTagsDto, image);

        return certificateAssembler.toModel(newCertificateTagsDto)
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<CertificateTagsDto> updateCertificate(@Valid @ModelAttribute CertificateTagsCreateEditDto certificateTagDto,
                                                          @PathVariable @Positive int id) {
        Optional<FileImageDto> optionalImage = Optional.ofNullable(certificateTagDto.image())
                .map(this::transformMultipartFile);

        CertificateTagsDto updatedCertificateTagsDto =
                certificateTagService.updateGiftCertificateTagDto(id, certificateTagDto, optionalImage);

        return certificateAssembler.toModel(updatedCertificateTagsDto)
                .add(linkTo(GiftCertificateController.class).withRel(ALL_GIFT_CERTIFICATES));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable @Positive int id) {
        certificateService.deleteCertificate(id);

        return ResponseEntity.noContent().build();
    }

    @SneakyThrows
    private FileImageDto transformMultipartFile(MultipartFile file) {
        String fileExtension = Optional.ofNullable(file.getOriginalFilename())
                .map(fileName -> fileName.substring(file.getOriginalFilename().lastIndexOf(".")))
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new MultipartException("File name or its extension is null"));

        return new FileImageDto(file.getInputStream(), fileExtension);
    }
}
