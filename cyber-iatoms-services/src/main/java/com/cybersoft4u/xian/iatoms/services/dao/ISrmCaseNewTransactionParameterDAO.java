package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewTransactionParameter;
/**
 * Purpose: SRM_案件最新交易參數資料檔DAO
 * @author CarrieDuan
 * @since  JDK 1.7
 * @date   2016/12/15
 * @MaintenancePersonnel CarrieDuan
 */
public interface ISrmCaseNewTransactionParameterDAO extends IGenericBaseDAO<SrmCaseNewTransactionParameter>{

	/**
	 * Purpose: 通過DTID獲取對應的最新交易參數列表
	 * @author CarrieDuan
	 * @param dtid：dtid
	 * @return List<SrmCaseNewTransactionParameterDTO>：交易參數列表
	 */
	public List<SrmCaseNewTransactionParameterDTO> listTransactionParameterDTOsByDtid(String dtid, boolean isNewHave);
	
	/**
	 * Purpose: 通過DTID獲取對應的最新交易參數列表
	 * @author amandawang
	 * @param dtid：dtid
	 * @param isHave:是否查處理中
	 * @return List<SrmCaseNewTransactionParameterDTO>：交易參數列表
	 */
	public List<SrmCaseNewTransactionParameterDTO> getTransactionParameterDTOsByDtid(String dtid, boolean isHave);
}
