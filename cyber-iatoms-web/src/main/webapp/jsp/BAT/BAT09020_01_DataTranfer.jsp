<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.dataTransfer.formDTO.OldDataTransferFormDTO"%>
<%@ page import="cafe.core.util.DateTimeUtils"%>

<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	OldDataTransferFormDTO formDTO = null;
	String logMessage = null;
	if (ctx != null) {
		// 得到FormDTO
		formDTO = (OldDataTransferFormDTO) ctx.getRequestParameter();
		if (formDTO != null) {
			logMessage = formDTO.getLogMessage();
		} else {
			formDTO = new OldDataTransferFormDTO();
		}
	} else {
		formDTO = new OldDataTransferFormDTO();
	}
//案件類別
	List<Parameter> caseCategoryList = (List<Parameter>) SessionHelper.getAttribute(request,
			IAtomsConstants.UC_NO_BAT_09020, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
%>
<html>
<c:set var="caseCategoryList" value="<%=caseCategoryList%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="logMessage" value="<%=logMessage%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>舊資料轉檔</title>
</head>
<body>
<div style="width: 100%; overflow-x:hidden; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height">
            <div class="ftitle">
            <table style="width: auto; height: auto">
           <tr>
				<td style="width: 18%;" colspan="2">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="setup();">部署流程圖</a>
				</td>
				<!-- <td style="width: 18%;" >
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="testSpeed();">測試DB連接速度</a>
				</td> -->
				
				<td style="width: 18%;" >
					<a href="javascript:void(0)" class="easyui-linkbutton c6 red" style="width: auto" onclick="updateTransaction();" >更新交易參數</a>
				</td>
				
				<td style="width: 18%;" >
					<a href="javascript:void(0)" class="easyui-linkbutton c6 red" style="width: auto" onclick="insertAssetLink();" disabled>增加設備鏈接</a>
				</td>
				
				<td style="width: 15%;" >
					<a href="javascript:void(0)" class="easyui-linkbutton c6 red" style="width: auto" onclick="updateSimAssetLink();" disabled>案件SIM卡設備調整</a>
				</td>
				
				<td style="width: 18%;" >
					<a href="javascript:void(0)" class="easyui-linkbutton c6 red" style="width: auto" onclick="updateSmartpayTrans();" disabled>Smartpay退貨交易更新</a>
				</td>
				<td style="width: 15%;" >
					<a href="javascript:void(0)" class="easyui-linkbutton c6 red" style="width: auto" onclick="updateManualInput();">一般交易人工輸入更新</a>
				</td>
			</tr>
            </table>
            </div>
            <div class="ftitle">
           <table style="width: auto; height: auto">
           <tr>
				<td style="width: 18%;">
					<input type="checkbox" id="allCheckButton" onclick="checkAll();">全選
				</td>
			</tr>
			<tr style="height: 10px;"></tr>
			<tr>
				<td style="width: 18%">
					<input type="checkbox" id="transferCalendar" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferCalendar');">行事曆轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferFaultParamData" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferFaultParamData');">故障參數轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferProblemData" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferProblemData');">報修參數轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferCompanyData" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferCompanyData');">公司資料轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferWarehouse" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferWarehouse');">倉庫據點轉入</a>
				</td>
			</tr>
			<tr>
				<td style="width: 18%">
					<input type="checkbox" id="transferAssetType" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferAssetType');">設備品項轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferApplicaton" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferApplicaton');">程式版本轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferMerchant" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferMerchant');">客戶特店、表頭轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferContract" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferContract');">合約資料轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferAdmUser" class="single-select-checkbox">
 					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferAdmUser');">使用者帳號資料轉入</a>
 				</td>
			</tr>
			<tr>
				<td style="width: 18%">
					<input type="checkbox" id="transferRepository" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferRepository');">設備庫存轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferHistoryRepository" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferHistoryRepository');">設備庫存歷史轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferCaseHandleInfo" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferCaseHandleInfo');">案件處理資料轉入</a>
				</td>
				<td style="width: 18%">
					<input type="checkbox" id="transferCaseNewHandleInfo" class="single-select-checkbox">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferData('transferCaseNewHandleInfo');">案件最新處理資料轉入</a>
				</td> 
			</tr>
			<tr style="height: 20px"></tr>
			<tr>
				<td style="width: 18%">
					<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="selectTransfer();">批量轉入</a>
				</td>
			</tr>
			</table>
				<br/>
				<table style="width: auto; height: auto">
					<tr>
						<td width="70">案件類別:</td>
						<td width="250">
							<cafe:droplisttag
								id="caseCategory"
								name="caseCategory" 
								css="easyui-combobox"
								hasBlankValue="true"
								result="${caseCategoryList}"
								blankName="請選擇"
								style="width: 200px"
								javascript="editable=false panelheight=\"auto\" required=true validType='requiredDropList' invalidMessage=\"請輸入案件類別\""
							></cafe:droplisttag>
						</td>
						<td>
							<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="exportInfo();">未結案案件轉換</a>
						</td>
					</tr>
				</table>
				
				<br/>
            </div>
            <span>輸入格式(每行一個案件編號):<br/>
						&nbsp;&nbsp;HS1710167380<br/>
						&nbsp;&nbsp;YS1606216914</span>
            	<table style="width: auto; height: auto">
            	
					<tr>
						<td width="110">轉入案件編號:</td>
						<td width="400">
							<textarea id="transferCaseId" 
                        	name="transferCaseId"
		                 	class="easyui-textbox" required="true"
		                 	data-options="multiline:true" 
		                 	style="height: 100px;width: 300px"></textarea>
						</td>
						<td>
							<a href="javascript:void(0)" class="easyui-linkbutton c6" style="width: auto" onclick="transferCaseHandle();">轉入案件</a>
						</td>
					</tr>
				</table>
            <div><span id="msg">${logMessage }</span></div>
