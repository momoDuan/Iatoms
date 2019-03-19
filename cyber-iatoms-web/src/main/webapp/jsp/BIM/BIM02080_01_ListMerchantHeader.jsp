<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MerchantHeaderFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	MerchantHeaderFormDTO formDTO = null;
	String useCaseNo = null;
	if (ctx != null) {
		formDTO = (MerchantHeaderFormDTO) ctx.getRequestParameter();
		useCaseNo = formDTO.getUseCaseNo();
	} else {
		formDTO = new MerchantHeaderFormDTO();
	}
	//客戶信息下拉框
	List<Parameter> customerInfoList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.ACTION_GET_COMPANY_PARAMETER_LIST);
	//是否VIP下拉框
	List<Parameter> isVipList = (List<Parameter>) SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.YES_OR_NO);
 %>
<c:set var="customerInfoList" value="<%= customerInfoList %>" scope="page"></c:set>
<c:set var="formDTO" value="<%= formDTO %>" scope="page"></c:set>
<c:set var="isVipList" value="<%= isVipList %>" scope="page"></c:set>
<link href="${contextPath}/css/iatoms_style.css" type="text/css" rel="stylesheet"/>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/jsp/common/easyui-common.jsp"%>
    <script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<!--     <div data-options="fit:true, region:'center'" style="width: auto; height: auto; padding: 1px; overflow-y: hidden"> -->
