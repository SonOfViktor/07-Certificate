package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaymentDao;
import com.epam.esm.entity.Payment;
import com.epam.esm.entity.UserOrder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentDaoImpl implements PaymentDao {
    public static final String SELECT_PAYMENT_BY_USER_ID_HQL = """
            select p from Payment p
            join p.user u
            where u.userId = :id
            order by p.paymentId
            """;

    public static final String SELECT_COUNT_PAYMENT_BY_USER_ID_HQL = """
            select count(p) from Payment p
            join p.user u
            where u.userId = :id
            """;

    public static final String SELECT_COUNT_PAYMENT_ORDERS_HQL = """
            select count(o) from UserOrder o
            join o.payment p
            where p.paymentId = :id
            """;

    public static final String SELECT_ORDERS_BY_PAYMENT_ID_HQL = """
            select o from UserOrder o
            join o.payment p
            where p.paymentId = :id
            order by o.orderId
            """;

    public static final String ID = "id";

    private final EntityManager entityManager;

    public PaymentDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Payment createPayment(Payment payment) {
        entityManager.persist(payment);

        return payment;
    }

    @Override
    public Optional<Payment> readPayment(int paymentId) {

        return Optional.ofNullable(entityManager.find(Payment.class, paymentId));
    }

    @Override
    public List<Payment> readPaymentByUserId(int userId, int offset, int size) {
        return entityManager.createQuery(SELECT_PAYMENT_BY_USER_ID_HQL, Payment.class)
                .setParameter(ID, userId)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<UserOrder> readUserOrderByPaymentId(int paymentId, int offset, int size) {
        return entityManager.createQuery(SELECT_ORDERS_BY_PAYMENT_ID_HQL, UserOrder.class)
                .setParameter(ID, paymentId)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public int countUserPayments(int userId) {
        return entityManager.createQuery(SELECT_COUNT_PAYMENT_BY_USER_ID_HQL, Long.class)
                .setParameter(ID, userId)
                .getSingleResult()
                .intValue();
    }

    @Override
    public int countPaymentOrders(int paymentId) {
       return entityManager.createQuery(SELECT_COUNT_PAYMENT_ORDERS_HQL, Long.class)
               .setParameter(ID, paymentId)
               .getSingleResult()
               .intValue();
    }
}
