<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@page import="cafe.core.context.SessionContext"%>
<%@page import="cafe.core.bean.dto.AbstractFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="parentNodes" value="${logonUser.parentNodes}"/>
<c:set var="childNodes" value="${logonUser.childNodes}"/>
<c:set var="useCaseNo" value="${formDTO.useCaseNo}"/>
<div id="sidebar" role="navigation">
    <div id="sidebar-trigger" class="sidebar-toggler"></div>
    <!-- BEGIN QUICK SEARCH FORM -->
    <div class="navbar">
    </div>
    <!-- END QUICK SEARCH FORM -->
    <!--BEGIN TABS-->
    <div id="main_nav" class="tabbable tabbable-sidebar">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab_service" data-toggle="tab">功能选单</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_service">
                <!-- BEGIN SIDEBAR MENU #1 -->
                <ul>
                    <li class="${(useCaseNo == 'BILL10000' || useCaseNo == '')?'active':''}"><a href="billing.do">开单</a> </li>
                    <c:forEach var="parentNode" items="${parentNodes}">
                    	<li class="${(useCaseNo != null && useCaseNo !='' && fn:startsWith(parentNode.id,fn:substring(useCaseNo,'0','4')))?'has-sub active':'has-sub'}" name="parant_li"><a href="javascript:;">${parentNode.functionName}<span class="arrow"></span></a>
                    		 <ul class="sub">
	                    		<c:forEach var="childNode" items="${childNodes}">
	                    			<c:if test="${parentNode.id == childNode.parentFunctionId}">
	                    				<li class="${useCaseNo == childNode.id?'active':''}" name="child_li"><a href="${childNode.functionUrl}">${childNode.functionName}</a></li>
	                    			</c:if>
	                    		</c:forEach>
                    		 </ul>
                    	</li>
                    </c:forEach>
                <!-- END SIDEBAR MENU #1  -->
            </div>
        </div>
    </div>
    <!--END TABS-->
    <script type="text/javascript">
    	function getActive(){
    		var parant_lis = document.getElementsByName("parant_li");
    		var child_lis = document.getElementsByName("child_li");
    		if(child_lis != null && child_lis.length>0){
    			var child_li = null;
    			for(var i=0;i<child_lis.length;i++){
    				child_li = child_lis[i];
    				if(child_li.className == 'active'){
    					child_li.parentNode.parentNode.className="has-sub active";
    				}
    			}
    		}
    	}
    	//window.onload=getActive();
    </script>
</div>
