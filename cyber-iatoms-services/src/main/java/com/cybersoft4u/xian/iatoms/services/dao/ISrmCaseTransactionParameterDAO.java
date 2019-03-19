package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTransactionParameter;
/**
 * Purpose:案件交易參數 DAO interface
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月16日
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmCaseTransactionParameterDAO  extends IGenericBaseDAO<SrmCaseTransactionParameter>{

	/**
	 * Purpose:根據案件編號得到交易參數的列表
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @param isHistory ： 是否查歷史表
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionParameterDTO> ：交易參數列表
	 */
	public List<SrmCaseTransactionParameterDTO> listByCaseId(String caseId, String isHistory) throws DataAccessException;
	/**
	 * Purpose:根據案件編號得到交易參數的列表
	 * @author amandawang
	 * @param caseIds ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionParameterDTO> ：交易參數列表
	 */
	public List<SrmCaseTransactionParameterDTO> listByCaseIds(String caseIds)throws DataAccessException;
	/**
	 * Purpose:刪除之前的交易參數
	 * @author CrissZhang
	 * @param caseId ： 案件編號
	 * @throws DataAccessException ： 出錯時拋出DataAccessException
	 * @return void
	 */
	public void deleteAll(String caseId) throws DataAccessException;
	
	/**
	 * Purpose:驗證案件是否有交易參數
	 * @param caseIds：案件編號集合
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionParameterDTO> ：含交易參數列表的案件列表
	 */
	public List<SrmCaseTransactionParameterDTO> listByCaseIdHaveTransParam(List<String> caseIds) throws DataAccessException;
	/**
	 * Purpose:查詢atoms版交易參數
	 * @param caseIds：案件編號集合
	 * @throws DataAccessException： 出錯時拋出DataAccessException
	 * @return List<SrmCaseTransactionParameterDTO> ：交易參數列表
	 */
	public List<SrmCaseTransactionParameterDTO> getTransactionParameterByCaseIds(String caseIds)throws DataAccessException;
}
