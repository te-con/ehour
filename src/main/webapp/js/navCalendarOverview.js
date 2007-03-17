var currentCalMonth;
var currentCalYear;

// check if calendar matches timesheet
function syncTimesheet(month, year, userId, zeroBaseDate)
{
	if (!zeroBaseDate)
	{
		month--;
		
		if (month < 0)
		{
			month = 11;
			year--;
		}
	}
	
	if (month != currentCalMonth || year != currentCalYear)
	{
		fireCalendarRequest(month, year, userId);
	}
}

// calendar month changed
function changeCalMonth(month, year, userId)
{
	showLoadingData();
	
	fireCalendarRequest(month, year, userId);

	// navCalendarUpdated should be implemented by page specific js
	navCalendarUpdated(month, year, userId);
		
	return false;
}

// request new calendar
function fireCalendarRequest(month, year, userId)
{
	currentCalMonth = month;
	currentCalYear = year;
	
	dojo.io.bind({
	               url: contextRoot + "eh/cal/navCalendar.do",
	               handler: navCalChanged,
	               content: {month: month,
	               			 year: year,
	               			 userId: userId}
	            }); 
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
