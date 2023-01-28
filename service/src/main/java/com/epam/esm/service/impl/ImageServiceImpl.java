package com.epam.esm.service.impl;

import com.epam.esm.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${module7.image.bucket}")
    private String bucket;

    @Override
    @SneakyThrows
    public void upload(String imagePath, InputStream content) {
        Path fullImagePath = Path.of(bucket, imagePath);

        try (content) {
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath, content.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }

    @Override
    @SneakyThrows
    public Optional<byte[]> getImage(String imagePath) {
        Path fullImagePath = Path.of(bucket, imagePath);

        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }

    @Override
    @SneakyThrows
    public boolean deleteImage(String imagePath) {
        Path fullImagePath = Path.of(bucket, imagePath);
        return Files.deleteIfExists(fullImagePath);
    }
}
