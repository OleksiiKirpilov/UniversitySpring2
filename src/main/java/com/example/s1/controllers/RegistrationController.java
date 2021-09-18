package com.example.s1.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RegistrationController {

    @GetMapping("/addUser")
    public String registerUser() {
        return "addUser";
    }


}
