package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentItemDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentItem;
/**
 * Purpose: 求償項目資料表DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/14
 * @MaintenancePersonnel CarrieDuan
 */
public interface ISrmPaymentItemDAO extends IGenericBaseDAO<SrmPaymentItem>{
	
	/**
	 * Purpose: 根據求償批號獲取求償的全部信息
	 * @author CarrieDuan
	 * @param paymentId ：求償資料
	 * @param isSend : 是否為發送mail 查詢數據
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentInfoDTO>
	 */
	public List<SrmPaymentInfoDTO> listBy(String paymentId, Boolean isSend) throws DataAccessException;
	
	/**
	 * Purpose: 獲取求償列表ID
	 * @author CarrieDuan
	 * @param paymentId ：求償批號
	 * @param paymentItem:求償項目
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentItemDTO>
	 */
	public List<SrmPaymentItemDTO> getItemIds(String paymentId, String paymentItem) throws DataAccessException;
	
	/**
	 * Purpose: 根據求償批號刪除求償列表資料
	 * @author CarrieDuan
	 * @param paymentId ：求償批號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return void
	 */
	public void deletedByPaymentId(String paymentId) throws DataAccessException;
	
	/**
	 * Purpose:根據求償編號獲取求償列表資料
	 * @author CarrieDuan
	 * @param itemId
	 * @param paymentId
	 * @throws DataAccessException
	 * @return List<SrmPaymentItemDTO>
	 */
	public List<SrmPaymentItemDTO> getPaymentItemDTOByItemId(String itemId, String paymentId) throws DataAccessException;
}
