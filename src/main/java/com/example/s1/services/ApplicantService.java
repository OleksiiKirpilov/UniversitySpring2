package com.example.s1.services;

import com.example.s1.model.Applicant;
import com.example.s1.model.User;
import com.example.s1.repository.ApplicantRepository;
import com.example.s1.repository.UserRepository;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
public class ApplicantService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;

    @Transactional
    public String saveApplicant(HttpSession session, User user, String city, String district, String school) {
        userRepository.save(user);
        log.trace("User record created: {}", user);
        Applicant applicant = new Applicant(city, district, school, user.getId(), false);
        applicantRepository.save(applicant);
        log.trace("Applicant record created: {}", applicant);
        session.setAttribute("user", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("lang", user.getLang());
        log.info("User: {} logged as {}", user, user.getRole());
        return Path.REDIRECT_TO_PROFILE;
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
        map.put("user", user);
        Applicant applicant = applicantRepository.findByUserId(user.getId());
        map.put("applicant", applicant);
        return Path.FORWARD_APPLICANT_PROFILE;
    }


}
