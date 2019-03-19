<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApplicationFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
 <%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//客戶下拉框列表
	List<Parameter> customerInfoList =
		 (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_PVM_04010, ApplicationFormDTO.CUSTOMER_INFO_LIST);
	//適用設備下拉框列表
	List<Parameter> assetTypeList =
		 (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_PVM_04010, ApplicationFormDTO.GET_ASSET_TYPE_LIST);
%> 
<c:set var="customerInfoList" value="<%=customerInfoList%>" scope="page"></c:set>
<c:set var="assetTypeList" value="<%=assetTypeList%>" scope="page"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/> 
<html>
<head>
<meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/css/multi-select.css">
    <%@include file="/jsp/common/easyui-common.jsp"%>
	<script type="text/javascript" src="${contextPath}/assets/js/jquery.multi-select.js"></script>
	<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview.js"></script>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
<!--     <div region="center" style="width: 98%; height: auto; padding: 1px;  overflow-y: hidden"> -->
        <table id="dataGrid" class="easyui-datagrid" title="程式版本維護" style="width: 98%; height: auto"
            data-options="
                rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				onClickRow : onClickRow,
				pageNumber:0,
				singleSelect: true,
				sortOrder:'asc',
				nowrap:false,
				toolbar:'#tb'">
            <thead>
            	<tr>
            		<th data-options="field:'applicationId',sortable:true,align:'center',hidden:true">主鍵</th>
            		<th data-options="field:'customerName',width:160,sortable:true,halign:'center'">客戶</th>
	               	<th data-options="field:'version',width:160,sortable:true,halign:'center'">版本編號</th>
	               	<th data-options="field:'name',width:160,sortable:true,halign:'center'">程式名稱</th>
	               	<th data-options="field:'assetTypeName',width:400,sortable:true,halign:'center'">適用設備</th>
	               	<th data-options="field:'comment',sortable:true,width:300,halign:'center'">說明</th>
	               	<th data-options="field:'updatedByName',width:160,sortable:true,halign:'center'">異動人員</th>
	               	<th data-options="field:'updatedDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp">異動日期</th>
          			<th data-options="field:'assetCategory',sortable:true,halign:'center',hidden:true">設備類型編號</th>
          		</tr>
			 </thead>
    </table>
    <div id="tb" style="padding: 2px 5px;">
    <form action="application.do" id="queryForm" name="queryForm" method="post">
    	<input type="hidden" id="actionId" name="actionId" />
		<input type="hidden" id="serviceId" name="serviceId" />
		<input type="hidden" id="useCaseNo" name="useCaseNo" />
    	客戶:	
    	<cafe:droplisttag 
               id="<%=ApplicationFormDTO.QUERY_COMPANY_ID%>"
               name="<%=ApplicationFormDTO.QUERY_COMPANY_ID%>" 
               css="easyui-combobox"
               result="${customerInfoList}"
               hasBlankValue="true"
               blankName="請選擇"
               style="width: 170px"
               javascript="editable=false">
              </cafe:droplisttag>
		版本編號:
		<input class="easyui-textbox" 
		id="<%=ApplicationFormDTO.QUERY_VERSION%>" 
		name="<%=ApplicationFormDTO.QUERY_VERSION%>"
		style="width: 150px"
		maxlength="20" 
		validType="maxLength[20]"/> 
		程式名稱:
		<input class="easyui-textbox"
		 id="<%=ApplicationFormDTO.QUERY_NAME%>" 
		 name="<%=ApplicationFormDTO.QUERY_NAME%>"
		 style="width: 150px"
		 maxlength="50"
		 validType="maxLength[50]"/>
		適用設備:
		<cafe:droplistchecktag 
               id="queryAssetTypeId"
               name="queryAssetTypeId" 
               css="easyui-combobox"
               hasBlankValue="true"
               result="${assetTypeList}"
               blankName="請選擇(複選)"
               style="width: 180px"
               javascript="editable=false multiple=true">
              </cafe:droplistchecktag>
		<a href="#" class="easyui-linkbutton" iconcls="icon-search" id="btnQuery">查詢</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="btnAdd">新增</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit"  id="btnEdit" onClick="update();">修改</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="btnDelete" onclick="deleteInfo();">刪除</a>
  	 	<div class="red" id="message" ></div>
  	 	<div style="text-align: right"><a href="#" style="width: 150px" onclick="doExport()" id="btnExport">匯出</a></div>
  	 	</form> 
    </div>
    <div id="dlg"></div>
