<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!-- spanTarget: locale -->

<form method="post" action="saveConfig.do" id="configForm">
<input type="hidden" name="fromForm" value="true">
<table class="contentTable">
	<tr>
		<td><fmt:message key="admin.config.locale.language" />:</td>
		<td><select class="textInputSmall" id="languageId" name="language">
				<c:forEach items="${languages}" var="locale">
					<option value="${locale.language}" <c:if test="${config.localeLanguage == locale.language}">
							SELECTED
						</c:if>
						>${locale.displayName}
				</c:forEach>
			</select>
		</td>
	</tr>
		
	<tr>
		<td>
			<fmt:message key="admin.config.locale.language.onlyTranslations" />:
		</td>
		
		<td>
			<input type="checkbox"
					id="showTranslationsOnlyId"
					name="showTranslationsOnly"
					<c:if test="${form.showTranslationsOnly}">CHECKED</c:if>>
		</td>
	</tr>

	<tr>
		<td>
			<fmt:message key="admin.config.locale.language.noForce" />:
		</td>
		
		<td>
			<input type="checkbox" id="noForce" name="noForce" <c:if test="${form.noForce}">CHECKED</c:if>>
		</td>
	</tr>
	
	<tr>
		<td colspan="2"><br></td>
	</tr>
	
	<tr>
		<td><fmt:message key="admin.config.locale.currency" />:</td>
		<td><select class="textInputSmall" id="currency" name="currency">
				<c:forEach items="${currencies}" var="currency">
					<option value="${currency.key}"
						<c:if test="${form.currency == currency.key}">SELECTED</c:if>
					>${currency.key} (${currency.value})
				</c:forEach>
			</select>
		</td>
	</tr>	

	<tr>
		<td colspan="2"><br><br></td>
	</tr>
	
	
	<tr>
		<td><fmt:message key="admin.config.showTurnover" />:</td>
		<td><input type="checkbox"
					id="showTurnOver"
					name="showTurnOver"
					<c:if test="${form.showTurnOver}">CHECKED</c:if>>
		</td>
	</tr>	

	<tr>
		<td colspan="2"><br><br></td>
	</tr>
	
	<tr>
		<td>
			<c:if test="${updateMsg != null}">
				<fmt:message key="${updateMsg}" />
			</c:if>
		</td><td>
				<input type="submit" class="submitButtonBlue" value="<fmt:message key="admin.config.save" />">		
		</td>
	</tr>
</table>

</form>

<script>
	initConfigAdmin();
</script>