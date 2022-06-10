package com.epam.esm.controller;

import com.epam.esm.assembler.OrderModelAssembler;
import com.epam.esm.assembler.PaymentModelAssembler;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.Page;
import com.epam.esm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

        return paymentAssembler.toModel(payment)
                .add(linkTo(UserController.class).withRel(USERS));
    }

    @GetMapping("/{paymentId}/orders")
    public Page<EntityModel<PaymentDto.UserOrderDto>> showPaymentOrder(
            @PathVariable @Positive Integer paymentId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<PaymentDto.UserOrderDto> userOrders = paymentService.findUserOrderByPaymentId(paymentId, page, size);

        return orderAssembler.toPageModel(userOrders)
                .add(linkTo(UserController.class).withRel(USERS));
    }

    @PostMapping("/user/{userId}")
    public EntityModel<PaymentDto> createPayment(@PathVariable @Positive int userId,
                                                 @RequestBody @NotEmpty List<@Positive Integer> certificateIdList) {
        PaymentDto payment = paymentService.addPayment(userId, certificateIdList);

        return paymentAssembler.toModel(payment)
                .add(linkTo(UserController.class).withRel(USERS));
    }
}
