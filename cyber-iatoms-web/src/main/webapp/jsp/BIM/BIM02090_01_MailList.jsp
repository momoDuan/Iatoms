<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.MailListManageFormDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	MailListManageFormDTO formDTO = null;
	if(ctx != null) {
		formDTO = (MailListManageFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new MailListManageFormDTO();
	}
	String useCaseNo = formDTO.getUseCaseNo();
	//通知種類集合
	List<Parameter> listMailGroup = (List<Parameter>)SessionHelper.getAttribute(request, useCaseNo, IAtomsConstants.MAIL_GROUP);
	//JSON格式的通知種類
	String stringMailGroup = (String)SessionHelper.getAttribute(request, useCaseNo, MailListManageFormDTO.PARAM_MAIL_GROUP_STRING);
	//當前登錄者名稱
 	String userName = logonUser.getName();
 	//獲取當前登錄者中文名稱
	if(StringUtils.hasText(userName) && userName.indexOf("(") >0 && userName.indexOf(")") > 0){
		userName = userName.substring(userName.indexOf("(") + 1, userName.indexOf(")"));
	}
%>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="userName" value="<%=userName%>" scope="page"></c:set>
<c:set var="listMailGroup" value="<%=listMailGroup%>" scope="page"></c:set>
<c:set var="stringMailGroup" value="<%=stringMailGroup%>" scope="page"></c:set>

<html>
<head>
	<%@include file="/jsp/common/easyui-common.jsp"%>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<!-- 	<div region="center" style="width: 98%; height: auto;"> -->
<div id="mailListDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height topSoller">
		<!-- <form id="formMail" method="post" novalidate> -->
		<table id="dataGrid" class="easyui-datagrid" title="電子郵件群組維護" style="width: 98%; height: auto"
			data-options="
				rownumbers:true,
				pagination:true,
				iconCls: 'icon-edit',
				singleSelect: true,
				method: 'get',
				pageList:[15,30,50,100],
				pageSize:15,
				onBeforeEdit:commonOnBeforeEdit,
				onCancelEdit:commonOnCancelEdit,
				onEndEdit:commonOnEndEdit,
				onDblClickRow:onDbClickRow,
				pageNumber:0,
				sortOrder:'asc',
				nowrap:false,
				toolbar:'#tb'
				">
	</div>
	 <thead>
        <tr>
             <th data-options="field:'mailGroup',width:200,sortable:true,halign:'center',
						formatter:function(value,row){
							return row.mailGroupName;
						},
						editor:{
							type:'combobox',
								options:{
									valueField:'value',
									textField:'name',
									required:true,
									editable:false,
									methord:'get',
									validType:['ignore[\'請選擇\']'],
									data:playMailGroup,
									panelHeight:'auto',
									invalidMessage:'請輸入通知種類'
								}
						}">通知種類</th>
             <th data-options="field:'name',halign:'center',width:300,sortable:true,editor:{type:'textbox'}">姓名</th>
             <th data-options="field:'email',halign:'center',width:311,sortable:true,required:true,editor:{
	             																							type:'textbox',
	             																							options:{
	             																							validType:[
	             																							'manyEmail[\'Mail格式有誤，請重新輸入\']'],
																											required:true,
																											missingMessage:'請輸入Mail'
																											}
																										},formatter:formatMail">Mail</th>
             <th data-options="field:'updatedByName',halign:'center',sortable:true,width:150,">異動人員</th>
             <th data-options="field:'updatedDate',halign:'center',width:190,align:'center',sortable:true,formatter:formatToTimeStamp">異動時間</th>
         </tr>
     </thead>
      <div id="tb" style="padding: 2px 5px;">
      <form id="formMail" method="post" novalidate>
            通知種類:
      			<cafe:droplisttag css="easyui-combobox"
					id="<%=MailListManageFormDTO.QUERY_NOTICE_TYPE %>"
					name="<%=MailListManageFormDTO.QUERY_NOTICE_TYPE %>" 
					style="width: 170px"
					blankName="請選擇" hasBlankValue="true" result="${listMailGroup }"
					javascript="editable=false panelheight=\"auto\"" 
					>
				</cafe:droplisttag>
       		姓名:
       		<input id="<%=MailListManageFormDTO.QUERY_NAME%>" name="<%=MailListManageFormDTO.QUERY_NAME%>" maxLength = "50" class="easyui-textbox" validType="maxLength[50]"/>&nbsp;
	        <a href="javascript:void(0)" id="btnQuery" class="easyui-linkbutton" iconcls="icon-search" onclick="queryData(1,'true');">查詢</a>&nbsp;
			<a href="#" class="easyui-linkbutton" id="btnAdd"  data-options="iconCls:'icon-add'" onclick="appendRow()"　>新增</a>&nbsp;
			<a href="javascript:void(0)" id="btnDelete" class="easyui-linkbutton" onclick = "deleteMail();" data-options="iconCls:'icon-remove'">刪除</a>&nbsp;
			<a href="javascript:void(0)" id="btnSave" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="save()">儲存</a>&nbsp;
			<a href="javascript:void(0)" id="btnCancel" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="reject()">取消</a>
			<div style="color: red">
				<span id="msg" class="red"></span>
			</div>
			</form>
     </div>
		</table> 
		<!-- </form> -->
	<script type="text/javascript">
	//编辑行的下拉列表的值，ＪＳＯＮ格式
	var playMailGroup = initSelect(<%=stringMailGroup%>);
	/**
	* 编辑行的行标
	*/
	var editIndex = undefined;
	/**
	* 得到查詢參數	
	*/
	function getQueryParam(){
		var querParam = {
			actionId : "<%=IAtomsConstants.ACTION_QUERY%>",
			queryNoticeType : $("#<%=MailListManageFormDTO.QUERY_NOTICE_TYPE%>").combobox('getValue'),
			queryName : $("#<%=MailListManageFormDTO.QUERY_NAME%>").textbox('getValue')
		};
		return querParam;
	}
	
	/**
	* 查詢
	*/
	function queryData(pageIndex, isHidden) {
		$("div.topSoller").unbind("scroll.validate");
		var queryParam = getQueryParam();
		var ignoreFirstLoad = isHidden;
		var options = {
			url : "${contextPath}/mailList.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
			queryParams :queryParam,
			pageNumber:pageIndex,
			method:'POST',
			onLoadSuccess:function(data){
				if (isHidden) {
					$("#msg").text("");
				}
				if (data.total == 0) {
					if (isHidden) {
						$("#msg").text(data.msg);
					}
				}
				isHidden = true;
				//分页，跳轉前結束編輯
				editIndex = undefined;
			},
			onBeforeEdit : commonOnBeforeEdit,
			onEndEdit : commonOnEndEdit,
			onCancelEdit : commonOnCancelEdit,
			onLoadError : function() {
				$.messager.alert('提示','查詢電子郵件羣組維護資料出錯','error');
				//分页，跳轉前結束編輯
				editIndex = undefined;
			}
		};
		// 清空之前点选點選排序
		if(isHidden){
			// 若初始化直接使用datagrid的sortName进行排序的请再次赋初值
			options.sortName = null;
		}
		options.ignoreFirstLoad = ignoreFirstLoad;
		rowEditGridQuery("dataGrid", options, 'msg');
		options.ignoreFirstLoad = true;
	}
	/**
	* 儲存電子郵件羣組維護方法
	*/
	function save() {
		$("div.topSoller").unbind("scroll.validate");
		var controls = ['mailGroup','name','email'];
		if(!validateFormInRow('dataGrid', editIndex, controls)) {
			return;
		}
		$("div.topSoller").unbind("scroll.validate");
		$("#dataGrid").datagrid('endEdit',editIndex);
	//	var form = $("#formMail");
		//得到新增或修改的行的数据
		var mailListRowJson = $("#dataGrid").datagrid('getSelected');
		var id = mailListRowJson.mailId;
		if (mailListRowJson != null) {
			//驗證格式
			var validDataGrid = $("#dataGrid").datagrid('validateRow',editIndex);
			//獲得需要保存的數據
		//	if (form.form("validate")) {
				var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle1);
				$.ajax({
					url: "${contextPath}/mailList.do?actionId=save",
					type:'post', 
					data:mailListRowJson,
					cache:false, 
					dataType:'json',
					async:false, 
					success:function(json){ 
						if (json.success) {
							var pageIndex = getGridCurrentPagerIndex("dataGrid");
							if (pageIndex < 1) {
								pageIndex = 1;
							}
							queryData(pageIndex,false);
							$("#msg").text(json.msg);
						} else {
							$("#msg").text(json.msg);
							$('#dataGrid').datagrid('selectRow', editIndex).datagrid(
												'beginEdit', editIndex);
							if (id != undefined) {
								var mailGroup = $('#dataGrid').datagrid('getEditor', {index:editIndex,field:'mailGroup'});
								$(mailGroup.target).combobox('disable');
							}
							handleScrollTop('mailListDiv');
						}
						$.unblockUI();
					},
					error:function(){
						$.messager.alert('提示', "保存失敗,請聯繫管理員", 'error');
						$('#dataGrid').datagrid('selectRow', editIndex).datagrid(
												'beginEdit', editIndex);
						if (id != undefined) {
							var mailGroup = $('#dataGrid').datagrid('getEditor', {index:editIndex,field:'mailGroup'});
							$(mailGroup.target).combobox('disable');
						}
						$.unblockUI();
					}
				});
		//	}
		} else {
			$("#dataGrid").datagrid('beginEdit',editIndex);
			$.messager.alert('提示', "無操作數據", 'warning');	
		}
	}
	/**
	* 刪除電子郵件羣組維護
	*/
	function deleteMail() {
		$("div.topSoller").unbind("scroll.validate");
		if (editIndex || editIndex == 0) {
			$.messager.alert('提示', '請先儲存再進行操作!', 'warning');
		} else {
			var row = $('#dataGrid').datagrid('getSelected');
			if (row) {
				$("#msg").text("");
				var params = {
						mailId:row.mailId,
						actionId:'<%=IAtomsConstants.ACTION_DELETE%>'
				};
				var url = '${contextPath}/mailList.do';
				var successFunction = function(json) {
					if (json.success) {
						$("#dataGrid").datagrid("clearSelections");
						//計算要顯示的頁碼
						var pageIndex = calDeletePagerIndex("dataGrid");
						queryData(pageIndex,false);
						$("#msg").text(json.msg);	
					}else{
						$("#msg").text(json.msg);
					}
				};
				var errorFunction = function(){
					$("#msg").text("刪除失敗");
				};
				commonDelete(params,url,successFunction,errorFunction);
			}else {
				$.messager.alert('提示','請點選要操作的記錄!','warning');
				return false;
			}
		}
	}
	/**
	* 结束编辑事件
	*/
	function endEditing(){
		$("div.topSoller").unbind("scroll.validate");
		if (editIndex == undefined){return true}
		if ($('#dataGrid').datagrid('validateRow', editIndex)){
			$('#dataGrid').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	* 雙擊變可編輯编辑
	*/
	function onDbClickRow(index){
		$("#msg").empty();
		if (editIndex != undefined) {
		} else {
			if (editIndex != index){
				if (endEditing()){
					$('#dataGrid').datagrid('updateRow', {
						index:index, 
						row:{
							updatedByName:'${userName}',
							updatedDate:new Date()}});
					$('#dataGrid').datagrid('selectRow', index).datagrid('beginEdit', index);
					var mailGroup = $('#dataGrid').datagrid('getEditor', {index:index,field:'mailGroup'});
					checkTextBox(index);
					$(mailGroup.target).combobox('disable');
					editIndex = index;
				} else {
					$('#dataGrid').datagrid('selectRow', editIndex);
				}
			}
		}
	}
	/**
	* 新增一行
	*/
	function appendRow(){
		$("div.topSoller").unbind("scroll.validate");
		$("#msg").empty();
		if (editIndex || editIndex == 0) {
			$.messager.alert('提示', '請先儲存再進行操作!', 'warning');
		} else {
			if ($("#dataGrid").datagrid("options").pageNumber == 0) {
				$('#dataGrid').datagrid({pageNumber:1});
			}
			if (endEditing()){
				editIndex = $('#dataGrid').datagrid('getRows').length;
				if (editIndex == 0) {
					var addInfoList = [];
					var addInfo = new Object();
					addInfo.mailGroup = '';
					addInfo.name = '';
					addInfo.updatedByName = '${userName}';
					addInfo.updatedDate = new Date();
					addInfoList.push(addInfo);
					$('#dataGrid').datagrid('loadData',{total:1, rows:addInfoList});
					editIndex = 0;
				} else {
					$('#dataGrid').datagrid('appendRow',{
						mailGroup : "",
						name : "",
						updatedByName:'${userName}',
						updatedDate:new Date()
					});
					editIndex = $('#dataGrid').datagrid('getRows').length - 1;
				}
				$('#dataGrid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
				checkTextBox(editIndex);
				$('#mailListDiv').scrollTop( $('#mailListDiv')[0].scrollHeight );
			}
		}
	}
	/**
	* 取消
	*/
	function reject(){
		$("div.topSoller").unbind("scroll.validate");
		confirmCancel(function(){
			$("#dataGrid").datagrid('clearSelections');
			var rows = $("#dataGrid").datagrid("getRows");
			if ((rows[0].mailId == null || rows[0].mailId ==  "") && rows.length == 1) {
				$('#dataGrid').datagrid('deleteRow', 0);
				$('#dataGrid').datagrid('loadData', {total:0,rows:[]});
			} else {
				$('#dataGrid').datagrid('rejectChanges');
			}
			$("#msg").empty();
			editIndex = undefined;
		});
	}
	
	/**
	* 檢驗textbox框的長度以及去除空格
	*/
	function checkTextBox(index) {
		var mailAddress = $('#dataGrid').datagrid('getEditor', {index:index,field:'email'});
		var name = $('#dataGrid').datagrid('getEditor', {index:index,field:'name'});
		$(mailAddress.target).textbox('textbox').attr('maxlength',255).bind('blur', function(e){
				$(mailAddress.target).textbox('setValue', $.trim($(this).val()));
			});
		$(name.target).textbox('textbox').attr('maxlength',50).bind('blur', function(e){
				$(name.target).textbox('setValue', $.trim($(this).val()));
			});
	}
	//Bug #2774 將英文分號改為中文分號，用來換行 2017/11/07
	function formatMail(val, row, index){
		if(val!=null && val.indexOf(';')>=0){
			val = val.replaceAll(';',"；");
		}
		return val;
	}
	</script>
</body>
</html>