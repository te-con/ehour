<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css;" %>

div.MonthAggregate
{
	text-align: left;
	width: 100%;
	min-width: 500px;
	max-width: 710px;
	margin: 0;
	background-color: white;
}

div.MonthAggregate h1
{
	text-align: left;
	font:  bold 1.0em/1.5em Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0 0 -5px 11px;
	padding: 0;
}

div.MonthAggregateFrame table
{
	margin: 0 1em 0 1em;
	border-collapse: collapse;
	width: 100%;
}

div.MonthAggregateFrame table th
{
	font:  bold 0.9em/1.5 Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
	border: 0;
	width: 20%;
	text-align: left;
	border-bottom-width: 1px;
	border-bottom-color: #233e55;
	border-bottom-style: solid;
}

div.MonthAggregateFrame table th.firstCell
{
	width: 40%;
}

div.MonthAggregateFrame table td
{
	font:  0.9em/1.5 Geneva, Arial, Helvetica, sans-serif;	
	color: #233e55;
}

div.MonthAggregateFrame table td.result
{
	border-top-width: 1px;
	border-top-color: #233e55;
	border-top-style: solid;
	font-weight: bold;
}

div.MonthAggregateFrame
{
	background: url(<c:url value="/img/grey/corner_left_top.gif" />) top left no-repeat;
	max-width: 710px;
} 

div.MonthAggregateFrame h3
{
  background: 	url(<c:url value="/img/grey/corner_right_top.gif" />) top right no-repeat;
  line-height:	11px;
  padding:		0;
  margin:		0 0 0px 75px;
}

div.MonthAggregateBody
{
  background: url(<c:url value="/img/grey/pixel_grey.gif" />);
  margin:0px;
  margin-top: 0em;
  padding:0;
}

div.MonthAggregateFrameFooter
{
  background: url(<c:url value="/img/grey/corner_left_bottom.gif" />) bottom left no-repeat;
}

div.MonthAggregateFrameFooter p
{
  background: url(<c:url value="/img/grey/corner_right_bottom.gif" />) bottom right no-repeat;
  padding:15px;
  display:block;
  margin:-2em 0 0 0;
}

div.MonthOverviewFrame
{
  background: url(<c:url value="/img/grey/corner_left_top.gif" />) top left no-repeat;
	max-width: 710px;
 } 

div.MonthOverviewFrame h3
{
  background: 	url(<c:url value="/img/grey/corner_right_top.gif" />) top right no-repeat;
  line-height:	11px;
  padding:		0;
  margin:		0 0 0px 75px;
}

div.MonthOverviewFrameBody
{
  background: url(<c:url value="/img/grey/pixel_grey.gif" />);
  margin:0px;
  margin-top: 0em;
  padding:0;
}

div.MonthOverviewFrameFooter
{
  background: url(<c:url value="/img/grey/corner_left_bottom.gif" />) bottom left no-repeat;
}

div.MonthOverviewFrameFooter p
{
  background: url(<c:url value="/img/grey/corner_right_bottom.gif" />) bottom right no-repeat;
  padding:15px;
  display:block;
  margin:-2em 0 0 0;
}


div.MOBlueFrame
{
  background: url(<c:url value="/img/ovw/gradient_blue.gif" />) top left repeat-x;
  margin-left: 20px;
  margin-right: 20px;
}

div.MOBLueLeftTop
{
  background: url(<c:url value="/img/ovw/left_top.gif" />) top left no-repeat;
}

div.MOBLueRightTop
{
  background: url(<c:url value="/img/ovw/right_top.gif" />) top right no-repeat;
}

div.MOBLueRightBottom
{
  background: url(<c:url value="/img/ovw/right_bottom.gif" />) bottom right no-repeat;
}

div.MOBLueLeftBottom
{
  background: url(<c:url value="/img/ovw/left_bottom.gif" />) bottom left no-repeat;
}

div.MOBlueFrame table.month
{
	margin-top: -15px;
	padding: 0;
}

div.MOBlueFrame table.month td
{
	color:	#536e87;
	height:	18px;
	width: 80px;
	border-right-style: solid;
	border-right-color:#a1bcd7;
	border-right-width: 1px;
}
	
div.MOBlueFrame table.month tr.weekColumnRow
{
}

div.MOBlueFrame table.month tr.weekColumnRow td
{
	font:  1.5em/1.5 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
}

div.MOBlueFrame table.month tr.weekColumnRow td.lastChild
{
	border: 0;
}

div.MOBlueFrame table.month tr.weekColumnRow td.weekNumber
{
	width: 50px;
	border-width: 0;
}

div.MOBlueFrame table.month tr.dateRow
{
	border-bottom-style: solid;
	border-bottom-color: red;
	border-bottom-width: 1px;
}

