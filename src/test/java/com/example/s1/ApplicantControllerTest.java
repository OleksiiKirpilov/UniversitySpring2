package com.example.s1;

import com.example.s1.controllers.ApplicantController;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = S1Application.class)
class ApplicantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileController profileController;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicantController applicantController;

    @Test
    void shouldViewApplicant() throws Exception {
        HttpSession session = mock(HttpSession.class);
        ModelMap map = mock(ModelMap.class);
        User user = userRepository.findByEmail("ivanov@gmail.com");
        Applicant ap = applicantRepository.findByUserId(user.getId());
        String result = applicantController.viewApplicant(ap.getId());
        result = applicantController.viewApplicant(ap.getId());
        Assertions.assertEquals(Path.REDIRECT_APPLICANT_PROFILE + ap.getUserId(), result);
        result = applicantController.viewApplicantPage(user.getId(), map);
        Assertions.assertEquals(Path.FORWARD_APPLICANT_PROFILE, result);
    }
}
