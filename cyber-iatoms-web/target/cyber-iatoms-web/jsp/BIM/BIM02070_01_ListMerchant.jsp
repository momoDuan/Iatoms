<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	MerchantFormDTO formDTO = null;
	String ucNo = null;
	if (ctx != null) {
		formDTO = (MerchantFormDTO) ctx.getResponseResult();
	} 
	if (formDTO == null) {
		formDTO = new MerchantFormDTO();
	}else{
		ucNo = formDTO.getUseCaseNo();
	}
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request,ucNo,IAtomsConstants.PARAM_CUSTOMER_LIST);
	//获取上传最大限制
	String uploadFileSize = formDTO.getUploadFileSize();
%>
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
<head>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript">
	$(function(){
		$("#btnEdit").linkbutton('disable');
		$("#btnDelete").linkbutton('disable');
		// 匯入格式下載設置樣式
		$('#btnDownload').attr("style","color:blue;");
		//查詢
		 $("#btnQuery").click(function(){
			queryData(1,true);
		}); 
		//新增
		$("#btnAdd").click(function(){
			viewEditData1('新增客戶特店維護', '<%=IAtomsConstants.ACTION_INIT_ADD%>', '');
		});
		// 匯入格式下載
		$("#btnDownload").click(function(){
			downLoad("");
		});
	});
	//修改客戶特店維護
	function update (){
		var row = $("#dataGrid").datagrid('getSelected');
		var merchantId = row.merchantId;
		if (merchantId != "") {
			viewEditData1('修改客戶特店維護', '<%=IAtomsConstants.ACTION_INIT_EDIT%>', merchantId);
		}
	}
	//跳轉到特店維護詳情頁
	function viewEditData1(title,actionId, merchantId) {
		$("#message").text("");
		var viewDlg = $('#dialogView').dialog({
			title: title, 
			width: 720, 
			height: 380, 
			top:10,
			closed: false, 
			cache: false, 
			queryParams : {
				actionId : actionId,
				merchantId : merchantId,
			}, 
			href: "${contextPath}/merchant.do",
			
			onLoad : function() {
				textBoxSetting("editMerchantDiv");
			},
			buttons : [{
				text:'儲存',
				width:88,
				iconCls:'icon-ok',
				handler: function(){
					var f = viewDlg.find("form");
					var controls = ['companyId','merchantCode','name'];
					if(validateForm(controls) && f.form("validate")){
						commonSaveLoading('dialogView');
						//取值
						var saveParam = f.form("getData");
						//客戶名稱 
						if (saveParam.companyId != '') {
							saveParam.companyId = f.find("#<%=MerchantDTO.ATTRIBUTE.COMPANY_ID.getValue()%>").combobox('getValue');
						}
						//save
						$.ajax({
							url: "${contextPath}/merchant.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>",
							data:saveParam,
							type:'post', 
							cache:false, 
							dataType:'json', 
							success:function(result){
								if (result.success) {
									commonCancelSaveLoading('dialogView');
									viewDlg.dialog('close');
									//拿到特店的id
									var resultMap = result.row;
									var merchantId = resultMap.merchantId;
									if(actionId=="initAdd"){
										if(merchantId != ""){
											var queryParams ={actionId : '<%=IAtomsConstants.ACTION_INIT_ADD%>', merchantId : merchantId};
											//$("#message").text("客戶特店資料新增成功");
											viewEditMerchantHeader('dialogView','新增特店表頭維護',queryParams, successCallBack, null, actionId, "${contextPath}", false);
										}
									}
									if(actionId=="initEdit"){
										var pageIndex = getGridCurrentPagerIndex("dataGrid");
										queryData(pageIndex, false);
										$("#message").text(result.msg);
									}
								}else{
									commonCancelSaveLoading('dialogView');
									$("#msg").text(result.msg);
								}
							},
							error:function(){
								var message;
								if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
									message = "客戶特店資料新增失敗";
								} else {
									message = "客戶特店資料新增失敗";
								}
								$.messager.alert('提示', message, 'error');							
							}
						});
					}
				}
			},{
				text:'取消',
				width:88,
				iconCls:'icon-cancel',
				handler: function(){
					$.messager.confirm('確認對話框','確認取消?', function(isCancle) {
						if (isCancle) {
							viewDlg.dialog('close');
						}
					});
				}
			}]
		});
	}
	//回調函數
	function successCallBack(data){
		$('#dialogView').dialog('close');
		$("#message").text("客戶特店資料新增成功");
		//計算要顯示的頁碼
		var pageIndex = calDeletePagerIndex("dataGrid");
		queryData(pageIndex, false);
	}
	//獲得操作行的merchantId
	function getSelectedMerchantId(actionId) {
		//要刪除的特店id
		var merchantId = '';
		var param;
		var row = $("#dataGrid").datagrid('getSelected');
		if (row != null) {
			param = {
				actionId : actionId,
				merchantId : row.merchantId,
			}
		} else {
			return false;
		}
		return param;
	}

	//删除数据
	function deleteData(){
		var row = $("#dataGrid").datagrid('getSelected');
		//特店id
		var merchantId = row.merchantId;
		if (merchantId != "") {
			//ajax查看特店下是否有表頭，有表頭，不可刪除
			ajaxService.getMerchantList(merchantId, function(data){
				if(data.flag){
					$("#message").text(data.msg);
					handleScrollTop('merchantDiv'); 
				} else {
					$("#message").text("");
					var params = getSelectedMerchantId("<%=IAtomsConstants.ACTION_DELETE%>");
					var url = '${contextPath}/merchant.do';
					var successFunction = function(data) {
						if (data.success) {
							var pageIndex = calDeletePagerIndex("dataGrid");
							queryData(pageIndex, false);
						} 
						$("#message").text(data.msg);
					};
					var errorFunction = function(){
						$("#message").text("刪除失敗");
					};
					//調用遮蓋的刪除方法
					commonDelete(params,url,successFunction,errorFunction);
				}
			})
		}
	}

	//查詢數據 pageIndex 頁碼 isCleanMsg：是否自己點擊查詢按鈕的標誌
	function queryData(pageIndex, isCleanMsg) {
	var queryParam = $("#searchForm").form("getData");
	//Task #3583
	queryParam.queryCompanyId = $("#queryCompanyId").combobox('getValue');
		var options = {
				url : "${contextPath}/merchant.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				method:'post',
				pageNumber:pageIndex,
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","dataGrid"); 
					
					if (isCleanMsg) {
						$("#message").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#message").text(data.msg);
						}
					}
					isCleanMsg = true;
				},
				onLoadError : function() {
					$("#message").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序
		if(isCleanMsg){
			options.sortName = null;
		}
		//調用查詢的遮罩
		openDlgGridQuery("dataGrid", options);
	}
	//匯入返回處理 fileName：名稱  response：響應
	function showMessage(id, fileName, response, maybeXhrOrXdr) {
		$("#message").text("");
		//保存數據時，在上傳文件前驗證SESSION是否過期
		if (maybeXhrOrXdr) {
			if (!sessionTimeOut(maybeXhrOrXdr)) {
				return false;
			}
		}
		if (response.success) {
			var pageIndex = getGridCurrentPagerIndex("dataGrid");
			queryData(pageIndex, false);
			$("#message").text(response.msg);
		} else {
			if(response.msg && response.errorFileName == null){
				$("#message").text(response.msg);
			} else {
				if (response.errorFileName != "" || response.errorFileName){
					$.messager.confirm('確認對話框','匯入失敗，是否下載匯入錯誤檔？', function(confirm) {
						if (confirm) {
							createSubmitForm("${contextPath}/merchant.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE%>&errorFileName=" + response.errorFileName + "&tempFilePath= " + response.tempFilePath,"post");
						}
					});
				}
			}
		}
	}
	
	//匯入格式下載
	function downLoad(fileName){
		$("#message").text("");
		// 匯入格式下載
		createSubmitForm("${contextPath}/merchant.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD%>","post");
	}
	//點擊行事件
	function onClickRow(){
		//編輯按鈕可用
		$('#btnEdit').linkbutton('enable');
		//刪除按鈕可用
		$('#btnDelete').linkbutton('enable');
	}
