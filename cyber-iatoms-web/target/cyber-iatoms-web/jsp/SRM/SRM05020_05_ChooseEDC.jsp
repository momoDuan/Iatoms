<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.GsonBuilder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	CaseManagerFormDTO formDTO = null;	
	String ucNo = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (CaseManagerFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			// 获得UseCaseNo
			ucNo = formDTO.getUseCaseNo();
			
		} else {
			ucNo = IAtomsConstants.UC_NO_SRM_05020;
			formDTO = new CaseManagerFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_SRM_05020;
		formDTO = new CaseManagerFormDTO();
	}
	// 設備類別列表
	List<Parameter> assetCategoryList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
	// 設備名稱列表
	List<Parameter> assetNameList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_ASSET_ANME_LIST);
	// 仓库据点列表
	List<Parameter> warehouseList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_WAREHOUSE_LIST);
	//案件關聯設備動作集合
	List<Parameter> actionsList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IATOMS_PARAM_TYPE.ACTIONS.getCode());
	Gson gsonss = new GsonBuilder().create();
	String actionsListString = gsonss.toJson(actionsList);
%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<html>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="assetNameList" value="<%=assetNameList%>" scope="page"></c:set>
<c:set var="actionsListString" value="<%=actionsListString%>" scope="page"></c:set>
<c:set var="actionsList" value="<%=actionsList%>" scope="page"></c:set>
<c:set var="warehouseList" value="<%=warehouseList%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>選擇EDC</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript"/>
</head>
<body>
<body>
<div region="center" style="margin-left:2%; width: auto; height: auto; padding: 10px; overflow-y: auto" class="setting-dialog-panel-height topSoller">
	<!-- <div region="center" style="margin-left:2%;width: auto; height: auto; padding: 10px;  overflow-y: hidden"> -->
        <!-- <form id="fmDTID" method="post"> -->
            <table id="dataGrid" class="easyui-datagrid" title="選擇 EDC" style="width: 98%; height: auto"
                data-options="
                rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				onCheck:chooseEdcOnCheck,
				singleSelect: true,
				nowrap:false,
				toolbar:'#tbDTID'">
                <thead>
                    <tr>
                        <th data-options="field:'serialNumber',width:150,sortable:true,halign:'center'">設備序號</th>
                        <th data-options="field:'itemCategoryName',width:100,sortable:true,halign:'center'">設備類別</th>
                        <th data-options="field:'itemName',width:150,sortable:true,halign:'center'">設備名稱</th>
                        <th data-options="field:'warehouseName',width:180,sortable:true,halign:'center'">倉庫名稱</th>
                        <th data-options="field:'edcPeople',width:140,sortable:true,halign:'center'">領用/借用人</th>
                        <th data-options="field:'edcComment',width:300,sortable:true,halign:'center'">領用/借用說明</th>
                    	<th data-options="field:'warehouseId',width:80,sortable:true,hidden:true, halign:'center'">倉庫id</th>
                    	<th data-options="field:'contractId',width:80,sortable:true, hidden:true, halign:'center'">合約id</th>
                    </tr>
                </thead>
            </table>
         <!-- </form> -->
            <div id="tbDTID" style="padding: 2px 5px;">
            <form id="searchForm">
            	
                <table>
                    <tr>
                        <td>設備類別:</td>
                        <td>
                            <cafe:droplisttag
                            	id="chooseEdcAssetCategory"
								name="chooseEdcAssetCategory" 
								css="easyui-combobox"
								hasBlankValue="true"
								result="${assetCategoryList}"
								blankName="請選擇"
								disabled="true"
								style="width: 150px;" 
								javascript="editable='false'"
								selectedValue="${formDTO.chooseEdcAssetCategory}"
							></cafe:droplisttag>
							</td>
                        <td>設備名稱:</td>
                        <td>
							<cafe:droplisttag
								id="chooseEdcAssetId"
								name="chooseEdcAssetId" 
								css="easyui-combobox"
								disabled="true"
								hasBlankValue="true"
								result="${assetNameList}"
								style="width:150px"
								blankName="請選擇"
								selectedValue="${formDTO.chooseEdcAssetId}"
								javascript="editable='false'"
							></cafe:droplisttag>
						</td>
                        <td>設備序號:</td>
                        <td>
                            <input maxlength="20" class="easyui-textbox" type="text" id="chooseEdcSerialNumber" name="chooseEdcSerialNumber" 
								style="width: 180px;" data-options="validType:['maxLength[20]']"/> 
                    </tr>
                    <tr>
                        <td>領用/借用人:</td>
                        <td>
                            <input id="chooseEdcCarrierOrBorrower" name="chooseEdcCarrierOrBorrower" class="easyui-textbox"
								style="width: 150px;" maxlength="50"  type="text" data-options="validType:['maxLength[50]']"/> 
                        <td>領用/借用說明:</td>
                        <td>
                            <input id="chooseEdcCarrierOrBorrowerComment" name="chooseEdcCarrierOrBorrowerComment" class="easyui-textbox"
								style="width: 150px;" maxlength="200"  type="text" data-options="validType:['maxLength[200]']"/> 
                        <td>倉庫名稱:</td>
                        <td>
                            <cafe:droplisttag
                            	id="chooseEdcWarehouseId"
								name="chooseEdcWarehouseId" 
								css="easyui-combobox"
								hasBlankValue="true"
								result="${warehouseList}"
								blankName="請選擇"
								style="width: 180px;" 
								javascript="editable='false'"
							></cafe:droplisttag>
                        <td> <a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" iconcls="icon-search">查詢</a></td>
						 <input type="hidden" id="assetLinkParamer" name="assetLinkParamer" value=""/>
						 <input type="hidden" id="caseId" name="caseId" value="<c:out value='${formDTO.caseId}'/>"/>
                    </tr>   
                </table>
				<div><span id="chooseEdcMsg" class="red"></span></div>
               
       		 </form>
          </div>


    </div>
    <script type="text/javascript">
     var editIndex = undefined;
        $(function(){
        	//查詢
			$("#btnQuery").click(function(){
				queryChooseEdcData(1);
			}); 
        })
	
	/*
	* 查詢EDC信息
	*/
	function queryChooseEdcData(pageIndex, params) {
		var saveForm = $("#searchForm");
		if(!params){
			params = $("#searchForm").form("getData");
			params.caseId =  saveForm.find("#caseId").val();
			params.chooseEdcAssetCategory = saveForm.find("#chooseEdcAssetCategory").val();
			params.chooseEdcAssetId = saveForm.find("#chooseEdcAssetId").val();
		}
		params.selectSn =$("#selectSn").val();
		var options = {
				url : "${contextPath}/caseHandle.do?actionId=<%=IAtomsConstants.QUERT_CASE_ASSET%>",
				queryParams :params,
				pageNumber:pageIndex,
				sortName : '',
				isOpenDialog:true,
				onLoadSuccess:function(data){
					$(this).datagrid("fixRownumber","dataGrid"); 
					
					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "pointer");
   					});
   					$(".datagrid-row").mouseout(function(){ 
   						$(this).css("cursor", "auto");
   					});
					$("#chooseEdcMsg").text("");
					if (data.total == 0) {
						// 提示查無數據
						$("#chooseEdcMsg").text(data.msg);
					}
				},
				onLoadError : function() {
					$("#chooseEdcMsg").text("查詢失敗！請聯繫管理員");
				}
			}
		openDlgGridQuery("dataGrid", options);
	}
	/*
	* 選中一行觸發事件
	*/
	function chooseEdcOnCheck(index, field) {
		var assetRow = field;
		var params = {
			itemCategory : assetRow.itemCategory,
			itemId : assetRow.itemId,
			serialNumber : assetRow.serialNumber,
			edcPeople : assetRow.edcPeople,
			edcComment : assetRow.edcComment,
			warehouseId : assetRow.warehouseId,
			propertyId : assetRow.propertyId
		}
		var value = $("#assetLinkParamerValue").val();
		var ddvid = $("#assetLinkParamerAssetLinkDatagridID").val();
		var caseId = $("#assetLinkParamerCaseId").val();
		var index = $("#assetLinkParamerIndex").val();
		var caseCategory = $("#assetLinkParamerCaseCategory").val();
		var assetLinkDatagridID = $("#assetLinkParamerAssetLinkDatagridID").val();
		var rows = $("#"+assetLinkDatagridID).datagrid('getRows');
		var row = rows[index];
		var assetLinkId = row.assetLinkId;
		var actionsList = initSelect(<%=actionsListString %>);
		var obj = new Object();
		if($("#"+assetLinkId+ddvid).length>0) {
			obj.action=$("#"+assetLinkId+ddvid).combobox("getValue");
		}
		if(caseCategory != '${caseCategoryAttr.INSTALL.code }') {
			obj.content=$("#"+assetLinkId+"content").textbox("getValue");
		}
		$("#"+ddvid).datagrid('updateRow',{
			index: index,
			row: {
				serialNumber: assetRow.serialNumber,
				isLink : 'Y',
				contractId : assetRow.contractId,
				warehouseId : assetRow.warehouseId,
				propertyId : assetRow.propertyId,
				isRepeatLink : 'N'
			}
		});
		var sn;
		sn = $("#selectSn").val();
		$("#selectSn").val(sn + assetRow.serialNumber + ",");
		$('#edcDialog').dialog('close'); // close the dialog
		if($("#"+assetLinkId+ddvid).length>0) {
			caseAssetLinkActionFormatter();
			$("#"+assetLinkId+ddvid).siblings("span").css('display','none');
		};
		if(caseCategory != '${caseCategoryAttr.INSTALL.code }') {
			caseAssetLinkContentFormatter(value,row,index);
			$(".textbox-contentLink").textbox();
		 	$(".textbox-contentLink").each(function(index, obj){
				textBoxDefaultSetting($(obj));
			});
			$("#"+assetLinkId+"content").textbox('setValue',obj.content);
		};
	}
	</script>
</body>
</body>
</html>