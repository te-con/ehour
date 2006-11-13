<%@ page contentType="text/html; charset=ASCII" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

	<table CLASS="admin_nav_table" CELLSPACING=0>
        <tr>
	    <td colspan=2>Entiteiten admin</td>
	</tr>

	<tr>
            <td rowspan=4>&nbsp;</td><td><a href="<c:url value="/admin/consultants/index.do" />">consultants</a></td>
        </tr>

        <tr>
            <td><a href="<c:url value="/admin/customers/index.do" />">klanten</a></td>
        </tr>

        <tr>
            <td><a href="<c:url value="/admin/projects/index.do" />">projecten</a></td>
        </tr>

       <tr>
            <td><a href="<c:url value="/admin/assignments/index.do" />">assignments</a></td>
        </tr>
<!--
	<tr>
		<td colspan=2><br>Timesheet onderhoud</td>
	</tr>


	<tr>
            <td>&nbsp;</td><td><a href="<c:url value="/admin/timesheet/index.do" />">wijzig timesheet</a></td>
        </tr>
-->

	<tr>
		<td colspan=2><br>Rapportage</td>
	</tr>


	<tr>
            <td>&nbsp;</td><td><a href="<c:url value="/admin/report/index.do" />">rapportage</a></td>
        </tr>
<!-- 
	<tr>
		<td colspan=2><br>Check timesheets</td>
	</tr>

	<tr>
        <td>&nbsp;</td><td><a href="<c:url value="/admin/timesheet/check.do" />">check timesheets</a></td>
	</tr>
 -->

    </table>
