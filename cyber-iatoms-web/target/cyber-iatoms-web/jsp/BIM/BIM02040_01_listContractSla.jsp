<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ContractSlaFormDTO contractSlaFormDTO = null;
	String ucNo = null;
	if (ctx != null) {
		// 得到FormDTO
		contractSlaFormDTO = (ContractSlaFormDTO) ctx.getRequestParameter();
		if (contractSlaFormDTO != null) {
			// 获得UseCaseNo
			ucNo = contractSlaFormDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_BIM_02040;
			contractSlaFormDTO = new ContractSlaFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_BIM_02040;
		contractSlaFormDTO = new ContractSlaFormDTO();
	}
	// 案件類別列表下拉框
	List<Parameter> ticketTypeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
	// 客戶列表下拉框
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	// 地區列表下拉框
	List<Parameter> locationList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.REGION.getCode());
	// 案件類型列表下拉框
	List<Parameter> ticketModeList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, ContractSlaFormDTO.PARAM_TICKET_MODE_LIST);
	//获取上传最大限制
	String uploadFileSize = contractSlaFormDTO.getUploadFileSize();
%>
 
<c:set var="ticketTypeList" value="<%=ticketTypeList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList%>" scope="page"></c:set>
<c:set var="ticketModeList" value="<%=ticketModeList%>" scope="page"></c:set>
<c:set var="uploadFileSize" value="<%=uploadFileSize%>" scope="page"></c:set>
<html>
<head>
<meta charset="UTF-8">
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
<script type="text/javascript">
/**
 * !!!入口函數
 */
	$(function(){
		/* $("#btnEdit").linkbutton('disable');
		$("#btnDelete").linkbutton('disable'); 
		$('#btnDownload').attr("style","color:blue;"); */
		//查詢
		$("#btnQuery").click(function(){
			query(1,true); 
		});
		// 修改
		$("#btnEdit").linkbutton({
			onClick : function (){
				var actionId = "<%=IAtomsConstants.ACTION_INIT_EDIT%>" ;
		 		var param = getSelectedParam(actionId);
		 		if (param) {
					viewEditData('修改合約SLA設定', param);
				}
			}
		});
		// 新增
		$("#btnAdd").click(function(){
			var param = {
				actionId : "<%=IAtomsConstants.ACTION_INIT_ADD%>",
			};
			viewEditData('新增合約SLA設定', param); 
		});
		// 刪除
		$("#btnDelete").linkbutton({
			onClick : function (){
				deleteData();
			}
		});
		//點擊複製
		$("#btnCopy").click(function(){
			viewCopyData('複製合約SLA設定', '<%=IAtomsConstants.ACTION_INIT_COPY%>');
		});
		// 匯入格式下載
		$("#btnDownload").click(function(){
			createSubmitForm("${contextPath}/contractSla.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD %>","post");
		});
		
		/*
		* 查询时合约编号根据所选客户联动
		*/
		$('#<%=ContractSlaFormDTO.QUERY_CUSTOMSER_ID%>').combobox({
			onChange: function(){
				$('#<%=ContractSlaFormDTO.QUERY_CONTRACT_ID%>').combobox('setValue','');
				var customerId = $('#<%=ContractSlaFormDTO.QUERY_CUSTOMSER_ID%>').combobox('getValue');
				if(!isEmpty(customerId)){
					ajaxService.getContractCodeList(customerId,function(data){
						// 设置默认值
						var data = initSelect(data);
						// 给合约编号下拉框放值
						$('#<%=ContractSlaFormDTO.QUERY_CONTRACT_ID%>').combobox('loadData',data);
					}); 
				} else {
					var data = initSelect();
					$('#<%=ContractSlaFormDTO.QUERY_CONTRACT_ID%>').combobox('loadData',data);
				} 	
			}
		});
	});
	
	/*
	*查詢方法
	*/ 
	function query(pageIndex, hidden) {
		var queryParam = $("#searchForm").form("getData");
		var options = {
				url : "${contextPath}/contractSla.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:pageIndex,
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","dataGrid"); 
					
					if (hidden) {
						$("#msg").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#msg").text(data.msg);
						}
					}
					hidden = true;
				},
				onLoadError : function() {
					$("#msg").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("dataGrid", options);
	}

	/*
	* 编辑、删除时拿到主键值
	*/
	function getSelectedParam(actionId) {
		var row = $("#dataGrid").datagrid('getSelected');
		var param ;
		if (row != null) {
			param = {
				actionId : actionId,	
				customerId : row.customerId,
				slaId : row.slaId,
			};
			return param;
		} else {
			$("#msg").text('請勾選要操作的記錄!');
			return null;
		}
	}

	/*
	* 编辑和新增
	*/
	function viewEditData(title, param) { 
		$("#msg").empty();
		var actionId = param.actionId;
		var viewDlg = $("#saveDlg").dialog({
			title:title,
			width: 700,
			height:500,
			top:10,
			closed: false,
			cache: false,
			queryParams : param,
			href: "${contextPath}/contractSla.do",
			modal: true,
			onLoad : function(){
				textBoxSetting("saveDlg");
				// 禁用當天件建案時間
				if(typeof setTimespinnerDisabled != 'undefined' && setTimespinnerDisabled instanceof Function){
					setTimespinnerDisabled();
				}
		    }, 
			buttons : [{
				text:'儲存',
				width:'88',
				iconCls:'icon-ok',
				handler: function(){
					$("#saveDlgMsg").text("");
					var url = "${contextPath}/contractSla.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>";
					var saveParam = $('#saveForm').form("getData");
					saveParam.customerId = $("#<%=ContractSlaDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>").combobox('getValue');
					var controls = ['customerId','contractId','ticketType','location','ticketMode','isWorkDay','isThatDay','thatDayTime',
									'responseHour', 'responseWarnning', 'arriveHour', 'arriveWarnning', 'completeHour', 'completeWarnning', 'comment'];
					if(validateForm(controls) && $('#saveForm').form("validate")){
						// 調保存遮罩
						commonSaveLoading('saveDlg');
						$.ajax({
							url:url,
							data:saveParam,
							type:'post', 
							cache:false, 
							dataType:'json', 
							success:function(result){
								// 去除保存遮罩
								commonCancelSaveLoading('saveDlg');
								if(result.success){
									$('#saveDlg').dialog('close');
									var pageIndex = getGridCurrentPagerIndex("dataGrid");
									query(pageIndex, false);
									$("#msg").text(result.msg);
								} else {
									$("#saveDlgMsg").text(result.msg);
								}
							},
							error:function(){
								// 去除保存遮罩
								commonCancelSaveLoading('saveDlg');
								var msg;
								if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
									msg = "新增失敗";
								} else {
									msg = "修改失敗";
								}
								$("#saveDlgMsg").text(msg);
							}
						});
					}
				}
			},{
				text:'取消',
				width:'88',
				iconCls:'icon-cancel',
				handler: function(){
					$.messager.confirm('確認對話框','確認取消?', function(confirm) {
						if (confirm) {
							viewDlg.dialog('close');
						}
					});
				}
			}]
		}); 
	}

	/**
	 * 合約SLA複製
	 */
	function viewCopyData(title, actionId) { 
		$("#msg").empty();
		var viewDlg = $("#copyDlg").dialog({
			title:title,
			width: 700,
			height:300,
			top:10,
			closed: false,
			cache: false,
			queryParams : {
			actionId : actionId,
			},
			href: "${contextPath}/contractSla.do",
			modal: true,
			buttons : [{
				text:'儲存',
				iconCls:'icon-ok',
				width:'88',
				handler: function(){
					$("#copyDlgMsg").text("");
					var url = "${contextPath}/contractSla.do?actionId=<%=IAtomsConstants.ACTION_COPY%>";
					if($('#copyForm').form("validate")){
						// 調保存遮罩
						commonSaveLoading('copyDlg');
						$.ajax({
							url:url,
							data:$('#copyForm').form("getData"),
							type:'post', 
							cache:false, 
							dataType:'json', 
							success:function(result){
								// 去除保存遮罩
								commonCancelSaveLoading('copyDlg');
								if(result.success){
									$('#copyDlg').dialog('close');
									var pageIndex = getGridCurrentPagerIndex("dataGrid");
									query(pageIndex, false);
									$("#msg").text(result.msg);
								} else {
									$("#copyDlgMsg").text(result.msg);
								}
							},
							error:function(){
								// 去除保存遮罩
								commonCancelSaveLoading('copyDlg');
								var msg = "复制失败";
								$("#copyDlgMsg").text(msg);
							}
						});
					}
				}
			},{
				text:'取消',
				width:'88',
				iconCls:'icon-cancel',
				handler: function(){
					$.messager.confirm('確認對話框','確認取消?', function(confirm) {
						if (confirm) {
							viewDlg.dialog('close');
						}
					});
				}
			}]
		}); 
		
	}
	
	/*
	* 刪除方法
	*/
	function deleteData(){
		$("#msg").text("");
		var params = getSelectedParam("<%=IAtomsConstants.ACTION_DELETE%>");
		var url = '${contextPath}/contractSla.do';
		var successFunction = function(data) {
			if (data.success) {
				var pageIndex = calDeletePagerIndex("dataGrid");
				query(pageIndex, false);
			} 
			$("#msg").text(data.msg);
		};
		var errorFunction = function(){
			$("#msg").text("刪除失敗");
		};
		commonDelete(params,url,successFunction,errorFunction);
	}

	/**
	* 匯入返回處理
	*/
	function showMessage(id, fileName, response, maybeXhrOrXdr) {
		if (maybeXhrOrXdr) {
			if (!sessionTimeOut(maybeXhrOrXdr)) {
				return false;
			}
		}
		if (response.success) {
			var pageIndex = getGridCurrentPagerIndex("dataGrid");
			query(pageIndex, false);
			$("#msg").text(response.msg);
		} else {
			if (response.msg && response.errorFileName == null){
				$("#msg").text(response.msg);
			} else {
				$.messager.confirm('確認對話框',' 匯入失敗，是否下載匯入錯誤檔？', function(confirm) {
					if (confirm) {
						var fileName = response.errorFileName;
						var tempFilePath = response.tempFilePath;
						createSubmitForm("${contextPath}/contractSla.do","actionId=<%=IAtomsConstants.ACTION_DOWNLOAD_ERROR_FILE %>&errorFileName=" + fileName + "&tempFilePath= " + tempFilePath,"post");
					}
				});
			}
		}
	}
