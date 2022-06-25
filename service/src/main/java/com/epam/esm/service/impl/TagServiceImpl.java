package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Override
    public Tag addTag(Tag tag) {
        return tagDao.save(tag);
    }

    @Override
    public Set<Tag> addTags(Set<Tag> tags) {
        return (tags != null) ? new HashSet<>(tagDao.saveAll(tags)) : Collections.emptySet();
    }

    @Override
    public Page<Tag> findAllTags(Pageable pageable) {

        return tagDao.findAll(pageable);
    }

    @Override
    public Set<Tag> findTagsByCertificateId(int certificateId) {

        return tagDao.findAllByGiftCertificatesId(certificateId);
    }

    @Override
    public Page<Tag> findTagsByCertificateId(int certificateId, Pageable pageable) {

        return tagDao.findAllByGiftCertificatesId(certificateId, pageable);
    }

    @Override
    public Tag findTagById(int tagId){
        Optional<Tag> tagOptional = tagDao.findById(tagId);

        return tagOptional.orElseThrow(() ->
                new ResourceNotFoundException("Tag with id " + tagId + " wasn't found"));
    }

    @Override
    public List<Tag> findMostPopularHighestPriceTag() {
        return tagDao.readMostPopularHighestPriceTag();
    }

    @Override
    public void deleteTag(int tagId) {
        tagDao.deleteById(tagId);
    }
}
