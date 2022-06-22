package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.PaymentDao;
import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.*;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentDao paymentDao;

    @Mock
    private UserDao userDao;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Test
    void testCreatePayment() {
        User user = new User(1, "Maks", "Silev");

        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftCertificateId(1)
                .price(new BigDecimal("40.00"))
                .name("Oz.by")
                .build();

        UserOrder order = UserOrder.builder()
                .orderId(1)
                .cost(new BigDecimal("40.00"))
                .giftCertificate(giftCertificate)
                .build();

        Payment payment = Payment.builder()
                .paymentId(1)
                .orders(List.of(order))
                .createdDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .user(user)
                .build();

        when(userDao.readUserById(1)).thenReturn(Optional.of(user));
        when(giftCertificateDao.readGiftCertificate(1)).thenReturn(Optional.of(giftCertificate));
        when(paymentDao.createPayment(any(Payment.class))).thenReturn(payment);

        PaymentDto expected = new PaymentDto(
                1, "Maks Silev",
                List.of(new PaymentDto.UserOrderDto(1, "Oz.by", new BigDecimal("40.00"))),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0));
        PaymentDto actual = paymentService.addPayment(1, List.of(1));

        assertEquals(expected, actual);
    }

    @Test
    void testCreatePaymentWithoutUser() {
        List<Integer> certificateIdList = List.of(1);

        when(userDao.readUserById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.addPayment(99, certificateIdList));
    }

    @Test
    void testCreatePaymentWithoutGiftCertificate() {
        List<Integer> certificateIdList = List.of(99);

        when(userDao.readUserById(1)).thenReturn(Optional.of(new User()));
        when(giftCertificateDao.readGiftCertificate(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.addPayment(1, certificateIdList));
    }

    @Test
    void testFindPayment() {
        Payment payment = createPayment();

        when(paymentDao.readPayment(1)).thenReturn(Optional.ofNullable(payment));

        PaymentDto expected = new PaymentDto(
                1, "Maks Silev",
                List.of(new PaymentDto.UserOrderDto(1,"Oz.by", new BigDecimal("40.00"))),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0));
        PaymentDto actual = paymentService.findPayment(1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindNonExistentPayment() {
        when(paymentDao.readPayment(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.findPayment(10));
    }

    @Test
    void testFindPaymentsByUserId() {
        Payment payment = createPayment();

        when(paymentDao.countUserPayments(1)).thenReturn(1);
        when(paymentDao.readPaymentByUserId(1, 0, 10)).thenReturn(List.of(payment));

        List<PaymentDto> payments = List.of(new PaymentDto(
                1, "Maks Silev",
                List.of(new PaymentDto.UserOrderDto(1,"Oz.by", new BigDecimal("40.00"))),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0)));

        Page<PaymentDto> expected = new Page<>(payments, new PageMeta(10, 1, 1, 1));
        Page<PaymentDto> actual = paymentService.findPaymentsByUserId(1, 1, 10);

        assertEquals(expected, actual);
    }

    @Test
    void testFindPaymentsByUserIdOnNonExistentPage() {
        when(paymentDao.countUserPayments(1)).thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.findPaymentsByUserId(1, 1, 10));
    }

    @Test
    void testFindUserOrderByPaymentId() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftCertificateId(1)
                .name("Oz.by")
                .build();
        UserOrder order = UserOrder.builder()
                .orderId(1)
                .cost(new BigDecimal("40.00"))
                .giftCertificate(giftCertificate)
                .build();

        PaymentDto.UserOrderDto orderDto = new PaymentDto.UserOrderDto(1, "Oz.by", new BigDecimal("40.00"));

        when(paymentDao.countPaymentOrders(3)).thenReturn(1);
        when(paymentDao.readUserOrderByPaymentId(3, 0, 10)).thenReturn(List.of(order));

        Page<PaymentDto.UserOrderDto> expected = new Page<>(List.of(orderDto), new PageMeta(10, 1, 1, 1));
        Page<PaymentDto.UserOrderDto> actual = paymentService.findUserOrderByPaymentId(3, 1, 10);

        assertEquals(expected, actual);
    }

    @Test
    void testFindUserOrderByPaymentIdWithDeletedCertificate() {
        UserOrder order = UserOrder.builder()
                .orderId(1)
                .cost(new BigDecimal("40.00"))
                .giftCertificate(null)
                .build();

        PaymentDto.UserOrderDto orderDto = new PaymentDto.UserOrderDto(0, "DELETED", new BigDecimal("40.00"));

        when(paymentDao.countPaymentOrders(3)).thenReturn(1);
        when(paymentDao.readUserOrderByPaymentId(3, 0, 10)).thenReturn(List.of(order));

        Page<PaymentDto.UserOrderDto> expected = new Page<>(List.of(orderDto), new PageMeta(10, 1, 1, 1));
        Page<PaymentDto.UserOrderDto> actual = paymentService.findUserOrderByPaymentId(3, 1, 10);

        assertEquals(expected, actual);
    }

    @Test
    void testFindUserOrderByPaymentIdOnNonExistentPage() {
        when(paymentDao.countPaymentOrders(3)).thenReturn(5);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.findUserOrderByPaymentId(3, 2, 10));
    }

    private Payment createPayment() {
        User user = new User(1, "Maks", "Silev");
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftCertificateId(1)
                .name("Oz.by")
                .build();
        UserOrder order = UserOrder.builder()
                .orderId(1)
                .cost(new BigDecimal("40.00"))
                .giftCertificate(giftCertificate)
                .build();
        Payment payment = Payment.builder()
                .paymentId(1)
                .user(user)
                .createdDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .build();
        payment.setOrders(List.of(order));

        return payment;
    }
}