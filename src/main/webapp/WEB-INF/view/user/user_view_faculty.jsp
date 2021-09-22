<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<h2 align="center">
		<c:out value="${lang eq 'ru' ? faculty.nameRu : faculty.nameEn}" />
	</h2>
	<div class="view">
		<p>
			<label><fmt:message key="faculty.view_jsp.label.name" /></label>
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
					<li><c:out value="${lang eq 'ru' ? subject.nameRu : subject.nameEn}"></c:out></li>
				</c:forEach>
			</ol>
		</c:if>

		<c:if test="${userRole eq 'user' and alreadyApplied eq 'no'}">
			<p>
				<a href="applyFaculty?name_en=${faculty.nameEn}">
					<fmt:message key="faculty.view_jsp.button.apply" /></a>
			</p>
		</c:if>
		<c:if test="${userRole eq 'user' and alreadyApplied eq 'yes'}">
			<p><fmt:message key="faculty.view_jsp.label.already_applied" /></p>
		</c:if>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>