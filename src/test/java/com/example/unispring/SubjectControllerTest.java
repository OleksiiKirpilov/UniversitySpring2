package com.example.unispring;

import com.example.unispring.controllers.SubjectController;
import com.example.unispring.model.Subject;
import com.example.unispring.repository.SubjectRepository;
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

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = UnispringApplication.class)
class SubjectControllerTest {

    @Autowired
    SubjectController subjectController;

    @Autowired
    SubjectRepository subjectRepository;

    @Test
    void shouldListAll() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Assertions.assertEquals(Path.FORWARD_SUBJECT_VIEW_ALL_ADMIN, subjectController.listAll(request));
        verify(request, times(1)).setAttribute(anyString(), anyIterable());

    }

    @Test
    void shouldViewSubject() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ModelMap map = mock(ModelMap.class);
        List<Subject> subjects = new ArrayList<>();
        subjectRepository.findAll().forEach(subjects::add);
        Subject subject = subjects.get(0);
        String result = subjectController.viewSubject(subject.getNameEn(), map);
        Assertions.assertEquals(Path.FORWARD_SUBJECT_VIEW_ADMIN, result);
        verify(map, times(1)).put(eq("subject"), any());
    }

    @Test
    void shouldDoCrudWithSubject() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Subject subject;
        String nameEn = "TESTSUBJECT";
        String nameRu = "тестовыйпредмет";
        String result = subjectController.addSubject(nameRu, nameEn, request);
        Assertions.assertEquals(Path.REDIRECT_TO_SUBJECT + nameEn, result);
        result = subjectController.edit(nameEn, nameEn, nameRu, request);
        Assertions.assertEquals(Path.REDIRECT_TO_SUBJECT + nameEn, result);
        subject = subjectRepository.findSubjectByNameEnEquals(nameEn);
        result = subjectController.delete(subject.getId(), request);
        Assertions.assertEquals(Path.REDIRECT_TO_VIEW_ALL_SUBJECTS, result);
    }

}
