package com.example.s1.services;

import com.example.s1.model.*;
import com.example.s1.repository.*;
import com.example.s1.utils.Fields;
import com.example.s1.utils.InputValidator;
import com.example.s1.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    GradeRepository gradeRepository;


    @Transactional
    public String addFaculty(String nameEn, String nameRu,
                             String budgetPlaces, String totalPlaces,
                             Long[] subjects) {
        boolean valid = InputValidator.validateFacultyParameters(nameRu,
                nameEn, budgetPlaces, totalPlaces);
        if (!valid) {
            //setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.error("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_FACULTY_ADD_ADMIN;
        }
        log.trace("All fields are properly filled. Start updating database.");
        int total = Integer.parseInt(totalPlaces);
        int budget = Integer.parseInt(budgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budget, total);
        log.trace("Create faculty transfer object: {}", faculty);
        facultyRepository.save(faculty);
        log.trace("Create faculty record in database: {}", faculty);
        // only after creating a faculty record we can proceed with
        // adding faculty subjects
        if (subjects != null) {
            List<FacultySubjects> newFS = new ArrayList<>();
            for (Long id : subjects) {
                newFS.add(new FacultySubjects(id, faculty.getId()));
            }
            facultySubjectsRepository.saveAll(newFS);
            log.trace("FacultySubjects record created in database: {}", newFS);
        }
        return Path.REDIRECT_TO_FACULTY + nameEn;
    }

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
            log.trace("Get checked subjects after: {}", Arrays.toString(newCheckedSubjectsIds));
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
            facultySubjectsRepository.deleteAllByFacultyId(faculty.getId());
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
        String userEmail = (String) session.getAttribute("user");
        if (Role.isUser(role)) {
            boolean applied = hasUserAppliedFacultyByEmail(facultyRecord, userEmail);
            map.put("alreadyApplied", applied ? "yes" : "no");
            return Path.FORWARD_FACULTY_VIEW_USER;
        }
        if (!Role.isAdmin(role)) {
            return Path.FORWARD_FACULTY_VIEW_USER;
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
        return Path.FORWARD_FACULTY_VIEW_ADMIN;
    }

    public String applyFacultyGet(String nameEn, ModelMap map) {
        Faculty faculty = facultyRepository.findByNameEn(nameEn);
        map.put(Fields.ENTITY_ID, faculty.getId());
        log.trace("Set the request faculty attribute: 'id' = {}", faculty.getId());
        map.put(Fields.FACULTY_NAME_RU, faculty.getNameRu());
        log.trace("Set the request attribute: 'name' = {}", faculty.getNameRu());
        map.put(Fields.FACULTY_NAME_EN, faculty.getNameEn());
        log.trace("Set the request attribute: 'name_en' = {}", faculty.getNameEn());
        map.put(Fields.FACULTY_TOTAL_PLACES, faculty.getTotalPlaces());
        log.trace("Set the request attribute: 'total_places' = {}", faculty.getTotalPlaces());
        map.put(Fields.FACULTY_BUDGET_PLACES, faculty.getBudgetPlaces());
        log.trace("Set the request attribute: 'budget_places' = {}", faculty.getBudgetPlaces());
        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(faculty.getId());
        map.put("facultySubjects", facultySubjects);
        log.trace("Set attribute 'facultySubjects': {}", facultySubjects);
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        map.put("allSubjects", allSubjects);
        log.trace("Set attribute 'allSubjects': {}", allSubjects);
        return Path.FORWARD_FACULTY_APPLY_USER;
    }

    @Transactional
    public String applyFacultyPost(HttpSession session,
                                   @RequestParam(name = "id") Long facultyId,
                                   HttpServletRequest request) {
        log.trace("Start processing applying for faculty form");
        String email = String.valueOf(session.getAttribute("user"));
        User user = userRepository.findByEmail(email);
        log.trace("Found user in database that wants to apply: {}", user);
        Applicant applicant = applicantRepository.findByUserId(user.getId());
        log.trace("Found applicant record in database for this user: {}", applicant);
        FacultyApplicants newFacultyApplicant = new FacultyApplicants();
        newFacultyApplicant.setFacultyId(facultyId);
        newFacultyApplicant.setApplicantId(applicant.getId());
        FacultyApplicants existingRecord =
                facultyApplicantsRepository.findByFacultyIdAndApplicantId(facultyId, applicant.getId());
        if (existingRecord != null) {
            // user is already applied
            log.trace("User: {} with Applicant record: {} already applied for faculty with id: {}",
                    user, applicant, facultyId);
            return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
        }
        log.trace("Start extracting data from request");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> e : parameterMap.entrySet()) {
            String parameterName = e.getKey();
            if (parameterName.endsWith("preliminary") || parameterName.endsWith("diploma")) {
                String[] value = e.getValue();
                int gradeValue = Integer.parseInt(value[0]);
                String[] subjectIdAndExamType = parameterName.split("_");
                Long subjectId = Long.parseLong(subjectIdAndExamType[0]);
                String examType = subjectIdAndExamType[1];
                Grade grade = new Grade(subjectId, applicant.getId(), gradeValue, examType);
                log.trace("Create Grade transfer object: {}", grade);
                gradeRepository.save(grade);
                log.trace("Grade record was created in database: {}", grade);
            }
        }
        log.trace("End extracting data from request");
        log.trace("Create FacultyApplicants transfer object: {}", newFacultyApplicant);
        facultyApplicantsRepository.save(newFacultyApplicant);
        log.trace("FacultyApplicants record was created in database: {}", newFacultyApplicant);
        log.trace("Finished processing applying for faculty form");
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);
        return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
    }

    private boolean hasUserAppliedFacultyByEmail(Faculty faculty, String email) {
        User user = userRepository.findByEmail(email);
        Applicant a = applicantRepository.findByUserId(user.getId());
        FacultyApplicants fa =
                facultyApplicantsRepository.findByFacultyIdAndApplicantId(faculty.getId(), a.getId());
        return fa != null;
    }
}
