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