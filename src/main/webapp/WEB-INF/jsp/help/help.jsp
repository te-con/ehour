<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!-- spanTarget: HelpTarget -->

<fmt:setBundle basename="HelpResources" var="help" />

<c:if test="${helpHeader == null}">
<%-- these may be imported in the enclosing layoutBase.jsp or try to import them here if this page
     is called thru an ajax request --%>
	<tiles:importAttribute name="helpHeader" scope="page"/>
	<tiles:importAttribute name="helpBody" scope="page"/>
</c:if>

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
