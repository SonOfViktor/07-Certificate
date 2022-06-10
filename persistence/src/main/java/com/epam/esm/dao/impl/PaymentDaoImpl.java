package com.epam.esm.dao.impl;

import com.epam.esm.dao.PaymentDao;
import com.epam.esm.entity.Payment;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PaymentDaoImpl implements PaymentDao {
    public static final String SELECT_PAYMENT_BY_USER_ID_HQL = """
            select p from Payment p
            join p.user u
            where u.userId = :id
            """;

    public static final String SELECT_PAYMENT_BY_ID_HQL = """
            select p from Payment p
            where p.paymentId = :id
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
    public Payment readPayment(int paymentId) {

        return entityManager.createQuery(SELECT_PAYMENT_BY_ID_HQL, Payment.class)
                .setParameter(ID, paymentId)
                .getSingleResult();
    }

    @Override
    public List<Payment> readPaymentByUserId(int userId) {
        return entityManager.createQuery(SELECT_PAYMENT_BY_USER_ID_HQL, Payment.class)
                .setParameter(ID, userId)
                .getResultList();
    }
}
