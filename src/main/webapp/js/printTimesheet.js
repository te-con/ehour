// 'abstract' methods from navCalendarOverview.js
function navCalendarUpdated(month, year, userId)
{
	// if we're not showing the timesheet form, apparently we show the overview and
	// it needs an update
		dojo.io.bind({
		               url: contextRoot + "eh/timesheet/printTimesheet.do",
		               handler: overviewChanged,
		               content: {month: month,
		               			 year: year,
		               			 ajaxCall: "Y",
		               			 fromForm: "N", 
		               			 userId: userId}
		            });  		
}

// overview changed	
function overviewChanged(type, xml, evt)
{
	hideLoadingData();
	
	if (type == 'error')
 	{
 		alert(ajaxError);
 	}
 	else
 	{
 		ajaxEventReceived(xml, true, {content: 'contentSpan'}); 	
	}
}