package com.example.s1.services;


import com.example.s1.model.Faculty;
import com.example.s1.model.ReportSheet;
import com.example.s1.repository.FacultyRepository;
import com.example.s1.repository.ReportSheetRepository;
import com.example.s1.utils.Fields;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

@Slf4j
@Service
public class ReportService {

    @Autowired
    ReportSheetRepository reportSheetRepository;
    @Autowired
    FacultyRepository facultyRepository;

    public String createReport(Long id, ModelMap map) {
        List<ReportSheet> report = reportSheetRepository.findAllByFacultyId(id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            log.error("Can not find faculty with id={}", id);
            return Path.ERROR_PAGE;
        }
        int totalPlaces = faculty.getTotalPlaces();
        int budgetPlaces = faculty.getBudgetPlaces();
        for (int i = 0; i < report.size(); i++) {
            ReportSheet sheet = report.get(i);
            if ((i < totalPlaces) && !sheet.isBlocked()) {
                sheet.setEntered(true);
                sheet.setEnteredOnBudget(i < budgetPlaces);
            } else {
                sheet.setEntered(false);
                sheet.setEnteredOnBudget(false);
            }
        }
        map.put(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        log.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        log.trace("Set attribute 'name_en': {}", faculty.getNameEn());
        map.put("facultyReport", report);
        log.trace("Set attribute 'facultyReport': {}", report);
        return Path.FORWARD_REPORT_SHEET_VIEW;
    }



}
