package com.example.s1.commands.faculty;

import com.example.s1.commands.Command;
import com.example.s1.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Invoked when admin wants to edit information about some faculty
 */
public class EditFaculty extends Command {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(EditFaculty.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        LOG.debug("Executing Command");
        return (RequestType.GET == requestType)
                ? doGet(request)
                : doPost(request);
    }

    /**
     * Forwards to the edit faculty page
     *
     * @return path to edit page.
     */
    private String doGet(HttpServletRequest request) {
//        String facultyName = request.getParameter(Fields.FACULTY_NAME_EN);
//        FacultyDao facultyDao = new FacultyDao();
//        Faculty faculty = facultyDao.find(facultyName);
//        request.setAttribute(Fields.FACULTY_NAME_RU, faculty.getNameRu());
//        LOG.trace("Set attribute 'name_ru': {}", faculty.getNameRu());
//        request.setAttribute(Fields.FACULTY_NAME_EN, faculty.getNameEn());
//        LOG.trace("Set attribute 'name_en': {}", faculty.getNameEn());
//        request.setAttribute(Fields.FACULTY_TOTAL_PLACES,
//                faculty.getTotalPlaces());
//        LOG.trace("Set attribute 'total_places': {}", faculty.getTotalPlaces());
//        request.setAttribute(Fields.FACULTY_BUDGET_PLACES,
//                faculty.getBudgetPlaces());
//        LOG.trace("Set attribute 'budget_places': {}", faculty.getBudgetPlaces());
//        SubjectDao subjectDao = new SubjectDao();
//        List<Subject> otherSubjects = subjectDao.findAllNotFacultySubjects(faculty);
//        request.setAttribute("otherSubjects", otherSubjects);
//        LOG.trace("Set attribute 'otherSubjects': {}", otherSubjects);
//        List<Subject> facultySubjects = subjectDao.findAllFacultySubjects(faculty);
//        request.setAttribute("facultySubjects", facultySubjects);
//        LOG.trace("Set attribute 'facultySubjects': {}", facultySubjects);
//        return Path.FORWARD_FACULTY_EDIT_ADMIN;
        return null;
    }

