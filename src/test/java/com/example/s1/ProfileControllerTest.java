package com.example.s1;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = S1Application.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void securityTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(unauthenticated());
    }


    @Test
    void shouldSetLanguage() throws Exception {
        mockMvc.perform(get("/setSessionLanguage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("welcome"));
    }

    @Test
    void shouldLogIn() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", "admin@univer.com")
                .param("password", "admin")
        ).andExpect(authenticated());
    }

    @Test
    void shouldNotLogIn() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", "admin@univer.com")
                .param("password", "admin---")
        ).andExpect(unauthenticated());
    }
//
//    @Test
//    void shouldViewProfile() throws Exception {
//        mockMvc.perform(post("/login")
//                .param("email", "admin@univer.com")
//                .param("password", "admin")
//        ).andExpect(authenticated());
//
//        mockMvc.perform(get("/viewProfile")
//        ).andExpect(authenticated());
//    }


}