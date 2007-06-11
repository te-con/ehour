var imgUpOn, imgUpOff, imgDownOn, imgDownOff;

function init()
{
	imgUpOn = new Image();
	imgUpOn.src = '${iconUpOn}';
	imgUpOff = new Image();
	imgUpOff.src = '${iconUpOff}';
	imgDownOn = new Image();
	imgDownOn.src = '${iconDownOn}';
	imgDownOff = new Image();
	imgDownOff.src = '${iconDownOff}';
}

function onMouseOver(elm)
{
	var id = elm.id;
	id = id.substring(id.lastIndexOf("_") + 1);
	var summaryRow = getSummaryRow(id);
	var value = summaryRow.style.display;

	elm.src = (value == 'none') ? imgDownOn.src : imgUpOn.src;
}

function onMouseOut(elm)
{
	var id = elm.id;
	id = id.substring(id.lastIndexOf("_") + 1);
	var summaryRow = getSummaryRow(id);
	var value = summaryRow.style.display;
	
	elm.src = (value == 'none') ? imgDownOff.src : imgUpOff.src;
}

function foldSummary(elm)
{
	var id = elm.parentNode.id;
	var summaryRow = getSummaryRow(id);
	
	if (summaryRow.style.display == 'none')
	{
		summaryRow.style.display = '';
	}
	else
	{
		summaryRow.style.display = 'none';
	}
	
	onMouseOut(document.getElementById("foldImg_" + id));
}

function getSummaryRow(id)
{
	return document.getElementById("summaryRow_" + id);
}
	