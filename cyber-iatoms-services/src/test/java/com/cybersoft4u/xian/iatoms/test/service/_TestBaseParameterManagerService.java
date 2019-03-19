package com.cybersoft4u.xian.iatoms.test.service;

import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;

/**
 * Purpose: 系統參數維護Service單元測試
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-5-26
 * @MaintenancePersonnel CrissZhang
 */
public class _TestBaseParameterManagerService extends AbstractTestCase {

	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestBaseParameterManagerService.class);

	/**
	 * 系統參數維護Service
	 */
	private IBaseParameterManagerService baseParameterManagerService;
	
	/**
	 * 系統參數維護DAO
	 */
	private IBaseParameterManagerDAO baseParameterManagerDAO;
	/**
	 * Constructor:構造函數
	 */
	public _TestBaseParameterManagerService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase#testInit()
	 */
	public void testInit(){
		try {
			// sessionContext 爲空
			SessionContext sessionContext = null;
			baseParameterManagerService.init(sessionContext);
			
			// 成功返回消息
			sessionContext = new SessionContext();
			BaseParameterManagerFormDTO formDTO = new BaseParameterManagerFormDTO();
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.init(sessionContext);
			Assert.assertTrue(IAtomsMessageCode.INIT_PAGE_SUCCESS.equals(sessionContext.getReturnMessage().getCode()));
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testInit()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試查詢參數類型列表的方法
	 * @author CrissZhang
	 * @return void
	 */
	public void testListParameterTypes(){
		try {
			// sessionContext 爲空
			SessionContext sessionContext = null;
			baseParameterManagerService.listParameterTypes(sessionContext);
			
			// 不爲空繼續處理其他的
			sessionContext = new SessionContext();
			sessionContext = baseParameterManagerService.listParameterTypes(sessionContext);
			List<Parameter> results = (List<Parameter>) sessionContext.getResponseResult();
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				if(results == null){
					Assert.assertNull(results);
				} else {
					Assert.assertTrue(results.size() == 0);
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testListParameterTypes()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據父類別參數查找對應的子類別參數列表
	 * @author CrissZhang
	 * @return void
	 */
	public void testGetParametersByParent(){/*
		try {
			MultiParameterInquiryContext inquiryContext = new MultiParameterInquiryContext();
			List<Parameter> results = baseParameterManagerService.getParametersByParent(inquiryContext);
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				if(results == null){
					Assert.assertNull(results);
				} else {
					Assert.assertTrue(results.size() == 0);
				}
			}
			// 父類別item_value
			String parentBptdValue = "12";
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, parentBptdValue);
			results = baseParameterManagerService.getParametersByParent(inquiryContext);
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				if(results == null){
					Assert.assertNull(results);
				} else {
					Assert.assertTrue(results.size() == 0);
				}
			}
			// 父類別item_value
			parentBptdValue = null;
			// 父类别bptd_code
			String parentBptdCode = "REPORT_CODE";
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, parentBptdValue);
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, parentBptdCode);
			results = baseParameterManagerService.getParametersByParent(inquiryContext);
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				if(results == null){
					Assert.assertNull(results);
				} else {
					Assert.assertTrue(results.size() == 0);
				}
			}
			// 父類別item_value
			parentBptdValue = "11";
			// 父类别bptd_code
			parentBptdCode = "REPORT_CODE";
			// 子類別bptd_code
			String childrenBptdCode = "REPORT_DETAIL";
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, parentBptdValue);
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, parentBptdCode);
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, childrenBptdCode);
			results = baseParameterManagerService.getParametersByParent(inquiryContext);
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				if(results == null){
					Assert.assertNull(results);
				} else {
					Assert.assertTrue(results.size() == 0);
				}
			}
			// 父類別item_value
			parentBptdValue = "12";
			// 父类别bptd_code
			parentBptdCode = "REPORT_CODE";
			// 子類別bptd_code
			childrenBptdCode = "REPORT_DETAIL";
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, parentBptdValue);
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, parentBptdCode);
			inquiryContext.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, childrenBptdCode);
			results = baseParameterManagerService.getParametersByParent(inquiryContext);
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(results);
			} else {
				if(results == null){
					Assert.assertNull(results);
				} else {
					Assert.assertTrue(results.size() == 0);
				}
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testGetParametersByParent()", e);
			e.printStackTrace();
		}
	*/}
	
	/**
	 * Purpose:測試進入編輯頁面
	 * @author CrissZhang
	 * @return void
	 */
	public void testInitEdit(){
		try {
			// sessionContext 爲空
			SessionContext sessionContext = null;
			baseParameterManagerService.initEdit(sessionContext);
			
			// sessionContext 不爲空 formDTO
			sessionContext = new SessionContext();
			BaseParameterManagerFormDTO formDTO = null;
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.initEdit(sessionContext);
			
			formDTO = new BaseParameterManagerFormDTO();
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.initEdit(sessionContext);
			
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.initEdit(sessionContext);
			
			formDTO.setEditBpidId(null);
			formDTO.setEditBptdCode("ACCOUNT_STATUS");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.initEdit(sessionContext);
			
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.initEdit(sessionContext);
			formDTO = (BaseParameterManagerFormDTO) sessionContext.getResponseResult();
			BaseParameterItemDefDTO baseParameterItemDefDTO = formDTO.getBaseParameterItemDefDTO();
			if(baseParameterItemDefDTO != null) {
				Assert.assertNotNull(baseParameterItemDefDTO);
			} else {
				Assert.assertNull(baseParameterItemDefDTO);
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testInitEdit()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刪除
	 * @author CrissZhang
	 * @return void
	 */
	public void testDelete(){
		try {
			// sessionContext 爲空
			SessionContext sessionContext = null;
			baseParameterManagerService.delete(sessionContext);
			
			// sessionContext 不爲空 formDTO
			sessionContext = new SessionContext();
			BaseParameterManagerFormDTO formDTO = null;
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.delete(sessionContext);
			
			formDTO = new BaseParameterManagerFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473815670182-0069");
			logonUser.setName("吼吼");
			formDTO.setLogonUser(logonUser);
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.delete(sessionContext);
			
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.delete(sessionContext);
			
			formDTO.setEditBpidId(null);
			formDTO.setEditBptdCode("ACCOUNT_STATUS");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.delete(sessionContext);
			
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.delete(sessionContext);
		//	formDTO = (BaseParameterManagerFormDTO) sessionContext.getResponseResult();
			BaseParameterItemDefDTO baseParameterItemDefDTO = this.baseParameterManagerDAO.getBaseParameterItemDefDTO(formDTO.getEditBpidId(), null, formDTO.getEditBptdCode(), null, null);
			if(baseParameterItemDefDTO != null){
				//将查询出的资料转换为dmo
				SimpleDtoDmoTransformer transformer = new SimpleDtoDmoTransformer();
				BaseParameterItemDef baseParameterItemDef = null;
				baseParameterItemDef = (BaseParameterItemDef) transformer.transform(baseParameterItemDefDTO, new BaseParameterItemDef());
				if(baseParameterItemDef != null){
					Assert.assertTrue(IAtomsMessageCode.DELETE_SUCCESS.equals(sessionContext.getReturnMessage().getCode()));
				} else {
					Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(sessionContext.getReturnMessage().getCode()));
				}
			} else {
				Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(sessionContext.getReturnMessage().getCode()));
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testDelete()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試查詢
	 * @author CrissZhang
	 * @return void
	 */
	public void testQuery(){
		try {
			// sessionContext 爲空
			SessionContext sessionContext = null;
			baseParameterManagerService.query(sessionContext);
			
			// sessionContext 不爲空 formDTO
			sessionContext = new SessionContext();
			BaseParameterManagerFormDTO formDTO = null;
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.query(sessionContext);
			
			formDTO = new BaseParameterManagerFormDTO();
			formDTO.getPageNavigation().setCurrentPage(1);
			formDTO.getPageNavigation().setPageSize(10);
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.query(sessionContext);
			Integer rowCount = this.baseParameterManagerDAO.count(formDTO.getQueryParamType(), formDTO.getQueryParamCode(), formDTO.getQueryParamName());
			if(rowCount > 0 ){
				Assert.assertTrue(IAtomsMessageCode.QUERY_SUCCESS.equals(sessionContext.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(IAtomsMessageCode.DATA_NOT_FOUND.equals(sessionContext.getReturnMessage().getCode()));
			}
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testDelete()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:
	 * @author CrissZhang
	 * @return void
	 */
	public void testSave(){
		try {
			// sessionContext 爲空
			SessionContext sessionContext = null;
			baseParameterManagerService.save(sessionContext);
			
			// sessionContext 不爲空 formDTO
			sessionContext = new SessionContext();
			BaseParameterManagerFormDTO formDTO = null;
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			formDTO = new BaseParameterManagerFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473815670182-0069");
			logonUser.setName("吼吼");
			formDTO.setLogonUser(logonUser);
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			BaseParameterItemDefDTO sourceDTO = null; 
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			sourceDTO = new BaseParameterItemDefDTO();
			sourceDTO.setBptdCode("ACCOUNT_STATUS");
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
		//	formDTO.setEditBptdCode("ACCOUNT_STATUS");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			formDTO.setEditBpidId(null);
			formDTO.setEditBptdCode(null);
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			sourceDTO.setItemName("新帳號");
			sourceDTO.setItemValue("123");
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			formDTO.setEditBptdCode("ACCOUNT_STATUS");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			sourceDTO.setItemName("456");
			sourceDTO.setItemValue("NEW");
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			formDTO.setEditBptdCode("ACCOUNT_STATUS");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			
			sourceDTO.setItemName("456");
			sourceDTO.setItemValue("123");
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			formDTO.setEditBpidId("ACCOUNT_STATUS_06");
			formDTO.setEditBptdCode("ACCOUNT_STATUS");
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			Assert.assertTrue(IAtomsMessageCode.UPDATE_SUCCESS.equals(sessionContext.getReturnMessage().getCode()));
			
			sourceDTO.setItemName("456");
			sourceDTO.setItemValue("123");
			formDTO.setBaseParameterItemDefDTO(sourceDTO);
			formDTO.setEditBpidId(null);
			formDTO.setEditBptdCode(null);
			sessionContext.setRequestParameter(formDTO);
			sessionContext = baseParameterManagerService.save(sessionContext);
			Assert.assertTrue(IAtomsMessageCode.INSERT_SUCCESS.equals(sessionContext.getReturnMessage().getCode()));
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".testCheck()", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the baseParameterManagerService
	 */
	public IBaseParameterManagerService getBaseParameterManagerService() {
		return baseParameterManagerService;
	}
	/**
	 * @param baseParameterManagerService the baseParameterManagerService to set
	 */
	public void setBaseParameterManagerService(
			IBaseParameterManagerService baseParameterManagerService) {
		this.baseParameterManagerService = baseParameterManagerService;
	}

	/**
	 * @return the baseParameterManagerDAO
	 */
	public IBaseParameterManagerDAO getBaseParameterManagerDAO() {
		return baseParameterManagerDAO;
	}

	/**
	 * @param baseParameterManagerDAO the baseParameterManagerDAO to set
	 */
	public void setBaseParameterManagerDAO(
			IBaseParameterManagerDAO baseParameterManagerDAO) {
		this.baseParameterManagerDAO = baseParameterManagerDAO;
	}
}
