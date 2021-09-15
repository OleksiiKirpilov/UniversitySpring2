package com.example.s1.controllers;

import com.example.s1.model.*;
import com.example.s1.repository.*;
import com.example.s1.utils.Fields;
import com.example.s1.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class FacultyController {

    private static final Logger LOG = LoggerFactory.getLogger(FacultyController.class);

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

    @GetMapping("/viewAllFaculties")
    public String list(HttpSession session, HttpServletRequest request) {
        Iterable<Faculty> faculties = facultyRepository.findAll();
        LOG.trace("Faculties records found: {}", faculties);
        request.setAttribute("faculties", faculties);
        LOG.trace("Set the request attribute: 'faculties' = {}", faculties);
        String role = (String) session.getAttribute("userRole");
        if (role == null || Role.isUser(role)) {
            return "/user/user_list_faculty";
        }
        if (Role.isAdmin(role)) {
            return "/admin/admin_list_faculty";
        }
        return "redirect:/";
    }

    @GetMapping("/viewFaculty")
    public String view(@RequestParam(name = "name_en") String nameEn,
                       HttpServletRequest request) {
        LOG.trace("Faculty name to look for is equal to: '{}'", nameEn);
        Faculty facultyRecord = facultyRepository.findByNameEn(nameEn);
        LOG.trace("Faculty record found: {}", facultyRecord);
        request.setAttribute(Fields.ENTITY_ID, facultyRecord.getId());
        request.setAttribute(Fields.FACULTY_NAME_RU, facultyRecord.getNameRu());
        request.setAttribute(Fields.FACULTY_NAME_EN, facultyRecord.getNameEn());
        request.setAttribute(Fields.FACULTY_TOTAL_PLACES, facultyRecord.getTotalPlaces());
        request.setAttribute(Fields.FACULTY_BUDGET_PLACES, facultyRecord.getBudgetPlaces());

        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(facultyRecord.getId());
        request.setAttribute("facultySubjects", facultySubjects);
        LOG.trace("Set the request attribute: 'facultySubjects' = {}", facultySubjects);

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("userRole");

        if (role == null || Role.isUser(role)) {
            String userEmail = String.valueOf(session.getAttribute("user"));
            boolean applied = hasUserAppliedFacultyByEmail(facultyRecord, userEmail);
            request.setAttribute("alreadyApplied", applied ? "yes" : "no");
            return "/user/iser_view_faculty";
        }
        if (!Role.isAdmin(role)) {
            return null;
        }

        Iterable<Applicant> applicants = applicantRepository.findAllByFacultyId(facultyRecord.getId());
        Map<Applicant, String> facultyApplicants = new TreeMap<>(Comparator.comparingLong(Applicant::getId));
        for (Applicant applicant : applicants) {
            User user = userRepository.findById(applicant.getUserId()).orElse(null);
            if (user == null) continue;
            facultyApplicants.put(applicant, user.getFirstName() + " " + user.getLastName());
        }
        request.setAttribute("facultyApplicants", facultyApplicants);
        LOG.trace("Set the request attribute: 'facultyApplicants' = {}", facultyApplicants);
        return "/admin/admin_view_faculty";
    }


    @GetMapping("/addFaculty")
    public String preAdd(HttpServletRequest request) {
        LOG.trace("Request for only showing (not adding) faculty/add.jsp");
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        LOG.trace("All subjects found: {}", allSubjects);
        request.setAttribute("allSubjects", allSubjects);
        LOG.trace("Set request attribute 'allSubjects' = {}", allSubjects);
        return "/admin/admin_add_faculty";
    }

    @PostMapping("/addFaculty")
    public String add(@RequestParam(name = "name_en") String nameEn,
                      @RequestParam(name = "name_ru") String nameRu,
                      @RequestParam(name = "budget_places") String budgetPlaces,
                      @RequestParam(name = "total_places") String totalPlaces,
                      @RequestParam Long[] subjects
    ) {
        boolean valid = InputValidator.validateFacultyParameters(nameRu,
                nameEn, budgetPlaces, totalPlaces);
        if (!valid) {
            //setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            LOG.error("errorMessage: Not all fields are properly filled");
            return "redirect:addFaculty";
        }
        LOG.trace("All fields are properly filled. Start updating database.");
        int total = Integer.parseInt(totalPlaces);
        int budget = Integer.parseInt(budgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budget, total);
        LOG.trace("Create faculty transfer object: {}", faculty);
        facultyRepository.save(faculty);
        LOG.trace("Create faculty record in database: {}", faculty);
        // only after creating a faculty record we can proceed with
        // adding faculty subjects
        if (subjects != null) {
            List<FacultySubjects> newFS = new ArrayList<>();
            for (Long id : subjects) {
                newFS.add(new FacultySubjects(id, faculty.getId()));
            }
            facultySubjectsRepository.saveAll(newFS);
            LOG.trace("FacultySubjects record created in database: {}", newFS);
        }
        return "redirect:viewFaculty?name_en=" + nameEn;
    }

    private boolean hasUserAppliedFacultyByEmail(Faculty faculty, String email) {
        User user = userRepository.findByEmail(email);
        Applicant a = applicantRepository.findByUserId(user.getId());
        FacultyApplicants fa =
                facultyApplicantsRepository.findByFacultyIdAndApplicantId(faculty.getId(), a.getId());
        return fa != null;
    }
}
