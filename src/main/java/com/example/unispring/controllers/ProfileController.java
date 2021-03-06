package com.example.unispring.controllers;

import com.example.unispring.model.Role;
import com.example.unispring.model.User;
import com.example.unispring.repository.UserRepository;
import com.example.unispring.services.ApplicantService;
import com.example.unispring.services.ProfileService;
import com.example.unispring.util.InputValidator;
import com.example.unispring.util.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import static com.example.unispring.util.MessageHelper.*;

/**
 * Profile related controller
 */
@Slf4j
@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ApplicantService applicantService;


    @GetMapping("/login")
    public String welcome() {
        return Path.WELCOME_PAGE;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        return profileService.login(email, password, session);
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return Path.WELCOME_PAGE;
    }

    @GetMapping("/userRegistration")
    public String registrationPage() {
        return Path.FORWARD_USER_REGISTRATION_PAGE;
    }

    @PostMapping("/userRegistration")
    public String addUser(@RequestParam String email, @RequestParam String password,
                          @RequestParam(name = "first_name") String firstName,
                          @RequestParam(name = "last_name") String lastName,
                          @RequestParam String lang,
                          @RequestParam String city, @RequestParam String district,
                          @RequestParam String school,
                          HttpSession session) {
        boolean valid = InputValidator.validateUserParameters(firstName, lastName, email, password, lang);
        valid &= InputValidator.validateApplicantParameters(city, district, school);
        if (!valid) {
            setErrorMessage(session, ERROR_FILL_ALL_FIELDS);
            log.debug("Not all fields are filled");
            return Path.REDIRECT_USER_REGISTRATION_PAGE;
        }
        User user = userRepository.findByEmail(email);
        if (user != null) {
            setErrorMessage(session, ERROR_EMAIL_USED);
            log.debug("This email is already in use.");
            return Path.REDIRECT_USER_REGISTRATION_PAGE;
        }
        user = new User(email, password, firstName, lastName, Role.USER_ROLE_NAME, lang);
        applicantService.saveApplicant(user, city, district, school);
        profileService.setUserAuthorities(user, session);
        return Path.REDIRECT_TO_PROFILE;
    }

    @GetMapping("/viewProfile")
    public String viewProfile(HttpSession session, ModelMap map) {
        return profileService.viewProfile(session, map);
    }

    @GetMapping("/adminRegistration")
    public String addAdminPage() {
        return Path.FORWARD_ADMIN_REGISTRATION_PAGE;
    }

    @PostMapping("/adminRegistration")
    public String addAdmin(@RequestParam String email, @RequestParam String password,
                           @RequestParam(name = "first_name") String firstName,
                           @RequestParam(name = "last_name") String lastName,
                           @RequestParam String lang,
                           HttpSession session
    ) {
        boolean valid = InputValidator.validateUserParameters(firstName, lastName, email, password, lang);
        if (!valid) {
            setErrorMessage(session, ERROR_FILL_ALL_FIELDS);
            log.debug("errorMessage: Not all fields are filled");
            return Path.REDIRECT_ADMIN_REGISTRATION_PAGE;
        }
        User user = userRepository.findByEmail(email);
        if (user != null) {
            setErrorMessage(session, ERROR_EMAIL_USED);
            log.debug("This email({}) is already in use.", email);
            return Path.REDIRECT_ADMIN_REGISTRATION_PAGE;
        }
        user = new User(email, password, firstName, lastName, Role.ADMIN.toString(), lang);
        userRepository.save(user);
        log.trace("User record created: {}", user);
        setOkMessage(session, MESSAGE_ACCOUNT_CREATED);
        return Path.REDIRECT_TO_PROFILE;
    }

    @GetMapping("/editProfile")
    public String editProfilePage(HttpSession session, ModelMap map) {
        return profileService.editProfilePage(session, map);
    }

    @PostMapping("/editProfile")
    public String editProfile(@RequestParam String oldEmail,
                              @RequestParam(name = "first_name") String firstName,
                              @RequestParam(name = "last_name") String lastName,
                              @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String lang,
                              HttpSession session,
                              @RequestParam(required = false) String city,
                              @RequestParam(required = false) String district,
                              @RequestParam(required = false) String school
    ) {
        return profileService.editProfile(oldEmail, firstName, lastName, email, password, lang,
                session, city, district, school);
    }


}
