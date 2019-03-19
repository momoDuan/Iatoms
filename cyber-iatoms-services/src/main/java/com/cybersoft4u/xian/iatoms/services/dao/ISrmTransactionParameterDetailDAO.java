package com.cybersoft4u.xian.iatoms.services.dao;

import java.util.List;

import cafe.core.dao.DataAccessException;
import cafe.core.dao.IGenericBaseDAO;

import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterDetailDTO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmTransactionParameterDetail;
/**
 * Purpose:SRM_交易參數項目 DAO 
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2016年12月14日
 * @MaintenancePersonnel CrissZhang
 */
public interface ISrmTransactionParameterDetailDAO extends IGenericBaseDAO<SrmTransactionParameterDetail>{
	/**
	 * Purpose:查找所有的交易參數項目明細
	 * @author CrissZhang
	 * @param transactionType:交易類別
	 * @param paramterItemId：參數項目編號
	 * @param isEdit：是否可以編輯
	 * @param versionDate: 新建案傳入當前時間，否則傳入案件起案日
	 * @throws DataAccessException：出錯後，丟出DataAccessException
	 * @return List<SrmTransactionParameterDetailDTO>：交易參數項目明細list
	 */
	public List<SrmTransactionParameterDetailDTO> listby(String transactionType, String paramterItemId,String isEdit, String versionDate) throws DataAccessException;
	/**
	 * Purpose:獲取交易參數list
	 * @author HermanWang
	 * @throws DataAccessException：出錯後，丟出DataAccessException
	 * @return List<SrmTransactionParameterDetailDTO>：交易參數項目明細list
	 */
	public List<SrmTransactionParameterDetailDTO> getSrmTransactionParameterDetailDTOList() throws DataAccessException;
}
