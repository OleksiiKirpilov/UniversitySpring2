package com.example.s1.commands.profile;

import com.example.s1.commands.Command;
import com.example.s1.utils.Path;
import com.example.s1.utils.RequestType;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Invoked when user wants to log out from the system.
 */
@Slf4j
public class Logout extends Command {

    private static final long serialVersionUID = -2785976616686657267L;

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response, RequestType requestType)
            throws IOException, ServletException {
        log.debug("Start executing Command");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        log.debug("Finished executing Command");
        return Path.WELCOME_PAGE;
    }

}