</div>
<script type="text/javascript">
// 頁面加載完成
$(function(){
	$(".single-select-checkbox").click(function(){
		var allCheckbox = $(".single-select-checkbox");
		var count = 0;
		var tempObj;
		for(var i=0; i<allCheckbox.length; i++){
			tempObj = allCheckbox[i];
			if($(tempObj).prop("checked")){
				count ++;
			}
		}
		if(count == 14){
			$("#allCheckButton").prop("checked", true)
		} else {
			$("#allCheckButton").prop("checked", false)
		}
	});
})
/**
 * 測試DB鏈接速度
 */
function testSpeed(){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "testSpeed"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
		},
		error:function(){
			$.unblockUI();	
		}
	});
}
/**
 * 更新交易參數
 */
function updateTransaction(){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "updateTransaction"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			if (json.success) {
				$("#msg").append("更新交易參數成功").append('</br>');
			} else {
				$("#msg").append("更新交易參數失敗").append('</br>');
			}
		},
		error:function(){
			$.unblockUI();	
			$("#msg").append("更新交易參數失敗").append('</br>');
		}
	});
}
/**
 * 更新Smartpay退貨交易
 */
function updateSmartpayTrans(){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "updateSmartpayTrans"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			if (json.success) {
				$("#msg").append("更新Smartpay退貨交易成功").append('</br>');
			} else {
				$("#msg").append("更新Smartpay退貨交易失敗").append('</br>');
			}
		},
		error:function(){
			$.unblockUI();	
			$("#msg").append("更新Smartpay退貨交易失敗").append('</br>');
		}
	});
}

/**
 * 更新Smartpay退貨交易
 */
function updateManualInput(){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "updateManualInput"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			if (json.success) {
				$("#msg").append("更新一般交易人工輸入成功").append('</br>');
			} else {
				$("#msg").append("更新一般交易人工輸入失敗").append('</br>');
			}
		},
		error:function(){
			$.unblockUI();	
			$("#msg").append("更新一般交易人工輸入失敗").append('</br>');
		}
	});
}

