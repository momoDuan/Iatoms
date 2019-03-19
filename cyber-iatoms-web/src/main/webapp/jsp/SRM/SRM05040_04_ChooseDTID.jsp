<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	PaymentFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (PaymentFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null ) {
		formDTO = new PaymentFormDTO();
		ucNo = IAtomsConstants.UC_NO_SRM_05040;
	} else {
		ucNo = formDTO.getUseCaseNo();
	}
	//客戶下拉列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, PaymentFormDTO.PARAM_COMPANY_LIST);
	//耗材品名稱下拉列表
	List<Parameter> suppliesNames = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, PaymentFormDTO.PARAM_SUPPLIES_TYPE_NAME_LIST);
%>
<html>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="suppliesNames" value="<%=suppliesNames%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- <div region="center" style="width: auto; height: auto; padding: 10px; background: #eee; overflow-y: hidden"> -->
<div region="center" style="margin-left:2%; width: auto; height: auto; padding: 10px; overflow-y: auto" class="setting-dialog-panel-height topSoller">
			<table id="chooseDtidPayTb" class="easyui-datagrid" title="選擇 DTID" style="width: 98%; height: auto"
				data-options="
					rownumbers:true,
					pagination:true,
					pageList:[15,30,50,100],
					pageSize:15,
					iconCls: 'icon-edit',
					singleSelect: true,
					method: 'get',
					pageNumber:0,
					onClickRow:clickDtidRow,
					nowrap:false,
					toolbar:'#tbDTID'">
				<thead>
					<tr>
						<!-- <th data-options="field:'ck',checkbox:true"></th> -->
						<th data-options="field:'caseId',halign:'center',width:200,sortable:true">案件編號</th>
						<th data-options="field:'requirementNo',halign:'center',width:200,sortable:true">需求單號</th>
						<th data-options="field:'dtid',halign:'center',width:100,sortable:true">DTID</th>
						<th data-options="field:'tid',halign:'center',sortable:true">TID</th>
						<th data-options="field:'merchantCode',halign:'center',width:180,sortable:true">特店代號</th>
						<th data-options="field:'merchantName',halign:'center',width:200,sortable:true">特店名稱</th>
						<th data-options="field:'createdDate',halign:'center',align:'center',width:190,sortable:true,formatter:formatToTimeStamp">進件時間</th>
						<th data-options="field:'acceptableFinishDate',halign:'center',align:'center',width:160,sortable:true,formatter:formaterTimeStampToyyyyMMDD">實際完修日</th>
						<th data-options="field:'serialNumber',halign:'center',width:190,sortable:true">設備序號</th>
						<th data-options="field:'assetName',halign:'center',width:190,sortable:true">設備名稱</th>
						<th data-options="field:'contractCode',halign:'center',width:190,sortable:true">合約編號</th>
					</tr>
				</thead>
			</table>
			<div id="tbDTID" style="padding: 2px 5px">
			<form id="queryDTIDPayForm" >
				<table>
					<tr>
						<td >客戶:</td>
						<td width="180px">
							<cafe:droplisttag
								id="dtidCustomerId"
								name="dtidCustomerId" 
								result="${companyList }"
								css="easyui-combobox"
								selectedValue="${formDTO.queryCompanyId }"
								hasBlankValue="true"
								blankName="請選擇"
								style="width: 150px"
								javascript="disabled"
							></cafe:droplisttag>
							<input id="<%=PaymentFormDTO.PARAM_DTID_QUERY_COMPANY_ID %>" name="<%=PaymentFormDTO.PARAM_DTID_QUERY_COMPANY_ID %>" value="<c:out value='${formDTO.queryCompanyId }'/>" type="hidden"/>
						</td>
						<td>特店代號:</td>
						<td width="180px">
							<input id="<%=PaymentFormDTO.PARAM_DTID_QUERY_MERCHANT_CODE%>" name="<%=PaymentFormDTO.PARAM_DTID_QUERY_MERCHANT_CODE%>" maxlength="20px" class="easyui-textbox" style="width: 150px">
						</td>
						<td>DTID:</td>
						<td width="170px">
							<input id="<%=PaymentFormDTO.PARAM_DTID_QUERY_DTID%>" name="<%=PaymentFormDTO.PARAM_DTID_QUERY_DTID%>" maxlength="8px" class="easyui-textbox" style="width: 150px">
						</td>
					</tr>
					<tr>
						<td>TID:</td>
						<td>
							<input id="<%=PaymentFormDTO.PARAM_DTID_QUERY_TID%>" name="<%=PaymentFormDTO.PARAM_DTID_QUERY_TID%>" maxlength="8px" class="easyui-textbox" style="width: 150px">
						</td>
						<td>設備序號:</td>
						<td>
							<input id="<%=PaymentFormDTO.PARAM_DTID_QUERY_SERIAL_NUMBER%>" name="<%=PaymentFormDTO.PARAM_DTID_QUERY_SERIAL_NUMBER%>" maxlength="20px" class="easyui-textbox" style="width: 150px">
						</td>
						<td>案件編號:</td>
						<td>
							<input id="<%=PaymentFormDTO.PARAM_DTID_QUERY_CASE_ID%>" name="<%=PaymentFormDTO.PARAM_DTID_QUERY_CASE_ID%>" maxlength="15px" class="easyui-textbox" style="width: 150px">
						</td>
						<td>
							<a href="#" class="easyui-linkbutton" iconcls="icon-search" onclick="queryDtidPay(1, true);">查詢</a>
						</td>
					</tr>
				</table>
				</form>
				<div style="font-size: 16px;color: red">
					<span id="msgDtid" class="red"></span>
				</div>
			</div>
		
	</div>
	<script type="text/javascript">
		var suppliesNames = null;
		/**
		* 查詢DTID
		*/
		function queryDtidPay(pageIndex, hidden){
			// 清空選中的行
			$("#chooseDtidPayTb").datagrid("unselectAll");
			var queryParam = $("#queryDTIDPayForm").form("getData");
			var blockStyleStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			
			$("#chooseDtidPayTb").datagrid({
				url : "${contextPath}/payment.do?actionId=queryDTID",
				queryParams :queryParam,
				pageNumber:pageIndex,
				isOpenDialog:true,
				sortName : '',
				method:'POST',
				loadMsg : '',
				autoRowHeight:true,
				onBeforeLoad : function() {
					$('#chooseDtidPayTb').closest('div.panel').block(blockStyleStyle);
				},
				onLoadSuccess:function(data){
					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "pointer");
   					});
   					$(".datagrid-row").mouseout(function(){ 
   						$(this).css("cursor", "auto");
   					});
					$('#chooseDtidPayTb').closest('div.panel').unblock();
					if (hidden) {
						$("#msgDtid").text("");
					}
					if (data.total == 0) {
						//提示查無數據
						$("#msgDtid").text(data.msg);
					}
					hidden = true;
				},
				onLoadError : function() {
					$('#chooseDtidPayTb').closest('div.panel').unblock();
					$("#msgDtid").text("查詢失敗！請聯繫管理員");
				}
			});
		}
		/**
		* 處理點擊事件
		*/
		function clickDtidRow(index, row){
			var caseId = row.caseId;
			var dtidCustomerId = $("#dtidCustomerId").combobox("getValue");
			viewQueryDtid.dialog('close');
			var blockStyleChoose = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$('#paymentId').closest('div.panel').block(blockStyleChoose);
			$(".datagrid-row").mouseover(function(){ 
				$(this).css("cursor", "auto");
			});
			var customerId = $("#customerId").combobox("getValue");
			ajaxService.getSuppliesTypeNameList(customerId, function(data){
				suppliesNames = initSelect(data);
			});
			ajaxService.getPayInfo(row.dtid, dtidCustomerId, row.caseId, function(result){
				uploadPayInfo(result);
			});
			
		}
		
	</script>
</body>
</html>