<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<!-- spanTarget:form -->

<form id="CustomerForm" method="post" action="editCustomer.do">

<input type="hidden" id="customerId" name="customerId" value="${customer.customerId}">

<h1><c:choose>
		<c:when test="${customer == null || customer.customerId == null}">
			<fmt:message key="admin.customer.addCustomer" />
		</c:when>
		
		<c:otherwise>
			<fmt:message key="admin.customer.editCustomer" />
		</c:otherwise>
	</c:choose></h1>

<div class="GreyFrame">
	<h3>&nbsp;</h3>

	<div class="GreyFrameBody">	

	<table class="contentTable" cellspacing="2">
		<tr>
			<td colspan="3">
			</td>	
		</tr>

	<tr>
		<td width="20%"><fmt:message key="admin.customer.name" />:</td>
		<td><input class="textInputSmall"  type="text" name="name" maxlength="255" size="30" value="${customer.name}"></td>
		<td id="customerNameError" style="color: red"><html:errors property="name" /></td>

		<td rowspan=3>
			&nbsp;&nbsp;
		</td>
		
		<td rowspan="3" valign="top">
			<c:if test="${customer != null && customer.customerId != null}">
				<b><fmt:message key="admin.customer.projects" /></b>
				<br>
				
				<c:forEach var="project" items="${customer.projects}">
					${project.fullname}<br>
				</c:forEach>
			</c:if>
		</td>		
	</tr>

	<tr>
		<td><fmt:message key="admin.customer.code" />:</td>
		<td><input class="textInputSmall"  type="text" name="code" maxlength="32" size="30" value="${customer.code}"></td>
		<td id="customerCodeError" style="color: red"></td>
	</tr>

	<tr>
		<td valign="top"><fmt:message key="admin.customer.description" />:</td>
		<td><textarea class="textInputSmall" name="description" cols="27" rows="3" wrap="virtual">${customer.description}</textarea></td>
		<td id="customerDescError" style="color: red"></td>
	</tr>

	<tr>
		<td valign="top"><fmt:message key="general.active" />:</td>
		<td><input class="textInputSmall" type="checkbox" name="active" <c:if test="${customer == null || customer.active}">checked</c:if>></td>
		<td>&nbsp;</td>
	</tr>

	
	<tr>
		<td>
			<c:if test="${customer != null && customer.customerId != null}">
				<a href="" onClick="return deleteCustomer(${customer.customerId})"><fmt:message key="general.delete" /></a>
			</c:if>
		</td>

		<td align="right" style="text-align: right">
			<c:choose>
				<c:when test="${customer == null || customer.customerId == null}">
					<input type="submit" class="submitButtonBlue" value="<fmt:message key="general.add" />">
				</c:when>
				
				<c:otherwise>
					<input type="submit" class="submitButtonBlue" value="<fmt:message key="general.edit" />">
				</c:otherwise>
			</c:choose>
		</td>
		
		<td></td>
	</tr>
	
	<tr>
		<td colspan="2" style="color: red">
			<html:errors property="delete" />
		</td>
	</tr>
</table>
</form>

	</div>
						
	<div class="GreyFrameFooter">
		<p>
		</p>
	</div>				
</div>