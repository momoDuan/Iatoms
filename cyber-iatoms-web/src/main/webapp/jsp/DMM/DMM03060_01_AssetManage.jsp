<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO"%>

<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetManageFormDTO formDTO = null;	
	String useCompany = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (AssetManageFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
		} else {
			formDTO = new AssetManageFormDTO();
		}
	} else {
		formDTO = new AssetManageFormDTO();
	}
	//廠商列表
	List<Parameter> vendorList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.VENDOR_LIST);
	//硬體廠商列表
	List<Parameter> repairVendorList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.REPAIR_VENDOR_LIST);
	//維護廠商列表
	List<Parameter> repairdVendorList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.REPAIRD_VENDOR_LIST);
	//客戶列表
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.CUSTOMER_LIST);
	//資產OWNER和使用人
	List<Parameter> customers = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.CUSTOMER_AND_VENDOR_LIST);
	//設備列表
	List<Parameter> assetNameList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.ASSET_NAME.getCode());
	//裝機區域列表
	List<Parameter> locationList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.LOCATION.getCode());
	//倉庫名稱
	List<Parameter> storageList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.ACTION_GET_WAREHOUSE_BY_USER_ID);
	//維護模式列表
	List<Parameter> maTypeList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.PARAM_MA_TYPE);
	//執行作業集合
	List<Parameter> actionList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.ACTION.getCode());
	//合約ID集合
	List<Parameter> contractList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.ACTION_GET_CONTRACT_LIST);
	//狀態集合
	List<Parameter> assetStatusList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.PARAM_ASSET_STATUS);
	//報廢原因集合
	List<Parameter> retireReasonList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.RETIRE_REASON.getCode());
	//故障組件集合
	List<Parameter> faultComponentList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
	//故障現象集合
	List<Parameter> faultDescriptionList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
	//是否啟用
	List<Parameter> isEnabled = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.YES_OR_NO.getCode());
	//設備類別
	List<Parameter> assetCategoryList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.ASSET_CATEGORY.getCode());
	//用戶公司
	List<Parameter> attributeList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue());
	if(!CollectionUtils.isEmpty(attributeList) && attributeList.get(0)!=null && attributeList.get(0).getName()!=null){
		if ("N".equals(attributeList.get(0).getName())) {
			if (logonUser != null) {
				useCompany = attributeList.get(0).getValue().toString();
			}
		} 
	}
	//解除綁定原因
	List<Parameter> removeOrLossList = (List<Parameter>) SessionHelper.getAttribute(request,
			IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.ASSET_REMOVE_STATUS.getCode());
	String ucNo = IAtomsConstants.UC_NO_DMM_03060;
	//角色權限
	List<String> userFunction = logonUser.getUserFunctionCodes();
%> 
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />
<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="vendorList" value="<%=vendorList%>" scope="page"></c:set> 
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="assetNameList" value="<%=assetNameList%>" scope="page"></c:set> 
<c:set var="storageList" value="<%=storageList%>" scope="page"></c:set>
<c:set var="maTypeList" value="<%=maTypeList%>" scope="page"></c:set>
<c:set var="actionList" value="<%=actionList%>" scope="page"></c:set>
<c:set var="contractList" value="<%=contractList%>" scope="page"></c:set>
<c:set var="assetStatusList" value="<%=assetStatusList%>" scope="page"></c:set>
<c:set var="isEnabled" value="<%=isEnabled%>" scope="page"></c:set>
<c:set var="assetCategoryList" value="<%=assetCategoryList%>" scope="page"></c:set>
<c:set var="retireReasonList" value="<%=retireReasonList%>" scope="page"></c:set> 
<c:set var="faultComponentList" value="<%=faultComponentList%>" scope="page"></c:set> 
<c:set var="faultDescriptionList" value="<%=faultDescriptionList%>" scope="page"></c:set> 
<c:set var="repairdVendorList" value="<%=repairdVendorList%>" scope="page"></c:set>
<c:set var="useCompany" value="<%=useCompany%>" scope="page"></c:set>
<c:set var="customers" value="<%=customers%>" scope="page"></c:set>
<c:set var="locationList" value="<%=locationList%>" scope="page"></c:set>
<c:set var="removeOrLossList" value="<%=removeOrLossList%>" scope="page"></c:set>
<c:set var="repairVendorList" value="<%=repairVendorList%>" scope="page"></c:set>
<c:set var="userFunction" value="<%=userFunction%>" scope="page"></c:set>

<head>
<meta charset="UTF-8">
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
<script type="text/javascript" src="${contextPath}/js/json2.js"></script>
<script type="text/javascript" src="${contextPath}/js/assetManage-1.1.min.js"></script>

