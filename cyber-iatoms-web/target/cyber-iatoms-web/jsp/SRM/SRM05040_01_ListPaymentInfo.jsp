<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentTranscationDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview-expander.js"></script>
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
	//資料狀態--新增
	String dataStatusAdd = IAtomsConstants.DATA_STATUS_CREATE;
	//資料狀態--退回
	String dataStatusCancel = IAtomsConstants.DATA_STATUS_ALREADY_CANCEL;
	//客戶下拉列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, PaymentFormDTO.PARAM_COMPANY_LIST);
	//資料狀態下拉列表
	List<Parameter> dataStatus = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PAYMENT_STATUS.getCode());
	//賠償方式下拉列表
	List<Parameter> compensationMethod = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PAYMENT_TYPE.getCode());
	//求償方式下拉列表-JSON格式
	String payType = (String) SessionHelper.getAttribute(request, ucNo, PaymentFormDTO.PARAM_PAY_TYPE_STRING);
	//獲取當前登錄者角色
	List<String> roleUsers = formDTO.getRoleUsers();
	//資料狀態-新增
	String createStatus = IAtomsConstants.DATA_STATUS_CREATE;
	//資料狀態-退回
	String backtatus = IAtomsConstants.DATA_STATUS_BACK;
	//資料狀態-求償確認
	String payConfirm = IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM;
	//資料狀態-完成
	String complete = IAtomsConstants.DATA_STATUS_COMPLETE;
	//資料狀態-已還款
	String repayment = IAtomsConstants.DATA_STATUS_ALREADY_REPAYMENT;
	//資料狀態-已取消
	String cancle = IAtomsConstants.DATA_STATUS_ALREADY_CANCEL;
	//資料狀態-已請款
	String requestFunds = IAtomsConstants.DATA_STATUS_REQUEST_FUNDS;
	//自行吸收
	String selfAbsorb = IAtomsConstants.SELF_ABSORB;
	//檢測正常
	String detectionNormal = IAtomsConstants.DETECTION_NORMAL;
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />
<html>
<c:set var="dataStatusAdd" value="<%=dataStatusAdd%>" scope="page"></c:set>
<c:set var="dataStatusCancel" value="<%=dataStatusCancel%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="dataStatus" value="<%=dataStatus%>" scope="page"></c:set>
<c:set var="compensationMethod" value="<%=compensationMethod%>" scope="page"></c:set>
<c:set var="roleUsers" value="<%=roleUsers%>" scope="page"></c:set>
<c:set var="createStatus" value="<%=createStatus%>" scope="page"></c:set>
<c:set var="backtatus" value="<%=backtatus%>" scope="page"></c:set>
<c:set var="payType" value="<%=payType%>" scope="page"></c:set>
<c:set var="payConfirm" value="<%=payConfirm%>" scope="page"></c:set>
<c:set var="complete" value="<%=complete%>" scope="page"></c:set>
<c:set var="repayment" value="<%=repayment%>" scope="page"></c:set>
<c:set var="cancle" value="<%=cancle%>" scope="page"></c:set>
<c:set var="selfAbsorb" value="<%=selfAbsorb%>" scope="page"></c:set>
<c:set var="requestFunds" value="<%=requestFunds%>" scope="page"></c:set>
<c:set var="detectionNormal" value="<%=detectionNormal%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>

