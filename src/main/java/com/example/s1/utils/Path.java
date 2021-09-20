package com.example.s1.utils;

/**
 * Path holder to jsp pages.
 */
public final class Path {

	public static final String WELCOME_PAGE = "/welcome";
	public static final String ERROR_PAGE = "/errorPage";

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

	public static final String FORWARD_SUBJECT_VIEW_ALL_ADMIN = "/WEB-INF/view/admin/subject/list.jsp";
	public static final String FORWARD_SUBJECT_VIEW_ADMIN = "/WEB-INF/view/admin/subject/view.jsp";
	public static final String FORWARD_SUBJECT_ADD_ADMIN = "/WEB-INF/view/admin/subject/add.jsp";
	public static final String REDIRECT_SUBJECT_ADD_ADMIN = "/WEB-INF/view/admin/subject/add.jsp";
	public static final String FORWARD_SUBJECT_EDIT_ADMIN = "/WEB-INF/view/admin/subject/edit.jsp";
	public static final String REDIRECT_SUBJECT_EDIT_ADMIN = "/editSubject?name_en=";

	public static final String FORWARD_APPLICANT_PROFILE = "/WEB-INF/view/admin/applicant/view.jsp";
	public static final String REDIRECT_APPLICANT_PROFILE = "/viewApplicant?userId=";

	public static final String FORWARD_REPORT_SHEET_VIEW = "/WEB-INF/view/admin/report/view.jsp";

	private Path() {
	}
}