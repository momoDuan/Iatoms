<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetStacktakeFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (AssetStacktakeFormDTO)ctx.getResponseResult();
	}
	if (formDTO == null) {
		formDTO = new AssetStacktakeFormDTO();
	}
	//倉庫集合
	List<Parameter> warehouseList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ACTION_GET_WAREHOUSE_LIST);
 	//設備狀態
 	List<Parameter> assetStatusList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ASSET_STATUS);
 	//設備類別
 	List<Parameter> assetCataGroyList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ASSET_CATEGORY);
 	//設備盤點批號集合
 	List<Parameter> inventoryNumberList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, AssetStacktakeFormDTO.PARAM_METHOD_GET_INVENTORY_NUMBER_LIST);
%>
<c:set var="warehouseList" value="<%=warehouseList%>"></c:set>
<c:set var="assetStatusList" value="<%=assetStatusList%>"></c:set>
<c:set var="assetCataGroyList" value="<%=assetCataGroyList%>"></c:set>
<c:set var="inventoryNumberList" value="<%=inventoryNumberList%>"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/>
<html>
<head>
<%@include file="/jsp/common/easyui-common.jsp" %>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<div id="p" class="easyui-panel" title="設備盤點作業" style="width: 98%; height: auto;">
        <div class="easyui-tabs" style="width: 100%; height: auto;">
            <div title="設備盤點" style="padding: 10px 10px;">
                <div class="dialogtitle" style="text-align: right;">
                    <a href="javascript:void(0)" id="exporeInventory" onclick="exportInventory();" class="easyui-linkbutton"  iconCls="icon-print">列印清冊</a>
                    <a href="javascript:void(0)" id="deleteAssetInven" class="easyui-linkbutton" onclick="deleteAssetInventory()" iconCls="icon-remove">刪除批號</a>
                    <a href="javascript:void(0)" id="addAssetInven" class="easyui-linkbutton"  iconCls="icon-add">新增批號</a>
				</div>
						<div>
				<span id="msg" class="red"></span>
			</div> 
                <form id="fmt" action="assetStocktake.do" method="post" method="post" novalidate>
                	<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" />
                  <div class="panel-header" style="width: auto;">
                    <div class="panel-title panel-with-icon" class="panel-icon icon-edit">盤點清冊</div>
                  	  <div class="panel-icon icon-edit"></div>
						<div class="panel-tool"></div>
					</div>
					<div class="datagrid-wrap panel-body" title="" style="width: auto;">
					 <div id="tb1" class="datagrid-toolbar" style="padding: 2px 5px;">	
                        <div>
                            盤點批號: <span class="red">*</span>
                            <cafe:droplisttag
                            	css="easyui-combobox"
                            	id="<%=AssetStacktakeFormDTO.ASSET_INVENTORY_ID %>"
								name="<%=AssetStacktakeFormDTO.ASSET_INVENTORY_ID %>" 
								result="${inventoryNumberList }"
								javascript="validType=\"ignore['請選擇']\" invalidMessage=\"請輸入盤點批號\" editable=false data-options=\"valueField:'value',textField:'name'\""
								blankName="請選擇"
								hasBlankValue="true"
								defaultValue="${inventoryNumberList[0].value }"
								style="width:150px">
                     		</cafe:droplisttag>
                     		<input name="queryStocktackId"  id="queryStocktackId" type="hidden"  value="<c:out value='${inventoryNumberList[0].value}'/>" />
                        </div>
                        <div id="hiddenDiv">
	                        <div id="houseName"></div>
	                        <div id="housecontact"></div>
	                        <div id="assetCateGroyNa"  style="width: 880px"></div>
	                        <div id="assetStatusNa"></div>
	                        <div id="assetCount"></div>
	                        <div id="renarkNa" style="width: 880px"></div>
                        </div>
                       </div>
                    </div>
                    <div class="datagrid-view" style="width: 100%; height: auto;">
                    <table id="dg1" >
                    </table>
                    </div>
                    <br/>
                    <div class="panel-header" style="width: auto;">
                    <div class="panel-title panel-with-icon" class="panel-icon icon-edit">Barcode輸入</div>
                  	  <div class="panel-icon icon-edit"></div>
						<div class="panel-tool"></div>
					</div>
						
						<div id="ly" class="easyui-panel panel-body" iconcls="icon-edit" title="" style="width: 100%;">
                       設備序號/財編: <span class="red">*</span><input class="easyui-textbox" 
                       				id="<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>" 
                       				name="<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>" 
                       				style="width: 200px" 
                       				type="text"
                       				maxlength="20"
                       				disabled="disabled"
                       				data-options="required:true,missingMessage:'請輸入設備序號/財編',validType:['englishOrNumber[\'設備序號/財產編號限英數字，請重新輸入\']']">
                       				</input>
                       <a href="javascript:void(0)" id="send" class="easyui-linkbutton" disabled="disabled" iconCls="icon-ok">送出</a>
                       </div>
                    <br/>
                    <div>
                    	<div>
							<span id="msg2" class="red"></span>
						</div>
                        <a href="javascript:void(0)" id="saveRemark" onclick="saveCommet()" class="easyui-linkbutton"  >儲存</a>
                        <a href="javascript:void(0)" id="assetInventoryComplete" onclick="assetInventoryComplete()" class="easyui-linkbutton" >盤點完成</a>
                        <a href="javascript:void(0)" id="exporeSummary" onclick="exportSummary()" class="easyui-linkbutton" >列印盤點結果</a>
                    </div>
                    <table id="dg" >
                    </table>
                    <input type="hidden" id="updateRemark"/>
                </form>
            </div>
             <div title="盤點歷史查詢" data-options="href:'${contextPath}/assetStocktackHistory.do?actionId=initHist',closed:true" ></div>
        </div>
         <div id="dlg" modal ="true"></div> 
	</div>
