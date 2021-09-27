package com.example.s1.controllers;

import com.example.s1.services.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Applicant related controller
 */
@Controller
public class ApplicantController {

    @Autowired
    ApplicantService applicantService;

    @GetMapping("/viewApplicant")
    public String viewApplicantPage(@RequestParam Long userId,
                                    ModelMap map) {
        return applicantService.viewApplicant(userId, map);
    }

    @PostMapping("/viewApplicant")
    public String viewApplicant(@RequestParam Long id) {
        return applicantService.updateApplicantStatus(id);
    }

}
