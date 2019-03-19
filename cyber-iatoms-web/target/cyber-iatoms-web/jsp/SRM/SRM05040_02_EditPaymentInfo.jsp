<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.PaymentFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	PaymentFormDTO formDTO = null;
	SrmPaymentInfoDTO srmPaymentInfoDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (PaymentFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null ) {
		formDTO = new PaymentFormDTO();
		srmPaymentInfoDTO = new SrmPaymentInfoDTO();
		ucNo = IAtomsConstants.UC_NO_SRM_05040;
	} else {
		ucNo = formDTO.getUseCaseNo();
		srmPaymentInfoDTO = formDTO.getPaymentInfoDTO();
	}
	//客戶下拉列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, PaymentFormDTO.PARAM_COMPANY_LIST);
	//耗材分類列表
	String suppliesTypes = (String) SessionHelper.getAttribute(request, ucNo, PaymentFormDTO.PARAM_SUPPLIES_TYPE_STRING);
	//求償資訊
	List<Parameter> paymentReason = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.PAYMENT_REASON.getCode());
	//求償資訊下拉列表---資行輸入
	String selfInput = IAtomsConstants.PAYMENT_REASON.SELF_INPUT.getCode();
%>
<html>
<c:set var="paymentReason" value="<%=paymentReason%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="suppliesTypes" value="<%=suppliesTypes%>" scope="page"></c:set>
<c:set var="payPrefix" value="editPay" scope="page"></c:set>
<c:set var="srmPaymentInfoDTO" value="<%=srmPaymentInfoDTO%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="selfInput" value="<%=selfInput%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
	<div id="paymentId" data-options="region:'center',fit:true" style="width: auto; height: auto; padding: 10px 20px;  overflow-y: hidden" class="topSoller">
		<div class="dialogtitle">求償資料</div>
		<form id="fm" method="post" data-options="" class="easyui-form">
				<div style="font-size: 16px;color: red">
					<span id="addPayInfoMsg" class="red"></span>
				</div>
			<input type="hidden" id="paymentId" name="paymentId" value="<c:out value='${srmPaymentInfoDTO.paymentId }'/>"/>
			<input type="hidden" id="assetTableValue" name="assetTableValue"/>
			<input type="hidden" id="peripheralSuppliesTableValue" name="peripheralSuppliesTableValue"/>
			<table cellpadding="5">
				<tr>
					<td width="15%">
						<label>客戶:<lable class="red">*</lable> </label>
					</td>
					<td style="width: 300px">
						<cafe:droplisttag
							id="customerId"
							name="customerId" 
							result="${companyList }"
							css="easyui-combobox"
							hasBlankValue="true"
							blankName="請選擇"
							selectedValue="${srmPaymentInfoDTO.customerId }"
							style="width: 200px"
							javascript="validType='requiredDropList' editable=false required=true  invalidMessage=\"請輸入客戶\"
								${formDTO.actionId ne 'initEdit'?'':'disabled=true' }"
						></cafe:droplisttag>
					</td>
					<td width="15%">
						<label>需求單號:</label>
					</td>  
					<td>
						<input name="show_requirementNo" id="show_requirementNo" style="width:200px" value="<c:out value='${srmPaymentInfoDTO.requirementNo }'/>" class="easyui-textbox" disabled="disabled">
						<input name="requirementNo" id="requirementNo" value="<c:out value='${srmPaymentInfoDTO.requirementNo }'/>" type="hidden"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>DTID:<lable class="red">*</lable></label>
					</td>
					<td>
						<input name="dtid" id="dtid" value="<c:out value='${srmPaymentInfoDTO.dtid }'/>" maxlength="8px" ${formDTO.actionId ne 'initEdit'?'':'disabled' } class="easyui-textbox" required="true" missingMessage="請輸入DTID">
						<input type="hidden" name="hiddenDtid" id="hiddenDtid" value="<c:out value='${srmPaymentInfoDTO.dtid }'/>"/>
						<c:if test="${formDTO.actionId ne 'initEdit' }">
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="queryDTIDInfoPay(true);"></a>
							<a href="javascript:void(0)" value="${srmPaymentInfoDTO.dtid }" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="queryDTIDInfoPay(false);">查詢</a>
						</c:if>
						
					</td>
					<td>
						<label>TID:</label>
					</td>
					<td>
						<input name="show_tid" id="show_tid" style="width:200px" value="<c:out value='${srmPaymentInfoDTO.tid }'/>" class="easyui-textbox" disabled="disabled">
						<input name="tid" id="tid" value="<c:out value='${srmPaymentInfoDTO.tid }'/>" type="hidden">
					</td>
				</tr>
				<tr>
					<td>
						<label>案件編號:</label>
					</td>
					<td>
						<input name="show_caseId" id="show_caseId" style="width:200px" value="<c:out value='${srmPaymentInfoDTO.caseId }'/>" class="easyui-textbox" disabled="disabled">
						<input name="caseId" id="caseId" value="<c:out value='${srmPaymentInfoDTO.caseId }'/>" type="hidden">
					</td>
					<td>
						<label>特店代號:</label>
					</td>
					<td>
						<input name="show_merchantCode" id="show_merchantCode" style="width:200px" value="<c:out value='${srmPaymentInfoDTO.merchantCode }'/>" class="easyui-textbox" disabled="disabled">
						<input name="merchantId" id="merchantId" value="<c:out value='${srmPaymentInfoDTO.merchantCode }'/>" type="hidden"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>特店名稱:</label>
					</td>
					<td>
						<input name="show_name" id="show_name" style="width:200px" value="<c:out value='${srmPaymentInfoDTO.name }'/>" class="easyui-textbox" maxlength='10' disabled="disabled"></input>
					</td>
					<td>
						<label>通報遺失日:</label>
					</td>
					<td>
						<input name="show_realFinishDate" id="show_realFinishDate" style="width:200px" value="${srmPaymentInfoDTO.realFinishDate }" class="easyui-datebox" maxlength='10' data-options="validType:['date[\'通報遺失日格式限YYYY/MM/DD\']']" disabled="disabled"></input>
						<input name="realFinishDate" id="realFinishDate" value="<c:out value='${srmPaymentInfoDTO.realFinishDate }'/>" type="hidden"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>進件時間:</label>
					</td>
					<td>
						<input name="show_caseCreatedDate" id="show_caseCreatedDate" style="width:200px" value="${srmPaymentInfoDTO.caseCreatedDate }" class="easyui-datetimebox" disabled="disabled"></input>
						<input name="caseCreatedDate" id="caseCreatedDate" value="${srmPaymentInfoDTO.caseCreatedDate }" type="hidden"/>
					</td>
					<td>
						<label>AO人員:</label>
					</td>
					<td>
						<input name="show_aoName" id="show_aoName" style="width:200px" value="<c:out value='${srmPaymentInfoDTO.aoName }'/>" class="easyui-textbox" maxlength='100' disabled="disabled"></input>
						<input name="merchantHeaderId" id="merchantHeaderId" value="<c:out value='${srmPaymentInfoDTO.merchantHeaderId }'/>" type="hidden"/>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<table id="assetTable" class="easyui-datagrid" title="設備清單" style="width: 710px;"
								data-options="iconCls: 'icon-edit',
									singleSelect:true,
									collapsible:true,
									onClickCell: onClickCellAsset,
									nowrap:false,
									data:assetList"
								>
							<thead>
								<tr>
									<th data-options="field:'itemName',width:120,halign:'center',align:'left'">設備名稱</th>
									<th data-options="field:'serialNumber',width:170,halign:'center',align:'left'">設備序號</th>
									<th data-options="field:'contractCode',width:150,halign:'center',align:'left'">合約編號</th>
									<th data-options="field:'isPay',width:80,align:'center',halign:'center',
										formatter:function(value, row, index){
											return checkboBox(value);
										}">是否求償</th>
									<th data-options="field:'payInfo',width:168,halign:'center',align:'left'
										">求償資訊</th>
									<th data-options="field:'paymentReason',hidden:true"></th>
									<th data-options="field:'reasonDetail',hidden:true"></th>
									<th data-options="field:'assetId',width:80,hidden:true">設備ID</th>
									<th data-options="field:'contractId',width:80,hidden:true">合約ID</th>
									<th data-options="field:'itemId',hidden:true"></th>
								</tr>
							</thead>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<label id="peripheralSuppliesMsg"></label>
						<table id="peripheralSuppliesTable" class="easyui-datagrid" title="週邊耗材清單" style="width: 610px;"
							data-options="iconCls: 'icon-edit',
								collapsible:true,
								singleSelect: true,
								method:'get',
								pageList:[15,30,50,100],
								pageSize:15,
								toolbar:'#tbInvitation',
								onDblClickRow: onDbClickCell,
								onEndEdit: onEndEdit,
								onClickRow:function(index, row) {
									if (editIndex >= 0 || editIndexAsset >= 0) {
										if (index != editIndex) {
											if (!endEditing() || !endEditingAsset()) {
												clearSelectRow('peripheralSuppliesTable', index);
												return false;
											}
										}
									}
									clearSelectRow('assetTable', editIndexAsset);
									$('#removeSupplies').linkbutton('enable');
								},
								nowrap:false,
								data:suppliesList">
							<thead>
								<tr>
									<th data-options="field:'suppliesType',width:120,halign:'center',align:'left',
										formatter:function(value,row){
											return row.suppliesTypeName;
										},
											editor:{
												type:'combobox',
												options:{
													editable:false,
													valueField:'value',
													textField:'name',
													editable:false,
													methord:'get',
													onChange:suppliesTypeChange,
													data:suppliesTypes,
												}
											}">耗材分類</th>
									<th data-options="field:'itemName',width:150,halign:'center',align:'left',
										formatter:function(value,row){
											return row.suppliesName;
										},
											editor:{
												type:'combobox',
												options:{
													editable:false,
													valueField:'value',
													textField:'name',
													method:'get',
													required:true,
													validType:'ignore[\'請選擇\']',
													invalidMessage:'請輸入耗材名稱'
												}
										}">耗材名稱</th>
									<th data-options="field:'suppliesAmount',width:120,halign:'center',align:'right',
											editor:{
												type:'textbox',
												options:{
													required:true,
													missingMessage:'請輸入數量',
													validType:['number[\'數量限輸入正整數，請重新輸入\']']
												}
											
											}">數量</th>
									<th data-options="field:'payInfo',width:168,halign:'center',align:'left',
										">求償資訊</th>
									<th data-options="field:'paymentReason',hidden:true"></th>
									<th data-options="field:'reasonDetail',hidden:true"></th>
									<th data-options="field:'itemId',hidden:true"></th>
								</tr>
							</thead>
						</table>
						<div id="tbInvitation" style="padding: 2px 5px;">
							<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="appendSupplies()">新增</a>
							<a href="javascript:void(0)" class="easyui-linkbutton" id="removeSupplies" data-options="iconCls:'icon-remove'" onclick="removeitSupplies()" disabled="disabled" >刪除</a>
						</div>
					</td>
				</tr>
			</table>
		</form>
		<div id="chooseDTIDPay"></div>
	</div>
	<script type="text/javascript">
	var suppliesTypes = initSelect(<%=suppliesTypes%>);
	var suppliesNames = null;
	//周邊耗材清單-當前編輯行
	var editIndex=undefined;
	//設備清單-當前編輯行
	var editIndexAsset = undefined;
	//
	var paymentInfoValue = null;
	var assetList = null;
	var suppliesList = null;
	var viewDlg = undefined;
	$(function(){
		/**
		* 客戶異動事件-獲取耗材拼名稱下拉列表
		*/
		$('#customerId').combobox({
			onChange: function(newValue){
				$("#addPayInfoMsg").html("");
				//清空所有數據
				uploadPayInfo(null);
				suppliesNames = initSelect(null);
			}
		});
		
		initPayMentInfoHtml();
		<c:if test="${formDTO.actionId eq 'initEdit'}">
			if (${empty formDTO.assetTableValue}) {
				assetList = {rows:[], total:0};
			} else {
				var assets = ${formDTO.assetTableValue};
				assetList = {rows:assets, total:assets.length};
			}
			if (${empty formDTO.peripheralSuppliesTableValue}) {
				suppliesList = {rows:[], total:0};
			} else {
				var supplies = ${formDTO.peripheralSuppliesTableValue};
				suppliesList = {rows:supplies, total:supplies.length};
			}
			var companyId = '${srmPaymentInfoDTO.customerId}';
			//獲取耗材名稱清單
			ajaxService.getSuppliesTypeNameList(companyId, function(data){
				suppliesNames = initSelect(data);
			});
		</c:if>
		<c:if test="${formDTO.actionId ne 'initEdit'}">
			suppliesList = {rows:[], total:0};
		</c:if>
	});
	/**
	* 新增、異動一筆耗材清單，獲取耗材名稱下拉列表。根據客戶以及耗材分類獲取，並去除已經選擇的數據。
	*/
	function suppliesTypeChange(newValue, oldValue) {
		var customerId = $("#customerId").combobox("getValue");
		if (customerId != "" && newValue != "") {
			ajaxService.getSuppliesTypeNameList(customerId, newValue, function(result){
				var rows = $('#peripheralSuppliesTable').datagrid('getRows');
				for (var i = 0; i < rows.length; i++) {
					if (i == editIndex) {
						//當前行不比對
						continue;
					}
					if (rows[i].suppliesType == newValue) {
						//和選擇的行值一樣, 需過濾掉當前值                        		
						for (var j = 0; j < result.length; j++) {
							if (result[j].value == rows[i].itemName) {
								result.splice(j,1);
								break;
							}
						} 
					}
				}
				var currentEditor = $('#peripheralSuppliesTable').datagrid('getEditor', {  
						index : editIndex,  
						field : 'itemName'
				});
				if (oldValue != "") {
					currentEditor.target.combobox('setValue', "");
				}
				currentEditor.target.combobox('loadData', initSelect(result));
			});
		} else {
			var currentEditor = $('#peripheralSuppliesTable').datagrid('getEditor', {  
				index : editIndex,  
				field : 'itemName'
			});
			currentEditor.target.combobox('setValue', "");
			currentEditor.target.combobox('loadData', initSelect(null));
		}
		
	}
	/**
	* 初始化求償資訊列下拉列表以及文本框
	*/
	function initPayMentInfoHtml(){
		var html = " <select class=\"easyui-combobox paymentAsset\" style='border-radius: 4px; border: #95B8E7 1px solid;width: 150px'>";
		html += "<option value=\"\">請選擇</option>"
		<c:forEach var="claimInfo" items="${paymentReason}" varStatus="claimInfos">
			html += "<option value=\"${claimInfo.value}\">${claimInfo.name}</option>"
		</c:forEach>
		html += "</select> <div style=\"display:none\"> <input class=\"easyui-textbox paymentComent\" required:true,missingMessage:'請輸入求償資訊', style='border-radius: 4px; border: #95B8E7 1px solid;width: 150px' maxlength='100'/> </div>";
		paymentInfoHtml = html;
		
	}
	/**
	* 求償資訊下拉框事件
	*/
	function initPaymentInfoCombobox(isRequired) {
		$(".paymentComent").textbox({
			required:isRequired,
			missingMessage:'請輸入求償資訊',
		});
		textBoxsDefaultSetting($('.paymentComent'));
		$(".paymentComent").textbox("disableValidation");
		$(".paymentAsset").combobox({
			editable:false,
			required:isRequired,
			validType:'ignore[\'請選擇\']',
			invalidMessage:'請輸入求償資訊',
			panelHeight:'auto',
			onChange: function(newValue, oldValue){
				var index = $(this).parents("td").parents("tr").attr("datagrid-row-index");
				var chiledFiled = $(this).parents("td").prev().attr("field");
				if (editIndexAsset != undefined || editIndex != undefined) {
					if (editIndexAsset != undefined && index != editIndexAsset) {
						if (!endEditingAsset()) {
							$(this).combobox("setValue", "");
							return;
						}
					}
					if (editIndex != undefined && index != endEditing && chiledFiled == "isPay") {
						if (!endEditing()) {
							$(this).combobox("setValue", "");
							return;
						}
					}
				}
				
				/* if (editIndexAsset == undefined) {
					if (!endEditing()) {
						$(this).combobox("setValue", "");
						return;
					}
				}
				if (editIndex == undefined) {
					if (!endEditingAsset()) {
						$(this).combobox("setValue", "");
						return;
					}
				} */
				/* if (index != editIndexAsset && editIndex != index) {
					if (!endEditingAsset() || !endEditing()) {
						$(this).combobox("setValue", "");
						return;
					}
				} */
				$(this).parents("td[field='payInfo']").children("div").children("div").children("input").textbox("setValue", "");
				if (newValue == "${selfInput}") {
					$(this).parents("td[field='payInfo']").children("div").children("div").css("display","inline-block");
					$(".paymentComent").textbox("enableValidation");
				} else {
					$(this).parents("td[field='payInfo']").children("div").children("div").css("display","none");
					$(".paymentComent").textbox("disableValidation");
				}
			}
		});
	}
	/**
	* 在點擊設備清單、周邊耗材清單時，給求償資訊列附下拉列表以及文本框。並賦值。
	*/
	function initPayMentInfoValue(tableId, index, fieldIndex) {
		//獲取當前選擇行
		var trs = $("#" + tableId).prev().find('div.datagrid-body').find('tr');
		//獲取隱藏列的值--存檔求償資訊列表的值
		var paymentInfoId = $($(trs[index].cells[fieldIndex+1]).find("div")).html();
		//獲取隱藏列的值--存檔求償資訊文本框的值
		var paymentInfo = $($(trs[index].cells[fieldIndex+2]).find("div")).html();
		//給求償資訊列附下拉列表以及文本框
		var combox = $('#'+tableId).prev().find('div.datagrid-body').find('tr').find('td[field="payInfo"]').eq(index).find(".combobox-f");
		if (combox.length == 0) {
			$($(trs[index].cells[fieldIndex]).find("div")).html(paymentInfoHtml);
		}
		//初始化求償資訊下拉列表-主要是添加樣式以及異動事件
		initPaymentInfoCombobox(true);
		var code = $($(trs[index].cells[fieldIndex]).find("div")).find("select");
		if (paymentInfoId != "") {
			code.combobox("setValue", paymentInfoId);
		}
		if (paymentInfo != "") {
			code.parents("td[field='payInfo']").children("div").children("div").children("input").textbox("setValue", paymentInfo)
			$($(trs[index].cells[fieldIndex]).find("div")).find("div").css("display","inline-block");
		}
	}
	/**
	* 在設備清單、周邊耗材清單結束編輯時，保存求償資訊選擇的數據。顯示在求償資訊列。並將值記錄在隱藏列。
	* （求償資訊列的下拉框以及文本框都是手動添加，所以在結束編輯時，無法自動為欄位賦值，所以需要手動賦值。）
	*/
	function savePaymentInfoValue(tableId, index, fieldIndex) {
		//獲取當前選擇行
		var trs = $("#" + tableId).prev().find('div.datagrid-body').find('tr');
		//獲取檔求償資訊列表的值、以及value
		var claimType = $($(trs[index].cells[fieldIndex]).find("div")).find("select").combobox("getText");
		var value = $($(trs[index].cells[fieldIndex]).find("div")).find("select").combobox("getValue");
		//獲取求償資訊文本框的值
		var comment = $($($(trs[index].cells[fieldIndex]).find("div")).find("div")).find("input").val();
		$($(trs[index].cells[fieldIndex]).find("div")).html("");
		 $('#' +tableId).datagrid('endEdit', index);
		//為隱藏於賦值
		$($(trs[index].cells[fieldIndex+1]).find("div")).html(value);
		$($(trs[index].cells[fieldIndex+2]).find("div")).html(comment);
		//
		if (value != "${selfInput}") {
			$($(trs[index].cells[fieldIndex]).find("div")).html(value == ""?"":claimType);
			$($(trs[index].cells[fieldIndex+2]).find("div")).html("");
		} else {
			if (comment != "") {
				claimType = claimType + "-" + comment;
			}
			$($(trs[index].cells[fieldIndex]).find("div")).html(claimType);
		}
	}
	/**
	* 驗證耗材的表單
	*/
	function validateSuppliesForm (index, datagridId) {
		var flag = true;
		if(index != undefined) {
			var controls = ['itemName','suppliesAmount'];
			if(!validateFormInRow(datagridId, index, controls)){
				flag = false;
				return flag;
			} else {
				var combox = $('#'+datagridId).prev().find('div.datagrid-body').find('tr').find('td[field="payInfo"]').eq(index).find(".combobox-f");
				var currentEditor = $('#'+datagridId).prev().find('div.datagrid-body').find('tr').find('td[field="payInfo"]').eq(index).find(".paymentComent");
				if(!combox.combobox('isValid')) {
					var textbox = combox.combobox('textbox');
					textbox.focus().trigger('mouseover');
					setTimeout(function () {
						$("div").bind("scroll.validate",function(event){	
							$("div").unbind("scroll.validate");
							textbox.blur();
						}); 
					}, 500);
					flag = false;
					return flag;
				}
				if(!currentEditor.textbox('isValid')) {
					var textbox = currentEditor.textbox('textbox');
					textbox.focus().trigger('mouseover');
					setTimeout(function () {
						$("div").bind("scroll.validate",function(event){	
							$("div").unbind("scroll.validate");
							textbox.blur();
						}); 
					}, 500);
					flag = false;
					return flag;
				}
				$("div").unbind("scroll.validate");
				//editIndex = undefined;
				return true;
			}
			$("div.topSoller").unbind("scroll.validate");
		}
		return flag;
	}
	/**
	* 周邊耗材清單-新增一行
	*/
	function appendSupplies(){
		if (endEditingAsset() && endEditing()) {
			clearSelectRow('assetTable', editIndexAsset);
			$('#peripheralSuppliesTable').datagrid('appendRow', { suppliesType: '' });
			var newIndex = $('#peripheralSuppliesTable').datagrid('getRows').length - 1;
			var trs = $("#peripheralSuppliesTable").prev().find('div.datagrid-body').find('tr');
			$('#peripheralSuppliesTable').datagrid('selectRow', newIndex).datagrid('beginEdit', newIndex);
			var currentEditor = $('#peripheralSuppliesTable').datagrid('getEditor', {  
				index : newIndex,  
				field : 'itemName'
			});
			editIndex = newIndex;
			currentEditor.target.combobox('loadData', initSelect(null));
			$($(trs[editIndex].cells[3]).find("div")).html(paymentInfoHtml);
			initPaymentInfoCombobox(true);
			checkTextBox(editIndex);
			$('#peripheralSuppliesTable').datagrid('selectRow', editIndex);
			$('#showEdit').scrollTop($('#showEdit')[0].scrollHeight );
			$('#removeSupplies').linkbutton('enable');
		}
	}
	/**
	* 周邊耗材清單-結束編輯
	*/
	function endEditing(){
		if (editIndex == undefined) { return true }
		if (validateSuppliesForm(editIndex, 'peripheralSuppliesTable')) {
			savePaymentInfoValue("peripheralSuppliesTable", editIndex, 3);
			editIndex = undefined;
			$('#removeSupplies').linkbutton('disable');
			return true;
		} else {
			return false;
		}
	}
	/**
	* 周邊耗材清單-雙擊變可編輯事件
	*/
	function onDbClickCell(index, field) {
		if (editIndex != index) {
			if (endEditing() && endEditingAsset()) {
				clearSelectRow('assetTable', editIndexAsset);
				$('#peripheralSuppliesTable').datagrid('selectRow', index).datagrid('beginEdit', index);
				var currentEditor = $('#peripheralSuppliesTable').datagrid('getEditor', {  
					index : index,  
					field : 'itemName'
				});
				currentEditor.target.combobox('loadData', suppliesNames);
				initPayMentInfoValue("peripheralSuppliesTable", index, 3);
				editIndex = index;
				checkTextBox(editIndex);
				$('#removeSupplies').linkbutton('enable');
			} else {
				setTimeout(function () {
					$('#peripheralSuppliesTable').datagrid('selectRow', editIndex);
				}, 0);
			}
		}
	}
	/**
	* 周邊耗材清單-結束編輯前觸發事件
	*/
	function onEndEdit(index, row) {
		var ed = $(this).datagrid('getEditor', {
			index: index,
			field: 'suppliesType'
		});
		row.suppliesTypeName = $(ed.target).combobox('getText');
		ed = $(this).datagrid('getEditor', {
			index: index,
			field: 'itemName'
		});
		row.suppliesName = $(ed.target).combobox('getText');
	}
	/**
	* 周邊耗材清單-刪除資料
	*/
	function removeitSupplies() {
		var row = $("#peripheralSuppliesTable").datagrid("getSelected");
		if (row) {
			//獲取當前選中的行號
			var rowIndex = $("#peripheralSuppliesTable").datagrid("getRowIndex",row);
			$("div").unbind("scroll.validate");
			$('#peripheralSuppliesTable').datagrid('deleteRow', rowIndex);
			editIndex = undefined;
			$('#removeSupplies').linkbutton('disable');
		} else {
			$("#peripheralSuppliesMsg").html("請勾選資料");
		}
	}
	/**
	* 設備清單-結束編輯
	*/
	function endEditingAsset(){
		$("div.topSoller").unbind("scroll.validate");
		if (editIndexAsset == undefined) { return true; }
		if (validateSuppliesForm(editIndexAsset, 'assetTable')) {
			savePaymentInfoValue("assetTable", editIndexAsset, 4);
			editIndexAsset = undefined;
			return true;
		} else {
			return false;
		}
	}
	/**
	* 設備清單-單擊可編輯事件
	*/
	function onClickCellAsset(index, field) {
		if (editIndexAsset != index) {
			if (endEditing() && endEditingAsset()) {
				$('#removeSupplies').linkbutton('disable');
				clearSelectRow('peripheralSuppliesTable', editIndex);
				$('#assetTable').datagrid('selectRow', index).datagrid('beginEdit', index);
				initPayMentInfoValue("assetTable", index, 4);
				var trs = $("#assetTable").prev().find('div.datagrid-body').find('tr');
				var checkbox = $($(trs[index].cells[3]).find("div")).find("input");
				//未選擇是否求償，則求償資訊下拉列表不必輸
				if (!checkbox.is(":checked")) {
					checkbox.parents("td").next().children("div").children("select").combobox("disableValidation");
				}
				editIndexAsset = index;
			} else {
				setTimeout(function () {
					$('#assetTable').datagrid('selectRow', editIndexAsset);
				}, 0);
			}
		}
	}
	/**
	* 清除已選擇的行
	*/
	function clearSelectRow(tableId, rowIndex) {
		$('#' + tableId).datagrid('clearSelections', rowIndex);
	}
	/**
	* 查詢DTID
	*/
	function queryDTIDInfoPay(obj) {
		$("#addPayInfoMsg").html("");
		var controlsQueryDtid = ['customerId'];
		if (validateForm(controlsQueryDtid)) {
			
			var customerId = $("#customerId").combobox("getValue");
			if (obj) {
				controlsQueryDtid = ['dtid'];
				if (validateForm(controlsQueryDtid)) {
					var dtid = $("#dtid").textbox("getValue");
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$('#paymentId').closest('div.panel').block(blockStyle1);
					ajaxService.getPayInfo(dtid, customerId, "", function(result){
						if (result != null) {
							uploadPayInfo(result);
						} else {
							$("#hiddenDtid").val('');
							uploadPayInfo(null);
							$("#addPayInfoMsg").html("查無資料");
							$("#dtid").textbox("setValue", dtid);
							handleScrollTop('showEdit');
						}
					});
				}
			} else {
				viewQueryDtid = $('#chooseDTIDPay').dialog({    
					title : "選擇DTID",    
					width : 960,
					height : 550,
					top:10,
					closed : false,
					cache : false,
					href : "${contextPath}/payment.do?actionId=initView&queryCompanyId="+customerId,
					modal : true,
					onLoad :function(){
						settingDialogPanel('chooseDTIDPay');
						textBoxSetting("chooseDTIDPay");
						dateboxSetting("chooseDTIDPay");
					},
					onClose : function(){
						$(".datagrid-row").mouseover(function(){ 
	   						$(this).css("cursor", "auto");
	   					});
						if(viewQueryDtid){
							viewQueryDtid.dialog('clear');
						}
					}, 
				});
			}
		}
	}
	/**
	* 處理新增頁面數據，顯示或清空
	*/
	function uploadPayInfo(result){
		$("#hiddenDtid").val(result==null?"":result.dtid);
		$("#show_requirementNo").textbox("setValue", result==null?"":result.requirementNo);
		$("#show_tid").textbox("setValue", result==null?"":result.tid);
		$("#show_merchantCode").textbox("setValue", result==null?"":result.merchantCode);
		$("#show_realFinishDate").datebox("setValue", result==null?"":result.acceptableFinishDate);
		$("#show_name").textbox("setValue", result==null?"":result.merchantName);
		$("#show_caseCreatedDate").datetimebox("setValue", result==null?"":formatToTimeStamp(result.createdDate));
		$("#show_caseId").textbox("setValue", result==null?"":result.caseId);
		$("#show_aoName").textbox("setValue", result==null?"":result.aoName);
		$("#requirementNo").val(result==null?"":result.requirementNo);
		$("#dtid").textbox("setValue",result==null?"":result.dtid);
		$("#tid").val(result==null?"":result.tid);
		$("#merchantId").val(result==null?"":result.merchantId);
		$("#realFinishDate").val(result==null?"":result.acceptableFinishDate);
		$("#caseCreatedDate").val(result==null?"":result.createdDate);
		$("#caseId").val(result==null?"":result.caseId);
		$("#merchantHeaderId").val(result==null?"":result.merchantHeaderId);
		if (result != null) {
			var srmPaymentItemDTOs = result.srmPaymentItemDTOs;
			$("#assetTable").datagrid("loadData", {total:srmPaymentItemDTOs.length, rows:srmPaymentItemDTOs} );
			initPaymentInfoCombobox(true);
			for (var i = 0; i < srmPaymentItemDTOs.length; i++) {
				initPayMentInfoValue("assetTable", i, 4);
			}
			$("#peripheralSuppliesTable").datagrid("loadData", {total:0, rows:[]} );
		} else {
			$("#assetTable").datagrid("loadData", {total:0, rows:[]} );
			$("#peripheralSuppliesTable").datagrid("loadData", {total:0, rows:[]} );
		}
		editIndexAsset = undefined;
		editIndex = undefined;
		$('#removeSupplies').linkbutton('disable');
		$('#paymentId').closest('div.panel').unblock();
	}
	/**
	* 初始化表格的複選框
	*/
	function checkboBox(value){
		if (value == '<%=IAtomsConstants.YES%>' || value == '1' || value == null) {
			return "<input type=\"checkbox\" checked onclick = \"checkIsClaim(this);\"/>";
		} else {
			return "<input type=\"checkbox\" onclick = \"checkIsClaim(this);\"/>";
		}
	}
	/**
	* 點選是否求償
	*/
	function checkIsClaim(obj) {
		var rows = $('#assetTable').datagrid('getRows');
		var isPay = undefined;
		var index = $(obj).parents("td").parents("tr").attr("datagrid-row-index");
		isPay = rows[index]['isPay'];
		if (index != editIndexAsset) {
			if (!endEditing() || !endEditingAsset()) {
				obj.checked = obj.checked ? false : true;
			} else {
				isPay = obj.checked ? '1' : '0';
			}
		} else {
			isPay = obj.checked ? '1' : '0';
		}
		rows[index]['isPay'] = isPay;
		var options = $(obj).parents("td").next().children("div").children("select");
		if (obj.checked) {
			options.combobox("enableValidation");
		} else {
			options.combobox("disableValidation");
		}
	}
	/**
	* 檢驗textbox框的長度以及去除空格
	*/
	function checkTextBox(index) {
		var suppliesAmount = $('#peripheralSuppliesTable').datagrid('getEditor', {index:index,field:'suppliesAmount'});
		$(suppliesAmount.target).textbox('textbox').attr('maxlength',3).bind('blur', function(e){
			$(suppliesAmount.target).textbox('setValue', $.trim($(this).val()));
		});
	}
	</script>
</body>
</html>
