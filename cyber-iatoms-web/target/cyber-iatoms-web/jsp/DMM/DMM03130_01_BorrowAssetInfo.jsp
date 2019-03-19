<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.DmmAssetBorrowFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	DmmAssetBorrowFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (DmmAssetBorrowFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null ) {
		formDTO = new DmmAssetBorrowFormDTO();
		ucNo = IAtomsConstants.UC_NO_DMM_03130;
	} else {
		ucNo = formDTO.getUseCaseNo();
	}
	//獲取設備類型集合
	String assetCategoryList = (String)SessionHelper.getAttribute(request, ucNo,  IAtomsConstants.PARAM_ASSET_CATEGORY_LIST);
%>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"  %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview-expander.js"></script>
<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>

<title>設備借用作業</title>
</head>
<body>
	<div id="assetTypeDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height topSoller" >
		<div id="p" region="center" class="easyui-panel" title="設備借用作業" style="width: 98%; height: auto;" >
			<form id="saveBorrowListForm"  method="post" novalidate>
				<div style="color: red">
					<span id="borrowMsg" class="red"></span>
				</div>
				<table cellpadding="6" style="width:auto;height:auto">
					<tr>
						<td>借用人: </td>
						<td width="100px"> <label>${logonUser.name }</label> </td>
						<td>借用日期: </td>
						<td width="500px">
							<input id="borrowStartDate" name="borrowStartDate" required=true  missingMessage='請輸入借用日期起',
								class="easyui-datebox" type="text" maxlength="10" style="width: 150px"
								data-options="validType:['maxLength[10]','date[\'借用日期格式限YYYY/MM/DD\']'],
									onChange:function(newValue,oldValue) {
                            			borrowDateChange(newValue);
                            			$('#borrowEndDate').timespinner('isValid');
                             	}" />
							~
							<input id="borrowEndDate" name="borrowEndDate" 
								class="easyui-datebox" type="text" maxlength="10" style="width: 150px" required=true  missingMessage='請輸入借用日期迄',
								data-options="validType:['maxLength[10]','date[\'借用日期格式限YYYY/MM/DD\']'
										,'compareDateStartEnd[\'#borrowStartDate\',\'借用日期起不可大於借用日期迄\']']" />
						</td>
					</tr>
					<tr>
						<td>說明: </td>
						<td colspan="3">
							<input maxLength="200" name="comment" id="comment" 
								class="easyui-textbox" data-options="multiline:true,validType:'maxLength[200]'" 
								style="height: 50px; width: 425px" >
						</td>
					</tr>
				</table>
				<div>
					<table cellpadding="6"  style="width:70%;">
						<tr>
							<td align="right">
								<div>
									<a href="#" id="confirmTrans"  class="easyui-linkbutton" iconcls="icon-ok" onclick="save();" > 送出</a>
									<a href="#" id="cancleTrans" class="easyui-linkbutton" iconcls="icon-cancel" onclick="clearData();" >清除</a>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div style="text-align: right;padding: 10px" id="addBorrowAssetDiv" class="topSoller">
					<table id="assetBorrowTable" class="easyui-datagrid" title="新增借用清單" style="width:70%;height:auto;"　
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						toolbar: '#tb3',
						striped:true,
						nowrap:false,
						onDblClickRow: onDblClickRow,
						onClickRow:onClickRow,
						onEndEdit: onEndEdit">
					<thead>
						<tr>
							<th data-options="field:'assetCategory',width:250,halign:'center',
								formatter:function(value,row){
									return row.assetCategoryName;
								},
								editor:{
									type:'combobox',
									options:{
										valueField:'value',
										textField:'name',
										method:'get',
										editable:false,
										required:true,
										data:assetCategoryList,
										validType:'ignore[\'請選擇\']',
										invalidMessage:'請輸入設備類別',
										onChange:getAssetTypeList
									}		
								}">設備類別
							</th>
							<th data-options="field:'assetTypeId',width:350,halign:'center',
								formatter:function(value,row){
									return row.assetTypeName;
								},
								editor:{
									type:'combobox',
									options:{
										valueField:'value',
										textField:'name',
										method:'get',
										required:true,
										editable:false,
										validType:'ignore[\'請選擇\']',
										invalidMessage:'請輸入設備名稱'
									}
								}">設備名稱
							</th>
							<th data-options="field:'borrowNumber',width:155,halign:'center',align:'right',
								editor:{type:'textbox',
									options:{
										required:true,
										missingMessage:'請輸入數量',
										validType:['number[\'數量限輸入正整數，請重新輸入\']'] }
								}">數量
							</th>
						</tr>
					</thead>
				</table>
				</div>
				
				<div id="tb3" style="height:auto">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="appendRow();">增加</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="deletedAsset" onclick="removeit();" disabled = "true">刪除</a>
					
				</div>
			</form>
			<div style="text-align: right;padding: 10px" id = "waitProcessDiv">
				<table id="waitProcessDataGrid" class="easyui-datagrid" title="處理中設備清單" style="width:100%;height:auto"
							data-options="
								pagination:true,
								pageNumber:0,
								pageList:[15,30,50,100],
								pageSize:15,
								iconCls: 'icon-edit',
								singleSelect: true,
								method: 'post',
								nowrap:false,
								onLoadSuccess: onLoadSuccess,
								url: '${contextPath}/assetBorrowList.do?actionId=query&caseStatus=WAIT_PROCESS',
							">
					<thead>
						<tr>
							<th data-options="field:'rowNumber',align:'center',halign:'center', width:30, styler: function(){return 'background-color: #F4F4F4;';}"></th>
							<th data-options="field:'borrowCaseId',width:200,halign:'center',align:'left'">編號</th>
							<th data-options="field:'borrowCategory',width:200,halign:'center',align:'left'">案件類別</th>
							<th data-options="field:'borrowStatusName',width:200,halign:'center',align:'left'">案件狀態</th>
							<th data-options="field:'borrowStatus',hidden:true">案件狀態</th>
							<th data-options="field:'borrowUser',width:200,halign:'center',align:'left'">借用人</th>
							<th data-options="field:'borrowStartDate',width:150,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD">借用起日</th>
							<th data-options="field:'borrowEndDate',width:150,halign:'center',align:'center', formatter:formaterTimeStampToyyyyMMDD">借用迄日</th>
							<th data-options="field:'assetCategoryName',width:200,halign:'center',align:'left'">設備類別</th>
							<th data-options="field:'assetTypeName',width:200,halign:'center',align:'left'">設備名稱</th>
							<th data-options="field:'borrowNumber',halign:'center',align:'right'">數量</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="processAssetDiv" style="text-align: right;padding: 10px">
				<div>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="showContinueDiv();">續借</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" id="backAsset" onclick="">歸還</a>
				</div>
				<div id="renewDiv">
					<table id="renewAssetTable" class="easyui-datagrid" title="續借" style="width:70%;height:auto"　
						data-options="
							iconCls: 'icon-edit',
							singleSelect: true,
							striped:true,
							nowrap:false
							">
						<thead>
							<tr>
								<th data-options="field:'contonueComment',width:350,halign:'center',align:'center',
										formatter:function(value,row){
											return showText('delayComment', true);
										}">说明
								</th>
								<th data-options="field:'assetTypeId',width:200,halign:'center',align:'center',
										formatter:function(value,row){
											return showText('delayDate', false);
										}">續借迄日
								</th>
								<th data-options="field:'amount',width:200,halign:'center',align:'center',
										formatter:function(value,row){
											return showText('', false);
										}">動作
								</th>
							</tr>
						</thead>
					</table>
				</div>
				<div style="margin-top: 5px" id = "ccc">
					<table id="processAssetDataGrid" class="easyui-datagrid" title="借用清單" style="width:99%;height:auto"
							data-options="
								border:true,
								pagination:true,
								pageList:[15,30,50,100],
								pageSize:15,
								remoteSort:true,
								pageNumber:0,
								nowrap : false,
								rownumbers:true,
								iconCls: 'icon-edit',
								onLoadSuccess: processLoadSuccess,
								url: '${contextPath}/assetBorrowList.do?actionId=queryProcess',
							">
						<thead>
							<tr>
								<th data-options="field:'v',width:40,checkbox:true"></th>
								<th data-options="field:'borrowListId',hidden:true">id</th>
								<th data-options="field:'borrowUser',width:150,halign:'center',align:'left'">借用人</th>
								<th data-options="field:'assetCategoryName',width:150,halign:'center',align:'left'">設備類別</th>
								<th data-options="field:'assetTypeName',width:200,halign:'center',align:'left'">設備名稱</th>
								<th data-options="field:'assetTypeId',hidden:true">設備id</th>
								<th data-options="field:'serialNumber',width:200,halign:'center',align:'left'">設備序號</th>
								<th data-options="field:'borrowStartDate',width:150,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD">借用起日</th>
								<th data-options="field:'borrowEndDate',halign:'center',width:150,formatter:formaterTimeStampToyyyyMMDD">借用迄日</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			
			
		</div>
	</div>
	<script type="text/javascript">
		var editIndex = undefined;
		var isDblClickRow = false;
		var assetCategoryList = initSelect(<%=assetCategoryList%>);
		$(function() {
			$("#renewDiv").hide();
		});
		function showText(id, isText){
			var returnInput = "";
			if (id != "" && id != null) {
				if (isText) {
					returnInput = "<input class='easyui-textbox textBox' id="+id+" style='width:100%; height: 50px;' maxlength='200'/>";
				} else {
					returnInput = "<input class='easyui-datebox date' maxlength='10' style='width:100%' id='"+id+"'/>";
				}
			} else {
				var s = '<a href="javascript:void(0)" style= "color:blue" onclick="saveDelay();">儲存</a> ';
				//Task #2987
				var c = '<a href="javascript:void(0)" style= "color:blue" onclick="clearDataConu();">取消</a> ';
				returnInput = s + c;
			}
			return returnInput;
		}
		function showContinueDiv() {
			var rows = $("#processAssetDataGrid").datagrid('getSelections');
			if (rows.length == 0) {
				$.messager.alert('提示','請勾選要操作的記錄!','warning');
				return false;
			}
			var serialNumber = "";
			for (var i = 0; i <rows.length; i++) {
				serialNumber = serialNumber + rows[i].serialNumber + ",";
			}
			ajaxService.checkAssetIsBorrow(serialNumber, function(result){
				if (result == null) {
					$("#renewDiv").show();
					var resultData = {};
					var rows = [];
					var editRow = {};
					editRow.contonueComment = '';
					editRow.delayComment = '';
					editRow.delayDate = '';
					rows.push(editRow);
					resultData.rows = rows;
					resultData.total = 1;
					$('#renewAssetTable').datagrid('loadData', resultData);
					$(".date").datebox({
						required:true,
						missingMessage:'請輸入續借迄日',
						validType:['date[\'續借迄日格式限YYYY/MM/DD\']', '']
					});
					$(".textBox").textbox({});
					dateBoxsDefaultSetting($('.date'));
					textBoxDefaultSetting($('.textBox'));
				} else {
					$("#borrowMsg").text(result);
					handleScrollTop('assetTypeDiv'); 
				}
			});
		}
		/**
		* 借用清單新增一行
		*/
		function appendRow(){
			$("#borrowMsg").text("");
			if (endEditing()) {
				$('#assetBorrowTable').datagrid('appendRow', {assetTypeId:'請選擇',assetCategory:''});
				editIndex = $('#assetBorrowTable').datagrid('getRows').length - 1;
				$('#assetBorrowTable').datagrid('selectRow', editIndex).datagrid(
						'beginEdit', editIndex);
				checkTextBox(editIndex);
				$("#deletedAsset").linkbutton('enable');
				//新增時，始終跳到最后行
				$('#addBorrowAssetDiv').scrollTop( $('#addBorrowAssetDiv')[0].scrollHeight );
			}
		}
		/**
		* 檢驗textBox框長度以及取消空格
		*/
		function checkTextBox(index) {
			var amount = $('#assetBorrowTable').datagrid('getEditor', { 'index': index, field: 'borrowNumber' });
			$(amount.target).textbox('textbox').attr('maxlength',10);
		}
		/**
		* 結束datagrid表格數據
		*/
		function endEditing() {
			$("div").unbind("scroll.validate");
			if (editIndex == undefined) {
				return true;
			}
			if(!validateFormInContract()){
				return false;
			}
			if ($('#assetBorrowTable').datagrid('validateRow', editIndex)) {
				$('#assetBorrowTable').datagrid('endEdit', editIndex);
				var row = $('#assetBorrowTable').datagrid('getSelected');
				var rowIndex = $('#assetBorrowTable').datagrid('getRowIndex',row);
				var rows = $('#assetBorrowTable').datagrid('getRows');
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		/**
		* 刪除datagrid表格一行數據
		*/
		function removeit() {
			$("#borrowMsg").text("");
			$("div.topSoller").unbind("scroll.validate");
			if (editIndex == undefined) {
				// 沒有正在編輯的row，獲取選中row
				var row = $('#assetBorrowTable').datagrid('getSelected');
				if (row){
					var rowIndex = $('#assetBorrowTable').datagrid('getRowIndex',row);
					$('#assetBorrowTable').datagrid('deleteRow',rowIndex);
					$("#deletedAsset").linkbutton('disable');
				}
				return;
			}
			$('#assetBorrowTable').datagrid('cancelEdit', editIndex).datagrid('deleteRow',
					editIndex);
			editIndex = undefined;
			$("#deletedAsset").linkbutton('disable');
		}
		/**
		* 設備類別下拉框聯動設備名稱，並去除已經選擇的設備名稱
		*/
		function getAssetTypeList(newValue, oldValue) {
			var row = $('#assetBorrowTable').datagrid('getSelected');
			var rowIndex = $('#assetBorrowTable').datagrid('getRowIndex',row);
			if (newValue != "") {
				ajaxService.getAssetTypeList(newValue, function(result) {
					if (result != null) {
						selectValues = result;
						//查找所有的row
						var rows = $('#assetBorrowTable').datagrid('getRows');
						for (var i = 0; i < rows.length; i++) {
							if (i == rowIndex) {
								//當前行不比對
								continue;
							}
							//和選擇的行值一樣, 需過濾掉當前值                        		
							for (var j = 0; j < result.length; j++) {
								if (result[j].value == rows[i].assetTypeId) {
									result.splice(j,1);
									break;
								}
							} 
						}
						var currentEditor = $('#assetBorrowTable').datagrid('getEditor', {  
								index : rowIndex,  
								field : 'assetTypeId'
						});
						if (!isDblClickRow) {
							currentEditor.target.combobox('setValue', "");
						} else {
							isDblClickRow = false;
						}
						currentEditor.target.combobox('loadData', initSelect(result));
					} else {
						var currentEditor = $('#assetBorrowTable').datagrid('getEditor', {  
							index : rowIndex,  
							field : 'assetTypeId'
						});
						currentEditor.target.combobox('setValue', "");
						currentEditor.target.combobox('loadData', initSelect(null));
					}
				});
			} else {
				var currentEditor = $('#assetBorrowTable').datagrid('getEditor', {  
					index : rowIndex,  
					field : 'assetTypeId'
				});
				currentEditor.target.combobox('setValue', "");
				currentEditor.target.combobox('loadData', initSelect(null));
			}
		}
		
		/**
		* 雙擊datagrid表格觸發的事件
		*/
		function onDblClickRow(index) {
			if (editIndex != index) {
				if (endEditing()) {
					var rows = $("#assetBorrowTable").datagrid("getRows");
					if (rows) {
						if (rows[index].assetCategory != "") {
							isDblClickRow = true;
						}
					}
					$('#assetBorrowTable').datagrid('selectRow', index).datagrid(
							'beginEdit', index);
					var ed = $('#assetBorrowTable').datagrid('getEditor', {
						index : index,
						field : 'assetCategory'
							});
					var assetCategoryValue = $(ed.target).combobox('getValue');
					if (assetCategoryValue == "") {
						ajaxService.getAssetTypeList(assetCategoryValue, function(result) {
							if (result != null) {
								selectValues = result;
								//查找所有的row
								var rows = $('#assetBorrowTable').datagrid('getRows');
								for (var i = 0; i < rows.length-1; i++) {
									if (i == editIndex) {
										//當前行不比對
										continue;
									}
									//和選擇的行值一樣, 需過濾掉當前值                        		
									for (var j = 0; j < result.length; j++) {
										if (result[j].value == rows[i].assetTypeId) {
											result.splice(j,1);
											break;
										}
									} 
								}
								var currentEditor = $('#assetBorrowTable').datagrid('getEditor', {  
										index : editIndex,  
										field : 'assetTypeId'
								});
								var selectValue = $(ed.target).combobox('getValue');
								var isExist = checkExistValue(selectValues, selectValue);
								if (!isExist) {
									$(ed.target).combobox('setValue', '');
								}
								currentEditor.target.combobox('loadData', initSelect(result));
							}
						});
					} else {
						ed = $('#assetBorrowTable').datagrid('getEditor', {
							index : index,
							field : 'assetTypeId'
								});
						var selectValue = $(ed.target).combobox('getValue');
						var isExist = checkExistValue(selectValues, selectValue);
						if (!isExist) {
							$(ed.target).combobox('setText', '');
						}
					}
					editIndex = index;
					checkTextBox(editIndex);
					$("#deletedAsset").linkbutton('enable');
					
				} else {
					setTimeout(function() {
						$('#assetBorrowTable').datagrid('selectRow', editIndex);
					}, 0);
				}
			}
		}
		/**
		* 驗證合約存儲頁面的表單提交
		*/
		function validateFormInContract () {
			if (editIndex == undefined) {
				return true;
			}
			var controls = ['assetCategory','assetTypeId','borrowNumber'];
			if(!validateFormInRow('assetBorrowTable', editIndex, controls)){
				return false;
			} else {
				$("div.topSoller").unbind("scroll.validate");
				//editIndex = undefined;
				return true;
			}
		}
		/**
		* 點擊datagrid表格觸發的事件
		*/
		function onClickRow(index, field){
			if (editIndex == undefined) {
					$('#assetBorrowTable').datagrid('selectRow', index);
			} else {
				$('#assetBorrowTable').datagrid('selectRow', editIndex);
			}
			$("#deletedAsset").linkbutton('enable');
		}
		/**
		* 當結束編輯時
		*/
		function onEndEdit(index, row) {
			$("div.topSoller").unbind("scroll.validate");
			var ed = $(this).datagrid('getEditor', {
				index : index,
				field : 'assetTypeId'
			});
			row.assetTypeName = $(ed.target).combobox('getText');
			ed = $(this).datagrid('getEditor', {
				index : index,
				field : 'assetCategory'
			});
			if ($(ed.target).combobox('getText') != "請選擇") {
				row.assetCategoryName = $(ed.target).combobox('getText');
			} else {
				row.assetCategoryName = null;
			}
			checkValueIsNull(row, index, "borrowNumber");
		}
		/**
		* 核檢數量是否為空
		*/
		function checkValueIsNull(row, index, field) {
			var ed = $("#assetBorrowTable").datagrid('getEditor', {
				index : index,
				field : field
			});
			if ($(ed.target).textbox('getValue') == ""){
				row[field] = null;
			}
		}
		/**
		* 保存借用
		*/
		function save() {
			if (endEditing() && checkSaveData()) {
				if (!$("#saveBorrowListForm").form("validate")) {
					return false;
				}
				var params = $("#saveBorrowListForm").form("getData");
				// 拿到集合的所有的行
				var assetRows = $("#assetBorrowTable").datagrid('getRows');
				params.assetBorrowNumber = JSON.stringify(assetRows);
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				$.ajax({
					url: "${contextPath}/assetBorrowList.do?actionId=save",
					data:params, 
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(json){
						if (json.success) {
							$("#waitProcessDataGrid").datagrid("reload", { });
							clearData();
							$("#borrowMsg").text(json.msg);
						}else{
							handleScrollTop('assetTypeDiv'); 
							$("#borrowMsg").text(json.msg);
						}	
						$.unblockUI();
					},
					error:function(){
						$("#msg1").text("合約保存失敗");
						$.unblockUI();
					}
				});
			}
		}
		/**
		* 保存成功後清空數據
		*/
		function clearData(){
			$("#borrowStartDate").datebox("setValue", "");
			$("#borrowEndDate").datebox("setValue", "");
			$("#comment").textbox("setValue", "");
			$("#assetBorrowTable").datagrid('loadData', {rows:[],total:0});
			editIndex = undefined;
		}
		/**
		* 核檢保存數據
		*/
		function checkSaveData() {
			if($("#saveBorrowListForm").form("validate")){
				var assetRows = $("#assetBorrowTable").datagrid('getRows');
				if (assetRows.length == 0) {
					handleScrollTop('dlg');
					$("#borrowMsg").text("請至少輸入一筆借用設備");
					return false;
				}
			}
			if(!validateFormInContract()){
				return false;
			}
			return true;
		}
		/**
		* 借用日期起異動事件－借用時間迄顯示延長三個月
		*/
		function borrowDateChange(newValue) {
			if (newValue == "" || newValue == null) {
				$("#borrowEndDate").datebox("setValue", "");
			} else {
				var myDate = new Date(newValue);
				myDate.setMonth(myDate.getMonth() + 3);
				$("#borrowEndDate").datebox("setValue", myDate);
			}
		}
		
		/**
		* 申請中設備列表加載完成方法
		*/
		function onLoadSuccess(data) {
			if (data.total == 0) {
				$("#waitProcessDiv").hide();
			} else {
				$("#waitProcessDiv").show();
				//查詢結果合併參考數組
	        	var arr =[{mergeFiled:"borrowCaseId",premiseFiled:"borrowCaseId"},
	        	          {mergeFiled:"borrowCategory",premiseFiled:"borrowCaseId"},
	        	          {mergeFiled:"borrowStatusName",premiseFiled:"borrowCaseId"},
	        	          {mergeFiled:"borrowUser",premiseFiled:"borrowCaseId"},
	        	          {mergeFiled:"borrowStartDate",premiseFiled:"borrowCaseId"},
	        	          {mergeFiled:"borrowEndDate",premiseFiled:"borrowCaseId"},
	        	          {mergeFiled:"rowNumber",premiseFiled:"rowNumber"}
		                  ];
	        	//合并列的field数组及对应前提条件filed（为空则直接内容合并）
				mergeCells(arr,"waitProcessDataGrid");
			}
		}
		
		function processLoadSuccess(data) {
			if (data.total == 0) {
				$("#processAssetDiv").hide();
			}
		}
		
		function saveDelay() {
			// 拿到集合的所有的行
			var assetRows = $("#processAssetDataGrid").datagrid('getSelections');
			var delayDate = $("#delayDate").datebox("getValue");
			if (delayDate == "" || delayDate == null) {
				$("#delayDate").datebox("textbox").focus();
				return false;
			}
			var borrowListId = '';
			for (var i = 0; i < assetRows.length; i++) {
				borrowListId = borrowListId + assetRows[i].borrowListId + ",";
			}
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle1);
			$.ajax({
				url: "${contextPath}/assetBorrowList.do?actionId=saveBorrow",
				data:{
					borrowListId: borrowListId.substring(0, borrowListId.length - 1),
					delayDate:delayDate
				}, 
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(json){
					if (json.success) {
						handleScrollTop('assetTypeDiv'); 
						$("#waitProcessDataGrid").datagrid("reload",{ });
						clearDataConu();
						$("#borrowMsg").text(json.msg);
					}else{
						handleScrollTop('assetTypeDiv'); 
						$("#borrowMsg").text(json.msg);
					}	
					$.unblockUI();
				},
				error:function(){
					$("#msg1").text("設備借用保存保存失敗");
					$.unblockUI();
				}
			});
		}
		/**
		* 保存成功後清空數據
		*/
		function clearDataConu(){
			$("#renewDiv").hide();
			$("#renewAssetTable").datagrid('loadData', {rows:[],total:0});
		}
	</script>
</body>
</html>