package com.cybersoft4u.xian.iatoms.test.service;

import junit.framework.Assert;
import cafe.core.bean.identity.LogonUser;
import cafe.core.context.SessionContext;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetTransInfoFormDTO;
import com.cybersoft4u.xian.iatoms.services.IAssetTransferService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.AdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.impl.WarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTransInfo;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
import com.cybersoft4u.xian.iatoms.test.dao._TestDmmRepositoryDAO;
/**
 * Purpose: 設備轉倉作業service單元測試
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/5/16
 * @MaintenancePersonnel ElvaHe
 */
public class _TestAssetTransferService extends AbstractTestCase {
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestAssetTransferService.class);
	
	/**
	 * 注入轉倉作業DAO
	 */
	private IAssetTransferService assetTransferService;
	/**
	 * admUserDAO
	 */
	private WarehouseDAO warehouseDAO;
	
	/**
	 * 無參構造
	 */
	public _TestAssetTransferService(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose:測試查詢轉入驗收方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testQueryTransInfo(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testQueryTransInfo()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試保存設備轉倉單主檔方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testSave(){
		try {
			SessionContext ctx = new SessionContext();
			AssetTransInfoFormDTO assetTransInfoFormDTO = new AssetTransInfoFormDTO();
			DmmAssetTransInfoDTO assetTransInfoDTO = new DmmAssetTransInfoDTO();
			
			
			//有轉倉批號時
			assetTransInfoDTO.setAssetTransId("TN201610080002");
			assetTransInfoFormDTO.setAssetTransInfoDTO(assetTransInfoDTO);
			ctx.setRequestParameter(assetTransInfoFormDTO);
			ctx = this.assetTransferService.save(ctx);
			/*if (IAtomsMessageCode.SAVE_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertFalse(false);
			}*/
			
			//無轉倉批號主鍵
			assetTransInfoDTO.setAssetTransId("");
			assetTransInfoFormDTO.setAssetTransInfoDTO(assetTransInfoDTO);
			ctx.setRequestParameter(assetTransInfoFormDTO);
			ctx = this.assetTransferService.save(ctx);
			/*if (IAtomsMessageCode.SAVE_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false);
			}*/
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testSave()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試確認入庫/轉倉退回的保存方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testSaveTranferCheck(){
		try {
			SessionContext ctx = new SessionContext();
			AssetTransInfoFormDTO assetTransInfoFormDTO = new AssetTransInfoFormDTO();
			//轉倉批號
			String queryAssetTransId = "TN201704050001";
			//入庫
			String option = "";
			String toWareHouseId = "1484037704443-0013";
			if (StringUtils.hasText(toWareHouseId)) {
				BimWarehouse bimWarehouse = this.warehouseDAO.findByPrimaryKey(BimWarehouse.class, toWareHouseId);
				if (bimWarehouse != null) {
					if (IAtomsConstants.YES.equals(bimWarehouse.getDeleted())) {
						ctx.setRequestParameter(assetTransInfoFormDTO);
						ctx = this.assetTransferService.saveTranferCheck(ctx);
						/*if(IAtomsMessageCode.TRANS_DELETED_TO_WAREHOUSE.equals(ctx.getReturnMessage().getCode())){
							Assert.assertTrue(true);
						} else {
							Assert.assertTrue(false);
						}*/
					} else {
						ctx.setRequestParameter(assetTransInfoFormDTO);
						ctx = this.assetTransferService.saveTranferCheck(ctx);
						/*if(IAtomsMessageCode.TRANS_SUCCESS.equals(ctx.getReturnMessage().getCode())){
							Assert.assertTrue(true);
						} else {
							Assert.assertTrue(false);
						}*/
					}
				}
			}
			//退回
			option = "isBack";
			ctx.setRequestParameter(assetTransInfoFormDTO);
			ctx = this.assetTransferService.saveTranferCheck(ctx);
			/*if (IAtomsMessageCode.TRANS_BACK_SUCCESS.equals(ctx.getReturnMessage().getCode())) {
				System.out.println("1");
				Assert.assertTrue(true);
			} else {
				System.out.println("2");
				Assert.assertTrue(false);
			}*/
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testSaveTranferCheck()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試待轉倉設備明細方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testQuery(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testQuery()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試設備轉倉單主檔邏輯刪除方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testDelete(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testDelete()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試增加設備轉倉明細集合方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testAddAssetTransList(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testAddAssetTransList()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試刪除設備轉倉明細方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testDeleteAssetTransList(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testDeleteAssetTransList()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試確認轉倉方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testTransferWarehouse(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testTransferWarehouse()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試取消轉倉方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testCancleTransferWarehouse(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testCancleTransferWarehouse()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試文件匯入方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testUpload(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testUpload()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試轉倉主檔資料方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetAssetTransInfoDTOByAssetTransId(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testGetAssetTransInfoDTOByAssetTransId()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試更新轉倉明細方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testUpdate(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testUpdate()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試列印出庫單方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testExportAsset(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testExportAsset()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試得到通知倉管人員下拉框方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetWareHouseUserNameList(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testGetWareHouseUserNameList()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試轉倉批號查詢方法
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetAssetInfoList(){
		try {
			
		} catch (Exception e) {
			LOGGER.error("_TestAssetTransferService.testGetAssetInfoList()", "is error :", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the assetTransferService
	 */
	public IAssetTransferService getAssetTransferService() {
		return assetTransferService;
	}

	/**
	 * @param assetTransferService the assetTransferService to set
	 */
	public void setAssetTransferService(IAssetTransferService assetTransferService) {
		this.assetTransferService = assetTransferService;
	}

	/**
	 * @return the warehouseDAO
	 */
	public WarehouseDAO getWarehouseDAO() {
		return warehouseDAO;
	}

	/**
	 * @param warehouseDAO the warehouseDAO to set
	 */
	public void setWarehouseDAO(WarehouseDAO warehouseDAO) {
		this.warehouseDAO = warehouseDAO;
	}
	
}
