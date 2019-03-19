<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ContractManageFormDTO formDTO = null;
	BimContractDTO contractManageDTO = null;
	if(ctx != null) {
		formDTO = (ContractManageFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
	contractManageDTO = formDTO.getContractManageDTO();
		}  else {
	formDTO = new ContractManageFormDTO();
		}
	}
	if (contractManageDTO == null) {
		contractManageDTO = new BimContractDTO();
	}
	String lease = IAtomsConstants.CONTRACT_TYPE_LEASE;
	String buy = IAtomsConstants.CONTRACT_TYPE_BUY;
	//獲取useCaseNo
	String ucNo = formDTO.getUseCaseNo();
	//客户集合
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//厂商集合
	List<Parameter> manuFacturerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo,  IAtomsConstants.PARAM_MANU_FACTURER_LIST);
	//獲取設備類型集合
	String assetCategoryList = (String)SessionHelper.getAttribute(request, ucNo,  ContractManageFormDTO.PARAM_ASSET_CATEGORY_LIST);
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="contractManageDTO" value="<%=contractManageDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="manuFacturerList" value="<%=manuFacturerList%>" scope="page"></c:set>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set> 
<c:set var="lease" value="<%=lease%>" scope="page"></c:set> 
<c:set var="buy" value="<%=buy%>" scope="page"></c:set> 

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<%@include file="/jsp/common/easyui-common.jsp"%>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
</head>
<body>
	<!-- <div region="center" style="width: 98%; height: auto;"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<table id="dataGrid" class="easyui-datagrid" title="合約維護" style="width: 98%; height: auto"
			data-options="
				rownumbers:true,
				pagination:true,
				pageList:[15,30,50,100],
				pageSize:15,
				singleSelect: true,
				iconCls: 'icon-edit',
				pageNumber:0,
				onClickRow:onClickRow,
				nowrap:false,
				toolbar:'#tb'">
			<thead>
				<tr>
					<th data-options="field:'customerName',width:260,halign:'center',sortable:true">客戶</th>
					<th data-options="field:'companyId',width:260,halign:'center',hidden:true,sortable:true">客戶ID</th>
					<th data-options="field:'companyNames',width:260,halign:'center',sortable:true">維護廠商</th>
					<th data-options="field:'companyIds',halign:'center',width:100,hidden:true">廠商ID</th>
					<th data-options="field:'unityNumber',halign:'center',width:100,sortable:true">統一編號</th>
					<th data-options="field:'contractCode',width:200,halign:'center',align:'left',sortable:true">合約編號</th>
					<th data-options="field:'contractTypeName',width:130,halign:'center',sortable:true">合約類型</th>
					<th data-options="field:'contractTypeId',halign:'center',width:100,hidden:true">合約類型ID</th>
					<th data-options="field:'contractDate',halign:'center',width:200,align:'center',sortable:true">合約起迄</th>
					<th data-options="field:'cancelDate',halign:'center',align:'center',width:120,sortable:true,formatter:formaterTimeStampToyyyyMMDD">解約日</th>
					<th data-options="field:'contractStatusName',halign:'center',width:100,sortable:true">合約狀態</th>
					<th data-options="field:'contractStatus',halign:'center',width:100,hidden:true,sortable:true">合約狀態ID</th>
					<th data-options="field:'contractPrice',halign:'center',width:100,align:'right',sortable:true" formatter="formatCapacity">合約金額</th>
					<th data-options="field:'payModeName',halign:'center',width:100,sortable:true">付款方式</th>
					<th data-options="field:'payRequire',halign:'center',width:250,sortable:true">付款條件</th>
					<th data-options="field:'factoryWarranty',halign:'center',width:150,align:'right',sortable:true">原廠保固期限(月)</th>
					<th data-options="field:'customerWarranty',halign:'center',width:150,align:'right',sortable:true">客戶保固期限(月)</th>
					<th data-options="field:'workHour1',halign:'center',width:120,align:'center',sortable:true">約定上班時間1</th>
					<th data-options="field:'workHour2',halign:'center',width:120,align:'center',sortable:true">約定上班時間2</th>
					<th data-options="field:'window1',halign:'center',width:230,sortable:true">窗口1</th>
					<th data-options="field:'window1Connection',width:150,halign:'center',sortable:true">窗口1聯繫方式</th>
					<th data-options="field:'window2',halign:'center',width:230,sortable:true">窗口2</th>
					<th data-options="field:'window2Connection',width:150,halign:'center',sortable:true">窗口2聯繫方式</th>
					<th data-options="field:'comment',halign:'center',width:500,sortable:true">說明</th>
				</tr>
			</thead>
		</table>
		<div id="tb" style="padding: 2px 5px;">
			客戶: 
			<cafe:droplisttag css="easyui-combobox"
					id="<%=ContractManageFormDTO.QUERY_CUSTOMSER_ID %>"
					name="<%=ContractManageFormDTO.QUERY_CUSTOMSER_ID %>" result="${customerList }"
					blankName="請選擇" hasBlankValue="true"
					style="width: 200px"
					javascript="editable=false" 
					>
			</cafe:droplisttag>
			維護廠商:
			<cafe:droplisttag css="easyui-combobox"
					id="<%=ContractManageFormDTO.QUERY_MANU_FACTURER %>"
					name="<%=ContractManageFormDTO.QUERY_MANU_FACTURER %>" result="${manuFacturerList }"
					blankName="請選擇" hasBlankValue="true"
					style="width: 200px"
					javascript="editable=false" 
					>
			</cafe:droplisttag>&nbsp;
			<a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" iconcls="icon-search" >查詢</a>&nbsp;
			<a href="javascript:void(0)" id="btnAdd" class="easyui-linkbutton" onclick = "save();" data-options="iconCls:'icon-add'">新增</a>&nbsp;
			<a href="javascript:void(0)" id="btnEdit" class="easyui-linkbutton" onclick = "update();" iconcls="icon-edit" disabled="true">修改</a>&nbsp;
			<a href="javascript:void(0)" id="btnDelete" class="easyui-linkbutton" onclick = "btnDelete();" data-options="iconCls:'icon-remove'" disabled="true">刪除</a>
			<div style="color: red">
				<span id="msg" class="red"></span>
			</div>
		</div>
		<div id="dlg" modal ="true"></div>
	</div>
	<script type="text/javascript">
		/**
		* 設備類別列表
		*/
		var assetCategoryList = initSelect(<%=assetCategoryList%>);
		$(function(){
			// 查詢
			$("#btnQuery").click(function(){
				queryData(1,true);
			});
		});
		/**
		* 新增
		*/
		function save() {
			addAndEditContract('新增合約維護','<%=IAtomsConstants.ACTION_INIT_ADD%>', '');
		}
		/**
		* 修改
		*/
		function update() {
			var row = $('#dataGrid').datagrid('getSelected');
			if (row) {
				var contractId = row.contractId;
				addAndEditContract('修改合約維護','<%=IAtomsConstants.ACTION_INIT_EDIT%>', contractId);
			} else {
				$("#msg").empty();
				$.messager.alert('提示','請勾選要操作的記錄!','warning');
				return false;
			}
		}
		/**
		* 表格單擊事件
		*/
		function onClickRow() {
			$("#msg").empty();
			$("#btnDelete").linkbutton('enable');
			$("#editContract").linkbutton('enable');
		}
		
		/**
		* 获得查询数据
		*/
		function getQueryParam(){
			var querParam = {
				actionId : "<%=IAtomsConstants.ACTION_QUERY%>",
				queryCustomerId : $("#<%=ContractManageFormDTO.QUERY_CUSTOMSER_ID%>").combobox('getValue'),
				queryManuFacturer : $("#<%=ContractManageFormDTO.QUERY_MANU_FACTURER%>").combobox('getValue')
			};
			return querParam;
		}
		/**
		* 查询数据
		*/
		function queryData(pageIndex, hidden){
			$("#msg").empty();
			var row;
			var queryParam = getQueryParam();
			var options = {
				url : "${contextPath}/contract.do",
				queryParams :queryParam,
				pageNumber:pageIndex,
				autoRowHeight:true,
				onLoadError : function(data) {
					$.messager.alert('提示','查詢合約維護資料出錯','error');
				},
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","dataGrid"); 
					
					$("#btnDelete").linkbutton('disable');
					$("#editContract").linkbutton('disable');
					if (hidden) {
						$("#msg").empty();	
						$("#msg").text(data.msg);
					}
					hidden = true;
				}
			};
			// 清空點選排序
			if(hidden){
				options.sortName = null;
			}
			openDlgGridQuery("dataGrid", options);
		}
		var viewDlg;
		/**
		* 新增,修改合約維護以及保存
		*/
		function addAndEditContract (title, actionId, contractId) {
			$("#msg").empty();
			//維護商ID
			var companyIds = undefined;
			//唯一編號
			var unityNumber = undefined;
			//合約類型ID
			var contractTypeId = undefined;
			//如果合約ID為空，表示進行添加操作，清除所選擇的行
			if (contractId == null || contractId == '') {
			} else {
				var row = $('#dataGrid').datagrid('getSelected');
				companyIds = row.companyIds;
				unityNumber = row.unityNumber;
				contractTypeId = row.contractTypeId;
			}
			viewDlg = $('#dlg').dialog({
				title: title,    
				width: 860,
				height:500,
				top:10,
				cache: false,
				loadingMessage:'loading...',
				queryParams : {
					actionId : actionId,
					contractId : contractId
				},
				onLoad :function(){
					textBoxSetting("dlg");
					dateboxSetting("dlg");
					if ($('#<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>').length > 0) {
						if (unityNumber != undefined) {
							$('#<%=BimContractDTO.ATTRIBUTE.UNITY_NUMBER.getValue()%>').textbox('setValue',unityNumber);
						}
					}
				},
				href: "${contextPath}/contract.do",
				buttons : [{
					text:'儲存',
					iconCls:'icon-ok',
					width: '88',
					handler: function(){
						$("#msg1").text("");
						var form = viewDlg.find("#fm");
						var controls = ['companyId','contractCode'];
						if(!validateForm(controls)) {
							return false;
						}
						if(!validateFormInContract()){
							return false;
						}
						if(form.form("validate")){
							if ($("#<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue') == "" || $("#<%=BimContractDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue') == null) {
								parent.$.messager.progress('close');
								$("#msg1").text("請輸入客戶");
								return false;
							}
							// 取出新增設備列表的集合
							$("#assetTable").datagrid('acceptChanges');
							// 拿到集合的所有的行
							var assetRows = $("#assetTable").datagrid('getRows');
							// 將所有的行數據轉化為JSON格式
							var assetListRow = JSON.stringify(assetRows);
							var isExist = true;
							var index = -1;
							javascript:dwr.engine.setAsync(false);
							ajaxService.getAssetTypeList('', function(result){
								for (var i = 0; i<assetRows.length; i++ ) {
									isExist = checkExistValue(result, assetRows[i].assetTypeId);
									if (!isExist) {
										index = i;
										break;
									}
								}
							});
							if (!isExist) {
								onDblClickRow(index);
								var currentEditor = $('#assetTable').datagrid('getEditor', {  
									index : index,  
									field : 'assetTypeId'
								});
								$(currentEditor.target).combobox('setValue', "");
								$("#assetMsg").text("該設備名稱已被刪除，請重新輸入");
								return false;
							}
							javascript:dwr.engine.setAsync(true);
							form.find("#assetListRow").val(assetListRow);
							// 取form表單的所有的值
							var saveParam = form.form("getData");
							//核檢合約日期是否全部輸入
							/* var contractStart = saveParam.startDate;
							var contractEnd = saveParam.endDate;
							if (contractStart != "" || contractEnd != "") {
								if (contractStart == "") {
									$("#msg1").text("請輸入合約起");
									return false;
								}
								if (contractEnd == "") {
									$("#msg1").text("請輸入合約迄");
									return false;
								}
							}*/
							//驗證上班時間是否全部輸入
							var workStart = saveParam.workHourStart1;
							var workEnd = saveParam.workHourEnd1;
							if (workStart != "" || workEnd != "") {
								if (workStart == "") {
									handleScrollTop('dlg');
									$("#msg1").text("請輸入上班時間1起");
									return false;
								}
								if (workEnd == "") {
									handleScrollTop('dlg');
									$("#msg1").text("請輸入上班時間1迄");
									return false;
								}
							}
							workStart = saveParam.workHourStart2;
							workEnd = saveParam.workHourEnd2;
							if (workStart != "" || workEnd != "") {
								if (workStart == "") {
									handleScrollTop('dlg'); 
									$("#msg1").text("請輸入上班時間2起");
									return false;
								}
								if (workEnd == "") {
									handleScrollTop('dlg'); 
									$("#msg1").text("請輸入上班時間2迄");
									return false;
								}
							}
							//判斷如果設備列表為空，則進行判斷選擇的合約實行ID是否為1或2，如果是，則提示需要選擇一筆合約設備
							var contractTypeId = new Array();
							var temp = "";
							contractTypeId = (saveParam.contractTypeId).split(",");
							for (var i = 0; i<contractTypeId.length; i++) {
								if (contractTypeId[i] == '${lease}' || contractTypeId[i] == '${buy}'){
									<%-- temp = saveParam['<%=BimContractDTO.ATTRIBUTE.FACTORY_WARRANTY.getValue()%>'];
									if (temp == "" || temp == null) {
										parent.$.messager.progress('close');
										$("#msg1").text("請輸入原廠保固期限");
										return false;
									}
									temp = saveParam['<%=BimContractDTO.ATTRIBUTE.CUSTOMER_WARRANTY.getValue()%>'];
									if (temp == "" || temp == null) {
										parent.$.messager.progress('close');
										$("#msg1").text("請輸入客戶保固期限");
										return false;
									} --%>
									if (assetRows.length == 0) {
										parent.$.messager.progress('close');
										handleScrollTop('dlg'); 
										$("#msg1").text("請至少輸入一筆設備資料");
										return false;
									}
								}
							}
							// 上傳文件和保存
							if (!(typeof(file_upload)=="undefined") && file_upload._storedIds!=""){
								file_upload.uploadStoredFiles();
							} else { // 保存
								saveDate();
							}
						} 
					}
				},{
					text:'取消',
					iconCls:'icon-cancel',
					width: '88',
					handler: function(){
						confirmCancel(function(){
							viewDlg.dialog('close');
						});
					}
				}]
			});
		}
		/**
		* 將所選則的臨時文件保存至臨時路徑
		*/
		function uploadAllComplete(succeeded, failed){
			if(failed) {
				saveDate();
			}else{
				$.messager.alert('提示', '保存失敗,請聯繫管理員.', 'error'); 
			}
		}
		/**
		* 將文件保存在臨時路徑后，保存合約基本信息。
		*/
		function saveDate() {
			// 取出新增設備列表的集合
			var form = viewDlg.find("#fm");
			$("#assetTable").datagrid('acceptChanges');
				// 拿到集合的所有的行
				var assetRows = $("#assetTable").datagrid('getRows');
				// 將所有的行數據轉化為JSON格式
				var assetListRow = JSON.stringify(assetRows);
				form.find("#assetListRow").val(assetListRow);
				// 取form表單的所有的值
				var saveParam = form.form("getData");
				 commonSaveLoading('dlg');
			$.ajax({
				url: "${contextPath}/contract.do?actionId=save",
				data:saveParam, 
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(json){
					commonCancelSaveLoading('dlg');
					if (json.success) {
						viewDlg.dialog('close');
						var pageIndex = getGridCurrentPagerIndex("dataGrid");
						if (pageIndex < 1) {
							pageIndex = 1;
						}
						queryData(pageIndex,false);
						$("#msg").text(json.msg);
					}else{
						handleScrollTop('dlg'); 
						$("#msg1").text(json.msg);
					}
					
				},
				error:function(){
					commonCancelSaveLoading('dlg');
					$("#msg1").text("合約保存失敗");
				}
			});
		}
		/**
		* 刪除合約維護
		*/
		function btnDelete(){
			$("#msg").text('');
			var row = $('#dataGrid').datagrid('getSelected');
			if (row == null ){
				$.messager.alert('提示','請勾選要操作的記錄!','warning');
				return false;
			}
			var url = "${contextPath}/contract.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>";
			var params = {contractId: row.contractId};
			var successFunction = function(json){
				if (json.success) {
					$("#dataGrid").datagrid("clearSelections");
					//計算要顯示的頁碼
					var pageIndex = calDeletePagerIndex("dataGrid");
					queryData(pageIndex,false);
					$("#msg").text(json.msg);	
				}else{
					$("#msg").text(json.msg);
				}
			};
			var errorFunction = function(){
				$("#msg").text("合約刪除失敗");
			};
			commonDelete(params,url,successFunction,errorFunction);
		}
		/**
		* 保存數據時，在上傳文件前驗證SESSION是否過期
		*/
		function showMessageContract(id, fileName, response, maybeXhrOrXdr){
			if (maybeXhrOrXdr) {
				if (!sessionTimeOut(maybeXhrOrXdr)) {
					return false;
				}
			}
		}
	</script>
</body>
</html>
