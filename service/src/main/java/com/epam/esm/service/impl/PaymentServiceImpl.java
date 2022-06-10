package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.PaymentDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.*;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

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
        Payment payment = paymentDao.readPayment(paymentId).orElseThrow(() ->
                new ResourceNotFoundException("There is no payment with Id " + paymentId + " in database"));

        return mapPaymentOnDto(payment);
    }

    @Override
    public Page<PaymentDto> findPaymentsByUserId(int userId, int page, int size) {
        int orderTotalElements = paymentDao.countUserPayments(userId);
        int totalPages = (int) Math.ceil((double) orderTotalElements / size);

        if (page > totalPages) {
            throw new ResourceNotFoundException("There is no payment in the database for " + page + " page");
        }

        PageMeta pageMeta = new PageMeta(size, orderTotalElements, totalPages, page);

        int offset = page * size - size;
        List<PaymentDto> paymentDtoList = paymentDao.readPaymentByUserId(userId, offset, size)
                .stream()
                .map(this::mapPaymentOnDto)
                .toList();

        return new Page<>(paymentDtoList, pageMeta);
    }

    public Page<PaymentDto.UserOrderDto> findUserOrderByPaymentId(int paymentId, int page, int size) {
        int orderTotalElements = paymentDao.countPaymentOrders(paymentId);
        int totalPages = (int) Math.ceil((double) orderTotalElements / size);

        if (page > totalPages) {
            throw new ResourceNotFoundException("There is no orders in the database for " + page + " page");
        }

        PageMeta pageMeta = new PageMeta(size, orderTotalElements, totalPages, page);

        int offset = page * size - size;
        List<PaymentDto.UserOrderDto> orders = createUserOrderDtoList(paymentDao.readUserOrderByPaymentId(paymentId, offset, size));

        return new Page<>(orders, pageMeta);
    }

    private PaymentDto mapPaymentOnDto(Payment payment) {
        return new PaymentDto(
                payment.getPaymentId(),
                (payment.getUser().getFirstName() + SPACE + payment.getUser().getLastName()),
                createUserOrderDtoList(payment.getOrders()),
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

    private List<PaymentDto.UserOrderDto> createUserOrderDtoList(List<UserOrder> orders) {
        return orders.stream()
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