</div>
<script type="text/javascript">
	$(function(){
		//首次進入頁面禁用修改、刪除、匯出按鈕，並將匯出按鈕變為灰色
		$("#btnEdit").linkbutton('disable');
		$("#btnDelete").linkbutton('disable');
		$('#btnExport').attr("onclick","return false;");
		$('#btnExport').attr("style","color:gray;");
		//查詢
		$("#btnQuery").click(function(){
			$("#btnEdit").linkbutton('disable');
			$("#btnDelete").linkbutton('disable');
			query(1, true);
		});
		//跳轉到新增畫面
		$("#btnAdd").click(function(){
			$("#message").text("");
			viewEditData('新增程式版本維護','<%=IAtomsConstants.ACTION_INIT_ADD%>', '','');
		});
		
	});
	//使用設備欄位的值
	$("#queryAssetTypeId").combobox({
		//選中某個值時添加值
		onSelect : function(newValue) {
			selectMultiple(newValue, "queryAssetTypeId")
		},
        //沒有選中的值時將“請選擇”設為選中
		onUnselect : function(newValue) { 
			unSelectMultiple(newValue, "queryAssetTypeId"); 
		},
  	});
	  	
	/**
	 *　點選datagrid中的一行時
	 */
	function onClickRow(){
		//獲取選中的行
		var row = $('#dataGrid').datagrid('getSelected');
		//當有選中的行時，修改和刪除按鈕變為enable狀態；無選中的行時修改和刪除按鈕仍為禁用狀態
		if (row != null) {
			$("#btnEdit").linkbutton('enable');
			$("#btnDelete").linkbutton('enable');
		} else {
			$("#btnEdit").linkbutton('disable')
			$("#btnDelete").linkbutton('disable')
		}
	}
	//修改
	function update(){
		var row = $('#dataGrid').datagrid('getSelected');
		if (row == null) {
			$.messager.alert('提示', '請勾選要操作的記錄!', 'warning');
			return false;
		} else {
	    	$("#message").text("");
	    	viewEditData('修改程式版本維護','<%=IAtomsConstants.ACTION_INIT_EDIT%>', row.applicationId, row.assetCategory);
	    }
	}
	//匯出
	function doExport(){
		var queryAssetTypeId = $('#queryAssetTypeId').combobox("getValues");
		$('#queryAssetTypeId').val(queryAssetTypeId);
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		actionClicked(document.forms["queryForm"],
				'<%=IAtomsConstants.UC_NO_PVM_04010%>',
				'',
				'<%=IAtomsConstants.ACTION_EXPORT%>');
		
		ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_PVM_04010%>',function(data){
			$.unblockUI();
		});
	}
	//跳轉到程式版本維護詳情頁--update by 2017/08/21 將小畫面調長，不要滾動條
	function viewEditData(title,actionId,applicationId,assetCategory) {
		var viewDlg = $('#dlg').dialog({    
		    title: title,    
		    width: 780,
		    height:550,
		    top:10,
		    closed: false,    
		    cache: false,
		    queryParams : {
		    	actionId : actionId,
		    	applicationId : applicationId,
		    	assetCategory : assetCategory
		    },
		    href: "${contextPath}/application.do",
		    modal: true,
		    //給頁面上的長度限制框添加maxlength屬性
		    onLoad : function() {
		    	textBoxSetting("dlg");
      		  },
      		//關閉頁面時移除提示消息
      		onClose:function(){
      			$("div[data-list-required]").removeClass("div-tips").addClass("div-list").tooltip("destroy");
		      },
		    buttons : [{
				text:'儲存',
				iconCls:'icon-ok',
				width : '90px',
				handler: function(){
					$("#errorMsg").text("");
					//獲取要傳遞的參數
					var saveParam = $("#fm").form("getData");
					//設置傳遞的客戶編號
				 	saveParam.customerId = $("#<%=ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>").combobox("getValue");
					//請求地址
					var url="${contextPath}/application.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>";
					//要驗證的節點集合
					var controls = [
						'<%=ApplicationDTO.ATTRIBUTE.CUSTOMER_ID.getValue()%>',
						'<%=ApplicationDTO.ATTRIBUTE.VERSION.getValue()%>',
						'<%=ApplicationDTO.ATTRIBUTE.NAME.getValue()%>',
						'checkWeekRests'];
					if (validateForm(controls) && $('#fm').form("validate")){
						// 調保存遮罩
						commonSaveLoading('dlg');
			            $.ajax({
							url : url,
							data : saveParam,
							type : 'post', 
							cache : false, 
							dataType : 'json', 
							success : function(data) {
								// 去除保存遮罩
								commonCancelSaveLoading('dlg');
								//儲存成功時關閉頁面設置提示消息並調用查詢方法
								if (data.success) {
			                		$('#dlg').dialog('close');
				                    $("#message").text(data.msg);
				                    //計算頁碼
				                    var pageIndex = getGridCurrentPagerIndex("dataGrid");
									query(pageIndex,false);
									$("#btnEdit").linkbutton('disable');
					            	$("#btnDelete").linkbutton('disable');
								} else {
									//儲存失敗時
									handleScrollTop('dlg');
									$("#errorMsg").text(data.msg);
								}
							},
							error : function() {
								// 去除保存遮罩
								commonCancelSaveLoading('dlg');
								var msg;
								if (actionId == '<%=IAtomsConstants.ACTION_INIT_ADD%>') {
									msg = "新增失敗";
								} else {
									msg = "修改失敗";
								}
								$.messager.alert('提示', msg, 'error');							
							}
						});
			        }
		    	}
			},{
				text : '取消',
				width : '90px',
				iconCls : 'icon-cancel',
				handler : function() {
					confirmCancel( function() {
	                     viewDlg.dialog('close');
	               });
				}
			}]
		});    
	}
	
		//查詢程式版本信息
		function query(pageIndex,isSaveOrEdit){
			var queryParam = $("#queryForm").form("getData");
			var options = {
					url : "${contextPath}/application.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
					queryParams :queryParam,
					pageNumber:pageIndex,
					onLoadSuccess:function(data){
						if (isSaveOrEdit) {
							$("#message").text("");
							if (data.total == 0) {
								// 提示查無數據
								$("#message").text(data.msg);
								$('#btnExport').attr("onclick","return false;");
								$('#btnExport').attr("style","color:gray;");
							}else{
								$('#btnExport').attr("onclick","return doExport();");
								$('#btnExport').attr("style","color:blue;");
							}
						}
						isSaveOrEdit = true;
					},
					onLoadError : function() {
						$("#message").text("查詢失敗！請聯繫管理員");
					}
				}
			// 清空點選排序
			if(isSaveOrEdit){
				options.sortName = null;
			}
			openDlgGridQuery("dataGrid", options);
		}
		//删除时拿到主键值
		function getSelectedParam(actionId) {
			var row = $("#dataGrid").datagrid('getSelected');
			var param ;
			if (row != null) {
				param = {
					actionId : actionId,	
					applicationId : row.applicationId,
				};
				return param;
			} else {
				$("#message").text('請勾選要操作的記錄!');
				return null;
			}
		}
	
		//刪除程式版本信息
		function deleteInfo() {
			$("#message").text("");
			//獲取要刪除的主鍵值
	       	var params = getSelectedParam("<%=IAtomsConstants.ACTION_DELETE%>");
			var url = '${contextPath}/application.do';
			//刪除成功
			var successFunction = function(data) {
				if (data.success) {
					//計算要顯示的頁碼
					var pageIndex = calDeletePagerIndex("dataGrid");
					query(pageIndex,false);
					$("#btnEdit").linkbutton('disable');
	            	$("#btnDelete").linkbutton('disable');
				} 
				$("#message").text(data.msg);
			};
			//刪除失敗
			var errorFunction = function(){
				$("#message").text("刪除失敗");
			};
			//使用通用的刪除方法
			commonDelete(params,url,successFunction,errorFunction);  
		}
</script>
</body>                