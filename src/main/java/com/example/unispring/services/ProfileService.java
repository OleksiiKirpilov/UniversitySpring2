package com.example.unispring.services;

import com.example.unispring.model.Applicant;
import com.example.unispring.model.Role;
import com.example.unispring.model.User;
import com.example.unispring.repository.ApplicantRepository;
import com.example.unispring.repository.UserRepository;
import com.example.unispring.util.Fields;
import com.example.unispring.util.InputValidator;
import com.example.unispring.util.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.util.ArrayList;
import java.util.List;

import static com.example.unispring.util.Fields.USER_LANG;
import static com.example.unispring.util.MessageHelper.*;

@Slf4j
@Service
public class ProfileService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;


    public String login(String email, String password, HttpSession session) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user == null) {
            setErrorMessage(session, ERROR_CAN_NOT_FIND_USER);
            log.debug("errorMessage: Cannot find user with such login/password");
            return Path.WELCOME_PAGE;
        }
        setUserAuthorities(user, session);
        return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
    }

    public String viewProfile(HttpSession session, ModelMap map) {
        String userEmail = String.valueOf(session.getAttribute("user"));
        User user = userRepository.findByEmail(userEmail);
        map.put("user", user);
        String role = user.getRole();
        if (Role.isAdmin(role)) {
            return Path.FORWARD_ADMIN_PROFILE;
        }
        if (Role.isUser(role)) {
            Applicant applicant = applicantRepository.findByUserId(user.getId());
            map.put("applicant", applicant);
            return Path.FORWARD_USER_PROFILE;
        }
        return Path.WELCOME_PAGE;
    }

    public String editProfilePage(HttpSession session, ModelMap map) {
        String userEmail = String.valueOf(session.getAttribute("user"));
        String role = String.valueOf(session.getAttribute("userRole"));
        User user = userRepository.findByEmail(userEmail);

        map.put(Fields.USER_FIRST_NAME, user.getFirstName());
        map.put(Fields.USER_LAST_NAME, user.getLastName());
        map.put(Fields.USER_EMAIL, user.getEmail());
        map.put(Fields.USER_PASSWORD, "***");
        map.put(USER_LANG, user.getLang());
        if (Role.isAdmin(role)) {
            return Path.FORWARD_ADMIN_PROFILE_EDIT;
        }
        if (Role.isUser(role)) {
            Applicant a = applicantRepository.findByUserId(user.getId());
            map.put(Fields.APPLICANT_CITY, a.getCity());
            map.put(Fields.APPLICANT_DISTRICT, a.getDistrict());
            map.put(Fields.APPLICANT_SCHOOL, a.getSchool());
            map.put(Fields.APPLICANT_IS_BLOCKED, a.isBlocked());
            return Path.FORWARD_USER_PROFILE_EDIT;
        }
        return Path.WELCOME_PAGE;
    }

    @Transactional
    public String editProfile(String oldEmail, String firstName, String lastName, String email,
                              String password, String lang, HttpSession session,
                              String city, String district, String school) {
        boolean valid = InputValidator.validateUserParameters(
                firstName, lastName, email, password, lang);
        String role = String.valueOf(session.getAttribute("userRole"));
        if (!valid) {
            setErrorMessage(session, ERROR_FILL_ALL_FIELDS);
            log.debug("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_EDIT_PROFILE;
        }
        User user = userRepository.findByEmail(oldEmail);
        User exisingUser = userRepository.findByEmail(email);
        if (exisingUser != null && !user.getId().equals(exisingUser.getId())) {
            setErrorMessage(session, ERROR_EMAIL_USED);
            log.debug("This email is already in use.");
            return Path.REDIRECT_EDIT_PROFILE;
        }
        if (Role.isAdmin(role)) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            user.setLang(lang);
            userRepository.save(user);
            log.trace("User info updated. {}", user);
            session.setAttribute("user", email);
            session.setAttribute(USER_LANG, lang);
            return Path.REDIRECT_TO_PROFILE;
        }
        if (Role.isUser(role)) {
            // if user role is user then we should also update applicant
            // record for them
            valid = InputValidator.validateApplicantParameters(city, district, school);
            if (!valid) {
                setErrorMessage(session, ERROR_FILL_ALL_FIELDS);
                log.debug("errorMessage: Not all fields are properly filled");
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
            Applicant ap = applicantRepository.findByUserId(user.getId());
            ap.setCity(city);
            ap.setDistrict(district);
            ap.setSchool(school);
            applicantRepository.save(ap);
            log.trace("Applicant info updated. {}", ap);
            session.setAttribute("user", email);
            session.setAttribute(USER_LANG, lang);
            return Path.REDIRECT_TO_PROFILE;
        }
        return Path.WELCOME_PAGE;
    }

    public void setUserAuthorities(User user, HttpSession session) {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), roles);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(token);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        session.setAttribute("user", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute(USER_LANG, user.getLang());
        Config.set(session, Config.FMT_LOCALE, new java.util.Locale(user.getLang()));
        log.info("User: {} logged as {}", user, user.getRole());
    }

}
