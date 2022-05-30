package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserOrderServiceImpl implements UserOrderService {
    private static final String DELETED = "DELETED";
    private final UserOrderDao userOrderDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public UserOrderServiceImpl(UserOrderDao userOrderDao, UserDao userDao, GiftCertificateDao giftCertificateDao) {
        this.userOrderDao = userOrderDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public UserOrderDto createOrder(int userId, int giftCertificateId) {
        User user = userDao.readUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("There is no user with Id " + userId + " in database"));

        GiftCertificate giftCertificate = giftCertificateDao.readGiftCertificate(giftCertificateId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "There is no certificate with Id " + giftCertificateId + " in database"));

        UserOrder userOrder = new UserOrder.UserOrderBuilder()
                .setCost(giftCertificate.getPrice())
                .setCreateDate(LocalDateTime.now())
                .setUser(user)
                .setGiftCertificate(giftCertificate)
                .createUserOrder();

        UserOrder createdUserOrder = userOrderDao.createOrder(userOrder);

        return mapUserOrderOnDto(createdUserOrder);
    }

    @Override
    public UserOrderDto readOrder(int orderId) {
        UserOrder userOrder = userOrderDao.readOrder(orderId).orElseThrow(() ->
                new ResourceNotFoundException("There is no order with Id " + orderId + " in database"));

        return mapUserOrderOnDto(userOrder);
    }

    @Override
    public List<UserOrderDto> readUserOrder(int userId) {
        List<UserOrderDto> userOrders = userOrderDao.readUserOrder(userId)
                .stream()
                .map(this::mapUserOrderOnDto)
                .toList();

        if(userOrders.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + userId + " hasn't any order yet");
        }

        return userOrders;
    }

    private UserOrderDto mapUserOrderOnDto(UserOrder userOrder) {
        return new UserOrderDto(
                userOrder.getOrderId(),
                (userOrder.getUser().getFirstName() + SPACE + userOrder.getUser().getLastName()),
                (userOrder.getGiftCertificate() != null) ? userOrder.getGiftCertificate().getName() : DELETED,
                userOrder.getCost(),
                userOrder.getCreatedDate()
        );
    }
}
