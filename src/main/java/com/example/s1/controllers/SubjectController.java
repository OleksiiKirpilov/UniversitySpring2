package com.example.s1.controllers;

import com.example.s1.model.Subject;
import com.example.s1.repository.FacultySubjectsRepository;
import com.example.s1.repository.GradeRepository;
import com.example.s1.repository.SubjectRepository;
import com.example.s1.services.SubjectService;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Subject related controller
 */
@Slf4j
@Controller
public class SubjectController {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    FacultySubjectsRepository facultySubjectsRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    SubjectService subjectService;


    @GetMapping("/viewAllSubjects")
    public String listAll(HttpServletRequest request) {
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        request.setAttribute("allSubjects", allSubjects);
        return Path.FORWARD_SUBJECT_VIEW_ALL_ADMIN;
    }

    @GetMapping("/viewSubject")
    public String viewSubject(@RequestParam(name = "name_en") String nameEn,
                              ModelMap map) {
        Subject subject = subjectRepository.findSubjectByNameEnEquals(nameEn);
        map.put("subject", subject);
        return Path.FORWARD_SUBJECT_VIEW_ADMIN;
    }

    @GetMapping("/addSubject")
    public String addSubjectPage() {
        return Path.FORWARD_SUBJECT_ADD_ADMIN;
    }

    @PostMapping("/addSubject")
    public String addSubject(@RequestParam(name = "name_ru") String nameRu,
                             @RequestParam(name = "name_en") String nameEn,
                             HttpServletRequest request) {
        return subjectService.add(nameRu, nameEn, request);
    }

    @PostMapping("/deleteSubject")
    public String delete(@RequestParam Long id) {
        return subjectService.delete(id);
    }

    @GetMapping("/editSubject")
    public String editPage(@RequestParam(name = "name_en") String nameEn,
                           ModelMap map) {
        Subject subject = subjectRepository.findSubjectByNameEnEquals(nameEn);
        map.put("subject", subject);
        return Path.FORWARD_SUBJECT_EDIT_ADMIN;
    }

    @PostMapping("/editSubject")
    public String edit(@RequestParam String oldName,
                       @RequestParam(name = "name_en") String nameEn,
                       @RequestParam(name = "name_ru") String nameRu,
                       HttpServletRequest request) {
        return subjectService.edit(oldName, nameEn, nameRu, request);
    }
}
