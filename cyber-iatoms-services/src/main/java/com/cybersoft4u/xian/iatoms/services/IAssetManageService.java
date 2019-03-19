package com.cybersoft4u.xian.iatoms.services;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.service.IAtomicService;
import cafe.core.service.ServiceException;

import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
/**
 * Purpose: 設備管理作業Service接口
 * @author amandawang
 * @since  JDK 1.6
 * @date   2016/7/22
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings("rawtypes")
public interface IAssetManageService extends IAtomicService {
	
	/**
	 * Purpose:查詢設備管理作業
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext query(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢庫存設備詳細信息
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext detail(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:查詢庫存歷史詳細信息
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext history(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:庫存修改頁面初始化
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext initEdit(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:由庫存序號獲取庫存集合
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext getRepositoryByAssetId(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:庫存資料修改保存庫存歷史
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext update(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:庫存資料修改
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext edit(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:獲得庫存歷史檔DTO
	 * @author amandawang
	 * @param inquiryContext:ajax上下文inquiryContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return DmmRepositoryHistoryDTO:庫存歷史檔DTO 
	 */
	public DmmRepositoryHistoryDTO getRepositoryHistDTOByHistId(MultiParameterInquiryContext inquiryContext) throws ServiceException; 
	
	/**
	 * Purpose:獲取郵箱
	 * @author amandawang
	 * @param inquiryContext:ajax上下文inquiryContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return Parameter
	 */
	public AdmUserDTO getEmailByUserId(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	
	/**
	 * Purpose:檢核財產編號是否重複
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext
	 */
	public SessionContext check(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:檢核DTID是否重複
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext
	 */
	public SessionContext checkDtid(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:列印出庫單
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext
	 */
	public SessionContext exportAsset(SessionContext sessionContext) throws ServiceException;


	/**
	 * Purpose: 由部門獲取人員
	 * @author amandawang
	 * @param inquiryContext ： ajax傳入參數
	 * @throws ServiceException:出錯時, 丟出ServiceException
	 * @return void ： 不返回
	 */
	public List<Parameter> getUserByDept(MultiParameterInquiryContext inquiryContext) throws ServiceException ;
	/**
	 * Purpose:查詢EDC
	 * @author echomou
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext queryEDC(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:查詢人員頁面初始化
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext initSearchEmployee(SessionContext sessionContext) throws ServiceException;

	/**
	 * Purpose:查詢特店初始化
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext initMid(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose: 核檢使用人是否等於台新
	 * @author CarrieDuan
	 * @param inquiryContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	/*public SessionContext checkAssetUserIsTaixinRent(MultiParameterInquiryContext inquiryContext) throws ServiceException;*/
	/**
	 * Purpose: 查詢庫存數量
	 * @author amandawang
	 * @param inquiryContext： ajax傳入參數
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return Integer
	 */
	public Integer countAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException;
	/**
	 * Purpose: 借用單返回消息
	 * @author amandawang
	 * @param inquiryContext： ajax傳入參數
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return Integer
	 */
	/*public String getAssetIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException;*/
	/**
	 * Purpose:發送查詢結果mail
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext sendQueryMail(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:掃碼槍－根據設備序號獲取條數
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext getCountByAsset(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:查詢驗證返回消息
	 * @author CarrieDuan
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext getAssetIdList(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:修改設備狀態
	 * @author amandawang
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext changeAssetStatus(SessionContext sessionContext) throws ServiceException;
	/**
	 * Purpose:獲取借用明細資料
	 * @author nicklin
	 * @param sessionContext:上下文SessionContext
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return SessionContext:上下文SessionContext 
	 */
	public SessionContext exportBorrowDetail(SessionContext sessionContext) throws ServiceException;
	
	/**
	 * Purpose:設備借用－核檢輸入的設備序號是否正確
	 * @author CarrieDuan
	 * @param inquiryContext: 參數
	 * @throws ServiceException:出錯時，丟出ServiceException
	 * @return boolean
	 */
	public boolean checkBorrowSerialNumber(MultiParameterInquiryContext inquiryContext) throws ServiceException;
}
