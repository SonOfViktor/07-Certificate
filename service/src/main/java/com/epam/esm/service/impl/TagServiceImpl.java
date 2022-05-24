package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public int addTag(Tag tag) {
        int tagId = tagDao.createTag(tag);

        if (tagId == 0) {
            throw new ResourceNotFoundException("Tag with name " + tag.getName() + " has already existed");
        }

        return tagId;
    }

    @Override
    public long addTags(List<Tag> tags) {
        return (tags != null) ? tagDao.addTags(tags) : 0L;
    }

    @Override
    public List<Tag> findAllTags() {
        return tagDao.readAllTag();
    }

    @Override
    public List<Tag> findTagsByCertificateId(int certificateId) {
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
