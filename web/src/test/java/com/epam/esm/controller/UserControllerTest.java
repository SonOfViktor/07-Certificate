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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MockConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {
    private static final String API_PREFIX = "/api/v1";
    public static final String USERS_LOGIN = "/users/login";

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void testAuthenticate() throws Exception {
        mockMvc.perform(post(API_PREFIX + USERS_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"ivan_pupkin@gmail.com\", \"password\": \"ZXCasd123\"}"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(is(1)),
                        jsonPath("$.email").value(is("ivan_pupkin@gmail.com")),
                        jsonPath("$.firstName").value(is("Ivan")),
                        jsonPath("$.lastName").value(is("Pupkin")),
                        jsonPath("$.role").value(is("ROLE_ADMIN")),
                        jsonPath("$.token").value(notNullValue(), String.class));
    }

    @Test
    @WithMockUser(username = "Ivan_Pupkin@gmail.com", password = "ZXCasd123", roles = "ADMIN")
    void testAuthenticateByAuthenticatedUser() throws Exception {
        mockMvc.perform(post(API_PREFIX + USERS_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Ivan_Pupkin@gmail.com\", \"password\": \"ZXCasd123\"}"))
                .andDo(log())
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),
                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateNoUser() throws Exception {
        mockMvc.perform(post(API_PREFIX + USERS_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Jonny_Silver@gmail.com\", \"password\": \"ZXCasd123\"}"))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Bad credentials")),
                        jsonPath("$.errorCode").value(is(40025)));
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateWrongPassword() throws Exception {
        mockMvc.perform(post(API_PREFIX + USERS_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Ivan_Pupkin@gmail.com\", \"password\": \"ZXcasd123\"}"))
                .andDo(log())
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Bad credentials")),
                        jsonPath("$.errorCode").value(is(40025)));
    }

    @Test
    @WithAnonymousUser
    void testAuthenticateNotValidEmail() throws Exception {
        mockMvc.perform(post(API_PREFIX + USERS_LOGIN).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Jonny_Silvergmail.com\", \"password\": \"ZXCasd123\"}"))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.email").value(is("must be a well-formed email address")),
                        jsonPath("$.errorCode").value(is(40015)));
    }

    @Test
    @WithAnonymousUser
    @Transactional
    void testCreateUser() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Maks_Silev@gmail.com\", " +
                                "\"password\": \"zxcASD123\", " +
                                "\"firstName\": \"Maks\", " +
                                "\"lastName\": \"Silev\"}"))
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$.email").value(is("maks_silev@gmail.com")),
                        jsonPath("$.role").value(is("USER")),
                        jsonPath("$._links.users.href").value(is("http://localhost/api/v1/users")));
    }

    @Test
    @WithAnonymousUser
    void testCreateUserWithTheSameEmail() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Ivan_Pupkin@gmail.com\", " +
                                "\"password\": \"zxcASD123\", " +
                                "\"firstName\": \"Maks\", " +
                                "\"lastName\": \"Silev\"}"))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("The user with email ivan_pupkin@gmail.com has already been registered")),
                        jsonPath("$.errorCode").value(is(40035)));
    }

    @Test
    @WithMockUser
    void testCreateUserByAuthenticatedUser() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/users/signup"))
                .andExpectAll(status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage").value(is("Access is denied")),                        jsonPath("$.errorCode").value(is(40301)));
    }

    @Test
    @WithAnonymousUser
    void testCreateUserWithNotValidData() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"Maks_Silevgmail.com\", " +
                                "\"password\": \"zxcasd123\", " +
                                "\"firstName\": \" \", " +
                                "\"lastName\": \"   \"}"))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.email").value(is("must be a well-formed email address")),
                        jsonPath("$.fieldError.password")
                                .value(is("must match \"(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})[\\p{Alnum}]{4,30}\"")),
                        jsonPath("$.fieldError.firstName").value(is("must match \"\\p{Alpha}{2,20}|[А-ЯЁа-яё]{2,20}\"")),
                        jsonPath("$.fieldError.lastName").value(is("must match \"\\p{Alpha}{2,20}|[А-ЯЁа-яё]{2,20}\"")),
                        jsonPath("$.errorCode").value(is(40015)));
    }

    @Test
    @WithAnonymousUser
    void testCreateUserWithoutRequestBody() throws Exception {
        mockMvc.perform(post(API_PREFIX + "/users/signup"))
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errorMessage").value(is("Conversion json to given object failed")),
                        jsonPath("$.errorCode").value(is(40010)));
    }

    @Test
    @WithMockUser
    void testShowAllUsers() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/users/"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..users.size()").value(2),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/users/?page=0&size=20")),
                        jsonPath("$..users[*].firstName").value(containsInAnyOrder("Ivan", "Sanek")));
    }

    @Test
    @WithMockUser
    void testShowAllUsersWithPagination() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/users/?page=1&size=1"))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..users.size()").value(1),
                        jsonPath("$._links.self.href").value(is("http://localhost/api/v1/users/?page=1&size=1")),
                        jsonPath("$..users[*].firstName").value(containsInAnyOrder("Sanek")));
    }

    @Test
    @WithAnonymousUser
    void testShowAllUsersByAnonymous() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/users/"))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Full authentication is required to access this resource")),
                        jsonPath("$.errorCode").value(is(40101)));
    }

    @Test
    @WithMockUser
    void testShowUserPayments() throws Exception{
        mockMvc.perform(get(API_PREFIX + "/users/{id}/payments", 1))
                .andDo(log())
                .andExpectAll(status().isOk(),
                        content().contentType(MediaTypes.HAL_JSON),
                        jsonPath("$..paymentDtoes.size()").value(3),
                        jsonPath("$..paymentDtoes[1].userName").value("Ivan Pupkin"),
                        jsonPath("$._links.self.href")
                                .value(is("http://localhost/api/v1/users/1/payments?page=0&size=20"))
                        );
    }

    @Test
    @WithMockUser
    void testShowUserPaymentsNotExistedUser() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/users/{id}/payments", 5))
                .andDo(log())
                .andExpectAll(status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("User with id 5 has no payments on 0 page")),
                        jsonPath("$.errorCode").value(is(40401)));
    }

    @Test
    @WithAnonymousUser
    void testShowUserPaymentsByAnonymousUser() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/users/{id}/payments", 5))
                .andDo(log())
                .andExpectAll(status().isUnauthorized(),
                        jsonPath("$.fieldError.exceptionMessage")
                                .value(is("Full authentication is required to access this resource")),
                        jsonPath("$.errorCode").value(is(40101)));
    }
}