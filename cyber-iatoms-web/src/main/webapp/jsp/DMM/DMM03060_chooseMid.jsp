<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetManageFormDTO formDTO = null;
	if(ctx != null) {
		formDTO = (AssetManageFormDTO) ctx.getResponseResult();
	}
	if (formDTO == null) {
		formDTO = new AssetManageFormDTO();
	}
	CompanyDTO company = formDTO.getMerchantFormDTO().getCompanyDTO();
	if (company == null) {
		company = new CompanyDTO();
	}
%>
<c:set var="company" value="<%=company%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<title></title>
</head>
<body>
    <div class="red">
		<span id="msgMid"></span>
	</div>
	<div region="center" style="margin-left:2%; width: auto; height: auto; padding: 10px; overflow-y: auto" class="setting-dialog-panel-height">
    <!-- <div region="center"  style="width: 750px; height: auto; padding: 1px; background: #eee; overflow-y: hidden"> -->
       
       <!--  <form id="fmMerchant" method="post" data-options="novalidate:true"> -->
                <table id="chooseMidDataGrid" class="easyui-datagrid" title="選擇 MID" style="width: 98%; height: auto"
                    data-options="
					nowrap : false,
					pagination:true,
					rownumbers:true,
					iconCls: 'icon-edit',
					singleSelect: true,
					method: 'get',
					pageList:[15,30,50,100],
					pageSize:15,
					pageNumber:0,
					toolbar:'#tbMerchart'">
                    <thead>
                        <tr>
                           <!--  <th data-options="field:'ck',halign:'center',checkbox:true"></th> -->
                            <th data-options="field:'merchantCode',halign:'center',width:80,sortable:true">特店代號</th>
                            <th data-options="field:'name',halign:'center',width:160,sortable:true">特店名稱</th>
                            <th data-options="field:'remark',halign:'center',width:160,sortable:true">備註</th>
                        </tr>
                    </thead>
                </table>
                
                <div id="tbMerchart" style="padding: 2px 5px;">
                <form id="fmMerchant" method="post" data-options="novalidate:true">
		                     客戶:
		            <input class="easyui-textbox" style="width: 110px" disabled="disabled"  value="${company.shortName }">
		            <input type="hidden" id="queryMIdCustomerId" name="queryMIdCustomerId" value="${company.companyId }">
		                    特店代號:
		            <input class="easyui-textbox" style="width: 110px" id="queryMId" name="queryMId">
		                    特店名稱:
		            <input class="easyui-textbox" style="width: 110px" id=queryMIdRegisteredName name="queryMIdRegisteredName">                  
		                    <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search"onclick="queryMid(1,true);">查詢</a>
		          </form>
                </div>
                
           <!--  </form> -->
		
    </div>
<script type="text/javascript">


function getQueryParam(){
	var querParam = {
		actionId : "<%=IAtomsConstants.ACTION_QUERY%>",
		queryCompanyId : $("#queryMIdCustomerId").val(),
		queryMerchantCode : $("#queryMId").textbox('getValue'),
		queryName : $("#queryMIdRegisteredName").textbox('getValue'),
	};
	return querParam;
}
function queryMid(page,hidden){
	var queryParam = getQueryParam();
		var options = {
				url : "${contextPath}/merchant.do",
				queryParams :queryParam,
				pageNumber:page,
				isOpenDialog:true,
				onLoadSuccess:function(data){
					if (hidden) {
						$("#msgMid").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#msgMid").text(data.msg);
						}
					}
					hidden = true;
				},
				onLoadError : function() {
					$("#msgMid").text("查詢失敗！請聯繫管理員");
				},
				onCheck : function(index,row){
				midClickRow(row,false);
				},
				onSelect : function(index,row){
   					midClickRow(row,false);
   					}
			};
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("chooseMidDataGrid", options); 
}

</script>
</body>
</html>