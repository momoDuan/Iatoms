/**
 * 案件處理相關js方法
 * 爲了減少頁面加載時間將一些方法寫入js文件中用以壓縮
 * 2017/09/07
 */
var editIndex = undefined;
var editRows = undefined;
var title = undefined;
var type = undefined;
var viewDlg = undefined;       
var viewDlgShow = undefined;
var saveParams;	
var mailgroupList = null;  
var isSign = false;
var signCaseCategory = undefined;
var signCaseStatus = undefined;

/*mail通知  使用方法-------------------------------------------------------start*/
/**
 * 通知設定畫面加載 
 */
 function openMailAction(group, name, actionId, datagridId){
 	if($('#mailShowFlag').val() != 'Y'){
 		$('#dlgMail').css("display","");
		$('#dlgMail').dialog({      
		  	title: '案件通知設定', 
		  	width : 760,
			height : 360,
		 	closed: true,   
		 	resizable:true,
		 	modal:true,
		 	buttons:[{
		 		id:'sure_btn',
				text:'確定',
				width: 90,
			},{
				id:'cancel_btn',
				text:'取消',
				width: 90,
			}],
			create:function () {
		        $(this).closest(".ui-dialog")
		            		.find(".ui-button:first") 
		            		.addClass("c6");
		        $(this).closest(".ui-dialog").find(".ui-button").eq(1).addClass("c6"); 
		    } 
		});
		$('#sure_btn').bind('click', function(){
			saveEmail(name);
	    });
		$('#cancel_btn').bind('click', function(){    
			$("#dlgMail").dialog('close');  
	    });
		$('#remindStart').datetimebox({
	      	validType:'dateTime',
	      	missingMessage:"請輸入提醒時間起",
	      	showSeconds:false
		});
		$('#remindStart').addClass("easyui-datetimebox");
		$('#remindStart').datetimebox('textbox').attr('maxlength', 10);	
	    $('#remindEnd').datetimebox({
			validType:'dateTime',
	      	missingMessage:"請輸入提醒時間迄",
	      	showSeconds:false
	   });
	   $('#remindEnd').addClass("easyui-datetimebox");
	   $('#remindEnd').datetimebox('textbox').attr('maxlength', 10);
	   //var mailgroupList =initSelect(group);  
	   $('#mailNotice').combobox({ 
	    	editable:false,  
		    valueField:'value',
		    textField:'name',
		   	data:group,
		});
		$('#mailNotice').addClass("easyui-combobox");	
		 
		$('#addEmail_linkbutton').linkbutton({    
		    iconCls: 'icon-add' ,
		});
		$('#addEmail_linkbutton').addClass("c6");
		$('#addEmail_linkbutton').bind('click', function(){
			addEmail();
	    });
		$('#remoreEmail_linkbutton').linkbutton({    
		    iconCls: 'icon-edit',
		});
		$('#remoreEmail_linkbutton').addClass("c6");
		$('#remoreEmail_linkbutton').bind('click', function(){
			remoreEmail(group);
	    });
		$('#toMail').textbox({ 
		    required:true,
	      	missingMessage:"請輸入收件人", 
			multiline:true
		});
		$('#toMail').addClass("easyui-textbox");
		$('#mailShowFlag').val('Y');
		//mailgroupList =  initSelect(<%=mailgroupListString%>); 
    	$('#jsonmail').val(JSON.stringify(group));
    	var jsonStr ={};
    	$('#saveEmailParamer').val(JSON.stringify(jsonStr));
    	
    	$("#remindStart").textbox("setValue","");
        $("#remindStart").textbox('disable');
        $("#remindEnd").textbox("setValue","");
        $("#remindEnd").textbox('disable');
 	}
 	//派工
 	//Task #2542
    if(actionId == ('dispatching') || actionId == ('autoDispatching')){
		$("#nextActivitiToMail").val("被指派的廠商;被指派的廠商部門;被指派的工程師;被指派的角色");
	//簽收
		//Task #2512 完修：建案之客服；建案AO人員 補上 線上排除、立即結案 也要，文件一併調整
		//Task #2514 建案AO人員 不要了
	} else if(actionId == ('complete')|| actionId == ('onlineExclusion') || actionId == ('immediatelyClosing')) {
		$("#nextActivitiToMail").val("建案之客服");
	}else if(actionId == ('retreat')) {
		$("#nextActivitiToMail").val("被指派的廠商部門;被指派的工程師");
	} else if(actionId == ('rushRepair')) {
		$("#nextActivitiToMail").val("被指派的客服;被指派的工程師;被指派的廠商部門");
	}
    //Task #2989 將通知類型改為複選 由於預設有值，不需要顯示驗證紅框 2017/12/25
    $("#checkNoticeType").removeClass("div-tips").addClass("div-list").tooltip("destroy");
 	var radio = document.getElementsByName("noticeType");
	radio[0].checked = true; 
	radio[1].checked = false; 
	$("#remindStart").textbox("setValue","");
	$("#remindEnd").textbox("setValue","");
	var nextActivitiToMail = $("#nextActivitiToMail").val();
	var tempToMail = $("#tempToMail").val();
	if(tempToMail == "") {
		$("#toMail").textbox("setValue", nextActivitiToMail);
	} else {
		$("#toMail").textbox("setValue", tempToMail);
	}
	//mailgroupList =  initSelect(group); 
	$("#mailNotice").combobox('setValue','');
	//var groupList =initSelect(group); 
	$("#mailNotice").combobox('loadData',group);
	$('#jsonmail').val(JSON.stringify(group));
	$("#dlgMail").dialog("open");
	$("#remindStart").textbox("setValue","");
		$("#remindStart").textbox('disable');
		$("#remindEnd").textbox("setValue","");
		$("#remindEnd").textbox('disable');
		$('#remindStart').textbox({
			required: false
	});
	$('#remindEnd').textbox({
		required: false
	});
	$("#dialogMsg").text("");
 }

 /**
  *案件部分的通知  （獲取一些收件人等信息。）actionId: 請求id  datagridId ：datagridId
  */
 function notice(actionId, datagridId) {
     var mailgroupListString = null;
     var name = null;
     var group = null;
     javascript:dwr.engine.setAsync(false);
			// 處理頁面使用當前的案件編號查詢出來的案件狀態
			ajaxService.getMailGroupList( function(data){
				if(data){
					group = initSelect(data);
				}
			});
			ajaxService.getNameList( function(data){
				if(data){
					name = data;
				}
			});
		openMailAction(group, name, actionId, datagridId);
		javascript:dwr.engine.setAsync(true);	
 }
 /*
 *通知類型的改變對通知時間的改變
 */
 function controlNoticeTime(){
 	$("#dialogMsg").text("");
 	var radio = document.getElementsByName("noticeType");
 	var emailRadio = radio[0]; 
 	var remindRadio = radio[1]; 
 	//Task #2989 將通知類型改為複選 2017/12/25
	if(remindRadio.checked == true){
		$("#remindStart").textbox('enable');
		$("#remindEnd").textbox('enable');
		$('#remindStart').textbox({
			required: true
		});
		$('#remindEnd').textbox({
			required: true
		});
 	} else {
 		$("#remindStart").textbox("setValue","");
 		$("#remindStart").textbox('disable');
 		$("#remindEnd").textbox("setValue","");
 		$("#remindEnd").textbox('disable');
 		$('#remindStart').textbox({
			required: true
		});
		$('#remindEnd').textbox({
			required: true
		});
 	}
 }
 /*
 *添加email
 */
 function addEmail(){
 	$("#dialogMsg").text("");
 	var mailNoticeName = $("#mailNotice").combobox('getText');
 	var mailNoticeValue = $("#mailNotice").combobox('getValue');
 	toMail = $("#toMail").textbox('getValue');
 	if(mailNoticeValue != ""){
 		if(toMail != ""){
 			if(toMail[toMail.length-1] == ";") {
         		$("#toMail").textbox("setValue", toMail+mailNoticeName);
 			} else {
 				$("#toMail").textbox("setValue", toMail+";"+mailNoticeName);
 			}
     	} else {
     		$("#toMail").textbox("setValue", mailNoticeName);
     	}
     	var array = [];
     	mailgroupList = JSON.parse($("#jsonmail").val());
     	for (var j=0; j<mailgroupList.length; j++) {
				if (mailgroupList[j].value != mailNoticeValue) {
					obj = new Object();
					obj.name = mailgroupList[j].name;
					obj.value = mailgroupList[j].value;
					array.push(obj);
				}
     	}
     	$("#mailNotice").combobox('loadData',array);
     	$("#mailNotice").combobox('setValue','');
     	$("#jsonmail").val(JSON.stringify(array));
 	}
 }
 /*
 *取消email添加
 */
 function remoreEmail(mailgroupListString){
 	$("#dialogMsg").text("");
 	$("#toMail").textbox("setValue", "");
 	//var mailList =  initSelect(mailgroupListString); 
	$('#jsonmail').val(JSON.stringify(mailgroupListString));
	$("#mailNotice").combobox('loadData',mailgroupListString);
 }
 /*
 *保存email設定的參數
 */
 function saveEmail(names){
 	$("#dialogMsg").text("");
 	var saveForm = $("#saveForm");
 	var controls = null;
 	//Task #2989 將通知類型改為複選 2017/12/25
 	var radio = document.getElementsByName("noticeType");
 	var remindRadio = radio[1]; 
	if(remindRadio.checked == true){
		controls = ['checkNoticeType','remindStart','remindEnd','toMail'];
	} else {
		controls = ['checkNoticeType','toMail'];
	}
	if (validateForm(controls) && saveForm.form("validate")){
 	//if(saveForm.form("validate")){
 		var remindStart = $("#remindStart").textbox('getValue');
 		var remindEnd = $("#remindEnd").textbox('getValue');
 		if(remindStart != "" && remindEnd != ""){
     		var result = compareTime(remindStart,remindEnd)
     		if(!result){
     			$("#dialogMsg").text("提醒時間起不可大於提醒時間迄");
     			return false;
     		}
 		}
 		var toMail = $("#toMail").textbox('getValue');
 		if(toMail.substr(toMail.length-1,toMail.length) == ";") {
 			toMail = toMail.substr(0,toMail.length-1);
 		}
 		var toMailName = toMail.split(";");
 		var nameList = JSON.stringify(names);
 		var nameListName = nameList.substr(1,nameList.length-1).split(",");
 		for(var k =0; k<toMailName.length;k++) {
 			var isNameList = false;
 			for (var z = 0; z < nameListName.length; z++) {
 				if(toMailName[k] == $.trim(nameListName[z].replace("[",""))){
 					isNameList = true;
 					break;
 				}
 			}
 			if(!isNameList){
 				if($.trim(toMailName[k]) != "") {
 					var nextToMail = $("#nextActivitiToMail").val();
 					var nextToMails = nextToMail.split(";");
 					var isNextToMail = false;
 					for(var i=0;i<nextToMails.length;i++) {
 						if($.trim(toMailName[k]) == nextToMails[i]) {
 							isNextToMail = true;
 							break;
 						}
 					}
 					if(!isNextToMail) {
	       					if (!(/^[a-zA-Z0-9_+.-]+\@([a-zA-Z0-9-]+\.)+[a-zA-Z0-9]{2,4}$/i.test($.trim(toMailName[k])))) {
								$("#dialogMsg").text("收件者格式有誤，請重新輸入");
		        				return false;
							}
 					}
 				} else {
 					$("#dialogMsg").text("收件者格式有誤，請重新輸入");
		        		return false;
 				}
 			}
 		}
 		if(toMailName.length > 1){
 			//用於驗證收件者是否重複
 			var toMailNames = toMail+';';
	       		for(var i=0;i<toMailName.length;i++) { 
	       			if(toMailName[i] != "") {
						if(toMailNames.replace(toMailName[i]+";", "").indexOf(toMailName[i]+";")>-1) { 
							$("#dialogMsg").text("收件者重複，請重新輸入");
							return false;
						} 
	       			}
				} 
 		}
 		var saveParams = getSaveParams();
 		$('#dlgMail').dialog('close');
 		$("#saveEmailParamer").val(JSON.stringify(saveParams));
 		$("#tempToMail").val("");
 		$("#tempToMail").val(toMail);
 	}
 }
