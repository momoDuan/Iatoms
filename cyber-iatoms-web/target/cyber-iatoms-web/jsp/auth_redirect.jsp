<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/jsp/common/common.jsp"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/jquery-easyui-1.4.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/jquery-easyui-1.4.3/themes/icon.css">
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/locale/easyui-lang-zh_TW.js"></script>
<script type="text/javascript">
	/**
	 * 獲得頂層窗口
	 * Evan
	 */
	function getTopWinow(){    
	    var p = window;    
	    while(p != p.parent){    
	        p = p.parent;    
	    }    
	    return p;    
	}
	/**goto login.do
	*/
	function gotoLogonPage() {
		l1 = window.parent;
		l2 = window.parent.parent;
		l3 = window.parent.parent.parent;
		if( l3 != null )
			l3.location.href = "${contextPath}/jsp/index.jsp";
		else if( l2 != null )
			l2.location.href = "${contextPath}/jsp/index.jsp";
		else if( l1 != null )
			l1.location.href = "${contextPath}/jsp/index.jsp";
		else
			window.location.href = "${contextPath}/jsp/index.jsp";
	}
	
	var theResult = '${returnMessageCode}';
	if (theResult == '<%=IAtomsMessageCode.LOGIN_ERROR_INVALID_USER_SESSION%>'){
		//獲得頂層窗口
		var win = getTopWinow();
		//使退出畫面不再彈出是否提示對話框
		win.setOffFlag(false);
		//alert("網路連線逾時,請重新登入");
		win.topShowMsg("網路連線逾時,請重新登入",function (){
			gotoLogonPage();
		});
		//$.messager.show('我的消息','这是一个提示信息！','info');			
	} else {
		gotoLogonPage();
	}	
</script>
</head>
<body class="nosidebar fixed-top stretch-background">
	
</body>
</html>
