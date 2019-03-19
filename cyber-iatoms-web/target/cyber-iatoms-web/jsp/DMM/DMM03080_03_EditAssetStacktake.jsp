<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetStacktakeInfoDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetStacktakeFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetStacktakeFormDTO formDTO = null;
	//新增时的设备批号
	String addAssetInventoryId = null;
	if (ctx != null) {
		formDTO = (AssetStacktakeFormDTO)ctx.getResponseResult();
	}
	if (formDTO == null) {
		formDTO = new AssetStacktakeFormDTO();
	} else {
		addAssetInventoryId = formDTO.getAssetInventoryId();
	}
	//倉庫集合
	List<Parameter> warehouseList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
 	//設備狀態
 	List<Parameter> assetStatusList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ASSET_STATUS);
 	//設備名稱
 	List<Parameter> assetNameList = (List<Parameter>) SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_DMM_03080, IAtomsConstants.ACTION_GET_ASSET_NAME_LIST);
%>
<c:set var="warehouseList" value="<%=warehouseList%>"></c:set>
<c:set var="assetStatusList" value="<%=assetStatusList%>"></c:set>
<c:set var="assetNameList" value="<%=assetNameList%>"></c:set>
<c:set var="addAssetInventoryId" value="<%=addAssetInventoryId%>"></c:set>
<html>
<head>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
	<div data-options="region:'center',fit:true" style="width: auto; height: auto; padding: 10px 20px; overflow-y: hidden">
			<div>
				<span id="msg1" class="red"></span>
			</div>   
				<form id="fm" method="post">
					<table cellpadding="4">
						<tr>
							<td>盤點批號:<span class="red">*</span></td>
							<td>
								<input id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.STOCKTACK_ID.getValue()%>"
									name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.STOCKTACK_ID.getValue()%>" 
									disabled="disabled"
									class="easyui-textbox" 
									value="新增批號" />
								<input name="stocktackId" type="hidden"   value="<c:out value='${addAssetInventoryId}'/>"/>
							</td>
							<%--  <input type="hidden" id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.TYPE_ID_LIST.getValue()%>" name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.TYPE_ID_LIST.getValue()%>" value=""/>
								<input type="hidden" id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.STATUS_LIST.getValue()%>" name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.STATUS_LIST.getValue()%>" value=""/> --%>
							<td>倉庫名稱:<span class="red">*</span></td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.WAR_WAREHOUSE_ID.getValue() %>"
									name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.WAR_WAREHOUSE_ID.getValue() %>" 
									result="${warehouseList }"
									javascript="validType=\"requiredDropList\" editable=false required=true invalidMessage=\"請輸入倉庫名稱\" "
									blankName="請選擇"
									hasBlankValue="true"
									style="width:150px">
								</cafe:droplisttag>
							</td>
						</tr>
						<tr>
							<td>設備名稱:<span class="red">*</span></td></td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>"
									name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue() %>" 
									result="${assetNameList }"
									javascript="validType=\"requiredDropList\" editable=false multiple=true editable=false required=true invalidMessage=\"請輸入設備名稱\""
									blankName="請選擇(複選)"
									hasBlankValue="true"
									style="width:186px">
								</cafe:droplisttag>
							</td>
							<td>設備狀態:<span class="red">*</span></td></td>
							<td>
								<cafe:droplisttag
									css="easyui-combobox"
									id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_STATUS.getValue() %>"
									name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_STATUS.getValue() %>" 
									result="${assetStatusList }"
									javascript="validType=\"requiredDropList\" editable=false multiple=true editable=false required=true invalidMessage=\"請輸入設備狀態\""
									blankName="請選擇(複選)"
									hasBlankValue="true"
									style="width:186px">
								</cafe:droplisttag>
							</td>
						</tr>
						<tr>
							<td>說明:</td>
							<td colspan="3">
								<textarea 
									id="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.REMARK.getValue()%>" 
									name="<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.REMARK.getValue()%>" 
									maxlength="200" 
									class="easyui-textbox" 
									data-options="multiline:true"
									validType="maxLength[200]"
									style="height: 88px; width: 400px" ></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
	<script type="text/javascript">
	    var editIndex = undefined;
	    var url;
	    //復選框
	   	$(function () {
	   		$("#<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue()%>").combobox({
	    			onSelect : function(newValue) {
	    			selectMultiple(newValue,"assetTypeId");
	   			},
			    	onUnselect : function (newValue) {
					unSelectMultiple(newValue, "assetTypeId")
			    },
	   		});
	   		$("#<%=DmmAssetStacktakeInfoDTO.ATTRIBUTE.ASSET_STATUS.getValue()%>").combobox({
	    			onSelect : function(newValue) {
	    			selectMultiple(newValue,"assetStatus");
	   			},
	   				onUnselect : function (newValue) {
					unSelectMultiple(newValue, "assetStatus")
			    },
	   		});
	   	});
	   	
	</script>
</body>
</html>