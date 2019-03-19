<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetManageFormDTO formDTO = null;
	DmmRepositoryDTO repositoryDTO = null;
	DmmRepositoryHistoryDTO repositoryHistoryDTO = null;
	if(ctx != null) {
		formDTO = (AssetManageFormDTO) ctx.getResponseResult();
		if (formDTO != null ) {
			repositoryDTO = formDTO.getDmmRepositoryDTO();
		} else {
			formDTO = new AssetManageFormDTO();
		}
	}
	if (repositoryDTO == null) {
		repositoryHistoryDTO = formDTO.getRepositoryHistDTO();
		if (repositoryHistoryDTO == null) {
			repositoryDTO = new DmmRepositoryDTO();
		}	
	}
	String tsbEdc = IAtomsConstants.PARAM_TSB_EDC;
%>
<html>
<c:set var="repositoryDTO" value="<%=repositoryDTO%>" scope="page"></c:set>
<c:set var="repositoryHistoryDTO" value="<%=repositoryHistoryDTO%>" scope="page"></c:set>
<c:set var="tsbEdc" value="<%=tsbEdc%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	 <div id="assetDetail" data-options="region:'center'" modal ="true" style="height: auto; padding: 10px 10px">
          <div class="ftitle">設備詳細資訊</div>
          <div><span id="message" class="red"></div>
           <table cellpadding="4" style="width: 65%">
                    <tr>
                        <td width="12%">設備序號:</td>
                        <td  width="19%" id="detailSerialNumber">${!empty repositoryDTO ?repositoryDTO.serialNumber : repositoryHistoryDTO.serialNumber }
						</td>
						<td width="11%">財產編號:</td>
                        <td width="19%" id="detailPropertyId">${!empty repositoryDTO ? repositoryDTO.propertyId : repositoryHistoryDTO.propertyId}
                        </td>
                        <td width="12%">狀態:</td>
                        <td width="19%" id="detailStatus">${!empty repositoryDTO ? repositoryDTO.status : repositoryHistoryDTO.status}
                        </td>
                    </tr>
                    <tr>
                        <td>設備名稱:</td>
                        <td id="detailName">${!empty repositoryDTO ? repositoryDTO.name : repositoryHistoryDTO.name}
						</td>
                        <td>所在位置:</td>
                        <td id="detailAddress">
                        	${!empty repositoryDTO ? repositoryDTO.brand : repositoryHistoryDTO.brand}
                        </td>
                        <td>倉庫名稱:</td>
                        <td id="detailItemName">${!empty repositoryDTO ? repositoryDTO.itemName : repositoryHistoryDTO.itemName}</td>
                    </tr>
                    <tr>
                        <td>合約編號:</td>
                        <td id="detailContractId">${!empty repositoryDTO ? repositoryDTO.contractCode : repositoryHistoryDTO.contractCode}
                        </td>
                        <td>維護模式:</td>
                        <td id="detailMaType">${!empty repositoryDTO ? repositoryDTO.maType : repositoryHistoryDTO.maType}</td>
                        <td>廠牌：</td><td id="detailAssetBrand">${!empty repositoryDTO ? repositoryDTO.assetBrand : repositoryHistoryDTO.assetBrand}</td>
                    </tr>
                    <tr>
                    	<td>型號:</td>
                        <td id="detailModel">${!empty repositoryDTO ? repositoryDTO.model : repositoryHistoryDTO.model}
                        </td>
                        <td>已啟用:</td>
                        <td><input id="detailIsEnabled" name="detailIsEnabled" disabled="disabled" type="checkbox" ${repositoryDTO.isEnabled eq 'Y'?'checked="checked"':'' }></input>  
                        </td>
                        <td>設備啟用日:</td>
                        <td id="detailEnableDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.enableDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.enableDate}" pattern="yyyy/MM/dd" /></c:if></td>
                    </tr>
                    <tr>
                        <td>租賃期滿日:</td>
                        <td id="detailSimEnableDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.simEnableDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.simEnableDate}" pattern="yyyy/MM/dd" /></c:if></td>
                        <td>租賃編號:</td>
                        <td id="detailSimEnableNo">${!empty repositoryDTO ? repositoryDTO.simEnableNo : repositoryHistoryDTO.simEnableNo}</td>
                    	<td></td>
                        <td></td>
                    </tr>
                    <tr>
                    	<td>領用/借用人:</td>
                        <td>
                            <c:if test="${!empty repositoryDTO }"><c:if test="${!empty repositoryDTO.carrier }" ><span id="detailCarrier">${repositoryDTO.carrier}</span></c:if><c:if test="${empty repositoryDTO.carrier }" ><span id="detailBorrower">${repositoryDTO.borrower}</span></c:if></c:if>
                            <c:if test="${empty repositoryDTO }"><c:if test="${!empty repositoryHistoryDTO.carrier }" ><span id="detailCarrier">${repositoryHistoryDTO.carrier}</span></c:if><c:if test="${empty repositoryHistoryDTO.carrier }" ><span id="detailBorrower">${repositoryHistoryDTO.borrower}</span></c:if></c:if>
                        </td>
                        <td>借用日期:</td>
                        <td>
                            <c:if test="${!empty repositoryDTO }"><span id="detailBorrowerStart"><fmt:formatDate value="${repositoryDTO.borrowerStart}" pattern="yyyy/MM/dd" /></span>～<span id="detailBorrowerEnd"><fmt:formatDate value="${repositoryDTO.borrowerEnd}" pattern="yyyy/MM/dd" /></span></c:if>
                            <c:if test="${empty repositoryDTO }"><span id="detailBorrowerStart"><fmt:formatDate value="${repositoryHistoryDTO.borrowerStart}" pattern="yyyy/MM/dd" /></span>～<span id="detailBorrowerEnd"><fmt:formatDate value="${repositoryHistoryDTO.borrowerEnd}" pattern="yyyy/MM/dd" /></span></c:if>
                        </td>
                        </td>
                        <td>歸還日期:</td>
                        <td id="detailBackDate" ><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.backDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.backDate}" pattern="yyyy/MM/dd" /></c:if></td>
                        </td>
                    </tr>
                    <tr>
                    	<td>案件編號 :
                        <td id="detailCaseId">
 							<c:if test="${!empty repositoryDTO}">
 								<c:if test="${(repositoryDTO.companyCode eq tsbEdc && repositoryDTO.queryCaseFlag ne 'C') or (repositoryDTO.queryCaseFlag eq 'N')}">${repositoryDTO.caseId}</c:if>
 								<c:if test="${(repositoryDTO.companyCode ne tsbEdc && (repositoryDTO.queryCaseFlag eq 'C' or repositoryDTO.queryCaseFlag eq 'B')) or ((repositoryDTO.companyCode eq tsbEdc && repositoryDTO.queryCaseFlag eq 'C') )}">
 									<a href='javascript:void(0)' onclick="showCaseInfo('${repositoryDTO.caseId}');">${repositoryDTO.caseId}</a>
 								</c:if>
 							</c:if>
 							<c:if test="${empty repositoryDTO}">
 								<c:if test="${(repositoryHistoryDTO.companyCode eq tsbEdc && repositoryHistoryDTO.queryCaseFlag ne 'C') or (repositoryHistoryDTO.queryCaseFlag eq 'N')}">${repositoryHistoryDTO.caseId}</c:if>
 								<c:if test="${(repositoryHistoryDTO.companyCode ne tsbEdc && (repositoryHistoryDTO.queryCaseFlag eq 'C' or repositoryHistoryDTO.queryCaseFlag eq 'B')) or ((repositoryHistoryDTO.companyCode eq tsbEdc && repositoryHistoryDTO.queryCaseFlag eq 'C') )}">
 									<a href='javascript:void(0)' onclick="showCaseInfo('${repositoryHistoryDTO.caseId}');">${repositoryHistoryDTO.caseId}</a>
 								</c:if>
 							</c:if>
 						 </td>
                        <td>裝機類別:</td>
                        <td id="detailInstallType">${!empty repositoryDTO ? repositoryDTO.installType : repositoryHistoryDTO.installType}</td>
                        <td>完修日期 :</td>
                        <td id="detailCaseCompletionDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.caseCompletionDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.caseCompletionDate}" pattern="yyyy/MM/dd" /></c:if></td>
                    </tr>
                    <tr>
                        <td>TID:</td>
                        <td id="detailTid">${!empty repositoryDTO ? repositoryDTO.tid : repositoryHistoryDTO.tid}
                        </td>
                        <td>特店代號 :</td>
                        <td id="detailMerchantCode">${!empty repositoryDTO ? repositoryDTO.merchantCode : repositoryHistoryDTO.merchantCode}</td>
                        <td>特店名稱 :</td>
                        <td id="detailMerName">${!empty repositoryDTO ? repositoryDTO.merName : repositoryHistoryDTO.merName}</td>
                    </tr>
                    <tr>
                    	<td>特店表頭:</td>
                        <td id="detailHeaderName">${!empty repositoryDTO ? repositoryDTO.headerName : repositoryHistoryDTO.headerName}</td>
                        <td>裝機區域:</td>
                        <td id="detailArea">${!empty repositoryDTO ? repositoryDTO.areaName : repositoryHistoryDTO.areaName}</td>
                        <td>簽收日期:</td>
                    	<td id="detailAnalyzeDate"><fmt:formatDate value="${!empty repositoryDTO ? repositoryDTO.analyzeDate : repositoryHistoryDTO.analyzeDate}" pattern="yyyy/MM/dd" /></td>
                    </tr>
                    <tr>
                    	<td>DTID:</td>
                        <td id="detailDtid">${!empty repositoryDTO ? repositoryDTO.dtid : repositoryHistoryDTO.dtid}</td>
                        <td>裝機地址:</td>
                        <td id="detailMerInstallAddress">
                    		${!empty repositoryDTO ? repositoryDTO.merInstallAddress : repositoryHistoryDTO.merInstallAddress }</td>
                    	<td>拆機/報修原因:</td>
                    	<td id="detailProblemReason">${!empty repositoryDTO ? repositoryDTO.problemReason : repositoryHistoryDTO.problemReason}</td>
                    </tr>
                    <tr>
                    	<td>程式名稱:</td><td id="detailAppName">${!empty repositoryDTO ? repositoryDTO.appName : repositoryHistoryDTO.appName}</td>
                    	<td>程式版本:</td><td id="detailAppVersion">${!empty repositoryDTO ? repositoryDTO.appVersion : repositoryHistoryDTO.appVersion}</td>
                   	 	<td>銀聯:</td>
                        <td><input id="detailIsCup" name="detailIsCup" disabled="disabled" type="checkbox" ${repositoryDTO.isCup eq 'Y'?'checked="checked"':'' }></input>  
                        </td>	
                    </tr>
                    <tr>
                    		<td>入庫批號:</td><td id="detailAssetInId">${!empty repositoryDTO ? repositoryDTO.assetInId : repositoryHistoryDTO.assetInId}</td>
                    		<td>建檔人員:</td>
                        <td id="detailCreateUserName">${!empty repositoryDTO ? repositoryDTO.createUserName : repositoryHistoryDTO.createUserName}
						</td>
						<td>入庫日期:</td><td id="detailAssetInTime"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.assetInTime}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.assetInTime}" pattern="yyyy/MM/dd" /></c:if></td>	
                    </tr>	
                    <tr>
                    		<td>實際驗收:</td>
                        <td><input id="detailIsChecked" name="detailIsChecked" disabled="disabled" type="checkbox" ${!empty repositoryDTO ? (repositoryDTO.isChecked eq 'Y'?'checked="checked"':''): (repositoryHistoryDTO.isChecked eq 'Y'?'checked="checked"':'')}></input>  
                        </td>
                        <td>驗收日期:</td>
                        <td id="detailCheckedDate" ><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.checkedDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.checkedDate}" pattern="yyyy/MM/dd" /></c:if></td>		
                        <td>原廠保固日期:</td>
                        <td id="detailFactoryWarrantyDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.factoryWarrantyDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.factoryWarrantyDate}" pattern="yyyy/MM/dd" /></c:if></td>		
                    </tr>
                    <tr>
                        <td>客戶實際驗收:</td>
                        <td><input id="detailIsCustomerChecked" name="detailIsCustomerChecked" disabled="disabled" type="checkbox" ${!empty repositoryDTO ? (repositoryDTO.isCustomerChecked eq 'Y'?'checked="checked"':''): (repositoryHistoryDTO.isCustomerChecked eq 'Y'?'checked="checked"':'')}></input>  
                        </td>
                        <td>客戶驗收日期:</td>
                        <td id="detailcustomerApproveDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.customerApproveDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.customerApproveDate}" pattern="yyyy/MM/dd" /></c:if></td>	
                        <td>客戶保固日期:</td>
                        <td id="detailCustomerWarrantyDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.customerWarrantyDate}" pattern="yyyy/MM/dd" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.customerWarrantyDate}" pattern="yyyy/MM/dd" /></c:if></td>	
                    </tr>
                    <tr>
                   		<td>使用人:</td>
                   		<td id="detailAssetUser">${!empty repositoryDTO ? repositoryDTO.assetUserName : repositoryHistoryDTO.assetUserName}</td>
                   		<td>資產Owner:</td>
                   		<td id="detailAssetOwner">${!empty repositoryDTO ? repositoryDTO.assetOwnerName : repositoryHistoryDTO.assetOwnerName}</td>
                   		<td>維護工程師:</td>
                    	<td id="detailVendorStaff">${!empty repositoryDTO ? repositoryDTO.vendorStaff : repositoryHistoryDTO.vendorStaff}</td>
                    </tr>
                    <tr>
                    	<td>維護廠商:</td>
                        <td id="detailMaintainCompany">${!empty repositoryDTO ? repositoryDTO.maintainCompany : repositoryHistoryDTO.maintainCompany}
						</td>
                    	<td>維修廠商:</td>
                        <td id="detailRepairVendor">${!empty repositoryDTO ? repositoryDTO.repairVendor : repositoryHistoryDTO.repairVendor}
						</td>
                    	<td>維護部門:</td>
                    	<td id="detailDepartmentId">${!empty repositoryDTO ? repositoryDTO.departmentId : repositoryHistoryDTO.departmentId}</td>
                    </tr>
                    <tr>
                        <td>故障組件:</td>
                        <td id="detailFaultComponent">${!empty repositoryDTO ? repositoryDTO.faultComponent : repositoryHistoryDTO.faultComponent}
                        </td>
                        <td>故障現象:</td>
                        <td id="detailFaultDescription">${!empty repositoryDTO ? repositoryDTO.faultDescription : repositoryHistoryDTO.faultDescription}
                        </td><td></td><td></td>
                     </tr>   
                     <tr>
                        <td>說明/排除方式:</td>
                        <td id="detailDescription" style="width:80px">${!empty repositoryDTO ? repositoryDTO.description : repositoryHistoryDTO.description}
                        <td>執行作業:</td><td id="detailAction">${!empty repositoryDTO ? repositoryDTO.action : repositoryHistoryDTO.action}</td><td></td><td></td>
                     </tr>
                     <tr>
                        <td>櫃位:</td>
                        <td id="detailCounter">${!empty repositoryDTO ? repositoryDTO.counter : repositoryHistoryDTO.counter}</td>
                        <td>箱號:</td><td id="detailCartonNo">${!empty repositoryDTO ? repositoryDTO.cartonNo : repositoryHistoryDTO.cartonNo}</td>
                        <td></td><td></td>
                     </tr> 
                    <tr>
                        <td>異動人員 :</td>
                        <td id="detailUpdateUserName">${!empty repositoryDTO ? repositoryDTO.updateUserName : repositoryHistoryDTO.updateUserName}
						</td>
                        <td>異動日期 :</td>
                        <td id="detailUpdateDate"><c:if test="${!empty repositoryDTO }"><fmt:formatDate value="${repositoryDTO.updateDate}" pattern="yyyy/MM/dd HH:mm:ss" /></c:if><c:if test="${empty repositoryDTO }"><fmt:formatDate value="${repositoryHistoryDTO.updateDate}" pattern="yyyy/MM/dd HH:mm:ss" /></c:if></td>
                        <td></td><td></td>
                    </tr>
                </table>
           <div class="ftitle">歷史資料</div>
           <div style="width: 900px">
               <table id="dg1" class="easyui-datagrid" style="width: 99%; height: auto"
                        data-options="
			                	rownumbers:true,
			                	pageNumber:0,
			                	idField:'histId',
			               		pagination:true,
								iconCls:'icon-edit',
								singleSelect:true,
								nowrap:false,
								pageSize : 5,
								pageList : [5, 10, 20, 30, 40, 50]
								">
                      <thead>
                         <tr>
                            <th data-options="field:'updateDate',width:180,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp">異動日期</th>
                            <th data-options="field:'updateUserName',width:160,halign:'center',align:'left',sortable:true">異動人員</th>
                            <th data-options="field:'action',width:100,halign:'center',align:'left',sortable:true">動作</th>
                            <th data-options="field:'status',width:80,halign:'center',align:'left',sortable:true">狀態</th>
                            <th data-options="field:'brand',width:170,halign:'center',align:'left',sortable:true">所在位置</th>
                            <th data-options="field:'isEnabled',width:80,sortable:true,halign:'center',align:'center',formatter:formatterCheckbox">已啟用</th>
                            <th data-options="field:'historyId',formatter:formatToHistory,width:87,halign:'center',align:'center',sortable:true">歷史查詢</th>
                            <th data-options="field:'a',hidden:true,sortable:true">歷史查詢</th>
                         </tr>
                    </thead>
               </table>
           </div>
       </div><div id="show"></div>
       <div id="showEditInfo" fit="true"></div>
       <script type="text/javascript">
       var viewDlgShow = undefined;
       /*
       *
       */
       function showCaseInfo(caseId){
    	   viewDlgShow = $('#showEditInfo').dialog({
   			title : '案件記錄',
   			width : 900,
   			height : 500,
   			modal : true,
   			method:'post',
   			closed : false,
   			cache : false,
   			queryParams : {
   				actionId : "showDetailInfo",
   				caseId : caseId,
   				isHistory : 'N'
   			},
   			onLoad :function(){
   				if(typeof queryCaseRecordShow != 'undefined' && queryCaseRecordShow instanceof Function){
   					queryCaseRecordShow();
   				}
   			},
   			onClose : function(){
   				if(viewDlgShow){
   					viewDlgShow.dialog('clear');
   				}
   			},
   			href : "${contextPath}/caseHandle.do",
   			modal : true
   		});
       }
     //記錄當前行不可編輯列
	var editColumns = undefined;  
    /*
	初始化時，為文本框屬性的列增加lable標籤。
	value：需要顯示的值
	row：該行信息
	index:行號
	field：該列的field
	*/
	function settingField(value,row,index,field) {
		if (editColumns && !editColumns.contains(field)){
			row[field] = "";
			return "<lable></lable>";
		} else {
			if (value) {
				return "<lable>"+value+"</lable>";
			} else {
				return "<lable></lable>";
			}
		}
	}
    /**
 * 聯動歷史查詢 
 */
