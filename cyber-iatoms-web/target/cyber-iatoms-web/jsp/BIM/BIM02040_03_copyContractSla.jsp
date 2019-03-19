<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/jsp/common/common.jsp"%>
<%@ include file="/jsp/common/easyui-common.jsp"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractSlaFormDTO"%>
<%@ page import="com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO"%>
<%
	SessionContext ctx = (SessionContext)request.getAttribute(IAtomsConstants.PARAM_SESSION_CONTEXT);
	ContractSlaFormDTO contractSlaFormDTO = null;
	String ucNo = null;
	if (ctx != null) {
		// 得到FormDTO
		contractSlaFormDTO = (ContractSlaFormDTO) ctx.getRequestParameter();
		if (contractSlaFormDTO != null) {
			// 获得UseCaseNo
			ucNo = contractSlaFormDTO.getUseCaseNo();
		} else {
			ucNo = IAtomsConstants.UC_NO_BIM_02040;
			contractSlaFormDTO = new ContractSlaFormDTO();
		}
	} else {
		ucNo = IAtomsConstants.UC_NO_BIM_02040;
		contractSlaFormDTO = new ContractSlaFormDTO();
	}
	// 客戶列表下拉框
	List<Parameter> customerList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_CUSTOMER_LIST);
	// 複製客戶下拉框
	List<Parameter> slaCompanyList = (List<Parameter>)SessionHelper.getAttribute(request, ucNo, IAtomsConstants.PARAM_SLA_COMPANY_LIST);
%>
<c:set var="contractSlaFormDTO" value="<%=contractSlaFormDTO%>" scope="page"></c:set>
<c:set var="customerList" value="<%=customerList%>" scope="page"></c:set>
<c:set var="slaCompanyList" value="<%=slaCompanyList%>" scope="page"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<script type="text/javascript">
$(function(){
	/*
	* 合約編號下拉框聯動
	*/
	$('#<%=ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID%>').combobox({
		onChange: function(){
			$('#<%=ContractSlaFormDTO.ORIGINAL_CONTRACT_ID%>').combobox('setValue','');
			var customerId = $('#<%=ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID%>').combobox('getValue');
			if(!isEmpty(customerId)){
				ajaxService.getContractCodeList(customerId,function(data){
					// 设置默认值
					var data = initSelect(data);
					// 给合约编号下拉框放值
					$('#<%=ContractSlaFormDTO.ORIGINAL_CONTRACT_ID%>').combobox('loadData',data);
				}); 
			} else {
				var data = initSelect();
				// 客戶為請選擇時合約編號為請選擇
				$('#<%=ContractSlaFormDTO.ORIGINAL_CONTRACT_ID%>').combobox('loadData',data);
			} 	
		}
	});
	/*
	* 複製合約編號下拉框聯動
	*/
	$('#<%=ContractSlaFormDTO.COPY_CUSTOMER_ID%>').combobox({
		onChange: function(newValue,oldValue){
			$('#<%=ContractSlaFormDTO.COPY_CONTRACT_ID%>').combobox('setValue','');
			var customerId = $('#<%=ContractSlaFormDTO.COPY_CUSTOMER_ID%>').combobox('getValue');
			if(!isEmpty(customerId)){
				ajaxService.getContractCodeList(customerId, '', true, function(data){
					// 设置默认值
					var data = initSelect(data);
					// 给复制合约编号下拉框放值
					$('#<%=ContractSlaFormDTO.COPY_CONTRACT_ID%>').combobox('loadData',data);
				}); 
			} else {
				var data = initSelect();
				// 複製客戶為請選擇時複製合約編號為請選擇
				$('#<%=ContractSlaFormDTO.COPY_CONTRACT_ID%>').combobox('loadData',data);
			} 
		}
	});		
});
</script>
<div data-options="region:'center'" style="width: auto; height: auto; padding: 10px 20px; background: #fff; overflow-y: hidden">
	<div class="dialogtitle">合約SLA設定</div>
    <div><span id="copyDlgMsg" class="red"></div>
    <form id="copyForm" class="formStyle" method="post" novalidate>
			<table cellpadding="4">
				<tr>
					<td>客戶:<span class="red">*</span></td>
					<td>
						<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID %>"
							name="<%=ContractSlaFormDTO.ORIGINAL_CUSTOMER_ID %>" result="${customerList}"
							hasBlankValue="true" blankName="請選擇" style="width: 150px"
							javascript="editable='false' validType=\"ignore['請選擇']\" required=true invalidMessage=\"請輸入客戶\" missingMessage=\"請輸入客戶\"">
						</cafe:droplisttag>
					</td>
					<td>合約編號:<span class="red">*</span></td>
					<td>
						<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.ORIGINAL_CONTRACT_ID%>"
							name="<%=ContractSlaFormDTO.ORIGINAL_CONTRACT_ID%>" 
							blankName="請選擇"
							hasBlankValue="true" style="width: 150px"
							javascript="editable='false' required=true validType=\"ignore['請選擇']\" required=true valueField='value' textField='name' invalidMessage=\"請輸入合約編號\" missingMessage=\"請輸入合約編號\"">
						</cafe:droplisttag>
					</td>
				</tr>
				<tr>
					<td>複製客戶:<span class="red">*</span></td>
					<td>
						<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.COPY_CUSTOMER_ID %>"
							name="<%=ContractSlaFormDTO.COPY_CUSTOMER_ID %>" result="${slaCompanyList}"
							selectedValue="" disabled="false" style="width: 150px"
							hasBlankValue="true" blankName="請選擇" 
							javascript="editable='false' validType=\"ignore['請選擇']\" required=true invalidMessage=\"請輸入複製客戶\" missingMessage=\"請輸入複製客戶\"" >
						</cafe:droplisttag>
                       </td>
                       <td>複製合約編號:<span class="red">*</span></td>
                       <td>
                       	<cafe:droplisttag css="easyui-combobox" id="<%=ContractSlaFormDTO.COPY_CONTRACT_ID%>"
							name="<%=ContractSlaFormDTO.COPY_CONTRACT_ID%>" 
							hasBlankValue="true" blankName="請選擇" style="width: 150px"
							javascript="editable='false' required=true valueField='value' textField='name' validType=\"ignore['請選擇']\" panelheight=\"auto\" missingMessage=\"請輸入複製合約編號\" invalidMessage=\"請輸入複製合約編號\" " >
						</cafe:droplisttag> 
                       </td>
				</tr>
			</table>	
		</form>
	</div>
</body>
</html>