</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<div id="p" class="easyui-panel" title="求償作業" style="width: 98%; height: auto;">
	<div>
		<span id="claimMsg" class="red"></span>
	</div>
		<form id="queryPaymentForm" method="post" novalidate>
			<input type="hidden" id="dataGrid-mergeFiled" value="paymentId">
			<input type="hidden" id="dataGrid-fileds" value="checkbox,customerName,paymentId">
					<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" /> 
		<table cellpadding="6">
			<tr>
				<td>客戶:</td>
				<td>
					<cafe:droplisttag css="easyui-combobox"
						id="<%=PaymentFormDTO.PARAM_QUERY_COMPANY_ID %>"
						name="<%=PaymentFormDTO.PARAM_QUERY_COMPANY_ID %>"
						result="${companyList }"
						blankName="請選擇" 
						hasBlankValue="true"
						style="width: 150px"
						javascript=" editable=false"
					></cafe:droplisttag>
				</td>
				<td>建案日期:</td>
				<td colspan="3">
					<input id="<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATESTART%>" name="<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATESTART%>" class="easyui-datebox" maxlength="10" validType="date" data-options="sharedCalendar:'#cc', onChange:function(newValue,oldValue) {
                            			$('#<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATE_END%>').datebox('isValid');
                             	}" style="width: 155px">
					&nbsp;~&nbsp;
					<input id="<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATE_END%>" name="<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATE_END%>" class="easyui-datebox" maxlength="10" data-options="sharedCalendar:'#cc',validType:['date','compareDateStartEnd[\'#<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATESTART%>\',\'日期起不可大於日期迄\']']" style="width: 155px">
					<div id="cc" class="easyui-calendar"></div>
				</td>
				<td>特店代號:</td>
				<td>
					<input class="easyui-textbox" id="<%=PaymentFormDTO.PARAM_QUERY_MERCHANT_CODE%>" name="<%=PaymentFormDTO.PARAM_QUERY_MERCHANT_CODE%>" maxlength="20" style="width: 110px">
				</td>
				<td>DTID:</td>
				<td>
					<input class="easyui-textbox" id="<%=PaymentFormDTO.PARAM_QUERY_DTID%>" name="<%=PaymentFormDTO.PARAM_QUERY_DTID%>" maxlength="8" style="width: 110px" data-options="validType:['maxLength[8]','numberBeginZero[\'DTID起限輸入長度為8碼的數字，請重新輸入\']','numberLengthEquals[\'8\',\'DTID起限輸入長度為8碼的數字，請重新輸入\']']">
				</td>
			</tr>
			<tr>
				<td>TID:</td>
				<td>
					<input class="easyui-textbox" id="<%=PaymentFormDTO.PARAM_QUERY_TID%>" name="<%=PaymentFormDTO.PARAM_QUERY_TID%>" maxlength="8" style="width: 150px">
				</td>
				<td>資料狀態:</td>
				<td>
					<cafe:droplisttag css="easyui-combobox"
						id="<%=PaymentFormDTO.PARAM_QUERY_DATA_STATUS %>"
						name="<%=PaymentFormDTO.PARAM_QUERY_DATA_STATUS %>"
						result="${dataStatus }"
						blankName="請選擇" 
						hasBlankValue="true"
						style="width: 150px"
						javascript=" editable=false panelheight=\"auto\""
					></cafe:droplisttag>
				</td>
				<td>賠償方式:</td>
				<td>
					<cafe:droplisttag css="easyui-combobox"
						id="<%=PaymentFormDTO.PARAM_QUERY_COMPENSATION_METHOD %>"
						name="<%=PaymentFormDTO.PARAM_QUERY_COMPENSATION_METHOD %>"
						result="${compensationMethod }"
						blankName="請選擇" 
						hasBlankValue="true"
						style="width: 100px"
						javascript=" editable=false, panelheight=\"auto\""
					></cafe:droplisttag>
				</td>
				<td>
					<label>通報遺失日:</label>
				</td>
				<td>
					<input class="easyui-datebox" id="<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_START%>" name="<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_START%>" validType="date" maxlength="10" style="width: 110px" data-options="sharedCalendar:'#cc', onChange:function(newValue,oldValue) {
                            			$('#<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_END%>').datebox('isValid');
                             	}"></input>
					&nbsp;~&nbsp;
					<input class="easyui-datebox" id="<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_END%>" name="<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_END%>" data-options="validType:['date','compareDateStartEnd[\'#<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_START%>\',\'日期起不可大於日期迄\']']" maxlength="10" style="width: 110px"></input>
				</td>
				<td>設備序號:</td>
				<td>
					<input class="easyui-textbox" id="<%=PaymentFormDTO.PARAM_QUERY_SERIAL_NUMBER%>" name="<%=PaymentFormDTO.PARAM_QUERY_SERIAL_NUMBER%>" maxlength="20" style="width: 110px">
				</td>
			<tr/>
			<tr>
                 <td>CyberEDC:</td>
                 <td>
                      <input type="radio" name="queryMicro" id="isMicro" value="2">是</input>
                      <input type="radio" name="queryMicro" id="notMicro" value="1" checked="checked">否</input>
                 </td>
            </tr>
		</table>
		</form>
		<div style="text-align: right;">
			<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.QUERY.code )}"><a class="easyui-linkbutton" href="#" id="btnQuery" iconcls="icon-search" onclick="queryClaimInfo(1, true);">查詢</a></c:if>
			<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.ADD.code )}"><a href="javascript:void(0)" class="easyui-linkbutton" id="btnAdd" data-options="iconCls:'icon-add'" onclick="addPayInfo()">新增</a></c:if>
			<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.EDIT.code )}"><a href="javascript:void(0)" id="btnEdit" class="easyui-linkbutton" iconcls="icon-edit" onclick="updateClaim()" disabled>修改</a></c:if>
			<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.DELETE.code )}"><a href="javascript:void(0)" id="btnDelete" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delPaymentInfo()" disabled>刪除</a></c:if>
		</div>
		<table style="width: 100%">
			<tr>
				<td style="text-align: left;" width="50%">
					<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.SEND.code )}"><a href="javascript:void(0)" id="btnSend" class="easyui-linkbutton c6" style="width: auto" onclick="payOptionSend();" disabled>送出</a></c:if>
					<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.LOCK.code )}"><a href="javascript:void(0)" id="btnLock" class="easyui-linkbutton c6" onclick="payOptionLock();" style="width: auto" disabled>鎖定</a></c:if>
					<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.COMPLETE.code )}"><a href="javascript:void(0)" id="btnComplete" class="easyui-linkbutton c6" style="width: auto" onclick="payOptionComplete();" disabled>完成</a></c:if>
					<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.REPAY.code )}"><a href="javascript:void(0)" id="btnRepay" class="easyui-linkbutton c6" style="width: auto" onclick="payOptionReturn();" disabled>還款</a></c:if>
					<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.BACK.code )}"><a href="javascript:void(0)" id="btnBack" class="easyui-linkbutton c6" style="width: auto" onclick="payOptionBack();" disabled>退回</a></c:if>
				</td>
				<td style="text-align: right;" width="50%">
					<c:if test="${fn:contains(formDTO.logonUser.accRghts[useCaseNo], functionType.EXPORT.code )}"><a href="#" id="btnExport" style="width: 150px;color: blue;" onclick="exportData()">匯出</a></c:if>
					
				</td>
			</tr>
		</table>
		<div>
			<span id="paymentItemMsg" class="red"></span>
		</div>
		<div id='payOperation' name='edit' closed="true" class="easyui-panel">
		<form method="post" class="easyui-form" id="payOperationForm">
			<table id="payOperationTable" class="easyui-datagrid" title="送出" style="width: auto; height: auto"
					data-options="
						toolbar:'#tbpayOperation',
						nowrap:false,
						singleSelect: true
					">
				<thead>
					<tr>
						<th data-options="field:'itemId',align:'center',hidden:true"></th>
						<th data-options="field:'status',align:'center',hidden:true"></th>
						<th data-options="field:'itemName',width:120,halign:'center',align:'left'">設備名稱/耗材名稱</th>
						<th data-options="field:'serialNumber',width:120,halign:'center',align:'left'">設備序號</th>
						<th data-options="field:'contractCode',width:120,halign:'center',align:'left'">合約編號</th>
						<th data-options="field:'suppliesAmount',width:120,halign:'center',align:'right'">耗材數量</th>
						<!-- 送出顯示 -->
						<th data-options="field:'checkResult',width:300,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('result'+row.itemId, 100, true, '');},hidden:true">檢測結果</th>
						<th data-options="field:'checkUser',width:200,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('user'+row.itemId, 50, false, '');},hidden:true">檢測人</th>
						<!-- 鎖定顯示 -->
						<th data-options="field:'isAskPay',width:100,align:'center',
							formatter:function(value,row)
								{
								return showCheckbox('check'+row.itemId, false, '');
							},hidden:true">是否需請款</th>
						<th data-options="field:'paymentType',width:150,align:'center',
							formatter:function(value,row)
								{
								return showCombobox('type'+row.itemId, true);
							},hidden:true">求償方式</th>
						<th data-options="field:'paymentTypeDesc',width:320,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('desc'+row.itemId, 100, false, '');},hidden:true">求償方式說明</th>
						<!-- 完成顯示 -->
						<th data-options="field:'askPayDate',width:200,align:'center',editor:{type:'datetimebox'},formatter:function(value,row){return showDatebox('askDate'+row.itemId, false, '', row.isAskPay);},hidden:true">請款時間</th>
						<th data-options="field:'askPayDesc',width:400,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('askDesc'+row.itemId, 100, false, row.isAskPay);},hidden:true">說明</th>
						<!-- 還款顯示 -->
						<th data-options="field:'cancelDate',width:150,align:'center',editor:{type:'datetimebox'},formatter:function(value,row){return showDatebox('cancleDate'+row.itemId, true, 'cancleDate','');},hidden:true">取消時間</th>
						<th data-options="field:'repaymentDate',width:150,align:'center',editor:{type:'datetimebox'},formatter:function(value,row){return showDatebox('repaymentDate'+row.itemId, true, 'repayDate','');},hidden:true">還款時間</th>
						<th data-options="field:'cancelDesc',width:300,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('cancelDesc'+row.itemId, 100, false, '');},hidden:true">取消說明</th>
						<!-- 退回顯示 -->
						<th data-options="field:'backDesc',width:300,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('backDesc'+row.itemId, 100, false, '');},hidden:true">退回說明</th>
						<!-- 送出-帳務情況 -->
						<th data-options="field:'fee',width:200,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('fee'+row.itemId, 10, false, '');},hidden:true">求償費用</th>
						<th data-options="field:'feeDesc',width:300,align:'center',editor:{type:'textbox'},formatter:function(value,row){return showText('feeDesc'+row.itemId, 100, false, '');},hidden:true">求償費用說明</th>
						<th data-options="field:'updateDateLong',width:120,align:'center',hidden:true"></th>
						<th data-options="field:'paymentReason',width:120,align:'center',hidden:true"></th>
					</tr>
				</thead>
			</table>
			<div id="tbpayOperation" style="padding: 2px 5px;">
				<a href="javascript:void(0)" id="payOperationSave" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="savePayOperation();">儲存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="payCancle();">取消</a>
			</div>
			</form>
		</div>
		
		
		<table id="dataGrid" class="easyui-datagrid" title="求償資料" style="width: 100%; height: auto"
			data-options="
				rownumbers:true,
				pagination:true,
				pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: false,
				method: 'get',
				nowrap:false,
				pageNumber:0">
			<thead>
				<tr>
					<th data-options="field:'checkbox',width:40,checkbox:true"></th>
					<th data-options="field:'itemId',hidden:true"></th>
					<th data-options="field:'customerId',halign:'center',width:140,hidden:true">客戶</th>
					<th data-options="field:'customerName',halign:'center',width:200,sortable:true">客戶</th>
					<th data-options="field:'paymentId',width:200,halign:'center',sortable:true">求償編號</th>
					<th data-options="field:'paymentItemName',width:80,halign:'center'">求償項目</th>
					<th data-options="field:'itemName',width:160,halign:'center'">設備名稱/耗材名稱</th>
					<th data-options="field:'serialNumber',width:160,halign:'center'">設備序號</th>
					<th data-options="field:'contractCode',width:160,halign:'center'">合約編號</th>
					<th data-options="field:'suppliesAmount',width:100,align:'right',halign:'center'">耗材數量</th>
					<th data-options="field:'paymentReasonName',width:160,halign:'center'">求償資訊</th>
					<th data-options="field:'statusName',width:100,halign:'center'">資料狀態</th>
					<th data-options="field:'status',width:100,halign:'center',hidden:true">資料狀態ID</th>
					<th data-options="field:'dtid',width:160,halign:'center'">DTID</th>
					<th data-options="field:'tid',width:160,halign:'center'">TID</th>
					<th data-options="field:'requirementNo',width:200,halign:'center'">需求單號</th>
					<th data-options="field:'edcType',width:150,halign:'center'">案件刷卡機型</th>
					<th data-options="field:'caseCategory',width:100,halign:'center'">案件類別</th>
					<th data-options="field:'caseId',width:200,halign:'center', formatter:initCaseId">案件編號</th>
					<th data-options="field:'merchantCode',width:200,halign:'center'">特店代號</th>
					<th data-options="field:'name',width:200,halign:'center'">特店名稱</th>
					<th data-options="field:'realFinishDate',width:160,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD">通報遺失日</th>
					
					<th data-options="field:'aoName',width:160,halign:'center',">AO人員</th>
					<th data-options="field:'lockedDate',width:190,halign:'center',align:'center',formatter:formatToTimeStamp">求償確認時間</th>
					
					<th data-options="field:'checkResult',width:160,halign:'center'">檢測結果</th>
					<th data-options="field:'checkUser',width:160,halign:'center'">檢測人</th>
					<th data-options="field:'fee',width:160,halign:'center',align:'right'">求償費用</th>
					<th data-options="field:'feeDesc',width:160,halign:'center'">求償費用說明</th>
					<th data-options="field:'isAskPay',width:160,halign:'center',align:'center',formatter:function(value,row)
							{
								return showCheckbox('',true,value);
							}">是否需請款</th>
					<th data-options="field:'paymentType',width:160,halign:'center'">求償方式</th>
					<th data-options="field:'paymentTypeDesc',halign:'center',width:160">求償方式說明</th>
					<th data-options="field:'askPayDate',width:160,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD">請款時間</th>
					<th data-options="field:'askPayDesc',width:160,halign:'center'">請款說明</th>
					<th data-options="field:'cancelDate',width:160,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD">取消時間</th>
					<th data-options="field:'repaymentDate',halign:'center',width:160,align:'center',formatter:formaterTimeStampToyyyyMMDD">還款時間</th>
					<th data-options="field:'cancelDesc',halign:'center',width:160">取消說明</th>
					<th data-options="field:'updatedByName',halign:'center',width:160">異動人員</th>
					<th data-options="field:'updatedDate',width:190,halign:'center',align:'center',formatter:formatToTimeStamp">異動時間</th>
					<th data-options="field:'isPay',hidden:true">是否需求償</th>
					<th data-options="field:'paymentItem',hidden:true">求償項目</th>
				</tr>
			</thead>
		</table>
		<input type="hidden" id="hiddenItemId"/>
		<div id="showEdit"></div>
		<div id="showCaseInfo" fit="true"></div>
	</div>