</head>
<body>
<div id="assetManageDivId" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
    <div id="p" class="easyui-panel" title="設備管理作業" style="width: 98%; height: auto; padding: 1px; overflow-y: hidden" iconcls="icon-edit">
        <div title="" style="padding: 10px">
            <form id="exportForm" method="post">
            <input type="hidden" id="actionId" name="actionId" />
			<input type="hidden" id="serviceId" name="serviceId" />
			<input type="hidden" id="useCaseNo" name="useCaseNo" />
			<input type="hidden" id="exportField" name="exportField"/>
			<input type="hidden" id="historyExport" name="historyExport"/>
			<input id="codeGunFlag" name="codeGunFlag"type="hidden" />
			<input id="exportCodeGunSerialNumbers" name="exportCodeGunSerialNumbers"type="hidden" />
            <input id="exportCodeGunPropertyIds" name="exportCodeGunPropertyIds"type="hidden" />
            <table cellpadding="6">
            	<tr><td>設備序號:</td>
                    <td colspan="5">
                        <input id="querySerialNumbers" name="querySerialNumbers"  style="width:600px" type="text" value=""></input><a href="javascript:void(0)"  onclick="beforeOpen('dlgQ');">多筆輸入</a></td>
                		<input id="hideCodeGun" type="hidden" />
                		<input id="hideCodeGunSerialNumbers" type="hidden" />
                		<input id="hideCodeGunRowNumber" type="hidden" />
                		<input id="hideCodeGunAfterQuery" type="hidden" />
                		<input id="hideCodeGunFlag" type="hidden" />
                		<input id="hideHistoryChangeFlag" type="hidden" value="0"/>
                </tr>
            	<tr>
            		<td>財產編號:</td>
                    <td colspan="5">
                        <input id="queryPropertyIds" name="queryPropertyIds"  type="text" style="width:600px" value=""></input><a href="javascript:void(0)"  onclick="beforeOpen('dlgInput');">多筆輸入</a></td>
                		<input id="hideCodeGunPropertyIds" type="hidden" />
                		<input id="isSelectedAll" type="hidden" />
                 </tr>
                <tr>
                	
                    <td>設備類別:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryAssetCategory"
			               name="queryAssetCategory" 
			               
			               result="${assetCategoryList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                    <td>設備名稱:</td>
                    <td>
                        <cafe:droplisttag 
			               id="assetTypeName"
			               name="assetTypeName" 
			              
			               hasBlankValue="true"
			               result="${assetNameList}"
			               blankName="請選擇(複選)"
			               style="width:120px">
			            </cafe:droplisttag>
                    </td>
                    <td>倉庫名稱:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryStorage"
			               name="queryStorage"  
			              
			               result="${storageList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:180px"
			               >
			            </cafe:droplisttag>
                    </td>
                 </tr>
                 <tr>
                    
                 	<td>設備狀態:</td>
                    <td>
			            <cafe:droplisttag 
			               id="queryStatus"
			               name="queryStatus" 
			              
			               result="${assetStatusList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                    <td>合約編號:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryContractId"
			               name="queryContractId" 
			              
			               hasBlankValue="true"
			               result="${contractList}"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                    <td>設備已啟用:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryIsEnabled"
			               name="queryIsEnabled" 
			               
			               result="${isEnabled}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                </tr>
                <tr>
                    
                    <td>維護模式:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryMaType"
			               name="queryMaType" 
			              
			               result="${maTypeList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>                   
                	<td>執行作業:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryAction"
			               name="queryAction" 
			              
			               result="${actionList}"
			               hasBlankValue="true"
			               blankName="請選擇(複選)"
			               style="width:120px"
			               >
			              </cafe:droplisttag>
                    </td>
                    <td>資產Owner:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryAssetOwner"
			               name="queryAssetOwner" 
			             
			               result="${customers}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			              </cafe:droplisttag>
                    </td>
                </tr>
                <tr>
                	
                    <td>使用人:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryAssetUser"
			               name="queryAssetUser" 
			             
			               result="${customers}"
			               selectedValue="${!empty useCompany?useCompany:'' }"
			               hasBlankValue="true"
			               blankName="請選擇(複選)"
			               style="width:120px"
			               javascript="${!empty useCompany?'disabled=true':'' }"
			               >
			              </cafe:droplisttag>
			              <input id="hideQueryAssetUser" name="hideQueryAssetUser" type="hidden" value="${!empty useCompany?useCompany:'' }"/>
                    </td>
                	<td>完修日期:</td>
                    <td>
                        <input id="beforeTicketCompletionDate" name="beforeTicketCompletionDate" style="width: 120px"  >～
                
                        <input id="afterTicketCompletionDate" name="afterTicketCompletionDate"  style="width: 120px" >
                    </td>
                    <td>異動日期:</td>
                    <td>
                        <input id="beforeUpdateDate" name="beforeUpdateDate"   style="width: 120px" >～
                        <input id="afterUpdateDate" name="afterUpdateDate"  style="width: 120px" >
                    </td>
                </tr>
                <tr>
                    
                    <td>保管人:</td>
                		<td>
                    		<input id="queryKeeperName" name="queryKeeperName"  type="text" value=""></input></td>
                
                		<td>TID:</td>
                    <td>
                        <input id="queryTid" name="queryTid"  type="text"  value=""></input></td>
                        <td>DTID:</td>
                    <td>
                        <input id="queryDtid" name="queryDtid"  type="text" value=""></input></td>
                    
                </tr>
                <tr>	
                	
                    <td>銀聯:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryIsCup"
			               name="queryIsCup" 
			              
			               result="${isEnabled}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                
                		<td>特店代號 :</td>
                    <td>
                        <input id="queryMerchantCode" name="queryMerchantCode"  type="text" value=""></input></td>
                        <td>特店名稱:</td>
                    <td>
                        <input id="queryMerName" name="queryMerName"  type="text" value=""></input></td>
                    
                </tr>	
                <tr>	
                	
                    <td>特店表頭:</td>
                    <td>
                        <input id="queryHeaderName" name="queryHeaderName" type="text" value=""></input></td>
                	<td>裝機區域:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryArea"
			               name="queryArea" 
			             
			               result="${locationList}"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                    <td>櫃位:</td>
                	<td>
                		<input id="queryCounter" name="queryCounter"type="text" value=""></input>
                	</td>
                </tr>
                <tr>
                	
                	<td>箱號:</td>
                	<td>
                		<input id="queryCartonNo" name="queryCartonNo"  type="text" value=""></input>
                	</td>
                	<td>設備型號:</td>
                    <td>
                        <cafe:droplisttag 
			               id="queryModel"
			               name="queryModel" 
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:120px"
			               >
			            </cafe:droplisttag>
                    </td>
                	<td>歷史資料:</td><td><input id="history" name="history" type="checkbox"></input></td></tr>
            		
                </tr>
               <tr>
                    <td colspan="6"></td>
                    <td>
                        <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.QUERY.code )}"><a href="javascript:void(0)" id="btnQuery"  onclick="queryDevice(1,true)">查詢</a></c:if>
                    </td>
                    </tr>
            </table></form>
            <div><span id="msgAction" class="red">${msg }</span></div>
            <div>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.CARRY.code )}"><a href="javascript:void(0)" id="btnCarry" name="btnCarry" onclick="showActionView('carrier','${contextPath}');" disabled>領用</a></c:if> 
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.BORROW.code )}"><a href="javascript:void(0)" id="btnBorrow"  onclick="showActionView('borrower','${contextPath}');" disabled>借用</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.TOORIGINOWNER.code )}"><a href="javascript:void(0)" id="btnCarryback"  onclick="showActionView('carryBack','${contextPath}');" disabled>歸還</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.ASSETIN.code )}"><a href="javascript:void(0)" id="btnAssetin"  onclick="showActionView('assetIn','${contextPath}');" disabled>入庫</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.REPAIR2.code )}"><a href="javascript:void(0)" id="btnRepair2"  onclick="showActionView('repair','${contextPath}');" disabled>維修</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.SENDREPAIR.code )}"><a href="javascript:void(0)" id="btnSendrepair"  onclick="showActionView('repaired','${contextPath}');" disabled>送修</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.PENDINGDISABLE.code )}"><a href="javascript:void(0)" id="btnPendingdisabled"  onclick="showActionView('retire','${contextPath}');" disabled>待報廢</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.DISABLED.code )}"><a href="javascript:void(0)" id="btnDisabled"  onclick="showActionView('retired','${contextPath}');" disabled>報廢</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.BACK.code )}"><a href="javascript:void(0)" id="btnBack"  onclick="showActionView('back','${contextPath}');" disabled>退回</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.DESTROY.code )}"><a href="javascript:void(0)" id="btnDestroy"  onclick="showActionView('destroy','${contextPath}');" disabled>銷毀</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.OTHEREDIT.code )}"><a href="javascript:void(0)" id="btnOtheredit"  onclick="showActionView('','${contextPath}');" disabled>其他修改</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.UNBOUND.code )}"><a href="javascript:void(0)" id="btnUnbound"  onclick="showCancelView('cancel','${contextPath}');" disabled>解除綁定</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.TAIXINRENT.code )}"><a href="javascript:void(0)" id="btnTaixinrent"  onclick="showActionView('taixinRent','${contextPath}');" disabled>台新租賃維護</a></c:if>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.JDWMAINTENANCE.code )}"><a href="javascript:void(0)" id="btnJdwmaintenance"  onclick="showActionView('taixinRent','${contextPath}','jdw');" disabled>捷達威維護</a></c:if>
            	<c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.PRINT.code )}"><a href="javascript:void(0)" id="btnPrint"  onclick="exportBorrowData('${contextPath}')" disabled>列印借用單</a></c:if>
            	<c:if test="${fn:contains(userFunction, 'ADVANCED_WAREHOUSE')}"><a href="javascript:void(0)" id="btnExportBorrowList" class="easyui-linkbutton c6" onclick="exportBorrowList()">借用明細</a></c:if>
            </div>
            <div style="width: 98%; height: auto;display:none" id="carrier">
                <form id="carrierForm" method="post">
                	<table>
                		<tr>
                			<td>
                				領用人:<span class="red">*</span>
                				<cafe:droplisttag 
					               id="carryCode"
					               name="carryCode" 
					               
			               		   hasBlankValue="false"
					               style="width:150px"
					               >
			              </cafe:droplisttag> 
                				<label id="carryCnEnName"></label>
                				<input type="hidden" id="accountFlag"/>
								<input type="hidden" id="carryName"/>
								<input type="hidden" id="carrierShowFlag"/>
								<a href="javascript:void(0)" id="btn_asset_queryEmployee" onclick="queryEmployee()"></a>
								<input type="hidden" id="carryAccount"/>
								<a href="javascript:void(0)" id="btn_asset_searchEmployee" onclick="searchEmployee(this,'','${contextPath}')"></a>
                			</td>
                			
                		</tr>
                		<tr>
                			<td> 
                				領用日期:<input style="width: 120px" id="date" type="text"  value="" disabled></input>
								&nbsp;領用說明: &nbsp;<input style="width: 350px" id="carryComment" type="text" maxlength="200" value=""></input>
								<a href="javascript:void(0)" id="btn_asset_carry" onclick="carry('${contextPath}')">送出</a>
								<a href="javascript:void(0)" id="btn_asset_cancel" onclick="cancel()">取消</a>
                			</td>
                			
                		</tr>
                	</table>
               </form>
            </div>
       
            <div style="width: 98%; height: auto; display:none" id="borrower">
            <input type="hidden" id="borrowerShowFlag"/>
               <form id="borrowForm" method="post">
                  <table cellpadding="4">
                   
                    <tr>
                    	<td>借用人:<span class="red">*</span>&nbsp;</td>
                        <td colspan="8">	
                        	<cafe:droplisttag 
					               id="borrows"
					               name="borrows" 
					              
			               		   hasBlankValue="false"
					               style="width:150px"
					               >
		    				</cafe:droplisttag>
                        	<lable id="borrowsName"> </lable>
                        	<a href="javascript:void(0)" id="btn_asset_queryEmployee_borrow"  onclick="queryEmployee()"></a>
                        	<a href="javascript:void(0)" id="btn_asset_searchEmployee_borrow" onclick="searchEmployee(this,'','${contextPath}')"></a>&nbsp;
                        </td>
                    </tr>
                    <tr>
                    	<td>借用日期:<span class="red">*</span></td>
                        <td width="280px">
                            <input style="width: 120px" id="borrowerStartDate" name="borrowerStartDate"  type="text" value="" disabled></input>~
							<input id="borrowerEnd" name="borrowerEnd" maxlength="10" style="width: 120px" />
                        		<input type="hidden" id="borrowerStart" name="borrowerStart">
                        </td>
                        <td>借用人郵箱:<span class="red">*</span></td>
                        <td colspan="6">
                            <input id="borrowerEmail" name="borrowerEmail" style="width: 210px" maxlength="50"></input>
                        		<input id="userId" name="userId" type="hidden"/>
                        		<input id="userName" name="userName" type="hidden"/>
                        </td>
                    </tr>
                    <tr>
                        <td>借用人主管郵箱:<span class="red">*</span></td>
                        <td>
                            <input id="borrowerMgrEmail" name="borrowerMgrEmail" style="width: 210px" maxlength="50"type="text">
                        </td>
                        <td>借用說明:<span class="red">*</span></td>
                        <td colspan="3">
                            <input id="borrowerComment" name="borrowerComment" style="width: 300px" type="text" maxlength="200" value=""></input>
                        </td><input type="hidden" id="borrowFlag" value="1">
                        <td colspan="3" style="text-align: right;">
                            <a href="javascript:void(0)" onclick="exportBorrow('${contextPath}')" id="button_borrow_export" >列印借用單</a>
                            <a href="javascript:void(0)"  id="button_borrow_out" onclick="borrow('${contextPath}')">送出</a>
                            <a href="javascript:void(0)"  id="button_borrow_cancel" onclick="cancel()">取消</a>
                        	<input type="hidden" id="queryAssetIdList" name="queryAssetIdList"/>
                        	<input type="hidden" id="actionId" name="actionId" />
							<input type="hidden" id="serviceId" name="serviceId" />
							<input type="hidden" id="useCaseNo" name="useCaseNo" />
							<input type="hidden" id="borrowerName" name="borrowerName" />
                        </td>
                        
                    </tr>
                    </table>
                </form>
            </div>
        
			<div style="width: 98%; height: auto; display:none" id="assetIn" >
			<input type="hidden" id="assetInShowFlag"/>
                <form id="assetInForm">說明:<input id="assetInComment" style="width: 350px" maxlength="200" type="text" value=""></input>
                <a href="javascript:void(0)" id="asset_asset_To_In" onclick="assetIn('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_asset_To_cancel" onclick="cancel()">取消</a></form>
            </div>
            <div style="width: 98%; height: auto; display:none" id="carryBack">
            <input type="hidden" id="carryBackShowFlag"/>
                <form id="carryBackForm">歸還說明:<input id="carryBackComment" style="width: 350px" type="text" maxlength="200" value=""></input>
                <a href="javascript:void(0)" id="asset_back" onclick="back('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_back_cancel" onclick="cancel()">取消</a></form>
            </div>
         
            <div style="width: 98%; height: auto; display:none" id="repair" >
            <input type="hidden" id="repairShowFlag"/>
                <form id="repairForm">維修廠商:<span class="red">*</span>
                	<cafe:droplisttag 
			               id="repairVendor"
			               name="repairVendor" 
			               hasBlankValue="true"
			               blankName="請選擇"
			               result="${repairdVendorList}"
			               style="width: 150px"
			              >
			    </cafe:droplisttag>故障組件:<span class="red">*</span>
                <cafe:droplisttag 
			               id="editFaultComponent"
			               name="editFaultComponent" 
			               hasBlankValue="true"
			               blankName="請選擇(複選)"
			               result="${faultComponentList}"
			               style="width: 150px"
			               >
			    </cafe:droplisttag>故障現象:<span class="red">*</span>
			    <cafe:droplisttag 
			               id="editFaultDescription"
			               name="editFaultDescription"
			               hasBlankValue="true"
			               blankName="請選擇(複選)"
			               result="${faultDescriptionList}"
			               style="width: 150px"
			               >
			    </cafe:droplisttag>說明/排除方式:
			    <input id="repairComment" style="width: 150px" type="text" maxlength="200" ></input>
                <a href="javascript:void(0)" id="asset_repair_fault" onclick="fault('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_repair_cancel" onclick="cancel()">取消</a></form>
            </div>  
                  
            <div style="width: 98%; height: auto; display:none" id="retire">
                <form id="retireForm">報廢原因:<span class="red">*</span>
                <cafe:droplisttag 
			               id="retireReason"
			               name="retireReason" 
			               hasBlankValue="true"
			               blankName="請選擇"
			               result="${retireReasonList }"
			               style="width: 150px"
			               >
			    </cafe:droplisttag>
                說明:<input id="retireComment" style="width: 350px" type="text" maxlength="200"></input>
                <a href="javascript:void(0)" id="asset_retire_retireReason" onclick="retireReasons('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_retire_cancel" onclick="cancel()">取消</a></form>
            </div>
           
           
            <div style="width: 98%; height: auto; display:none" id="retired">
            <input type="hidden" id="retiredShowFlag"/>
                <form id="retiredForm">說明:<input id="retiredComment" style="width: 350px" maxlength="200" type="text" value=""></input>
                <a href="javascript:void(0)" id="asset_retired"  onclick="retired('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_retiredCancel" onclick="cancel()">取消</a></form>
            </div>
            <div style="width: 98%; height: auto; display:none" id="back">
             <input type="hidden" id="backShowFlag"/>
                <form id="backForm">說明:<input id="backComment" style="width: 350px"type="text" maxlength="200" value=""></input>
                <a href="javascript:void(0)" id="asset_backReturnBack" onclick="returnBack('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_backCancel"  onclick="cancel()">取消</a></form>
            </div>
            <div style="width: 98%; height: auto; display:none" id="destroy" >
            <input type="hidden" id="destroyShowFlag"/>
                <form id="destroyForm" method="post" novalidate>說明:<input id="destroyComment" style="width: 350px" maxlength="200" type="text" value=""></input>
                <a href="javascript:void(0)" id="asset_destroy" onclick="destroy('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_destroyCancel" onclick="cancel()">取消</a></form>
            </div>
           
             <div style="width: 98%; height: auto; display:none" id="repaired" >
             <input type="hidden" id="repairedShowFlag"/>
                <form id="repairedForm" method="post" novalidate>原廠: <span class="red">*</span>
                <cafe:droplisttag 
			               id="repairedVendor"
			               name="repairedVendor" 
			               result="${repairVendorList }"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width: 150px"
			               >
			    </cafe:droplisttag>
                &nbsp;
                說明: &nbsp;<input id="repairedComment" maxlength="200" style="width: 350px" type="text" value=""></input>
                <a href="javascript:void(0)" id="asset_repaired" onclick="repaired('${contextPath}')">送出</a>
                <a href="javascript:void(0)" id="asset_repaired_cancel"onclick="cancel()">取消</a>
                </form>
            </div>
            <div style="width: 98%; height: auto; display:none" id="cancel">
            <input type="hidden" id="cancelShowFlag"/>
                 <form id="cancelForm" method="post" novalidate>
                 <table style="width: 100%">
                		<tr>
                			<td style="width: 20%">
		                	原因:<span class="red">*</span>&nbsp;
		                   		<div id="removeOrLossReason" style="display:inline" class="div-list" data-list-required='請輸入原因'>
									<cafe:checklistTag 
										name="removeOrLossStatus" 
										id="removeOrLossStatus" 
										type="radio"
										result="${removeOrLossList}"
										>
									</cafe:checklistTag>
								</div>
						</td>
						<td style="width: 42%">
                		說明:&nbsp;<input id="removeOrLossDescription" maxlength="200" style="width: 350px" type="text" value=""></input>
		       			</td>
		       			<td style="width: 18%">
		       			完修日期:<input id="caseCompletionDate" name="caseCompletionDate" style="width: 120px" maxlength="10">
		       			</td>   
		       		</tr>  
		       		<tr>
		       			<td>   
			               	 維護廠商:<cafe:droplisttag 
						               id="maintenanceVendor"
						               name="maintenanceVendor" 
						               result="${repairdVendorList}"
						               hasBlankValue="true"
						               blankName="請選擇"
						               style="width: 150px"
						               >
						    </cafe:droplisttag>
					    </td>
					    <td><input id="maintenanceUserId" name="userId" type="hidden"/>
					    <input id="hideMaintenanceUserCode" name="userId" type="hidden"/>
                        		<input id="maintenanceUserName" name="userName" type="hidden"/>
			   			 維護工程師:<input style="width: 150px" id="maintenanceUserCode" maxlength="50" type="text" value=""></input>
			    			<label id="maintenanceCnEnName"></label>
			    			<a href="javascript:void(0)" id="btn_asset_queryMaintenanceUser" onclick="queryEmployee('maintenanceUser')"></a>
							<a href="javascript:void(0)" id="btn_asset_searchMaintenanceUser" onclick="searchEmployee(this,'maintenanceUser','${contextPath}')"></a>
               			</td>
               			<td>
               			簽收日期:<input id="analyzeDate" name="analyzeDate"  style="width: 120px" maxlength="10">
               			</td>
               			<td style="text-align: left">
               				<a href="javascript:void(0)" id="analyzeConfirm" onclick="cancelRemoveOrLoss('${contextPath}')">送出</a>
                			<a href="javascript:void(0)" id="analyzeCancel" onclick="cancel()">取消</a>
               
               			</td>
               </tr>
               </table>
                </form>
            </div>
            <div style="width: 98%; height: auto; display:none" id="taixinRent">
            <input type="hidden" id="taixinRentShowFlag"/>
                <form id="taiXinForm" method="post" novalidate>
                <table border="0" cellspacing="1" style="width: 100%;">
                    <tr>
                        <td width="5%">DTID:<span class="red">*</span>
                        </td>
                        <td width="14%"> 
                            <input id="taixinDtid" name="taixinDtid" style="width: 150px"  maxlength="8" type="text" value=""></input>
                        </td>
                        <td width="7%">案件編號:<span class="red">*</span></td>
                        <td width="13%">
                            <input id="taixinCaseId" style="width: 150px" maxlength="15" type="text" value=""></input></td>
                        <td width="4%">特店代號:<span class="red">*</span>
                        </td>
                        <td width="18%" colspan="2">
                           <input id="taixinMid" style="width: 150px"maxlength="50" type="text" value=""></input>
                           <a href="javascript:void(0)" id="asset_ok_searchMid" onclick="searchMid(false)"></a>
                           <a href="javascript:void(0)" id="asset_search_openDialogMID" onclick="openDialogMID('${contextPath}')"></a>
                           <a href="javascript:void(0)" id="asset_search_editMID" onclick="editAssetMid()"></a>
                        	<input type="hidden" id="taixinmerId"/>
                        	<input type="hidden" id="taixinCompanyName"/>
                        	<input type="hidden" id="taixinCompanyId"/>
                        	<input type="hidden" id="hidTaixinMid"/>
                        	<input type="hidden" id="hidJdwFlag"/>
                        </td>
                    </tr>
                    <tr>
                        <td>特店名稱:<span class="red">*</span>
                        </td>
                        <td>
                            <input id="taixinName" style="width: 150px" type="text" value="" disabled="disabled"></input>
                        </td>
                        <td>表頭（同對外名稱）:<span class="red">*</span>
                        </td>
                        <td>
							<cafe:droplisttag 
								id="taixinHeader"
								name="taixinHeader" 
								hasBlankValue="true"
								blankName="請選擇"
								style="width: 150px"
								>
							</cafe:droplisttag>
							<a href="javascript:void(0)" id="asset_add_taixinHeader" onclick="addTaixinHeader('${contextPath}');"></a>
							<a href="javascript:void(0)" id="asset_edit_taixinHeader" onclick="editTaixinHeader('${contextPath}');"></a>
						</td>
						<td>維護廠商:<span class="red">*</span>
						</td>
						<td colspan="2">
							<cafe:droplisttag 
								id="taixinVendor"
								name="taixinVendor" 
								hasBlankValue="true"
								blankName="請選擇"
								result="${vendorList}"
								style="width: 150px"
								>
							</cafe:droplisttag>
                        </td>
                    </tr>
                    <tr>
               			<td>
               				設備啟用日:
               			</td>
               			<td><input id="isEnableDate" name="isEnableDate" style="width: 120px" maxlength="10"></td>
                    	<td> 維護工程師:</td>
                        <td colspan="3">   
                         	<input id="taixinMaintenanceUserId" name="userId" type="hidden"/>
                        	<input id="taixinMaintenanceUserName" name="userName" type="hidden"/>
			   				<input style="width: 150px" id="taixinMaintenanceUserCode" maxlength="50" type="text" value=""></input>
			    			<label id="taixinMaintenanceCnEnName"></label>
			    			<a href="javascript:void(0)" id="btn_asset_queryTaixin_MaintenanceUser"  onclick="queryEmployee('taixinMaintenanceUser')"></a>
							<a href="javascript:void(0)" id="btn_asset_searchTaixin_MaintenanceUser"  onclick="searchEmployee(this,'taixinMaintenanceUser','${contextPath}')"></a>
               			</td>
                    </tr>
                     <tr>
                     	<td>
		       				完修日期:</td>
		       				<td><input id="taixinCaseCompletionDate" name="taixinCaseCompletionDate" style="width: 120px" maxlength="10" >
		       			</td> 
                    	<td>
               				簽收日期:</td>
               				<td><input id="taixinAnalyzeDate" name="taixinAnalyzeDate" style="width: 120px" maxlength="10" >
               			</td>
                   </tr>
                    <tr>
                         <td>裝機地址: <span class="red">*</span>
                        </td>
                      
                        <td colspan="3">
                             <input id="taixinSameAddress" name="taixinSameAddress" checked="checked" onclick="merInstallAddressChecked()" type="checkbox" />同營業地址
                            <cafe:droplisttag 
				               id="taixinLocation"
				               name="taixinLocation" 
				               hasBlankValue="true"
				               style="width: 90px"
				               blankName="請選擇"
				               result="${locationList}"
				               >
			    			</cafe:droplisttag>
                            <input id="taixinAddress" maxlength="100"  style="width: 250px">
                        </td>
                      
                        <td>銀聯:</td>
                        <td>
                            <input id="taixinIscup" name="taixinIscup" type='checkbox' />
       				</td>            
                   <td style="text-align: left">
                            <a href="javascript:void(0)" id="asset_taixin_taixinAction" onclick="taixinAction('${contextPath}')">送出</a>
                            <a href="javascript:void(0)" id="asset_taixin_cancel" onclick="cancel()">取消</a>
                        </td>
                    </tr>
                </table>
                </form>
            </div>
            
            <div style="width: 97%;text-align: right;padding: 10px;">
           <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.CONFIRMSEND.code )}"><span id="btnConfirmsend" > 通知人員：<cafe:droplisttag 
		                				id="toMail" 
										name="toMail" 
										
										hasBlankValue="true" 
										blankName="請選擇(複選)"
										style="width:138px" 
										javascript="disabled">
									</cafe:droplisttag>
		                    <a href="javascript:void(0)" id="confirmSend"  onclick="sendMail('${contextPath}')" disabled> 確認通知</a>&nbsp;&nbsp;</span></c:if>
		                	
                <a href="javascript:void(0)" style="width: 150px" id="filterBlank" onclick="filterBlank(this)">欄位</a>
                <c:if test="${fn:contains(formDTO.logonUser.accRghts[ucNo], functionType.EXPORT.code )}"><a href="javascript:void(0)" style="width: 150px" id="btnExport" onclick="exportData()">匯出</a></c:if>
            </div>
            <table id="dataGrid"  style="width: 98%; height: auto"title="庫存清單 &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input id='queryAll' name='queryAll' type='checkbox'></input> <span style='color: black;'>批量處理 </span> ">
                
            </table>
           
            
            <div id="exportBlank" style="display:none">
				<div id="exportList"  style="width:200px;height:340px">
				</div>
				<div id="exportToolbarAll">
					<input id="exportCheckAll" type="checkbox" checked="false"/> 全選
				</div>
				<input id="showExportListFlag" type="hidden"/>
			</div>
            <div id="dlg" fit="true"></div>
            <div id="dlgg" fit="true"></div>
            <div id="dlvSearch"></div>
            <div id="dlvMid"></div>
            <div id="merchanAddDlg"></div>
            <div id="merchanAddHeaderDlg"></div>
            <div id="dlgQ" style="display:none; padding: 10px 10px">
                <form id="dlgQForm"><div class="ftitle">多筆輸入</div>
                    輸入格式:<br />123456789<br />
                    456789012
                    <input id="serialNumbers" setting='fdfd' name="serialNumbers" type="text"  style="height: 200px;width:350px" value="">
                    <div id="dlgQ-buttons" style="text-align: right;">
                      <a href="javascript:void(0)" id="serialNumber_linkbutton_comfirm" onclick="serialNumberInput()" style="width: 90px">確定</a>
                   <a href="javascript:void(0)" id="serialNumber_linkbutton_cancel" onclick="serialNumberClose()" style="width: 90px">取消</a>
             </div>
				</form>
			</div>
            <div id="dlgInput" style="display:none; padding: 10px 10px">
                <form id="dlgInputForm"><div class="ftitle">多筆輸入</div>
                輸入格式:<br/>123456789<br/>
                456789012
                		<input name="propertyIds" id="propertyIds" style="height: 200px;width:350px">
                	<div id="dlgQ-button" style="text-align: right;">
                    <a href="javascript:void(0)" id="propertyId_linkbutton_comfirm" onclick="propertyIdInput()" style="width: 90px">確定</a>
                    <a href="javascript:void(0)" id="propertyId_linkbutton_cancel" onclick="propertyIdClose()" style="width: 90px">取消</a>
                </div></form>
            </div>
		</div>
		
		<form id="zipForm" method="post">
			<input type="hidden" id="actionId" name="actionId" />
			<input type="hidden" id="serviceId" name="serviceId" />
			<input type="hidden" id="useCaseNo" name="useCaseNo" />
			<input type="hidden" id="queryAssetIds" name="queryAssetIds"/>
		</form>
     </div>
