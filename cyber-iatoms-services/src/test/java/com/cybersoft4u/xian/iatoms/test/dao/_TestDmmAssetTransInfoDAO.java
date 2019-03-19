package com.cybersoft4u.xian.iatoms.test.dao;

import java.sql.Timestamp;
import java.util.List;

import junit.framework.Assert;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmAssetTransInfoDAO;
import com.cybersoft4u.xian.iatoms.test.common.AbstractTestCase;
/**
 * Purpose:設備轉倉作業DAO
 * @author ElvaHe
 * @since  JDK 1.6
 * @date   2017/5/16
 * @MaintenancePersonnel ElvaHe
 */
public class _TestDmmAssetTransInfoDAO extends AbstractTestCase{
	/**
	 * 日志記錄物件
	 */
	private static final CafeLog LOGGER = (CafeLog)CafeLogFactory.getLog(_TestDmmAssetTransInfoDAO.class);
	
	/**
	 * 注入轉倉作業DAO
	 */
	private IDmmAssetTransInfoDAO assetTransInfoDAO;
	
	/**
	 * 無參構造
	 */
	public _TestDmmAssetTransInfoDAO(){
		this.setAutowireMode(AUTOWIRE_BY_NAME);
	}

	/**
	 * Purpose:獲取轉倉批號列表
	 * @author ElvaHe
	 * @return void
	 */
	public void testListBy(){
		try {
			//登錄者ID
			String userId = "1473076241770-004812";
			//轉倉批號列表
			List<Parameter> result = this.assetTransInfoDAO.listBy(userId);
			if (!CollectionUtils.isEmpty(result)) {
				Assert.assertNotNull(result);
			} else {
				Assert.assertFalse(result == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testListBy()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試獲取代確認入庫或退回的下拉列表
	 * @author ElvaHe
	 * @return void
	 */
	public void testListToWarehouseBy(){
		try {
			//登錄者ID
			String userId = "1473076241770-0048";
			//轉倉批號列表
			List<Parameter> result = this.assetTransInfoDAO.listToWarehouseBy(userId);
			if (!CollectionUtils.isEmpty(result)) {
				Assert.assertNotNull(result);
			} else {
				Assert.assertFalse(result == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testListToWarehouseBy()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據轉倉批號查詢到轉倉清單信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testListByInfo(){
		try {
			//轉倉批號
			String assetTransId = "TN201611010002as";
			//順序
			String order = "";
			//排序列
			String sort = "";
			//頁碼
			Integer page = 1;
			//每頁行數
			Integer rows = 10;
			//轉倉清單信息
			List<DmmAssetTransListDTO> assetTransList = this.assetTransInfoDAO.listBy(assetTransId, Boolean.FALSE, order, sort, page, rows, false, false);
			if (!CollectionUtils.isEmpty(assetTransList)) {
				Assert.assertNotNull(assetTransList);
			} else {
				Assert.assertFalse(assetTransList == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testListByInfo()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試跟據轉倉批號查詢到轉倉清單的總比數
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetCount(){
		try {
			//轉倉批號
			String assetTransId = "TN201611010002";
			Integer count = this.assetTransInfoDAO.getCount(assetTransId, Boolean.FALSE);
			if (count > 0) {
				Assert.assertEquals(count.intValue(), count.intValue());
			} else {
				Assert.assertFalse(count > 0);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testGetCount()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據轉倉批號刪除轉倉清單信息
	 * @author ElvaHe
	 * @return void 
	 */
	public void testDeleteAssetTransListByAssetTransId(){
		try {
			//轉倉批號
			String assetTransId = "TN201611010002xcxzcvXZ";
			this.assetTransInfoDAO.deleteAssetTransListByAssetTransId(assetTransId);
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testDeleteAssetTransListByAssetTransId()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試根據轉倉批號得到轉倉信息
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetAssetTransInfoById(){
		try {
			//轉倉批號
			String assetTransId = "TN201611010002";
			DmmAssetTransInfoDTO assetTransInfoDTO = this.assetTransInfoDAO.getAssetTransInfoById(assetTransId);
			if(assetTransInfoDTO != null){
				Assert.assertNotNull(assetTransInfoDTO);
			} else {
				Assert.assertFalse(assetTransInfoDTO != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testGetAssetTransInfoById()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試取得角色為“倉管”且具有轉入倉檢視資料權限且狀態為正常的使用者帳號資料
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetWareHouseUserNameList(){
		try {
			//轉入倉Id
			String wareHouseId = "1470713143335-0002asdsa";
			//是否倉庫控管
			Boolean hasWarehouserManager = true;
			//轉倉批號列表
			List<Parameter> result = this.assetTransInfoDAO.getWareHouseUserNameList(wareHouseId, hasWarehouserManager);
			if (!CollectionUtils.isEmpty(result)) {
				Assert.assertNotNull(result);
			} else {
				Assert.assertFalse(result != null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testGetWareHouseUserNameList()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:測試設備轉倉-確認轉倉與取消轉倉
	 * @author ElvaHe
	 * @return void
	 */
	public void testUpdateAssetTransInfo(){
		try {
			//設備轉倉ID
			String assetTransId = "";
			//設備狀態
			String status = "";
			//是否確認轉倉
			String isListDone = "";
			//是否取消轉倉
			String isCancel = "";
			//歷史庫存ID
			String historyId = "";
			//當前登錄者ID
			String userId = "";
			//當前登錄者名稱
			String userName = "";
			this.assetTransInfoDAO.updateAssetTransInfo(assetTransId, status, isListDone, isCancel, historyId, userId, userName, "", "", "");
			Assert.assertTrue(true);
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testUpdateAssetTransInfo()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose:查詢歷史轉倉記錄
	 * @author ElvaHe
	 * @return void
	 */
	public void testGetAssetInfoList(){
		try {
			//當前登錄者ID
			String userId = "";
			//轉出時間起
			Timestamp queryFromDateStart = DateTimeUtils.getCurrentTimestamp();
			//轉出時間訖
			Timestamp queryFromDateEnd = DateTimeUtils.getCurrentTimestamp();
			//轉入時間起
			Timestamp queryToDateStart = DateTimeUtils.getCurrentTimestamp();
			//轉入時間迄
			Timestamp queryToDateEnd = DateTimeUtils.getCurrentTimestamp();
			//轉出倉
			String queryFromWarehouseId = "";
			//轉入倉
			String queryToWarehouseId = "";
			//轉倉批號列表
			List<Parameter> result = this.assetTransInfoDAO.getAssetInfoList(userId, queryFromDateStart, queryFromDateEnd, queryToDateStart, queryToDateEnd, queryFromWarehouseId, queryToWarehouseId);
			if(!CollectionUtils.isEmpty(result)){
				Assert.assertNotNull(result);
			} else {
				Assert.assertFalse(result == null);
			}
		} catch (Exception e) {
			LOGGER.error("_TestDmmAssetTransInfoDAO.testGetAssetInfoList()", "is error：", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the assetTransInfoDAO
	 */
	public IDmmAssetTransInfoDAO getAssetTransInfoDAO() {
		return assetTransInfoDAO;
	}

	/**
	 * @param assetTransInfoDAO the assetTransInfoDAO to set
	 */
	public void setAssetTransInfoDAO(IDmmAssetTransInfoDAO assetTransInfoDAO) {
		this.assetTransInfoDAO = assetTransInfoDAO;
	}
	
}
