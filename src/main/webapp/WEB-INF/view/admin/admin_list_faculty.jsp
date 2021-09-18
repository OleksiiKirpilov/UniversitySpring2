<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<div class="header">
		<fmt:message key="faculty.list_jsp.label.faculties_list" />
	</div>
	<p>
		<a href="addFaculty"><fmt:message
				key="faculty.list_jsp.button.add" /></a>
	</p>
	<table id="facultiesTable" class="display">
		<thead>
			<tr>
				<td><fmt:message key="faculty.list_jsp.label.name" /></td>
				<td><fmt:message key="faculty.list_jsp.label.total_places" /></td>
				<td><fmt:message key="faculty.list_jsp.label.budget_places" /></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="faculty" items="${faculties}">
				<tr>
					<td>
						<a href="<c:url value="viewFaculty"><c:param name="name_en" value="${faculty.nameEn}"/></c:url>">
							<c:out value="${lang eq 'ru' ? faculty.nameRu : faculty.nameEn}"></c:out>
						</a>
					</td>
					<td><c:out value="${faculty.totalPlaces}"></c:out></td>
					<td><c:out value="${faculty.budgetPlaces}"></c:out></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

	<h:datatables table="#facultiesTable"/>

</body>
</html>