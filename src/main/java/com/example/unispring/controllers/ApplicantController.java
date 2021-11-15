package com.example.unispring.controllers;

import com.example.unispring.services.ApplicantService;
import com.example.unispring.util.Path;
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

    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping("/viewApplicant")
    public String viewApplicantPage(@RequestParam Long userId,
                                    ModelMap map) {
        return applicantService.viewApplicant(userId, map);
    }

    @PostMapping("/viewApplicant")
    public String viewApplicant(@RequestParam Long id) {
        return applicantService.updateApplicantStatus(id);
    }

    @PostMapping("/confirmGrades")
    public String confirmGrades(@RequestParam Long id,
                                @RequestParam Long userId) {
        applicantService.confirmGrades(id);
        return Path.REDIRECT_APPLICANT_PROFILE + userId;
    }
}
