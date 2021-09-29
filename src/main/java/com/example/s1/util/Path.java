package com.example.s1.util;

/**
 * Path holder to jsp pages.
 */
public final class Path {

	public static final String WELCOME_PAGE = "welcome";
	public static final String ERROR_PAGE = "error";

	public static final String FORWARD_USER_REGISTRATION_PAGE = "/user/user_add_user";
	public static final String REDIRECT_USER_REGISTRATION_PAGE = "redirect:/userRegistration";

	public static final String FORWARD_ADMIN_REGISTRATION_PAGE = "/admin/admin_add_admin";
	public static final String REDIRECT_ADMIN_REGISTRATION_PAGE = "redirect:/adminRegistration";

	public static final String REDIRECT_TO_PROFILE = "redirect:/viewProfile";
	public static final String REDIRECT_TO_FACULTY = "redirect:/viewFaculty?name_en=";
	public static final String REDIRECT_TO_SUBJECT = "redirect:/viewSubject?name_en=";
	public static final String REDIRECT_TO_VIEW_ALL_FACULTIES = "redirect:/viewAllFaculties";
	public static final String REDIRECT_TO_VIEW_ALL_SUBJECTS = "redirect:/viewAllSubjects";

	public static final String FORWARD_ADMIN_PROFILE = "/admin/admin_view_profile";
	public static final String FORWARD_ADMIN_PROFILE_EDIT = "/admin/admin_edit_profile";

	public static final String FORWARD_USER_PROFILE = "/user/user_view_profile";
	public static final String FORWARD_USER_PROFILE_EDIT = "/user/user_edit_profile";

	public static final String REDIRECT_EDIT_PROFILE = "redirect:/editProfile";

	public static final String FORWARD_FACULTY_VIEW_ALL_ADMIN = "/admin/admin_list_faculty";
	public static final String FORWARD_FACULTY_VIEW_ALL_USER = "/user/user_list_faculty";

	public static final String FORWARD_FACULTY_VIEW_ADMIN = "/admin/admin_view_faculty";
	public static final String FORWARD_FACULTY_EDIT_ADMIN = "/admin/admin_edit_faculty";
	public static final String REDIRECT_FACULTY_EDIT_ADMIN = "redirect:/editFaculty?name_en=";
	public static final String FORWARD_FACULTY_ADD_ADMIN = "/admin/admin_add_faculty";
	public static final String REDIRECT_FACULTY_ADD_ADMIN = "redirect:/addFaculty";

	public static final String FORWARD_FACULTY_VIEW_USER = "/user/user_view_faculty";
	public static final String FORWARD_FACULTY_APPLY_USER = "/user/user_apply_faculty";

	public static final String FORWARD_SUBJECT_VIEW_ALL_ADMIN = "/admin/admin_list_subject";
	public static final String FORWARD_SUBJECT_VIEW_ADMIN = "/admin/admin_view_subject";
	public static final String FORWARD_SUBJECT_ADD_ADMIN = "/admin/admin_add_subject";
	public static final String REDIRECT_SUBJECT_ADD_ADMIN = "redirect:/admin/admin_add_subject";
	public static final String FORWARD_SUBJECT_EDIT_ADMIN = "/admin/admin_edit_subject";
	public static final String REDIRECT_SUBJECT_EDIT_ADMIN = "redirect:/editSubject?name_en=";

	public static final String FORWARD_APPLICANT_PROFILE = "/admin/admin_view_applicant";
	public static final String REDIRECT_APPLICANT_PROFILE = "redirect:/viewApplicant?userId=";

	public static final String FORWARD_REPORT_SHEET_VIEW = "/admin/admin_view_report";
	public static final String REDIRECT_REPORT_SHEET_VIEW = "redirect:/createReport?id=";

	private Path() {
	}
}