package com.example.s1.controllers;

import com.example.s1.model.Role;
import com.example.s1.model.User;
import com.example.s1.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LogInController {

    private static final Logger LOG = LoggerFactory.getLogger(LogInController.class);

    @Autowired
    UserRepository repository;

    @PostMapping("/welcome")
    public String login(@RequestParam String command,
                        @RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        if (!"login".equals(command)) {
            return "redirect:welcome";
        }
        User user = repository.findUserByEmailAndPassword(email, password);
        LOG.trace("User found: {}", user);
        if (user == null) {
//            setErrorMessage(request, ERROR_CAN_NOT_FIND_USER);
            LOG.error("errorMessage: Cannot find user with such login/password");
            return "redirect:/";
        }
        session.setAttribute("user", user.getEmail());
        LOG.trace("Set session attribute 'user' = {}", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        LOG.trace("Set session attribute: 'userRole' = {}", user.getRole());
        session.setAttribute("lang", user.getLang());
        LOG.trace("Set session attribute 'lang' = {}", user.getLang());
        LOG.info("User: {} logged as {}", user, user.getRole());
        return  "redirect:viewAllFaculties";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        LOG.debug("Finished logout.");
        return "redirect:/";
    }
}
