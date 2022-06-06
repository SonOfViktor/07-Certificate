package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    public static final String PAYMENTS = "payments";

    @Override
    @NonNull
    public EntityModel<User> toModel(@NonNull User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).showUserPayments(entity.getUserId())).withRel(PAYMENTS));
    }

    @Override
    @NonNull
    public CollectionModel<EntityModel<User>> toCollectionModel(@NonNull Iterable<? extends User> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(UserController.class).showAllUsers()).withSelfRel());
    }
}