</script>
</head>
<body>
<!-- <div data-options="region:'center',fit:true" style="width: auto; height: auto; padding: 1px; overflow-y: auto"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
	<table id="dataGrid" class="easyui-datagrid" title="合約SLA設定" style="width: 98%; height: auto"
		data-options="
		fitColumns:false,
		border:true,
		pagination:true,
		pageNumber:0,
		pageList:[15,30,50,100],
		pageSize:15,
		remoteSort:true,
		singleSelect:true,
		nowrap : false,
		toolbar : '#tb',
		rownumbers:true,
		iconCls: 'icon-edit'">
		<thead>
			<tr>
				<th data-options="field:'customerName',width:150,sortable:true,halign:'center',align:'left'" >客戶</th>
				<th data-options="field:'contractCode',width:150,sortable:true,halign:'center',align:'left'" >合約編號</th>
				<th data-options="field:'ticketTypeName',width:80,sortable:true,halign:'center',align:'left'">案件類別</th>
				<th data-options="field:'locationName',width:120,sortable:true,halign:'center',align:'left'">區域</th>
				<th data-options="field:'ticketModeName',width:80,sortable:true,halign:'center',align:'left'">案件類型</th>
				<th data-options="field:'isWorkDay',width:70,sortable:true,halign:'center',align:'left'" formatter="fomatterYesOrNo">上班日</th>
				<th data-options="field:'isThatDay',width:70,sortable:true,halign:'center',align:'left'" formatter="fomatterYesOrNo">當天件</th>
				<th data-options="field:'thatDayTime',width:150,sortable:true,halign:'center',align:'center'" >當天件建案時間</th>
				<th data-options="field:'responseHour',width:110,sortable:true,halign:'center',align:'right'">回應時效(時)</th>
				<th data-options="field:'responseWarnning',width:110,sortable:true,halign:'center',align:'right'">回應警示(時)</th>
				<th data-options="field:'arriveHour',width:110,sortable:true,halign:'center',align:'right'">到場時效(時)</th>
				<th data-options="field:'arriveWarnning',width:110,sortable:true,halign:'center',align:'right'">到場警示(時)</th>
				<th data-options="field:'completeHour',width:110,sortable:true,halign:'center',align:'right'">完修時效(時)</th>
				<th data-options="field:'completeWarnning',width:110,sortable:true,halign:'center',align:'right'">完修警示(時)</th>
				<th data-options="field:'comment',width:500,sortable:true,halign:'center',align:'left'">說明</th>
			</tr>
		</thead>
	</table>
	<div id="tb" style="padding: 2px 5px;">
		<form id="searchForm" style="margin: 4px 0px 0px 0px">
			客戶:&nbsp; 
			<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.QUERY_CUSTOMSER_ID %>"
				name="<%=ContractSlaFormDTO.QUERY_CUSTOMSER_ID %>" result="${customerList}"
				disabled="false"
				hasBlankValue="true" blankName="請選擇" 
				style="width: 130px"
				javascript="editable='false' ">
			</cafe:droplisttag>
			合約編號:&nbsp;
			<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.QUERY_CONTRACT_ID %>"
				name="<%=ContractSlaFormDTO.QUERY_CONTRACT_ID %>" 
				disabled="false"
				hasBlankValue="true" blankName="請選擇" 
				style="width: 120px"
				javascript="editable='false' valueField='value' textField='name' " >
			</cafe:droplisttag>
			案件類別:&nbsp;
			<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.QUERY_TICKET_TYPE %>"
				name="<%=ContractSlaFormDTO.QUERY_TICKET_TYPE %>" result="${ticketTypeList}"
				disabled="false"
				style="width: 90px"
				hasBlankValue="true" blankName="請選擇" 
				javascript="editable='false' panelheight=\"auto\"">
			</cafe:droplisttag>
			區域:&nbsp;
			<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.QUERY_LOCATION %>"
				name="<%=ContractSlaFormDTO.QUERY_LOCATION %>" result="${locationList}"
				disabled="false"
				style="width: 90px"
				hasBlankValue="true" blankName="請選擇" 
				javascript="editable='false' panelheight=\"auto\"">
			</cafe:droplisttag>
			案件類型:&nbsp;
			<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.QUERY_IMPORTANCE %>"
				name="<%=ContractSlaFormDTO.QUERY_IMPORTANCE %>" result="${ticketModeList}"
				disabled="false"
				style="width: 90px"
				hasBlankValue="true" blankName="請選擇" 
				javascript="editable='false' panelheight=\"auto\"" >
			</cafe:droplisttag>&nbsp;
			<a class="easyui-linkbutton" href="#" iconcls="icon-search" id="btnQuery">查詢</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="btnAdd">新增</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit" id="btnEdit" >修改</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="btnDelete">刪除</a>&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-tip'" id="btnCopy">複製</a>&nbsp;
			<a href="#" style="width: 150px" id="btnDownload">匯入格式下載</a>&nbsp;&nbsp;&nbsp;
			<cafe:fileuploadTag 
				id="uploadFiled"
				name="uploadFiled"
				uploadUrl="${contextPath}/contractSla.do" 
				uploadParams="{actionId:'upload'}"
				allowedExtensions="'xls','xlsx'" 
				acceptFiles = ".xls,.xlsx" 
				sizeLimit = "${uploadFileSize }"
				showFileList="false"        
				showName="匯入"
				whetherImport="true"
				messageAlert="false"
				whetherDelete="false"
				multiple="false"
				showUnderline = "true"
				whetherDownLoad="false"
				isCustomError = "true"
				messageId="msg"
				javaScript="onComplete:showMessage" >
			</cafe:fileuploadTag>
		</form>
		<div><span id="msg" class="red"></div>
	</div>
	<div id="saveDlg"></div>
	<div id="copyDlg"></div>
</div>
</body>
</html>