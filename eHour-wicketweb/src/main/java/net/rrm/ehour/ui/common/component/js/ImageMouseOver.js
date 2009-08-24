var imgs = 
{
	Set : function(key, value) {this[key] = value;},
	Get : function(key) {return this[key];}
}

function initImagePreload()
{
}

function onMouseOver(elm, img, id)
{
	if (imgs[id + 'over'] == null)
	{
		imgs[id + 'over'] = new Image();
		imgs[id + 'over'].src = img;
	}
	
	elm.src = imgs[id + 'over'].src;
}

function onMouseOut(elm, img, id)
{
	if (imgs[id + 'out'] == null)
	{
		imgs[id + 'out'] = new Image();
		imgs[id + 'out'].src = img;
	}

	elm.src = imgs[id + 'out'].src;
}

