package com.epam.esm.service;

import com.epam.esm.dto.UserOrderDto;
import java.util.List;

public interface UserOrderService {
    UserOrderDto createOrder(int userId, int giftCertificateId);

    UserOrderDto readOrder(int orderId);

    List<UserOrderDto> readUserOrder(int userId);
}
