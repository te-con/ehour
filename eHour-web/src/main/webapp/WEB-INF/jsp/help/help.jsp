<%@ page contentType="text/html; charset=UTF-8"%>
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
	<tiles:importAttribute name="helpDocLink" scope="page"/>	
</c:if>

<c:if test="${helpHeader != ''}">
<div class="Help">
	<div class="GreyNavFrame">
		<h3 style="font-size: 1.0em">
			<fmt:message bundle="${help}" key="${helpHeader}"/></h3>
	
		<div class="GreyNavBody">
			<fmt:message bundle="${help}" key="${helpBody}"/>
			
			<br><br>
			
			<c:if test="${helpDocLink != ''}">
				<a target="_ehourDoc" href="http://www.ehour.nl/${helpDocLink}"><fmt:message bundle="${help}" key="general.linkA" /></a>
				<fmt:message key="general.linkB" bundle="${help}"  />
			</c:if>
		</div>
		<div class="GreyNavFrameFooter">
			<p>
			</p>
		</div>	
	</div>	
</div>
</c:if>
