<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


function changeCalMonth(month, year, userId)
{
	dojo.io.bind({
	               url: '<c:url value="/eh/cal/navCalendar.do" />',
	               handler: formChanged,
	               content: {month: month, year: year, userId: userId}
	            });  		
	
	return false;
}

// form changed	
function formChanged(type, xml, evt)
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