package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
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
    public List<Tag> findAllTags() {
        return tagDao.readAllTag();
    }

    @Override
    public Set<Tag> findTagsByCertificateId(int certificateId) {
        return tagDao.readAllTagByCertificateId(certificateId);
    }

    @Override
    public Tag findTagById(int tagId){
        Optional<Tag> tagOptional = tagDao.readTag(tagId);

        return tagOptional.orElseThrow(() ->
                new ResourceNotFoundException("Tag with id " + tagId + " wasn't found"));
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
}
