/**
 * 設備管理相關js方法
 * 爲了減少頁面加載時間將一些方法寫入js文件中用以壓縮
 * 2017/09/07
 */
//全局變量--在掃碼槍查詢之後 執行作業 完成調用查詢時，需要用到的查詢條件
var codeGunParam = undefined;
//全局變量--dialog對象
var viewDlg = undefined;
//全局變量--發mail查詢條件
var sendMailParam = undefined;
//全局變量--存儲已列印的借用設備
var borrowerArray;
$(function () {
	init();
	
	checkedDivListOnchange();
	// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
/*	$('#filterBlank').attr("onclick", "return false;");
	$('#filterBlank').attr("style", "color:gray");*/
	$('#btnExport').attr("onclick", "return false;");
	$('#btnExport').attr("style", "color:gray");
});

/*
 * 初始化加載控件 
 */
function init(){
	
	$("#queryAssetCategory").combobox({
		editable: false,
		panelHeight:'auto',
		onChange : function(newValue, oldValue){
			//設備類別連動資料
   		    $("#assetTypeName").combobox("setValue", "");
   			if (!isEmpty(newValue)) {
   				//newValue不為空時 根據設備類型獲取對應的設備類表
    			ajaxService.getAssetTypeList(newValue,function(data){
    				if (data !=null) {
    					$("#assetTypeName").combobox("loadData", initSelect(data,'請選擇(複選)'));
    				}
    			});
   			} else {
   				//為空時,全查 --update by 20170808
   				ajaxService.getAssetTypeList(null,function(data){
    				if (data !=null) {
    					$("#assetTypeName").combobox("loadData", initSelect(data,'請選擇(複選)'));
    				} else {
    					$("#assetTypeName").combobox("loadData", initSelect(null,'請選擇(複選)'));
    				}
    			});
   			}
   		}
	}); 
	//Task #3127
	$("#queryModel").combobox({
		editable: false,
		valueField: 'value',
		textField: 'name',
		
	});
	//Task #2991 單選 改為 複選
	$("#assetTypeName").combobox({
		editable: false,
		valueField: 'value',
		textField: 'name',
		multiple:true,
		onSelect : function (newValue) {
        	selectMultiple(newValue, "assetTypeName")
    	},
    	onUnselect : function (newValue) {
			unSelectMultiple(newValue, "assetTypeName")
    	},
    	onChange : function(newValue, oldValue){
			//Task #3127 設備名稱連動資料
   		    $("#queryModel").combobox("setValue", "");
   		    var newValueString = newValue.toString();
   			if (newValue != null && newValue != '' ){
   				//設備名稱為複選，且經過特殊處理，選擇一次會調用兩次onchange事件，所以進行判斷，第一次調用時不查詢
   				if(newValueString.indexOf(',') < 0 
   							|| (newValueString.charAt(0) != ',' 
   							&& newValueString.charAt(newValueString.length-1) != ',')) {
	   				//newValue不為空時 根據設備名稱獲取對應的設型號
	    			ajaxService.getAssetModelList(newValue.toString(), function(data){
	    				if (data !=null) {
	    					$("#queryModel").combobox("loadData", initSelect(data));
	    				}
	    			});
   				}
   			} else {
   				$("#queryModel").combobox("loadData", initSelect(null));
   			}
   		}
	});
	$("#queryStorage").combobox({editable: false,});
	$("#queryStatus").combobox({editable: false,});
	$("#queryContractId").combobox({
		editable: false,
		valueField: 'value',
		textField: 'name',
	});
	$("#queryIsEnabled").combobox({
		editable: false,
		panelHeight:'auto',
	});
	$("#queryMaType").combobox({
		editable: false,
		panelHeight:'auto',
	});
	//Task #2991 單選 改為 複選
	$("#queryAction").combobox({
		editable: false,
		valueField: 'value',
		textField: 'name',
		multiple:true,
		onSelect : function (newValue) {
        	selectMultiple(newValue, "queryAction")
    	},
    	onUnselect : function (newValue) {
			unSelectMultiple(newValue, "queryAction")
    	},});
	$("#queryAssetOwner").combobox({editable: false,});
	//Task #2991 單選 改為 複選
	$("#queryAssetUser").combobox({
		editable: false,
		valueField: 'value',
		textField: 'name',
		multiple:true,
		onSelect : function (newValue) {
        	selectMultiple(newValue, "queryAssetUser")
    	},
    	onUnselect : function (newValue) {
			unSelectMultiple(newValue, "queryAssetUser")
    	},});
	$("#queryIsCup").combobox({
		editable: false,
		panelHeight:'auto',
	});
	$("#queryArea").combobox({editable: false,});
	var toMail = $("#toMail");
	if(toMail.length>0){
		toMail.combobox({
			editable: false,
			multiple: true,
			valueField: 'value',
			textField: 'name'
		});  
	}
	
	codeGunAction();
	var queryKeeperName = $('#queryKeeperName').textbox();
	queryKeeperName.textbox('textbox').attr('maxlength', 50);	
	var queryTid = $('#queryTid').textbox();
	queryTid.textbox('textbox').attr('maxlength', 8);	
	var queryDtid = $('#queryDtid').textbox();
	queryDtid.textbox('textbox').attr('maxlength', 8);
	var queryMerchantCode = $('#queryMerchantCode').textbox();
	queryMerchantCode.textbox('textbox').attr('maxlength', 20);
	var queryMerName = $('#queryMerName').textbox();
	queryMerName.textbox('textbox').attr('maxlength', 50);
	var queryHeaderName = $('#queryHeaderName').textbox();
	queryHeaderName.textbox('textbox').attr('maxlength', 100);
	var queryCounter = $('#queryCounter').textbox();
	queryCounter.textbox('textbox').attr('maxlength', 20);
	var queryCartonNo = $('#queryCartonNo').textbox();
	queryCartonNo.textbox('textbox').attr('maxlength', 50);
	$('#beforeTicketCompletionDate').datebox({validType:['date[\'完修日期格式限YYYY/MM/DD\']'],});
	$('#beforeTicketCompletionDate').datebox('textbox').attr('maxlength', 10);
	$('#afterTicketCompletionDate').datebox({validType:['date[\'完修日期格式限YYYY/MM/DD\']','compareDateStartEnd[\'#beforeTicketCompletionDate\',\'完修日期起不可大於日期迄\']']});
	$('#afterTicketCompletionDate').datebox('textbox').attr('maxlength', 10);
	$('#beforeUpdateDate').datebox({validType:['date[\'異動日期格式限YYYY/MM/DD\']'],});
	$('#beforeUpdateDate').datebox('textbox').attr('maxlength', 10);
	$('#afterUpdateDate').datebox({validType:['date[\'異動日期格式限YYYY/MM/DD\']','compareDateStartEnd[\'#beforeUpdateDate\',\'異動日期起不可大於日期迄\']']});
	$('#afterUpdateDate').datebox('textbox').attr('maxlength', 10);
	
	var btnQuery = $("#btnQuery");
	if(btnQuery.length>0){
		btnQuery.linkbutton({iconCls: 'icon-search' });  
	}
	var confirmSend = $("#confirmSend");
	if(confirmSend.length>0){
		confirmSend.linkbutton({iconCls: 'icon-ok' });  
	}
	var btnCarry = $("#btnCarry");
	if(btnCarry.length>0){
		btnCarry.linkbutton();  
	}
	var btnBorrow = $("#btnBorrow");
	if(btnBorrow.length>0){
		btnBorrow.linkbutton();  
	}
	var btnCarryback = $("#btnCarryback");
	if(btnCarryback.length>0){
		btnCarryback.linkbutton();  
	}
	var btnAssetin = $("#btnAssetin");
	if(btnAssetin.length>0){
		btnAssetin.linkbutton();  
	}
	var btnRepair2 = $("#btnRepair2");
	if(btnRepair2.length>0){
		btnRepair2.linkbutton();  
	}
	var btnSendrepair = $("#btnSendrepair");
	if(btnSendrepair.length>0){
		btnSendrepair.linkbutton();  
	}
	var btnPendingdisabled = $("#btnPendingdisabled");
	if(btnPendingdisabled.length>0){
		btnPendingdisabled.linkbutton();  
	}
	var btnDisabled = $("#btnDisabled");
	if(btnDisabled.length>0){
		btnDisabled.linkbutton();  
	}
	var btnBack = $("#btnBack");
	if(btnBack.length>0){
		btnBack.linkbutton();  
	}
	var btnDestroy = $("#btnDestroy");
	if(btnDestroy.length>0){
		btnDestroy.linkbutton();  
	}
	var btnOtheredit = $("#btnOtheredit");
	if(btnOtheredit.length>0){
		btnOtheredit.linkbutton();  
	}
	var btnUnbound = $("#btnUnbound");
	if(btnUnbound.length>0){
		btnUnbound.linkbutton();  
	}
	var btnTaixinrent = $("#btnTaixinrent");
	if(btnTaixinrent.length>0){
		btnTaixinrent.linkbutton();  
	}
	var btnJdwmaintenance = $("#btnJdwmaintenance");
	if(btnJdwmaintenance.length>0){
		btnJdwmaintenance.linkbutton();  
	}
	var btnPrint = $("#btnPrint");
	if(btnPrint.length>0){
		btnPrint.linkbutton();  
	}
	// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
	var allColumnOptions = getAllColumnOptions();
	//延時加載datagrid
	setTimeout(function () {
		$("#dataGrid").datagrid({
			rownumbers:true,
			pagination:true,
			pageNumber:0,
			iconCls: 'icon-edit',
			fitColumns:false,
			pageList:[15,30,50,100],
			pageSize:15,
			nowrap:false,
			singleSelect: false,
			columns:[allColumnOptions]
		});
		}, 5);
}
/**
 * 關閉財產編號編輯畫面
 */
function propertyIdClose() {
	confirmCancel(function(){
		$('#dlgInput').dialog('close');
		$('#propertyIds').textbox('clear');
	});
}

/**
 * 關閉設備序號編寫畫面
 */
function serialNumberClose() {
	$('#serialNumbers').textbox('textbox').blur();
	confirmCancel(function(){
		$('#dlgQ').dialog('close');
		$('#serialNumbers').textbox('clear');
	});
	
}

/*掃碼槍  使用方法-------------------------------------------------------start*/
/*
 * 掃碼槍keydown事件綁定
 */
function codeGunAction(){
	var querySerialNumbers = null;
	$('#querySerialNumbers').textbox();
	$('#queryPropertyIds').textbox();
	//頁面加載設備序號聚焦
	$('#querySerialNumbers').textbox('textbox').focus();
   	//設備序號掃碼槍掃碼查詢，定義keydown事件
   	$("#querySerialNumbers").textbox("textbox").keydown(function(event){
   		//如果該輸入框聚焦
   		if($("#querySerialNumbers").textbox("textbox").is(":focus")){
   			//如果按下tab鍵
   			if (event.keyCode == 9 || event.keyCode == 13) {
   				setTimeout(function () {
   				//如果有逗號，或為空，則不是掃碼槍
   				if($("#querySerialNumbers").textbox("getValue")!=null 
   						&& $("#querySerialNumbers").textbox("getValue")!='' 
   						&& $("#querySerialNumbers").textbox("getValue").indexOf(',')<0
   						&& $("#querySerialNumbers").textbox("getValue").indexOf('，')<0){
   					//需要判斷 掃碼槍查詢之後改變$("#history")的值 ，然後進行的是匯出還是掃碼槍 查詢
   					//預設隱藏域值為0，當掃碼槍掃描  將其更新為1，當其為1時，點擊$("#history") 將值改為2，如果值為2，掃碼槍查詢將其改為1，清除掃碼槍隱藏域相關值。 2018/01/03
   					if ($("#hideHistoryChangeFlag").val()=='0') {
   						$("#hideHistoryChangeFlag").val('1');
					}
   					if ($("#hideHistoryChangeFlag").val()=='2') {
   						$("#hideHistoryChangeFlag").val('1');
   						clearHide();
   					}
   					$("#hideCodeGun").val("");
   					//轉換掃碼欄位--如果之前在掃財產編號則清空行數欄位
   					if($("#hideCodeGunFlag").val()=='2'){
   						$("#hideCodeGunRowNumber").val("");
   					}
   					//"1" 為設備序號查詢
   					$("#hideCodeGunFlag").val("1");
   					//如果隱藏欄位為空，則設置不存在時的提示消息，向隱藏域複值
   					if($("#hideCodeGunSerialNumbers").val() == ''){
   						$("#hideCodeGun").val("此設備序號不存在");
   						//拼接 ** 是爲了防止驗證重複時候出現錯誤
	   					$("#hideCodeGunSerialNumbers").val("**"+$("#querySerialNumbers").textbox("getValue")+"**");
	   				} else {
	   					//如果已存在則不寫入隱藏域  提示 此設備序號重複掃描
		   				if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+$("#querySerialNumbers").textbox("getValue")+"**")>=0
		   						|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+$("#querySerialNumbers").textbox("getValue")+'**,')>=0
		   						|| $("#hideCodeGunSerialNumbers").val() == "**"+$("#querySerialNumbers").textbox("getValue")+"**"){
		   						
		   					$("#hideCodeGun").val("");
		   					$("#msgAction").text("此設備序號重複掃描");
		   					$('#querySerialNumbers').textbox('setValue','');
		   					$('#querySerialNumbers').textbox('textbox').focus();
		   					return false;
		   				//如果不存在則寫入隱藏域
		   				} else {
	   						$("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val() + ",**" + $("#querySerialNumbers").textbox("getValue")+"**");
		   					$("#hideCodeGun").val("此設備序號不存在");
		   				}
	   				}
	   				//設置掃碼槍查詢財產編號為空 
					$("#hideCodeGunPropertyIds").val("");
					querySerialNumbers = $("#hideCodeGunSerialNumbers").val();
					$("#querySerialNumbers").textbox("setValue","");
					$('#querySerialNumbers').textbox('textbox').focus();
					var editParam = getQueryParamter();
					editParam.actionId = 'query';
	    			editParam.querySerialNumbers = querySerialNumbers.replaceAll("**",'');
					if($(":checkbox[name=history]").is(":checked")) {
						editParam.actionId = "history";
					}
					//爲了在執行作業之後進行原查詢，參數放入全局變量之中
					codeGunParam = editParam;
					//判斷之前進行的掃碼槍查詢 是財產編號還是設備序號 
					$("#hideCodeGunAfterQuery").val('serialNumber');
					//調用查詢
					if(queryDevice(1, true, editParam, 'serialNumber')){
						$('#querySerialNumbers').textbox('textbox').focus();
					}
   				}
				}, 100);
         	}
   		} 	 
	});
	
	var queryPropertyIds = null;
   	//財產編號掃碼槍掃碼查詢，定義keydown事件 
   	$("#queryPropertyIds").textbox("textbox").keydown(function(event){
   		//如果該輸入框聚焦
   		if($("#queryPropertyIds").textbox("textbox").is(":focus")){
   			//如果按下tab鍵
   			if (event.keyCode == 9 || event.keyCode == 13) {
   				setTimeout(function () {
   					//如果有逗號，或為空，則不是掃碼槍
   					if($("#queryPropertyIds").textbox("getValue")!=null 
   							&& $("#queryPropertyIds").textbox("getValue")!='' 
   							&& $("#queryPropertyIds").textbox("getValue").indexOf(',')<0
   							&& $("#queryPropertyIds").textbox("getValue").indexOf('，')<0){
   								if ($("#hideHistoryChangeFlag").val()=='0') {
			   						$("#hideHistoryChangeFlag").val('1');
								}
			   					if ($("#hideHistoryChangeFlag").val()=='2') {
			   						$("#hideHistoryChangeFlag").val('1');
			   						clearHide();
			   					}
	   							$("#hideCodeGun").val("");
	   							//轉換掃碼欄位--如果之前在掃設備序號則清空行數欄位 
	   							if($("#hideCodeGunFlag").val()=='1'){
	   								$("#hideCodeGunRowNumber").val("");
	   							}
	   							//"2" 為財產編號查詢 
	   							$("#hideCodeGunFlag").val("2");
	   							//如果隱藏欄位為空，則設置不存在時的提示消息，向隱藏域複值
   								if($("#hideCodeGunPropertyIds").val() == ''){
   									$("#hideCodeGun").val("此財產編號不存在");
   									//拼接 ** 是爲了防止驗證重複時候出現錯誤
				   					$("#hideCodeGunPropertyIds").val("**"+$("#queryPropertyIds").textbox("getValue")+"**");
				   				} else {
					   				//如果已存在則不寫入隱藏域  提示 此財產編號重複掃描
					   				if($("#hideCodeGunPropertyIds").val().indexOf(',**'+$("#queryPropertyIds").textbox("getValue")+"**")>=0
					   						|| $("#hideCodeGunPropertyIds").val().indexOf("**"+$("#queryPropertyIds").textbox("getValue")+'**,')>=0
					   						|| $("#hideCodeGunPropertyIds").val() == "**"+$("#queryPropertyIds").textbox("getValue")+"**"){
					   				$("#hideCodeGun").val("");
					   				$("#msgAction").text("此財產編號重複掃描");
					   				$('#queryPropertyIds').textbox('setValue','');
					   				$('#queryPropertyIds').textbox('textbox').focus();	
		   							return false;
					   				//如果不存在則寫入隱藏域
					   				} else {
					   					$("#hideCodeGunPropertyIds").val($("#hideCodeGunPropertyIds").val() + ",**" + $("#queryPropertyIds").textbox("getValue")+"**");
					   					$("#hideCodeGun").val("此財產編號不存在");
					   				}
				   				}
								queryPropertyIds = $("#hideCodeGunPropertyIds").val();
								$("#queryPropertyIds").textbox("setValue","");
								$('#queryPropertyIds').textbox('textbox').focus();
								//設置掃碼槍查詢設備序號為空  
								$("#hideCodeGunSerialNumbers").val("");
								var editParam = getQueryParamter();
								editParam.actionId = 'query';
				    			editParam.queryPropertyIds = queryPropertyIds.replaceAll("**",'');
								if($(":checkbox[name=history]").is(":checked")) {
									editParam.actionId = "history";
								}
								//爲了在執行作業之後進行原查詢，參數放入全局變量之中
								codeGunParam = editParam;
								//判斷之前進行的掃碼槍查詢 是財產編號還是設備序號 
								$("#hideCodeGunAfterQuery").val('propertyId');
								//調用查詢
								if(queryDevice(1, true, editParam, 'propertyId')){
   									$('#queryPropertyIds').textbox('textbox').focus();	
								}
   							}
				}, 100);
         	}
   		} 
	});
}
/*
*鍵盤單擊事件：按下回格鍵複製value，
*按回車建不執行換行
*/
function keyup (event) {
	//當點擊刪除鍵時觸發
	if (event.keyCode == 8) {
		var value = $('#serialNumbers').textbox("textbox").val();
		$('#serialNumbers').textbox("setValue", value);
	}
	if (event.keyCode == 13) {
		return false;
	}
}
/*
*掃碼槍光標自動換行
*/
function changeSerialNumbers() {
	var value = $('#serialNumbers').textbox("textbox").val();
	if (value.length > 1 && value.substr(value.length-1,1) != '\n') {	
		value = value + '\n';
	}
	$('#serialNumbers').textbox("setValue", value);
	//由於ie獲取事件在focus之后，將focus延時100ms update by 2017/09/22
	setTimeout(function () {
		$('#serialNumbers').textbox('textbox').focus();
	},100);
}
//清空隱藏域
function clearHide(){
	 $("#hideCodeGunRowNumber").val('');
	 $("#hideCodeGunSerialNumbers").val('');
	 $("#hideCodeGunPropertyIds").val('');
	 codeGunParam = undefined;
	 $("#hideCodeGunAfterQuery").val('');
	 $("#hideCodeGunFlag").val('');
	 $("#codeGunFlag").val('');
}
//掃碼槍查詢后對隱藏域值進行處理 
function dealHidenValue(codeGunFlag,rowLength){
	//將本次查詢條數存入隱藏域
	if(typeof(codeGunFlag)!="undefined"){
		$("#hideCodeGunRowNumber").val(rowLength);
	}
	//將不存在的值從隱藏域里刪除
	if(typeof(codeGunFlag)!="undefined"){
		if($("#hideCodeGunFlag").val()=='1'){
				if($("#msgAction").text()=='此設備序號不存在'){
					if($("#hideCodeGunSerialNumbers").val().indexOf(",")>=0){
						var hideCodeGunSerialNumbers = $("#hideCodeGunSerialNumbers").val();
						hideCodeGunSerialNumbers = hideCodeGunSerialNumbers;
						var point = hideCodeGunSerialNumbers.lastIndexOf(",");
						$("#hideCodeGunSerialNumbers").val(hideCodeGunSerialNumbers.substring(0, point));
					} else {
						$("#hideCodeGunSerialNumbers").val("");
					}
				}
			}
		if($("#hideCodeGunFlag").val()=='2'){
			if($("#msgAction").text()=='此財產編號不存在'){
				if($("#hideCodeGunPropertyIds").val().indexOf(",")>=0){
					var hideCodeGunPropertyIds = $("#hideCodeGunPropertyIds").val();
					var point = hideCodeGunPropertyIds.lastIndexOf(",");
					$("#hideCodeGunPropertyIds").val(hideCodeGunPropertyIds.substring(0, point));
				} else {
					$("#hideCodeGunPropertyIds").val("");
				}
			}
		}
	}
}
/*
 * 掃碼槍查詢提示消息處理 
 */
