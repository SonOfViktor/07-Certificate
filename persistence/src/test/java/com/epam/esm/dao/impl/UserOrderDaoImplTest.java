package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserOrderDaoImplTest {
    public static final String USER_ORDER_TABLE = "module_3.orders";
    private final UserOrderDao userOrderDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserOrderDaoImplTest(UserOrderDao userOrderDao, JdbcTemplate jdbcTemplate) {
        this.userOrderDao = userOrderDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void testTableRowQuantity() {
        int actual = JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_ORDER_TABLE);
        int expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    void testCreateOrder() {
        User user = new User(1, "Ivan", "Pupkin");
        GiftCertificate giftCertificate =
                new GiftCertificate.GiftCertificateBuilder().setGiftCertificateId(4).createGiftCertificate();
        UserOrder order = new UserOrder.UserOrderBuilder()
                .setCreateDate(LocalDateTime.now())
                .setCost(new BigDecimal("20"))
                .setUser(user)
                .setGiftCertificate(giftCertificate)
                .createUserOrder();

        userOrderDao.createOrder(order);

        assertEquals(6, order.getOrderId());
        assertEquals(6, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_ORDER_TABLE));
    }

    @Test
    void testReadOrder() {
        UserOrder expected = new UserOrder.UserOrderBuilder()
                .setUserOrderId(5)
                .setCreateDate(LocalDateTime.of(2022, 5, 28, 22, 25, 17, 0))
                .setCost(new BigDecimal("20.00"))
                .createUserOrder();

        UserOrder actual = userOrderDao.readOrder(5).get();

        assertEquals(expected, actual);
    }

    @Test
    void testNonExistOrder() {
        Optional<UserOrder> actual = userOrderDao.readOrder(50);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    void testReadUserOrders() {
        List<UserOrder> expected = createUserOrderList();
        List<UserOrder> actual = userOrderDao.readUserOrder(1);

        assertEquals(expected, actual);
    }

    @Test
    void testNoUserOrders() {
        List<UserOrder> actual = userOrderDao.readUserOrder(5);

        assertEquals(Collections.emptyList(), actual);
    }

    private List<UserOrder> createUserOrderList() {
        return List.of(new UserOrder.UserOrderBuilder()
                        .setUserOrderId(1)
                        .setCreateDate(LocalDateTime.of(2022, 5, 26, 22, 25, 17, 0))
                        .setCost(new BigDecimal("20.00"))
                        .createUserOrder(),
                new UserOrder.UserOrderBuilder()
                        .setUserOrderId(2)
                        .setCreateDate(LocalDateTime.of(2022, 5, 27, 22, 25, 17, 0))
                        .setCost(new BigDecimal("10.00"))
                        .createUserOrder(),
                new UserOrder.UserOrderBuilder()
                        .setUserOrderId(3)
                        .setCreateDate(LocalDateTime.of(2022, 5, 28, 22, 25, 17, 0))
                        .setCost(new BigDecimal("5.00"))
                        .createUserOrder()
                );
    }
}