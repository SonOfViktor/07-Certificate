package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.criteria.CriteriaParameterMaker;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import com.epam.esm.entity.SelectQueryParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    public static final String SELECT_ALL_GIFT_CERTIFICATES_HQL = """
            select cert from GiftCertificate cert
            order by cert.giftCertificateId
            """;
    public static final String SELECT_COUNT_ALL_GIFT_CERTIFICATES_HQL = "select count(cert) from GiftCertificate cert";

    private final EntityManager entityManager;
    private final CriteriaParameterMaker criteriaParameterMaker;

    @Autowired
    public GiftCertificateDaoImpl(EntityManager entityManager, CriteriaParameterMaker criteriaParameterMaker) {
        this.entityManager = entityManager;
        this.criteriaParameterMaker = criteriaParameterMaker;
    }

    @Override
    public GiftCertificate createGiftCertificate(GiftCertificate certificate) {
        LocalDateTime createTime = LocalDateTime.now();
        certificate.setCreateDate(createTime);
        certificate.setLastUpdateDate(createTime);

        entityManager.persist(certificate);

        return certificate;
    }

    @Override
    public List<GiftCertificate> readAllCertificate(int offset, int size) {
        return entityManager.createQuery(SELECT_ALL_GIFT_CERTIFICATES_HQL, GiftCertificate.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> readGiftCertificateWithParam(SelectQueryParameter params, int offset, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteria = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> giftCertificate = criteria.from(GiftCertificate.class);

        List<Predicate> predicates = criteriaParameterMaker
                .createPredicate(criteriaBuilder, criteria, giftCertificate, params);
        List<Order> orders = criteriaParameterMaker.createOrder(criteriaBuilder, giftCertificate, params);

        criteria.select(giftCertificate)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(orders)
                .groupBy(giftCertificate.get(GiftCertificate_.giftCertificateId));

        return entityManager.createQuery(criteria)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> readGiftCertificate(int id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public int countGiftCertificate(SelectQueryParameter params) {
        return params == null ? countAllGiftCertificate() : countGiftCertificateWithParams(params);
    }

    @Override
    public Optional<GiftCertificate> updateGiftCertificate(GiftCertificate newCertificate) {
        Optional<GiftCertificate> updatedCertificate =
                Optional.ofNullable(entityManager.find(GiftCertificate.class, newCertificate.getGiftCertificateId()));

        return updatedCertificate
                .map(cert -> fillCertificateNewValues(cert, newCertificate))
                .or(Optional::empty);
    }

    @Override
    public int deleteGiftCertificate(int id) {
        int deletedCertificateId = INTEGER_ZERO;
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);

        if (certificate != null) {
            entityManager.remove(certificate);
            deletedCertificateId = certificate.getGiftCertificateId();
        }

        return deletedCertificateId;
    }

    private GiftCertificate fillCertificateNewValues(GiftCertificate updatedCertificate, GiftCertificate updatingCertificate) {
        if (StringUtils.isNotBlank(updatingCertificate.getName())) {
            updatedCertificate.setName(updatingCertificate.getName().trim());
        }
        if (StringUtils.isNotBlank(updatingCertificate.getDescription())) {
            updatedCertificate.setDescription(updatingCertificate.getDescription().trim());
        }
        if (updatingCertificate.getPrice() != null) {
            updatedCertificate.setPrice(updatingCertificate.getPrice());
        }
        if (updatingCertificate.getDuration() > 0) {
            updatedCertificate.setDuration(updatingCertificate.getDuration());
        }
        if (!updatingCertificate.getTags().isEmpty()) {
            updatedCertificate.setTags(updatingCertificate.getTags());
        }
        updatedCertificate.setLastUpdateDate(LocalDateTime.now());

        return updatedCertificate;
    }

    private int countAllGiftCertificate() {
        return entityManager.createQuery(SELECT_COUNT_ALL_GIFT_CERTIFICATES_HQL, Long.class)
                .getSingleResult()
                .intValue();
    }

    private int countGiftCertificateWithParams(SelectQueryParameter params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);

        Root<GiftCertificate> giftCertificate = criteria.from(GiftCertificate.class);

        List<Predicate> predicates = criteriaParameterMaker
                .createPredicate(criteriaBuilder, criteria, giftCertificate, params);

        criteria.select(criteriaBuilder.count(giftCertificate))
                .where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(criteria).getSingleResult().intValue();
    }
}