function getCodeGunMsg(codeGunFlag,rowLength,totalSize){
	//掃碼槍查詢 查無資料
	if(typeof(codeGunFlag)!="undefined" && rowLength<1){
		$("#msgAction").text($("#hideCodeGun").val());
		$("#hideCodeGunRowNumber").val('0');
		if($("#hideCodeGunFlag").val()=='1'){
			$("#msgAction").text("此設備序號不存在");
		}
		if($("#hideCodeGunFlag").val()=='2'){
			$("#msgAction").text("此財產編號不存在");
		}
	//顯示提示消息	
	} else if(typeof(codeGunFlag)!="undefined" && $("#hideCodeGunRowNumber").val() !=null 
				&& $("#hideCodeGunRowNumber").val() !=''
	 			&& (parseInt($("#hideCodeGunRowNumber").val())>=totalSize )){
 		if (($("#hideCodeGunSerialNumbers").val()!=null && $("#hideCodeGunSerialNumbers").val()!='')
 					&& ($("#hideCodeGunPropertyIds").val()!=null && $("#hideCodeGunPropertyIds").val()!='')
 					&& totalSize>0){
 		}else {
			$("#msgAction").text($("#hideCodeGun").val());
 		}
	}
}
/*掃碼槍  使用方法-------------------------------------------------------end*/


/*執行作業  使用方法-------------------------------------------------------start*/
/**
 * 預設日期
 */
function showDate(dateFlag){
	var date = new Date();
	var month = 0;
	month = date.getMonth() + 1;
	borrowerStartDate = date.getFullYear() + "/" + AppendZero(month) + "/" + AppendZero(date.getDate());
	if (dateFlag =='date'){
		//領用日期
		$("#date").textbox('setValue', borrowerStartDate);
	} else if (dateFlag =='borrowerStartDate'){
		//借用日期
		$("#borrowerStartDate").textbox('setValue', borrowerStartDate);
		$("#borrowerStart").val(borrowerStartDate);
	} else if (dateFlag =='taixin'){
		//領用日期
		$("#isEnableDate").textbox('setValue', borrowerStartDate);
	}
	
}
/**
 * 日期拼接
 */
function AppendZero(obj){  
        if(obj<10) return "0" + "" + obj;  
        else return obj;  
} 

/**
 * 點擊對號，查詢人員資料
 */
function queryEmployee(maintenanceUserFlag){
	$("#msgAction").text("");
	javascript:dwr.engine.setAsync(false);
	var flag = false;
	//維護工程師
	if(maintenanceUserFlag =='maintenanceUser'){
		var maintenanceUser = $("#maintenanceUserCode").textbox('getValue');
		//領用人為空提示消息
		if(maintenanceUser == null || maintenanceUser == ''){
			//隱藏提示驗證消息
			hideDivListValidate();
			$("#msgAction").text("請輸入維護工程師");
			
			$("#maintenanceUserCode").textbox("textbox").focus();
			flag = false;
			handleScrollTop("assetManageDivId");
		} else {
			//不為空時，查找人員資料
			ajaxService.getEmailByUserId(maintenanceUser,$("#maintenanceVendor").combobox('getValue'), function(data){
				if (data != null) {
					//將userId放入隱藏域用來驗證與提交
					$("#maintenanceUserId").val(data.userId);
					//中文姓名(英文姓名)
					if (data.ename != null && data.ename != '') {
						$("#maintenanceCnEnName").html(data.cname + "(" + data.ename + ")");
					} else {
						$("#maintenanceCnEnName").html(data.cname);
					}
					$("#maintenanceUserName").val(borrows);
					$("#hideMaintenanceUserCode").val(maintenanceUser);
					flag = true;
				} else {
					$("#msgAction").text("查無資料");
					$("#maintenanceCnEnName").html('');
					flag = false;
					handleScrollTop("assetManageDivId");
				}
			});
		}
	}
	if(maintenanceUserFlag =='taixinMaintenanceUser'){
		var taixinMaintenanceUser = $("#taixinMaintenanceUserCode").textbox('getValue');
		//領用人為空提示消息
		if(taixinMaintenanceUser == null || taixinMaintenanceUser == ''){
			$("#msgAction").text("請輸入維護工程師");
			
			$("#taixinMaintenanceUserCode").textbox("textbox").focus();
			flag = false;
			handleScrollTop("assetManageDivId");
		} else {
			//不為空時，查找人員資料
			ajaxService.getEmailByUserId(taixinMaintenanceUser,$("#taixinVendor").combobox('getValue'), function(data){
				if (data != null) {
					//將userId放入隱藏域用來驗證與提交
					$("#taixinMaintenanceUserId").val(data.userId);
					//中文姓名(英文姓名)
					if (data.ename != null && data.ename != '') {
						$("#taixinMaintenanceCnEnName").html(data.cname + "(" + data.ename + ")");
					} else {
						$("#taixinMaintenanceCnEnName").html(data.cname);
					}
					$("#taixinMaintenanceUserName").val(borrows);
					$("#hideMaintenanceUserCode").val(taixinMaintenanceUser);
					flag = true;
				} else {
					$("#msgAction").text("查無資料");
					$("#taixinMaintenanceCnEnName").html('');
					flag = false;
					handleScrollTop("assetManageDivId");
				}
			});
		}
	} 
	//當為領用時
	if ($("#accountFlag").val() == "account") {
		var carry=$("#carryCode").combobox('getValue');
		//領用人為空提示消息
		if (carry == null || carry == '') {
			$("#carryCode").textbox("textbox").focus();
			flag = false;
		} else {
			//不為空時，查找人員資料
			ajaxService.getEmailByUserId(carry, function(data) {
				if (data != null) {
					//將userId放入隱藏域用來驗證與提交
					$("#carryAccount").val(data.userId);
					if(data.ename != null && data.ename != ''){
						$("#carryCnEnName").html(data.cname + "(" + data.ename + ")");
					} else {
						$("#carryCnEnName").html(data.cname);
					}
					$("#carryName").val(carry);
					flag = true;
				}else{
					$("#msgAction").text("查無資料");
					$("#carryCnEnName").html('');
					flag = false;
					handleScrollTop("assetManageDivId");
				}
			});
		}
	//當為借用時
	} else if ($("#accountFlag").val() == "borrow") {
		var borrows = $("#borrows").combobox('getValue');
		//領用人為空提示消息
		if(borrows == null || borrows == ''){
			$("#borrows").combobox("textbox").focus();
			flag = false;
		} else {
			//不為空時，查找人員資料
			ajaxService.getEmailByUserId(borrows, function(data){
				if (data != null) {
					$("#borrowerEmail").textbox("setValue", data.email);
					$("#borrowerMgrEmail").textbox("setValue", data.managerEmail);
					//將userId放入隱藏域用來驗證與提交
					$("#userId").val(data.userId);
					//中文姓名(英文姓名)
					if (data.ename != null && data.ename != '') {
						$("#borrowsName").html(data.cname + "(" + data.ename + ")");
					} else {
						$("#borrowsName").html(data.cname);
					}
					$("#userName").val(borrows);
					flag = true;
				} else {
					$("#msgAction").text("查無資料");
					$("#borrowsName").html('');
					flag = false;
					handleScrollTop("assetManageDivId");
				}
			});
		}
	}
	javascript:dwr.engine.setAsync(true);
	return flag;
}
/**
 * 人員查詢畫面單擊事件 
 */
function dlgClickRow(row, isCheck){
	//當為維護工程師
	if ($("#accountFlag").val() == "maintenanceUser") {
		//中文姓名(英文姓名)
		if(row.ename != null && row.ename != ''){
			$("#maintenanceCnEnName").html(row.cname + "(" + row.ename + ")");
		} else {
			$("#maintenanceCnEnName").html(row.cname);
		}
		//將userId放入隱藏域用來驗證與提交
	    $("#maintenanceUserId").val(row.userId);
	    $("#maintenanceUserCode").textbox('setValue', row.account);
	    $("#hideMaintenanceUserCode").val(row.account);
	}
	//當為台新維護工程師
	if ($("#accountFlag").val() == "taixin") {
		//中文姓名(英文姓名)
		if(row.ename != null && row.ename != ''){
			$("#taixinMaintenanceCnEnName").html(row.cname + "(" + row.ename + ")");
		} else {
			$("#taixinMaintenanceCnEnName").html(row.cname);
		}
		//將userId放入隱藏域用來驗證與提交
	    $("#taixinMaintenanceUserId").val(row.userId);
	    $("#taixinMaintenanceUserCode").textbox('setValue', row.account);
	   	$("#hideMaintenanceUserCode").val(row.account);
	} 
	//當為領用時
	if ($("#accountFlag").val() == "account") {
		//中文姓名(英文姓名)
		if(row.ename != null && row.ename != ''){
			$("#carryCnEnName").html(row.cname + "(" + row.ename + ")");
		} else {
			$("#carryCnEnName").html(row.cname);
		}
		//將userId放入隱藏域用來驗證與提交
	    $("#carryAccount").val(row.userId);
	    $("#carryCode").combobox('setValue', row.account);
	    $("#carryName").val(row.account);
	 //當為借用時
	} else if ($("#accountFlag").val() == "borrow") {
		$("#borrowerEmail").textbox("setValue", row.email);
		$("#borrowerMgrEmail").textbox("setValue", row.managerEmail);
		//將userId放入隱藏域用來驗證與提交
		$("#userId").val(row.userId);
		//中文姓名(英文姓名)
		if(row.ename != null && row.ename != ''){
			$("#borrowsName").html(row.cname + "(" + row.ename + ")");
		} else {
			$("#borrowsName").html(row.cname);
		}
		$("#borrows").combobox('setValue', row.account);
		$("#userName").val(row.account);
	}
	viewDlg.dialog('close');
}
/**
 * mid查詢畫面單擊事件
 */
