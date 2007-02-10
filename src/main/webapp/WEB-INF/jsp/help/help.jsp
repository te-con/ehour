<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<fmt:setBundle basename="HelpResources" var="help" />

<c:if test="${helpHeader != ''}">
	<div class="HelpFrame">
		<h3><fmt:message bundle="${help}" key="${helpHeader}"/></h3>
	
		<div class="HelpBody">
			<fmt:message bundle="${help}" key="${helpBody}"/>
		</div>
		<div class="HelpFrameFooter">
			<p>
			</p>
		</div>	
	</div>	
</c:if>