</div>
<script type="text/javascript">
$(function () {
   	//批量處理按鈕全選
   	$("#queryAll").click(function(){
	   	if($(":checkbox[name=queryAll]").is(":checked")) {
	   		$("#isSelectedAll").val("1");
	   		$('#dataGrid').datagrid('selectAll');
	   	} else {
	   		$("#isSelectedAll").val("0");
	   	}
   	});
   	$("#history").click(function(){
   		//需要判斷 掃碼槍查詢之後改變$("#history")的值 ，然後進行的是匯出還是掃碼槍 查詢
   		//預設隱藏域值為0，當掃碼槍掃描  將其更新為1，當其為1時，點擊$("#history") 將值改為2，如果值為2，掃碼槍查詢將其改為1，清除掃碼槍隱藏域相關值。 2018/01/03
	   	if($("#hideHistoryChangeFlag").val()=='1'){
	   		$("#hideHistoryChangeFlag").val('2');
	   	}
   	});
}); 

/**
 * 解除綁定畫面加載
 */
function openCancelAction(){
	if($('#cancelShowFlag').val()!='Y'){
		$('#cancel').attr("name","edit");
		$('#cancel').css("display","");
		$('#cancel').panel({      
		  	title: '解除綁定作業',    
		 	closed: true   
		});
		$('#removeOrLossDescription').textbox({ validType:'maxLength[200]'});
		$('#removeOrLossDescription').textbox('textbox').attr('maxlength', 200);	
		$('#caseCompletionDate').datebox({
		    validType:['date[\'完修日期格式限YYYY/MM/DD\']'],
		});
		$('#caseCompletionDate').datebox('textbox').attr('maxlength', 10);
		   $('#maintenanceVendor').combobox({ 
		    	editable:false,  
			    valueField:'value',
			    textField:'name',
			   	
		      	onChange : function(newValue,oldValue){
	   				$("#maintenanceUserCode").textbox('setValue','');
	   				$("#maintenanceCnEnName").html('');
	   			}   
			});
		$('#maintenanceVendor').addClass("easyui-combobox");
			
		$('#maintenanceUserCode').textbox({ validType:'maxLength[50]'});
		$('#maintenanceUserCode').textbox('textbox').attr('maxlength', 50);	
		
		$('#btn_asset_queryMaintenanceUser').linkbutton({iconCls: 'icon-ok' });  
		$('#btn_asset_searchMaintenanceUser').linkbutton({iconCls: 'icon-search'}); 
		
		$('#analyzeDate').datebox({
		    validType:['date[\'簽收日期格式限YYYY/MM/DD\']'],
		});
		$('#analyzeDate').datebox('textbox').attr('maxlength', 10);
		
		$('#analyzeConfirm').linkbutton({});  
		$('#analyzeCancel').linkbutton({}); 
		$('#cancelShowFlag').val('Y'); 
	}	
}

