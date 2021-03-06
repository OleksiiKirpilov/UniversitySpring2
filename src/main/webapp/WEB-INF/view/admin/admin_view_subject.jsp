<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<div class="view">
		<p>
			<label><fmt:message key="subject.view_jsp.label.name" /></label>
			<c:out value="${lang eq 'ru' ? subject.nameRu : subject.nameEn}" />
		</p>
		<a href="editSubject?name_en=${subject.nameEn}"><fmt:message key="subject.view_jsp.button.edit" /></a>
		<br> <br>
		<form id="delete_subject" action="deleteSubject" method="POST">
			<input type="hidden" name="command" value="deleteSubject" /><input
				type="hidden" name="id" value="${subject.id}" /><input type="submit"
				value="<fmt:message key="subject.view_jsp.button.delete" />" />
		</form>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>