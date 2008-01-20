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
	
	var imgRow = document.getElementById("foldcss" + customerId);
	imgRow.className = (toUp) ? "timesheetFoldedImg" : "timesheetFoldImg";
	imgRow.blur();
}
