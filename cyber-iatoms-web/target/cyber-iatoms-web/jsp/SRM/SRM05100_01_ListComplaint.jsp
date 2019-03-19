<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ComplaintFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ComplaintDTO"%>
<%
	//初始化加載頁面數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ComplaintFormDTO formDTO = null;
	if (ctx != null) {
		formDTO = (ComplaintFormDTO) ctx.getResponseResult();
	} else {
		formDTO = new ComplaintFormDTO();
	}
	
	ComplaintDTO complaintDTO = formDTO.getComplaintDTO();
	if (complaintDTO == null) {
		complaintDTO = new ComplaintDTO();
	}
	
	//客戶下拉菜單
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IAtomsConstants.PARAM_CUSTOMER_LIST);
	
	//維護廠商列表(客訴對象廠商)
	List<Parameter> vendorList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IAtomsConstants.PARAM_VENDOR_LIST);
	
	//問題分類
	List<Parameter> questionTypeList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IATOMS_PARAM_TYPE.QUESTION_TYPE.getCode());
	
	//是否下拉框
	List<Parameter> yesOrNoList = (List<Parameter>)SessionHelper.getAttribute(request, IAtomsConstants.UC_NO_SRM_05100, IAtomsConstants.YES_OR_NO);
	
	//當日日期
	String currentDate = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), "yyyy/MM/dd");
%>

<html>
<c:set var="formDTO" value="<%=formDTO%>" scope="page"></c:set>
<c:set var="complaintDTO" value="<%=complaintDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="vendorList" value="<%=vendorList%>" scope="page"></c:set>
<c:set var="questionTypeList" value="<%=questionTypeList%>" scope="page"></c:set>
<c:set var="yesOrNoList" value="<%=yesOrNoList%>" scope="page"></c:set>
<c:set var="currentDate" value="<%=currentDate%>" scope="page"></c:set>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>客訴管理</title>
	<script type="text/javascript" src="${contextPath}/assets/jquery-easyui-1.4.3/datagrid-detailview.js"></script>
	<script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
	<script type="text/javascript" src="${contextPath}/js/fineuploader-5.0.2.js"></script>
	<link rel="stylesheet" href="${contextPath}/css/fineuploader-5.0.2.css"/>
	<link rel="stylesheet" type="text/css" href="${contextPath}/assets/css/jquery.multiselect2side.css">
