package com.epam.esm.controller;

import com.epam.esm.assembler.OrderModelAssembler;
import com.epam.esm.assembler.PaymentModelAssembler;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/payment")
public class PaymentController {
    public static final String USERS = "users";
    private final PaymentService paymentService;
    private final PaymentModelAssembler paymentAssembler;
    private final OrderModelAssembler orderAssembler;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentModelAssembler paymentAssembler,
                             OrderModelAssembler orderAssembler) {
        this.paymentService = paymentService;
        this.orderAssembler = orderAssembler;
        this.paymentAssembler = paymentAssembler;
    }

    @GetMapping("/{paymentId}")
    public EntityModel<PaymentDto> showPayment(@PathVariable @Positive int paymentId) {
        PaymentDto payment = paymentService.findPayment(paymentId);

        return paymentAssembler.toModel(payment);
    }

    @GetMapping("/{paymentId}/orders")
    public CollectionModel<EntityModel<PaymentDto.UserOrderDto>> showPaymentOrder(@PathVariable @Positive int paymentId) {
        PaymentDto payment = paymentService.findPayment(paymentId);

        return orderAssembler.toCollectionModel(payment.userOrderDtoList())
                .add(linkTo(methodOn(PaymentController.class).showPaymentOrder(paymentId)).withSelfRel())
                .add(linkTo(methodOn(UserController.class).showAllUsers()).withRel(USERS));
    }

    @PostMapping("/user/{userId}")
    public EntityModel<PaymentDto> createPayment(@PathVariable @Positive int userId,
                                                 @RequestBody @NotEmpty List<@Positive Integer> certificateIdList) {
        PaymentDto payment = paymentService.addPayment(userId, certificateIdList);

        return paymentAssembler.toModel(payment);
    }
}