    /**
     * Edits faculty according to entered data by admin.
     *
     * @return path to the view of edited faculty if succeeded, otherwise
     * refreshes page
     */
    private String doPost(HttpServletRequest request) {
//        // get parameters from page
//        String facultyNameRu = request.getParameter(Fields.FACULTY_NAME_RU);
//        LOG.trace("Get parameter 'name_ru' = {}", facultyNameRu);
//        String facultyNameEn = request.getParameter(Fields.FACULTY_NAME_EN);
//        LOG.trace("Get parameter 'name_en' = {}", facultyNameEn);
//        String facultyTotalPlaces = request.getParameter(Fields.FACULTY_TOTAL_PLACES);
//        LOG.trace("Get parameter 'total_places = {}", facultyTotalPlaces);
//        String facultyBudgetPlaces = request.getParameter(Fields.FACULTY_BUDGET_PLACES);
//        LOG.trace("Get parameter 'budget_places' = {}", facultyBudgetPlaces);
//        // if user changes faculty name we need to know the old one
//        // to update record in db
//        String oldFacultyName = request.getParameter("oldName");
//        LOG.trace("Get old faculty name from page: {}", oldFacultyName);
//        boolean valid = InputValidator.validateFacultyParameters(facultyNameRu,
//                facultyNameEn, facultyBudgetPlaces, facultyTotalPlaces);
//        if (!valid) {
//            setErrorMessage(request, ERROR_FILL_ALL_FIELDS);
//            LOG.error("errorMessage: Not all fields are properly filled");
//            return Path.REDIRECT_FACULTY_EDIT_ADMIN + oldFacultyName;
//        }
//        // if it's true then let's start to update the db
//        LOG.trace("All fields are properly filled. Start updating database.");
//        int totalPlaces = Integer.parseInt(facultyTotalPlaces);
//        int budgetPlaces = Integer.parseInt(facultyBudgetPlaces);
//        Faculty faculty = new Faculty(facultyNameRu, facultyNameEn,
//                budgetPlaces, totalPlaces);
//        FacultyDao facultyDao = new FacultyDao();
//        Faculty oldFacultyRecord = facultyDao.find(oldFacultyName);
//        faculty.setId(oldFacultyRecord.getId());
//        facultyDao.update(faculty);
//        LOG.trace("Faculty record updated from: {}, to: {}",
//                oldFacultyRecord, faculty);
//        String[] oldCheckedSubjectsIds = request.getParameterValues("oldCheckedSubjects");
//        String[] newCheckedSubjectsIds = request.getParameterValues("subjects");
//        if (LOG.isTraceEnabled()) {
//            LOG.trace("Get checked subjects before: {}",
//                    Arrays.toString(oldCheckedSubjectsIds));
//            LOG.trace("Get checked subjects after: {}",
//                    Arrays.toString(newCheckedSubjectsIds));
//        }
//        FacultySubjectsDao facultySubjectsDao = new FacultySubjectsDao();
//        if (oldCheckedSubjectsIds == null) {
//            if (newCheckedSubjectsIds == null) {
//                // if before all subjects were unchecked and they are still
//                // are
//                // then nothing changed - do nothing
//                LOG.trace("No faculty subjects records will be changed");
//                return Path.REDIRECT_TO_FACULTY + facultyNameEn;
//            }
//            // if user checked something,but before no subjects were
//            // checked
//            for (String newCheckedSubject : newCheckedSubjectsIds) {
//                int subjectId = Integer.parseInt(newCheckedSubject);
//                FacultySubjects facultySubject = new FacultySubjects(
//                        subjectId, faculty.getId());
//                facultySubjectsDao.create(facultySubject);
//                LOG.trace("Faculty subjects record was created: {}", facultySubject);
//            }
//            return Path.REDIRECT_TO_FACULTY + facultyNameEn;
//        }
//        if (newCheckedSubjectsIds == null) {
//            // if user unchecked all checkboxes and before
//            // there were some checked subjects
//            LOG.trace("No subjects were checked for this faculty - all records that will be found will be deleted ");
//            facultySubjectsDao.deleteAllSubjects(faculty);
//            return Path.REDIRECT_TO_FACULTY + facultyNameEn;
//        }
//        // if there were checked subjects and still are
//        // then for INSERT we should check if the record already
//        // exists in db
//        Set<String> existingRecords = new HashSet<>(
//                Arrays.asList(oldCheckedSubjectsIds));
//        for (String newCheckedSubject : newCheckedSubjectsIds) {
//            if (existingRecords.contains(newCheckedSubject)) {
//                // if exists - then do nothing
//                LOG.trace("This faculty subjects records already exists in db: facultyId = {}, subjectId = {}",
//                        faculty.getId(), newCheckedSubject);
//            } else {
//                // otherwise INSERT
//                int subjectId = Integer.parseInt(newCheckedSubject);
//                FacultySubjects facultySubject = new FacultySubjects(
//                        subjectId, faculty.getId());
//                facultySubjectsDao.create(facultySubject);
//                LOG.trace("Faculty subjects record was created: {}", facultySubject);
//            }
//        }
//        // and check for DELETE records that were previously
//        // checked and now are not
//        Set<String> newRecords = new HashSet<>(Arrays.asList(newCheckedSubjectsIds));
//        existingRecords.removeIf(newRecords::contains);
//        if (!existingRecords.isEmpty()) {
//            for (String subjectToRemove : existingRecords) {
//                int subjectId = Integer.parseInt(subjectToRemove);
//                FacultySubjects facultySubjectRecordToDelete = new FacultySubjects(
//                        subjectId, faculty.getId());
//                facultySubjectsDao.delete(facultySubjectRecordToDelete);
//                LOG.trace("Faculty subjects record was deleted: {}", facultySubjectRecordToDelete);
//            }
//        }
//        return Path.REDIRECT_TO_FACULTY + facultyNameEn;
        return null;
    }

}
