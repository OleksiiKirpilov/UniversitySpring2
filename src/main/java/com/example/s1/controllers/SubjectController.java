package com.example.s1.controllers;

import com.example.s1.model.FacultySubjects;
import com.example.s1.model.Grade;
import com.example.s1.model.Subject;
import com.example.s1.repository.FacultySubjectsRepository;
import com.example.s1.repository.GradeRepository;
import com.example.s1.repository.SubjectRepository;
import com.example.s1.utils.Fields;
import com.example.s1.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Controller
public class SubjectController {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    FacultySubjectsRepository facultySubjectsRepository;

    @Autowired
    GradeRepository gradeRepository;

    @GetMapping("/viewAllSubjects")
    public String listAll(HttpServletRequest request) {
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        log.trace("Subjects records found: {}", allSubjects);
        request.setAttribute("allSubjects", allSubjects);
        log.trace("Set the request attribute: 'allSubjects' = {}", allSubjects);
        return "admin/admin_list_subject";
    }

    @GetMapping("/viewSubject")
    public String viewSubject(@RequestParam(name = "name_en") String nameEn,
                              ModelMap map) {
        log.trace("Subject name to look for is equal to: '{}'", nameEn);
        Subject subject = subjectRepository.findSubjectByNameEnEquals(nameEn);
        log.trace("Subject record found: {}", subject);
        map.put(Fields.ENTITY_ID, subject.getId());
        log.trace("Set the request attribute: 'id' = {}", subject.getId());
        map.put(Fields.SUBJECT_NAME_RU, subject.getNameRu());
        log.trace("Set the request attribute: 'name_ru' = {}", subject.getNameRu());
        map.put(Fields.SUBJECT_NAME_EN, subject.getNameEn());
        log.trace("Set the request attribute: 'name_en' = {}", subject.getNameEn());
        return "admin/admin_view_subject";
    }

    @GetMapping("/addSubject")
    public String addSubjectJsp() {
        return "admin/admin_add_subject";
    }

    @PostMapping("/addSubject")
    public String addSubject(@RequestParam(name = "name_ru") String nameRu,
                             @RequestParam(name = "name_en") String nameEn) {
        log.trace("Fetch request parameter: 'name_ru' = {}", nameRu);
        log.trace("Fetch request parameter: 'name_en' = {}", nameEn);
        boolean valid = InputValidator.validateSubjectParameters(nameRu, nameEn);
        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return "redirect:admin/admin_add_subject";
        }
        Subject subject = new Subject();
        subject.setNameRu(nameRu);
        subject.setNameEn(nameEn);
        subjectRepository.save(subject);
        log.trace("Create subject record in database: {}", subject);
        return "redirect:/viewSubject?name_en=" + nameEn;
    }

    @PostMapping("/deleteSubject")
    public String delete(@RequestParam Long id) {
        Subject subjectToDelete = subjectRepository.findById(id).orElse(null);
        if (subjectToDelete == null) {
            return "redirect:admin/admin_list_subject";
        }
        log.trace("Found subject that should be deleted: {}", subjectToDelete);
        Collection<FacultySubjects> facultySubjects = new ArrayList<>();
        for (FacultySubjects fs : facultySubjectsRepository.findAll()) {
            if (fs.getSubjectId().equals(subjectToDelete.getId())) {
                facultySubjects.add(fs);
            }
        }
        String result;
        if (facultySubjects.isEmpty()) {
            log.trace("No faculties have this subject as preliminary. Check applicant grades.");
            Collection<Grade> grades = new ArrayList<>();
            for (Grade g : gradeRepository.findAll()) {
                if (g.getSubjectId().equals(subjectToDelete.getId())) {
                    grades.add(g);
                }
            }
            if (grades.isEmpty()) {
                log.trace("No grades records on this subject. Perform deleting.");
                subjectRepository.delete(subjectToDelete);
                result = "redirect:/viewAllSubjects";
            } else {
                log.trace("There are grades records that rely on this subject.");
                result = "redirect:/viewSubject?name_en=" + subjectToDelete.getNameEn();
            }
            return result;
        }
        log.trace("There are faculties that have this subject as preliminary.");
        return "redirect:/viewSubject?name_en=" + subjectToDelete.getNameEn();
    }

    @GetMapping("/editSubject")
    public String editGet(@RequestParam(name = "name_en") String nameEn,
                          ModelMap map) {
        Subject subject = subjectRepository.findSubjectByNameEnEquals(nameEn);
        map.put(Fields.SUBJECT_NAME_RU, subject.getNameRu());
        log.trace("Set attribute 'name_ru': {}", subject.getNameRu());
        map.put(Fields.SUBJECT_NAME_EN, subject.getNameEn());
        log.trace("Set attribute 'name_en': {}", subject.getNameEn());
        return "admin/admin_edit_subject";
    }

    @PostMapping("/editSubject")
    public String editPost(@RequestParam String oldName,
                           @RequestParam(name = "name_en") String nameEn,
                           @RequestParam(name = "name_ru") String nameRu) {
        log.trace("Fetch request parameter: 'oldName' = {}", oldName);
        Subject subject = subjectRepository.findSubjectByNameEnEquals(oldName);
        log.trace("Subject record found with this data: {}", subject);
        log.trace("Fetch request parameter: 'name_en' = {}", nameEn);
        boolean valid = InputValidator.validateSubjectParameters(nameRu, nameEn);
        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return "redirect:/editSubject" + oldName;
        }
        subject.setNameRu(nameRu);
        subject.setNameEn(nameEn);
        log.trace("After calling setters with request parameters on subject entity: {}", subject);
        subjectRepository.save(subject);
        log.trace("Subject record updated");
        return "redirect:/viewSubject?name_en=" + nameEn;
    }
}
