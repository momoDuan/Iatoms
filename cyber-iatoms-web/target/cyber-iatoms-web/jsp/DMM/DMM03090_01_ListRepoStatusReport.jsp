<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@page
	import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoStatusReportFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	RepoStatusReportFormDTO formDTO = null;
	String roleAttribute = null;
	String logonUserCompanyId = null;
	String ucNo = "";
	if (ctx != null) {
		//根據返回的消息狀態獲取formDTO
		if (Message.STATUS.SUCCESS.equals(message.getStatus())) {
			formDTO = (RepoStatusReportFormDTO) ctx.getResponseResult();
		} else {
			formDTO = (RepoStatusReportFormDTO) ctx.getRequestParameter();
		}
		if (formDTO != null) {
			//獲取usecaseNO
			ucNo = formDTO.getUseCaseNo();
			//獲取角色
			roleAttribute = formDTO.getRoleAttribute();
			//獲取當前登入者為客戶屬性對應之公司
			logonUserCompanyId = formDTO.getLogonUserCompanyId();
		}
	}
	//維護模式列表
	List<Parameter> maTypeList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.MA_TYPE.getCode());
	//通訊模式列表
	List<Parameter> commModeList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.COMM_MODE.getCode());
	//客戶列表
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//設備名稱列表
	List<Parameter> assetNameList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ASSET_NAME.getCode());
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="roleAttribute" value="<%=roleAttribute%>" scope="page"></c:set>
<c:set var="logonUserCompanyId" value="<%=logonUserCompanyId%>" scope="page"></c:set>
<!-- DataLoader -->
<c:set var="maTypeList" value="<%=maTypeList%>" scope="page"></c:set>
<c:set var="commModeList" value="<%=commModeList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="assetNameList" value="<%=assetNameList%>" scope="page"></c:set>
<!-- DataLoader -->
<html>
<head>
<title>設備狀態報表</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
	//頁面加載完成時
	$(function (){
		//匯出和匯出清單按鈕disable
		$('#exportData').attr("onclick","return false;");
		$('#exportData').attr("style","color:gray;");
		$('#exportDataList').attr("onclick","return false;");
		$('#exportDataList').attr("style","color:gray;");
		//設定查詢月份欄位的日曆控件為年月
		createMonthDataBox('queryDateShow');
		textBoxDefaultSetting($('#queryDateShow'));
	});
	//查詢
	function queryData(){
		$("#msg").text("");
		//提交表單時要驗證的必輸欄位
		var controls = ['queryDateShow'];
		var queryForm = $("#queryForm");
		if(validateForm(controls) && queryForm.form("validate")){
			//要傳遞的參數
			var queryParam = {
					actionId 		: "<%=IAtomsConstants.ACTION_QUERY%>",
					queryCustomer 	: $("#queryCustomerShow").combobox("getValue"),
					queryAssetName 	: getMultiSelValues("queryAssetNameShow"),
					queryMaType 	: $("#queryMaTypeShow").combobox("getValue"),
					queryCommMode 	: getMultiSelValues("queryCommModeShow"),
					queryDate 		: $("#queryDateShow").datebox("getValue")
				};
			var options = {
					url : "${contextPath}/assetStatusReport.do",
					queryParams :queryParam,
					pageNumber:1,
					onLoadSuccess:function(data){
						$("#msg").text(data.msg);
						//若存在查詢結果
				        if(data.total > 0){
				        	//查詢結果合併參考數組
				        	var arr =[{mergeFiled:"assetCategory",premiseFiled:"warehouseId"},
				        	          {mergeFiled:"companyName",premiseFiled:"companyName"},
				        	          {mergeFiled:"warehouseId",premiseFiled:"companyName"},
				        	          {mergeFiled:"warehouseName",premiseFiled:"warehouseId"},
				        	          {mergeFiled:"number",premiseFiled:"number"}
					                  ];
				        	//合并列的field数组及对应前提条件filed（为空则直接内容合并）
							mergeCells(arr,"dataGrid");
				        	//查詢到數據后將匯出和匯出清單按鈕變為可用 
							$('#exportData').attr("onclick","exportData('statusDetail')");
							$('#exportData').attr("style","color:blue;");
							$('#exportDataList').attr("onclick","exportData('statusList')");
							$('#exportDataList').attr("style","color:blue;");
							// 定義合並列的字段
							var mergeFiledArray = ['companyName', 'warehouseName', 'warehouseId','assetCategory'];
							// 不处理一些列
							var ignoreFiledArray = ['number'];
							// 調用處理懸浮樣式設置
							setMouseEvent(mergeFiledArray, ignoreFiledArray);
				        } else {
				        	//查無資料時匯出和匯出清單欄位禁用
				        	$('#exportData').attr("onclick","return false;");
				    		$('#exportData').attr("style","color:gray;");
				    		$('#exportDataList').attr("onclick","return false;");
				    		$('#exportDataList').attr("style","color:gray;");
				        }
					},  
					onLoadError : function(data) {
						$.messager.alert('提示','查詢設備狀態報表出錯!','error');
						$("#queryFlag").val('<%=IAtomsConstants.NO%>');
					},
				}
			//查詢的公用方法
			openDlgGridQuery("dataGrid", options);
		};
	}
	
	/*
	* 設置懸浮樣式以及點擊選中樣式
	mergeFiledArray ： 合並列的數組 將需要合並的列依次傳入
	ignoreFiledArray ：忽略處理樣式的列 裏出合並列的number等列
	*/
	function setMouseEvent(mergeFiledArray, ignoreFiledArray){
		// 鼠標進入懸浮事件
		$(".datagrid-row>td").mouseover(function(event){
			event.stopPropagation();
			// 调用事件控制函数
			eventControl($(this), mergeFiledArray, ignoreFiledArray, true);
		});
		// 鼠標離開懸浮事件
		$(".datagrid-row>td").mouseout(function(event){
			event.stopPropagation();
			// 调用事件控制函数
			eventControl($(this), mergeFiledArray, ignoreFiledArray, false);
		})
		// 鼠標點擊事件
		$(".datagrid-row>td").click(function(event){
			event.stopPropagation();
			$("#dataGrid").datagrid("unselectAll");
			// 當前節點對象
			var thisObj = $(this);
			// 合並行 行數
			var rowspan = thisObj.prop("rowspan");
			// 字段名
			var fieldName = thisObj.attr("field");
			// 當前行對象
			var trObj = thisObj.closest(".datagrid-row");
			// 行號
			var trIndex = trObj.prop("rowIndex");
			// 當前table
			var tableObj = thisObj.closest("table.datagrid-btable");
			// 移除選中class
			trObj.removeClass("datagrid-row-checked datagrid-row-selected");
			// 需要處理字段數組
			var settingFieldArr = [];
			// 跳出循環標記
			var breakFlag = false;
			// 取消選中標記
			var cancelFlag = false;
			// 之前的下標
			var oldIndex = undefined;
			// 新的下標
			var newIndex = undefined;
			// 處理合並列樣式
			for(var mergeIndex = 0; mergeIndex < mergeFiledArray.length; mergeIndex++){
				// 當前字段值爲合並列的字段值 處理列的樣式跳出循環
				if(mergeFiledArray[mergeIndex] == fieldName){
					breakFlag = true;
				// 當前字段值爲合並列的字段值 
				} else {
					settingFieldArr.push(mergeFiledArray[mergeIndex]);
					// 最後一項不符合
					if(mergeIndex == mergeFiledArray.length-1){
						breakFlag = true;
					}
				}
				// 處理完樣式結束循環
				if(breakFlag){
					// 存放合並列開始字段的臨時變量
					var tempStartFiled = undefined;
					for(var mergeIndex = 0; mergeIndex < mergeFiledArray.length; mergeIndex++){
						// 拿到當前字段所做列
						var tempMergeTdObj = trObj.children("[field='"+ mergeFiledArray[mergeIndex] +"']");
						// 判斷該列是否有選中樣式
						if(tempMergeTdObj.hasClass("checked-style-for-row")){
							// 如果合並列有選中
							tempStartFiled = mergeFiledArray[mergeIndex];
							break;
						}
					}
					// 有開始字段
					if(tempStartFiled){
						// 處理選中後再次選中的情況
						for(var mergeIndex = 0; mergeIndex < mergeFiledArray.length; mergeIndex++){
							if(mergeFiledArray[mergeIndex] == fieldName){
								oldIndex = mergeIndex;
							}
							if(tempStartFiled == mergeFiledArray[mergeIndex]){
								newIndex = mergeIndex;
							}
						}
						if((oldIndex < newIndex)){
							cancelFlag = true;
							tempStartFiled = undefined;
						}
					} else {
						// 處理未選中的情況
						if(mergeFiledArray.indexOf(fieldName) != -1){
							cancelFlag = true;
						}
					}
					// 處理先選擇打區塊再選擇小區塊的情況
					if(tempStartFiled){
						// 拿到當前td對象
						var tempMergeTdObj = trObj.children("[field='"+ tempStartFiled +"']");
						var tempMergeTrObj = undefined;
						// 如果td不爲禁用則得到相應tr對象
						if(tempMergeTdObj.css('display') != "none"){
							tempMergeTrObj = trObj;
						// 如果td禁用則得到用遞歸的帶相應tr對象
						} else {
							// 通過遞歸得到tr對象
							tempMergeTdObj = findFirstTrObject(tempMergeTdObj, tempStartFiled);
							if(tempMergeTdObj && tempMergeTdObj.length != 0){
								tempMergeTrObj = tempMergeTdObj.closest(".datagrid-row");
							} else {
								tempMergeTrObj = trObj;
							}
						}
						// 行號
						var oldIndex = tempMergeTrObj.prop("rowIndex");
						// 合並行 行數
						var oldRowspan = tempMergeTdObj.prop("rowspan");
						// 給當前列增加樣式
						for(var index = oldIndex; index < (oldRowspan + oldIndex); index++){
							var showRowObj = tableObj.find("[datagrid-row-index='"+ index +"']");
							var tdFieldObj = showRowObj.children();
							tdFieldObj.each(function(index, tempObj){
								var tempField = $(tempObj).attr("field");
								// 不是數字列以及不是小記、總計列
								if((tempField != 'number') && ($(tempObj).attr("style") != "background-color:#ffee00;")){
									// 入多有選中樣式 則移除選中樣式
									if($(tempObj).hasClass("checked-style-for-row")){
										$(tempObj).removeClass("checked-style-for-row");
									}
								}
							})
						}
					} else {
						// 給當前列增加樣式
						for(var index = trIndex; index < (rowspan + trIndex); index++){
							var showRowObj = tableObj.find("[datagrid-row-index='"+ index +"']");
							// 拿到其所有td節點
							var tdFieldObj = showRowObj.children();
							tdFieldObj.each(function(index, tempObj){
								var tempField = $(tempObj).attr("field");
								// 不是數字列以及不是小記、總計列
								if((tempField != 'number') && ($(tempObj).attr("style") != "background-color:#ffee00;")){
									if((settingFieldArr.indexOf(tempField)) == -1){
										// 需要取消選中則取消選中
										if(cancelFlag){
											$(tempObj).removeClass("mouse-style-for-row");
											if(!$(tempObj).hasClass("checked-style-for-row")){
												$(tempObj).addClass("checked-style-for-row");
											}
										// 處理無標志位 選中情況
										} else {
											if(!$(tempObj).hasClass("checked-style-for-row")){
												$(tempObj).removeClass("mouse-style-for-row");
												$(tempObj).addClass("checked-style-for-row");
											} else {
												$(tempObj).removeClass("checked-style-for-row");
											}
										}
									}
								}
							})
						}
					}
					break;
				} 
			}
		})
	}
	/*
	* 控制鼠標懸浮事件 鼠標懸浮後單元格樣式顯示，包含合並列的處理
	thisObj ： 當前鼠標懸浮的td對象
	mergeFiledArray ： 合並列的數組 將需要合並的列依次傳入
	ignoreFiledArray ：忽略處理樣式的列 裏出合並列的number等列
	isMouseOver ： 是否爲鼠標進入事件
	*/
	function eventControl(thisObj, mergeFiledArray, ignoreFiledArray, isMouseOver){
		// 合並行 行數
		var rowspan = thisObj.prop("rowspan");
		// 字段名
		var fieldName = thisObj.attr("field");
		// 當前行對象
		var trObj = thisObj.closest(".datagrid-row");
		// 行號
		var trIndex = trObj.prop("rowIndex");
		// 當前table
		var tableObj = thisObj.closest("table.datagrid-btable");
		// 移除选中样式设定
		trObj.removeClass("datagrid-row-over");
		// 用来存放需要单独设定的列
		var settingFieldArr = [];
		// 跳出循环标记
		var breakFlag = false;
		// 處理合並列樣式
		for(var mergeIndex = 0; mergeIndex < mergeFiledArray.length; mergeIndex++){
			// 當前字段值爲合並列的字段值 處理列的樣式跳出循環
			if(mergeFiledArray[mergeIndex] == fieldName){
				breakFlag = true;
			// 當前字段值爲合並列的字段值 
			} else {
				settingFieldArr.push(mergeFiledArray[mergeIndex]);
				// 最後一項不符合
				if(mergeIndex == mergeFiledArray.length-1){
					breakFlag = true;
				}
			}
			// 處理完樣式結束循環
			if(breakFlag){
				// 給當前列增加樣式
				for(var index = trIndex; index < (rowspan + trIndex); index++){
					// 根据rowspan拿到显示的实际行队形
					var showRowObj = tableObj.find("[datagrid-row-index='"+ index +"']");
					// 拿到该行的所有td对象
					var tdFieldObj = showRowObj.children();
					// 遍历td列
					tdFieldObj.each(function(index, tempObj){
						var tempField = $(tempObj).attr("field");
						//	['companyName', 'warehouseName', 'assetCategory']
						// 无忽略的列或者忽略列不为当前列
						if(!ignoreFiledArray || ((ignoreFiledArray.length != 0) && (ignoreFiledArray.indexOf(tempField) == -1))){
							// 判断当前列是否为总计、小计列
							if($(tempObj).attr("style") != "background-color:#ffee00;"){
								// 如果当前列不是需要特殊处理的列，而且当前列的样式不是隐藏
								if((settingFieldArr.indexOf(tempField) == -1)){
									// 鼠标进入
									if(isMouseOver){
										// 增加懸浮樣式 如果爲選中行則不處理
										if(!$(tempObj).hasClass("checked-style-for-row")){
											$(tempObj).addClass("mouse-style-for-row");
										}
									} else {
										// 取消鼠標懸浮樣式
										if($(tempObj).hasClass("mouse-style-for-row")){
											$(tempObj).removeClass("mouse-style-for-row");
										}
									}
								}
							}
						}
					})
				}
				break;
			} 
		}
	}
	/*
	* 找第一個不爲隱藏節點 通過當前的td節點找到該合並列所在的第一個
	tempMergeTdObj ： 傳入的當前的td的對象
	tempStartFiled ： 傳入合並列所在字段名
	*/
	function findFirstTrObject(tempMergeTdObj, tempStartFiled){
		if(tempMergeTdObj && tempMergeTdObj.length != 0){
			// 當前td對象的tr父節點
			var thisTrObj = tempMergeTdObj.closest(".datagrid-row");
			// 當前tr父節點的上一級節點
			var newTrObj = thisTrObj.prev();
			// 找到合並列字段名的子節點
			var newMergeTdObj = newTrObj.find("[field='"+ tempStartFiled +"']");
			// 該節點不爲隱藏列則直接返回
			if(newMergeTdObj && (newMergeTdObj.css('display') != "none")){
				return newMergeTdObj;
			} else {
				return findFirstTrObject(newMergeTdObj, tempStartFiled);
			}
		}
		return tempMergeTdObj;
	}
	//匯出
	function exportData(reportType){
		var controls = ['queryDateShow'];
		var queryForm = $("#queryForm");
		if(validateForm(controls) && queryForm.form("validate")){
			$("#msg").empty();
	    	//獲取當前當前Grid的行數
	    	var rowLength = getGridRowsCount("dataGrid");
			//若有值就將值依次放入對應的隱藏域節點中
			if(rowLength >= 1) {
				$("#reportType").val(reportType);
				$("#queryCustomer").val($("#queryCustomerShow").combobox("getValue"));
				$("#queryMaType").val($("#queryMaTypeShow").combobox("getValue"));
				$("#queryAssetName").val(getMultiSelValues("queryAssetNameShow"));
				$("#queryCommMode").val(getMultiSelValues("queryCommModeShow"));
				$("#queryDate").val($("#queryDateShow").datebox("getValue"));
				
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle);
				
				actionClicked( document.forms["listReportFrom"],
					'${ucNo}',
					'',
					'<%=IAtomsConstants.ACTION_EXPORT%>');
				
				ajaxService.getExportFlag('${ucNo}',function(data){
					$.unblockUI();
				});
			}
		}
	}
