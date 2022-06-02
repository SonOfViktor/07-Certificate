package com.epam.esm.entity;

import com.epam.esm.listener.AuditListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "payments", schema = "module_3")
@EntityListeners(AuditListener.class)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int paymentId;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
    private List<UserOrder> orders;

    public Payment() {
    }

    public Payment(int paymentId, LocalDateTime createdDate, User user) {
        this.paymentId = paymentId;
        this.createdDate = createdDate;
        this.user = user;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int id) {
        this.paymentId = id;
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

    public List<UserOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<UserOrder> orders) {
        this.orders = orders;
        this.orders.forEach(order -> order.setPayment(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return paymentId == payment.paymentId && Objects.equals(createdDate, payment.createdDate) && Objects.equals(user, payment.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, createdDate, user);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + paymentId +
                ", createdDate=" + createdDate +
                '}';
    }

    public static class PaymentBuilder {
        private Payment payment;

        public PaymentBuilder() {
            payment = new Payment();
        }

        public Payment.PaymentBuilder setPaymentId(int id) {
            payment.setPaymentId(id);
            return this;
        }

        public Payment.PaymentBuilder setCreatedDate(LocalDateTime createdDate) {
            payment.setCreatedDate(createdDate);
            return this;
        }

        public Payment.PaymentBuilder setUser(User user) {
            this.payment.setUser(user);
            return this;
        }

        public Payment.PaymentBuilder setUserOrder(List<UserOrder> userOrders) {
            payment.setOrders(userOrders);
            return this;
        }

        public Payment createPayment() {
            return payment;
        }
    }
}