/**
 * 維修作業畫面加載
 */
function openRepairAction(){
	if($('#repairShowFlag').val()!='Y'){
		$('#repair').attr("name","edit");
		$('#repair').css("display","");
		$('#repair').panel({      
		  	title: '維修作業',    
		 	closed: true   
		});  
		 
	   $('#repairVendor').combobox({ 
	    	editable:false,  
		    required:true,
		    valueField:'value',
		    textField:'name',
		   
		    validType:'ignore[\'請選擇\']',
	      	invalidMessage:"請輸入維修廠商"     
		});
		$('#repairVendor').addClass("easyui-combobox");
		
		$('#editFaultComponent').combobox({
			editable:false, 
			required:true,
			multiple:true,
			valueField:'value',
		    textField:'name',

		    validType:'ignore[\'請選擇(複選)\']',
	      	invalidMessage:"請輸入故障組件" ,
	      	onSelect : function (newValue) {
            	selectMultiple(newValue, "editFaultComponent")
        	},
        	onUnselect : function (newValue) {
   				unSelectMultiple(newValue, "editFaultComponent")
        	},
		});
		$('#editFaultComponent').addClass("easyui-combobox");

		$('#editFaultDescription').combobox({
			editable:false, 
			required:true,
			valueField:'value',
		    textField:'name',
			multiple:true,
			
			validType:'ignore[\'請選擇(複選)\']',
	      	invalidMessage:"請輸入故障現象" ,
	      	onSelect : function (newValue) {
            	selectMultiple(newValue, "editFaultDescription")
        	},
        	onUnselect : function (newValue) {
   				unSelectMultiple(newValue, "editFaultDescription")
        	},
		}); 
		$('#editFaultDescription').addClass("easyui-combobox");
		$('#repairComment').textbox({ validType:'maxLength[200]'});
		$('#repairComment').textbox('textbox').attr('maxlength', 200);	
		
		$('#asset_repair_fault').linkbutton({});  
		$('#asset_repair_cancel').linkbutton({}); 
		$('#repairShowFlag').val('Y');    
	} 		
}
/**
 * 待報廢作業畫面加載
 */
