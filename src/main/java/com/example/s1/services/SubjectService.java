package com.example.s1.services;

import com.example.s1.model.FacultySubjects;
import com.example.s1.model.Grade;
import com.example.s1.model.Subject;
import com.example.s1.repository.FacultySubjectsRepository;
import com.example.s1.repository.GradeRepository;
import com.example.s1.repository.SubjectRepository;
import com.example.s1.utils.InputValidator;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

import static com.example.s1.utils.MessageHelper.ERROR_FILL_ALL_FIELDS;
import static com.example.s1.utils.MessageHelper.setErrorMessage;

@Slf4j
@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    FacultySubjectsRepository facultySubjectsRepository;

    @Autowired
    GradeRepository gradeRepository;


    public String add(String nameRu, String nameEn, HttpServletRequest request) {
        boolean valid = InputValidator.validateSubjectParameters(nameRu, nameEn);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_SUBJECT_ADD_ADMIN;
        }
        Subject subject = new Subject();
        subject.setNameRu(nameRu);
        subject.setNameEn(nameEn);
        subjectRepository.save(subject);
        log.trace("Create subject record in database: {}", subject);
        return Path.REDIRECT_TO_SUBJECT + nameEn;
    }

    public String edit(String oldName, String nameEn, String nameRu, HttpServletRequest request) {
        Subject subject = subjectRepository.findSubjectByNameEnEquals(oldName);
        boolean valid = InputValidator.validateSubjectParameters(nameRu, nameEn);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_SUBJECT_EDIT_ADMIN + oldName;
        }
        subject.setNameRu(nameRu);
        subject.setNameEn(nameEn);
        subjectRepository.save(subject);
        log.trace("After calling setters with request parameters on subject entity: {}", subject);
        return Path.REDIRECT_TO_SUBJECT + nameEn;
    }

    public String delete(Long id) {
        Subject subjectToDelete = subjectRepository.findById(id).orElse(null);
        if (subjectToDelete == null) {
            return Path.FORWARD_SUBJECT_VIEW_ALL_ADMIN;
        }
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
                result = Path.REDIRECT_TO_VIEW_ALL_SUBJECTS;
            } else {
                log.trace("There are grades records that rely on this subject.");
                result = Path.REDIRECT_TO_SUBJECT + subjectToDelete.getNameEn();
            }
            return result;
        }
        log.trace("There are faculties that have this subject as preliminary.");
        return Path.REDIRECT_TO_SUBJECT + subjectToDelete.getNameEn();
    }


}
