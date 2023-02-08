package com.epam.esm.config;

import com.epam.esm.service.ImageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class MockConfig {

    @MockBean
    private ImageService imageService;
}