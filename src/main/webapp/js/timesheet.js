// validate form
    function validateForm()
    {
        var elements = calForm.elements;
        var i;
        var	key;
        var	value;
        var	floatValue;
        var	inError = false;

        for (i = 0;
             i < elements.length;
             i++)
         {
            if (elements[i].name.substr(0, 3) == "qh_" &&
            		elements[i].value != "")
            {
            	key = elements[i].name;
                value = elements[i].value;

                value = value.replace(",", "\.");

                floatValue = parseFloat(value);

                if (isNaN(floatValue))
                {
                	alert(value + " is geen getal");
                	value = "";
                	inError = true;
                }
              	else if (floatValue > 24)
              	{
              		alert("In dit zonnestelsel kan max. 24u op een dag geboekt worden");
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
              	}

                calForm.elements[key].value = value;

            }
         }
	}

// book all hours to a project
    function bookToProject(assignmentID)
    {
        var bookedHours = new Array(7);
        var elements = calForm.elements;
        var	qh;
        var	value;
        var	i;
        var oldVal;

        for (i = 0; i < 7;i++)
        {
            bookedHours[i] = 0;
        }

        // get values on other projects
        for (i = 0;
             i < elements.length;
             i++)
        {
            if (elements[i].name.substr(0, 3) == "qh_")
            {
            	value = elements[i].value * 1;
            	qh = elements[i].name.split("_");

            	if (qh[1] != assignmentID)
            	{
            		bookedHours[qh[2]] += value;
            	}
            }
        }

        for (i = 1;
        	 i < bookedHours.length - 1;
        	 i++)
        {
        	value = 8 - (1 * bookedHours[i]);

            if (value < 0)
            {
                value = 0;
            }

            if (calForm.elements[("qh_" + assignmentID + "_" + i)] != null)
            {
                calForm.elements[("qh_" + assignmentID + "_" + i)].value = value;
            }
            else
            {
                bookedHours[i] = 0;
            }
        }

        updateTotal();
	}



// count all booked hours and update the total
    function updateTotal()
    {
    	validateForm();

        var elements = calForm.elements;
        var i;
        var totalHours = 0;

        for (i = 0;
             i < elements.length;
             i++)
         {
            if (elements[i].name.substr(0, 3) == "qh_")
            {
                totalHours += elements[i].value * 1;
            }
         }

        document.getElementById("totalHours").innerHTML = totalHours;
    }

    function resetTotal()
    {
        calForm.reset();
        updateTotal();
    }
