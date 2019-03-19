package com.cybersoft4u.xian.iatoms.services.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmAssetTransListDTO;

/**
 * Purpose: 設備轉倉作業DAO接口
 * @author Amanda Wang
 * @since  JDK 1.6
 * @date   2016/7/12
 * @MaintenancePersonnel Amanda Wang
 */
@SuppressWarnings("rawtypes")
public interface IDmmAssetTransInfoDAO extends IGenericBaseDAO {
	
	/**
	 * Purpose:獲取轉倉批號列表
	 * @param userId : 登錄者Id
	 * @author barryZhang
	 * @throws DataAccessException:出錯時拋出DataAccessException
	 * @return List<Parameter>：轉倉批號列表
	 */
	public List<Parameter> listBy(String userId) throws DataAccessException;
	
	/**
	 * Purpose ：獲取代確認入庫或退回的下拉列表
	 * @author CarrieDuan
	 * @param userId
	 * @throws DataAccessException
	 * @return List<Parameter>
	 */
	public List<Parameter> listToWarehouseBy(String userId) throws DataAccessException;
	
	/**
	 * Purpose:根據轉倉批號查詢到轉倉清單信息
	 * @author barryZhang
	 * @param assetTransId:轉倉批號
	 * @param isHistory : 是否查詢歷史表
	 * @param order：順序
	 * @param sort：排序列
	 * @param page：頁碼
	 * @param rows：一頁顯示多少行
	 * @throws DataAccessException
	 * @return List<AssetTransListDTO>
	 */
	public List<DmmAssetTransListDTO> listBy(String assetTransId, Boolean isHistory, String order, String sort, Integer page, Integer rows, Boolean historyFlag, Boolean isShowStatus) throws DataAccessException;
	
	/**
	 * Purpose:跟據轉倉批號查詢到轉倉清單的總比數
	 * @author barryZhang
	 * @param assetTransId:轉倉批號
	 * @param isHistory : 是否查詢歷史表
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return Integer：查詢的轉倉清單總筆數
	 */
	public Integer getCount(String assetTransId, Boolean isHistory) throws DataAccessException;
	
	/**
	 * Purpose:根據轉倉批號刪除轉倉清單信息
	 * @author barryZhang
	 * @param assetTransId：轉倉批號
	 * @throws DataAccessException：出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteAssetTransListByAssetTransId(String assetTransId) throws DataAccessException;
	
	/**
	 * Purpose:根據轉倉批號得到轉倉信息
	 * @author barryzhang
	 * @param assetTransId：轉倉批號
	 * @throws DataAccessException：出錯時，丟出DataAccessException
	 * @return AssetTransInfoDTO：轉倉信息DTO
	 */
	public DmmAssetTransInfoDTO getAssetTransInfoById(String assetTransId) throws DataAccessException;
	
	/**
	 * Purpose:取得角色為“倉管”且具有轉入倉檢視資料權限且狀態為正常的使用者帳號資料
	 * @author barryzhang
	 * @param wareHouseId：轉入倉Id
	 * @param hasWarehouserManager：是否倉庫控管
	 * @throws DataAccessException：出錯時，丟出DataAccessException
	 * @return List<Parameter>：通知人員列表
	 */
	public List<Parameter> getWareHouseUserNameList(String wareHouseId, Boolean hasWarehouserManager) throws DataAccessException;
	
	/**
	 * Purpose: 設備轉倉-確認轉倉與取消轉倉
	 * @author CarrieDuan
	 * @param assetTransId ：設備轉倉ID
	 * @param status ：設備狀態
	 * @param isListDone ：是否確認轉倉
	 * @param isCancel ：是否取消轉倉
	 * @param historyId ：歷史庫存ID
	 * @param userId ：當前登錄者ID
	 * @param userName ：當前登錄者名稱
	 * @param transAction :動作
	 * @param toMailId :通知人員id
	 * @throws DataAccessException：出錯時，丟出DataAccessException
	 * @return void
	 */
	public void updateAssetTransInfo(String assetTransId, String status, String isListDone, String isCancel, String historyId, String userId, String userName, String transAction, String toMailId, String transStatus) throws DataAccessException;

	/**
	 * Purpose:查詢歷史轉倉記錄
	 * @author CarrieDuan
	 * @param userId 當前登錄者ID
	 * @param queryFromDateStart 轉出時間起
	 * @param queryFromDateEnd	轉出時間訖
	 * @param queryToDateStart  轉入時間起
	 * @param queryToDateEnd	轉入時間訖
	 * @param queryFromWarehouseId 轉出倉
	 * @param queryToWarehouseId轉入倉
	 * @return List<Parameter> :轉倉批號清單
	 */
	public List<Parameter> getAssetInfoList(String userId, Timestamp queryFromDateStart, Timestamp queryFromDateEnd, Timestamp queryToDateStart, Timestamp queryToDateEnd,
			String queryFromWarehouseId, String queryToWarehouseId);
	
	/**
	 * Purpose:轉倉資料清除
	 * @author CrissZhang
	 * @param deleteDate : 刪除日期
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void
	 */
	public void deleteAssetTransOut(Date deleteDate) throws DataAccessException;
}
