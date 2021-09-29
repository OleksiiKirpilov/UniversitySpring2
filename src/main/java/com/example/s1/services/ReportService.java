package com.example.s1.services;


import com.example.s1.model.Faculty;
import com.example.s1.model.ReportSheet;
import com.example.s1.model.ReportSheetView;
import com.example.s1.repository.FacultyRepository;
import com.example.s1.repository.ReportSheetRepository;
import com.example.s1.util.Fields;
import com.example.s1.util.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ReportService {

    @Autowired
    ReportSheetRepository reportSheetRepository;
    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    EntityManager em;


    public String createReport(Long id, ModelMap map, boolean saveReport) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            log.error("Can not find faculty with id = {}", id);
            return Path.ERROR_PAGE;
        }
        List<ReportSheet> report = reportSheetRepository.getAllByFacultyIdEquals(id);
        List<ReportSheetView> reportView = new ArrayList<>();
        boolean finalized = !report.isEmpty();
        if (!finalized) {
            report = calculateReportSheet(faculty);
            if (saveReport) {
                for (ReportSheet sheet : report) {
                    reportView.add(new ReportSheetView(sheet));
                    em.detach(sheet);
                }
                report.clear();
                for (ReportSheetView sheet : reportView) {
                    log.error(sheet.toString());
                    report.add(new ReportSheet(sheet));
                }
                reportSheetRepository.saveAll(report);
                finalized = true;
            }
        } else {
            saveReport = false;
        }
        map.put(Fields.REPORT_SHEET_FACULTY_FINALIZED, finalized);
        map.put(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        map.put(Fields.ENTITY_ID, faculty.getId());
        map.put("facultyReport", report);
        return saveReport ? Path.REDIRECT_REPORT_SHEET_VIEW + faculty.getId() : Path.FORWARD_REPORT_SHEET_VIEW;
    }

    private List<ReportSheet> calculateReportSheet(Faculty faculty) {
        List<ReportSheet> report = reportSheetRepository.computeAllByFacultyId(faculty.getId());
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
        return report;
    }

}
