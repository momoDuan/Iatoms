<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.IAtomsConstants"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmUserFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO"%>
<%
	//初始化加載下拉框數據
	SessionContext ctx = (SessionContext) request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	AssetManageFormDTO formDTO = null;
	String maintenanceUserFlag = "";
	List<Parameter> deptList = new ArrayList<Parameter>();
	if (ctx != null) {
		formDTO = (AssetManageFormDTO) ctx.getResponseResult();
		maintenanceUserFlag = formDTO.getMaintenanceUserFlag();
		if (StringUtils.hasText(maintenanceUserFlag)) {
			deptList = formDTO.getDepartmentList();
		}
	} 
	if(CollectionUtils.isEmpty(deptList)){
		deptList = new ArrayList<Parameter>();
	}
	//公司列表
	List<Parameter> companyList = (List<Parameter>) SessionHelper.getAttribute(request,
	IAtomsConstants.UC_NO_DMM_03060, IAtomsConstants.ACTION_GET_COMPANY_LIST);



%>
<html>
<c:set var="companyList" value="<%=companyList%>" scope="page"></c:set>
<c:set var="maintenanceUserFlag" value="<%=maintenanceUserFlag%>" scope="page"></c:set>
<c:set var="deptList" value="<%=deptList%>" scope="page"></c:set>

<head>
    <meta charset="UTF-8">
    <script type="text/javascript" src="${contextPath}/js/base-function-common.js"></script>
