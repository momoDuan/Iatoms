package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchant;
/**
 * 
 * Purpose: 客戶特店維護DAO interface
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/6/16
 * @MaintenancePersonnel DavidZheng
 */
public interface IMerchantDAO extends IGenericBaseDAO<BimMerchant> {

	/**
	 * Purpose: 查詢
	 * @author HermanWang
	 * @param queryCustomerId:查詢的客戶id
	 * @param queryRegisteredName：查詢的特店名稱
	 * @param rows:每頁顯示的條數
	 * @param page:每頁開始頁的序號
	 * @param sort:排序
	 * @param orderby:查詢
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return List<MerchantNewDTO>:客戶特店列表
	 */
	public List<MerchantDTO> listBy(String queryCompanyId, String queryName, String queryMerchantCode, Integer rows, Integer page, String sort, String order) throws DataAccessException;
	/**
	 * Purpose: 獲得記錄總條數
	 * @author HermanWang
	 * @param queryCompanyId:查詢的客戶id
	 * @param queryName：查詢的特店名稱
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return count:總條數
	 */
	public Integer count(String queryCompanyId, String queryName, String queryMerchantCode) throws DataAccessException;
	/**
	 * Purpose:根據ID查詢一條特店信息
	 * @author HermanWang
	 * @param merchantId：特店主鍵
	 * @param merchantCode：特店代號
	 * @param companyId：公司
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 */
	public MerchantDTO getMerchantInfo(String merchantId, String merchantCode, String companyId, String companyCode) throws DataAccessException;
	/**
	 * Purpose:檢查merchantCode或name是否重複
	 * @author HermanWang
	 * @param merchantCode:特店序號
	 * @param registeredName：特店名稱
	 * @param merchantId：特店主鍵
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return Boolean：是否重複
	 */
	public boolean isCheck(String merchantCode,  String merchantId, String companyId ) throws DataAccessException;
	
	/**
	 * Purpose:判斷當前DB是否存在數據 不存在返回true存在false
	 * @author CrissZhang
	 * @throws DataAccessException:出錯拋出DataAccessException
	 * @return boolean:是否存在數據的boolean值
	 */
	public boolean isNoData() throws DataAccessException;
	/**
	 * Purpose:由倉庫code，公司id獲取倉庫集合
	 * @author amandawang
	 * @param merchantCode:倉庫code
	 * @param companyId：公司id
	 * @throws DataAccessException
	 * @return List<Parameter>
	 */
	public List<Parameter> getMerchantsByCodeAndCompamyId(String merchantCode, String companyId) throws DataAccessException;
	
	/**
	 * Purpose:刪除所有轉入數據
	 * @author CrissZhang
	 * @throws DataAccessException:出錯時, 丟出DataAccessException
	 * @return void : 無返回
	 */
	public void deleteTransferData() throws DataAccessException;
}
