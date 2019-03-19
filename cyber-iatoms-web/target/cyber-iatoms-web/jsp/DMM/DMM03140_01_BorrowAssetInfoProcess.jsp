<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.DmmAssetBorrowFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	DmmAssetBorrowFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (DmmAssetBorrowFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null ) {
		formDTO = new DmmAssetBorrowFormDTO();
		ucNo = IAtomsConstants.UC_NO_DMM_03140;
	} else {
		ucNo = formDTO.getUseCaseNo();
	}
	//獲取當前用戶角色
	String userRole = formDTO.getLoginRoles();
	//獲取
	List<Parameter> borrowCaseCategorys = (List<Parameter>) SessionHelper.getAttribute(request, ucNo,  IATOMS_PARAM_TYPE.BORROW_CASE_CATEGORY.getCode());
	//獲取
	List<Parameter> borrowCaseStatuss = (List<Parameter>) SessionHelper.getAttribute(request, ucNo,  IATOMS_PARAM_TYPE.BORROW_CASE_STATUS.getCode());
	List<Parameter> assetBorrowIds = (List<Parameter>) SessionHelper.getAttribute(request, ucNo,  IAtomsConstants.PARAM_ASSET_BORROW_CASE_ID);
%>
<c:set var="borrowCaseCategorys" value="<%=borrowCaseCategorys%>" scope="page"></c:set>
<c:set var="borrowCaseStatuss" value="<%=borrowCaseStatuss%>" scope="page"></c:set>
<c:set var="assetBorrowIds" value="<%=assetBorrowIds%>" scope="page"></c:set>
<c:set var="userRole" value="<%=userRole%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview-expander.js"></script>
<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
	<div id="assetBorrowProcessDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height" >
		<div id="pp" region="center" class="easyui-panel" title="設備借用處理作業" style="width: 98%; height: auto;" >
		<div style="padding: 10px">
			
		
		<table cellpadding="6" style="width:auto; height:auto">
			<tr>
				<td>案件編號: </td>
				<td width="250px">
					<cafe:droplisttag 
		               	id="borrowCaseId" 
						name="borrowCaseId" 
						css="easyui-combobox"
						hasBlankValue="true" 
						blankName="請選擇"
						result="${assetBorrowIds }"
						style="width:190px" 
						javascript="editable=false data-options=\"valueField:'value',textField:'name'\"">
					</cafe:droplisttag>
				</td>
				<td>借用日期: </td>
				<td width="500px">
					<input id="borrowStartDate" name="borrowStartDate" 
							class="easyui-datebox" type="text" maxlength="10" style="width: 150px" 
							data-options="validType:['maxLength[10]','date[\'借用日期格式限YYYY/MM/DD\']']" />
					~
					<input id="borrowEndDate" name="borrowEndDate" 
							class="easyui-datebox" type="text" maxlength="10" style="width: 150px" 
							data-options="validType:['maxLength[10]','date[\'借用日期格式限YYYY/MM/DD\']']" />
				</td>
			</tr>
			<tr>
				<td>案件類別: </td>
				<td width="250px">
					<cafe:droplisttag 
		                id="caseCategory" 
						name="caseCategory" 
						css="easyui-combobox"
						hasBlankValue="true" 
						result="${borrowCaseCategorys }"
						blankName="請選擇"
						style="width:190px" 
						javascript="editable=false data-options=\"valueField:'value',textField:'name'\"">
					</cafe:droplisttag>
				</td>
				<td>案件狀態: </td>
				<td width="500px">
					<cafe:droplisttag 
		                id="caseStatus" 
						name="caseStatus" 
						css="easyui-combobox"
						hasBlankValue="true" 
						selectedValue="${borrowCaseStatuss[0].value }"
						result="${borrowCaseStatuss }"
						blankName="請選擇"
						style="width:138px" 
						javascript="editable=false data-options=\"valueField:'value',textField:'name'\"">
					</cafe:droplisttag>
				</td>
			</tr>
			<tr>
				<td colspan="4" style="text-align: right;">
					<a href="javascript:void(0)" id="button_query" class="easyui-linkbutton c6" iconcls="icon-search" onclick="query(1, true)" style="width: 90px">查詢</a>
				</td>
			</tr>
		</table>
		<div>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="confirmBorrow" onclick="aa('true');" disabled=true>同意</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="unConfirmBorrow" onclick="aa('false');" disabled=true>不同意</a>
		</div>
		<div style="color: red">
			<span id="processBorrowMsg" class="red"></span>
		</div>
		<form action="" id = "test22">
			<div id="continueDiv2" style="width: 85%; margin-bottom: 5px">
			<input type="hidden" id="isBack" value=""/>
			<input type="hidden" id="isDirectorCheck" value="Y"/>
				<table id="continueAssetTable2" class="easyui-datagrid" title="動作" style="width:98%;height:auto"　
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						striped:true,
						nowrap:false">
				<thead>
					<tr>
						<th data-options="field:'contonueComment2',width:450,halign:'center',align:'center',
								formatter:function(value,row){
										return showText('delayComment2', true);
								}">说明
						</th>
						<th data-options="field:'amount2',width:200,halign:'center',align:'center',
								formatter:function(value,row){
										return showText('', false);
								}">動作
						</th>
					</tr>
				</thead>
			</table>
			
			<div id = "bb" >
				<table id="continueAssetTable3" class="easyui-datagrid" title="設備清單" style="width:98%;height:auto"　
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						striped:true,
						nowrap:false">
					<thead>
						<tr>
							<th data-options="field:'borrowCaseId',width:200,halign:'center',align:'left'">案件編號</th>
							<th data-options="field:'borrowCategory',width:100,halign:'center',align:'left'">案件類別</th>
							<th data-options="field:'borrowEndDate',width:150,halign:'center',align:'center',formatter:function(value,row,index){return settingTable3box(value,row,index,'false', 'borrowEndDate'+row.rowNumber);}">借用迄日</th>
							<th data-options="field:'assetCategoryName',width:150,halign:'center',align:'left'">設備類別</th>
							<th data-options="field:'assetTypeName',width:200,halign:'center',align:'left'">設備名稱</th>
							<th data-options="field:'serialNumber',halign:'center',align:'left',formatter:function(value,row,index){return settingTable3box(value,row,index,'true', 'serialNumber'+row.rowNumber);}">設備序號</th>
							<th data-options="field:'rowNumber',hidden:true"></th>
							<th data-options="field:'assetTypeId',hidden:true"></th>
							<th data-options="field:'serialNumberHidden',hidden:true"></th>
							<th data-options="field:'borrowListId',hidden:true"></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		</form>
		<!-- <div style="text-align: right;padding: 10px"> -->
		<table id="aaa" class="easyui-datagrid" title="設備清單" style="width:98%;height:auto"
					data-options="
					border:true,
					pagination:true,
					iconCls: 'icon-edit',
					singleSelect: false,
					method: 'post',
					nowrap:false,
					onClickRow:dataGridOnSelect,
					onUnSelectRow:dataGridUnCheck
				">
			<thead>
				<tr>
					<th data-options="field:'rowNumber',align:'center',halign:'center', width:30, styler: function(){return 'background-color: #F4F4F4;';}"></th>
					<!-- <th data-options="field:'v',width:40,checkbox:true"></th> -->
					<th data-options="field:'checked',width:40,align:'center',halign:'center',formatter:function(value,row,index){return settingCheckbox(row, index, 'checkBtn'+row.rowNumber);},title:'<input type=checkbox onclick =allCheckButton(this); id=allCheckButton style=display:none; >'"></th>
					<th data-options="field:'borrowCaseId',width:155,halign:'center',align:'left'">編號</th>
					<th data-options="field:'borrowCategory',width:100,halign:'center',align:'left'">案件類別</th>
					<th data-options="field:'borrowStatusName',width:150,halign:'center',align:'left'">案件狀態</th>
					<th data-options="field:'borrowStatus',hidden:true">案件狀態</th>
					<th data-options="field:'borrowUser',width:140,halign:'center',align:'left'">借用人</th>
					<th data-options="field:'borrowStartDate',width:100,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD">借用起日</th>
					<th data-options="field:'borrowEndDate',width:100,halign:'center',align:'center', formatter:formaterTimeStampToyyyyMMDD">借用迄日</th>
					<th data-options="field:'assetCategoryName',width:150,halign:'center',align:'left'">設備類別</th>
					<th data-options="field:'assetTypeName',width:200,halign:'center',align:'left'">設備名稱</th>
					<th data-options="field:'borrowNumber',halign:'center',align:'right'">數量</th>
					<th data-options="field:'directorBack',hidden:true">倉管主管是否退回</th>
					<th data-options="field:'isAgentProcess',hidden:true">是否倉管經辦處理</th>
				</tr>
			</thead>
		</table>
		<!-- </div> -->
		</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#continueDiv2").hide();
		});
		
		var isCheck = false;
		function aa(isConfirm) {
			var rows = $("#aaa").datagrid('getSelections');
			if (rows.length == 0) {
				$.messager.alert('提示','請勾選要操作的記錄!','warning');
				return false;
			}
			$("#continueDiv2").show();
			var resultData = {};
			var rows = [];
			var editRow = {};
			editRow.contonueComment2 = '';
			rows.push(editRow);
			resultData.rows = rows;
			resultData.total = 1;
			$('#continueAssetTable2').datagrid('loadData', resultData);
			$(".textBox").textbox({
				validType:'maxLength[200]',
				multiline:true,
				required:isConfirm=='true'?false:true,
				missingMessage:'請輸入設備序號'
			});
			textBoxDefaultSetting($('.textBox'));
			if (isConfirm == "true") {
				$("#bb").show();
				$("#isBack").val('N');
				rows = $("#aaa").datagrid('getSelections');
				if (rows[0].isAgentProcess == 'Y' && "${userRole}" == '01') {
					$("#bb").show();
					$("#isDirectorCheck").val('N');
					var borrowCaseId = '';
					for (var i = 0; i < rows.length; i++) {
						borrowCaseId = borrowCaseId + rows[i].borrowCaseId + ",";
					}
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle1);
					ajaxService.getBorrowAssetItemByIds(borrowCaseId.substring(0, borrowCaseId.length - 1), function(result){
						if (result != null) {
							$("#continueAssetTable3").datagrid("loadData", {total:result.length, rows:result});
							var arr =[{mergeFiled:"borrowCaseId",premiseFiled:"borrowCaseId"},
				        	          {mergeFiled:"borrowCategory",premiseFiled:"borrowCaseId"}
					                  ];
				        	//合并列的field数组及对应前提条件filed（为空则直接内容合并）
							mergeCells(arr,"continueAssetTable3");
							
							/* $('.payText').textbox({
								validType:'checkSerialNumber[]',
								required:true,
								missingMessage:'請輸入設備序號'
							});
							textBoxsDefaultSetting($('.payText')); */
							for(var i = 0; i < result.length; i++) {
								if (result[i].borrowCategory != "續借") {
									$('#serialNumber'+result[i].rowNumber).textbox({
										validType:'checkSerialNumber["' + result[i].assetTypeId + '","'+ result[i].rowNumber +'"]',
										required:true,
										missingMessage:'請輸入設備序號'
									});
								}
								/* $("#borrowEndDate"+result[i].rowNumber).datebox({
									validType:'[\'date\', \'checkBorrowEndDate["' + result[i].borrowEndDate + '"]\']',
									required:true,
									missingMessage:'請輸入借用迄日'
								}); */
								$("#borrowEndDate"+result[i].rowNumber).datebox({
									validType:['date', 'checkBorrowEndDate\["' + result[i].borrowEndDate + '"\]'],
									required:true,
									missingMessage:'請輸入借用迄日'
								});
								$("#borrowEndDate"+result[i].rowNumber).datebox("setValue", result[i].borrowEndDate);
							}
							textBoxsDefaultSetting($('.payText')); 
							dateBoxsDefaultSetting($('.date'));
						} else {
							$("#paymentItemMsg").html("資料狀態已更新，請重新查詢");
						}
						// 去除遮罩
						$.unblockUI();
					});
				} else {
					$("#bb").hide();
				}
			} else {
				$("#bb").hide();
				$("#isBack").val('Y');
			}
		}
		
		/**
		* 查詢數據
		*/
		function query(pageIndex, hidden) {
			var row;
			var queryParam = getQueryParam();
			var options = {
				url : "${contextPath}/assetBorrowProcess.do",
				queryParams :queryParam,
				pageNumber:pageIndex,
				autoRowHeight:true,
				onLoadError : function(data) {
					$.messager.alert('提示','查詢資料出錯','error');
				},
				onLoadSuccess:function(data){
					//查詢結果合併參考數組
		        	var arr =[{mergeFiled:"checked",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"borrowCaseId",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"borrowCategory",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"borrowStatusName",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"borrowUser",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"borrowStartDate",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"borrowEndDate",premiseFiled:"borrowCaseId"},
		        	          {mergeFiled:"rowNumber",premiseFiled:"rowNumber"}
			                  ];
		        	//合并列的field数组及对应前提条件filed（为空则直接内容合并）
					mergeCells(arr,"aaa");
					$(this).datagrid("fixRownumber","aaa"); 
					if (data.total > 0) {
						$("#allCheckButton").unbind().click(function(){allCheckButton();});
						var checkBox = $("input[name='checkBtn']").length;
						if (checkBox == 0) {
							$("#allCheckButton").hide();
						} else {
							$("#allCheckButton").show();
						}
						$("#confirmBorrow").linkbutton('enable');
						$("#unConfirmBorrow").linkbutton('enable');
					} else {
						$("#allCheckButton").hide();
						$("#confirmBorrow").linkbutton('disable');
						$("#unConfirmBorrow").linkbutton('disable');
					}
					if (hidden) {
						$("#processBorrowMsg").html("");
						$("#processBorrowMsg").html(data.msg);
					}
					hidden = true;
				}
			};
			// 清空點選排序
			if(hidden){
				options.sortName = null;
			}
			openDlgGridQuery("aaa", options);
		}
		
		/**
		* 获得查询数据
		*/
		function getQueryParam(){
			var querParam = {
				actionId : "<%=IAtomsConstants.ACTION_QUERY%>",
				borrowCaseId : $("#borrowCaseId").combobox('getValue'),
				caseCategory : $("#caseCategory").combobox('getValue'),
				caseStatus : $("#caseStatus").combobox('getValue'),
				borrowStartDate : $("#borrowStartDate").datebox('getValue'),
				borrowEndDate : $("#borrowEndDate").datebox('getValue')
			};
			return querParam;
		}
		
		/**
		* 保存處理
		*/
		function processSave() {
			javascript:dwr.engine.setAsync(false);
			var rows = $("#aaa").datagrid('getSelections');
			var processRows = $("#continueAssetTable3").datagrid("getRows");
			var obj;
			if(!$("#test22").form("validate")){
				return false;
			}
			if (processRows.length > 0) {
				for (var i = 0; i < processRows.length; i++) {
					processRows[i].serialNumber = $("#serialNumber"+processRows[i].rowNumber).textbox("getValue");
					var borrowEndDate = $("#borrowEndDate"+processRows[i].rowNumber).datebox("getValue");
					if (borrowEndDate != "" && borrowEndDate != null) {
						processRows[i].borrowEndDate = new Date(borrowEndDate).getTime();
					}
					processRows[i].borrowStartDate = null;
					processRows[i].directorCheckByDate = null;
				}
				obj = {
					saveAssetBorrowInfoDTO : JSON.stringify(processRows),
					isBack : $("#isBack").val(),
					isDirectorCheck : $("#isDirectorCheck").val(),
					comment : $("#delayComment2").textbox("getValue")
				};
			} else {
				var borrowCaseId = '';
				for (var i = 0; i < rows.length; i++) {
					borrowCaseId = borrowCaseId + rows[i].borrowCaseId + ",";
				}
				obj = {
					borrowCaseId : borrowCaseId.substring(0, borrowCaseId.length - 1),
					isBack : $("#isBack").val(),
					isDirectorCheck : $("#isDirectorCheck").val(),
					comment : $("#delayComment2").textbox("getValue")
				};
			}
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle1);
			$.ajax({
				url: "${contextPath}/assetBorrowProcess.do?actionId=saveProcess",
				data: obj,
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(json){
					$("#processBorrowMsg").html("");
					if (json.success) {
						var pageIndex = getGridCurrentPagerIndex("aaa");
						if (pageIndex < 1) {
							pageIndex = 1;
						}
						query(pageIndex, false);
						processCancel();
						$("#processBorrowMsg").html(json.msg);
					}else{
						$("#processBorrowMsg").html(json.msg);
					}
					$.unblockUI();
				},
				error:function(){
					$("#processBorrowMsg").html("");
					$("#processBorrowMsg").html("保存失敗");
					$.unblockUI();
				}
			});
			javascript:dwr.engine.setAsync(true);
		}
		
		/**
		* 取消處理
		*/
		function processCancel() {
			$('#continueAssetTable2').datagrid('loadData', {total:0, rows:[]});
			$('#continueAssetTable3').datagrid('loadData', {total:0, rows:[]});
			$("#continueDiv2").hide();
		}
		
		/**
		 * 設置datagrid內部複選框選中
		*/
		function settingCheckbox(row, index){
			if (row.borrowStatus == "PROCESS") {
				return '';
			}
			if ("${userRole}" == '02' && row.isAgentProcess == 'N') {
				$("#isDirectorCheck").val('Y');
				return '<input name="checkBtn" type="checkbox" onclick = "checkBorrowFunction(this);">';
			}
			if ("${userRole}" == '01' && row.isAgentProcess == 'Y') {
				$("#isDirectorCheck").val('N');
				return '<input name="checkBtn" type="checkbox" onclick = "checkBorrowFunction(this);">';
			}
			return '';
		}
		
		/**
		*　加載處理按鈕
		*/
		function showText(id, isText){
			var returnInput = "";
			if (id != "" && id != null) {
				if (isText) {
					returnInput = "<input class='easyui-textbox textBox' id="+id+" style='width:100%; height: 50px;' maxlength='200'/>";
				} else {
					returnInput = "<input class='easyui-datebox date' maxlength='10' style='width:100%' id='"+id+"'/>";
				}
			} else {
				var s = '<a href="javascript:void(0)" style= "color:blue" onclick="processSave();">儲存</a> ';
				//Task #2987
				var c = '<a href="javascript:void(0)" style= "color:blue" onclick="processCancel();">取消</a> ';
				returnInput = s + c;
			}
			return returnInput;
		}
		
		/**
		* 加載經辦處理區塊
		*/
		function settingTable3box(value,row,index, isSerialNumber, id){
			if (isSerialNumber == "true") {
				if (row.borrowCategory == "續借") {
					return value;
				} else {
					return "<input class=\"easyui-textbox payText\" id=\"" + id + "\" maxlength='20' style='width:200px;border-radius: 4px; border: #95B8E7 1px solid;' />";
				}
			} else {
				return "<input class=\"easyui-datebox date\" maxlength='10' style='width:100%' id='" + id + "'/>";
			}
		}
		
		/**
		* 單擊復選框選中／取消選中
		*/
		function checkBorrowFunction(obj) {
			var index = $(obj).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
			$(obj).prop("checked", obj.checked?false:true);
		}
		
		/*
		* 處理全選按鈕的選中
		*/
		function allCheckButton(obj){
			var selectRows = $("#aaa").datagrid("getSelections");
			var rows = $("#aaa").datagrid("getRows");
			var borrowCaseId = '';
			var i = 0;
			var isCheck = false;
			$("input[name='checkBtn']").each(function(index, obj1){
				isCheck = false;
				var index = $(obj1).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
				if (borrowCaseId == '') {
					isCheck = true;
					$("#aaa").datagrid("selectRow", rows[index].rowIndex);
					borrowCaseId = rows[index].borrowCaseId;
					i = 1;
				}
				if (borrowCaseId != rows[index].borrowCaseId) {
					isCheck = true;
					borrowCaseId = rows[index].borrowCaseId;
					i++;
				}
				if (obj != null && obj != undefined && isCheck) {
					if (obj.checked) {
						$("#aaa").datagrid("selectRow", index);
					} else {
						$("#aaa").datagrid("unselectRow", index);
					}
					$(obj1).prop("checked", obj.checked?true:false);
				}
			});
			if (obj == null || obj == undefined) {
				if(selectRows.length == i){
					$("#allCheckButton").prop("checked", true);
				} else {
					$("#allCheckButton").prop("checked", false);
				}
			}
		}
		
		/*
		* 選中一行
		*/
		function dataGridOnSelect(index,　row){
			if (((row.isAgentProcess == 'N') && ("${userRole}" == "01"))
					|| ((row.isAgentProcess == 'Y') && ("${userRole}" == "02")) || (row.borrowStatus == 'PROCESS')) {
				alert("案件狀態不符，不可進行操作");
				$("#aaa").datagrid("unselectRow", index);
				return false;
			}
			var obj = $("#aaa").closest(".datagrid-wrap").find(".datagrid-view2").find("[datagrid-row-index='"+ index +"']").find("input[name='checkBtn']");
			if (obj.length > 0) {
				if (obj.prop("checked")) {
					obj.prop("checked", false);
					$("#aaa").datagrid("unselectRow", index);
				} else {
					obj.prop("checked", true);
				}
			}
			// 處理全選按鈕的選中
			allCheckButton();
		}
		
		function dataGridUnCheck() {
			alert("333");
		}
	</script>
</body>
</html>