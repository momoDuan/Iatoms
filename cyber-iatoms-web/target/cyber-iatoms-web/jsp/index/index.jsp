<%@page import="java.sql.Timestamp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@page import="cafe.core.context.SessionContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp" %>
<%
    	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
    	IAtomsLogonUser iatomsLogonUser = null;
    	if (ctx != null) {
    		iatomsLogonUser = (IAtomsLogonUser)ctx.getLogonUser();
    	}
     %>
<c:set var="logonUser" value="<%=iatomsLogonUser %>"> </c:set>
<c:set var="parentNodes" value="${logonUser.parentNodes}"/>
<c:set var="childNodes" value="${logonUser.childNodes}"/>

<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=EDGE">
    <title>Welcome To iATOMS</title>
    <link rel="stylesheet" type="text/css" href="assets/jquery-easyui-1.4.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="assets/jquery-easyui-1.4.3/themes/icon.css">
    <script type="text/javascript" src="assets/jquery-easyui-1.4.3/jquery.min.js"></script>
    <script type="text/javascript" src="assets/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="assets/jquery-easyui-1.4.3/locale/easyui-lang-zh_TW.js"></script>
    <script type="text/javascript" src="js/tab-default.js"></script>
    <link rel="shortcut icon" href="assets/img/cyber.ico" type="image/gif" />
    <script type="text/javascript">
	//Task #2624 瀏覽器上一頁
	history.pushState(null, null, document.URL);
	window.addEventListener('popstate', function () {
		history.pushState(null, null, document.URL);
	});
    var offFlag = true;
    $(window).bind("unload",function(event){
    	if (offFlag) {
    		$.post("${contextPath}/logoff.do",null,null,null);
    	}
    	//$("#logoffForm").attr("action","logoff.do").submit();    	
    	$(window).unbind('beforeunload');
    });
    function setOffFlag(flag) {
    	offFlag = flag;
    }
	function logoff(){
		offFlag = false;
		$.messager.confirm('確認對話框','即將登出系統，是否確定?', function(confirm) {
			if (confirm) {
				$("#logoffForm").attr("action","logoff.do").submit();
			} else {
				offFlag = true;
			}
		});
	}
	/*
	* 顯示錯誤消息
	*/
	function topShowMsg(msg,callbackFun) {
		//alertErrorCommon(msg, callbackFun);
		$.messager.alert({title:'錯誤訊息',
			msg:msg,
			icon:'error',
			fun:callbackFun,
			onClose:callbackFun});
	}
</script>  
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
  	<noscript>
        <div style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
            <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
        </div>
    </noscript>
    <form id="logoffForm" method="post">
	    <div data-options="fit:false,region:'north'" style="overflow: hidden; height: 30px; line-height: 20px; background-image: url('assets/img/bg3.gif')">
	        <table cellpadding="3" width="100%">
	            <tr>
	                <td width="10%"><b>iATOMS</b></td>
	                <td width="70%" >&nbsp;&nbsp;登入者： ${logonUser.name }
	                         &nbsp;&nbsp;|&nbsp;&nbsp;登入時間：<fmt:formatDate value="${logonUser.lastLoginTime }" pattern="yyyy/MM/dd HH:mm:ss"/> </td>
	                <td width="20%" style="text-align: right !important;">|&nbsp;<a class="fontcolor"  onclick="logoff();"
	                    style="color: black; text-decoration: none; cursor: pointer;">登出</a> &nbsp;&nbsp;</td>
	            </tr>
	        </table>	
	    </div>
    	
    	<div title="功能選單" style="width: 210px; overflow-y: auto;" data-options="fit:false,iconCls:'icon-redo',region:'west'">
        	<div id="menu" class="easyui-accordion" data-options="border:false,multiple:true,selected:false">
        		<c:forEach var="parentNode" items="${parentNodes}" >
        			<div title="${parentNode.functionName}" style="overflow: hidden; padding: 0px;" >
        				<div id="${parentNode.id}" style="width: 100%; padding: 2px">
        					<c:forEach var="childNode" items="${childNodes}" >
        						<c:if test="${parentNode.id == childNode.parentFunctionId}">
        							<a href="javascript:void(0)" class="easyui-linkbutton" style="width: 100%" onclick="addTab('${childNode.functionName}','${contextPath}/${childNode.functionUrl}')">${childNode.functionName}</a>
        						</c:if>
        					</c:forEach>
        				</div>
        			</div>
        		</c:forEach>
        	</div>
    	</div>
    	
    	<div region="center" id="mainPanle" style="background: #eee; overflow: hidden;">
	        <div id="tt" class="easyui-tabs" data-options="fit:true,border:false">
	            <div title="首頁" style="font-weight: bold; color: red; text-align: left;">
	                歡迎使用 iATOMS 系統。
	            </div>
	        </div>
	    </div>
    	<div scroll="no" data-options="fit:false,region:'south'" style="overflow-y: hidden;height: 30px; line-height: 30px;">
	         <div  style="overflow: hidden; width: 100%;background-color:RGB(22,81,111);color: white;font-size: 15px;">
	    	<!-- ●建議瀏覽器版本:IE9或Chrome16或Safari5或FireFox3.6 <span style="margin-left: 50px">●最佳解析度1366X768 </span> -->
	    	●建議瀏覽器版本:IE11或Chrome 51.0 <span style="margin-left: 50px">●最佳解析度1366X768 </span>  
	    	<span style="margin-left: 650px">●版号：<%@ include file="//version.txt"%> </span>
	    	</div> 
    	</div>
    </form>
</body>
</html>