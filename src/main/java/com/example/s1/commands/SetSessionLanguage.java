package com.example.s1.commands;

import com.example.s1.utils.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SetSessionLanguage extends com.example.s1.commands.Command {

    private static final long serialVersionUID = -8779243740825414648L;
    private static final Logger LOG = LogManager.getLogger(SetSessionLanguage.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response,
                          RequestType requestType) throws IOException, ServletException {
        LOG.debug("Executing Command");
        if (requestType == RequestType.GET) {
            return doGet(request, response);
        }
        return null;
    }

    private String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String lang = "en".equals(request.getParameter("lang")) ? "en" : "ru";
        session.setAttribute("lang", lang);
        LOG.trace("Set session attribute 'lang' = {}", lang);
        //return (String) session.getAttribute("view");
        return null;
    }
}
