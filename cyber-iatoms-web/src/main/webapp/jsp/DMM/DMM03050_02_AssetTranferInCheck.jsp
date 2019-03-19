<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
AssetTransInfoFormDTO formDTO = null;
	String messager = "";
	String ucNo = "";
	if (ctx != null) {
		if(message != null){
	messager = i18NUtil.getName(message.getCode(),message.getArguments(),null);
		}
		formDTO = (AssetTransInfoFormDTO) ctx.getResponseResult();
		if(formDTO != null){
	ucNo = formDTO.getUseCaseNo();
		}
	}
	String userId = logonUser.getId();
	List<Parameter> assetTransIdList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, AssetTransInfoFormDTO.PARAM_LIST_CHECK_TRANST_INFO);
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="ucNo" value="<%=ucNo%>" scope="page"></c:set>
<c:set var="messager" value="<%=messager%>" scope="page"></c:set>
<c:set var="assetTransIdList" value="<%=assetTransIdList%>" scope="page"></c:set>
<c:set var="userId" value="<%=userId%>" scope="page"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>設備品項維護</title>
<%@include file="/jsp/common/easyui-common.jsp"%>
</head>
<body>
	        <div>
				<div>
					<span id="changeIntoMsg" class="red"></span>
				</div>
	        	<form action="dmmCheckTransInfo.do" id="saveForm" name="saveForm" method="post">
	        	<input type="hidden" id="option" name="option" value=""/>
	        	<input type="hidden" id="serviceId" name="serviceId" value=""/>
	        	<input type="hidden" id="actionId" name="actionId" value=""/>
	        	<input type="hidden" id="useCaseNo" name="useCaseNo" value=""/>
	        	<input type="hidden" id="refuseReason" name="refuseReason" value=""/>
	        	<table cellpadding="6" style="width: auto; height: auto">
	            	<tr>
	                	<td style="width: 70px">轉倉批號:</td>
	                    <td style="width: 150px">
	                    	<cafe:droplisttag 
	                    		name="queryAssetTransId"
	                    		id="queryAssetTransId"
	                    		css="easyui-combobox"
	                    		blankName="請選擇"
	                    		defaultValue=""
	                    		hasBlankValue="true"
	                    		result="${assetTransIdList}"
	                    		style="width:200px"
	                    		javascript="editable=false onchange=queryData('this.value')"
	                    		></cafe:droplisttag>
	                    </td>
	                    <td style="width: 60px">轉出倉:</td>
	                    <td style="width: 150px;text-align: left;word-wrap:break-word;word-break:break-all;" id="fromWareHouse"></td>
	                    <td style="width: 60px">轉入倉:</td>
	                    <td style="text-align: left;word-wrap:break-word;word-break:break-all;" id="toWareHouse"></td>
	                </tr>
	                <tr>
	                	<td style="width: 65px;">說明:</td>
	                    <td colspan="8" style="text-align: left;word-wrap:break-word;word-break:break-all;" id="generalComment"></td>
	                </tr>
				</table>
				</form>
				<div><span id="msgAction" class="red"></span></div>
				<div><span style="padding:9px">設備序號:</span><input id="queryNumber" name="queryNumber" class="easyui-textbox" maxlength="50" type="text" data-options="validType:'maxLength[50]'"value=""></input>
					<input id="hideCodeGunSerialNumbers" type="hidden"></<input></div>
					<input id="isSelectedAll" name="isSelectedAll" type="hidden" />
					<br>
				<div style="display:inline;float: left;"><a href="javascript:void(0)" class="easyui-linkbutton" style="width: 90px" id="btnExpOut" onclick="exportAssetCheck()">列印出貨單</a>
					<input id='queryAll' name='queryAll' type='checkbox'></input> <span style='color: black;'>批量處理 </span>
				</div>
				<div style="display:inline;float: right;">
					<a href="javascript:void(0)" class="easyui-linkbutton" style="width: 100px" id="btnBack" onclick="confirm('isBack')">轉倉退回</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" style="width: 100px" id="btnTransDone" onclick="confirm('isTransDone')">確認入庫</a>
					<a href="javascript:void(0)" id="btnExport" style="width: 150px" onclick="exportData()">匯出</a>
				</div>
				<table id="dataGridOut" >
				</table>
				<div id="refuseDlg" class="easyui-dialog" closed="true">
					<span id="errorMsg" class="red" style="font-weight: normal;"></span>
					<textarea id="rrArea" name="rrArea" class="easyui-textbox" style="width: 100%;height: 100%" data-options="multiline:true, validType:['maxLength[200]']" maxlength="200" ></textarea> 
				</div>
			</div>
	</div>
