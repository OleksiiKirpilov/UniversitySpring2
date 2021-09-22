<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<div class="view">
		<p>
			<label><fmt:message key="profile.view_jsp.label.first_name" />
			</label>
			<c:out value="${user.firstName}"></c:out>
		</p>
		<p>
			<label><fmt:message key="profile.view_jsp.label.last_name" />
			</label>
			<c:out value="${user.lastName}"></c:out>
		</p>
		<p>
			<label><fmt:message key="profile.view_jsp.label.email" /> </label>
			<c:out value="${user.email}"></c:out>
		</p>
		<a href="editProfile?user=${user.email}"><fmt:message
				key="profile.view_jsp.button.edit" /></a> <br>
		<p>
			<a href="adminRegistration"><fmt:message
					key="profile.view_jsp.label.register_new_admin" /></a>
		</p>
		<br>
		<form action="logout" method="POST">
			<input type="hidden" name="command" value="logout"> <input
				type="submit"
				value="<fmt:message key="profile.view_jsp.button.logout" />">
		</form>

	</div>

	<br><br>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>