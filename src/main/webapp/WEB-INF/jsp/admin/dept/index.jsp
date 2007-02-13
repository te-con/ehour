<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="../../../js/dojo.js" type="text/javascript"></script>

<script src="<c:url value="/js/validation.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/base.js" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/departmentAdmin.jsp" />" type="text/javascript"></script>
<script src="<c:url value="/js/admin/baseAdmin.js" />" type="text/javascript"></script>

<div class="ContentFrame" style="max-width: 1200px">
	<table class="contentTable" cellspacing="2">
		<tr>
			<td valign="top" width="270">
				<h1><fmt:message key="admin.dept.title" /></h1>
				<div class="GreyFrame" style="width: 260px;">
					<h3>&nbsp;</h3>

					<div class="GreyFrameBody">
						<div class="BlueFrame" style="width: 220px; padding-top: 0; margin-top: 10px">
							<div class="BlueLeftTop">
								<div class="BlueRightTop">
									&nbsp;
								</div>
							</div>						
					
							<div class="adminListScroll">
								<span id="listDepartmentsSpan">
									<tiles:insert page="listUserDepartments.jsp" />
								</span>
							</div>
							
							<div class="BlueLeftBottom">
								<div class="BlueRightBottom">
									&nbsp;
								</div>
							</div>			
						</div>
						
						<p style="text-align: right;margin: 5px 20px 0 0">
							<a href="" onClick="return showAddForm()"><fmt:message key="admin.dept.addDepartment" /></a>
						</p>													
					</div>
					
					<br>
					
					<div class="GreyFrameFooter">
						<p>
						</p>
					</div>				
				</div>	
			</td>
			
			<td valign="top">						
				<span id="userDepartmentFormSpan">
					<tiles:insert page="userDepartmentForm.jsp" />
				</span>						
			</td>
		</tr>
		
		<tr>
			<td colspan="2" align="right" style="text-align: right; margin-right: 20px; color: #233e55" id="statusMessage">
				&nbsp;
			</td>
		</tr>
	</table>
</div>