function opentRetireAction(){
	if($('#retireShowFlag').val()!='Y'){
		$('#retire').attr("name","edit");
		$('#retire').css("display","");
		$('#retire').panel({      
		  	title: '待報廢作業',    
		 	closed: true   
		});
        $('#retireReason').combobox({ 
	    	editable:false,  
		    required:true,
		    valueField:'value',
		    textField:'name',
		   
		    panelHeight:'auto',
		    validType:'ignore[\'請選擇\']',
	      	invalidMessage:"請輸入報廢原因"     
		}); 
		$('#retireReason').addClass("easyui-combobox");
        $('#retireComment').textbox({validType:'maxLength[200]'});
		$('#retireComment').textbox('textbox').attr('maxlength', 200);
		$('#asset_retire_retireReason').linkbutton({});  
		$('#asset_retire_cancel').linkbutton({}); 
		$('#retireShowFlag').val('Y');    
	} 
}
/**
 * 送修作業畫面加載
 */ 
function openRepairedAction(){
	if($('#repairedShowFlag').val()!='Y'){
		$('#repaired').attr("name","edit");
		$('#repaired').css("display","");
		$('#repaired').panel({      
		  	title: '送修作業',    
		 	closed: true   
		});
		
        $('#repairedVendor').combobox({ 
	    	editable:false,  
		    required:true,
		    valueField:'value',
		    textField:'name',
		   
		    validType:'ignore[\'請選擇\']',
	      	invalidMessage:"請輸入原廠"     
		});  
		$('#repairedVendor').addClass("easyui-combobox");
        $('#repairedComment').textbox({validType:'maxLength[200]'});
		$('#repairedComment').textbox('textbox').attr('maxlength', 200);
		$('#asset_repaired').linkbutton({});  
		$('#asset_repaired_cancel').linkbutton({}); 
		$('#repairedShowFlag').val('Y');  
	} 
}
/**
 * 台新作業畫面加載
 */
