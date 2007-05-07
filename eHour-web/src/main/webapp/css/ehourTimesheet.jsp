<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css; charset=UTF-8" %>


a:link
{
	color: #536e87;
	font-weight: normal;
	text-decoration: none;
}

a:visited
{
	color: #536e87;
	font-weight: normal;	
	text-decoration: none;
}

a:hover
{
	color: #233e55;
	text-decoration: underline;
}

a:active
{
	text-decoration: none
}

.TimesheetHeaderDiv
{
	margin-left: 11px;
}

.TimesheetHeaderDiv a:link
{
	font:  bold 0.9em/1em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	font-weight: normal;
	text-decoration: none;
}

.TimesheetHeaderDiv a:visited
{
	font:  bold 0.9em/1em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	font-weight: normal;	
	text-decoration: none;
}

.TimesheetHeaderDiv a:hover
{
	font:  bold 0.9em/1em Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
	text-decoration: none;
}

.TimesheetHeaderDiv a:active
{
	font:  bold 0.9em/1em Geneva, Arial, Helvetica, sans-serif;	
	text-decoration: none
}

.timesheetHeader
{
	font-family: Arial,Geneva,Helvetica,sans-serif;
	font-weight: bold;
	font-size: 1em;
	color: #536e87;
	margin: 0;
	padding: 0;
}

.MonthAggregateTable 
{
	margin: -1em 1em 0 1em;
	width:expression(document.body.clientWidth > 710? "710px": "auto" );
}

.MonthAggregateTable th
{
	padding: 5px 0 5px 0;
	font:  bold 0.9em/1em Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
	border: 0;
	text-align: left;
}

.MonthAggregateTable th.foldCell
{
	width: 15px;
}


.MonthAggregateTable td.foldCell
{
	width: 15px;
}

.MonthAggregateTable th.projectCell
{
	width: 23%;
}

.MonthAggregateTable th.total
{
	border-style: solid;
	border-top-width: 1px;
	border-color: #ededed
}

.MonthAggregateTable td
{
	font:  1em Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
}

.MonthAggregateTable td.result
{
	border-top-width: 1px;
	border-top-color: #233e55;
	border-top-style: solid;
	font-weight: bold;
}


.MonthOverviewIEWorkAround
{
	margin: -15px 0 0 0;
	padding: 0;
}

.MonthOverviewMonthTable
{
}

.MonthOverviewMonthTable td
{
	color:	#536e87;
	height:	18px;
	width: 90px;
	border-right-style: solid;
	border-right-color:#a1bcd7;
	border-right-width: 1px;
}
	
.MonthOverviewMonthTable tr.weekColumnRow
{
}

.MonthOverviewMonthTable tr.weekColumnRow td
{
	font:  1.3em/1.3 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	padding: 0 0 0 3px;
	margin: 0;
}

.MonthOverviewMonthTable tr.weekColumnRow td.lastChild
{
	border: 0;
}

.MonthOverviewMonthTable tr.weekColumnRow td.weekNumber
{
	width: 50px;
	border: 0;
}

.MonthOverviewMonthTable tr.dateRow
{
	border: 0;
}

.MonthOverviewMonthTable tr.dateRow td
{
	font:  0.8em/0.8 Geneva, Arial, Helvetica, sans-serif;	
	text-align:	right;
	margin: 0;
	padding: 0 0 2px 0;
	vertical-align: bottom;
}


.MonthOverviewMonthTable tr.dateRow td.lastChild
{
	border-right-width: 0;
}


.MonthOverviewMonthTable tr.dateRow td.noMonth
{
	border: 0;
}


.MonthOverviewMonthTable tr.dateRow td.weekNumber
{
	border-width: 0;
	width: 50px;
}


.MonthOverviewMonthTable tr.hourRow
{
}

.MonthOverviewMonthTable tr.hourRow td
{
	height: 45px;
	font:  0.8em/1 Geneva, Arial, Helvetica, sans-serif;	
	text-align:	left;
	vertical-align: top;
	background-color: #fefeff;
	border-bottom-style: solid;
	border-bottom-color:#a1bcd7;
	border-bottom-width: 1px;
	width: inherit;
}

.MonthOverviewMonthTable tr.hourRow td.weekNumber
{
	background-color: transparent;
	vertical-align: bottom;
	font:  0.9em/1 Geneva, Arial, Helvetica, sans-serif;	
	padding: 0 0 2px 5px;
	width: 55px;
	border-right-width: 0;
}

.MonthOverviewMonthTable tr.hourRow td.sunday
{
	background-color: transparent;
	background: url(<c:url value="/img/ovw/sunday_back.gif" />) left top no-repeat;
}

.MonthOverviewMonthTable tr.hourRow td.saturday
{
	background-color: transparent;
	background: url(<c:url value="/img/ovw/saturday_back.gif" />) right top no-repeat;
	border-right-width: 0;
}


