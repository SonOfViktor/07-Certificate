package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UserOrderDao;
import com.epam.esm.dto.UserOrderDto;
import com.epam.esm.entity.GiftCertificate;
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
class UserOrderServiceImplTest {
    @InjectMocks
    private UserOrderServiceImpl userOrderService;

    @Mock
    private UserOrderDao userOrderDao;

    @Mock
    private UserDao userDao;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Test
    void testCreateOrder() {
        User user = new User(1, "Maks", "Silev");
        GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setPrice(new BigDecimal("40.00"))
                .setName("Oz.by")
                .createGiftCertificate();
        UserOrder order = new UserOrder.UserOrderBuilder()
                .setUserOrderId(1)
                .setCost(new BigDecimal("40.00"))
                .setCreateDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .setUser(user)
                .setGiftCertificate(giftCertificate)
                .createUserOrder();

        when(userDao.readUserById(1)).thenReturn(Optional.of(user));
        when(giftCertificateDao.readGiftCertificate(1)).thenReturn(Optional.of(giftCertificate));
        when(userOrderDao.createOrder(any(UserOrder.class))).thenReturn(order);

        UserOrderDto expected = new UserOrderDto(
                1, "Maks Silev", "Oz.by",
                new BigDecimal("40.00"),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0));
        UserOrderDto actual = userOrderService.createOrder(1, 1);

        assertEquals(expected, actual);
    }

    @Test
    void testCreateOrderWithoutUser() {
        when(userDao.readUserById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.createOrder(99, 1));
    }

    @Test
    void testCreateOrderWithoutGiftCertificate() {
        when(userDao.readUserById(1)).thenReturn(Optional.of(new User()));
        when(giftCertificateDao.readGiftCertificate(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.createOrder(1, 99));
    }

    @Test
    void testReadOrder() {
        User user = new User(1, "Maks", "Silev");
        GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setName("Oz.by")
                .createGiftCertificate();
        UserOrder order = new UserOrder.UserOrderBuilder()
                .setUserOrderId(1)
                .setCost(new BigDecimal("40.00"))
                .setCreateDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .setUser(user)
                .setGiftCertificate(giftCertificate)
                .createUserOrder();
        when(userOrderDao.readOrder(1)).thenReturn(Optional.of(order));

        UserOrderDto expected = new UserOrderDto(
                1, "Maks Silev", "Oz.by",
                new BigDecimal("40.00"),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0));
        UserOrderDto actual = userOrderService.readOrder(1);

        assertEquals(expected, actual);
    }

    @Test
    void testReadNonExistOrder() {
        when(userOrderDao.readOrder(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.readOrder(99));
    }

    @Test
    void testReadUserOrder() {
        User user = new User(1, "Maks", "Silev");
        GiftCertificate giftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .setGiftCertificateId(1)
                .setName("Oz.by")
                .createGiftCertificate();
        UserOrder order = new UserOrder.UserOrderBuilder()
                .setUserOrderId(1)
                .setCost(new BigDecimal("40.00"))
                .setCreateDate(LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0))
                .setUser(user)
                .setGiftCertificate(giftCertificate)
                .createUserOrder();
        when(userOrderDao.readUserOrder(1)).thenReturn(List.of(order));

        List<UserOrderDto> expected = List.of(new UserOrderDto(
                1, "Maks Silev", "Oz.by",
                new BigDecimal("40.00"),
                LocalDateTime.of(2022, 5, 29, 13, 49, 0, 0)));
        List<UserOrderDto> actual = userOrderService.readUserOrder(1);

        assertEquals(expected, actual);
    }

    @Test
    void testReadUserOrderWithNonExistUser() {
        when(userOrderDao.readUserOrder(99)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> userOrderService.readUserOrder(99));
    }
}