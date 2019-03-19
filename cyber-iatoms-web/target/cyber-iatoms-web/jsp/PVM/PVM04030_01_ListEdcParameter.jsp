<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.EdcParameterDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterItemDTO"%>
<script type="text/javascript" src="assets/jquery-easyui-1.4.3/datagrid-detailview.js"></script> 
<%
	//空的列表
	List<Parameter> emptyList = new ArrayList<Parameter>();
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	EdcParameterFormDTO edcParameterFormDTO = null;
	String ucNo = null;
	String roleAttribute = null;
	String logonUserCompanyId = null;
	if (ctx != null) {
		// 得到FormDTO
		edcParameterFormDTO = (EdcParameterFormDTO) ctx.getRequestParameter();
		if (edcParameterFormDTO != null) {
			// 获得UseCaseNo
			ucNo = edcParameterFormDTO.getUseCaseNo();
			roleAttribute = edcParameterFormDTO.getRoleAttribute();
			logonUserCompanyId = edcParameterFormDTO.getLogonUserCompanyId();
		} else {
			ucNo = IAtomsConstants.UC_NO_PVM_04030;
			edcParameterFormDTO = new EdcParameterFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_PVM_04030;
		edcParameterFormDTO = new EdcParameterFormDTO();
	}
	
	//客戶列表
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//公司列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_COMPANY_LIST);
	// 設備支援功能列表下拉框
	List<Parameter> supportedFunctionList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
	// EDC機型列表
	List<Parameter> edcAssetList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, EdcParameterFormDTO.PARAM_EDC_ASSET_LIST);
	// 周邊設備機型列表
	List<Parameter> categoryRodundAssetList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, EdcParameterFormDTO.PARAM_CATEGORY_RODUND_ASSET_LIST);
	// 交易類別 
	List<Parameter> transCategoryList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANS_CATEGORY_LIST);
	// 設備開啟模式列表
	List<Parameter> openModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ASSET_OPEN_MODE.getCode());
	//交易參數項目列表
	List<Parameter> transationParameterItemList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_ITEM_LIST);
	//獲取交易參數可以編輯的列名，以交易參數分組
	String editFildsMap = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_TRANSACTION_PARAMETER_EDIT_FIELDS_MAP);
	
%>

