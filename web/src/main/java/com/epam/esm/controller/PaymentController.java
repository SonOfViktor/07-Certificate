package com.epam.esm.controller;

import com.epam.esm.assembler.OrderModelAssembler;
import com.epam.esm.assembler.PaymentModelAssembler;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Validated
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    public static final String USERS = "users";
    private final PaymentService paymentService;
    private final PaymentModelAssembler paymentAssembler;
    private final OrderModelAssembler orderAssembler;
    private final PagedResourcesAssembler<PaymentDto.UserOrderDto> pagedResourcesUserOrderDtoAssembler;

    @GetMapping("/{paymentId}")
    public EntityModel<PaymentDto> showPayment(@PathVariable @Positive int paymentId) {
        PaymentDto payment = paymentService.findPayment(paymentId);

        return paymentAssembler.toModel(payment)
                .add(linkTo(UserController.class).withRel(USERS));
    }

    @GetMapping("/{paymentId}/orders")
    public CollectionModel<EntityModel<PaymentDto.UserOrderDto>> showPaymentOrder(@PathVariable @Positive Integer paymentId,
                                                                                  Pageable pageable) {
        Page<PaymentDto.UserOrderDto> userOrders = paymentService.findUserOrderByPaymentId(paymentId, pageable);

        return pagedResourcesUserOrderDtoAssembler.toModel(userOrders, orderAssembler)
                .add(linkTo(UserController.class).withRel(USERS));
    }
}