</div>
<script type="text/javascript">
	/*$.extend($.fn.validatebox.defaults.rules, {
		numberOrEnglish: {
			validator: function (value) {
				return /^[a-zA-Z0-9]+$/i.test(value);
			},
			message: '設備序號/財產編號限英文或數字，請重新輸入'
		},
	});*/

	//隐藏提示讯息栏位
	/*$(function(){
		//列印清冊
		$("#exporeInventory").linkbutton('disable');
		//列印盤點結果
		$("#exporeSummary").linkbutton('disable');
		//盤點完成
		$("#assetInventoryComplete").linkbutton('disable');
		$("#saveRemark").linkbutton('disable');
		//刪除批號
		$("#deleteAssetInven").linkbutton('disable');
		$("#hiddenDiv").hide();
		$("#assetInventoryId").trigger("change");
	});*/
	var editIndex = undefined;
	var url;
	var isQuery = true;
	//點擊的方法
	$(function() {
		//update by hermanwang start 2017/05/31
		//列印清冊
		$("#exporeInventory").linkbutton('disable');
		//列印盤點結果
		$("#exporeSummary").linkbutton('disable');
		//盤點完成
		$("#assetInventoryComplete").linkbutton('disable');
		$("#saveRemark").linkbutton('disable');
		//刪除批號
		$("#deleteAssetInven").linkbutton('disable');
		$("#hiddenDiv").hide();
		//设备批号
		$("#assetInventoryId").trigger("change");
		//update by hermanwang end 2017/05/31
		//新增批號
		$("#addAssetInven").click(function(){
			addAssetInventory('新增盤點批號', '<%=IAtomsConstants.ACTION_INIT_ADD%>');
		});
		//送出
		$("#send").click(function() {
			//设备批号
			var stocktackId = $("#assetInventoryId").combobox('getValue');
			var sendSerialNumber = $("#sendSerialNumber").textbox('getValue');
			if (!$("#fmt").form('validate')) {
				return false;
			}
			//盤點单个設備
			sendInventoryComplete(sendSerialNumber, stocktackId);
		});
		init();
		// 查詢datagrid初始化
		setTimeout("initStackResultDatagrid();",5);
		setTimeout("initStackItemDatagrid();",10);
	});
	
	/*
	*初始化查詢datagrid
	*/
	function initStackResultDatagrid(){
		var grid = $("#dg");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 100%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'checked',width:40,formatter:function(value,row){if(row.stocktakeStatus == 0){return showCheckBox(row.tackId);}},title:"<input type='checkbox' onclick='checkAll(this)' id = 'all'>"},
				{field:'stocktakeStatus',halign:'center',width:130,sortable:true,formatter:function(value,row){if(row.stocktakeStatus ==0){return '待盤點';}else if(row.stocktakeStatus ==1){return '已盤點';}else if(row.stocktakeStatus ==2){return '盤盈';} else {return '盤差';}},title:"盤點狀態"},
				{field:'assetStatus',halign:'center',hidden:true,sortable:true,title:"設備狀態ID"},
				{field:'serialNumber',halign:'center',width:140,sortable:true,title:"設備編號"},
				{field:'assetTypeId',halign:'center',hidden:true,sortable:true,title:"類別ID"},
				{field:'assetTypeName',halign:'center',width:150,sortable:true,title:"設備名稱"},
				{field:'remark',width:250,halign:'center',sortable:true,formatter:function(value,row){return showText(value,row.tackId);},title:"盤點說明"},
				{field:'updatedByName',width:137,halign:'center',sortable:true,title:"異動人員"},
				{field:'updatedDate',width:200,halign:'center',sortable:true,align:'center',formatter:formatToTimeStamp,title:"異動日期"}
			];
			// 創建datagrid
			grid.datagrid({
				title:"盤點結果",
				columns:[datagridColumns],
			    rownumbers:true,
			    pagination:true,
			    pageList:[15,30,50,100],
				pageSize:15,
			    iconCls: 'icon-edit',
			    singleSelect: false,	    
			    checkOnSelect: false, selectOnCheck: false,
			    method: 'get',
			    onClickRow :onClickRow,
			     nowrap:false,
			    pageNumber:0
			});
			
		}
	}

	/*
	*初始化查詢datagrid
	*/
	function initStackItemDatagrid(){
		var grid = $("#dg1");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 100%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'assetTypeName',halign:'center',width:150,sortable:true,title:"設備名稱"},
				{field:'assetStatusName',halign:'center',width:150,sortable:true,title:"設備狀態"},
				{field:'noStocktack',halign:'center',align:'right',width:120,sortable:true,formatter:formatCapacity,title:"待盤點"},
				{field:'alreadyStocktack',halign:'center',align:'right',width:120,sortable:true,formatter:formatCapacity,title:"已盤點"},
				{field:'overage',halign:'center',align:'right',width:120,sortable:true,formatter:formatCapacity,title:"盤盈"},
				{field:'assetlLess',halign:'center',align:'right',width:120,sortable:true,formatter:formatCapacity,title:"盤差"},
				{field:'totalStocktack',halign:'center',align:'right',width:120,sortable:true,formatter:formatCapacity,title:"總計"}
			];
			// 創建datagrid
			grid.datagrid({
				title:"盤點清冊",
				columns:[datagridColumns],
			    rownumbers:true,
			    pagination:true,
			    pageList:[15,30,50,100],
				pageSize:15,
			    iconCls: 'icon-edit',
			    singleSelect: true,
			    method: 'get',
			    nowrap:false,
			    pageNumber:0
			});
			
		}
	}
	
	//初始化頁面的時候當前盤點批號查出的信息
	function init(){
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle1);
		isQuery = false;
		//设备批号
		var newValue = $("#assetInventoryId").val();
 		$("#msg").empty();
		$("#msg2").empty();
		if (!isEmpty(newValue)) {
			javascript:dwr.engine.setAsync(false);
			//根據盤點批號查詢盤點詳細信息
			ajaxService.getAssetStocktackInfoByStocktackId(newValue, function(data){
				if (data != null) {
					$("#hiddenDiv").show();
					$("#houseName").text("盤點倉庫:  " + data.warHouseName);
					$("#housecontact").text("倉庫保管人:  " + data.contact);
					$("#assetCateGroyNa").html("<td width='85px;'>設備名稱:</td> <td style='text-align:left;word-wrap:break-word;word-break:break-all;'>" + data.assetTypeName + "</td>");
					$("#assetStatusNa").text("設備狀態:  " + data.assetStatusName);
					$("#assetCount").html("應盤點總數: <span class='red'>" + formatCapacity(data.count) + "</span>");
					$("#renarkNa").html("<td width='50px;'>說明:</td><td> <span style='word-wrap:break-word;word-break:break-all;'>" + data.remark.replaceAll("\n","<br>").replaceAll(" ","&nbsp") + "</span></td>");
				}
			});
		} else {
			$("#hiddenDiv").hide();
			//列印清冊
			$("#exporeInventory").linkbutton('disable');
			//列印盤點結果
			$("#exporeSummary").linkbutton('disable');
			$("#send").linkbutton('disable');
			//盤點完成
			$("#assetInventoryComplete").linkbutton('disable');
			$("#saveRemark").linkbutton('disable');
			$("#send").linkbutton('disable');
			$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('disable');
			$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('disableValidation');
			//查询當前批号下的所有资料明细
			getAssetInventoryList("", 1);
			//查詢盤點清冊數據
			getAssetStocktackList("", 1);
		}
		if (!isEmpty(newValue)) {
			//查询當前批号下的所有资料明细
			getAssetInventoryList(newValue, 1);
			//查詢盤點清冊數據
			getAssetStocktackList(newValue, 1);
		}else{
			//列印清冊
			$("#exporeInventory").linkbutton('disable');
			//列印盤點結果
			$("#exporeSummary").linkbutton('disable');
			$("#send").linkbutton('disable');
			$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('disable');
			$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('disableValidation');
			//盤點完成
			$("#assetInventoryComplete").linkbutton('disable');
			$("#saveRemark").linkbutton('disable');
			//刪除批號
			$("#deleteAssetInven").linkbutton('disable');
		}
		javascript:dwr.engine.setAsync(true);
		isQuery = true;
		$.unblockUI();
	}
	//加载完成时将数据提交
	function dlgGridLoadSuccess() {
		$('#dg').datagrid('acceptChanges');
	}
	
	//儲存說明
	function saveCommet(){
		//设备批号
		var stocktackId = $("#assetInventoryId").combobox('getValue');
		saveRemark(stocktackId);
	}
	//储存说明
	function saveRemark(stocktackId) {
		$("#msg").empty();
		$("#msg2").empty();
		$('#dg').datagrid('acceptChanges');
		//拿到集合的所有的行
		var assetRows = $("#dg").datagrid('getRows');
		var updateRows = $("#updateRemark").val();
		updateRows = updateRows.split(",");
		var data =[];
		var obj = new Object();
		var isValue = false;
		//设备批号
		var stocktackId = $("#assetInventoryId").combobox("getValue");
		for(var i = 0; i<assetRows.length; i++){
			obj = new Object();
			isValue = false;
			//id
			obj.id=assetRows[i].tackId;
			if ($("#check"+assetRows[i].tackId).is(":checked")) {
				obj.checked=true;
				isValue = true;
			} else {
				obj.checked=false;
			}
			//是否包含在隱藏域的值（說明欄位的異動）
			if (updateRows.contains(assetRows[i].tackId)){
				isValue = true;
				obj.remark=$("#"+assetRows[i].tackId).val();
			}
			if (isValue) {
				data.push(obj);
			}
		}
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle1);
		$.ajax({
			url : "${contextPath}/assetStocktake.do?actionId=saveRemark",
			data : {assetStocktackList:JSON.stringify(data)},
			type : 'post', 
			cache : false, 
			dataType : 'json', 
			success : function(json) {
				if (json.success) {
					$("#dg").datagrid("clearSelections");
					//查询當前批号下的所有资料明细
					getAssetInventoryList(stocktackId, null);
					//查詢盤點清冊數據
					var pageIndex = getGridCurrentPagerIndex("dg");
					if (pageIndex < 1) {
						pageIndex = 1;
					}
					getAssetStocktackList(stocktackId, pageIndex);
				} else {
					$("#msg2").text(json.msg);
				}
				$.unblockUI();
			},
			error : function() {
				$.messager.alert('提示', "儲存失敗,請聯繫管理員.", 'error');	
				$.unblockUI();
			}
		});
	}
	//獲取當前批号下的所有资料明细
	function getAssetInventoryList(stocktackId, pageIndex) {
		$('#queryStocktackId').val(stocktackId);
		var queryParams = $("#fmt").form("getData");
		var options = {
			url : "${contextPath}/assetStocktake.do?actionId=<%=IAtomsConstants.ACTION_LIST%>",
			queryParams :queryParams,
			pageNumber:pageIndex,
			sortName:"",
			onLoadSuccess:function(data){
				if (data.total > 0) {
					showButtomAssetStake();
				} else {
					disabledButtomAssetSatke();
				}
			},
			onLoadError : function() {
				$("#msg").text("查詢失敗！請聯繫管理員");
			}
		}
		options.ignoreFirstLoad = true;
		openDlgGridQuery("dg1", options); 
		options.ignoreFirstLoad = false;
	} 
	//獲取當前批号下的盤點清冊數據
	function getAssetStocktackList(stocktackId, pageIndex) {
		var queryParam = {queryStocktackId : stocktackId};
		var options = {
			url : "${contextPath}/assetStocktake.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
			queryParams :queryParam,
			pageNumber:pageIndex,
			sortName:"",
			onLoadSuccess:function(data){
				if (data.total > 0) {
					showButtomAssetStake();
				} else {
					disabledButtomAssetSatke();
				}
			},
			onLoadError : function() {
				$("#msg").text("查詢失敗！請聯繫管理員");
			}
		}
		options.ignoreFirstLoad = true;
		openDlgGridQuery("dg", options);
		options.ignoreFirstLoad = false;
	}
	
	//按鈕可用事件
	function showButtomAssetStake(){
		//列印清冊
		$('#exporeInventory').linkbutton('enable');
		//列印盤點結果
		$('#exporeSummary').linkbutton('enable');
		//盤點完成
		$("#assetInventoryComplete").linkbutton('enable');
		//刪除批號
		$("#deleteAssetInven").linkbutton('enable');
		$("#saveRemark").linkbutton('enable');
		$("#send").linkbutton('enable');
		$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('enable');
		$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('enableValidation');
	}
	//disabled掉盤點按鈕
	function disabledButtomAssetSatke(){
		//列印清冊
		$('#exporeInventory').linkbutton('disable');
		//列印盤點結果
		$('#exporeSummary').linkbutton('disable');
		//盤點完成
		$("#assetInventoryComplete").linkbutton('disable');
		//刪除批號
		$("#deleteAssetInven").linkbutton('disable');
		$("#saveRemark").linkbutton('disable');
		$("#send").linkbutton('disable');
		$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('disable');
		$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('disableValidation');
	}
	
	//點擊“盤點清冊”某一行時出現的文本框
	function onClickCell(index, field) {
		$('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
		var ed = $('#dg').datagrid('getEditor', {
				index : index,
				field : 'remark'
		});
		if (ed) {
			($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
		}
	}
	//盤點单个設備 stocktackId: 设备批号  sendSerialNumber:单笔盘点送出的设备批号
	function sendInventoryComplete(sendSerialNumber, stocktackId) {
		$("#msg").empty();
		$("#msg2").empty();
		$("#dg").datagrid("clearSelections");
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle1);
		$.ajax({
			url : "${contextPath}/assetStocktake.do?actionId=send&sendSerialNumber=" + sendSerialNumber + "&queryStocktackId=" + stocktackId,
			type : 'post', 
			cache : false, 
			dataType : 'json', 
			success : function(json) {
				if (json.success) {
					//重新加載設備盤點批號下拉框
					//查询當前批号下的所有资料明细
					getAssetInventoryList(stocktackId, null); 
					//查詢盤點清冊數據
					var pageIndex = getGridCurrentPagerIndex("dg");
					if (pageIndex < 1) {
						pageIndex = 1;
					}
					getAssetStocktackList(stocktackId, pageIndex);
					$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('setValue', '');
					//$("#msg2").text(json.msg);
				}else{
					$("#msg2").text(json.msg);
					$("#<%=AssetStacktakeFormDTO.SEND_SERIAL_NUMBER%>").textbox('setValue', '');
				}
				$.unblockUI();
			},
			error : function() {
				$.messager.alert('提示', "盤點失敗,請聯繫管理員.", 'error');
				$.unblockUI();
			}
		});
	}

	//盤點完成操作
	function assetInventoryComplete() {
		//设备批号
		var stocktackId = $("#assetInventoryId").combobox('getValue');
		$("#msg").empty();
		$("#msg2").empty();
		$("#dg").datagrid("clearSelections");
		var rows = $("#dg").datagrid('getRows');
		if(rows) {
			for(var i = 0; i < rows.length; i++){
				$("#check"+rows[i].tackId).removeAttr("checked"); 
			}
		}
		$.messager.confirm('確認對話框','確認完成?', function(b) {
			if (b) {
				/* parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍候....'
				}); */
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				$.ajax({
					url : "${contextPath}/assetStocktake.do?actionId=complete&queryStocktackId=" + stocktackId,
					type : 'post', 
					cache : false, 
					dataType : 'json', 
					success : function(json) {
						if (json.success) {
							//重新加載設備盤點批號下拉框
							ajaxService.getInventoryNumberList(function(data) {
									if (data != null) {
										//設備盘点批号下拉選單添加請選擇
										var loadData = initSelect(data);
										$("#<%=AssetStacktakeFormDTO.ASSET_INVENTORY_ID%>").combobox('loadData', loadData).combobox('setValue','');
										$("#msg2").text(stocktackId + "盤點完成，盤點結果/盤點清冊請於歷史盤點查看");
									}
								});
						} else {
							$("#msg").text(json.msg);
						}
						$.unblockUI();
					},
					error:function(){
						$.messager.alert('提示', "盤點失敗,請聯繫管理員.", 'error');
						$.unblockUI();
					}
				});
			}
		});
	}
	//匯出列印清冊
	function exportInventory() {
		//设备批号	
		var stocktackId = $("#assetInventoryId").combobox('getValue');
		$("#msg").empty();
		$("#msg2").empty();
		$("#dg").datagrid("clearSelections");
		var rows = $("#dg").datagrid('getRows');
		if(rows) {
			for(var i = 0; i < rows.length; i++){
				$("#check"+rows[i].tackId).removeAttr("checked"); 
			}
		}
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		var useCaseNo = '<%=IAtomsConstants.UC_NO_DMM_03080%>';
		actionClicked(document.forms["fmt"], useCaseNo, '', 'exportInventory');
		
		ajaxService.getExportFlag(useCaseNo,function(data){
			$.unblockUI();
		});
	}
	//匯出盤點結果
	function exportSummary() {
		//设备批号
		var stocktackId = $("#assetInventoryId").combobox('getValue');
		$("#msg").empty();
		$("#msg2").empty();
		$("#dg1").datagrid("clearSelections");
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		actionClicked(document.forms["fmt"], '<%=IAtomsConstants.UC_NO_DMM_03080%>', '', 'exportSummary');
		
		ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_DMM_03080%>',function(data){
			$.unblockUI();
		});
	}
	
	//新增設備批號
	function addAssetInventory (title, actionId, stocktackId) {
		$("#msg").empty();
		$("#msg2").empty();
		$("#dg").datagrid("clearSelections");
		var rows = $("#dg").datagrid('getRows');
		if(rows) {
			for(var i = 0; i < rows.length; i++){
				$("#check"+rows[i].tackId).removeAttr("checked"); 
			}
		}
		var viewDlg = $('#dlg').dialog({    
			title : title,    
			width : 780,
			height : 300,
			top:10,
			closed : false,    
			cache : false,
			queryParams : {
				actionId : actionId
			},
			href : "${contextPath}/assetStocktake.do",
			modal : true,
			onLoad : function() {
				textBoxSetting("dlg");
			},
			buttons : [{
				text : '儲存',
				iconCls : 'icon-ok',
				width : '90px',
				handler : function(){
					var f = viewDlg.find("#fm");
					var controls = ['warWarehouseId','assetTypeId','assetStatus'];
					if (validateForm(controls) && f.form("validate")) {
						//增加遮罩
						commonSaveLoading('dlg');
						//取form表單的所有的值
						var saveParam = f.form("getData");
						//設備狀態ID
						var assetType = $("#<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_STATUS.getValue()%>").combobox('getValue');
						//saveParam.typeIdList = getMultiSelValues("assetTypeId");
						//saveParam.statusList = getMultiSelValues("assetStatus");
						saveParam.assetStatusName = $("#<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_STATUS.getValue()%>").combobox("getText");
						//設備ID
						saveParam.assetTypeName = $("#<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox("getText");
						//save
						$.ajax({
							url : "${contextPath}/assetStocktake.do?actionId=save",
							data : saveParam, 
							type : 'post', 
							cache : false, 
							dataType : 'json', 
							success : function(json) {
								//去除遮罩
								commonCancelSaveLoading('dlg');
								if (json.success) {
									viewDlg.dialog('close');
									//新增批號成功後，刷新盤點批號下拉列表
									ajaxService.getInventoryNumberList(function(data) {
										if (data != null) {
											//設備盘点批号下拉選單添加請選擇
											var loadData = initSelect(data);
											$("#<%=AssetStacktakeFormDTO.ASSET_INVENTORY_ID%>").combobox('loadData', loadData);
											$("#msg").text(json.msg);
										}
									});
									//獲取stocktackId
									var stocktackId = json.stocktackId;
									//把盤點批號下拉框的值設為新增的那個批號,觸發Onchange事件 
									$('#<%=AssetStacktakeFormDTO.ASSET_INVENTORY_ID%>').combobox('setValue', stocktackId);
								} else {
										$("#msg1").text(json.msg);
								}
							},
							error : function(){
								//去除遮罩
								commonCancelSaveLoading('dlg');
								parent.$.messager.progress('close');
								$.messager.alert('提示', "保存失敗,請聯繫管理員.", 'error');							
							}
						});
					}
				}
			},{
				text : '取消',
				width : '90px',
				iconCls : 'icon-cancel',
				handler : function(){
					 confirmCancel(function(){
						viewDlg.dialog('close');
					});
				}
			}]
		});
	}
	//刪除設備批號
	function deleteAssetInventory() {
		//设备批号
		var stocktackId = $("#assetInventoryId").combobox('getValue');
		$("#msg").text("");
		$("#msg2").empty();
		//獲取刪除的參數
       	param = {
				actionId : "<%=IAtomsConstants.ACTION_DELETE%>",	
				queryStocktackId : stocktackId,
			};
		var url = '${contextPath}/assetStocktake.do';
		var successFunction = function(json) {
			if (json.success) {
				//抓取盤點批號集合
				ajaxService.getInventoryNumberList(function(data) {
					if (data != null) {
						//設備盘点批号下拉選單添加請選擇
						var loadData = initSelect(data);
						//給設備批號下拉框重新加載數據
						$("#<%=AssetStacktakeFormDTO.ASSET_INVENTORY_ID%>").combobox('loadData', loadData).combobox('setValue','');
						$("#msg").text(json.msg);
					}
				});
			} else {
				$.messager.progress('close');
			}
		};
		var errorFunction = function(){
			$("#msg").text("刪除失敗");
		};
		commonDelete(param,url,successFunction,errorFunction);
	}
	//點擊盤點批號時加載數據
	$("#assetInventoryId").combobox({
		onChange:function(newValue, oldValue) {
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle1);
			$("#msg").empty();
			$("#msg2").empty();
			if (!isEmpty(newValue)) {
				//根據盤點批號查詢盤點詳細信息
				ajaxService.getAssetStocktackInfoByStocktackId(newValue, function(data){
					if (data != null) {
						$("#hiddenDiv").show();
						$("#houseName").text("盤點倉庫:  " + data.warHouseName);
						$("#housecontact").text("倉庫保管人:  " + data.contact);
						$("#assetCateGroyNa").html("<td width='85px;'>設備名稱:</td> <td style='text-align: left;word-wrap:break-word;word-break:break-all;'>" + data.assetTypeName + "</td>");
						$("#assetStatusNa").text("設備狀態:  " + data.assetStatusName);
						$("#assetCount").html("應盤點總數: <span class='red'>" + data.count + "</span>");
						$("#renarkNa").html("<td width='50px;'>說明:</td> <td style='text-align: left;word-wrap:break-word;word-break:break-all;'>" + data.remark.replaceAll("\n","<br>").replaceAll(" ","&nbsp") + "</td>");
					}
					getAssetStocktackList(newValue, 1);
					getAssetInventoryList(newValue, 1);
				});
			} else {
				$("#hiddenDiv").hide();
				getAssetStocktackList('', 1);
				getAssetInventoryList('', 1);
			}
			$.unblockUI();
		}
	});
	//update by hermanwang 2017/05/31 (此方法可能無用)
	/*function getMultiSelValues(selName) {
		var result = "";
		var sels = document.getElementsByName(selName);
		if (sels != null) {
			for (var i = 0; i < sels.length; i++) {
				if (sels[i].value != null && sels[i].value != "") {
					result += "'" + sels[i].value + "',";
				}
			}
			result = result.substring(0, result.length - 1);
		}
		return result;
	} */
	//說明的編輯框
	function showText(value, tackId){
		if (value == null){
			return "<input class='textbox1' onclick=\"clickRemark('"+tackId+"')\" id=" + tackId + " style='border-radius: 4px; width:223px; border: #95B8E7 1px solid;' maxlength='200' value='' onchange='changeRemark(\""+tackId+"\");'/>";
		} else {
			return "<input class='textbox1' onclick=\"clickRemark('"+tackId+"')\" id=" + tackId + " style='border-radius: 4px; width:223px; border: #95B8E7 1px solid;' maxlength='200' value='" + value + "' onchange='changeRemark(\""+tackId+"\");'/>";
		}
	}
	//點擊說明出發的不改變行是否被選中的事件
	function clickRemark(tackId) {
		var rows = $("#dg").datagrid('getRows');
		for(var i = 0; i < rows.length; i++){
			//如果是待盤點
			if(rows[i].stocktakeStatus == 0) {
				if(rows[i].tackId == tackId) {
					if(!$("#check"+tackId).is(":checked")) {
						$("#dg").datagrid("selectRow", i);
						$("#check"+tackId).prop("checked","true");
					} else {
						$("#dg").datagrid("unselectRow", i);
						$("#check"+tackId).removeAttr("checked"); 
					}
					break;
				}
			} else {
				if(rows[i].tackId == tackId) {
					var obj = $(".datagrid-view2").find("table.datagrid-btable").find("[datagrid-row-index='"+ i +"']");
					//當前狀態是被選中
					if(obj.attr('class').indexOf("datagrid-row-selected") > 0) {
						$("#dg").datagrid("unselectRow", i);
					} else {
						$("#dg").datagrid("selectRow", i);
					}
				}
			}
		}
	}
	//選擇框
	function showCheckBox(tackId){
		return "<input type='checkbox' class='hhhh' onclick='checksigle(this)'  id='check" + tackId + "'"+"/>";
	}

	$('#datagrid').datagrid({
		onLoadSuccess: function (data) {
			$('.textbox1').textbox();
		},
	});
	//複選框點擊事件
	function checksigle(Obj) {
		//拿到checkbox的length
		var hh = $(".hhhh").length;
		//拿到checkbox的checked的length
		var hhh = $(".hhhh:checked").length;
		$("#all").prop("checked",hh == hhh);
		if ($(Obj).is(":checked")) {
			$(Obj).removeAttr("checked"); 
		} else {
			$(Obj).prop("checked","true"); 
		}
	}
	//全選
	function checkAll(obj){
		//全選框是否選中
		var isState = obj.checked;
		$(".hhhh").prop("checked",isState);
		var rows = $("#dg").datagrid('getRows');
		if(!isState) {
			for(var i = 0; i < rows.length; i++) {
				$("#dg").datagrid("unselectRow", i);
			}
		} else {
			for(var i = 0; i < rows.length; i++){
				if(rows[i].stocktakeStatus == 0) {
					var obj = $(".datagrid-view2").find("table.datagrid-btable").find("[datagrid-row-index='"+ i +"']");
					//當前狀態是被選中
					$("#dg").datagrid("selectRow", i);
				}
			}
		}
	}
	//說明欄位異動事件
	function changeRemark(tackId) {
		var id= $("#updateRemark").val();
		$("#updateRemark").val(id + "," + tackId);
	}
	//點擊行
	function onClickRow(index) {
		var assetRows = $("#dg").datagrid('getRows');
		var tackId = assetRows[index].tackId;
		if ($("#check"+tackId).is(":checked")) {
			$("#check"+tackId).removeAttr("checked"); 
		} else {
			$("#check"+tackId).prop("checked","true"); 
		}
		//拿到checkbox的length
		var hh = $(".hhhh").length;
		//拿到checkbox的checked的length
		var hhh = $(".hhhh:checked").length;
		$("#all").prop("checked",hh == hhh);
	}
	</script>
</body>
</html>