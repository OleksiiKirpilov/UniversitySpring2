package com.example.s1;

import com.example.s1.model.*;
import com.example.s1.repository.*;
import com.example.s1.utils.Fields;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.*;

@Slf4j
@Service
public class FacultyService {

    @Autowired
    FacultyRepository facultyRepository;
    @Autowired
    FacultySubjectsRepository facultySubjectsRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    FacultyApplicantsRepository facultyApplicantsRepository;

    @Transactional
    public String updateFaculty(String oldFacultyName,
                                String nameEn, String nameRu,
                                String facultyTotalPlaces, String facultyBudgetPlaces,
                                String[] oldCheckedSubjectsIds, String[] newCheckedSubjectsIds) {
        log.trace("All fields are properly filled. Start updating database.");
        int totalPlaces = Integer.parseInt(facultyTotalPlaces);
        int budgetPlaces = Integer.parseInt(facultyBudgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budgetPlaces, totalPlaces);
        Faculty oldFacultyRecord = facultyRepository.findByNameEn(oldFacultyName);
        faculty.setId(oldFacultyRecord.getId());
        facultyRepository.save(faculty);
        log.trace("Faculty record updated from: {}, to: {}", oldFacultyRecord, faculty);
        if (log.isTraceEnabled()) {
            log.trace("Get checked subjects before: {}", Arrays.toString(oldCheckedSubjectsIds));
            log.trace("Get checked subjects after: {}",  Arrays.toString(newCheckedSubjectsIds));
        }
        if (oldCheckedSubjectsIds == null) {
            if (newCheckedSubjectsIds == null) {
                // if before all subjects were unchecked and they still
                // are then nothing changed - do nothing
                log.trace("No faculty subjects records will be changed");
                return Path.REDIRECT_TO_FACULTY + nameEn;
            }
            // if user checked something,but before no subjects were checked
            for (String newCheckedSubject : newCheckedSubjectsIds) {
                Long subjectId = Long.parseLong(newCheckedSubject);
                FacultySubjects facultySubject = new FacultySubjects(subjectId, faculty.getId());
                facultySubjectsRepository.save(facultySubject);
                log.trace("Faculty subjects record was created: {}", facultySubject);
            }
            return Path.REDIRECT_TO_FACULTY + nameEn;
        }
        if (newCheckedSubjectsIds == null) {
            // if user unchecked all checkboxes and before there were some checked subjects
            log.trace("No subjects were checked for this faculty - all records that will be found will be deleted ");
            facultySubjectsRepository. deleteAllByFacultyId(faculty.getId());
            return Path.REDIRECT_TO_FACULTY + nameEn;
        }
        // if there were checked subjects and still are
        // then for INSERT we should check if the record already exists in db
        Set<String> existingRecords = new HashSet<>(Arrays.asList(oldCheckedSubjectsIds));
        for (String newCheckedSubject : newCheckedSubjectsIds) {
            if (existingRecords.contains(newCheckedSubject)) {
                // if exists - then do nothing
                log.trace("This faculty subjects records already exists in db: facultyId = {}, subjectId = {}",
                        faculty.getId(), newCheckedSubject);
            } else {
                // otherwise INSERT
                Long subjectId = Long.parseLong(newCheckedSubject);
                FacultySubjects facultySubject = new FacultySubjects(subjectId, faculty.getId());
                facultySubjectsRepository.save(facultySubject);
                log.trace("Faculty subjects record was created: {}", facultySubject);
            }
        }
        // and check for DELETE records that were previously checked and now are not
        Set<String> newRecords = new HashSet<>(Arrays.asList(newCheckedSubjectsIds));
        existingRecords.removeIf(newRecords::contains);
        if (!existingRecords.isEmpty()) {
            for (String subjectToRemove : existingRecords) {
                Long subjectId = Long.parseLong(subjectToRemove);
                FacultySubjects facultySubjectRecordToDelete =
                        new FacultySubjects(subjectId, faculty.getId());
                facultySubjectsRepository.delete(facultySubjectRecordToDelete);
                log.trace("Faculty subjects record was deleted: {}", facultySubjectRecordToDelete);
            }
        }
        return Path.REDIRECT_TO_FACULTY + nameEn;
    }

    public String viewFaculty(String nameEn, ModelMap map, HttpSession session) {
        log.trace("Faculty name to look for is equal to: '{}'", nameEn);
        Faculty facultyRecord = facultyRepository.findByNameEn(nameEn);
        log.trace("Faculty record found: {}", facultyRecord);
        map.put(Fields.ENTITY_ID, facultyRecord.getId());
        map.put(Fields.FACULTY_NAME_RU, facultyRecord.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, facultyRecord.getNameEn());
        map.put(Fields.FACULTY_TOTAL_PLACES, facultyRecord.getTotalPlaces());
        map.put(Fields.FACULTY_BUDGET_PLACES, facultyRecord.getBudgetPlaces());

        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(facultyRecord.getId());
        map.put("facultySubjects", facultySubjects);
        log.trace("Set the request attribute: 'facultySubjects' = {}", facultySubjects);

        String role = (String) session.getAttribute("userRole");

        if (role == null || Role.isUser(role)) {
            String userEmail = String.valueOf(session.getAttribute("user"));
            boolean applied = hasUserAppliedFacultyByEmail(facultyRecord, userEmail);
            map.put("alreadyApplied", applied ? "yes" : "no");
            return "/user/user_view_faculty";
        }
        if (!Role.isAdmin(role)) {
            return null;
        }

        Iterable<Applicant> applicants = applicantRepository.findAllByFacultyId(facultyRecord.getId());
        Map<Applicant, String> facultyApplicants = new TreeMap<>(Comparator.comparingLong(Applicant::getId));
        for (Applicant applicant : applicants) {
            User user = userRepository.findById(applicant.getUserId()).orElse(null);
            if (user == null) continue;
            facultyApplicants.put(applicant, user.getFirstName() + " " + user.getLastName());
        }
        map.put("facultyApplicants", facultyApplicants);
        log.trace("Set the request attribute: 'facultyApplicants' = {}", facultyApplicants);
        return "/admin/admin_view_faculty";
    }


    private boolean hasUserAppliedFacultyByEmail(Faculty faculty, String email) {
        User user = userRepository.findByEmail(email);
        Applicant a = applicantRepository.findByUserId(user.getId());
        FacultyApplicants fa =
                facultyApplicantsRepository.findByFacultyIdAndApplicantId(faculty.getId(), a.getId());
        return fa != null;
    }
}
