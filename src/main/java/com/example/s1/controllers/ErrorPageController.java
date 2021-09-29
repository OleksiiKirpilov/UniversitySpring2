package com.example.s1.controllers;

import com.example.s1.util.Path;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Error page controller
 */
@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return Path.ERROR_PAGE;
    }

}
