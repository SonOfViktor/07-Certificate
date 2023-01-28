package com.epam.esm.service.impl;

import com.epam.esm.service.ImageService;
import com.epam.esm.service.config.TestServiceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig(TestServiceConfig.class)
@TestPropertySource(properties = "module7.image.bucket=D:/Epam/Tasks/07 Web/certificate/image")
class ImageServiceImplTest {
    private static final String BUCKET = "D:/Epam/Tasks/07 Web/certificate/image";
    public static final String FILE_NAME = "test/test.txt";
    public static final String FILE_CONTENT = "Hello world";

    @Autowired
    private ImageService imageService;

    @Test
    void testUpload() throws IOException {
        String expected = FILE_CONTENT;
        InputStream content = new ByteArrayInputStream(expected.getBytes());
        Path file = Path.of(BUCKET, FILE_NAME);
        Path parentDir = file.getParent();

        imageService.upload(FILE_NAME, content);

        String actual = Files.readString(file);

        assertThat(actual).isEqualTo(expected);

        Files.deleteIfExists(file);
        Files.deleteIfExists(parentDir);
    }

    @Test
    void testGetImage() throws IOException {
        byte[] content = FILE_CONTENT.getBytes();
        InputStream expected = new ByteArrayInputStream(content);
        Path file = Path.of(BUCKET, FILE_NAME);
        Path parentDir = file.getParent();
        imageService.upload(FILE_NAME, expected);

        Optional<byte[]> actual = imageService.getImage(FILE_NAME);

        assertThat(actual).contains(content);

        Files.deleteIfExists(file);
        Files.deleteIfExists(parentDir);
    }

    @Test
    void testGetImageNonExistFile() {
        Optional<byte[]> actual = imageService.getImage(FILE_NAME);

        assertThat(actual).isEmpty();
    }

    @Test
    void testDeleteImage() throws IOException {
        InputStream expected = new ByteArrayInputStream(FILE_CONTENT.getBytes());
        Path file = Path.of(BUCKET, FILE_NAME);
        Path parentDir = file.getParent();
        imageService.upload(FILE_NAME, expected);

        boolean actual = imageService.deleteImage(FILE_NAME);
        assertThat(actual).isTrue();


        Files.deleteIfExists(parentDir);
    }
}