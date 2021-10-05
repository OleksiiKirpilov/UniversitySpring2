package com.example.unispring.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class MessageHelper {

    public static final String ERROR_FILL_ALL_FIELDS = "error_message.fill_all_fields";
    public static final String ERROR_FACULTY_DEPENDS = "error_message.can_not_delete_faculty_dependent";
    public static final String ERROR_CAN_NOT_FIND_USER = "error_message.can_not_find_user";
    public static final String ERROR_EMAIL_USED = "error_message.email_in_use";
    public static final String MESSAGE_ACCOUNT_CREATED = "profile.view_jsp.message.account_created";
    public static final String ERROR_FACULTY_EXISTS = "error_message.faculty_exists";
    public static final String ERROR_SUBJECT_DELETE = "error_message.subject_delete";


    public static void setErrorMessage(HttpServletRequest request, String messageKey) {
        setMessage(request, "errorMessage", messageKey);
    }

    public static void setErrorMessage(HttpSession session, String messageKey) {
        setMessage(session, "errorMessage", messageKey);
    }

    public static void setOkMessage(HttpServletRequest request, String messageKey) {
        setMessage(request, "successfulMessage", messageKey);
    }

    public static void setOkMessage(HttpSession session, String messageKey) {
        setMessage(session, "successfulMessage", messageKey);
    }

    private static void setMessage(HttpServletRequest request, String attribute, String messageKey) {
        HttpSession session = request.getSession();
        session.setAttribute(attribute, messageKey);
    }

    private static void setMessage(HttpSession session, String attribute, String messageKey) {
        session.setAttribute(attribute, messageKey);
    }

    private MessageHelper() {}
}
