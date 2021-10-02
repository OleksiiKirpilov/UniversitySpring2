package com.example.unispring;

import com.example.unispring.model.Applicant;
import com.example.unispring.model.User;
import com.example.unispring.repository.ApplicantRepository;
import com.example.unispring.repository.UserRepository;
import com.example.unispring.services.ProfileService;
import com.example.unispring.util.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = UnispringApplication.class)
class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldEditUserProfile() {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn("user");
        User user = userRepository.findByEmail("ivanov@gmail.com");
        Applicant ap = applicantRepository.findByUserId(user.getId());
        String result = profileService.editProfile(user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getLang(), session,
                ap.getCity(), ap.getDistrict(), ap.getSchool());
        Assertions.assertEquals(Path.REDIRECT_TO_PROFILE, result);
    }

    @Test
    void shouldEditAdminProfile() {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn("admin");
        User user = userRepository.findByEmail("admin@univer.com");
        String result = profileService.editProfile(user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getLang(), session,
                null, null, null);
        Assertions.assertEquals(Path.REDIRECT_TO_PROFILE, result);
    }

}