div.MOBlueFrame table.month tr.dateRow td
{
	font:  0.8em/1 Geneva, Arial, Helvetica, sans-serif;	
	text-align:	right;
	margin: 0;
	padding: 0 0 2px 0;
	vertical-align: bottom;
}


div.MOBlueFrame table.month tr.dateRow td.lastChild
{
	border-right-width: 0;
}


div.MOBlueFrame table.month tr.dateRow td.noMonth
{
	border: 0;
}


div.MOBlueFrame table.month tr.dateRow td.weekNumber
{
	border-width: 0;
	width: 50px;
}


div.MOBlueFrame table.month tr.hourRow
{
}

div.MOBlueFrame table.month tr.hourRow td
{
	height: 45px;
	font:  0.8em/1 Geneva, Arial, Helvetica, sans-serif;	
	text-align:	left;
	vertical-align: top;
	background-color: #fefeff;
	border-bottom-style: solid;
	border-bottom-color:#a1bcd7;
	border-bottom-width: 1px;
}
div.MOBlueFrame table.month tr.hourRow td.weekNumber
{
	background-color: transparent;
	vertical-align: bottom;
	font:  0.9em/1 Geneva, Arial, Helvetica, sans-serif;	
	padding: 0 0 2px 5px;
	width: 55px;
	border-right-width: 0;
}

div.MOBlueFrame table.month tr.hourRow td.sunday
{
	background-color: transparent;
	background: url(<c:url value="/img/ovw/sunday_back.gif" />) left top no-repeat;
}

div.MOBlueFrame table.month tr.hourRow td.saturday
{
	background-color: transparent;
	background: url(<c:url value="/img/ovw/saturday_back.gif" />) right top no-repeat;
	border-right-width: 0;
}


div.MOBlueFrame table.hourContentTable td
{
	border: 0;
}

div.MOBlueFrame table tr.hourRow td.noMonthBefore
{
	background-color: transparent;
	border-bottom-style: solid;
	border-bottom-color:#a1bcd7;
	border-bottom-width: 1px;
}

div.MOBlueFrame table tr.hourRow td.noMonthAfter
{
	background-color: transparent;
	border: 0;
}


.bookedHours
{
	width:		98%;
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

div.TimesheetFrame
{
	background: url(<c:url value="/img/grey/corner_left_top.gif" />) top left no-repeat;
	max-width: 710px;
} 

div.TimesheetFrame h3
{
  background: 	url(<c:url value="/img/grey/corner_right_top.gif" />) top right no-repeat;
  line-height:	11px;
  padding:		0;
  margin:		0 0 0px 75px;
}

div.TimesheetBody
{
  background: url(<c:url value="/img/grey/pixel_grey.gif" />);
  margin:0px;
  margin-top: 0em;
  padding:0;
}

div.TimesheetFrameFooter
{
	background: url(<c:url value="/img/grey/corner_left_bottom.gif" />) bottom left no-repeat;
}

div.TimesheetFrameFooter p
{
  background: url(<c:url value="/img/grey/corner_right_bottom.gif" />) bottom right no-repeat;
  padding:15px;
  display:block;
  margin:-2em 0 0 0;
}

div.MOBlueFrame table.timesheet
{
	margin-top: -15px;
	padding: 0;
	width: 100%;
	margin-right: 15px;
}

div.MOBlueFrame table.timesheet td
{
	color:	#536e87;
	height:	18px;
	width: 50px;
	border-right-style: solid;
	border-right-color:#a1bcd7;
	border-right-width: 1px;
}
	
div.MOBlueFrame table.timesheet tr.weekColumnRow
{
}

div.MOBlueFrame table.timesheet tr.weekColumnRow td
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
}

div.MOBlueFrame table.timesheet tr.weekColumnRow td.lastChild
{
	border: 0;
}

div.MOBlueFrame table.timesheet tr.weekColumnRow td.project
{
	width: 290px;
	border-width: 0;
}

div.MOBlueFrame table.timesheet tr.projectRow td.project
{
	width: 290px;
	border-width: 0;
	text-align: left;
}

div.MOBlueFrame table.timesheet tr.projectRow td
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
}


div.MOBlueFrame table.timesheet tr.projectRow td a:visited
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	text-decoration: none;
}

div.MOBlueFrame table.timesheet tr.projectRow td a:hover
{
	font:  1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	text-decoration: underline;
}


div.MOBlueFrame table.timesheet tr.projectRow td.sunday
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
	background-color:#edf5fe;
}

div.MOBlueFrame table.timesheet tr.projectRow td.saturday
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
	background-color:#edf5fe;
	border: 0;
}

div.MOBlueFrame table.timesheet tr.projectRow td.weekday
{
	font:  bold 1.0em/1.1 Geneva, Arial, Helvetica, sans-serif;	
	color: #536e87;
	margin: 0;
	padding: 0;
	text-align: center;
	background-color:#fefeff;
}
