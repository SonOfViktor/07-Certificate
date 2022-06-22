package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.PaymentDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Payment;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaymentDaoImplTest {
    public static final String PAYMENTS_TABLE = "module_4.payments";
    public static final String USER_ORDER_TABLE = "module_4.orders";
    private final PaymentDao paymentDao;
    private final GiftCertificateDao giftCertificateDao;
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentDaoImplTest(PaymentDao paymentDao, GiftCertificateDao giftCertificateDao,
                              JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.paymentDao = paymentDao;
        this.giftCertificateDao = giftCertificateDao;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Test
    void testPaymentTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, PAYMENTS_TABLE);
        int expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    void testUserOrderTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_ORDER_TABLE);
        int expected = 9;
        assertEquals(expected, actual);
    }

    @Test
    void testCreatePayment() {
        User user = new User(1, "Ivan", "Pupkin");
        GiftCertificate giftCertificate =
                GiftCertificate.builder().giftCertificateId(4).build();
        UserOrder order1 = UserOrder.builder()
                .cost(new BigDecimal("20"))
                .giftCertificate(giftCertificate)
                .build();
        UserOrder order2 = UserOrder.builder()
                .cost(new BigDecimal("30"))
                .giftCertificate(giftCertificate)
                .build();
        Payment payment = Payment.builder()
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();
        payment.setOrders(List.of(order1, order2));

        paymentDao.createPayment(payment);
        entityManager.flush();

        assertEquals(6, payment.getPaymentId());
        assertEquals(10, order1.getOrderId());
        assertEquals(11, order2.getOrderId());
        assertEquals(6, JdbcTestUtils.countRowsInTable(jdbcTemplate, PAYMENTS_TABLE));
        assertEquals(11, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_ORDER_TABLE));
    }

    @Test
    void testReadPayment() {
        GiftCertificate certificate = GiftCertificate.builder()
                .giftCertificateId(4)
                .name("Evroopt")
                .description("Buy two bananas")
                .price(new BigDecimal("20.00"))
                .duration(10)
                .createDate(LocalDateTime.of(2022, 4, 10, 13, 48, 14, 0))
                .lastUpdateDate(LocalDateTime.of(2022, 4, 10, 13, 48, 14, 0))
                .build();


        Payment expected = Payment.builder()
                .paymentId(4)
                .createdDate(LocalDateTime.of(2022, 5, 26, 22, 25, 17, 0))
                .user(new User(2, "Sanek", "Lupkin"))
                .build();

        UserOrder order1 = UserOrder.builder()
                .orderId(6)
                .cost(new BigDecimal("20.00"))
                .giftCertificate(certificate)
                .payment(expected)
                .build();

        UserOrder order2 = UserOrder.builder()
                .orderId(7)
                .cost(new BigDecimal("20.00"))
                .giftCertificate(certificate)
                .payment(expected)
                .build();

        Payment actual = paymentDao.readPayment(4).get();

        assertEquals(expected, actual);
        assertEquals(List.of(order1, order2), actual.getOrders());
    }

    @Test
    void testNonExistOrder() {
        assertTrue(paymentDao.readPayment(50).isEmpty());
    }

    @Test
    void testReadUserOrderByPaymentId() {
        List<UserOrder> expected = createUserOrderList();
        List<UserOrder> actual = paymentDao.readUserOrderByPaymentId(2, 0, 4);

        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("payment", "giftCertificate")
                .isEqualTo(expected);
        assertThat(actual.get(0).getGiftCertificate().getName()).isEqualTo("Belvest");
        assertThat(actual.get(1).getPayment().getCreatedDate())
                .isEqualTo(LocalDateTime.of(2022, 5, 27, 22, 25, 17,0));
    }

    @Test
    void testReadUserOrders() {
        List<Payment> expected = createPaymentList();
        List<Payment> actual = paymentDao.readPaymentByUserId(1, 0, 3);

        assertEquals(expected, actual);
    }

    @Test
    void testNoUserOrders() {
        List<Payment> actual = paymentDao.readPaymentByUserId(5, 0 , 3);

        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void testOrderWithDeletedGiftCertificate() {
        giftCertificateDao.deleteGiftCertificate(4);
        entityManager.flush();

        Payment payment = paymentDao.readPayment(3).get();
        GiftCertificate actual = payment.getOrders().get(0).getGiftCertificate();

        assertNull(actual);
    }

    @Test
    void testCountUserPayments() {
        int actual = paymentDao.countUserPayments(1);

        assertEquals(3, actual);
    }

    @Test
    void testCountNonexistentUserPayments() {
        int actual = paymentDao.countUserPayments(3);

        assertEquals(0, actual);
    }

    @Test
    void testCountPaymentOrders() {
        int actual = paymentDao.countPaymentOrders(4);

        assertEquals(2, actual);
    }

    @Test
    void testCountNonexistentPaymentOrders() {
        int actual = paymentDao.countPaymentOrders(44);

        assertEquals(0, actual);
    }

    private List<Payment> createPaymentList() {
        User user = new User(1, "Ivan", "Pupkin");
        return List.of(Payment.builder()
                        .paymentId(1)
                        .createdDate(LocalDateTime.of(2022, 5, 26, 22, 25, 17, 0))
                        .user(user)
                        .build(),
                Payment.builder()
                        .paymentId(2)
                        .createdDate(LocalDateTime.of(2022, 5, 27, 22, 25, 17, 0))
                        .user(user)
                        .build(),
                Payment.builder()
                        .paymentId(3)
                        .createdDate(LocalDateTime.of(2022, 5, 28, 22, 25, 17, 0))
                        .user(user)
                        .build()
                );
    }

    private List<UserOrder> createUserOrderList() {
        return List.of(UserOrder.builder()
                        .orderId(3)
                        .cost(new BigDecimal("5.00"))
                        .build(),
                UserOrder.builder()
                        .orderId(4)
                        .cost(new BigDecimal("20.00"))
                        .build());
    }
}