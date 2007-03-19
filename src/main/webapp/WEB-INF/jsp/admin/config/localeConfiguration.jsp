<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- spanTarget: locale -->

<form method="post" action="updateLocales.do">

<table class="contentTable">
	<tr>
		<td><fmt:message key="admin.config.locale.language" />:</td>
		<td><select class="textInputSmall" name="language">
				<c:forEach items="${languages}" var="locale">
					<option value="${locale.language}" <c:if test="${config.localeLanguage == locale.language}">
							SELECTED
						</c:if>>${locale.displayName}
				</c:forEach>
			</select>
		</td>
	</tr>
		
	<tr>
		<td>
			<fmt:message key="admin.config.locale.language.onlyTranslations" />:
		</td>
		
		<td>
			<input type="checkbox" id="showTranslationsOnly" name="showTranslationsOnly" <c:if test="${form.showTranslationsOnly}">CHECKED</c:if>>
		</td>
	</tr>
</table>

</form>

<script>
	initConfigAdmin();
</script>