var doNotSubmitTimesheet = true;
var inSheetForm = false;

// 'abstract' methods from navCalendarOverview.js
function navCalendarUpdated(month, year, userId)
{
	// if we're not showing the timesheet form, apparently we show the overview and
	// it needs an update
	if (!inSheetForm)
	{
		dojo.io.bind({
		               url: contextRoot + "eh/timesheet/overviewSnippet.do",
		               handler: overviewChanged,
		               content: {month: month,
		               			 year: year,
		               			 userId: userId}
		            });  		
	}
}

// 'abstract' methods from navCalendaroverview.js
// week clicked in the calendar
function enterSheet(year, month, day, userId)
{
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

// week changed	
function weekChanged(type, xml, evt)
{
	overviewChanged(type, xml, evt);

	new dojo.io.FormBind({	formNode: dojo.byId('timesheetFormId'),
  							handler: timesheetSubmitted
							});
			
	// update contextual help?							
	if (!inSheetForm)
	{
		inSheetForm = true;

		// helpChanged method is in base.js
		dojo.io.bind({
		               url: contextRoot + "eh/timesheet/timesheetHelp.do",
		               handler: helpChanged
		            });
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

// book all remaining hours to a project
function bookToProject(assignmentId)
{
	var bookedHours = new Array();
    var elements = document.getElementById('timesheetFormId').elements;
    var	rowId;
    var	value;
    var oldVal;
    var assIdKey = "ehts_" + assignmentId;

    // get values on other project assignments
    for (i = 0;
         i < elements.length;
         i++)
    {
        if (elements[i].name.indexOf("ehts_") == 0 ||
	        elements[i].name.indexOf("inactive_") == 0)
        {
        	// rowId: ehts_<assignmentId>_<date>_<dayInWeek>
        	value = elements[i].value * 1;
        	rowId = elements[i].name.split("_");

        	if (rowId[1] != assignmentId)
        	{
        		if (isNaN(bookedHours[rowId[2]]))
        		{
        			bookedHours[rowId[2]] = 0;
        		}
        		
        		bookedHours[rowId[2]] += value;
        	}
        }
    }

	// go through each date of the clicked assignment id and check
	// if other projects got values for them
    for (i = 0;
         i < elements.length;
         i++)
    {
		if (elements[i].name.indexOf(assIdKey) == 0)
		{
	       	rowId = elements[i].name.split("_");

			// exclude sat & sun@todo config?
			// also exclude days which have more hours booked than max. defined
			if (rowId[3] != 1
				&& rowId[3] != 7
				&& elements[i].value < maxHoursPerDay)
			{
				if (!isNaN(bookedHours[rowId[2]]))
				{
					value = maxHoursPerDay - (1 * bookedHours[rowId[2]]);
				}
				else
				{
					value = maxHoursPerDay;
				}
				
				if (value < 0)
				{
					value = 0;
				}
	
	            elements[i].value = value;
			}	        
        }
    }

    updateTotal();
    
    return false;
}



// count all booked hours and update the total
function updateTotal()
{
	var form = document.getElementById('timesheetFormId');
    var elements = form.elements;
    var i;
    var totalHours = 0;

    for (i = 0;
         i < elements.length;
         i++)
     {
        if (elements[i].name.substr(0, 5) == "ehts_" ||
	        elements[i].name.substr(0, 9) == "inactive_")
        {
            totalHours += elements[i].value * 1;
        }
     }

    document.getElementById("totalHours").innerHTML = totalHours.toFixed(2);
}

// reset the form and recalc total
function resetTotal()
{
	// from navCalenderOverview.js
	doNotSubmitTimesheet = true;
	document.getElementById('timesheetFormId').reset();
    updateTotal();
}

// onClick on the submit, allow the form to submit
function makeFormSubmittable()
{
	doNotSubmitTimesheet = false;
	
	return true;
}

// extend FormBind to add the validation call
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										return validateForm();
									}});

// prevent the FormBind to submit when the cancel flag is set
function validateForm()
{
	if (!doNotSubmitTimesheet)
	{
		if (document.getElementById("comment").value.length > 1023)
		{
			alert(errorCommentTooLong);
			
			return false;
		}
		
		showLoadingData();
	}
	
	return !doNotSubmitTimesheet;
}


// timesheet was submitted
function timesheetSubmitted(type, xml, evt)
{
	doNotSubmitTimesheet = true;
	
	weekChanged(type, xml, evt);

	currentCalMonth = document.getElementById("sheetMonth").value * 1;
	currentCalMonth--;
	
	currentCalYear = document.getElementById("sheetYear").value * 1;

	// darn calendar being zero based..	
	if (currentCalMonth < 0)
	{
		currentCalMonth = 11;
		currentCalYear--;
	}
	
	var userId = document.getElementById("userId").value;
	
	dojo.io.bind({
	               url: contextRoot + "eh/cal/navCalendar.do",
	               handler: navCalChanged,
	               content: {month: currentCalMonth,
	               			 year: currentCalYear,
	               			 userId: userId}});
}

// validate field
function validateField(field)
{
	var value = field.value;
	var inError = false;

	value = value.replace(",", "\.");

    floatValue = parseFloat(value);

    if (isNaN(floatValue))
    {
    	alert(value + " " + errorNotValidNumber);
        field.value = "";
        inError = true;
	}
    else if (floatValue > 24)
    {
    	alert(error24HoursMax);
		floatValue = 24;
	}
	else if (floatValue < 0)
    {
    	value = 0;
       	floatValue = 0;
	}

    if (!inError)
    {
		if (Math.round(floatValue) != floatValue)
       	{
       		value = floatValue.toFixed(2);
       	}
       	else
       	{
       		value = floatValue.toFixed(0);
       	}

		field.value = value;
		
		updateTotal();
	}	
}

