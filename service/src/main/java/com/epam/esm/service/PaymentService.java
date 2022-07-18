package com.epam.esm.service;

import com.epam.esm.dto.PaymentDto;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * The interface provided methods to manage business logic related with payments.
 */
public interface PaymentService {
    /**
     * Add payment with gift certificates made by authenticated user
     *
     * @param username the username of authenticated user
     * @param giftCertificateIdList gift certificate ids that user wants to buy
     * @return payment dto with info about made payment
     * @throws ResourceNotFoundException if user with specified name wasn't found
     */
    PaymentDto addPayment(String username, List<Integer> giftCertificateIdList);

    /**
     * Find payment with specified id
     *
     * @param paymentId id of payment in database
     * @return payment dto with info about payment with specified id
     * @throws ResourceNotFoundException if payment with specified id wasn't found
     */
    PaymentDto findPayment(int paymentId);

    /**
     * Find page with payments made by user with specified id
     *
     * @param userId id of the user that made payment
     * @param pageable the pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return page of payment dtos with info about payments made by specified user
     * @throws ResourceNotFoundException if user with specified id doesn't have any payments
     */
    Page<PaymentDto> findPaymentsByUserId(int userId, Pageable pageable);

    /**
     * Find page with orders in payment with specified id
     *
     * @param paymentId payment id
     * @param pageable the pageable to request a paged result, can be Pageable.unpaged(), must not be null.
     * @return page with order dtos with info about order in payment with specified id
     * @throws ResourceNotFoundException if payment with specified id doesn't found any orders
     */
    Page<PaymentDto.UserOrderDto> findUserOrderByPaymentId(int paymentId, Pageable pageable);

}
