package com.example.s1.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LocaleController {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleController.class);

    @GetMapping("/setSessionLanguage")
    public String setSessionLanguage(@RequestParam(name = "lang", defaultValue = "en") String lang,
                                     HttpSession session, HttpServletRequest request) {
        session.setAttribute("lang", lang);
        LOG.trace("Set session attribute 'lang' = {}", lang);
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = "welcome";
        }
        return "redirect:" + referer;
    }

}
