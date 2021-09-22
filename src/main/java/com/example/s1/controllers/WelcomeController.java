package com.example.s1.controllers;

import com.example.s1.utils.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String index() {
        return Path.WELCOME_PAGE;
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return Path.WELCOME_PAGE;
    }

    @RequestMapping("/error*")
    public String error() {
        return Path.ERROR_PAGE;
    }

}
