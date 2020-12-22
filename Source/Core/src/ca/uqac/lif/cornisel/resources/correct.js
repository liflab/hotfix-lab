//get html node
var html = document.getElementsByTagName("HTML")[0];

//select node to correct
var node = html.querySelectorAll("[corniid='%ID%']")[0];

node.style.position = 'absolute';
//node.style.padding = '0px';
//node.style.margin = '0px';

//correct top
node.style.top = "%TOP%px";

//correct left
node.style.left = "%LEFT%px";

//correct with
node.style.width= "%WIDTH%px";

//correct height
node.style.height= "%HEIGHT%px";
console.log(document.querySelectorAll("[corniid]").length);

return script;