package com.example.s1.controllers;

import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LocaleController {

    @GetMapping("/setSessionLanguage")
    public String setSessionLanguage(@RequestParam(name = "lang", defaultValue = "en") String lang,
                                     HttpSession session, HttpServletRequest request) {
        session.setAttribute("lang", lang);
        log.trace("Set session attribute 'lang' = {}", lang);
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = "welcome";
        }
        return "redirect:" + referer;
//        return Path.WELCOME_PAGE;
    }

}