</head>
<body>
<div region="center" style="margin-left:3%; width: auto; height: auto; padding: 10px; overflow-y: auto" class="setting-dialog-panel-height topSoller">
    <!-- <div region="center" style="width: 545px; height: auto; padding: 1px; background: #eee; overflow-y: hidden" id="11111"> -->
       <!--  <form id="searchForm" method="post" data-options="novalidate:true"> -->
            <table id="dataGridEmployee" class="easyui-datagrid" title="選擇帳號" style="width: 95%; height: auto"
                data-options="
				nowrap : false,
                rownumbers:true,
                pagination:true,
                pageList:[15,30,50,100],
				pageSize:15,
				iconCls: 'icon-edit',
				singleSelect: true,
				pageNumber:0,
				toolbar:'#tbDTID'">
                <thead>
                    <tr>
                        <!-- <th data-options="field:'ck',checkbox:true"></th> -->
                        <th data-options="field:'company',halign:'center',width:100,sortable:true">公司</th>
                        <th data-options="field:'deptName',halign:'center',width:100,sortable:true">部門</th>
                        <th data-options="field:'account',halign:'center',width:140,sortable:true">帳號</th>
                        <th data-options="field:'cname',halign:'center',width:150,sortable:true">中文姓名</th>
                        <th data-options="field:'ename',halign:'center',width:80,sortable:true">英文姓名</th>
                        
                    </tr>
                </thead>
            </table>
            <div id="tbDTID" style="padding: 2px 5px">
             <form id="searchForm" method="post" data-options="novalidate:true">
                <table width="100%">
                    <tr>
                        <td>公司:</td>
                        <td>
                           <cafe:droplisttag 
			               	id="<%=AdmUserFormDTO.QUERY_COMPANY %>"
						   	name="<%=AdmUserFormDTO.QUERY_COMPANY %>"
			               	css="easyui-combobox"
			               	result="${companyList }"
				            hasBlankValue="true"
				            selectedValue="${!empty maintenanceUserFlag?maintenanceUserFlag:'' }"
				            blankName="請選擇"
				            style="width:100px"
				            javascript="editable=false 
				            ${!empty maintenanceUserFlag?'disabled=true':'' }">
			               </cafe:droplisttag></td>
                        <td>部門:</td>
                        <td>
                            <cafe:droplisttag 
			               id="<%=AdmUserFormDTO.QUERY_DEPT_CODE %>"
						   	name="<%=AdmUserFormDTO.QUERY_DEPT_CODE %>"
			               css="easyui-combobox"
			               result="${deptList }"
			               hasBlankValue="true"
			               blankName="請選擇"
			               style="width:100px"
			               javascript="editable=false data-options=\"valueField:'value',textField:'name'\"">
			            </cafe:droplisttag></td>
                        </tr>
                    <tr>
                        <td>帳號:</td>
                        <td>
                            <input id="<%=AdmUserFormDTO.QUERY_ACCOUNT%>" name="<%=AdmUserFormDTO.QUERY_ACCOUNT%>" class="easyui-textbox" style="width: 110px"></td>
                        <td>中文姓名:</td>
                        <td>
                            <input id="<%=AdmUserFormDTO.QUERY_CNAME%>" name="<%=AdmUserFormDTO.QUERY_CNAME%>" class="easyui-textbox" style="width: 110px"></td>
                        <td><a href="javascript:void(0)" class="easyui-linkbutton" iconcls="icon-search" onclick="searchEmployees(1,true);">查詢</a></td>
                    </tr>                   
                </table>
                </form>
				<div><span id="msgs" class="red"></div>
            </div>
            

     <!--    </form> -->

    </div>
    <script type="text/javascript">
    var editIndex = undefined;
    //公司部門聯動    
    $("#queryCompany").combobox({
  		onChange:function(newValue, oldValue){
  		    $("#queryDeptCode").combobox("setValue","");
  			if (!isEmpty(newValue)) {
   			ajaxService.getDeptList(newValue,function(data){
   				if (data !=null) {
   					$("#queryDeptCode").combobox("loadData", initSelect(data));
   				}
   			});
  			} else {
  				//置空表單數據
  				$("#queryDeptCode").combobox("loadData",initSelect());
  			}
  		}
  	});
        

     // 查詢
   	function searchEmployees(pageIndex, hidden) {
   		var queryParam = $("#searchForm").form("getData");
   		queryParam.queryCompany = $("#queryCompany").combobox("getValue");
   		var options = {
   				url : "${contextPath}/admUser.do?actionId=<%=IAtomsConstants.ACTION_LIST%>",
   				queryParams :queryParam,
   				pageNumber:pageIndex,
   				isOpenDialog:true,
   				onLoadSuccess:function(data){
   					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "pointer");
   					});
   					$(".datagrid-row").mouseout(function(){ 
   						$(this).css("cursor", "auto");
   					});
   					if (hidden) {
   						$("#msgs").text("");
   						if (data.total == 0) {
   							// 提示查無數據
   							$("#msgs").text(data.msg);
   						}
   					}
   					hidden = true;
   				},
   				
   				onLoadError : function() {
   					$("#msgs").text("查詢失敗！請聯繫管理員");
   				},
   				onCheck : function(index,row){
   					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "auto");
   					});
   					dlgClickRow(row,false);
   				},
   				onSelect : function(index,row){
   					$(".datagrid-row").mouseover(function(){ 
   						$(this).css("cursor", "auto");
   					});
   					dlgClickRow(row,false);
   				},
   			};
   		// 清空點選排序(注：若初始化直接使用datagrid的sortName进行排序的请再次赋初值)
   		if(hidden){
   			options.sortName = null;
   		}
   		openDlgGridQuery("dataGridEmployee", options); 
   		
   	}

        function endEditing() {
            if (editIndex == undefined) { return true }
            if ($('#dg').datagrid('validateRow', editIndex)) {
                $('#dg').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onClickCell(index, field) {
            if (editIndex != index) {
                if (endEditing()) {
                    $('#dg').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    var ed = $('#dg').datagrid('getEditor', { index: index, field: field });
                    if (ed) {
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }
                    editIndex = index;
                } else {
                    setTimeout(function () {
                        $('#dg').datagrid('selectRow', editIndex);
                    }, 0);
                }
            }
        }
        function onEndEdit(index, row) {
            var ed = $(this).datagrid('getEditor', {
                index: index,
                field: 'id'
            });
            row.name = $(ed.target).combobox('getText');
        }
        function append() {
            if (endEditing()) {
                $('#dgcase').datagrid('appendRow', { company: 'Cyber' });
                editIndex = $('#dgcase').datagrid('getRows').length - 1;
                $('#dgcase').datagrid('selectRow', editIndex)
                        .datagrid('beginEdit', editIndex);
            }
        }
        function removeit() {
            if (editIndex == undefined) { return }
            $('#dgcase').datagrid('cancelEdit', editIndex)
                    .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
        function accept() {
            if (endEditing()) {
                $('#dgcase').datagrid('acceptChanges');
            }
        }
        function reject() {
            $('#dgcase').datagrid('rejectChanges');
            editIndex = undefined;
        }
        function newUser() {
            $('#dlg').dialog('open').dialog('center').dialog('setTitle', '案件建案-裝機');
            $('#fm').form('clear');
            url = 'save_user.php';
        }
        function searchMer() {
            $('#dlgMerchant').dialog('open').dialog('center').dialog('setTitle', '選擇 MID');
            $('#fmMerchant').form('clear');
        }
        function editUser() {
            var row = $('#dg').datagrid('getSelected');
            if (row) {
                $('#dlg').dialog('open').dialog('center').dialog('setTitle', '案件建案-裝機');
                $('#fm').form('load', row);
                url = 'update_user.php?id=' + row.id;
            }
        }
        function saveUser() {
            $('#fm').form('submit', {
                url: url,
                onSubmit: function () {
                    return $(this).form('validate');
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.errorMsg) {
                        $.messager.show({
                            title: 'Error',
                            msg: result.errorMsg
                        });
                    } else {
                        $('#dlg').dialog('close');        // close the dialog
                        $('#dg').datagrid('reload');    // reload the user data
                    }
                }
            });
        }
        function destroyUser() {
            var row = $('#dg').datagrid('getSelected');
            if (row) {
                $.messager.confirm('Confirm', '確認刪除?', function (r) {
                    if (r) {
                        $.post('destroy_user.php', { id: row.id }, function (result) {
                            if (result.success) {
                                $('#dg').datagrid('reload');    // reload the user data
                            } else {
                                $.messager.show({    // show error message
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            }
                        }, 'json');
                    }
                });
            }
        }
	</script>
</body>
</html>
