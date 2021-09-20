package com.example.s1.controllers;

import com.example.s1.utils.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class WelcomeController {

    @RequestMapping(value = "/")
    public String index() {
        return Path.WELCOME_PAGE;
    }

//    @GetMapping("/welcome*")
//    public String welcome2() {
//        return "welcome";
//    }

//    @GetMapping("/error*")
//    public String error() {
//        return Path.ERROR_PAGE;
//    }

}
