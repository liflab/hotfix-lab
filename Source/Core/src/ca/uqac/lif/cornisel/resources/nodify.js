var body = document.getElementsByTagName("BODY")[0];
var NEXT_NODE_NAME = body.getAttribute("corniId");
var nbNodes = body.getElementsByTagName("*").length + 1;

function getNodeName()         
{       
  	return ++NEXT_NODE_NAME;
}

/** Queue and travers code taken from https://medium.com/swlh/traversing-trees-breadth-first-and-depth-first-searches-with-javascript-316f23c9fe8f **/3
class QueueNode {
  constructor(value) {
    this.value = value;
    this.next = null;
  }
}

class Queue {
  constructor() {
    this.first = null;
    this.last = null;
    this.size = 0;
  }
  //newnode goes to back of the line/end of the queue
  enqueue(value) {
    const newNode = new QueueNode(value);
    //if queue is empty
    if (this.size === 0) {
      this.first = newNode;
      this.last = newNode;
      // add current first pointer to new first(new node), and make new node new first
    } else {
      this.last.next = newNode;
      this.last = newNode;
    }
    //add 1 to size
    this.size++;

    return this;
  }
  // dequeue nodes off the front of the line
  dequeue() {
    //if queue is empty return false
    if (this.size === 0) return false;
    //get dequeuedNode
    const dequeuedNode = this.first;
    //get new first (could be NULL if stack is length 1)
    const newFirst = this.first.next;
    //if newFirst is null, reassign last to newFirst(null)
    if (!newFirst) {
      this.last = newFirst;
    }
    //assign new first
    this.first = newFirst;
    //remove refernce to list
    dequeuedNode.next = null;
    //remove 1 from size
    this.size--;
    //return dequeuednode
    return dequeuedNode;
  }
}


function traverseBFS(root) {
    //if there is no root, return false
    if (!root) {
      return false;
    }
    //start a new Queue
    const queue = new Queue();
  
    //add root to queue
    queue.enqueue(root);
    //while queue is not empty
    while (queue.size !== 0) {
		//get TreeNode Children
		const nodeChildren = queue.first.value.children;
		//if node has children, loop and add each to queue
		if (nodeChildren.length !== 0) {
			nodeChildren.forEach(child => queue.enqueue(child));
		}
		//push the first item in the queue to the tree values

		//Normal
		//queue.first.value.nodeName = getNodeName();
		//queue.first.value.domNode.setAttribute("corniId", queue.first.value.nodeName);
		
		//small part only
		if(queue.first.value.domNode.getAttribute("corniId") == null)
		{
			queue.first.value.nodeName = getNodeName();
			queue.first.value.domNode.setAttribute("corniId", queue.first.value.nodeName);
		}
		else
		{
			queue.first.value.nodeName = queue.first.value.domNode.getAttribute("corniId");
		}
		
		delete queue.first.value.domNode;
	  
		//remove first node from queue
		queue.dequeue();
    }
   
}
function nodify(domNode, level)         
{         			
	var node = {};
	var paddingTop = 0;
	var paddingBottom = 0;
	var paddingLeft = 0;
	var paddingRight = 0;
	var nodeCounter = 0;
	var borderTop = 0;
	var borderLeft = 0;
	var borderRight = 0;
	var borderBottom = 0;
	var marginTop = 0;
	var marginBottom = 0;
	var marginLeft = 0;
	var marginRight = 0;
	
	node.domNode = domNode;
	/*paddingTop = domNode.style.paddingTop.split("px")[0];
	paddingBottom = domNode.style.paddingBottom.split("px")[0];
	paddingLeft = domNode.style.paddingLeft.split("px")[0];
	paddingRight = domNode.style.paddingRight.split("px")[0];*/
	
	/*paddingTop = parseFloat(window.getComputedStyle(domNode).paddingTop);
	paddingBottom = parseFloat(window.getComputedStyle(domNode).paddingBottom);
	paddingLeft = parseFloat(window.getComputedStyle(domNode).paddingLeft);
	paddingRight = parseFloat(window.getComputedStyle(domNode).paddingRight);*/
	paddingTop = 0;
	paddingBottom = 0;
	paddingLEft = 0;
	paddingRight = 0;
	
	marginTop = parseFloat(window.getComputedStyle(domNode).marginTop);
	marginBottom = parseFloat(window.getComputedStyle(domNode).marginBottom);
	marginLeft = parseFloat(window.getComputedStyle(domNode).marginLeft);
	marginRight = parseFloat(window.getComputedStyle(domNode).marginRight);
	
	borderLeft = parseFloat(window.getComputedStyle(domNode).borderLeftWidth);
	borderRight = parseFloat(window.getComputedStyle(domNode).borderRightWidth);
	borderTop = parseFloat(window.getComputedStyle(domNode).borderTopWidth);
	borderBottom = parseFloat(window.getComputedStyle(domNode).borderBottomWidth);
		
	node.level = level;
	node.x = domNode.getBoundingClientRect().x;
	node.y = domNode.getBoundingClientRect().y;
	node.width = domNode.getBoundingClientRect().width;
	node.height = domNode.getBoundingClientRect().height;
	/*node.width = domNode.getBoundingClientRect().width + paddingLeft + paddingRight + borderLeft + borderRight + marginLeft + marginRight;
	node.height = domNode.getBoundingClientRect().height + paddingBottom + paddingTop + borderTop + borderBottom +marginTop + marginBottom;*/
	node.children = [];
	if(domNode.getAttribute("corniid") != null)
	{
		for(var i = 0;i < domNode.children.length; i++)   	
		{         			
			if(domNode.children[i].tagName == "SCRIPT" || domNode.children[i].tagName == "STYLE" || domNode.children[i].tagName == "META" || domNode.children[i].tagName == "LINK")
				continue;
			nodeCounter ++;
			node.children.push(nodify(domNode.children[i], level+1));
		}  
	}       			
	return node;
} 
  
function getAllNodes()         
{      
	var nodeTree = nodify(body,0);
	traverseBFS(nodeTree);
	return JSON.stringify(nodeTree);
}                  
return getAllNodes();                          