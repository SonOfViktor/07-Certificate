package com.epam.esm.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
public class RootController {

    public static final String TAGS = "tags";
    public static final String GIFT_CERTIFICATES = "gift_certificates";
    public static final String USERS = "users";

    @GetMapping
    public Object root() {
        RepresentationModel<?> rootResource = new RepresentationModel<>();

        return rootResource.add(
                linkTo(methodOn(RootController.class).root()).withSelfRel(),
                linkTo(UserController.class).withRel(USERS),
                linkTo(GiftCertificateController.class).withRel(GIFT_CERTIFICATES),
                linkTo(TagController.class).withRel(TAGS));
    }
}
