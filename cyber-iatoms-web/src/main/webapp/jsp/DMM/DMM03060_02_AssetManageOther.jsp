<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	//設備狀態集合
	//List<Parameter> assetStatusList = (List<Parameter>) SessionHelper.getAttribute(request,
	//IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.PARAM_ASSET_STATUS);
	//客戶列表
	//List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request,
	//IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.CUSTOMER_LIST);
	//故障組件集合
	List<Parameter> faultComponentList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
	//故障現象集合
	List<Parameter> faultDescriptionList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
	//合約ID集合
	List<Parameter> contractList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.ACTION_GET_CONTRACT_LIST);
	//資產OWNER和使用人
	List<Parameter> customers = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, AssetManageFormDTO.CUSTOMER_AND_VENDOR_LIST);
	AssetManageFormDTO formDTO = null;
	DmmRepositoryDTO repositoryDTO = null;
	if(ctx != null) {
		formDTO = (AssetManageFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
			repositoryDTO = formDTO.getDmmRepositoryDTO();
		}  else {
			formDTO = new AssetManageFormDTO();
		}
	}
	//故障組件數組
	String [] faultComponentArray = null;
	//故障現象數組
	String [] faultDescriptionArray = null;
	//故障組件
	List<String> faultComponents = null;
	//故障現象
	List<String> faultDescriptions = null;
	if (repositoryDTO == null) {
		repositoryDTO = new DmmRepositoryDTO();
	} else {
		
		String faultComponent = repositoryDTO.getFaultComponentId();
		String faultDescription = repositoryDTO.getFaultDescriptionId();
		// 為故障組件多選框賦值
		if (StringUtils.hasText(faultComponent)) {
			faultComponent = faultComponent.trim();
			faultComponentArray = faultComponent.split(",");
			faultComponents = Arrays.asList(faultComponentArray);
		}
		// 為故障現象多選框賦值
		if (StringUtils.hasText(faultDescription)) {
			faultDescription = faultDescription.trim();
			faultDescriptionArray = faultDescription.split(",");
			faultDescriptions = Arrays.asList(faultDescriptionArray);
		}
	}
