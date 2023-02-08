package com.epam.esm.controller;

import com.epam.esm.config.MockConfig;
import com.epam.esm.service.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MockConfig.class ,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GiftCertificateControllerTest {
    private static final String API_PREFIX = "/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageService imageService;

    @Test
    void testShowCertificate() throws Exception {
        mockMvc.perform(get( API_PREFIX + "/certificates/{id}", 2))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$.name").value("Belvest"),
                        jsonPath("$._links.self.href")
                                .value("http://localhost/api/v1/certificates/2"));
    }

    @Test
    void testShowNotExistedCertificate() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/certificates/{id}", 10))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("There is no certificate with Id 10 in database")),
                        jsonPath("$.errorCode").value(is(40401)));
    }

    @Test
    void testShowCertificateNotValidId() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/certificates/{id}", -10))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("showCertificate.id: must be greater than 0")),
                        jsonPath("$.errorCode").value(is(40020)));
    }

    @Test
    void testShowTagWithCertificateId() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/certificates/{id}/tags", 2))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..tags.size()").value(3),
                        jsonPath("$..tags[*].name").value(containsInAnyOrder("shoe", "paper", "game")),
                        jsonPath("$._links.self.href")
                                .value("http://localhost/api/v1/certificates/2/tags?page=0&size=20")

                );
    }

    @Test
    void testShowTagWithNotExistedCertificateId() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/certificates/{id}/tags", 10))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Certificate with id 10 has no tags on 0 page")),
                        jsonPath("$.errorCode").value(is(40401)));
    }

    @Test
    void testFindCertificateImage() throws Exception {
        when(imageService.getImage(Mockito.any(String.class))).thenReturn(Optional.of("Hello world".getBytes()));

        mockMvc.perform(get(API_PREFIX + "/certificates/{id}/image", 1))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

    @Test
    void testNotFoundCertificateImage() throws Exception {
        when(imageService.getImage(Mockito.any(String.class))).thenReturn(Optional.empty());

        mockMvc.perform(get(API_PREFIX + "/certificates/{id}/image", 1))
                .andDo(log())
                .andExpectAll(status().isNotFound());
    }

    @Test
    void testShowCertificateWithFilter() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/certificates?sort=name,asc&sort=createDate,desc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"certificateDescription" : "tw",
                                "certificateName" : "eV",
                                "tagNames" : ["paper", "game"]}"""))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..certificateTagsDtoes.size()").value(3),
                        jsonPath("$..certificateTagsDtoes[*].name").value(contains("Belvest", "Evroopt", "Evroopt")),
                        jsonPath("$..certificateTagsDtoes[2].description")
                                .value("Buy two bananas"),
                        jsonPath("$._links.self.href")
                                .value(is("http://localhost/api/v1/certificates?page=0&size=20&sort=name,asc&sort=createDate,desc")));
    }

    @Test
    void testShowCertificateWithoutFilter() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/certificates"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..certificateTagsDtoes.size()").value(4),
                        jsonPath("$..certificateTagsDtoes[*].name")
                                .value(containsInAnyOrder("Oz.by", "Belvest", "Evroopt", "Evroopt")),
                        jsonPath("$._links.self.href")
                                .value(is("http://localhost/api/v1/certificates?page=0&size=20")));
    }

    @Test
    void testShowCertificateWithFilterNotFound() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"certificateDescription\" : \"abra-codabra\"}"))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("There is no certificates with such filter " +
                                        "GiftCertificateFilter[tagNames=null, certificateName=null, " +
                                        "certificateDescription=abra-codabra, category=null] in database")),
                        jsonPath("$.errorCode").value(40401));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testAddCertificate() throws Exception {
        MockMultipartFile filePart
                = new MockMultipartFile(
                "image",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart(API_PREFIX + "/certificates/creating").file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("name", "Example")
                        .param("description", "some descriptions")
                        .param("price", "20")
                        .param("duration", "10")
                        .param("category", "Food")
                        .param("tags", "mobile", "game"))
                .andDo(log())
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$.name").value(is("Example")),
                        jsonPath("$.image").value(matchesRegex("^certificate.Example.*\\.jpg$")),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/certificates/5")));

        mockMvc.perform(get(API_PREFIX + "/tags"))
                .andDo(log())
                .andExpectAll(
                        jsonPath("$..tags.size()").value(7),
                        jsonPath("$..tags[*].name")
                                .value(containsInAnyOrder("food", "stationery", "shoe", "virtual", "paper", "game", "mobile")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testAddCertificateWithWrongFileMediaType() throws Exception {
        MockMultipartFile filePart
                = new MockMultipartFile(
                "image",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart(API_PREFIX + "/certificates/creating").file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("name", "Epam")
                        .param("description", "some descriptions")
                        .param("price", "20")
                        .param("duration", "10")
                        .param("category", "Food")
                        .param("tags", "mobile", "game"))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errorMessage")
                                .value(is("Opps, something went wrong")),
                        jsonPath("$.errorCode").value(40099));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testAddCertificateWithNotValidBodyData() throws Exception {
        MockMultipartFile filePart
                = new MockMultipartFile(
                "image",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart(API_PREFIX + "/certificates/creating").file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("name", "    ")
                        .param("description", "0")
                        .param("price", "0")
                        .param("duration", "-10")
                        .param("category", "")
                        .param("tags", null, " "))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errorMessage")
                                .value(is("Opps, something went wrong")),
                        jsonPath("$.errorCode").value(40099));
    }

    @Test
    @WithMockUser
    void testAddCertificateByUserRole() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/certificates/creating"))
                .andDo(log())
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),
                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testAddCertificateByAnonymous() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/certificates/creating"))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value("Full authentication is required to access this resource"),
                        jsonPath("$.errorCode").value(40101));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testUpdateCertificate() throws Exception {
        mockMvc.perform(patch(API_PREFIX + "/certificates/{id}", 2)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("name", "MaxCompany")
                        .param("description", "Update success")
                        .param("price", "99")
                        .param("duration", "99")
                        .param("category", "Tech")
                        .param("tags", "new", "food"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$.name").value(is("MaxCompany")),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/certificates/2")));

        mockMvc.perform(get(API_PREFIX + "/certificates/2/tags"))
                .andDo(log())
                .andExpect(jsonPath("$..tags[*].name")
                        .value(containsInAnyOrder("food", "new")));

        mockMvc.perform(get(API_PREFIX + "/tags"))
                .andDo(log())
                .andExpectAll(
                        jsonPath("$..tags.size()").value(7),
                        jsonPath("$..tags[*].name")
                                .value(containsInAnyOrder("food", "stationery", "shoe", "virtual", "paper", "game", "new")));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testUpdateCertificateTags() throws Exception {
        mockMvc.perform(patch(API_PREFIX + "/certificates/{id}", 2)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("tags", "new", "food"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$.name").value(is("Belvest")),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/certificates/2")));

        mockMvc.perform(get(API_PREFIX + "/certificates/2/tags"))
                .andDo(log())
                .andExpect(jsonPath("$..tags[*].name")
                        .value(containsInAnyOrder("food", "new")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testUpdateNotExistedCertificate() throws Exception {
        mockMvc.perform(patch(API_PREFIX + "/certificates/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Certificate with id 10 can't be updated. It was not found")));
    }

    @Test
    @WithMockUser
    void testUpdateCertificateByUserRole() throws Exception {
        mockMvc.perform(patch(API_PREFIX + "/certificates/{id}", 4))
                .andDo(log())
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),
                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testUpdateCertificateByAnonymousUser() throws Exception {
        mockMvc.perform(patch(API_PREFIX + "/certificates/{id}", 3))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value("Full authentication is required to access this resource"),
                        jsonPath("$.errorCode").value(40101));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void testDeleteCertificate() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/certificates/{id}", 1))
                .andDo(log())
                .andExpect(status().isNoContent());

        mockMvc.perform(post(API_PREFIX + "/certificates"))
                .andDo(log())
                .andExpectAll(
                        jsonPath("$..certificateTagsDtoes.size()").value(3),
                        jsonPath("$..certificateTagsDtoes[*].name")
                                .value(containsInAnyOrder("Belvest", "Evroopt", "Evroopt")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteNotExistedCertificate() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/certificates/{id}", 10))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("No class com.epam.esm.entity.GiftCertificate entity with id 10 exists!")));
    }

    @Test
    @WithMockUser
    void testDeleteCertificateByUserRole() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/certificates/{id}", 10))
                .andDo(log())
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),
                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testDeleteCertificateByAnonymous() throws Exception {
        mockMvc.perform(delete(API_PREFIX + "/certificates/{id}", 10))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value("Full authentication is required to access this resource"),
                        jsonPath("$.errorCode").value(40101));
    }
}