//獲取存儲數據
	function getSaveParams (){
		var saveForm = $("#saveForm");
		var saveParams = saveForm.form("getData");
		return saveParams;
	}

	/** 
	* 目的：比較通知時間起和通知時間迄
	* remindStart：通知時間起
	* remindEnd：通知時間迄
	*/
    function compareTime(remindStart,remindEnd){
    	var startDate = remindStart.split(" ");
		var closeDate = remindEnd.split(" ");
		var startTime = startDate[1].split(":");
		var closeTime = closeDate[1].split(":");
		var startYear = startDate[0].split("/");
		var closeYear = closeDate[0].split("/");
		//判斷年
		if(startYear[0]>closeYear[0]){
			return false;
		}else if (startYear[0] == closeYear[0]){
			//判斷月份
			if(startYear[1]>closeYear[1]){
				return false;
			} else if(startYear[1] == closeYear[1]) {
				//判斷日期
				if(startYear[2]>closeYear[2]) {
					return false;
				} else if(startYear[2] == closeYear[2]) {
					//判斷小時
					if (startTime[0]>closeTime[0]){
						return false;
					} else if(startTime[0] == closeTime[0]){
						//判斷分鐘
						if(startTime[1]>closeTime[1]){
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
    }
/*mail通知  使用方法-------------------------------------------------------end*/
/*格式化  使用方法-------------------------------------------------------start*/    
	/*
	* 顯示案件狀態 formatter函數
	*/
	function showStatusFormatter(value,row,index){
		if(value == 'response'){
			return '已回應';
		} else if(value == 'arrive'){
			return '已到場';
		} else if(value == 'complete'){
			return '已完修';
		} else {
			return '';
		}
	}
	//檢驗textBox框長度以及取消空格
	function checkTextBox(index, datagridId, actionId) {
		if (actionId == 'delay') {
			addTextLength(index, datagridId, 'delayTime', 10);
		}
		if (actionId == 'changeCaseType') {
			addTextLength(index, datagridId, 'expectedCompleteDate', 10);
		}
		addTextLength(index, datagridId, 'description', 200);
		addTextLength(index, datagridId, 'dealDate', 16);
	}
	
	function addTextLength(index, datagridId, field, length){
		var description = $('#' + datagridId).datagrid('getEditor', { 'index': index, field: field });
		if(description && description != null){
			$(description.target).textbox('textbox').attr('maxlength',length).bind('blur', function(e){
				$(description.target).textbox('setValue', $.trim($(this).val()));
			});
		}
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
	*處理sla設定的時效 以及時效逾時的消息處理
	* formatter函數拓展 hourType表示時效類型
	*/
	function dealSlaSetHourFormatter(value,row,index,hourType){
 		var title;
		var flag;
		// 回應逾時消息
 		if(hourType == 'response'){
			if(row.responseCondition != ''){
				if(row.responseCondition == 'OVER_WARNNING'){
					flag = 'Y';
				} else if(row.responseCondition == 'OVER_HOUR'){
					flag = 'N';
				}
				title = "應回應時間："+ transToTimeStamp(row.acceptableResponseDate) + "及已回應時間：" + transToTimeStamp(row.responseDate);
			}
		// 到場逾時消息
		} else if(hourType == 'arrive'){
			if(row.arriveCondition != ''){
				if(row.arriveCondition == 'OVER_WARNNING'){
					flag = 'Y';
				} else if(row.arriveCondition == 'OVER_HOUR'){
					flag = 'N';
				}
				title = "應到場時間："+ transToTimeStamp(row.acceptableArriveDate) + "及已到場時間：" + transToTimeStamp(row.arriveDate);
			}
		// 完修逾時消息
		} else if(hourType == 'complete'){
			if(row.completeCondition != ''){
				if(row.completeCondition == 'OVER_WARNNING'){
					flag = 'Y';
				} else if(row.completeCondition == 'OVER_HOUR'){
					flag = 'N';
				}
				title = "應完修時間："+ transToTimeStamp(row.acceptableFinishDate) + "及已完修時間：" + transToTimeStamp(row.completeDate);
			}
		}
		// 處理消息的圖標顯示
		if(flag && flag == 'Y'){
			return "<a href='javascript:void(0)' class=\"easyui-tooltip\" title=" + title + " name='iconWarnning'></a>";
		} else if(flag && flag == 'N'){
			return "<a href='javascript:void(0)' class=\"easyui-tooltip\" title=" + title + " name='iconInfo'></a>";
		}
	}
/*格式化  使用方法-------------------------------------------------------end*/  
/*datagrid公用操作  使用方法-------------------------------------------------------start*/  
	/*
	*全选按钮选中
	*/ 
	function checkAllFun(){
		var isChecked = true;
		if($("#allCheckButton").prop("checked")){
		} else {
			isChecked = false;
		}
		var rows = $("#dg").datagrid("getRows");
		if(rows){
			var rowsSelect = $("#dg").datagrid("getSelections");
			var rowsLength = $('#dgResponse').datagrid("getRows").length;
			if (rowsLength != 0 && rowsSelect && rowsSelect.length != 0 && isChecked) {
				for (var i = 0; i < rows.length; i++) {
					if (rowsSelect[0].caseCategory != rows[i].caseCategory || rows[i].caseStatus != rowsSelect[0].caseStatus) {
						$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件','warning');
						//$("#dg").datagrid("collapseRow",index);
						$("#allCheckButton").prop("checked", false);
						return false;
					}
				}
			}
			for(var i = 0; i < rows.length; i ++){
				var index = $("#dg").datagrid("getRowIndex", rows[i]);
				var obj = $("#dg").closest(".datagrid-wrap").find(".datagrid-view2").find("[datagrid-row-index='"+ index +"']").find("input[name='checkBtn']");
				if(obj.length > 0){
					$(obj).prop("checked",isChecked);
					if(isChecked){
						$("#dg").datagrid("selectRow", index);
					} else {
						$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', rows[i]));
						$('#dg').datagrid('fixRowHeight', $('#dg').datagrid('getRowIndex', rows[i]));
						$("#dg").datagrid("unselectRow", index);
					}
				}
			}
		}
	}
	/*
	* 處理全選按鈕的選中
	*/
	function allCheckButton(){
		var selectRows = $("#dg").datagrid("getSelections");
		var checkBtnRow = $("#dg").closest(".datagrid-wrap").find(".datagrid-view2").find("input[name='checkBtn']");
		if(selectRows.length == checkBtnRow.length){
			$("#allCheckButton").prop("checked", true);
		} else {
			$("#allCheckButton").prop("checked", false);
		}
	}
	/*
	* 選中一行
	*/
	function dataGridOnSelect(index,row){
		var obj = $("#dg").closest(".datagrid-wrap").find(".datagrid-view2").find("[datagrid-row-index='"+ index +"']").find("input[name='checkBtn']");
		if(obj.length > 0){
			obj.prop("checked", true);
			var rows = $("#dg").datagrid('getSelections');
			var dgRow = $('#dgResponse').datagrid('getData').rows[0];
			if(dgRow!=null){
				if (dgRow.caseActionId == 'leasePreload') {
					checkCmsCase(rows,dgRow);
				}
			}
		} else {
			$("#dg").datagrid("unselectRow", index);
		}
		// 處理全選按鈕的選中
		allCheckButton();
	}
	/*
	* 取消選中一行
	*/
	function dataGridUnSelect(index,row){
		var obj = $("#dg").closest(".datagrid-wrap").find(".datagrid-view2").find("[datagrid-row-index='"+ index +"']").find("input[name='checkBtn']");
		if(obj.length > 0){
			obj.prop("checked", false);
			$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', row));
			$('#dg').datagrid('fixRowHeight', $('#dg').datagrid('getRowIndex', row));
		}
		var rows = $("#dg").datagrid('getSelections');
		if (rows.length > 0) {
			var dgRow = $('#dgResponse').datagrid('getData').rows[0];
			if(dgRow!=null){
				if (dgRow.caseActionId == 'leasePreload') {
					checkCmsCase(rows,dgRow);
				}
			}
		}
		// 處理全選按鈕的選中
		allCheckButton();
	}
	/**
	 * 核減裝機件且爲CMS案件時，是否有未確認授權的
	 */
	function checkConfirmAuthorizes(rows) {
		if (rows.length > 0) {
			for (var i = 0 ; i < rows.length; i++) {
				if (rows[i].caseCategory == "INSTALL" && rows[i].installType == "4" && rows[i].confirmAuthorizes == 'N') {
					return rows[i];
				}
			}
			return null;
		}
	}
	/**
	 * 核檢是否包含cms案件,如果是 設為必填
	 */
	function checkCmsCase(rows,dgRow) {
		var cmsCaseFlag = false;
		if (rows.length > 0) {
			for (var i = 0 ; i < rows.length; i++) {
				if (rows[i].cmsCase == "Y") {
					cmsCaseFlag = true;
					var serialNumberEditor = $("#dgResponse").datagrid('getEditor', {  
					    index : 0,  
					    field : 'serialNumber'
					});
					if($(serialNumberEditor.target).length>0){
						$(serialNumberEditor.target).textbox({required:true,missingMessage:'請輸入設備序號(EDC)',});
						$(serialNumberEditor.target).textbox('textbox').attr('maxlength',20);
						$(serialNumberEditor.target).next().children().focus();
					}
					var simSerialNumberEditor = $("#dgResponse").datagrid('getEditor', {  
					    index : 0,  
					    field : 'simSerialNumber'
					});
					if($(simSerialNumberEditor.target).length>0){
						$(simSerialNumberEditor.target).textbox({required:true,missingMessage:'請輸入設備序號(SIM卡)',});
						$(simSerialNumberEditor.target).textbox('textbox').attr('maxlength',20);
						//$(simSerialNumberEditor.target).next().children().focus();
					}
					break;
				}
			}
			if (!cmsCaseFlag) {
				var serialNumberEditor = $("#dgResponse").datagrid('getEditor', {  
				    index : 0,  
				    field : 'serialNumber'
				});
				if($(serialNumberEditor.target).length>0){
					$(serialNumberEditor.target).textbox({required:false});
					$(serialNumberEditor.target).textbox('textbox').attr('maxlength',20);
					$(serialNumberEditor.target).next().children().focus();
				}
				var simSerialNumberEditor = $("#dgResponse").datagrid('getEditor', {  
				    index : 0,  
				    field : 'simSerialNumber'
				});
				if($(simSerialNumberEditor.target).length>0){
					$(simSerialNumberEditor.target).textbox({required:false});
					$(simSerialNumberEditor.target).textbox('textbox').attr('maxlength',20);
				}
			}
		}
		return cmsCaseFlag;
	}
	/*
	* 重新設置顯示記錄
	*/
	function addCaseLogExpand(contextPath){
		// 得到options屬性
		var options = $("#dg").datagrid('options');
		options.onExpandRow = function (index, row) {
//			if (isSign) {
//				var rows = $("#dg").datagrid("getSelections");
//				if (rows) {
//					if (rows[0].caseCategory != row.caseCategory || row.caseStatus != rows[0].caseStatus) {
//						$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件','warning');
//						$("#dg").datagrid("collapseRow",index);
//						return false;
//					}
//				}
//			}
			var queryData = {caseId : row.caseId, isHistory : row.isHistory};
			var addCaseLogOptions = {
					url : contextPath+"/caseHandle.do?actionId=queryTransaction",
					queryParams : queryData,
					title: '',
					nowrap:false,
					singleSelect: true,
					rownumbers: true,
					loadMsg: '',
					columns: [[
						{ field: 'actionName', title: '動作', halign:'center',align:'left', width: 150,formatter:function(value,row,index){return actionFormatter(value,row,index);} },
						{ field: 'afterActionStatus', title: '動作後狀態', halign:'center',align:'left', width: 120 },
						//{ field: 'dealDescription', title: '處理說明', halign:'center',align:'left', width: 400, formatter:wrapFormatter},
						{ field: 'dealDescription', title: '處理說明', halign:'center',align:'left', width: 400, formatter:function(value,row,index){return actionHyperlink(value,row,index);}},
						{ field: 'dealDate', title: '實際執行時間', halign:'center',align:'center', width: 170,formatter:formatToTimeStampIgnoreSecond,},
						
						{ field: 'createdByName', title: '記錄人員', halign:'center',align:'left',width: 120},
						{ field: 'mailInfo', title: 'mailInfo', halign:'center',align:'left',hidden:true,width: 160},
						{ field: 'createdDate', title: '記錄日期', halign:'center',align:'center',width: 190,formatter:formatToTimeStamp}
					]],
					onLoadSuccess: function (data) {
						setTimeout(function () {
							$.each($('#dg').datagrid('getRows'), function (i, row) {
								$('#dg').datagrid('fixRowHeight', i);
							});
						}, 0);
						$('#dg').datagrid('fixDetailRowHeight',index); 
					}
			}
			// 列自適應
		//	$('#dg').datagrid('fixDetailRowHeight', index);
			// 顯示記錄的grid節點
			var addCaseLogGrid = $("#dg").datagrid('getRowDetail', index).find('table.ddv');
			// 之前設定耗材的grid節點
			var suppliesLinkDatagrid = $("#dg").datagrid('getRowDetail', index).find('table.dataGridSuppliesLink');
			// 耗材上一個兄弟節點
			var suppliesPrev = suppliesLinkDatagrid.closest('.panel').prev();
			// 如果這個節點有panel的class 設置隱藏
			if(suppliesPrev.hasClass('panel')){
				suppliesLinkDatagrid.closest('.panel').attr("style","display:none;");
			}
			// 加載options
			addCaseLogGrid.datagrid(addCaseLogOptions);
			// 
			var rows = $('#dg').datagrid('getRows');
			if(!rows || rows == null || rows.length == 0){
			} else {
				// 自適應高度
				for (var i = 0; i < rows.length; i++) {
					$('#dg').datagrid('fixDetailRowHeight', $('#dg').datagrid('getRowIndex', rows[i]));
				}
			}
		}
	}
	function actionHyperlink(value, row, index){
		//判斷是否有物流編號，若有，則添加網絡郵局的超鏈接
		if(!isEmpty(value)){
			var str = value.replaceAll('\n','<br>');
			if(str != null && str.indexOf("物流編號：") != -1 ){
				var arr = str.split("物流編號："); 
			    return arr[0] + '物流編號：' + '<a href=\"javascript:void(0)\" class=\"easyui-tooltip\" onclick=\"openHttp(\''+row.logisticsVendorEmail+'\');\">' +arr[1]+ '</a>';
			}else{
				return str;
			}
		}else{
			return value;
		}
	}
	function openHttp(url){
		window.open(url);
	}
	/*
	* 折疊展開行
	*/
	function collapseSubView(contextPath){
		// 得到所有選中行
		var rows = $('#dg').datagrid('getRows');
		if(!rows || rows == null || rows.length == 0){
		} else {
			// 打開案件記錄
			for (var i = 0; i < rows.length; i++) {
				$('#dg').datagrid("collapseRow", $('#dg').datagrid('getRowIndex', rows[i]));
				$('#dg').datagrid('fixRowHeight', $('#dg').datagrid('getRowIndex', rows[i]));
			}
			// 調顯示記錄方法
			addCaseLogExpand(contextPath);
		}
	}
	/*
	* 處理創建DataGrid
	*/
	function updateGrid(index, datagridId) {
		$('#' + datagridId).datagrid('updateRow', {
			index: index,
			row: {}
		});
	}
	/*
	* 進入編輯前觸發事件
	*/
	function dgBeforeEdit (index, row) {
		row.editing = true;
		updateGrid(index, $(this)[0].id);
	}
	
	/*
	* 進入編輯后觸發事件
	*/
	function dgAfterEdit (index, row) {
		row.editing = false;
		updateGrid(index, $(this)[0].id);
	}
	
	/*
	* 取消編輯后觸發事件
	*/
	function dgCancelEdit (index, row) {
		row.editing = false;
		updateGrid(index, $(this)[0].id);
	}
	/**
	* 用戶選中一行時，判斷選中行與當前已選中的資料狀態、類型是否一致。
	*/
	function checkSign(index, row){
		var rowsLength = $('#dgResponse').datagrid("getRows").length;
		if (rowsLength != 0) {
			var rows = $("#dg").datagrid("getSelections");
			if(rows && rows.length != 0){
				var pushRow = true;
				var tempRow;
				for(var i = 0; i<rows.length; i++){
					tempRow = rows[i];
					if(tempRow.caseId == row.caseId){
						pushRow = false;
					}
				}
				if(pushRow){
					rows.push(row);
				}
			}
			if (rows.length > 1) {
				var oneIndex = $("#dg").datagrid("getRowIndex", rows[0]);
				var i = 0;
				if (oneIndex == index) {
					i = 1;
				}
				if (rows[i].caseCategory != row.caseCategory || row.caseStatus != rows[i].caseStatus) {
					$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件','warning');
					setTimeout(function () {
						$("#dg").datagrid("uncheckRow", index);
					}, 0);
				} else {
					if (isSign) {
						setTimeout(function () {
							$("#dg").datagrid("expandRow", index);
						}, 0);
					}
				}
			}
		}
	}
	/*
	* 處理顯示案件記錄按鈕
	*/
	function showSubView() {
		var rows = $('#dg').datagrid('getSelections');
		if(!rows || rows == null || rows.length == 0){
			$.messager.alert('提示訊息','請勾選資料','warning');
			return false; 
		} else {
			// 打開案件記錄
			for (var i = 0; i < rows.length; i++) {
				$('#dg').datagrid("expandRow", $('#dg').datagrid('getRowIndex', rows[i]));
			}
		}
	}
	
	//點擊取消查詢中的勾選
	function onUncheck(index, row, contextPath) {
		setTimeout(function () {
			var rows = $("#dg").datagrid("getSelections");
			if (rows.length == 0) {
				var rowsLength = $('#dgResponse').datagrid("getRows").length;
				if (rowsLength != 0) {
					deleteAction('dgResponse', contextPath, 'response');
				}
			}
		}, 0);
	}
	/**
	*刪除
	*/
	function deleteAction(datagridId, contextPath, panelId) {
		$("#deleteCaseSuppliesLinkIds").val("");
    	$("#deleteCaseAssetLinkIds").val("");
    	$("#selectSn").val("");
    	$("#hideLeaseSignFlag").val('');
		// 遮罩样式
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		//加遮罩
		$.blockUI(blockStyle);
		isSign = false;
		$('#' + datagridId).datagrid('deleteRow', 0); 
	//	$('#' + datagridId).closest('.easyui-panel').panel('close');
		$('#' + panelId).panel('close');
		editIndex = undefined;
		collapseSubView(contextPath);
		// 去除遮罩
		$.unblockUI();
	}
	/*
	* 案件處理的處理頁面
	* caseId：案件編號 isHistory：是否查歷史表
	* caseStatus：案件狀態 index：當前行下標
	*/
	function popEditCaseWindows(event,caseId, isHistory, caseStatus, index, contextPath){
		$("#dgResponse-msg").text("");
		// 清空標志位 用來處理選中行清空
		var clearFlag = true;
		// 拿到所有選中行
		var rows = $("#dg").datagrid('getSelections');
		if(rows){
			var indexArray = [];
			var tempIndex;
			// 得到所有選中行行號
			for(var i = 0; i < rows.length; i++){
				tempIndex = $('#dg').datagrid('getRowIndex', rows[i]);
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
		viewDlg = $('#editDlg').dialog({
			title : '案件記錄',
			width : 900,
			height : 500,
			modal : true,
			method:'post', 
			closed : false,
			cache : false,
			queryParams : {
				actionId : "initEdit",
				caseId : caseId,
				isHistory : isHistory
			},
			onBeforeLoad : function(){
				// clear當前對話框
				if(viewDlg){
					viewDlg.panel('clear');
				}
			},
			onLoad :function(){
				// 文本框限制
		    	textBoxSetting("editDlg");
				// 日期框設定
		    	dateboxSetting("editDlg");
		    	// 調用多選下拉框選中事件
		    	mutilComboSelectEvent("editDlg");
				$("#hidCaseStatus").val(caseStatus);
		    	// clear查詢dtid對話框
				if(viewQueryDtid){
					viewQueryDtid.panel('clear');
				}
				// clear查詢Mid對話框
				if(viewChooseMid){
					viewChooseMid.panel('clear');
				}
				$('#merchanEditDlg').panel('clear');
				//清空設備序號隱藏域 2018/01/31
				$("#selectSn").val("");
				var row = $('#dgResponse').datagrid('getData').rows[0];
				var selectRow =	$('#dg').datagrid('getSelections');
				//當處理頁面點開 清空展開項 2018/01/31
				//加判空 2018/02/06
				if(row!=null && selectRow.length>0){
					if((row.caseActionId == 'sign' || row.caseActionId == 'onlineExclusion') && selectRow[0].caseCategory!='CHECK'){
						deleteAction('dgResponse', contextPath, 'response');
					}
				}
				
			},
			onClose : function(){
				//清空設備序號隱藏域 2018/01/31
				$("#selectSn").val("");
				// 不選中當前行
				if(clearFlag){
					$("#dg").datagrid("unselectRow", index);
				// 選中當前行
				} else {
					$("#dg").datagrid("selectRow", index);
				}
				// clear當前案件打開對話框
				viewDlg.panel('clear');
				// clear查詢dtid對話框
				if(viewQueryDtid){
					viewQueryDtid.panel('clear');
				}
				// clear查詢Mid對話框
				if(viewChooseMid){
					viewChooseMid.panel('clear');
				}
				$('#merchanEditDlg').panel('clear');
				$(".qq-uploader-selector").unblock();
			},
			href : contextPath+"/caseHandle.do",
			modal : true
		});
		event.stopPropagation(); 
		return false;
	}

	/**
	* 在建案畫面顯示重複進件的信息
	*/
	function showEditInfoPage(caseId, contextPath){
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
			href : contextPath+"/caseHandle.do",
			modal : true
		});
	}
	
/*datagrid公用操作  使用方法-------------------------------------------------------end*/
/*匯入  使用方法-------------------------------------------------------start*/
	//關閉案件匯入頁簽
	function btnCancelUpload(){
	//	$("#caseCategory").combobox("setValue", "");
		if($('#caseCategory').hasClass("easyui-combobox")){
			$("#caseCategory").combobox("setValue", "");
		}
		$(".qq-upload-list-selector").html("");
		if($('#uploadCaeInfo').hasClass("easyui-panel")){
			$('#uploadCaeInfo').panel('close');
		} else {
			$("#uploadCaeInfo").addClass("easyui-panel");
			$("#uploadCaeInfo").attr("style", "width: 98%; height: auto;");
			$("#uploadCaeInfo").panel();
		}
	//	$('#uploadCaeInfo').panel('close');
		$("#assetListMsg").text("");
	}
	/**
	* 點擊案件匯入按鈕
	*/
	function showUploadView(){
		$('#dgResponse-msg').html('');
		if($("#response").hasClass("easyui-panel")){
			$('#dgResponse').closest('.easyui-panel').panel('close');
		} else {
			$("#response").addClass("easyui-panel");
			$("#response").attr("style", "width: 98%; height: auto;");
			$("#response").panel();
			$("#dgResponse").addClass("easyui-datagrid");
			$("#dgResponse").datagrid();
			$("#response").children(':first').attr("style", "display:none;");
		}
	//	$('#response').panel('close');
		// 生成案件匯入樣式
		createUploadStyle();
		//Task #3269
		if($('#uploadCaseEnd').hasClass("easyui-panel")){
			$('#uploadCaseEnd').panel('close');
		}
	//	$('#uploadCaeInfo').panel('open');
		if($('#uploadCaeInfo').hasClass("easyui-panel")){
			$('#uploadCaeInfo').panel('open');
		} else {
			$("#uploadCaeInfo").addClass("easyui-panel");
			$("#uploadCaeInfo").attr("style", "width: 98%; height: auto;");
			$("#uploadCaeInfo").panel();
			$('#uploadCaeInfo').panel('open');
		}
	}
	/*
	* 匯入模板下載
	*/
	function templateDownLoad(contextPath) {
		$("#dgResponse-msg").text("");
		var controls = ['caseCategory'];
		if (validateForm(controls) && $("#caseCategoryUpload").form("validate")  ) {
			var caseCategory = $("#caseCategory").combobox("getValue");
			createSubmitForm(contextPath + "/caseHandle.do", "actionId=exportTemplate&caseCategory="+caseCategory, "post");
		}
	}
	/**
	* 案件匯入
	*/
	function uploadCaseCategory(id,name) {
		$("#dgResponse-msg").text("");
		var controls = ['caseCategory'];
		if (validateForm(controls) && $("#caseCategoryUpload").form("validate")) {
			var blockStyle = {message:'loading...',css: {width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			if (!(typeof(file_upload_import)=="undefined") && file_upload_import._storedIds!=""){
				$.blockUI(blockStyle);
				if (!(typeof(file_upload_import_Additional)=="undefined") && file_upload_import_Additional._storedIds!=""){
					file_upload_import_Additional.uploadStoredFiles();
				} else {
					uploadCaseInfo();
				}
			} else {
				$("#dgResponse-msg").text("請輸入要匯入的文件");
				return false;
			}
		}
	}
	/**
	* 將所選則的臨時文件保存至臨時路徑
	*/
	function onAllComplete(succeeded, failed){
		if(failed) {
			uploadCaseInfo();
		}else{
			$.unblockUI();
			$.messager.alert('提示', '保存失敗,請聯繫管理員.', 'error'); 
		}
	}
	/**
	* 附加檔案保存完畢后，匯入案件信息
	*/
	function uploadCaseInfo() {
		var caseCategory = $("#caseCategory").combobox("getValue");
		file_upload_import.setParams({
			caseCategory: caseCategory,
			fileName: $("#tempFileName").val()
		});
		file_upload_import.uploadStoredFiles();
	}
/*匯入  使用方法-------------------------------------------------------end*/
/*欄位 匯出 使用方法-------------------------------------------------------start*/
/*
*動態顯示datagrid列
*/
function filterBlank() {
	//欄位加載列調用初始化 
	showDataListOInfo();
	// 其他需要顯示但不存在與datalist的列
	var otherColumn = ['checked', 'src'];
	// 處理欄位按鈕點擊打開選擇顯示欄位對話框
	dynamicshowColumn('exportBlank', 'exportList', 'dg', otherColumn);
}
/**
 *加載匯出內容
 */
function showDataListOInfo(){
	if($('#showExportListFlag').val()!='Y'){
		$('#exportList').datalist({ 
			    checkbox: true,checked:false,
				singleSelect:false,
				selectOnCheck: false,toolbar:'#exportToolbarAll',
				checkOnSelect:true,selectOnCheck:true,
				idField:'colName',
				valueField:'colName',
				textField:'colText',
				onSelect:function(index,row) {
					dataListOnSelect(row, index, 'exportList', 'exportCheckAll');
				},
				onUnselect:function(index,row){
					dataListUnSelect(row, index, 'exportList', 'exportCheckAll');
				},
				
				onLoadSuccess : function(){
					datalistSuccessLoad('exportBlank', 'exportList', 'exportCheckAll');
				},
				data: [
					{colName:'customerName', colText:'客戶', value:'defaultShow'},
					{colName:'caseStatusName', colText:'案件狀態', value:'defaultShow'},
					{colName:'caseTypeName', colText:'案件類型', value:'defaultShow'},
					{colName:'responseCondition', colText:'回應', value:'defaultShow'},
					{colName:'arriveCondition', colText:'到場', value:'defaultShow'},
					{colName:'completeCondition', colText:'完修', value:'defaultShow'},
					{colName:'responseStatus', colText:'回應狀態'},
					{colName:'arriveStatus', colText:'到場狀態'},
					{colName:'completeStatus', colText:'完修狀態'},
					{colName:'installTypeName', colText:'裝機類型'},
					{colName:'uninstallTypeName', colText:'拆機類型'},
					{colName:'caseCategoryName', colText:'案件類別', value:'defaultShow'},
					{colName:'requirementNo', colText:'需求單號'},
					{colName:'caseId', colText:'案件編號', value:'defaultShow'},
					{colName:'contractCode', colText:'案件合約編號'},
					{colName:'edcTypeContract', colText:'設備合約編號'},
					{colName:'isProject', colText:'專案'},
					{colName:'projectCode', colText:'專案代號'},
					{colName:'projectName', colText:'專案名稱'},
					{colName:'companyName', colText:'派工廠商', value:'defaultShow'},
					{colName:'vendorName', colText:'廠商名稱'},
					
					{colName:'departmentName', colText:'派工部門'},
					{colName:'vendorStaff', colText:'廠商人員'},
					{colName:'sameInstalled', colText:'是否同裝機作業'},
					{colName:'attendanceTimes', colText:'到場次數', value:'defaultShow'},
					{colName:'dtid', colText:'DTID', value:'defaultShow'},
					{colName:'tid', colText:'TID', value:'defaultShow'},
					{colName:'merchantCode', colText:'特店代號', value:'defaultShow'},
					{colName:'merchantName', colText:'特店名稱', value:'defaultShow'},
					{colName:'headerName', colText:'表頭（同對外名稱）', value:'defaultShow'},
					{colName:'oldMerchantCode', colText:'舊特店代號'},
					{colName:'areaName', colText:'特店區域'},
					{colName:'contact', colText:'特店聯絡人'},
					{colName:'contactTel', colText:'特店聯絡人電話1'},
					{colName:'contactTel2', colText:'特店聯絡人電話2'},
					{colName:'phone', colText:'特店聯絡人行動電話'},
					{colName:'contactEmail', colText:'特店聯絡人Email'},
					{colName:'businessHours', colText:'特店營業時間'},
					{colName:'aoName', colText:'AO人員', value:'defaultShow'},
					{colName:'aoemail', colText:'AO EMAIL'},
					{colName:'businessAddress', colText:'特店營業地址'},
					{colName:'installedAdress', colText:'特店裝機地址'},
					{colName:'contactAddress', colText:'特店聯繫地址'},
					{colName:'installedContact', colText:'特店裝機聯絡人'},
					{colName:'contactUser', colText:'特店聯繫聯絡人'},
					{colName:'installedContactPhone', colText:'特店裝機電話'},
					{colName:'contactUserPhone', colText:'特店聯繫聯絡人電話'},
					{colName:'installedContactMobilePhone', colText:'特店裝機聯絡人手機'},
					{colName:'contactMobilePhone', colText:'特店聯繫聯絡人手機'},
					{colName:'installedContactEmail', colText:'特店裝機聯絡人Email'},
					{colName:'contactUserEmail', colText:'特店聯繫聯絡人Email'},
					{colName:'edcTypeName', colText:'刷卡機型', value:'defaultShow'},
					
					{colName:'edcSerialNumber', colText:'設備序號'},
					{colName:'enableDate', colText:'設備啟用日'},
					{colName:'wareHouseName', colText:'倉別'},
					{colName:'openFunctionName', colText:'設備開啟的功能'},
					{colName:'applicationName', colText:'程式名稱'},
					{colName:'multiModuleName', colText:'雙模組模式', value:'defaultShow'},
					{colName:'ecrConnection', colText:'ECR連線'},
					
					{colName:'networkLineNumber', colText:'網路線'},
					
					{colName:'peripheralsName', colText:'週邊設備1'},
					{colName:'peripheralsFunctionName', colText:'週邊設備1功能'},
					{colName:'peripheralsSerialNumber', colText:'週邊設備1序號'},
					{colName:'peripheralsContract', colText:'週邊設備1合約編號'},
					{colName:'peripherals2Name', colText:'週邊設備2'},
					{colName:'peripheralsFunction2Name', colText:'週邊設備2功能'},
					{colName:'peripherals2SerialNumber', colText:'週邊設備2序號'},
					{colName:'peripherals2Contract', colText:'週邊設備2合約編號'},
					{colName:'peripherals3Name', colText:'週邊設備3'},
					{colName:'peripheralsFunction3Name', colText:'週邊設備3功能'},
					{colName:'peripherals3SerialNumber', colText:'週邊設備3序號'},
					{colName:'peripherals3Contract', colText:'週邊設備3合約編號'},
					{colName:'connectionTypeName', colText:'連線方式'},
					{colName:'logoStyle', colText:'LOGO'},
					{colName:'isOpenEncrypt', colText:'是否開啟加密'},
					{colName:'electronicPayPlatform', colText:'電子化繳費平台'},
					
					{colName:'electronicInvoice', colText:'電子發票載具'},
					{colName:'cupQuickPass', colText:'銀聯閃付'},
					{colName:'netVendorName', colText:'寬頻連線'},
					{colName:'localhostIp', colText:'本機IP'},
					{colName:'netmask', colText:'Netmask'},
					{colName:'gateway', colText:'Getway'},
					
					{colName:'description', colText:'其他說明'},
					{colName:'merchantCodeOther', colText:'MID2'},
					{colName:'cupTransType', colText:'CUP交易'},
					{colName:'dccTransType', colText:'DCC交易'},
					{colName:'aeMid', colText:'AE-MID'},
					{colName:'aeTid', colText:'AE-TID'},
					
					{colName:'tmsParamDesc', colText:'TMS參數說明'},
					{colName:'updatedDescription', colText:'異動說明', value:'defaultShow'},
					{colName:'repairReasonName', colText:'報修原因'},
					{colName:'problemReasonCategoryName', colText:'報修問題原因分類'},
					{colName:'problemReasonName', colText:'報修問題原因'},
					{colName:'problemSolutionCategoryName', colText:'報修解決方式分類'},
					{colName:'problemSolutionName', colText:'報修解決方式'},
					{colName:'responsibityName', colText:'是否為責任報修'},
					{colName:'acceptableResponseDate', colText:'應回應時間'},
					{colName:'acceptableArriveDate', colText:'應到場時間'},
					{colName:'acceptableFinishDate', colText:'應完修時間'},
					{colName:'dispatchUserName', colText:'執行派工人員'},
					{colName:'dispatchDate', colText:'派工時間'},
					{colName:'responseUserName', colText:'執行回應人員'},
					{colName:'responseDate', colText:'回應時間'},
					
					{colName:'arriveUserName', colText:'執行到場人員'},
					{colName:'arriveDate', colText:'到場時間'},
					{colName:'completeUserName', colText:'執行完修人員'},
					{colName:'completeDepartmentName', colText:'執行完修部門'},
					
					{colName:'completeDate', colText:'完修時間'},
					{colName:'analyzeUserName', colText:'執行簽收人員'},
					{colName:'analyzeDate', colText:'簽收時間'},
					{colName:'closeUserName', colText:'執行結案人員'},
					{colName:'closeDate', colText:'結案時間'},
					{colName:'processTypeName', colText:'處理方式'},
					{colName:'logisticsVendor', colText:'物流廠商'},
					{colName:'logisticsNumber', colText:'物流編號'},
					{colName:'receiptType', colText:'Receipt_type'},
					
					{colName:'dispatchProcessUsername', colText:'目前處理人', value:'defaultShow'},
					{colName:'updatedByName', colText:'最後異動人', value:'defaultShow'},
					{colName:'updatedDate', colText:'最後異動日期', value:'defaultShow'},
					{colName:'createdByName', colText:'進件人員', value:'defaultShow'},
					{colName:'createdDate', colText:'進件日期', value:'defaultShow'}
					],
				lines:true
			});
			$('#showExportListFlag').val('Y'); 
	}
}
/*
*頁面匯出操作(可變列)
*/
function variableExport(){
	$("#message").empty();
	var rowLength = getGridRowsCount("dg");
	// CR #2546 欄位重置
/*	//欄位加載列調用初始化 
	showDataListOInfo();
	var rows = $("#exportList").datagrid("getRows");
	var selectRows = $('#exportList').datagrid('getSelections');
	var newRows = [];
	for (var i = 0; i < rows.length; i ++) {
		for (var j = 0; j < selectRows.length; j ++) {
			if(rows[i].colName == selectRows[j].colName) {
				newRows.push(rows[i].colName);
				break;
			}
		}
	}*/
	var queryCompanyId = $("#queryCompanyId").combobox('getValue');
	document.getElementById("exportQueryCompanyId").value=queryCompanyId;
	// CR #2546 欄位重置
//	document.getElementById("exportField").value=newRows;
	if(rowLength >= 1){
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		
		actionClicked( document.forms["queryForm"],
			'SRM05020',
			'',
			'variableExport');
		
		ajaxService.getExportFlag('SRM05020',function(data){
			$.unblockUI();
		});
	}
}
/*欄位 匯出  使用方法-------------------------------------------------------end*/
/*修改案件類型  使用方法-------------------------------------------------------start*/
/**
*修改案件類型--若案件類型為“預約”，才可編輯且必填  其他類型該欄位置空且不可修改
*/
 var appointmentChange = function(newValue, oldValue) {
	// 找到需要聯動下拉框的節點
	var editorObject = $(this).closest('td[field="caseType"]').siblings('td[field="expectedCompleteDate"]').find('input:text').first();
	if (newValue=='APPOINTMENT') {
		var deptCode = "";
		var roleCode = "";

		editorObject.datebox("enable");
		editorObject.datebox("enableValidation");
	} else {
		editorObject.datebox("disable");
		editorObject.datebox("disableValidation");
		editorObject.datebox('setValue','');
	}
}; 
/*修改案件類型  使用方法--------------------------------------------------------end*/
/*派工  使用方法-------------------------------------------------------start*/
/**
*調用派工畫面判斷QA方法
*/
function IsQaConfirm(allParams, selectRow){
	var nextIsQA = true;
	var isTms = allParams.isTms;
	var departmentName = allParams.departmentName;
	//若案件為自動派工案件，則派工單位預設帶入下一個需要處理的單位
	if(selectRow.length>0) {
		for(var i =0; i<selectRow.length; i++){
			if(selectRow[i].isTms=='N' || selectRow[i].departmentName!='TMS'){
				nextIsQA = false;
				break;
			}
		}
	} else {
		if(isTms=='N' || departmentName!='TMS'){
			nextIsQA = false;
		}
	}
	return nextIsQA;
}
/**
*派工--派工單位，處理人員聯動
*/
 var selectChange = function(newValue, oldValue) {
	// 找到需要聯動下拉框的節點
	var editorObject = $(this).closest('td[field="dispatchUnit"]').siblings('td[field="dealById"]').find('input:text').first();
	var selectRow = $("#dg").datagrid('getSelections')[0];
	if (newValue) {
		var deptCode = "";
		var roleCode = "";
		if (newValue =='CUSTOMER_SERVICE' || newValue =='QA' || newValue =='TMS') {
			roleCode = newValue;
		} else {
			deptCode = newValue;
		}
		ajaxService.getUserByDepartmentAndRole(deptCode, roleCode, false, false,function(result){
			if(result != null) {
				if (oldValue) {
					editorObject.combobox('setValue', "");
				}
				editorObject.combobox('loadData', initSelect(result));
			} else {
				editorObject.combobox('loadData', initSelect(null));
				editorObject.combobox('setValue', "");
			}
		});
	} else {
			editorObject.combobox('loadData', initSelect(null));
			editorObject.combobox('setValue', "");
	}
}; 

/*派工  使用方法--------------------------------------------------------end*/
/*簽收、線上排除  使用方法-------------------------------------------------------start*/
/*
* 目的：案件簽收和線上排除的時候，由後台傳來的標誌位格式化設定鏈接。
*caseId : 案件編號 
*row ：正在格式化的這一行. 
*index：正在格式化的這一行行號
* assetLinkDatagridID ：正在處理的datagridId  
*caseCategory：正在處理行的案件類別
*/
function caseAssetLinkFormatter( value, row, index, caseId, assetLinkDatagridID, caseCategory){
	//標誌位為N，說明設備未啟用鏈接.
	if(row.isLink == 'N') {
		if(caseCategory != 'UNINSTALL') {
			return "<a href='javascript:void(0)' class='easyui-linkbutton' iconCls:'icon-search'  onclick=\"linkCaseAsset(this,'"+value+"',"+index+",'"+assetLinkDatagridID+"','"+encodeURIComponent(caseId)+"','"+caseCategory+"')\"  name='lbtLink'></a>";
		} else {
			return "";
		}
	//標誌位為Y 說明設備已經關聯過設備
	} else if(row.isLink == 'Y') {
			return "<a href='javascript:void(0)' onclick=\"linkCaseAssetName(this,'"+value+"','"+assetLinkDatagridID+"',"+index+",'"+caseCategory+"')\">移除設備</a>";
	//標誌位為R 說明此設備需要移除
	} else if(row.isLink == 'R') {
			return "<a href='javascript:void(0)' onclick=\"delectCaseAssetName(this,'"+value+"','"+assetLinkDatagridID+"',"+index+",'"+caseCategory+"')\">移除設備</a>";
	} 
}

//簽收和線上排除formatter動作下拉框的class（重新加載樣式）
function caseAssetLinkActionFormatter(){
	$(".paymentAsset").combobox({
	});
}
//目的：簽收和線上排除時候，標誌位為Y ，取消設備鏈接之後，更新設備鏈接的row  
//assetLinkDatagridID：設備鏈接的datagridId
// index:設備鏈接datagrid裡面正在處理行的行號 ,
//caseCategory:選擇行的案件類別
function linkCaseAssetName(obj,value,assetLinkDatagridID,index, caseCategory){
	//拆機
	var index = $(obj).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
 	var rows = $("#"+assetLinkDatagridID).datagrid('getRows');
	var row = rows[index];
	//需要填寫動作下拉框的標誌位
	var isRepeatLink = row.isRepeatLink;
	//主鍵
	var assetLinkId = row.assetLinkId;
	var snString = $("#selectSn").val();
	if(snString.length > 0) {
		snString = snString.substring(0,snString.length-1);
	}
	var snArray = snString.split(",");
	var newSn = "";
	for(var k =0; k<snArray.length;k++) {
		if(snArray[k] != row.serialNumber) {
			newSn = newSn + snArray[k] + ",";
		}
	}
	if(newSn == ",") {
		newSn = "";
	}
	$("#selectSn").val(newSn);
	var content;
	//需要填寫動作下拉框的標誌位
	if(isRepeatLink == 'Y') {
		content = $("#"+assetLinkId+"content").textbox("getValue");
		if($("#"+assetLinkId+assetLinkDatagridID).length>0) {
			if($("#"+assetLinkId+assetLinkDatagridID).combobox("getValue") == "") {
				$("#"+assetLinkId+assetLinkDatagridID).combobox('textbox').focus().trigger('mouseover');
				return false;
			}
		}
	}
	if(caseCategory != 'INSTALL') {
		$(".textbox-contentLink").textbox();
	 	$(".textbox-contentLink").each(function(index, obj){
			textBoxDefaultSetting($(obj));
		})
	}
	var obj = new Object();
	if(caseCategory == 'UNINSTALL') {
		obj.action=$("#"+assetLinkId+assetLinkDatagridID).combobox("getValue");
		obj.content=$("#"+assetLinkId+"content").textbox("getValue");
	}
	//需要填寫動作下拉框的標誌位
	if(isRepeatLink == 'Y') {
		var actionValue = "";
		actionValue = $("#"+assetLinkId+assetLinkDatagridID).combobox("getValue");
		$("#"+assetLinkDatagridID).datagrid('updateRow',{
			index: index,
			row: {
				serialNumber: '',
				isLink : 'N',
				isRepeatLink : 'N',
				actionValue : actionValue,
				removeContent : content
			}
		});
	} else {
		$("#"+assetLinkDatagridID).datagrid('updateRow',{
			index: index,
			row: {
				serialNumber: '',
				isLink : 'N',
				isRepeatLink : 'N'
			}
		});
	}
	$("#"+assetLinkDatagridID).datagrid('beginEdit', index);
	caseAssetLinkActionFormatter();
	if($("#"+assetLinkId+assetLinkDatagridID).length>0) {
		$("#"+assetLinkId+assetLinkDatagridID).siblings("span").css('display','none');
	};
	if(caseCategory != 'INSTALL') {
		$(".textbox-contentLink").textbox();
	 	$(".textbox-contentLink").each(function(index, obj){
			textBoxDefaultSetting($(obj));
		})
		$("#"+assetLinkId+"content").textbox('setValue',obj.content);
	};
//	$('a[name=contentLink]').textbox({ plain: true, width: 140 });
	$('a[name=lbtLink]').linkbutton({ plain: true, iconCls: 'icon-search' });
}
//目的：簽收和線上排除移除設備鏈接，將R的標誌位改為D，更新datagridRow  
//assetLinkDatagridID：設備鏈接的datagridId
// index:設備鏈接datagrid裡面正在處理行的行號 ,
//caseCategory:選擇行的案件類別
function delectCaseAssetName(obj, value,assetLinkDatagridID,index,caseCategory){
	var index = $(obj).parents("tr[datagrid-row-index]").attr("datagrid-row-index");
 	var rows = $("#"+assetLinkDatagridID).datagrid('getRows');
	var row = rows[index];
	
	var deletedObj = {};
	var assetLinkId = row.assetLinkId;
	deletedObj.assetLinkId = row.assetLinkId;
	//var assetLinkId = row.assetLinkId;
	var action;
	var content;
	//如果有 動作下拉框
	if($("#"+assetLinkId+assetLinkDatagridID).length>0) {
		content = $("#"+assetLinkId+"content").textbox("getValue");
		action = $("#"+assetLinkId+assetLinkDatagridID).combobox("getValue");
		if($("#"+assetLinkId+assetLinkDatagridID).combobox("getValue") == "") {
			$("#"+assetLinkId+assetLinkDatagridID).combobox('textbox').focus().trigger('mouseover');
			return false;
		}
	}
	if(caseCategory == 'INSTALL' || action == null) {
		deletedObj.action  = 'INSTALL';
		//assetLinkId = assetLinkId + ";" +'INSTALL'+";";
	} else {
		action = $("#"+assetLinkId+assetLinkDatagridID).combobox("getValue");
		deletedObj.action  = action;
		deletedObj.content = content;
		//assetLinkId = assetLinkId + ";" +action + ";" +content;
	}
	//要刪除的設備和耗材的id
	var deletedObjs = [];
	var deleteCaseAssetLinkIds = $("#deleteCaseAssetLinkIds").val();
	if (deleteCaseAssetLinkIds != "") {
		var obj = JSON.parse(deleteCaseAssetLinkIds);
		if (obj instanceof Array) {
			deletedObjs = obj;
		} else {
			deletedObjs.push(obj);
		}
	}
	deletedObjs.push(deletedObj);
	var deletedIds = JSON.stringify(deletedObjs);
	$("#deleteCaseAssetLinkIds").val(deletedIds);
	//if(deleteCaseAssetLinkIds != "") {
	//	$("#deleteCaseAssetLinkIds").val(deleteCaseAssetLinkIds + "," +assetLinkId)
	//} else {
	//	$("#deleteCaseAssetLinkIds").val(assetLinkId)
	//}
	$("#"+assetLinkDatagridID).datagrid('updateRow',{
		index: index,
		row: {
			serialNumber: '',
			isLink : 'D',
			action:'',
			content:''
		}
	});
	$("#"+assetLinkDatagridID).datagrid('deleteRow', parseInt(index));  
}
//目的：簽收和線上排除案件建立設備鏈接datagrid的說明欄位的格式化
function caseAssetLinkContentFormatter(value,row,index){
	var assetLinkId = row.assetLinkId;
	var content = row.content;
	if(content == null) {
		content = "";
	}
//	return "<a href='javascript:void(0)' name='contentLink' maxlength='10' id='"+actionId+"content'></a>";
	return "<input class='textbox-contentLink' id='"+assetLinkId+"content' maxlength='200' value='"+content+"'/>";
}
function showAssetAndSupplies() {
	var rows = $('#dg').datagrid('getSelections');
	// 簽收、線上排除-設備設定連結及耗材設定連接 
	for (var i = 0; i < rows.length; i++) {
		$('#dg').datagrid("expandRow", $('#dg').datagrid('getRowIndex', rows[i]));
	}
}
//目的：簽收和線上排除時候 耗材datagrid獲取該客戶下的耗材分類 LIst   
//customerId ：客戶Id
function getSuppliesTypeList(customerId) {
	javascript:dwr.engine.setAsync(false);
	ajaxService.getSuppliesListByCustomseId(customerId, function(data){
		if(data.flag){
			var suppliesString = data.jsonData;
			var suppliesObj = jQuery.parseJSON(suppliesString);
			suppliesTypeList = initSelect(suppliesObj);
		}
	});
	javascript:dwr.engine.setAsync(true);
	return suppliesTypeList;
}
//目的：簽收和線上排除時候 耗材datagrid 獲取該客戶下的耗材名稱list  
//customerId ：客戶Id
function getSuppliesNameList(customerId) {
	javascript:dwr.engine.setAsync(false);
	ajaxService.getSuppliesNameList(customerId, "", function(data){
		if(data.flag){
			var suppliesNameString = data.jsonData;
			var suppliesNameObj = jQuery.parseJSON(suppliesNameString);
			suppliesNameList = initSelect(suppliesNameObj);
		}
	});
	javascript:dwr.engine.setAsync(true);
	return suppliesNameList;
}
/*
*結束編輯耗材連接
* datagridId：耗材鏈接的datagrid
* indexSupplies：長在結束編輯的耗材行號index
* row：正在編輯的耗材行的數據
*/
function onEndEditCaseSuppliesLink(datagridId,indexSupplies, row){
		//耗材分類
	var itemIdRow = $(datagridId).datagrid('getEditor', {
		index : indexSupplies,
		field : 'itemId'
	});
	//耗材名稱
	var itemCategoryRow = $(datagridId).datagrid('getEditor', {
		index : indexSupplies,
		field : 'itemCategory'
	});
	row.itemName = $(itemIdRow.target).combobox('getText');
	row.itemCategoryName = $(itemCategoryRow.target).combobox('getText');
}
/**
*Task #2797 驗證設備鏈接周邊設備
*/
function validateRepairOnlineExclusion(selectRow,problemSolution,dealFlag,caseCategory) {
	var flag = "";
	var ddvRow;
	//主頁面
	if (dealFlag) {
		var rowindexs = [];
		var rowindex;
		//案件類別
		var caseCategoryVal = selectRow[0].caseCategory;
		for(var i = 0;i<selectRow.length; i++){
			rowindex = $("#dg").datagrid('getRowIndex',selectRow[i]);
			rowindexs.push(rowindex)
		}
		//選取的行
		for(var i = 0;i<rowindexs.length; i++){
			//設備鏈接
			var ddvRows = $("#ddv-"+rowindexs[i]).datagrid("getRows");
			if(ddvRows.length > 0) {
				for (var j = 0; j < ddvRows.length; j ++) {
					ddvRow = ddvRows[j];
					//Task #2797解決方式=特店-提供新刷卡機 ，但無替換主機設備，請警示，解決方式=特店-提供新刷卡機，請替換設備 2017/11/14
					// Task #2992 案件處理工程師修復問題解決方式選擇[刷卡機-更換刷卡機]時 須檢核是否已更新設備
					//Task #3154 其他-異動資產(更換刷卡機) PROBLEM_SOLUTION_OTHER
					if (caseCategoryVal == 'REPAIR' && ddvRow.itemCategory=='EDC' 
						&& (problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC' 
									|| problemSolution == 'PROBLEM_SOLUTION_EDC-CHANGE_EDC' 
									|| problemSolution == 'PROBLEM_SOLUTION_OTHER-07'
									|| problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC_CMS'
									|| problemSolution == 'PROBLEM_SOLUTION_EDC-CHANGE_EDC_CMS'
									|| problemSolution == 'PROBLEM_SOLUTION_OTHER-CMS006')) {
						if (ddvRow.isRepeatLink =='Y' && ddvRow.uninstallSerialNumber==ddvRow.serialNumber) {
							if(problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC' || problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC_CMS'){
								$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=特店-提供新刷卡機，請替換設備','warning');
							} else if(problemSolution == 'PROBLEM_SOLUTION_OTHER-CMS006'){
								$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=其他-異動設備，請替換設備','warning');
							} else if(problemSolution == 'PROBLEM_SOLUTION_OTHER-07'){
								$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=其他-異動資產(更換刷卡機)，請替換設備','warning');
							} else {
								$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=刷卡機-更換刷卡機，請替換設備','warning');
							}
							flag = 'Y';
							return flag;
						}
					}		
				}
			}
		}
	} else {
		//設備鏈接
		var ddvRows = $('#dataGridAssetLink').datagrid("getRows");
		if(ddvRows.length > 0) {
			for (var j = 0; j < ddvRows.length; j ++) {
				ddvRow = ddvRows[j];
				//Task #2797解決方式=特店-提供新刷卡機 ，但無替換主機設備，請警示，解決方式=特店-提供新刷卡機，請替換設備 2017/11/14
				// Task #2992 案件處理工程師修復問題解決方式選擇[刷卡機-更換刷卡機]時 須檢核是否已更新設備
				//Task #3154 其他-異動資產(更換刷卡機) PROBLEM_SOLUTION_OTHER
				if (caseCategory == 'REPAIR' && ddvRow.itemCategory=='EDC'
					&& (problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC' 
							|| problemSolution == 'PROBLEM_SOLUTION_EDC-CHANGE_EDC' 
							|| problemSolution == 'PROBLEM_SOLUTION_OTHER-07'
							|| problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC_CMS'
							|| problemSolution == 'PROBLEM_SOLUTION_EDC-CHANGE_EDC_CMS'
							|| problemSolution == 'PROBLEM_SOLUTION_OTHER-CMS006'	)) {
					if (ddvRow.isRepeatLink =='Y' && ddvRow.uninstallSerialNumber==ddvRow.serialNumber) {
						if(problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC'|| problemSolution == 'PROBLEM_SOLUTION_MER-PROVIDE_NEW_EDC_CMS'){
							$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=特店-提供新刷卡機，請替換設備','warning');
						} else if(problemSolution == 'PROBLEM_SOLUTION_OTHER-CMS006'){
							$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=其他-異動設備，請替換設備','warning');
						} else if(problemSolution == 'PROBLEM_SOLUTION_OTHER-07'){
							$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=其他-異動資產(更換刷卡機)，請替換設備','warning');
						} else {
							$.messager.alert('提示訊息','案件編號：'+ddvRow.caseId+'，解決方式=刷卡機-更換刷卡機，請替換設備','warning');
						}
						flag = 'Y';
						return flag;
					}
				}
			}
		}	
	}	
	return flag;
}

/**
*驗證設備鏈接周邊設備是否取消或設定完整(主頁面)
*/
function validatePeripheralsIsComplete(selectRow,row,datagridId) {
	var rowindexs = [];
	var rowindex;
	var ddvRow;
	//案件類別
	var caseCategory = selectRow[0].caseCategory;
	for(var i = 0;i<selectRow.length; i++){
		rowindex = $("#dg").datagrid('getRowIndex',selectRow[i]);
		rowindexs.push(rowindex)
	}
	var flag = false;
	var caseAssetLinkSerialNumber = "";
		//選取的多大行
		for(var i = 0;i<rowindexs.length; i++){
			//設備鏈接
			var ddvRows = $("#ddv-"+rowindexs[i]).datagrid("getRows");
			//設備鏈接的Column
			var ddvColumn = $("#ddv-"+rowindexs[i]).datagrid("getColumnFields");
			//設備鏈接的fieldName
			var ddvfieldName;
			var ddvRow;
			var isRemovePeripherals = false;
			if(ddvRows.length == 0) {
				flag = 'Y';
				return flag;
			}
			for (var j = 0; j < ddvRows.length; j ++) {
				ddvRow = ddvRows[j];
				if(ddvRow.itemType != '20') {
					//(a)若案件類別為“拆機”，則需將設備與週邊設備資訊移除，
					if(caseCategory == 'UNINSTALL') {
						//若EDC未移除，「案件編號：{案件編號}，請移除連結的設備與週邊設備資料」
						if(ddvRow.itemType == '10' && ddvRow.isLink != 'N') {
							$.messager.alert('提示訊息','案件編號：'+selectRow[i].caseId+'，請移除連結的設備與週邊設備資料','warning');
							isRemovePeripherals = true;
							flag = 'D';
							return flag;
						} 
					} 
				}
			}
			if(!isRemovePeripherals) {
				//每大行裡面的所有小行
				for (var j = 0; j < ddvRows.length; j ++) {
					ddvRow = ddvRows[j];
					if(ddvRow.itemType != '20') {
						//(a)若案件類別為“拆機”，則需將設備與週邊設備資訊移除，
						if(caseCategory == 'UNINSTALL') {
							if((ddvRow.itemType == '11' 
							|| ddvRow.itemType == '12' 
							|| ddvRow.itemType == '13') 
							&& ddvRow.isLink != 'N'){
								flag = selectRow[i].caseId;
								return flag;
							} else {
								flag = 'Y';
							}
							// 除拆機外所有案件，系統檢核設備與週邊設備，是否設定或取消完整，不符合，則提示「案件編號：{案件編號}，設備與週邊設備設定不完整」
						} else {
							if((ddvRow.itemType == '10' || ddvRow.itemType == '11'
							 || ddvRow.itemType == '12' || ddvRow.itemType == '13')
							  && (ddvRow.isLink == 'N' || ddvRow.isLink == 'R')){
								$.messager.alert('提示訊息','案件編號：'+selectRow[i].caseId+'，設備與週邊設備設定不完整','warning');
								flag = 'D';
								return flag;
							} else {
								flag = 'Y';
							}
							if(ddvRow.itemType == '10' || ddvRow.itemType == '11'
							 || ddvRow.itemType == '12' || ddvRow.itemType == '13') {
								if(caseAssetLinkSerialNumber.indexOf(ddvRow.serialNumber) > 0) {
									$.messager.alert('提示訊息','案件編號：'+selectRow[i].caseId+'，設備與週邊設備設備序號：' + ddvRow.serialNumber + '已被連結','warning');
									flag = 'D';
									return flag;
								} else {
									caseAssetLinkSerialNumber = caseAssetLinkSerialNumber + "," + ddvRow.serialNumber;
									flag = 'Y';
								}
							} else {
								flag = 'Y';
							}
						}
						if(ddvRow.isLink == 'Y') {
							if($("#"+ddvRow.assetLinkId+"ddv-"+rowindexs[i]).length) {
								var action = $("#"+ddvRow.assetLinkId+"ddv-"+rowindexs[i]).combobox("getValue");
								if(action != null && action != '') {
									$.messager.alert('提示訊息','案件編號：'+selectRow[i].caseId+'，設備與週邊設備未移除連結,不可選擇動作','warning');
									flag = 'D';
									return flag;
								}
							}
						}
					} else {
						flag = 'Y';
					}
				}
			}
		}
	return flag;
}
/*
* 目的：簽收和線上排除時候 展開設備鏈接的datagrid
* isExpandRow：判斷是主頁面還是處理頁面，主頁面是true
* queryData：查詢數據
* datagridColums:設備及週邊設備設定連結的Colums
* index：所選行 行號
*/
function expandAssetLinkDatagrid(isExpandRow, queryData, datagridColums, index, contextPath){
	var caseAssetLinkOptions = {
			url : contextPath+"/caseHandle.do?actionId=queryCaseAssetLink",
			queryParams : queryData,
			title: "設備及週邊設備設定連結",
			width: 900,
			singleSelect: true,
			rownumbers: true,
			loadMsg: '',
			columns:[datagridColums],
			onLoadSuccess: function (data) {
				$('a[name=lbtLink]').linkbutton({ plain: true, iconCls: 'icon-search' });
			//	$('a[name=contentLink]').textbox({ plain: true, width: 140 });
				$(".textbox-contentLink").textbox();
			 	$(".textbox-contentLink").each(function(index, obj){
					textBoxDefaultSetting($(obj));
				})
		        $(".paymentAsset").combobox({
		        });
		        if (isExpandRow) {
					setTimeout(function () {
						$.each($('#dg').datagrid('getRows'), function (i, row) {
							$('#dg').datagrid('fixRowHeight', i);
						});
						$('#dg').datagrid('fixDetailRowHeight', index);
					}, 0);
				}
			}
		}
		if (isExpandRow) {
			caseAssetLinkOptions.onResize = function () {
				$('#dg').datagrid('fixDetailRowHeight', index);
			};
		}
		return caseAssetLinkOptions;
}
/*
 *耗材品新增一行 
 *	datagridId :datagrid的ID
 *	index：正在編輯的行索引
 * 	isExpandRow：true是主頁面，false是 處理頁面
 */
 function appendCaseSupplies(datagridId, index, isExpandRow){
 	if($(datagridId).datagrid('getRows').length == 0) {
 		//如果是新增從0行開始，不用驗證結束編輯狀態
			$(datagridId).datagrid('appendRow', {itemCategory:'',itemId:''});
			editIndex = $(datagridId).datagrid('getRows').length - 1;
			$(datagridId).datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
			//耗材數量
			var number = $(datagridId).datagrid('getEditor',{ 'index': editIndex, field: 'number' });
			$(number.target).textbox('textbox').attr('maxlength',3);
			//耗材說明
			var content = $(datagridId).datagrid('getEditor',{'index':editIndex,field:'content'});
			$(content.target).textbox('textbox').attr('maxlength',200);
		} else {
			if (endEditingCaseSuppliesLink(editIndex, datagridId,isExpandRow)) {
				$(datagridId).datagrid('appendRow', {itemCategory:'',itemId:''});
				editIndex = $(datagridId).datagrid('getRows').length - 1;
				$(datagridId).datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
				//耗材數量
				var number = $(datagridId).datagrid('getEditor',{ 'index': editIndex, field: 'number' });
				$(number.target).textbox('textbox').attr('maxlength',3);
				//耗材說明
				var content = $(datagridId).datagrid('getEditor',{'index':editIndex,field:'content'});
				$(content.target).textbox('textbox').attr('maxlength',200);
			}
		}
		editRows = editIndex;
		$('#dg').datagrid('fixDetailRowHeight', index);
		//$(datagridId).datagrid("unselectRow", editIndex);
	}
//驗證耗材的下拉框是否驗證通過
function validateSuppliesDatagrid(){
	if(editIndex == undefined) {
		return true;
	}
	var controls = ['itemCategory','itemId','number','content'];
	if(!validateFormInRow('dataGridSuppliesLink', editIndex, controls)){
		return false;
	} else {
		$("div").unbind("scroll.validate");
		editIndex = undefined;
		return true;
	}
}
/*
* 結束編輯狀態 
* editIndex：正在編輯行的index
* datagridId 耗材鏈接的datagridId
* isExpandRow：true是主頁面，false是處理頁面
*/
function endEditingCaseSuppliesLink(editIndex, datagridId, isExpandRow){
	$('#caseDivId').unbind("scroll.validate");
	var rows = $(datagridId).datagrid("getRows");
	var selectRow = $(datagridId).datagrid('getSelections');
	for(var i = 0; i< rows.length; i++) {
		var index = $(datagridId).datagrid('getRowIndex', rows[i]);
 		if (!$(datagridId).datagrid('validateRow', index)){
 			editIndex = index;
 		}
	}
	if(!isExpandRow) {
		if(!validateSuppliesDatagrid()) {
			return false;
		}
	} else {
		var controls = ['itemCategory','itemId'];
		if(!validateFormInRow(datagridId.substring(1,datagridId.length), editIndex, controls)){
			return false;
		} else {
			$("div").unbind("scroll.validate");
		}
	}
	var endEditFlag = false;
	for(var i = 0; i< rows.length; i++) {
 		//驗證行數據
 		var index = $(datagridId).datagrid('getRowIndex', rows[i]);
 		if ($(datagridId).datagrid('validateRow', index)){
 			//結束編輯行
 			$(datagridId).datagrid('endEdit', parseInt(index));
 			var isSelect = false;
 			for(var j = 0;j< selectRow.length; j++) {
	    		var rowIndex = $(datagridId).datagrid('getRowIndex', selectRow[j]);
	    		if(index == rowIndex) {
	    			isSelect = true;
	    		}
	    	}
	    	if((!isSelect) && index != editIndex) {
	 			//$(datagridId).datagrid("unselectRow", parseInt(index));
	    	}
 			endEditFlag = true;
 		} else {
 			editIndex = index;
 			endEditFlag = false;
 			break;
 		}
	//$(datagridId).datagrid("unselectRow", editIndex);
	}
	if(endEditFlag) {
		editIndex = undefined;
	}
	return endEditFlag;
}
/**
*簽收的存儲(子頁面) 設備鏈接和耗材datagrid獲取參數值 caseCategory 案件類別， caseId：caseid ，row：選擇的案件行
*/
function saveSignParamerInCaseAction(caseCategory,row, caseId) {
	var ddvRow;
	//案件類別
	//var caseCategory = selectRow[0].caseCategory;
	
	var ddvRows = $('#dataGridAssetLink').datagrid("getRows");
	var ddvColumn = $('#dataGridAssetLink').datagrid("getColumnFields");
	var ddv2Rows = $('#dataGridSuppliesLink').datagrid("getRows");
	var ddv2Column = $('#dataGridSuppliesLink').datagrid("getColumnFields");
	var ddvfieldName;
	var ddv2fieldName;
	var ddvparameter;
	var ddv2parameter;
	var saveSignParamer = [];
	//設備鏈接datagrid獲取參數值
	for (var i = 0; i < ddvRows.length; i++) {
	
		ddvRow = ddvRows[i];
		ddvparameter = {};
		//裝機的時候才顯示 動作的下拉框
		var obj = new Object();
		if(caseCategory != 'INSTALL') {
			obj.content=$("#"+ddvRow.assetLinkId+"content").textbox("getValue");
			ddvparameter.content = obj.content;
		}
		if($("#"+ddvRow.assetLinkId).length>0) {
			obj.action=$("#"+ddvRow.assetLinkId).combobox("getValue");
			ddvparameter.action = obj.action;
		}
		//每行裡面的column
		for (var k = 0; k < ddvColumn.length; k ++) {
			ddvfieldName = ddvColumn[k];
			if (ddvfieldName == 'serialNumber') {
				ddvparameter.serialNumber = ddvRow.serialNumber;
			} else if(ddvfieldName == 'itemId') {
				ddvparameter.itemId = ddvRow.itemId;
				ddvparameter.itemName = ddvRow.itemName;
			} else if(ddvfieldName == 'itemCategory') {
				ddvparameter.itemCategory = ddvRow.itemCategory;
			} else if(ddvfieldName == 'isLink') {
				ddvparameter.isLink = ddvRow.isLink;
			} else if(ddvfieldName == 'warehouseId') {
				ddvparameter.warehouseId = ddvRow.warehouseId;
			} else if(ddvfieldName == 'contractId') {
				ddvparameter.contractId = ddvRow.contractId;
			} else if(ddvfieldName == 'uninstallSerialNumber') {
				ddvparameter.uninstallSerialNumber = ddvRow.uninstallSerialNumber;
			} else if(ddvfieldName == 'propertyId') {
				ddvparameter.propertyId = ddvRow.propertyId;
			} else if(ddvfieldName == 'actionValue') {
				ddvparameter.actionValue = ddvRow.actionValue;
			} else if(ddvfieldName == 'removeContent') {
				ddvparameter.removeContent = ddvRow.removeContent;
			} else if(ddvfieldName == 'isRepeatLink') {
				ddvparameter.isRepeatLink = ddvRow.isRepeatLink;
			}
			//設備鏈接
			ddvparameter.itemType = ddvRow.itemType;
			ddvparameter.assetLinkId = ddvRow.assetLinkId;
		}
		ddvparameter.caseId = caseId;
		saveSignParamer.push(ddvparameter);
		//saveSignParamer.push(ddvRows[i]);
	}
	if(!validateSuppliesDatagrid()) {
		return false;
	}
	for (var i = 0; i < ddv2Rows.length; i++) {
		if ($('#dataGridSuppliesLink').datagrid('validateRow', i)){
			$('#dataGridSuppliesLink').datagrid('endEdit', i);
			editIndex = undefined;
		} else {
			editIndex = i;
			return false;
		}
		ddv2Row = ddv2Rows[i];
		ddv2parameter = {};
		// Bug #2346 若客戶是彰銀且為裝機案件，再預設帶出「口窗貼」，若沒輸入數量，要填說明
		//1. 只針對 口窗貼，目前都檢核
		//2. 錯誤訊息調整，耗材-口窗貼 請輸入數量或
		//裝機
		var isFlag = true;
		if(caseCategory == 'INSTALL') {
			javascript:dwr.engine.setAsync(false);
			ajaxService.getCaseInfoById(caseId, function(data){
				if(data) {
					//彰銀
					if(data.companyCode == "CHB_EDC") {
						//只針對 口窗貼
						if("WindowSticker" == ddv2Row.itemCategory) {
							if((ddv2Row.number == "" || ddv2Row.number == null)
									&& ($.trim(ddv2Row.content) == "" || $.trim(ddv2Row.content) == null)) {
								$.messager.alert('提示訊息','案件編號：'+caseId+'，耗材-口窗貼 請輸入數量或說明','warning');
								isFlag = false;
								return false;
							}
						}
					}
				}
			});
			javascript:dwr.engine.setAsync(false);
		}
		if(!isFlag) {
			return false;
		}
		//每行裡面的column
		for (var k = 0; k < ddv2Column.length; k ++) {
			ddv2fieldName = ddv2Column[k];
			if (ddv2fieldName == 'itemCategory') {
				ddv2parameter.itemCategory = ddv2Row.itemCategory;
			} else if(ddv2fieldName == 'itemId') {
				ddv2parameter.itemId = ddv2Row.itemId;
				ddv2parameter.itemName = ddv2Row.itemName;
			} else if(ddv2fieldName == 'number') {
				ddv2parameter.number = ddv2Row.number;
				if(ddv2parameter.number == ""){
					ddv2parameter.number = null;
				}
			} else if(ddv2fieldName == 'content') {
				ddv2parameter.content = ddv2Row.content;
			} 
			//20標示是耗材品
			ddv2parameter.itemType = '20';
			ddv2parameter.assetLinkId = ddv2Row.assetLinkId;
		}
		ddv2parameter.caseId = caseId;
		saveSignParamer.push(ddv2parameter);
		//saveSignParamer.push(ddv2Rows[i]);
	}
	return saveSignParamer;
}
/**
*簽收的存儲(主頁面)
*/
function saveSignParamer(selectRow,row,datagridId) {
	var rowindexs = [];
	var caseIds = [];
	var rowindex;
	var ddvRow;
	//(rows[i].caseCategory == '${caseCategoryAttr.UNINSTALL.code }'
	//案件類別
	var caseCategory = selectRow[0].caseCategory;
	for(var i = 0;i<selectRow.length; i++){
		rowindex = $("#dg").datagrid('getRowIndex',selectRow[i]);
		rowindexs.push(rowindex);
		caseIds.push(selectRow[i].caseId);
	}
	var ddvAssetLinkParameters = [];
	//選取的多大行
	for(var i = 0;i<rowindexs.length; i++){
		var caseId = caseIds[i];
		//設備鏈接
		var ddvRows = $("#ddv-"+rowindexs[i]).datagrid("getRows");
		//設備鏈接的Column
		var ddvColumn = $("#ddv-"+rowindexs[i]).datagrid("getColumnFields");
		//設備鏈接的fieldName
		var ddvfieldName;
		var ddvparameter;
		var ddvRow;
		//耗材品
		var ddv2Rows = $("#dataGridSuppliesLink-" + rowindexs[i]).datagrid("getRows");
		var ddv2Column = $("#dataGridSuppliesLink-" + rowindexs[i]).datagrid("getColumnFields");
		var ddv2fieldName;
		var ddv2parameter;
		var ddv2Row;
		//var ddv2SuppliesParameters = [];
		//每大行裡面的所有小行
		var controls = ['itemCategory','itemId','number','content'];
		if(editIndex != undefined) {
			if(!validateFormInRow('dataGridSuppliesLink-'+rowindexs[i], editIndex, controls)){
				return false;
			} else {
				$("div").unbind("scroll.validate");
			}
		}
		for (var j = 0; j < ddvRows.length; j ++) {
			ddvRow = ddvRows[j];
			ddvparameter = {};
			//裝機的時候才顯示 動作的下拉框
			var obj = new Object();
			if(caseCategory != 'INSTALL') {
				obj.content=$("#"+ddvRow.assetLinkId+"content").textbox("getValue");
				ddvparameter.content = obj.content;
			}
			if($("#"+ddvRow.assetLinkId).length>0) {
				obj.action=$("#"+ddvRow.assetLinkId).combobox("getValue");
				ddvparameter.action = obj.action;
			}
			//每行裡面的column
			for (var k = 0; k < ddvColumn.length; k ++) {
				ddvfieldName = ddvColumn[k];
				if (ddvfieldName == 'serialNumber') {
					ddvparameter.serialNumber = ddvRow.serialNumber;
				} else if(ddvfieldName == 'itemId') {
					ddvparameter.itemId = ddvRow.itemId;
					ddvparameter.itemName = ddvRow.itemName;
				} else if(ddvfieldName == 'itemCategory') {
					ddvparameter.itemCategory = ddvRow.itemCategory;
				} else if(ddvfieldName == 'isLink') {
					ddvparameter.isLink = ddvRow.isLink;
				} else if(ddvfieldName == 'warehouseId') {
					ddvparameter.warehouseId = ddvRow.warehouseId;
				} else if(ddvfieldName == 'contractId') {
					ddvparameter.contractId = ddvRow.contractId;
				} else if(ddvfieldName == 'uninstallSerialNumber') {
					ddvparameter.uninstallSerialNumber = ddvRow.uninstallSerialNumber;
				} else if(ddvfieldName == 'propertyId') {
					ddvparameter.propertyId = ddvRow.propertyId;
				} else if(ddvfieldName == 'actionValue') {
					ddvparameter.actionValue = ddvRow.actionValue;
				} else if(ddvfieldName == 'removeContent') {
					ddvparameter.removeContent = ddvRow.removeContent;
				} else if(ddvfieldName == 'isRepeatLink') {
					ddvparameter.isRepeatLink = ddvRow.isRepeatLink;
				}
				//設備鏈接
				ddvparameter.itemType = ddvRow.itemType;
				ddvparameter.assetLinkId = ddvRow.assetLinkId;
			}
			ddvparameter.caseId = caseId;
			ddvAssetLinkParameters.push(ddvparameter);
		}
			//驗證耗材datagrid
			for (var j = 0; j < ddv2Rows.length; j ++) {
				ddv2Row = ddv2Rows[j];
				ddv2parameter = {};
				if ($("#dataGridSuppliesLink-"+rowindexs[i]).datagrid('validateRow', j)){
   					$("#dataGridSuppliesLink-"+rowindexs[i]).datagrid('endEdit', j);
  				} else {
  					editIndex = j;
  					return false;
  				}
				// Bug #2346 若客戶是彰銀且為裝機案件，再預設帶出「口窗貼」，若沒輸入數量，要填說明
				//1. 只針對 口窗貼，目前都檢核
				//2. 錯誤訊息調整，耗材-口窗貼 請輸入數量或說明
				var isFlag = true;
				if(caseCategory == 'INSTALL') {
					javascript:dwr.engine.setAsync(false);
					ajaxService.getCaseInfoById(caseId, function(data){
						if(data) {
							//彰銀
							if(data.companyCode == "CHB_EDC") {
								//只針對 口窗貼
								if("WindowSticker" == ddv2Row.itemCategory) {
									if((ddv2Row.number == "" || ddv2Row.number == null)
											&& ($.trim(ddv2Row.content) == "" || $.trim(ddv2Row.content) == null)) {
										$.messager.alert('提示訊息','案件編號：'+caseId+'，耗材-口窗貼 請輸入數量或說明','warning');
										isFlag = false;
										return false;
									}
								}
							}
						}
					});
					javascript:dwr.engine.setAsync(true);
				}
				if(!isFlag) {
					editIndex = j;
					return false;
				}
				//每行裡面的column
				for (var k = 0; k < ddv2Column.length; k ++) {
					ddv2fieldName = ddv2Column[k];
					if (ddv2fieldName == 'itemCategory') {
						ddv2parameter.itemCategory = ddv2Row.itemCategory;
					} else if(ddv2fieldName == 'itemId') {
						ddv2parameter.itemId = ddv2Row.itemId;
						ddv2parameter.itemName = ddv2Row.itemName;
					} else if(ddv2fieldName == 'number') {
						ddv2parameter.number = ddv2Row.number;
						if(ddv2parameter.number == ""){
							ddv2parameter.number = null;
						}
					} else if(ddv2fieldName == 'content') {
						ddv2parameter.content = ddv2Row.content;
					} 
					//20標示是耗材品
					ddv2parameter.itemType = '20';
					ddv2parameter.assetLinkId = ddv2Row.assetLinkId;
				}
				ddv2parameter.caseId = caseId;
				ddvAssetLinkParameters.push(ddv2parameter);
			}
	}
	editIndex = undefined;
	return ddvAssetLinkParameters;
}
/**
*驗證設備鏈接周邊設備是否取消或設定完整(子頁面)
*/
function validatePeripheralsIsCompleteInCaseAction(caseCategory,row, caseId) {
	var ddvRow;
	//案件類別
	//var caseCategory = selectRow[0].caseCategory;
	var flag = false;
		//選取的多大行
	//設備鏈接
	var ddvRows = $('#dataGridAssetLink').datagrid("getRows");
	//設備鏈接的Column
	var ddvColumn = $('#dataGridAssetLink').datagrid("getColumnFields");
	//設備鏈接的fieldName
	var ddvfieldName;
	var ddvRow;
	var isRemovePeripherals = false;
	if(ddvRows.length == 0) {
		flag = 'Y';
		return flag;
	}
	for (var j = 0; j < ddvRows.length; j ++) {
		ddvRow = ddvRows[j];
		if(ddvRow.itemType != '20') {
			//(a)若案件類別為“拆機”，則需將設備與週邊設備資訊移除，
			if(caseCategory == 'UNINSTALL') {
				//若EDC未移除，「案件編號：{案件編號}，請移除連結的設備與週邊設備資料」
				if(ddvRow.itemType == '10' && ddvRow.isLink != 'N') {
					$.messager.alert('提示訊息','案件編號：'+caseId+'，請移除連結的設備與週邊設備資料','warning');
					isRemovePeripherals = true;
					flag = 'D';
					return flag;
				} 
			} 
		}
	}
	if(!isRemovePeripherals) {
		//每大行裡面的所有小行
		for (var j = 0; j < ddvRows.length; j ++) {
			ddvRow = ddvRows[j];
			if(ddvRow.itemType != '20') {
				//(a)若案件類別為“拆機”，則需將設備與週邊設備資訊移除，
				if(caseCategory == 'UNINSTALL') {
					if((ddvRow.itemType == '11' || ddvRow.itemType == '12'
					 || ddvRow.itemType == '13') && ddvRow.isLink != 'N'){
						flag = caseId;
						return flag;
					} else {
						flag = 'Y';
					}
					// 除拆機外所有案件，系統檢核設備與週邊設備，是否設定或取消完整，不符合，則提示「案件編號：{案件編號}，設備與週邊設備設定不完整」
				} else {
					if((ddvRow.itemType == '10' || ddvRow.itemType == '11' 
					|| ddvRow.itemType == '12' || ddvRow.itemType == '13') 
					&& (ddvRow.isLink == 'N' || ddvRow.isLink == 'R')){
						$.messager.alert('提示訊息','案件編號：'+caseId+'，設備與週邊設備設定不完整','warning');
						flag = 'D';
						return flag;
					} else {
						flag = 'Y';
					}
				}
				if(ddvRow.isLink == 'Y') {
					if($("#"+ddvRow.assetLinkId+"dataGridAssetLink").length>0) {
						var action = $("#"+ddvRow.assetLinkId+"dataGridAssetLink").combobox("getValue");
						if(action != null && action != '') {
							$.messager.alert('提示訊息','案件編號：'+caseId+'，設備與週邊設備未移除連結,不可選擇動作','warning');
							flag = 'D';
							return flag;
						}
					}
				}
			} else {
				flag = 'Y';
			}
		}
	}
	return flag;
}
/*
 *耗材品移除 datagridId datagrid的id  ，index：要移除的行索引
 */
	function removeCaseSuppliesLink(datagridId, index) {
		var row = $(datagridId).datagrid('getSelections');
		var flag = false;
		var count = 0;
		if (row.length > 0) {
			//var flag = true;
			//驗證選中的行
			//if(!$(datagridId).datagrid('validateRow', editIndex)){
				//flag = false;
			//}
			//循環刪除選中的每一行
			for(var i = 0;i<row.length;i++){
				if(row[i].assetLinkId != "") {
   				var deleteCaseSuppliesLinkIds = $("#deleteCaseSuppliesLinkIds").val();
   				if(deleteCaseSuppliesLinkIds != "") {
   					$("#deleteCaseSuppliesLinkIds").val(deleteCaseSuppliesLinkIds + "," +row[i].assetLinkId)
   				} else {
   					$("#deleteCaseSuppliesLinkIds").val(row[i].assetLinkId)
   				}
				}
				var rowIndex = $(datagridId).datagrid('getRowIndex', row[i]);
				if(editIndex != undefined) {
					if(editIndex == rowIndex) {
  						flag = true;
  					}
  					if(editIndex > rowIndex) {
  						count ++;
  					}
				}
				//$(datagridId).datagrid("unselectRow", rowIndex);
				$(datagridId).datagrid('deleteRow', rowIndex);  
			}
		} else {
			$.messager.alert('提示訊息','請選擇耗材','warning');
			return false; 
		}
		if(flag) {
			editIndex = undefined;
		} else {
			if(editIndex != undefined) {
				editIndex = editIndex-count;
			}
		}
		editRows = editIndex;
		$('#dg').datagrid('fixDetailRowHeight', index);
   }
	 /*
	*耗材說明的聯動
	* suppliesLinkDatagridId ：耗材品datagrid的id
	*/
	var assetContentOnChange = function(newValue, oldValue,suppliesLinkDatagridId) {
		editRows = editIndex;
		//耗材數量
		var number = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editRows, field: 'number' });
		//耗材說明
		var content = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editRows, field: 'content' });
		var contentValue11 = $(content.target).textbox('getValue');
		if(contentValue11){
			var realContentValue = $.trim(contentValue11);
			$(content.target).textbox('setValue', realContentValue);
		}
		
		//耗材數量的value值
		var numberValue = $(number.target).textbox('getValue');
		var contentValue = $(content.target).textbox('getValue');
		var options = $(content.target).textbox('options');
		//若沒有輸入耗材數量，說明不能為空
		if(!options.required) {
			if(contentValue == "" || contentValue == null) {
				if(numberValue == "" || numberValue == null) {
					$(content.target).textbox('setValue','');
					//給說明添加required
					$(content.target).textbox({
						required: true
					});
				} 
  			}
		}
	}
	
	/*
	* 耗材品雙擊编辑事件
	* datagridId:datagrid的ID
	* indexSupplies：雙擊的耗材品的行
	* isExpandRow：true是主頁母安 false是處理頁面
	*/
	function onDblClickCellCaseSuppliesLink(datagridId, indexSupplies, field, value, isExpandRow){
		$('#caseDivId').unbind("scroll.validate");
		var selectRow = $(datagridId).datagrid('getSelections');
		if (endEditingCaseSuppliesLink(editIndex, datagridId, isExpandRow)){
			editIndex = indexSupplies;
			editRows = indexSupplies;
			$(datagridId).datagrid('selectRow', indexSupplies).datagrid('beginEdit', indexSupplies);
			//耗材分類
			var itemCategory = $(datagridId).datagrid('getEditor',{'index':indexSupplies,field:'itemCategory'});
			//耗材名稱
			var itemId = $(datagridId).datagrid('getEditor',{'index':indexSupplies,field:'itemId'});
			//數量
			var number = $(datagridId).datagrid('getEditor',{ 'index': indexSupplies, field: 'number' });
  			$(number.target).textbox('textbox').attr('maxlength',3);
  			$(number.target).textbox('textbox').attr('maxlength',3).bind('blur', function(e){
  				$(number.target).textbox('setValue', $.trim($(this).val()));
  			});
  			//說明
  			var content = $(datagridId).datagrid('getEditor',{'index':indexSupplies,field:'content'});
			$(content.target).textbox('textbox').attr('maxlength',200);
			$(content.target).textbox('textbox').attr('maxlength',200).bind('blur', function(e){
  				$(content.target).textbox('setValue', $.trim($(this).val()));
  			});
		} else {
			$(datagridId).datagrid('selectRow', editIndex);
		}
		var isSelect = false;
		for(var j = 0;j< selectRow.length; j++) {
	   		var rowIndex = $(datagridId).datagrid('getRowIndex', selectRow[j]);
	   		if(rowIndex == editIndex) {
	   			isSelect = true;
	   		}
	   	}
	   	if(!isSelect) {
			//$(datagridId).datagrid("unselectRow", editIndex);
	   	}
   	}
	/*
	*耗材數量的聯動
	* suppliesLinkDatagridId ：耗材品datagrid的id
	*/
	var assetNumberOnChange = function(newValue, oldValue,suppliesLinkDatagridId) {
			editRows = editIndex;
			//耗材數量
			var number = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editRows, field: 'number' });
			//耗材說明
			var content = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editRows, field: 'content' });
			//耗材數量的value值
			var numberValue = $(number.target).textbox('getValue');
			var contentValue = $(content.target).textbox('getValue');
			//若沒有輸入耗材數量，說明不能為空
			if(numberValue == "" || numberValue == null) {
				$(content.target).textbox({
					required: true
				});
  			} else {
  				if(contentValue == "" || contentValue == null) {
					$(content.target).textbox('setValue','');
  				}
				$(content.target).textbox({required: false});
  			}
  			$(content.target).textbox('textbox').attr('maxlength',200);
	}
	
	/*
	* 該客戶下的 耗材分類和耗材名稱的聯動
	* customerId：該客戶的客戶id
	* suppliesLinkDatagridId：耗材鏈接的datagrid的Id
	*/
	function suppliesTypeOnChange(newValue, oldValue,customerId,suppliesLinkDatagridId) {
		var rows = $(suppliesLinkDatagridId).datagrid('getRows');
			var row = rows[editIndex];
			//耗材數量
		var number = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editRows, field: 'number' });
		//耗材說明
		var content = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editRows, field: 'content' });
		//耗材數量的value值
		var numberValue = $(number.target).textbox('getValue');
		var contentValue = $(content.target).textbox('getValue');
		// 耗材分類
		var itemCategory = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editIndex,field:'itemCategory'});
		var itemCategoryValue = $(itemCategory.target).combobox('getValue');
		//耗材名稱
		var itemId = $(suppliesLinkDatagridId).datagrid('getEditor',{'index':editIndex,field:'itemId'});
		var itemIdValue = $(itemId.target).combobox('getValue');
		if (newValue != ""){
				ajaxService.getSuppliesNameList(customerId, $(itemCategory.target).combobox('getValue'), function(data){
					if(data.flag){
						var suppliesName = data.jsonData;
						var suppliesNameObj = jQuery.parseJSON(suppliesName);
						var result = suppliesNameObj;
						suppliesNameList = initSelect(suppliesNameObj);
						//查找所有的row
						for (var i = 0; i < rows.length; i++) {
							if (i == editIndex) {
								//當前行不比對
								continue;
							}
							if (rows[i].itemCategory == newValue) {
								//和選擇的行值一樣, 需過濾掉當前值                        		
								for (var j = 0; j < result.length; j++) {
									if (result[j].value == rows[i].itemId) {
										result.splice(j,1);
										break;
									}
								} 
							}
						}
						var currentEditor = $(suppliesLinkDatagridId).datagrid('getEditor', {  
								index : editIndex,
								field : 'itemId'
						});
						var currentValue = currentEditor.target.combobox('getValue');
						if (result.length > 0 && row.itemCategory == newValue) {
							currentEditor.target.combobox('setValue', currentValue);
						} else {
							currentEditor.target.combobox('setValue', '');
						}
						currentEditor.target.combobox('loadData', suppliesNameList);
					} 
				});
		} else {
			var currentEditor = $(suppliesLinkDatagridId).datagrid('getEditor', {  
				index : editIndex,  
				field : 'itemId'
			});
			currentEditor.target.combobox('setValue','');
			currentEditor.target.combobox('loadData',initSelect(null));
		} 
	};
	/**
	* 目的：簽收和線上排除時候給耗材分類的datagrid添加columns
	* customerId:客戶id
	* suppliesLinkDatagridId：耗材datagrid的id
	*/
	function addSuppliesLinkColumns(customerId,suppliesLinkDatagridId) {
		var columns = [];
		var suppliesTypeList = getSuppliesTypeList(customerId);
		var suppliesNameList = getSuppliesNameList(customerId);
		columns = [
			{	field: 'itemCategory', title: '耗材分類', width: 140, halign:'center', formatter: function (value, row) {
					return row.itemCategoryName;
				},
				editor: {
					type: 'combobox',
					options: {
						valueField: 'value',
						textField: 'name',
						editable:false,
						invalidMessage:'請輸入耗材分類',
						data: suppliesTypeList,
						panelHeight:'auto',
						onChange : function (newValue, oldValue){
							suppliesTypeOnChange(newValue, oldValue,customerId,suppliesLinkDatagridId);
						},
						required: true,
						validType:['requiredDropList']
					}
				}
			},
			{	field: 'itemId', title: '耗材名稱', width: 140, halign:'center', halign:'center', formatter: function (value, row) {
					return row.itemName;
				},
				editor: {
					type: 'combobox',
					options: {
						valueField: 'value',
						textField: 'name',
						editable:false,
						invalidMessage:'請輸入耗材名稱',
						data: suppliesNameList,
						required: true,
						validType:['requiredDropList']
					}
				}
			},
			{	field: 'number', title: '數量', width: 100, halign:'center',
              	editor:{
						type:'textbox',
						options:{
							required:false,
							missingMessage:'請輸入數量',
							validType:['number[\'數量限正整數，請重新輸入\']','maxLength[3]']
						}
					}
     		},
     		{	field: 'content', title: '說明', width: 240, halign:'center', 
     			editor:{
						type:'textbox',
						options:{
							required:false,
							missingMessage:'請輸入說明',
							validType:['maxLength[200]']
						}
					} 
     		},
     		{ field: 'assetLinkId', title: 'ID', width: 300,hidden:true,editor: 'textbox' },
     		{ field: 'itemType', title: '類別', width: 300,hidden:true,editor: 'textbox' },	
     		{ field: 'propertyId', title: '財產編號', width: 300,hidden:true,editor: 'textbox' }		
		];
		return columns;
	}
	
	/**
	* 點擊datagrid表格觸發的事件
	*/
	function onClickRow(datagridId,indexSupplies,field,value){
		if (editIndex == undefined) {
			$(datagridId).datagrid('selectRow', indexSupplies);
		} else {
			$(datagridId).datagrid('selectRow', editIndex);
		}
	}
	/**
	 * 裝機-派工-確認授權
	 */
	function confirmAuthorizes(flag, contextPath) {
		$("#dgResponse-msg").text("");
		var param = new Object();
		var caseIds = "";
		var updatedDateString = "";
		var selectRow = $("#dg").datagrid("getSelections");
		if (checkAuthorizesValidate(selectRow, false)) {
			//主頁面 存放異動時間
			for (var i = 0; i<selectRow.length; i++) {
				if(selectRow.length >=1 && i == selectRow.length -1 ){
					caseIds = caseIds + selectRow[i].caseId;
					updatedDateString = updatedDateString + selectRow[i].updatedDate+";"+ selectRow[i].caseId;
				} else {
					caseIds = caseIds + selectRow[i].caseId + ",";
					updatedDateString = updatedDateString + selectRow[i].updatedDate + ";" + selectRow[i].caseId + ",";
				}
			}
			param.caseId = caseIds;
			param.updatedDateString = updatedDateString;
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 加遮罩
			if(flag){
				// 調保存遮罩
				commonSaveLoading('editDlg');
			} else {
				$.blockUI(blockStyle);
			}
			$.ajax({
				url : contextPath + "/caseHandle.do?actionId=confirmAuthorizes",
				data : param,
				type : 'post',
				cache : false, 
				dataType :'json',
				success : function(json) {
					var pageIndex = getGridCurrentPagerIndex("dg");
					var msg;
					if (json.success) {
						$("#deleteCaseSuppliesLinkIds").val("");
						$("#deleteCaseAssetLinkIds").val("");
						$("#selectSn").val("");
						//成功提示
						if (json.cmsResult != undefined && !json.cmsResult) {
							$.messager.alert('提示訊息',json.msg,'');
						} else {
							$("#dgResponse-msg").text(json.msg);
						}
						query(pageIndex, false, json.msg);
						if(flag){
							$('#responsePanel').panel('close');
						} else {
							$('#response').panel('close');
						}
						isSign = false;
					} else {
						// 去除遮罩
						if(flag){
							// 調取消保存遮罩
							commonCancelSaveLoading('editDlg');
						} else {
							$.unblockUI();
						}
						$("#deleteCaseSuppliesLinkIds").val("");
						$("#deleteCaseAssetLinkIds").val("");
						$("#selectSn").val("");
						//失敗提示
						$.messager.alert('提示訊息',json.msg,'');
						//$("#dgResponse-msg").text(json.msg);
					}
				},
				error : function(){
					// 去除遮罩
					if(flag){
						// 調取消保存遮罩
						commonCancelSaveLoading('editDlg');
					} else {
						$.unblockUI();
					}
					//$('#' + datagridId).text("頁面檢核失敗");							
				}	
			});
		}
		
	}
	/**
	 * 裝機-派工-租賃授權取消
	 */
	function cancelConfirmAuthorizes(flag, contextPath) {
		$("#dgResponse-msg").text("");
		var param = new Object();
		var caseIds = "";
		var updatedDateString = "";
		var selectRow = $("#dg").datagrid("getSelections");
		if (checkAuthorizesValidate(selectRow, true)) {
			//主頁面 存放異動時間
			for (var i = 0; i<selectRow.length; i++) {
				if(selectRow.length >=1 && i == selectRow.length -1 ){
					caseIds = caseIds + selectRow[i].caseId;
					updatedDateString = updatedDateString + selectRow[i].updatedDate+";"+ selectRow[i].caseId;
				} else {
					caseIds = caseIds + selectRow[i].caseId + ",";
					updatedDateString = updatedDateString + selectRow[i].updatedDate + ";" + selectRow[i].caseId + ",";
				}
			}
			param.caseId = caseIds;
			param.updatedDateString = updatedDateString;
			var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			// 加遮罩
			if(flag){
				// 調保存遮罩
				commonSaveLoading('editDlg');
			} else {
				$.blockUI(blockStyle);
			}
			$.ajax({
				url : contextPath + "/caseHandle.do?actionId=cancelConfirmAuthorizes",
				data : param,
				type : 'post',
				cache : false, 
				dataType :'json',
				success : function(json) {
					var pageIndex = getGridCurrentPagerIndex("dg");
					var msg;
					if (json.success) {
						$("#deleteCaseSuppliesLinkIds").val("");
						$("#deleteCaseAssetLinkIds").val("");
						$("#selectSn").val("");
						//成功提示
						if (json.cmsResult != undefined && !json.cmsResult) {
							$.messager.alert('提示訊息',json.msg,'');
						} else {
							$("#dgResponse-msg").text(json.msg);
						}
						query(pageIndex, false, json.msg);
						if(flag){
							$('#responsePanel').panel('close');
						} else {
							$('#response').panel('close');
						}
						isSign = false;
					} else {
						// 去除遮罩
						if(flag){
							// 調取消保存遮罩
							commonCancelSaveLoading('editDlg');
						} else {
							$.unblockUI();
						}
						$("#deleteCaseSuppliesLinkIds").val("");
						$("#deleteCaseAssetLinkIds").val("");
						$("#selectSn").val("");
						//失敗提示
						$.messager.alert('提示訊息',json.msg,'');
						//$("#dgResponse-msg").text(json.msg);
					}
				},
				error : function(){
					// 去除遮罩
					if(flag){
						// 調取消保存遮罩
						commonCancelSaveLoading('editDlg');
					} else {
						$.unblockUI();
					}
					//$('#' + datagridId).text("頁面檢核失敗");							
				}	
			});
		}
		
	}
	/**
	 * 核檢所選資料是否可進行授權確認
	 */
	
	function checkAuthorizesValidate(rows, isCancel) {
		if(!rows || rows == null ||rows.length == 0){
			$.messager.alert('提示訊息','請勾選資料','warning');
			return false; 
		}
		//第一個案件類別
		var tempCaseCategory = rows[0].caseCategory;
		var tempCaseStatus = rows[0].caseStatus;
		//核檢案件狀態是否一致
		for (var i = 0; i<rows.length; i++) {
			if ((tempCaseCategory != rows[i].caseCategory) || (tempCaseStatus != rows[i].caseStatus)) {
				$.messager.alert('提示訊息','請選擇同一案件狀態、案件類別的案件','warning');
				return false;
			}
		}
		//核檢狀態是否爲代派工
		if (tempCaseStatus != 'WaitDispatch') {
			if (isCancel) {
				$.messager.alert('提示訊息','案件狀態不符，不可進行租賃授權取消','warning');
			} else {
				$.messager.alert('提示訊息','案件狀態不符，不可進行授權確認','warning');
			}
			return false;
		}
		if (!isCancel) {
			for (var i = 0; i<rows.length; i++) {
				if (rows[i].caseCategory == "INSTALL" && rows[i].installType == "4" && rows[i].confirmAuthorizes == 'N') {
					continue;
				} else if (rows[i].caseCategory != "INSTALL"){
					$.messager.alert('提示訊息','案件編號' + rows[i].caseId +'，不爲裝機件，無需授權確認','warning');
					return false;
				} else if (rows[i].installType != "4"){
					$.messager.alert('提示訊息','案件編號' + rows[i].caseId +'，非雲端租賃設備，無需授權確認','warning');
					return false;
				} else if (rows[i].confirmAuthorizes == 'Y') {
					$.messager.alert('提示訊息','案件編號' + rows[i].caseId +'，雲端租賃設備，已完成授權確認','warning');
					return false;
				}
			}
		} else {
			for (var i = 0; i<rows.length; i++) {
				if (rows[i].caseCategory == "INSTALL" && rows[i].installType == "4" && rows[i].confirmAuthorizes == 'Y') {
					continue;
				} else if (rows[i].caseCategory != "INSTALL"){
					$.messager.alert('提示訊息','案件編號' + rows[i].caseId +'，不是CyberEDC裝機案件無法租賃授權取消','warning');
					return false;
				} else if (rows[i].installType != "4"){
					$.messager.alert('提示訊息','案件編號' + rows[i].caseId +'，非雲端租賃設備，無需租賃授權取消','warning');
					return false;
				} else if (rows[i].confirmAuthorizes == 'N') {
					$.messager.alert('提示訊息','案件編號' + rows[i].caseId +'，CyberEDC裝機件未授權成功，無法取消','warning');
					return false;
				}
			}
		}
		return true;
	}
	/*
	* 目的：簽收和線上排除時候 展開耗材鏈接的datagrid
	*querySuppliesData：查詢參數
	*datagridId：datagrid的Id
	*isExpandRow：是否展開行的時候觸發（判斷是主頁面還是處理頁面，主頁面傳入True）
	*customerId：選中行的客戶id
	*index：選中行的index
	*/
	function expandSuppliesLinkDatagrid(querySuppliesData, datagridId, isExpandRow, customerId, index, contextPath){
		var suppliesColumns = [];
		//var suppliesLinkDatagridId = suppliesLinkDatagrid.attr("id");
		suppliesColumns = addSuppliesLinkColumns(customerId,datagridId);
		var caseSuppliesLinkOptions = {
			url : contextPath+"/caseHandle.do?actionId=queryCaseSuppliesLink",
			queryParams : querySuppliesData,
			title: "耗材設定連結",
			width: 900,
			singleSelect: true,
			selectOnCheck: true,
			rownumbers: true,
			loadMsg: '',
			onDblClickCell: function(indexSupplies,field,value) {
				onDblClickCellCaseSuppliesLink(datagridId,indexSupplies,field,value,isExpandRow);
			},
			onClickRow: function(indexSupplies,field,value) {
				onClickRow(datagridId,indexSupplies,field,value);
			},
			toolbar: [{
				iconCls: 'icon-add',
					//新增耗材鏈接行
					handler: function () { appendCaseSupplies(datagridId, index, isExpandRow); }
				}, '-', {
					//刪除耗材鏈接行
					iconCls: 'icon-remove',
					handler: function () { removeCaseSuppliesLink(datagridId, index); }
				}],
			columns: [suppliesColumns],
			onLoadSuccess: function (data) {
				if(isExpandRow) {
					setTimeout(function () {
						$.each($('#dg').datagrid('getRows'), function (i, row) {
								$('#dg').datagrid('fixRowHeight', i);
						});
						$('#dg').datagrid('fixDetailRowHeight', index);
					}, 0);
				}
			}
		}
		if(isExpandRow) {
			caseSuppliesLinkOptions.onResize = function () {
				$('#dg').datagrid('fixDetailRowHeight', index);
			};
		}
		caseSuppliesLinkOptions.onEndEdit = function(indexSupplies, row) {
			//結束編輯耗材行
			onEndEditCaseSuppliesLink(datagridId,indexSupplies, row);
		};
		return caseSuppliesLinkOptions;
	}
