package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public int createTag(Tag tag) {
        readTagByName(tag.getName())
                .ifPresentOrElse(t -> {}, () -> entityManager.persist(tag));

        return tag.getTagId();
    }

    @Override
    public long addTags(List<Tag> tags) {
        return tags.stream()
                .map(this::createTag)
                .filter(id -> id != 0)
                .count();
    }

    @Override
    public List<Tag> readAllTag() {
        return entityManager.createQuery(SELECT_ALL_TAGS_HQL, Tag.class).getResultList();
    }

    @Override
    public List<Tag> readAllTagByCertificateId(int certificateId) {
        return entityManager.createQuery(SELECT_TAGS_BY_GIFT_CERTIFICATE_ID_HQL, Tag.class)
                .setParameter(ID, certificateId)
                .getResultList();
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

    @Override
    public int deleteTag(int id) {
        return entityManager.createQuery(DELETE_TAG_BY_ID_HQL)
                .setParameter(ID, id)
                .executeUpdate();
    }
}
