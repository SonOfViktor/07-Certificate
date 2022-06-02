package com.epam.esm.service;

import com.epam.esm.dto.PaymentDto;
import java.util.List;

public interface PaymentService {
    /**
     * Add payment with gift certificates made by specified user
     *
     * @param userId if of a user that makes a payment
     * @param giftCertificateIdList gift certificate ids that user wants to buy
     * @return payment dto with info about made payment
     */
    PaymentDto addPayment(int userId, List<Integer> giftCertificateIdList);

    /**
     * Find payment with specified id
     *
     * @param paymentId id of payment in database
     * @return payment dto with info about payment with specified id
     */
    PaymentDto findPayment(int paymentId);

    /**
     * Find payment made by user with specified id
     *
     * @param userId id of the user that made payment
     * @return list payment dtos with info about payments made by specified user
     */
    List<PaymentDto> findPaymentsByUserId(int userId);

}
