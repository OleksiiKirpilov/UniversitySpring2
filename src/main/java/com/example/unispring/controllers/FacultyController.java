package com.example.unispring.controllers;

import com.example.unispring.model.Faculty;
import com.example.unispring.model.Subject;
import com.example.unispring.repository.*;
import com.example.unispring.services.FacultyService;
import com.example.unispring.services.ReportService;
import com.example.unispring.util.InputValidator;
import com.example.unispring.util.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.unispring.util.MessageHelper.ERROR_FILL_ALL_FIELDS;
import static com.example.unispring.util.MessageHelper.setErrorMessage;

/**
 * Faculty related controller
 */
@Slf4j
@Controller
public class FacultyController {

    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    FacultySubjectsRepository facultySubjectsRepository;
    @Autowired
    FacultyApplicantsRepository facultyApplicantsRepository;
    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    ReportSheetRepository reportSheetRepository;
    @Autowired
    ReportService reportService;
    @Autowired
    FacultyService facultyService;


    @GetMapping("/viewAllFaculties")
    public String list(HttpSession session, HttpServletRequest request) {
        return facultyService.viewAllFaculties(session, request);
    }

    @GetMapping("/viewFaculty")
    public String view(@RequestParam(name = "name_en") String nameEn,
                       ModelMap map, HttpSession session) {
        return facultyService.viewFaculty(nameEn, map, session);
    }


    @GetMapping("/addFaculty")
    public String addFacultyPage(HttpServletRequest request) {
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        request.setAttribute("allSubjects", allSubjects);
        return Path.FORWARD_FACULTY_ADD_ADMIN;
    }

    @PostMapping("/addFaculty")
    public String add(@RequestParam(name = "name_en") String nameEn,
                      @RequestParam(name = "name_ru") String nameRu,
                      @RequestParam(name = "budget_places") String budgetPlaces,
                      @RequestParam(name = "total_places") String totalPlaces,
                      @RequestParam Long[] subjects, HttpServletRequest request) {
        return facultyService.addFaculty(nameEn, nameRu, budgetPlaces, totalPlaces, subjects, request);
    }

    @PostMapping("/deleteFaculty")
    public String deleteFaculty(@RequestParam Long id, HttpSession session) {
        return facultyService.deleteFaculty(id, session);
    }

    @GetMapping("/editFaculty")
    public String editFacultyPage(@RequestParam(name = "name_en") String nameEn,
                                  ModelMap map) {
        Faculty faculty = facultyRepository.findByNameEn(nameEn);
        map.put("faculty", faculty);
        Iterable<Subject> otherSubjects = subjectRepository.findAllByFacultyIdNotEquals(faculty.getId());
        map.put("otherSubjects", otherSubjects);
        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(faculty.getId());
        map.put("facultySubjects", facultySubjects);
        return Path.FORWARD_FACULTY_EDIT_ADMIN;
    }

    @PostMapping("/editFaculty")
    public String editFaculty(@RequestParam(name = "name_en") String nameEn,
                              @RequestParam(name = "name_ru") String nameRu,
                              @RequestParam(name = "total_places") String facultyTotalPlaces,
                              @RequestParam(name = "budget_places") String facultyBudgetPlaces,
                              @RequestParam(name = "oldName") String oldFacultyName,
                              @RequestParam(name = "oldCheckedSubjects") String[] oldCheckedSubjectsIds,
                              @RequestParam(name = "subjects") String[] newCheckedSubjectsIds,
                              HttpServletRequest request) {
        boolean valid = InputValidator.validateFacultyParameters(nameRu,
                nameEn, facultyBudgetPlaces, facultyTotalPlaces);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.debug("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_FACULTY_EDIT_ADMIN + oldFacultyName;
        }
        int totalPlaces = Integer.parseInt(facultyTotalPlaces);
        int budgetPlaces = Integer.parseInt(facultyBudgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budgetPlaces, totalPlaces);
        return facultyService.updateFaculty(oldFacultyName, faculty,
                oldCheckedSubjectsIds, newCheckedSubjectsIds, request);
    }

    @GetMapping("/applyFaculty")
    public String applyFacultyPage(@RequestParam(name = "name_en") String nameEn,
                               ModelMap map) {
        return facultyService.applyFacultyPage(nameEn, map);
    }

    @PostMapping("/applyFaculty")
    public String applyFaculty(HttpSession session,
                                   @RequestParam Long id,
                                   HttpServletRequest request) {
        return facultyService.applyFaculty(session, id, request);
    }

    @GetMapping("/createReport")
    public String createReport(@RequestParam Long id, ModelMap map) {
        return reportService.createReport(id, map, false);
    }

    @PostMapping("/createReport")
    public String finalizeReport(@RequestParam Long id, ModelMap map) {
        return reportService.createReport(id, map, true);
    }

}
