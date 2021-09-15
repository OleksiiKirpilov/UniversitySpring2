package com.example.s1.commands.faculty;

import com.example.s1.commands.Command;
import com.example.s1.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Invoked when user wants to delete faculty. Command allowed only for admins.
 */
public class DeleteFaculty extends Command {

    private static final long serialVersionUID = 1L;
//    private static final Logger LOG = LogManager.getLogger(DeleteFaculty.class);
//
    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
//        LOG.debug("Executing Command");
//        if (RequestType.POST == requestType) {
//            return doPost(request);
//        }
        return null;
    }
//
//    /**
//     * Redirects user to view of all faculties after submiting a delete button.
//     *
//     * @return path to view of all faculties if deletion was successful,
//     * otherwise to faculty view.
//     */
//    private String doPost(HttpServletRequest request) {
//        int facultyId = Integer.parseInt(request.getParameter(Fields.ENTITY_ID));
//        FacultyDao facultyDao = new FacultyDao();
//        Faculty facultyToDelete = facultyDao.find(facultyId);
//        ApplicantDao applicantDao = new ApplicantDao();
//        List<Applicant> facultyApplicants = applicantDao.findAllFacultyApplicants(facultyToDelete);
//        if (facultyApplicants != null) {
//            setErrorMessage(request, ERROR_FACULTY_DEPENDS);
//            return Path.REDIRECT_TO_FACULTY + facultyToDelete.getNameEn();
//        }
//        facultyDao.delete(facultyToDelete);
//        LOG.trace("Delete faculty record in database: {}", facultyToDelete);
//        return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
//    }
}
