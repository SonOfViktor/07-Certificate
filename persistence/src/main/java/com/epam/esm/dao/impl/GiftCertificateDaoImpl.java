package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaParameterMaker;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    public static final String SELECT_ALL_GIFT_CERTIFICATES_HQL = "select cert from GiftCertificate cert";
    public static final String DELETE_GIFT_CERTIFICATE_BY_ID_HQL =
            "delete from GiftCertificate cert where cert.giftCertificateId = :id";
    public static final String ID_PARAMETER = "id";

    private final EntityManager entityManager;
    private final CriteriaParameterMaker criteriaParameterMaker;

    @Autowired
    public GiftCertificateDaoImpl(EntityManager entityManager, CriteriaParameterMaker criteriaParameterMaker) {
        this.entityManager = entityManager;
        this.criteriaParameterMaker = criteriaParameterMaker;
    }

    @Override
    public int createGiftCertificate(GiftCertificate certificate) {
        entityManager.persist(certificate);
        return certificate.getGiftCertificateId();
    }

    @Override
    public List<GiftCertificate> readAllCertificate() {
        return entityManager.createQuery(SELECT_ALL_GIFT_CERTIFICATES_HQL, GiftCertificate.class).getResultList();
    }

    @Override
    public List<GiftCertificate> readGiftCertificateWithParam(SelectQueryParameter parameter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteria = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> giftCertificate = criteria.from(GiftCertificate.class);

        List<Predicate> predicates = criteriaParameterMaker.createPredicate(criteriaBuilder, giftCertificate, parameter);
        List<Order> orders = criteriaParameterMaker.createOrder(criteriaBuilder, giftCertificate, parameter);

        criteria.select(giftCertificate)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(orders);

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<GiftCertificate> readGiftCertificate(int id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public GiftCertificate updateGiftCertificate(GiftCertificate certificate) {
        return entityManager.merge(certificate);
    }

    @Override
    public int deleteGiftCertificate(int id) {
        return entityManager.createQuery(DELETE_GIFT_CERTIFICATE_BY_ID_HQL)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
