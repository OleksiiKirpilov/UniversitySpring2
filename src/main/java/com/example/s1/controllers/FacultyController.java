package com.example.s1.controllers;

import com.example.s1.services.FacultyService;
import com.example.s1.model.*;
import com.example.s1.repository.*;
import com.example.s1.utils.Fields;
import com.example.s1.utils.InputValidator;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    FacultyService facultyService;
    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    ReportSheetRepository reportSheetRepository;

    @GetMapping("/viewAllFaculties")
    public String list(HttpSession session, HttpServletRequest request) {
        Iterable<Faculty> faculties = facultyRepository.findAll();
        log.trace("Faculties records found: {}", faculties);
        request.setAttribute("faculties", faculties);
        log.trace("Set the request attribute: 'faculties' = {}", faculties);
        String role = (String) session.getAttribute("userRole");
        if (role == null || Role.isUser(role)) {
            return Path.FORWARD_FACULTY_VIEW_ALL_USER;
        }
        if (Role.isAdmin(role)) {
            return Path.FORWARD_FACULTY_VIEW_ALL_ADMIN;
        }
        return Path.WELCOME_PAGE;
    }

    @GetMapping("/viewFaculty")
    public String view(@RequestParam(name = "name_en") String nameEn,
                       ModelMap map, HttpSession session) {
        return facultyService.viewFaculty(nameEn, map, session);
    }


    @GetMapping("/addFaculty")
    public String preAdd(HttpServletRequest request) {
        log.trace("Request for only showing (not adding) faculty/add.jsp");
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        log.trace("All subjects found: {}", allSubjects);
        request.setAttribute("allSubjects", allSubjects);
        log.trace("Set request attribute 'allSubjects' = {}", allSubjects);
        return Path.FORWARD_FACULTY_ADD_ADMIN;
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
            log.error("errorMessage: Not all fields are properly filled");
            return "redirect:addFaculty";
        }
        log.trace("All fields are properly filled. Start updating database.");
        int total = Integer.parseInt(totalPlaces);
        int budget = Integer.parseInt(budgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budget, total);
        log.trace("Create faculty transfer object: {}", faculty);
        facultyRepository.save(faculty);
        log.trace("Create faculty record in database: {}", faculty);
        // only after creating a faculty record we can proceed with
        // adding faculty subjects
        if (subjects != null) {
            List<FacultySubjects> newFS = new ArrayList<>();
            for (Long id : subjects) {
                newFS.add(new FacultySubjects(id, faculty.getId()));
            }
            facultySubjectsRepository.saveAll(newFS);
            log.trace("FacultySubjects record created in database: {}", newFS);
        }
        return "redirect:viewFaculty?name_en=" + nameEn;
    }

    @PostMapping("/deleteFaculty")
    public String deleteFaculty(@RequestParam Long id) {
        Faculty facultyToDelete = facultyRepository.findById(id).orElse(null);
        Iterable<Applicant> facultyApplicants = applicantRepository.findAllByFacultyId(id);
        if (facultyApplicants != null) {
//            setErrorMessage(request, ERROR_FACULTY_DEPENDS);
            return Path.REDIRECT_TO_FACULTY + facultyToDelete.getNameEn();
        }
        facultyRepository.delete(facultyToDelete);
        log.trace("Delete faculty record in database: {}", facultyToDelete);
        return "redirect:/viewAllFaculties";
    }

    @GetMapping("/editFaculty")
    public String editFaculty(@RequestParam(name = "name_en") String nameEn,
                                ModelMap map) {
        Faculty faculty = facultyRepository.findByNameEn(nameEn);
        map.put(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        log.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        log.trace("Set attribute 'name_en': {}", faculty.getNameEn());
        map.put(Fields.FACULTY_TOTAL_PLACES, faculty.getTotalPlaces());
        log.trace("Set attribute 'total_places': {}", faculty.getTotalPlaces());
        map.put(Fields.FACULTY_BUDGET_PLACES, faculty.getBudgetPlaces());
        log.trace("Set attribute 'budget_places': {}", faculty.getBudgetPlaces());
        Iterable<Subject> otherSubjects = subjectRepository.findAllByFacultyIdNotEquals(faculty.getId());
        map.put("otherSubjects", otherSubjects);
        log.trace("Set attribute 'otherSubjects': {}", otherSubjects);
        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(faculty.getId());
        map.put("facultySubjects", facultySubjects);
        log.trace("Set attribute 'facultySubjects': {}", facultySubjects);
        return "/admin_edit_faculty";
    }

    @PostMapping("/editFaculty")
    public String editFacultyPost(@RequestParam(name = "name_en") String nameEn,
                                  @RequestParam(name = "name_ru") String nameRu,
                                  @RequestParam(name = "total_places") String facultyTotalPlaces,
                                  @RequestParam(name = "budget_places") String facultyBudgetPlaces,
                                  @RequestParam(name = "oldName") String oldFacultyName,
                                  @RequestParam(name = "oldCheckedSubjects") String[] oldCheckedSubjectsIds,
                                  @RequestParam(name = "subjects") String[] newCheckedSubjectsIds
                                  ) {
        // if user changes faculty name we need to know the old one
        // to update record in db
        boolean valid = InputValidator.validateFacultyParameters(nameRu,
                nameEn, facultyBudgetPlaces, facultyTotalPlaces);
        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return "redirect:/editFaculty&name_en=" + oldFacultyName;
        }
        // if it's true then let's start to update the db
        return facultyService.updateFaculty(oldFacultyName, nameEn, nameRu, facultyTotalPlaces,
                facultyBudgetPlaces, oldCheckedSubjectsIds, newCheckedSubjectsIds);
    }

    @GetMapping("/viewApplicant")
    public String viewApplicant(@RequestParam Long userId,
                                ModelMap map) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.error("Can not found user with id={}", userId);
            return "redirect:/";
        }
        map.put("first_name", user.getFirstName());
        log.trace("Set the request attribute: 'first_name' = {}", user.getFirstName());
        map.put("last_name", user.getLastName());
        log.trace("Set the request attribute: 'last_name' = {}", user.getLastName());
        map.put("email", user.getEmail());
        log.trace("Set the request attribute: 'email' = {}", user.getEmail());
        map.put("role", user.getRole());
        log.trace("Set the request attribute: 'role' = {}", user.getRole());
        Applicant applicant = applicantRepository.findByUserId(user.getId());
        map.put(Fields.ENTITY_ID, applicant.getId());
        log.trace("Set the request attribute: 'id' = {}", applicant.getId());
        map.put(Fields.APPLICANT_CITY, applicant.getCity());
        log.trace("Set the request attribute: 'city' = {}", applicant.getCity());
        map.put(Fields.APPLICANT_DISTRICT, applicant.getDistrict());
        log.trace("Set the request attribute: 'district' = {}", applicant.getDistrict());
        map.put(Fields.APPLICANT_SCHOOL, applicant.getSchool());
        log.trace("Set the request attribute: 'school' = {}", applicant.getSchool());
        map.put(Fields.APPLICANT_IS_BLOCKED, applicant.isBlockedStatus());
        log.trace("Set the request attribute: 'isBlocked' = {}", applicant.isBlockedStatus());
        return Path.FORWARD_APPLICANT_PROFILE;
    }

    @PostMapping("/viewApplicant")
    public String viewApplicantPost(@RequestParam Long id) {
		Applicant applicant = applicantRepository.findById(id).orElse(null);
        if (applicant == null) {
            return "redirect:/";
        }
		boolean updatedBlockedStatus = !applicant.isBlockedStatus();
		applicant.setBlockedStatus(updatedBlockedStatus);
		log.trace("Applicant with 'id' = {} and changed 'isBlocked' status = {}"
				+ " record will be updated.", id, updatedBlockedStatus);
		applicantRepository.save(applicant);
		return Path.REDIRECT_APPLICANT_PROFILE + applicant.getUserId();
    }

    @GetMapping("/applyFaculty")
    public String applyFaculty (@RequestParam(name = "name_en") String nameEn,
                                ModelMap map) {
        Faculty faculty = facultyRepository.findByNameEn(nameEn);
        map.put(Fields.ENTITY_ID, faculty.getId());
        log.trace("Set the request faculty attribute: 'id' = {}", faculty.getId());
        map.put(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        log.trace("Set the request attribute: 'name' = {}", faculty.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        log.trace("Set the request attribute: 'name_en' = {}", faculty.getNameEn());
        map.put(Fields.FACULTY_TOTAL_PLACES, faculty.getTotalPlaces());
        log.trace("Set the request attribute: 'total_places' = {}", faculty.getTotalPlaces());
        map.put(Fields.FACULTY_BUDGET_PLACES, faculty.getBudgetPlaces());
        log.trace("Set the request attribute: 'budget_places' = {}", faculty.getBudgetPlaces());
        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(faculty.getId());
        map.put("facultySubjects", facultySubjects);
        log.trace("Set attribute 'facultySubjects': {}", facultySubjects);
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        map.put("allSubjects", allSubjects);
        log.trace("Set attribute 'allSubjects': {}", allSubjects);
        return Path.FORWARD_FACULTY_APPLY_USER;
    }

    @PostMapping("/applyFaculty")
    public String applyFacultyPost(HttpSession session,
                                   @RequestParam(name = "id") Long facultyId,
                                   HttpServletRequest request) {
        return facultyService.applyFacultyPost(session, facultyId, request);
    }

    @GetMapping("/createReport")
    public String createReport(@RequestParam Long id,
                               ModelMap map) {
        List<ReportSheet> report = reportSheetRepository.findAllByFacultyId(id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            log.error("Can not find faculty with id={}", id);
            return Path.ERROR_PAGE;
        }
        int totalPlaces = faculty.getTotalPlaces();
        int budgetPlaces = faculty.getBudgetPlaces();
        for (int i = 0; i < report.size(); i++) {
            ReportSheet sheet = report.get(i);
            if ((i < totalPlaces) && !sheet.isBlocked()) {
                sheet.setEntered(true);
                sheet.setEnteredOnBudget(i < budgetPlaces);
            } else {
                sheet.setEntered(false);
                sheet.setEnteredOnBudget(false);
            }
        }
        map.put(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        log.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        log.trace("Set attribute 'name_en': {}", faculty.getNameEn());
        map.put("facultyReport", report);
        log.trace("Set attribute 'facultyReport': {}", report);
        return Path.FORWARD_REPORT_SHEET_VIEW;

    }

}
