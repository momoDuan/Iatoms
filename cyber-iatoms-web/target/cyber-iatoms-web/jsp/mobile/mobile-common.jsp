<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<!-- Common Paramter start -->

<title>Welcome To Mobil iATOMS</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/jquery-easyui-1.4.3/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/jquery-easyui-1.4.3/themes/mobile.css">  
<link rel="stylesheet" type="text/css" href="${contextPath}/assets/jquery-easyui-1.4.3/themes/icon.css">  
<link rel="shortcut icon" type="image/gif" href="${contextPath}/assets/img/cyber.ico" />
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/jquery.min.js"></script>  
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/jquery.easyui.mobile.js"></script>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/locale/easyui-lang-zh_TW.js"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/easyui-extend.js"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/jquery.blockui.js"></script>
<script type="text/javascript" src="${contextPath}/js/jquery.client.js"></script>

<script type="text/javascript" src="${contextPath}/dwr/interface/ajaxService.js"></script>
<script type="text/javascript" src="dwr/engine.js"></script>
<script type="text/javascript" src="dwr/util.js"></script>
<script type="text/javascript" src="${contextPath}/js/cafe_utilities.js"></script>

<script type="text/javascript" src="${contextPath}/assets/js/jeasyui.extensions.form.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/jquery.jdirk.js"></script>
<script type="text/javascript" src="${contextPath}/assets/plugins/jquery.blockui.js"></script>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview.js"></script>

<%@ page import="cafe.workflow.util.i18NUtil" %>
<script>
jQuery.ajaxSetup({
	complete: function (xhr,textStatus) {
		try {
			sessionTimeOut(xhr);
		} catch (ex){
		}
     }
 });
 //session過期
 function sessionTimeOut(xhr) {
 	try {
		var sessionStatus = xhr.getResponseHeader('sessionstatus');
		if(sessionStatus == 'timeout') {
			//$(".panel.window").hide();
			//$(".window-shadow").hide();
			//$(".window-mask").hide();
			alertErrorCommon("網路連線逾時,請重新登入", function() {
				//var win = getTopWinow();
				//win.setOffFlag(false);
				window.location.href = "${contextPath}/mlogon.do"; 
			});
			return false;
        }
        return true;
	} catch (ex){
	}
 }
</script>