package com.cybersoft4u.xian.iatoms.test.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.PageNavigation;
import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractAssetDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTypeFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAssetTypeService;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetType;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 設備品項維護service單元測試
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/10
 * @MaintenancePersonnel HermanWang
 */
public class _TestAssetTypeService extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestChangePasswordService.class);
	/**
	 * 注入assetTypeService
	 */
	private IAssetTypeService assetTypeService;

	/**
	 * 設備品項維護DAO注入
	 */
	private IAssetTypeDAO assetTypeDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestAssetTypeService() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase#testInit()
	 */
	public void testInit(){
		try {
			//ctx 不為空
			SessionContext  ctx =  new SessionContext();
			AssetTypeFormDTO assetTypeFormDTO = new AssetTypeFormDTO();
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.init(ctx);
			Assert.assertTrue(IAtomsMessageCode.INIT_PAGE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
			
			//ctx 為空
			ctx = null;
			ctx = assetTypeService.init(ctx);
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testQuery()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試設備品項維護的查詢
	 * @author HermanWang
	 * @return void
	 */
	public void testQuery(){
		try {
			//ctx為空
			SessionContext  ctxTest = null;
			assetTypeService.query(ctxTest);
			
			//ctx不為空 formdto為空
			SessionContext  ctx =  new SessionContext();
			assetTypeService.query(ctx);
			
			//ctx不為空 formdto不為空
			AssetTypeFormDTO assetTypeFormDTO = new AssetTypeFormDTO();
			//查詢條件 -- 設備類別
			assetTypeFormDTO.setQueryAssetCategoryCode("EDC");
			//查詢條件 -- 排序欄位
			assetTypeFormDTO.setSort(null);
			//查詢條件 -- 排序方式
			assetTypeFormDTO.setOrder(null);
			PageNavigation pageNavigation = new PageNavigation();
			pageNavigation.setCurrentPage(0);
			pageNavigation.setPageSize(10);
			assetTypeFormDTO.setPageNavigation(pageNavigation);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.query(ctx);
			assetTypeFormDTO = (AssetTypeFormDTO) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(assetTypeFormDTO.getList())) {
				Assert.assertNotNull(assetTypeFormDTO.getList());
			} else {
				Assert.assertTrue(assetTypeFormDTO.getList().size() == 0);
			}
			
			//ctx不為空 formdto不為空 
			//查詢條件 -- 設備類別
			assetTypeFormDTO.setQueryAssetCategoryCode("EsdasdasDC");
			//查詢條件 -- 排序欄位
			assetTypeFormDTO.setSort(null);
			//查詢條件 -- 排序方式
			assetTypeFormDTO.setOrder(null);
			pageNavigation.setCurrentPage(0);
			pageNavigation.setPageSize(10);
			assetTypeFormDTO.setPageNavigation(pageNavigation);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.query(ctx);
			assetTypeFormDTO = (AssetTypeFormDTO) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(assetTypeFormDTO.getList())) {
				Assert.assertNotNull(assetTypeFormDTO.getList());
			} else {
				Assert.assertTrue(assetTypeFormDTO.getList().size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testQuery()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試初始化編輯
	 * @author HermanWang
	 * @return void
	 */
	public void testInitEdit(){
		try {
			//ctx為空
			SessionContext  ctxTest = null;
			assetTypeService.initEdit(ctxTest);
			
			//ctx不為空 formdto為空
			SessionContext  ctx =  new SessionContext();
			assetTypeService.initEdit(ctx);
			
			AssetTypeFormDTO assetTypeFormDTO = new AssetTypeFormDTO();
			
			//測試設備品相的初始化編輯 編輯id不為空  查不到數據
			assetTypeFormDTO.setEditAssetTypeId("測試ID查不到數據");
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.initEdit(ctx);
			assetTypeFormDTO = (AssetTypeFormDTO) ctx.getResponseResult();
			List<AssetTypeDTO> list = this.assetTypeDAO.listBy(null, assetTypeFormDTO.getEditAssetTypeId(), null, null);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(assetTypeFormDTO.getAssetTypeDTO());
			} else {
				Assert.assertTrue(assetTypeFormDTO.getAssetTypeDTO() == null);
			}
			
			//測試設備品相的初始化編輯 編輯id不為空 可以查到數據 
			assetTypeFormDTO.setEditAssetTypeId("1480580486159-0008");
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.initEdit(ctx);
			assetTypeFormDTO = (AssetTypeFormDTO) ctx.getResponseResult();
			list = this.assetTypeDAO.listBy(null, assetTypeFormDTO.getEditAssetTypeId(), null, null);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(assetTypeFormDTO.getAssetTypeDTO());
			} else {
				Assert.assertTrue(assetTypeFormDTO.getAssetTypeDTO() == null);
			}
			
			//測試設備品相的初始化編輯 編輯id不為空 可以查到數據 
			assetTypeFormDTO.setEditAssetTypeId("1494402932487-0014");
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.initEdit(ctx);
			assetTypeFormDTO = (AssetTypeFormDTO) ctx.getResponseResult();
			list = this.assetTypeDAO.listBy(null, assetTypeFormDTO.getEditAssetTypeId(), null, null);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(assetTypeFormDTO.getAssetTypeDTO());
			} else {
				Assert.assertTrue(assetTypeFormDTO.getAssetTypeDTO() == null);
			}
			
			//測試設備品相的初始化編輯 編輯id為空
			assetTypeFormDTO.setEditAssetTypeId("");
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.initEdit(ctx);
			assetTypeFormDTO = (AssetTypeFormDTO) ctx.getResponseResult();
			Assert.assertTrue(assetTypeFormDTO.getAssetTypeDTO() == null);
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testInitEdit()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試獲取廠商信息下拉框初始化資料
	 * @author HermanWang
	 * @return void
	 */
	public void testGetCompanyParameterList(){
		try {
			//ctx為空
			SessionContext  ctxTest = null;
			assetTypeService.getCompanyParameterList(ctxTest);
			
			//ctx不為空 param為空
			SessionContext  ctx =  new SessionContext();
			
			MultiParameterInquiryContext param = null;
			ctx.setRequestParameter(param);
			ctx = assetTypeService.getCompanyParameterList(ctx);
			List<Parameter> results = (List<Parameter>) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(ctx.getResponseResult());
			} else {
				Assert.assertTrue(results.size() == 0);
			}
			// ctx不為空 param不為空
			param = new MultiParameterInquiryContext();
			//廠商類型
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_TYPE.getValue(), IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
			//廠商編號
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_ID.getValue(), "454");
			//廠商代碼
			param.addParameter(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue(), "34534");
			//廠商簡稱
			param.addParameter(CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue(), "345");
			ctx.setRequestParameter(param);
			ctx = assetTypeService.getCompanyParameterList(ctx);
			results = (List<Parameter>) ctx.getResponseResult();
			if(!CollectionUtils.isEmpty(results)) {
				Assert.assertNotNull(ctx.getResponseResult());
			} else {
				Assert.assertTrue(results.size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testGetCompanyParameterList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試設備品相維護service的刪除方法
	 * @author HermanWang
	 * @return void
	 */
	public void testDelete(){
		try {
			//ctx為空
			SessionContext  ctxTest = null;
			assetTypeService.delete(ctxTest);
			
			//ctx為空 formdto為空
			SessionContext  ctx =  new SessionContext();
			ctx = assetTypeService.delete(ctx);
			
			//ctx為空 formdto不為空
			AssetTypeFormDTO assetTypeFormDTO = new AssetTypeFormDTO();
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473311383499-0060");
			logonUser.setName("王佳強");
			
			//測試設備品相的刪除 刪除id為空
			assetTypeFormDTO.setEditAssetTypeId("");
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.delete(ctx);
			Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
			
			//測試設備品相的刪除 刪除id不為空
			assetTypeFormDTO.setEditAssetTypeId("測試ID");
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.delete(ctx);
			DmmAssetType dmmAssetType = this.assetTypeDAO.findByPrimaryKey(DmmAssetType.class, assetTypeFormDTO.getEditAssetTypeId());
			if(dmmAssetType != null) {
				Assert.assertTrue(IAtomsMessageCode.DELETE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
			}
			
			//測試設備品相的刪除 刪除id不為空 
			assetTypeFormDTO.setEditAssetTypeId("1494402932487-0014");
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.delete(ctx);
			dmmAssetType = this.assetTypeDAO.findByPrimaryKey(DmmAssetType.class, assetTypeFormDTO.getEditAssetTypeId());
			if(dmmAssetType != null) {
				Assert.assertTrue(IAtomsMessageCode.DELETE_SUCCESS.equals(ctx.getReturnMessage().getCode()));
			} else {
				Assert.assertTrue(IAtomsMessageCode.DELETE_FAILURE.equals(ctx.getReturnMessage().getCode()));
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testDelete()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試設備品項維護的保存方法
	 * @author HermanWang
	 * @return void
	 */
	public void testSave(){
		try {
			//ctx為空
			SessionContext  ctxTest = null;
			assetTypeService.save(ctxTest);
			
			//ctx為空 formdto 為空
			SessionContext  ctx =  new SessionContext();
			assetTypeService.save(ctx);
			
			//ctx為空 formdto 不為空 assetTypeDTO 為空
			AssetTypeFormDTO assetTypeFormDTO = new AssetTypeFormDTO();
			ctx.setRequestParameter(assetTypeFormDTO);
			assetTypeService.save(ctx);
			
			//
			IAtomsLogonUser logonUser = new IAtomsLogonUser();
			logonUser.setId("1473311383499-0060");
			logonUser.setName("王佳強");
			AssetTypeDTO assetTypeDTO = new AssetTypeDTO();
			//測試新增 id為空
			//設備名稱
			assetTypeDTO.setName("不會重複的設備名稱");
			//主鍵
			assetTypeDTO.setAssetTypeId("");
			assetTypeFormDTO.setAssetTypeDTO(assetTypeDTO);
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.save(ctx);
			if(IAtomsMessageCode.INSERT_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
			
			//測試新增 id為空
			//設備名稱
			assetTypeDTO.setName("設備一");
			//主鍵
			assetTypeDTO.setAssetTypeId("");
			assetTypeFormDTO.setAssetTypeDTO(assetTypeDTO);
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.save(ctx);
			if(IAtomsMessageCode.ASSET_NAME_REPEAT.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
			
			//測試修改  id不為空
			//設備名稱 重複
			assetTypeDTO.setName("設備一");
			//主鍵
			assetTypeDTO.setAssetTypeId("1494402932487-0014");
			assetTypeFormDTO.setAssetTypeDTO(assetTypeDTO);
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.save(ctx);
			if(IAtomsMessageCode.ASSET_NAME_REPEAT.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
			
			//測試修改  id不為空
			//設備名稱 不會重複
			assetTypeDTO.setName("委屈二群无");
			//主鍵
			assetTypeDTO.setAssetTypeId("1494402932487-0014");
			assetTypeFormDTO.setAssetTypeDTO(assetTypeDTO);
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.save(ctx);
			if(IAtomsMessageCode.UPDATE_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
			
			//測試修改  id不為空
			//設備名稱 不會重複
			assetTypeDTO.setName("委屈二群无");
			//欲存儲的支援功能編號集合
			Set<String> functionIds = new HashSet<String>();
			functionIds.add("Dongle");
			functionIds.add("Pinpad");
			functionIds.add("Electronic_Signature");
			assetTypeDTO.setFunctionIds(functionIds);
			//欲存儲的廠商編號集合
			Set<String> companyIds = new HashSet<String>();
			companyIds.add("1474440222588-0125");
			companyIds.add("1473062858855-0065");
			companyIds.add("1475139958276-0141");
			assetTypeDTO.setCompanyIds(companyIds);
			//欲存儲的通訊模式編號集合
			Set<String> commModeIds = new HashSet<String>();
			commModeIds.add("TCP_IP");
			commModeIds.add("BLUETOOTH");
			commModeIds.add("Bluetooth_3G_WIFI");
			assetTypeDTO.setCommModeIds(commModeIds);
			//主鍵
			assetTypeDTO.setAssetTypeId("1494402932487-0014");
			assetTypeFormDTO.setAssetTypeDTO(assetTypeDTO);
			assetTypeFormDTO.setLogonUser(logonUser);
			ctx.setRequestParameter(assetTypeFormDTO);
			ctx = assetTypeService.save(ctx);
			if(IAtomsMessageCode.UPDATE_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testSave()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:依據設備ID獲取通訊模式
	 * @author HermanWang
	 * @return void
	 */
	public void testGetConnectionTypeList(){
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//assetTypeId 沒有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), null);
			List<Parameter> connectionTypes = assetTypeService.getConnectionTypeList(param);
			if(!CollectionUtils.isEmpty(connectionTypes)) {
				Assert.assertNotNull(connectionTypes);
			} else {
				Assert.assertTrue(connectionTypes == null || connectionTypes.size() == 0);
			}
			//assetTypeId 有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "1");
			connectionTypes = assetTypeService.getConnectionTypeList(param);
			if(!CollectionUtils.isEmpty(connectionTypes)) {
				Assert.assertNotNull(connectionTypes);
			} else {
				Assert.assertTrue(connectionTypes == null || connectionTypes.size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testGetConnectionTypeList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取內建功能
	 * @author HermanWang
	 * @return void
	 */
	public void testGetBuiltInFeature(){
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//assetTypeId 沒有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), null);
			List<Parameter> builtInFeatures = assetTypeService.getBuiltInFeature(param);
			if(!CollectionUtils.isEmpty(builtInFeatures)) {
				Assert.assertNotNull(builtInFeatures);
			} else {
				Assert.assertTrue(builtInFeatures == null || builtInFeatures.size() == 0);
			}
			
			//assetTypeId 有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "1");
			builtInFeatures = assetTypeService.getBuiltInFeature(param);
			if(!CollectionUtils.isEmpty(builtInFeatures)) {
				Assert.assertNotNull(builtInFeatures);
			} else {
				Assert.assertTrue(builtInFeatures == null || builtInFeatures.size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testGetBuiltInFeature()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:跟據設備id獲取設備廠牌
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetBrandList(){
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//assetTypeId 沒有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), null);
			List<Parameter> assetBrands = assetTypeService.getAssetBrandList(param);
			if(!CollectionUtils.isEmpty(assetBrands)) {
				Assert.assertNotNull(assetBrands);
			} else {
				Assert.assertTrue(assetBrands == null || assetBrands.size() == 0);
			}
			//assetTypeId 有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "ceshifsdfadfa");
			assetBrands = assetTypeService.getAssetBrandList(param);
			if(!CollectionUtils.isEmpty(assetBrands)) {
				Assert.assertNotNull(assetBrands);
			} else {
				Assert.assertTrue(assetBrands == null || assetBrands.size() == 0);
			}
			
			//assetTypeId 有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "1493106846009-0010");
			assetBrands = assetTypeService.getAssetBrandList(param);
			if(!CollectionUtils.isEmpty(assetBrands)) {
				Assert.assertNotNull(assetBrands);
			} else {
				Assert.assertTrue(assetBrands == null || assetBrands.size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetBrandList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取設備型號
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetModelList(){
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//assetTypeId 沒有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), null);
			List<Parameter> assetBrands = assetTypeService.getAssetModelList(param);
			if(!CollectionUtils.isEmpty(assetBrands)) {
				Assert.assertNotNull(assetBrands);
			} else {
				Assert.assertTrue(assetBrands == null || assetBrands.size() == 0);
			}
			//assetTypeId 有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "ceshifsdfadfa");
			assetBrands = assetTypeService.getAssetModelList(param);
			if(!CollectionUtils.isEmpty(assetBrands)) {
				Assert.assertNotNull(assetBrands);
			} else {
				Assert.assertTrue(assetBrands == null || assetBrands.size() == 0);
			}
			
			//assetTypeId 有值
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "1493106846009-0010");
			assetBrands = assetTypeService.getAssetModelList(param);
			if(!CollectionUtils.isEmpty(assetBrands)) {
				Assert.assertNotNull(assetBrands);
			} else {
				Assert.assertTrue(assetBrands == null || assetBrands.size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetModelList()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:根據設備類別獲取對應的設備列表
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetTypeList(){
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//設備類別
			param.addParameter(BimContractAssetDTO.ATTRIBUTE.ASSET_CATEGORY.getValue(), null);
			//合約id
			param.addParameter(BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue(), null);
			List<Parameter> assAssetTypes = assetTypeService.getAssetTypeList(param);
			if(!CollectionUtils.isEmpty(assAssetTypes)) {
				Assert.assertNotNull(assAssetTypes);
			} else {
				Assert.assertTrue(assAssetTypes == null || assAssetTypes.size() == 0);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetTypeList()", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:判斷改設備是否有庫存和設備代碼
	 * @author HermanWang
	 * @return void
	 */
	public void testIsAssetList(){
		try {
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			//合約id
			param.addParameter(AssetTypeDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue(), "0.0.0.0.");
			boolean isAssetList = assetTypeService.isAssetList(param);
			boolean flag = this.assetTypeDAO.isAssetList("0.0.0.0.");
			if(flag) {
				Assert.assertTrue(isAssetList);
			} else {
				Assert.assertTrue(!isAssetList);
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getName()+".testIsAssetList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * @return the assetTypeService
	 */
	public IAssetTypeService getAssetTypeService() {
		return assetTypeService;
	}

	/**
	 * @param assetTypeService the assetTypeService to set
	 */
	public void setAssetTypeService(IAssetTypeService assetTypeService) {
		this.assetTypeService = assetTypeService;
	}
	/**
	 * @return the assetTypeDAO
	 */
	public IAssetTypeDAO getAssetTypeDAO() {
		return assetTypeDAO;
	}
	/**
	 * @param assetTypeDAO the assetTypeDAO to set
	 */
	public void setAssetTypeDAO(IAssetTypeDAO assetTypeDAO) {
		this.assetTypeDAO = assetTypeDAO;
	}
	
}
