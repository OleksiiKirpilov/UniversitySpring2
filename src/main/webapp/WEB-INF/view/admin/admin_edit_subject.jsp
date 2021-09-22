<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<div class="form">
		<form id="edit_subject" method="POST" action="editSubject">
			<input type="hidden" name="command" value="editSubject"> <input
				type="hidden" name="oldName" value="${subject.nameEn}">

			<div class="field">
				<p>
					<label for="name_ru"><fmt:message
							key="subject.edit_jsp.label.name" /> (ru)</label> <input type="text"
						name="name_ru" id="name_ru" value="${subject.nameRu}" required>
				</p>
			</div>
			<div class="field">
				<p>
					<label for="name_en"><fmt:message
							key="subject.edit_jsp.label.name" /> (en)</label> <input type="text"
						name="name_en" id="name_en" value="${subject.nameEn}" required>
				</p>
			</div>
			<p>
				<input type="submit"
					value="<fmt:message key="subject.edit_jsp.button.submit" />">
			</p>
		</form>

		<p>
			<a href="viewSubject?name_en=${subject.nameEn}"><fmt:message
					key="subject.edit_jsp.button.back" /></a>
		</p>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

	<script src="${pageContext.request.contextPath}/script/subject-validation.js"></script>
</body>
</html>