<div style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
        <table id="dataGrid" class="easyui-datagrid" title="特店表頭維護" style="width: 98%; height: auto"
            data-options="
                rownumbers:true,
                pagination:true,
				iconCls: 'icon-edit',
				singleSelect: true,
				method: 'post',
				pageNumber:0,					
				pageList:[15,30,50,100],
				pageSize:15,
				onClickRow : onClickRow,
				iconCls: 'icon-edit',
            	sortOrder:'asc',
            	nowrap:false,
				toolbar:'#tb'">
            <thead>
                <tr>
                    <th data-options="field:'merchantHeaderId',sortable:true,hidden:true"></th>
                    <th data-options="field:'customerName',width:260,sortable:true,halign:'center'">客戶</th> 
                    <th data-options="field:'companyId',width:100,sortable:true,hidden:true">客戶編號</th>
                    <th data-options="field:'merchantCode',width:180,sortable:true,halign:'center'">特店代號</th>
                    <th data-options="field:'name',width:180,sortable:true,halign:'center'">特店名稱</th>
					<th data-options="field:'unityNumber',width:180,sortable:true,halign:'center'">統一編號</th> 
                    <th data-options="field:'remark',width:400,sortable:true,halign:'center'">備註</th>
                    <th data-options="field:'isVip',width:50,sortable:true,halign:'center',formatter:fomatterYesOrNo">VIP</th>
                    <th data-options="field:'headerName',width:180,sortable:true,halign:'center'">表頭（同對外名稱）</th>
                    <th data-options="field:'merchantAreaName',width:100,sortable:true,halign:'center'">特店區域</th>
                    <th data-options="field:'contact',width:200,sortable:true,halign:'center'">特店聯絡人</th>
                    <th data-options="field:'contactTel',width:180,sortable:true,halign:'center'">特店聯絡人電話1</th>
                    <th data-options="field:'contactTel2',width:180,sortable:true,halign:'center'">特店聯絡人電話2</th>
                    <th data-options="field:'contactEmail',width:300,sortable:true,halign:'center'">聯絡人Email</th>
                    <th data-options="field:'phone',width:100,width:180,sortable:true,halign:'center'">行動電話</th>
                    <th data-options="field:'location',sortable:true,hidden:true,halign:'center'">縣市編號</th>
                    <th data-options="field:'merchantLocationName',width:100,sortable:true,hidden:true,halign:'center'">營業縣市</th>
                    <th data-options="field:'businessAddress',sortable:true,hidden:true,halign:'center'">地址</th>
                    <th data-options="field:'address',width:180,sortable:true,halign:'center',formatter:function(value,row){if((row.postCode == '')||(row.postCode == null)){if((row.merchantLocationName == '')||(row.merchantLocationName == null)){if((row.businessAddress == '')||(row.businessAddress == null)){return '';}else{return row.businessAddress;}}else{if((row.businessAddress == '')||(row.businessAddress == null)){return row.merchantLocationName;}else{return row.merchantLocationName+' '+row.businessAddress;}}}else{if((row.merchantLocationName == '')||(row.merchantLocationName == null)){if((row.businessAddress == '')||(row.businessAddress == null)){return row.postCode;}else{return row.postCode+' '+row.businessAddress;}}else{if((row.businessAddress == '')||(row.businessAddress == null)){return row.postCode+' '+row.merchantLocationName;}else{return row.postCode+' '+row.merchantLocationName+' '+row.businessAddress;}}}}">營業地址</th>
                    <th data-options="field:'openHour',width:100,sortable:true,halign:'center',align:'center'">營業時間起</th>
                    <th data-options="field:'closeHour',width:100,sortable:true,halign:'center',align:'center'">營業時間迄</th>
                    <th data-options="field:'aoName',width:200,sortable:true,halign:'center'">AO人員</th>
                    <th data-options="field:'aoemail',width:300,sortable:true,halign:'center'">AOEmail</th>
                </tr>
            </thead>
        </table>
	      <div id="tb" style="padding: 2px 5px;">
	        <form id="searchForm">
	        <div>
	            客戶:&nbsp;
	            <cafe:droplisttag 
	               id="<%=MerchantHeaderFormDTO.QUERY_CUSTOMER_ID%>"
	               name="<%=MerchantHeaderFormDTO.QUERY_CUSTOMER_ID%>" 
	               css="easyui-combobox"
	               result="${customerInfoList}"
	               hasBlankValue="true"
	               selectedValue="${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?logonUser.companyId:'' }" 
	               blankName="請選擇"
	               style="width: 100px"
	               javascript="editable='false' ${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?'disabled=true':''}">
	            </cafe:droplisttag>
	             特店代號:&nbsp;
	            <input class="easyui-textbox" 
	            	id="<%=MerchantHeaderFormDTO.QUERY_MERCHANT_CODE%>" 
	            	name="<%=MerchantHeaderFormDTO.QUERY_MERCHANT_CODE%>" 
	            	type="text" 
	            	validType="maxLength[20]" 
	            	maxlength="20" 
	            	style="width: 130px"></input>
	            特店名稱:&nbsp;
	            <input class="easyui-textbox" 
	           		 maxlength="50" 
	            	id="<%=MerchantHeaderFormDTO.QUERY_NAME%>" 
	            	name="<%=MerchantHeaderFormDTO.QUERY_NAME%>" 
	            	type="text" 
	            	validType="maxLength[50]" 
	            	style="width: 130px"></input>
	            表頭（同對外名稱）:&nbsp;
	             <input class="easyui-textbox" 
	             maxlength="100" 
	             id="<%=MerchantHeaderFormDTO.QUERY_HEADER_NAME%>" 
	             name="<%=MerchantHeaderFormDTO.QUERY_HEADER_NAME%>" 
	             type="text" 
	             validType="maxLength[100]" 
	             style="width: 130px"></input>
	            VIP:&nbsp;
	            <cafe:droplisttag 
	               id="<%=MerchantHeaderFormDTO.QUERY_IS_VIP%>"
	               name="<%=MerchantHeaderFormDTO.QUERY_IS_VIP%>" 
	               css="easyui-combobox"
	               result="${isVipList}"
	               hasBlankValue="true"
	               blankName="請選擇"
	               style="width: 80px"
	               javascript="panelHeight='auto' editable='false'">
	            </cafe:droplisttag>&nbsp;
	            <a class="easyui-linkbutton" href="#" iconcls="icon-search" id="btnQuery">查詢</a>&nbsp;
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" id="btnAdd">新增</a>&nbsp;
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-edit"  id="btnEdit" onclick="update();">修改</a>&nbsp;
	            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-remove" id="btnDelete"  onclick="deleteData();">刪除</a>
	            <div><span id="msg" class="red"></span></div>
	          </form>
	        </div>
        <div id="dialogView"></div>
        <div id="dialogQueryView"></div>
    </div>
    <script type="text/javascript">
    $(function(){
    //	textBoxMaxlengthSetting();
    	//修改刪除按鈕不可編輯
    	$("#btnEdit").linkbutton('disable');
		$("#btnDelete").linkbutton('disable');
    	//查詢
		$("#btnQuery").click(function() {
			queryData(1, true);
		});
		//新增按鈕
		$("#btnAdd").click(function() {
			$("#msg").text("");
			var queryParams ={actionId : '<%=IAtomsConstants.ACTION_INIT_EDIT%>', merchantHeaderId : 'null'};
			viewEditMerchantHeader('dialogView', '新增特店表頭維護',  queryParams, successCallBack, null, '<%=IAtomsConstants.ACTION_INIT_EDIT%>', "${contextPath}", false);
		});
		/* {if((row.postCode == '')||(row.postCode == null)){if((row.location == '')||(row.location == null)){if((row.businessAddress == '')||(row.businessAddress == null)){return '';}else{return row.businessAddress;}}else{if((row.businessAddress == '')||(row.businessAddress == null)){return row.merchantLocationName;}else{return row.merchantLocationName+' '+row.businessAddress;}}}else{if((row.location == '')||(row.location == null)){if((row.businessAddress == '')||(row.businessAddress == null)){return row.postCode;}else{return row.postCode+' '+row.businessAddress;}}else{if((row.businessAddress == '')||(row.businessAddress == null)){return row.postCode+' '+row.merchantLocationName;}else{return row.postCode+' '+row.merchantLocationName+' '+row.businessAddress;}}}} */
		
	 });
	 /**
	 *　點選datagrid中的一行時
	 */
	function onClickRow() {
		//修改和刪除按鈕變為enable狀態
		$("#btnEdit").linkbutton('enable');
		$("#btnDelete").linkbutton('enable');
	}
	/**
	 * 獲得操作行的merchantheaderId
	 */
	 function getSelectedHeaderId() {
		var merchantHeaderId = '';
		//拿到選中行
		var row = $("#dataGrid").datagrid('getSelected');
		//if (row == null){		
			$("#msg").empty();
			//$.messager.alert('提示','請選擇要操作的記錄!','info'); 
			//return false;
		//} else { 
			//拿到選擇編輯的行的表頭id
			merchantHeaderId = row.merchantHeaderId;   
		//} 
		return merchantHeaderId;
	} 
	/**
	 * 修改按鈕
	 */
	 function update(){
	 	$("#msg").text("");
	 	var row = $("#dataGrid").datagrid('getSelected');
	 	//表頭id
		var merchantHeaderId = getSelectedHeaderId();
		//獲取參數
		var queryParams ={actionId : '<%=IAtomsConstants.ACTION_INIT_EDIT%>', merchantHeaderId : merchantHeaderId};
		viewEditMerchantHeader('dialogView', '修改特店表頭維護', queryParams, successCallBack, null, '<%=IAtomsConstants.ACTION_INIT_ADD%>', "${contextPath}", false);
	 }
	/**
	* 查詢特店表頭的信息
	*/
    function queryData(pageIndex, isCleanMsg) {
    	var queryParam = $("#searchForm").form("getData");
    	//Task #3583
    	queryParam.queryCustomerId = $("#queryCustomerId").combobox('getValue');
		var options = {
				url : "${contextPath}/merchantHeader.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams :queryParam,
				pageNumber:pageIndex,
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","dataGrid"); 
					
					if (isCleanMsg) {
						$("#msg").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#msg").text(data.msg);
						}
					}
					isCleanMsg = true;
				},
				onLoadError : function() {
					$("#msg").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序
		if(isCleanMsg){
			options.sortName = null;
		}
		openDlgGridQuery("dataGrid", options);
    }
 	/**
 	*成功觸發函數
 	*/
	function successCallBack(data){
		$('#dialogView').dialog('close');
        $("#msg").text(data.msg);
        //當前頁碼
        var pageIndex = getGridCurrentPagerIndex("dataGrid");
		queryData(pageIndex, false); 
	}
	//删除时拿到主键值
	function getSelectedParam(actionId) {
		//選中行
		var row = $("#dataGrid").datagrid('getSelected');
		var param ;
		if (row != null) {
			param = {
				actionId : actionId,	
				merchantHeaderId : row.merchantHeaderId,
			};
			return param;
		} //else {
			//$("#msg").text('請勾選要操作的記錄!');
			//return null;
		//}
	}
	
   	//刪除特店表頭信息
    function deleteData() {
       	$("#msg").text("");
       	var params = getSelectedParam("<%=IAtomsConstants.ACTION_DELETE%>");
		var url = '${contextPath}/merchantHeader.do';
		var successFunction = function(data) {
			if (data.success) {
				$("#msg").text(data.msg);
             	//計算要顯示的頁碼
				var pageIndex = calDeletePagerIndex("dataGrid");
				//調用查詢方法；不清空消息
				queryData(pageIndex, false);
			} 
			$("#msg").text(data.msg);
		};
		var errorFunction = function(){
			$("#msg").text("刪除失敗");
		};
		//共有的刪除方法，遮罩
		commonDelete(params,url,successFunction,errorFunction);   
    }
	</script>
</body>
</html>