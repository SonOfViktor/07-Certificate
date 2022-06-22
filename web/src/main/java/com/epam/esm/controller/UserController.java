package com.epam.esm.controller;

import com.epam.esm.assembler.PaymentModelAssembler;
import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.service.PaymentService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    public static final String USERS = "users";
    public static final String CREATE = "create";
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
    public Page<EntityModel<User>> showAllUsers(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<User> users = userService.findAllUser(page, size);

        return userAssembler.toPageModel(users);
    }

    @GetMapping("/{userId}/payments")
    public Page<EntityModel<PaymentDto>> showUserPayments(
            @PathVariable @Positive Integer userId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<PaymentDto> paymentsByUserId = paymentService.findPaymentsByUserId(userId, page, size);

        return paymentAssembler.toPageModel(paymentsByUserId)
                .add(linkTo(methodOn(UserController.class).showAllUsers(page, size)).withRel(USERS))
                .add(linkTo(methodOn(UserController.class).createPayment(userId, null)).withRel(CREATE));
    }

    @PostMapping("/{userId}/payments")
    public EntityModel<PaymentDto> createPayment(@PathVariable @Positive int userId,
                                                 @RequestBody @NotEmpty List<@Positive Integer> certificateIdList) {
        PaymentDto payment = paymentService.addPayment(userId, certificateIdList);

        return paymentAssembler.toModel(payment)
                .add(linkTo(UserController.class).withRel(USERS));
    }
}
