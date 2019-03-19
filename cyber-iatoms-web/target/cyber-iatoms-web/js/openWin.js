/**
 * 控制彈出DIV層
 */
function doOpenDiv(divName){
	//抓取div控制對象
	var divObj=document.getElementById(divName);
	if(divObj==null){
		return false;
	}
	sAlert(divObj);
}
//控制DIV彈出及遮罩層
function sAlert(divObj){
	
	var sWidth,sHeight; 
    sWidth=document.body.scrollWidth ; 
    	//document.body.client;
    	///document.body.offsetWidth;//浏览器工作区域内页面宽度 
    sHeight=document.body.scrollHeight  ;//屏幕高度（垂直分辨率） 
//	    sHeight=document.body.offsetHeight;//屏幕高度（垂直分辨率） 
    var msgw=350;//提示窗口的宽度 
    var msgh=225;//提示窗口的高度 
    //背景层（大小与窗口有效区域相同，即当弹出对话框时，背景显示为放射状透明灰色） 
    var   bgObj=document.createElement("div");//创建一个div对象（背景层） 
    //定义div属性，即相当于 
    // <div   id="bgDiv"   style="position:absolute;   top:0;   background-color:#777;   filter:progid:DXImagesTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75);   opacity:0.6;   left:0;   width:918px;   height:768px;   z-index:10000;"> </div> 
    bgObj.setAttribute('id','bgDiv'); 
    bgObj.style.position="absolute"; 
    bgObj.style.top="0"; 
    bgObj.style.background="#e7e7ff"; 
    bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75"; 
    bgObj.style.opacity="0.6"; 
    bgObj.style.left="0"; 
    bgObj.style.width=sWidth   +  "px"; 
    bgObj.style.height=sHeight   +  "px"; 
    bgObj.style.zIndex   ="10000"; 
    document.body.appendChild(bgObj);//在body内添加该div对象 
    
    //創建一個div對象，用於彈出畫面
    var openObj = document.createElement("div");
	openObj.setAttribute("id","openDiv"); 
	openObj.setAttribute("align","center"); 
	openObj.style.background="white"; 
	openObj.style.position   =  "absolute"; 
	openObj.style.left   =  "50%"; 
	openObj.style.top   =  "50%"; 
	openObj.style.font="12px/1.6em   Verdana,   Geneva,   Arial,   Helvetica,   sans-serif"; 
	openObj.style.marginLeft   =  "-225px"   ; 
	openObj.style.marginTop   =   -75+document.documentElement.scrollTop+"px"; 
	openObj.style.width   =   msgw   +  "px"; 
	openObj.style.height   =msgh   +  "px"; 
	openObj.style.textAlign   =  "center";
	openObj.style.lineHeight   ="25px"; 
	openObj.style.zIndex   =  "10010"; 
	openObj.style.display   =  "inline";
	openObj.innerHTML = divObj.innerHTML;
	document.body.appendChild(openObj);
	document.getElementById(openDiv).style.display="inline";
   
}