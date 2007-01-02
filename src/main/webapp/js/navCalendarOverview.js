dojo.require("dojo.event.*");
dojo.require("dojo.lfx.*");

var inSheetForm = false;
var doNotSubmitTimesheet = true;

// calendar month changed
function changeCalMonth(month, year, userId)
{
	showLoadingData();
	
	dojo.io.bind({
	               url: contextRoot + "eh/cal/navCalendar.do",
	               handler: navCalChanged,
	               content: {month: month,
	               			 year: year,
	               			 userId: userId}
	            });  		

	// if we're not showing the timesheet form, apparently we show the overview and
	// it needs an update
	if (!inSheetForm)
	{
		dojo.io.bind({
		               url: contextRoot + "eh/timesheet/overviewSnippet.do",
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
	
	doNotSubmitTimesheet = true;
	
	dojo.io.bind({
	               url: 'getTimesheetForm.do',
	               handler: weekChanged,
	               content: {month: month,
	               			 year: year,
	               			 day: day,
	               			 userId: userId}
	            });  		

	showLoadingData();
}




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
 		ajaxEventReceived(xml, true, {navCalendar: "navCalendarSpan"});
	}
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
 		ajaxEventReceived(xml, true, {overview: 'overviewSpan'}); 	
	}
}

// week changed	
function weekChanged(type, xml, evt)
{
	overviewChanged(type, xml, evt);

	new dojo.io.FormBind({	formNode: dojo.byId('timesheetFormId'),
  							handler: timesheetSubmitted
							});
}

// timesheet was submitted
function timesheetSubmitted(type, xml, evt)
{
	doNotSubmitTimesheet = true;
	
	alert('hello');
}

//
function showLoadingData()
{
	dojo.html.setOpacity(dojo.byId('statusMessage'), 100);
	document.getElementById('statusMessage').innerHTML = loadingMsg;
}

function hideLoadingData()
{
	dojo.lfx.html.fadeOut('statusMessage', 300).play();
}


// extend FormBind to add the validation call
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										return validateForm();
									}});

// onClick on the submit, allow the form to submit
function makeFormSubmittable()
{
	doNotSubmitTimesheet = false;
	
	return true;
}

// prevent the FormBind to submit when the cancel flag is set
function validateForm()
{
	return !doNotSubmitTimesheet;
}