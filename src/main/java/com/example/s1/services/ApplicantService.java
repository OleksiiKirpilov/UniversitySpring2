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
        log.trace("Set session attribute 'user' = {}", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        log.trace("Set session attribute: 'userRole' = {}", user.getRole());
        session.setAttribute("lang", user.getLang());
        log.trace("Set session attribute 'lang' = {}", user.getLang());
        log.info("User: {} logged as {}", user, user.getRole());
        return Path.REDIRECT_TO_PROFILE;
    }

}