/**
 * 新增案件設備鏈接
 */
function insertAssetLink(){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "insertAssetLink"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			if (json.success) {
				$("#msg").append("增加設備鏈接成功").append('</br>');
			} else {
				$("#msg").append("增加設備鏈接失敗").append('</br>');
			}
		},
		error:function(){
			$.unblockUI();	
			$("#msg").append("增加設備鏈接失敗").append('</br>');
		}
	});
}

/**
 * 調整案件SIM設備
 */
function updateSimAssetLink(){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "updateSimAssetLink"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			if (json.success) {
				$("#msg").append("更新案件SIM卡設備成功").append('</br>');
			} else {
				$("#msg").append("更新案件SIM卡設備失敗").append('</br>');
			}
		},
		error:function(){
			$.unblockUI();	
			$("#msg").append("更新案件SIM卡設備失敗").append('</br>');
		}
	});
}

/**
 * 轉入案件資料
 */
function transferCaseHandle(){
	var controls = ['transferCaseId'];
	var transferCaseId = $("#transferCaseId").textbox("getValue");
	if(!validateForm(controls)){
		return false;
	}
	
	var tempLogMessage;
	var currentStratTime = new Date().format("yyyy-MM-dd HH:mm:ss");
	tempLogMessage = "(" + currentStratTime + ")" + "案件處理資料開始轉入。。。";
	$("#msg").append("(" + currentStratTime + ")" + '案件處理資料開始轉入。。。').append('</br>');
	
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{
			actionId : 'transferCaseHandleInfo',
			logMessage:tempLogMessage,
			transferCaseId : transferCaseId
		},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			$("#msg").append(json.resultMsg);
		},
		error:function(){
			$.unblockUI();
			var currentEndTime = new Date().format("yyyy-MM-dd HH:mm:ss");
			$("#msg").append("(" + currentEndTime + ")" + '案件處理資料轉入失敗').append('</br>');
		}
	});
}

function setup() {
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{actionId : "setup"},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			if (json.success) {
				$("#msg").append("部署成功").append('</br>');
			} else {
				$("#msg").append("部署失敗").append('</br>');
			}
		},
		error:function(){
			$.unblockUI();	
			$("#msg").append("部署失敗").append('</br>');
		}
	});
}

/**
 * 舊資料轉移數據
 */
