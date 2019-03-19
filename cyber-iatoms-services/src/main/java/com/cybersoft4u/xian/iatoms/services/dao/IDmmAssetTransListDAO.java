package com.cybersoft4u.xian.iatoms.services.dao;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetTransList;
/**
 * Purpose: 設備轉倉明細DAO接口
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/12
 * @MaintenancePersonnel Amanda Wang
 */
public interface IDmmAssetTransListDAO extends IGenericBaseDAO<DmmAssetTransList> {
	/**
	 * Purpose:根據設備序號刪除設備入庫明細檔
	 * @author hungli
	 * @param assetTransListId :id 設備轉倉明細檔id
	 * @param assetTransId :id 設備轉倉批號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteAssetTransListById(String assetTransListId, String assetTransId) throws DataAccessException;
	
	/**
	 * Purpose:檢查轉倉清單中是否有該設備序號
	 * @author barryZhang
	 * @param serialNumber:設備序號
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return boolean:轉倉清單中是否有該設備序號
	 */
	public boolean isCheckHasSerialNumber(String serialNumber) throws DataAccessException;
	
	/**
	 * Purpose:檢查轉倉清單中是否有該設備序號
	 * @author carrieDuan
	 * @param assetTransId :转仓批号ID
	 * @param assetTransListId：新增转仓列表ID
	 * @param serialNumbers：設備序號
	 * @param comments：转仓说明
	 * @param createId：新增人员ID
	 * @param createName: 新增人员名称
	 * @throws DataAccessException
	 * @return void
	 */
	public void addDmmAssetTransList(String assetTransId, String assetTransListId, String serialNumbers, String comments, String createId, String createName) throws DataAccessException;
	/**
	 * Purpose:檢查轉倉清單中是否有該設備序號
	 * @author amandawang
	 * @param assetTransId:转仓批号ID
	 * @param serialNumber:設備序號
	 * @return boolean:轉倉清單中是否有該設備序號
	 * @throws DataAccessException
	 */
	public boolean checkSerialNumber(String assetTransId, String serialNumber) throws DataAccessException;
}
