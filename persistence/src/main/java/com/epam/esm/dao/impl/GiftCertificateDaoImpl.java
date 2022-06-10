package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaParameterMaker;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
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
    public GiftCertificate createGiftCertificate(GiftCertificate certificate) {
        LocalDateTime createTime = LocalDateTime.now();
        certificate.setCreateDate(createTime);
        certificate.setLastUpdateDate(createTime);

        entityManager.persist(certificate);

        return certificate;
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

        List<Predicate> predicates = criteriaParameterMaker
                .createPredicate(criteriaBuilder, criteria, giftCertificate, parameter);
        List<Order> orders = criteriaParameterMaker.createOrder(criteriaBuilder, giftCertificate, parameter);

        criteria.select(giftCertificate)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(orders)
                .groupBy(giftCertificate.get(GiftCertificate_.giftCertificateId));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<GiftCertificate> readGiftCertificate(int id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
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
        return entityManager.createQuery(DELETE_GIFT_CERTIFICATE_BY_ID_HQL)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }

    private GiftCertificate fillCertificateNewValues(GiftCertificate updatedCertificate, GiftCertificate updatingCertificate) {
        if(StringUtils.isNotBlank(updatingCertificate.getName())) {
            updatedCertificate.setName(updatingCertificate.getName().trim());
        }
        if(StringUtils.isNotBlank(updatingCertificate.getDescription())) {
            updatedCertificate.setDescription(updatingCertificate.getDescription().trim());
        }
        if(updatingCertificate.getPrice() != null) {
            updatedCertificate.setPrice(updatingCertificate.getPrice());
        }
        if(updatingCertificate.getDuration() > 0) {
            updatedCertificate.setDuration(updatingCertificate.getDuration());
        }
        if(!updatingCertificate.getTags().isEmpty()) {
            updatedCertificate.setTags(updatingCertificate.getTags());
        }
        updatedCertificate.setLastUpdateDate(LocalDateTime.now());

        return updatedCertificate;
    }
}
