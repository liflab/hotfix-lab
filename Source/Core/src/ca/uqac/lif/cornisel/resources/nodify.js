var body = document.getElementsByTagName("BODY")[0];
var NEXT_NODE_NAME = 0;
var nbNodes = body.getElementsByTagName("*").length + 1;

function getNodeName()         
{         			
	return NEXT_NODE_NAME++;
};

function nodify(domNode, level, parentNode)        
{         			
	var node = {};
	var paddingTop = 0;
	var paddingBottom = 0;
	var paddingLeft = 0;
	var paddingRight = 0;
	var nodeCounter = 0;
	node.domNode = domNode;
	paddingTop = domNode.style.paddingTop.split("px")[0];
	paddingBottom = domNode.style.paddingBottom.split("px")[0];
	paddingLeft = domNode.style.paddingLeft.split("px")[0];
	paddingRight = domNode.style.paddingRight.split("px")[0];
	node.parentNode = parentNode;
	node.nodeName = getNodeName();
	domNode.setAttribute("corniId", node.nodeName);
	node.level = level;
	node.x = domNode.getBoundingClientRect().x;
	node.y = domNode.getBoundingClientRect().y;
	node.width = domNode.getBoundingClientRect().width - paddingLeft - paddingRight;
	node.height = domNode.getBoundingClientRect().height - paddingBottom - paddingTop;
	node.children = [];
	for(var i = 0; i < domNode.children.length; i++)         			
	{         			
		if(domNode.children[i].tagName == "SCRIPT" || domNode.children[i].tagName == "STYLE")			
			continue;
		nodeCounter ++;
		node.children.push(nodify(domNode.children[i], level+1, node.nodeName));
	}         			
	delete node.domNode;
	return node;
}                                    

function getAllNodes()         
{         			
	return JSON.stringify(nodify(body,0, null));
};

return getAllNodes();
