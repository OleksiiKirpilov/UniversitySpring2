package com.example.s1.controllers;

import com.example.s1.model.Role;
import com.example.s1.model.User;
import com.example.s1.repository.ApplicantRepository;
import com.example.s1.repository.UserRepository;
import com.example.s1.services.ApplicantService;
import com.example.s1.services.ProfileService;
import com.example.s1.utils.InputValidator;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Profile related controller
 */
@Slf4j
@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    ApplicantService applicantService;


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
                          HttpSession session

    ) {
        boolean valid = InputValidator.validateUserParameters(firstName, lastName, email, password, lang);
        valid &= InputValidator.validateApplicantParameters(city, district, school);
        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("Not all fields are filled");
            return Path.REDIRECT_USER_REGISTRATION_PAGE;
        }
        User user = userRepository.findByEmail(email);
        if (user != null) {
            log.error("This email is already in use.");
            return Path.REDIRECT_USER_REGISTRATION_PAGE;
        }
        user = new User(email, password, firstName, lastName, Role.USER.toString(), lang);
        return applicantService.saveApplicant(session, user, city, district, school);
    }

    @GetMapping("/viewProfile")
    public String viewProfile(HttpSession session, ModelMap map) {
        return profileService.viewProfile(session, map);
    }

    @GetMapping("/adminRegistration")
    public String addAdmin() {
        return Path.FORWARD_ADMIN_REGISTRATION_PAGE;
    }

    @PostMapping("/adminRegistration")
    public String addAdminPost(@RequestParam String email, @RequestParam String password,
                               @RequestParam(name = "first_name") String firstName,
                               @RequestParam(name = "last_name") String lastName,
                               @RequestParam String lang
    ) {
        boolean valid = InputValidator.validateUserParameters(firstName, lastName, email, password, lang);
        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are filled");
            return Path.REDIRECT_ADMIN_REGISTRATION_PAGE;
        }
        User user = new User(email, password, firstName, lastName, Role.ADMIN.toString(), lang);
        userRepository.save(user);
        log.trace("User record created: {}", user);
//        setOkMessage(request, MESSAGE_ACCOUNT_CREATED);
        return Path.REDIRECT_TO_PROFILE;
    }

    @GetMapping("/editProfile")
    public String editProfile(HttpSession session, ModelMap map) {
        return profileService.editProfilePage(session, map);
    }

    @PostMapping("/editProfile")
    public String editProfilePost(@RequestParam String oldEmail,
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