function openTaixinRentdAction(){
	//CR #2696 增加捷達威維護 2017/10/25
	var title = '台新租賃維護';
	if($("#hidJdwFlag").val() == 'jdw'){
		title = '捷達威維護';
	}
	$('#taixinRent').panel({      
	  	title: title,    
	 	closed: true   
	});
	$('#taixinRent').attr("name","edit");
	$('#taixinRent').css("display","");
	if($('#taixinRentShowFlag').val()!='Y'){
        $('#taixinDtid').textbox({
         	required:true, 
        	validType:['englishOrNumber[\'DTID限英數字，請重新輸入\']','maxLength[8]'],
         	missingMessage:"請輸入DTID",
         	invalidMessage:"DTID限英數字，請重新輸入"
        });
        //加class 調用自定義驗證的時候 如果不加 順序會亂
        $('#taixinDtid').addClass("easyui-textbox");
		$('#taixinDtid').textbox('textbox').attr('maxlength', 8);
		
		$('#taixinCaseId').textbox({
            	required:true, 
           		validType:'maxLength[15]',
            	missingMessage:"請輸入案件編號"
        });
        //加class 調用自定義驗證的時候 如果不加 順序會亂
        $('#taixinCaseId').addClass("easyui-textbox");
		$('#taixinCaseId').textbox('textbox').attr('maxlength', 15);	
		$('#taixinMid').textbox({
            	required:true, 
           		validType:'maxLength[50]',
            	missingMessage:"請輸入特店代號"
            });
		$('#taixinMid').textbox('textbox').attr('maxlength', 50);
		//加class 調用自定義驗證的時候 如果不加 順序會亂
		$('#taixinMid').addClass("easyui-textbox");
		$('#asset_ok_searchMid').linkbutton({    
		    iconCls: 'icon-ok'   
		}); 
		$('#asset_search_openDialogMID').linkbutton({    
		    iconCls: 'icon-search'   
		}); 
		$('#asset_search_editMID').linkbutton({
			iconCls: 'icon-edit'
		});
		$('#asset_add_taixinHeader').linkbutton({
			iconCls: 'icon-add'
		});
        $('#asset_edit_taixinHeader').linkbutton({
			iconCls: 'icon-edit'
		});
		$('#taixinName').textbox({
            	required:true, 
            	missingMessage:"請輸入特店名稱"
        });
        //加class 調用自定義驗證的時候 如果不加 順序會亂
		$('#taixinMid').addClass("easyui-textbox");
		
        $('#taixinHeader').combobox({
			editable:false, 
			required:true,
			valueField:'value',
			textField:'name',
			validType:'ignore[\'請選擇\']',
	      	invalidMessage:"請輸入表頭（同對外名稱）" ,
	      	onChange : function(newValue,oldValue){
	      		//特店表頭改變時，清空裝機地址
	   			//$("#taixinSameAddress").prop("checked", false);
	      		merInstallAddressChecked();
	   			/* $("#taixinAddress").textbox('enable');
	   			$("#taixinLocation").combobox('enable'); */
	   			/* $("#taixinLocation").combobox('readonly', false);
	   			$("#taixinAddress").textbox('readonly', false);
	   			$("#taixinAddress").textbox('setValue', '');
	   			$("#taixinLocation").combobox('setValue', ''); */
   			}
		}); 
        $('#taixinHeader').addClass("easyui-combobox");
		$('#taixinVendor').combobox({
			editable:false, 
			required:true,
			valueField:'value',
			textField:'name',
			
			validType:'ignore[\'請選擇\']',
	      	invalidMessage:"請輸入維護廠商" ,
	      	onChange : function(newValue,oldValue){
   				$("#taixinMaintenanceUserCode").textbox('setValue','');
   				$("#taixinMaintenanceCnEnName").html('');
   			}
		}); 
		$('#taixinVendor').addClass("easyui-combobox");
		
		$('#isEnableDate').datebox({
		    validType:['date[\'設備啟用日格式限YYYY/MM/DD\']'],
		});
		$('#isEnableDate').addClass("easyui-datebox");
		$('#isEnableDate').datebox('textbox').attr('maxlength', 10);
		
		$('#taixinMaintenanceUserCode').textbox({
       		validType:'maxLength[50]',
        });
		$('#taixinMaintenanceUserCode').textbox('textbox').attr('maxlength', 50);
		
		$('#btn_asset_queryTaixin_MaintenanceUser').linkbutton({    
		    iconCls: 'icon-ok'   
		}); 
		$('#btn_asset_searchTaixin_MaintenanceUser').linkbutton({    
		    iconCls: 'icon-search'   
		}); 
		
		$('#taixinCaseCompletionDate').addClass("easyui-datebox");
		$('#taixinCaseCompletionDate').datebox({
		    validType:['date[\'完修日期格式限YYYY/MM/DD\']'],
		});
		$('#taixinCaseCompletionDate').datebox('textbox').attr('maxlength', 10);
		$('#taixinAnalyzeDate').datebox({
		    validType:['date[\'簽收日期格式限YYYY/MM/DD\']'],
		});
		$('#taixinAnalyzeDate').addClass("easyui-datebox");
		$('#taixinAnalyzeDate').datebox('textbox').attr('maxlength', 10);
		$('#taixinLocation').combobox({
			editable:false, 
			required:true,
			valueField:'value',
		    textField:'name',
		  
			validType:'ignore[\'請選擇\']',
	      	invalidMessage:"請輸入裝機地址-縣市" 
		});  
		$('#taixinLocation').addClass("easyui-combobox");
		$('#asset_taixin_taixinAction').linkbutton({});  
		$('#asset_taixin_cancel').linkbutton({}); 
		
		$('#taixinAddress').textbox({
       		validType:'maxLength[100]',
       		required:true,
       	 	missingMessage:"請輸入裝機地址",
       		invalidMessage:"請輸入裝機地址" 
        });
        //位於驗證最後一個 不需要加addclass 如果之後后增加需要驗證的欄位 則要放開註釋 不然順序會亂
		//$('#taixinAddress').addClass("easyui-textbox");
		$('#taixinAddress').textbox('textbox').attr('maxlength', 100);
		showDate('taixin');
		$('#taixinRentShowFlag').val('Y');	
		$("#taixinSameAddress").prop("checked", true);
	}
	if ($("#hidJdwFlag").val() == 'jdw') {
		$('#asset_search_editMID').hide();
		$('#asset_add_taixinHeader').hide();
		$('#asset_edit_taixinHeader').hide();
	} else {
		$('#asset_search_editMID').show();
		$('#asset_add_taixinHeader').show();
		$('#asset_edit_taixinHeader').show();
	}
}
/**
 * 打開解除綁定作業畫面
 */
