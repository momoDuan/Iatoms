package com.cybersoft4u.xian.iatoms.services.dao.impl;


import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;

import cafe.core.bean.CoreMessageCode;
import cafe.core.bean.identity.LogonUser;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.support.SqlQueryBean;
import cafe.core.dmo.SequenceNumberControl;
import cafe.core.dmo.SequenceNumberControlId;
import cafe.core.service.ServiceException;
import cafe.core.util.CoreConstants;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.workflow.bean.dto.AbstractCaseDetailDTO;
import cafe.workflow.bean.dto.CaseFormDTO;
import cafe.workflow.dao.impl.AbstractCaseDetailDAO;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.services.dao.ICaseDetailDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.CaseDetail;

/**
 * Purpose: 案件资料DAO IMPL
 * @author RiverJin
 * @since  JDK 1.6
 * @date   2016/09/12
 * @MaintenancePersonnel RiverJin
 */
public class CaseDetailDAO extends AbstractCaseDetailDAO<CaseDetail> implements ICaseDetailDAO {
	private static final Log log = LogFactory.getLog(CaseDetailDAO.class);
	public static final String MESSAGE_CASE_DETAIL					 	= "案件明細資料";
	
	/**
	 * Constructor:口构造函数
	 */
	public CaseDetailDAO() {
			super();
	}
    
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.dao.IAbstractCaseDetailDAO#createNewCase(cafe.workflow.bean.dto.CaseFormDTO)
	 */
	public CaseDetail createNewCase(CaseFormDTO formDTO) throws DataAccessException{
		if (formDTO == null) {
			throw new ServiceException(CoreMessageCode.ARGUMENT_IS_NULL, new String[]{"案件明細"});
		}
		try {
			// DTO to DMO
			LogonUser logonUser = formDTO.getLogonUser();
			
			/*CaseDetailDTO caseDetailDTO = (CaseDetailDTO) formDTO.getCaseDetail(); 
			if (!StringUtils.hasText(caseDetailDTO.getId())) {	
				String caseId = this.generateCaseId();
				caseDetailDTO.setId(caseId);
			}
			formDTO.setCaseId(caseDetailDTO.getId());*/
			
			CaseDetail caseDetail = new CaseDetail();
			caseDetail.setId(formDTO.getCaseId());
			caseDetail.setProcessId(formDTO.getProcessId());
			caseDetail.setActivityCode(formDTO.getNextActivityCode());
			caseDetail.setStatus(formDTO.getStatus());
			caseDetail.setAction(formDTO.getActionId());
			caseDetail.setAcquiredBy(logonUser.getUserCode());
			
			caseDetail.setCreatedById(logonUser.getUserCode());
			caseDetail.setCreatedByName(logonUser.getName());
			caseDetail.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
			this.getDaoSupport().save(caseDetail);
			this.getDaoSupport().flush();
			log.debug("Exiting CaseDetailDAOImpl.insertNewCase() " + caseDetail);
			return caseDetail;
		} catch (Throwable e) {
			throw new DataAccessException(CoreMessageCode.INSERT_FAILURE, new String[]{MESSAGE_CASE_DETAIL}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.dao.IAbstractCaseDetailDAO#generateCaseId(cafe.workflow.bean.dto.CaseFormDTO)
	 */
	public String generateCaseId(CaseFormDTO formDTO) throws DataAccessException {
		log.debug("start to generate case id.."); 
		if (formDTO == null || formDTO.getCaseDetail() == null) throw new ServiceException(CoreMessageCode.ARGUMENT_IS_NULL, new String[]{""});
		return this.generateCaseId();
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.dao.IAbstractCaseDetailDAO#findByCaseId(java.lang.String)
	 */
	public CaseDetail findByCaseId(String caseId) throws DataAccessException{
		log.debug("Entering CaseDetailDAOImpl.findCaseDetailByCaseId(): " + caseId);
		if (!StringUtils.hasText(caseId)) throw new DataAccessException(CoreMessageCode.ARGUMENT_IS_NULL, new String[]{"案件編號"});
		try{
			Object o = this.getDaoSupport().findById(CaseDetail.class, caseId);
			this.getDaoSupport().evict(o);
			if (o!=null)
				return (CaseDetail)o;
			log.debug("Exit CaseDetailDAOImpl.findCaseDetailByCaseId(): " + caseId);
			return null;	
		}catch(Exception e){
			throw new DataAccessException(e);
		}	
	}

	/**
	 * (non-Javadoc)
	 * @see cafe.workflow.dao.IAbstractCaseDetailDAO#generateCaseId(cafe.workflow.dmo.AbstractCaseDetail)
	 */
	public String generateCaseId(CaseDetail caseDetail) throws DataAccessException {
		log.debug("start to generate case id.."); 
		if (caseDetail == null) throw new ServiceException(CoreMessageCode.ARGUMENT_IS_NULL, new String[]{""});
		return this.generateCaseId();
	}
	/**
	 * Purpose:产生caseId
	 * @author candicechen
	 * @throws DataAccessException:出错时丢出DataAccessException
	 * @return String:返回CaseId
	 */
	public String generateCaseId() throws DataAccessException {
		try{
    		StringBuffer buffer = new StringBuffer();
    		//流水號每月從00001重新編號,允許斷號.
    		String yearMonth = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(), CoreConstants.DT_FMT_YYYYMM);
    		buffer.append(yearMonth);
			long seqNo = this.getSeqNo("CASE_ID", yearMonth);
    		buffer.append(StringUtils.toFixString(5, seqNo));
			return buffer.toString();
		}catch(Exception e){
			throw new DataAccessException(CoreMessageCode.DB_UNKNOWN_ERROR,e);
		}
	}
	/**
	 * Purpose:插入sequenceNumberControl數據
	 * @author evanliu
	 * @param sequenceNumberControl:dmo
	 * @throws DataAccessException:出錯後，拋出DataAccessException
	 * @return void
	 */
	private void insertSeqNo(SequenceNumberControl sequenceNumberControl)  throws DataAccessException {
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("INSERT INTO ").append(schema).append(".SEQNO_CONTROL(SEQ_NO_TYPE, ATTR_VALUE, SEQ_NO)VALUES(:seqNoType, :attrValue, :seqNo)");
			sqlQueryBean.setParameter("seqNoType", sequenceNumberControl.getId().getType());
			sqlQueryBean.setParameter("attrValue", sequenceNumberControl.getId().getAttributeValue());
			sqlQueryBean.setParameter("seqNo", sequenceNumberControl.getSeqNo());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			log.error("insertSeqNo() error(生成caseId，插入SequenceNumberControl 出錯):", e);
			throw new DataAccessException(CoreMessageCode.DB_UNKNOWN_ERROR,e);
		}
	}
	
	/**
	 * Purpose: 修改sequenceNumberControl數據
	 * @author evanliu
	 * @param sequenceNumberControl:dmo
	 * @throws DataAccessException:出錯後，拋出DataAccessException
	 * @return void
	 */
	private void updateSeqNo(SequenceNumberControl sequenceNumberControl)  throws DataAccessException {
		try {
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("UPDATE ").append(schema).append(".SEQNO_CONTROL SET SEQ_NO=:seqNo WHERE SEQ_NO_TYPE=:seqNoType AND ATTR_VALUE=:attrValue");
			sqlQueryBean.setParameter("seqNoType", sequenceNumberControl.getId().getType());
			sqlQueryBean.setParameter("attrValue", sequenceNumberControl.getId().getAttributeValue());
			sqlQueryBean.setParameter("seqNo", sequenceNumberControl.getSeqNo());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			log.error("insertSeqNo() error(生成caseId，插入SequenceNumberControl 出錯):", e);
			throw new DataAccessException(CoreMessageCode.DB_UNKNOWN_ERROR,e);
		}
	}
	/**
	 * 取得流水序號。決定流水序號的屬性值可為單一屬性或複合屬性，若為複合屬性，其值需組合
	 * ,格式可為attr1Value-attr2Value-...例如:不同業務區有不同流水序號，且序號每月重新開始，
	 * 則其屬性值可為北一區-200908
	 * @param type 流水序號類型,若為案件編號，則值為CASE_ID,否則為表格名稱
	 * @param attributeValue 決定流水序號的屬性值
	 * @return 流水序號
	 * @throws DataAccessException
	 */
	@Override
	public long getSeqNo(String type, String attributeValue) throws DataAccessException {
		Long seqNo = null;
		SequenceNumberControl snc = null;
		long currentTime = System.currentTimeMillis();
		try {
			String sequenceSqlStatement = this.getSequenceSql(type);
			if (StringUtils.hasText(sequenceSqlStatement)) {
				try {
					List<BigDecimal> result = this.getDaoSupport().findByNativeSql(sequenceSqlStatement);
					this.getDaoSupport().flush();
		    		seqNo = result.get(0).longValue();
		    		return seqNo;
				}catch(Exception e) {
					log.error("get sequence number is failed:"+e, e);
					throw e;
				}
			}
			SequenceNumberControlId id = new SequenceNumberControlId(type, attributeValue);
			seqNo = new Long(0);
			synchronized (seqNo){
				try {
					try {
						snc = (SequenceNumberControl)this.getDaoSupport().load(SequenceNumberControl.class, id, LockMode.UPGRADE);
						if (snc != null && snc.getSeqNo() == null) snc.setSeqNo((long)0);
					} catch (Throwable e) {
						log.debug("load persistant object sequence number is failed:"+e, e);
						snc = null;
					}
					if (snc == null) {
						snc = new SequenceNumberControl(id, (long)1);
						this.insertSeqNo(snc);
					}else {
						seqNo = snc.getSeqNo() + 1;
						snc.setSeqNo(seqNo);
						this.updateSeqNo(snc);
					}	
					this.getDaoSupport().flush();
					return seqNo;
				}catch (Throwable ee) {
					log.error("get sequence number is failed:"+ee, ee);
					throw new DataAccessException(CoreMessageCode.GET_SEQNO_IS_FAILED, ee);
				}
			}
		}catch (Throwable ee) {
				log.error("get sequence number is failed:"+ee, ee);
				//throw new DataAccessException(CoreMessageCode.GET_SEQNO_IS_FAILED, ee);
				return getSimpleSeqNo();
		}
		finally {
				try {
					if (snc != null) this.getDaoSupport().attachClean(snc);
				}catch(Throwable e) {}
				log.debug("get sequence number("+seqNo+") of "+type+" 'time ("+(System.currentTimeMillis()-currentTime)+" ms)");
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICaseDetailDAO#saveCaseDetailInfo(com.cybersoft4u.xian.iatoms.services.dmo.CaseDetail)
	 */
	public void saveCaseDetailInfo(CaseDetail caseDetail)throws DataAccessException {
		try{
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			String schema = this.getMySchema();
			sqlQueryBean.append("INSERT INTO ");
			sqlQueryBean.append(schema).append(".CAFE_CASE_DETAIL");
			sqlQueryBean.append(IAtomsConstants.MARK_BRACKETS_LEFT);
			sqlQueryBean.append("CASE_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_TYPE").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("PROCESS_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_STAGE").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_TITLE").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_STATUS").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_SUBSTATUS").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_ACTION").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ASSIGNER_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("ASSIGNER_NAME").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CASE_ACQUIRED_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CREATED_BY_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CREATED_BY_NAME").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("CREATED_DATE").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("UPDATE_BY_ID").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("UPDATE_BY_NAME").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append("UPDATE_DATE");
			sqlQueryBean.append(IAtomsConstants.MARK_BRACKETS_RIGHT);
			sqlQueryBean.append(" VALUES").append(IAtomsConstants.MARK_BRACKETS_LEFT);
			sqlQueryBean.append(":id").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":caseType").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":processId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":activityCode").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":activityName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":status").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":substatus").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":action").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":assignerId").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":assignerName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":acquiredBy").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":createdById").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":createdByName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":createdDate").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":updatedById").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":updatedByName").append(IAtomsConstants.MARK_SEPARATOR);
			sqlQueryBean.append(":updatedDate");
			sqlQueryBean.append(IAtomsConstants.MARK_BRACKETS_RIGHT);
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_CASE_ID, StringUtils.hasText(caseDetail.getId())?caseDetail.getId():"");
			sqlQueryBean.setParameter("caseType", StringUtils.hasText(caseDetail.getCaseType())?caseDetail.getCaseType():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_PROCESS_ID, StringUtils.hasText(caseDetail.getProcessId())?caseDetail.getProcessId():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_ACTIVITY_CODE, StringUtils.hasText(caseDetail.getActivityCode())?caseDetail.getActivityCode():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_ACTIVITY_NAME, StringUtils.hasText(caseDetail.getActivityName())?caseDetail.getActivityName():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_STATUS, StringUtils.hasText(caseDetail.getStatus())?caseDetail.getStatus():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_SUBSTATUS, StringUtils.hasText(caseDetail.getSubstatus())?caseDetail.getSubstatus():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_ACTION, StringUtils.hasText(caseDetail.getAction())?caseDetail.getAction():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_ASSIGNER_ID, StringUtils.hasText(caseDetail.getAssignerId())?caseDetail.getAssignerId():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_ASSIGNER_NAME, StringUtils.hasText(caseDetail.getAssignerName())?caseDetail.getAssignerName():"");
			sqlQueryBean.setParameter("acquiredBy", StringUtils.hasText(caseDetail.getAcquiredBy())?caseDetail.getAcquiredBy():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_CREATED_BY_ID, StringUtils.hasText(caseDetail.getCreatedById())?caseDetail.getCreatedById():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_CREATED_BY_NAME, StringUtils.hasText(caseDetail.getCreatedByName())?caseDetail.getCreatedByName():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_CREATED_DATE, caseDetail.getCreatedDate() != null?caseDetail.getCreatedDate().toString():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_UPDATED_BY_ID, StringUtils.hasText(caseDetail.getUpdatedById())?caseDetail.getUpdatedById():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_UPDATED_BY_NAME, StringUtils.hasText(caseDetail.getUpdatedByName())?caseDetail.getUpdatedByName():"");
			sqlQueryBean.setParameter(AbstractCaseDetailDTO.FIELD_UPDATED_DATE, caseDetail.getUpdatedDate() != null?caseDetail.getUpdatedDate().toString():"");
			log.debug("saveCaseDetailInfo()" + sqlQueryBean);
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		}catch(Exception e){
			log.error("SrmCaseAssetFunctionDAO---->saveCaseDetailInfo", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.dao.ICaseDetailDAO#deleteCaseInfos(java.lang.String)
	 */
	public void deleteCaseInfos(String caseIds) throws DataAccessException {
		log.debug("CaseDetailDAO---->deleteCaseInfos()：parameter - caseIds = " + caseIds);
		try {
			String schema = this.getMySchema();
			SqlQueryBean sqlQueryBean = new SqlQueryBean();
			StringBuffer stringBuffer = new StringBuffer();
			//拼接刪除CAFE_CASE_DETAIL表數據的SQL
			stringBuffer.append("DELETE FROM ").append(schema).append(".CAFE_CASE_DETAIL WHERE CASE_ID in ").append(IAtomsConstants.MARK_BRACKETS_LEFT)
				.append(caseIds).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
			sqlQueryBean.append(stringBuffer.toString());
			//刪除之前的數據，拼接刪除CAFE_CASE_TRANSACTION表數據的SQL
			stringBuffer.delete(0, stringBuffer.length());
			stringBuffer.append("DELETE FROM ").append(schema).append(".CAFE_CASE_TRANSACTION WHERE CASE_ID in ").append(IAtomsConstants.MARK_BRACKETS_LEFT)
				.append(caseIds).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
			sqlQueryBean.append(stringBuffer.toString());
			//打印SQL
			log.debug("CaseDetailDAO---->deleteCaseInfos()：SQL = " + sqlQueryBean.toString());
			this.getDaoSupport().updateByNativeSql(sqlQueryBean);
		} catch (Exception e) {
			log.error("CaseDetailDAO---->deleteCaseInfos() is error", e);
			throw new DataAccessException(CoreMessageCode.DATA_ACCESS_FAILURE, e);
		}
	}
}