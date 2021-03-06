package com.example.unispring.services;

import com.example.unispring.model.*;
import com.example.unispring.repository.*;
import com.example.unispring.util.Fields;
import com.example.unispring.util.InputValidator;
import com.example.unispring.util.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.example.unispring.util.MessageHelper.*;


@Slf4j
@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultySubjectsRepository facultySubjectsRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private FacultyApplicantsRepository facultyApplicantsRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private ReportSheetRepository reportSheetRepository;


    public String viewAllFaculties(HttpSession session, HttpServletRequest request) {
        Iterable<Faculty> faculties = facultyRepository.findAll();
        request.setAttribute("faculties", faculties);
        String role = (String) session.getAttribute("userRole");
        if (role == null || Role.isUser(role)) {
            return Path.FORWARD_FACULTY_VIEW_ALL_USER;
        }
        if (Role.isAdmin(role)) {
            return Path.FORWARD_FACULTY_VIEW_ALL_ADMIN;
        }
        return Path.WELCOME_PAGE;
    }

    @Transactional
    public String addFaculty(String nameEn, String nameRu,
                             String budgetPlaces, String totalPlaces,
                             Long[] subjects, HttpServletRequest request) {
        boolean valid = InputValidator.validateFacultyParameters(nameRu,
                nameEn, budgetPlaces, totalPlaces);
        if (!valid) {
            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
            log.debug("errorMessage: Not all fields are properly filled");
            return Path.REDIRECT_FACULTY_ADD_ADMIN;
        }
        int total = Integer.parseInt(totalPlaces);
        int budget = Integer.parseInt(budgetPlaces);
        if (facultyRepository.findByNameEn(nameEn) != null
                || facultyRepository.findByNameRu(nameRu) != null) {
            setErrorMessage(request, ERROR_FACULTY_EXISTS);
            log.debug("Can not create faculty with names {}, {}", nameEn, nameRu);
            return Path.REDIRECT_FACULTY_ADD_ADMIN;
        }
        Faculty faculty = new Faculty(nameRu, nameEn, budget, total);
        facultyRepository.save(faculty);
        log.trace("Create faculty record in database: {}", faculty);
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

    public String deleteFaculty(Long id, HttpSession session) {
        Faculty facultyToDelete = facultyRepository.findById(id).orElse(null);
        List<Applicant> facultyApplicants = applicantRepository.findAllByFacultyId(id);
        if (!facultyApplicants.isEmpty()) {
            setErrorMessage(session, ERROR_FACULTY_DEPENDS);
            return Path.REDIRECT_TO_FACULTY + facultyToDelete.getNameEn();
        }
        facultyRepository.delete(facultyToDelete);
        log.trace("Delete faculty record in database: {}", facultyToDelete);
        return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
    }

    @Transactional
    public String updateFaculty(String oldFacultyName, Faculty faculty,
                                String[] oldCheckedSubjectsIds, String[] newCheckedSubjectsIds,
                                HttpServletRequest request) {
        Faculty oldFacultyRecord = facultyRepository.findByNameEn(oldFacultyName);
        faculty.setId(oldFacultyRecord.getId());
        List<Applicant> facultyApplicants = applicantRepository.findAllByFacultyId(faculty.getId());
        if (!facultyApplicants.isEmpty()) {
            setErrorMessage(request.getSession(), ERROR_FACULTY_DEPENDS);
            return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
        }
        facultyRepository.save(faculty);
        log.trace("Faculty record updated from: {}, to: {}", oldFacultyRecord, faculty);
        if (oldCheckedSubjectsIds == null) {
            if (newCheckedSubjectsIds == null) {
                // if before all subjects were unchecked and they still
                // are then nothing changed - do nothing
                return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
            }
            // if user checked something,but before no subjects were checked
            for (String newCheckedSubject : newCheckedSubjectsIds) {
                Long subjectId = Long.parseLong(newCheckedSubject);
                FacultySubjects facultySubject = new FacultySubjects(subjectId, faculty.getId());
                facultySubjectsRepository.save(facultySubject);
                log.trace("Faculty subjects record was created: {}", facultySubject);
            }
            return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
        }
        if (newCheckedSubjectsIds == null) {
            // if user unchecked all checkboxes and before there were some checked subjects
            facultySubjectsRepository.deleteAllByFacultyId(faculty.getId());
            return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
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
        return Path.REDIRECT_TO_FACULTY + faculty.getNameEn();
    }

    public String viewFaculty(String nameEn, ModelMap map, HttpSession session) {
        Faculty facultyRecord = facultyRepository.findByNameEn(nameEn);
        map.put("faculty", facultyRecord);

        Iterable<Subject> facultySubjects = subjectRepository.findAllByFacultyId(facultyRecord.getId());
        map.put("facultySubjects", facultySubjects);

        List<ReportSheet> report = reportSheetRepository.getAllByFacultyIdEquals(facultyRecord.getId());
        boolean finalized = !report.isEmpty();
        map.put(Fields.REPORT_SHEET_FACULTY_FINALIZED, finalized);

        String role = (String) session.getAttribute("userRole");
        String userEmail = (String) session.getAttribute("user");
        if (Role.isUser(role)) {
            boolean applied = hasUserAppliedFacultyByEmail(facultyRecord, userEmail);
            map.put("alreadyApplied", applied);
            boolean enrolled = isApplicantEnrolled(userEmail, report);
            map.put("enrolled", enrolled);
            return Path.FORWARD_FACULTY_VIEW_USER;
        }
        if (!Role.isAdmin(role)) {
            return Path.FORWARD_FACULTY_VIEW_USER;
        }
        Iterable<Applicant> applicants = applicantRepository.findAllByFacultyId(facultyRecord.getId());
        Map<Applicant, String> facultyApplicants = new LinkedHashMap<>();
        for (Applicant applicant : applicants) {
            User user = userRepository.findById(applicant.getUserId()).orElse(null);
            if (user == null) {
                continue;
            }
            facultyApplicants.put(applicant, user.getFirstName() + " " + user.getLastName());
        }
        map.put("facultyApplicants", facultyApplicants);
        return Path.FORWARD_FACULTY_VIEW_ADMIN;
    }

    public String applyFacultyPage(String nameEn, ModelMap map) {
        Faculty faculty = facultyRepository.findByNameEn(nameEn);
        map.put("faculty", faculty);
        List<Subject> facultySubjects = subjectRepository.findAllByFacultyId(faculty.getId());
        map.put("facultySubjects", facultySubjects);
        List<Subject> allSubjects = subjectRepository.findAll();
        map.put("allSubjects", allSubjects);
        return Path.FORWARD_FACULTY_APPLY_USER;
    }

    @Transactional
    public String applyFaculty(HttpSession session,
                               Long facultyId,
                               HttpServletRequest request) {
        String email = String.valueOf(session.getAttribute("user"));
        User user = userRepository.findByEmail(email);
        Applicant applicant = applicantRepository.findByUserId(user.getId());
        FacultyApplicants newFacultyApplicant = new FacultyApplicants();
        newFacultyApplicant.setFacultyId(facultyId);
        newFacultyApplicant.setApplicantId(applicant.getId());
        FacultyApplicants existingRecord =
                facultyApplicantsRepository.findByFacultyIdAndApplicantId(facultyId, applicant.getId());
        if (existingRecord != null) {
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
                Grade oldGrade = gradeRepository
                        .findBySubjectIdAndApplicantIdAndExamType(subjectId, applicant.getId(), examType);
                if (oldGrade == null) {
                    gradeRepository.save(grade);
                    log.trace("Grade record was created in database: {}", grade);
                } else if (!oldGrade.isConfirmed()) {
                    grade.setId(oldGrade.getId());
                    gradeRepository.save(grade);
                    log.trace("Grade record was updated in database: {}", grade);
                } else {
                    log.trace("Grade already exists. {}", oldGrade);
                }
            }
        }
        facultyApplicantsRepository.save(newFacultyApplicant);
        log.trace("FacultyApplicants record was created in database: {}", newFacultyApplicant);
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

    private boolean isApplicantEnrolled(String email, List<ReportSheet> report) {
        return report.stream()
                .filter(r -> r.getEmail().equals(email))
                .anyMatch(ReportSheet::isEntered);
    }

}