function chooseMidOnCheck(index, row){
	//將所選行的值帶入所需欄位
	$("#taixinmerId").val(row.merchantId);
	$("#taixinName").textbox('setValue', row.name);
	$("#taixinMid").textbox('setValue', row.merchantCode);
	$("#hidTaixinMid").val(row.merchantCode);
	//獲取表頭集合
	ajaxService.getMerchantHeaderList($("#taixinCompanyId").val(), null, row.merchantId, function(result){
		if (result != null) {
			$("#taixinHeader").combobox("loadData", initSelect(result));
			$("#taixinHeader").combobox("setValue", '');
		}
	});
	viewDlg.dialog('close');
}
//點擊對號，查特店資料
function searchMid(isSelect){
	$("#msgAction").text("");
	var taixinMid=$("#taixinMid").textbox('getValue');
	//向隱藏域賦值
	$("#taixinHeader").combobox("loadData", initSelect(null));
	$("#taixinHeader").combobox("setValue", "");
	$("#taixinName").textbox("setValue", "");
	if(taixinMid!=null && taixinMid!=''){
		javascript:dwr.engine.setAsync(false);   
		//由code獲取台新公司名稱及id
		//台新code，如果變更需要更改
		var companyCode = 'TSB-EDC';
		//CR #2696
		if($("#hidJdwFlag").val() == 'jdw'){
			companyCode = 'JDW-EDC';
		}
		ajaxService.getCompanyByCompanyCode(companyCode, function(result){
		   if (result != null){
			   	$("#taixinCompanyName").val(result.shortName);
			   	$("#taixinCompanyId").val(result.companyId);
			}
	    });
		//根據客戶與特點代號獲取特點信息
		ajaxService.getMerchantDTOById("", taixinMid, $("#taixinCompanyId").val(), function(result){
			if (result != null) {
				$("#hidTaixinMid").val(taixinMid);
				$("#taixinName").textbox('setValue', result.name);
				$("#taixinmerId").val(result.merchantId);
				//根據所選的特店獲取對應的特店表頭下拉列表
				ajaxService.getMerchantHeaderList($("#taixinCompanyId").val(), taixinMid, "", function(result){
					if (result != null) {
						$("#taixinHeader").combobox("setValue", "");
						$("#taixinHeader").combobox("loadData", initSelect(result));
						if (isSelect && companyCode == 'TSB-EDC') {
							$("#taixinHeader").combobox("setValue", result[1]["value"]);
						}
					}
				});
			} else {
				//當無特店資料時
				$("#msgAction").text("查無資料");
				if (companyCode == 'TSB-EDC') {
					viewEditMerchantData1('新增客戶特店維護', 'initAdd', '');
				}
			}
		});
		javascript:dwr.engine.setAsync(true);
	} else {
		$("#msgAction").text("請輸入特店代號");
	}
}
/**
 * 修改特店信息
 * @param contextPath
 */
function editAssetMid() {
	var isError = checkMerchantCode();
	if (isError) {
		handleScrollTop("assetManageDivId");
		return false;
	}
	viewEditMerchantData1('修改客戶特店維護', 'initEdit', $("#taixinmerId").val());
}
/**
 * 新增特店信息
 */
function addTaixinHeader(contextPath) {
	var isError = checkMerchantCode();
	if (isError) {
		handleScrollTop("assetManageDivId");
		return false;
	}
	var queryParams ={actionId : 'initAdd', merchantId : $("#taixinmerId").val()};
	viewEditMerchantHeader('merchanAddHeaderDlg','新增特店表頭維護',queryParams, loadHeaderNameCom, null, "initAdd", contextPath, true);
}
/**
 * 修改特店信息
 */
function editTaixinHeader(contextPath) {
	var headerName = $("#taixinHeader").combobox("getValue");
	if (headerName == null || headerName == "") {
		$("#msgAction").text("請輸入表頭（同對外名稱）");
		return false;
	}
	var queryParams ={actionId : 'initEdit', merchantHeaderId : headerName};
	viewEditMerchantHeader('merchanAddHeaderDlg','修改特店表頭維護',queryParams, loadHeaderNameCom, null, "initEdit", contextPath, true);
}
/**
 * 修改特點、特點表頭時，需要進行的核減
 * @returns {Boolean}
 */
function checkMerchantCode() {
	$("#msgAction").text("");
	var taixinMid=$("#taixinMid").textbox('getValue');
	if (taixinMid == null || taixinMid == '') {
		$("#msgAction").text("請輸入特店代號");
		return true;
	}
	var hidTaixinMid = $("#hidTaixinMid").val();
	if (hidTaixinMid == null || hidTaixinMid == '') {
		$("#msgAction").text("請根據特店代號帶入資料");
		return true;
	}
	if (taixinMid != hidTaixinMid) {
		$("#msgAction").text("特店代號已變更，請重新帶入資料");
		return true;
	}
}
function loadHeaderNameCom(result) {
	//根據所選的特店獲取對應的特店表頭下拉列表
	var taixinMid=$("#taixinMid").textbox('getValue');
	var resultMap = result.row;
	var headerId = resultMap.merchantHeaderId;
	ajaxService.getMerchantHeaderList($("#taixinCompanyId").val(), taixinMid, "", function(result){
		if (result != null) {
			$("#taixinHeader").combobox("setValue", "");
			$("#taixinHeader").combobox("loadData", initSelect(result));
			$("#taixinHeader").combobox("setValue", headerId);
		}
	});
}
//同營業地址checkbox聯動
function merInstallAddressChecked(){
	//同營業地址選中時
	if($(":checkbox[name=taixinSameAddress]").is(":checked")){
		var taixinHeader = $("#taixinHeader").combobox('getValue');
		$("#taixinAddress").textbox('enable');
		$("#taixinLocation").combobox('enable');
		$("#taixinLocation").combobox('readonly', false);
		$("#taixinAddress").textbox('readonly', false);
		//設定為disable
		javascript:dwr.engine.setAsync(false);
		if (taixinHeader != null && taixinHeader != '') {
			//獲取表頭相關資料
			ajaxService.getMerchantHeaderDTOById(taixinHeader, function(result){
				//不為空時
				if (result != null){
					if(result.location != null && result.location != ''){
						$("#taixinLocation").combobox('disable');
					} 
					$("#taixinLocation").combobox('setValue',result.location);
					if(result.businessAddress != null && result.businessAddress != ''){
						$("#taixinAddress").textbox('disable');
					} 
					$("#taixinAddress").textbox('setValue',result.businessAddress);
				} else {
					$("#taixinAddress").textbox('setValue', "");
					$("#taixinLocation").combobox('setValue', "");
				}
			});
		} else {
			$("#taixinAddress").textbox('setValue', "");
			$("#taixinLocation").combobox('setValue', "");
		}
		javascript:dwr.engine.setAsync(true);
		var taixinAddress = $("#taixinAddress").textbox('getValue');
		var taixinLocation = $("#taixinLocation").combobox('getValue');
		if( taixinAddress == null || taixinAddress ==''){
			$("#taixinAddress").textbox('enable');
			$("#taixinAddress").textbox('readonly', true);
		}
		if( taixinLocation == null || taixinLocation ==''){
			$("#taixinLocation").combobox('enable');
			$("#taixinLocation").combobox('readonly', true);
		}
	}else{
		//同營業地址取消選中時
		$("#taixinAddress").textbox('enable');
		$("#taixinLocation").combobox('enable');
		$("#taixinAddress").textbox('readonly', false);
		$("#taixinLocation").combobox('readonly', false);
	}
}
/**
 * 查詢特店初始化頁面
 */
function openDialogMID(contextPath){
	$('#msgAction').text("");
    javascript:dwr.engine.setAsync(false);
	var companyCode = 'TSB-EDC';
	//CR #2696
	if($("#hidJdwFlag").val() == 'jdw'){
		companyCode = 'JDW-EDC';
	}
    //根據公司code獲取公司資料
    ajaxService.getCompanyByCompanyCode(companyCode, function(result){
	   if (result != null){
		    //公司簡稱
		   	$("#taixinCompanyName").val(result.shortName);
		  	//公司ID
		   	$("#taixinCompanyId").val(result.companyId);
		}
    });
    //打開查詢mid畫面
    viewDlg = $('#dlvMid').dialog({
		title : '選擇 MID',
		width : 840,
		height : 460,
		top:10,
		closed : false,
		cache : false,
		queryParams : {
			actionId : "initMid",
			queryCompanyId: $("#taixinCompanyId").val(),
		},
		href : contextPath + "/merchant.do",
		modal : true,
		onLoad : function(){
			settingDialogPanel('dlvMid');
			textBoxSetting('dlvMid');
	    }
	});
    javascript:dwr.engine.setAsync(true);
}
/**
 * 查詢人員初始化頁面
 */
function searchEmployee(obj, maintenanceUserFlag, contextPath){
	var company = "";
	if(maintenanceUserFlag == 'maintenanceUser'){
		company = $("#maintenanceVendor").combobox("getValue");
	} else if(maintenanceUserFlag == 'taixinMaintenanceUser'){
		company = $("#taixinVendor").combobox("getValue");
	} 
	//打開查詢人員畫面
    viewDlg = $('#dlvSearch').dialog({
		title : '選擇帳號',
		width : 750,
		height : 550,
		top:10,
		closed : false,
		cache : false,
		queryParams : {
			actionId : "initSearchEmployee",
			maintenanceUserFlag : company
		},
		href : contextPath+"/assetManage.do",
		modal : true,
		onLoad : function(){
			settingDialogPanel('dlvSearch');
			textBoxSetting('dlvSearch');
	    },
	    onClose:function(){
			$(".datagrid-row").mouseover(function(){ 
				$(this).css("cursor", "auto");
			});
		},
		
	});
}
/**
 * 領用作業畫面加載
 */
function openActionCarry(){
	$('#carryCode').combobox({ 
    	editable:true,  
    	panelHeight:'auto',
	    valueField:'value',
	    textField:'name',
	    method:'get',
	    hasDownArrow:false,
	    required:true,
	    validType:'maxLength[50]',
	    missingMessage:"請輸入領用人",
      	invalidMessage:"請輸入領用人"     
	});
	$('#carryCode').addClass("easyui-combobox");
	//Task #2564 領用人、借用人 欄位，使用 autocomplete 效果，CLIET記住輸入內容 2017/10/11
	try {
		//如果cookie中存在該值
		var cookie = $.cookie('carryCode');
		if (cookie!=null) {
			$("#carryCode").combobox('loadData', JSON.parse(cookie));
		}
		$("#carryCode").combobox('setValue', '');
		$('#carryCode').combobox('textbox').bind('focus.showPanel',function(){  
			$('#carryCode').combobox('showPanel');  
		}); 
	} catch (e) {
		$.cookie('carryCode', null);
	}

	if($('#carrierShowFlag').val()!='Y'){
		$('#carryCode').combobox('textbox').attr('maxlength', 50); 
		$('#carrier').attr("name","edit");
		$('#carrier').css("display","");
		$('#carrier').panel({      
		  	title: '領用作業',    
		 	closed: true   
		});                  	
	    
		$('#date').textbox({});
		$('#carryComment').textbox({validType:'maxLength[200]'});
		$('#carryComment').textbox('textbox').attr('maxlength', 200);
		$('#btn_asset_queryEmployee').linkbutton({    
		    iconCls: 'icon-ok'   
		}); 
		$('#btn_asset_searchEmployee').linkbutton({    
		    iconCls: 'icon-search'   
		});  
		$('#btn_asset_carry').linkbutton({});  
	    $('#btn_asset_cancel').linkbutton({});  
		showDate('date');
		$('#carrierShowFlag').val('Y');
	}
}
/**
 * 借用作業畫面加載 
 */
function openActionBorrower(){
	$('#borrows').combobox({ 
    	editable:true,  
    	panelHeight:'auto',
	    valueField:'value',
	    textField:'name',
	    method:'get',
	    hasDownArrow:false,
	    required:true,
	    validType:'maxLength[50]',
	    missingMessage:"請輸入借用人",
      	invalidMessage:"請輸入借用人"     
	});
	$('#borrows').addClass("easyui-combobox");
	try {
		//如果cookie中存在該值
		var cookie = $.cookie('borrows');
		if (cookie!=null) {
			$("#borrows").combobox('loadData', JSON.parse(cookie));
		}
		$("#borrows").combobox('setValue', '');
		$('#borrows').combobox('textbox').bind('focus.showBorrowPanel',function(){  
			$('#borrows').combobox('showPanel');  
		}); 
	} catch (e) {
		$.cookie('borrows', null);
	}
	if($('#borrowerShowFlag').val()!='Y'){
		 $('#borrower').attr("name","edit");
		 $('#borrower').css("display","");
		 $('#borrower').panel({      
		  	title: '借用作業',    
		 	closed: true   
		});      
		$('#borrows').combobox('textbox').attr('maxlength', 50); 
	   
		$('#btn_asset_queryEmployee_borrow').linkbutton({iconCls: 'icon-ok'   });  
		$('#btn_asset_searchEmployee_borrow').linkbutton({ iconCls: 'icon-search'});  
		  
		$('#borrowerStartDate').textbox({});
		$('#borrowerEnd').datebox({
			required:true,
		    validType:['date[\'借用日期迄格式限YYYY/MM/DD\']','compareToDate','requiredDropList'],
	      	missingMessage:"請輸入借用日期" 
		});
		$('#borrowerEnd').datebox('textbox').attr('maxlength', 10);	
		$('#borrowerEmail').textbox({
			required:true,
			validType:['email[\'借用人郵箱格式有誤，請重新輸入\']','maxLength[50]'],
		 	missingMessage:"請輸入借用人郵箱"
		});
		$('#borrowerEmail').textbox('textbox').attr('maxlength', 50);
		$('#borrowerMgrEmail').textbox({
			required:true,
			validType:['email[\'借用人主管郵箱格式有誤，請重新輸入\']','maxLength[50]'],
		 	missingMessage:"請輸入借用人主管郵箱"
		});
		$('#borrowerMgrEmail').textbox('textbox').attr('maxlength', 50);	
		$('#borrowerComment').textbox({
			required:true,
			validType:'maxLength[200]', 
		 	missingMessage:"請輸入借用說明"
		});
		$('#borrowerComment').textbox('textbox').attr('maxlength', 200);
		$('#btn_asset_exportBorrow_borrow').linkbutton({});  
		$('#btn_asset_borrow_borrow').linkbutton({});  
		$('#btn_asset_cancel_borrow').linkbutton({}); 
		$('#button_borrow_export').linkbutton({});
		$('#button_borrow_out').linkbutton({});
		$('#button_borrow_cancel').linkbutton({});
		  
		showDate('borrowerStartDate');
		$('#borrowerShowFlag').val('Y');
	} 	 
}
/**
 * 只有說明欄位的作業畫面加載 
 */
function openManyAction(divId, title,comment,comfrimId,canelId){
	if($('#' + divId + "ShowFlag").val()!='Y'){
		$('#' + divId).attr("name","edit"); 
		$('#' + divId).css("display","");
		$('#' + divId).panel({      
		  	title: title,     
		 	closed: true   
		});                  	
	        
		$('#' + comment).textbox({ validTyp:'maxLength[200]'});
		$('#' + comment).textbox('textbox').attr('maxlength', 200);
		 
		$('#' + comfrimId).linkbutton({});     
	    $('#' + canelId).linkbutton({}); 
	    $('#' + divId + "ShowFlag").val('Y');      
	}         	
}
/**
 * 列印清冊(借用單)
 */
