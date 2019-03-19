<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeListDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetStacktakeFormDTO formDTO = null;
	//新增时的设备批号
	String addAssetInventoryId = null;
	if (ctx != null) {
		formDTO = (AssetStacktakeFormDTO)ctx.getResponseResult();
	}
	if (formDTO == null) {
		formDTO = new AssetStacktakeFormDTO();
	}
	//倉庫集合
	List<Parameter> warehouseList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
	//設備類別
	List<Parameter> assetNameList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ACTION_GET_ASSET_NAME_LIST);
%>
<c:set var="warehouseList" value="<%=warehouseList%>"></c:set>
<c:set var="assetNameList" value="<%=assetNameList%>"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%@include file="/jsp/common/easyui-common.jsp" %>
</head>
<body>

     <div style="padding: 10px">
           <div><span id="messages" class="red"></span></div>
                <table cellpadding="4" style="width: auto; height: auto">
                    <tr>
                        <td>盤點倉庫:</td>
                        <td>
                            <cafe:droplisttag
								css="easyui-combobox"
								id="historyWarWarehouseId"
								name="historyWarWarehouseId" 
								result="${warehouseList }"
								javascript="editable=false"
								blankName="請選擇"
								hasBlankValue="true"
								style="width:200px">
                           	</cafe:droplisttag>
                        </td>
                        <td>設備名稱:</td>
                        <td>
                            <cafe:droplisttag
	                            css="easyui-combobox"
	                            id="historyAssetTypeId"
								name="historyAssetTypeId" 
								result="${assetNameList }"
								javascript="editable=false"
								blankName="請選擇"
								hasBlankValue="true"
								style="width:200px">
                           </cafe:droplisttag>
                        </td>
                        <td>
                            <a href="#" class="easyui-linkbutton" iconcls="icon-search" onclick="query()">查詢</a> </td>
                    </tr>

                </table>
                <div style="height: 10px"></div>
				<form id="formt"> 
               <table cellpadding="4" style="width: auto; height: auto" >
                    <tr>
                        <td width="75px">盤點批號:</td>
                        <td colspan="3">
                            <cafe:droplisttag
                            	css="easyui-combobox"
                            	id="historyAssetInventoryId"
								name="historyAssetInventoryId" 
								result="${inventoryNumberList }"
								javascript="editable='false' valueField='value' textField='name' "
								blankName="請選擇"
								hasBlankValue="true"
								style="width:150px">
                     		</cafe:droplisttag>
                     		<input name="queryHistoryStocktackId"  id="queryHistoryStocktackId" type="hidden"  value="" />
                        </td>

                    </tr>
                    <tr>
                        <td width="75px">盤點倉庫:</td>
                        <td id="historyWarehouseId" width="200px">
                        </td >
                        <td width="85px">應盤點總數:</td>
                        <td id="historyCount">
                        </td>
                    </tr>
                    <tr>
                        <td width="85px">設備名稱:</td>
                        <td id="historyName" width="200px"></td>
                        <td width="75px">設備狀態:</td>
                        <td id="historystatus" ></td>
                    </tr>
                    <tr>
                    	<td width="65px">說明:</td>
                        <td id="historyComment" colspan="3">
                        </td >
                    </tr>
                </table>
				</form>
                <div id="tb" style="padding: 2px 5px;">

                    <a href="javascript:void(0)" class="easyui-linkbutton" id="exporetInventory" disabled="disabled" onclick="exportData();" >列印清冊</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" id="exporetSum" disabled="disabled" onclick="exportSum();" >列印盤點結果</a>

                </div>

                <table id="Table1" >
                </table>

                <br />


                <table id="dg2" >
                </table>
                
   <script type="text/javascript">
	$(function () {
	//點擊盤點批號時加載數據
	$("#historyAssetInventoryId").combobox({
		onChange:function(newValue, oldValue) {
			$("#messages").empty();
   			if  (!isEmpty(newValue)) {
   				//根據盤點批號查詢盤點詳細信息
   				ajaxService.getAssetStocktackInfoByStocktackId(newValue, function(data){
					if (data != null) {
						$("#messages").text("");
						//盤點倉庫
						$("#historyWarehouseId").html(data.warHouseName);
						//應盤點總數
						$("#historyCount").html(formatCapacity(data.count));
						//設備名稱
   						$("#historyName").html(data.assetTypeName);
   						//設備狀態
   						$("#historystatus").html(data.assetStatusName);
   						//說明
   						$("#historyComment").html(data.remark.replaceAll("\n","<br>").replaceAll(" ","&nbsp"));
					}
   				});
   			} else {
   				//盤點倉庫
   				$("#historyWarehouseId").html("");
   				//應盤點總數
   				$("#historyCount").html("");
   				//設備名稱
    			$("#historyName").html("");
    			//設備狀態
    			$("#historystatus").html("");
    			//說明
    			$("#historyComment").html("");
   			}
   			if (!isEmpty(newValue)) {
   				//查询當前批号下的所有资料明细
   				getAssetInventorys(newValue, "1"); 
   				//查詢盤點清冊數據
   				getAssetStocktacks(newValue);
   			}else{
   				//查询當前批号下的所有资料明细
   				getAssetInventorys("", "1"); 
   				//查詢盤點清冊數據
   				getAssetStocktacks("");
   			} 
   		}
 	});	
	
	// 查詢datagrid初始化
	setTimeout("initStackHisItemDatagrid();",5);
	setTimeout("initStackHisResultDatagrid();",10);
});
	
	/*
	*初始化查詢datagrid
	*/
	function initStackHisItemDatagrid(){
		var grid = $("#Table1");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: auto; height: auto;");
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
			    iconCls: 'icon-edit',
			    singleSelect: true,
			    method: 'get',
			    pageList:[15,30,50,100],
				pageSize:15,
				nowrap:false,
			    pageNumber:0
			});
			
		}
	}
	/*
	*初始化查詢datagrid
	*/
	function initStackHisResultDatagrid(){
		var grid = $("#dg2");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: auto; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'stocktakeStatus',halign:'center',width:140,sortable:true,formatter:function(value,row){if(row.stocktakeStatus ==0){return '待盤點';}else if(row.stocktakeStatus ==1){return '已盤點';}else if(row.stocktakeStatus ==2){return '盤盈';} else {return '盤差';}},title:"盤點狀態"},
				{field:'assetStatus',halign:'center',hidden:true,sortable:true,title:"設備狀態ID"},
				{field:'serialNumber',halign:'center',width:140,sortable:true,title:"設備編號"},
				{field:'assetTypeId',halign:'center',hidden:true,sortable:true,title:"類別ID"},
				{field:'assetTypeName',halign:'center',width:150,sortable:true,title:"設備名稱"},
				{field:'remark',width:250,halign:'center',sortable:true,title:"盤點說明"},
				{field:'updatedByName',width:140,halign:'center',sortable:true,title:"異動人員"},
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
				ortName:'serialNumber',sortOrder:'asc',
				singleSelect: true,
				method: 'get',
				nowrap:false,
				pageNumber:0
			});
			
		}
	}
	
	//匯出盤點結果
	function exportSum() {
		//盤點批號
		var stocktackId = $("#historyAssetInventoryId").combobox('getValue');
		var historyStocktackId = $("#queryHistoryStocktackId").val();
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		var useCaseNo = '<%=IAtomsConstants.UC_NO_DMM_03080%>';
		window.location.href='${contextPath}/assetStocktake.do?actionId=exportSummaryHistory&assetInventoryId=' + stocktackId + '&queryStocktackId=' + historyStocktackId +"&useCaseNo="+useCaseNo;
		
		ajaxService.getExportFlag(useCaseNo,function(data){
			$.unblockUI();
		});
	}
	//匯出列印清冊
	function exportData() {	
		//盤點批號
		var stocktackId = $("#historyAssetInventoryId").combobox('getValue');
		var historyStocktackId = $("#queryHistoryStocktackId").val();
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		var useCaseNo = '<%=IAtomsConstants.UC_NO_DMM_03080%>';
		window.location.href='${contextPath}/assetStocktake.do?actionId=exportInventoryHistory&assetInventoryId=' + stocktackId + '&queryStocktackId=' + historyStocktackId +"&useCaseNo="+useCaseNo;
		
		ajaxService.getExportFlag(useCaseNo,function(data){
			$.unblockUI();
		});
	}
	//禁用
	function disableLink(link) {
    		//删除href属性,使其成为文本元素
    		link.removeAttribute("href");
    		//设置disabled属性
    		link.setAttribute("disabled","disabled");
	}
	//启用
	function enableLink(link) {
	    // 重新设置href
	    link.setAttribute("href","javascript:void(0)");
	    // 删除disabled属性
	    link.removeAttribute("disabled");
	    
	}
 	//獲取當前批号下的所有资料明细
   	function getAssetInventorys(stocktackId, isComplete) {
		$('#queryHistoryStocktackId').val(stocktackId);
		var isTrue="1";
		var queryParams = {
			queryStocktackId : stocktackId,
			//盤點批號
			assetInventoryId : $("#historyAssetInventoryId").combobox("getValue"),
		};
		queryParams.isComplete = isComplete;
		var options = {
			url : '${contextPath}/assetStocktake.do?actionId=list',
			queryParams :queryParams,
			sortName:"",
			onLoadSuccess : function(data){
				var rowLength = getGridRowsCount("Table1");
				if (rowLength >= 1) {
					//列印清冊設置可編輯
					$("#exporetInventory").linkbutton('enable');
					//列印盤點結果設置可編輯
    				$("#exporetSum").linkbutton('enable');
				} else {
					//列印清冊設置不可編輯
					$("#exporetInventory").linkbutton('disable');
					//列印盤點結果設置不可編輯
    				$("#exporetSum").linkbutton('disable');
				}
			},
			onLoadError : function() {
				$("#messages").text("查詢失敗！請聯繫管理員");
			}
		};
		openDlgGridQuery("Table1", options); 
	} 
	//獲取當前批号下的盤點清冊數據
	function getAssetStocktacks(stocktackId) {
		var options = {
			url : "${contextPath}/assetStocktake.do?actionId=query&queryStocktackId=" + stocktackId,
			autoRowHeight : true,
			pageNumber : 1,
			sortName:"",
			onLoadSuccess : function(data){
				var rowLength = getGridRowsCount("dg2");
				if (rowLength >= 1) {
					//列印清冊設置可編輯
					$("#exporetInventory").linkbutton('enable');
					//列印盤點結果設置可編輯
					$("#exporetSum").linkbutton('enable');
				} else {
					//列印清冊設置不可編輯
					$("#exporetInventory").linkbutton('disable');
					//列印盤點結果設置不可編輯
					$("#exporetSum").linkbutton('disable');
				}
			},
			onLoadError : function() {
				$("#messages").text("查詢失敗！請聯繫管理員");
			}
		};
		openDlgGridQuery("dg2", options); 
	}
	//查詢盤點批號
	function query() {
		//查詢條件--倉庫Id
		historyWarWarehouseId = $("#historyWarWarehouseId").combobox("getValue");
		//查詢條件--設備名稱
		historyAssetTypeIdList = $("#historyAssetTypeId").combobox("getValue");
		//由倉庫和設備獲取盤點批號
		ajaxService.getStocktackIdByWarehouseAndAssetType(historyWarWarehouseId, historyAssetTypeIdList, function(data){
   			var len=0;
			if(Boolean(data)){
				for(i in data)len++;
			}
			if (len!="32") {
				//設置第一條默認提示
				var value = data[0]["value"];
				//盤點批號重新加載數據
				$("#historyAssetInventoryId").combobox("loadData", data);
				//盤點批號設置值
				$("#historyAssetInventoryId").combobox("setValue", value);
			} else {
				data.insert("0",{"value":"","name":"請選擇"});
				//盤點批號重新加載數據
				$("#historyAssetInventoryId").combobox("loadData", data);
				//盤點批號置空
				$("#historyAssetInventoryId").combobox("setValue", "");
				$("#messages").text("查無資料");
			}
    	});
	}
</script>
</body>
</html>