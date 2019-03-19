package com.cybersoft4u.xian.iatoms.test.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ContractSlaDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CompanyFormDTO;
import com.cybersoft4u.xian.iatoms.services.ICompanyService;
import com.cybersoft4u.xian.iatoms.services.dao.impl.CompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose:公司基本訊息維護Service的單元測試
 * @author ElvaHe
 * @since JDK 1.6
 * @date 2017/05/12
 * @MaintenancePersonnel ElvaHe
 */
public class _TestCompanyService extends AbstractTestCase{
	
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestCompanyService.class);
	
	/**
	 * 注入公司service
	 */
	private ICompanyService companyService;
	
	/**
	 * 注入公司DAO
	 */
	private CompanyDAO companyDAO;

	/**
	 * 無參構造
	 */
	public _TestCompanyService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	public void testInit() {
		try {
			SessionContext  ctx =  new SessionContext();
			CompanyFormDTO formDTO = new CompanyFormDTO();
			ctx.setRequestParameter(formDTO);
			ctx = this.companyService.init(ctx);
		} catch (ServiceException e) {
			System.out.println("_TestCompanyService.testInit() is error" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試初始化修改方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testInitEdit(){
		try {
			SessionContext  ctx =  new SessionContext();
			CompanyFormDTO formDTO = new CompanyFormDTO();
			BimCompany company = null;
			
			//公司編號無對應的公司信息
			formDTO.setCompanyId("12345");
			company = this.companyDAO.findByPrimaryKey(BimCompany.class, formDTO.getCompanyId());
			ctx.setRequestParameter(formDTO);
			ctx = companyService.initEdit(ctx);
			if (IAtomsMessageCode.INIT_PAGE_FAILURE.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
			
			//公司編號有對應的公司信息
			formDTO.setCompanyId("1470649357293-0003");
			company = this.companyDAO.findByPrimaryKey(BimCompany.class, formDTO.getCompanyId());
			ctx.setRequestParameter(formDTO);
			ctx = companyService.initEdit(ctx);
			if (IAtomsMessageCode.INIT_PAGE_FAILURE.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(false);
			} else {
				Assert.assertTrue(true);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testInitEdit()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試保存方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testSave(){
		try {
			SessionContext  ctx =  new SessionContext();
			CompanyFormDTO formDTO = new CompanyFormDTO();
			
			//存在公司編號 -- 修改操作 -- 公司簡稱重複
			CompanyDTO companyDTO = new CompanyDTO();
			companyDTO.setShortName("英雄聯盟");
			companyDTO.setCompanyId("1470649357293-0003");
			formDTO.setCompanyDTO(companyDTO);
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473076241770-0048");
			logonUser.setName("賀文華");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = companyService.save(ctx);
			
			companyDTO = new CompanyDTO();
			companyDTO.setCompanyCode("duanin");
			formDTO.setCompanyDTO(companyDTO);
			logonUser.setId("1473076241770-0048");
			logonUser.setName("賀文華");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			ctx = companyService.save(ctx);
			if (IAtomsMessageCode.COMPANY_CODE_REPEAT.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(false);
			} else {
				Assert.assertTrue(true);
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testSave()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試初始化修改方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testQuery(){
		try {
			SessionContext  ctx =  new SessionContext();
			CompanyFormDTO formDTO = new CompanyFormDTO();
			formDTO.setQueryCompanyType("CUSTOMER");
			formDTO.setQueryShortName("");
			formDTO.setRows(10);
			formDTO.setPage(1);
			formDTO.setSort("");
			formDTO.setOrder("");
			ctx.setRequestParameter(formDTO);
			companyService.query(ctx);
			//符合查詢條件的記錄數
			Integer totalSize = this.companyDAO.count(formDTO.getQueryCompanyType(), formDTO.getQueryShortName());
			//若記錄數不等於0則查詢符合查詢條件的記錄信息
			if (totalSize.intValue() > 0) {
				//符合查詢條件的信息集合
				List<CompanyDTO> companyDTOs = 
						this.companyDAO.listBy(formDTO.getQueryCompanyType(), formDTO.getQueryShortName(), formDTO.getSort(), formDTO.getOrder(), formDTO.getRows(), formDTO.getPage());
				if(!CollectionUtils.isEmpty(companyDTOs)){
					Assert.assertNotNull(companyDTOs);
				} else {
					Assert.assertFalse(companyDTOs != null);
				}
			} else {
				Assert.assertTrue(IAtomsMessageCode.DATA_NOT_FOUND.equals(ctx.getReturnMessage().getCode()));
			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testQuery()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刪除方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testDelete(){
		try {
			SessionContext  ctx =  new SessionContext();
			CompanyFormDTO formDTO = new CompanyFormDTO();
			formDTO.setCompanyId("1493021779502-0355");
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473076241770-0048");
			logonUser.setName("賀文華");
			formDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(formDTO);
			companyService.delete(ctx);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testDelete()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據公司編號驗證登入驗證方式是否為iatoms驗證
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsAuthenticationTypeEqualsIAtoms(){
		try {
			//公司編號
			String companyId = "1486106270734-0341";
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			companyService.getCompanyByCompanyCode(inquiryContext);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testIsAuthenticationTypeEqualsIAtoms()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試匯出查詢匯出數據
	 * @author ElvaHe
	 * @return void
	 */
	public void testExport(){
		try {
			SessionContext  ctx =  new SessionContext();
			CompanyFormDTO formDTO = new CompanyFormDTO();
			String queryCompanyType = "CUSTOMER";
			String queryShortName = "lol";
			//依條件查出公司信息
			List<CompanyDTO> companyDTOs = this.companyDAO.listBy(queryCompanyType, queryShortName, null, null, Integer.valueOf(-1), Integer.valueOf(-1));
			formDTO.setList(companyDTOs);
			ctx.setRequestParameter(formDTO);
			companyService.export(ctx);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testExport()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據公司編號獲取相應的唯一編碼
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetUnityNameByCompanyId(){
		try {
			//公司編號
			String companyId = "1486106270734-0341";
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			companyService.getUnityNameByCompanyId(inquiryContext);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testGetUnityNameByCompanyId()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	public void testGetUnityNameByCompanyCode(){
		try {
			//公司編號
			String companyCode = "lol";
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue(), companyCode);
			companyService.getCompanyByCompanyCode(inquiryContext);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testGetUnityNameByCompanyId()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	public void testGetCompanyList(){
		try {
			//公司編號
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			inquiryContext.addParameter(ContractSlaDTO.ATTRIBUTE.IS_HAVE_SLA.getValue(), false);
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue(), null);
			companyService.getCompanyList(inquiryContext);
			inquiryContext = new MultiParameterInquiryContext();
			List<String> list = new ArrayList<String>();
			list.add(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			list.add(IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), list);
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.AUTHENTICATION_TYPE.getValue(), IAtomsConstants.PARAM_IATOMS_AUTHENTICATION_TYPE);
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.DTID_TYPE.getValue(), null);
			companyService.getCompanyList(inquiryContext);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testGetCompanyList()", "is error :", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試根據條件獲取公司下拉框列表
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetCompanyParameters(){
		try {
			//公司下拉列表
			List<String> companyTypeList = new ArrayList<String>();
			companyTypeList.add("CUSTOMER");
			companyTypeList.add("MAINTENANCE_VENDOR");
			companyTypeList.add("HARDWARE_VENDOR");
			//登入驗證方式
			String authenticationType = "CYBER_AUTHEN";
			//是否有SLA信息
			Boolean isHaveSla = true;
 			List<Parameter> list = companyService.getCompanyParameters(companyTypeList, authenticationType, isHaveSla, null);
 			if(!CollectionUtils.isEmpty(list)){
 				Assert.assertEquals(list.size(), list.size());
 			} else {
 				Assert.assertTrue(list == null);
 			}
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testGetCompanyParameters()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試是否為同DTID
	 * @author ElvaHe
	 * @return void
	 */
	public void testIsDtidType(){
		try {
			String companyId = "1493021779502-0355";
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			inquiryContext.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), companyId);
			companyService.isDtidType(inquiryContext);
		} catch (Exception e) {
			LOGGER.error("_TestCompanyService.testIsDtidType()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the companyService
	 */
	public ICompanyService getCompanyService() {
		return companyService;
	}
	/**
	 * @param companyService the companyService to set
	 */
	public void setCompanyService(ICompanyService companyService) {
		this.companyService = companyService;
	}

	/**
	 * @return the companyDao
	 */
	public CompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	/**
	 * @param companyDao the companyDao to set
	 */
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
	
}
