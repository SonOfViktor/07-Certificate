package com.epam.esm.dao;

import com.epam.esm.entity.UserOrder;

import java.util.List;
import java.util.Optional;

public interface UserOrderDao {
    UserOrder createOrder(UserOrder userOrder);

    Optional<UserOrder> readOrder(int orderId);

    List<UserOrder> readUserOrder(int userId);
}
