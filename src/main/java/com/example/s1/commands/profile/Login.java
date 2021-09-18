package com.example.s1.commands.profile;

import com.example.s1.commands.Command;
import com.example.s1.utils.RequestType;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Invoked when user logins in the system.
 */
@Slf4j
public class Login extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        log.debug("Executing Command");
        if (requestType == RequestType.POST) {
            return doPost(request);
        } else {
            return null;
        }
    }

    /**
     * Logins user in system. As first page displays view of all faculties.
     *
     * @return path to the view of all faculties.
     */
    private String doPost(HttpServletRequest request) {
        return null;
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        UserDao userDao = new UserDao();
//        User user = userDao.find(email, password);
//        log.trace("User found: {}", user);
//        if (user == null) {
//            setErrorMessage(request, ERROR_CAN_NOT_FIND_USER);
//            log.error("errorMessage: Cannot find user with such login/password");
//            return null;
//        }
//        HttpSession session = request.getSession(true);
//        session.setAttribute("user", user.getEmail());
//        log.trace("Set session attribute 'user' = {}", user.getEmail());
//        session.setAttribute("userRole", user.getRole());
//        log.trace("Set session attribute: 'userRole' = {}", user.getRole());
//        session.setAttribute("lang", user.getLang());
//        log.trace("Set session attribute 'lang' = {}", user.getLang());
//        log.info("User: {} logged as {}", user, user.getRole());
//        return Path.REDIRECT_TO_VIEW_ALL_FACULTIES;
    }

}