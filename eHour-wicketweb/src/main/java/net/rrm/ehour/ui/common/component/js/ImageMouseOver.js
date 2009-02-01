var imgOn = null
	, imgOff = null;

function initImagePreload()
{
	imgOn = null;
	imgOff = null;
}

function onMouseOver(elm, img)
{
	if (imgOn == null)
	{
		imgOn = new Image();
		imgOn.src = img;
	}
	
	elm.src = imgOn.src;
}

function onMouseOut(elm, img)
{
	if (imgOff == null)
	{
		imgOff = new Image();
		imgOff.src = img;
	}

	elm.src = imgOff.src;
}
