package com.example.unispring.util;

/**
 * Utility class for field names in database tables.
 */
public final class Fields {

    public static final String ENTITY_ID = "id";

    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_LANG = "lang";

    public static final String APPLICANT_CITY = "city";
    public static final String APPLICANT_DISTRICT = "district";
    public static final String APPLICANT_SCHOOL = "school";
    public static final String APPLICANT_IS_BLOCKED = "blocked";

    public static final String FACULTY_NAME_RU = "name_ru";
    public static final String FACULTY_NAME_EN = "name_en";

    public static final String REPORT_SHEET_FACULTY_FINALIZED = "finalized";

    private Fields() {
    }

}