.MonthOverviewTable hourContentTable td
{
	border: 0;
}

.MonthOverviewMonthTable tr.hourRow td.noMonthBefore
{
	background-color: transparent;
	border-bottom-style: solid;
	border-bottom-color:#a1bcd7;
	border-bottom-width: 1px;
}

.MonthOverviewMonthTable tr.hourRow td.noMonthAfter
{
	background-color: transparent;
	border: 0;
}


.bookedHours
{
	overflow:	hidden;
	margin: 0 0 2px 2px;
	padding: 0;
	font-size: 1.1em;	
}

.bookedHourValue
{
	font-weight: bold;
	float: 	right;
	margin: -1em 2px 0 0;
}


.timesheetTable
{
	padding: 0;
	width: 100%;
}

.timesheetTable td
{
	color:	#536e87;
	height:	18px;
	width: 50px;
	border-right-style: solid;
	border-right-color:#a1bcd7;
	border-right-width: 1px;
}

.timesheetTable tr.weekColumnRow
{
}

.timesheetTable tr.weekColumnRow td
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
}

.timesheetTable tr.weekColumnRow td.lastChild
{
	border: 0;
}

.timesheetTable tr.weekColumnRow td.project
{
	width: 250px;
	border-width: 0;
	color: #536e87;
	text-align: left;
	vertical-align: bottom;
	padding-bottom: 1px;
}

.timesheetTable tr.projectRow td.project
{
	width: 250px;
	text-align: left;
	border-right: 0px;
	font:  1em/1.1 Geneva, Arial, Helvetica, sans-serif;	
}

.timesheetTable tr.projectRow td.projectCode
{
	text-align: left;
	font-weight: normal;
}


.timesheetTable tr.projectRow td.customer
{
	font-weight: normal;
	weight: 10em;
	text-align: left;
	padding-left: 3px;
	border-right: 0;
}

.timesheetTable tr.weekColumnRow td.spacer
{
	border-right: 0;
}

.timesheetTable tr.weekColumnRow td.customer
{
	border-right: 0;
	text-align: left;
	padding-left: 3px;
	padding-bottom: 1px;
}

.timesheetTable tr.weekColumnRow td.projectCode
{
	border: 0;
	padding-bottom: 1px;
	text-align: left;
	vertical-align: bottom;
}

.timesheetTable tr.projectRow td.spacer
{
	border-right: 0;
	weight: 10em;
}

.timesheetTable tr.projectRow td.project a:link
{
	text-decoration: none;
	color: #536e87;
}

.timesheetTable tr.projectRow td.project a:visited
{
	text-decoration: none;
	color: #536e87;
}

.timesheetTable tr.projectRow td.project a:hover
{
	text-decoration: underline;
	color: #536e87;
}

.timesheetTable tr.projectRow td.project a:active
{
	text-decoration: none;
	color: #536e87;
}

.timesheetTable tr.projectRow td
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0 0 1px 0;
	text-align: center;
	border-bottom-style:  solid;
	border-bottom-color: #b9d3ef;
	border-bottom-width: 1px;
}


.timesheetTable tr.projectRow
{
	background-color: #eef6fe;
	padding-bottom: 1px;
}

.timesheetTable tr.projectRow td a:visited
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: white;
	text-decoration: none;
}

.timesheetTable tr.projectRow td a:hover
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	text-decoration: underline;
}


.timesheetTable tr.projectRow td.sunday
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
}

.timesheetTable tr.projectRow td.saturday
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	border-right: 0px;
	text-align: center;
}

.timesheetTable tr.projectRow td.weekday
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
}

.timesheetTable input
{
	border-style: solid;
	border-width: 1px;
	border-color: #cfdbe6;
	margin: 5px 0 5px 0;
	font: 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
	color: #233e55;
	background-color: white;
	width: 3em;
	size: 3;
}
.timesheetTable tr.totalRow td
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	border: 0;
}


.timesheetCommentsTable
{
	margin-top: -8px;
	padding: 0;
	width: 100%;
}

.timesheetCommentsTable td
{
	color:	#536e87;
	border:	0;
}

.timesheetCommentsTable textarea
{
	border-style: solid;
	border-width: 1px;
	border-color: #b3c0cb;
	margin: 0 0 5px 0;	
	font: 0.9em/1.5 Geneva, Arial, Helvetica, sans-serif;
	overflow-y: auto;
	width: 500px;
	color: #536e87;
	background-color: white;
}

.TimesheetPrintFormTable
{
	width: auto;
	padding: 0;
	margin: -10px 0 0 0;
}

.TimesheetPrintFormTable td
{
	color:	#536e87;
	border-left-style: solid;
	border-left-color:#a1bcd7;
	border-left-width: 1px;
	vertical-align: top;
}


	