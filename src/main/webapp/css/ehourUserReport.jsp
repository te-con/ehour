<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css; charset=UTF-8" %>

.userCriteriaTable
{
	width: 100%;
	margin: 0;
	padding: 0;
}

.userCriteriaTable td
{
	color:	#536e87;
}

.userCriteriaTable input
{
	border-style: solid;
	border-width: 1px;
	border-color: #b3c0cb;
	margin: 5px 0 5px 0;
	font: 0.9em/1.5 Geneva, Arial, Helvetica, sans-serif;
	color: #233e55;
	width: 50px;
}

.reportTable
{
	padding: 0;
}

.reportTable tr.reportTotal td
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	padding: 0 5px 0 5px;
}

.reportTable tr.customerRow
{
	background-color: #eef6fe;
	padding-bottom: 1px;
	border-top-style: solid;
	border-top-color:#a1bcd7;
	border-top-width: 1px;
}


.reportTable tr.customerRow td
{
	color:	#536e87;
	border-right-style: solid;
	border-right-color:#a1bcd7;
	border-right-width: 1px;
	padding: 0 5px 0 5px;
}

.reportTable tr.customerRow td.lastChild
{
	border: 0;
}

.reportTable th
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
}

div.reportCriteria
{
	margin: 0 10px 0 10px;
}