function transferData(actionId){
	var tempLogMessage;
	var currentStratTime = new Date().format("yyyy-MM-dd HH:mm:ss");
	if(actionId == 'transferCalendar'){
		tempLogMessage = "(" + currentStratTime + ")" + "行事曆資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '行事曆資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	} else if(actionId == 'transferFaultParamData'){
		tempLogMessage = "(" + currentStratTime + ")" + "故障參數資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '故障參數資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	} else if(actionId == 'transferCompanyData'){
		comfirmCommon('轉入公司資料會清空公司、合約、倉庫據點、特店表頭、設備庫存、設備庫存歷史、處理中案件、最新案件資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "公司資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '公司資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferWarehouse'){
		comfirmCommon('轉入倉庫據點資料會清空倉庫據點、庫存、設備庫存歷史、最新案件資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "倉庫據點資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '倉庫據點資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferAssetType'){
		comfirmCommon('轉入設備品項資料會清空設備品項、程式版本、設備庫存、設備庫存歷史、處理中案件、最新案件資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "設備品項資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '設備品項資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferApplicaton'){
		comfirmCommon('轉入程式版本資料會清空程式版本、設備庫存、處理中案件、最新案件資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "程式版本資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '程式版本資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferMerchant'){
		comfirmCommon('轉入客戶特店、表頭資料會清空特店、特店表頭、設備庫存、設備庫存歷史、處理中案件、最新案件資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "客戶特店、表頭資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '客戶特店、表頭資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferContract'){
		comfirmCommon('轉入合約資料會清空合約、設備庫存、設備庫存歷史、處理中案件、最新案件資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "合約資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '合約資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferRepository'){
		tempLogMessage = "(" + currentStratTime + ")" + "設備庫存資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '設備庫存資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	} else if(actionId == 'transferHistoryRepository'){
		tempLogMessage = "(" + currentStratTime + ")" + "設備庫存歷史資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '設備庫存歷史資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	} else if(actionId == 'transferCaseHandleInfo'){
		tempLogMessage = "(" + currentStratTime + ")" + "案件處理資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '案件處理資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	} else if(actionId == 'transferAdmUser'){
		comfirmCommon('轉入使用者資料會清空使用者、設備庫存、設備庫存歷史資料，是否繼續？',function(){
			tempLogMessage = "(" + currentStratTime + ")" + "使用者帳號資料開始轉入。。。";
			$("#msg").append("(" + currentStratTime + ")" + '使用者帳號資料開始轉入。。。').append('</br>');
			confirmTransferData(actionId, tempLogMessage);
		});
	} else if(actionId == 'transferCaseNewHandleInfo'){
		tempLogMessage = "(" + currentStratTime + ")" + "案件最新處理資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '案件最新處理資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	} else if(actionId == 'transferProblemData'){
		tempLogMessage = "(" + currentStratTime + ")" + "報修參數資料開始轉入。。。";
		$("#msg").append("(" + currentStratTime + ")" + '報修參數資料開始轉入。。。').append('</br>');
		confirmTransferData(actionId, tempLogMessage);
	}
}
/*
 * 確認數據轉入
 */
function confirmTransferData(actionId, tempLogMessage){
	var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle);
	$.ajax({
		url:'${contextPath}/oldDataTranfer.do',
		data:{
			actionId : actionId,
			logMessage:tempLogMessage
		},
		type:'post', 
		cache:false, 
		dataType:'json',
		success:function(json) {
			$.unblockUI();
			$("#msg").append(json.resultMsg);
		},
		error:function(){
			$.unblockUI();
			var currentEndTime = new Date().format("yyyy-MM-dd HH:mm:ss");
			if(actionId == 'transferCalendar'){
				$("#msg").append("(" + currentEndTime + ")" + '行事曆資料轉入失敗').append('</br>');
			} else if(actionId == 'transferFaultParamData'){
				$("#msg").append("(" + currentEndTime + ")" + '故障參數資料轉入失敗').append('</br>');
			} else if(actionId == 'transferCompanyData'){
				$("#msg").append("(" + currentEndTime + ")" + '公司資料轉入失敗').append('</br>');
			} else if(actionId == 'transferWarehouse'){
				$("#msg").append("(" + currentEndTime + ")" + '倉庫據點資料轉入失敗').append('</br>');
			} else if(actionId == 'transferAssetType'){
				$("#msg").append("(" + currentEndTime + ")" + '設備品項資料轉入失敗').append('</br>');
			} else if(actionId == 'transferApplicaton'){
				$("#msg").append("(" + currentEndTime + ")" + '程式版本資料轉入失敗').append('</br>');
			} else if(actionId == 'transferMerchant'){
				$("#msg").append("(" + currentEndTime + ")" + '客戶特店、表頭資料轉入失敗').append('</br>');
			} else if(actionId == 'transferContract'){
				$("#msg").append("(" + currentEndTime + ")" + '合約資料轉入失敗').append('</br>');
			} else if(actionId == 'transferRepository'){
				$("#msg").append("(" + currentEndTime + ")" + '設備庫存資料轉入失敗').append('</br>');
			}else if(actionId == 'transferHistoryRepository'){
				$("#msg").append("(" + currentEndTime + ")" + '設備庫存歷史資料轉入失敗').append('</br>');
			} else if(actionId == 'transferCaseHandleInfo'){
				$("#msg").append("(" + currentEndTime + ")" + '案件處理資料轉入失敗').append('</br>');
			} else if(actionId == 'transferAdmUser'){
				$("#msg").append("(" + currentEndTime + ")" + '使用者帳號資料轉入失敗').append('</br>');
			} else if(actionId == 'transferCaseNewHandleInfo'){
				$("#msg").append("(" + currentEndTime + ")" + '案件最新處理資料轉入失敗').append('</br>');
			} else if(actionId == 'transferProblemData'){
				$("#msg").append("(" + currentEndTime + ")" + '報修參數資料轉入失敗').append('</br>');
			}
		}
	});
}
/*
 * 依所選內容轉入
 */
function selectTransfer(){
	var allCheckbox = $(".single-select-checkbox");
	var count = 0;
	var tempObj;
	for(var i=0; i<allCheckbox.length; i++){
		tempObj = allCheckbox[i];
		if($(tempObj).prop("checked")){
			count ++;
		}
	}
	if(count == 0){
		$("#msg").append('請至少選中一項轉入').append('</br>');
		return false;
	} else {
		var tempLogMessage = "(" + new Date().format("yyyy-MM-dd HH:mm:ss") + ")" + "開始批量轉入。。。";
		var isStart = false;
		var selectType;
		if($("#transferCalendar").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType = 'transferCalendar';
		}
		if($("#transferFaultParamData").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferFaultParamData';
		}
		if($("#transferCompanyData").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferCompanyData';
		}
		if($("#transferWarehouse").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferWarehouse';
		}
		if($("#transferAssetType").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferAssetType';
		}
		if($("#transferApplicaton").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferApplicaton';
		}
		if($("#transferMerchant").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferMerchant';
		}
		if($("#transferContract").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferContract';
		}
		if($("#transferRepository").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferRepository';
		}
		if($("#transferHistoryRepository").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferHistoryRepository';
		}
		if($("#transferCaseHandleInfo").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferCaseHandleInfo';
		}
		if($("#transferAdmUser").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferAdmUser';
		}
		if($("#transferCaseNewHandleInfo").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferCaseNewHandleInfo';
		}
		if($("#transferProblemData").prop("checked")){
			if(!isStart){
				isStart = true;
			}
			selectType += ',transferProblemData';
		}
		var selectCount = 0;
		if(selectType){
			var selectTypeArr = selectType.split(",");
			if(selectTypeArr){
				var selectActionId;
				for(var i=0; i<selectTypeArr.length; i++){
					selectActionId = selectTypeArr[i];
					if((selectActionId == 'transferHistoryRepository') || (selectActionId == 'transferCaseHandleInfo')
							|| (selectActionId == 'transferCaseNewHandleInfo')){
						selectCount ++;
					}
				}
				if(selectCount >= 2){
					var msgDesc = "因設備庫存歷史、案件處理、案件最新處理會排入線程處理，請勿同時選擇";
					alertPromptCommon(msgDesc, false, function(){
						
					});
				} else {
					$("#msg").append(tempLogMessage).append('</br>');
					var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle);
					$.ajax({
						url:'${contextPath}/oldDataTranfer.do',
						data:{
							actionId : 'selectTransfer',
							logMessage:tempLogMessage,
							isTransferCalendar:$("#transferCalendar").prop("checked"),
							isTransferFaultParamData:$("#transferFaultParamData").prop("checked"),
							isTransferCompanyData:$("#transferCompanyData").prop("checked"),
							isTransferWarehouse:$("#transferWarehouse").prop("checked"),
							isTransferAssetType:$("#transferAssetType").prop("checked"),
							isTransferApplicaton:$("#transferApplicaton").prop("checked"),
							isTransferMerchant:$("#transferMerchant").prop("checked"),
							isTransferContract:$("#transferContract").prop("checked"),
							isTransferRepository:$("#transferRepository").prop("checked"),
							isTransferHistoryRepository:$("#transferHistoryRepository").prop("checked"),
							isTransferCaseHandleInfo:$("#transferCaseHandleInfo").prop("checked"),
							isTransferAdmUser:$("#transferAdmUser").prop("checked"),
							isTransferCaseNewHandleInfo:$("#transferCaseNewHandleInfo").prop("checked"),
							isTransferProblemData:$("#transferProblemData").prop("checked")
						},
						type:'post', 
						cache:false, 
						dataType:'json',
						success:function(json) {
							$.unblockUI();
							$("#msg").append(json.resultMsg);
						},
						error:function(){
							$.unblockUI();
							if(selectType){
								var selectTypeArr = selectType.split(",");
								if(selectTypeArr){
									var actionId;
									var currentEndDate = new Date().format("yyyy-MM-dd HH:mm:ss");
									for(var i=0; i<selectTypeArr.length; i++){
										actionId = selectTypeArr[i];
										if(actionId == 'transferCalendar'){
											$("#msg").append("(" + currentEndDate + ")" + '行事曆資料轉入失敗').append('</br>');
										} else if(actionId == 'transferFaultParamData'){
											$("#msg").append("(" + currentEndDate + ")" + '故障參數資料轉入失敗').append('</br>');
										} else if(actionId == 'transferCompanyData'){
											$("#msg").append("(" + currentEndDate + ")" + '公司資料轉入失敗').append('</br>');
										} else if(actionId == 'transferWarehouse'){
											$("#msg").append("(" + currentEndDate + ")" + '倉庫據點資料轉入失敗').append('</br>');
										} else if(actionId == 'transferAssetType'){
											$("#msg").append("(" + currentEndDate + ")" + '設備品項資料轉入失敗').append('</br>');
										} else if(actionId == 'transferApplicaton'){
											$("#msg").append("(" + currentEndDate + ")" + '程式版本資料轉入失敗').append('</br>');
										} else if(actionId == 'transferMerchant'){
											$("#msg").append("(" + currentEndDate + ")" + '客戶特店、表頭資料轉入失敗').append('</br>');
										} else if(actionId == 'transferContract'){
											$("#msg").append("(" + currentEndDate + ")" + '合約資料轉入失敗').append('</br>');
										} else if(actionId == 'transferRepository'){
											$("#msg").append("(" + currentEndDate + ")" + '設備庫存資料轉入失敗').append('</br>');
										}else if(actionId == 'transferHistoryRepository'){
											$("#msg").append("(" + currentEndDate + ")" + '設備庫存歷史資料轉入失敗').append('</br>');
										} else if(actionId == 'transferCaseHandleInfo'){
											$("#msg").append("(" + currentEndDate + ")" + '案件處理資料轉入失敗').append('</br>');
										} else if(actionId == 'transferAdmUser'){
											$("#msg").append("(" + currentEndDate + ")" + '使用者帳號資料轉入失敗').append('</br>');
										} else if(actionId == 'transferCaseNewHandleInfo'){
											$("#msg").append("(" + currentEndDate + ")" + '案件最新處理資料轉入失敗').append('</br>');
										} else if(actionId == 'transferProblemData'){
											$("#msg").append("(" + currentEndDate + ")" + '報修參數資料轉入失敗').append('</br>');
										}
									}
								}
							}
						}
					});
				}
			}
		}
	}
}
/*
 * 全部選中
 */
function checkAll(){
	if($("#allCheckButton").prop("checked")){
		$(".single-select-checkbox").prop("checked", true);
	} else {
		$(".single-select-checkbox").prop("checked", false);
	}
}
function exportInfo(){
		var controls = ['caseCategory'];
		if (validateForm(controls)) {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			var caseCategory = $("#caseCategory").combobox("getValue");
			createSubmitForm("${contextPath}/oldDataTranfer.do", "actionId=export&caseCategory="+caseCategory, "post");
			
			ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_BAT_09020 %>',function(data){
				$.unblockUI();
			});
		}
}
</script>
</body>
</html>