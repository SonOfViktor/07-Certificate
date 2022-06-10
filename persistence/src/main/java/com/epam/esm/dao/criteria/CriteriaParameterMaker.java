package com.epam.esm.dao.criteria;

import com.epam.esm.entity.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.math.NumberUtils.*;

@Component
public class CriteriaParameterMaker {
    public static final String LIKE_PATTERN = "%%%s%%";

    public List<Predicate> createPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteria,
                                           Root<GiftCertificate> root, SelectQueryParameter params) {
        List<Predicate> predicates = new ArrayList<>();

        if (!CollectionUtils.isEmpty(params.tagNames())) {
            if (params.tagNames().size() > INTEGER_ONE) {
                Path<Integer> certificateId = root.get(GiftCertificate_.giftCertificateId);

                Subquery<String> giftCertificateTags = findGiftCertificateTags(criteriaBuilder, criteria, certificateId);

                params.tagNames()
                        .forEach(tagName ->
                                predicates.add(
                                        criteriaBuilder.literal(tagName.toLowerCase()).in(giftCertificateTags))
                        );
            } else {
                SetJoin<GiftCertificate, Tag> tag = root.join(GiftCertificate_.tags);

                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(tag.get(Tag_.name)),
                                params.tagNames().get(INTEGER_ZERO).toLowerCase())
                );
            }
        }

        if (!isAllBlank(params.certificateName(), params.certificateDescription())) {
            predicates.add(createGiftCertificateParameterPredicate(criteriaBuilder, root, params));
        }

        return predicates;
    }

    public List<Order> createOrder(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root,
                                   SelectQueryParameter params) {
        List<Order> orders = new ArrayList<>();

        createGiftCertificateOrder(criteriaBuilder, root, params.orderName(), GiftCertificate_.name)
                .ifPresent(orders::add);
        createGiftCertificateOrder(criteriaBuilder, root, params.orderDate(), GiftCertificate_.createDate)
                .ifPresent(orders::add);
        orders.add(criteriaBuilder.asc(root.get(GiftCertificate_.giftCertificateId)));

        return orders;
    }

    private Subquery<String> findGiftCertificateTags(CriteriaBuilder criteriaBuilder,
                                                     CriteriaQuery<?> criteria,
                                                     Path<Integer> certificateId) {
        Subquery<String> tagNameSubquery = criteria.subquery(String.class);

        Root<Tag> tag = tagNameSubquery.from(Tag.class);
        ListJoin<Tag, GiftCertificate> giftCertificate = tag.join(Tag_.giftCertificates);

        tagNameSubquery.select(criteriaBuilder.lower(tag.get(Tag_.name)))
                .where(criteriaBuilder.equal(giftCertificate.get(GiftCertificate_.giftCertificateId), certificateId));

        return tagNameSubquery;
    }

    private Predicate createGiftCertificateParameterPredicate(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root,
                                                              SelectQueryParameter params) {
        Optional<Predicate> certificateNameContains = (!isBlank(params.certificateName())) ?
                Optional.of(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GiftCertificate_.name)),
                        String.format(LIKE_PATTERN, params.certificateName().toLowerCase()))) :
                Optional.empty();


        Optional<Predicate> certificateDescriptionContains = (!isBlank(params.certificateDescription())) ?
                Optional.of(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GiftCertificate_.description)),
                        String.format(LIKE_PATTERN, params.certificateDescription().toLowerCase()))) :
                Optional.empty();

        return (certificateNameContains.isPresent() && certificateDescriptionContains.isPresent()) ?
                criteriaBuilder.or(certificateNameContains.get(), certificateDescriptionContains.get()) :
                certificateNameContains.orElseGet(certificateDescriptionContains::get);
    }

    private Optional<Order> createGiftCertificateOrder(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root,
                                                       SelectQueryOrder queryOrder,
                                                       SingularAttribute<GiftCertificate, ?> singularAttribute) {
        return (queryOrder != null) ?
                switch (queryOrder) {
                    case ASC -> Optional.of(criteriaBuilder.asc(root.get(singularAttribute)));
                    case DESC -> Optional.of(criteriaBuilder.desc(root.get(singularAttribute)));
                } :
                Optional.empty();
    }
}