function showCancelView(id, contextPath) {
	//清空提示消息
	$('#msgAction').empty(); 
	$('div[name=edit]').panel('close');
	$("#taixinMaintenanceUserId").val('');
	$("#maintenanceUserId").val('');
	$("#userId").val('');
	$("#removeOrLossReason").removeClass("div-tips").addClass("div-list").tooltip("destroy");
	//調用驗證
	if(openActionValidate(id)){
		 if (id == 'cancel') {
			openCancelAction();
			
			$("#maintenanceVendor").combobox('setValue', '');
			$("#maintenanceUserCode").textbox('setValue', '');
			$("#hideMaintenanceUserCode").val("");
			$("#maintenanceCnEnName").html('');
			$("#analyzeDate").datebox('setValue', '');
			$("#caseCompletionDate").datebox('setValue', '');
			//解除綁定作業
			$("#removeOrLossDescription").textbox('setValue', '');
			$("#removeOrLossReason").html('<cafe:checklistTag name="removeOrLossStatus" id="removeOrLossStatus" type="radio" result="${removeOrLossList}"></cafe:checklistTag>');
			checkedDivListOnchange();
			//隱藏提示驗證消息
			hideDivListValidate();
			//需要聚焦 則手動加滾動事件
			$("#removeOrLossReason").removeClass("div-list").addClass("div-tips").tooltip("show");
			var message = $("#removeOrLossReason").attr("data-list-required");
			setTimeout(function () {
				$("#assetManageDivId").bind("scroll.validate",function(event){			
					$("#removeOrLossReason").tooltip("destroy");
					$("#removeOrLossReason").tooltip({    
						position: 'right',   
						content: '<span style="color:#000">' + message + '</span>', 
						onShow: function(){
							$(this).tooltip('tip').css({
								backgroundColor: 'rgb(255,255,204)',
								borderColor: 'rgb(204,153,51)'
							});
						}   
					});
					$("#assetManageDivId").unbind("scroll.validate");
				}); 
			}, 500);
			$("#assetManageDivId").unbind("scroll.validate"); 
			$('#accountFlag').val('maintenanceUser');
			//打開作業畫面
			$('#' + id).panel('open');
			$("[name=removeOrLossStatus]").first().focus();
		}
	} 
}
//格式化詳情按鈕
function formatToDetail(val, row, index){
    if(val !=null){
    		historyId = row.historyId;
    		return "<a href='javascript:void(0)'name='btnEdit' onclick='detail(\"" + val + '\",\"' + historyId + '\",\"' +index + '\",\"' + "${contextPath}" + "\")'> </a>";
    }
}

/**
 * 查詢庫存清單
 */