</head>
<body>
	<div style="width: 100%;overflow-x:hidden; height: auto; padding: 1px; overflow-y: auto" class="setting-panel-height topSoller">
        <table id="transDataGrid" class="easyui-datagrid" title="客訴管理">
            <thead>
                <tr>
					<!-- <th data-options="field:'check', checkbox:true"></th> -->
                    <th data-options="field:'information', formatter:dealCaseFormatter, width:100, halign:'center', align:'center'">處理</th>
                    <th data-options="field:'caseId', width:120, sortable:true, halign:'center', align:'left'">客訴案號</th>
                    <th data-options="field:'complaintStaff', width:150, halign:'center', align:'left'">申訴人員</th>
                    <th data-options="field:'complaintDate', width:120, sortable:true, halign:'center', align:'center'">發生日期</th>
                    <th data-options="field:'contactWay', width:200, halign:'center', align:'left'">聯繫方式</th>
                    <th data-options="field:'customerName', width:150, halign:'center', align:'left'">客戶</th>
                    <th data-options="field:'isVip', width:80, halign:'center', align:'left'">VIP特店</th>
                    <th data-options="field:'merchantCode', width:200, sortable:true, halign:'center', align:'left'">特店代號</th>
                    <th data-options="field:'merchantName', width:200, halign:'center', align:'left'">特店名稱</th>
                    <th data-options="field:'tid', width:100, halign:'center', align:'left'">TID/DTID</th>
                    <th data-options="field:'complaintContent', width:250, halign:'center', align:'left', formatter:wrapFormatter">客訴內容</th>
                    <th data-options="field:'handleContent', width:250, halign:'center', align:'left', formatter:wrapFormatter">處理過程</th>
                    <th data-options="field:'companyName', width:120, halign:'center', align:'left'">歸責廠商</th>
                    <th data-options="field:'userName', width:150, halign:'center', align:'left'">歸責人員</th>
                    <th data-options="field:'questionTypeName', width:120, halign:'center', align:'left'">問題分類</th>
                    <th data-options="field:'isCustomer', width:80, halign:'center', align:'left'">賠償客戶</th>
                    <th data-options="field:'customerAmount', width:100, halign:'center', align:'right', formatter:formatCapacity">賠償金額</th>
                    <th data-options="field:'isVendor', width:80, halign:'center', align:'left'">廠商罰款</th>
                    <th data-options="field:'vendorAmount', width:100, halign:'center', align:'right', formatter:formatCapacity">罰款金額</th>
                    <th data-options="field:'createdDate', width:120, sortable:true, halign:'center', align:'center'">新增日期</th>
                </tr>
            </thead>
        </table>
        <div id="toolbar" style="padding: 2px 5px;">
            <form id="queryForm">
				<input type="hidden" id="actionId" name="actionId" />
				<input type="hidden" id="serviceId" name="serviceId" />
				<input type="hidden" id="useCaseNo" name="useCaseNo" />
				<input type="hidden" name="queryFlag" id="queryFlag" value="N"/>
	            <table cellpadding="3">
	            	<tr>
		                <td>客戶:</td>
		                <td>
		                	<cafe:droplisttag
				                id="<%=ComplaintFormDTO.QUERY_CUSTOMER_ID %>"
				                name="<%=ComplaintFormDTO.QUERY_CUSTOMER_ID %>"
				                result="${customerList }"
				                css="easyui-combobox"
				                style="width:150px"
				                blankName="請選擇" 
				                hasBlankValue="true"
				                javascript="editable=false"
				            ></cafe:droplisttag>
		                </td>
		                <td>特店代號:</td>
		                <td>
		                	<input id="<%=ComplaintFormDTO.QUERY_MERCHANT %>" 
				                name="<%=ComplaintFormDTO.QUERY_MERCHANT %>" style="width:150px"
				                class="easyui-textbox" maxlength="20" 
				                data-options="validType:['maxLength[20]']" value=""></input>
		                </td>
						<td>TID/DTID:</td>
						<td>
							<input id="<%=ComplaintFormDTO.QUERY_TID %>" 
					            name="<%=ComplaintFormDTO.QUERY_TID %>" style="width:150px" 
					            class="easyui-textbox" maxlength="8" 
					            data-options="validType:['maxLength[8]']" value=""></input>
						</td>
						<td>特店名稱:</td>
			            <td>
				            <input id="<%=ComplaintFormDTO.QUERY_MERCHANT_NAME %>" 
					            name="<%=ComplaintFormDTO.QUERY_MERCHANT_NAME %>" style="width:150px" 
					            class="easyui-textbox" maxlength="20" 
					            data-options="validType:['maxLength[20]']" value=""></input>
			        	</td>
					</tr>
					<tr>
						<td>問題分類:</td>
						<td>
			               <cafe:droplisttag
								id="<%=ComplaintFormDTO.QUERY_QUESTION_TYPE %>"
				                name="<%=ComplaintFormDTO.QUERY_QUESTION_TYPE %>"
				                result="${questionTypeList }"
				                css="easyui-combobox"
				                style="width:150px"
				                blankName="請選擇" 
				                hasBlankValue="true"
				                javascript="editable=false"
				           ></cafe:droplisttag>
						</td>
						<td>歸責廠商:</td>
						<td>
							<cafe:droplisttag
				                id="<%=ComplaintFormDTO.QUERY_VENDOR %>"
				                name="<%=ComplaintFormDTO.QUERY_VENDOR %>"
				                result="${vendorList }"
				                css="easyui-combobox"
				                style="width:150px"
				                blankName="請選擇" 
				                hasBlankValue="true"
				                javascript="editable=false"
				            ></cafe:droplisttag>
						</td>
						<td>賠償客戶:</td>
						<td>
							<cafe:droplisttag
				                id="<%=ComplaintFormDTO.QUERY_IS_CUSTOMER %>"
				                name="<%=ComplaintFormDTO.QUERY_IS_CUSTOMER %>"
				                result="${yesOrNoList }"
				                css="easyui-combobox"
				                hasBlankValue="true"
				                blankName="請選擇"
				                style="width:150px"
				                javascript="editable=false"
				            ></cafe:droplisttag>
						</td>
						<td>廠商罰款:</td>
						<td>
							<cafe:droplisttag
				                id="<%=ComplaintFormDTO.QUERY_IS_VENDOR %>"
				                name="<%=ComplaintFormDTO.QUERY_IS_VENDOR %>"
				                result="${yesOrNoList }"
				                css="easyui-combobox"
				                hasBlankValue="true"
				                blankName="請選擇"
				                style="width:150px"
				                javascript="editable=false"
				            ></cafe:droplisttag>
						</td>
					</tr>
					<tr>
						<td>發生日期:</td>
						<td colspan="3">
							<input class="easyui-datebox" id="<%=ComplaintFormDTO.QUERY_START_DATE %>" name="<%=ComplaintFormDTO.QUERY_START_DATE %>" maxlength="16"
								data-options="formatter:formaterTimeStampToyyyyMMDD, validType:['maxLength[16]','date']" style="width: 150px" value="${currentDate}"/> ～ 
							<input class="easyui-datebox" id="<%=ComplaintFormDTO.QUERY_END_DATE %>" name="<%=ComplaintFormDTO.QUERY_END_DATE %>" maxlength="16"
								data-options="formatter:formaterTimeStampToyyyyMMDD, validType:['maxLength[16]','date','compareDateStartEnd[\'#queryStartDate\',\'日期起不可大於日期迄\']']"
								style="width: 150px" value="${currentDate}"/>
						</td>
		           		<td colspan="2">
				            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" id="btnQuery">查詢</a>
				            <a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-add" id="btnAdd">新增</a>
		           			<a href="#" id="btnExport" style="width: 150px" onclick="doExport();">匯出</a>
		           		</td>
					</tr>
				</table>
        		<div class="red" id="message" data-options="multiline:true"></div>
            </form>
        </div>
    </div>
    <div id="dlg" modal ="true"></div>
    <script  type="text/javascript">
    //初始化datagrid
    $("#transDataGrid").datagrid({
        width:'100%', height:'auto',
        nowrap:false,
        pagination:true,
        singleSelect:true,
        fitColumn:false,
        pageNumber:0,
        pageList:[15, 30, 50, 100],
        pageSize:15,
        toolbar:'#toolbar',
        rownumbers:true,
		//selectOnCheck:true,
		//checkOnSelect:true,
        iconCls:'icon-edit'
    });
    
    $(function() {
		//查詢
		$("#btnQuery").click(function() {
			queryData(1,true);
		});
		//跳轉到新增畫面
		$("#btnAdd").click(function() {
			$("#message").text("");
			viewEditData('新增客訴管理','<%=IAtomsConstants.ACTION_INIT_EDIT%>', '');
		});
	});
    
    /**
     * 跳轉到修改畫面
     */
    function doUpdate(index) {
    	//取得選取row
		var temp = $('#transDataGrid').datagrid('selectRow',index);
		var row = $('#transDataGrid').datagrid('getSelected');
		if(row.caseId != null) {
			$("#message").text("");
	       	viewEditData('檢視客訴管理','<%=IAtomsConstants.ACTION_INIT_EDIT%>', row.caseId);
		} else {
			$.messager.alert('錯誤', '操作錯誤!', 'error');
			return false;
		}
    }
    
    /**
     * 作廢
     */
    function doDelete(index) {
    	//取得選取row
    	var temp = $('#transDataGrid').datagrid('selectRow',index);
		var row = $('#transDataGrid').datagrid('getSelected');
		var param = null;
		var url = "${contextPath}/<%=IAtomsConstants.COMPLAINT_MANAGE%>";
		if(row != null) {
	    	$("#message").empty();
	    	params = {
    			actionId : '<%=IAtomsConstants.ACTION_DELETE%>',
    			caseId : row.caseId
    		};
	    	var successFunction = function(data) {
				if (data.success) {
					var pageIndex = calDeletePagerIndex("transDataGrid");
					queryData(pageIndex, false);
				} 
				$("#message").text("客訴案號" + row.caseId + "，作廢" + data.msg.substring(6, 8));
			};
			var errorFunction = function() {
				$("#message").text("刪除失敗");
			};
			commonDelete(params, url, successFunction, errorFunction);
		} else {
			$.messager.alert('錯誤', '操作錯誤!', 'error');
			return false;
		}
    }
    
    /**
     * 匯出查詢結果
     */
    function doExport() {
		$("#message").text("");
    	var queryFlag = $('#queryFlag').val();
    	//若查詢標誌位為N，則提示用戶先進行查詢
		if(queryFlag == "<%=IAtomsConstants.NO%>"){
			$.messager.alert('警告','請先查詢,再執行匯出!','error');
			return false;
		} else {
			//得到查詢到的數據
			var rows = $("#transDataGrid").datagrid("getData");
			//若存在要匯出的資料就發送請求
			if(rows.total > 0){
				var blockStyle = {message:'loading...',fadeIn:0,fadeOut:0,css:{width:'150px',top:'40%',left:'40%',height:'50px',border:'3px solid #95B8E7',lineHeight:'50px'},overlayCSS:{backgroundColor:'#aaa',cursor:'default'}};
				$.blockUI(blockStyle);
				
				actionClicked( document.forms["queryForm"],
						'<%=IAtomsConstants.UC_NO_SRM_05100 %>',
						'',
						'<%=IAtomsConstants.ACTION_EXPORT%>');
				
				ajaxService.getExportFlag('<%=IAtomsConstants.UC_NO_SRM_05100 %>', function(data){
					$.unblockUI();
				});
			}
		}
    }
    
    /**
	 * 查詢數據
	 */
	function queryData(pageNum,hidden) {
		//清空選中
		$("#transDataGrid").datagrid("clearSelections");
		
		var queryParam = $("#queryForm").form("getData");
		var options = {
				url:"${contextPath}/<%=IAtomsConstants.COMPLAINT_MANAGE%>?actionId=<%=IAtomsConstants.ACTION_QUERY%>",
				queryParams:queryParam,
				pageNumber:pageNum,
				autoRowHeight:true,
				onLoadSuccess:function(data){
					if (hidden) {
						$("#message").text("");
						if (data.total == 0) {
							// 提示查無數據
							$("#message").text(data.msg);
							//將匯出按鈕變為灰色並不可點擊
							$('#btnExport').attr("onclick","return false;");
							$('#btnExport').attr("style","color:gray;");
						}
					}
					if (data.total > 0) {
						//查詢標誌位為Y
						$("#queryFlag").val("<%=IAtomsConstants.YES%>");
						//將匯出按鈕變為藍色且可編輯
						$('#btnExport').attr("onclick","doExport()");
						$('#btnExport').attr("style","color:blue;");
					}
					hidden = true
				},
				onLoadError : function() {
					$("#message").text("查詢失敗！請聯繫管理員");
				}
			}
		// 清空點選排序(注：若初始化直接使用datagrid的sortName進行排序的請再次賦予初值)
		if(hidden){
			options.sortName = null;
		}
		openDlgGridQuery("transDataGrid", options);
	}
    
    /**
	 * 案件清單處理按鈕 formatter函數
	 */
	function dealCaseFormatter(value,row,index) {
		//return '<a href="javascript:void(0)" name="viewInfoBtn" onclick="doUpdate(' + index + ');"></a>';
		return '<a href="javascript:void(0)" onclick="doUpdate(' + index + ');">修改</a>' +
		'  ' + '<a href="javascript:void(0)" onclick="doDelete(' + index + ');">作廢</a>';
    }
    
	/**
     * 跳轉到客訴管理詳情頁
     * title:頁面名稱
     * actionId:actionId
     * caseId:客訴編號
     */
    function viewEditData(title, actionId, caseId) {
		$("#message").text("");
		//打開新增/修改頁面
		var viewDlg = $('#dlg').dialog({
		    title : title,    
		    width : '720px',
		    height : '580px',
		    top : 10,
		    left : '20%',
		    closed : false,    
		    cache : false,
		  	//傳遞的參數
		    queryParams : {
		    	actionId : actionId,
		    	caseId : caseId
		    },
		    href : "${contextPath}/complaintManage.do",
		    modal : true,
		    //給頁面上的長度限制框添加maxlength屬性
		    onLoad : function() {
				textBoxSetting("dlg");
				dateboxSetting("dlg");
            },
		    buttons : [{
				text : '儲存',
				iconCls : 'icon-ok',
				width : '90px',
				handler : function() {
					$("#errorMsg").text("");
					//必填欄位
					var controls = ['<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_STAFF.getValue() %>',
					                '<%=ComplaintDTO.ATTRIBUTE.COMPLAINT_DATE.getValue() %>',
					                '<%=ComplaintDTO.ATTRIBUTE.CUSTOMER_ID.getValue() %>',
					                '<%=ComplaintDTO.ATTRIBUTE.MERCHANT_CODE.getValue() %>',
					                '<%=ComplaintDTO.ATTRIBUTE.TID.getValue() %>',
					                '<%=ComplaintDTO.ATTRIBUTE.COMPANY_ID.getValue() %>',
					                '<%=ComplaintDTO.ATTRIBUTE.QUESTION_TYPE.getValue() %>'];
					if (validateForm(controls) && $("#editForm").form("validate")) {
						//調保存遮罩
						commonSaveLoading('dlg');
						//發送請求
						$.ajax({
							url:"${contextPath}/<%=IAtomsConstants.COMPLAINT_MANAGE%>?actionId=<%=IAtomsConstants.ACTION_SAVE%>", 
							data : $("#editForm").form("getData"),
							type : 'post',
							cache : false, 
							dataType : 'json',
							success : function(json) {
								//去除保存遮罩
				             	commonCancelSaveLoading('dlg');
								$("#transDataGrid").datagrid("clearSelections");
								if (json.success) {
									viewDlg.dialog('close');
									//得到當前頁，進行再次查詢 
									var pageIndex = getGridCurrentPagerIndex("transDataGrid");
									queryData(pageIndex, false);
									setTimeout(function(){
										//顯示消息
										var msg = '';
										var caseId = $('#caseId').val();
										if (caseId == undefined || caseId == '') {
											var data = $('#transDataGrid').datagrid('getData');
											msg = '客訴案號' + data.rows[0].caseId + '，' + json.msg.substring(4, 8);
										} else {
											msg = '客訴案號' + $('#caseId').val() + '，' + json.msg.substring(4, 8);
										}
										$("#message").text(msg);
									}, 300);
								} else {
									$("#errorMsg").text(json.msg);
								}
							},
							error : function() {
								//去除保存遮罩
				             	commonCancelSaveLoading('dlg');
								$.messager.alert('提示', "修改失敗", 'error');			
							}
						});
					}
					//上傳文件和保存
					if (!(typeof(file_upload) == "undefined") && file_upload._storedIds != "") {
						file_upload.uploadStoredFiles();
					}
		    	}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				width :'90px',
				handler: function() {
					//關閉畫面
					confirmCancel(function() {
						viewDlg.dialog('close');
					});
				}
			}]
		});
	}
    </script>
</body>
</html>