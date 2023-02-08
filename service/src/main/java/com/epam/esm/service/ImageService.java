package com.epam.esm.service;

import java.io.InputStream;
import java.util.Optional;

public interface ImageService {

    /**
     * Upload image to specified path
     *
     * @param imagePath path of image file
     * @param content input stream of image
     */
    void upload(String imagePath, InputStream content);

    /**
     * Load bytes from image file with specified path
     *
     * @param imagePath path of image file
     * @return optional with byte of image file if it is
     */
    Optional<byte[]> getImage(String imagePath);

    /**
     * Delete image file with specified path
     *
     * @param imagePath path of image file
     * @return true if delete successful
     */
    boolean deleteImage(String imagePath);
}