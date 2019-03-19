<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsp/common/common.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.SuppliesTypeFormDTO"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.bean.dto.DmmSuppliesDTO"%>
<html>
<%
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	SuppliesTypeFormDTO formDTO = null;
	String ucNo = null;
	List<DmmSuppliesDTO> listSuppliesMaintenance  = new ArrayList<DmmSuppliesDTO>();
	if (ctx != null) {
		formDTO = (SuppliesTypeFormDTO) ctx.getResponseResult();
	}
	if (formDTO == null) {
		formDTO = new SuppliesTypeFormDTO();
	}else{
		ucNo = formDTO.getUseCaseNo();
	}
	//查詢條件耗材品集合
	List<Parameter> suppliesList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_SUPPLIES_LIST);
	//查詢條件客戶集合
	List<Parameter> customerList = (List<Parameter>) SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	//查詢結果客戶集合
	String customerJson = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_JSON);
	//查詢結果耗材品集合
	String suppliesJson = (String)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_SUPPLIES_JSON);
%>
<c:set var="customerJson" value="<%=customerJson%>" scope="page"></c:set>
<c:set var="suppliesJson" value="<%=suppliesJson%>" scope="page"></c:set>
<c:set var="suppliesList" value="<%=suppliesList%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<head>
<meta charset="UTF-8">
<%@include file="/jsp/common/easyui-common.jsp"%>
<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<!-- 	<div region="center"
		style="width: auto; height: auto; padding: 1px; background: #eee; overflow-y: hidden"> -->
<div id="suppliesDiv" style="width: auto; height: auto; padding: 1px; overflow-y: auto" region="center" class="setting-panel-height topSoller">
		<!-- <form id=formGrid method="post"> -->
		<table id="dataGrid" class="easyui-datagrid" title="耗材品項維護"
			style="width: 98%; height: auto"
			data-options="
				rownumbers:true,
				pagination:true,
				onBeforeEdit:commonOnBeforeEdit,
				onCancelEdit:commonOnCancelEdit,
				onEndEdit:commonOnEndEdit,
				iconCls: 'icon-edit',
				pageList:[15,30,50,100],
				pageSize:15,
				method: 'get',
				singleSelect: true,
				selectOnCheck: true,
				pageNumber:0,
				onDblClickCell :onDblClickCell,
				nowrap:false,
				toolbar:'#tb'">
			<thead>
				<tr>
					<th
						data-options="field:'companyId',width:250,sortable:true,halign:'center',
									formatter:function(value,row){
										return row.customerName;
									},
									editor:{
										type:'combobox',
										options:{
											valueField:'value',
											textField:'name',
											method:'get',
											data:customerJson,
											required:true,
											invalidMessage:'請輸入客戶',
											validType:['requiredDropList'],
											editable:false,
											selectedValue:'${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?logonUser.companyId:'' }',
											${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?'disabled:true':''}
										}
									}">客戶</th>

					<th
						data-options="field:'suppliesType',width:200,sortable:true,halign:'center',
									formatter:function(value,row){
										return row.suppliesCode;
									},
									editor:{
										type:'combobox',
										options:{
											valueField:'value',
											textField:'name',
											method:'get',
											data:suppliesJson,
											required:true,
											invalidMessage:'請輸入耗材分類',
											editable:false,
											validType:['requiredDropList']
										}
									}">耗材分類</th>
					<th  maxlength="50" data-options="field:'suppliesName',width:350,sortable:true,halign:'center',
					editor:{
						type:'textbox',
						options:{
							required:true,
							missingMessage:'請輸入耗材名稱',
							validType:['maxLength[50]']
						}
					}" >耗材名稱</th>
					<th  maxlength="10" data-options="field:'price',width:150,align:'right',sortable:true,halign:'center',
					editor:{
						type:'textbox',
						options:{
							required:true,
							missingMessage:'請輸入單價',
							validType:['numberNonnegative[\'單價限0和正整數，請重新輸入\']','maxLength[10]']
						}
					}" 
					formatter="formatCapacity">單價</th>
				</tr>
			</thead>  
		</table>
		<!-- </form> -->
			<div id="tb" style="padding: 2px 5px;">
				<form id="seachForm">
				客戶: <cafe:droplisttag css="easyui-combobox"
							id="<%=SuppliesTypeFormDTO.QUERY_CUSTOMSER_ID%>"
							name="<%=SuppliesTypeFormDTO.QUERY_CUSTOMSER_ID%>" 
							result="${customerList }"
							blankName="請選擇" 
							hasBlankValue="true"
	               			selectedValue="${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?logonUser.companyId:'' }" 
							style="width: 20%"
							javascript="editable=false ${(formDTO.roleAttribute eq 'CUSTOMER_VENDOR')?'disabled=true':''}"
							>
					 </cafe:droplisttag>
				耗材分類: <cafe:droplisttag css="easyui-combobox"
							id="<%=SuppliesTypeFormDTO.QUERY_SUPPLIES_CODE%>"
							name="<%=SuppliesTypeFormDTO.QUERY_SUPPLIES_CODE%>" 
							result="${suppliesList }"
							blankName="請選擇" 
							hasBlankValue="true"
							selectedValue=""
							style="width: 15%"
							javascript="editable=false">
						 </cafe:droplisttag>
				耗材名稱: <input maxlength="50" class="easyui-textbox" type="text" style="width: 150px" name="<%=SuppliesTypeFormDTO.QUERY_SUPPLIES_NAME%>" id="<%=SuppliesTypeFormDTO.QUERY_SUPPLIES_NAME%>" data-options="validType:['maxLength[50]']"></input>
				<a class="easyui-linkbutton" href="#" id="btnQuery" iconcls="icon-search">查詢</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'" onclick="appendRow()"id="btnAdd">新增</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-remove'" onclick="deleted()"id="btnDelete">刪除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="save()" id="btnSave">儲存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'" onclick="reject()"id="btnCancel">取消</a>
				<div align="left"><span id="msg" class="red"></span></div>
				</form>
			</div>
	</div>
