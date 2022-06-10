package com.epam.esm.entity;

import com.epam.esm.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(schema = "module_3", name = "orders")
@EntityListeners(AuditListener.class)
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int orderId;

    @Positive
    @Digits(integer = 3, fraction = 2)
    private BigDecimal cost;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    public UserOrder() {
    }

    public UserOrder(int orderId, BigDecimal cost, Payment payment, GiftCertificate giftCertificate) {
        this.orderId = orderId;
        this.cost = cost;
        this.payment = payment;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

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
        return Objects.equals(cost, userOrder.cost) && Objects.equals(payment, userOrder.payment) && Objects.equals(giftCertificate, userOrder.giftCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, payment, giftCertificate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", cost=" + cost +
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

        public UserOrder.UserOrderBuilder setPayment(Payment payment) {
            userOrder.setPayment(payment);
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
