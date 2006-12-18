<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


function changeCalMonth(month, year, userId)
{
	dojo.io.bind({
	               url: '<c:url value="/eh/cal/navCalendar.do" />',
	               handler: navCalChanged,
	               content: {month: month, year: year, userId: userId}
	            });  		

	dojo.io.bind({
	               url: '<c:url value="/eh/timesheet/projectsOverview.do" />',
	               handler: projectsOverviewChanged,
	               content: {month: month, year: year, userId: userId}
	            });  		
	
	return false;
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


// projects overview changed	
function projectsOverviewChanged(type, xml, evt)
{
	if (type == 'error')
 	{
 		alert("<fmt:message key="errors.ajax.general" />");
 		return;
 	}
 	else
 	{
		dojo.byId('projectsOverviewSpan').innerHTML = xml;
	}
}