function exportBorrow(contextPath) {
	var f = $("#borrowForm");
	//如果通過驗證
	if (f.form('validate')) {
		$("#message").empty();
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		if (rows == null || rows.length == 0){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		if ($("#borrowerShowFlag").val()=='Y') {
			//驗證借用人
			if($("#borrows").combobox('getValue') != $("#userName").val()){
				if(!queryEmployee()){
					return false;
				}
			}
		}
		var assetIdList = [];
		var propertyId = "";
		//初始化數組
		borrowerArray = new Array();
		var assetIdListRow = null;
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			for (var i = 0; i < rows.length; i++) {
				var itemValue = rows[i].itemValue;
				if (itemValue != 'REPERTORY' && (itemValue)) {
					alertPromptCommon('設備狀態不為庫存，不可進行借用作業', false, function(){
					});
					return false;
				}
				propertyId = rows[i].propertyId;
				if (propertyId == "") {
					alertPromptCommon('設備無財產編號，不可進行借用作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
				//將選中的設備id放入數組中
				borrowerArray[i] = rows[i].assetId;
			}
			//將所選設備id集合轉為string
			assetIdListRow = assetIdList.toString();
		}
		$("#queryAssetIdList").val(assetIdListRow);
		//修改借用人名稱獲取欄位 update by 2017/08/31
		$("#borrowerName").val($("#borrowsName").html());
		//判斷是否批量處理按鈕
		if($(":checkbox[name=queryAll]").is(":checked")) {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			javascript:dwr.engine.setAsync(false);
			var editParams = getQueryParamter();
			editParams.editFlag='borrow';
			editParams.queryAllSelected ='Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParams.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParams.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParams.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParams.codeGunFlag = 'serialNumber';
				}
			}
			//將查詢條件轉為json字符串
			var str = JSON.stringify(editParams);
			var isError = false;
			$.ajax({
				url: contextPath+"/assetManage.do?actionId=getAssetIdList",
				async: false,
				data: {queryParam:str},
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(json){
					if (json.success) {
						var data = json.returnMsg;
						if (data =='設備狀態不為庫存，不可進行借用作業'||data =='設備無財產編號，不可進行借用作業' ||data == '列印借用單不可超過1000筆') {
							$.unblockUI();
							alertPromptCommon(data, false, function(){
							});
							
						} else {
							
							$("input[name='actionId']").each(function(){  
									$(this).val('');
								}  
							);   
							$("input[name='serviceId']").each(function(){  
									$(this).val('');
								}  
							);  
							$("input[name='useCaseNo']").each(function(){  
									$(this).val('');
								}  
							); 
							
							assetIdListRow = data;
							$("#queryAssetIdList").val(assetIdListRow);
							//提交表單
							actionClicked(document.forms["borrowForm"], 'DMM03060', '', 'exportAsset');
							
							
							//設置列印借用單標誌為0
							$("#borrowFlag").val("0");	
						}
					} else {
						isError = true;
					}
				},
				error:function(){
					isError = true;
				}
			});
			if (isError) {
				$.unblockUI();
				$("#msgAction").append("列印借用單失敗");
				return false;
			}
			/*ajaxService.getAssetIdList(str, function(data){
				if (data !=null) {
					if (data =='設備狀態不為庫存，不可進行借用作業'||data =='設備無財產編號，不可進行借用作業' ||data == '列印借用單不可超過1000筆') {
						$.unblockUI();
						alertPromptCommon(data, false, function(){
						});
						
						return false;
					} else {
						
						$("input[name='actionId']").each(function(){  
								$(this).val('');
							}  
						);   
						$("input[name='serviceId']").each(function(){  
								$(this).val('');
							}  
						);  
						$("input[name='useCaseNo']").each(function(){  
								$(this).val('');
							}  
						); 
						
						assetIdListRow = data;
						$("#queryAssetIdList").val(assetIdListRow);
						//提交表單
						actionClicked(document.forms["borrowForm"], 'DMM03060', '', 'exportAsset');
						
						
						//設置列印借用單標誌為0
						$("#borrowFlag").val("0");	
					}
				} 
			});*/
			javascript:dwr.engine.setAsync(true);
			ajaxService.getExportFlag('DMM03060',function(data){
				$.unblockUI();
			});
		} else {
			
			$("input[name='actionId']").each(function(){  
					$(this).val('');
				}  
			);   
			$("input[name='serviceId']").each(function(){  
					$(this).val('');
				}  
			);  
			$("input[name='useCaseNo']").each(function(){  
					$(this).val('');
				}  
			); 
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			//提交表單
			actionClicked(document.forms["borrowForm"], 'DMM03060', '', 'exportAsset');
			
			ajaxService.getExportFlag('DMM03060',function(data){
				$.unblockUI();
			});
			//設置列印借用單標誌為0
			$("#borrowFlag").val("0");	
			
		}
	}	
}

/**
 * 打開作業畫面
 */
function showActionView(id, contextPath,jdwFlag) {
	//清空提示消息
	$('#msgAction').empty(); 
	$('div[name=edit]').panel('close');
	$("#taixinMaintenanceUserId").val('');
	$("#maintenanceUserId").val('');
	$("#userId").val('');
	$("#removeOrLossReason").removeClass("div-tips").addClass("div-list").tooltip("destroy");
	   
	//只有其他修改畫面id為‘’
	if (id != '') {
		//調用驗證
		if(openActionValidate(id, jdwFlag)){
			//當為領用，借用時向隱藏域複製
			if (id == 'carrier') {
				openActionCarry();
				//領用
				$("#carryCode").combobox('setValue', '');
				$("#carryCnEnName").html('');
				$("#carryAccount").val('');
				$("#carryComment").textbox('setValue', '');
				$("#carryName").val('');
				$('#accountFlag').val('account');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#carryCode").textbox('textbox').focus();
			} else if (id == 'borrower') {
				openActionBorrower();
				//借用
				$("#borrows").combobox('setValue', '');
				$("#borrowsName").html('');
				$("#borrowerEnd").datebox('setValue', '');
				$("#borrowerEmail").textbox('setValue', '');
				$("#borrowerMgrEmail").textbox('setValue', '');
				$("#borrowerComment").textbox('setValue', '');
				$("#userName").val('');
				$('#accountFlag').val('borrow');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#borrows").combobox('textbox').focus();
			} else if (id == 'assetIn') {
				openManyAction('assetIn', '入庫作業', 'assetInComment', 'asset_asset_To_In', 'asset_asset_To_cancel');
				//入庫作業
				$("#assetInComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#assetInComment").textbox('textbox').focus();
			} else if (id == 'carryBack') {
				//歸還作業
				openManyAction('carryBack', '歸還作業', 'carryBackComment', 'asset_back', 'asset_back_cancel');
				$("#carryBackComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#carryBackComment").textbox('textbox').focus();
			} else if (id == 'retired') {
				//報廢作業
				openManyAction('retired', '報廢作業', 'retiredComment', 'asset_retired', 'asset_retiredCancel');
				$("#retiredComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#retiredComment").textbox('textbox').focus();
			} else if (id == 'back') {
				//退回作業
				openManyAction('back', '退回作業', 'backComment', 'asset_backReturnBack', 'asset_backCancel');
				$("#backComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#backComment").textbox('textbox').focus();
			} else if (id == 'destroy') {
				//銷毀作業
				openManyAction('destroy', '銷毀作業', 'destroyComment', 'asset_destroy', 'asset_destroyCancel');
				$("#destroyComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#destroyComment").textbox('textbox').focus();
			} else if (id == 'repair') {
				//維修作業
				openRepairAction();
				$("#editFaultComponent").combobox("setValue", '');
				$("#repairVendor").combobox("setValue",'');
				$("#editFaultDescription").combobox("setValue", '');
				$("#repairComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#repairVendor").combobox('textbox').focus();
			} else if (id == 'retire') {
				opentRetireAction();
				//待報廢作業
				$("#retireReason").combobox('setValue', '');
				$("#retireComment").textbox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#retireReason").combobox('textbox').focus();
			} else if (id == 'repaired') {
				//送修作業
				openRepairedAction();
				$("#repairedComment").textbox('setValue', '');
				$("#repairedVendor").combobox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#repairedVendor").combobox('textbox').focus();
			} else if (id == 'taixinRent') {
				
				openTaixinRentdAction();
				//台新租賃
				$("#taixinDtid").textbox('setValue', '');
				$("#taixinCaseId").textbox('setValue', '');
				$("#taixinmerId").val('');
				$("#taixinName").textbox('setValue', '');
				$("#taixinHeader").combobox('loadData', initSelect());
				$("#taixinHeader").combobox('setValue', '');
				$("#taixinVendor").combobox('setValue', '');
				$("#taixinAddress").textbox('setValue', '');
				$("#taixinMid").textbox('setValue', '');
				$("#taixinMaintenanceUserCode").textbox('setValue', '');
				$("#taixinMaintenanceCnEnName").html("");
				$("#taixinSameAddress").prop("checked", true);
				$("#taixinLocation").combobox('readonly');
				$("#taixinAddress").textbox('readonly');
				$("#taixinLocation").combobox('setValue', '');
				$("#taixinIscup").prop('checked', false);
				$("#hidTaixinMid").val("");
				$('#accountFlag').val('taixin');
				$("#hideMaintenanceUserCode").val("");
				//如果解除綁定直接跳到台新租賃 將tooltip去掉
				$("#removeOrLossReason").tooltip("destroy");
				$("#taixinAnalyzeDate").datebox('setValue', '');
				$("#taixinCaseCompletionDate").datebox('setValue', '');
				//打開作業畫面
				$('#' + id).panel('open');
				$("#taixinDtid").textbox('textbox').focus();
			} else if (id == 'cancel') {
				openCancelAction();
				$('#cancel').css("display","");
				
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
	} else {
		//關閉作業畫面
		$('div[name=edit]').panel('close');
		$("#removeOrLossReason").removeClass("div-tips").addClass("div-list").tooltip("destroy");
		//打開其他作業畫面
		viewEditData(contextPath);
	}
}

/**
 * 設備序號編寫畫面確定
 */
function serialNumberInput() {
	$('#serialNumbers').textbox('textbox').blur();
	var f = $("#dlgQForm");
	if(f.form("validate")){
		var str = $("#serialNumbers").textbox("getValue");
		var point ='';
		//cr2232 多筆輸入查詢清除上次的值
		
		var number = null;
		if(str.length==1){
			str = str+"\n";
		}
		number = str.replaceAll("\n", ",").substring(0, str.length-1);
		
		var nowValue = $("#querySerialNumbers").textbox("getValue");
		$("#querySerialNumbers").textbox("setValue",'');
		number = number.replaceAll(",,", ",");
		if(number.length>1 && number.substring(0, 1)==','){
			number = number.substring(1, number.length);
		}
		if(number.length>1 && number.substring(number.length-1)==','){
			number = number.substring(0, number.length-1);
		}
		if(number == ','){
			number='';
		}
		//向輸入框賦值
		$("#querySerialNumbers").textbox("setValue", number);
		$('#dlgQ').dialog('close');
		$('#serialNumbers').textbox('clear');
		//獲取全部查詢條件
		var qParam = getQueryParamter();
		qParam.actionId = 'query';
		qParam.querySerialNumbers = $("#querySerialNumbers").val().toString();
		
		queryDevice(1, true, qParam);
	}	
}

/**
 * 財產編號編輯畫面確定
 */
function propertyIdInput() {
	$('#propertyIds').textbox('textbox').blur();
	var f = $("#dlgInputForm");
	//表單驗證
	if(f.form("validate")){
		var point ='';
		var str = $("#propertyIds").textbox("getValue");
		//cr2232 多筆輸入查詢清除上次的值

		var property = str.replaceAll("\n",",");
		$("#queryPropertyIds").textbox("setValue",'');
		var number = property.replaceAll(",,",",");
		if(number.length>1 && number.substring(0, 1)==','){
			number = number.substring(1, number.length);
		}
		if(number.length>1 && number.substring(number.length-1)==','){
			number = number.substring(0, number.length-1);
		}
		if(number == ','){
			number='';
		}
		//向輸入框賦值
		$("#queryPropertyIds").textbox("setValue", number);
		$('#dlgInput').dialog('close');
		$('#propertyIds').textbox('clear');
		//獲取全部查詢條件
		var qParam = getQueryParamter();
		qParam.actionId = 'query';
		qParam.queryPropertyIds = $("#queryPropertyIds").val().toString();
		
		queryDevice(1, true, qParam);
	}
}


/**
 * 發送修改請求
 */
function doAjax(editParam,editFlag, contextPath){
	var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
	$.blockUI(blockStyle1);
	$.ajax({
		url : contextPath+"/assetManage.do",
		data : editParam,
		type : 'post', 
		cache : false, 
		dataType : 'json',
		success : function(json) {
			if (json.success) {
				$("#dataGrid").datagrid("clearSelections");
				var pageIndex = getGridCurrentPagerIndex("dataGrid");
				if(codeGunParam!= undefined){
					queryDevice(pageIndex, false, codeGunParam, $("#hideCodeGunAfterQuery").val());
				} else {
					queryDevice(pageIndex, false);
				}
				//成功提示
				$("#msgAction").text(json.msg);
				//將借用標誌寫為1
				if (editFlag == "borrow") {
					$("#borrowFlag").val("1");
				}
			} else {
				//失敗提示
				$("#msgAction").text(json.msg);
			}
			$.unblockUI();
		},
		error : function(){
			$("#msgAction").text("修改失敗！請聯繫管理員");
			$.unblockUI();
		}	
	}); 
}
/*
 * 保存借用人領用人cookie
 */
function saveBorrowActionCookie(id){
	//Task #2564 領用人、借用人 欄位，使用 autocomplete 效果，CLIET記住輸入內容 2017/10/11
	var result = null;
	var val = null;
	try {
		//如果cookie中存在該值
		var cookie = $.cookie(id);
		if (cookie != null) {
			result = JSON.parse(cookie);
			//轉化為json格式，去除重複
			val = $("#" +id).combobox('getValue');
			if (val && $.trim(val).length > 0) {				
				var repeate = false;
				for (j = 0; j < result.length; j ++) {
					if (result[j].value == $.trim(val)) {
						repeate = true;
						break;
					}					
				}
				if (!repeate) {
					result.push({value : $.trim(val), name : $.trim(val)});
				}
			}
			 $("#" + id).combobox('loadData', result);
			 //將值寫入cookie之中
			 $.cookie(id,JSON.stringify(result));			
		} else {
			val = $("#"+id).combobox('getValue');
			result='[{"value":"'+$.trim(val)+'","name":"'+$.trim(val)+'"}]';
			$.cookie(id ,result);
		} 
	} catch(e) {
		$.cookie(id,null);
	}
}
/**
 * 領用作業 
 */
function carry(contextPath) {
	var f = $("#carrierForm");
	//表單驗證
	if (f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		var propertyId= "";
		//驗證領用人
		if ($("#carryCode").combobox('getValue') != $("#carryName").val()) {
			if (!queryEmployee()) {
				return false;
			}
		}
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;	
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				propertyId = rows[i].propertyId;
				if (itemValue != 'REPERTORY' && (itemValue)) {
					alertPromptCommon('設備狀態不為庫存，不可進行領用作業', false, function(){
					});
					return false;
				}
				//驗證財產編號
				if (propertyId=="") {
					alertPromptCommon('設備無財產編號，不可進行領用作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		
		//Task #2564 領用人、借用人 欄位，使用 autocomplete 效果，CLIET記住輸入內容 2017/10/11
		saveBorrowActionCookie('carryCode');
		
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					carrier : $("#carryCode").combobox('getValue'),
					carryAccount : $("#carryAccount").val(),
					carryComment : $("#carryComment").val(),
					carryDate : $("#date").textbox('getValue'),
					actionId : 'edit',
					editFlag : "carry",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N'
			};
		} else {
			editParam = getQueryParamter();
			editParam.carrier = $("#carryCode").combobox('getValue');
			editParam.carryAccount = $("#carryAccount").val();
			editParam.carryComment = $("#carryComment").val();
			editParam.carryDate = $("#date").textbox('getValue');
			editParam.actionId = 'edit';
			editParam.editFlag = "carry";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}	
		}
		doAjax(editParam, '', contextPath);
	}
}

/**
 * 借用作業
 */
function borrow(contextPath) {
	var f = $("#borrowForm");
	//表單驗證
	if (f.form('validate')) {
		//借用人驗證
		if($("#borrows").combobox('getValue')!=$("#userName").val()){
			if(!queryEmployee()){
				return false;
			}
		}
		var flag = false;
		var assetIdList = [];
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows == null || rows.length == 0){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		var propertyId = "";
		var newArray = new Array();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				propertyId = rows[i].propertyId;
				var itemValue = rows[i].itemValue;
				if (itemValue != 'REPERTORY' && (itemValue)) {
					alertPromptCommon('設備狀態不為庫存，不可進行借用作業', false, function(){
					});
					return false;
				}
				propertyId = rows[i].propertyId;
				if (propertyId == "") {
					alertPromptCommon('設備無財產編號，不可進行借用作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
				newArray[i]=rows[i].assetId;
			} 
		}
		//判斷是否列印借用單
		if(borrowerArray != undefined && (borrowerArray.length == newArray.length) && (borrowerArray.sort().toString() == newArray.sort().toString())) {
			flag = true;
		}
		var editParam;
		if(!flag){
			$.messager.confirm('確認對話框', '未列印借用單，確認送出？', function(b) {
				if(b){
					var assetIdListRow = assetIdList.toString();
					saveBorrowActionCookie('borrows');
					//判斷是否批量處理按鈕
					if(!$(":checkbox[name=queryAll]").is(":checked")) {
						editParam = {
								borrower : $("#borrows").combobox("getValue"),
								borrowerEnd : $("#borrowerEnd").datebox("getValue"),
								borrowerEmail : $("#borrowerEmail").val(),
								borrowerStart : $("#borrowerStart").val(),
								borrowerMgrEmail : $("#borrowerMgrEmail").val(),
								borrowerComment : $("#borrowerComment").val(),
								actionId : 'edit',
								editFlag : "borrow",
								queryAssetIds : assetIdListRow,
								userId : $("#userId").val(),
								queryAllSelected : 'N'
						};
					} else {
						editParam = getQueryParamter();
						editParam.borrower = $("#borrows").combobox("getValue");
						editParam.borrowerEnd = $("#borrowerEnd").datebox("getValue");
						editParam.borrowerEmail = $("#borrowerEmail").val();
						editParam.borrowerStart = $("#borrowerStart").val();
						editParam.borrowerMgrEmail = $("#borrowerMgrEmail").val();
						editParam.borrowerComment = $("#borrowerComment").val();
						editParam.actionId = 'edit';
						editParam.editFlag = "borrow";
						editParam.userId = $("#userId").val();
						editParam.queryAllSelected = 'Y';
						if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
							editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
							if($("#hideCodeGunFlag").val()=='2'){
								editParam.codeGunFlag = 'propertyId';
							}
						}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
							editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
							if($("#hideCodeGunFlag").val()=='1'){
								editParam.codeGunFlag = 'serialNumber';
							}
						}
					}
					doAjax(editParam, '', contextPath);
				} else {
					return false;
				}
			});
		} else {
			var assetIdListRow = assetIdList.toString();
			saveBorrowActionCookie('borrows');
			//判斷是否批量處理按鈕
			if(!$(":checkbox[name=queryAll]").is(":checked")) {
				editParam = {
						borrower : $("#borrows").combobox("getValue"),
						borrowerEnd : $("#borrowerEnd").datebox("getValue"),
						borrowerEmail : $("#borrowerEmail").val(),
						borrowerMgrEmail : $("#borrowerMgrEmail").val(),
						borrowerStart : $("#borrowerStart").val(),
						borrowerComment : $("#borrowerComment").val(),
						actionId : 'edit',
						editFlag : "borrow",
						queryAssetIds : assetIdListRow,
						userId : $("#userId").val(),
						queryAllSelected : 'N'
				};
			} else {
				editParam = getQueryParamter();
				editParam.borrower = $("#borrows").combobox("getValue");
				editParam.borrowerEnd = $("#borrowerEnd").datebox("getValue");
				editParam.borrowerEmail = $("#borrowerEmail").val();
				editParam.borrowerMgrEmail = $("#borrowerMgrEmail").val();
				editParam.borrowerStart = $("#borrowerStart").val();
				editParam.borrowerComment = $("#borrowerComment").val();
				editParam.actionId = 'edit';
				editParam.editFlag = "borrow";
				editParam.userId = $("#userId").val();
				editParam.queryAllSelected = 'Y';
				if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
					editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
					if($("#hideCodeGunFlag").val()=='2'){
						editParam.codeGunFlag = 'propertyId';
					}
				}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
					editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
					if($("#hideCodeGunFlag").val()=='1'){
						editParam.codeGunFlag = 'serialNumber';
					}
				}
			}
			doAjax(editParam, '', contextPath);
		}
	}
}

