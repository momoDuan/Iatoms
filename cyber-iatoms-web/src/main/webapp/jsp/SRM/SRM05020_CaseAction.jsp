<%--
	案件動作
	author：crisszhang 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iAtomsConstants" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_ACTION" var="caseActionAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$FUNCTION_TYPE" var="functionType" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_ROLE" var="caseRoleAttr" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*,java.lang.*"%>
<%-- 案件處理DTO  SrmCaseHandleInfoDTO--%>
<tiles:useAttribute id="templatesList" name="templatesList" classname="java.util.List"/>
<tiles:useAttribute id="caseHandleInfoDTO" name="caseHandleInfoDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO" ignore="true" />
<%-- 案件處理formDTO  CaseManagerFormDTO--%>
<tiles:useAttribute id="caseManagerFormDTO" name="caseManagerFormDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO"/>
<%-- 用戶角色--%>
<tiles:useAttribute id="userRights" name="userRights" classname="java.util.List" ignore="true" />
<tiles:useAttribute id="contextPathAction" name="contextPathAction" classname="java.lang.String" ignore="true" />
<c:set var="stuff" value="case" scope="page"></c:set>
	<c:if test="${not empty caseHandleInfoDTO}">
		<c:choose>
			<c:when test="${caseHandleInfoDTO.caseStatus ne caseStatusAttr.WAIT_DISPATCH.code}">
				<c:if test="${caseHandleInfoDTO.caseStatus ne caseStatusAttr.VOIDED.code}" >
					<div style="text-align: right;width: 99%; height: auto;">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" id="saveCaseBtn" onclick="saveCase(true,'${caseHandleInfoDTO.caseCategory}');" style="width: 90px">儲存</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-cancel" id="cancelCaseBtn" onclick="cancelEdit();" style="width: 90px">取消</a>
					</div>
				</c:if>
				<div id="caseAction" class="topSoller">
					<div><span id="dataGridResponse-msg" class="red"></span></div>
					<table style="width: 100%">
						<tr>
						<c:if test="${caseHandleInfoDTO.caseStatus ne caseStatusAttr.VOIDED.code }" >
							<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.ADD_RECORD.code )}"><a href="javascript:void(0)" id="btn_add_record" class="easyui-linkbutton c6" onclick="openDealGrid('addRecord');" style="width: auto">新增記錄</a></c:if>
							<c:if test="${caseHandleInfoDTO.caseStatus ne caseStatusAttr.CLOSED.code and caseHandleInfoDTO.caseStatus ne caseStatusAttr.IMMEDIATE_CLOSE.code }" >
						
								<c:set var="isDispatch" value="${false }"/>
								<c:set var="isAutoDispatching" value="${false }"/>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.DISPATCH.code )}">
									<c:forEach var="item" items="${userRights }" >
										<c:if test="${functionType.DISPATCH.code eq item }">
											<c:set var="isDispatch" value="${true }"></c:set>
										</c:if>
										<c:if test="${functionType.AUTO_DISPATCH.code eq item }">
											<c:set var="isAutoDispatching" value="${true }"></c:set>
										</c:if>
									</c:forEach>
								</c:if>
							
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.DISPATCH.code ) and isDispatch}"><a href="javascript:void(0)" id="btn_dispatch" class="easyui-linkbutton c6" style="width: auto" onclick="openDealGrid('dispatching');">派工</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.AUTO_DISPATCH.code ) and isAutoDispatching}"><a href="javascript:void(0)" id="btn_auto_dispatch" class="easyui-linkbutton c6" style="width: auto" onclick="openDealGrid('autoDispatching');">自動派工</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.RESPONSED.code )}"><a href="javascript:void(0)" id="btn_responsed" class="easyui-linkbutton c6" onclick="openDealGrid('response');" style="width: auto">回應</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.ARRIVE.code )}"><a href="javascript:void(0)" id="btn_arrive" class="easyui-linkbutton c6" onclick="openDealGrid('arrive');" style="width: auto">到場</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.COMPLETED.code )}"><a href="javascript:void(0)" id="btn_completed" class="easyui-linkbutton c6" onclick="openDealGrid('complete');" style="width: auto">完修</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.SIGN.code )}"><a href="javascript:void(0)" id="btn_sign" class="easyui-linkbutton c6" onclick="openDealGrid('sign');" style="width: auto">簽收</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.ONLINE_EXCLUSION.code )}"><a href="javascript:void(0)" id="btn_online_exclusion" class="easyui-linkbutton c6" onclick="openDealGrid('onlineExclusion');" style="width: auto">線上排除</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.BACK.code )}"><a href="javascript:void(0)" id="btn_back" class="easyui-linkbutton c6" onclick="openDealGrid('retreat');" style="width: auto">退回</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.DELAY.code )}"><a href="javascript:void(0)" id="btn_delay" class="easyui-linkbutton c6" onclick="openDealGrid('delay');" style="width: auto">延期</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.RUSH_REPAIR.code )}"><a href="javascript:void(0)" id="btn_rush_repair" class="easyui-linkbutton c6" onclick="openDealGrid('rushRepair');" style="width: auto">催修</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_CASE_TYPE.code )}"><a href="javascript:void(0)" id="btn_change_case_type" class="easyui-linkbutton c6" onclick="openDealGrid('changeCaseType');" style="width: auto">修改案件類型</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CLOSED.code )}"><a href="javascript:void(0)" id="btn_closed" class="easyui-linkbutton c6" onclick="openDealGrid('closed');" style="width: auto">結案</a></c:if>
								
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.VOID_CASE.code )}"><a href="javascript:void(0)" id="btn_void_case" class="easyui-linkbutton c6" onclick="VoidCaseView();" style="width: auto">作廢</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.IMMEDIATELY_CLOSING.code )}"><a href="javascript:void(0)" id="btn_immediately_closing" class="easyui-linkbutton c6" onclick="openDealGrid('immediatelyClosing');" style="width: auto">協調完成</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_COMPLETE_DATE.code )}"><a href="javascript:void(0)" id="button_change_complete_date" class="easyui-linkbutton c6" onclick="openDealGrid('changeCompleteDate');" style="width: auto">修改實際完修時間</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_CREATE_DATE.code )}"><a href="javascript:void(0)" id="button_change_create_date" class="easyui-linkbutton c6" onclick="openDealGrid('changeCreateDate');" style="width: auto">修改進件時間</a></c:if>
								
							</c:if>
							<c:if test="${caseHandleInfoDTO.caseStatus eq caseStatusAttr.CLOSED.code or caseHandleInfoDTO.caseStatus eq caseStatusAttr.IMMEDIATE_CLOSE.code }" >
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_CASE_TYPE.code )}"><a href="javascript:void(0)" id="btn_change_case_type" class="easyui-linkbutton c6" onclick="openDealGrid('changeCaseType');" style="width: auto">修改案件類型</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_COMPLETE_DATE.code )}"><a href="javascript:void(0)" id="button_change_complete_date" class="easyui-linkbutton c6" onclick="openDealGrid('changeCompleteDate');" style="width: auto">修改實際完修時間</a></c:if>
								<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.CHANGE_CREATE_DATE.code )}"><a href="javascript:void(0)" id="button_change_create_date" class="easyui-linkbutton c6" onclick="openDealGrid('changeCreateDate');" style="width: auto">修改進件時間</a></c:if>
							</c:if>							
						</c:if> 
							<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.PAYMENT.code )}"><a href="javascript:void(0)" id="button_payment" class="easyui-linkbutton c6" onclick="openDealGrid('payment');" style="width: auto">到貨檢測</a></c:if>
							<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.LEASE_PRELOAD.code )}"><a href="javascript:void(0)" id="button_lease_preload" class="easyui-linkbutton c6" onclick="openDealGrid('leasePreload');" style="width: auto">租賃預載</a></c:if>
							<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.DISTRIBUTION.code )}"><a href="javascript:void(0)" id="button_distribution" class="easyui-linkbutton c6" onclick="openDealGrid('distribution');" style="width: auto">租賃配送中</a></c:if>
							<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.LEASE_SIGN.code )}"><a href="javascript:void(0)" id="button_lease_sign" class="easyui-linkbutton c6" onclick="openDealGrid('onlineExclusion','Y');" style="width: auto">租賃簽收</a></c:if>
							<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.PRINT.code )}"><a href="javascript:void(0)" id="btn_print" class="easyui-menubutton" menu="#mm2" style="width: auto">列印工單</a></c:if>
						</tr>
					</table>
					<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.PRINT.code )}">
					<div id="mm2" style="width: 250px;">
						<c:if test="${!empty templatesList}">
							<c:forEach items="${templatesList}" var="templates">
								<div class="menu-sep"></div>
								<div onclick="exportData('${templates.name }','${templates.value }', 'true','${caseHandleInfoDTO.caseId}');">${templates.name }</div>
							</c:forEach>
						</c:if>
					</div></c:if>
						<div id="responsePanel" style="display:none;">
							<table id="dataGridResponse" style="width: 100%; height: auto">
							</table>
							<table id="dataGridAssetLink" style="width: 100%; height: auto">
							</table>
							<table id="dataGridSuppliesLink" style="width: 100%; height: auto">
							</table>
						</div>
				</div>
			</c:when>
			<c:otherwise>
				<div style="text-align: right;width: 99%; height: auto;">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-ok" id="saveCaseBtn" onclick="saveCase(true,'${caseHandleInfoDTO.caseCategory}');" style="width: 90px">儲存</a>
					<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.DISPATCH.code )}">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-redo" id="dispatchCaseBtn" onclick="validateDispatch()" style="width: 90px">派工</a>
					</c:if>
					<c:if test="${fn:contains(caseManagerFormDTO.logonUser.accRghts[useCaseNo], functionType.VOID_CASE.code )}"><a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-undo" id="btn_void_case" onclick="VoidCaseView()" style="width: 90px">作廢</a></c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-cancel" id="cancelCaseBtn" onclick="cancelEdit();" style="width: 90px">取消</a>
				</div>
			</c:otherwise>
		</c:choose>
		<input id="hideCaseStatus" name="hideCaseStatus" value="${caseHandleInfoDTO.caseStatus }" type="hidden"/>
		<div id="record">
			<table id="recordDataGrid" style="width: 100%; height: auto">
			</table>
		</div> 	
		<input id="hideConfirmAuthorizes" name="hideConfirmAuthorizes" value="${caseHandleInfoDTO.confirmAuthorizes }" type="hidden"/>
	<script type="text/javascript">
	
	var recordData;
	<c:if test="${not empty caseHandleInfoDTO and caseHandleInfoDTO.caseStatus ne caseStatusAttr.WAIT_DISPATCH.code}" >
	$(function(){	
		setTimeout('initCaseActionPage();',15);
	//	dealDisabledControl('${caseHandleInfoDTO.caseStatus }');
	//	initCaseActionPage();
	});
	</c:if>
	/*初始化頁面*/
	function initCaseActionPage() {
		var	panelWidth =  parseInt($("#editDlg").css("width"), 10) * 0.97;
		$("#caseAction").panel({title:'案件處理',width:'99%'});
		
		<c:if test="${caseHandleInfoDTO.caseStatus ne caseStatusAttr.VOIDED.code and caseHandleInfoDTO.caseStatus ne caseStatusAttr.CLOSED.code and caseHandleInfoDTO.caseStatus ne caseStatusAttr.IMMEDIATE_CLOSE.code}" >
		if (${caseManagerFormDTO.isHiddenAllBtn}) { 
		//	caseButtonDeal(0);
			// Task #3070 案件管理 TMS等可新增記錄 CyberAgent不變 	//Task #3578 新增 客戶廠商客服
			if (${!caseManagerFormDTO.isCustomerService} && ${!caseManagerFormDTO.isCusVendorService} && ${!caseManagerFormDTO.isVendorService} && ${!caseManagerFormDTO.isTMS} && ${!caseManagerFormDTO.isQA} && ${caseManagerFormDTO.isCyberAgent}) {
				caseButtonDeal(0);
			} else {
				caseButtonDeal(2048);
			}
		} else {
			caseButtonByStatus('${caseHandleInfoDTO.caseStatus }');
		}
		casebuttonSetStatus("#button_change_complete_date", false);
		casebuttonSetStatus("#button_change_create_date", false);
		</c:if> 
		
		<c:if test="${caseHandleInfoDTO.caseStatus ne caseStatusAttr.DISPATCHED.code and caseHandleInfoDTO.caseStatus ne caseStatusAttr.RESPONSED.code }">
			casebuttonSetStatus("#button_lease_sign", false);
		</c:if> 
		$("#record").panel({title:'案件記錄',width:'99%'});
		var cols = [
			{title:'動作',field:'actionName',width:140,halign:'center',align:'left',sortable:true,formatter:function(value,row,index){return actionFormatter(value,row,index);}},
			{title:'動作后狀態',field:'caseStatusName',width:160,halign:'center',align:'left',sortable:true},
			{title:'處理說明',field:'description',width:400,halign:'center',align:'left',formatter:function(value,row,index){return actionHyperlinkEdit(value,row,index);}},
			{title:'實際執行時間',field:'dealDate',width:170,halign:'center',align:'center',sortable:true,formatter:formatToTimeStamp},
			{title:'記錄人員',field:'createdByName',width:120,halign:'center',align:'left',sortable:true},
			{title:'mailInfo',field:'mailInfo',width:160,halign:'center',align:'left',hidden:true,sortable:true},
			{title:'記錄日期',field:'createdDate',width:190,halign:'center',align:'center',sortable:true,formatter:formatToTimeStamp}
		];
		var options = {
				columns:[cols],
				singleSelect: true,
				method: 'post',
				nowrap:false,
				sortOrder:'asc'
		}
		queryCaseRecord('${caseHandleInfoDTO.isHistory}', true, options);
		//Task #3500
		//1. 若是CyberEDC，顯示 租賃授權確認、案件匯入、新增記錄、顯示記錄、派工、自動派工、線上排除、作廢、協調完成、到貨檢測、租賃預載、租賃配送中、租賃簽收、列印工單
		if($("#hideCyberEdc").val()=='1') {
		  	//Task #3549 若CyberEDC=是，開放 選擇 CyberEDC到場 是/否，預設否
			//CyberEDC=是 & CyberEDC到場=是，操作按鈕，比照 CyberEDC=否，然後移除 協調完成匯入、然後 新增 租賃授權確認、租賃授權取消、租賃預載
			if($("input:radio[name='queryMicroArrive']:checked").val()=='2'){
				if($("#caseAction").find("a[id=button_distribution]").length > 0){
					$("#caseAction").find("a[id=button_distribution]").remove();
				}
				if($("#caseAction").find("a[id=button_lease_sign]").length > 0){
					$("#caseAction").find("a[id=button_lease_sign]").remove();
				}
				if($("#caseAction").find("a[id=button_payment]").length > 0){
					$("#caseAction").find("a[id=button_payment]").find(".l-btn-text").text("求償");
					$("#caseAction").find("a[id=button_payment]").attr('onclick', '').unbind('click').click( function () { openDealGrid('arrivalInspection'); });
				}
			} else {
				//CyberEDC=是 & CyberEDC到場=否，操作按鈕維持現狀
				if($("#btn_responsed").length > 0){
			    	$("#btn_responsed").remove();
				}
			    if($("#btn_arrive").length > 0){
			    	$("#btn_arrive").remove();
				}
			    if($("#btn_completed").length > 0){
			    	$("#btn_completed").remove();
				}
			    if($("#btn_sign").length > 0){
			    	$("#btn_sign").remove();
				}
			    if($("#btn_back").length > 0){
			    	$("#btn_back").remove();
				}
			    if($("#btn_delay").length > 0){
			    	$("#btn_delay").remove();
				}
			    if($("#btn_rush_repair").length > 0){
			    	$("#btn_rush_repair").remove();
				}
			    if($("#btn_change_case_type").length > 0){
			    	$("#btn_change_case_type").remove();
				}
			    if($("#btn_closed").length > 0){
			    	$("#btn_closed").remove();
				}
			    if($("#button_change_complete_date").length > 0){
			    	$("#button_change_complete_date").remove();
				}
			    if($("#button_change_create_date").length > 0){
			    	$("#button_change_create_date").remove();
				}
			}
		}else{
			//3. 若不是CyberEDC，到貨檢測 按鈕 顯示為 求償     
			if($("#caseAction").find("a[id=button_payment]").length > 0){
				$("#caseAction").find("a[id=button_payment]").find(".l-btn-text").text("求償");
				$("#caseAction").find("a[id=button_payment]").attr('onclick', '').unbind('click').click( function () { openDealGrid('arrivalInspection'); });
			}
			//2. 若不是CyberEDC，則不顯示 租賃開頭的按鈕
			if($("#button_lease_preload").length > 0){
		    	$("#button_lease_preload").remove();
			}
			if($("#caseAction").find("a[id=button_distribution]").length > 0){
				$("#caseAction").find("a[id=button_distribution]").remove();
			}
			if($("#caseAction").find("a[id=button_lease_sign]").length > 0){
				$("#caseAction").find("a[id=button_lease_sign]").remove();
			}
		}

	}
	function caseButtonByStatus(caseStatus) {
		//待派工
		if ('${caseStatusAttr.WAIT_DISPATCH.code }' == caseStatus) {
		//	caseButtonDeal(1572);  //011000100100 -- 624
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(5668);  //1011000100100 --1624
			//已派工
		} else if ('${caseStatusAttr.DISPATCHED.code }' == caseStatus) {
		//	caseButtonDeal(3621);  //111000100101 -- e25
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(7717);  //1111000100101 --1e25
		//已回應
		} else if ('${caseStatusAttr.RESPONSED.code }' == caseStatus) {
		//	caseButtonDeal(3373); //110100101101 -- d2d
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(7469); //1110100101101 -- 1d2d
		//延期   Task #3123 延期也可以延期 
		} else if ('${caseStatusAttr.DELAYING.code }' == caseStatus) {
			//caseButtonDeal(3365);  //110100100101 --d25
		//	caseButtonDeal(3373);  //110100101101 --d2d
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(7469);  //1110100101101 --1d2d
		//到場
		} else if ('${caseStatusAttr.ARRIVED.code }' == caseStatus) {
		//	caseButtonDeal(3501);  //110110101101  --dad
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(7597);  //1110110101101  --1dad
		//完修
		} else if ('${caseStatusAttr.COMPLETED.code }' == caseStatus) {
			// caseButtonDeal(3424);  //110101100000  -- d60
			// Task #3113 完修可以退回客服
		//	caseButtonDeal(3440);  //110101110000  -- d70
			// Task #3205 案件僅能完修一次，即完修後不能線上排除
			caseButtonDeal(3408);  //110101010000  -- d50
		//待結案審查
		} else if ('${caseStatusAttr.WAIT_CLOSE.code }' == caseStatus) {
			caseButtonDeal(2066);  //100000010010  -- 812
		//結案
		} else if ('${caseStatusAttr.CLOSED.code }' == caseStatus) {
		//	caseButtonDeal(2048);  //100000000000  -- 800
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(6144);  //1100000000000  -- 1800
		//立即結案
		} else if ('${caseStatusAttr.IMMEDIATE_CLOSE.code }' == caseStatus) {
		//	caseButtonDeal(2048);  //100000000000  -- 800
			// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
			caseButtonDeal(6144);  //1100000000000  -- 1800
		//作廢
		} else if ('${caseStatusAttr.VOIDED.code }' == caseStatus) {
			caseButtonDeal(0);
		}
	}
	//案件按鈕處理，按鈕對應一個數字，一共12位數字，每位數字位，1標識可用，0標識不可用
	function caseButtonDeal(flag) {
		//1.新增記錄
		casebuttonSetStatus("#btn_add_record",(2048 & flag) == 2048);  //100000000000
		//2.派工和自動派工
		if ((1024 & flag) == 1024) {  //10000000000
			casebuttonSetStatus("#btn_dispatch",true);
			casebuttonSetStatus("#btn_auto_dispatch",true);
		} else {
			casebuttonSetStatus("#btn_dispatch",false);
			casebuttonSetStatus("#btn_auto_dispatch",false);
		}
		//3.回應
		casebuttonSetStatus("#btn_responsed",(512 & flag) == 512);  //1000000000
		//4.到場
		casebuttonSetStatus("#btn_arrive",(256 & flag) == 256);  //100000000
		//5完修
		casebuttonSetStatus("#btn_completed",(128 & flag) == 128);  //10000000
		//簽收
		casebuttonSetStatus("#btn_sign",(64 & flag) == 64);  //1000000
		//綫上排除
		casebuttonSetStatus("#btn_online_exclusion",(32 & flag) == 32);  //100000
		//退回
		casebuttonSetStatus("#btn_back",(16 & flag) == 16);  //10000
		//延期
		casebuttonSetStatus("#btn_delay",(8 & flag) == 8); //1000
		//催修，修改案件類型
		casebuttonSetStatus("#btn_rush_repair",(4 & flag) == 4);  //100
		// Task #3227 結案后可修改案件類型，需調整原修改案件類型取值
	//	casebuttonSetStatus("#btn_change_case_type",(4 & flag) == 4);
		casebuttonSetStatus("#btn_change_case_type",(4096 & flag) == 4096);  //1000000000000
		//結案
		casebuttonSetStatus("#btn_closed",(2 & flag) == 2); //10
		//立即結案，作廢  // CR #2951 廠商客服	      //Task #3578 新增 客戶廠商客服
		if (${caseManagerFormDTO.isCustomerService or caseManagerFormDTO.isVendorService or caseManagerFormDTO.isCusVendorService} && ((1 & flag) == 1)) {
			casebuttonSetStatus("#btn_void_case", true);
			casebuttonSetStatus("#btn_immediately_closing",true);
		} else {
			casebuttonSetStatus("#btn_void_case", false);
			casebuttonSetStatus("#btn_immediately_closing",false);
		}		
	}
	//按鈕顯示與隱藏，id-按鈕id，status：1顯示，0隱藏
	function casebuttonSetStatus(id,status) {
		var button = $(id);
		if (button.length > 0) {
			if (status) {
				button.attr("style", "width: auto;display:inline-block;");
			} else {
				button.attr("style", "width: auto;display:none;");
			}
		}		
	}
	
	/*
	* 處理返回按鈕
	*/
	function cancelEdit(){
		$.messager.confirm('確認對話框','確認取消?', function(confirm) {
			if (confirm) {
				viewDlg.dialog('close');
			}
		});
	}
	/*
	* 處理按鈕
	*/
	function dealButtonShow(caseStatus, caseId){
		// CR #2951 廠商客服			//Task #3578 新增 客戶廠商客服
		if (${caseManagerFormDTO.isCustomerService or caseManagerFormDTO.isVendorService or caseManagerFormDTO.isCusVendorService}) {
			caseButtonByStatus(caseStatus);
		} else {
			//判斷是否要隱藏所有按鈕
			if (checkCaseButtonAllHidden(caseId, caseStatus)) {
				caseButtonByStatus(caseStatus);
			} else {
			//	caseButtonDeal(0);
			// Task #3070 案件管理 TMS等可新增記錄 CyberAgent不變				//Task #3578 新增 客戶廠商客服
				if (${!caseManagerFormDTO.isCustomerService} && ${!caseManagerFormDTO.isVendorService} && ${!caseManagerFormDTO.isCusVendorService} && ${!caseManagerFormDTO.isTMS} && ${!caseManagerFormDTO.isQA} && ${caseManagerFormDTO.isCyberAgent}) {
					caseButtonDeal(0);
				} else {
					caseButtonDeal(2048);
				}
			}
		}
		if ('${caseStatusAttr.CLOSED.code }' == caseStatus || '${caseStatusAttr.IMMEDIATE_CLOSE.code }' == caseStatus) {
			casebuttonSetStatus("#button_change_complete_date", true);
			casebuttonSetStatus("#button_change_create_date", true);
		} else {
			casebuttonSetStatus("#button_change_complete_date", false);
			casebuttonSetStatus("#button_change_create_date", false);
		}
		if(caseStatus == '${caseStatusAttr.DISPATCHED.code }' || caseStatus == '${caseStatusAttr.RESPONSED.code }'){
			casebuttonSetStatus("#button_lease_sign", true);
		} else {
			casebuttonSetStatus("#button_lease_sign", false);
		}
	}
	//true:正常處理，false:全部隱藏
	function checkCaseButtonAllHidden(caseId, caseStatus) {
		var hiddenAllBtn = true;
		javascript:dwr.engine.setAsync(false);
		var departmentName;
		// 處理頁面使用當前的案件編號查詢出來的案件狀態
		ajaxService.getCaseInfoById(caseId, function(data){
			if(data){
				departmentName = data.dispatchDeptName;
				companyId = data.companyId;			
				// CR #2951 廠商客服	//Task #3578 新增 客戶廠商客服
				if (${caseManagerFormDTO.isCustomerService or caseManagerFormDTO.isVendorService or caseManagerFormDTO.isCusVendorService}) {
				} else {
					//CR #2394 增加cyberAgent update by 2017/09/13
					//不是VendorAgent 且 不是CyberAgent
					if((!${caseManagerFormDTO.isVendorAgent}) && (!${caseManagerFormDTO.isCyberAgent})){
						//既是tms又是QA時
						if (${caseManagerFormDTO.isTMS} && ${caseManagerFormDTO.isQA} && departmentName !='${caseRoleAttr.TMS.code}' && departmentName !='${caseRoleAttr.QA.code}') {
							hiddenAllBtn = false;
							//是tms時
						} else if (${caseManagerFormDTO.isTMS} && ${!caseManagerFormDTO.isQA} && departmentName !='${caseRoleAttr.TMS.code}') {
							hiddenAllBtn = false;
							//是QA時
						} else if (${caseManagerFormDTO.isQA} && ${!caseManagerFormDTO.isTMS} && departmentName !='${caseRoleAttr.QA.code}') {
							hiddenAllBtn = false;
						}
					} else {
						if(caseStatus !='WaitDispatch'){
							//維護廠商不為登陸者公司
							if(companyId != '${logonUser.companyId}') {
								if((!${caseManagerFormDTO.isTMS}) && (!${caseManagerFormDTO.isQA})){
									hiddenAllBtn = false;
								}
								//既是tms又是QA時
								else if (${caseManagerFormDTO.isTMS} && ${caseManagerFormDTO.isQA} && departmentName !='${caseRoleAttr.TMS.code}' && departmentName !='${caseRoleAttr.QA.code}') {
									hiddenAllBtn = false;
									//是tms時
								} else if (${caseManagerFormDTO.isTMS} && ${!caseManagerFormDTO.isQA} && departmentName !='${caseRoleAttr.TMS.code}') {
									hiddenAllBtn = false;
									//是QA時
								} else if (${caseManagerFormDTO.isQA} && ${!caseManagerFormDTO.isTMS} && departmentName !='${caseRoleAttr.QA.code}') {
									hiddenAllBtn = false;
								}
							} else {
								//維護廠商為登陸者公司
								if (departmentName =='${caseRoleAttr.TMS.code}' 
											|| departmentName =='${caseRoleAttr.QA.code}' 
											|| departmentName =='客服'){
									//$.messager.alert('提示訊息','TMS，QA只能處理派工給TMS或QA的案件','warning');
									if(departmentName =='${caseRoleAttr.TMS.code}' && ${!caseManagerFormDTO.isTMS}){
										hiddenAllBtn = false;
									}
									if(departmentName =='${caseRoleAttr.QA.code}' && ${!caseManagerFormDTO.isQA}){
										hiddenAllBtn = false;
									}
									if(departmentName =='客服'){
										hiddenAllBtn = false;
									}
								}
							}
						} else {
							hiddenAllBtn = false;
						}
					}
				}				
			}
		});
		javascript:dwr.engine.setAsync(true);
		return hiddenAllBtn;
	}
	/*
	* 處理禁用控件 客服、客戶才有權限操作按鈕，其他全部禁用
	* caseStatus ： 案件狀態
	*/
	function dealDisabledControl(caseStatus){
		$("#hideCaseStatus").val(caseStatus);
		// 客服
		var isCustomerService = false;
		// 客戶
		var isCustomer = false;
		// 其他
		var isOther = false;
		
 		/* if(${caseManagerFormDTO.isCustomerService }){
			isCustomerService = true;
		} else if(${caseManagerFormDTO.isCustomer } && ${!caseManagerFormDTO.isCustomerService }){
			isCustomer = true;
		} else if(${!caseManagerFormDTO.isCustomer } && ${!caseManagerFormDTO.isCustomerService } ){
			isOther = true;
		} */
		// CR #2951 廠商客服	//Task #3578 新增 客戶廠商客服
		if(${caseManagerFormDTO.isCustomerService } || ${caseManagerFormDTO.isCusVendorService } ){
			isCustomerService = true;
		} else if(${caseManagerFormDTO.isVendorService } && ${!caseManagerFormDTO.isCustomerService }){
			// 建案廠商給客服公司不等於當前
			if('${caseHandleInfoDTO.vendorServiceCustomer }' != '${logonUser.companyId }'){
				isOther = true;
			} else {
				isCustomerService = true;
			}							//Task #3578 新增 客戶廠商客服
		} else if(${caseManagerFormDTO.isCustomer } && ${!caseManagerFormDTO.isCustomerService } && ${!caseManagerFormDTO.isVendorService } && ${!caseManagerFormDTO.isCusVendorService }){
			isCustomer = true;				//Task #3578 新增 客戶廠商客服
		} else if(${!caseManagerFormDTO.isCustomer } && ${!caseManagerFormDTO.isCustomerService } && ${!caseManagerFormDTO.isVendorService } && ${!caseManagerFormDTO.isCusVendorService }){
			isOther = true;
		}
		// 禁用全部
		var isDisabledAll = false;
		// 其他角色或者客戶待派工之後
		if(isOther || (isCustomer && (caseStatus != '${caseStatusAttr.WAIT_DISPATCH.code}'))){
			// 處理按鈕
		//	if(caseStatus != '${caseStatusAttr.WAIT_CLOSE.code}'){
				$("#saveCaseBtn").attr("style", "width: auto;display:none;");
				// CR #2951 廠商客服 	//Task #3578 新增 客戶廠商客服
				if(${caseManagerFormDTO.isVendorService } && ${!caseManagerFormDTO.isCustomerService }){
					$("#dispatchCaseBtn").attr("style", "width: auto;display:none;");
				} else {
					$("#cancelCaseBtn").attr("style", "width: auto;display:none;");
				}
		//	}
			isDisabledAll = true;
		}
		// 處理是客服以及按鈕全部禁用的情況
		if(isCustomerService || isDisabledAll){
			// 結案 立即結案 作廢 Task #2682 進入待結案審查，就只能改 需求單號
			if((caseStatus == '${caseStatusAttr.CLOSED.code }')
					|| (caseStatus == '${caseStatusAttr.IMMEDIATE_CLOSE.code }')
					|| (caseStatus == '${caseStatusAttr.VOIDED.code }')
					|| (caseStatus == '${caseStatusAttr.WAIT_CLOSE.code}')
					|| isDisabledAll){
				if (caseStatus == '${caseStatusAttr.VOIDED.code }') {
					if(${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory}){
						var body = $("#transDataGrid").datagrid("getRows");
						if (body.length == 0){
							$("#btn_print").menubutton('disable');
						}
					}
				}
				// 裝機 + 異動
				if(${caseCategoryAttr.INSTALL.code eq caseHandleInfoDTO.caseCategory}
						|| ${caseCategoryAttr.UPDATE.code eq caseHandleInfoDTO.caseCategory}
						|| ${caseCategoryAttr.OTHER.code eq caseHandleInfoDTO.caseCategory}
						|| ${caseCategoryAttr.PROJECT.code eq caseHandleInfoDTO.caseCategory}){
					if((caseStatus == '${caseStatusAttr.VOIDED.code}') || isDisabledAll){
						// 需求單號
						$("#${stuff }_requirementNo").textbox('disable');
					}
					// 裝機
					if(${caseCategoryAttr.INSTALL.code eq caseHandleInfoDTO.caseCategory}
						|| ${caseCategoryAttr.OTHER.code eq caseHandleInfoDTO.caseCategory}){
						// 客戶
						$("#${stuff }_customer").combobox('disable');
						// 合約編號
						$("#${stuff }_contractId").combobox('disable');
						// 裝機類型
						if (${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory}) {
							$("#${stuff }_installType").combobox('disable');
						}
						// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
						/* // 維護廠商
						$("#${stuff }_companyId").combobox('disable'); */
						// 刷卡機型
						$("#${stuff }_edcType").combobox('disable');
						
						var isTmsObject = $("#isTms");
						// 處理isTms禁用
						if(isTmsObject.length > 0){
							isTmsObject.attr("disabled", true);
						}
						if (${caseCategoryAttr.OTHER.code eq caseHandleInfoDTO.caseCategory}) {
							// DTID
							$("#${stuff }_dtid").textbox('disable');
							// DTID勾選按鈕
							$("#getCaseInfoBtn").linkbutton('disable');
							// DTID查詢按鈕
							$("#queryCaseInfoBtn").linkbutton('disable');
						}
					// 異動
					} else {
						// DTID
						$("#${stuff }_dtid").textbox('disable');
						if (${caseCategoryAttr.UPDATE.code eq caseHandleInfoDTO.caseCategory}) {
							// 舊特店代號
							$("#${stuff }_oldMerchantCode").textbox('disable');
							// 是否同裝機作業
							$("#${stuff }_sameInstalled").combobox('disable');
						}
						// DTID勾選按鈕
						$("#getCaseInfoBtn").linkbutton('disable');
						// DTID查詢按鈕
						$("#queryCaseInfoBtn").linkbutton('disable');
					}
					if(${caseCategoryAttr.PROJECT.code eq caseHandleInfoDTO.caseCategory}){
						// 專案代碼
						$("#${stuff }_projectCode").textbox('disable');
						// 專案名稱
						$("#${stuff }_projectName").textbox('disable');
					}
					// 特店代號勾選按鈕
					$("#getMerchantBtn").linkbutton('disable');
					// 特店代號查詢按鈕
					$("#queryMerchantBtn").linkbutton('disable');
					if (${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory}){
						// 特店代號修改按鈕
						$("#editMerchantBtn").linkbutton('disable');
						// 特店表頭修改按鈕
						$("#editMerHeaderBtn").linkbutton('disable');
					}
					// 專案
					if (${caseCategoryAttr.PROJECT.code ne caseHandleInfoDTO.caseCategory}){
						var isProjects = document.getElementsByName("${stuff }_isProject");
						if(isProjects != null && isProjects.length >0){
							for(var i=0; i < isProjects.length; i++){
								isProjects[i].disabled = true;
							}
						}
					}
				//	$("#${stuff }_isProject").attr('disabled', true);
					// 案件類型
					$("#${stuff }_caseType").combobox('disable');
					// 預計完成日
					$("#${stuff }_expectedCompletionDate").datebox("disable");
					// 特店代號
					$("#${stuff }_merMid").textbox('disable');
					// 表頭（同對外名稱）
					$("#${stuff }_merchantHeaderId").combobox('disable');
					if (${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory} && ${caseCategoryAttr.PROJECT.code ne caseHandleInfoDTO.caseCategory}) {
						// 裝機地址 同營業地址
						$("#${stuff }_isBussinessAddress").attr('disabled', true);
						// 裝機地址 縣市
						$("#${stuff }_installedAdressLocation").combobox('disable');
						$("#${stuff }_installedPostCode").combobox('disable');
						// 裝機地址
						$("#${stuff }_merInstallAddress").textbox('disable');
						
						// 裝機聯絡人 同特店聯絡人
						$("#${stuff }_isBussinessContact").attr('disabled', true);
						// 裝機聯絡人
						$("#${stuff }_merInstallUser").textbox('disable');
						// 裝機聯絡人電話 同特店聯絡人電話
						$("#${stuff }_isBussinessContactPhone").attr('disabled', true);
						// 裝機聯絡人電話
						$("#${stuff }_merInstallPhone").textbox('disable');
						
						// 裝機連絡人手機 同特店聯絡人行動電話
						$("#${stuff }_isBussinessContactMobilePhone").attr('disabled', true);
						// 裝機連絡人手機
						$("#${stuff }_installedContactMobilePhone").textbox('disable');
						// 裝機聯絡人Email 同特店聯絡人Email
						$("#${stuff }_isBussinessContactEmail").attr('disabled', true);
						// 裝機聯絡人Email
						$("#${stuff }_installedContactEmail").textbox('disable');
					} else {
						// 裝機地址 縣市
						$("#${stuff }_installedAdressLocation").combobox('disable');
						$("#${stuff }_installedPostCode").combobox('disable');
						// 裝機地址
						$("#${stuff }_merInstallAddress").textbox('disable');
						// 聯繫地址 同營業地址
						$("#${stuff }_contactIsBussinessAddress").attr('disabled', true);
						// 聯繫地址 縣市
						$("#${stuff }_contactAddressLocation").combobox('disable');
						$("#${stuff }_contactPostCode").combobox('disable');
						// 聯繫地址
						$("#${stuff }_contactAddress").textbox('disable');
						// 聯繫聯絡人 同特店聯絡人
						$("#${stuff }_contactIsBussinessContact").attr('disabled', true);
						// 聯繫聯絡人
						$("#${stuff }_contactUser").textbox('disable');
						// 聯繫聯絡人電話 同特店聯絡人電話
						$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', true);
						// 聯繫聯絡人電話
						$("#${stuff }_contactUserPhone").textbox('disable');
						
						// 聯繫連絡人手機 同特店聯絡人行動電話
						$("#${stuff }_contactIsBussinessContactMobilePhone").attr('disabled', true);
						// 聯繫連絡人手機
						$("#${stuff }_contactMobilePhone").textbox('disable');
						// 聯繫聯絡人Email 同特店聯絡人Email
						$("#${stuff }_contactIsBussinessContactEmail").attr('disabled', true);
						// 聯繫聯絡人Email
						$("#${stuff }_contactUserEmail").textbox('disable');
					}
					//Receipt_type
					$("#${stuff }_receiptType").combobox('disable');
					// 軟體版本
					$("#${stuff }_softwareVersion").combobox('disable');
					// 內建功能
					$("#${stuff }_builtInFeature").combobox('disable');
					// 雙模組模式
					$("#${stuff }_multiModule").combobox('disable');
					// 週邊設備1
					$("#${stuff }_peripherals").combobox('disable');
					// 週邊設備功能1
					$("#${stuff }_peripheralsFunction").combobox('disable');
					// ECR連線
					$("#${stuff }_ecrConnection").combobox('disable');
					// 週邊設備2
					$("#${stuff }_peripherals2").combobox('disable');
					// 週邊設備功能2
					$("#${stuff }_peripheralsFunction2").combobox('disable');
					// 連接方式
					$("#${stuff }_connectionType").combobox('disable');
					
					// 週邊設備3
					$("#${stuff }_peripherals3").combobox('disable');
					// 週邊設備功能3
					$("#${stuff }_peripheralsFunction3").combobox('disable');
					// 本機IP
					$("#${stuff }_localhostIp").textbox('disable');
					// 寬頻連線
					$("#${stuff }_netVendorId").combobox('disable');
					// Gateway
					$("#${stuff }_gateway").textbox('disable');
					// Netmask
					$("#${stuff }_netmask").textbox('disable');
					// 其他說明
					$("#${stuff }_description").textbox('disable');
					
					// 電子發票載具
					$("#${stuff }_electronicInvoice").combobox('disable');
					// 銀聯閃付
					$("#${stuff }_cupQuickPass").combobox('disable');
					// LOGO
					$("#${stuff }_logoStyle").combobox('disable');
					// 是否開啟加密
					$("#${stuff }_isOpenEncrypt").combobox('disable');
					// 電子化繳費平台
					$("#${stuff }_electronicPayPlatform").combobox('disable');
					if (${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory}) {
						// 交易參數新增按鈕
						$("#btnAppendRow").linkbutton('disable');
						// 交易參數刪除按鈕
						$("#btnDelTrans").linkbutton('disable');
						// 不響應交易參數點擊事件
						$("#transDataGrid").datagrid({
							onDblClickRow: function(){
								return false;
							},
							onClickRow:  function(){
								return false;
							}
						});
						// TMS參數說明
						$("#${stuff }_tmsParamDesc").textbox('disable');
					}
					// 禁用交易參數
					blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1',width:'100%'}};
					// 附加檔案
				//	$("#caseAdd").block(blockOptions);
					$(".qq-uploader-selector").block(blockOptions);
					$("a.delete-button").hide();
				} else {
					if((caseStatus == '${caseStatusAttr.VOIDED.code}') || isDisabledAll){
						// 需求單號
						$("#${stuff }_requirementNo").textbox('disable');
					}
					/* if (${caseCategoryAttr.PROJECT.code eq caseHandleInfoDTO.caseCategory}) {
						//Receipt_type
						$("#${stuff }_receiptType").combobox('disable');
					} */
					// DTID
					$("#${stuff }_dtid").textbox('disable');
					// 案件類型
					$("#${stuff }_caseType").combobox('disable');
					// 預計完成日
					$("#${stuff }_expectedCompletionDate").datebox("disable");
					// 聯繫地址 同營業地址
					$("#${stuff }_contactIsBussinessAddress").attr('disabled', true);
					// 聯繫地址 縣市
					$("#${stuff }_contactAddressLocation").combobox('disable');
					$("#${stuff }_contactPostCode").combobox('disable');
					// 聯繫地址
					$("#${stuff }_contactAddress").textbox('disable');
					// 聯繫聯絡人 同特店聯絡人
					$("#${stuff }_contactIsBussinessContact").attr('disabled', true);
					// 聯繫聯絡人
					$("#${stuff }_contactUser").textbox('disable');
					// 聯繫聯絡人電話 同特店聯絡人電話
					$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', true);
					// 聯繫聯絡人電話
					$("#${stuff }_contactUserPhone").textbox('disable');
					// 聯繫連絡人手機 同特店聯絡人行動電話
					$("#${stuff }_contactIsBussinessContactMobilePhone").attr('disabled', true);
					// 聯繫連絡人手機
					$("#${stuff }_contactMobilePhone").textbox('disable');
					// 聯繫聯絡人Email 同特店聯絡人Email
					$("#${stuff }_contactIsBussinessContactEmail").attr('disabled', true);
					// 聯繫聯絡人Email
					$("#${stuff }_contactUserEmail").textbox('disable');
					// 其他說明
					$("#${stuff }_description").textbox('disable');
					
					// DTID勾選按鈕
					$("#getCaseInfoBtn").linkbutton('disable');
					// DTID查詢按鈕
					$("#queryCaseInfoBtn").linkbutton('disable');
					// 不響應交易參數點擊事件
					if(${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory}){
						$("#transDataGrid").datagrid({
							onDblClickRow: function(){
								return false;
							},
							onClickRow:  function(){
								return false;
							}
						});
					}
					// 禁用交易參數
					blockOptions = {message:null,overlayCSS:{backgroundColor:'#fff',cursor:'default',opacity:'0.1',width:'100%'}};
					// 附加檔案
				//	$("#caseAdd").block(blockOptions);
					$(".qq-uploader-selector").block(blockOptions);
					$("a.delete-button").hide();
					// 併機
					if(${caseCategoryAttr.MERGE.code eq caseHandleInfoDTO.caseCategory}){
						// 軟體版本
						$("#${stuff }_softwareVersion").combobox('disable');
 						// 交易參數新增按鈕
						$("#btnAppendRow").linkbutton('disable');
						// 交易參數刪除按鈕
						$("#btnDelTrans").linkbutton('disable');
						
						// 是否同裝機作業
						$("#${stuff }_sameInstalled").combobox('disable');
						// TMS參數說明
						$("#${stuff }_tmsParamDesc").textbox('disable');
					// 拆機
					} else if(${caseCategoryAttr.UNINSTALL.code eq caseHandleInfoDTO.caseCategory}){
						// 拆機類型
						$("#${stuff }_uninstallType").combobox('disable');
					// 查核
					} else if(${caseCategoryAttr.CHECK.code eq caseHandleInfoDTO.caseCategory}){
					// 專案
					} /* else if(${caseCategoryAttr.PROJECT.code eq caseHandleInfoDTO.caseCategory}){
						// 專案代碼
						$("#${stuff }_projectCode").textbox('disable');
						// 專案名稱
						$("#${stuff }_projectName").textbox('disable');
						//CR #3237
						var isTmsObject = $("#isTms");
						// 處理isTms禁用
						if(isTmsObject.length > 0){
							isTmsObject.attr("disabled", true);
						}
						// 交易參數新增按鈕
						$("#btnAppendRow").linkbutton('disable');
						// 交易參數刪除按鈕
						$("#btnDelTrans").linkbutton('disable');
						
						// 不響應交易參數點擊事件
						$("#transDataGrid").datagrid({
							onDblClickRow: function(){
								return false;
							},
							onClickRow:  function(){
								return false;
							}
						});
					// 報修
					} */ else if(${caseCategoryAttr.REPAIR.code eq caseHandleInfoDTO.caseCategory}){
						// 報修原因 
						$("#${stuff }_repairReason").combobox('disable');
					}
				}
				// 維護部門
				$("#${stuff }_departmentId").combobox('disable');
				// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
				// 維護廠商
				$("#${stuff }_companyId").combobox('disable');
			} 
			// 已派工 已回應 延期中 已到場 完修
			 else if((caseStatus == '${caseStatusAttr.DISPATCHED.code}') || (caseStatus == '${caseStatusAttr.RESPONSED.code}')
					|| (caseStatus == '${caseStatusAttr.DELAYING.code}') || (caseStatus == '${caseStatusAttr.ARRIVED.code}')
					|| (caseStatus == '${caseStatusAttr.COMPLETED.code}')){
				 // 裝機
				if(${caseCategoryAttr.INSTALL.code eq caseHandleInfoDTO.caseCategory}
						|| ${caseCategoryAttr.OTHER.code eq caseHandleInfoDTO.caseCategory}){
					// 客戶
					$("#${stuff }_customer").combobox('disable');
					// 合約編號
					$("#${stuff }_contractId").combobox('disable');
					// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
					/* // Task #2578 裝機已派工狀態可修改維護廠商
					if(caseStatus != '${caseStatusAttr.DISPATCHED.code}'){
						// 維護廠商
						$("#${stuff }_companyId").combobox('disable');
					} */
				}
				// Task #2578 已派工狀態可修改維護部門
				if(caseStatus != '${caseStatusAttr.DISPATCHED.code}'){
					// Task #3092 完修前(回應、到場)，都可以改維護部門
					if(caseStatus == '${caseStatusAttr.COMPLETED.code}'){
						// 維護部門
						$("#${stuff }_departmentId").combobox('disable');
					}
					
					// Task #3028 併機 異動 拆機 查核 專案 報修，建案之維護廠商，DTID預設帶出後要能再調整
					// 維護廠商
					$("#${stuff }_companyId").combobox('disable');
				} else {
					// Task #3113 完修退回至客服
					// 維護部門
					$("#${stuff }_departmentId").combobox('enable');
					// 維護廠商
					//Task #3460 若裝機類型為微型商戶，則維護廠商只能為經貿聯網
					if(!'${caseHandleInfoDTO.cmsCase }' == '${iatomsContantsAttr.PARAM_YES }' && 
							!'${caseHandleInfoDTO.companyId }' == '10000000-01'){
						$("#${stuff }_companyId").combobox('enable');
					}
					// 非裝機
					if(!${caseCategoryAttr.INSTALL.code eq caseHandleInfoDTO.caseCategory}){
						// DTID
						$("#${stuff }_dtid").textbox('enable');
						// DTID勾選按鈕
						$("#getCaseInfoBtn").linkbutton('enable');
						// DTID查詢按鈕
						$("#queryCaseInfoBtn").linkbutton('enable');
					}
				}
				
				// 待結案審查退回 爲完修
				if(caseStatus == '${caseStatusAttr.COMPLETED.code}'){
					// 案件類型
					$("#${stuff }_caseType").combobox('enable');
					// 預計完成日
					if($("#${stuff }_caseType").combobox('getValue') == '${iAtomsConstants.TICKET_MODE_APPOINTMENT}'){
						// 預計完成日
						$("#${stuff }_expectedCompletionDate").datebox("enable");
					}
					// 裝機 + 異動
					if(${caseCategoryAttr.INSTALL.code eq caseHandleInfoDTO.caseCategory}
							|| ${caseCategoryAttr.UPDATE.code eq caseHandleInfoDTO.caseCategory}
							|| ${caseCategoryAttr.PROJECT.code eq caseHandleInfoDTO.caseCategory}){
						// 軟體版本
						$("#${stuff }_softwareVersion").combobox('enable');
						// 內建功能
						$("#${stuff }_builtInFeature").combobox('enable');
						// 雙模組模式
						$("#${stuff }_multiModule").combobox('enable');
						// 週邊設備1
						$("#${stuff }_peripherals").combobox('enable');
						// 週邊設備功能1
						$("#${stuff }_peripheralsFunction").combobox('enable');
						// ECR連線
						$("#${stuff }_ecrConnection").combobox('enable');
						// 週邊設備2
						$("#${stuff }_peripherals2").combobox('enable');
						// 週邊設備功能2
						$("#${stuff }_peripheralsFunction2").combobox('enable');
						// 連接方式
						$("#${stuff }_connectionType").combobox('enable');
						
						// 週邊設備3
						$("#${stuff }_peripherals3").combobox('enable');
						// 週邊設備功能3
						$("#${stuff }_peripheralsFunction3").combobox('enable');
						// 本機IP
						$("#${stuff }_localhostIp").textbox('enable');
						// 寬頻連線
						$("#${stuff }_netVendorId").combobox('enable');
						// Gateway
						$("#${stuff }_gateway").textbox('enable');
						// Netmask
						$("#${stuff }_netmask").textbox('enable');
						// RECEIPT_TYPE
						$("#${stuff }_receiptType").combobox('enable');
						// 電子發票載具
						$("#${stuff }_electronicInvoice").combobox('enable');
						// 銀聯閃付
						$("#${stuff }_cupQuickPass").combobox('enable');
						// LOGO
						$("#${stuff }_logoStyle").combobox('enable');
						// 是否開啟加密
						$("#${stuff }_isOpenEncrypt").combobox('enable');
						// 電子化繳費平台
						$("#${stuff }_electronicPayPlatform").combobox('enable');
						
						// 裝機	
						if(${caseCategoryAttr.INSTALL.code eq caseHandleInfoDTO.caseCategory}){
							// 裝機類型
							$("#${stuff }_installType").combobox('enable');
						// 異動
						} else {
							// DTID
							$("#${stuff }_dtid").textbox('disable');
							// DTID勾選按鈕
							if('${caseHandleInfoDTO.isUpdateAsset }' != 'Y'){
								$("#getCaseInfoBtn").linkbutton('disable');
							}
							// DTID查詢按鈕
							$("#queryCaseInfoBtn").linkbutton('disable');
							// 刷卡機型
							$("#${stuff }_edcType").combobox('disable');
							if (${caseCategoryAttr.UPDATE.code eq caseHandleInfoDTO.caseCategory}) {
								// 舊特店代號
								$("#${stuff }_oldMerchantCode").textbox('enable');
								// 是否同裝機作業
								$("#${stuff }_sameInstalled").combobox('enable');
							}
						}
						// 特店代號勾選按鈕
						$("#getMerchantBtn").linkbutton('enable');
						// 特店代號查詢按鈕
						$("#queryMerchantBtn").linkbutton('enable');
						// 特店代號修改按鈕
						$("#editMerchantBtn").linkbutton('enable');
						// 特店表頭修改按鈕
						$("#editMerHeaderBtn").linkbutton('enable');
						// 專案
						var isProjects = document.getElementsByName("${stuff }_isProject");
						if(isProjects != null && isProjects.length >0){
							for(var i=0; i < isProjects.length; i++){
								isProjects[i].disabled = false;
							}
						}
						// 特店代號
						$("#${stuff }_merMid").textbox('enable');
						// 表頭（同對外名稱）
						$("#${stuff }_merchantHeaderId").combobox('enable');
						// TMS參數說明
						$("#${stuff }_tmsParamDesc").textbox('enable');
						if (${caseCategoryAttr.PRJECT.code ne caseHandleInfoDTO.caseCategory}) {
							// 裝機地址 同營業地址
							$("#${stuff }_isBussinessAddress").attr('disabled', false);
							if(!$("#${stuff }_isBussinessAddress").prop("checked")){
								// 裝機地址 縣市
								$("#${stuff }_installedAdressLocation").combobox('enable');
								$("#${stuff }_installedPostCode").combobox('enable');
								// 裝機地址
								$("#${stuff }_merInstallAddress").textbox('enable');
							}
							
							// 裝機聯絡人 同特店聯絡人
							$("#${stuff }_isBussinessContact").attr('disabled', false);
							if(!$("#${stuff }_isBussinessContact").prop("checked")){
								// 裝機聯絡人
								$("#${stuff }_merInstallUser").textbox('enable');
							}
							// 裝機聯絡人電話 同特店聯絡人電話
							$("#${stuff }_isBussinessContactPhone").attr('disabled', false);
							if(!$("#${stuff }_isBussinessContactPhone").prop("checked")){
								// 裝機聯絡人電話
								$("#${stuff }_merInstallPhone").textbox('enable');
							}
							// 裝機聯絡人電話 同特店聯絡人手機
							$("#${stuff }_isBussinessContactMobilePhone").attr('disabled', false);
							if(!$("#${stuff }_isBussinessContactMobilePhone").prop("checked")){
								// 裝機聯絡人手機
								$("#${stuff }_installedContactMobilePhone").textbox('enable');
							}
							// 裝機聯絡人電話 同特店聯絡人email
							$("#${stuff }_isBussinessContactEmail").attr('disabled', false);
							if(!$("#${stuff }_isBussinessContactEmail").prop("checked")){
								// 裝機聯絡人email
								$("#${stuff }_installedContactEmail").textbox('enable');
							}
						} else {
							// 聯繫地址 同營業地址
							$("#${stuff }_contactIsBussinessAddress").attr('disabled', false);
							if(!$("#${stuff }_contactIsBussinessAddress").prop("checked")){
								// 聯繫地址 縣市
								$("#${stuff }_contactAddressLocation").combobox('enable');
								$("#${stuff }_contactPostCode").combobox('enable');
								// 聯繫地址
								$("#${stuff }_contactAddress").textbox('enable');
							}
							
							// 聯繫聯絡人 同特店聯絡人
							$("#${stuff }_contactIsBussinessContact").attr('disabled', false);
							if(!$("#${stuff }_contactIsBussinessContact").prop("checked")){
								// 聯繫聯絡人
								$("#${stuff }_contactUser").textbox('enable');
							}
							
							// 聯繫聯絡人電話 同特店聯絡人電話
							$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', false);
							if(!$("#${stuff }_contactIsBussinessContactPhone").prop("checked")){
								// 聯繫聯絡人電話
								$("#${stuff }_contactUserPhone").textbox('enable');
							}
							// 聯繫聯絡人電話 同特店聯絡人手機
							$("#${stuff }_contactIsBussinessContactMobilePhone").attr('disabled', false);
							if(!$("#${stuff }_contactIsBussinessContactMobilePhone").prop("checked")){
								// 聯繫聯絡人手機
								$("#${stuff }_contactMobilePhone").textbox('enable');
							}
							// 聯繫聯絡人電話 同特店聯絡人email
							$("#${stuff }_contactIsBussinessContactEmail").attr('disabled', false);
							if(!$("#${stuff }_contactIsBussinessContactEmail").prop("checked")){
								// 聯繫聯絡人email
								$("#${stuff }_contactUserEmail").textbox('enable');
							}
							// 專案代碼
							$("#${stuff }_projectCode").textbox('enable');
							// 專案名稱
							$("#${stuff }_projectName").textbox('enable');
						}
						
						
						// 其他說明
						$("#${stuff }_description").textbox('enable');
						// 交易參數新增按鈕
						$("#btnAppendRow").linkbutton('enable');
						// 響應交易參數點擊事件
						$("#transDataGrid").datagrid({
							onDblClickRow: onDblClickRow,
							onClickRow: onClickRowtransData
						});
						// 附加檔案
						$(".qq-uploader-selector").unblock();
						$("a.delete-button").show();
						
					
					// 其他
					} else {
						// DTID
						$("#${stuff }_dtid").textbox('disable');
						// DTID勾選按鈕
					//	$("#getCaseInfoBtn").linkbutton('disable');
						if('${caseHandleInfoDTO.isUpdateAsset }' != 'Y'){
							$("#getCaseInfoBtn").linkbutton('disable');
						}
						// DTID查詢按鈕
						$("#queryCaseInfoBtn").linkbutton('disable');
						
						// 聯繫地址 同營業地址
						$("#${stuff }_contactIsBussinessAddress").attr('disabled', false);
						if(!$("#${stuff }_contactIsBussinessAddress").prop("checked")){
							// 聯繫地址 縣市
							$("#${stuff }_contactAddressLocation").combobox('enable');
							$("#${stuff }_contactPostCode").combobox('enable');
							// 聯繫地址
							$("#${stuff }_contactAddress").textbox('enable');
						}
						
						// 聯繫聯絡人 同特店聯絡人
						$("#${stuff }_contactIsBussinessContact").attr('disabled', false);
						if(!$("#${stuff }_contactIsBussinessContact").prop("checked")){
							// 聯繫聯絡人
							$("#${stuff }_contactUser").textbox('enable');
						}
						
						// 聯繫聯絡人電話 同特店聯絡人電話
						$("#${stuff }_contactIsBussinessContactPhone").attr('disabled', false);
						if(!$("#${stuff }_contactIsBussinessContactPhone").prop("checked")){
							// 聯繫聯絡人電話
							$("#${stuff }_contactUserPhone").textbox('enable');
						}
						
						// 其他說明
						$("#${stuff }_description").textbox('enable');
						
						// 響應交易參數點擊事件
						if(${caseCategoryAttr.OTHER.code ne caseHandleInfoDTO.caseCategory}){
							$("#transDataGrid").datagrid({
								onDblClickRow: onDblClickRow,
								onClickRow: onClickRowtransData
							});
						}
						// 附加檔案
						$(".qq-uploader-selector").unblock();
						$("a.delete-button").show();
						// 併機
						if(${caseCategoryAttr.MERGE.code eq caseHandleInfoDTO.caseCategory}){
							// 軟體版本
							$("#${stuff }_softwareVersion").combobox('enable');
							// 交易參數新增按鈕
							$("#btnAppendRow").linkbutton('enable');
							
							// 是否同裝機作業
							$("#${stuff }_sameInstalled").combobox('enable');
							// TMS參數說明
							$("#${stuff }_tmsParamDesc").textbox('enable');
						// 拆機
						} else if(${caseCategoryAttr.UNINSTALL.code eq caseHandleInfoDTO.caseCategory}){
							// 拆機類型
							$("#${stuff }_uninstallType").combobox('enable');
						// 查核
						} else if(${caseCategoryAttr.CHECK.code eq caseHandleInfoDTO.caseCategory}){
						/* // 專案
						} else if(${caseCategoryAttr.PROJECT.code eq caseHandleInfoDTO.caseCategory}){
							// 專案代碼
							$("#${stuff }_projectCode").textbox('enable');
							// 專案名稱
							$("#${stuff }_projectName").textbox('enable');
							//
							$("#${stuff }_receiptType").combobox('enable'); */
						// 報修
						} else if(${caseCategoryAttr.REPAIR.code eq caseHandleInfoDTO.caseCategory}){
							// 報修原因 
							$("#${stuff }_repairReason").combobox('enable');
						}
					}
				}
			} 
		}
	}
	/*
	* 查詢案件歷程
	* isHistory：是否查歷史表
	*/
	function queryCaseRecord(isHistory, ignoreBlock, option){
		// 得到案件編號
		var caseId = $("#hideCaseId").val();
		if(!isHistory){
			isHistory = '${caseHandleInfoDTO.isHistory}';
		}
		if(ignoreBlock){
		} else {
			ignoreBlock = false;
		}
		// 查詢參數
		var params = {caseId : caseId, isHistory : isHistory};
		var options = {
			url : "${contextPathAction}/caseHandle.do?actionId=<%=IAtomsConstants.ACTION_QUERY_TRANSACTION%>",
			queryParams :params,
			isOpenDialog:true,
			ignoreFirstLoad:ignoreBlock,
			onLoadSuccess:function(data){
			},
			onLoadError : function() {
			}
		}
		if(typeof option != 'undefined') {
			options.columns = option.columns;
			options.singleSelect = option.singleSelect;
			options.method = option.method;
			options.nowrap = option.nowrap;
			options.sortOrder = option.sortOrder;
		}
		// 調用公用查詢方法
		openDlgGridQuery("recordDataGrid", options);
		if(ignoreBlock){
			// 調取消保存遮罩
			commonCancelSaveLoading('editDlg');
		}
	}

	/*
	* 打開處理的datagrid
	*/
		function openDealGrid(actionId,leaseSignFlag){
			$("#dataGridResponse-msg").text("");
			// 得到案件編號
			var caseId = $("#hideCaseId").val();
			if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') && leaseSignFlag=='Y' && $("#hideLeaseSignFlag").length>0){
				$("#hideLeaseSignFlag").val('Y');
			} else {
				$("#hideLeaseSignFlag").val('N');
			}
			// 立即結案
			if(actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }'){
				// 未派工給客服則不能立即結案 // CR #2951 廠商客服	//Task #3578 新增 客戶廠商客服
				if(${caseManagerFormDTO.isCustomerService  or caseManagerFormDTO.isVendorService or caseManagerFormDTO.isCusVendorService}){
					javascript:dwr.engine.setAsync(false);
					var breakFlag = false;
					ajaxService.getCaseInfoById(caseId, function(data){
						if(data){
							if((data.caseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }')
									&& (data.dispatchDeptId != 'CUSTOMER_SERVICE')){
								$.messager.alert('提示訊息','案件狀態不符，不可進行協調完成','warning');
								breakFlag = true;
							} else {
								// Task #3113 完修可以退回客服 簽收設備后不能作廢
								if(data.hasRetreat == 'Y'){
									$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+'此案件已經過簽收或線上排除，不可進行協調完成','warning');
									breakFlag = true;
								}
							}
						}
					});
					javascript:dwr.engine.setAsync(true);
					if(breakFlag){
						return false;
					};
				}
			}
			// 完修
			if(actionId == '${caseActionAttr.COMPLETE.code }') {
				//CR #2551   驗證重複進建 當前一筆為裝機時，前案件必須完修  2017/12/07
				if (('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.INSTALL.code }') && ('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.OTHER.code }')) {
					javascript:dwr.engine.setAsync(false);
					flag = false;
					ajaxService.getCountByInstall('${caseHandleInfoDTO.dtid}', caseId, function(data){
						if(data=='Y'){
							$.messager.alert('提示訊息',"來自"+caseId+"訊息：DTID之裝機案件不存在，請派工客服作廢",'');
							flag = true;
						}
					})
					javascript:dwr.engine.setAsync(true);
					if(flag){
						return false;
					}
				
					javascript:dwr.engine.setAsync(false);
					var flag = false;
					if('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.OTHER.code }'){
						ajaxService.getCaseRepeatByInstallUncomplete('${caseHandleInfoDTO.dtid}',caseId, function(data){
							if(data.flag){
								if(data.caseId != null && data.caseId != ''){
									$.messager.alert('提示訊息',"來自"+caseId+"訊息：請客服先將 "+ data.caseId +"結案<br>若已結案，因缺少設備資料，請聯繫系統管理員。",'');
									flag = true;
								}
							}
						})
					}
					javascript:dwr.engine.setAsync(true);
					if(flag){
						return false;
					} 
				}
			}
			// 退回
			if(actionId == '${caseActionAttr.RETREAT.code }'){
				// Task #3113 拆機件簽收、線上排除后不可完修退回
				javascript:dwr.engine.setAsync(false);
				var breakFlag = false;
				ajaxService.getCaseInfoById(caseId, function(data){
					if(data){
						if(${caseCategoryAttr.UNINSTALL.code eq caseHandleInfoDTO.caseCategory} && (data.caseStatus == '${caseStatusAttr.COMPLETED.code }') && (data.hasRetreat == 'Y')){
							$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+'拆機案件已經過簽收或線上排除，不可進行退回','warning');
							breakFlag = true;
						}
					}
				});
				javascript:dwr.engine.setAsync(true);
				if(breakFlag){
					return false;
				};
			}
			// 籤收、線上排除驗證重復進件 
			//CR #2551 將完修重複進件與設備異動改為 在簽收時做              補充：除了裝機都需驗證  update at 2017/12/12 
			if((actionId == '${caseActionAttr.SIGN.code }') 
				|| (actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')
				|| (actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }')) {
				
				/* if ((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }') && ('${caseHandleInfoDTO.caseCategory}' == '${caseCategoryAttr.INSTALL.code }')) {
					var confirmAuthorizes = $("#hideConfirmAuthorizes").val();
					if (confirmAuthorizes == 'N' && ${caseHandleInfoDTO.caseCategory eq caseCategoryAttr.INSTALL.code } 
							&& ${caseHandleInfoDTO.installType eq iAtomsConstants.PARAM_INSTALL_TYPE_4 }) {
						$.messager.alert('提示訊息', '雲端租賃設備，不可線上排除','warning');
						return false;
					}
				} */
				//Bug #3067 裝機也要卡重複進件
				if (('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.INSTALL.code }') && ('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.OTHER.code }')) {
						//
					if((actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')
						|| (actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }')){
						javascript:dwr.engine.setAsync(false);
						flag = false;
						ajaxService.getCountByInstall('${caseHandleInfoDTO.dtid}', caseId, function(data){
							if(data=='Y'){
								$.messager.alert('提示訊息',"來自"+caseId+"訊息：DTID之裝機案件不存在，請派工客服作廢",'');
								flag = true;
							}
						})
						javascript:dwr.engine.setAsync(true);
						if(flag){
							return false;
						}
					}
				}
				// 驗證重復進件
				javascript:dwr.engine.setAsync(false);
				var isSignFlag = false;
				//CR #2551 簽收傳true 其他false
				if(actionId == '${caseActionAttr.SIGN.code }'){
					isSignFlag = true;
				}
				var flag = false;
				if('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.OTHER.code }'){
					ajaxService.getCaseRepeatList('${caseHandleInfoDTO.dtid}', caseId, isSignFlag, function(data){
						if(data.flag){
							for(var key in data) { 
								if(key != "flag"){
									$.messager.alert('提示訊息',"來自"+key+"訊息：請客服先將"+ data[key] +"結案",'');
									flag = true;
									break;
								}
							} 
						}
					})
				}
				javascript:dwr.engine.setAsync(true);
				if(flag){
					return false;
				}
				if ('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.INSTALL.code }' && '${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.OTHER.code }') {
					//判斷改案件的設備關聯是否被修改
					javascript:dwr.engine.setAsync(false);
					var isChange = false;
					var hasUpdateCase = false;
					ajaxService.getCaseLinkIsChange('${caseHandleInfoDTO.dtid}', caseId, function(data){});
					ajaxService.getCaseLinkIsChange('${caseHandleInfoDTO.dtid}', caseId, function(data){
						if(data.flag){
							//最新資料檔有設備時 2018/01/30
							if(data.initEditCheckUpdate=='N'){
								// CR #2951 廠商客服	//Task #3578 新增 客戶廠商客服
								if (${caseManagerFormDTO.isCustomerService or caseManagerFormDTO.isCusVendorService}) {
									$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
									+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
								} else {
									//CR #2551 3. 設備資料 與 最新資料檔 不一致，提醒USER 聯繫客服 2017/12/07
									// CR #2951 廠商客服
									if (${caseManagerFormDTO.isVendorService }) {
										if('${caseHandleInfoDTO.vendorServiceCustomer }' == '${logonUser.companyId }'){
											$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
													+ data["closeDate"] +"已更新案件最新設備連接資料，請至處理頁面點✔重新帶入最新資料",'');
										} else {
											$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
													+ data["closeDate"] +"異動設備與此案件不符，請聯繫Cyber客服確認",'');
										}
									} else {
										$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+ data["caseId"] +"於"
												+ data["closeDate"] +"異動設備與此案件不符，請聯繫客服確認",'');
									}
								}
							} else {
								$.messager.alert('提示訊息',"來自"+caseId+"訊息： 本案件dtid之EDC設備已拆除，請退回客服作廢",'');
							}
							isChange = true;
						} else {
							if(data.assetLinkIsChange){
								
								hasUpdateCase = true
							} 
						}
					});
					if(hasUpdateCase){
						//var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
						//2551 更新安檢設備連接檔
						javascript:dwr.engine.setAsync(false);
							ajaxService.getChangeCaseInfoById(caseId, function(data){
								if(data){
									$("#hideUpdateDate").val(data.updatedDate);
									//加遮罩
									//$.blockUI(blockStyle);
									//處理頁面調用查詢
									var pageIndex = getGridCurrentPagerIndex("dg");
									query(pageIndex, false);
									queryCaseRecord('N', true);
									// 去除遮罩
							//$.unblockUI();
								}
							});
					} 
					javascript:dwr.engine.setAsync(true);
					if(isChange){
						return false;
					};
					
					// Bug #2331 若設備異動則不能立即結案
					if(actionId == '${caseActionAttr.IMMEDIATELY_CLOSING.code }'){
						//驗證該案件設備是否被之前同dtid的案件修改過
						javascript:dwr.engine.setAsync(false);
						var flag = false;
						ajaxService.isChangeAsset(caseId, function(data){
							if(data){
								$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+"案件設備有異動，不可進行協調完成",'warning');
								flag = true;
							}
						});
						javascript:dwr.engine.setAsync(true);
						if(flag){
							return false;
						};
					}
				}
			}
			var responsibity = '${caseHandleInfoDTO.responsibity}';
			var problemReason = '${caseHandleInfoDTO.problemReason}';
			var problemReasonCode = '${caseHandleInfoDTO.problemReasonCode}';
			var problemSolution = '${caseHandleInfoDTO.problemSolution}';
			var problemSolutionCode = '${caseHandleInfoDTO.problemSolutionCode}';
			//Bug #2276 處理頁面簽收后直接結案dto中沒有問題原因等相關的值 需要從db中查詢 2017/11/16
			if(actionId == ('${caseActionAttr.CLOSED.code }')){
				
				javascript:dwr.engine.setAsync(false);
				// 處理頁面使用當前的案件編號查詢出來的案件狀態
				ajaxService.getCaseInfoById(caseId, function(data){
					if(data){
						problemReason = data.problemReason;
						problemReasonCode = data.problemReasonCode;
						problemSolution = data.problemSolution;
						problemSolutionCode = data.problemSolutionCode;
						responsibity = data.responsibity;
					}
				});
				javascript:dwr.engine.setAsync(true);
				
			}
			// 所有參數綁定
			var allParams = {
					actionId : actionId,
					datagridId : 'dataGridResponse',
					panelId : 'responsePanel',
					caseStatus : '${caseHandleInfoDTO.caseStatus}',
					caseCategory : '${caseHandleInfoDTO.caseCategory}',
					flag : true,
					caseId : caseId,
					isTms : '${caseHandleInfoDTO.isTms}',
					responsibity:responsibity,
					problemReason:problemReasonCode+"-"+problemReason,
					problemSolution:problemSolutionCode+"-"+problemSolution
					
			};
			// 處理頁面調用打開案件動作處理datagrid
		//	dealCreateGrid(actionId, 'dataGridResponse', '${caseHandleInfoDTO.caseStatus}', '${caseHandleInfoDTO.caseCategory}', true, caseId);
			//Task #2542
			if(actionId == '${caseActionAttr.DISPATCHING.code }' || actionId == ('${caseActionAttr.AUTO_DISPATCHING.code }')){
				if ('${caseHandleInfoDTO.caseCategory}' == '${caseCategoryAttr.INSTALL.code }') {
					var confirmAuthorizes = $("#hideConfirmAuthorizes").val();
					if (confirmAuthorizes == 'N' && ${caseHandleInfoDTO.caseCategory eq caseCategoryAttr.INSTALL.code } 
							&& ${caseHandleInfoDTO.installType eq iAtomsConstants.PARAM_INSTALL_TYPE_4 }) {
						$.messager.alert('提示訊息', '尚未授權確認，不可派工','warning');
						return false;
					}
				}
				var departmentName = null;
				javascript:dwr.engine.setAsync(false);
				// 處理頁面使用當前的案件編號查詢出來的案件狀態
				ajaxService.getCaseInfoById(caseId, function(data){
					if(data){
						allParams.departmentName = data.dispatchDeptName;
						//CR #2394 加cyberagent角色
						allParams.companyId = data.companyId;
					}
				});
				javascript:dwr.engine.setAsync(true);
				if(!validateQaAndTms(allParams)){
					return false;
				}
			}
			// 打開案件動作的panel
			if(!$('#responsePanel').hasClass("easyui-panel")){
				$('#responsePanel').panel();
			}
			$("#responsePanel").attr("style","diaplay:block;");
			$('#responsePanel').panel('open');
 			if(!$('#dataGridResponse').hasClass("easyui-datagrid")){
				$('#dataGridResponse').datagrid();
			}
			if(!$('#dataGridAssetLink').hasClass("easyui-datagrid")){
				$('#dataGridAssetLink').datagrid();
			}
			if(!$('#dataGridSuppliesLink').hasClass("easyui-datagrid")){
				$('#dataGridSuppliesLink').datagrid();
			}
			dealCreateGridInfo(allParams);
			if((actionId == '${caseActionAttr.SIGN.code }') || (actionId == '${caseActionAttr.ONLINE_EXCLUSION.code }')) {
				if('${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.CHECK.code}' && '${caseHandleInfoDTO.caseCategory}' != '${caseCategoryAttr.OTHER.code}') {
					var datagridColums = [];
					var assetLinkDatagrid = $('#dataGridAssetLink');
					var assetLinkDatagridID = assetLinkDatagrid.attr("id");
					var suppliesLinkDatagrid = $('#dataGridSuppliesLink');
					var queryData = {caseId : '${caseHandleInfoDTO.caseId}', dtid : '${caseHandleInfoDTO.dtid}'};
					var customerId = '${caseHandleInfoDTO.customerId}';
					var querySuppliesDate = {caseId : '${caseHandleInfoDTO.caseId}',customerId : customerId, caseCategory : '${caseHandleInfoDTO.caseCategory}'};
					var isExpandRow = false;
					var index;
					datagridColums = createCaseAssetLinkDatagridColums(caseId, customerId, assetLinkDatagridID,'${caseHandleInfoDTO.caseCategory}');
					createAssetAndSuppliesLinkOptions(isExpandRow,assetLinkDatagrid,suppliesLinkDatagrid, datagridColums, querySuppliesDate, index, queryData);
				}
			} else {
				// 隱藏panel邊線
				$("#dataGridAssetLink").closest('.panel').attr("style","display:none;");
				$("#dataGridSuppliesLink").closest('.panel').attr("style","display:none;");
			}
		}
	/*
	 * 作廢按鈕
	 */
	 function VoidCaseView(){
	 	var caseId = $("#hideCaseId").val();
		// CR #2951 廠商客服				//Task #3578 新增 客戶廠商客服
	 	if(${caseManagerFormDTO.isCustomerService  or caseManagerFormDTO.isVendorService or caseManagerFormDTO.isCusVendorService}){
			javascript:dwr.engine.setAsync(false);
			var breakFlag = false;
			ajaxService.getCaseInfoById(caseId, function(data){
				if(data){
					if((data.caseStatus != '${caseStatusAttr.WAIT_DISPATCH.code }')
							&& (data.dispatchDeptId != 'CUSTOMER_SERVICE')){
						$.messager.alert('提示訊息','案件狀態不符，不可進行作廢','warning');
						breakFlag = true;
					} else {
						// Task #3113 完修可以退回客服 簽收設備后不能作廢
						if(data.hasRetreat == 'Y'){
							$.messager.alert('提示訊息',"來自"+caseId+"訊息： "+'此案件已經過簽收或線上排除，不可進行作廢','warning');
							breakFlag = true;
						}
					}
				}
			});
			javascript:dwr.engine.setAsync(true);
			if(breakFlag){
				return false;
			};
		}
	 	showVoidView('dataGridResponse', caseId, $("#hideCaseStatus").val());
	 }
	function actionFormatter(value,row,index){
		var actionTitle;
		actionTitle = row.mailInfo;
		if(actionTitle == null) {
			return value;
		} else {
			return "<a href='javascript:void(0)' class=\"easyui-tooltip\" title='" + actionTitle + "'>"+value+"</a>";
		}
	}
	
	/*
	* 驗證派工按鈕
	*/
	function validateDispatch(){
		if(${caseHandleInfoDTO.caseStatus eq caseStatusAttr.WAIT_DISPATCH.code }
				&& ${caseCategoryAttr.MERGE.code eq caseHandleInfoDTO.caseCategory}){
			var rows = $("#transDataGrid").datagrid("getRows");
			if(!rows || rows == null || rows.length == 0){
				$.messager.alert('提示訊息', '案件編號' + '${caseHandleInfoDTO.caseId}' + '，請設定交易參數', 'warning');
				return false;
			}
		}
		var confirmAuthorizes = $("#hideConfirmAuthorizes").val();
		if (confirmAuthorizes == 'N' && ${caseHandleInfoDTO.caseCategory eq caseCategoryAttr.INSTALL.code } 
				&& ${caseHandleInfoDTO.installType eq iAtomsConstants.PARAM_INSTALL_TYPE_4 }) {
			$.messager.alert('提示訊息', '尚未授權確認，不可派工','warning');
			return false;
		}
		comfirmDispatch("${caseHandleInfoDTO.caseCategory}");
	}
	function actionHyperlinkEdit(value,row,index){
		//判斷是否有物流編號，若有，則添加網絡郵局的超鏈接
		if(!isEmpty(value)){
			var str = value.replaceAll('\n','<br>');
			if(str != null && str.indexOf("物流編號：") != -1 ){
				//var title = '網路郵局';
			    //var url = 'http://postserv.post.gov.tw/pstmail/main_mail.html';
			    //var icon = 'icon-sys';
			   	//return arr[0] + '物流編號：' + '<a style=\"color:blue\" href="#" onclick="self.parent.addTab(\'' + title + '\',\'' + url + '\',\'' + icon + '\')">' +arr[1]+ '</a>';
				var arr = str.split("物流編號："); 
			    return arr[0] + '物流編號：' + '<a href=\"javascript:void(0)\" class=\"easyui-tooltip\" onclick=\"openHttpEdit(\''+row.logisticsVendorEmail+'\');\">' +arr[1]+ '</a>';
			}else{
				return str;
			}
		}else{
			return value;
		}
	}
	function openHttpEdit(url){
		window.open(url);
	}
	</script>
</c:if>
