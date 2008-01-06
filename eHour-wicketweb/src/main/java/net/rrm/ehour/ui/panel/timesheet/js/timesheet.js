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
