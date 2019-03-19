
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"  %>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetTransInfoFormDTO assetTransInfoFormDTO = null;
	//獲取當前登錄者ID
	String userId = logonUser.getId();
	String useNo = null;
	DmmAssetTransInfoDTO assetTransInfoDTO = null;
	if (ctx != null) {
		assetTransInfoFormDTO = (AssetTransInfoFormDTO) ctx.getRequestParameter();
		if(assetTransInfoFormDTO != null){
	useNo = assetTransInfoFormDTO.getUseCaseNo();
	assetTransInfoDTO = assetTransInfoFormDTO.getAssetTransInfoDTO();
		}
	} else {
		assetTransInfoFormDTO = new AssetTransInfoFormDTO();
	}
	//依使用者控管權限取得轉出倉庫列表
	List<Parameter> fromWareHouseList = (List<Parameter>) SessionHelper.getAttribute(request, useNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
	//倉庫列表
	List<Parameter> wareHouseList = (List<Parameter>) SessionHelper.getAttribute(request, useNo, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST);
	//轉倉批號列表
	List<Parameter> assetTransInfoList = (List<Parameter>) SessionHelper.getAttribute(request, useNo, IAtomsConstants.ACTION_GET_ASSET_TRANS_ID_LIST);
	//通知人員列表
	//List<Parameter> userList = (List<Parameter>)SessionHelper.getAttribute(request, useNo, IAtomsConstants.PARAM_WARE_HOUSE_USER_NAME_LIST);
	//获取上传最大限制
	String uploadFileSize = assetTransInfoFormDTO.getUploadFileSize();
%>
<c:set var="fromWareHouseList" value="<%=fromWareHouseList%>" scope="page"></c:set>
<c:set var="wareHouseList" value="<%=wareHouseList%>" scope="page"></c:set>
<c:set var="assetTransInfoList" value="<%=assetTransInfoList%>" scope="page"></c:set>
<c:set var="assetTransInfoDTO" value="<%=assetTransInfoDTO%>" scope="page"></c:set>
<c:set var="userId" value="<%=userId%>" scope="page"></c:set>
<c:set var="useNo" value="<%=useNo%>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
<div id="transDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<div id="p" region="center" class="easyui-panel" title="設備轉倉作業" style="width: 98%; height: auto; " >
		<div class="easyui-tabs" style="width: 100%; height: auto;">
			<div id="dlg" title="轉出" style="padding: 10px">
			<div>
						<span id="message" class="red"></span>
			</div>
				<form id="saveTransInfoForm" method="post" action="<%=IAtomsConstants.ASSET_TRANS_INFO%>" novalidate>
					<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" />
				<table cellpadding="6" >
					<tr>
						<td>轉倉批號:</td>
						<td>
							<cafe:droplisttag 
								id="<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>" 
								name="<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>" 
								css="easyui-combobox"
								hasBlankValue="true" 
								result="${assetTransInfoList}"
								blankName="新增批號"
								style="width:200px"
								javascript="editable=false  data-options=\"valueField:'value',textField:'name'\" validType=\"ignore['請選擇']\" invalidMessage=\"請輸入轉出倉\" ">
							</cafe:droplisttag>
							<input type="hidden" id="isBack">
						</td>
						
						<td>轉出倉:<span class="red">*</span></td>
						<td>
							<cafe:droplisttag 
								id="<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>" 
								name="<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>" 
								css="easyui-combobox"
								result="${fromWareHouseList}"
								style="width:300px"
								hasBlankValue="true" 
								blankName="請選擇"
								javascript="editable=false  validType=\"ignore['請選擇']\" invalidMessage=\"請輸入轉出倉\"">
							</cafe:droplisttag>
						</td>
						<td>轉入倉:<span class="red">*</span></td>
						<td>
							<cafe:droplisttag 
								id="<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>" 
								name="<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>" 
								css="easyui-combobox"
								result="${wareHouseList}"
								hasBlankValue="true" 
								style="width:300px"
								blankName="請選擇"
								javascript="editable=false  validType=\"ignore['請選擇']\" invalidMessage=\"請輸入轉入倉\"">
							</cafe:droplisttag>
							<input type="hidden" id="saveToWarehouseId"/>
						</td>
					</tr>
					<tr>
						<td>說明:</td>
						<td colspan="5">
							<input maxLength="200" name="<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>" id="<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>" class="easyui-textbox" data-options="multiline:true,validType:'maxLength[200]'" style="height: 50px; width: 425px" >
					</tr>
					<tr>
						<td colspan="6" style="text-align: right;">
							<a id="saveTrans" href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" onclick="saveTransInfo()">儲存</a>
							<a id="deleteTrans" href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-cancel" onclick="deleteTransInfo()">刪除</a></td>
					</tr>
				</table>
				</form>
				<div>
					<span id="transListMsg" class="red"></span>
				</div>
				<form id="saveListForm"  method="post" novalidate>
					<div>
						設備序號:<span class="red">*</span> 
						<input id="<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>" maxlength="20" class="easyui-textbox" type="text" data-options="required:true, validType:['maxLength[20]','englishOrNumber[\'設備序號限英數字，請重新輸入\']']" missingMessage="請輸入設備序號"></input>
						<a href="#" id="addTrans" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="appendData()">新增</a>
						<a href="#" id="deleteList" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="removeit()">刪除</a>
						<div id="btnUpload" style ="display: none">
							<a href="#" id="fildDownLoad" onclick="downLoadFile();" style="width: 150px;color: blue;" >匯入格式下載</a>&nbsp;
							<cafe:fileuploadTag 
		                           		id="fileUpLoad"
		                           		name="fileUpLoad"
		                           		uploadUrl="${contextPath}/assetTransfer.do" 
		                           		acceptFiles=".xls,.xlsx"
	                           			allowedExtensions="'xls', 'xlsx'"
	                           			sizeLimit = "${uploadFileSize }"
		                           		showFileList="false"
		                           		showName="匯入"
		                           		whetherImport="true"
		                           		messageAlert="false"
		                           		showUnderline = "true"
		                           		whetherDelete="false"
		                           		multiple="false"
		                           		whetherDownLoad="true"
		                           		javaScript="onSubmit:function(id,name){fileUpLoad.setParams({'actionId':'upload', 'assetTransId': $('#assetTransId').combobox('getValue') });},
		                           			onComplete:showMessage,onError:function(id, name, reason, maybeXhrOrXdr){$('#message').text(reason);}">
							</cafe:fileuploadTag> 
						</div>
					</div>
					<input type="hidden" id="assetTransListRow" name="assetTransListRow"/>
                    <div id="tb1">
                    <table cellpadding="6"  style="width: 100%;">
                    <tr>
                       <td align="left" >
                            <a href="#" id="saveComment" class="easyui-linkbutton" onclick="saveTransComment()">儲存轉倉說明</a>
                            <a href="#" id="exportAsset" class="easyui-linkbutton" onclick="exportAssetOrder()">列印出貨單</a>
                      </td>
                      <td align="right">
                        <div>
		                	通知人員：
		                	<cafe:droplisttag 
		                				id="<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>" 
										name="<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>" 
										css="easyui-combobox"
										hasBlankValue="true" 
										blankName="請選擇(複選)"
										style="width:138px" 
										javascript="multiple=true editable=false data-options=\"valueField:'value',textField:'name'\"">
									</cafe:droplisttag>
		                    <a href="#" id="confirmTrans"  class="easyui-linkbutton" iconcls="icon-ok" onclick="confirmTrans()" > 確認轉倉</a>
		                    <a href="#" id="cancleTrans" class="easyui-linkbutton" iconcls="icon-cancel" onclick="cancleTrans()" >取消轉倉</a>
		                	 <a href="#" id="exportData" style="width: 150px;color: blue;" onclick="exportTransData()">匯出</a>
		                </div>
		             </td>
		             </tr>
		             </table>
                    </div>
                    <table id="datagrid" >
                    </table>
                    <input type="hidden" id="updateComment"/>
                </form>
            </div>
			<div title="轉入驗收" data-options="onLoad:function(){$('#queryNumber').textbox('disable');},href:'${contextPath}/dmmCheckTransInfo.do?actionId=initCheck',closed:true" style="padding:10px"></div>
			<div title="歷史轉倉查詢" data-options="onLoad:function(){textBoxSetting('initHist'); dateboxSetting('initHist');},href:'${contextPath}/assetTransInfoHistory.do?actionId=initHist',closed:true" style="padding:10px;"></div>
	</div>
</div>
	<script type="text/javascript">
	
		var isButtonEvent = false;
		var isOpenBlockStyle = true;
		/**
		 * 頁面加載完成時執行
		 */
		$(function() {
			//將按鈕禁用
			disableTransButton();
			//隱藏取消轉倉按鈕
			document.getElementById("cancleTrans").style.display = "none";
			$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("disableValidation");
			$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('disable');
			// 轉倉批號改變時觸發事件 
			$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox({
				onChange: function(newValue, oldValue){
					var blockStyleGlobal = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					if (isOpenBlockStyle) {
						$.blockUI(blockStyleGlobal);
					}
					$("#updateComment").val("");
					$("#transListMsg").html("");
					//清除通知人員下拉框的值 
					$("#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>").combobox('loadData',initSelect([],'請選擇(複選)'));
					$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("setValue", "");
					if (!isButtonEvent) {
						$("#message").empty();
					}
					isButtonEvent = false;
					//newValue為空，則代表為新增轉倉批號，所有欄位都可輸入，且只有新增按鈕可以使用
					if (isEmpty(newValue)) {
						$("#isBack").val("Y");
						$("#saveToWarehouseId").val("");
						// 信息變為可以修改 
						$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>').combobox('enable');
						$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>').combobox('enable');
						$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>').textbox('enable');
						//清空轉出倉和轉入倉 
						$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>').combobox('setValue','');
						$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>').combobox('setValue','');
						$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox('setValue','');
						$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>').textbox('setValue','');
						//清空轉倉清單 
						//$('#datagrid').datagrid('loadData', { total: 0, rows: [] });
						queryAssetList('', 1, false, true);
						//將按鈕變為不可用
						disableTransButton();
						$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("disableValidation");
						$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('disable');
						$('#saveTrans').linkbutton('enable');
						$("#datagrid").datagrid("hideColumn",'v');
						//設備序號輸入框清空
						$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("clear");
					} else {
						//選擇轉倉批號,根據批號顯示頁面信息 
						ajaxService.getAssetTransInfoByAssetTransId(newValue,function(data){
							if(data){
								//隱藏取消轉倉按鈕
								document.getElementById("cancleTrans").style.display = "none";
								//根據轉倉批號將數據加載到頁面 
								$("#<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>").combobox("setValue", data.fromWarehouseId);
								$("#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>").combobox("setValue", data.toWarehouseId);
								var selectOptions = document.getElementById("<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>").options;
								var isExist = checkExistValue(selectOptions, data.fromWarehouseId);
								if (!isExist) {
									$("#<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>").combobox("setValue", "");
								}
								selectOptions = document.getElementById("<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>").options;
								isExist = checkExistValue(selectOptions, data.toWarehouseId);
								if (!isExist) {
									$("#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>").combobox("setValue", "");
								}
								$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>').textbox('setValue',data.comment);
								//轉出倉不可修改
								$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>').combobox('disable');
								$("#saveToWarehouseId").val(data.toWarehouseId);
								//判斷選擇的轉倉批號是否是被退回的轉倉
								if (data.isBack == '<%=IAtomsConstants.YES%>') {
									loadCname([]);
									$("#isBack").val("");
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>').combobox('disable');
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>').textbox('disable');
									//將按鈕變為不可用 
									disableTransButton();
									$('#saveTrans').linkbutton('disable');
									$('#cancleTrans').linkbutton('enable');
									$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("disableValidation");
									$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('disable');
									//設備序號輸入框清空
									$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("clear");
									//顯示取消轉倉按鈕
									document.getElementById("cancleTrans").style.display = "";
									$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox('disable');
									$("#datagrid").datagrid("hideColumn",'v');
									//根據轉倉批號查詢到轉倉清單中的信息 
									queryAssetList(newValue, 1, true, true);
								} else {
									$("#isBack").val("Y");
									// 信息變為可以修改 
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>').combobox('enable');
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.COMMENT.getValue()%>').textbox('enable');
									$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox('enable');
									//將按鈕恢復可用狀態 
									enableTransButton();
									$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('enable');
									$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("enableValidation");
									//Task #3084 設置聚焦，用於掃碼槍掃描
									$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('textbox').focus();
									$("#datagrid").datagrid("showColumn",'v');
									//根據轉倉批號查詢到轉倉清單中的信息 
									queryAssetList(newValue, 1, false, true);
									//重新記載通知人員下拉框，取得具有轉入倉檢視權限的使用者帳號資料 
									 ajaxService.getWareHouseUserNameList(newValue, function(data) {
										if (data) {
											loadCname(data);
										}
									});
								}
							} else {
								$.messager.alert('提示', "error", 'error');
							}
						
						});
					}
					if (isOpenBlockStyle) {
						$.unblockUI();
					} else {
						isOpenBlockStyle = true;
					}
					
				}
			});
			// 查詢datagrid初始化
			setTimeout("initTransOutDatagrid();",5);
			//Task #3084
			codeGunAction();
		});
		/*
		*掃碼槍事件Task #3084
		*/
		function codeGunAction(){
			$("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("textbox").keydown(function(event){
		   		//如果該輸入框聚焦
		   		if($("#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>").textbox("textbox").is(":focus")){
		   			//如果按下tab鍵或者enter鍵
		   			if (event.keyCode == 9 || event.keyCode == 13) {
		   				setTimeout(function () {
		   					appendData()
		   				}, 100);
         			}
   				} 	 
			});
		}
		/*
		*初始化查詢datagrid1
		*/
		function initTransOutDatagrid(){
			var grid = $("#datagrid");
			if(!grid.hasClass("easyui-datagrid")){
				grid.attr("style", "width: 100%; height: auto;");
				// 查詢列
				var datagridColumns = [
					{field:'v',width:40,checkbox:true},
					{field:'serialNumber',sortable:true,halign:'center',width:150,title:"設備序號"},
					{field:'name',width:200,halign:'center',sortable:true,title:"設備名稱"},
					{field:'isEnabled',halign:'center',align:'center',width:90,formatter:function(value,row){if('<%=IAtomsConstants.YES%>' == row.isEnabled){return '<input type=checkbox checked  disabled/>';}else{ return '<input type=checkbox disabled/>';}},title:"已啟用"},
					{field:'isCup',width:90,halign:'center',align:'center',formatter:function(value,row){if('<%=IAtomsConstants.YES%>'== row.isCup){return '<input type=checkbox checked  disabled/>';}else{ return '<input type=checkbox disabled/>';}},title:"銀聯"},
					{field:'comment',width:240,halign:'center',sortable:true, editor: {type: 'textbox',options:{required : true}},formatter:function(value,row){return showText(value,row.assetTransListId);},title:"轉倉說明"},
					{field:'contractCode',width:160,halign:'center',sortable:true,title:"合約編號"},
					{field:'assetUser',width:160,halign:'center',sortable:true,hidden:true,title:""},
					{field:'assetUserName',width:160,halign:'center',sortable:true,title:"使用人"},
					{field:'updatedByName',halign:'center',width:180,sortable:true,title:"異動人員"},
					{field:'updatedDate',halign:'center',align:'center',width:170,sortable:true,formatter:formatToTimeStamp,title:"異動日期"},
				];
				// 創建datagrid
				grid.datagrid({
					title:"轉倉清單",
					columns:[datagridColumns],
					rownumbers:true,
					pagination:true,
					iconCls: 'icon-edit',
					toolbar: '#tb',
					pageNumber: 0,
					pageList:[15,30,50,100],
					pageSize:15,
					onClickRow: onClickRow,
					onSelect: onSelect,
					onUnselect: onUnselect,
					onSelectAll: onSelectAll,
					onUnselectAll: onUnselectAll,
					onCheck: deleteButton,
					onCheckAll: deleteButton,
					onUncheck: deleteButton,
					nowrap:false,
					onUncheckAll: deleteButton
				});
			}
		}
		
		
		/*
		 * 儲存轉倉信息
		 */
		function saveTransInfo() {
			$("#message").empty();
			$("#transListMsg").empty();
			var controls = ['<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>', '<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>'];
			if (validateForm(controls) && $('#saveTransInfoForm').form("validate")) {
				isOpenBlockStyle = false;
				// 調保存遮罩
				commonSaveLoading('dlg');
				//轉出倉的值 
				var fromWareHouse = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.FROM_WAREHOUSE_ID.getValue()%>').combobox("getValue");
				//轉入倉的值 
				var toWareHouse = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.TO_WAREHOUSE_ID.getValue()%>').combobox("getValue");
				//判斷轉出倉和轉入倉不能相同
				if (fromWareHouse == toWareHouse) {
					// 去除保存遮罩
					commonCancelSaveLoading('dlg');
					$("#message").text("轉出倉不可與轉入倉相同");
					return;
				}
				$.ajax({
					url:"${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_SAVE%>",
					data: $('#saveTransInfoForm').form("getData"),
					type: 'post', 
					cache: false, 
					dataType: 'json', 
					success: function(result) {	
						// 去除保存遮罩
						commonCancelSaveLoading('dlg');
						if (result.success) {
							if(result.row){
								isButtonEvent = true;
								javascript:dwr.engine.setAsync(false);
								//重新加載數據 
								ajaxService.getAssetTransIdList('assetTranferOut', '${userId}', function(data) {
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox('loadData', initSelect(data,"新增批號"));
								});
								javascript:dwr.engine.setAsync(true);
								$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("setValue", result.row);
							}
							//顯示消息 
							$("#message").text(result.msg);
							$("#saveToWarehouseId").val(toWareHouse);
							//Task #3084 設置聚焦，用於掃碼槍掃描
							$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('textbox').focus();
						} else {//2018/01/12 当后台验证卡住时，前台提示消息 
							//顯示消息 
							$("#message").text(result.msg);
						}
					},
					error:function(){
						// 去除保存遮罩
						commonCancelSaveLoading('dlg');
						$("#message").text("轉倉批號儲存失敗");
					}
				});
			}
		}
		/*
		 *刪除轉倉信息 
		 */
		function deleteTransInfo() {
			$("#message").empty();
			$("#transListMsg").empty();
			//轉倉批號 
			var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
			comfirmDelete(function(){
				isOpenBlockStyle = false;
				var blockStyleGlobal = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyleGlobal);
					$.ajax({
						url:"${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_DELETE%>",
						data: { assetTransId : assetTransId },
						type: 'post', 
						cache: false, 
						dataType: 'json', 
						success: function(result) {
							if (result.success) {
								isButtonEvent = true;
								//顯示消息 
								$("#message").text(result.msg);
								//重新加載數據 
								ajaxService.getAssetTransIdList('assetTranferOut', '${userId}', function(data){
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox('loadData', initSelect(data, "新增批號"));
								});
								$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("setValue", '');
							}
							$.unblockUI();
						},
						error:function(){
							$("#message").text("刪除轉倉信息失敗");
							$.unblockUI();
						}
					});
			});
		}
		/*
		 *根據輸入的設備序號給轉倉清單新增一行
		 */
		function appendData() {
			$("#message").empty();
			$("#transListMsg").empty();
			//轉倉批號 
			var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
			//設備序號 
			var serialNumber = $('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox("getValue");
			if ($('#saveListForm').form("validate")) {
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				//根據轉倉批號顯示頁面信息
				$.ajax({
					url:"${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_ADD_ASSET_TRANS_LIST%>",
					data: { assetTransId : assetTransId ,
							serialNumber : serialNumber	},
					type: 'post', 
					cache: false, 
					dataType: 'json', 
					success: function(data) {
						//顯示消息 
						$("#transListMsg").text(data.msg);
						if (data.success) {
							//查詢轉倉清單
							enableTransButton();
							var pageIndex = calDeletePagerIndex("datagrid");
							queryAssetList(assetTransId, pageIndex, false, true);
							//儲存成功清空【設備序號】欄位 
							$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('setValue','');
							//Task #3084 設置聚焦，用於掃碼槍掃描
							$('#<%=DmmAssetTransListDTO.ATTRIBUTE.SERIAL_NUMBER.getValue()%>').textbox('textbox').focus();
						}
						$.unblockUI();
					}, 
					error: function() {
						$("#transListMsg").html("設備新增失敗");
						$.unblockUI();
					}
				}); 
			}
			
		}
		/*
		* 刪除轉倉清單信息
		*/ 
		function removeit() {
			$("#message").empty();
			$("#transListMsg").empty();
			//轉倉批號 
			var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
			//得到選中行 
			var rows = $('#datagrid').datagrid('getSelections');
			for(var i = 0; i<rows.length; i++){
				//將json中的日期數據置空 
				rows[i].updatedDate = null;
				//得到說明信息
				rows[i].comment = $("#"+rows[i].assetTransListId).val();
			}
			// 將所有的行數據轉化為JSON格式
			var assetTransListRow = JSON.stringify(rows);
			//將JSON格式放到隱藏域中 
			$("#saveListForm").find("#assetTransListRow").val(assetTransListRow);
			// 取form表單的所有的值
			var saveParam = $("#saveListForm").form("getData");
			if (rows != '') {
				comfirmDelete(function() {
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle1);
						$.ajax({
							url:"${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_DELETE_ASSET_TRANS_LIST%>",
							data: saveParam,
							type: 'post', 
							cache: false, 
							dataType: 'json', 
							success: function(data) {
								//顯示消息 
								$("#transListMsg").text(data.msg);
								var pageIndex = calDeletePagerIndex("datagrid");
								queryAssetList(assetTransId, pageIndex, false, true);
								$.unblockUI();
							}, 
							error: function() {	
								$("#transListMsg").html("設備刪除失敗");
								$.unblockUI();
							}
						}); 
				});	
			} else {
				$("#transListMsg").html("請勾選資料");
			}
		}
		/*
		 *儲存轉倉說明
		 */
		function saveTransComment() {
			$("#message").empty();
			$("#transListMsg").empty();
			
			//轉倉批號 
			var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
			//得到所有行的數據 
			var data = $('#datagrid').datagrid('getData');
			var rows = data.rows;
			var updateRows = $("#updateComment").val();
			updateRows = updateRows.split(",");
			var data =[];
			var obj = new Object();
			 for(var i = 0; i<rows.length; i++){
				if (updateRows.contains(rows[i].assetTransListId)){
					obj = new Object();
					obj.assetTransListId=rows[i].assetTransListId;
					obj.comment=$("#"+rows[i].assetTransListId).val();
					data.push(obj);
				}
			}
			 if(data != null ) {
				 //不進行修改數據，不進行存儲
				if(data.length > 0) {
					//將JSON格式放到隱藏域中 
					$("#assetTransListRow").val(JSON.stringify(data));
					if(JSON.stringify(data) == "" || JSON.stringify(data) == null) {
						$("#transListMsg").html("轉倉說明未修改");
						return false;
					}
					// 取form表單的所有的值
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle1);
					var saveParam = $("#saveListForm").form("getData");
					 $.ajax({
						url:"${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_UPDATE%>",
						data: saveParam,
						type: 'post', 
						cache: false, 
						dataType: 'json', 
						success: function(data) {
							//顯示消息 
							var pageIndex = calDeletePagerIndex("datagrid");
							queryAssetList(assetTransId, pageIndex,false, true);
							$("#transListMsg").text(data.msg);
							$("#updateComment").val("");
							$.unblockUI();
						}, 
						error: function() {
							$("#transListMsg").html("轉倉批號儲存失敗");
							$.unblockUI();
						}
					});
				} else {
					$("#transListMsg").html("轉倉說明未修改");
				}
			}
		}
		/*
		 *確認轉倉
		 */ 
		function confirmTrans(){
			$("#message").empty();
			$("#transListMsg").empty();
			var saveToWarehouseId = $("#saveToWarehouseId").val();
			var toWarehouseId = $("#toWarehouseId").combobox("getValue");
			if (toWarehouseId != saveToWarehouseId) {
				handleScrollTop('transDiv'); 
				$("#message").html("轉入倉已變更，請重新儲存");
				return false;
			}
			//轉倉批號 
			var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
			//通知人員Id
			var toMailId = getMultiSelValues('<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>');
			comfirmCommon("確認轉倉?",function() {
					//得到所有行的數據 
					var rows = $('#datagrid').datagrid('getRows');
					if (rows != '') {
						isOpenBlockStyle = false;
						var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
						$.blockUI(blockStyle);
						//根據轉倉批號顯示頁面信息
						$.ajax({ 
							url: "${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_TRANSFER_WAREHOUSE%>",
							data: { assetTransId : assetTransId,
									toMailId : toMailId },
							type: 'post', 
							cache: false, 
							dataType: 'json', 
							success: function(result) {
								if(result.success){
									isButtonEvent = true;
									//顯示消息 
									$("#message").text(result.msg);
									javascript:dwr.engine.setAsync(false);
									//重新加載轉倉批號下拉框數據 
									ajaxService.getAssetTransIdList('assetTranferOut', '${userId}', function(data){
										$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox('loadData', initSelect(data, "新增批號"));
									});
									$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("setValue", '');
									if(typeof queryAssetTransList != 'undefined' && queryAssetTransList instanceof Function){
										//重新加載轉入驗收頁面下拉框
										ajaxService.getAssetTransIdList('assetTranferinCheck','${userId}',function(data){
											if(data){
												data = initSelect(data,"請選擇");
												//清除之前下拉框的值
												$("#queryAssetTransId").combobox('clear');
												//清空轉倉清單 
												$('#dataGrid').datagrid('loadData', { total: 0, rows: [] });
												queryAssetTransList('');
												//將後台得到的值賦值進相應的ID
												$("#queryAssetTransId").combobox({
													data:data,
													valueField:'value',
													textField:'name'
												});
											}
										});
									}
									$("#message").html(result.msg);
									javascript:dwr.engine.setAsync(true);
									$.unblockUI();
								} else {
									$("#message").html(result.msg);
									$.unblockUI();
								}
							}, 
							error: function() {
								$.messager.alert('提示', "轉倉失敗", 'error');		
								$.unblockUI();
							}
						}); 
					} else {
						//轉倉清單若無資料，則顯示錯誤訊息「無轉倉設備，不可轉倉」
						$("#message").text("無轉倉設備，不可轉倉");
					}
			});
		}
		/*
		 *取消轉倉
		 */ 
		function cancleTrans(){
			$("#message").empty();
			$("#transListMsg").empty();
			comfirmCommon("確認取消轉倉?",function() {
				//轉倉批號 
				isOpenBlockStyle = false;
				var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
				var blockStyleGlobal = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyleGlobal);
				//根據轉倉批號顯示頁面信息
				$.ajax({ 
					url: "${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_CANCLE_TRANSFER_WAREHOUSE%>",
					data: { assetTransId : assetTransId},
					type: 'post', 
					cache: false, 
					dataType: 'json', 
					success: function(result) {
						isButtonEvent = true;
						if(result.success){
							//顯示消息 
							$("#message").html(result.msg);
							//重新加載轉倉批號下拉框數據 
							ajaxService.getAssetTransIdList('assetTranferOut', '${userId}', function(data){
								$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox('loadData', initSelect(data, "新增批號"));
							});
							$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("setValue", '');
							document.getElementById("cancleTrans").style.display = "none";
						}  else {
							$("#message").html(result.msg);
						}
						$.unblockUI();
					}, 
					error: function() {
						$.messager.alert('提示', "轉倉失敗", 'error');
						$.unblockUI();
					}
				}); 
			});
		}
		
		/*
		 *頁面匯出操作
		 */
        function exportTransData(){
        	$("#message").empty();
        	$("#transListMsg").empty();
        	var rowLength = getGridRowsCount("datagrid");
			if(rowLength >= 1){
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle);
				
				actionClicked( document.forms["saveTransInfoForm"],
					'${useNo}',
					'',
					'<%=IAtomsConstants.ACTION_EXPORT%>');
				
				ajaxService.getExportFlag('${useNo}',function(data){
					$.unblockUI();
				});
			}
		}
		/**
		 * 下載文本
		 */
		function downLoadFile(){
			$("#message").empty();
			createSubmitForm("${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>","post");
		}
		
		/**
		 * 匯入返回處理
		 */
		function showMessage(id, fileName, response, maybeXhrOrXdr) {
			$("#message").empty();
			$("#transListMsg").empty();
			//保存數據時，在上傳文件前驗證SESSION是否過期
			if (maybeXhrOrXdr) {
				if (!sessionTimeOut(maybeXhrOrXdr)) {
					return false;
				}
			}
			if (response.success) {
				$("#message").text(response.msg);
				var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
				var pageIndex = calDeletePagerIndex("datagrid");
				queryAssetList(assetTransId, pageIndex, false, true);
				//$('#datagrid').datagrid('reload');
				
				$('#exportData').attr("onclick","exportTransData()");
				$('#exportData').attr("style","color:blue;");
				$('#saveComment').linkbutton('enable');
				$('#exportAsset').linkbutton('enable');
			} else {
				if(response.msg && response.errorFileName == null){
					$("#transListMsg").text(response.msg);
				} else {
			   		if (response.errorFileName != "" || response.errorFileName){
			   			$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(confirm) {
							if (confirm) {
								createSubmitForm("${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE%>&errorFileName=" + response.errorFileName + "&tempFilePath= " + response.tempFilePath,"post");
							}
						});
			  		}
			  	}
			}
		}	
		/*
		 *列印出庫單
		 */
		function exportAssetOrder(){
			$("#message").empty();
			//轉倉批號 
			var assetTransId = $('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox("getValue");
			var rowLength = getGridRowsCount("datagrid");
			if (rowLength >= 1) {
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle);
				
				
				window.location.href='${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=exportAsset&assetTransId='+assetTransId;
				
				ajaxService.getExportFlag('${useNo}',function(data){
					$.unblockUI();
				});
			}
		}
		
		/**
		*對轉倉清單的刪除按鈕進行的操作
		*/
		function deleteButton() {
			var rows = $("#datagrid").datagrid("getSelections");
			if (rows !=''){
				if ($("#isBack").val() == 'Y') {
					//刪除按鈕變為enable狀態
					$("#deleteList").linkbutton('enable');
				}
			} else {
				$('#deleteList').linkbutton('disable');
			}
		}
		/**
		*　點選datagrid中的一行時
		*/
		function onClickRow() {
			deleteButton();
		}
		/**
		*點選複選框前的對鈎時觸發
		*/
		function onSelect(){
			deleteButton();
		}
		/**
		*取消對鈎取消選中一行時觸發
		*/
		function onUnselect() {
			deleteButton();
		} 
		/**
		*點擊全選按鈕時觸發
		*/
		function onSelectAll() {
			deleteButton();
		} 
		/**
		*取消全選時觸發的事件
		*/
		function onUnselectAll() {
			$('#deleteList').linkbutton('disable');
		}
		/**
		 *顯示轉倉說明
		 */
		function showText(value,assetId){
			var isBack = $("#isBack").val();
			if (isBack == 'Y') {
				if (value == null){
					return "<input class='textbox1' id="+assetId+" style='border-radius: 4px; border: #95B8E7 1px solid; width:100%;' onchange='descOnChange(\""+assetId+"\");' maxlength='200' value=''/>";
				} else {
					return "<input class='textbox1' id="+assetId+" style='border-radius: 4px; border: #95B8E7 1px solid; width:100%;' onchange='descOnChange(\""+assetId+"\");' maxlength='200' value='"+value+"'/>";
				}
			} else {
				if (value == null){
					return "<input class='textbox1' id="+assetId+" style='border-radius: 4px; border: #95B8E7 1px solid; width:100%;' onchange='descOnChange(\""+assetId+"\");' maxlength='200' value='' disabled=\"disabled\"/>";
				} else {
					return "<input class='textbox1' id="+assetId+" style='border-radius: 4px; border: #95B8E7 1px solid; width:100%;' onchange='descOnChange(\""+assetId+"\");' maxlength='200' value='"+value+"' disabled=\"disabled\"/>";
				}
			}
			
		}
		/**
		* 轉倉說明異動方法
		*/
		function descOnChange(value){
			var id= $("#updateComment").val();
			$("#updateComment").val(id + "," + value);
		}
		
		$('#datagrid').datagrid({
			onLoadSuccess: function (data) {
				$('.textbox1').textbox();
			},
		});
		//通知人員下拉框多選
		$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox({
			onSelect : function(newValue) {
				selectMultiple(newValue, '<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>')
			},
		});
		$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox({
			onUnselect : function(newValue) {
				unSelectMultiple(newValue, '<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>')
			},
		});
		//獲取復選下拉框的值
		function getMultiSelValues(selName) {
			var result = "";
			var sels = document.getElementsByName(selName);
			if (sels != null) {
				for (var i = 0; i < sels.length; i++) {
					if (sels[i].value != null && sels[i].value != "") {
						result += sels[i].value + ",";
					}
				}
				result = result.substring(0, result.length - 1);
			}
			return result;
		}
		/**
		 *根據轉倉批號查詢轉倉清單
		 *assetTransId：轉倉批號
		 *isBack:是否返回
		 */
		function queryAssetList(assetTransId, pageIndex, isBack, isHiddenMsg){
			 //顯示轉倉清單 
			var options = {
				url:"${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=<%=IAtomsConstants.ACTION_QUERY%>&isHistory=false",
				queryParams: {assetTransId : assetTransId},
				pageNumber: pageIndex,
				onLoadError : function(data) {
					var pageIndex = calDeletePagerIndex("datagrid");
					/* if (pageIndex != 1) {
						$("#message").text("");
					}
					$("#transListMsg").text(""); */
					if (!isHiddenMsg) {
						$("#message").text("");
						$("#transListMsg").text("");
					} else {
						isHiddenMsg = false;
					}
					$.messager.alert('提示','查詢轉倉清單出錯','error');
				},
				onLoadSuccess:function(data){
					/* if(!isBack) {
						$("#transListMsg").text("");
					} */
					if (!isHiddenMsg) {
						$("#message").text("");
						$("#transListMsg").text("");
					} else {
						isHiddenMsg = false;
					}
					var pageIndex = calDeletePagerIndex("datagrid");
					/* if (pageIndex != 1) {
						$("#message").text("");
					} */
					$('#deleteList').linkbutton('disable');
					//判斷datagrid中是否有數據
					/* if (!isBack) {
						enableTransButton();
					} else {
						$('#exportData').attr("onclick","exportTransData()");
						$('#exportData').attr("style","color:blue;");
						$('#saveComment').linkbutton('enable');
						$('#exportAsset').linkbutton('enable');
					} */
					if (data.total == 0) {
						$('#exportData').attr("onclick","return false;");
						$('#exportData').attr("style","color:gray;");
						$('#exportAsset').linkbutton('disable');
						$('#saveComment').linkbutton('disable');
					}
				},
				onLoadError : function() {
					$.messager.alert('提示','查詢轉倉清單出錯','error');
				}
			}
			options.sortName="";
			openDlgGridQuery.ignoreFirstLoad = true;
			openDlgGridQuery("datagrid", options);
			openDlgGridQuery.ignoreFirstLoad = false;
			$("#datagrid").datagrid('acceptChanges');
		}
		
		/**
		 * 將按鈕變為disable
		 */
		function disableTransButton() {
			$('#deleteTrans').linkbutton('disable');
			$('#addTrans').linkbutton('disable');
			$('#deleteList').linkbutton('disable');
			$('#saveComment').linkbutton('disable');
			$('#confirmTrans').linkbutton('disable')
			$('#cancleTrans').linkbutton('disable');
			$('#exportAsset').linkbutton('disable');
			$("#btnUpload").css("display", "none");
			$('#exportData').attr("onclick","return false;");
			$('#exportData').attr("style","color:gray;");
			$('#showName').attr("style","color:gray;");
			$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox('disable');
		}
		
		/**
		 *將按鈕變為enable
		 */
		function enableTransButton() {
			$('#deleteTrans').linkbutton('enable');
			$('#addTrans').linkbutton('enable');
			//$('#deleteList').linkbutton('enable');
			$('#confirmTrans').linkbutton('enable')
			$('#cancleTrans').linkbutton('enable');
			$('#saveTrans').linkbutton('enable');
			$('#saveComment').linkbutton('enable');
			$('#exportAsset').linkbutton('enable');
			$("#btnUpload").css("display", "inline-block");
			$('#exportData').attr("onclick","exportTransData()");
			$('#exportData').attr("style","color:blue;");
			$('#showName').attr("style","color:blue;");
			$('#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>').combobox('enable');
		}
		
		/**
		 *給通知人員下拉框加載值
		 */
		 function loadCname(data) {
		 	data = initSelect(data,"請選擇(複選)");
			//將後台得到的值賦值進相應的ID
			$("#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>").combobox("loadData", data);
			//設置請選擇
			$("#<%=AdmUserDTO.ATTRIBUTE.CNAME.getValue()%>").combobox('setValue','');
		 }
	</script>
</body>
</html>