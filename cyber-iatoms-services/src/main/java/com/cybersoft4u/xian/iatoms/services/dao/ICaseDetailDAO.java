package com.cybersoft4u.xian.iatoms.services.dao;

import com.cybersoft4u.xian.iatoms.services.dmo.CaseDetail;

import cafe.core.dao.DataAccessException;
import cafe.workflow.dao.IAbstractCaseDetailDAO;

/**
 * Purpose:案件资料DAO interface
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016/09/12
 * @MaintenancePersonnel RiverJin
 */
public interface ICaseDetailDAO extends IAbstractCaseDetailDAO<CaseDetail> {
	public long getSeqNo(String type, String attributeValue) throws DataAccessException;
	/**
	 * Purpose:案件匯入-手動保存案件资料
	 * @author CarrieDuan
	 * @param caseAssetFunction ：案件资料
	 * @throws DataAccessException ：出錯時拋出DataAccessException
	 * @return void
	 */
	public void saveCaseDetailInfo (CaseDetail caseDetail) throws DataAccessException;
	
	/**
	 * Purpose：根據案件編號刪除流程相關的信息
	 * @author ElvaHe
	 * @param caseIds：案件編號組成的字符串
	 * @return void
	 * @throws DataAccessException：出錯時拋出DataAccessException異常
	 */
	public void deleteCaseInfos(String caseIds) throws DataAccessException;
}
