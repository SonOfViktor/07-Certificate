package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.criteria.SubqueryMostUsedHighestPriceTagMaker;
import com.epam.esm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TagDaoImpl implements TagDao {

    public static final String SELECT_ALL_TAGS_HQL = "select tag from Tag tag order by tag.tagId";
    public static final String SELECT_TAGS_BY_GIFT_CERTIFICATE_ID_HQL = """
                    select tag from GiftCertificate cert
                    join cert.tags tag
                    where cert.giftCertificateId = :id
            """;
    public static final String DELETE_TAG_BY_ID_HQL = "delete from Tag tag where tag.tagId = :id";
    public static final String SELECT_TAG_BY_NAME_HQL = "select t from Tag t where t.name = :name";
    public static final String TAG_NAME = "name";
    public static final String ID = "id";

    private final EntityManager entityManager;
    private final SubqueryMostUsedHighestPriceTagMaker subqueryTagMaker;

    @Autowired
    public TagDaoImpl(EntityManager entityManager, SubqueryMostUsedHighestPriceTagMaker subqueryTagMaker) {
        this.entityManager = entityManager;
        this.subqueryTagMaker = subqueryTagMaker;
    }

    @Override
    public Tag createTag(Tag tag) {
        readTagByName(tag.getName())
                .ifPresentOrElse(t -> tag.setTagId(t.getTagId()), () -> entityManager.persist(tag));

        return tag;
    }

    @Override
    public Set<Tag> addTags(Set<Tag> tags) {
        return tags.stream()
                .map(this::createTag)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Tag> readAllTag() {
        return entityManager.createQuery(SELECT_ALL_TAGS_HQL, Tag.class).getResultList();
    }

    @Override
    public Set<Tag> readAllTagByCertificateId(int certificateId) {
        List<Tag> tags = entityManager.createQuery(SELECT_TAGS_BY_GIFT_CERTIFICATE_ID_HQL, Tag.class)
                .setParameter(ID, certificateId)
                .getResultList();

        return new HashSet<>(tags);
    }

    @Override
    public Optional<Tag> readTag(int id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    public Optional<Tag> readTagByName(String name) {
        List<Tag> tags = entityManager.createQuery(SELECT_TAG_BY_NAME_HQL, Tag.class)
                .setParameter(TAG_NAME, name)
                .getResultList();

        return (!tags.isEmpty()) ? Optional.of(tags.get(0)) : Optional.empty();
    }

    public List<Tag> readMostPopularHighestPriceTag() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteria = criteriaBuilder.createQuery(Tag.class);

        Subquery<BigDecimal> maxPrice = subqueryTagMaker.createMaxPriceSubquery(criteriaBuilder, criteria);
        Subquery<Long> tagCount = subqueryTagMaker.createTagCountWithMaxPriceSubquery(criteriaBuilder, criteria, maxPrice);

        Root<Tag> tag = criteria.from(Tag.class);
        ListJoin<Tag, GiftCertificate> giftCertificate = tag.join(Tag_.giftCertificates);
        ListJoin<GiftCertificate, UserOrder> userOrder = giftCertificate.join(GiftCertificate_.userOrders);

        criteria.select(tag)
                .where(criteriaBuilder.equal(userOrder.get(UserOrder_.cost), maxPrice))
                .groupBy(tag.get(Tag_.tagId), tag.get(Tag_.name))
                .having(
                        criteriaBuilder.greaterThanOrEqualTo(
                                criteriaBuilder.count(tag.get(Tag_.name)), criteriaBuilder.all(tagCount))
                );

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public int deleteTag(int id) {
        return entityManager.createQuery(DELETE_TAG_BY_ID_HQL)
                .setParameter(ID, id)
                .executeUpdate();
    }
}
