var m_idMap = {};

/**
 * Computes the absolute coordinates of an element
 * with respect to the document
 * @param element The element to get the position
 * @return A JSON structure giving the cumulative top and left
 *   properties
 */
var cumulativeOffset = function(element)
{
	var top = 0, left = 0;
	do
	{
		top += element.offsetTop  || 0;
		left += element.offsetLeft || 0;
		element = element.offsetParent;
	} while(element);

	return {
		top: top,
		left: left
	};
};

var remove_units = function(s)
{
	if (typeof s == "string" || s instanceof String)
	{
		s = s.replace("px", "");
	}
	return Number(s);

};

var add_dimensions = function(dimensions)
{
	var sum = 0;
	for (var i = 0; i < dimensions.length; i++)
	{
		var d = dimensions[i];
		sum += remove_units(d);
	}
	return sum;
};

var unHighlightElements = function()
{
	if(document.getElementById("cp-highlight"))
	{
		document.getElementById("cp-highlight").innerHTML = "";
	}
};

/**
 * Highlights an element that violates a property
 */
var highlightElement = function(id, tuple_id)
{
	if(!(window.m_idMap[id]))
	{
		return;
	}
	var el = window.m_idMap[id].element;
	if(!el.tagName)
	{
		el = el.parentElement;
	}
	var offset = cumulativeOffset(el);
	var in_html = highlightDiv.innerHTML;
	in_html += "<div class=\"cp-highlight-zone\" ";
	in_html += "style=\"pointer-events:none;opacity:0.1;border-radius:3px;cursor:help;position:absolute;border:4px solid red;";
	in_html += "left:" + add_dimensions([offset.left, "-4px"]) + "px;top:" + add_dimensions([offset.top, "-4px"]) + "px;";
	in_html += "width:" + add_dimensions([el.offsetWidth, "4px"]) + "px;height:" + add_dimensions([el.offsetHeight, "4px"]) + "px\">";
	in_html += "</div>";
	highlightDiv.innerHTML = in_html;
};

unHighlightElements();

if(!(document.getElementById("cp-highlight")))
{
	var highlightDiv = document.createElement("div");
	highlightDiv.id = "cp-highlight";
	highlightDiv.style = "position:absolute;top:0px;left:0px;";
	document.body.appendChild(highlightDiv);
}
else
{
	highlightDiv = document.getElementById("cp-highlight");
}

//Highlight elements, if any  
for (var i = 0; i < arguments[0].length; i++)
{
	var set_of_tuples = arguments[0][i];
	for (var j = 0; j < set_of_tuples.length; j++)
	{
		var tuple = set_of_tuples[j];
		for (var k = 0; k < tuple.length; k++)
		{
			var el_id = tuple[k];
			highlightElement(el_id, i);
		}
	}
}
