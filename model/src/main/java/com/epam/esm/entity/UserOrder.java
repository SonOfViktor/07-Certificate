package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(schema = "module_3", name = "orders")
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int orderId;

    @Positive
    @Digits(integer = 3, fraction = 2)
    private BigDecimal cost;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    public UserOrder() {
    }

    public UserOrder(int orderId, BigDecimal cost, LocalDateTime createdDate, User user, GiftCertificate giftCertificate) {
        this.orderId = orderId;
        this.cost = cost;
        this.createdDate = createdDate;
        this.user = user;
        this.giftCertificate = giftCertificate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate.truncatedTo(ChronoUnit.MILLIS);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOrder userOrder = (UserOrder) o;
        return orderId == userOrder.orderId && Objects.equals(cost, userOrder.cost) && Objects.equals(createdDate, userOrder.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, cost, createdDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", cost=" + cost +
                ", createdDate=" + createdDate +
                '}';
    }


    public static class UserOrderBuilder {
        private UserOrder userOrder;

        public UserOrderBuilder() {
            userOrder = new UserOrder();
        }

        public UserOrder.UserOrderBuilder setUserOrderId(int id) {
            userOrder.setOrderId(id);
            return this;
        }

        public UserOrder.UserOrderBuilder setCost(BigDecimal cost) {
            userOrder.setCost(cost);
            return this;
        }

        public UserOrder.UserOrderBuilder setCreateDate(LocalDateTime createdDate) {
            userOrder.setCreatedDate(createdDate);
            return this;
        }

        public UserOrder.UserOrderBuilder setUser(User user) {
            userOrder.setUser(user);
            return this;
        }

        public UserOrder.UserOrderBuilder setGiftCertificate(GiftCertificate giftCertificate) {
            userOrder.setGiftCertificate(giftCertificate);
            return this;
        }

        public UserOrder createUserOrder() {
            return userOrder;
        }
    }
}
