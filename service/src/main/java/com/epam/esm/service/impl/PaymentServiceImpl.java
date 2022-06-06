package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.PaymentDao;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.Payment;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.math.NumberUtils.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentServiceImpl implements PaymentService {
    private static final String DELETED = "DELETED";
    private final PaymentDao paymentDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao, UserDao userDao, GiftCertificateDao giftCertificateDao) {
        this.paymentDao = paymentDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public PaymentDto addPayment(int userId, List<Integer> giftCertificateIdList) {
        User user = userDao.readUserById(userId).orElseThrow(() ->
                new ResourceNotFoundException("There is no user with Id " + userId + " in database"));

        Payment payment = new Payment.PaymentBuilder()
                .setCreatedDate(LocalDateTime.now())
                .setUser(user)
                .setUserOrder(createUserOrders(giftCertificateIdList))
                .createPayment();

        Payment createdPayment = paymentDao.createPayment(payment);

        return mapPaymentOnDto(createdPayment);
    }

    @Override
    public PaymentDto findPayment(int paymentId) {
        Payment payment = paymentDao.readPayment(paymentId);

        return mapPaymentOnDto(payment);
    }

    @Override
    public List<PaymentDto> findPaymentsByUserId(int userId) {
        List<PaymentDto> paymentDtoList = paymentDao.readPaymentByUserId(userId)
                .stream()
                .map(this::mapPaymentOnDto)
                .toList();

        if (paymentDtoList.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + userId + " hasn't any payment yet");
        }

        return paymentDtoList;
    }

    private PaymentDto mapPaymentOnDto(Payment payment) {
        return new PaymentDto(
                payment.getPaymentId(),
                (payment.getUser().getFirstName() + SPACE + payment.getUser().getLastName()),
                createUserOrderDtoListFromPayment(payment),
                payment.getCreatedDate()
        );
    }

    private List<UserOrder> createUserOrders(List<Integer> giftCertificateIdList) {
        return giftCertificateIdList.stream()
                .map(id -> giftCertificateDao.readGiftCertificate(id).orElseThrow(() ->
                        new ResourceNotFoundException("There is no certificate with Id " + id + " in database")))
                .map(gc -> new UserOrder.UserOrderBuilder()
                        .setCost(gc.getPrice())
                        .setGiftCertificate(gc)
                        .createUserOrder())
                .toList();
    }

    private List<PaymentDto.UserOrderDto> createUserOrderDtoListFromPayment(Payment payment) {
        return payment.getOrders().stream()
                .map(order -> new PaymentDto.UserOrderDto(
                        (order.getGiftCertificate() != null) ?
                                order.getGiftCertificate().getGiftCertificateId() :
                                INTEGER_ZERO,
                        (order.getGiftCertificate() != null) ?
                                order.getGiftCertificate().getName() :
                                DELETED,
                        order.getCost()))
                .toList();
    }

}
