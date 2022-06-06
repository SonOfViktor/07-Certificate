package com.epam.esm.controller;

import com.epam.esm.assembler.PaymentModelAssembler;
import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.PaymentService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    public static final String USERS = "users";
    private final UserService userService;
    private final PaymentService paymentService;
    private final UserModelAssembler userAssembler;
    private final PaymentModelAssembler paymentAssembler;

    @Autowired
    public UserController(UserService userService, PaymentService paymentService,
                          UserModelAssembler userAssembler, PaymentModelAssembler paymentAssembler) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.userAssembler = userAssembler;
        this.paymentAssembler = paymentAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> showAllUsers() {
        List<User> users = userService.findAllUser();

        return userAssembler.toCollectionModel(users);
    }

    @GetMapping("/{userId}/payments")
    public CollectionModel<EntityModel<PaymentDto>> showUserPayments(@PathVariable @Positive int userId) {
        List<PaymentDto> paymentsByUserId = paymentService.findPaymentsByUserId(userId);

        return paymentAssembler.toCollectionModel(paymentsByUserId)
                .add(linkTo(methodOn(UserController.class).showUserPayments(userId)).withSelfRel())
                .add(linkTo(methodOn(UserController.class).showAllUsers()).withRel(USERS));
    }
}
