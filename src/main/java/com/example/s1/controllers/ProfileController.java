package com.example.s1.controllers;

import com.example.s1.model.Applicant;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.util.ArrayList;
import java.util.List;

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
    public String loginGet() {
        return Path.WELCOME_PAGE;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user == null) {
//            setErrorMessage(request, ERROR_CAN_NOT_FIND_USER);
            log.error("errorMessage: Cannot find user with such login/password");
            return Path.WELCOME_PAGE;
        }
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(email, password, roles);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(token);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

        session.setAttribute("user", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("lang", user.getLang());
        Config.set(session, Config.FMT_LOCALE, new java.util.Locale(user.getLang()));
        return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return Path.WELCOME_PAGE;
    }

    @GetMapping("/userRegistration")
    public String addUser() {
        return Path.FORWARD_USER_REGISTRATION_PAGE;
    }

    @PostMapping("/userRegistration")
    public String addUserPost(@RequestParam String email, @RequestParam String password,
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
        return profileService.editProfileGet(session, map);
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
        return profileService.editProfilePost(oldEmail, firstName, lastName, email, password, lang,
                session, city, district, school);
    }


}
