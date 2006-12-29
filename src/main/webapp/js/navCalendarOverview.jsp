<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

var inSheetForm = false;

function changeCalMonth(month, year, userId)
{
	dojo.io.bind({
	               url: '<c:url value="/eh/cal/navCalendar.do" />',
	               handler: navCalChanged,
	               content: {month: month,
	               			 year: year,
	               			 userId: userId}
	            });  		

	if (!inSheetForm)
	{
		dojo.io.bind({
		               url: '<c:url value="/eh/timesheet/overviewSnippet.do" />',
		               handler: overviewChanged,
		               content: {month: month, year: year, userId: userId}
		            });  		
	}
		
	return false;
}

// week clicked in the calendar
function enterSheet(year, month, day, userId)
{
	inSheetForm = true;
	dojo.io.bind({
	               url: 'getTimesheetForm.do',
	               handler: overviewChanged,
	               content: {month: month,
	               			 year: year,
	               			 day: day,
	               			 userId: userId}
	            });  		

}

// navigation calendar changed	
function navCalChanged(type, xml, evt)
{
	if (type == 'error')
 	{
 		alert("<fmt:message key="errors.ajax.general" />");
 		return;
 	}
 	else
 	{
		dojo.byId('navCalendarSpan').innerHTML = xml;
	}
}


// overview changed	
function overviewChanged(type, xml, evt)
{
	if (type == 'error')
 	{
 		alert("<fmt:message key="errors.ajax.general" />");
 		return;
 	}
 	else
 	{
		dojo.byId('overviewSpan').innerHTML = xml;
	}
}