</div>

	<script type="text/javascript">
		var isUnCheck = false;
		var operation = "";
		
		$(function(){
			//設置匯入按鈕樣式-灰色且不可點擊
			$('#btnExport').attr("onclick","return false;");
			$('#btnExport').attr("style","color:gray;");
		});
		// 求償歷程列
		var dataColumns = [
			{ field: 'actionName', title: '動作', width: 100, halign:'center' },
			{ field: 'status', title: '動作後狀態', width: 150, halign:'center' },
			{ field: 'paymentContent', title: '求償內容', width: 400,halign:'center', formatter:wrapFormatter },
			{ field: 'updatedByName', title: '異動人員', width: 120, halign:'center' },
			{ field: 'updatedDate', title: '異動日期', width: 190, halign:'center',align:'center', formatter:formatToTimeStamp}
		];
		/**
		* 當單擊一行時觸發，控制修改、刪除按鈕是否可以使用。
		*/
		function onClickRow(index, row) {
			$("#paymentItemMsg").html("");
			$("#paymentItemMsg").html("");
			var canUpdateOrDelete = getPaymentIds();
			if (canUpdateOrDelete) {
				$("#btnEdit").linkbutton('enable');
				$("#btnDelete").linkbutton('enable');
			} else {
				$("#btnEdit").linkbutton('disable');
				$("#btnDelete").linkbutton('disable');
			}
		}
		/**
		* 點擊一行時，判斷當前處於選中狀態的行數
		* 只有選擇一筆求償編號相同的資料，才可進行修改、刪除操作
		*/
		function getPaymentIds(){
			var row = $('#dataGrid').datagrid('getSelections');
			if(row.length > 0) {
				var paymentIds = [];
				var tempPayId = null;
				for (var i=0;i<row.length;i++) {
					if (i==0 || (row[i].paymentId != tempPayId)) {
						paymentIds.push(row[i].paymentId);
						tempPayId = row[i].paymentId;
					}
				}
				if (paymentIds.length > 1) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
		var payType = initSelect(payType);
		/*
		* 查詢
		*/
		function queryClaimInfo(pageIndex, isHidden) {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			payCancle();
			if (!$("#queryPaymentForm").form("validate")) {
				return false;
			}
			var ignoreFirstLoad = isHidden;
			var queryStartDate = $("#<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATESTART%>").datebox("getValue");
			var queryEndDate = $("#<%=PaymentFormDTO.PARAM_QUERY_CREATE_CASE_DATE_END%>").datebox("getValue");
			var queryLostDayStart = $("#<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_START%>").datebox("getValue");
			var queryLostDayEnd = $("#<%=PaymentFormDTO.PARAM_QUERY_LOST_DAY_END%>").datebox("getValue");
			if (queryStartDate != '' || queryEndDate != '') {
				if (queryStartDate == '') {
					$("#paymentItemMsg").html("請輸入建案日期起");
					return false;
				}
				if (queryEndDate == '') {
					$("#paymentItemMsg").html("請輸入建案日期迄");
					return false;
				}
			}
			if (queryLostDayStart != '' || queryLostDayEnd != '') {
				if (queryLostDayStart == '') {
					$("#paymentItemMsg").html("請輸入通報遺失日起");
					return false;
				}
				if (queryLostDayEnd == '') {
					$("#paymentItemMsg").html("請輸入通報遺失日迄");
					return false;
				}
			}
			var queryParam = $("#queryPaymentForm").form("getData");
			//Task #3452 微型商戶
			queryParam.isMicro = $("#isMicro").prop("checked");
			queryParam.actionId = "<%=IAtomsConstants.ACTION_QUERY%>";
			var options = {
				url : '${contextPath}/payment.do',
				queryParams : queryParam,
				pageNumber : pageIndex,
				onLoadError : function(data) {
					$("#paymentItemMsg").html("查詢求償資料出錯");
				},
				onLoadSuccess : function(data){
					if (data.total == 0) {
						if (isHidden) {
							$("#paymentItemMsg").html(data.msg);
						}
						$("#btnEdit").linkbutton('disable');
						$("#btnDelete").linkbutton('disable');
						$("#btnBack").linkbutton('disable');
						$("#btnSend").linkbutton('disable');
						$("#btnLock").linkbutton('disable');
						$("#btnRepay").linkbutton('disable');
						$("#btnComplete").linkbutton('disable');
						$('#btnExport').attr("onclick","return false;");
						$('#btnExport').attr("style","color:gray;");
					} else {
						$("#btnBack").linkbutton('enable');
						$("#btnSend").linkbutton('enable');
						$("#btnLock").linkbutton('enable');
						$("#btnRepay").linkbutton('enable');
						$("#btnComplete").linkbutton('enable');
						$('#btnExport').attr("onclick","exportData()");
						$('#btnExport').attr("style","color:blue;");
					}
				},
				view: detailview,
				detailFormatter: function (index, row) {
					return '<div style="padding:2px"><div class="load-style-class"></div><table class="ddv"></table></div>';
				},
				onExpandRow: function (index, row) {
 					var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
					// loading加載
					ddv.prev("div.load-style-class").html("loading...").addClass("panel-loading");
					$('#dataGrid').datagrid('fixDetailRowHeight', index);
					
					var paymentData;
					var url = '${contextPath}/payment.do?actionId=queryPaymentTranscation';
					$.ajax({
						url: url,
						data:{'paymentId':row.paymentId},
						type:'post', 
						cache:false, 
						dataType:'json', 
						success:function(json){
							if (json.success) {
								paymentData = {
									rows:json.rows,
									total:json.total
								}
							}
							if(typeof paymentData == 'undefined'){
								paymentData = {
									rows:[],
									total:0
								}
							}
							// 取消loading加載
							if(ddv.prev("div.load-style-class").hasClass("panel-loading")){
								ddv.prev("div.load-style-class").html("").removeClass("panel-loading");
							}
							ddv.datagrid({
								data: paymentData,
								singleSelect: true,
								rownumbers: true,
								nowrap : false,
								loadMsg: '',
								height: 'auto',
								width: 1000,
								columns:[dataColumns],
								onLoadSuccess:function(){
									$('#dataGrid').datagrid('fixRowHeight', index);
									$('#dataGrid').datagrid('fixDetailRowHeight',index);
								}
							});
						},
						error:function(){
							
						}
					});
				},
				onCheck : function (index, row) {
					var payOperationRows = $("#payOperationTable").datagrid("getRows");
					if (payOperationRows.length > 0) {
						isUnCheck = true;
						setTimeout(function () {
							$("#dataGrid").datagrid("uncheckRow", index);
						});
						return false;
					}
					var rowSpan = row.dataSpan;
					var rowFirst = row.isStartRow;
					var sel = $("#dataGrid").datagrid("getSelections");
					if (rowSpan && rowSpan > 1) {
						if (rowFirst) {
							for(var i = index + 1; i < rowSpan + index; i ++) {
								$("#dataGrid").datagrid("checkRow", i);
							}
						} else {
							for(var i = index - 1; i > index - rowSpan; i --) {
								var row = $("#dataGrid").datagrid("getRows")[i];
								if (row.isStartRow) {
									var currentIndex = i;
									var selections = $("#dataGrid").datagrid("getSelections");
									var isChecked = false;
									for (var j = 0; j < selections.length; j ++) {
										var checkIndex = $("#dataGrid").datagrid("getRowIndex",selections[j]);
										if (currentIndex == checkIndex) {
											isChecked = true;
											break;
										}
									}
									if(!isChecked) {
										$("#dataGrid").datagrid("checkRow", i);
									}
									break;
								}							
							}
						}
					}
					setTimeout(function () {
						onClickRow(index, row);
					}, 0);
				}, 
				onUncheck : function (index, row) {
					var rowSpan = row.dataSpan;
					var rowFirst = row.isStartRow;
					if (rowSpan && rowSpan > 1) {
						if (rowFirst) {
							for(var i = index + 1; i < rowSpan + index; i ++) {
								$("#dataGrid").datagrid("uncheckRow", i);
							}
						} else {
							for(var i = index - 1; i > index - rowSpan; i --) {
								var row = $("#dataGrid").datagrid("getRows")[i];
								if (row.isStartRow) {
									var currentIndex = i;
									var selections = $("#dataGrid").datagrid("getSelections");
									var isChecked = false;
									for (var j = 0; j < selections.length; j ++) {
										var checkIndex = $("#dataGrid").datagrid("getRowIndex",selections[j]);
										if (currentIndex == checkIndex) {
											isChecked = true;
											break;
										}
									}
									if(isChecked) {
										$("#dataGrid").datagrid("uncheckRow", i);
									}
									break;
								}
							}
						}
					}
					setTimeout(function () {
						onClickRow(index, row);
					}, 0);
					if (!isUnCheck) {
						setTimeout(function () {
							var row = $("#dataGrid").datagrid("getRows");
							var rowsLength = $('#payOperationTable').datagrid("getRows").length;
							if (rowsLength != 0) {
								payCancle();
							}
							
						}, 0);
					} else {
						isUnCheck = false;
					}
					
				}
			};
			// 清空點選排序
			if(isHidden){
				options.sortName = '';
			}
			options.ignoreFirstLoad = !ignoreFirstLoad;
			openDlgGridQuery("dataGrid", options);
			options.ignoreFirstLoad = false;
		}
		
		/**
		* 新增求償資料
		*/
		function addPayInfo() {
			$("#paymentItemMsg").html("");
			addAndEditClaim('新增求償資料','<%=IAtomsConstants.ACTION_INIT_ADD%>', '');
		}
		
		/**
		* 修改求償資料
		*/
		function updateClaim() {
			$("#paymentItemMsg").html("");
			var row = $('#dataGrid').datagrid('getSelections');
			if (row.length >0) {
				for (var i =0;i<row.length;i++) {
					if(row[i].status=='${payConfirm}' || row[i].status=='${complete}' 
							||row[i].status=='${repayment}' ||row[i].status=='${cancle}'
							|| row[i].status == '${requestFunds}') {
						$("#paymentItemMsg").html("求償資料已鎖定，不可修改");
						return false;
					}
				}
				var updatedDate = row[0].updatedDate;
				var paymentId=row[0].paymentId;
				ajaxService.initEditCheckUpdate(paymentId, updatedDate, function(result){
					if (result) {
						$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
					} else {
						addAndEditClaim('修改求償資料','<%=IAtomsConstants.ACTION_INIT_EDIT%>', paymentId);
					}
				});
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
				return false;
			}
		}
		// 查詢dtid的dialog
		var viewQueryDtid = undefined;
		/**
		* 初始化新增修改頁面
		*/
		function addAndEditClaim(title, actionId, paymentId){
			var viewDlg = $('#showEdit').dialog({
				title: title,
				width: 900,
				height:500,
				top:10,
				cache: false,
				modal: true,
				loadingMessage:'loading...',
				queryParams : {
					actionId : actionId,
					paymentId : paymentId
				},
				onLoad :function(){
					textBoxSetting("showEdit");
					dateboxSetting("showEdit");
				},
				onClose : function(){
					$('#showEdit').dialog('clear');
					if(viewQueryDtid){
						viewQueryDtid.dialog('clear');
					}
				}, 
				href: "${contextPath}/payment.do",
				buttons : [{
					text:'儲存',
					iconCls:'icon-ok',
					width: '88',
					handler: function(){
						var dtid = $("#hiddenDtid").val();
						if (dtid != $("#dtid").textbox("getValue")) {
							$("#addPayInfoMsg").html("DTID已變更，請重新帶入資料");
							return false;
						}
						//結束設備以及耗材品表格編輯狀態並核檢數據的正確性
						if (!endEditing() || !endEditingAsset()) {
							return false;
						}
						/* if(!validateSuppliesForm()) {
							$("#claimMsg").html("");
							return false;
						} */
						var controls = ['customerId'];
						if (validateForm(controls)) {
							//獲取設備列表數據
							var assetRows = $("#assetTable").datagrid("getRows");
							//獲取耗材列表數據
							var perRows = $("#peripheralSuppliesTable").datagrid("getRows");
							//是否有求償資料--設備列表以及耗材列表是否有數據
							var isHavePay = false;
							var trs = $("#assetTable").prev().find('div.datagrid-body').find('tr');
							for (var i=0; i<assetRows.length; i++) {
								var isCheck = $($(trs[i].cells[3]).find("div")).find("input").is(":checked");
								if (isCheck) {
									if ($($(trs[i].cells[5]).find("div")).html() == ""
											|| $($(trs[i].cells[5]).find("div")).html() == null) {
										onClickCellAsset(i);
										if (!validateSuppliesForm(i, 'assetTable')) {
											return false;
										}
									}
									assetRows[i].isPay='Y';
									assetRows[i].paymentReason = $($(trs[i].cells[5]).find("div")).html();
									assetRows[i].reasonDetail = $($(trs[i].cells[6]).find("div")).html();
									assetRows[i].assetName = assetRows[i].itemName;
									assetRows[i].itemName = assetRows[i].assetId;
									isHavePay = true;
								}
							}
							if (!isHavePay && (perRows == "" || perRows == null)) {
								handleScrollTop('showEdit');
								$("#addPayInfoMsg").html("請至少設定一筆待求償資料");
								return false;
							}
							$("#assetTableValue").val(JSON.stringify(assetRows));
							trs = $("#peripheralSuppliesTable").prev().find('div.datagrid-body').find('tr');
							//由於設備列表以及耗材列表的求償資訊都是手動添加下拉列表以及文本框，所以在取值時，需要單獨進行處理。
							for (var i=0; i<perRows.length; i++) {
								perRows[i].paymentReason = $($(trs[i].cells[4]).find("div")).html();
								perRows[i].reasonDetail = $($(trs[i].cells[5]).find("div")).html();
								perRows[i].itemName = perRows[i].itemName;
								perRows[i].suppliesName = perRows[i].suppliesName;
							}
							$("#peripheralSuppliesTableValue").val(JSON.stringify(perRows));
							var saveParam = $("#fm").form("getData");
							saveParam.realFinishDate=$("#show_realFinishDate").datebox("getValue");
							saveParam.caseCreatedDate=$("#show_caseCreatedDate").datebox("getValue");
							if (saveParam.paymentId != "") {
								var row = $('#dataGrid').datagrid('getSelections');
								saveParam.saveStatus = row[0].status;
								saveParam.oldUpdatedDate = row[0].updatedDate;
							}
							commonSaveLoading('showEdit');
							$.ajax({
								url: "${contextPath}/payment.do?actionId=save",
								data:saveParam, 
								type:'post', 
								cache:false, 
								dataType:'json', 
								success:function(json){
									commonCancelSaveLoading('showEdit');
									if (json.success) {
										viewDlg.dialog('close');
										var pageIndex = getGridCurrentPagerIndex("dataGrid");
										if (pageIndex < 1) {
											pageIndex = 1;
										}
										queryClaimInfo(pageIndex,true);
										$("#paymentItemMsg").html(json.msg);
									}else{
										$("#addPayInfoMsg").html(json.msg);
									}
								},
								error:function(){
									commonCancelSaveLoading('showEdit');
									$.messager.alert('提示', "保存失敗,請聯繫管理員.", 'error');
								}
							});
						}
					} 
				},{
					text:'取消',
					iconCls:'icon-cancel',
					width: '88',
					handler: function(){
						confirmCancel(function(){
							viewDlg.dialog('close');
						});
					}
				}]
			});
		}
		
		/**
		* 顯示需要顯示的列
		* action:動作
		* isAccounting：
		*/
		function showColumns(action, isAccounting){
			hideColumnPay();
			if (action == 'send' && !isAccounting) {
				$("#payOperationTable").datagrid("getPanel").panel("setTitle","送出");
				$("#payOperationTable").datagrid("showColumn",'checkResult');
				$("#payOperationTable").datagrid("showColumn",'checkUser');
			} else if (action == 'send' && isAccounting) {
				$("#payOperationTable").datagrid("getPanel").panel("setTitle","送出");
				$("#payOperationTable").datagrid("showColumn",'fee');
				$("#payOperationTable").datagrid("showColumn",'feeDesc');
			} else if (action == 'lock') {
				$("#payOperationTable").datagrid("getPanel").panel("setTitle","鎖定");
				$("#payOperationTable").datagrid("showColumn",'isAskPay');
				$("#payOperationTable").datagrid("showColumn",'paymentType');
				$("#payOperationTable").datagrid("showColumn",'paymentTypeDesc');
			} else if (action == 'complete') {
				$("#payOperationTable").datagrid("getPanel").panel("setTitle","完成");
				$("#payOperationTable").datagrid("showColumn",'askPayDate');
				$("#payOperationTable").datagrid("showColumn",'askPayDesc');
			} else if (action == 'refund') {
				$("#payOperationTable").datagrid("getPanel").panel("setTitle","還款");
				$("#payOperationTable").datagrid("showColumn",'cancelDate');
				$("#payOperationTable").datagrid("showColumn",'repaymentDate');
				$("#payOperationTable").datagrid("showColumn",'cancelDesc');
			} else {
				$("#payOperationTable").datagrid("getPanel").panel("setTitle","退回");
				$("#payOperationTable").datagrid("showColumn",'backDesc');
			}
		}
		
		/**
		* 隱藏按鈕顯示列
		*/
		function hideColumnPay() {
			$("#payOperationTable").datagrid("hideColumn", "checkResult");
			$("#payOperationTable").datagrid("hideColumn", "checkUser");
			$("#payOperationTable").datagrid("hideColumn", "fee");
			$("#payOperationTable").datagrid("hideColumn", "feeDesc");
			$("#payOperationTable").datagrid("hideColumn", "isAskPay");
			$("#payOperationTable").datagrid("hideColumn", "paymentType");
			$("#payOperationTable").datagrid("hideColumn", "paymentTypeDesc");
			$("#payOperationTable").datagrid("hideColumn", "askPayDate");
			$("#payOperationTable").datagrid("hideColumn", "askPayDesc");
			$("#payOperationTable").datagrid("hideColumn", "backDesc");
			$("#payOperationTable").datagrid("hideColumn", "cancelDate");
			$("#payOperationTable").datagrid("hideColumn", "repaymentDate");
			$("#payOperationTable").datagrid("hideColumn", "cancelDesc");
		}
		
		/**
		* 送出
		*/
		function payOptionSend() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			payCancle();
			//獲取選中行
			var rows = $('#dataGrid').datagrid('getSelections');
			var peripheralSuppliesList = [];
			var dataStatusList = [];
			var obj = new Object();
			var isHaveItem = false;
			if (rows.length >0) {
				//獲取所選行的狀態以及求償項目ID
				dataStatusList.push(rows[0].status);
				for (var i = 0; i < rows.length; i++) {
					 if (dataStatusList.contains(rows[i].status) || dataStatusList.length == 0) {
						obj = new Object();
						obj.itemId = rows[i].itemId;
						obj.itemName = rows[i].itemName;
						obj.status = rows[i].status;
						obj.paymentId = rows[i].paymentId;
						obj.updatedDate = rows[i].updatedDate;
						obj.paymentReason = rows[i].paymentReason;
						peripheralSuppliesList.push(obj);
					} else {
						$("#paymentItemMsg").html("請選擇同一資料狀態的求償資料");
						return false;
					}
					if ((rows[i].isPay == "Y" && rows[i].paymentItem == "<%=IAtomsConstants.PAYMENT_ITEM_ASSET %>")
							|| rows[i].paymentItem == "<%=IAtomsConstants.PAYMENT_ITEM_SUPPLIES %>") {
						isHaveItem = true;
					}
				}
				if (!isHaveItem) {
					$("#paymentItemMsg").html("請至少設定一筆待求償資料");
					return false;
				}
				//獲取當前用戶所具有的角色
				var roles = [];
				<c:forEach var="roleUser" items="${roleUsers}">
					roles.push("${roleUser}");
				</c:forEach>
				//判斷如果狀態是新增或者退回狀態時
				if (dataStatusList[0] == '${backtatus}' || dataStatusList[0] == '${createStatus}') {
					//如果當前登錄者角色包含客服角色
					if (roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>")) {
						$.messager.confirm('確認對話框','確認送出?', function(confirm) {
							if (confirm) {
								var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
								$.blockUI(blockStyle1);
								var obj = getSelectItemIds();
								ajaxService.getPaymentItemByItemIds(obj.itemId, JSON.stringify(obj.updatedDate), function(result){
									if (result != null) {
										$.ajax({
											url: "${contextPath}/payment.do?actionId=send",
											data: {<%=PaymentFormDTO.PARAM_SEND_SUPPLIES_LIST%> : JSON.stringify(peripheralSuppliesList)}, 
											type:'post', 
											cache:false, 
											dataType:'json', 
											success:function(json){
												if (json.success) {
													var pageIndex = getGridCurrentPagerIndex("dataGrid");
													if (pageIndex < 1) {
														pageIndex = 1;
													}
													queryClaimInfo(pageIndex, false);
													payCancle();
													$("#paymentItemMsg").html(json.msg);
												}else{
													$("#paymentItemMsg").html(json.msg);
												}
												// 去除遮罩
												$.unblockUI();
											},
											error:function(){
												$("#paymentItemMsg").html("送出失敗");
												// 去除遮罩
												$.unblockUI();
											}
										}); 
									} else {
										// 去除遮罩
										$.unblockUI();
										$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
									}
									operation = "";
								});
							}
						});
					} else {
						if (roles.contains("<%=IAtomsConstants.WAREHOUSE_KEEPER%>")) {
							$("#paymentItemMsg").html("資料狀態不為待維修，不可進行送出");
						} else {
							$("#paymentItemMsg").html("資料狀態不為待確認金額，不可進行送出");
						}
						return false;
					}
					//如果資料狀態為待維修，則判斷是否具有倉管角色。
				} else if (dataStatusList[0] == '<%=IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED%>') {
					if (roles.contains("<%=IAtomsConstants.WAREHOUSE_KEEPER%>")) {
						var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
						$.blockUI(blockStyle1);
						var obj = getSelectItemIds();
						ajaxService.getPaymentItemByItemIds(obj.itemId, JSON.stringify(obj.updatedDate), function(result){
							if (result != null) {
								$('#payOperation').panel('open');
								showColumns('send', false);
								$("#payOperationTable").datagrid("loadData", {total:result.length, rows:result});
								 $('.payText').textbox({
									required:true,
									missingMessage:'請輸入檢測結果',
								}); 
								textBoxsDefaultSetting($('.payText'));
							} else {
								$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
							}
							operation = "";
							// 去除遮罩
							$.unblockUI();
						});
					} else {
						if (roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>")) {
							$("#paymentItemMsg").html("資料狀態不為新增或退回，不可進行送出");
						} else {
							$("#paymentItemMsg").html("資料狀態不為待確認金額，不可進行送出");
						}
						return false;
					}
				} else if (dataStatusList[0] == '<%=IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED%>') {
					//如果資料狀態為代確認金額
					//判斷當前登錄者角色是否包含帳務
					if (roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
						var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
						$.blockUI(blockStyle1);
						var obj = getSelectItemIds();
						ajaxService.getPaymentItemByItemIds(obj.itemId, JSON.stringify(obj.updatedDate), function(result){
							if (result != null) {
								$('#payOperation').panel('open');
								showColumns('send', true);
								$("#payOperationTable").datagrid("loadData", {total:result.length, rows:result});
								for (var i = 0; i < result.length; i++) {
									$('#fee' + result[i].itemId).textbox({
										validType:'numberNonnegative[\'請輸入0或正整數\']',
									});
									textBoxDefaultSetting($('#fee' + result[i].itemId));
								}
							} else {
								$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
							}
							operation = "";
							// 去除遮罩
							$.unblockUI();
						});
					} else {
						if (roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>")) {
							$("#paymentItemMsg").html("資料狀態不為新增或退回，不可進行送出");
						} else {
							$("#paymentItemMsg").html("資料狀態不為待維修，不可進行送出");
						}
						return false;
					}
				} else {
					if (roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>")) {
						$("#paymentItemMsg").html("資料狀態不為新增或退回，不可進行送出");
						return false;
					} else if (roles.contains("<%=IAtomsConstants.WAREHOUSE_KEEPER%>")){
						$("#paymentItemMsg").html("資料狀態不為待維修，不可進行送出");
						return false;
					} else {
						$("#paymentItemMsg").html("資料狀態不為待確認金額，不可進行送出");
						return false;
					}
				}
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
			}
		}
		/**
		* 鎖定
		*/
		function payOptionLock() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			payCancle();
			//獲取選中行
			var rows = $('#dataGrid').datagrid('getSelections');
			if (rows.length >0) {
				//獲取所選行的狀態以及求償項目ID
				if (checkPayStatus("<%=IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM%>", "資料狀態不為待償確認，不可進行鎖定")) {
					//獲取當前用戶所具有的角色
					var roles = [];
					<c:forEach var="roleUser" items="${roleUsers}">
						roles.push("${roleUser}");
					</c:forEach>
					if (!roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
						$("#paymentItemMsg").html("當前登錄者不為帳務，不可進行鎖定");
						return false;
					}
					var obj = getSelectItemIds();
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle1);
					ajaxService.getPaymentItemByItemIds(obj.itemId, JSON.stringify(obj.updatedDate), function(result){
						if (result != null) {
							$('#payOperation').panel('open');
							showColumns('lock', false);
							$("#payOperationTable").datagrid("loadData", {total:result.length, rows:result});
							$(".payType").combobox({
								required:true,
								validType:'ignore[\'請選擇\']',
								invalidMessage:'請輸入求償方式',
								editable:false,
							});
						} else {
							$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
						}
						operation = "";
						// 去除遮罩
						$.unblockUI();
					});
				}
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
			}
		}
		/**
		* 退回
		*/
		function payOptionBack() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			payCancle();
			//獲取選中行
			var rows = $('#dataGrid').datagrid('getSelections');
			if (rows.length >0) {
				//獲取所選行的狀態以及求償項目ID
				var roles = [];
				<c:forEach var="roleUser" items="${roleUsers}">
					roles.push("${roleUser}");
				</c:forEach>
				var dataStatusList = [];
				dataStatusList.push(rows[0].status);
				//核檢資料狀態是否為同一狀態
				for (var i = 0; i < rows.length; i++) {
					if (!dataStatusList.contains(rows[i].status)) {
						$("#paymentItemMsg").html("請選擇同一資料狀態的求償資料");
						return false;
					}
				}
				//如果資料狀態爲不可退回的狀態且當前登陸者角色包含客服
				if ((rows[0].status == "<%=IAtomsConstants.DATA_STATUS_CREATE%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_BACK%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_REQUEST_FUNDS%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_ALREADY_CANCEL%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_ALREADY_REPAYMENT%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_COMPLETE%>") && roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>")) {
					$("#paymentItemMsg").html("資料狀態不符，不可進行退回操作");
					return false;
				//如果資料狀態是待維修
				} else if (rows[0].status == "<%=IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED%>") {
					//如果當前登錄者不包含倉管角色
					if (!roles.contains("<%=IAtomsConstants.WAREHOUSE_KEEPER%>")) {
						if (roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>") && !roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
							$("#paymentItemMsg").html("當前登陸者爲客服，不可進行退回操作");
							return false;
						}
						$("#paymentItemMsg").html("資料狀態不為待確認金額/待償確認/求償確認，不可進行退回");
						return false;
					}
				//如果資料狀態是待確認金額、代償確認、求償確認
				} else if (rows[0].status == "<%=IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM%>"
						|| rows[0].status == "<%=IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM%>") {
					//如果當前登錄者不包含帳務角色
					if (!roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
						if (roles.contains("<%=IAtomsConstants.CUSTOMER_SERVICE%>") && !roles.contains("<%=IAtomsConstants.WAREHOUSE_KEEPER%>")) { 
							$("#paymentItemMsg").html("當前登陸者爲客服，不可進行退回操作");
							return false;
						}
						$("#paymentItemMsg").html("資料狀態不為待維修，不可進行退回");
						return false;
					}
				} else {
					//當前登錄這包倉管管角色
					if (roles.contains("<%=IAtomsConstants.WAREHOUSE_KEEPER%>")) {
						$("#paymentItemMsg").html("資料狀態不為待維修，不可進行退回");
						return false;
					//當前登錄這包帳務管角色
					} else if (roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
						$("#paymentItemMsg").html("資料狀態不為待確認金額/待償確認/求償確認，不可進行退回");
						return false;
					} else {
						$("#paymentItemMsg").html("當前登陸者爲客服，不可進行退回操作");
						return false;
					}
				}
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				var obj = getSelectItemIds();
				ajaxService.getPaymentItemByItemIds(obj.itemId, JSON.stringify(obj.updatedDate), function(result){
					if (result != null) {
						$('#payOperation').panel('open');
						showColumns('back', false);
						$("#payOperationTable").datagrid("loadData", {total:result.length, rows:result});
						operation = "<%=IAtomsConstants.PAY_ACTION.BACK.getCode()%>";
					} else {
						$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
					}
					// 去除遮罩
					$.unblockUI();
				});
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
			}
		}
		/**
		* 完成
		*/
		function payOptionComplete() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			/* var obj;
			var array = []; */
			payCancle();
			//獲取選中行
			var rows = $('#dataGrid').datagrid('getSelections');
			if (rows.length >0) {
				//獲取所選行的狀態以及求償項目ID
				if (checkPayStatus("<%=IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM%>", "資料狀態不為求償確認，不可進行完成")) {
					//獲取當前用戶所具有的角色
					var roles = [];
					<c:forEach var="roleUser" items="${roleUsers}">
						roles.push("${roleUser}");
					</c:forEach>
					if (!roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
						$("#paymentItemMsg").html("當前登錄者不為帳務，不可進行完成");
						return false;
					}
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle1);
					var obj = getSelectItemIds();
					ajaxService.getPaymentItemByItemIds(obj.itemId, JSON.stringify(obj.updatedDate), function(result){
						if (result != null) {
							$('#payOperation').panel('open');
							showColumns('complete', false);
							$("#payOperationTable").datagrid("loadData", {total:result.length, rows:result});
							$(".date").datebox({
								validType:'date',
							});
							dateBoxsDefaultSetting($('.date'));
						} else {
							$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
						}
						operation = "";
						// 去除遮罩
						$.unblockUI();
					});
				}
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
			}
		}
		/**
		* 還款
		*/
		function payOptionReturn() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			payCancle();
			//獲取選中行
			var rows = $('#dataGrid').datagrid('getSelections');
			var itemId = "";
			var dataStatusList = [];
			if (rows.length >0) {
				var paymentId = "";
				var isRefund = false;
				var paymentIds = [];
				var paymentAllIds = [];
				var obj;
				var array = [];
				//獲取所選行的狀態以及求償項目ID
				for (var i = 0; i < rows.length; i++) {
					if (i == 0) {
						paymentId = rows[i].paymentId;
						paymentAllIds.push(paymentId);
					} else {
						if (paymentId != rows[i].paymentId) {
							paymentId = rows[i].paymentId;
							paymentAllIds.push(paymentId);
						}
					}
					if (rows[i].status == "<%=IAtomsConstants.DATA_STATUS_REQUEST_FUNDS%>") {
						obj = new Object();
						obj.updatedDate = rows[i].updatedDate;
						obj.itemId = rows[i].itemId;
						array.push(obj);
						if (!paymentIds.contains(paymentId)) {
							paymentIds.push(paymentId);
						}
						itemId += "<%=IAtomsConstants.MARK_QUOTES%>" 
							+ rows[i].itemId 
							+ "<%=IAtomsConstants.MARK_QUOTES%>" 
							+ "<%=IAtomsConstants.MARK_SEPARATOR%>";
						isRefund = true;
					}	
				}
				if (isRefund && paymentIds.length!=paymentAllIds.length) {
					$("#paymentItemMsg").html("請選擇同一資料狀態的求償資料");
					return false;
				}
				if (!isRefund) {
					$("#paymentItemMsg").html("資料狀態不為已完成，不可進行還款");
					return false;
				}
				var roles = [];
				<c:forEach var="roleUser" items="${roleUsers}">
					roles.push("${roleUser}");
				</c:forEach>
				if (!roles.contains("<%=IAtomsConstants.ACCOUNTING%>")) {
					$("#paymentItemMsg").html("當前登錄者不為帳務，不可進行還款");
					return false;
				}
				itemId = itemId.substring(0, itemId.length-1);
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				ajaxService.getPaymentItemByItemIds(itemId, JSON.stringify(array), function(result){
					if (result != null) {
						$('#payOperation').panel('open');
						showColumns('refund', false);
						$("#payOperationTable").datagrid("loadData", {total:result.length, rows:result});
						$(".cancleDate").datebox({
							validType:'date',
							required:true,
							missingMessage:'請輸入取消時間',
						});
						$(".repayDate").datebox({
							validType:'date',
							required:true,
							missingMessage:'請輸入還款時間',
						});
						dateBoxsDefaultSetting($('.cancleDate'));
						dateBoxsDefaultSetting($('.repayDate'));
					} else {
						$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
					}
					operation = "";
					// 去除遮罩
					$.unblockUI();
				});
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
			}
		}
		
		/**
		* 獲取當前所選擇行的求償批號
		*/
		function getSelectItemIds() {
			var itemId = "";
			var obj;
			var array = [];
			var rows = $('#dataGrid').datagrid('getSelections');
			for (var i=0; i<rows.length; i++) {
				obj = new Object();
				obj.itemId = rows[i].itemId;
				obj.updatedDate = rows[i].updatedDate;
				array.push(obj);
				itemId += "<%=IAtomsConstants.MARK_QUOTES%>" 
					+ rows[i].itemId 
					+ "<%=IAtomsConstants.MARK_QUOTES%>" 
					+ "<%=IAtomsConstants.MARK_SEPARATOR%>";
			}
			obj = new Object();
			obj.itemId = itemId.substring(0, itemId.length-1);
			obj.updatedDate = array;
			return obj;
		}
		
		/**
		* 核檢所選擇的資料狀態是否唯一
		*/
		function checkPayStatus(status, message) {
			var dataStatusList = [];
			var rows = $('#dataGrid').datagrid('getSelections');
			dataStatusList.push(rows[0].status);
			//核檢資料狀態是否為同一狀態
			for (var i = 0; i < rows.length; i++) {
				if (!dataStatusList.contains(rows[i].status)) {
					$("#paymentItemMsg").html("請選擇同一資料狀態的求償資料");
					return false;
				}
			}
			//判斷狀態是否為代償確認。
			if (rows[0].status != status) {
				$("#paymentItemMsg").html(message);
				return false;
			}
			return true;
		}
		
		/**
		* 取消操作
		*/
		function payCancle() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			$("#payOperationTable").datagrid("loadData", {total:0, rows:[]});
			$('#payOperation').panel('close');
		}
		
		/**
		* 保存求償操作的數據
		*/
		function savePayOperation() {
			$("#claimMsg").html("");
			$("#paymentItemMsg").html("");
			var rows = $("#payOperationTable").datagrid("getRows");
			if (rows) {
				var controls = [];
				var actionId = "";
				var status = rows[0].status;
				var dataList = [];
				var obj = new Object();
				if (status == '<%=IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM%>' && operation == "") {
					for (var i = 0; i<rows.length; i++) {
						controls.push('type'+rows[i].itemId);
					}
					if (!validateForm(controls)) {
						return false;
					} else {
						$("div").unbind("scroll.validate");
					}
				} else {
					if (status == '<%=IAtomsConstants.DATA_STATUS_REQUEST_FUNDS%>') {
						var isHaveValue = false;
						for (var i = 0; i<rows.length; i++) {
							var cancleDate = $("#cancleDate"+rows[i].itemId).datebox("getValue");
							var repaymentDate = $("#repaymentDate"+rows[i].itemId).datebox("getValue");
							if (cancleDate != "" || repaymentDate != "") {
								if (cancleDate != "" && repaymentDate == "") {
									$("#repaymentDate"+rows[i].itemId).datebox("textbox").focus();
									return false;
								} else if (cancleDate == "" && repaymentDate != ""){
									$("#cancleDate"+rows[i].itemId).datebox("textbox").focus();
									return false;
								} else {
									isHaveValue = true;
								}
							}
						}
						if (!isHaveValue) {
							$("#paymentItemMsg").html("請至少填寫一筆還款資料");
							return false;
						}
					} else {
						if (!$("#payOperationForm").form("validate")) {
							return false;
						}
					}
				}
				
				
				if (status == '<%=IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED%>' && operation == "") {
					actionId = "send";
					for (var i = 0; i<rows.length; i++) {
						rows[i].checkResult = $("#result"+rows[i].itemId).textbox("getValue");
						rows[i].checkUser = $("#user"+rows[i].itemId).val();
						rows[i].askPayDate = null;
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED%>' && operation == ""){
					actionId = "send";
					for (var i = 0; i<rows.length; i++) {
						rows[i].fee = $("#fee"+rows[i].itemId).val();
						rows[i].feeDesc = $("#feeDesc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM%>' && operation == "") {
					actionId = "lock";
					var paymentType = null;
					for (var i = 0; i<rows.length; i++) {
						paymentType = $("#type"+rows[i].itemId).combobox("getValue");
						if ($("#check"+rows[i].itemId).is(":checked")) {
							if (paymentType == "${selfAbsorb}" || paymentType == "${detectionNormal}") {
								$("#paymentItemMsg").html("若需請款，求償方式不可為自行吸收或檢測正常");
								return false;
							}
						} else {
							if (paymentType != "${selfAbsorb}" && paymentType != "${detectionNormal}") {
								$("#paymentItemMsg").html("若不需請款，求償方式只可為自行吸收或檢測正常");
								return false;
							}
						}
						rows[i].isAskPay = $("#check"+rows[i].itemId).is(":checked")?'Y':'N';
						rows[i].paymentType = $("#type"+rows[i].itemId).combobox("getValue");
						rows[i].paymentTypeName = $("#type"+rows[i].itemId).combobox("getText");
						rows[i].paymentTypeDesc = $("#desc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_WAIT_CLAIM_CONFIRM%>' && operation == "<%=IAtomsConstants.PAY_ACTION.BACK.getCode()%>"){
					actionId = "back";
					for (var i = 0; i<rows.length; i++) {
						rows[i].backDesc = $("#backDesc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_TO_BE_MAINTAINED%>' && operation == "<%=IAtomsConstants.PAY_ACTION.BACK.getCode()%>") {
					actionId = "back";
					for (var i = 0; i<rows.length; i++) {
						obj = new Object();
						rows[i].backDesc = $("#backDesc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_AMOUNT_TO_BE_CONFIRMED%>' && operation == "<%=IAtomsConstants.PAY_ACTION.BACK.getCode()%>") {
					actionId = "back";
					for (var i = 0; i<rows.length; i++) {
						rows[i].backDesc = $("#backDesc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM%>' && operation == "<%=IAtomsConstants.PAY_ACTION.BACK.getCode()%>") {
					actionId = "back";
					for (var i = 0; i<rows.length; i++) {
						rows[i].backDesc = $("#backDesc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_CLAIM_CONFIRM%>') {
					actionId = "complete";
					for (var i = 0; i<rows.length; i++) {
						var date = new Date($("#askDate"+rows[i].itemId).datebox("getValue"));
						rows[i].askPayDesc = $("#askDesc"+rows[i].itemId).val();
						rows[i].askPayDate = date.getTime();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
					}
				} else if (status == '<%=IAtomsConstants.DATA_STATUS_REQUEST_FUNDS%>') {
					actionId = "repayment";
					for (var i = 0; i<rows.length; i++) {
						var date = new Date($("#cancleDate"+rows[i].itemId).datebox("getValue"));
						rows[i].cancelDate = date == ""?null:date.getTime();
						date = new Date($("#repaymentDate"+rows[i].itemId).datebox("getValue"));
						rows[i].repaymentDate = date == ""?null:date.getTime();
						rows[i].cancelDesc = $("#cancelDesc"+rows[i].itemId).val();
						rows[i].realFinishDate = null;
						rows[i].updatedDate = null;
						rows[i].askPayDate = null;
					}
				}
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				$.ajax({
					url: "${contextPath}/payment.do?actionId="+actionId,
					data: {<%=PaymentFormDTO.PARAM_SEND_SUPPLIES_LIST%> : JSON.stringify(rows)}, 
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(json){
						if (json.success) {
							var pageIndex = getGridCurrentPagerIndex("dataGrid");
							if (pageIndex < 1) {
								pageIndex = 1;
							}
							queryClaimInfo(pageIndex, false);
							payCancle();
							operation = "";
							if (json.cmsResult != undefined && !json.cmsResult) {
								$.messager.alert('提示訊息',json.msg,'');
							} else {
								$("#paymentItemMsg").html(json.msg);
							}
						}else{
							$("#paymentItemMsg").html(json.msg);
						}
						$.unblockUI();
					},
					error:function(){
						$("#paymentItemMsg").html("保存失敗");
						$.unblockUI();
					}
				});
			}
		}
		/**
		 * 顯示文本框
		 */
		function showText(itemId, length, isRequired, isAskPay){
			if (isRequired) {
				return "<input class='easyui-textbox payText' id="+itemId+" style='width:100%;border-radius: 4px; border: #95B8E7 1px solid;' maxlength='"+length+"'/>";
			} else {
				if (isAskPay == 'Y' || isAskPay == "") {
					return "<input class='easyui-textbox' id="+itemId+" style='width:100%;border-radius: 4px; border: #95B8E7 1px solid;' maxlength='"+length+"'/>";
				} else {
					return "<input class='easyui-textbox' id="+itemId+" style='width:100%;border-radius: 4px; border: #95B8E7 1px solid;' maxlength='"+length+"' disabled='disabled'/>";
				}
			}
			
		}
		/**
		* 初始化表格時，顯示下拉列表
		*/
		function showCombobox(id, isRequired) {
			var html = "";
			if (isRequired) {
				html = " <select class=\"easyui-combobox payType\" id='"+id+"' style='border-radius: 4px; border: #95B8E7 1px solid;width: 100%'>";
			} else {
				html = " <select class=\"easyui-combobox\" id='"+id+"' style='border-radius: 4px; border: #95B8E7 1px solid;width: 100px'>";
			}
			html += "<option value=\"\">請選擇</option>"
			<c:forEach var="type" items="${compensationMethod}">
				html += "<option value=\"${type.value}\">${type.name}</option>"
			</c:forEach>
			html += "</select>";
			return html;
		}
		/**
		* 初始化表格時，顯示複選框
		*/
		function showCheckbox(id ,isDisable, value) {
			if (value == '<%=IAtomsConstants.YES%>' && isDisable) {
				return "<input type=\"checkbox\" checked disabled/>";
			} else if (value == '<%=IAtomsConstants.NO%>' && isDisable){
				return "<input type=\"checkbox\" disabled/>";
			} else if(isDisable){
				return "<input type=\"checkbox\" disabled/>";
			} else {
				return "<input type=\"checkbox\" id='"+id+"'/>";
			}
		}
		/**
		* 初始化表格時，顯示時間框
		*/
		function showDatebox(id, isRequired, dateClass, isAskPay) {
			if (isRequired) {
				return "<input class=\"easyui-datebox "+dateClass+"\" maxlength='10' style='width:100%' id='"+id+"'/>";
			} else {
				if (isAskPay == 'Y' || isAskPay == "") {
					return "<input class=\"easyui-datebox date\" maxlength='10' style='width:100%' id='"+id+"'/>";
				} else {
					return "<input class=\"easyui-datebox date\" maxlength='10' style='width:100%' id='"+id+"' disabled='disabled' />";
				}
				
			}
			
		}
		/**
		* 刪除資料
		*/
		function delPaymentInfo() {
			$("#claimMsg").html("");
			var row = $('#dataGrid').datagrid('getSelections');
			if(row.length >0) {
				var paymentId = row[0].paymentId;
				for (var i=0;i<row.length;i++) {
					if (row[i].status != '${backtatus}' && row[i].status != '${createStatus}') {
						$("#paymentItemMsg").html("求償資料之資料狀態為新增或退回，才可刪除");
						return false;
					}
				}
				var url = "${contextPath}/payment.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>";
				var params = {paymentId: paymentId};
				var successFunction = function(json){
					if (json.success) {
						var pageIndex = getGridCurrentPagerIndex("dataGrid");
						if (pageIndex < 1) {
							pageIndex = 1;
						}
						queryClaimInfo(pageIndex, false);
						payCancle();
						$("#paymentItemMsg").html(json.msg);
					}else{
						$("#paymentItemMsg").html(json.msg);
					}
				};
				var errorFunction = function(){
					$.messager.alert('提示', "刪除失敗,請聯繫管理員.", 'error');	
				};
				commonDelete(params,url,successFunction,errorFunction);
			} else {
				$.messager.alert('提示訊息','請勾選資料','warning');
			}
		}
		//匯出
		function exportData(){
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			var useCaseNo = '<%=IAtomsConstants.UC_NO_SRM_05040%>';
			actionClicked(document.forms["queryPaymentForm"],
				useCaseNo,
				'',
				'export');
			
			ajaxService.getExportFlag(useCaseNo,function(data){
				$.unblockUI();
			});
		}
		/**
		* 核檢當前是否有選中行，如果沒有選中行，則刪除展開的gatagrid
		*/
		function onUncheck(index, row) {
			setTimeout(function () {
				var rows = $("#dataGrid").datagrid("getSelections");
				if (rows.length == 0) {
					var rowsLength = $('#payOperationTable').datagrid("getRows").length;
					if (rowsLength != 0) {
						payCancle();
					}
				}
				
			}, 0);
		}
		/**
		* 
		*/
		function checkSign(index, row){
			var rowsLength = $('#payOperationTable').datagrid("getRows").length;
			if (rowsLength != 0) {
				var dataStatusList = [];
				var rows = $('#dataGrid').datagrid('getSelections');
				dataStatusList.push(rows[0].status);
				//核檢資料狀態是否為同一狀態
				for (var i = 0; i < rows.length; i++) {
					if (!dataStatusList.contains(rows[i].status)) {
						$("#paymentItemMsg").html("請選擇同一資料狀態的求償資料");
						var rowSpan = row.dataSpan;
						var rowFirst = row.isStartRow;
						if (rowSpan && rowSpan > 1) {
							if (rowFirst) {
								for(var i = index + 1; i < rowSpan + index; i ++) {
									$("#dataGrid").datagrid("uncheckRow", i);
								}
							} else {
								for(var i = index - 1; i > index - rowSpan; i --) {
									var row = $("#dataGrid").datagrid("getRows")[i];
									if (row.isStartRow) {
										var currentIndex = i;
										var selections = $("#dataGrid").datagrid("getSelections");
										var isChecked = false;
										for (var j = 0; j < selections.length; j ++) {
											var checkIndex = $("#dataGrid").datagrid("getRowIndex",selections[j]);
											if (currentIndex == checkIndex) {
												isChecked = true;
												break;
											}
										}
										if(isChecked) {
											$("#dataGrid").datagrid("uncheckRow", i);
										}
										break;
									}							
								}
							}
						}
						return false;
					}
				}
				
			}
		}
		function initCaseId(value, row) {
			var html = "<a href='javascript:void(0)' onclick=\"showCaseInfo('";
			html += value;
			html += "');\">";
			html += value;
			html += "</a>";
			return html;
		}
		function showCaseInfo(caseId){
	    	   viewDlgShow = $('#showCaseInfo').dialog({
	   			title : '案件記錄',
	   			width : 900,
	   			height : 500,
	   			modal : true,
	   			method:'post',
	   			closed : false,
	   			cache : false,
	   			queryParams : {
	   				actionId : "showDetailInfo",
	   				caseId : caseId,
	   				isHistory : 'N'
	   			},
	   			onLoad :function(){
	   				if(typeof queryCaseRecordShow != 'undefined' && queryCaseRecordShow instanceof Function){
	   					queryCaseRecordShow();
	   				}
	   			},
	   			onClose : function(){
	   				if(viewDlgShow){
	   					viewDlgShow.dialog('clear');
	   				}
	   			},
	   			href : "${contextPath}/caseHandle.do",
	   			modal : true
	   		});
	       }
	</script>
</body>
</html>
