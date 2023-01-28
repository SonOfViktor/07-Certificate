package com.epam.esm.service.config;

import com.epam.esm.service.ImageService;
import com.epam.esm.service.impl.ImageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfig {
    @Bean
    public ImageService imageService() {

        return new ImageServiceImpl();
    }
}