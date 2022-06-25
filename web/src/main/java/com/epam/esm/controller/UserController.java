package com.epam.esm.controller;

import com.epam.esm.assembler.PaymentModelAssembler;
import com.epam.esm.assembler.UserModelAssembler;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.PaymentService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
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
@RequiredArgsConstructor
public class UserController {
    public static final String USERS = "users";
    public static final String CREATE = "create";

    private final UserService userService;
    private final PaymentService paymentService;
    private final UserModelAssembler userAssembler;
    private final PaymentModelAssembler paymentAssembler;
    private final PagedResourcesAssembler<User> pagedResourcesUserAssembler;
    private final PagedResourcesAssembler<PaymentDto> pagedResourcesPaymentDtoAssembler;

    @GetMapping
    public CollectionModel<EntityModel<User>> showAllUsers(Pageable pageable) {
        Page<User> users = userService.findAllUser(pageable);

        return pagedResourcesUserAssembler.toModel(users, userAssembler);
    }

    @GetMapping("/{userId}/payments")
    public CollectionModel<EntityModel<PaymentDto>> showUserPayments(@PathVariable @Positive Integer userId, Pageable pageable) {

        Page<PaymentDto> paymentsByUserId = paymentService.findPaymentsByUserId(userId, pageable);

        return pagedResourcesPaymentDtoAssembler.toModel(paymentsByUserId, paymentAssembler)
                .add(linkTo(methodOn(UserController.class).showAllUsers(pageable)).withRel(USERS))
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
