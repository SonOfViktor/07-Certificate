package com.epam.esm.service;

import com.epam.esm.dto.PaymentDto;
import com.epam.esm.entity.Page;
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
     * Find page with payments made by user with specified id
     *
     * @param userId id of the user that made payment
     * @param page number of page with payments
     * @param size amount objects in one page
     * @return page of payment dtos with info about payments made by specified user
     */
    Page<PaymentDto> findPaymentsByUserId(int userId, int page, int size);

    /**
     * Find page with orders in payment with specified id
     *
     * @param paymentId payment id
     * @param page number of page with orders
     * @param size amount objects in one page
     * @return page with order dtos with info about order in payment with specified id
     */
    Page<PaymentDto.UserOrderDto> findUserOrderByPaymentId(int paymentId, int page, int size);

}
