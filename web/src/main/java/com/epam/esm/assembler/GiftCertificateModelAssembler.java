package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class GiftCertificateModelAssembler implements
        RepresentationModelAssembler<GiftCertificate, EntityModel<GiftCertificate>> {
    public static final String TAGS = "tags";

    @Override
    @NonNull
    public EntityModel<GiftCertificate> toModel(@NonNull GiftCertificate entity) {

        return EntityModel.of(entity,
                linkTo(methodOn(GiftCertificateController.class)
                        .showCertificate(entity.getGiftCertificateId()))
                        .withSelfRel(),
                linkTo(methodOn(GiftCertificateController.class)
                        .showTagWithCertificateId(entity.getGiftCertificateId()))
                        .withRel(TAGS));
    }
}