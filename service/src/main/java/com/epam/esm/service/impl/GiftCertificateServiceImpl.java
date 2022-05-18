package com.epam.esm.service.impl;

import com.epam.esm.builder.SelectSqlBuilder;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.SelectQueryParameter;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String NAME_DESCRIPTION_PATTERN = "%%%s%%";
    private GiftCertificateDao giftCertificateDao;
    private SelectSqlBuilder builder;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, SelectSqlBuilder builder) {
        this.giftCertificateDao = giftCertificateDao;
        this.builder = builder;
    }

    @Override
    public int addGiftCertificate(GiftCertificate certificate) {
        return giftCertificateDao.createGiftCertificate(certificate);
    }

    @Override
    public List<GiftCertificate> findAllCertificates() {
        return giftCertificateDao.readAllCertificate();
    }

    @Override
    public List<GiftCertificate> findCertificatesWithParams(SelectQueryParameter params) {
        String findCertificatesSql = builder.buildSelectGiftCertificateSQL(params);
        List<String> args = defineArguments(params);

        List<GiftCertificate> certificates = giftCertificateDao.readGiftCertificateWithParam(findCertificatesSql, args);

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
                new ResourceNotFoundException("There is no certificate with Id" + certificateId + " in database"));
    }

    @Override
    public int updateGiftCertificate(GiftCertificate certificate, int id) {
        certificate.setGiftCertificateId(id);
        int affectedRow = giftCertificateDao.updateGiftCertificate(certificate);

        if (affectedRow == 0) {
            throw new ResourceNotFoundException("Certificate with id " + id +
                    " can't be updated. It was not found");
        }

        return affectedRow;
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

    private List<String> defineArguments(SelectQueryParameter params) {
        List<String> args = new ArrayList<>();

        if(!StringUtils.isBlank(params.tagName())) {
            args.add(params.tagName());
        }

        if(!StringUtils.isBlank(params.certificateName())) {
            args.add(String.format(NAME_DESCRIPTION_PATTERN, params.certificateName()));
        }

        if(!StringUtils.isBlank(params.certificateDescription())) {
            args.add(String.format(NAME_DESCRIPTION_PATTERN, params.certificateDescription()));
        }

        return args;
    }
}
