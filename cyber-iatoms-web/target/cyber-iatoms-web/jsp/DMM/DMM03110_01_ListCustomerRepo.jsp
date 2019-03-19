<%@page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.RepoStatusReportDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CustomerRepoFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CustomerRepoFormDTO formDTO = null;
	List<String>  assetCategoryKeyList = null;
	List<String> assetList = null;
	List<String> assetEDCList = null;
	String ucNo = "";
	List<RepoStatusReportDTO> results = null;
	String messager = "";
	Map<String, String> assetStatus = null;
	String jsonData = "";
	Integer count = null;
	String logonUserCompanyId = null;
	if (ctx != null) {
		if(message != null){
			messager = i18NUtil.getName(message.getCode(),message.getArguments(),null);
			if(message.getStatus() == Message.STATUS.SUCCESS){
				formDTO = (CustomerRepoFormDTO) ctx.getResponseResult();
			}else{
				formDTO = (CustomerRepoFormDTO) ctx.getRequestParameter();
			}
			if(formDTO != null){
				ucNo = formDTO.getUseCaseNo();
				//後台返回的查詢結果轉換過的json字符串
				jsonData = formDTO.getJsonData();
				//筆數
				count = formDTO.getPageNavigation().getRowCount();
				//當前登入者公司id
				logonUserCompanyId = formDTO.getLogonUserCompanyId();
				//設備類別list
				assetCategoryKeyList = formDTO.getAssetCategoryKeyList();
				//周邊設備list
				assetList = formDTO.getAssetList();
				//edc的list
				assetEDCList = formDTO.getAssetEDCList();
			}
		}
	}
	//維護類型
	List<Parameter> maTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,IATOMS_PARAM_TYPE.MA_TYPE.getCode());
	//登入着不是客戶的公司list
	List<Parameter> companyList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,IAtomsConstants.PARAM_COMPANY_LIST);
	//登入着為客戶的公司list
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,IAtomsConstants.PARAM_CUSTOMER_LIST);
	//List<Parameter> assetNameList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,IATOMS_PARAM_TYPE.ASSET_NAME.getCode());
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<!-- DataLoader -->
<c:set var="maTypeList" value="<%=maTypeList%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<%--<c:set var="assetNameList" value="<%=assetNameList%>" scope="page"></c:set> --%>
<c:set var="messager" value="<%=messager%>" scope="page"></c:set>
<c:set var="jsonData" value="<%=jsonData%>" scope="page"></c:set>
<c:set var="count" value="<%=count%>" scope="page"></c:set>
<c:set var="assetCategoryKeyList" value="<%=assetCategoryKeyList%>" scope="page"></c:set>
<c:set var="assetList" value="<%=assetList%>" scope="page"></c:set>
<c:set var="assetEDCList" value="<%=assetEDCList%>" scope="page"></c:set>
<c:set var="logonUserCompanyId" value="<%=logonUserCompanyId%>" scope="page"></c:set>
<!-- DataLoader -->
<html>
<head>
<title>客戶設備總表</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
	$(function (){
		var options = $("#dataGrid").datagrid("options");
		//var coltitle = [];
		//頁面加載的時候，初始化出來客戶和設備狀態.
		var	coltitle = [{
				field:'shortName',title:'客戶',rowspan:2,width:150,sortable:true,align:'left',halign:'center'
			},{field:'status',title:'設備狀態',rowspan:2,width:100,sortable:false,align:'left',halign:'center'}];
		options.columns = [coltitle,[]];
		//數據為0，初始化頁面或者 查無資料
		if($('#dataGrid').datagrid('getData').total == 0){
			//給匯出設置樣式
			$('#export').attr("onclick","return false;");
			$('#export').attr("style","color:gray;");
			//設置分頁參數
			if ($("#dataGrid").datagrid("options").pageNumber == 1) {
				$('#dataGrid').datagrid({pageNumber:0});	
			}
		}
		$("#btnQuery").click(function(){
			//每頁筆數
			pageSize = options.pageSize;
			query(1,pageSize);
		});
	});
	//查詢   pageIndex 頁碼 ，pageSize 每頁多少條
	function query(pageIndex,pageSize){
		var controls = ['queryDate'];
		var queryForm = $("#queryForm");
		if(validateForm(controls) && queryForm.form("validate")){
		/* var queryForm = $("#queryForm");
		if(queryForm.form("validate")){ */
			
			//$('#dataGrid').datagrid('options').sortName = null;
			//獲取查詢參數
			var queryParam = $("#listCustomerReportFrom").form("getData");
			queryParam.page = pageIndex;
			queryParam.rows = pageSize;
			
			queryParam.queryCustomer = $("#queryCustomer").combobox('getValue');
			var params = queryParam;
			params.actionId = "<%=IAtomsConstants.ACTION_QUERY_DATA%>";
			var url = "${contextPath}/customerAssetList.do";
			var successFunction = function(result){
				if (result.success) {
					$("#msg").text("");
					//設備類別list
					var assetCategoryKeyList = result.assetCategoryKeyList;
					//設備類別EDC對應的設備名稱list
					var assetEDCList = result.assetEDCList;
					//設備類別周邊對應的設備名稱list
					var assetList = result.assetList;
					//數量
					var count = result.count;
					//後台傳過來的json數據
					var jsonData = eval(result.jsonData);	
					var coltitle = [{
						field:'shortName',title:'客戶',rowspan:2,width:150,sortable:true,align:'left',halign:'center'
					},{field:'status',title:'設備狀態',rowspan:2,width:100,sortable:false,align:'left',halign:'center'}];
					//循環添加field
					for(var i = 0; i < assetCategoryKeyList.length; i ++) {
						if (assetCategoryKeyList[i] == "EDC") {
							coltitle.push({title:assetCategoryKeyList[i],colspan:assetEDCList.length,width:150});
						} else {
							coltitle.push({title:assetCategoryKeyList[i],colspan:assetList.length,width:150});
						}
					}
					var col = [];	
					//添加edc對應的設備名稱
					if(assetEDCList.length > 0){
						for(var k = 0; k < assetEDCList.length; k ++) {
							col.push({field:assetEDCList[k],title:assetEDCList[k],width:120,align:'right',halign:'center',sortable:false});
						}
					}
					//添加周邊設備對應的設備名稱
					if(assetList.length >0){
						for(var j = 0; j < assetList.length; j ++) {
							col.push({field:assetList[j],title:assetList[j],width:120,align:'right',halign:'center',sortable:false});
						}
					}
					var options = $("#dataGrid").datagrid("options");
					//根據json數據來判斷設置匯出的樣式
					if (jsonData) {
						options.data = jsonData;
						$('#export').attr("onclick","exportData()");
						$('#export').attr("style","color:blue;");
					} else {
						options.data = [];
						$('#export').attr("onclick","return false;");
						$('#export').attr("style","color:gray;");
					}
					
					options.columns = [coltitle,col];
					options.pageSize = pageSize;
					options.pageNumber = pageIndex;
					options.height = "550px";
					//排序事件的觸發
					options.onSortColumn = function(sort, order) {
						$("#sort").val(sort);
						$("#order").val(order);
						query(pageIndex,pageSize);
					};
					$('#dataGrid').datagrid(options);
					var pager = $("#dataGrid").datagrid("getPager"); 
					pager.pagination({ 
						total:count, 
						pageNumber:pageIndex,
						pageSize:pageSize,
						//分頁事件的觸發
						onSelectPage:function (pageNo, pageSize) {
							query(pageNo,pageSize);
						} 
					});
					
					var arr =[{mergeFiled:"shortName",premiseFiled:"shortName"}];
					//合并列的field数组及对应前提条件filed（为空则直接内容合并）  
					mergeCells(arr,"dataGrid");
					$("#msg").text(result.msg);
				}
			}
			var errorFunction = function(){
					$.messager.alert('提示', "error", 'error');
				}
			commonQuery(params, url, successFunction, errorFunction);
		}
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		//options.sortName = null;
	}
	/*
	*給查詢加遮罩  params 查詢參數 ， url 鏈接地址. successFunction：成功執行的函數.errorFunction：錯誤執行的函數
	*/
	function commonQuery(params, url, successFunction, errorFunction){
		if (params && url) {
				// 遮罩样式
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				// 形成遮罩
				$.blockUI(blockStyle);
				// ajax请求
				$.ajax({
					url:url,
					data:params,
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(data) {
						//$("#listReportFrom").css("height", '100%');
						// 去除遮罩
						$.unblockUI();
						// 请求成功时加载的函数
						var deleteSuccess = successFunction;
						if(deleteSuccess){
							deleteSuccess(data);
						}
					},
					error:function(){
						// 去除遮罩
						$.unblockUI();
						// 请求失败时加载的函数
						var deleteError = errorFunction;
						if(deleteError){
							deleteError();
						}
					}
				});
		}
	}
	//匯出
	function exportData(){
		var controls = ['queryDate'];
		var queryForm = $("#queryForm");
		if(validateForm(controls) && queryForm.form("validate")){
			var queryCustomer = $("#queryCustomer").combobox('getValue');
			$("#exportQueryCustomer").val(queryCustomer);
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			//提交表單
			actionClicked( document.forms["listCustomerReportFrom"],
				'${ucNo}',
				'',
				'<%=IAtomsConstants.ACTION_EXPORT%>');
			
			ajaxService.getExportFlag('${ucNo}',function(data){
				$.unblockUI();
			});
			$('#export').attr("onclick","return exportData();");
			$('#export').attr("style","color:blue;");
		}
	}
