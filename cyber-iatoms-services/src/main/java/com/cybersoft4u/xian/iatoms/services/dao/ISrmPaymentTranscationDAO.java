package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmPaymentTranscationDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmPaymentTranscation;
/**
 * Purpose: 求償處理記錄檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2017/2/16
 * @MaintenancePersonnel CarrieDuan
 */
public interface ISrmPaymentTranscationDAO extends IGenericBaseDAO<SrmPaymentTranscation>{
	
	/**
	 * Purpose: 依據求償編號獲取歷程
	 * @author CarrieDuan 
	 * @param paymentId ：求償編號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return List<SrmPaymentTranscationDTO>：求償處理記錄檔集合
	 */
	public List<SrmPaymentTranscationDTO> listBy(String paymentId) throws DataAccessException;
	
	/**
	 * Purpose:刪除求償歷程資料
	 * @author CarrieDuan
	 * @param paymentId：求償編號
	 * @throws DataAccessException：出错时抛出：DataAccessExecption
	 * @return void
	 */
	public void deleteByPaymentId(String paymentId) throws DataAccessException;
}