%>
<html>
<%-- <c:set var="assetStatusList" value="<%=assetStatusList%>" scope="page"></c:set> --%>
<c:set var="repositoryDTO" value="<%=repositoryDTO%>" scope="page"></c:set>
<%-- <c:set var="customerList" value="<%=customerList%>" scope="page"></c:set> --%>
<c:set var="faultComponentList" value="<%=faultComponentList%>" scope="page"></c:set>
<c:set var="faultDescriptionList" value="<%=faultDescriptionList%>" scope="page"></c:set>
<c:set var="contractList" value="<%=contractList%>" scope="page"></c:set>
<c:set var="faultComponents" value="<%=faultComponents%>" scope="page"></c:set>
<c:set var="faultDescriptions" value="<%=faultDescriptions%>" scope="page"></c:set>
<c:set var="customers" value="<%=customers%>" scope="page"></c:set>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
       <div data-options="region:'center'" modal ="true" style="padding: 10px 10px">
             <div class="ftitle">設備詳細資訊</div>
             <form id="editForm" method="post"><div><span id="messager" class="red"></span></div>
                <table cellpadding="4" style="width: 65%">
                    <tr>
                        <td width="13%">設備序號:</td>
                        <td  id="serialNumber">${repositoryDTO.serialNumber }
						</td><input type="hidden" id="editAssetId" value="<c:out value='${repositoryDTO.assetId }'/>"/>
                        <td width="13%">財產編號:</td>
                        <td>
                            <input id="propertyId" data-options="validType:['maxLength[20]','englishOrNumber[\'財產編號限英數字，請重新輸入\']']" class="easyui-textbox input-width-144" type="text" maxlength="20" value="${repositoryDTO.propertyId }"></input>
                        </td>
                        <td width="13%">狀態:</td>
                        <td>
                           ${repositoryDTO.status }
                        </td>
                    </tr>
                    <tr>
                        <td>設備名稱:</td>
                        <td>${repositoryDTO.name}
						</td>
                        <td>所在位置:</td>
                        <td>	
                        	${repositoryDTO.brand}
                        </td>
                        <td>倉庫:</td>
                        <td>${repositoryDTO.itemName}</td>
                    </tr>
                    <tr>
                        <td>合約編號:</td>
                        <td><cafe:droplisttag 
				               id="contractId"
				               name="contractId" 
				               css="easyui-combobox"
				               result="${contractList}"
				               hasBlankValue="true"
				               blankName="請選擇"
				               selectedValue="${repositoryDTO.contractId}"
				               style="width:150px"
				               javascript="editable=false">
				            </cafe:droplisttag>
                        </td>
                        <td>維護模式:</td>
                        <td id = "otherMaType">${repositoryDTO.maType}</td><input id = "hiddenMaType" type="hidden"></input>
                        <td>廠牌:</td><td id="assetBrand">${repositoryDTO.assetBrand}</td>
                    </tr>
                    <tr>
                    	<td>型號:</td>
                        <td>
                            ${repositoryDTO.model}
                        </td>
                        <td>已啟用:</td>
                        <td>
                        	<input id="isEnabled" name="isEnabled" disabled="disabled" type="checkbox" ${repositoryDTO.isEnabled eq 'Y'?'checked="checked"':'' }></input>                           
                        </td>
                        <td>設備啟用日:</td>
                        <td>
                        	<input id="enableDate" value="${repositoryDTO.enableDate}" class="easyui-datebox" style="width: 120px" maxlength="10" missingMessage="請輸入設備啟用日，格式限YYYY/MM/DD" invalidMessage="請輸入設備啟用日，格式限YYYY/MM/DD" data-options="required:true,validType:['date','validDate'] "></td>
                    </tr>
                    <tr>
                        <td>租賃期滿日:</td>
                        <td>
                        	<input id="simEnableDate" value="${repositoryDTO.simEnableDate}" class="easyui-datebox" maxlength="10" style="width: 120px" invalidMessage="租賃期滿日格式限YYYY/MM/DD" data-options="validType:['date','validDate'] "></td>
                        <td>租賃編號:</td>
                        <td><input id="simEnableNo" class="easyui-textbox input-width-144" type="text" data-options="validType:['maxLength[20]']" maxlength="20" value="<c:out value='${repositoryDTO.simEnableNo}'/>"></input></td>
                    	<td></td>
                        <td></td>
                    </tr>
                    <tr>
                    		<td>領用/借用人:</td>
                        <td>
                            <c:if test="${!empty repositoryDTO.carrier }" ><span id="carrier">${repositoryDTO.carrier}</span></c:if><c:if test="${empty repositoryDTO.carrier }" ><span id="borrower">${repositoryDTO.borrower}</span></c:if>
                        </td>
                        <td>借用日期:</td>
                        <td>
                            <span id="borrowerStart"><fmt:formatDate value="${repositoryDTO.borrowerStart}" pattern="yyyy/MM/dd" /></span><c:if test="${!empty repositoryDTO.borrowerStart}">～</c:if><span id="borrowerEnd"><fmt:formatDate value="${repositoryDTO.borrowerEnd}" pattern="yyyy/MM/dd" /></span></td>
                       </td>
                        <td>歸還日期:</td>
                        <td>
                        	<fmt:formatDate value="${repositoryDTO.backDate}" pattern="yyyy/MM/dd" />
                        </td>
                    </tr>
                    <tr>
                    	<td>案件編號 :</td>
                        <td id="caseId">${repositoryDTO.caseId}</td>
                        <td>裝機類別:</td>
                        <td id="installType">${repositoryDTO.installType}</td>
                        <td>完修日期 :</td>
                        <td>
                        	<fmt:formatDate value="${repositoryDTO.caseCompletionDate}" pattern="yyyy/MM/dd" /></td>
                    </tr> 
                    <tr>
                        <td>TID:</td>
                        <td><input class="easyui-textbox input-width-144" type="text" id="tid" maxlength="8" invalidMessage="TID限英數字，請重新輸入" data-options="validType:['numberOrEnglish[\'TID限英數字，請重新輸入\']','maxLength[8]']" value="${repositoryDTO.tid}"></input>
                        </td>
                        <td>特店代號 :</td>
                        <td>${repositoryDTO.merchantCode}</td>
                        <td>特店名稱 :</td>
                        <td>${repositoryDTO.merName}</td>
                    </tr>
                    <tr>
                    	<td>特店表頭:</td>
                        <td>${repositoryDTO.headerName}</td>
                        <td>裝機區域:</td>
                        <td>${repositoryDTO.areaName }</td>
                        <td>簽收日期:</td>
	                    <td><fmt:formatDate value="${repositoryDTO.analyzeDate}" pattern="yyyy/MM/dd" /></td>
                    </tr>
                    <tr>
                    	<td>DTID:</td>
                        <td><input class="easyui-textbox input-width-144" type="text" id="dtid" invalidMessage="DTID限英數字，請重新輸入" maxlength="8" data-options="validType:['numberOrEnglish[\'DTID限英數字，請重新輸入\']','maxLength[8]']" value="${repositoryDTO.dtid}"></input></td>
                        <td>裝機地址:</td>
                        <td>${repositoryDTO.merInstallAddress }</td>
                    	<td>拆機/報修原因:</td>
	                    <td>${repositoryDTO.problemReason}</td>	
                    	</tr>
                    	<tr>
                    		<td>程式名稱:</td><td>${repositoryDTO.appName}</td>
                    		<td>程式版本:</td><td>${repositoryDTO.appVersion}</td>
                   	 	<td>銀聯:</td>
                        <td><input id="isCup" name="isCup" disabled="disabled" type="checkbox" ${repositoryDTO.isCup eq 'Y'?'checked="checked"':'' }></input>                           
                        </td>	
                    	</tr>
                    	<tr>
                    		<td>入庫批號:</td><td>${repositoryDTO.assetInId}</td>
                    		<td>建檔人員:</td>
                        <td>${repositoryDTO.createUserName}
						</td>
						<td>入庫日期:</td><td><fmt:formatDate value="${repositoryDTO.assetInTime}" pattern="yyyy/MM/dd" /></td>	
                    	</tr>	
                    <tr>
                    		<td>實際驗收:</td>
                        <td><input id="isChecked" name="isChecked" disabled="disabled" type="checkbox" ${repositoryDTO.isChecked eq 'Y'?'checked="checked"':'' }></input>                           
                        </td>
                        <td>驗收日期:</td>
                        <td><input id="checkedDate" value="${repositoryDTO.checkedDate}" maxlength="10" class="easyui-datebox" style="width: 120px" invalidMessage="驗收日期格式限YYYY/MM/DD" data-options="validType:['date','validDate'] "/></td>
                        <td>原廠保固日期:</td>
                        <td><input id="factoryWarrantyDate" maxlength="10" value="${repositoryDTO.factoryWarrantyDate}" class="easyui-datebox" style="width: 120px" invalidMessage="原廠保固日期格式限YYYY/MM/DD" data-options="validType:['date','validDate'] "/></td>
                    </tr>	
                    <tr>
                        <td>客戶實際驗收:</td>
                        <td>
                        	<input id="isCustomerChecked" name="isCustomerChecked" disabled="disabled" type="checkbox" ${repositoryDTO.isCustomerChecked eq 'Y'?'checked="checked"':'' }></input>  
                        </td>
                        <td>客戶驗收日期:</td>
                        <td><fmt:formatDate value="${repositoryDTO.customerApproveDate}" pattern="yyyy/MM/dd" /></td>	
                        </td>
                        <td>客戶保固日期:</td>
                        <td>
                            <input id="customerWarrantyDate" value="${repositoryDTO.customerWarrantyDate}" maxlength="10" class="easyui-datebox" style="width: 120px" invalidMessage="客戶保固日期格式限YYYY/MM/DD" data-options="validType:['date','validDate'] "/>
                        </td>
                    </tr>
                    <tr>
                    		<td>使用人:<span class="red">*</span></td>
                    		<td><cafe:droplisttag 
				               id="assetUser"
				               name="assetUser" 
				               css="easyui-combobox"
				               result="${customers}"
				               hasBlankValue="true"
				               blankName="請選擇"
				               selectedValue="${repositoryDTO.assetUser}"
				               style="width:150px"
				               javascript="validType='requiredDropList' editable=false invalidMessage=\"請輸入使用人\"">
				            </cafe:droplisttag></td>
                    		<td>資產Owner:<span class="red">*</span></td>
                    		<td><cafe:droplisttag 
				               id="assetOwner"
				               name="assetOwner" 
				               css="easyui-combobox"
				               result="${customers}"
				               hasBlankValue="true"
				               blankName="請選擇"
				               selectedValue="${repositoryDTO.assetOwner}"
				               style="width:150px"
				               javascript="validType='requiredDropList' editable=false invalidMessage=\"請輸入資產Owner\"">
				           </cafe:droplisttag></td>
				        <td>維護工程師:</td>
                    	<td>${repositoryDTO.vendorStaff}</td>
                    </tr>
                    <tr>
                    		<td>維護廠商:</td>
                        <td>${repositoryDTO.maintainCompany}
						</td>
                    		<td>維修廠商:</td>
                        <td>${repositoryDTO.repairVendor}</td>
						<td>維護部門:</td>
                    	<td>${repositoryDTO.departmentId}</td>
                    </tr>
                    <tr>
                        <td>故障組件:</td>
                        <td>
                            <cafe:droplistchecktag 
					               id="faultComponent"
					               name="faultComponent" 
					               css="easyui-combobox"
					               result="${faultComponentList}"
					               hasBlankValue="true"
					               blankName="請選擇(複選)"
					               selectedValues="${faultComponents }"
					               style="width: 150px"
					               javascript="editable=false multiple=true">
			    				</cafe:droplistchecktag>
                        </td>
                        <td>故障現象:</td>
                        <td>
                            <cafe:droplistchecktag 
					               id="faultDescription"
					               name="faultDescription" 
					               css="easyui-combobox"
					               result="${faultDescriptionList}"
					               hasBlankValue="true"
					               blankName="請選擇(複選)"
					               selectedValues="${faultDescriptions}"
					               style="width: 150px"
					               javascript="editable=false multiple=true">
			    				</cafe:droplistchecktag>
                        </td><td></td><td></td>
                     </tr>   
                     <tr>
                        <td>說明/排除方式:</td>
                        <td>
                            <input class="easyui-textbox input-width-144" type="text" maxLength="200" data-options="validType:'maxLength[200]'" id="repairComment" value="<c:out value='${repositoryDTO.description}'/>"></input>
                        </td>
                        <td>執行作業:</td><td>${repositoryDTO.action}</td><td></td><td></td>
                     </tr>
                     <tr>
                        <td>櫃位:</td>
                        <td>${repositoryDTO.counter}</td>
                        <td>箱號:</td><td>${repositoryDTO.cartonNo}</td>
                        <td></td><td></td>
                     </tr>     
                    <tr>
                        <td>異動人員 :</td>
                        <td>${repositoryDTO.updateUserName}
						</td>
                        <td>異動日期 :</td>
                        <td><fmt:formatDate value="${repositoryDTO.updateDate}" pattern="yyyy/MM/dd HH:mm:ss" /></td>
                        <td></td><td></td>
                    </tr>
                </table>
            </form>
        </div>
    <script type="text/javascript">
	
