package com.epam.esm.dao.criteria;

import com.epam.esm.entity.*;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;

@Component
public class CriteriaParameterMaker {
    public static final String LIKE_PATTERN = "%%%s%%";

    public List<Predicate> createPredicate(CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root,
                                           SelectQueryParameter params) {
        List<Predicate> predicates = new ArrayList<>();

        if (!isBlank(params.tagName())) {
            ListJoin<GiftCertificate, Tag> tag = root.join(GiftCertificate_.tags);
            predicates.add(
                    criteriaBuilder.equal(
                            criteriaBuilder.lower(tag.get(Tag_.name)),
                            params.tagName().toLowerCase()
                    )
            );
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

        return orders;
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
