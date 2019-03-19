<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetTransInfoFormDTO formDTO = null;
	DmmAssetTransInfoDTO assetTransInfoDTO = null;
	String ucNo = "";
	if(ctx != null) {
		formDTO = (AssetTransInfoFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
			assetTransInfoDTO = formDTO.getAssetTransInfoDTO();
			ucNo = formDTO.getUseCaseNo();
		}  else {
			formDTO = new AssetTransInfoFormDTO();
		}
	}
	String userId = logonUser.getId();
	if (assetTransInfoDTO == null) {
		assetTransInfoDTO = new DmmAssetTransInfoDTO();
	}
	//轉出倉庫列表
	List<Parameter> fromWareHouseList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
	//轉入倉庫列表
	List<Parameter> wareHouseList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
	
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="fromWareHouseList" value="<%=fromWareHouseList%>" scope="page"></c:set>
<c:set var="wareHouseList" value="<%=wareHouseList%>" scope="page"></c:set>
<c:set var="userId" value="<%=userId%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<title></title>
</head>
<body>
 <div id="initHist" style="width: 99%; height: auto;">
  <div><span id="historyMsg" class="red"></span></div>
     <div  style="width: 100%; height: auto;">
     		<form id="queryForm" name="queryForm" method="post">
     			<input type="hidden" id="queryAssetTransId" name="queryAssetTransId" />
                <table cellpadding="4">
					<tr>
						<td>轉出日期:</td>
						<td>
							<input class="easyui-datebox" style="width: 160px" maxlength="10"
								data-options="validType:['maxLength[10]','validDate'],
								onChange:function(newValue,oldValue) {
									$('#queryFromDateEnd').timespinner('isValid');
								}" id="queryFromDateStart" name="queryFromDateStart"> 
							~
							<input class="easyui-datebox" style="width: 160px" maxlength="10"
								data-options="validType:['maxLength[10]','validDate',
									'compareDateStartEnd[\'#queryFromDateStart\',\'轉出日期(起)不可大於轉出日期(迄)\']'],
									onChange:function(newValue,oldValue) {
										$('#queryToDateEnd').timespinner('isValid');
									}" 
								id="queryFromDateEnd" name="queryFromDateEnd">
						</td>
						<td>轉入日期:</td>
						<td>
							<input class="easyui-datebox" style="width: 160px" maxlength="10"
								data-options="validType:['maxLength[10]','validDate'],
									onChange:function(newValue,oldValue) {
										$('#queryToDateEnd').timespinner('isValid');
									}"
								id="queryToDateStart" name="queryToDateStart">
							~
							<input class="easyui-datebox" style="width: 160px" maxlength="10"
								data-options="validType:['maxLength[10]','validDate',
									'compareDateStartEnd[\'#queryToDateStart\',\'轉入日期(起)不可大於轉入日期(迄)\']',
									'compareDateStartEnd[\'#queryFromDateEnd\',\'轉出日期不可大於轉入日期\']']"
								id="queryToDateEnd" name="queryToDateEnd">
						</td>
					</tr>
                    <tr>
                        <td>轉出倉:</td>
                        <td >
                            <cafe:droplisttag css="easyui-combobox"
							id="queryAssetFromWare"
							name="queryAssetFromWare" 
							result="${fromWareHouseList }"
							style="width: 160px"
							blankName="請選擇" hasBlankValue="true"
							javascript="editable=false"
							>
							</cafe:droplisttag>
                        </td>
						<td>轉入倉:</td>
						<td>
							<cafe:droplisttag 
								css="easyui-combobox"
								id="queryAssetToWare"
								name="queryAssetToWare" 
								style="width: 160px"
								result="${fromWareHouseList }" 
								javascript="editable=false"
								blankName="請選擇" 
								hasBlankValue="true">
							</cafe:droplisttag>
						</td>
                    </tr>
                    <tr>
                        <td colspan="7" style="text-align: right;">
                        <a id="btnQuery" class="easyui-linkbutton"  onclick="queryAssetTransHis();" iconcls="icon-search">查詢</a></td>
                    </tr>
                </table>
          </form>      
                <div style="height: 10px"></div>
                <form id="idForm" name="idForm" method="post">
                <table cellpadding="3" style="width: 100%; height: auto">
                    <tr>
                        <td style="width: 75px">轉倉批號:</td>
                        <td colspan="1">
                             <cafe:droplisttag css="easyui-combobox"
							id="historyAssetTransId"
							name="historyAssetTransId"
							result="${transDefaultValues }" 
							blankName="請選擇"
							hasBlankValue="true"
							javascript="editable=false data-options=\"valueField:'value',textField:'name'\""
							style="width:155px">
							</cafe:droplisttag>
                        </td>
                    </tr>
                    <tr>
                        <td >轉出倉:</td>
                        <td style="width: 60px" id="fromHouse" colspan="1"></td>
                        <td style="width: 60px;">轉入倉:</td>
                        <td style="width: 120px;" id="toHouse" colspan="1"> </td>
                        <td style="width: 70px;">轉倉結果:</td>
                        <td id="result" colspan="2"></td>
                    </tr>
                    <tr>
                        <td >說明:</td>
                        <td style="text-align: left;word-wrap:break-word;word-break:break-all;" id="comTxt" colspan="6">
                        </td>
                    </tr>
                </table>
                 <div>
                     <a href="#" id="exportList" disabled="true" class="easyui-linkbutton" onclick="exportAssetList()" >列印出貨單</a>
                     	 <span style="width: 50%;display: inline-block;float: right;text-align: right">
                      		<a href="#" id="export" style="width: 150px" onclick="doExport()" >匯出</a>
	           			</span>
                 </div>
               </form>  
                <table id="dg" >
                </table>
        </div>
 <script type="text/javascript">
 var isUpdateTransList = false;
 
 $.extend($.fn.validatebox.defaults.rules, {
	 compareDate: {
			validator:function(value,param){
	        	var data = $(param[0]).datebox("getValue");
	        		if(value>=data){
		        		return true;
		        	}else{
		        		return false;
		        	}	
	        },
	        message:'轉入日期應大於轉出日期'
	    },
     validDate : {     
		validator: function(value){
			if(value.length != 10){
				return false;
			}else{
				var y = value.substring(0,4);
				var sep1 = value[4];
				var m = value.substring(5,7);
				var sep2 = value[7];
				var d = value.substring(8,value.length);
				if(sep1 != '/' || sep2 != '/'){
					return false;
				}
				if(parseInt(m) > 12){
					return false;
				}
				if (m == '00') {
					return false;
				}
				if(parseInt(d) > 31){
					return false;
				}
				if (d == '00') {
					return false;
				}
				if(m == '04' || m == '06'|| m == '09'|| m == '11'){
					if(parseInt(d) > 30){
						return false;
					}
				}maxlength="10"
				if(parseFloat(y) % 4 == 0){
					if(m == '02'){
						if(parseInt(d) > 29){
							return false;
						}
					}
				}else{
					if(m == '02'){
						if(parseInt(d) > 28){
							return false;
						}
					}
				}
			}
			return true;
		},     
		message: '日期格式限YYYY/MM/DD'    
	}, 
  });
	var isOpenBlockStyleHis = true;
	//選擇轉倉批號查詢list清單
	$(function(){
		$('#export').attr("onclick","return false;");
		$('#export').attr("style","color:gray;");
		$("#historyAssetTransId").combobox({
			onChange:function(newValue, oldValue){
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				if (isOpenBlockStyleHis) {
					$.blockUI(blockStyle1);
				}
				if (newValue != "") {
					ajaxService.getAssetTransInfoByAssetTransId(newValue, function(data){
						if (data != null) {
							var isOpenExportList = true;
	 						$("#fromHouse").text(data.fromWarehouseName);
							$("#toHouse").text(data.toWarehouseName);
							$("#comTxt").html(data.comment.replaceAll("\n","<br>").replaceAll(" ","&nbsp"));
							if(data.isTransDone == 'Y') {
								$("#result").text("轉倉完成已確認");
							} else if(data.isBack == 'Y' && data.isCancel == 'N') {
								$("#result").text("轉倉退回已確認");
							} else if(data.isBack == 'Y' && data.isCancel == 'Y') {
								$("#result").text("轉倉取消已確認");
								isOpenExportList = false;
							}
							queryList(newValue, isOpenExportList);
							$("#queryAssetTransId").val(newValue);
						} else {
							$("#fromHouse").text("");
							$("#toHouse").text("");
							$("#comTxt").text("");
							$("#result").text("");
						}
					});
				} else {
					queryList("");
				}
				$.unblockUI();
				isOpenBlockStyleHis = true;
			}
		});
		// 查詢datagrid初始化
		setTimeout("initTransHisDatagrid();",5);
	});
	
	/*
	*初始化查詢datagrid3
	*/
	function initTransHisDatagrid(){
		var grid = $("#dg");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 100%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'serialNumber',halign:'center',sortable:true,width:'200px',title:"設備序號"},
				{field:'name',halign:'center',sortable:true,width:'200px',title:"設備名稱"},
				{field:'transStatus',halign:'center',sortable:true,width:'150px',title:"轉倉結果"},
				{field:'isEnabled',width:'60px',sortable:false,width:'100px',align:'center',formatter:function(value,row){if(row.isEnabled == '<%=IAtomsConstants.YES%>'){return '<input type=checkbox checked  disabled/>';}else{ return '<input type=checkbox disabled/>';}},title:"已啟用"},
				{field:'isCup',width:'60px',sortable:false,width:'100px',align:'center',formatter:function(value,row){if(row.isCup == '<%=IAtomsConstants.YES%>'){return '<input type=checkbox checked  disabled/>';}else{ return '<input type=checkbox disabled/>';}},title:"銀聯"},
				{field:'comment',width:160,halign:'center',sortable:true,title:"轉倉說明"},
				{field:'contractCode',halign:'center',width:'150px',sortable:true,title:"合約編號"},
				{field:'assetUser',width:160,halign:'center',sortable:true,hidden:true,title:""},
				{field:'assetUserName',width:160,halign:'center',sortable:true,title:"使用人"},
				{field:'updatedByName',halign:'center',width:'150px',sortable:true,title:"異動人員"},
				{field:'updatedDate',width:'180px',halign:'center',align:'center',sortable:true,formatter:formatToTimeStamp,title:"異動日期"}
			];
			// 創建datagrid
			grid.datagrid({
				title:'轉倉清單',
				columns:[datagridColumns],
				rownumbers:true,
                pagination:true,
                pageNumber:0,
                pageList:[15,30,50,100],
				pageSize:15,
                iconCls: 'icon-edit',
			    singleSelect: true,
			    nowrap:false,
			    toolbar: '#tb'
			});
		}
	}
	
 	//查詢轉倉清單
	function queryList(assetTransId, isOpenExportList) {
		var queryParam = {assetTransId : assetTransId};
		var options = {
				url : "${contextPath}/assetTransInfoHistory.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>&isHistory=true",
				queryParams :queryParam,
				pageNumber:1,
				onLoadSuccess:function(data){
					if (!isUpdateTransList) {
						$("#historyMsg").text("");
						isUpdateTransList = false;
					}
					if (data.total == 0) {
						$("#fromHouse").text("");
						$("#toHouse").text("");
						$("#comTxt").text("");
						$("#result").text("");
						$('#export').attr("onclick","return false;");
						$('#export').attr("style","color:gray;");
						$('#exportList').linkbutton('disable');
					}else{
						if (isOpenExportList) {
							$('#exportList').linkbutton('enable');
						} else {
							$('#exportList').linkbutton('disable');
						}
						$('#export').attr("onclick","return doExport();");
						$('#export').attr("style","color:blue;");
						
					}
				},
				onLoadError : function() {
					$("#historyMsg").text("查詢失敗！請聯繫管理員");
				}
			}
			options.sortName="";
			openDlgGridQuery.ignoreFirstLoad = true;
			openDlgGridQuery("dg", options);
			openDlgGridQuery.ignoreFirstLoad = false;
	}
	
	
	//查詢轉倉批號
	function queryAssetTransHis() {
		$("#historyMsg").text("");
		if (!$("#queryForm").form('validate')) {
			return false;
		}
		var selectValue = $("#historyAssetTransId").combobox("getValue");
		var queryFromDateStart = $("#queryFromDateStart").textbox('getValue');
		var queryFromDateEnd = $("#queryFromDateEnd").textbox('getValue');
		var queryToDateStart = $("#queryToDateStart").textbox('getValue');
		var queryToDateEnd = $("#queryToDateEnd").textbox('getValue');
		var queryFromWarehouseId = $("input[name=queryAssetFromWare]").val();
		var queryToWarehouseId = $("input[name=queryAssetToWare]").val();
		if (queryFromDateStart != "" || queryFromDateEnd != "") {
			if (queryFromDateStart == "") {
				$("#historyMsg").text("請輸入轉出日期起");
				return false;
			}
			if (queryFromDateEnd == "") {
				parent.$.messager.progress('close');
				$("#historyMsg").text("請輸入轉出日期迄");
				return false;
			}
		}
		if (queryToDateStart != "" || queryToDateEnd != "") {
			if (queryToDateStart == "") {
				$("#historyMsg").text("請輸入轉入日期起");
				return false;
			}
			if (queryToDateEnd == "") {
				$("#historyMsg").text("請輸入轉入日期迄");
				return false;
			}
		}
		isOpenBlockStyleHis = false;
		var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle1);
		ajaxService.getAssetInfoList('${userId}', queryFromDateStart, queryFromDateEnd, queryToDateStart, queryToDateEnd, queryFromWarehouseId, queryToWarehouseId, function(data){
			$.unblockUI();
			if (data.length>0) {
					$("#historyAssetTransId").combobox("loadData", initSelect(data));
					$("#historyAssetTransId").combobox('setValue', data[1].value);
					if (selectValue == data[1].value) {
						isOpenBlockStyleHis = true;
					}
				}else{
					isUpdateTransList = true;
					$("#historyAssetTransId").combobox("loadData", initSelect(null));
					$("#historyAssetTransId").combobox('setValue', "");
					$("#historyMsg").text("查無資料");
				}
		});
	}
	
	//匯出
	function doExport(){
		var queryAssetTransId = $('#historyAssetTransId').combobox("getValue");
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		
		window.location.href='${contextPath}/assetTransInfoHistory.do?actionId=<%=IAtomsConstants.ACTION_EXPORT%>&isHistory=true&assetTransId='+queryAssetTransId;
		
		ajaxService.getExportFlag('${ucNo}',function(data){
			$.unblockUI();
		});
	}
	
	//列印出貨單
	function exportAssetList() {
		var queryAssetTransId = $('#historyAssetTransId').combobox("getValue");
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		
		window.location.href='${contextPath}/assetTransInfoHistory.do?actionId=exportAsset&isHistory=true&assetTransId='+queryAssetTransId;
		
		ajaxService.getExportFlag('${ucNo}',function(data){
			$.unblockUI();
		});
	}
</script>
</body>
</html>