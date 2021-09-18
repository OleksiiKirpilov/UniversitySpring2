package com.example.s1.commands.profile;

import com.example.s1.commands.Command;
import com.example.s1.utils.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * View profile command.
 */
@Slf4j
public class ViewProfile extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response,
                          RequestType requestType)
            throws IOException, ServletException {
        log.debug("Command execution");
        if (requestType == RequestType.GET) {
            return doGet(request);
        }
        return null;
    }

    /**
     * Forwards user to his profile page, based on his role.
     *
     * @return path to user profile
     */
    private String doGet(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        String userEmail = String.valueOf(session.getAttribute("user"));
//        UserDao userDao = new UserDao();
//        User user = userDao.find(userEmail);
//        request.setAttribute("first_name", user.getFirstName());
//        log.trace("Set the request attribute: 'first_name' = {}", user.getFirstName());
//        request.setAttribute("last_name", user.getLastName());
//        log.trace("Set the request attribute: 'last_name' = {}", user.getLastName());
//        request.setAttribute("email", user.getEmail());
//        log.trace("Set the request attribute: 'email' = {}", user.getEmail());
//        request.setAttribute("role", user.getRole());
//        log.trace("Set the request attribute: 'role' = {}", user.getRole());
//        String role = user.getRole();
//        if (Role.isAdmin(role)) {
//            return Path.FORWARD_ADMIN_PROFILE;
//        }
//        if (Role.isUser(role)) {
//            ApplicantDao applicantDao = new ApplicantDao();
//            Applicant applicant = applicantDao.find(user);
//            request.setAttribute("city", applicant.getCity());
//            log.trace("Set the request attribute: 'city' = {}", applicant.getCity());
//            request.setAttribute("district", applicant.getDistrict());
//            log.trace("Set the request attribute: 'district' = {}", applicant.getDistrict());
//            request.setAttribute("school", applicant.getSchool());
//            log.trace("Set the request attribute: 'school' = {}", applicant.getSchool());
//            return Path.FORWARD_USER_PROFILE;
//        }
        return null;
    }
}