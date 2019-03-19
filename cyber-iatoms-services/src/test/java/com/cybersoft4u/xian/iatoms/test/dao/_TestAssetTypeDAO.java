package com.cybersoft4u.xian.iatoms.test.dao;

import java.util.List;

import org.springframework.util.CollectionUtils;

import junit.framework.Assert;
import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.ctc.wstx.sr.ElemAttrs;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedCommDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetSupportedFunctionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeCompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose: 測試設備品項維護的dao的單元測試
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/5/10
 * @MaintenancePersonnel HermanWang
 */
public class _TestAssetTypeDAO extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestAssetTypeDAO.class);
	/**
	 * 設備品項維護DAO注入
	 */
	public IAssetTypeDAO assetTypeDAO;

	/**
	 * 無參構造
	 */
	@SuppressWarnings("deprecation")
	public _TestAssetTypeDAO() {
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}
	/**
	 * Purpose:設備品項維護dao測試 listby
	 * @author HermanWang
	 * @return void
	 */
	public void testListBy(){
		try {
			
			//查詢條件 设备类别
			String assetCategoryCode = "";
			//查詢條件 設備編號
			String assetTypeId = "";
			//排序字段
			String sort = null;
			//排序方式
			String order = null;
			List<AssetTypeDTO> list = assetTypeDAO.listBy(assetCategoryCode, assetTypeId, sort, order);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list == null ||list.size() == 0);
			}  
			
			//查詢條件 设备类别
			assetCategoryCode = "EDC";
			//查詢條件 設備編號
			assetTypeId = "09123123";
			//排序字段
			sort = "NAME";
			//排序方式
			order = "asc";
			list = assetTypeDAO.listBy(assetCategoryCode, assetTypeId, sort, order);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list == null ||list.size() == 0);
			} 
			
			//排序字段
			sort = "NAME";
			//排序方式
			order = "asc";
			list = assetTypeDAO.listBy(assetCategoryCode, assetTypeId, sort, order);
			if(!CollectionUtils.isEmpty(list)) {
				Assert.assertNotNull(list);
			} else {
				Assert.assertTrue(list == null ||list.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListBy()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose::設備品項維護dao測試 設備數量
	 * @author HermanWang
	 * @return void
	 */
	public void testCount(){
		try {
			//查詢條件 设备类别
			String assetCategoryCode = "";
			Integer count = assetTypeDAO.count(assetCategoryCode);
			if(count != 0) {
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertTrue(count==0);
			}
			
			//查詢條件 设备类别
			assetCategoryCode = "EDC";
			count = assetTypeDAO.count(assetCategoryCode);
			if(count != 0) {
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertTrue(count==0);
			}
			
			//查詢條件 设备类别
			assetCategoryCode = "EDCdsdsdsd";
			count = assetTypeDAO.count(assetCategoryCode);
			if(count != 0) {
				Assert.assertTrue(count!=0);
			} else {
				Assert.assertTrue(count==0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testCount()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取廠商使用設備檔資料
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetTypeCompanyDTOList(){
		try {
			//查詢條件 設備類別編號
			String assetTypeId = "";
			//查詢條件 廠商編號
			String companyId = "";
			List<AssetTypeCompanyDTO> assetTypeCompanyDTOList = assetTypeDAO.getAssetTypeCompanyDTOList(assetTypeId, companyId);
			if(!CollectionUtils.isEmpty(assetTypeCompanyDTOList)) {
				Assert.assertNotNull(assetTypeCompanyDTOList);
			} else {
				Assert.assertTrue(assetTypeCompanyDTOList == null || assetTypeCompanyDTOList.size() == 0);
			} 
			
			//查詢條件 設備類別編號
			assetTypeId = "123";
			//查詢條件 廠商編號
			companyId = "123";
			assetTypeCompanyDTOList = assetTypeDAO.getAssetTypeCompanyDTOList(assetTypeId, companyId);
			if(!CollectionUtils.isEmpty(assetTypeCompanyDTOList)) {
				Assert.assertNotNull(assetTypeCompanyDTOList);
			} else {
				Assert.assertTrue(assetTypeCompanyDTOList == null || assetTypeCompanyDTOList.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetTypeCompanyDTOList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:檢查設備代碼和設別名稱是否重複
	 * @author HermanWang
	 * @return void
	 */
	public void testIsCheck(){
		try {
			//測試 新增的 設備名稱重複
			//設備代碼
			String assetTypeId = "";
			//設備名稱
			String name = "";
			boolean isCheck = assetTypeDAO.isCheck(assetTypeId, name);
			if(isCheck) {
				Assert.assertTrue(isCheck);
			} else {
				Assert.assertTrue(!isCheck);
			}
			
			//測試 修改的 設備名稱重複
			//設備代碼
			assetTypeId = "0101";
			//設備名稱
			name = "0101";
			isCheck = assetTypeDAO.isCheck(assetTypeId, name);
			if(isCheck) {
				Assert.assertTrue(isCheck);
			} else {
				Assert.assertTrue(!isCheck);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsCheck()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:測試刪除設備時候 該設備下是否有庫存
	 * @author HermanWang
	 * @return void
	 */
	public void testIsAssetList(){
		try {
			//測試 刪除設備 該設備下是否有庫存
			//設備代碼
			String editAssetTypeId = "1480580486159-0008";
			boolean hasAssetType = assetTypeDAO.isAssetList(editAssetTypeId);
			if(hasAssetType) {
				Assert.assertTrue(hasAssetType);
			} else {
				Assert.assertTrue(!hasAssetType);
			}
			//id隨機數
			editAssetTypeId = "阿瑟阿斯大所打所";
			hasAssetType = assetTypeDAO.isAssetList(editAssetTypeId);
			if(hasAssetType) {
				Assert.assertTrue(hasAssetType);
			} else {
				Assert.assertTrue(!hasAssetType);
			}
			
			//id不存在
			editAssetTypeId = "";
			hasAssetType = assetTypeDAO.isAssetList(editAssetTypeId);
			if(hasAssetType) {
				Assert.assertTrue(hasAssetType);
			} else {
				Assert.assertTrue(!hasAssetType);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsAssetList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取設備型號
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetInfo(){
		try {
			//assetTypeId :id
			String assetTypeId = "1480580486159-0008";
			//fieldName:需要查詢的列
			String fieldName = "model";
			String brand = assetTypeDAO.getAssetInfo(assetTypeId, fieldName);
			if(StringUtils.hasText(brand)) {
				Assert.assertNotNull(brand);
			} else {
				Assert.assertTrue(brand == null);
			}
			
			assetTypeId = "";
			//fieldName:需要查詢的列
			fieldName = "brand";
			brand = assetTypeDAO.getAssetInfo(assetTypeId, fieldName);
			if(StringUtils.hasText(brand)) {
				Assert.assertNotNull(brand);
			} else {
				Assert.assertTrue(brand == null);
			}
			
			assetTypeId = "1480580486159-0008";
			//fieldName:需要查詢的列
			fieldName = "";
			brand = assetTypeDAO.getAssetInfo(assetTypeId, fieldName);
			if(StringUtils.hasText(brand)) {
				Assert.assertNotNull(brand);
			} else {
				Assert.assertTrue(brand == null);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetInfo()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:依據設備獲取對應的支援功能
	 * @author HermanWang
	 * @return void
	 */
	public void testListBuiltInFeatureByAssetTypeId(){
		try {
			//assetTypeId :id
			String assetTypeId = "1480580486159tr5t-0008";
			List<Parameter> builtInFeatures = assetTypeDAO.listBuiltInFeatureByAssetTypeId(assetTypeId);
			if(!CollectionUtils.isEmpty(builtInFeatures)) {
				Assert.assertNotNull(builtInFeatures);
			} else {
				Assert.assertTrue(builtInFeatures == null || builtInFeatures.size() == 0);
			}
			
			//assetTypeId :id
			assetTypeId = "";
			builtInFeatures = assetTypeDAO.listBuiltInFeatureByAssetTypeId(assetTypeId);
			if(!CollectionUtils.isEmpty(builtInFeatures)) {
				Assert.assertNotNull(builtInFeatures);
			} else {
				Assert.assertTrue(builtInFeatures == null || builtInFeatures.size() == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListBuiltInFeatureByAssetTypeId()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取設備支援通訊模式檔資料
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetSupportedCommDTOList(){
		try {
			//查詢條件 設備類別編號
			String assetTypeId = "";
			//查詢條件 廠商編號
			String commModeId = "";
			List<AssetSupportedCommDTO> assetSupportedCommDTOList = assetTypeDAO.getAssetSupportedCommDTOList(assetTypeId, commModeId);
			if(!CollectionUtils.isEmpty(assetSupportedCommDTOList)) {
				Assert.assertNotNull(assetSupportedCommDTOList);
			} else {
				Assert.assertTrue(assetSupportedCommDTOList == null || assetSupportedCommDTOList.size() == 0);
			} 
			
			//查詢條件 設備類別編號
			assetTypeId = "123";
			//查詢條件 廠商編號
			commModeId = "123";
			assetSupportedCommDTOList = assetTypeDAO.getAssetSupportedCommDTOList(assetTypeId, commModeId);
			if(!CollectionUtils.isEmpty(assetSupportedCommDTOList)) {
				Assert.assertNotNull(assetSupportedCommDTOList);
			} else {
				Assert.assertTrue(assetSupportedCommDTOList == null || assetSupportedCommDTOList.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetSupportedCommDTOList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取設備支援通訊模式檔資料
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetSupportedFunctionDTOList(){
		try {
			//查詢條件 設備類別編號
			String assetTypeId = "";
			//查詢條件 廠商編號
			String functionId = "";
			List<AssetSupportedFunctionDTO> assetSupportedFunctionDTOList = assetTypeDAO.getAssetSupportedFunctionDTOList(assetTypeId, functionId);
			if(!CollectionUtils.isEmpty(assetSupportedFunctionDTOList)) {
				Assert.assertNotNull(assetSupportedFunctionDTOList);
			} else {
				Assert.assertTrue(assetSupportedFunctionDTOList == null || assetSupportedFunctionDTOList.size() == 0);
			} 
			
			//查詢條件 設備類別編號
			assetTypeId = "123";
			//查詢條件 廠商編號
			functionId = "123";
			assetSupportedFunctionDTOList = assetTypeDAO.getAssetSupportedFunctionDTOList(assetTypeId, functionId);
			if(!CollectionUtils.isEmpty(assetSupportedFunctionDTOList)) {
				Assert.assertNotNull(assetSupportedFunctionDTOList);
			} else {
				Assert.assertTrue(assetSupportedFunctionDTOList == null || assetSupportedFunctionDTOList.size() == 0);
			} 
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetSupportedFunctionDTOList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:獲取設備名稱列表
	 * @author HermanWang
	 * @return void
	 */
	public void testGetAssetNameList(){
		try {
			//設備品類編號
			String assetTypeId = "";
			//設備名稱
			String name = "";
			//设备类别
			String assetCategory = "";
			//合約ID
			String contractId = "";
			//是否已刪除
			String deleted = "";
			List<Parameter> assetNameList = assetTypeDAO.getAssetNameList(assetTypeId, name, assetCategory, contractId, deleted);
			if(!CollectionUtils.isEmpty(assetNameList)) {
				Assert.assertNotNull(assetNameList);
			} else {
				Assert.assertTrue(assetNameList == null || assetNameList.size() == 0);
			}
			
			//設備品類編號
			assetTypeId = "123";
			//設備名稱
			name = "123";
			//设备类别
			assetCategory = "123";
			//合約ID
			contractId = "123";
			//是否已刪除
			deleted = "123";
			assetNameList = assetTypeDAO.getAssetNameList(assetTypeId, name, assetCategory, contractId, deleted);
			if(!CollectionUtils.isEmpty(assetNameList)) {
				Assert.assertNotNull(assetNameList);
			} else {
				Assert.assertTrue(assetNameList == null || assetNameList.size() == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testGetAssetNameList()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:核檢設備名稱是否存在
	 * @author HermanWang
	 * @return void
	 */
	public void testIsCheckAssetName(){
		try {
			//測試 刪除設備 該設備下是否有庫存
			//設備名稱
			String name = "1480580486159-0008";
			boolean isCheckAssetName = assetTypeDAO.isCheckAssetName(name);
			if(isCheckAssetName) {
				Assert.assertTrue(isCheckAssetName);
			} else {
				Assert.assertTrue(!isCheckAssetName);
			}
			//id隨機數
			name = "苹果";
			isCheckAssetName = assetTypeDAO.isCheckAssetName(name);
			if(isCheckAssetName) {
				Assert.assertTrue(isCheckAssetName);
			} else {
				Assert.assertTrue(!isCheckAssetName);
			}
			
			//id不存在
			name = "";
			isCheckAssetName = assetTypeDAO.isCheckAssetName(name);
			if(isCheckAssetName) {
				Assert.assertTrue(isCheckAssetName);
			} else {
				Assert.assertTrue(!isCheckAssetName);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testIsCheckAssetName()", e);
			e.printStackTrace();
		}
	}
	/**
	 * Purpose:依據設備ID獲取對應的通訊模式
	 * @author HermanWang
	 * @return void
	 */
	public void testListConnectionTypeByAssetTypeId(){
		try {
			//assetTypeId :id
			String assetTypeId = "1480580486159tr5t-0008";
			List<Parameter> builtInFeatures = assetTypeDAO.listConnectionTypeByAssetTypeId(assetTypeId);
			if(!CollectionUtils.isEmpty(builtInFeatures)) {
				Assert.assertNotNull(builtInFeatures);
			} else {
				Assert.assertTrue(builtInFeatures == null || builtInFeatures.size() == 0);
			}
			
			//assetTypeId :id
			assetTypeId = "";
			builtInFeatures = assetTypeDAO.listConnectionTypeByAssetTypeId(assetTypeId);
			if(!CollectionUtils.isEmpty(builtInFeatures)) {
				Assert.assertNotNull(builtInFeatures);
			} else {
				Assert.assertTrue(builtInFeatures == null || builtInFeatures.size() == 0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName()+".testListConnectionTypeByAssetTypeId()", e);
			e.printStackTrace();
		}
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
