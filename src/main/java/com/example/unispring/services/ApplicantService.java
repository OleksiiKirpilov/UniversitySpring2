package com.example.unispring.services;

import com.example.unispring.model.Applicant;
import com.example.unispring.model.Grade;
import com.example.unispring.model.Subject;
import com.example.unispring.model.User;
import com.example.unispring.repository.ApplicantRepository;
import com.example.unispring.repository.GradeRepository;
import com.example.unispring.repository.SubjectRepository;
import com.example.unispring.repository.UserRepository;
import com.example.unispring.util.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApplicantService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    SubjectRepository subjectRepository;

    @Transactional
    public void saveApplicant(User user, String city, String district, String school) {
        userRepository.save(user);
        log.trace("User record created: {}", user);
        Applicant applicant = new Applicant(city, district, school, user.getId(), false);
        applicantRepository.save(applicant);
        log.trace("Applicant record created: {}", applicant);
    }

    public String updateApplicantStatus(Long id) {
        Applicant applicant = applicantRepository.findById(id).orElse(null);
        if (applicant == null) {
            log.error("Applicant with 'id' = {} not found.", id);
            return Path.WELCOME_PAGE;
        }
        boolean updatedBlockedStatus = !applicant.isBlocked();
        applicant.setBlocked(updatedBlockedStatus);
        log.trace("Applicant with 'id' = {} and changed 'blocked' status = {}"
                + " record will be updated.", id, updatedBlockedStatus);
        applicantRepository.save(applicant);
        return Path.REDIRECT_APPLICANT_PROFILE + applicant.getUserId();
    }

    public String viewApplicant(Long userId, ModelMap map) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.error("Can not found user with id = {}", userId);
            return Path.ERROR_PAGE;
        }
        Applicant applicant = applicantRepository.findByUserId(user.getId());
        List<Grade> grades = gradeRepository.findAllByApplicantId(applicant.getId());
        Map<Grade, Subject> preliminaryGrades = new LinkedHashMap<>();
        Map<Grade, Subject> dimplomaGrades = new LinkedHashMap<>();
        for (Grade g : grades) {
            Subject subject = subjectRepository.findById(g.getSubjectId()).orElse(null);
            if (g.getExamType().equals("preliminary")) {
                preliminaryGrades.put(g, subject);
            } else {
                dimplomaGrades.put(g, subject);
            }
        }
        boolean notConfirmed = grades.stream().anyMatch(g -> !g.isConfirmed());
        map.put("user", user);
        map.put("applicant", applicant);
        map.put("preliminaryGrades", preliminaryGrades);
        map.put("diplomaGrades", dimplomaGrades);
        map.put("notConfirmed", notConfirmed);
        return Path.FORWARD_APPLICANT_PROFILE;
    }

    @Transactional
    public void confirmGrades(Long id) {
        List<Grade> grades = gradeRepository.findAllByApplicantId(id);
        for (Grade g : grades) {
            g.setConfirmed(true);
        }
        gradeRepository.saveAll(grades);
    }

}
