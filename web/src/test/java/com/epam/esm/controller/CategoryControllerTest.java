package com.epam.esm.controller;

import com.epam.esm.config.MockConfig;
import com.epam.esm.service.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = MockConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CategoryControllerTest {
    private static final String API_PREFIX = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageService imageService;

    @Test
    void testFilter() throws Exception {
        mockMvc.perform(get("/static/js/main.0852bbd4.js"))
                .andDo(log())
                .andExpectAll(status().isOk());
    }

    @Test
    void testFindAllCategories() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/categories"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[*].name").value(contains("Tech", "Food")));
    }

    @Test
    void testFindCategoryImage() throws Exception {
        when(imageService.getImage(Mockito.any(String.class))).thenReturn(Optional.of("Hello world".getBytes()));

        mockMvc.perform(get(API_PREFIX + "/categories/{name}/image", "Tech"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @Test
    void testNotFoundCategoryImage() throws Exception {
        when(imageService.getImage(Mockito.any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(get(API_PREFIX + "/categories/{name}/image", "Tech"))
                .andDo(log())
                .andExpectAll(status().isNotFound());
    }
}