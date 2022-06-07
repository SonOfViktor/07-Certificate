package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
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
    public List<GiftCertificate> findAllCertificates() {
        return giftCertificateDao.readAllCertificate();
    }

    @Override
    public List<GiftCertificate> findCertificatesWithParams(SelectQueryParameter params) {
        List<GiftCertificate> certificates = giftCertificateDao.readGiftCertificateWithParam(params);

        if(certificates.isEmpty()) {
            throw new ResourceNotFoundException("There is no certificates with such parameters " + params +
                    " in database");
        }

        return certificates;
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
}
