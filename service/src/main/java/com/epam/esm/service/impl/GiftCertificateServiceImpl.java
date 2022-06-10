package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.PageMeta;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public GiftCertificate addGiftCertificate(GiftCertificate certificate) {
        return giftCertificateDao.createGiftCertificate(certificate);
    }

    @Override
    public Page<GiftCertificate> findAllCertificates(int page, int size) {
        PageMeta pageMeta = createPageMeta(page, size, null);

        int offset = page * size - size;
        List<GiftCertificate> certificates = giftCertificateDao.readAllCertificate(offset, size);

        return new Page<>(certificates, pageMeta);
    }

    @Override
    public Page<GiftCertificate> findCertificatesWithParams(SelectQueryParameter params, int page, int size) {
        PageMeta pageMeta = createPageMeta(page, size, params);

        int offset = page * size - size;
        List<GiftCertificate> certificates = giftCertificateDao.readGiftCertificateWithParam(params, offset, size);

        return new Page<>(certificates, pageMeta);
    }

    @Override
    public GiftCertificate findCertificateById(int certificateId) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.readGiftCertificate(certificateId);

        return certificateOptional.orElseThrow(() ->
                new ResourceNotFoundException("There is no certificate with Id " + certificateId + " in database"));
    }

    @Override
    public GiftCertificate updateGiftCertificate(GiftCertificate certificate, int id) {
        certificate.setGiftCertificateId(id);

        return giftCertificateDao.updateGiftCertificate(certificate)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate with id " + id +
                        " can't be updated. It was not found"));
    }

    @Override
    public int deleteCertificate(int id) {
        int affectedRow = giftCertificateDao.deleteGiftCertificate(id);

        if (affectedRow == 0) {
            throw new ResourceNotFoundException("Certificate with id " + id +
                    " can't be deleted. It was not found");
        }

        return affectedRow;
    }

    private PageMeta createPageMeta(int page, int size, SelectQueryParameter params) {
        int giftCertificatesTotalElements = giftCertificateDao.countGiftCertificate(params);
        int totalPages = (int) Math.ceil((double) giftCertificatesTotalElements / size);

        if (page > totalPages) {
            throw new ResourceNotFoundException("There is no certificates in the database on " + page + " page");
        }

        return new PageMeta(size, giftCertificatesTotalElements, totalPages, page);
    }
}
