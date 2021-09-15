<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<c:set var='view' value='/WEB-INF/view/admin/addAdmin.jsp' scope='session' />


	<div class="header">
		<fmt:message key="registration.label.enter_admin_info_msg" />
	</div>
	<br>
	<br>
	<div class="form">
		<form id="registration_form" method="POST" action="adminRegistration">
			<input type="hidden" name="command" value="adminRegistration" />

			<div class="field">
				<label for="lang"> <fmt:message
						key="registration.label.language" />
				</label>
				<select name="lang" id="lang">
					<option value="ru">Russian</option>
					<option value="en">English</option>
				</select>
			</div>
			<div class="field">
				<label for="first_name"><fmt:message key="registration.label.first_name" /></label>
				<input type="text" name="first_name" id="first_name" value="" required />
			</div>
			<div class="field">
				<label for="last_name"><fmt:message key="registration.label.last_name" /></label>
				<input type="text" name="last_name" id="last_name" value="" required />
			</div>
			<div class="field">
				<label for="email"><fmt:message key="registration.label.email" /></label>
				<input type="text" name="email" id="email" value="" required />
			</div>
			<div class="field">
				<label for="password"> <fmt:message key="registration.label.password" /></label>
				<input type="password" name="password" id="password" value="" required />
			</div>
			<br>
			<div class="field">
				<input type="reset" value="<fmt:message key="registration.button.reset" />" />
			</div>
			<br>
			<div class="field">
				<input type="submit" value="<fmt:message key="registration.button.submit" />" />
			</div>
		</form>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>