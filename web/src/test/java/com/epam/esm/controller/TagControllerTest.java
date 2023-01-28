package com.epam.esm.controller;

import com.epam.esm.config.MockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MockConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TagControllerTest {
    private static final String API_PREFIX = "/api/v1";

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    void testShowAllTags() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..tags.size()").value(6),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/tags?page=0&size=20")),
                        jsonPath("$..tags[*].name")
                                .value(containsInAnyOrder("food", "stationery", "shoe", "virtual", "paper", "game")));
    }

    @Test
    @WithMockUser
    void testShowAllTagsWithPagination() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags?page=2&size=2"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..tags.size()").value(2),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/tags?page=2&size=2")),
                        jsonPath("$..tags[*].name").value(containsInAnyOrder("stationery", "virtual")));
    }

    @Test
    @WithAnonymousUser
    void testShowAllTagsByAnonymous() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags"))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Full authentication is required to access this resource")),
                        jsonPath("$.errorCode").value(is(40101)));
    }

    @Test
    @WithMockUser
    void testShowTag() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags/{id}", 3))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..name").value("shoe"),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/tags/3")));
    }

    @Test
    @WithMockUser
    void testShowNotExistedTag() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags/{id}", 10))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Tag with id 10 wasn't found")),
                        jsonPath("$.errorCode").value(is(40401)));
    }

    @Test
    @WithAnonymousUser
    void testShowTagByAnonymous() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags/{id}", 3))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Full authentication is required to access this resource")),
                        jsonPath("$.errorCode").value(is(40101)));
    }

    @Test
    @WithMockUser
    void testShowMostPopularHighestPriceTag() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/tags/highest"))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..tags[*].name")
                                .value(containsInAnyOrder("stationery", "paper", "game")));
    }

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void testAddTag() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("tag"))
                .andDo(log())
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$.name").value("tag"));

        mockMvc.perform(get(API_PREFIX + "/tags"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..tags.size()").value(7),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/tags?page=0&size=20")),
                        jsonPath("$..tags[*].name")
                                .value(containsInAnyOrder("food", "stationery", "shoe", "virtual", "paper", "game", "tag")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddTagWithNotValidBodyData() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("t"))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("addTag.tagName: size must be between 3 and 15")),
                        jsonPath("$.errorCode").value(is(40020)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddExistedTag() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("food"))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("The tag with name food has already been existed in database")),
                        jsonPath("$.errorCode").value(is(40035)));
    }

    @Test
    @WithMockUser
    void testAddTagByUserRole() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("tag"))
                .andDo(log())
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),
                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testAddTagByAnonymous() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("tag"))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Full authentication is required to access this resource")),
                        jsonPath("$.errorCode").value(is(40101)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testDeleteTag() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/tags/{id}", 1))
                .andDo(log())
                .andExpect(status().isNoContent());

        mockMvc.perform(get(API_PREFIX + "/tags"))
                .andDo(log())
                .andExpect(jsonPath("$..tags.size()").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteNotExistedTag() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/tags/{id}", 10))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("No class com.epam.esm.entity.Tag entity with id 10 exists!")),
                        jsonPath("$.errorCode").value(is(40401)));
    }

    @Test
    @WithMockUser
    void testDeleteTagByUser() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/tags/{id}", 1))
                .andDo(log())
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),
                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testDeleteTagByAnonymous() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/tags/{id}", 1))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Full authentication is required to access this resource")),
                        jsonPath("$.errorCode").value(is(40101)));
    }
}