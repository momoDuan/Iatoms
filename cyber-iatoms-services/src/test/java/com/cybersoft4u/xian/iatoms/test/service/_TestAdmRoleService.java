package com.cybersoft4u.xian.iatoms.test.service;

import java.util.ArrayList;
import java.util.List;

import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AdmRoleFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAdmRoleService;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

public class _TestAdmRoleService extends AbstractTestCase {

	/**
	 * 角色service
	 */
	private IAdmRoleService admRoleService;

	/**
	 * Constructor:無參構造函數
	 */
	public _TestAdmRoleService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	
	/**
	 * Purpose:測試-初始化角色清單
	 * @author CarrieDuan
	 * @return void
	 */
	public void testInit() {
		try {
			SessionContext  ctx =  new SessionContext();
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.init(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testInit() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-初始化角色清單
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetRoleList() {
		try {
			this.admRoleService.getRoleList();
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testGetRoleList() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetRolesByUserId() {
		try {
			String userId = "123";
			this.admRoleService.getRoleListByUserId(userId);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testGetRolesByUserId() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試- 初始化查詢參數角色代號
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetRoleCode() {
		try {
			this.admRoleService.getRoleCode();
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testGetRoleCode() is error" + e);
			e.printStackTrace();
		}
	}
	
	public void testGetParentFunction() {
		try {
			this.admRoleService.getParentFunction();
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testGetRoleCode() is error" + e);
			e.printStackTrace();
		}
	}
	
	public void testGetFunctionByParentId() {
		try {
			MultiParameterInquiryContext parameterInquiryContext = new MultiParameterInquiryContext();
			this.admRoleService.getFunctionByParentId(parameterInquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testGetRoleCode() is error" + e);
			e.printStackTrace();
		}
	}
	public void saveSystemLog() {
		try {
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(AdmRoleFormDTO.PARAM_USER_LOGON_USER, logonUser);
			inquiryContext.addParameter("actionId", "query");
			this.admRoleService.saveSystemLog(inquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testCheckUseRole() is error" + e);
			e.printStackTrace();
		}
	}
	public void saveLog() {
		try {
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(AdmRoleFormDTO.PARAM_USER_LOGON_USER, logonUser);
			inquiryContext.addParameter("actionId", "query");
			this.admRoleService.saveLog(inquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testCheckUseRole() is error" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Purpose: 測試-查詢
	 * @author CarrieDuan
	 * @return void
	 */
	public void testQuery() {
		try {
			SessionContext  ctx =  new SessionContext();
			this.admRoleService.query(ctx);
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			admRoleFormDTO.setQueryRoleCode(null);
			admRoleFormDTO.setQueryRoleName(null);
			admRoleFormDTO.setRows(10);
			admRoleFormDTO.setPage(1);
			admRoleFormDTO.setSort("roleCode");
			admRoleFormDTO.setOrder("asc");
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.query(ctx);
			ctx =  new SessionContext();
			admRoleFormDTO = new AdmRoleFormDTO();
			admRoleFormDTO.setQueryRoleCode("1qaz2wsx");
			admRoleFormDTO.setQueryRoleName("2wsx3edc");
			admRoleFormDTO.setRows(10);
			admRoleFormDTO.setPage(1);
			admRoleFormDTO.setSort("roleCode");
			admRoleFormDTO.setOrder("asc");
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.query(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testQuery() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試 - 刪除
	 * @author CarrieDuan
	 * @return void
	 */
	public void testDelete() {
		try {
			SessionContext  ctx =  new SessionContext();
			this.admRoleService.delete(ctx);
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			admRoleFormDTO.setLogonUser(logonUser);
			admRoleFormDTO.setRoleId("1471325616968-0067");
			ctx.setRequestParameter(admRoleFormDTO);
			ctx =  new SessionContext();
			admRoleFormDTO = new AdmRoleFormDTO();
			admRoleFormDTO.setLogonUser(logonUser);
			admRoleFormDTO.setRoleId("1000000000-0018");
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.delete(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testDelete() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試 - 保存角色信息
	 * @author CarrieDuan
	 * @return void
	 */
	public void testSave() {
		try {
			SessionContext  ctx =  new SessionContext();
			//this.admRoleService.save(ctx);
			
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			admRoleFormDTO.setLogonUser(logonUser);
			AdmRoleDTO roleDTO = new AdmRoleDTO();
			roleDTO.setRoleCode("CUSTOMER");
			roleDTO.setRoleId("1000000000-0012");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);
			
			roleDTO = new AdmRoleDTO();
			roleDTO.setRoleName("客戶");
			roleDTO.setRoleCode("ADVANCED_WAREHOUSE");
			roleDTO.setRoleId("1000000000-0012");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);
			
			roleDTO = new AdmRoleDTO();
			roleDTO.setRoleCode("testCode");
			roleDTO.setRoleName("testName");
			roleDTO.setRoleDesc("DFDSF");
			roleDTO.setAttribute("CUSTOMER");
			roleDTO.setWorkFlowRole("BANK_AGENT");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);
			
			roleDTO = new AdmRoleDTO();
			roleDTO.setRoleCode("ADVANCED_SERVICE");
			roleDTO.setRoleId("1000000000-0003");
			roleDTO.setRoleName("進階客服");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);
			
			/*roleDTO = new AdmRoleDTO();
			roleDTO.setRoleCode("VENDOR_ENGINEER");
			roleDTO.setRoleId("1000000000-0010");
			roleDTO.setRoleName("客戶test");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);
			
			roleDTO = new AdmRoleDTO();
			roleDTO.setRoleCode("TMS");
			roleDTO.setRoleId("1000000000-0005");
			roleDTO.setRoleName("客戶test");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);
			
			roleDTO = new AdmRoleDTO();
			roleDTO.setRoleName("客戶");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.save(ctx);*/
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testSave() is error" + e);
			e.printStackTrace();
		}
	}
	
	/** 
	 * Purpose:測試 - 初始化明細頁面
	 * @author CarrieDuan
	 * @return void
	 */
	public void testInitDetail() {
		try {
			SessionContext ctx =  new SessionContext();
			this.admRoleService.initDetail(ctx);
			
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			AdmRoleDTO roleDTO = new AdmRoleDTO();
			roleDTO.setRoleId("1471325616968-0067");
			admRoleFormDTO.setAdmRoleDTO(roleDTO);
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.initDetail(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testInitDetail() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試 - 明細頁面加載數據
	 * @author CarrieDuan
	 * @return void
	 */
	public void testLoadDlgData() {
		try {
			SessionContext  ctx =  new SessionContext();
			this.admRoleService.loadDlgData(ctx);
			
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			admRoleFormDTO.setRoleId("1000000000-0017");
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.loadDlgData(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testLoadDlgData() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試 - 保存角色權限
	 * @author CarrieDuan
	 * @return void
	 */
	public void testSaveRolePermission() {
		try {
			SessionContext  ctx =  new SessionContext();
			this.admRoleService.saveRolePermission(ctx);
			
			PermissionDTO permissionDTO = null;
			AdmRoleFormDTO admRoleFormDTO = new AdmRoleFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1");
			logonUser.setName("amanda");
			admRoleFormDTO.setLogonUser(logonUser);
			List<PermissionDTO> permissionDTOs = new ArrayList<PermissionDTO>();
			permissionDTOs.add(new PermissionDTO(null, "ADM01020", "create"));
			permissionDTOs.add(new PermissionDTO(null, "ADM01020", "update"));
			permissionDTOs.add(new PermissionDTO(null, "ADM01020", "delete"));
			permissionDTOs.add(new PermissionDTO(null, "BIM02010", "create"));
			admRoleFormDTO.setPermissionDTOs(permissionDTOs);
			admRoleFormDTO.setRoleId("1000000000-0017");
			ctx.setRequestParameter(admRoleFormDTO);
			this.admRoleService.saveRolePermission(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testSaveRolePermission() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試-根據角色屬性獲取對應的表單角色
	 * @author CarrieDuan
	 * @return void
	 */
	public void testGetWorkFlowRoleList() {
		try {
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			String attributeValue = "01";
			inquiryContext.addParameter(AdmRoleDTO.ATTRIBUTE.ATTRIBUTE.getValue(), attributeValue);
			this.admRoleService.getWorkFlowRoleList(inquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testGetWorkFlowRoleList() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試 - 檢測角色是否已經被人員使用
	 * @author CarrieDuan
	 * @return void
	 */
	public void testCheckUseRole() {
		try {
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			String roleId = "1471263723387-0050";
			inquiryContext.addParameter(AdmRoleDTO.ATTRIBUTE.ROLE_ID.getValue(), roleId);
			this.admRoleService.checkUseRole(inquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testCheckUseRole() is error" + e);
			e.printStackTrace();
		}
	}
	
	public void testUserByDepartmentAndRole() {
		try {
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			this.admRoleService.getUserByDepartmentAndRole(inquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testCheckUseRole() is error" + e);
			e.printStackTrace();
		}
	}
	
	public void testCheckRoleRepeat() {
		try {
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter("roleCode", "VENDOR_SERVICE");
			inquiryContext.addParameter("roleId", "1000000000-0017");
			this.admRoleService.checkRoleRepeat(inquiryContext);
			inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter("roleName", "廠商倉管");
			inquiryContext.addParameter("roleId", "1000000000-0017");
			this.admRoleService.checkRoleRepeat(inquiryContext);
		} catch (ServiceException e) {
			System.out.println("_TestAdmRoleService.testCheckUseRole() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the admRoleService
	 */
	public IAdmRoleService getAdmRoleService() {
		return admRoleService;
	}

	/**
	 * @param admRoleService the admRoleService to set
	 */
	public void setAdmRoleService(IAdmRoleService admRoleService) {
		this.admRoleService = admRoleService;
	}
	
	
}