/**
 * 歸還作業 
 */
function back(contextPath) {
	var f = $("#carryBackForm");
	//表單驗證
	if (f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if (itemValue != 'BORROWING' && (itemValue)) {
					alertPromptCommon('設備狀態不為借用中，不可進行歸還作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#carryBackComment").val(),
					actionId : 'edit',
					editFlag : "back",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.description = $("#carryBackComment").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "back";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	}
}

/**
 * 入庫作業
 */
function assetIn(contextPath) {
	var f = $("#assetInForm");
	//表單驗證
	if (f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				//狀態若不為“領用中”或“維修中”或“送修中”或“已拆回”，則顯示訊息「設備狀態不為領用中或維修中或送修中或已拆回，不可進行入庫作業」
				if (itemValue != 'IN_APPLY' && itemValue != 'REPAIR' && itemValue != 'MAINTENANCE' && itemValue != 'RETURNED' && (itemValue)) {
					alertPromptCommon('設備狀態不為領用中或維修中或送修中或已拆回，不可進行入庫作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#assetInComment").val(),
					actionId : 'edit',
					editFlag : "assetIn",
					status : itemValue,
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.description = $("#assetInComment").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "assetIn";
			editParam.status = itemValue;
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	}
}

/**
 * 維修作業 
 */
function fault(contextPath) {
	var f = $("#repairForm");
	//下拉框驗證
	var controls = ['repairVendor','editFaultComponent','editFaultDescription'];
	//表單驗證
	if (validateForm(controls) && f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if (itemValue != 'REPERTORY' && (itemValue)) {
					alertPromptCommon('設備狀態不為庫存，不可進行維修作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					faultComponentId : $("#editFaultComponent").combobox("getValues").toString(),
					repairVendorId : $("#repairVendor").combobox("getValue"),
					faultDescriptionId : $("#editFaultDescription").combobox("getValues").toString(),
					actionId : 'edit',
					description : $("#repairComment").val(),
					editFlag : "repair",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.faultComponentId = $("#editFaultComponent").combobox("getValues").toString();
			editParam.repairVendorId = $("#repairVendor").combobox("getValue");
			editParam.faultDescriptionId = $("#editFaultDescription").combobox("getValues").toString();
			editParam.actionId = 'edit';
			editParam.description = $("#repairComment").val();
			editParam.editFlag = "repair";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	}
}

/**
 * 送修作業
 */
function repaired(contextPath){
	var f = $("#repairedForm");
	//下拉框驗證
	var controls = ['repairedVendor'];
	//進行相關驗證
	if (validateForm(controls) && f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if (itemValue != 'REPAIR' && (itemValue)) {
					alertPromptCommon('設備狀態不為維修中，不可進行送修作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#repairedComment").textbox('getValue'),
					actionId : 'edit',
					editFlag : "repaired",
					queryAssetIds : assetIdListRow,
					repairVendorId : $("#repairedVendor").combobox("getValue"),
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.description = $("#repairedComment").textbox('getValue');
			editParam.actionId = 'edit';
			editParam.editFlag = "repaired";
			editParam.repairVendorId = $("#repairedVendor").combobox("getValue");
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	 } 
}

/**
 * 待報廢作業 
 */
function retireReasons(contextPath) {
	var f = $("#retireForm");
	//下拉框驗證
	var controls = ['retireReason'];
	//表單驗證
	if (validateForm(controls) && f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if ((itemValue != 'REPERTORY' && itemValue != 'REPAIR' && itemValue != 'MAINTENANCE' && itemValue != 'LOST') && (itemValue)) {
					alertPromptCommon('設備狀態不為庫存或維修中或送修中或已遺失，不可進行待報廢作業', false, function(){
					});
					return false;
				} 
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					retireReasonCode : $("#retireReason").combobox("getValue"),
					retireComment : $("#retireComment").val(),
					actionId : 'edit',
					editFlag : "retire",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.retireReasonCode = $("#retireReason").combobox("getValue");
			editParam.retireComment = $("#retireComment").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "retire";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);	
	}
}

/**
 * 報廢作業 
 */
function retired(contextPath) {
	var f = $("#retiredForm");
	//表單驗證
	if (f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if (itemValue != 'PENDING_DISABLED' && (itemValue)) {
					alertPromptCommon('設備狀態不為待報廢，不可進行報廢作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#retiredComment").val(),
					actionId : 'edit',
					editFlag : "retired",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.description = $("#retiredComment").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "retired";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	}
}

/**
 * 退回作業 
 */
function returnBack(contextPath) {
	var f = $("#backForm");
	//表單驗證
	if (f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if (itemValue != 'PENDING_DISABLED' && (itemValue)) {
					alertPromptCommon('設備狀態不為待報廢，不可進行退回作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#backComment").val(),
					actionId : 'edit',
					editFlag : "return",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.description = $("#backComment").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "return";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	}
}
/**
 * 銷毀作業
 */
function destroy(contextPath) {
	var f = $("#destroyForm");
	//表單驗證
	if (f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		//是否勾選資料
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				if (itemValue != 'DISABLED' && (itemValue)) {
					alertPromptCommon('設備狀態不為報廢，不可進行銷毀作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#destroyComment").val(),
					actionId : 'edit',
					editFlag : "delete",
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.description = $("#destroyComment").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "delete";
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	}
}
/**
 * 解除綁定作業
 */
function cancelRemoveOrLoss(contextPath){
	//下拉框驗證
	var controls = ['removeOrLossReason'];
	//驗證領用人
	if ($("#maintenanceUserCode").textbox('getValue') != $("#hideMaintenanceUserCode").val()) {
		if (!queryEmployee('maintenanceUser')) {
			return false;
		}
	}
	var f = $("#cancelForm");
	//進行相關驗證
	if (validateForm(controls) && f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		var assetCategory = "";
		var companyCode = "";
		
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				assetCategory = rows[i].assetCateGory;
				companyCode = rows[i].companyCode;
				//設備狀態不為使用中，不可進行解除綁定作業
				if (itemValue != 'IN_USE' && (itemValue)) {
					alertPromptCommon('設備狀態不為使用中，不可進行解除綁定作業', false, function(){
					});
					return false;
				} else if(companyCode != 'TSB-EDC' && assetCategory != 'Related_Products'){
					alertPromptCommon('使用中的EDC設備，不可進行解除綁定作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		//修改複選框的驗證狀態改變class
		$("#removeOrLossReason").removeClass("div-tips").addClass("div-list").tooltip("destroy");
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					description : $("#removeOrLossDescription").val(),
					actionId : 'edit',
					editFlag : "remove",
					status : $("[name=removeOrLossStatus]:checked").val(),
					maintainCompany : $("#maintenanceVendor").combobox('getValue'),
					maintainUser : $("#maintenanceUserId").val(),
					analyzeDate : $("#analyzeDate").datebox('getValue'),
					caseCompletionDate : $("#caseCompletionDate").datebox('getValue'), 
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N'
			};
		} else {
			editParam = getQueryParamter();
			//editParam = {
			editParam.description = $("#removeOrLossDescription").val();
			editParam.actionId = 'edit';
			editParam.editFlag = "remove";
			editParam.status = $("[name=removeOrLossStatus]:checked").val();
			editParam.maintainCompany = $("#maintenanceVendor").combobox('getValue'); 
			editParam.maintainUser = $("#maintenanceUserId").val();
			editParam.analyzeDate =$("#analyzeDate").datebox('getValue');
			editParam.caseCompletionDate = $("#caseCompletionDate").datebox('getValue'); 
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		$("#maintenanceUserCode").textbox('disableValidation');
		doAjax(editParam, '', contextPath);
	}
}

/**
 * 台新租賃維護作業
 */
function taixinAction(contextPath){
	var f = $("#taiXinForm");
	var taixinMid = $("#taixinMid").textbox('getValue');
	var hidTaixinMid = $("#hidTaixinMid").val();
	//清空提示消息
	$('#msgAction').empty();
	//進行相關驗證4
	//下拉框驗證
	var controls = ['taixinDtid', 'taixinCaseId', 'taixinMid', 'taixinName', 'taixinHeader', 'taixinVendor', 'isEnableDate', 'taixinCaseCompletionDate','taixinAnalyzeDate', 'taixinLocation',  'taixinAddress'];
	if (validateForm(controls) && f.form('validate')) {
		var assetIdList = [];
		var itemValue = "";
		var companyCode = "";
		//當隱藏域沒有值時，提示消息
		if(hidTaixinMid == '' || hidTaixinMid == null) {
			$('#msgAction').text("請根據特店代號帶入資料");
			handleScrollTop("assetManageDivId");
			return false;
			//特點代號變更時，提示消息，清空相關欄位
		} else if(taixinMid != hidTaixinMid) {
			$('#msgAction').text("特店代號已變更，請重新帶入資料");
			//當無特店資料時，清空相關欄位
			$("#taixinHeader").combobox("loadData", initSelect(null));
			$("#taixinHeader").combobox("setValue", "");
			$("#taixinName").textbox("setValue", "");
			$("#taixinSameAddress").prop("checked", true);
			$("#taixinLocation").combobox('readonly');
			$("#taixinAddress").textbox('readonly');
			$("#taixinAddress").textbox('enable');
			$("#taixinLocation").combobox('enable');
			$("#taixinAddress").textbox('readonly', false);
			$("#taixinLocation").combobox('readonly', false);
			$("#taixinAddress").textbox('setValue', '');
			$("#taixinLocation").combobox('setValue', '');
			handleScrollTop("assetManageDivId");
			return false;
		}
		//驗證工程師
		if ($("#taixinMaintenanceUserCode").textbox('getValue') != $("#hideMaintenanceUserCode").val()) {
			if (!queryEmployee('taixinMaintenanceUser')) {
				return false;
			}
		}
		//獲取選中行
		var rows = $("#dataGrid").datagrid('getSelections');
		if (rows < 1){
			if (!alertCheckMessage()) {
				return false;
			}
		}
		//CR #2696 增加捷達威維護 2017/10/25
		var msg = '設備狀態不為領用中或使用中，不可進行台新租賃維護作業';
		var msgOwner = '設備使用人不為台新，不可進行台新租賃維護作業';
		var company = 'TSB-EDC';
		var editFlag = "taixinRent";
		if($("#hidJdwFlag").val() == 'jdw'){
			msg = '設備狀態不為領用中或使用中，不可進行捷達威維護作業';
			msgOwner = '設備使用人不為捷達威，不可進行捷達威維護作業';
			company = 'JDW-EDC';
			editFlag = 'JDWMaintenance';
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				companyCode = rows[i].companyCode;
				if (itemValue != 'IN_USE' && itemValue != 'IN_APPLY' && (itemValue)) {
					alertPromptCommon(msg, false, function(){
					});
					return false;
				} else if(companyCode != company){
					alertPromptCommon(msgOwner, false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
		}
		var assetIdListRow = assetIdList.toString();
		var isCup = '';
		//是否是cup
		if($(":checkbox[name=taixinIscup]").is(":checked")){
			isCup = 'Y';
		} else {
			isCup = 'N';
		}
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			editParam = {
					dtid : $("#taixinDtid").textbox('getValue'),
					caseId : $("#taixinCaseId").textbox('getValue'),
					merchantId : $("#taixinmerId").val(),
					merchantHeaderId : $("#taixinHeader").combobox('getValue'),
					maintainCompany : $("#taixinVendor").combobox('getValue'),
					installedAdress :$("#taixinAddress").textbox('getValue'),
					installedAdressLocation : $("#taixinLocation").combobox('getValue'),
					isCup : isCup,
					actionId : 'edit',
					editFlag : editFlag,
					maintainUser : $("#taixinMaintenanceUserId").val(),
					enableDate : $("#isEnableDate").datebox('getValue'), 
					analyzeDate : $("#taixinAnalyzeDate").datebox('getValue'), 
					caseCompletionDate : $("#taixinCaseCompletionDate").datebox('getValue'), 
					queryAssetIds : assetIdListRow,
					queryAllSelected : 'N',
			};
		} else {
			editParam = getQueryParamter();
			editParam.dtid = $("#taixinDtid").textbox('getValue');
			editParam.caseId = $("#taixinCaseId").textbox('getValue');
			editParam.merchantId = $("#taixinmerId").val();
			editParam.merchantHeaderId = $("#taixinHeader").combobox('getValue');
			editParam.maintainCompany = $("#taixinVendor").combobox('getValue');
			editParam.installedAdress = $("#taixinAddress").textbox('getValue');
			editParam.installedAdressLocation = $("#taixinLocation").combobox('getValue');
			editParam.isCup = isCup;
			editParam.actionId = 'edit';
			editParam.editFlag = editFlag;
			editParam.maintainUser = $("#taixinMaintenanceUserId").val();
			editParam.enableDate = $("#isEnableDate").datebox('getValue'); 
			editParam.analyzeDate = $("#taixinAnalyzeDate").datebox('getValue');
			editParam.caseCompletionDate = $("#taixinCaseCompletionDate").datebox('getValue'); 
			editParam.queryAllSelected = 'Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParam.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParam.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParam.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParam.codeGunFlag = 'serialNumber';
				}
			}
		}
		doAjax(editParam, '', contextPath);
	 } 
}

/**
 * 列印借用單壓縮
 */
function exportBorrowData(contextPath) {
	//清空大話面提示消息
	$("#message").empty();
	$('#msgAction').empty();
	//獲取選中行
	var rows = $("#dataGrid").datagrid('getSelections');
	if (rows == null || rows.length == 0) {
		if (!alertCheckMessage()) {
			return false;
		}
	} else {
		var assetIdList = [];
		var assetIdListRow = null;
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				var itemValue = rows[i].itemValue;
				if (itemValue != 'BORROWING' && (itemValue)) {
					alertPromptCommon('設備狀態不為借用中，不可進行列印借用單作業', false, function(){
					});
					return false;
				}
				assetIdList.push(rows[i].assetId);
			}
			assetIdListRow = assetIdList.toString();
		}
		//判斷是否批量處理按鈕
		if($(":checkbox[name=queryAll]").is(":checked")) {
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			javascript:dwr.engine.setAsync(false);
			var editParams = getQueryParamter();
			editParams.editFlag='downloadZip';
			editParams.queryAllSelected ='Y';
			if(codeGunParam !=undefined && codeGunParam.queryPropertyIds!=null && codeGunParam.queryPropertyIds!=''){
				editParams.queryPropertyIds = codeGunParam.queryPropertyIds;
				if($("#hideCodeGunFlag").val()=='2'){
					editParams.codeGunFlag = 'propertyId';
				}
			}else if(codeGunParam !=undefined && codeGunParam.querySerialNumbers!=null && codeGunParam.querySerialNumbers!=''){
				editParams.querySerialNumbers = codeGunParam.querySerialNumbers;
				if($("#hideCodeGunFlag").val()=='1'){
					editParams.codeGunFlag = 'serialNumber';
				}
			}
			//將查詢條件轉為json字符串
			var isError = false;
			var str = JSON.stringify(editParams);
			$.ajax({
				url: contextPath+"/assetManage.do?actionId=getAssetIdList",
				async: false,
				data: {queryParam:str},
				type:'post', 
				cache:false, 
				dataType:'json', 
				success:function(json){
					if (json.success) {
						var data = json.returnMsg;
						if (data =='設備狀態不為借用中，不可進行列印借用單作業' || data =='列印借用單不可超過1000筆') {
							$.unblockUI();
							alertPromptCommon(data, false, function(){
								
							});
							return false;
						} else {
							$("input[name='actionId']").each(function(){  
									$(this).val('');
								}  
							);   
							$("input[name='serviceId']").each(function(){  
									$(this).val('');
								}  
							);  
							$("input[name='useCaseNo']").each(function(){  
									$(this).val('');
								}  
							); 
							
							assetIdListRow = data;
							$("#msgAction").text("");
							$("#queryAssetIds").val(assetIdListRow);
							actionClicked(document.forms["zipForm"], 'DMM03060', '', 'downloadZip');
						}
					} else {
						isError = true;
					}
				},
				error:function(){
					isError = true;
				}
			});
			if (isError) {
				$.unblockUI();
				$("#msgAction").append("列印借用單失敗");
				return false;
			}
			/*ajaxService.getAssetIdList(str, function(data){
				if (data !=null) {
					if (data =='設備狀態不為借用中，不可進行列印借用單作業' || data =='列印借用單不可超過1000筆') {
						$.unblockUI();
						alertPromptCommon(data, false, function(){
							
						});
						return false;
					} else {
						$("input[name='actionId']").each(function(){  
								$(this).val('');
							}  
						);   
						$("input[name='serviceId']").each(function(){  
								$(this).val('');
							}  
						);  
						$("input[name='useCaseNo']").each(function(){  
								$(this).val('');
							}  
						); 
						
						assetIdListRow = data;
						$("#msgAction").text("");
						$("#queryAssetIds").val(assetIdListRow);
						actionClicked(document.forms["zipForm"], 'DMM03060', '', 'downloadZip');
					}
				} 
			});*/
			javascript:dwr.engine.setAsync(true);
			ajaxService.getExportFlag('DMM03060',function(data){
				$.unblockUI();
			});
		} else {
			$("#msgAction").text("");
			$("#queryAssetIds").val(assetIdListRow);
			$("input[name='actionId']").each(function(){  
					$(this).val('');
				}  
			);   
			$("input[name='serviceId']").each(function(){  
					$(this).val('');
				}  
			);  
			$("input[name='useCaseNo']").each(function(){  
					$(this).val('');
				}  
			); 
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle);
			
			actionClicked(document.forms["zipForm"], 'DMM03060', '', 'downloadZip');
			
			ajaxService.getExportFlag('DMM03060',function(data){
				$.unblockUI();
			});
		}	
	}
}
/*執行作業  使用方法-------------------------------------------------------end*/
/*datagrid  使用方法-------------------------------------------------------start*/
/**
 * 格式化checkbox
 */
function formatterCheckbox(value,row,index){
	if (value == "Y") {
		return '<input type="checkbox" checked="checked" disabled="disabled">';
	} else {
		return '<input type="checkbox" disabled="disabled">';
	}
}


//格式化歷史查詢按鈕
function formatToHistory(val){
    if(val !=null){
    	return "<a href='javascript:void(0)'name='update' onclick='showAssetHistory(\"" + val + "\")'> </a>";
    }
}

/**
 * 格式化領用借用人
 */
function formatToCarry(val, row){
	if (row.itemValue=='BORROWING') {
		return row.borrower;
	} else if (row.itemValue=='IN_APPLY') {
		return row.carrier;
	}
}
//批量操作
function queryAllAction(){
	$("#queryAll").click(function(){
	   	if($(":checkbox[name=queryAll]").is(":checked")) {
	   		$("#isSelectedAll").val("1");
	   		$('#dataGrid').datagrid('selectAll');
	   	} else {
	   		$("#isSelectedAll").val("0");
	   		$('#dataGrid').datagrid('unselectAll');
	   	}
	});
	if($(":checkbox[name=queryAll]").is(":checked")) {
		$('#dataGrid').datagrid('selectAll');
	} 
	//如果全選按鈕取消選中，則批量操作按鈕取消選中
	$('#dataGrid').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).click(function(){
			if(!$('#dataGrid').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).is(":checked")) {
			$('#queryAll').prop('checked',false);
			$("#isSelectedAll").val("0");
		}
	}); 
}
/**
 * 取消按鈕
 */
function cancel() {
	//清空提示消息
	$('#msgAction').empty();
	//關閉畫面
	confirmCancel(function(){
		$('div[name=edit]').panel('close');
		//取消class的div-tips增加class的div-list
		$("#removeOrLossReason").removeClass("div-tips").addClass("div-list").tooltip("destroy");
	});
}
/*
 * 獲得查詢參數
 */
function getQueryParamter(){
	queryParam = {
	 	actionId : 'query',
		queryStorage : $("#queryStorage").combobox("getValue"),
		queryStatus : $("#queryStatus").combobox("getValue"),
		queryAssetCategory : $("#queryAssetCategory").combobox("getValue"),
		/*queryUser : $("#queryUser").combobox("getValue"),*/
		queryContractId : $("#queryContractId").combobox("getValue"),
		//Task #2991 單選 改為 複選
		queryAction : $("#queryAction").combobox("getValues").toString(),
		queryHeaderName : $("#queryHeaderName").val(),
		queryMerName : $("#queryMerName").val(),
		queryArea : $("#queryArea").combobox("getValue"),
	    queryMerchantCode : $("#queryMerchantCode").val(),
	    queryMaType : $("#queryMaType").combobox("getValue"), 
	    queryIsEnabled : $("#queryIsEnabled").combobox("getValue"),
	    //Task #2991 單選 改為 複選
	    assetTypeName : $("#assetTypeName").combobox("getValues").toString(),
	    queryKeeperName : $("#queryKeeperName").val(),
	    queryTid : $("#queryTid").val(),
	    queryAssetOwner : $("#queryAssetOwner").combobox("getValue"),
	    //Task #2991 單選 改為 複選
	    queryAssetUser : $("#queryAssetUser").combobox("getValues").toString(), 
	    queryDtid : $("#queryDtid").val(),
	    queryPropertyIds : $("#queryPropertyIds").val().toString(),
	    querySerialNumbers : $("#querySerialNumbers").val().toString(),
		beforeTicketCompletionDate : $("#beforeTicketCompletionDate").datebox('getValue'),
		afterTicketCompletionDate : $("#afterTicketCompletionDate").datebox('getValue'),
		beforeUpdateDate : $("#beforeUpdateDate").datebox('getValue'),
		afterUpdateDate : $("#afterUpdateDate").datebox('getValue'),
		queryIsCup : $("#queryIsCup").combobox("getValue"),
		queryCounter : $("#queryCounter").val(),
		queryCartonNo : $("#queryCartonNo").val(),
		queryModel : $("#queryModel").combobox("getValue")
	};           
	if (queryParam.assetTypeName =="請選擇") {
		queryParam.assetTypeName =null;
	}
	if($(":checkbox[name=history]").is(":checked")) {
		queryParam.actionId = "history";
	}
	return queryParam;
}     
/**
 * 獲取其他修改的修改參數
 */
function getUpdateParamter(form) {
	updateParam = {
		propertyId : form.find("#propertyId").textbox("getValue"),
		contractId : form.find("#contractId").combobox('getValue'),
		enableDate : form.find("#enableDate").datebox('getValue'),
		tid : form.find("#tid").textbox("getValue"),
		dtid : form.find("#dtid").textbox("getValue"),
		isEnabled : $(":checkbox[name=isEnabled]").is(":checked"),
		isSimEnable : $(":checkbox[name=isSimEnable]").is(":checked"), 
		simEnableDate : form.find("#simEnableDate").datebox('getValue'),
		checkedDate : form.find("#checkedDate").datebox('getValue'),
		factoryWarrantyDate : form.find("#factoryWarrantyDate").datebox('getValue'),
		customerWarrantyDate : form.find("#customerWarrantyDate").datebox('getValue'),
		description : form.find("#repairComment").val(),
		assetUser : form.find("#assetUser").combobox('getValue'),
		assetOwner : form.find("#assetOwner").combobox('getValue'),
		faultComponentId : form.find("#faultComponent").combobox('getValues').toString(),
		faultDescriptionId : form.find("#faultDescription").combobox('getValues').toString(),
		assetId : form.find("#editAssetId").val(),
		simEnableNo : form.find("#simEnableNo").textbox('getValue'),
		actionId : 'update'
	};
	if (updateParam.isEnabled ) {
		updateParam.isEnabled = "Y";
	}
	if (!updateParam.isEnabled) {
		updateParam.isEnabled = "N";
	}
	if (updateParam.isSimEnable ) {
		updateParam.isSimEnable = "Y";
	}
	if (!updateParam.isSimEnable) {
		updateParam.isSimEnable = "N";
	} 
	return updateParam;
}
/**
 * 獲得操作行的assetId
 */
function getSelectedId() {
	var assetId = '';
	var itemValue = '';
	var row = $("#dataGrid").datagrid('getSelections');
	if (row != null) {		
		if (row.length == 0) {				
			if (!alertCheckMessage()) {
				return false;
			}
		} else {
   			if (row.length > 1) {
   				$.messager.alert('提示訊息','每次只能操作一條記錄','warning'); 
				return false;
   			}
   			itemValue = row[0].itemValue;
		 	if (itemValue == 'DESTROY') {
				alertPromptCommon('設備狀態為已銷毀，不可進行其他修改作業', false, function(){
				});
				return false;
			}
   			assetId = row[0].assetId;
		} 
	} else {
		//請勾選資料
		if (!alertCheckMessage()) {
				return false;
			}
	}
	return assetId;
} 
/*datagrid  使用方法-------------------------------------------------------end*/

/*驗證  使用方法-------------------------------------------------------start*/

/**
 * 請勾選資料提示
 */
function alertCheckMessage() {
	alertPromptCommon('請勾選資料', false, function(){
	});
	return false;
}
/**
 * 檢核財產編號是否重複
 */
function checkPropertyId(contextPath){
	var isReapt = false;
	$.ajax({
        type : 'post',
        async : false,
        url : contextPath+"/assetManage.do",
        data : {
            propertyId : $("#propertyId").textbox("getValue"),
            actionId : 'check',
        	assetId : $("#editAssetId").val()
        },
        success : function(data) {
            if(data.success){
            	isReapt = true;
            } else {
            	$("#messager").text("財產編號不可重複，請重新輸入");
            	isReapt = false;
            	handleScrollTop('dlgg');
            }
        },
        error: function() {
        	
        }
    });
	return isReapt;
}

/**
 * 檢核dtid是否重複
 */
function checkDtid(contextPath){
	var isReapt = false;
	$.ajax({
        type : 'post',
        async : false,
        url : contextPath + "/assetManage.do",
        data : {
            dtid : $("#dtid").textbox("getValue"),
            actionId : 'checkDtid',
        	assetId : $("#editAssetId").val()
        },
        success : function(data) {
			if(data.success){
				isReapt = true;
            } else {
            	$("#messager").text("DTID不可重複，請重新輸入");
            	isReapt = false;
            	handleScrollTop('dlgg');
            }
        },
        error: function() {
        }
    });
	return isReapt;
}
/**
 * 打開作業畫面
 */
function openActionValidate(id, jdwFlag) {
	//獲取選中行
	var rows = $("#dataGrid").datagrid('getSelections');
	//若無選中行，提示消息
	if (rows == null || rows.length == 0) {
		if (!alertCheckMessage()) {
			return false;
		}
	} else {
		var itemValue = "";
		var propertyId = "";
		var companyCode = "";
		var assetCategory = "";
		var flag =false;
		//判斷是否批量處理按鈕
		if(!$(":checkbox[name=queryAll]").is(":checked")) {
			//驗證設備狀態
			for (var i = 0; i<rows.length; i++) {
				itemValue = rows[i].itemValue;
				propertyId = rows[i].propertyId;
				companyCode = rows[i].companyCode;
				assetCategory = rows[i].assetCateGory;
				if (id =='destroy') {
					//設備狀態不為報廢，不可進行銷毀作業
					if (itemValue != 'DISABLED' && (itemValue)) {
						alertPromptCommon('設備狀態不為報廢，不可進行銷毀作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					} 
				} else if (id =="back") {
					//設備狀態不為待報廢，不可進行退回作業
					if (itemValue != 'PENDING_DISABLED' && (itemValue)) {
						alertPromptCommon('設備狀態不為待報廢，不可進行退回作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					} 
				} else if (id =="repair") {
					//設備狀態不為庫存，不可進行維修作業
					if (itemValue != 'REPERTORY' && (itemValue)) {
						alertPromptCommon('設備狀態不為庫存，不可進行維修作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					} 
				} else if (id =="carrier") {
					//設備狀態不為庫存，不可進行領用作業
					if (itemValue != 'REPERTORY' && (itemValue)) {
						alertPromptCommon('設備狀態不為庫存，不可進行領用作業', false, function(){
							flag = false;
						});
						return false;
					} else if (propertyId=="") {
						//設備無財產編號，不可進行領用作業
						alertPromptCommon('設備無財產編號，不可進行領用作業', false, function(){
							flag = false;
						});
						return false;
					}
					else {
						flag = true;
					} 
				} else if (id =="assetIn") {
					//設備狀態不為領用中或維修中或送修中或已拆回，不可進行入庫作業
					if (itemValue != 'IN_APPLY' && itemValue != 'REPAIR' && itemValue != 'MAINTENANCE' && itemValue != 'RETURNED' && (itemValue)) {
						alertPromptCommon('設備狀態不為領用中或維修中或送修中或已拆回，不可進行入庫作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					}
				} else if (id =="borrower") {
					//設備狀態不為庫存，不可進行借用作業
					if (itemValue != 'REPERTORY' && (itemValue)) {
						alertPromptCommon('設備狀態不為庫存，不可進行借用作業', false, function(){
							flag = false;
						});
						return false;
					} else if (propertyId=="") {
						//設備無財產編號，不可進行借用作業
						alertPromptCommon('設備無財產編號，不可進行借用作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					} 
				} else if (id =="carryBack") {
					//設備狀態不為借用中，不可進行歸還作業
					if (itemValue != 'BORROWING' && (itemValue)) {
						alertPromptCommon('設備狀態不為借用中，不可進行歸還作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					} 
				} else if (id =="retire") {
					//狀態若不為“庫存、維修中、送修中、已遺失”，則顯示訊息「設備狀態不為庫存或維修中或送修中或已遺失，不可進行待報廢作業」
					if ((itemValue != 'REPERTORY' && itemValue != 'REPAIR' && itemValue != 'MAINTENANCE' && itemValue != 'LOST') && (itemValue)) {
						alertPromptCommon('設備狀態不為庫存或維修中或送修中或已遺失，不可進行待報廢作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					}
				} else if (id =="retired") {
					//設備狀態不為待報廢，不可進行報廢作業
					if (itemValue != 'PENDING_DISABLED' && (itemValue)) {
						alertPromptCommon('設備狀態不為待報廢，不可進行報廢作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					}
				} else if (id =="cancel") {
					//若狀態不為“使用中”，則顯示訊息「設備狀態不為使用中，不可進行解除綁定作業」
					if (itemValue != 'IN_USE' && (itemValue)) {
						alertPromptCommon('設備狀態不為使用中，不可進行解除綁定作業', false, function(){
							flag = false;
						});
						return false;
					} else if(companyCode != 'TSB-EDC' && assetCategory != 'Related_Products'){
						alertPromptCommon('使用中的EDC設備，不可進行解除綁定作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					}
				} else if (id =="taixinRent") {
					//當點擊捷達威維護時 CR #2696 增加捷達威維護 2017/10/25
					if(jdwFlag == 'jdw'){
						$("#hidJdwFlag").val('jdw');
						//若狀態不為“領用中、使用中”，則顯示訊息「設備狀態不為領用中或使用中，不可進行台新租賃維護作業」
						if (itemValue != 'IN_USE' && itemValue != 'IN_APPLY' && (itemValue)) {
							alertPromptCommon('設備狀態不為領用中或使用中，不可進行捷達威維護作業', false, function(){
								flag = false;
							});
							return false;
						} else if(companyCode != 'JDW-EDC'){
							alertPromptCommon('設備使用人不為捷達威，不可進行捷達威維護作業', false, function(){
								flag = false;
							});
							return false;
						} else {
							flag = true;
						}
					} else {
						$("#hidJdwFlag").val('');
						//若狀態不為“領用中、使用中”，則顯示訊息「設備狀態不為領用中或使用中，不可進行台新租賃維護作業」
						if (itemValue != 'IN_USE' && itemValue != 'IN_APPLY' && (itemValue)) {
							alertPromptCommon('設備狀態不為領用中或使用中，不可進行台新租賃維護作業', false, function(){
								flag = false;
							});
							return false;
						} else if(companyCode != 'TSB-EDC'){
							alertPromptCommon('設備使用人不為台新，不可進行台新租賃維護作業', false, function(){
								flag = false;
							});
							return false;
						} else {
							flag = true;
						}
					}
				} else if (id =="repaired") {
					//狀態若不為“維修中”，則顯示訊息「設備狀態不為維修中，不可進行送修作業」
					if (itemValue != 'REPAIR' && (itemValue)) {
						alertPromptCommon('設備狀態不為維修中，不可進行送修作業', false, function(){
							flag = false;
						});
						return false;
					} else {
						flag = true;
					}
				} 
			}
		} else {
			flag = true;
			if (id =="taixinRent") {
				//當點擊捷達威維護時 CR #2696 增加捷達威維護 2017/10/25
				if(jdwFlag == 'jdw'){
					$("#hidJdwFlag").val('jdw');
				} else {
					$("#hidJdwFlag").val('');
					
				}
			}
		}
		if (flag){
			return flag;
		}
	}
}

/*驗證  使用方法-------------------------------------------------------end*/

/*頁面  使用方法-------------------------------------------------------start*/

/**
*加載匯出內容
*/
function showDataListOInfo(flag){
	if(flag=='Y'){
		$("#exportBlank").css("display","none");
	} else {
		$("#exportBlank").css("display","");
	}
	if($('#showExportListFlag').val()!='Y'){
		
		 //datalist全選按鈕綁定事件
		 $("#exportCheckAll").click(function(){
			 // 調用datalist全選按鈕綁定事件
			 checkAllForDatalist('exportList', 'exportCheckAll');
		 });
		$('#exportList').datalist({ 
			    checkbox: true,
			    checked:false,
				singleSelect:false,
				selectOnCheck: false,
				toolbar:'#exportToolbarAll',
				checkOnSelect:true,
				selectOnCheck:true,
				idField:'colName',
				valueField:'colName',
				textField:'colText',
				onSelect:function(index,row) {
					dataListOnSelect(row, index, 'exportList', 'exportCheckAll');
				},
				
				onLoadSuccess : function(){
					datalistSuccessLoad('exportBlank', 'exportList', 'exportCheckAll');
				},
				onUnselect:function(index,row){
					dataListUnSelect(row, index, 'exportList', 'exportCheckAll');
				},
				data: [
					{colName:'status', colText:'狀態', value:'defaultShow'},
					{colName:'serialNumber', colText:'設備序號', value:'defaultShow'},
					{colName:'propertyId', colText:'財產編號'},
					{colName:'name', colText:'設備名稱', value:'defaultShow'},
					{colName:'brand', colText:'所在位置', value:'defaultShow'},
					{colName:'itemName', colText:'倉庫名稱'},
					{colName:'contractCode', colText:'合約編號'},
					{colName:'maType', colText:'維護模式'},
					{colName:'assetBrand', colText:'廠牌'},
					{colName:'model', colText:'型號'},
					{colName:'isEnabled', colText:'已啓用'},
					{colName:'enableDate', colText:'設備啓用日'},
					
					{colName:'simEnableDate', colText:'租賃期滿日'},
					{colName:'simEnableNo', colText:'租賃編號'},
					{colName:'carry', colText:'領用/借用人'},
					{colName:'borrowerStart', colText:'借用日期(起)'},
					{colName:'borrowerEnd', colText:'借用日期(迄)'},
					{colName:'backDate', colText:'歸還日期'},
					{colName:'caseId', colText:'案件編號'},
					{colName:'installType', colText:'裝機類別'},
					
					{colName:'caseCompletionDate', colText:'完修日期'},
					{colName:'tid', colText:'TID'},
					{colName:'merchantCode', colText:'特店代號'},
					{colName:'merName', colText:'特店名稱'},
					{colName:'headerName', colText:'特店表頭'},
					{colName:'areaName', colText:'裝機區域'},
					{colName:'dtid', colText:'DTID'},
					{colName:'merInstallAddress', colText:'裝機地址'},
					{colName:'appName', colText:'程式名稱'},
					{colName:'appVersion', colText:'程式版本'},
					{colName:'isCup', colText:'銀聯'},
					{colName:'assetInId', colText:'入庫批號'},
					{colName:'createUserName', colText:'建檔人員'},
					{colName:'assetInTime', colText:'入庫日期'},
					{colName:'isChecked', colText:'實際驗收'},
					{colName:'checkedDate', colText:'驗收日期'},
					{colName:'factoryWarrantyDate', colText:'原廠保固日期'},
					{colName:'isCustomerChecked', colText:'客戶實際驗收'},
					{colName:'customerApproveDate', colText:'客戶驗收日期'},
					{colName:'customerWarrantyDate', colText:'客戶保固日期'},
					{colName:'assetUserName', colText:'使用人'},
					{colName:'assetOwnerName', colText:'資產Owner'},
					{colName:'maintainCompany', colText:'維護廠商'},
					{colName:'repairVendor', colText:'維修廠商'},
					{colName:'faultComponent', colText:'故障組件'},
					{colName:'faultDescription', colText:'故障現象'},
					{colName:'description', colText:'說明/排除方式'},
					{colName:'action', colText:'執行作業'},
					{colName:'counter', colText:'櫃位'},
					{colName:'cartonNo', colText:'箱號'},
					{colName:'updateUserName', colText:'異動人員', value:'defaultShow'},
					{colName:'updateDate', colText:'異動日期', value:'defaultShow'},
					{colName:'departmentId', colText:'維護部門'},
					{colName:'vendorStaff', colText:'維護工程師'},
					{colName:'analyzeDate', colText:'簽收日期'},
					{colName:'problemReason', colText:'拆機/報修原因'},
					{colName:'warehouseConfirm', colText:'倉管確認'},
					{colName:'assetId', colText:'詳細', value:'defaultShow'}
					],
				lines:true  
			});
			$('#showExportListFlag').val('Y'); 
	}
}
/**
 * 動態顯示datagrid列
 */
function filterBlank(obj) {
	showDataListOInfo('N');
	// 其他需要顯示但不存在與datalist的列
	if($("#historyExport").val()=="0"){
		var otherColumn = ['v'];
	}
	// 處理欄位按鈕點擊打開選擇顯示欄位對話框
	dynamicshowColumn('exportBlank', 'exportList', 'dataGrid', otherColumn);

}
/**
*頁面匯出操作
*/
function exportData(){
	$("#message").empty();
	var rowLength = getGridRowsCount("dataGrid");
	showDataListOInfo('Y');
	var newRows = [];
	//定義數組放入動態欄位
	var selectRows = $("#exportList").datagrid('getSelections');
	//console.info(selectRows);
	if(selectRows){
		for(var i=0; i<selectRows.length; i++){
			var selectRow = selectRows[i];
			newRows.push(selectRow.colName);
		}
	}
	//去除詳情欄位
	newRows.remove("assetId");
	//去除v
	//newRows.remove("v");
	//放入要匯出的欄位
	document.getElementById("exportField").value=newRows;
	if($("#hideCodeGunFlag").val()=='1'){
		$("#codeGunFlag").val('serialNumber');
		$("#exportCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replaceAll("**",''));
		 
	} else if($("#hideCodeGunFlag").val()=='2'){
		$("#codeGunFlag").val('propertyId');
		$("#exportCodeGunPropertyIds").val($("#hideCodeGunPropertyIds").val().replaceAll("**",''));
	}
	$("input[name='actionId']").each(function(){  
			$(this).val('');
		}  
	);   
	$("input[name='serviceId']").each(function(){  
			$(this).val('');
		}  
	);  
	$("input[name='useCaseNo']").each(function(){  
			$(this).val('');
		}  
	);  
	//頁面查詢結果大於一行時
	if(rowLength >= 1){
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		actionClicked( document.forms["exportForm"],
			'DMM03060',
			'',
			'export');
		
		ajaxService.getExportFlag('DMM03060',function(data){
			$.unblockUI();
		});
	}
}

//刪除數組元素方法
Array.prototype.remove = function(val) {
								var index = this.indexOf(val);
								if (index > -1) {
								this.splice(index, 1);
								}
};

/**
 * 設備詳情查詢
 */
function detail(val,historyId, index, contextPath) {
    //$("#msg").empty();
    $("#msgAction").empty();
	if (historyId =="undefined") {
		historyId = null;
	}
	// 清空標志位 用來處理選中行清空
	var clearFlag = true;
	// 拿到所有選中行
	var rows = $("#dataGrid").datagrid('getSelections');
	if(rows){
		var indexArray = [];
		var tempIndex;
		// 得到所有選中行行號
		for(var i = 0; i < rows.length; i++){
			tempIndex = $('#dataGrid').datagrid('getRowIndex', rows[i]);
			indexArray.push(tempIndex);
		}
		// 判斷當前點擊行是否有必要選中
		if(indexArray && (indexArray.length != 0)){
			for(var j = 0; j < indexArray.length; j++){
				if(indexArray[j] == index){
					clearFlag = false;
					break;
				}
			}
		}
	}
	var isSelectedAll = null;
	if($("#isSelectedAll").val()=='1'){
		isSelectedAll = true;
	}

	viewDlg = $('#dlg').dialog({
		title : '設備詳細資訊',
		width : 900,
		height : 820,
		closed : false,
		cache : false,
		queryParams : {
			actionId : "detail",
			queryAssetId : val,
			queryHistId : historyId,
			queryCaseFlag : 'Y'
		},
		href : contextPath + "/assetManage.do",
		modal : true,
		onLoad :function() {
			// 不選中當前行
			if(clearFlag){
				$("#dataGrid").datagrid("unselectRow", index);
			// 選中當前行
			} else {
				$("#dataGrid").datagrid("selectRow", index);
			}
			if (isSelectedAll) {
				$('#queryAll').prop('checked',true);
				$("#isSelectedAll").val("1");
			}
			var options ={
				url :contextPath + "/assetManage.do",
				queryParams : {
					actionId : "history",
					queryAssetId : val,
					queryHistory : 'updateDate'
				},
				pageNumber : 1,
				autoRowHeight : true,
				isOpenDialog : true,
				onLoadError : function(data) {
				$("#msgAction").append("查詢失敗");
				},
				onLoadSuccess : function(data){
					$("#msgAction").empty();
					$("#message").empty();
					$('a[name=update]').linkbutton({ plain: true, iconCls: 'icon-search' });
					//為零行時提示信息
					if (data.total == 0) {
						$("#message").text(data.msg);
					}	
				}
			};
			openDlgGridQuery("dg1", options);
   		},
		buttons : [{
			text : '取消',
			width :'90px',
			iconCls : 'icon-cancel',
			handler : function() {
				viewDlg.dialog('close');
			}
		}],
	});
}
/**
 * 其他修改頁面打開方法
 */
function viewEditData(contextPath) {
   	$("#msgAction").empty();
    var assetId = getSelectedId();
    if (!assetId) {
    	return false;
    }
	viewDlg = $('#dlgg').dialog({
		title : '設備詳細資訊',
		width : 900,
		height : 730,
		closed : false,
		cache : false,
		queryParams : {
			actionId : "initEdit",
			queryAssetId : assetId
		},
		href : contextPath + "/assetManage.do",
		modal : true,
		onLoad : function() {
				dateboxSetting('dlgg');
		    	textBoxSetting('dlgg');
		    	if($("#enableDate").length>0){
		    		if($("#enableDate").datebox('getValue') != '' && $("#enableDate").datebox('getValue') != null){
			    		$("#isEnabled").prop("checked", true);
			    	}
		    		//設備啟用日蘭動啟用聚焦事件綁定
			    	$("#enableDate").textbox("textbox").bind("blur",function(){
						if(!isEmpty($('#enableDate').textbox("getText"))) {
							var dateValue = $('#enableDate').textbox("getText");
							$('#enableDate').focus();
							$('#enableDate').textbox("setText", "");
							$('#enableDate').blur();
							$('#enableDate').textbox("setText", dateValue);
							if ($(":checkbox[name=isEnabled]").is(":checked")) {
						    } else {
						    }
						   	$("#enableDate").datebox("enableValidation");
						} else {
					    	$("#isEnabled").prop("checked",false);
					    	$("#enableDate").datebox("disableValidation");
				    	}
					});		    	
			    	
					//如果已啟用未勾選，啟用日去除驗證				
			       	if($(":checkbox[name=isEnabled]").is(":checked")){
			       	} else {
			       		$("#enableDate").datebox("disableValidation");
			       	}
			       	//聚焦
			       	$("#propertyId").textbox('textbox').focus();
		    	}
		    	
      	},
		buttons : [{
			text : '儲存',
			width :'90px',
			iconCls : 'icon-ok',
			handler : function() {
				$("#isSimEnable").blur();
				$("#enableDate").blur();
				var f = viewDlg.find("#editForm");
				$("#messager").text(""); 
				controls = ['propertyId', 'enableDate', 'simEnableDate', 'tid', 'dtid',
							'checkedDate', 'factoryWarrantyDate', 'customerWarrantyDate',
							'assetUser','assetOwner'];
				if (validateForm(controls) && f.form("validate")) {
					if(!isEmpty($("#propertyId").textbox('getValue')) &&! checkPropertyId(contextPath)){
						return false;
					}
					//Task #3069
					/*if(!isEmpty($("#dtid").textbox('getValue')) && !checkDtid(contextPath)){
						return false;
					}*/
					//取值
					var saveParam = getUpdateParamter(f);
					// 調用保存遮罩，傳入參數爲當前dialog的id
                    commonSaveLoading('dlgg');
					//傳參
					$.ajax({
						url : contextPath+"/assetManage.do",
						data : saveParam,
						type : 'post', 
						cache : false, 
						dataType :'json',
						success : function(json) {
							// 去除保存遮罩
                         	commonCancelSaveLoading('dlgg');
							$("#messager").text("");
							if (json.success) {
								//取消所選中
								$("#dataGrid").datagrid("clearSelections");
								viewDlg.dialog('close');
								var pageIndex = getGridCurrentPagerIndex("dataGrid");
								if(codeGunParam!= undefined){
									queryDevice(pageIndex, false, codeGunParam, $("#hideCodeGunAfterQuery").val());
								} else {
									queryDevice(pageIndex, false);
								}
								//成功提示
								$("#msgAction").text(json.msg);
							} else {
								//失敗提示
								$("#messager").text(json.msg);
							}
						},
						error : function(){
							// 去除保存遮罩
                         	commonCancelSaveLoading('dlgg');
							$("#msgAction").text("设备资料修改失败");		
						}	
					});
				}
			}
		},
		{
			text : '取消',
			width :'90px',
			iconCls : 'icon-cancel',
			handler : function() {
				confirmCancel(function(){
					viewDlg.dialog('close');
				});
			}
		}],
	}); 
}

/*
 * 得到所有的column
 */
 function getAllColumnOptions(){
 	// Task #3086 開放案件處理及設備管理欄位模板先編輯再查詢
 	var allColumnOptions = [
 					{field:'v',width:80,align:'center',halign:'center',sortable:true,checkbox:true},
                     {field:'status',halign:'center',width:60,sortable:true,title:"狀態"},
                     {field:'itemValue',width:'auto',halign:'center',sortable:true,hidden:true,title:"狀態"},
                     {field:'serialNumber',width:140,halign:'center',sortable:true,title:"設備序號"},
                     {field:'propertyId',width:140,halign:'center',sortable:true,hidden:true,title:"財產編號"},
                     {field:'name',width:180,halign:'center',sortable:true,title:"設備名稱"},
                     {field:'brand',width:180,halign:'center',sortable:true,title:"所在位置"},
                     {field:'itemName',width:120,halign:'center',sortable:true,hidden:true,title:"倉庫名稱"},
                     {field:'contractCode',width:120,halign:'center',sortable:true,hidden:true,title:"合約編號"},
                     {field:'maType',width:100,halign:'center',sortable:true,hidden:true,title:"維護模式"},
                     {field:'assetBrand',width:100,halign:'center',sortable:true,hidden:true,title:"廠牌"},

                     {field:'model',width:120,halign:'center',sortable:true,hidden:true,title:"型號"},
                     {field:'isEnabled',width:80,halign:'center',align:'center',sortable:true,formatter:formatterCheckbox,hidden:true,title:"已啟用"},
                     {field:'enableDate',width:140,halign:'center',align:'center',sortable:true,formatter:formatLongToDate,hidden:true,title:"設備啟用日"},
                     {field:'simEnableDate',width:140,halign:'center',align:'center',sortable:true,formatter:formatLongToDate,hidden:true,title:"租賃期滿日"},
                     {field:'simEnableNo',width:80,halign:'center',align:'left',sortable:true,hidden:true,title:"租賃編號"},
                     {field:'carry',width:140,sortable:true,halign:'center',hidden:true,title:"領用/借用人"},
                     {field:'borrowerStart',width:140,sortable:true,halign:'center',align:'center',formatter:formatLongToDate,hidden:true,title:"借用日期(起)"},
                     {field:'borrowerEnd',width:140,sortable:true,halign:'center',align:'center',formatter:formatLongToDate,hidden:true,title:"借用日期(迄)"},
                     {field:'backDate',width:160,sortable:true,halign:'center',align:'center',formatter:formatLongToDate,hidden:true,title:"歸還日期"},
                     {field:'caseId',width:150,halign:'center',sortable:true,hidden:true,title:"案件編號"},
                     {field:'installType',width:140,halign:'center',sortable:true,hidden:true,title:"裝機類別"},

                     {field:'caseCompletionDate',width:140,halign:'center',align:'center',sortable:true,formatter:formatLongToDate,hidden:true,title:"完修日期"},
                     {field:'tid',width:100,halign:'center',sortable:true,hidden:true,title:"TID"},

                     {field:'merchantCode',width:180,halign:'center',sortable:true,hidden:true,title:"特店代號"},
                     {field:'merName',width:180,halign:'center',sortable:true,hidden:true,title:"特店名稱"},
                     {field:'headerName',width:180,halign:'center',sortable:true,hidden:true,title:"特店表頭"},
                     {field:'areaName',width:180,halign:'center',sortable:true,hidden:true,title:"裝機區域"},
                     {field:'dtid',width:100,halign:'center',sortable:true,hidden:true,title:"DTID"},
                     {field:'merInstallAddress',width:160,halign:'center',sortable:true,hidden:true,title:"裝機地址"},

                     {field:'appName',width:160,halign:'center',sortable:true,hidden:true,title:"程式名稱"},
                     {field:'appVersion',width:160,halign:'center',sortable:true,hidden:true,title:"程式版本"},
                     {field:'isCup',width:80,halign:'center',align:'center',sortable:true,formatter:formatterCheckbox,hidden:true,title:"銀聯"},
                     {field:'assetInId',width:140,halign:'center',sortable:true,hidden:true,title:"入庫批號"},
                     {field:'createUserName',width:120,halign:'center',sortable:true,hidden:true,title:"建檔人員"},
                     {field:'assetInTime',width:120,halign:'center',align:'center',sortable:true,formatter:formatLongToDate,hidden:true,title:"入庫日期"},
                     {field:'isChecked',width:80,halign:'center',align:'center',sortable:true,formatter:formatterCheckbox,hidden:true,title:"實際驗收"},
                     {field:'checkedDate',width:120,halign:'center',align:'center',sortable:true,formatter:formatLongToDate,hidden:true,title:"驗收日期"},
                     
                     {field:'factoryWarrantyDate',width:140,sortable:true,halign:'center',align:'center',formatter:formatLongToDate,hidden:true,title:"原廠保固日期"},
                     {field:'isCustomerChecked',width:110,halign:'center',align:'center',sortable:true,formatter:formatterCheckbox,hidden:true,title:"客戶實際驗收"},
                     {field:'customerApproveDate',width:120,halign:'center',align:'center',sortable:true,formatter:formatLongToDate,hidden:true,title:"客戶驗收日期"},
                     {field:'customerWarrantyDate',width:110,sortable:true,halign:'center',align:'center',formatter:formatLongToDate,hidden:true,title:"客戶保固日期"},
                     {field:'assetUserName',width:140,halign:'center',sortable:true,hidden:true,title:"使用人"},
                     {field:'assetOwnerName',width:140,halign:'center',sortable:true,hidden:true,title:"資產Owner"},
                     {field:'maintainCompany',width:140,halign:'center',sortable:true,hidden:true,title:"維護廠商"},
                     {field:'repairVendor',width:140,halign:'center',sortable:true,hidden:true,title:"維修廠商"},
                     {field:'faultComponent',width:140,halign:'center',sortable:true,hidden:true,title:"故障組件"},
                     {field:'faultDescription',width:140,halign:'center',sortable:true,hidden:true,title:"故障現象"},
                     {field:'description',halign:'center',sortable:true,hidden:true,title:"說明/排除方式"},
                     {field:'action',width:140,halign:'center',sortable:true,hidden:true,title:"執行作業"},
                     
                     {field:'counter',width:140,halign:'center',sortable:true,hidden:true,title:"櫃位"},
                     {field:'cartonNo',width:140,halign:'center',sortable:true,hidden:true,title:"箱號"},
                     
                     {field:'updateUserName',width:140,halign:'center',sortable:true,title:"異動人員"},
                     {field:'updateDate',width:180,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,title:"異動日期"},
                     {field:'departmentId',width:100,sortable:true,halign:'center',hidden:true,title:"維護部門"},
                     {field:'vendorStaff',width:140,sortable:true,halign:'center',hidden:true,title:"維護工程師"},
                     {field:'analyzeDate',width:160,sortable:true,align:'center',halign:'center',formatter:formatLongToDate,hidden:true,title:"簽收日期"},
                     {field:'problemReason',width:120,sortable:true,halign:'center',hidden:true,title:"拆機/報修原因"},
                     {field:'warehouseConfirm',width:100,sortable:false,halign:'center',hidden:true,title:"倉管確認"},
                     
                     {field:'borrower',width:'auto',sortable:true,halign:'center',hidden:true,title:"借用者"},
                     {field:'assetId',width:80,halign:'center',formatter:formatToDetail,align:'center',title:"詳細"},
                     {field:'historyId',width:'auto',sortable:true,halign:'center',hidden:true,title:"歷史庫存編號"},
                 	{field:'exportFlag',width:'auto',sortable:true,halign:'center',hidden:true,title:"匯出標誌"},
                 	{field:'carrier',width:'auto',sortable:true,halign:'center',hidden:true,title:"領用人"},
                     {field:'assetCateGory',width:'auto',sortable:true,halign:'center',hidden:true,title:"設備類別"},
                 
 	];
 	return allColumnOptions;
 }
 /**
  * 多筆輸入畫面加載
  */
 function beforeOpen(id){
 	$("#"+id).css("display","");
 	if(id=='dlgQ'){
 		$("#"+id).dialog({ 
 			top: 10,
 		    width: 400,    
 		    height: 410,
 		    closed: true,    
 		    buttons: "#dlgQ-buttons",    
 		    title: '多筆輸入',    
 		    modal: true   
 		}); 
 		$('#serialNumbers').textbox({multiline:true, validType:'numberOrEnglishOrEnter'});
 		$('#'+id).panel('open');
 		$('#serialNumbers').textbox('textbox').focus();
 		$('#serialNumbers').textbox('clear');
 		$('#serialNumber_linkbutton_comfirm').linkbutton({iconCls: 'icon-ok' });  
 		$('#serialNumber_linkbutton_cancel').linkbutton({iconCls: 'icon-cancel'}); 
 		$("#serialNumbers").textbox("textbox").keyup(function(e){
 			keyup(e);
 		}).change(function(e) {
 			changeSerialNumbers();
 		})
 	} else {
 		$("#"+id).dialog({ 
 			top: 10,
 		    width: 400,    
 		    height: 410,
 		    closed: true,    
 		    buttons: "#dlgQ-button",    
 		    title: '多筆輸入',    
 		    modal: true   
 		}); 
 		$('#propertyIds').textbox({multiline:true, validType:'numberOrEnglishOrEnter'});
 		$('#'+id).panel('open');
 		$('#propertyIds').textbox('textbox').focus();
 		$('#propertyIds').textbox('clear');
 		$('#propertyId_linkbutton_comfirm').linkbutton({iconCls: 'icon-ok' });  
 		$('#propertyId_linkbutton_cancel').linkbutton({iconCls: 'icon-cancel'}); 
 	}
 }
/*頁面  使用方法-------------------------------------------------------end*/
/*通知人員  使用方法-------------------------------------------------------start*/
/*
*CR #2563 發送mail方法
*/
 function sendMail(contextPath){
 	if($("#toMail").combobox('getValues')==null || $("#toMail").combobox('getValues')==''){
 		alertPromptCommon('請輸入通知人員', false, function(){
 		});
 		return false;
 	}
 	var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
 	$.blockUI(blockStyle1);
 	try {
 		sendMailParam.actionId='sendQueryMail';
 		var rowLength = getGridRowsCount("dataGrid");
 		showDataListOInfo('Y');
 		var newRows = [];
 		//定義數組放入動態欄位
 		var selectRows = $("#exportList").datagrid('getSelections');
 		if(selectRows){
 			for(var i=0; i<selectRows.length; i++){
 				var selectRow = selectRows[i];
 				newRows.push(selectRow.colName);
 			}
 		}
 		//去除詳情欄位
 		newRows.remove("assetId");
 		sendMailParam.exportField = newRows.toString();
 		sendMailParam.toMail = $("#toMail").combobox('getValues').toString();
 		if ($(":checkbox[name=history]").is(":checked")) {
 			//歷史查詢設為1
 			sendMailParam.historyExport='1';
 		} else {
 			sendMailParam.historyExport='0';
 		} 
 		$.ajax({
 			url : contextPath+"/assetManage.do",
 			data : sendMailParam,
 			type : 'post', 
 			cache : false, 
 			dataType : 'json',
 			success : function(json) {
 				if (json.success) {
 					$("#msgAction").text(json.msg);
 				} else {
 					$("#msgAction").text("mail發送失敗");
 				}
 				$.unblockUI();
 				if($(":checkbox[name=history]").is(":checked")) {
 					sendMailParam.actionId = "history";
 				} else {
 					sendMailParam.actionId = "query";
 				}
 			},
 			error : function(){
 				$("#msgAction").text("mail發送失敗");
 				$.unblockUI();
 			}	
 		}); 
 	} catch(e) {
 		$.unblockUI();
 	}
 }

//CR #2563 向通知人員多選框賦值
 function queryVendorWarehouseList(value,param){
 	sendMailParam = param;
 	if($("#toMail").length>0){
 		var data = $("#toMail").combobox("getData");
 	    if(data.length<2){
 	    	javascript:dwr.engine.setAsync(false);
 	    	//加載通知人員下拉框，取得具有轉入倉檢視權限的使用者帳號資料 
 			ajaxService.getUserByDepartmentAndRole(null, value, false, false, function(result){
 				if(result != null) {
 					//複選框取消選中事件
 					$("#toMail").combobox({
 				   		onUnselect : function (newValue) {
 				   			unSelectMultiple(newValue, "toMail")
 				        },
 				   	});
 				    //複選框取消選中事件
 				   	$("#toMail").combobox({
 				   		onSelect : function (newValue) {
 				   			selectMultiple(newValue, "toMail")
 				        },
 				    }); 
 					result = initSelect(result, "請選擇(複選)");
 					//將後台得到的值賦值進相應的ID
 					$("#toMail").combobox("loadData", result);
 					//設置請選擇
 					$("#toMail").combobox('setValue', '');	
 				} else {
 					$("#toMail").combobox('loadData', initSelect(null, "請選擇(複選)"));
 					$("#toMail").combobox('setValue', "");
 				}
 			});
 			javascript:dwr.engine.setAsync(true);
 	    }
 	}
 }
 /*通知人員  使用方法-------------------------------------------------------end*/
