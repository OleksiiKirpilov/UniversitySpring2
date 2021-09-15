package com.example.s1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class WelcomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "welcome";
    }

    @GetMapping("/welcome*")
    public String welcome2() {
        return "welcome";
    }

}
