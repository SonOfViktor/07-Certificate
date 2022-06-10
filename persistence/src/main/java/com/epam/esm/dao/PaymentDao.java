package com.epam.esm.dao;

import com.epam.esm.entity.Payment;
import com.epam.esm.entity.UserOrder;
import java.util.List;
import java.util.Optional;

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
     * @return return optional payment with specified id if exist in database otherwise empty optional
     */
    Optional<Payment> readPayment(int paymentId);

    /**
     * Read payments that made user with specified id
     *
     * @param userId id of a user
     * @param offset initial offset in table with payments
     * @param size amount payments to extract from table with payments
     * @return payments that user with specified id made
     */
    List<Payment> readPaymentByUserId(int userId, int offset, int size);

    /**
     * Find out amount of entries in table with payments related to user with specified id
     *
     * @param userId id of a user
     * @return amount of entries in table with payments related to user with specified id
     */
    int countUserPayments(int userId);

    /**
     * Read orders that made in payment with specified id
     *
     * @param paymentId id of a payment
     * @param offset initial offset in table with orders
     * @param size amount certificates to extract from table with orders
     * @return orders that made in payment with specified id
     */
    List<UserOrder> readUserOrderByPaymentId(int paymentId, int offset, int size);

    /**
     * Find out amount of entries in table with orders related to payment with specified id
     *
     * @param paymentId id of a payment
     * @return amount of entries in table with orders related to payment with specified id
     */
    int countPaymentOrders(int paymentId);
}
