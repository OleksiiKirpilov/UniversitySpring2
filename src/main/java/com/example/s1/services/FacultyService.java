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
        int total = Integer.parseInt(totalPlaces);
        int budget = Integer.parseInt(budgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budget, total);
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
        int totalPlaces = Integer.parseInt(facultyTotalPlaces);
        int budgetPlaces = Integer.parseInt(facultyBudgetPlaces);
        Faculty faculty = new Faculty(nameRu, nameEn, budgetPlaces, totalPlaces);
        Faculty oldFacultyRecord = facultyRepository.findByNameEn(oldFacultyName);
        faculty.setId(oldFacultyRecord.getId());
        facultyRepository.save(faculty);
        log.trace("Faculty record updated from: {}, to: {}", oldFacultyRecord, faculty);
        if (oldCheckedSubjectsIds == null) {
            if (newCheckedSubjectsIds == null) {
                // if before all subjects were unchecked and they still
                // are then nothing changed - do nothing
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
                facultySubjectsRepository.deleteByFacultyIdAndSubjectId(faculty.getId(), subjectId);
                log.trace("Faculty subjects record was deleted: facultyId={}, subjectId={}",
                        faculty.getId(), subjectId);
            }
        }
        return Path.REDIRECT_TO_FACULTY + nameEn;
    }

    public String viewFaculty(String nameEn, ModelMap map, HttpSession session) {
        Faculty facultyRecord = facultyRepository.findByNameEn(nameEn);
        map.put("faculty", facultyRecord);

        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(facultyRecord.getId());
        map.put("facultySubjects", facultySubjects);

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
        return Path.FORWARD_FACULTY_VIEW_ADMIN;
    }

    public String applyFacultyGet(String nameEn, ModelMap map) {
        Faculty faculty = facultyRepository.findByNameEn(nameEn);
        map.put("faculty", faculty);
        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(faculty.getId());
        map.put("facultySubjects", facultySubjects);
        Iterable<Subject> allSubjects = subjectRepository.findAll();
        map.put("allSubjects", allSubjects);
        return Path.FORWARD_FACULTY_APPLY_USER;
    }

    @Transactional
    public String applyFacultyPost(HttpSession session,
                                   @RequestParam(name = "id") Long facultyId,
                                   HttpServletRequest request) {
        log.trace("Start processing applying for faculty form");
        String email = String.valueOf(session.getAttribute("user"));
        User user = userRepository.findByEmail(email);
        Applicant applicant = applicantRepository.findByUserId(user.getId());
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
                Grade oldgrade = gradeRepository
                        .findBySubjectIdAndApplicantIdAndExamType(subjectId, applicant.getId(), examType);
                if (oldgrade == null) {
                    gradeRepository.save(grade);
                    log.trace("Grade record was created in database: {}", grade);
                } else
                    log.trace("Grade already exists. {}", oldgrade);
            }
        }
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
