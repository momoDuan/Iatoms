<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	MerchantFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (MerchantFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null) {
		formDTO = new MerchantFormDTO();
	}else{
		ucNo = formDTO.getUseCaseNo();
	}
	CompanyDTO company = formDTO.getCompanyDTO();
	if (company == null) {
		company = new CompanyDTO();
	}
	//客戶下拉菜單
	List<Parameter> midCustomerList = (List<Parameter>) SessionHelper.getAttribute(request,ucNo,IAtomsConstants.PARAM_CUSTOMER_LIST);
%>
<c:set var="company" value="<%=company%>" scope="page"></c:set>
<c:set var="midCustomerList" value="<%=midCustomerList%>" scope="page"></c:set>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div region="center" style="margin-left:2%; width: auto; height: auto; padding: 10px; overflow-y: auto" class="setting-dialog-panel-height topSoller">
	<!-- <div region="center" style="margin-left:2%;width: auto; height: auto; padding: 10px;  overflow-y: hidden"> -->
		<!-- <form id="fmMerchant" method="post" data-options="novalidate:true"> -->
			<table id="chooseMidDataGrid" class="easyui-datagrid" title="選擇 MID" style="width: 98%; height: auto"
				data-options="
					nowrap : false,
					pagination:true,
					pageList:[15,30,50,100],
					pageSize:15,
					onCheck:chooseMidOnCheck,
					rownumbers:true,
					iconCls: 'icon-edit',
					singleSelect: true,
					method: 'get',
					pageNumber:0,
					toolbar:'#tbMerchart'">
				<thead>
					<tr>
						<!-- <th data-options="field:'ck',checkbox:true"></th> -->
						<th data-options="field:'merchantId',width:120,sortable:true,hidden:true">特店表頭ID</th>
						<th data-options="field:'merchantCode',width:240,sortable:true">特店代號</th>
						<th data-options="field:'name',width:240,sortable:true">特店名稱</th>
						<th data-options="field:'remark',width:260,sortable:true">備註</th>
					</tr>
				</thead>
			</table>
			<div id="tbMerchart" style="padding: 2px 5px;">
				<form id="fmMerchant" method="post" data-options="novalidate:true">
					客戶:
					<cafe:droplisttag
						id="queryMIdCustomerId"
						name="queryMIdCustomerId" 
						css="easyui-combobox"
						hasBlankValue="true"
						blankName="請選擇"
						result="${midCustomerList }"
						selectedValue="${company.companyId }"
						style="width:170px"
						javascript="editable=false disabled"
							></cafe:droplisttag>
					<%-- <input class="easyui-textbox" style="width: 150px" disabled="disabled" value="<c:out value='${company.shortName }'/>">
					<input type="hidden" id="queryMIdCustomerId" name="queryMIdCustomerId" value="<c:out value='${company.companyId }'/>"> --%>
					特店代號:
					<input class="easyui-textbox" id="queryMId" name="queryMId" style="width: 150px" maxlength='20px'>
					特店名稱:
					<input class="easyui-textbox" id=queryMIdRegisteredName name="queryMIdRegisteredName" style="width: 150px" maxlength='50px'>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" onclick="queryMid(1);">查詢</a>
					<div class="red">
						<span id="queryMidMsg"></span>
					</div>
				</form>
			</div>
		<!-- </form> -->
	</div>
	<script type="text/javascript">
	
	function getQueryParam(){
		var querParam = {
			actionId : "<%=IAtomsConstants.ACTION_QUERY_MID%>",
			queryCompanyId : $("#queryMIdCustomerId").combobox("getValue"),
			queryMerchantCode : $("#queryMId").textbox('getValue'),
			queryName : $("#queryMIdRegisteredName").textbox('getValue'),
		};
		return querParam;
	}
	/*
	* 查詢mid
	*/
	function queryMid(pageIndex){
		var queryParam = getQueryParam();
		var options = {
				url : "${contextPath}/merchant.do",
				queryParams :queryParam,
				pageNumber:pageIndex,
				isOpenDialog:true,
				sortName : '',
				method:'POST',
				onLoadSuccess:function(data){
					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "pointer");
   					});
   					$(".datagrid-row").mouseout(function(){ 
   						$(this).css("cursor", "auto");
   					});
					$("#queryMidMsg").text("");
					// 提示查無數據
					if (data.total == 0) {
						$("#queryMidMsg").text(data.msg);
					}
				},
				onLoadError : function() {
					$("#queryMidMsg").text("查詢失敗！請聯繫管理員");
				}
			}
		openDlgGridQuery("chooseMidDataGrid", options);
	}
	</script>
</body>
</html>