</script>
</head>
<body>
<!-- 	<div region="center"
		style="width: auto; height: auto; padding: 2px; overflow-y: hidden"> -->
<div style="width: auto; padding: 1px;" region="center" class="setting-panel-height">
	<form id="listReportFrom" class="roleFunction" method="post" style="height: 80%;">
		<table id="dataGrid" class="easyui-datagrid" title="設備狀態報表"
			style="width: 99%; height: 125%"
			data-options="	
						pagination:true,
						pageNumber:0,
						pageList:[15,30,50,100],
						pageSize:15,
						iconCls: 'icon-edit',
						singleSelect: true,
						method: 'post',
						nowrap:false,
						toolbar:'#toolBar'">
			<thead>
			
				<tr>
					<th data-options="field:'number',align:'left',halign:'center', width:30, styler: function(){return 'background-color: #F4F4F4;';}"></th>
					<th data-options="field:'companyName',sortable:true,align:'left', width:200, halign:'center'">客戶</th>
					<th data-options="field:'warehouseName',align:'left',width:200,halign:'center'">倉庫名稱</th>
					<th data-options="field:'warehouseId',align:'left',width:200,halign:'center',hidden:true">倉庫id</th>
					<th data-options="field:'assetCategory',align:'left',width:150,halign:'center'">設備類別</th>
					<th data-options="field:'assetTypeName',styler:cellStyler,width:200,align:'left',halign:'center'">設備名稱</th>
					<th data-options="field:'commModeName',styler:cellStyler,width:200,align:'left',halign:'center'">通訊模式</th>
					<th data-options="field:'inStorage',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">庫存</th>
					<th data-options="field:'inApply',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">領用中</th>
					<th data-options="field:'inBorrow',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">借用中</th>
					<th data-options="field:'inTrans',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">在途中</th>
					<th data-options="field:'inUse',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">使用中</th>
					<th data-options="field:'inRepaired',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">維修中</th>
					<th data-options="field:'inRepair',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">送修中</th>
					<th data-options="field:'toScrap',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">待報廢</th>
					<th data-options="field:'inScrap',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">已報廢</th>
					<th data-options="field:'inDestroy',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">已銷毀</th>
					<th data-options="field:'inReturned',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">已拆回</th>
					<th data-options="field:'inLost',width:80,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:cellStyler">已遺失</th>
				
					<th data-options="field:'total',width:100,align:'right',halign:'center',formatter:function(value,row){if(value != 0 && value != '0') {return value}},styler:function(value){ return 'background-color:#ffee00;'}">總計</th>
				</tr>
			</thead>
		</table>
		<div id="toolBar" style="padding: 2px 5px; height: 120px">
			<form action="assetStatusReport.do" id="queryForm" name="queryForm"
				method="post" novalidate>
				<input type="hidden" id="queryCustomer" name="queryCustomer"
					value="" /> <input type="hidden" id="queryMaType"
					name="queryMaType" value="" /> <input type="hidden"
					id="queryCommMode" name="queryCommMode" value="" /> <input
					type="hidden" id="queryAssetName" name="queryAssetName" value="" />
				<input type="hidden" id="queryDate" name="queryDate" value="" /> <input
					type="hidden" id="reportType" name="reportType" value="" /> <input
					type="hidden" id="queryFlag" name="queryFlag" value="N" /> <input
					type="hidden" id="serviceId" name="serviceId" value="" /> <input
					type="hidden" id="actionId" name="actionId" value="" /> <input
					type="hidden" id="useCaseNo" name="useCaseNo" value="" />
				<table cellpadding="4">
					<tr>
						<td>客戶:</td>
						<td>
							<cafe:droplisttag 
								name="queryCustomerShow"
								id="queryCustomerShow"
								css="easyui-combobox"
								result="${customerList}"
								selectedValue="${empty formDTO.logonUserCompanyId?'':empty customerList?'':customerList[0].value}"
								blankName="請選擇"
								hasBlankValue="true"
								style="width:200px"
								javascript="editable='false' ${empty formDTO.logonUserCompanyId?'':'disabled=true'}"
								>
							</cafe:droplisttag>
						<td>設備名稱:</td>
						<td><cafe:droplistchecktag
	                            id="queryAssetNameShow"
	                            name="queryAssetNameShow" 
	                            css="easyui-combobox"
	                            result="${assetNameList}"
	                            hasBlankValue="true"
	                            blankName="請選擇(複選)"
	                            style="width: 200px"
                           		javascript="multiple=true editable=false"
                            ></cafe:droplistchecktag></td>
					</tr>
					<tr>
						<td>維護模式:</td>
						<td><cafe:droplisttag 
								name="queryMaTypeShow"
								id="queryMaTypeShow" 
								css="easyui-combobox"
								result="${maTypeList}" 
								defaultValue="" 
								blankName="請選擇"
								hasBlankValue="true" 
								style="width:200px"
								javascript="editable='false' panelheight=\"auto\"">
							</cafe:droplisttag></td>
						<td>通訊模式:</td>
						<td> <cafe:droplistchecktag
	                            id="queryCommModeShow"
	                            name="queryCommModeShow" 
	                            css="easyui-combobox"
	                            result="${commModeList}"
	                            hasBlankValue="true"
	                            blankName="請選擇(複選)"
	                            style="width: 200px"
                           		javascript="multiple=true editable=false panelheight=\"auto\""
                            ></cafe:droplistchecktag>
					</tr>
					<tr>
						<td>查詢月份:<span class="red">*</span></td>
						<td colspan="2">
							<input maxlength="7" class="easyui-datebox" 
							name="queryDateShow" id="queryDateShow"
							data-options="validType:'validDateYearMonth',required:'true',missingMessage:'請輸入查詢月份'"
							style="width: 100px" value="${formDTO.queryDate}"/>
						</td>
						<td colspan="1" align="right"><a href="javascript:void(0)"
							class="easyui-linkbutton" iconcls="icon-search"
							onclick="queryData()">查詢</a></td>
					</tr>
				</table>
			</form>
			<div style="width: 100%; display: inline-block;">
				<span id="msg" class="red"
					style="width: 50%; display: inline-block;"></span> <span
					style="width: 50%; display: inline-block; float: right; text-align: right">
					<a href="javascript:void(0)" id="exportData" onclick="exportData('statusDetail')">匯出</a>
					<a href="javascript:void(0)" id="exportDataList" onclick="exportData('statusList')">匯出清單</a>
				</span>
			</div>
		</div>
		</form>
	</div>
	<script type="text/javascript">
		//將小計行背景色設為黃色，總計行背景為深黃色
		function cellStyler(value, row, index) {
			if (row.assetTypeName == "小計") {
				return 'background-color:#ffee00;';
			}
			if (row.assetTypeName == "總計") {
				return 'background-color:#FFD700;';
			}
		}
		//通訊模式下拉框
		$("#queryCommModeShow").combobox({
			onSelect : function(newValue) {
				selectMultiple(newValue, "queryCommModeShow")
			},
			//沒有選中的值時將“請選擇”設為選中
			onUnselect : function(newValue) { 
				unSelectMultiple(newValue, "queryCommModeShow"); 
			},
		});
		//設備名稱下拉框
		$("#queryAssetNameShow").combobox({
			onSelect : function(newValue) {
				selectMultiple(newValue, "queryAssetNameShow")
			},
			//沒有選中的值時將“請選擇”設為選中
			onUnselect : function(newValue) { 
				unSelectMultiple(newValue, "queryAssetNameShow"); 
			},
		});
		
		/**
		 *獲取復選下拉框查詢條件
		 *selName:複選框的節點id
		 */
		function getMultiSelValues(selName) {
			var result = "";
			//根據id獲取節點
			var sels = document.getElementsByName(selName);
			if (sels != null) {
				//循環節點將選中的值添加到result
				for (var i = 0; i < sels.length; i++) {
					//選中有效值時用逗號拼接
					if (sels[i].value != null && sels[i].value != "") {
						result += "'" + sels[i].value + "',";
					}
				}
				result = result.substring(0, result.length - 1);
			}
			return result;
		}
	</script>
</body>
</html>