</body>
<script type="text/javascript">
	//耗材品名轉成json
	var suppliesJson = ${suppliesJson };
	//客戶專程json
	var customerJson = ${customerJson };
	//調用initSelect方法拿到耗材品下拉框的請選擇
	suppliesJson = initSelect(suppliesJson);
	//調用initSelect方法拿到客戶下拉框的請選擇
	customerJson = initSelect(customerJson);
	var editIndex = undefined;
	//全局函數，初始化頁面，設為第一頁
	$(function(){
		//var queryCustomerId = $("#queryCustomerId").combobox('getValue');
		//頁面加載 保存按鈕不可用
		$("#btnSave").linkbutton('disable');
		//頁面加載 取消按鈕不可用
		$("#btnCancel").linkbutton('disable');
		//頁面加載 刪除按鈕不可用
		$('#btnDelete').linkbutton('disable');
		$("#btnQuery").click(function(){
			$("#message").empty();
			if (editIndex || editIndex == 0) {
				return false;
			} else {
				/* $.messager.progress({
					title : '提示',
					text : '数据处理中，请稍候....'
				}); */
				query(1,true);
			}
		});
	})
	
	//查詢
	function query(pageIndex,flag){
		var ignoreFirstLoad = flag;
		var queryParam = $("#seachForm").form("getData");
		queryParam.queryCustomerId = $("#queryCustomerId").combobox('getValue');
		var options = {
			url : "${contextPath}/suppliesType.do?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
			pageNumber:pageIndex,
			method:'post',
			//從表單中獲取到查詢的條件
			queryParams :queryParam,
			onLoadError : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示','查詢出錯','error');
			},
			onLoadSuccess:function(data){
				$.messager.progress('close');
				//分頁的時候清空消息
				if (flag) {
					$("#msg").empty();
					$("#msg").text(data.msg);
					editIndex = undefined;
				}
				flag = true;
			/* $("#btnSave").linkbutton('disable');
			$("#btnCancel").linkbutton('disable');
			$("#btnDelete").linkbutton('disable');
			$("#btnAdd").linkbutton('enable');
			$("#btnQuery").linkbutton('enable'); */
			},
			onBeforeEdit : commonOnBeforeEdit,
			onEndEdit : commonOnEndEdit,
			onCancelEdit : commonOnCancelEdit
		};
		// 清空之前点选點選排序
		if(flag){
			// 若初始化直接使用datagrid的sortName进行排序的请再次赋初值
			options.sortName = null;
		}
		options.ignoreFirstLoad = ignoreFirstLoad;
		rowEditGridQuery("dataGrid", options, 'msg');
		options.ignoreFirstLoad = true;
	}
   	//雙擊编辑事件
	function onDblClickCell(index){
		$("#msg").empty();
		//雙擊進入可編輯狀態 保存按鈕可用
		$("#btnSave").linkbutton('enable');
		//雙擊進入可編輯狀態 取消按鈕可用
		$("#btnCancel").linkbutton('enable');
		//雙擊進入可編輯狀態 刪除按鈕不可用
		$("#btnDelete").linkbutton('disable');
		//雙擊進入可編輯狀態 查詢按鈕不可用
		$("#btnQuery").linkbutton('disable');
		//雙擊進入可編輯狀態 新增按鈕不可用
		$("#btnAdd").linkbutton('disable');
		if (editIndex || editIndex==0) {
		}else{
			if (editIndex != index){
				if (endEditing()){
					$('#dataGrid').datagrid('selectRow', index).datagrid('beginEdit', index);
					//拿到這一行裡面的客戶這個單元格(字段名)
					var companyId = $('#dataGrid').datagrid('getEditor',{index:index,field:'companyId'});
					//拿到這一行裡面的耗材品這個單元格(字段名)
					var suppliesName = $('#dataGrid').datagrid('getEditor',{index:index,field:'suppliesName'});
					//耗材單價
					var price = $('#dataGrid').datagrid('getEditor',{index:index,field:'price'});
					//給單價設置長度
					$(price.target).textbox('textbox').attr('maxlength',10);
					//耗材名稱
					$(suppliesName.target).textbox('textbox').attr('maxlength',50);
					//公司不可編輯
					$(companyId.target).combobox('disable');
					editIndex = index;
				} else {
					$('#dataGrid').datagrid('selectRow', editIndex);
					//editIndex = index;
				}
			}
		}
	}
	//結束編輯狀態
	function endEditing(){
		$("div.topSoller").unbind("scroll.validate");
		if (editIndex == undefined) {
			return true;
		}
		//驗證行數據
		if ($('#dataGrid').datagrid('validateRow', editIndex)){
			//驗證通過，結束編輯
			$('#dataGrid').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	//新增行
	function appendRow(){
		$("div.topSoller").unbind("scroll.validate");
		if (editIndex || editIndex == 0) {
			return false;
		} else {
			//新增按鈕 不可用
			$("#btnAdd").linkbutton('disable');
			$("#msg").empty();
			//剛進入頁面新增時，把pagenumber設為1(不會出現序號-9的問題)
			if ($("#dataGrid").datagrid("options").pageNumber == 0) {
					$('#dataGrid').datagrid({pageNumber:1});	
			}
			if (endEditing()) {
				if(${formDTO.roleAttribute eq 'CUSTOMER_VENDOR'}){
					//給客戶和設備類別添加請選擇
					$('#dataGrid').datagrid('appendRow',{companyId:'${logonUser.companyId}',suppliesType:''});
				} else {
					//給客戶和設備類別添加請選擇
					$('#dataGrid').datagrid('appendRow',{companyId:'',suppliesType:''});
				}
				//正在編輯行
				editIndex = $('#dataGrid').datagrid('getRows').length-1;
				//開始編輯狀態
				$('#dataGrid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
				//耗材單價
				var priceObj = $('#dataGrid').datagrid('getEditor',{ 'index': editIndex, field: 'price' });
				$(priceObj.target).textbox('textbox').attr('maxlength',10).bind('blur', function(e){
					$(priceObj.target).textbox('setValue', $.trim($(this).val()));
				});;
				//設備名稱
				var suppliesNameObj = $('#dataGrid').datagrid('getEditor',{'index':editIndex,field:'suppliesName'});
				$(suppliesNameObj.target).textbox('textbox').attr('maxlength',50).bind('blur', function(e){
					$(suppliesNameObj.target).textbox('setValue', $.trim($(this).val()));
				});;
				$('#suppliesDiv').scrollTop( $('#suppliesDiv')[0].scrollHeight );
			}
			/* $("#btnDelete").linkbutton('disable');
			$("#btnSave").linkbutton('enable');
			$("#btnCancel").linkbutton('enable');
			$("#btnQuery").linkbutton('disable'); */
		}
	}
	
	//删除
	function deleted(){
		$("div.topSoller").unbind("scroll.validate");
		//if (editIndex || editIndex == 0) {
		//	$.messager.alert('提示', '請先儲存再進行操作!', 'warning');
		//} else {
			//選中行
			var row = $('#dataGrid').datagrid('getSelected');
			if (row) {
				$("#msg").text("");
				var params = {
					//耗材id
					suppliesId:row.suppliesId,
					actionId:'<%=IAtomsConstants.ACTION_DELETE%>'
				};
				var url = '${contextPath}/suppliesType.do';
				var successFunction = function(json) {
					if (json.success) {
						$("#dataGrid").datagrid("clearSelections");
						//計算要顯示的頁碼
						var pageIndex = calDeletePagerIndex("dataGrid");
						query(pageIndex,false);
						$("#msg").text(json.msg);	
					}else{
						$("#msg").text(json.msg);
					}
				};
				var errorFunction = function(){
					$("#msg").text("刪除失敗");
				};
				commonDelete(params,url,successFunction,errorFunction);
				//deleteMailList(mailId);	
			}else {
				$.messager.alert('提示','請點選要操作的記錄!','warning');
				return false;
			}
		//}
		<%-- var row = $('#dataGrid').datagrid('getSelected');
	 	if (editIndex || editIndex == 0) {
			return false;
		}else{
		    if (row == null) {
				$.messager.alert('提示', '請选中需要操作的記錄!', 'info');
				return false;
			}else{
				$.messager.confirm('確認對話框', '確認刪除?', function (isDeleted) {
					$("#btnAdd").linkbutton('enable');
					if (isDeleted) {
						$("#msg").empty();
						//delete
						$.ajax({  
							url:"${contextPath}/suppliesType.do?actionId=<%=IAtomsConstants.ACTION_DELETE%>&suppliesId="+row.suppliesId,    
							success:function(data){
								$.messager.progress('close');
								//調用查詢方法.
								if(data.success){
									//計算要顯示的頁碼
									var pageIndex = calDeletePagerIndex("dataGrid");
										query(pageIndex,false);
									$("#msg").text(data.msg);
								}else{
									query(pageIndex,false);
									$("#msg").text(data.msg);
								}
							}, 
							error:function(){
								parent.$.messager.progress('close');
								$.messager.alert('提示', "刪除失敗", 'error');
							}
						}); 
					}
				});
			}
		} --%>
	}
	//存儲
	function save(){
		//判斷表單是否驗證成功
		var controls = ['companyId','suppliesType','suppliesName','price'];
/* 		if(!validateFormInRow('dataGrid', editIndex, controls) || !$('#formGrid').form("validate")){
			return;
		} */
		if(!validateFormInRow('dataGrid', editIndex, controls)){
			return;
		}
		$("div.topSoller").unbind("scroll.validate");
		//清空消息
		$("#msg").empty();
		//拿到集合的所有的行
		$("#dataGrid").datagrid('endEdit',editIndex);
		//拿到選中的這一行，是一個對象
		var suppliesListRow = $("#dataGrid").datagrid('getSelected');
		if (suppliesListRow != null) {
			$("#btnSave").linkbutton('disable');
			$("#btnCancel").linkbutton('disable');
			$("#btnAdd").linkbutton('enable');
			var blockStyle1 = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
			$.blockUI(blockStyle1);
			//save 
			$.ajax({
				url: "${contextPath}/suppliesType.do?actionId=<%=IAtomsConstants.ACTION_SAVE%>",
				type : 'post',
				data : suppliesListRow,
				cache : false,
				dataType : 'json',
				async:false, 
				//存儲成功，調取查詢函數
				success : function(data) {
					if (data.success) {
						//當前頁碼
						var pageIndex = getGridCurrentPagerIndex("dataGrid");
						//調用查詢
						query(pageIndex,false);
						//顯示msg
						$("#msg").text(data.msg);
						editIndex = undefined;
					} else {
						$("#msg").text(data.msg);
						//開始編輯狀態
						$('#dataGrid').datagrid('selectRow', editIndex).datagrid(
											'beginEdit', editIndex);
						/* $("#btnAdd").linkbutton('disable');
						$("#btnSave").linkbutton('enable');
						$("#btnCancel").linkbutton('enable'); */
						//如果是修改
						if(suppliesListRow.suppliesId != null){
							//拿到公司
							var companyId = $('#dataGrid').datagrid('getEditor',{index:editIndex,field:'companyId'});
							//設置公司不可編輯
							$(companyId.target).combobox('disable');
						}
						handleScrollTop('suppliesDiv');
					}
					$.unblockUI();
				}
			});
		} else {
			editIndex = undefined;
		}	
	}
	//取消
	function reject() {
		/* if (editIndex || editIndex == 0) {
			$.messager.confirm('確認對話框', '確定取消？', function(isRemove) {
				if (isRemove) {
					$('#dataGrid').datagrid('rejectChanges');
					editIndex = undefined;
					$("#btnSave").linkbutton('disable');
					$("#btnCancel").linkbutton('disable');
					$("#btnAdd").linkbutton('enable');
					$("#btnQuery").linkbutton('enable');
				}
			});
		} */
		$("div.topSoller").unbind("scroll.validate");
		//調公有刪除方法
		confirmCancel(function(){
			$("#dataGrid").datagrid('clearSelections');
			$('#dataGrid').datagrid('rejectChanges');
			$("#msg").empty();
			editIndex = undefined;
		});
	}
	/*  function onClickRow(index) {
		$("#msg").empty();
		if (editIndex != undefined) {
			$('#dataGrid').datagrid('selectRow', editIndex)
		} else {
			$("#btnDelete").linkbutton('disable');
			$("#btnAdd").linkbutton('enable');
			$('#btnDelete').linkbutton('enable');
		}
	} */
</script>

</html>
