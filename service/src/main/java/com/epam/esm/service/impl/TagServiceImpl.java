package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.PageMeta;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagDao.createTag(tag);
    }

    @Override
    public Set<Tag> addTags(Set<Tag> tags) {
        return (tags != null) ? tagDao.addTags(tags) : Collections.emptySet();
    }

    @Override
    public Page<Tag> findAllTags(int page, int size) {
        PageMeta pageMeta = createPageMeta(page, size, Collections.emptyMap());

        int offset = page * size - size;
        List<Tag> tags = tagDao.readAllTag(offset, size);

        return new Page<>(tags, pageMeta);
    }

    @Override
    public Set<Tag> findTagsByCertificateId(int certificateId) {
        return tagDao.readTagByCertificateId(certificateId);
    }

    @Override
    public Page<Tag> findTagsByCertificateId(int certificateId, int page, int size) {
        PageMeta pageMeta = createPageMeta(page, size, Map.of("id", certificateId));

        int offset = page * size - size;

        Set<Tag> tags = tagDao.readTagByCertificateId(certificateId, offset, size);
        return new Page<>(tags, pageMeta);
    }

    @Override
    public Tag findTagById(int tagId){
        Optional<Tag> tagOptional = tagDao.readTag(tagId);

        return tagOptional.orElseThrow(() ->
                new ResourceNotFoundException("Tag with id " + tagId + " wasn't found"));
    }

    @Override
    public List<Tag> findMostPopularHighestPriceTag() {
        return tagDao.readMostPopularHighestPriceTag();
    }

    @Override
    public int deleteTag(int tagId) {
        int affectedRow = tagDao.deleteTag(tagId);

        if (affectedRow == 0) {
            throw new ResourceNotFoundException("Tag with id " + tagId +
                    " can't be deleted. It was not found");
        }

        return affectedRow;
    }

    private PageMeta createPageMeta(int page, int limit, Map<String, Integer> params) {
        int tagTotalElements = tagDao.countTags(params);
        int totalPages = (int) Math.ceil((double) tagTotalElements / limit);

        if(page > totalPages) {
            throw new ResourceNotFoundException("There is no tags in the database for " + page + " page");
        }

        return new PageMeta(limit, tagTotalElements, totalPages, page);
    }
}
