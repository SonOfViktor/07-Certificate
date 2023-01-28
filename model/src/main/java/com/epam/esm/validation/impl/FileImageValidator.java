package com.epam.esm.validation.impl;

import com.epam.esm.validation.FileImage;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.function.Predicate;

public class FileImageValidator implements ConstraintValidator<FileImage, MultipartFile> {
    private static final String MEDIA_TYPE_PNG = "image/png";
    private static final String MEDIA_TYPE_JPG = "image/jpg";
    private static final String MEDIA_TYPE_JPEG = "image/jpeg";

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return Optional.of(value)
                .filter(Predicate.not(MultipartFile::isEmpty))
                .map(MultipartFile::getContentType)
                .map(type -> type.equals(MEDIA_TYPE_PNG) || type.equals(MEDIA_TYPE_JPG) || type.equals(MEDIA_TYPE_JPEG))
                .orElse(false);
    }
}