<script type="text/javascript">
	var isCheckButtomEvent = false;
	var isOpenBlockStyleCheck = true;
	function textBoxSetting1() {	
		var textObj = $("#rrArea");
		textObj.textbox('textbox').bind('blur', function(e){
			textObj.textbox('setValue', $.trim($(this).val()));
		});
		var maxlength = textObj.attr('maxlength');
		if (maxlength) {
			textObj.textbox('textbox').attr('maxlength',maxlength);
		}
	}
	$(function(){
		//批量處理按鈕全選 #3405
	   	$("#queryAll").click(function(){
		   	if($(":checkbox[name=queryAll]").is(":checked")) {
		   		$("#isSelectedAll").val("1");
		   		$('#dataGridOut').datagrid('selectAll');
		   	} else {
		   		$("#isSelectedAll").val("0");
		   		$('#dataGridOut').datagrid('unselectAll');
		   	}
	   	});
		//CR #2703  如果全選按鈕取消選中，將值從隱藏域中去除
		$('#dataGridOut').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).click(function(){
			if(!$('#dataGridOut').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).is(":checked")) {
				rows = $("#dataGridOut").datagrid("getRows");
				for(var i=0;i<rows.length;i++){
					if ($("#hideCodeGunSerialNumbers").val() == "**"+rows[i].serialNumber+"**") {
					   $("#hideCodeGunSerialNumbers").val("");
				   } else if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+rows[i].serialNumber+"**")>=0) {
					   $("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replace(',**'+rows[i].serialNumber+"**",''));
				   } else if($("#hideCodeGunSerialNumbers").val().indexOf("**"+rows[i].serialNumber+'**,')>=0) {
					   $("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replace("**"+rows[i].serialNumber+'**,',''));
				   }
				}
			}
		}); 
		$('#dataGridOut').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).click(function(){
			if($('#dataGridOut').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).is(":checked")) {
				rows = $("#dataGridOut").datagrid("getRows");
				for(var i=0;i<rows.length;i++){
					//CR #2703  如果隱藏欄位為空，則向隱藏域複值
					if($("#hideCodeGunSerialNumbers").val() == ''){
						//拼接 ** 是爲了防止驗證重複時候出現錯誤
						$("#hideCodeGunSerialNumbers").val("**"+rows[i].serialNumber+"**");
					} else {
						//如果隱藏域已存在 則不寫入隱藏域  不提示  
						if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+rows[i].serialNumber+"**")>=0
								|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+rows[i].serialNumber+'**,')>=0
								|| $("#hideCodeGunSerialNumbers").val() == "**"+rows[i].serialNumber+"**"){
							return false;
						//如果不存在則寫入隱藏域
						} else {
							$("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val() + ",**" + rows[i].serialNumber+"**");
						}
					}
				}
			}
		}); 
		//頁面加載后100做disableButton
		setTimeout(disableButton,100);
		// 查詢datagrid初始化
		setTimeout("initAssetInDatagrid();",5);
	});

		/*
		 *列印出庫單 Task #3083
		 */
		function exportAssetCheck(){
			$("#message").empty();
			//轉倉批號 
			var assetTransId = $('#queryAssetTransId').combobox("getValue");
			var rowLength = getGridRowsCount("dataGridOut");
			if (rowLength >= 1) {
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle);
				
				
				window.location.href='${contextPath}/<%=IAtomsConstants.ASSET_TRANS_INFO%>?actionId=exportAsset&assetTransId='+assetTransId;
				
				ajaxService.getExportFlag('${ucNo}',function(data){
					$.unblockUI();
				});
			}
		}
	/*
	*初始化查詢datagrid2
	*/
	function initAssetInDatagrid(){
		var grid = $("#dataGridOut");
		if(!grid.hasClass("easyui-datagrid")){
			grid.attr("style", "width: 100%; height: auto;");
			// 查詢列
			var datagridColumns = [
				{field:'v',width:80,align:'center',halign:'center',sortable:true,checkbox:true},
				{field:'serialNumber',width:160,align:'left',halign:'center',sortable:true,title:"設備序號"},
				{field:'name',width:200,align:'left',halign:'center',sortable:true,title:"設備名稱"},
				{field:'isEnableChar',width:100,align:'center',halign:'center',sortable:true,formatter:function(value,row){if('<%=IAtomsConstants.YES%>'== row.isEnabled){return '<input type=checkbox checked  disabled/>';}else{ return '<input type=checkbox disabled/>';}},title:"已啟用"},
				{field:'isCupChar',width:140,align:'center',halign:'center',sortable:true,formatter:function(value,row){if('<%=IAtomsConstants.YES%>'== row.isCup){return '<input type=checkbox checked  disabled/>';}else{ return '<input type=checkbox disabled/>';}},title:"銀聯"},
				{field:'comment',width:160,align:'left',halign:'center',sortable:true,title:"轉倉說明"},
				{field:'contractCode',width:180,align:'left',halign:'center',sortable:true,title:"合約編號"},
				{field:'assetUser',width:160,halign:'center',sortable:true,hidden:true,title:""},
				{field:'assetUserName',width:160,halign:'center',sortable:true,title:"使用人"},
				{field:'updatedByName',width:180,align:'left',halign:'center',sortable:true,title:"異動人員"},
				{field:'updatedDate',align:'center',width:180,halign:'center',sortable:true,formatter:formatToTimeStamp,title:"異動日期"}
			];
			// 創建datagrid
			grid.datagrid({
				columns:[datagridColumns],
	            rownumbers:true,
	            pagination:true,
	            pageNumber:0,
	         	pageList:[15,30,50,100],
				pageSize:15,
	            fitColumns:false,
	            collapsible:true,
				iconCls: 'icon-edit',
				singleSelect: true,
				nowrap:false,
				singleSelect: false,
	        	sortOrder:'asc'
			});
		}
	}
	
	//禁用確認退回、確認入庫按鈕
	function disableButton(){
		$('#btnBack').linkbutton('disable');
		$('#btnTransDone').linkbutton('disable');
		$('#btnExport').attr("onclick","return false;");
		$('#btnExport').attr("style","color:gray");
		//Task #3083
		$('#btnExpOut').linkbutton('disable');
	}
	//當改變下拉框的值后會重新進行查詢
	$("#queryAssetTransId").combobox({
		onChange: function(newValue){
			if($(":checkbox[name=queryAll]").is(":checked")) {
			   	$("#isSelectedAll").val("0");
			   	$('#queryAll').prop('checked',false);
			}
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			if (isOpenBlockStyleCheck) {
				$.blockUI(blockStyle1);
			}
			
			if (!isCheckButtomEvent) {
				$("#changeIntoMsg").empty();
			}
			isCheckButtomEvent = false;
			if(isEmpty(newValue)){
				//CR #2703  
				$('#queryNumber').textbox('setValue','');
				$('#queryNumber').textbox('disable');
				//清空轉出倉和轉入倉 
				$('#fromWareHouse').text('');
				$('#toWareHouse').text('');
				$('#generalComment').text('');
				//清空轉倉清單 
				queryAssetTransList('', false);
			} else {
				//CR #2703  
				$('#queryNumber').textbox('enable');
				//選擇轉倉批號,根據批號顯示頁面信息 
				ajaxService.getAssetTransInfoByAssetTransId(newValue, function(data){
					if(data){
						//依次將查詢的數據放入對應的Label
						$("#fromWareHouse").text(data.fromWarehouseName);
						$("#toWareHouse").text(data.toWarehouseName);
						$("#generalComment").html(data.comment.replaceAll("\n","<br>").replaceAll(" ","&nbsp"));
						//根據轉倉批號查詢到轉倉清單中的信息 
						queryAssetTransList(newValue, false);
					}else{
						$.messager.alert('提示', "error", 'error');
					}
				});
			}
			if (isOpenBlockStyleCheck) {
				$.unblockUI();
			} else {
				isOpenBlockStyleCheck = true;
			}
			//CR #2703 轉倉批號改變 隱藏域清空 
			$("#hideCodeGunSerialNumbers").val("");
			//CR #2703 掃碼槍 2017/11/01
			checkSerialNumber();
		}
	});
	//確認操作(確認轉倉/確認退回)
	function confirm(option){
		$("#changeIntoMsg").text("");
		var queryAssetTransId = $("#queryAssetTransId").combobox('getValue');
		var msg = "";
		//轉倉退回
		if(option == 'isBack'){
			$("#option").val(option);
			comfirmCommon('確認退回?', function(){
				//清空錯誤信息
				$("#errorMsg").text("");
				//清空div
				$("#rrArea").textbox('setValue','');
				//退回原因
				$('#refuseDlg').dialog({
					title : "退回原因",
					width : 400,
					height : 300,
					closed : false,
					cache : false,
					modal : true,
					onOpen : function(){
						//$("#rrarea").val().substring(0, 200);
						textBoxSetting1();
					},
					buttons : [{
						text : "確認",
						width : 70,
						iconCls : 'icon-ok',
						handler : function(){
							
							$("#errorMsg").text("");
							//獲取輸入的退回原因的值
							var reason = $("#rrArea").val();
							if(reason == ''){
								$("#errorMsg").text("請輸入退回原因");
							} else if(reason.length > 200){
								$("#errorMsg").text("退回原因輸入有誤，請重新輸入");
							} else {
								isOpenBlockStyleCheck = false;
								var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
								$('#refuseDlg').closest('div.panel').block(blockStyle1);
								//將輸入的退回原因的值放入隱藏域
								$("#refuseReason").val(reason);
								//發請求
								$.ajax({
									url : "${contextPath}/dmmCheckTransInfo.do?actionId=<%=IAtomsConstants.ACTION_SAVE_TRANFER_CHECK%>",
									data:{queryAssetTransId:queryAssetTransId,option:$("#option").val(),refuseReason:reason},
									type:'post', 
									cache:false, 
									dataType:'json', 
									success: function(data) {
										if (data.success) {
											isCheckButtomEvent = true;
											$("#changeIntoMsg").text(data.msg);
											$('#refuseDlg').dialog('close');
											//清空轉倉清單 
											queryAssetTransList('', true);
											//調用service的方法獲取轉倉批號需要的值
											updateAssetTransId();
											//重新加載"轉出"頁面的轉倉批號數據 
											ajaxService.getAssetTransIdList('assetTranferOut','${userId}', function(data) {
												$('#<%=DmmAssetTransInfoDTO.ATTRIBUTE.ASSET_TRANS_ID.getValue()%>').combobox('loadData', initSelect(data,"新增批號"));
											});
										} else {
											$('#refuseDlg').dialog('close');
											$("#message").html(result.msg);
										}
										$('#refuseDlg').closest('div.panel').unblock();
									},
									error:function(){
										$.messager.alert('提示', '轉倉退回失敗', 'error');
										$('#refuseDlg').closest('div.panel').unblock();
									}
								});
							}
						}
					},
					{
						text : "取消",
						width : 70,
						iconCls : 'icon-cancel',
						handler : function(){
							$('#refuseDlg').dialog("close");
						}
					}]
				}).window("center");
			});
		}else{
			$("#option").val(option);
			//CR #2703  判斷是否勾選資料 
			if ($("#hideCodeGunSerialNumbers").val() == null || $("#hideCodeGunSerialNumbers").val() == '') {
				alertPromptCommon('請勾選資料', false, function(){
				});
				return false;
			} else {
				comfirmCommon('確認入庫?', function(){
					isOpenBlockStyleCheck = false;
					var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
					$.blockUI(blockStyle1);
					$.ajax({
						url : "${contextPath}/dmmCheckTransInfo.do?actionId=<%=IAtomsConstants.ACTION_SAVE_TRANFER_CHECK%>",
						data:{queryAssetTransId:queryAssetTransId,option:$("#option").val(),serialNumbers:$("#hideCodeGunSerialNumbers").val().replaceAll("**",""),isSelectedAll:$("#isSelectedAll").val()},
						type:'post', 
						cache:false, 
						dataType:'json', 
						success:function(data){
							if (data.success) {
								isCheckButtomEvent = true;
								//清空轉倉清單 
								queryAssetTransList('', false);
								//調用service的方法獲取轉倉批號需要的值
								updateAssetTransId();
								$("#changeIntoMsg").text(data.msg);
							} else {
								$('#refuseDlg').dialog('close');
								$("#changeIntoMsg").html(data.msg);
							}
							$.unblockUI();
						},
						error:function(){
							$.unblockUI();
							$.messager.alert('提示', '確認入庫失敗', 'error');
						}
					});
				})
			}
		}
	}
	//匯出
	function exportData(){
		$("#changeIntoMsg").text("");
		var queryAssetTransId = $("#queryAssetTransId").combobox('getValue');
		if(queryAssetTransId == null || queryAssetTransId == ""){
			$.messager.alert('警告','請先進行查詢！','error');
			return false;
		}
		var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
		$.blockUI(blockStyle);
		
		window.location.href='${contextPath}/dmmCheckTransInfo.do?actionId=<%=IAtomsConstants.ACTION_EXPORT%>&isHistory=false&assetTransId='+queryAssetTransId;
		
		ajaxService.getExportFlag('${ucNo}',function(data){
			$.unblockUI();
		});
	}
	
	//查詢
	function queryAssetTransList(queryAssetTransId, isBack){
		var ignoreFirstLoad = isBack;
		//顯示轉倉清單 
		var options = {
				url : "${contextPath}/dmmCheckTransInfo.do?actionId=queryTransInfo",
				queryParams :{
					assetTransId : queryAssetTransId
				},
				pageNumber:1,
				onUnselect : function (index, row){
				   if ($("#hideCodeGunSerialNumbers").val() == "**"+row.serialNumber+"**") {
					   $("#hideCodeGunSerialNumbers").val("");
				   } else if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+row.serialNumber+"**")>=0) {
					   $("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replace(',**'+row.serialNumber+"**",''));
				   } else if($("#hideCodeGunSerialNumbers").val().indexOf("**"+row.serialNumber+'**,')>=0) {
					   $("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replace("**"+row.serialNumber+'**,',''));
				   }
				   if($(":checkbox[name=queryAll]").is(":checked")) {
					   	$("#isSelectedAll").val("0");
					   	$('#queryAll').prop('checked',false);
					}
				},
				onSelect : function (index, row){
					//CR #2703  如果隱藏欄位為空，則向隱藏域複值
					if($("#hideCodeGunSerialNumbers").val() == ''){
						//拼接 ** 是爲了防止驗證重複時候出現錯誤
						$("#hideCodeGunSerialNumbers").val("**"+row.serialNumber+"**");
					} else {
						//如果隱藏域已存在 則不寫入隱藏域  不提示  
						if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+row.serialNumber+"**")>=0
								|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+row.serialNumber+'**,')>=0
								|| $("#hideCodeGunSerialNumbers").val() == "**"+row.serialNumber+"**"){
							
						//如果不存在則寫入隱藏域
						} else {
							$("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val() + ",**" + row.serialNumber+"**");
						}
					}
				},
				onSelectAll : function (rows){
					for(var i=0;i<rows.length;i++){
						//如果隱藏欄位為空，則設置不存在時的提示消息，向隱藏域複值
						if($("#hideCodeGunSerialNumbers").val() == ''){
							//拼接 ** 是爲了防止驗證重複時候出現錯誤
							$("#hideCodeGunSerialNumbers").val("**"+rows[i].serialNumber+"**");
						} else {
							//如果隱藏域已存在 則不寫入隱藏域  不提示  
							if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+rows[i].serialNumber+"**")>=0
									|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+rows[i].serialNumber+'**,')>=0
									|| $("#hideCodeGunSerialNumbers").val() == "**"+rows[i].serialNumber+"**"){
								
							//如果不存在則寫入隱藏域
							} else {
								$("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val() + ",**" + rows[i].serialNumber+"**");
							}
						}
					}
				},
				
				onLoadSuccess:function(data){
					//查詢到的該批號具有設備信息數
					var length = data.rows.length;
					//有設備信息時啟用按鈕和超鏈
					if(length > 0){
						$('#btnBack').linkbutton('enable');
						$('#btnTransDone').linkbutton('enable');
						//Task #3083
						$('#btnExpOut').linkbutton('enable');
						//啟用匯出
						$('#btnExport').attr("style","color:blue");
						$('#btnExport').attr("onclick","exportData()");
						//CR #2703 掃碼槍 如果隱藏域里存在該頁的設備序號，則勾選該行  2017/11/01
						var rows = $("#dataGridOut").datagrid("getRows");
						
						 //CR #2703  如果全選按鈕取消選中，則批量操作按鈕取消選中
						$('#dataGridOut').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).click(function(){
							if($(":checkbox[name=queryAll]").is(":checked")) {
							   	$("#isSelectedAll").val("0");
							   	$('#queryAll').prop('checked',false);
							}
							if(!$('#dataGridOut').parent().find("div .datagrid-header-check").children("input[type='checkbox']").eq(0).is(":checked")) {
								rows = $("#dataGridOut").datagrid("getRows");
								for(var i=0;i<rows.length;i++){
									if ($("#hideCodeGunSerialNumbers").val() == "**"+rows[i].serialNumber+"**") {
									   $("#hideCodeGunSerialNumbers").val("");
								   } else if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+rows[i].serialNumber+"**")>=0) {
									   $("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replace(',**'+rows[i].serialNumber+"**",''));
								   } else if($("#hideCodeGunSerialNumbers").val().indexOf("**"+rows[i].serialNumber+'**,')>=0) {
									   $("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val().replace("**"+rows[i].serialNumber+'**,',''));
								   }
								}
							}
						}); 
						//CR #2703  將隱藏域中有的值勾選 
						for(var i=0;i<rows.length;i++){
	   						if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+rows[i].serialNumber+"**")>=0
					   						|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+rows[i].serialNumber+'**,')>=0
					   						|| $("#hideCodeGunSerialNumbers").val() == "**"+rows[i].serialNumber+"**"){
   							  $("#dataGridOut").datagrid('selectRow',i);  
   						    }
   					    }
						if($("#isSelectedAll").val()=='1'){
					   		$('#dataGridOut').datagrid('selectAll');
						}else if($("#isSelectedAll").val()=='0'){
							$('#dataGridOut').datagrid('unselectAll');
						}
					} else {
						//禁用按鈕和超鏈
						$('#btnBack').linkbutton('disable');
						$('#btnTransDone').linkbutton('disable');
						//Task #3083
						$('#btnExpOut').linkbutton('disable');
						$('#btnExport').attr("style","color:gray");
						$('#btnExport').attr("onclick","return false;");
					}
				},
				onLoadError : function() {
					$.messager.alert('提示','查詢轉倉清單出錯','error');
				}
			}
		// 清空點選排序
		options.sortName = '';
		options.ignoreFirstLoad = !ignoreFirstLoad;
		openDlgGridQuery("dataGridOut", options);
		options.ignoreFirstLoad = false;
	}
	//操作后改變下拉框的值
	function updateAssetTransId(){
		isCheckButtomEvent = true;
		//調用service的方法獲取轉倉批號需要的值
		ajaxService.getAssetTransIdList('assetTranferinCheck','${userId}', function(data){
			if(data){
				data = initSelect(data,"請選擇");
				//清除之前下拉框的值
				$("#queryAssetTransId").combobox('clear');
				//將後台得到的值賦值進相應的ID
				$("#queryAssetTransId").combobox({
					data:data,
					valueField:'value',
					textField:'name'
				});
			}
		});
	}
	//CR #2703  掃碼槍掃描如果該設備序號存在則勾選 反之 提示消息 
	function checkSerialNumber(){
		$('#queryNumber').textbox('textbox').focus();
		$("#msgAction").html("");
		$("#queryNumber").textbox("textbox").keydown(function(event){
	   		//如果該輸入框聚焦
	   		if($("#queryNumber").textbox("textbox").is(":focus")){
	   			//如果按下tab鍵
	   			if (event.keyCode == 9 || event.keyCode == 13) {
	   				setTimeout(function () {
	   				//如果有逗號，或為空，則不是掃碼槍
	   				if($("#queryNumber").textbox("getValue")!=null 
	   							&& $("#queryNumber").textbox("getValue")!='' 
	   	   						&& $("#queryNumber").textbox("getValue").indexOf(',')<0
	   	   						&& $("#queryNumber").textbox("getValue").indexOf('，')<0){	
	   					ajaxService.checkSerialNumber($("#queryAssetTransId").combobox('getValue'),$("#queryNumber").textbox("getValue"), function(result){
	   					   if (result != null){
	   						   $("#msgAction").html("");
		   					   var rows = $("#dataGridOut").datagrid("getRows"); 
		   					   for(var i=0;i<rows.length;i++){
		   						   if(result==rows[i].serialNumber.toUpperCase() || result==rows[i].serialNumber.toLowerCase() ){
		   							$("#dataGridOut").datagrid('selectRow',i);
		   						   }
		   					   }
		   						//如果隱藏欄位為空，則設置不存在時的提示消息，向隱藏域複值
			   					if($("#hideCodeGunSerialNumbers").val() == ''){
			   						//拼接 ** 是爲了防止驗證重複時候出現錯誤
				   					$("#hideCodeGunSerialNumbers").val("**"+result+"**");
				   				} else {
				   					//如果隱藏域已存在 則不寫入隱藏域  不提示  
					   				if($("#hideCodeGunSerialNumbers").val().indexOf(',**'+result.toUpperCase()+"**")>=0
					   						|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+result.toUpperCase()+'**,')>=0
					   						|| $("#hideCodeGunSerialNumbers").val() == "**"+result.toUpperCase()+"**"
					   						||$("#hideCodeGunSerialNumbers").val().indexOf(',**'+result.toLowerCase()+"**")>=0
					   						|| $("#hideCodeGunSerialNumbers").val().indexOf("**"+result.toLowerCase()+'**,')>=0
					   						|| $("#hideCodeGunSerialNumbers").val() == "**"+result.toLowerCase()+"**"){
					   					return false;
					   				//如果不存在則寫入隱藏域
					   				} else {
				   						$("#hideCodeGunSerialNumbers").val($("#hideCodeGunSerialNumbers").val() + ",**" + result+"**");
					   				}
				   				}
	   					   } else {
	   						   $("#msgAction").html("此設備序號不存在");
	   						   
	   					   }
	   				    });
	   					
 						$("#queryNumber").textbox("setValue","");
						$('#queryNumber').textbox('textbox').focus();
						
	   				} else {
	   					$('#queryNumber').textbox('textbox').focus();
	   				}
					}, 100);
	         	}
	   		} 	 
		});
	}
	
</script>
</body>
</html>