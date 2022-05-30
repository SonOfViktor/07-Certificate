package com.epam.esm.controller;

import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.service.impl.UserOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/order")
public class UserOrderController {
    private final UserOrderServiceImpl userOrderService;

    @Autowired
    public UserOrderController(UserOrderServiceImpl userOrderService) {
        this.userOrderService = userOrderService;
    }

    @GetMapping("/{orderId}")
    public UserOrderDto showUserOrder(@PathVariable @Positive int orderId) {
        return userOrderService.readOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<UserOrderDto> showOrdersOfUser(@PathVariable @Positive int userId) {
        return userOrderService.readUserOrder(userId);
    }

    @PostMapping("/user/{userId}/certificate/{certificateId}")
    public UserOrderDto createUserOrder(@PathVariable @Positive int userId, @PathVariable @Positive int certificateId) {
        return userOrderService.createOrder(userId, certificateId);
    }
}
