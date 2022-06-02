package com.epam.esm.controller;

import com.epam.esm.dto.PaymentDto;
import com.epam.esm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{paymentId}")
    public PaymentDto showPayment(@PathVariable @Positive int paymentId) {
        return paymentService.findPayment(paymentId);
    }

    @GetMapping("/user/{userId}")
    public List<PaymentDto> showUserPayments(@PathVariable @Positive int userId) {
        return paymentService.findPaymentsByUserId(userId);
    }

    @PostMapping("/user/{userId}")
    public PaymentDto createPayment(@PathVariable @Positive int userId,
                                      @RequestBody @NotEmpty List<@Positive Integer> certificateIdList) {
        return paymentService.addPayment(userId, certificateIdList);
    }
}
