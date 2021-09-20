package com.example.s1.services;

import com.example.s1.model.Applicant;
import com.example.s1.model.Role;
import com.example.s1.model.User;
import com.example.s1.repository.ApplicantRepository;
import com.example.s1.repository.UserRepository;
import com.example.s1.utils.Fields;
import com.example.s1.utils.InputValidator;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Service
public class ProfileService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;

    public String editProfileGet(HttpSession session, ModelMap map) {
        String userEmail = String.valueOf(session.getAttribute("user"));
        String role = String.valueOf(session.getAttribute("userRole"));
        User user = userRepository.findByEmail(userEmail);

        map.put(Fields.USER_FIRST_NAME, user.getFirstName());
        log.trace("Set attribute 'first_name': {}", user.getFirstName());
        map.put(Fields.USER_LAST_NAME, user.getLastName());
        log.trace("Set attribute 'last_name': {}", user.getLastName());
        map.put(Fields.USER_EMAIL, user.getEmail());
        log.trace("Set attribute 'email': {}", user.getEmail());
        map.put(Fields.USER_PASSWORD, user.getPassword());
        log.trace("Set attribute 'password': ***");
        map.put(Fields.USER_LANG, user.getLang());
        log.trace("Set attribute 'lang': {}", user.getLang());
        if (Role.isAdmin(role)) {
            return Path.FORWARD_ADMIN_PROFILE_EDIT;
        }
        if (Role.isUser(role)) {
            Applicant a = applicantRepository.findByUserId(user.getId());
            map.put(Fields.APPLICANT_CITY, a.getCity());
            log.trace("Set attribute 'city': {}", a.getCity());
            map.put(Fields.APPLICANT_DISTRICT, a.getDistrict());
            log.trace("Set attribute 'district': {}", a.getDistrict());
            map.put(Fields.APPLICANT_SCHOOL, a.getSchool());
            log.trace("Set attribute 'school': {}", a.getSchool());
            map.put(Fields.APPLICANT_IS_BLOCKED, a.isBlocked());
            log.trace("Set attribute 'isBlocked': {}", a.isBlocked());
            return Path.FORWARD_USER_PROFILE_EDIT;
        }
        return Path.WELCOME_PAGE;
    }

    @Transactional
    public String editProfilePost(String oldEmail, String firstName, String lastName, String email,
                                  String password, String lang, HttpSession session,
                                  String city, String district, String school) {
        log.trace("Fetch request parameter: 'oldEmail' = {}", oldEmail);
        log.trace("Fetch request parameter: 'first_name' = {}", firstName);
        log.trace("Fetch request parameter: 'last_name' = {}", lastName);
        log.trace("Fetch request parameter: 'email' = {}", email);
        log.trace("Fetch request parameter: 'password' = ***");
        log.trace("Fetch request parameter: 'lang' = {}", lang);

        boolean valid = InputValidator.validateUserParameters(
                firstName, lastName, email, password, lang);
        String role = String.valueOf(session.getAttribute("userRole"));
        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_EDIT_PROFILE;
        }
        User user = userRepository.findByEmail(oldEmail);
        log.trace("User found with such email: {}", user);
        if (Role.isAdmin(role)) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setLang(lang);
            log.trace("After calling setters with request parameters on user entity: {}", user);
            userRepository.save(user);
            log.trace("User info updated");
            // update session attributes if user changed it
            session.setAttribute("user", email);
            session.setAttribute(Fields.USER_LANG, lang);
            return Path.REDIRECT_TO_PROFILE;
        }
        if (Role.isUser(role)) {
            // if user role is user then we should also update applicant
            // record for them
            log.trace("Fetch request parameter: 'school' = {}", school);
            log.trace("Fetch request parameter: 'district' = {}", district);
            log.trace("Fetch request parameter: 'city' = {}", city);
            valid = InputValidator.validateApplicantParameters(city, district, school);
            if (!valid) {
//                setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
                log.error("errorMessage: Not all fields are properly filled");
                return Path.REDIRECT_EDIT_PROFILE;
            }
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setLang(lang);
            log.trace("After calling setters with request parameters on user entity: {}", user);
            userRepository.save(user);
            log.trace("User info updated");
            Applicant a = applicantRepository.findByUserId(user.getId());
            a.setCity(city);
            a.setDistrict(district);
            a.setSchool(school);
//            a.setBlocked(isBlocked);
            log.trace("After calling setters with request parameters on applicant entity: {}", a);
            applicantRepository.save(a);
            log.trace("Applicant info updated");
            // update session attributes if user changed it
            session.setAttribute("user", email);
            session.setAttribute(Fields.USER_LANG, lang);
            return Path.REDIRECT_TO_PROFILE;
        }
        return Path.WELCOME_PAGE;
    }



}
