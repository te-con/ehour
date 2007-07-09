function toggleProjectRow(customerId)
{
	var i = 1;
	
	var ok = true;
	
	while (ok)
	{
		row = document.getElementById("pw" + customerId + "" + i++);

		if (row != null)
		{
			if (row.style.display == 'none')
			{
				row.style.display = '';
			}
			else
			{
				row.style.display = 'none';
			}
		}
		else
		{
			ok = false;
		}
	}
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