function showAssetHistory(update) {
	//根據庫存歷史檔Id獲取庫存歷史信息
	var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	//$.blockUI(blockStyle1);
	$('#assetDetail').closest('div.panel').block(blockStyle1);
	ajaxService.getRepositoryHistDTOByHistId(update,function(data){
 				//設備編號
 				$("#detailSerialNumber").html(data.serialNumber);
 				//財產編號
 				$("#detailPropertyId").html(data.propertyId);
 				//狀態
 				$("#detailStatus").html(data.status);
 				//設備名稱
 				$("#detailName").html(data.name);
 				//所在位置
        		$("#detailAddress").html(data.brand);
			    //倉庫名稱
 				$("#detailItemName").html(data.itemName);
 				//合約ID
 				$("#detailContractId").html(data.contractCode);
 				//維護模式
 				$("#detailMaType").html(data.maType);
 				//型號
 				$("#detailModel").html(data.model);
 				//是否啟用
 				if (data.isEnabled=="Y") {
 					$("#detailIsEnabled").prop("checked",true);
 				} else {
 					$("#detailIsEnabled").prop("checked",false);
 				}
 				//設備啟用日
 				$("#detailEnableDate").html(formatLongToDate(data.enableDate));
 				//租賃是否啟用
 				/* if (data.isSimEnable=="Y") {
 					$("#detailIsSimEnable").prop("checked",true);
 				} else {
 					$("#detailIsSimEnable").prop("checked",false);
 				} */
 				//租賃啟用日
 				$("#detailSimEnableDate").html(formatLongToDate(data.simEnableDate));
 				//租賃編號
 				$("#detailSimEnableNo").html(data.simEnableNo);
 				//借用人
 				$("#detailBorrower").html(data.borrower);
 				//借用日期起乾
 				if (data.borrowerStart && data.borrowerEnd) {
 					$("#detailBorrowerStart").html(formatLongToDate(data.borrowerStart));
 					$("#detailBorrowerEnd").html(formatLongToDate(data.borrowerEnd));
 				} else {
 					$("#detailBorrowerStart").html("");
 					$("#detailBorrowerEnd").html("");
 				}
			    //歸還日
 				$("#detailBackDate").html(formatLongToDate(data.backDate));
 				//案件完修日
 				$("#detailTicketCompletionDate").html(formatLongToDate(data.ticketCompletionDate));
 				$("#detailTid").html(data.tid);
 				$("#detailMerchantCode").html(data.merchantCode);
 				$("#detailMerName").html(data.merName);
 				$("#detailHeaderName").html(data.headerName);
 				$("#detailDtid").html(data.dtid);
 				//裝機類別
 				$("#detailInstallType").html(data.installType);
 				//裝機地址
 				$("#detailMerInstallAddress").html(data.merInstallAddress);
 				//裝機區域
 				$("#detailArea").html(data.areaName);
 				//程式名稱
 				$("#detailAppName").html(data.appName);
 				//程式版本
 				$("#detailAppVersion").html(data.appVersion);
 				//銀聯
 				if (data.isCup=="Y") {
 					$("#detailIsCup").prop("checked",true);
 				} else {
 					$("#detailIsCup").prop("checked",false);
 				}
 				//入庫ID
 				$("#detailAssetInId").html(data.assetInId);
 				$("#detailCreateUserName").html(data.createUserName);
 				//入庫日期
 				$("#detailAssetInTime").html(formatLongToDate(data.assetInTime));
 				//是否驗收
 				if (data.isChecked=="Y") {
 					$("#detailIsChecked").prop("checked",true);
 				} else {
 					$("#detailIsChecked").prop("checked",false);
 				}
 				//驗收日期
 				$("#detailCheckedDate").html(formatLongToDate(data.checkedDate));
 				//原廠保固日期
 				$("#detailFactoryWarrantyDate").html(formatLongToDate(data.factoryWarrantyDate));
 				//客戶驗收
 				if (data.isCustomerChecked=="Y") {
 					$("#detailIsCustomerChecked").prop("checked",true);
 				} else {
 					$("#detailIsCustomerChecked").prop("checked",false);
 				}
 				//客戶驗收日期
 				$("#detailcustomerApproveDate").html(formatLongToDate(data.customerApproveDate));
 				//客戶保固日期
 				$("#detailCustomerWarrantyDate").html(formatLongToDate(data.customerWarrantyDate));
 				//資產owner
 				$("#detailAssetOwner").html(data.assetOwnerName);
 				//使用人
 				$("#detailAssetUser").html(data.assetUserName);
 				//說明
 				$("#detailDescription").html(data.description);
 				//使用人
 				$("#detailCarrier").html(data.carrier);
 				//故障組件
 				$("#detailFaultComponent").html(data.faultComponent);
 				//故障現象
 				$("#detailFaultDescription").html(data.faultDescription);
 				//維護廠商
 				$("#detailMaintainCompany").html(data.maintainCompany);
 				//維修廠商
 				$("#detailRepairVendor").html(data.repairVendor);
 				$("#detailUpdateUserName").html(data.updateUserName);
 				$("#detailUpdateDate").html(formatToTimeStamp(data.updateDate));
 				$("#detailAction").html(data.action);
 				$("#detailAssetBrand").html(data.assetBrand);
 				//$("#detailCaseId").html(data.caseId);
 				//Task #2675 案件編號，若沒有對應案件可檢視，不要提供超連結 2017/10/23
 				if((data.companyCode == 'TSB-EDC' && data.queryCaseFlag !='C') || data.queryCaseFlag =='N'){
	 				$("#detailCaseId").html(data.caseId);
 				} else {
 					$("#detailCaseId").children().remove();
 					$("#detailCaseId").html("");
 					$("#companyCodeHistDEC").remove();
 					$("#detailCaseId").empty();  
 					if(data.caseId!=null && data.caseId!=''){
 						$("#detailCaseId").append("<a id = 'companyCodeHistDEC' href='javascript:void(0)' >" + data.caseId + "</a>");
 					 		$("#companyCodeHistDEC").click(function(){
 					 			showCaseInfo(data.caseId);
 					 		});
 					}
 				}
 				$("#detailCaseCompletionDate").html(formatLongToDate(data.caseCompletionDate));
 				//update by 2017-07-28 詳細頁面增加四個欄位
 				$("#detailAnalyzeDate").html(formatLongToDate(data.analyzeDate));
 				$("#detailProblemReason").html(data.problemReason);
 				$("#detailDepartmentId").html(data.departmentId);
 				$("#detailVendorStaff").html(data.vendorStaff);
 				//update by 2017-08-04 詳細頁面增加兩個欄位欄位
 				//
 				$("#detailCounter").html(data.counter);
 				$("#detailCartonNo").html(data.cartonNo);
 	});
	$('#assetDetail').closest('div.panel').unblock();
}
       </script>
</body>
</html>