package com.example.s1.controllers;

import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

@Slf4j
@Controller
public class LocaleController {

    @GetMapping("/setSessionLanguage")
    public String setSessionLanguage(@RequestParam(name = "lang", defaultValue = "en") String lang,
                                     HttpSession session, HttpServletRequest request) {
        session.setAttribute("lang", lang);
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = "welcome";
        }
        Config.set(session, Config.FMT_LOCALE, new java.util.Locale(lang));
        return "redirect:" + referer;
//        return Path.WELCOME_PAGE;
    }

}
