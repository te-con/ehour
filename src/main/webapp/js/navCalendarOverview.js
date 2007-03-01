var currentCalMonth;
var currentCalYear;

// calendar month changed
function changeCalMonth(month, year, userId)
{
	showLoadingData();
	
	currentCalMonth = month;
	currentCalYear = year;
	
	dojo.io.bind({
	               url: contextRoot + "eh/cal/navCalendar.do",
	               handler: navCalChanged,
	               content: {month: month,
	               			 year: year,
	               			 userId: userId}
	            });  		

	// navCalendarUpdated should be implemented by page specific js
	navCalendarUpdated(month, year, userId);
		
	return false;
}

// function enterSheet(year, month, day, userId) must be implemented by page specific js

// navigation calendar changed	
function navCalChanged(type, xml, evt)
{
	hideLoadingData();
	
	if (type == 'error')
 	{
 		alert(ajaxError);
 	}
 	else
 	{
 		ajaxEventReceived(xml, true, {navCalendar: "NavCalTarget"});
	}
}




