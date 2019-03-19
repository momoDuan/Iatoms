<%--
	交易參數元件
	author：carrieDuan 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/common/taglibs.jsp"%>
<%@page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_CATEGORY" var="caseCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$TRANSACTION_CATEGORY" var="transactionCategoryAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants" var="iAtomsConstants" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_STATUS" var="caseStatusAttr" />
<cafe:constMap path="com.cybersoft4u.xian.iatoms.common.IAtomsConstants$CASE_ACTION" var="caseActionAttr" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*,java.lang.*"%>
<%-- 案件類別 --%>
<tiles:useAttribute id="caseCategory" name="caseCategory" classname="java.lang.String" ignore="true"/>
<%-- 動態顯示列列表 List<SrmTransactionParameterItemDTO> 需要傳入代碼、名稱、形態、長度--%>
<tiles:useAttribute id="transactionParameterItemDTOs" name="transactionParameterItemDTOs" classname="java.util.List" ignore="true"/>
<%-- 案件類別對應的可編輯交易類別下拉列表 List<Parameter> json格式--%>
<tiles:useAttribute id="editabledTradingTypes" name="editabledTradingTypes" classname="java.lang.String" ignore="true"/>
<%-- 交易類別對應的可編輯列 Map<String, List<String>>轉為json格式，key值:交易類型value值， value:交易類型對應的可編輯列的code值 --%>
<tiles:useAttribute id="editabledFields" name="editabledFields" classname="java.lang.String" ignore="true"/>
<%-- 是否可以修改交易參數，工單範本維護頁面調用--%>
<tiles:useAttribute id="hasUpdate" name="hasUpdate" classname="java.lang.String"  ignore="true"/>
<%-- 案件處理DTO  SrmCaseHandleInfoDTO--%>
<tiles:useAttribute id="caseHandleInfoDTO" name="caseHandleInfoDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO" ignore="true" />
<%-- 案件處理FormDTO  CaseManagerFormDTO--%>
<tiles:useAttribute id="caseManagerFormDTO" name="caseManagerFormDTO" classname="com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO" ignore="true"/>

