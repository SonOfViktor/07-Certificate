package com.epam.esm.dao;

import com.epam.esm.entity.Payment;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface PaymentDao {
    /**
     * Add payment to database
     *
     * @param payment the payment to add
     * @return the payment with generated id by database
     */
    Payment createPayment(Payment payment);

    /**
     * Read payment with specified id from database
     *
     * @param paymentId id of searched payment
     * @return return payment with specified id if exist in database
     * @throws EmptyResultDataAccessException if payment wasn't found
     */
    Payment readPayment(int paymentId);

    /**
     * Read payments that made user with specified id
     *
     * @param userId id of a user
     * @return payments that user with specified id made
     */
    List<Payment> readPaymentByUserId(int userId);
}