/*簽收、線上排除  使用方法--------------------------------------------------------end*/
	
	
    /*
    * 得到所有的column
    */
    function getAllColumnOptions(){
    	var allColumnOptions = [
    		{field:'customerName',width:180,sortable:true,halign:'center',align:'left',title:"客戶"},
    		{field:'caseStatusName',width:90,sortable:true,halign:'center',align:'left',title:"案件狀態"},
    		{field:'caseTypeName',width:100,sortable:true,halign:'center',align:'left',title:"案件類型"},
    		{field:'responseCondition',width:50,halign:'center',align:'center',formatter:function(value,row,index){return dealSlaSetHourFormatter(value,row,index,'response');},title:"回應"},
    		{field:'arriveCondition',width:50,halign:'center',align:'center',formatter:function(value,row,index){return dealSlaSetHourFormatter(value,row,index,'arrive');},title:"到場"},
    		{field:'completeCondition',width:50,halign:'center',align:'center',formatter:function(value,row,index){return dealSlaSetHourFormatter(value,row,index,'complete');},title:"完修"},
    		{field:'responseStatus',width:80,sortable:true,halign:'center',align:'left',formatter:showStatusFormatter,hidden:true,title:"回應狀態"},
    		{field:'arriveStatus',width:80,sortable:true,halign:'center',align:'left',formatter:showStatusFormatter,hidden:true,title:"到場狀態"},
    		{field:'completeStatus',width:80,sortable:true,halign:'center',align:'left',formatter:showStatusFormatter,hidden:true,title:"完修狀態"},
    		{field:'installTypeName',width:120,sortable:true,halign:'center',align:'left',hidden:true,title:"裝機類型"},
    		{field:'uninstallTypeName',width:120,sortable:true,halign:'center',align:'left',hidden:true,title:"拆機類型"},
    		{field:'caseCategoryName',width:80,sortable:true,halign:'center',align:'left',title:"案件類別"},
    		{field:'requirementNo',width:160,sortable:true,halign:'center',align:'left',hidden:true,title:"需求單號"},
    		{field:'caseId',width:180,sortable:true,halign:'center',align:'left',title:"案件編號"},
    		{field:'contractCode',width:150,sortable:true,halign:'center',align:'left',hidden:true,title:"案件合約編號"},
    		{field:'edcTypeContract',width:150,sortable:true,halign:'center',align:'left',hidden:true,title:"設備合約編號"},
    		{field:'isProject',width:60,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo,hidden:true,title:"專案"},
    		{field:'projectCode',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"專案代號"},
    		{field:'projectName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"專案名稱"},

    		{field:'companyName',width:240,sortable:true,halign:'center',align:'left',title:"派工廠商"},
    		{field:'vendorName',width:150,sortable:true,halign:'center',align:'left',hidden:true,title:"廠商名稱"},
    		{field:'departmentName',width:150,sortable:true,halign:'center',align:'left',hidden:true,title:"派工部門"},
    		{field:'vendorStaff',width:150,sortable:true,halign:'center',align:'left',hidden:true,title:"廠商人員"},
    		{field:'sameInstalled',width:140,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo,hidden:true,title:"是否同裝機作業"},
    		{field:'attendanceTimes',width:90,sortable:true,halign:'center',align:'right',title:"到場次數"},
    		{field:'dtid',width:120,sortable:true,halign:'center',align:'left',title:"DTID"},
    		{field:'tid',width:120,sortable:true,halign:'center',align:'left',title:"TID"},
    		{field:'merchantCode',width:140,sortable:true,halign:'center',align:'left',title:"特店代號"},
    		{field:'merchantName',width:160,sortable:true,halign:'center',align:'left',title:"特店名稱"},
    		{field:'headerName',width:160,sortable:true,halign:'center',align:'left',title:"表頭（同對外名稱）"},
    		{field:'oldMerchantCode',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"舊特店代號"},
    		{field:'areaName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"特店區域"},
    		{field:'contact',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯絡人"},
    		{field:'contactTel',width:180,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯絡人電話1"},
    		{field:'contactTel2',width:180,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯絡人電話2"},
    		{field:'phone',width:180,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯絡人行動電話"},
    		{field:'contactEmail',width:220,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯絡人Email"},
    		{field:'businessHours',width:120,sortable:true,halign:'center',align:'left',hidden:true,title:"特店營業時間"},

    		{field:'aoName',width:90,sortable:true,halign:'center',align:'left',title:"AO人員"},
    		{field:'aoemail',width:260,sortable:true,halign:'center',align:'left',hidden:true,title:"AO EMAIL"},
    		{field:'caseArea',width:250,sortable:true,halign:'center',align:'left',hidden:true,title:"案件區域"},
    		{field:'businessAddress',width:250,sortable:true,halign:'center',align:'left',hidden:true,title:"特店營業地址"},
    		{field:'installedAdress',width:250,sortable:true,halign:'center',align:'left',hidden:true,title:"特店裝機地址"},
    		{field:'contactAddress',width:250,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯繫地址"},
    		{field:'installedContact',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"特店裝機聯絡人"},
    		{field:'contactUser',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯繫聯絡人"},
    		{field:'installedContactPhone',width:160,sortable:true,halign:'center',align:'left',hidden:true,title:"特店裝機電話"},
    		{field:'contactUserPhone',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯繫聯絡人電話"},
    		
    		{field:'installedContactMobilePhone',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"特店裝機聯絡人手機"},
    		{field:'contactMobilePhone',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯繫聯絡人手機"},
    		{field:'installedContactEmail',width:220,sortable:true,halign:'center',align:'left',hidden:true,title:"特店裝機聯絡人Email"},
    		{field:'contactUserEmail',width:220,sortable:true,halign:'center',align:'left',hidden:true,title:"特店聯繫聯絡人Email"},

    		{field:'edcTypeName',width:140,sortable:true,halign:'center',align:'left',title:"刷卡機型"},
    		{field:'edcSerialNumber',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"設備序號"},
    		{field:'enableDate',width:130,sortable:true,halign:'center',align:'center',formatter:formaterTimeStampToyyyyMMDD,hidden:true,title:"設備啟用日"},
    		{field:'wareHouseName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"倉別"},
    		{field:'openFunctionName',width:190,sortable:true,halign:'center',align:'left',hidden:true,title:"設備開啟的功能"},
    		{field:'applicationName',width:220,sortable:true,halign:'center',align:'left',hidden:true,title:"程式名稱"},
    		{field:'multiModuleName',width:140,sortable:true,halign:'center',align:'left',title:"雙模組模式"},
    		{field:'ecrConnection',width:90,sortable:true,halign:'center',align:'left',hidden:true,formatter:ecrConnectionFormatter,title:"ECR連線"},

    		{field:'networkLineNumber',width:120,sortable:true,halign:'center',align:'left',hidden:true,title:"網路線"},

    		{field:'peripheralsName',width:130,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備1"},
    		{field:'peripheralsFunctionName',width:190,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備1功能"},
    		{field:'peripheralsSerialNumber',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備1序號"},
    		{field:'peripheralsContract',width:180,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備1合約編號"},
    		{field:'peripherals2Name',width:130,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備2"},
    		{field:'peripheralsFunction2Name',width:190,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備2功能"},
    		{field:'peripherals2SerialNumber',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備2序號"},
    		{field:'peripherals2Contract',width:180,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備2合約編號"},
    		{field:'peripherals3Name',width:130,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備3"},
    		{field:'peripheralsFunction3Name',width:190,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備3功能"},
    		{field:'peripherals3SerialNumber',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備3序號"},
    		{field:'peripherals3Contract',width:180,sortable:true,halign:'center',align:'left',hidden:true,title:"週邊設備3合約編號"},
    		{field:'connectionTypeName',width:170,sortable:true,halign:'center',align:'left',hidden:true,title:"連線方式"},

    		{field:'logoStyle',width:150,sortable:true,halign:'center',align:'left',formatter:fomatterLogoStyle,hidden:true,title:"LOGO"},
    		{field:'isOpenEncrypt',width:150,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo,hidden:true,title:"是否開啟加密"},
    		{field:'electronicPayPlatform',width:150,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo,hidden:true,title:"電子化繳費平台"},
    		{field:'electronicInvoice',width:150,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo,hidden:true,title:"電子發票載具"},
    		{field:'cupQuickPass',width:100,sortable:true,halign:'center',align:'left',formatter:fomatterYesOrNo,hidden:true,title:"銀聯閃付"},
    		{field:'netVendorName',width:110,sortable:true,halign:'center',align:'left',hidden:true,title:"寬頻連線"},
    		{field:'localhostIp',width:130,sortable:true,halign:'center',align:'left',hidden:true,title:"本機IP"},
    		{field:'netmask',width:130,sortable:true,halign:'center',align:'left',hidden:true,title:"Netmask"},
    		{field:'gateway',width:130,sortable:true,halign:'center',align:'left',hidden:true,title:"Getway"},
    		{field:'description',width:260,sortable:true,halign:'center',align:'left',hidden:true,title:"其他說明"},
    		{field:'merchantCodeOther',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"MID2"},

    		{field:'cupTransType',width:90,sortable:true,halign:'center',align:'left',hidden:true,title:"CUP交易"},
    		{field:'dccTransType',width:90,sortable:true,halign:'center',align:'left',hidden:true,title:"DCC交易"},
    		{field:'aeMid',width:110,sortable:true,halign:'center',align:'left',hidden:true,title:"AE-MID"},
    		{field:'aeTid',width:110,sortable:true,halign:'center',align:'left',hidden:true,title:"AE-TID"},

    		{field:'tmsParamDesc',width:260,sortable:true,halign:'center',align:'left',hidden:true,title:"TMS參數說明"},
    		{field:'updatedDescription',width:360,sortable:false,halign:'center',align:'left',title:"異動說明"},
    		{field:'repairReasonName',width:240,sortable:true,halign:'center',align:'left',hidden:true,title:"報修原因"},
    		{field:'problemReasonCategoryName',width:160,sortable:true,halign:'center',align:'left',hidden:true,title:"報修問題原因分類"},
    		{field:'problemReasonName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"報修問題原因"},
    		{field:'problemSolutionCategoryName',width:160,sortable:true,halign:'center',align:'left',hidden:true,title:"報修解決方式分類"},
    		{field:'problemSolutionName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"報修解決方式"},
    		{field:'responsibityName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"是否為責任報修"},
    		{field:'acceptableResponseDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"應回應時間"},
    		{field:'acceptableArriveDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"應到場時間"},
    		{field:'acceptableFinishDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"應完修時間"},
    		{field:'dispatchUserName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行派工人員"},
    		{field:'dispatchDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"派工時間"},
    		{field:'responseUserName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行回應人員"},
    		{field:'responseDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"回應時間"},
    		{field:'arriveUserName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行到場人員"},
    		{field:'arriveDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"到場時間"},
    		{field:'completeUserName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行完修人員"},
    		{field:'completeDepartmentName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行完修部門"},
    		
    		{field:'completeDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"完修時間"},
    		{field:'analyzeUserName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行簽收人員"},
    		{field:'analyzeDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"簽收時間"},
    		{field:'closeUserName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"執行結案人員"},
    		{field:'closeDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,hidden:true,title:"結案時間"},

    		{field:'processTypeName',width:140,sortable:true,halign:'center',align:'left',hidden:true,title:"處理方式"},
    		{field:'dispatchProcessUsername',width:140,sortable:true,halign:'center',align:'left',title:"目前處理人"},
    		
    		{field:'logisticsVendor',width:120,sortable:true,halign:'center',align:'left',hidden:true,title:"物流廠商"},
    		{field:'logisticsNumber',width:120,sortable:true,halign:'center',align:'left',title:"物流編號"},
    		{field:'receiptType',width:120,sortable:true,halign:'center',align:'left',title:"Receipt_type"},
    		
    		{field:'updatedByName',width:140,sortable:true,halign:'center',align:'left',title:"最後異動人"},
    		{field:'updatedDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,title:"最後異動日期"},
    		{field:'createdByName',width:140,sortable:true,halign:'center',align:'left',title:"進件人員"},
    		{field:'createdDate',width:190,sortable:true,halign:'center',align:'center',formatter:formatToTimeStamp,title:"進件日期"},
    		{field:'firstDescription',width:360,sortable:false,halign:'center',align:'left',title:"最近第一次記錄", formatter:wrapFormatter},
    		{field:'secondDescription',width:360,sortable:false,halign:'center',align:'left',title:"最近第二次記錄", formatter:wrapFormatter},
    		{field:'thirdDescription',width:360,sortable:false,halign:'center',align:'left',title:"最近第三次記錄", formatter:wrapFormatter},
    		{field:'hasDelay',width:140,sortable:false,halign:'center',align:'left',title:"是否執行過延期", formatter:formatHaveOrNot},
    		{field:'confirmAuthorizes',width:190,sortable:true,halign:'center',align:'center',hidden:true,title:"授權確認"},
    		{field:'cmsCase',width:190,sortable:true,halign:'center',align:'center',hidden:true,title:"CMS案件"},
    		];
    	return allColumnOptions;
    }