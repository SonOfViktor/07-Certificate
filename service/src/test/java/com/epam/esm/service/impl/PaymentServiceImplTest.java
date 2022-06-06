package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.PaymentDao;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Payment;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    private PaymentServiceImpl userOrderService;

    @Mock
    private PaymentDao paymentDao;

    @Mock
    private UserDao userDao;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Test
    void testCreatePayment() {
        User user = new User(1, "Maks", "Silev");

        GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setPrice(new BigDecimal("40.00"))
                .setName("Oz.by")
                .createGiftCertificate();

        UserOrder order = new UserOrder.UserOrderBuilder()
                .setUserOrderId(1)
                .setCost(new BigDecimal("40.00"))
                .setGiftCertificate(giftCertificate)
                .createUserOrder();

        Payment payment = new Payment.PaymentBuilder()
                .setPaymentId(1)
                .setUserOrder(List.of(order))
                .setCreatedDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .setUser(user)
                .createPayment();

        when(userDao.readUserById(1)).thenReturn(Optional.of(user));
        when(giftCertificateDao.readGiftCertificate(1)).thenReturn(Optional.of(giftCertificate));
        when(paymentDao.createPayment(any(Payment.class))).thenReturn(payment);

        PaymentDto expected = new PaymentDto(
                1, "Maks Silev",
                List.of(new PaymentDto.UserOrderDto(1, "Oz.by", new BigDecimal("40.00"))),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0));
        PaymentDto actual = userOrderService.addPayment(1, List.of(1));

        assertEquals(expected, actual);
    }

    @Test
    void testCreatePaymentWithoutUser() {
        List<Integer> certificateIdList = List.of(1);

        when(userDao.readUserById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.addPayment(99, certificateIdList));
    }

    @Test
    void testCreatePaymentWithoutGiftCertificate() {
        List<Integer> certificateIdList = List.of(99);

        when(userDao.readUserById(1)).thenReturn(Optional.of(new User()));
        when(giftCertificateDao.readGiftCertificate(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.addPayment(1, certificateIdList));
    }

    @Test
    void testReadPayment() {
        User user = new User(1, "Maks", "Silev");
        GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setName("Oz.by")
                .createGiftCertificate();
        UserOrder order = new UserOrder.UserOrderBuilder()
                .setUserOrderId(1)
                .setCost(new BigDecimal("40.00"))
                .setGiftCertificate(giftCertificate)
                .createUserOrder();
        Payment payment = new Payment.PaymentBuilder()
                .setPaymentId(1)
                .setUser(user)
                .setUserOrder(List.of(order))
                .setCreatedDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .createPayment();

        when(paymentDao.readPayment(1)).thenReturn(payment);

        PaymentDto expected = new PaymentDto(
                1, "Maks Silev",
                List.of(new PaymentDto.UserOrderDto(1,"Oz.by", new BigDecimal("40.00"))),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0));
        PaymentDto actual = userOrderService.findPayment(1);

        assertEquals(expected, actual);
    }

    @Test
    void testReadPaymentsByUserId() {
        User user = new User(1, "Maks", "Silev");
        GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setName("Oz.by")
                .createGiftCertificate();
        UserOrder order = new UserOrder.UserOrderBuilder()
                .setUserOrderId(1)
                .setCost(new BigDecimal("40.00"))
                .setGiftCertificate(giftCertificate)
                .createUserOrder();
        Payment payment = new Payment.PaymentBuilder()
                .setPaymentId(1)
                .setUser(user)
                .setUserOrder(List.of(order))
                .setCreatedDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .createPayment();
        when(paymentDao.readPaymentByUserId(1)).thenReturn(List.of(payment));

        List<PaymentDto> expected = List.of(new PaymentDto(
                1, "Maks Silev",
                List.of(new PaymentDto.UserOrderDto(1,"Oz.by", new BigDecimal("40.00"))),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0)));
        List<PaymentDto> actual = userOrderService.findPaymentsByUserId(1);

        assertEquals(expected, actual);
    }

    @Test
    void testReadPaymentsWithNonExistUser() {
        when(paymentDao.readPaymentByUserId(99)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.findPaymentsByUserId(99));
    }
}