<c:set var="stuff" value="case" scope="page"></c:set>
<c:set var="combobox" value="<%=IAtomsConstants.PARAMTER_ITEM_TYPE_COMBOBOX %>" scope="page"></c:set>
<c:set var="click" value="<%=IAtomsConstants.PARAMTER_ITEM_TYPE_CLICK %>" scope="page"></c:set>
<c:set var="merCode" value="<%=IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE %>" scope="page"></c:set>
<c:set var="merOtherCode" value="<%=IAtomsConstants.PARAMTER_ITEM_CODE_MERCHANT_CODE_OTHER %>" scope="page"></c:set>
<c:set var="tid" value="<%=IAtomsConstants.PARAMTER_ITEM_CODE_TID %>" scope="page"></c:set>
<c:set var="closedStatus" value="<%=IAtomsConstants.CASE_STATUS.CLOSED.getCode() %>" scope="page"></c:set>
<c:set var="voidedStatus" value="<%=IAtomsConstants.CASE_STATUS.VOIDED.getCode() %>" scope="page"></c:set>
<c:set var="immediateCloseStatus" value="<%=IAtomsConstants.CASE_STATUS.IMMEDIATE_CLOSE.getCode() %>" scope="page"></c:set>
<c:set var="waitCloseStatus" value="<%=IAtomsConstants.CASE_STATUS.WAIT_CLOSE.getCode() %>" scope="page"></c:set>

	<div id="trading" class="topSoller">
		<c:if test="${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory
				or caseCategoryAttr.PROJECT.code eq caseCategory}">
			<form id="searchForm" style="margin: 4px 0px 0px 0px">
				<a href="javascript:void(0)" id="btnAppendRow" class="easyui-linkbutton" iconcls="icon-add" onclick="addTranParam();" >新增</a>
				<a href="javascript:void(0)" id="btnDelTrans"   class="easyui-linkbutton" onclick="deleteTranParam();" iconcls="icon-remove" disabled="true">刪除</a>
			</form>
		</c:if>
		<table id="transDataGrid"></table>
		<c:if test="${not empty caseManagerFormDTO }">
			<table style="width: 100%">
				<tr>
					<td width="10%">TMS參數說明: </td>
					<td>
						
						<c:choose>
							<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
								<textarea id="${stuff }_tmsParamDesc" name="${stuff }_tmsParamDesc" 
									maxlength="2000"><c:out value='${caseHandleInfoDTO.tmsParamDesc }'/></textarea>
							</c:when>
							<c:otherwise>
								<textarea
									name="${stuff }_tmsParamDesc_vo" id="${stuff }_tmsParamDesc_vo" disabled><c:out value='${caseHandleInfoDTO.tmsParamDesc }'/></textarea>
								<input type="hidden" id="${stuff }_tmsParamDesc" name="${stuff }_tmsParamDesc" value="<c:out value='${caseHandleInfoDTO.tmsParamDesc }'/>">
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</c:if>
	</div>
	<script type="text/javascript">
	var tradingParamsData;
	function initTradingParameterPage() {
		// panel
		$("#trading").panel({title:'交易參數',width:'99%'});
		var paramDesc;
		<c:choose>
			<c:when test="${caseCategoryAttr.INSTALL.code eq caseCategory or caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}">
				paramDesc = $("#${stuff }_tmsParamDesc");
			</c:when>
			<c:otherwise>
				paramDesc = $("#${stuff }_tmsParamDesc_vo");
			</c:otherwise>
		</c:choose>
		/* if (paramDesc.length == 0) {
			paramDesc = $("#${stuff }_tmsParamDesc_vo");
		} */
		if (paramDesc.length > 0) {
			paramDesc.textbox({multiline:true,width: '500px', height: '120px'});
		}
		var cols = [];
		var col11;
		cols.push({field:'transactionType', width:200 ,halign:'center',align:'left',
			formatter:function(value,row){
				return row.transactionTypeName;
			},
			editor:{
				type:'combobox',
				options:{
					editable:false,
					valueField:'value',
					textField:'name',
					onChange:changeTransactionType,
					required:true,
					validType:'ignore[\'請選擇\']',
					invalidMessage:'請輸入交易類別',
					data:editabledTradingTypes
				}
			},title:"交易類別"});
		<c:forEach var="columns1" items="${transactionParameterItemDTOs }">
			<c:choose>
				<c:when test="${fn:length(columns1.paramterItemName) < 3}">
					<c:set var="accessLen" value="90"/>								
				</c:when>
				<c:when test="${fn:length(columns1.paramterItemName) < 5}">
					<c:set var="accessLen" value="120"/>								
				</c:when>
				<c:otherwise>
					<c:set var="accessLen" value="180"/>
				</c:otherwise>
			</c:choose>		
		 	<c:choose>
		 		<c:when test="${columns1.paramterItemType eq combobox}">
		 		col11 = {field:'${columns1.paramterItemCode }',halign:'center', width:150, align:'left',
						formatter:function(value,row){
							if (value == '請選擇') {
								row['${columns1.paramterItemCode }']='';
								return '';
							}
							return row['${columns1.paramterItemCode }'];
						},
						editor:{
							type:'combobox',
							options:{
								editable:true,
								valueField:'value',
								textField:'name',
								method:'get'
							}
					},styler: function(value,row,index){
						return cellTransStyler(value,row,index,'${columns1.paramterItemCode }');
					},title:"${columns1.paramterItemName }"};
		 		</c:when>
		 		<c:when test="${columns1.paramterItemType eq click }">
		 		col11 = {field:'${columns1.paramterItemCode }',halign:'center',width:<c:out value='${accessLen }'/>,align:'center',formatter:function(value,row,index){
						return settingField(value,row,index,'${columns1.paramterItemCode }');
					},styler: function(value,row,index){
						return cellTransStyler(value,row,index,'${columns1.paramterItemCode }','');
					},title:"${columns1.paramterItemName }"};
				</c:when>
				<c:otherwise>
				col11 = {field:'${columns1.paramterItemCode }',halign:'center',width:<c:out value='${accessLen }'/>,align:'center',formatter:function(value,row,index){
						return settingField(value,row,index,'${columns1.paramterItemCode }');
					},title:"${columns1.paramterItemName }"};
				</c:otherwise>
		 	</c:choose>
		 	cols.push(col11);
		</c:forEach>
		cols.push({field:'transactionParameterId',halign:'center',align:'left',hidden:'true'});
		cols.push({field:'caseId',halign:'center',align:'left',hidden:'true'});
		<c:if test="${empty caseHandleInfoDTO or (not empty caseHandleInfoDTO and caseHandleInfoDTO.haveTransParameter ne 'Y')}">
		tradingParamsData = undefined;
	</c:if>
	<c:if test="${not empty caseHandleInfoDTO and caseHandleInfoDTO.haveTransParameter eq 'Y'}">
		// 處理交易參數值的顯示
		 var transResult = [];
		var tradingParams = ${caseHandleInfoDTO.caseTransactionParameterStr};
		if(tradingParams){
			for (var i=0; i<tradingParams.length; i++) {
				var itemValue;
				itemValue = tradingParams[i].itemValue;
				if (itemValue == null || isEmpty(itemValue)) {
					itemValue = new Object();
				} else {
					itemValue=JSON.parse(itemValue);
				}
				itemValue.transactionType=tradingParams[i].transactionType;
				itemValue.transactionTypeName=tradingParams[i].transactionTypeName;
				itemValue.MID=tradingParams[i].merchantCode;
				itemValue.MID2=tradingParams[i].merchantCodeOther;
				itemValue.TID=tradingParams[i].tid;
				transResult.push(itemValue);
			}
			tradingParamsData = {
				rows:transResult,
				total:tradingParams.length
			}
			if("${caseHandleInfoDTO.isCaseTemplate}" == 'Y') {
			//	$("#transDataGrid").datagrid("loadData", tradingParamsData);
			}
		}
	</c:if>
	$("#transDataGrid").datagrid({
		width: '100%',height: 'auto',
		fitColumns:false,
		border:true,
		pageList:[15,30,50,100],
		pageSize:15,
		pagination:false,
		onEndEdit: onEndEdit,
		singleSelect: true,
		onDblClickRow: onDblClickRow,
		onClickRow: onClickRowtransData,
		onClickCell:onClickCell,
		idField:'transactionParameterId',
		nowrap : false,
		rownumbers:true,
		columns:[cols],
		data:tradingParamsData
	});
	}
	/*
	* 頁面加載完成函數
	*/
	$(function(){
		setTimeout('initTradingParameterPage();',5);
	//	initTradingParameterPage();		
	});
	//编辑行的下拉列表的值，ＪＳＯＮ格式
 	var editabledTradingTypes = initSelect(<%=editabledTradingTypes%>);
	//编辑行的行标
	var editIndex = undefined;
	//記錄當前行不可編輯列
	var editColumns = undefined;
	var isOnDblClickRow=false;
	//記錄當前交易類別下拉框
	var transactionTypes = undefined;
	
	/*
	初始化數據時改變單元格背景顏色。
	value: 需要顯示內容
	row：該行信息
	index:行號
	id：該列的field
	*/
	
	function cellTransStyler(value, row, index, id) {
		//定義只在初始化數據時執行
		if (row.transactionType != "" && editIndex == undefined) {
			/* if (index != rowIndex) {
				rowIndex = index;
				//獲取當前交易類別可編輯列
				var updateColumn = ${editabledFields};
				editColumns=eval(updateColumn[row.transactionType]);
			} */
			getEditField(row, index);
			//如果當前列不再可編輯範圍內，則將單元格背景色變為灰色
			if (editColumns && !editColumns.contains(id)){
				row[id] = "";
				return 'background-color:gray;color:gray';
			} 
		} 
	}
	var rowIndex = undefined;
	function getEditField(row, index){
		if (index != rowIndex) {
			rowIndex = index;
			//獲取當前交易類別可編輯列
			var updateColumn = ${editabledFields};
			editColumns=eval(updateColumn[row.transactionType]);
		}
	}
	/*
	初始化時，為文本框屬性的列增加lable標籤。
	value：需要顯示的值
	row：該行信息
	index:行號
	field：該列的field
	*/
	function settingField(value,row,index,field) {
		getEditField(row, index);
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
	
	/*
	新增一行
	*/
	function addTranParam() {
		if (endEditing()) {
			isOnDblClickRow = false;
			  $('#transDataGrid').datagrid('appendRow',{
				transactionType:"",
				merchantCode:"",
				merchantCodeOther:"",
				tid:""
			}); 
			editIndex = $('#transDataGrid').datagrid('getRows').length-1;
			 $('#transDataGrid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
			//設置除交易類別外，其餘文本框為灰色
			updateCellBackground(false,true);
			//獲取最先交易類別下拉列表
			getTransactionTypes();
			$("#btnDelTrans").linkbutton('enable');
		}
	}
	
	/*
	獲取最新交易類別下拉列表
	*/
	function getTransactionTypes(){
		var data=editabledTradingTypes.clone();
		var rows = $('#transDataGrid').datagrid('getRows');
		var isCommon = false;
		//更新交易類別下拉菜單，去除已經選擇過的
		for (var i = 0; i < rows.length; i++) {
			if (i == editIndex) {
				//當前行不比對
				continue;
			}
			for (var j=0;j<data.length;j++){
				if (rows[i].transactionType == data[j].value) {
					if (rows[i].transactionType == '${transactionCategoryAttr.COMMON_VM.code}'
						|| rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJ.code}'
						|| rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJU.code}') {
						for (var m = 0;m<data.length;m++){
							if (data[m].value == '${transactionCategoryAttr.COMMON_VM.code}'
								|| data[m].value == '${transactionCategoryAttr.COMMON_VMJ.code}'
								|| data[m].value == '${transactionCategoryAttr.COMMON_VMJU.code}') {
								data.splice(m,1);
								m = -1;
							}
						}
						break;
					} else {
						data.splice(j,1);
						break;
					} 
					
				}
			}
		} 
		transactionTypes = data;
		var currentEditor = $('#transDataGrid').datagrid('getEditor', {  
			index : editIndex,  
			field : 'transactionType'
		});
		currentEditor.target.combobox('loadData', data);
	}

	/*
	单击单元格 
	*/
	function onClickCell(index, field, value) {
		if ($("#hideCaseStatus").val() == '${closedStatus}' || $("#hideCaseStatus").val() == '${voidedStatus}'
				|| $("#hideCaseStatus").val() == '${immediateCloseStatus}' || $("#hideCaseStatus").val() == '${waitCloseStatus}') {
			return false;
		}
		var transactionType = undefined;
		var currentEditor = $('#transDataGrid').datagrid('getEditor', {  
			index : index,
			field : 'transactionType'
		});
		if (currentEditor != undefined) {
			transactionType = currentEditor.target.combobox('getValue');
		}
		//未選擇交易類別，則直接返回，不可編輯
		if (transactionType != "" && transactionType != null && editColumns != undefined) {
			if(editIndex != index){
				return;
			}else{
				//如果點擊的單元格為該交易類別的可編輯列，則賦值V/""
				if(editColumns.contains(field)){
					var column = $("#transDataGrid").datagrid("getColumnFields");
					var trs = $("#transDataGrid").prev().find('div.datagrid-body').find('tr');
					for (var i=1;i<column.length;i++){
						if (column[i]==field) {
							var obj = $($($(trs[index].cells[i+1]).prev().find('div')).find('lable'));
							if (obj.html() == "V") {
								obj.html("");
							} else {
								obj.html("V");
							}
							var rows = $('#transDataGrid').datagrid('getRows');
							rows[index][field]=obj.html();
							break;
						}
					}
				}
			}
		}
	}
	
	/*
	雙擊事件，該行變為可編輯狀態
	*/
	function onDblClickRow(index, field) {
		if ($("#hideCaseStatus").val() == '${closedStatus}' || $("#hideCaseStatus").val() == '${voidedStatus}'
			|| $("#hideCaseStatus").val() == '${immediateCloseStatus}' || $("#hideCaseStatus").val() == '${waitCloseStatus}') {
			return false;
		}
		if ((index != editIndex) && endEditing() && ('${hasUpdate}' != "N")) {
			var data=editabledTradingTypes.clone();
			isOnDblClickRow=true;
			$('#transDataGrid').datagrid('selectRow', index);
			//獲取當前選擇行數據
			var updateRow = $("#transDataGrid").datagrid('getSelected');
			var transactionType = updateRow.transactionType;
			var isHave = false;
			for (var j=0;j<data.length;j++){
				if (transactionType == data[j].value) {
					isHave = true;
					editIndex = index;
					$('#transDataGrid').datagrid('selectRow', index).datagrid('beginEdit', index);
					//交易類別變為不可編輯
					var code = $('#transDataGrid').datagrid('getEditor', { 'index': editIndex, field: 'transactionType' });
					$(code.target).combobox('disable');
					isOnDblClickRow=false;
					//獲取當前交易類別可編輯列
					var updateColumn = ${editabledFields};
					editColumns=eval(updateColumn[$(code.target).combobox('getValue')]);
					//欄位增加驗證
					validation($(code.target).combobox('getValue'));
					//加載下拉框數據
					getComboboxValues();
					$("#btnDelTrans").linkbutton('enable');
					$("#btnDelTrans").linkbutton('enable');
				}
			}
			if (!isHave) {
				$("#btnDelTrans").linkbutton('disable');
			}
		}
	}
	
	/*
	單擊事件
	*/
	function onClickRowtransData(index, row) {
		if ($("#hideCaseStatus").val() == '${closedStatus}' || $("#hideCaseStatus").val() == '${voidedStatus}'
				|| $("#hideCaseStatus").val() == '${immediateCloseStatus}' || $("#hideCaseStatus").val() == '${waitCloseStatus}') {
			return false;
		}
		if (editIndex != undefined) {
			$('#transDataGrid').datagrid('selectRow', editIndex);
		} else {
			$('#transDataGrid').datagrid('selectRow', index);
		}
		var data=editabledTradingTypes.clone();
		var updateRow = $("#transDataGrid").datagrid('getSelected');
		var transactionType = updateRow.transactionType;
		var isHave = false;
		for (var j=0;j<data.length;j++){
			if (transactionType == data[j].value) {
				isHave = true;
				break;
			}
		}
		if (!isHave) {
			$("#btnDelTrans").linkbutton('disable');
		} else {
			$("#btnDelTrans").linkbutton('enable');
		}
	}
	
	/*
	结束编辑
	*/
	function endEditing() {
		$("div.topSoller").unbind("scroll.validate");
		if (editIndex == undefined) {
			$("div.topSoller").unbind("scroll.validate");
			return true;
		} else {
			var controls = ['transactionType','${merCode}','${tid}', '${merOtherCode}'];
			if(!validateFormInRow('transDataGrid', editIndex, controls)){
				return;
			} else {
				$("div").unbind("scroll.validate");
			}
			if ($('#transDataGrid').datagrid('validateRow', editIndex)) {
				$('#transDataGrid').datagrid('endEdit', editIndex);
				//將不可編輯列的背景顏色變為灰色
				updateCellBackground(true, false);
				$("#transDataGrid").datagrid('clearSelections');
				editIndex = undefined;
				$("#btnDelTrans").linkbutton('disable');
				return true;
			} else {
				return false;
			}
		}
	}
	
	/*
	當結束編輯時
	*/
	function onEndEdit(index, row) {
		 var transactionType = $(this).datagrid('getEditor', {
			index : index,
			field : 'transactionType'
		});
		row.transactionTypeName = $(transactionType.target).combobox('getText');
	} 
	
	/*
	删除該行交易參數
	*/
	function deleteTranParam(){
		comfirmDelete(function(){
			var row = $("#transDataGrid").datagrid('getSelected');
			var transType = row.transactionType;
			if (row) {
				var rowIndex = $('#transDataGrid').datagrid('getRowIndex',row);
				$('#transDataGrid').datagrid('deleteRow',	rowIndex);
				editIndex = undefined;
			} 
			$("#btnDelTrans").linkbutton('disable');
			var rows = $("#transDataGrid").datagrid('getRows');
			if (rows.length == 0) {
				$("#transDataGrid").datagrid('loadData', {rows:[],total:0});
			}
		});
	}
	
	/*
	欄位增加驗證
	*/
	function validation(value) {
		var trs = $("#transDataGrid").prev().find('div.datagrid-body').find('tr');
		<c:forEach var='column' items='${transactionParameterItemDTOs}' varStatus='status'>
			param = '${column.paramterItemCode}';
			//如果當前欄位可編輯且不是文本框，則增加或者取消驗證
			if (editColumns.contains(param) && '${column.paramterItemType}' != '${click}') {
				var code = $("#transDataGrid").datagrid("getEditor",{index:editIndex,field:param});
				$(code.target).combobox("enable");
				var codeValue=$(code.target).combobox("getValue");
				//如果該欄位數據類型為數字，則增加數字驗證。
				if (${column.paramterDataType eq iAtomsConstants.DTAT_TYPE_NUMBER}) {
					if (${column.paramterItemCode eq tid}) {
						// Task #2518 有輸入 DTID、TID 的地方，原限制為數字，改為限制英數字，會有英文發生
						$(code.target).combobox({required: true,missingMessage:'請輸入${column.paramterItemName}',validType:'englishOrNumber["${column.paramterItemName}限英數字"]'});
					} else {
						$(code.target).combobox({missingMessage:'請輸入${column.paramterItemName}',validType:'englishOrNumber["${column.paramterItemName}限英數字"]'});
					}
					$(code.target).combobox("setValue", codeValue);
				}
				//如果當前欄位類型為text，且欄位為tid或者特點代號,則增加必填屬性。
				if (${column.paramterDataType eq iAtomsConstants.DATA_TYPE_TEXT} && (${column.paramterItemCode eq merCode || column.paramterItemCode eq merOtherCode})) {
					// Task #2518 交易參數中，MID2 改為非必填
					if(${column.paramterItemCode eq merOtherCode}){
						$(code.target).combobox({missingMessage:'請輸入${column.paramterItemName}',validType:'englishOrNumber["${column.paramterItemName}限輸入英數字"]'});
					} else {
						$(code.target).combobox({required: true,missingMessage:'請輸入${column.paramterItemName}',validType:'englishOrNumber["${column.paramterItemName}限輸入英數字"]'});
					}
					$(code.target).combobox("setValue", codeValue);
				}
				textMaxLength(param,'${column.paramterItemLength}','${combobox}');
			} else {
				//取消不可編輯欄位的驗證，並且去除背景顏色以及變為不可編輯狀態
				if ('${column.paramterItemType}' != '${click}' && !isOnDblClickRow) {
					var code = $("#transDataGrid").datagrid("getEditor",{index:editIndex,field:param});
					$(code.target).combobox({required: false});
					$(code.target).combobox('disable');
					trs[editIndex].cells['${status.index+1}'].style.backgroundColor = '';
				}
			}
		</c:forEach>
	}
	
	/*
	設置不可編輯單元格背景顏色
	isEndEditing--是否為結束編輯
	isAllCell--是否需要全部變為不可編輯狀態
	*/
	function updateCellBackground(isEndEditing, isAllCell) {
		var column = $("#transDataGrid").datagrid("getColumnFields");
		var trs = $("#transDataGrid").prev().find('div.datagrid-body').find('tr');
		//如果為結束編輯，則將不可異動欄位背景變為灰色。
		if (isEndEditing) {
			for (var i=1; i<column.length; i++) {
				if (!editColumns.contains(column[i])) {
					trs[editIndex].cells[i].style.backgroundColor = 'gray';
				}
			}
		} else {
			for (var i=1; i<column.length; i++) {
				//如果是新增一行或者所選newValue為空，則將所有文本框背景顏色變灰，下拉框變為不可編輯。
				if (isAllCell?true:!editColumns.contains(column[i])) {
					var editor = $("#transDataGrid").datagrid("getColumnOption",column[i]).editor;
					if (editor != undefined) {
						var code = $('#transDataGrid').datagrid('getEditor', { 'index': editIndex, field: column[i] });
						$(code.target).combobox('disable');
					} else {
						trs[editIndex].cells[i].style.backgroundColor = 'gray';
					} 
				} else {
					//去掉可編輯欄位的背景顏色
					trs[editIndex].cells[i].style.backgroundColor = '';
				}
			}
		}
	}
	
	/*
	交易類別改變事件
	*/
	function changeTransactionType(newValue, oldValue) {
		if (!isOnDblClickRow) {
			if (newValue != "" && newValue != null) {
				//是否由一般交易
				var isCommon = false;
				//如果存在oldValue，則清除該行的所有信息。
				if (oldValue != "") {
					clearCellMsg();
				}
				var rows = $('#transDataGrid').datagrid('getRows');
				//獲取當前交易類別的可編輯列
				var updateColumn = ${editabledFields};
				editColumns=eval(updateColumn[newValue]);
				if (editColumns == undefined) {
					updateCellBackground(false, true);
				} else {
					//將不可編輯文本框變背景變為灰色
					updateCellBackground(false, false);
					//欄位增加驗證
					validation(newValue);
					//重新加載下拉列表
					getComboboxValues();
					var column = $("#transDataGrid").datagrid("getColumnFields");
					var trs = $("#transDataGrid").prev().find('div.datagrid-body').find('tr');
					//將一般交易的交易參數賦值帶入
					var isCommon = (newValue != '${transactionCategoryAttr.COMMON_VM.code}' && newValue!='${transactionCategoryAttr.COMMON_VMJ.code}'
								&& newValue != '${transactionCategoryAttr.COMMON_VMJU.code}');
					for (var i=0;i<rows.length && isCommon;i++) {
						if (rows[i].transactionType == '${transactionCategoryAttr.COMMON_VM.code}'
								|| rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJ.code}'
								|| rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJU.code}') {
							isCommon = true;
							var edit;
							//獲取一般交易的可編輯列
							if (rows[i].transactionType == '${transactionCategoryAttr.COMMON_VM.code}') {
								edit=eval(updateColumn['${transactionCategoryAttr.COMMON_VM.code}']);
							} else if (rows[i].transactionType == '${transactionCategoryAttr.COMMON_VMJ.code}') {
								edit=eval(updateColumn['${transactionCategoryAttr.COMMON_VMJ.code}']);
							} else {
								edit=eval(updateColumn['${transactionCategoryAttr.COMMON_VMJU.code}']);
							}
							//var edit=eval(updateColumn['${transactionCategoryAttr.COMMON.code}']);
							//循環列，找出是否存在交易類型為一般交易的交易參數
							for (var j=1;j<column.length;j++){
								//判斷該欄位是否一般交易與當前交易都可異動
								if (edit.contains(column[j]) && editColumns.contains(column[j])){
									//獲取當前單元格的類型
									var editor = $("#transDataGrid").datagrid("getColumnOption",column[j]).editor;
									//如果是輸入框類型
								 	if (editor != undefined) {
										var currentEditor = $('#transDataGrid').datagrid('getEditor', {  
											index : editIndex,
											field : column[j]
										});
										currentEditor.target.combobox('setValue', rows[i][column[j]]);
									} else {
										//獲取一般交易該單元格下的lable標籤
										var objCommon = $($($(trs[i].cells[j+1]).prev().find('div')).find('lable'));
										//獲取當前單元格下的lable標籤
										var obj = $($($(trs[editIndex].cells[j+1]).prev().find('div')).find('lable'));
										obj.html(objCommon.html());
										rows[editIndex][column[j]]=objCommon.html();
									}
								}
							}
							break;
						}
					}
				}
				isOnDblClickRow = false;
			} else {
				$('#transDataGrid').datagrid('deleteRow',editIndex);
				editIndex = undefined;
				addTranParam();
			} 
		}
	}

	/*
	加載下拉框的值
	*/
	function getComboboxValues() {
		var column = $("#transDataGrid").datagrid("getColumnFields");
		for (var i=1;i<column.length;i++){
			if (editColumns.contains(column[i])){
				var editor = $("#transDataGrid").datagrid("getColumnOption",column[i]).editor;
				if (editor!=undefined && editor.type=="${combobox}") {
					uploadCombobox(column[i]);
				}
			}
		}
	}
	
	/*
	獲取該列的值，放入下拉列表，去除重複。
	field:該列的field
	*/
	function uploadCombobox(field) {
		var rows = $('#transDataGrid').datagrid('getRows');
		var currentEditor = $('#transDataGrid').datagrid('getEditor', {  
			index : editIndex,  
			field : field
		});
		var defaultValue = "";
		var value = currentEditor.target.combobox("getValue");
		if (value != "") {
			defaultValue = value;
		}
		var result = [];
		if (defaultValue == "" && (field == '${merCode}')) {
			defaultValue = $("#${stuff}_merMid").val();
			if (defaultValue != "") {
				result=[{value:$.trim(defaultValue),name:$.trim(defaultValue)}];
			}
		}
		if (currentEditor != undefined) {
			for(var i = 0; i < rows.length; i ++){
				var val = rows[i][field];
				if (val && $.trim(val).length > 0) {				
					var repeate = false;
					for (j = 0; j < result.length; j ++) {
						if (result[j].value == $.trim(val)) {
							repeate = true;
							break;
						}
					}
					if (!repeate) {
						result.push({value:$.trim(val),name:$.trim(val)});
					}
				}
			}
			currentEditor.target.combobox('loadData', result);
			if (defaultValue != "") {
				currentEditor.target.combobox('setValue', defaultValue);
			}
		}
	}
	
	/*
	清除當前可編輯行的信息
	*/
	function clearCellMsg() {
		var rows = $('#transDataGrid').datagrid('getRows');
		var column = $("#transDataGrid").datagrid("getColumnFields");
		var trs = $("#transDataGrid").prev().find('div.datagrid-body').find('tr');
		if (editColumns != undefined) {
			for (var i=1; i<column.length; i++) {
				if (editColumns.contains(column[i])) {
					var editor = $("#transDataGrid").datagrid("getColumnOption",column[i]).editor;
					if (editor != undefined) {
						var code = $('#transDataGrid').datagrid('getEditor', { 'index': editIndex, field: column[i] });
						$(code.target).textbox('setValue', "");
					} else {
						var obj = $($($(trs[editIndex].cells[i+1]).prev().find('div')).find('lable'));
						obj.html("");
						rows[editIndex][column[i]]="";
					} 
				}
			}
		}
	}
	
	/*
	增加欄位的長度驗證
	*/
	function textMaxLength(id, length, boxType){
		var code = $('#transDataGrid').datagrid('getEditor', {'index': editIndex, field: id });
		if (boxType == 'textbox' && code != undefined) {
			$(code.target).textbox('textbox').attr('maxlength',length).bind('blur', function(e){
				$(code.target).textbox('setValue', $.trim($(this).val()));
			});
		}
		if (boxType == '${combobox}' && code != undefined) {
			$(code.target).combobox('textbox').attr('maxlength',length).bind('blur', function(e){
				$(code.target).combobox('setValue', $.trim($(this).val()));
			});
		}
	}
	
	/*
	* 選擇dtid之後，將dtid所對應的交易參數賦值到交易參數模塊。
	*/
	function loadTradingParamElement(result){
		editIndex = undefined;
		//刪除當前已有行
		var row = $("#transDataGrid").datagrid("getRows");
		if (row != undefined) {
			/* var length =row.length; 
			for (var i=0; i<length; i++) {
				$('#transDataGrid').datagrid('deleteRow', 0);
			} */
			$('#transDataGrid').datagrid('loadData', {total:0,rows:[]});
		}
		if (result != null) {
			var tradingParams = result.caseNewTransactionParameterDTOs;
			for (var i=0; i<tradingParams.length; i++) {
				var itemValue;
				itemValue = tradingParams[i].itemValue;
				if (itemValue == null || itemValue == "") {
					itemValue = new Object();
				} else {
					itemValue=JSON.parse(itemValue);
				}
				itemValue.transactionType=tradingParams[i].transactionType;
				itemValue.transactionTypeName=tradingParams[i].transactionTypeName;
				itemValue.MID=tradingParams[i].merchantCode;
				itemValue.MID2=tradingParams[i].merchantCodeOther;
				itemValue.TID=tradingParams[i].tid;
				$("#transDataGrid").datagrid("appendRow",itemValue);
			} 
			
		}
		if (${caseCategoryAttr.UPDATE.code eq caseCategory or caseCategoryAttr.MERGE.code eq caseCategory or caseCategoryAttr.PROJECT.code eq caseCategory}) {
			// 放置TMS參數說明
			$("#${stuff }_tmsParamDesc").textbox("setValue", result==null?"":result.tmsParamDesc);
		} else {
			// 放置TMS參數說明
			$("#${stuff }_tmsParamDesc_vo").textbox("setValue", result==null?"":result.tmsParamDesc);
			$("#${stuff }_tmsParamDesc").val(result==null?"":result.tmsParamDesc);
		}
	}
	/**
	交易元件驗證
	**/
	function tradingParameterValidate(caseCategory) {
		if (endEditing()){
			$("#caseDialogMsg").text("");
			var rows = $("#transDataGrid").datagrid("getRows");
			if(caseCategory == '${caseCategoryAttr.INSTALL.code}' || caseCategory == '${caseCategoryAttr.UPDATE.code}'
					|| caseCategory == '${caseCategoryAttr.PROJECT.code}'){
				if(!rows || rows == null || rows.length == 0){
					$("#caseDialogMsg").text("請至少設定一筆交易參數");
					handleScrollTop('createDlg','caseDialogMsg');
					handleScrollTop('editDlg','caseDialogMsg');
					return false;
				} else {
					var isCommon = false;
					var isRepeat = false;
					var tempTransType = rows[0].transactionType;
					var count = 0;
					for (var i = 0; i < rows.length; i ++) {
						row = rows[i];
						// 判斷是否含有一般交易或建設公司參數
						if((row.transactionType == '${transactionCategoryAttr.COMMON_VM.code}')
						|| (row.transactionType == '${transactionCategoryAttr.COMMON_VMJ.code}')
						|| (row.transactionType == '${transactionCategoryAttr.COMMON_VMJU.code}')
						|| (row.transactionType == '${transactionCategoryAttr.CONSTRUCTION_COMPANY.code}')){
							count ++;
							isCommon = true;
							break;
						}
						// 判斷是否重複
						if(i != 0){
							if(row.transactionType == tempTransType){
								isRepeat = true;
								break;
							}
						}
					}
					// 不含一般交易
					if(!isCommon){
						$("#caseDialogMsg").text("請設定交易類別為“一般交易”的交易參數");
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					} else {
						if(count > 1){
							$("#caseDialogMsg").text("交易類別重複，請重新設定");
							handleScrollTop('createDlg','caseDialogMsg');
							handleScrollTop('editDlg','caseDialogMsg');
							return false;
						}
						return true;
					}
					// 重複
					if(!isRepeat){
						$("#caseDialogMsg").text("交易類別重複，請重新設定");
						handleScrollTop('createDlg','caseDialogMsg');
						handleScrollTop('editDlg','caseDialogMsg');
						return false;
					} else {
						return true;
					}
				}
			} else {
				return true;
			}
		}
	}
	/**
	獲取修改後交易參數列表
	**/
	function getTradingParametersData(){
		if (endEditing()){
			var rows = $("#transDataGrid").datagrid("getRows");
			var column = $("#transDataGrid").datagrid("getColumnFields");
			var transactionParameters = [];
			var parameter;
			var otherParameter;
			var row;
			var fieldName;
			for (var i = 0; i < rows.length; i ++) {
				row = rows[i];
				parameter = {};				
				otherParameter = {};
				for (var j = 0; j < column.length; j ++) {
					fieldName = column[j];
					if (fieldName == 'paramterValueId') {
						parameter.paramterValueId = row.paramterValueId;
					} else if (fieldName == 'caseId') {
						parameter.caseId = row.caseId;
					} else if (fieldName == 'transactionType') {
						parameter.transactionType = row.transactionType;
					} else if (fieldName == '${merCode}') {
						parameter.merchantCode = row[fieldName];
					} else if (fieldName == '${merOtherCode}') {
						parameter.merchantCodeOther = row[fieldName];
					} else if (fieldName == '${tid}') {
						parameter.tid = row[fieldName];
					} else {
						otherParameter[fieldName] = row[fieldName];
					}
					if (j == column.length - 1) {
						parameter.itemValue = JSON.stringify(otherParameter);
					}
				}
				transactionParameters.push(parameter);
			}
			return JSON.stringify(transactionParameters);
		}
	}
	
	</script>