</script>
</head>
<body>
<!-- 	<div region="center" style="width: auto; height: auto; padding: 1px;  overflow-y: hidden"> -->
<div id="merchantDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
		<table id="dataGrid" class="easyui-datagrid" title="客戶特店維護" style="width: 98%; height: auto"
			data-options="
				rownumbers:true,
				pagination:true,
				iconCls: 'icon-edit',
				singleSelect: true,
				selectOnCheck: true,
				method: 'get',
				pageNumber:0,
				pageList:[15,30,50,100],
				pageSize:15,
				onClickRow :onClickRow,
				sortOrder:'asc',
				nowrap:false,
				toolbar:'#tb'
				">
			<thead>
				<tr>
					<th data-options="field:'merchantId',hidden:true"></th>
					<th data-options="field:'shortName',width:270,sortable:true,halign:'center'">客戶</th>
					<th data-options="field:'merchantCode',width:180,sortable:true,halign:'center'">特店代號</th>
					<th data-options="field:'name',width:300,sortable:true,halign:'center'">特店名稱</th>
					<th data-options="field:'unityNumber',width:180,sortable:true,halign:'center'">統一編號</th> 
					
					<th data-options="field:'remark',width:500,sortable:true,halign:'center'">備註</th>
				</tr>
			</thead>
		</table>
		<div id="tb" style="padding: 2px 5px;">
		<form id="searchForm">
			客戶:
			<cafe:droplisttag id="<%=MerchantFormDTO.QUERY_COMPANY_ID %>"
				css="easyui-combobox"
				style="width: 150px"
				name="<%=MerchantFormDTO.QUERY_COMPANY_ID %>" 
				result="${customerList}"
				selectedValue="${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?logonUser.companyId:'' }" 
				hasBlankValue="true" 
				javascript="editable='false' ${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?'disabled=true':''}"
				blankName="請選擇">
			</cafe:droplisttag>
			特店代號: <input maxlength="20" class="easyui-textbox" type="text" style="width: 100px" name="<%=MerchantFormDTO.QUERY_MERCHANT_CODE%>" id="<%=MerchantFormDTO.QUERY_MERCHANT_CODE%>" data-options="validType:['maxLength[20]']"></input>
			<%-- 分期特店代號: <input maxlength="20" class="easyui-textbox" type="text" style="width: 100px" name="<%=MerchantFormDTO.QUERY_STAGES_MERCHANT_CODE%>" id="<%=MerchantFormDTO.QUERY_STAGES_MERCHANT_CODE%>"data-options="validType:['maxLength[20]']"></input> --%> 
			特店名稱: <input maxlength="50" class="easyui-textbox" type="text" style="width: 100px" name="<%=MerchantFormDTO.QUERY_NAME%>" id="<%=MerchantFormDTO.QUERY_NAME%>"data-options="validType:['maxLength[50]']"></input> 
			<a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查詢</a> 
			<a href="javascript:void(0)" id="btnAdd" class="easyui-linkbutton"  data-options="iconCls:'icon-add'">新增</a> 
			<a href="javascript:void(0)" id="btnEdit"  onclick = "update()" class="easyui-linkbutton"  data-options="iconCls:'icon-edit'">修改</a> 
			<a href="javascript:void(0)" id="btnDelete" onclick = "deleteData()" class="easyui-linkbutton"  data-options="iconCls:'icon-remove'">刪除</a> 
			<a href="#" id="btnDownload" style="width: 150px">匯入格式下載</a>&nbsp;
			<cafe:fileuploadTag 
				id="uploadFiled"
				name="uploadFiled"
				uploadUrl="${contextPath}/merchant.do" 
				uploadParams="{actionId:'upload'}"
				allowedExtensions="'xls'" 
				sizeLimit = "${uploadFileSize }"
				showFileList="false"
				showName="匯入"
				whetherImport="true"
				messageAlert="false"
				whetherDelete="false"
				multiple="false"
				whetherDownLoad="false"
				showUnderline = "true"
				javaScript="onComplete:showMessage,onError:function(id, name, reason, maybeXhrOrXdr){
							$('#message').text(reason);
				}">
			</cafe:fileuploadTag>
			<div><span id="message" class="red"></span></div>
		</form>
		</div>
		<!-- 新增修改彈出框 -->
		<div id="dialogView" modal ="true"></div>
</body>
</html>
