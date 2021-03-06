<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<h2 align="center">
		<c:out value="${lang eq 'ru' ? faculty.nameRu : faculty.nameEn}"></c:out>
	</h2>
	<div class="view">
		<p>
			<label><fmt:message key="faculty.view_jsp.label.name" /> </label>
			<c:out value="${lang eq 'ru' ? faculty.nameRu : faculty.nameEn}"></c:out>
		</p>

		<p>
			<label><fmt:message key="faculty.view_jsp.label.total_places" /></label>
			<c:out value="${faculty.totalPlaces}"></c:out>
		</p>
		<p>
			<label><fmt:message key="faculty.view_jsp.label.budget_places" /></label>
			<c:out value="${faculty.budgetPlaces}"></c:out>
		</p>
		<p>
			<label><fmt:message
					key="faculty.view_jsp.label.preliminary_subjects" /></label>
		</p>

		<c:if test="${empty facultySubjects}">
			<fmt:message key="faculty.view_jsp.label.no_subjects_msg" />
		</c:if>

		<br>
		<c:if test="${not empty facultySubjects}">
			<ol>
				<c:forEach var="subject" items="${facultySubjects}">
					<li>
						<a href="<c:url value="viewSubject"><c:param name="name_en" value="${subject.nameEn}"/></c:url>">
							<c:out value="${lang eq 'ru' ? subject.nameRu : subject.nameEn}"></c:out>
						</a>
					</li>
				</c:forEach>
			</ol>
		</c:if>
		<p>
			<a
				href="editFaculty?name_en=${faculty.nameEn}"><fmt:message
					key="faculty.view_jsp.button.edit" /></a>
		</p>

		<p>
<%--			<a href="createReport?id=${id}">--%>
			<a href="<c:url value="createReport"><c:param name="id" value="${faculty.id}"/></c:url>">
				<fmt:message key="faculty.view_jsp.button.create_report" />
			</a>
		</p>
		<br>
		<form id="delete_faculty" action="deleteFaculty" method="POST">
			<input type="hidden" name="command" value="deleteFaculty" /><input
				type="hidden" name="id" value="${faculty.id}" /><input type="submit"
				value="<fmt:message key="faculty.view_jsp.button.delete" />" />
		</form>

		<br> <label><fmt:message
				key="faculty.view_jsp.label.faculty_applicants" /> </label> <br>
		<c:if test="${empty facultyApplicants}">
			<fmt:message key="faculty.view_jsp.label.no_faculty_applicants_msg" />
		</c:if>

		<c:if test="${not empty facultyApplicants}">
			<ol>
				<c:forEach var="applicant" items="${facultyApplicants}">
					<li>
						<a href="<c:url value="viewApplicant"><c:param name="userId" value="${applicant.key.userId}"/></c:url>">
							<c:out value="${applicant.value}"></c:out>
						</a>
						<br>
					</li>
				</c:forEach>
			</ol>
		</c:if>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>