$(function(){ 
	//dateboxSetting();
	$("#hiddenMaType").val('${repositoryDTO.maType}');
 	$("#faultComponent").combobox({
 		onSelect: function (newValue) {
             selectMultiple(newValue,"faultComponent")
        },
   	});
   	$("#faultDescription").combobox({
   		onSelect: function (newValue) {
             selectMultiple(newValue,"faultDescription")
        },
    }); 
   	$("#faultComponent").combobox({
   		onUnselect: function (newValue) {
   			unSelectMultiple(newValue,"faultComponent")
        },
   	});
   	$("#faultDescription").combobox({
   		onUnselect: function (newValue) {
   			unSelectMultiple(newValue,"faultDescription")
        },
    });
	//如果設備啟用日有值，開啟驗證，是否啟用checkbox選中
   	$("#enableDate").datebox({
   		onChange:function(newValue, oldValue){
    			if (!isEmpty(newValue)) {
	    			 if ($(":checkbox[name=isEnabled]").is(":checked")) {
	    			} else {
	    				$("#isEnabled").prop("checked",true);
	    			}
	    			$("#enableDate").datebox("enableValidation");
    			} else {
	    			$("#isEnabled").prop("checked",false);
	    			$("#enableDate").datebox("disableValidation");
    			}	
   		}   		
   	});
  	//租賃期滿日有值，將維護模式改為租賃轉買斷 update by --2017-07-03
	$("#simEnableDate").datebox({
   		onChange:function(){
   			var newValue = $('#simEnableDate').datebox("getValue");
   			//
   			if(newValue !=null && newValue != ''){
   				$("#otherMaType").text('租賃轉買斷');
   			} else {
   				$("#otherMaType").text($("#hiddenMaType").val());
   			}
   		}
   	}); 
});

    </script>
</body>
</html>