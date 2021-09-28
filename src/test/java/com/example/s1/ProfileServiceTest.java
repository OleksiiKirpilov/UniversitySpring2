package com.example.s1;

import com.example.s1.model.Applicant;
import com.example.s1.model.User;
import com.example.s1.repository.ApplicantRepository;
import com.example.s1.repository.UserRepository;
import com.example.s1.services.ProfileService;
import com.example.s1.utils.Path;
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
@ContextConfiguration(classes = S1Application.class)
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
