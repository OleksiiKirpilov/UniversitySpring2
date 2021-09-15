<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<c:set var='view' value='/WEB-INF/view/admin/applicant/view.jsp' scope='session' />

	<div class="view">

		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.first_name" />
			</label>
			<c:out value="${requestScope.first_name}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.last_name" />
			</label>
			<c:out value="${requestScope.last_name}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.email" />
			</label>
			<c:out value="${requestScope.email}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.city" />
			</label>
			<c:out value="${requestScope.city}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.district" />
			</label>
			<c:out value="${requestScope.district}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.school" />
			</label>
			<c:out value="${requestScope.school}"></c:out>
		</p>

		<p>
			<label>
				<fmt:message key="applicant.view_jsp.label.blocked_status" />
			</label>
			<c:if test="${requestScope.isBlocked == true}">
				<fmt:message key="applicant.view_jsp.label.blocked" />
			</c:if>
			<c:if test="${requestScope.isBlocked == false}">
				<fmt:message key="applicant.view_jsp.label.unblocked" />
			</c:if>
		</p>

		<form action="viewApplicant" method="POST">
			<input type="hidden" name="command" value="viewApplicant">
			<input type="hidden" name="id" value="${requestScope.id}">
			<input type="submit"
				   value="<fmt:message key="applicant.view_jsp.button.change_blocked_status_msg" />">
		</form>

	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>