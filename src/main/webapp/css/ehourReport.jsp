<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css; charset=UTF-8" %>
.reportCriteriaLabel
{
	text-align: left;
	font:  bold 1.0em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0 5px 2px 11px;
	padding: 0;
	background-image: url(<c:url value="/img/fold_up.gif" />);
	background-repeat: no-repeat;
	background-position: bottom right;
	max-width: 710px;
}

.reportCriteriaLabelFolded
{
	text-align: left;
	font:  bold 1.0em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0 5px 2px 11px;
	padding: 0;
	background-image: url(<c:url value="/img/fold_down.gif" />);
	background-repeat: no-repeat;
	background-position: bottom right;
	max-width: 710px;
}

.reportCriteriaTableTable 
{
	margin: -0.5em 1em 0 0.5em;
	width: 100%;
}


.reportCriteriaTableTable th
{
	padding: 5px 0 5px 0;
	font:  bold 0.9em/1em Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
	border: 0;
	width: 20%;
	text-align: left;
}

.reportCriteriaTableTable th.firstCell
{
	width: 40%;
}

.reportCriteriaTableTable td
{
	font:  1.0em Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
	vertical-align: top;
}

.reportCriteriaTableTable td.result
{
	border-top-width: 1px;
	border-top-color: #233e55;
	border-top-style: solid;
	font-weight: bold;
}

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
	font:  0.9em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	padding: 0 5px 0 5px;
}

.reportTable tr.dataRow
{
	background-color: #eef6fe;
	padding-bottom: 1px;
	border-top-style: solid;
	border-top-color:#a1bcd7;
	border-top-width: 1px;
}


.reportTable tr.dataRow td
{
	color:	#536e87;
	border-right-style: solid;
	border-right-color:#a1bcd7;
	border-right-width: 1px;
	padding: 0 5px 0 5px;
	font:  0.9em Geneva, Arial, Helvetica, sans-serif;	
}

.reportTable tr.dataRow td.lastChild
{
	border: 0;
}

.reportTable tr.dataRow a:link
{
	color: #233e55;
	font-weight: normal;
	text-decoration: none;
}

.reportTable tr.dataRow a:visited
{
	color: #233e55;
	font-weight: normal;	
	text-decoration: none;
}

.reportTable tr.dataRow a:hover
{
	text-decoration: underline;
}

.reportTable tr.dataRow a:active
{
	text-decoration: none
}

.reportTable tr.totalRow td
{
	color:	#536e87;
	border-top-style: solid;
	border-top-color:#a1bcd7;
	border-top-width: 1px;
	padding: 0 5px 5px 5px;
	font: bold 0.9em Geneva, Arial, Helvetica, sans-serif;
}

.reportTable th
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: left;
}

.criteriaInput
{
	border-style: solid;
	border-width: 1px;
	border-color: #cfdbe6;
	margin: 0;
	font: 1em/ Geneva, Arial, Helvetica, sans-serif;
	color: #233e55;
	width:	100%;
}


div.CriteriaBlueFrameContainer
{
	margin-left: 20px;
	font:  bold 1.0em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
}

div.UserReportCriteria
{
	margin: 0 10px 0 10px;
	color: #536e87;
	font: 1em Arial, Geneva, Helvetica, sans-serif;	
}