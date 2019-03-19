<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/jsp/common/common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<html>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//倉庫名稱
	List<Parameter> storageList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
	//設備類別
	List<Parameter> assetCategoryList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
	//設備列表
	List<Parameter> assetNameList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.ASSET_NAME.getCode());
%> 
<c:set var="storageList" value="<%=storageList%>" scope="page"></c:set>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set>
<c:set var="assetNameList" value="<%=assetNameList%>" scope="page"></c:set> 
<head>
	<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
    <div region="center" style="width: auto; height: auto; padding: 1px; background: #eee; overflow-y: hidden">
            <table id="dgEDC" class="easyui-datagrid" title="選擇 EDC" style="width: auto; height: auto"
                data-options="
	                rownumbers:true,
	                pagination:true,
	                pageList:[15,30,50,100],
					pageSize:15,
					iconCls: 'icon-edit',
					singleSelect: true,
					method: 'post',
					pageNumber:0,
					fitColumns:false,
					sortOrder:'asc',
					nowrap:false,
					toolbar:'#tbEDC'">
                <thead>
                    <tr>
                        <th data-options="field:'ck',checkbox:true"></th>
                        <th data-options="field:'serialNumber',width:150,halign:'center',sortable:true">設備序號</th>
                        <th data-options="field:'name',width:150,halign:'center',sortable:true">設備名稱</th>
                        <th data-options="field:'assetCateGory',width:150,halign:'center',sortable:true">設備類別</th>
                        <th data-options="field:'itemName',width:150,halign:'center',sortable:true">倉庫名稱</th>
                        <th data-options="field:'edcPeople',width:150,halign:'center',sortable:true">領用/借用人</th>
                        <th data-options="field:'edcComment',width:250,halign:'center',sortable:true">領用/借用說明</th>
                    </tr>
                </thead>
            </table>
            <div id="tbEDC" style="padding: 2px 5px;">
             <form id="queryEDCform" >
                設備類別:<cafe:droplisttag 
			               id="<%=RepositoryFormDTO.QUERY_ASSET_TYPE %>"
			               name="<%=RepositoryFormDTO.QUERY_ASSET_TYPE %>" 
			               css="easyui-combobox"
			               result="${assetCategoryList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:100px"
			               javascript="editable=false">
			            </cafe:droplisttag>
                設備名稱:<cafe:droplisttag 
			               id="<%=AssetManageFormDTO.QUERY_ASSET_NAME%>"
			               name="<%=AssetManageFormDTO.QUERY_ASSET_NAME%>" 
			               css="easyui-combobox"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:100px"
			               javascript="editable=false data-options=\"valueField:'value',textField:'name'\"">
			            </cafe:droplisttag>
                領用/借用人:<input class="easyui-textbox" 
				            	id="<%=AssetManageFormDTO.QUERY_PEOPLE%>" 
				            	name="<%=AssetManageFormDTO.QUERY_PEOPLE%>" 
				            	type="text" 
				            	validType="maxLength[20]" 
				            	maxlength="20" 
				            	style="width: 100px">
			            	</input><br>
                領用/借用說明:<input class="easyui-textbox" 
				            	id="<%=AssetManageFormDTO.QUERY_COMMET%>" 
				            	name="<%=AssetManageFormDTO.QUERY_COMMET%>" 
				            	type="text" 
				            	validType="maxLength[20]" 
				            	maxlength="20" 
				            	style="width: 100px">
			            	</input>
                倉庫名稱: <cafe:droplisttag 
			               id="<%=RepositoryFormDTO.QUERY_HOUSE %>"
			               name="<%=RepositoryFormDTO.QUERY_HOUSE %>" 
			               css="easyui-combobox"
			               result="${storageList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:100px"
			               javascript="editable=false">
			            </cafe:droplisttag>
                設備序號:<input class="easyui-textbox" 
				            	id="<%=AssetManageFormDTO.QUERY_NUMBER%>" 
				            	name="<%=AssetManageFormDTO.QUERY_NUMBER%>" 
				            	type="text" 
				            	validType="maxLength[20]" 
				            	maxlength="20" 
				            	style="width: 100px">
			           </input>
                <a href="#" class="easyui-linkbutton" iconcls="icon-search" id="btnEDCQuery">查詢</a>
                <div><span id="EDCmsg" class="red"></span></div>
                </form>
            </div>
    </div>
    <script type="text/javascript">
  //設備類別連動資料
  $(function(){
	$("#queryAssetType").combobox({
		onChange:function(newValue, oldValue){
		    $("#queryAssetName").combobox("setValue","");
			if (!isEmpty(newValue)) {
    			ajaxService.getAssetTypeList(newValue,function(data){
    				if (data !=null) {
    					$("#queryAssetName").combobox("loadData", initSelect(data));
    				}
    			});
			} else {
				//置空表單數據
				$("#queryAssetName").combobox("loadData",initSelect());
			}
		}
	}); 
  }); 
  
  $(function(){
 	 	$("#EDCmsg").text("");
  	//查詢
		$("#btnEDCQuery").click(function(){
			queryDTID(1,true);
		}); 
    });
    
    //查詢數據
   function queryDTID(pageIndex, isCleanMsg) {
  	 $("#EDCmsg").text("");
  	// 清空選中的行
		 $("#dgEDC").datagrid("unselectAll");
		 $("#dgEDC").datagrid({
			url : "${contextPath}/assetManage.do?actionId=queryEDC",
			queryParams : $("#queryEDCform").form("getData"),
			//當前頁
			pageNumber:pageIndex,
			//返回出錯信息
			onLoadError : function(data) {
				$.messager.alert('提示', '查詢EDC錯', 'error');
			},
			//返回成功信息
		   onLoadSuccess : function (data) {
				if (isCleanMsg) {
					$("#EDCmsg").text("");
				}
				if (data.total == 0) {
					//提示查無數據
					$("#EDCmsg").text(data.msg);
				}
				isCleanMsg = true;
			}
  	}); 
   }
	</script>
</body>
</html>