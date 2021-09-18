package com.example.s1.controllers;

import com.example.s1.model.User;
import com.example.s1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LogInController {

    @Autowired
    UserRepository repository;

    @GetMapping("/login")
    public String loginGet() {
        return "redirect:welcome";
    }

    @PostMapping("/login")
    public String login(@RequestParam String command,
                        @RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        User user = repository.findUserByEmailAndPassword(email, password);
        log.trace("User found: {}", user);
        if (user == null) {
//            setErrorMessage(request, ERROR_CAN_NOT_FIND_USER);
            log.error("errorMessage: Cannot find user with such login/password");
            return "redirect:/";
        }
        session.setAttribute("user", user.getEmail());
        log.trace("Set session attribute 'user' = {}", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        log.trace("Set session attribute: 'userRole' = {}", user.getRole());
        session.setAttribute("lang", user.getLang());
        log.trace("Set session attribute 'lang' = {}", user.getLang());
        log.info("User: {} logged as {}", user, user.getRole());
        return "redirect:viewAllFaculties";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        log.debug("Finished logout.");
        return "redirect:/";
    }
}
