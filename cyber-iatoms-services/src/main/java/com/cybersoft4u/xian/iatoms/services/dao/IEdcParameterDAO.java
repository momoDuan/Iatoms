package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.bean.Parameter;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.EdcParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcParameterFormDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewHandleInfo;

/**
 * Purpose: EDC交易參數數據訪問層接口
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016/9/22
 * @MaintenancePersonnel CrissZhang
 */
public interface IEdcParameterDAO extends IGenericBaseDAO<SrmCaseNewHandleInfo>{

	/**
	 * Purpose:EDC交易參數查詢
	 * @author CrissZhang
	 * @param edcParameterFormDTO : 查詢條件放置的對象
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<EdcParameterDTO> ： EdcParameterDTO對象集合
	 */
	public List<SrmCaseHandleInfoDTO> listBy(EdcParameterFormDTO edcParameterFormDTO) throws DataAccessException;
	
	/**
	 * Purpose:查询总笔数
	 * @author CrissZhang
	 * @param edcParameterFormDTO : 查詢條件放置的對象
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return Integer ： 總筆數
	 */
	public Integer count(EdcParameterFormDTO edcParameterFormDTO) throws DataAccessException;
	
	/**
	 * Purpose:得到交易參數所有交易項的集合
	 * @author CrissZhang
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<Parameter> : Parameter的list集合
	 */
	public List<Parameter> getItemList() throws DataAccessException;
	
	/**
	 * Purpose:根據dtid的list集合得到交易參數的項
	 * @author CrissZhang
	 * @param dtidList ： dtid的List集合
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return List<SrmCaseNewTransactionParameterDTO> ： SrmCaseNewTransactionParameterDTO的List集合
	 */
	public List<SrmCaseNewTransactionParameterDTO> getTransParamsByDtid(List<String> dtidList) throws DataAccessException;
}