function queryDevice(pageNum, hidden, param, codeGunFlag){
	$("#msgAction").empty();
	var f = $("#exportForm");
	$("#removeOrLossReason").removeClass("div-tips").addClass("div-list").tooltip("destroy");
	$("div[name=edit]").panel('close');
	var sortFlag = true;
	//如果是掃碼槍查詢則按掃描順序排列，不可點擊排序
	if(typeof(codeGunFlag)!="undefined"){
		sortFlag = false;
	} else {
		sortFlag = true;
	}
	if (f.form('validate')) {
		if ($(":checkbox[name=history]").is(":checked")) {
			$("#btnCarry").linkbutton("disable");
			$("#btnBorrow").linkbutton("disable");
			$("#btnCarryback").linkbutton("disable");
			$("#btnAssetin").linkbutton("disable");
			$("#btnRepair2").linkbutton("disable");
			$("#btnPendingdisabled").linkbutton("disable");
			$("#btnDisabled").linkbutton("disable");
			$("#btnBack").linkbutton("disable");
			$("#btnDestroy").linkbutton("disable");
			$("#btnOtheredit").linkbutton("disable");
			$("#btnPrint").linkbutton("disable");
			$("#btnUnbound").linkbutton("disable");
			$("#btnTaixinrent").linkbutton("disable");
			$("#btnSendrepair").linkbutton("disable");
			$("#btnJdwmaintenance").linkbutton("disable");
		} else {
		}
		//cr2232
		var queryParam = null;
		if(typeof(param)!="undefined") {
			queryParam = param;
			//如果是掃碼槍掃描查詢 先查詢得到數量，計算顯示頁數
			if(typeof(codeGunFlag)!="undefined"){
				/* javascript:dwr.engine.setAsync(false);
				//將查詢條件轉為json字符串
				var str = JSON.stringify(queryParam);
				ajaxService.getCountByAsset(str, function(data){
					if (data !=null) {
						queryParam.totalSize = parseInt(data);
						var opt = $('#dataGrid').datagrid('getPager').data("pagination").options;
						var total = queryParam.totalSize%opt.pageSize > 0 ? queryParam.totalSize/opt.pageSize + 1 : queryParam.totalSize/opt.pageSize;
						pageNum = parseInt(total);
					} 
				});
				javascript:dwr.engine.setAsync(true); */
				var isError = false;
				if (queryParam.actionId =='history') {
				 	queryParam.queryHistory = '1';
				}
				var str = JSON.stringify(queryParam);
				$.ajax({
					url: "${contextPath}/assetManage.do?actionId=getCountByAsset",
					async: false,
					data: {queryParam:str},
					type:'post', 
					cache:false, 
					dataType:'json', 
					success:function(json){
						if (json.success) {
							if (json.total !=null) {
								var data = json.total;
								queryParam.totalSize = parseInt(data);
								var opt = $('#dataGrid').datagrid('getPager').data("pagination").options;
								var total = queryParam.totalSize%opt.pageSize > 0 ? queryParam.totalSize/opt.pageSize + 1 : queryParam.totalSize/opt.pageSize;
								pageNum = parseInt(total);
							}
						} else {
							isError = true;
						}
					},
					error:function(){
						isError = true;
					}
				});
				queryParam.codeGunFlag = codeGunFlag;
				if(isError){
					$("#msgAction").append("查詢失敗！請聯繫管理員");
					return false;
				}	
			//如果不是掃碼槍掃描查詢 清空相關隱藏域 
			} else {
				$("#hideHistoryChangeFlag").val('0');
				clearHide();
			}
		//如果不是掃碼槍掃描查詢 清空相關隱藏域 
		} else {
			 $("#hideHistoryChangeFlag").val('0');
	    	 queryParam = getQueryParamter();
	    	 clearHide();
		}
	    var ignoreFirstLoad = hidden;
	    // Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
	    /* var allColumnOptions = getAllColumnOptions();
	    var dgOptions = $("#dataGrid").datagrid("options");
		var columnOptions = dgOptions.columns;
		var dgColumns = (columnOptions)[0];
		// 與預設列長度相等
		if(dgColumns.length == 8){
			var firstColumn = dgColumns[0];
			allColumnOptions.unshift(firstColumn);
			(columnOptions)[0] = allColumnOptions;
		} */
		var options ={
			url : "${contextPath}/assetManage.do",
			queryParams : queryParam,
			pageNumber : pageNum,
			autoRowHeight : true,
			/* columns:columnOptions, */
			onBeforeSortColumn : function(){return sortFlag;},
			onLoadError : function(data) {
				$("#msgAction").append("查詢失敗！請聯繫管理員");
			},
			onUnselect : function (){
				if($(":checkbox[name=queryAll]").is(":checked")) {
					$('#queryAll').prop('checked',false);
					$("#isSelectedAll").val("0");
				}
			},
			onLoadSuccess : function(data){
				 //添加自定義屬性解決，當欄位超出3為數時行號遮擋的問題
				 $(this).datagrid("fixRownumber","dataGrid"); 
				 //解決表頭與內容變亂的問題
				 $("#dataGrid").datagrid("resize");
				 if ($(":checkbox[name=history]").is(":checked")) {
				 	//歷史查詢設為1
				 	$("#historyExport").val("1");
				 	//歷史資料查詢時隐藏checkbox
	                $('#dataGrid').datagrid('hideColumn','v');
	            } else {
	            	//庫存查詢設為0
	            	$("#historyExport").val("0");
	            	$('#dataGrid').datagrid('showColumn','v');
	            }
				$("#dataGrid").datagrid("clearSelections");
				$('a[name=btnEdit]').linkbutton({ plain: true, iconCls: 'icon-search' });
				var rowLength = getGridRowsCount("dataGrid");
				//批量造作
				queryAllAction();
				if (hidden) {
					$("#msgAction").empty();
					if (data.total == 0) {
						$("#msgAction").text(data.msg);
					}
					
					//掃碼槍查詢提示消息
					getCodeGunMsg(codeGunFlag,rowLength,queryParam.totalSize);
					if (rowLength >= 1) {
						//CR #2563 向複選框賦值 2017/10/12
						queryVendorWarehouseList('VENDOR_WAREHOUSE', queryParam);
						// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
						/* $('#filterBlank').attr("onclick","filterBlank(this);");
						$('#filterBlank').attr("style","color:blue"); */
						$('#btnExport').attr("onclick","exportData();");
						$('#btnExport').attr("style","color:blue");
						$("#confirmSend").linkbutton("enable");
						$("#toMail").combobox('enable');
						if ($(":checkbox[name=history]").is(":checked")) {
							$("#btnCarry").linkbutton("disable");
							$("#btnBorrow").linkbutton("disable");
							$("#btnCarryback").linkbutton("disable");
							$("#btnAssetin").linkbutton("disable");
							$("#btnRepair2").linkbutton("disable");
							$("#btnPendingdisabled").linkbutton("disable");
							$("#btnDisabled").linkbutton("disable");
							$("#btnBack").linkbutton("disable");
							$("#btnDestroy").linkbutton("disable");
							$("#btnOtheredit").linkbutton("disable");
							$("#btnPrint").linkbutton("disable");
							$("#btnUnbound").linkbutton("disable");
							$("#btnTaixinrent").linkbutton("disable");
							$("#btnSendrepair").linkbutton("disable");
							$("#btnJdwmaintenance").linkbutton("disable");
						} else {
							$("#btnCarry").linkbutton("enable");
							$("#btnBorrow").linkbutton("enable");
							$("#btnCarryback").linkbutton("enable");
							$("#btnAssetin").linkbutton("enable");
							$("#btnRepair2").linkbutton("enable");
							$("#btnPendingdisabled").linkbutton("enable");
							$("#btnDisabled").linkbutton("enable");
							$("#btnBack").linkbutton("enable");
							$("#btnDestroy").linkbutton("enable");
							$("#btnOtheredit").linkbutton("enable");
							$("#btnPrint").linkbutton("enable");
							$("#btnUnbound").linkbutton("enable");
							$("#btnTaixinrent").linkbutton("enable");
							$("#btnSendrepair").linkbutton("enable");
							$("#btnJdwmaintenance").linkbutton("enable");
						}
					} else {
						// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
					/* 	$('#filterBlank').attr("onclick","return false;");
						$('#filterBlank').attr("style","color:gray"); */
						$('#btnExport').attr("onclick","return false;");
						$('#btnExport').attr("style","color:gray");
						$("#btnCarry").linkbutton("disable");
						//CR #2563 查無資料時候置灰
						$("#confirmSend").linkbutton("disable");
						$("#toMail").combobox('setValue', '');
						$("#toMail").combobox('disable');
						if(hidden){
							options.sortName = null;
						}$("#btnBorrow").linkbutton("disable");
						$("#btnCarryback").linkbutton("disable");
						$("#btnAssetin").linkbutton("disable");
						$("#btnRepair2").linkbutton("disable");
						$("#btnPendingdisabled").linkbutton("disable");
						$("#btnDisabled").linkbutton("disable");
						$("#btnBack").linkbutton("disable");
						$("#btnDestroy").linkbutton("disable");
						$("#btnOtheredit").linkbutton("disable");
						$("#btnPrint").linkbutton("disable");
						$("#btnUnbound").linkbutton("disable");
						$("#btnTaixinrent").linkbutton("disable");
						$("#btnSendrepair").linkbutton("disable");
						$("#btnJdwmaintenance").linkbutton("disable");
					}
				}		
				hidden = true;
				//掃碼槍查詢后對隱藏域值進行處理 
				dealHidenValue(codeGunFlag,rowLength);
			}
		};	
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery.ignoreFirstLoad = !ignoreFirstLoad;
		openDlgGridQuery("dataGrid", options);
		openDlgGridQuery.ignoreFirstLoad = false;
	}
}  
//跳轉到特店維護詳情頁
function viewEditMerchantData1(title,actionId, merchantId) {
	$("#msgAction").text("");
	var viewDlg = $('#merchanAddDlg').dialog({
		title: title, 
		width: 700, 
		height: 370, 
		top:10,
		closed: false, 
		cache: false, 
		modal : true,
		queryParams : {
			actionId : actionId,
			merchantId : merchantId,
		}, 
		href: "${contextPath}/merchant.do",
		onLoad : function() {
			textBoxSetting("editMerchantDiv");
			ajaxService.getCompanyByCompanyCode("TSB-EDC", function(result){
				if (result != null){
					$("#companyId").combobox("setValue", result.companyId);
					$("#companyId").combobox("disable", "disable");
				}
			});
			$("#merchantCode").textbox("setValue", $("#taixinMid").textbox('getValue'));
		},
		onClose:function(){
			if(viewDlg){
				viewDlg.panel('clear');
			}
		},
		buttons : [{
			text:'儲存',
			width:88,
			iconCls:'icon-ok',
			handler: function(){
				var f = viewDlg.find("form");
				var controls = ['companyId','merchantCode','name'];
				if(validateForm(controls) && f.form("validate")){
					commonSaveLoading('merchanAddDlg');
					//取值
					var saveParam = f.form("getData");
					//客戶名稱 
					if (saveParam.companyId != '') {
						saveParam.companyId = f.find("#companyId").combobox('getValue');
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
								commonCancelSaveLoading('merchanAddDlg');
								viewDlg.dialog('close');
								//拿到特店的id
								var resultMap = result.row;
								var merchantId = resultMap.merchantId;
								var merchantCode = resultMap.merchantCode;
								if(actionId=="initAdd"){
									if(merchantId != ""){
										$("#taixinMid").textbox("setValue", merchantCode);
										var queryParams ={actionId : '<%=IAtomsConstants.ACTION_INIT_ADD%>', merchantId : merchantId};
										//$("#message").text("客戶特店資料新增成功");
										viewEditMerchantHeader('merchanAddHeaderDlg','新增特店表頭維護',queryParams, successCallBack, cancleCallBack, "<%=IAtomsConstants.ACTION_INIT_ADD%>", "${contextPath}", true);
									}
								} else {
									var name = resultMap.name;
									$("#taixinName").textbox("setValue", name);
									$("#taixinMid").textbox("setValue", merchantCode);
									$("#hidTaixinMid").val(merchantCode);
								}
							}else{
								commonCancelSaveLoading('merchanAddDlg');
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
							$("#msg").text(message);
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
function successCallBack(){
	$('#merchanAddDlg').dialog('close');
	searchMid(true);
}
//取消函數
function cancleCallBack(){
	searchMid(false);
}
//匯出借用明細
function exportBorrowList() {
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$("#message").empty();
	$('#msgAction').empty();
	
	$("input[name='actionId']").each(function(){
		$(this).val('');
	});
	$("input[name='serviceId']").each(function(){
		$(this).val('');
	});
	$("input[name='useCaseNo']").each(function(){
		$(this).val('');
	});
	
	$.blockUI(blockStyle);
	actionClicked(document.forms["zipForm"], 'DMM03060', '', 'exportBorrowDetail');
	ajaxService.getExportFlag('DMM03060', function(data){
		$.unblockUI();
	});
}
</script>
</body>
</html>