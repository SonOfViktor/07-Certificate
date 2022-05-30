package com.epam.esm.dao.criteria;

import com.epam.esm.entity.*;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.math.BigDecimal;

@Component
public class SubqueryMostUsedHighestPriceTagMaker {
    public Subquery<Long> createTagCountWithMaxPriceSubquery(
            CriteriaBuilder criteriaBuilder, CriteriaQuery<Tag> criteria, Subquery<BigDecimal> maxPrice) {
        Subquery<Long> tagCount = criteria.subquery(Long.class);

        Root<UserOrder> userOrder = tagCount.from(UserOrder.class);
        Join<UserOrder, GiftCertificate> giftCertificate = userOrder.join(UserOrder_.giftCertificate);
        SetJoin<GiftCertificate, Tag> tag = giftCertificate.join(GiftCertificate_.tags);

        tagCount.select(criteriaBuilder.count(tag.get(Tag_.name)))
                .where(criteriaBuilder.equal(userOrder.get(UserOrder_.cost), maxPrice))
                .groupBy(tag.get(Tag_.name));

        return tagCount;
    }

    public Subquery<BigDecimal> createMaxPriceSubquery(CriteriaBuilder criteriaBuilder, CriteriaQuery<Tag> criteria) {
        Subquery<BigDecimal> maxPrice = criteria.subquery(BigDecimal.class);

        Root<UserOrder> userOrderMaxPrice = maxPrice.from(UserOrder.class);

        maxPrice.select(criteriaBuilder.max(userOrderMaxPrice.get(UserOrder_.cost)));

        return maxPrice;
    }
}
