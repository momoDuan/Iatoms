<script type="text/javascript">
	var str;
	//得到當前瀏覽器
	window["MzBrowser"]={};   
(function()   
{   
    if(MzBrowser.platform) return;   
   var ua = window.navigator.userAgent;   
    MzBrowser.platform = window.navigator.platform;   
  
    MzBrowser.firefox = ua.indexOf("Firefox")>0;   
    MzBrowser.opera = typeof(window.opera)=="object";   
    MzBrowser.ie = !MzBrowser.opera && ua.indexOf("MSIE")>0;   
    MzBrowser.mozilla = window.navigator.product == "Gecko";   
    MzBrowser.netscape= window.navigator.vendor=="Netscape";   
  MzBrowser.safari= ua.indexOf("Safari")>-1;   
  
    if(MzBrowser.firefox) var re = /Firefox(\s|\/)(\d+(\.\d+)?)/;   
   else if(MzBrowser.ie) var re = /MSIE( )(\d+(\.\d+)?)/;   
    else if(MzBrowser.opera) var re = /Opera(\s|\/)(\d+(\.\d+)?)/;   
    else if(MzBrowser.netscape) var re = /Netscape(\s|\/)(\d+(\.\d+)?)/;   
    else if(MzBrowser.safari) var re = /Version(\/)(\d+(\.\d+)?)/;   
    else if(MzBrowser.mozilla) var re = /rv(\:)(\d+(\.\d+)?)/;   
  
    if("undefined"!=typeof(re)&&re.test(ua))   
    MzBrowser.version = parseFloat(RegExp.$2);   
    })();    
       
    function demo()   
    {   
        if(MzBrowser.ie)   
        {   
            str="IE";   
        }   
        if(MzBrowser.firefox)   
        {   
           str="Firefox";   
        }   
}   
demo();   


	

//changePosition(){
	window.onscroll=function(){
		//得到當前頁面的高度(當前頁面的高度，固定值)
		var pageHeight = document.body.clientHeight;
		//得到當前頁面的寬度(當前頁面的寬度，固定值)
		var pageWidth = document.body.clientWidth;
		//得到當前窗口的高度(所能看到內容的高度，固定值)
		var currentWindowHeight;
		//當前窗口最頂的位置(窗口最上距離頁面最上的高度)
		var currentWindowTop;
		//當前窗口的寬度(所能看到內容的高度，固定值）
		var currentWindowWidth;
		//當前窗口最左的位置(窗口最左距離頁面最左的寬度)
		var currentWindowLeft;
		if(str == "Firefox") {
			currentWindowHeight = document.documentElement.clientHeight;
			currentWindowTop = document.documentElement.scrollTop;
			currentWindowWidth = document.documentElement.clientWidth;
			currentWindowLeft = document.documentElement.scrollLeft;
		} else if(str == "IE"){
			currentWindowHeight = document.documentElement.clientHeight;
			currentWindowTop = document.documentElement.scrollTop;
			currentWindowWidth = document.documentElement.clientWidth;
			currentWindowLeft = document.documentElement.scrollLeft;
			//部分加入titles的頁面
			if(currentWindowHeight == 0 && currentWindowTop == 0) {
				currentWindowHeight = pageHeight;
				currentWindowTop = document.body.scrollTop;
			}
			if(currentWindowWidth == 0 && currentWindowLeft == 0) {
				currentWindowWidth = pageWidth;
				currentWindowLeft = document.body.scrollLeft;
			}
		}
		var floatDiv = document.getElementById("floatDiv");
		if(currentWindowTop == 0){
			floatDiv.style.display = "none";
		} else {
			floatDiv.style.display = "";
		}
		//當前窗口最上的位置(窗口最上距离頁面最上的高度)
		//計算上下位置=當前窗口高度＋窗口最上位置－任意值(浮動距离窗口下方的距離)
		var positionY = currentWindowHeight + currentWindowTop - 40;
		//當前窗口最左的位置(窗口最左距离頁面最左的寬度)
		//計算左右位置=當前窗口寬度＋窗口最左位置－任意值(浮動距离窗口下方的距離)
		var positionX = currentWindowWidth + currentWindowLeft - 70;
		
		floatDiv.style.top = positionY + "px";
		floatDiv.style.left = positionX + "px";
	}

	function toTop() {
		location ="#";
		document.getElementById("floatDiv").style.display = "none";
	}
	var rDrag = {
			o:null,
			init:function(o){
				o.onmousedown = this.start;
			},
			start:function(e){
				var o;
				e = rDrag.fixEvent(e);
		               e.preventDefault && e.preventDefault();
		               rDrag.o = o = this;
				o.x = e.clientX - rDrag.o.offsetLeft;
		                o.y = e.clientY - rDrag.o.offsetTop;
				document.onmousemove = rDrag.move;
				document.onmouseup = rDrag.end;
			},
			move:function(e){
				e = rDrag.fixEvent(e);
				var oLeft,oTop;
				oLeft = e.clientX - rDrag.o.x;
				oTop = e.clientY - rDrag.o.y;
				rDrag.o.style.left = oLeft + 'px';
				rDrag.o.style.top = oTop + 'px';
			},
			end:function(e){
				e = rDrag.fixEvent(e);
				rDrag.o = document.onmousemove = document.onmouseup = 

		null;
			},
		    fixEvent: function(e){
		        if (!e) {
		            e = window.event;
		            e.target = e.srcElement;
		            e.layerX = e.offsetX;
		            e.layerY = e.offsetY;
		        }
		        return e;
		    }
		}
	//如果直接輸入漢字，會出現，尚未找到在tag文件下實現的方法，只能用ascII碼
	suspendcode = "<div style='Z-index:7;position:absolute;top:95%;height:16px;display:none;' id='floatDiv'>"
		+	"<table bgcolor='lightpink'><tr><td><pre><a href='javascript:;' onclick='toTop();return false;'><font size='2'>\u56de\u81f3\u9801\u9996</font></a></td></tr></table></pre>"
		+ "</div>";
	document.write(suspendcode);
	var obj = document.getElementById('floatDiv');
	rDrag.init(obj);
//window.setInterval("changePosition()",1);
</script>

