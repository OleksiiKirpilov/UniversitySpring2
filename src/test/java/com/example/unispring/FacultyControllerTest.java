package com.example.unispring;

import com.example.unispring.controllers.FacultyController;
import com.example.unispring.model.Faculty;
import com.example.unispring.repository.FacultyRepository;
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
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = UnispringApplication.class)
class FacultyControllerTest {

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void shouldViewFaculty() {
        HttpSession session = mock(HttpSession.class);
        ModelMap map = mock(ModelMap.class);
        Faculty faculty = new Faculty("Тестовый факультет", "Test faculty", 1, 2);
        facultyRepository.save(faculty);
        String result = facultyController.view(faculty.getNameEn(), map, session);
        facultyRepository.delete(faculty);
        Assertions.assertEquals(Path.FORWARD_FACULTY_VIEW_USER, result);
    }

    @Test
    void shouldViewFacultyAdmin() {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn("admin");
        ModelMap map = mock(ModelMap.class);
        Faculty faculty = new Faculty("Тестовый факультет", "Test faculty", 1, 2);
        facultyRepository.save(faculty);
        String result = facultyController.view(faculty.getNameEn(), map, session);
        facultyRepository.delete(faculty);
        Assertions.assertEquals(Path.FORWARD_FACULTY_VIEW_ADMIN, result);
    }

    @Test
    void shouldViewAllFaculties() {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        String result = facultyController.list(session, request);
        Assertions.assertEquals(Path.FORWARD_FACULTY_VIEW_ALL_USER, result);
    }

    @Test
    void shouldDoCrudWithFaculty() {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(session.getAttribute("userRole")).thenReturn("admin");
        Faculty faculty = new Faculty("Тестовый факультет", "Test faculty", 1, 2);
        Long[] subjects = new Long[1];
        subjects[0] = 1L;
        String[] stringSubjects = {"1"};
        String result = facultyController.add(faculty.getNameEn(), faculty.getNameRu(),
                String.valueOf(faculty.getBudgetPlaces()), String.valueOf(faculty.getTotalPlaces()),
                subjects, request);
        Assertions.assertEquals(Path.REDIRECT_TO_FACULTY + faculty.getNameEn(), result);
        faculty = facultyRepository.findByNameEn(faculty.getNameEn());
        result = facultyController.editFaculty(faculty.getNameEn(), faculty.getNameRu(),
                String.valueOf(faculty.getTotalPlaces()), String.valueOf(faculty.getBudgetPlaces()),
                faculty.getNameEn(), stringSubjects, new String[0], request);
        Assertions.assertEquals(Path.REDIRECT_TO_FACULTY + faculty.getNameEn(), result);
        result = facultyController.deleteFaculty(faculty.getId(), session);
        Assertions.assertEquals(Path.REDIRECT_TO_VIEW_ALL_FACULTIES, result);
    }

}
