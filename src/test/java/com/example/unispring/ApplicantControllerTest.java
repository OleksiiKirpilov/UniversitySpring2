package com.example.unispring;

import com.example.unispring.controllers.ApplicantController;
import com.example.unispring.model.Applicant;
import com.example.unispring.model.User;
import com.example.unispring.repository.ApplicantRepository;
import com.example.unispring.repository.UserRepository;
import com.example.unispring.util.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ModelMap;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = UnispringApplication.class)
class ApplicantControllerTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicantController applicantController;

    @Test
    void shouldViewApplicant() {
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