<html>
<c:set var="formDTO" value="<%=edcParameterFormDTO%>" scope="page"></c:set>
<c:set var="supportedFunctionList" value="<%=supportedFunctionList%>" scope="page"></c:set>
<c:set var="edcAssetList" value="<%=edcAssetList%>" scope="page"></c:set>
<c:set var="categoryRodundAssetList" value="<%=categoryRodundAssetList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="roleAttribute" value="<%=roleAttribute%>" scope="page"></c:set>
<c:set var="logonUserCompanyId" value="<%=logonUserCompanyId%>" scope="page"></c:set>
<c:set var="emptyList" value="<%=emptyList%>" scope="page"></c:set>
<c:set var="transCategoryList" value="<%=transCategoryList%>" scope="page"></c:set>
<c:set var="openModeList" value="<%=openModeList%>" scope="page"></c:set>
<c:set var="transationParameterItemList" value="<%=transationParameterItemList%>" scope="page"></c:set>
<c:set var="editFildsMap" value="<%=editFildsMap %>" scope="page"></c:set>
<c:set var="combobox" value="<%=IAtomsConstants.PARAMTER_ITEM_TYPE_COMBOBOX %>" scope="page"></c:set>
<c:set var="click" value="<%=IAtomsConstants.PARAMTER_ITEM_TYPE_CLICK %>" scope="page"></c:set>
<c:set var="merCode" value="<%=IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE %>" scope="page"></c:set>
<c:set var="merOtherCode" value="<%=IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER %>" scope="page"></c:set>
<c:set var="tid" value="<%=IAtomsConstants.PARAMTER_ITEM_CODE_TID %>" scope="page"></c:set>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$TRANSACTION_CATEGORY" var="transactionCategoryAttr" />
<head>
<meta charset="UTF-8">
<title>Welcome To iATOMS</title>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
<!--     <div data-options="region:'center',fit:true" style="width: auto; height: auto; padding: 1px; overflow-y: auto"> -->
        <table id="dataGrid" class="easyui-datagrid" title="EDC交易參數查詢" style="width: 98%; height: auto"
            data-options="
                rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: true,
				method: 'post',
                sortOrder:'asc',
                pageNumber:0,
                nowrap:false,
				toolbar:'#toolbar'">
            <thead>
                <tr>
                     <th data-options="field:'dtid',width:80,halign:'center',align:'left',sortable:true">DTID</th>
                    <th data-options="field:'customerName',width:260,halign:'center',align:'left',sortable:true">客戶</th>
                    <th data-options="field:'caseId',width:140,halign:'center',align:'left',sortable:true">案件編號</th>
                    <th data-options="field:'merchantCode',width:180,halign:'center',align:'left',sortable:true">特店代號</th>
                    <th data-options="field:'merchantName',width:180,halign:'center',align:'left',sortable:true">特店名稱</th>
                    <th data-options="field:'headerName',width:180,halign:'center',align:'left',sortable:true">表頭（同對外名稱）</th>
                    <th data-options="field:'installedDate',width:180,halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD">裝機日期</th>
                    <th data-options="field:'uninstalledDate',width:180,halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD">拆機日期</th>
                    <th data-options="field:'cupEnableDate',width:180,halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD">CUP 啟用日期</th>
                    <th data-options="field:'cupDisableDate',width:180,halign:'center',align:'center',sortable:true,formatter:formaterTimeStampToyyyyMMDD">CUP 移除日期 </th>
                    <th data-options="field:'openTransactionList',width:180,halign:'center',align:'left',sortable:true">已開放交易清單</th>
                    <th data-options="field:'installedAdress',width:200,halign:'center',align:'left',sortable:true">裝機地址</th>
                    <th data-options="field:'businessAddress',width:200,halign:'center',align:'left',sortable:true">營業地址</th>
                    <th data-options="field:'contact',width:200,halign:'center',align:'left',sortable:true">特店聯絡人</th>
                    <th data-options="field:'contactTel',width:180,halign:'center',align:'left',sortable:true">特店聯絡人電話1</th>
                    <th data-options="field:'contactTel2',width:180,halign:'center',align:'left',sortable:true">特店聯絡人電話2</th>

                    <th data-options="field:'phone',width:160,halign:'center',align:'left',sortable:true">特店聯絡人行動電話</th>
                    <th data-options="field:'aoName',width:200,halign:'center',align:'left',sortable:true">AO 人員</th>
                    <th data-options="field:'businessHours',width:120,halign:'center',align:'center',sortable:true">營業時間</th>
                    <th data-options="field:'companyName',width:260,halign:'center',align:'left',sortable:true">維護廠商</th>
                    <th data-options="field:'edcTypeName',width:100,halign:'center',align:'left',sortable:true">刷卡機型</th>
                    <th data-options="field:'applicationName',width:150,halign:'center',align:'left',sortable:true">軟體版本</th>
                    <th data-options="field:'openFunctionName',width:140,halign:'center',align:'left',sortable:true">內建功能</th>
                    <th data-options="field:'multiModuleName',width:100,halign:'center',align:'left',sortable:true">雙模組模式</th>
                    <th data-options="field:'ecrConnectionName',width:100,halign:'center',align:'left',sortable:true">ECR 連線</th>
                    <th data-options="field:'connectionTypeName',width:200,halign:'center',align:'left',sortable:true">通訊模式</th>
                    <th data-options="field:'netVendorName',width:80,halign:'center',align:'left',sortable:true">寬頻連線</th>
                    <th data-options="field:'localhostIp',width:130,halign:'center',align:'left',sortable:true">刷卡機 IP 位址</th>
                    <th data-options="field:'gateway',width:130,halign:'center',align:'left',sortable:true">刷卡機 GateWay</th>
                    <th data-options="field:'netmask',width:130,halign:'center',align:'left',sortable:true">刷卡機 Netmask</th>
                    <th data-options="field:'peripheralsName',width:100,halign:'center',align:'left',sortable:true">週邊設備1</th>
                    <th data-options="field:'peripheralsFunctionName',width:150,halign:'center',align:'left',sortable:true">週邊設備功能1</th>
                    <th data-options="field:'peripherals2Name',width:100,halign:'center',align:'left',sortable:true">週邊設備2</th>
                    <th data-options="field:'peripheralsFunction2Name',width:150,halign:'center',align:'left',sortable:true">週邊設備功能2</th>
                    <th data-options="field:'peripherals3Name',width:100,halign:'center',align:'left',sortable:true">週邊設備3</th>
                    <th data-options="field:'peripheralsFunction3Name',width:150,halign:'center',align:'left',sortable:true">週邊設備功能3</th>
                    <th data-options="field:'logoStyle',width:120,sortable:true,halign:'center',align:'left',formatter:fomatterLogoStyle">LOGO</th>
                    <th data-options="field:'isOpenEncrypt',width:120,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo" >是否開啟加密</th>
                    <th data-options="field:'electronicPayPlatform',width:130,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo" >電子化繳費平台</th>
                    <th data-options="field:'electronicInvoice',width:120,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo" >電子發票載具</th>
	                <th data-options="field:'cupQuickPass',width:80,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo" >銀聯閃付</th>
	                
                    <th data-options="field:'updateItem',width:300,halign:'center',align:'left',sortable:true">異動說明</th>
                    <th data-options="field:'installTypeName',width:120,halign:'center',align:'left',sortable:true">裝機類型</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar" style="padding: 2px 5px;">
        <form id="searchForm" style="margin: 4px 0px 0px 0px" method="post">
        			<input type="hidden" id="actionId" name="actionId" />
					<input type="hidden" id="serviceId" name="serviceId" />
					<input type="hidden" id="useCaseNo" name="useCaseNo" /> 
            <table>
                <tr>
                <td>客戶:</td>
              		 		<td>
	             		 		<c:choose>
						      		<c:when test="${not empty logonUserCompanyId}">
							           <c:if test="${formDTO.isNoRoles == true}">
							            	<cafe:droplisttag 
								            	name="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>"
								    			id="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>"
								    			css="easyui-combobox"
								    			result="${emptyList}"
								    			selectedValue=""
								    			hasBlankValue="true"
									            blankName="請選擇" 
									            style="width:180px"
								    			javascript="editable='false' panelheight=\"auto\""
							    			>
							    			</cafe:droplisttag>
							            </c:if>
							    		 <c:if test="${formDTO.isNoRoles == false}">
							            	<cafe:droplisttag 
								            	name="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>"
								    			id="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>"
								    			css="easyui-combobox"
								    			result="${customerList}"
								    			selectedValue="${logonUserCompanyId}"
								    			hasBlankValue="true"
									            blankName="" 
									            style="width:180px"
								    			javascript="editable='false' disabled=disabled"
							    			>
							    			</cafe:droplisttag>
							            </c:if>	 
							    		<input type="hidden" id="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>" name ="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>" value="<c:out value='${logonUserCompanyId}'/>"/>
							    	</c:when>
							    	<c:otherwise>
							            <cafe:droplisttag 
							    			name="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>"
							    			id="<%=EdcParameterFormDTO.QUERY_CUSTOMER_ID %>"
							    			css="easyui-combobox"
							    			result="${companyList}"
							    			selectedValue=""
							    			blankName="請選擇"
							    			hasBlankValue="true"
							    			style="width:180px"
							    			javascript="editable='false'"
							    		>
							    		</cafe:droplisttag>
							    	</c:otherwise>
								</c:choose>
								<input type="hidden" id="<%=EdcParameterFormDTO.PARAM_IS_NO_ROLES %>" name ="<%=EdcParameterFormDTO.PARAM_IS_NO_ROLES %>" value="${formDTO.isNoRoles}"/>
								<input type="hidden" id="<%=EdcParameterFormDTO.PARAM_ROLE_ATTRIBUTE %>" name="<%=EdcParameterFormDTO.PARAM_ROLE_ATTRIBUTE %>" value="${formDTO.roleAttribute}"/> 
							</td>
                    <td>特店代號: 
                    </td>
                    <td>
                        <input class="easyui-textbox" id="<%=EdcParameterFormDTO.QUERY_MERCHANT_CODE %>" name="<%=EdcParameterFormDTO.QUERY_MERCHANT_CODE %>" maxlength="20" data-options="validType:['maxLength[20]']">
                    </td>
                    <td>特店名稱:</td>
                    <td>
                        <input class="easyui-textbox" id="<%=EdcParameterFormDTO.QUERY_MERCHANT_NAME %>" name="<%=EdcParameterFormDTO.QUERY_MERCHANT_NAME %>" maxlength="50" data-options="validType:['maxLength[50]']"></td>
                   
                </tr>
                <tr>
                 	<td>表頭（同對外名稱）:</td>
                    <td>
                        <input class="easyui-textbox" id="<%=EdcParameterFormDTO.QUERY_MERANNOUNCED_NAME %>" name="<%=EdcParameterFormDTO.QUERY_MERANNOUNCED_NAME %>" maxlength="100" data-options="validType:['maxLength[100]']"></td>
                    <td>TID: 
                    </td>
                    <td>
                        <input class="easyui-textbox" id="<%=EdcParameterFormDTO.QUERY_TID %>" name="<%=EdcParameterFormDTO.QUERY_TID %>" maxlength="8" data-options="validType:['maxLength[8]']">
                    </td>
                    <td>DTID: 
                    </td>
                    <td>
                        <input class="easyui-textbox" id="<%=EdcParameterFormDTO.QUERY_DTID %>" name="<%=EdcParameterFormDTO.QUERY_DTID %>" maxlength="8" data-options="validType:['maxLength[8]']">
                    </td>
                   
                </tr>
                <tr>
                <td>EDC 機型: 
                    </td>
                    <td>
		                   <cafe:droplisttag css="easyui-combobox easyui-mutil-select"
								id="<%=EdcParameterFormDTO.QUERY_EDC_TYPE %>"
								name="<%=EdcParameterFormDTO.QUERY_EDC_TYPE %>" result="${edcAssetList }"
								blankName="請選擇(複選)" hasBlankValue="true"
								selectedValue=""  style="width: 180px"
								javascript="editable='false' multiple=true">
							</cafe:droplisttag>
                    </td>
                     <td>週邊設備機型: 
                    </td>
                    <td>
                    	<cafe:droplisttag css="easyui-combobox easyui-mutil-select"
								id="<%=EdcParameterFormDTO.QUERY_PERIPHERAL_EQUIPMENT %>"
								name="<%=EdcParameterFormDTO.QUERY_PERIPHERAL_EQUIPMENT %>" result="${categoryRodundAssetList }"
								blankName="請選擇(複選)" hasBlankValue="true"
								selectedValue=""  style="width: 180px"
								javascript="editable='false' multiple=true">
						</cafe:droplisttag>
                    </td>
                    <td>設備支援功能: 
                    </td>
                    <td>
						<cafe:droplisttag css="easyui-combobox easyui-mutil-select"
								id="<%=EdcParameterFormDTO.QUERY_ASSET_SUPPORTED_FUNCTION %>"
								name="<%=EdcParameterFormDTO.QUERY_ASSET_SUPPORTED_FUNCTION %>" result="${supportedFunctionList }"
								blankName="請選擇(複選)" hasBlankValue="true"
								selectedValue="" style="width: 180px"
								javascript="editable='false' multiple=true">
						</cafe:droplisttag>
                    </td>
                </tr>

                <tr>
                <td>設備開啟模式: 
                    </td>
                    <td>
                        	<cafe:checklistTag 
							name="<%=EdcParameterFormDTO.QUERY_ASSET_OPEN_MODE%>" 
							id="<%=EdcParameterFormDTO.QUERY_ASSET_OPEN_MODE%>" 
							type="checkbox"
							result="${openModeList}"
							javascript=""
							>
						</cafe:checklistTag>
                    </td>
                    <td>已開放交易: 
                    </td>
                    <td>
						<cafe:droplisttag css="easyui-combobox easyui-mutil-select"
							id="<%=EdcParameterFormDTO.QUERY_OPEN_TRANSACTION %>"
							name="<%=EdcParameterFormDTO.QUERY_OPEN_TRANSACTION %>" result="${transCategoryList }"
							blankName="請選擇(複選)" hasBlankValue="true"
							selectedValue="" style="width: 180px"
							javascript="editable='false' multiple=true">
						</cafe:droplisttag>
                    </td>
                    <td></td>
                    <td></td>
                    <td><a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" iconcls="icon-search" >查詢</a></td>
                </tr>
            </table>
            <div><span id="msg" class="red"></span></div>
            <div style="text-align: right; padding: 10px">
                <a href="javascript:void(0)" id="btnExport" style="width: 150px" onclick="exportData()">匯出</a>
            </div>
        </form>
        </div>

    </div>
    <script type="text/javascript">
    $(function(){
    	$('#btnExport').attr("onclick","return false;");
		$('#btnExport').attr("style","color:gray;");
		//查詢
	 	$("#btnQuery").click(function(){
			queryData(1,true);
		}); 
	});
 	// 處理交易參數顯示
	var dataColumns = [];
	getDataColumns(dataColumns);
	/*
	* 處理交易參數列
	*/
 	function getDataColumns(dataColumns){
 		dataColumns.push({ field: 'transactionTypeName',width: 200,halign:'center',align:'left', title: '交易類別' });
 		<c:forEach var="columns1" items="${transationParameterItemList }">
 	 	<c:choose>
 	 		<c:when test="${columns1.paramterItemType eq combobox}">
 				dataColumns.push({ field:'${columns1.paramterItemCode }',halign:'center', width:150, align:'left',styler: function(value,row,index){
 					return cellTransStyler(value,row,index,'${columns1.paramterItemCode }','');
 				}, title: '${columns1.paramterItemName }' });
 	 		</c:when>
 	 		<c:when test="${columns1.paramterItemType eq click }">
 				dataColumns.push({ field:'${columns1.paramterItemCode }',halign:'center',align:'center',styler: function(value,row,index){
 					return cellTransStyler(value,row,index,'${columns1.paramterItemCode }','');
 				}, title: '${columns1.paramterItemName }' });
 			</c:when>
 			<c:otherwise>
 				dataColumns.push({ field:'${columns1.paramterItemCode }',halign:'center',align:'center', title: '${columns1.paramterItemName }' });
 			</c:otherwise>
 	 	</c:choose>
 		</c:forEach>
 		return dataColumns;
 	}
	/*
	初始化時，為文本框屬性的列增加lable標籤。
	value：需要顯示的值
	row：該行信息
	index:行號
	field：該列的field
	*/
	function settingField(value,row,index,field) {
		getEditField(row, index);
		if (editColumns && !editColumns.contains(field)){
			return "<lable></lable>";
		} else {
			if (value) {
				return "<lable>"+value+"</lable>";
			} else {
				return "<lable></lable>";
			}
		}
	}
	//编辑行的行标
	var editIndex = undefined;
	var rowIndex;
	function cellTransStyler(value, row, index, id) {
		//定義只在初始化數據時執行
		if (row.transactionType != "" && editIndex == undefined) {
			if (index != rowIndex) {
				rowIndex = index;
				//獲取當前交易類別可編輯列
				var updateColumn = ${editFildsMap };
				editColumns=eval(updateColumn[row.transactionType]);
			}
			//如果當前列不再可編輯範圍內，則將單元格背景色變為灰色
			if (editColumns && !editColumns.contains(id)){
				return 'background-color:gray;color:gray';
			} 
		} 
	}
	/*
	* 查詢方法 
	pageIndex ： 頁碼
	hidden ： 標志位
	*/
	function queryData(pageIndex, hidden) {
		// 清空選中的行
	//	$("#dataGrid").datagrid("clearSelections");
		// 遮罩样式
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		var queryParam = $('#searchForm').form("getData");
		$("#dataGrid").datagrid({
			url : "${contextPath}/edcParameter.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
			queryParams :queryParam,
			pageNumber:pageIndex,
			autoRowHeight:true,
			loadMsg : '',
			onBeforeLoad : function() {
				$.blockUI(blockStyle);
			},
			sortName : '',
			onLoadError : function(data) {
				$.unblockUI();
				$("#msg").text("查詢資料出錯");
			},
			onLoadSuccess : function (data) {
				$(this).datagrid("fixRownumber","dataGrid"); 
				
				$.unblockUI();
				if (hidden) {
					$("#msg").text("");
					// 查無資料時處理匯出顯示
					if (data.total == 0) {
						$("#msg").text(data.msg);
						// 提示查無數據
						$('#btnExport').attr("onclick","return false;");
						$('#btnExport').attr("style","color:gray;");
					} else {
						$('#btnExport').attr("onclick","exportData()");
						$('#btnExport').attr("style","color:blue;");
					}
				}
				hidden = true;
				$("#dataGrid").datagrid("clearSelections");
			},
			view: detailview,
			detailFormatter:function(index,row){
				return '<div style="padding:2px"><div class="load-style-class"></div><table class="ddv"></table></div>';
			},
			onExpandRow: function (index, row) {
				// 查尋交易參數值
				var ddv = $("#dataGrid").datagrid('getRowDetail', index).find('table.ddv');
				var tradingParamsData;
				// loading加載
			//	ddv.prev("div.load-style-class").html($("<div class=\"panel-loading\"></div>").html("loading..."));
				ddv.prev("div.load-style-class").html("loading...").addClass("panel-loading");
				$('#dataGrid').datagrid('fixDetailRowHeight',index); 
				var url = '${contextPath}/edcParameter.do?actionId=<%=IAtomsConstants.ACTION_GET_TRANSACTION_PARAMS%>';
				$.ajax({
        			url: url,
        			data:{'dtid' : row.dtid},
        			type:'post', 
        			cache:false, 
        			dataType:'json', 
        			success:function(json){
        				if (json.success) {
        					if(json.rows){
        						var transResult = [];
        						var tradingParams = json.rows;
        						if(tradingParams){
        							// 處理交易參數值顯示
        							for (var i=0; i<tradingParams.length; i++) {
        								var itemValue;
        								itemValue = tradingParams[i].itemValue;
        								if (itemValue == null || itemValue == "") {
        									itemValue = new Object();
        								} else {
        									itemValue=JSON.parse(itemValue);
        								}
        								itemValue.transactionType=tradingParams[i].transactionType;
        								itemValue.transactionTypeName=tradingParams[i].transactionTypeName;
        								itemValue.MID=tradingParams[i].merchantCode;
        								itemValue.MID2=tradingParams[i].merchantCodeOther;
        								itemValue.TID=tradingParams[i].tid;
        								transResult.push(itemValue);
        							}
        							tradingParamsData = {
        								rows:transResult,
        								total:json.total
        							}
        						}
        					}
        				}
        				if(typeof tradingParamsData == 'undefined'){
    						tradingParamsData = {
    								rows:[],
    								total:0
    							}
    					}
        				// 取消loading加載
        			//	ddv.prev("div.load-style-class").html("");
        				if(ddv.prev("div.load-style-class").hasClass("panel-loading")){
        					ddv.prev("div.load-style-class").html("").removeClass("panel-loading");
        				}
                        var subgrid = ddv.datagrid({
                       	data: tradingParamsData,
                           singleSelect:true,
                           rownumbers:true,
                           loadMsg:'',
                           columns:[dataColumns],
                           onLoadSuccess:function(){
                           	// 處理行自適應
        						$('#dataGrid').datagrid('fixDetailRowHeight',index); 
        						$('#dataGrid').datagrid('fixRowHeight', index);
        						/* setTimeout(function () {
        							$.each($('#dataGrid').datagrid('getRows'), function (i, row) {
        								$('#dataGrid').datagrid('fixRowHeight', i);
        							});
        						}, 0); */
                           }
                       });
        			},
        			error:function(){
        			}
        		});
               
            }
		});	
	}
	/*
	初始化數據時改變單元格背景顏色。
	value: 需要顯示內容
	row：該行信息
	index:行號
	id：該列的field
	*/
	function cellTransStyler(value, row, index, id) {
		//定義只在初始化數據時執行
		if (row.transactionType != "" && editIndex == undefined) {
			/* if (index != rowIndex) {
				rowIndex = index;
				//獲取當前交易類別可編輯列
				var updateColumn = ${editabledFields};
				editColumns=eval(updateColumn[row.transactionType]);
			} */
			getEditField(row, index);
			//如果當前列不再可編輯範圍內，則將單元格背景色變為灰色
			if (editColumns && !editColumns.contains(id)){
				return 'background-color:gray;color:gray';
			} 
		} 
	}
	var rowIndex = undefined;
	function getEditField(row, index){
		if (index != rowIndex) {
			rowIndex = index;
			//獲取當前交易類別可編輯列
			var updateColumn = ${editFildsMap };
			editColumns=eval(updateColumn[row.transactionType]);
		}
	}
    /*
    * 匯出
    */
	function exportData(){
    	var thisForm = document.forms["searchForm"];
		$("#msg").empty();
		var useCaseNo = '<%=IAtomsConstants.UC_NO_PVM_04030%>';
    	var rowLength = getGridRowsCount("dataGrid");
		if(rowLength >= 1) {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			actionClicked(document.forms["searchForm"],
				useCaseNo,
				'',
				'<%=IAtomsConstants.ACTION_EXPORT%>');
			
			ajaxService.getExportFlag(useCaseNo,function(data){
				$.unblockUI();
			});
		}
	}
	</script>
</body>
</html>