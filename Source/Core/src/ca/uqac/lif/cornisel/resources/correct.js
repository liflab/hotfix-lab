//get html node
var html = document.getElementsByTagName("HTML")[0];

//select node to correct
var node = html.querySelectorAll("[corniid='%ID%']")[0];

function getRealWidth(e)
{
	return e.getBoundingClientRect().width;
}

function getRealHeight(e)
{
	return e.getBoundingClientRect().height;
}

function getRealX(e)
{
	return e.getBoundingClientRect().x;
}

function getRealY(e)
{
	return e.getBoundingClientRect().y;
}

function setRealWidth(e, w)
{
	var c_style = window.getComputedStyle(e);
	var padding = parseFloat(c_style.paddingLeft) + parseFloat(c_style.paddingRight);
	var border = parseFloat(c_style.borderLeftWidth) + parseFloat(c_style.borderRightWidth);
	var new_width = w - padding - border;
	e.style.width = new_width + "px";
}

function setRealHeight(e, w)
{
	var c_style = window.getComputedStyle(e);
	var padding = parseFloat(c_style.paddingTop) + parseFloat(c_style.paddingBottom);
	var border = parseFloat(c_style.borderTopWidth) + parseFloat(c_style.borderBottomWidth);
	var new_height = w - padding - border;
	e.style.height = new_height + "px";
}

function setRealXY(e, x, y)
{
	// Get parent info
	var parent = getParent(e);
	var p_style = window.getComputedStyle(parent);
	var parent_offset_x = 0;
	var parent_offset_y = 0;
	if (parent.tagName != "BODY")
	{
		parent_offset_x = parseFloat(parent.getBoundingClientRect().x);// - parseFloat(p_style.marginLeft);
		parent_offset_y = parseFloat(parent.getBoundingClientRect().y);// - parseFloat(p_style.marginTop);
	}
	// Get absolute node info
	var e_style = window.getComputedStyle(e);
	var margin_x = parseFloat(e_style.marginLeft);
	var margin_y = parseFloat(e_style.marginTop);
	var new_value_x = (x - parent_offset_x - margin_x) + "px";
	var new_value_y = (y - parent_offset_y - margin_y) + "px";
	// Set new position
	e.style.position = "absolute";
	e.style.left = new_value_x;
	e.style.top = new_value_y;
}

function getParent(e)
{
	var p = e.parentNode;
	while (p.tagName != "BODY")
	{
		if (p.style.position == "absolute")
		{
			return p;
		}
		p = p.parentNode;
	}
	return p;
}

if (Math.round(getRealX(node)) != Math.round(%LEFT%) || Math.round(getRealY(node)) != Math.round(%TOP%))
{
	setRealXY(node, %LEFT%, %TOP%);
}

if (Math.round(getRealWidth(node)) != Math.round(%WIDTH%))
{
	setRealWidth(node, %WIDTH%);
}

if (Math.round(getRealHeight(node)) != Math.round(%HEIGHT%))
{
	setRealHeight(node, %HEIGHT%);
}
