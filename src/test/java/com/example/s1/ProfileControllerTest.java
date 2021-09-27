package com.example.s1;

import com.example.s1.controllers.ProfileController;
import com.example.s1.model.Applicant;
import com.example.s1.model.User;
import com.example.s1.repository.ApplicantRepository;
import com.example.s1.repository.UserRepository;
import com.example.s1.utils.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

    @Autowired
    private ProfileController profileController;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private UserRepository userRepository;

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
    void shouldLogInLogOut() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", "admin@univer.com")
                .param("password", "admin")
        ).andExpect(authenticated());
        mockMvc.perform(post("/logout")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldNotLogIn() throws Exception {
        mockMvc.perform(post("/login")
                .param("email", "admin@univer.com")
                .param("password", "admin---")
        ).andExpect(unauthenticated());
    }

    @Test
    void shouldViewProfile() throws Exception {
        HttpSession session = mock(HttpSession.class);
        ModelMap map = mock(ModelMap.class);
        when(session.getAttribute("user")).thenReturn("admin@univer.com");
        Assertions.assertEquals(Path.FORWARD_ADMIN_PROFILE, profileController.viewProfile(session, map));
        when(session.getAttribute("user")).thenReturn("ivanov@gmail.com");
        Assertions.assertEquals(Path.FORWARD_USER_PROFILE, profileController.viewProfile(session, map));
    }

    @Test
    void shouldDoUserRegistration() {
        HttpSession session = mock(HttpSession.class);
        String email = "testuser@test.test";
        String password = "testpassword";
        String firstName = "firstname";
        String lastName = "lastname";
        String lang = "en";
        String city = "city";
        String district = "district";
        String school = "school";
        String result = profileController.addUser(email, password, firstName,
                lastName, lang, city, district, school, session);
        User user = userRepository.findByEmail(email);
        Applicant applicant = applicantRepository.findByUserId(user.getId());
        applicantRepository.delete(applicant);
        userRepository.delete(user);
        Assertions.assertEquals(Path.REDIRECT_TO_PROFILE, result);
    }

    @Test
    void shouldDoAdminRegistration() {
        HttpSession session = mock(HttpSession.class);
        String email = "testadmin@testadmin.testadmin";
        String password = "testpassword";
        String firstName = "firstname";
        String lastName = "lastname";
        String lang = "en";
        String result = profileController.addAdmin(email, password, firstName,
                lastName, lang, session);
        User user = userRepository.findByEmail(email);
        userRepository.delete(user);
        Assertions.assertEquals(Path.REDIRECT_TO_PROFILE, result);
    }

}