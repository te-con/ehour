var imgDownOff = new Image();
imgDownOff.src = '${iconDownOff}';
var imgUpOff= new Image();
imgUpOff.src = '${iconUpOff}';

function toggleProjectRow(customerId)
{
	var i = 1;
	
	var toUp = false;
	
	var ok = true;
	
	while (ok)
	{
		row = document.getElementById("pw" + customerId + "" + i++);

		if (row != null)
		{
			if (row.style.display == 'none')
			{
				row.style.display = '';
				toUp = false;
			}
			else
			{
				row.style.display = 'none';
				toUp = true; 
			}
		}
		else
		{
			ok = false;
		}
	}
	
	var imgRow = document.getElementById("img" + customerId);
	imgRow.childNodes[0].src = (toUp) ? imgUpOff.src : imgDownOff.src;
}

// count all booked hours and update the total
function updatffeTotal()
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