</script>
</head>
<body>
<div style="width: auto;  padding: 1px;"  region="center" class="setting-panel-height">
<form id="listCustomerReportFrom" class="roleFunction" method="post" style="height: 80%;">
<!-- 	<div region="center" style="width: auto; height: auto; padding: 0; background: #eee; overflow-y: hidden"> -->
        <table id="dataGrid" class="easyui-datagrid" title="客戶設備總表" style="width: 99%; height: auto"
            data-options="
				rownumbers:true,
				pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				pagination:true,
				singleSelect: true,
				method: 'post',
				nowrap:false,
				toolbar:'#toolbar'">
        </table>
      <div id="toolbar" style="padding: 2px 5px;height:auto">
      	<form  id="queryForm" name="queryForm" method="post" novalidate>
      		<input type="hidden" id="queryFlag" name="queryFlag" value="${formDTO.queryFlag}"/>
      		<input type="hidden" id="serviceId" name="serviceId" value=""/>
        	<input type="hidden" id="actionId" name="actionId" value=""/>
        	<input type="hidden" id="useCaseNo" name="useCaseNo" value=""/>
        	<input type="hidden" id="page" name="page" value="${empty pageNumber? 1: formDTO.page }"/>
        	<input type="hidden" id="rows" name="rows" value="${empty formDTO.rows? 10: formDTO.rows }"/>
        	<input type="hidden" id="sort" name="sort" value=""/>
        	<input type="hidden" id="order" name="order" value=""/>
        	<input type="hidden" id="exportQueryCustomer" name="exportQueryCustomer" value=""/>
            客戶:
        <c:choose>
      	 <c:when test="${not empty logonUserCompanyId}">
	            <cafe:droplisttag 
	    			name="queryCustomer"
	    			id="queryCustomer"
	    			css="easyui-combobox"
	    			result="${customerList}"
	    			selectedValue="${logonUserCompanyId}"
	    			hasBlankValue="true"
		            blankName="" 
	    			style="width:150px"
	    			javascript="editable='false' disabled=disabled"
	    		>
	    		</cafe:droplisttag>
	    	</c:when>
	    	<c:otherwise>
	            <cafe:droplisttag 
	    			name="queryCustomer"
	    			id="queryCustomer"
	    			css="easyui-combobox"
	    			result="${companyList}"
	    			selectedValue=""
	    			blankName="請選擇"
	    			hasBlankValue="true"
	    			style="width:150px"
	    			javascript="editable='false'"
	    		>
	    		</cafe:droplisttag>
	    	</c:otherwise>
	    </c:choose>
            查詢月份:<span class="red">*</span>
       		<input class="easyui-datebox" maxlength="7"
       				name="queryDate" id="queryDate"
       		 		data-options="validType:'validDateYearMonth',required:'true',missingMessage:'請輸入查詢月份'" 
       		 		required="true"
       		 		style="width: 100px" 
       		 		value="${empty formDTO.queryDate? formDTO.currentDate: formDTO.queryDate }  " 
       		 		missingMessage="請輸入查詢月份"/>
    	 	維護類型:
    	 	<cafe:droplisttag 
    			name="queryMaType"
    			id="queryMaType"
    			css="easyui-combobox"
    			result="${maTypeList}"
    			defaultValue=""
    			blankName="請選擇"
    			hasBlankValue="true"
    			style="width:150px"
    			javascript="panelHeight='auto' editable='false'"
    		>
    		</cafe:droplisttag>
	   	 	<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" id="btnQuery">查詢</a>
	   	 	<div style="width: 100%;display: inline-block;">
	           	<span id="msg" class="red" style="width: 50%;display: inline-block;"></span>
	           	<span style="width: 50%;display: inline-block;float: right;text-align: right">
	           		<a href="javascript:void(0)" id="export" style="width: 150px" onclick="exportData()">匯出</a>
	           	</span>
	           </div>
       </form> 
	  </div>
	  </form>
   </div>
<script type="text/javascript">
	$(function(){
		//設定查詢月份欄位的日曆控件為年月
		createMonthDataBox('queryDate');
		textBoxDefaultSetting($("#queryDate"));
	});
</script>
</body>
</html>