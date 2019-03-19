package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.List;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.StringUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.RepoHistoryFormDTO;
import com.cybersoft4u.xian.iatoms.services.IRepoHistoryService;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;

/**
 * Purpose: 設備歷史記錄查詢
 * @author ericdu
 * @since  JDK 1.7
 * @date   2016年7月20日
 * @MaintenancePersonnel ericdu
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RepoHistoryService extends AtomicService implements
		IRepoHistoryService {

	/**
	 * 系統日志記錄
	 */
	private static final CafeLog log = CafeLogFactory.getLog(RepoHistoryService.class);
	
	/**
	 * 設備歷史記錄DAO層
	 */
	private IDmmRepositoryHistoryDAO dmmRepositoryHistoryDAO;
	
	/**
	 * Constructor: 無參構造
	 */
	public RepoHistoryService() {
			super();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IRepoHistoryService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		try{
			RepoHistoryFormDTO formDTO = null;
			if(sessionContext != null){
				formDTO = (RepoHistoryFormDTO) sessionContext.getRequestParameter();
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			log.error(this.getClass().getName()+".init() is error: "+e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IRepoHistoryService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		try{
			List<DmmRepositoryHistoryDTO> results = null;
			Message message = null;
			RepoHistoryFormDTO formDTO = null;
			if(sessionContext != null){
				formDTO = (RepoHistoryFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					//查詢條件
					String querySerialNumber = formDTO.getQuerySerialNumber();
					log.error(this.getClass().getName()+".query() --> querySerialNumber :"+querySerialNumber);
					String queryTID = formDTO.getQueryTID();
					log.error(this.getClass().getName()+".query() --> queryTID :"+queryTID);
					String queryDTID = formDTO.getQueryDTID();
					log.error(this.getClass().getName()+".query() --> queryDTID :"+queryDTID);
					//拼接'%',後置模糊查詢
					if(StringUtils.hasText(querySerialNumber)) {
						querySerialNumber += IAtomsConstants.MARK_PERCENT;
					}
					if(StringUtils.hasText(queryTID)) {
						queryTID += IAtomsConstants.MARK_PERCENT;
					}
					if(StringUtils.hasText(queryDTID)) {
						queryDTID += IAtomsConstants.MARK_PERCENT;
					}
					String sort = formDTO.getSort();
					log.error(this.getClass().getName()+".query() --> sort :"+sort);
					String order = formDTO.getOrder();
					log.error(this.getClass().getName()+".query() --> order :"+order);
					//統計結果數
					Integer rowCount = this.dmmRepositoryHistoryDAO.count(null, querySerialNumber, queryTID, queryDTID);
					formDTO.getPageNavigation().setRowCount(rowCount);
					log.error(this.getClass().getName()+".query() --> rowCount :"+rowCount);
					Integer currentPage = formDTO.getPageNavigation().getCurrentPage();
					log.error(this.getClass().getName()+".query() --> currentPage :"+currentPage);
					Integer pageSize = formDTO.getPageNavigation().getPageSize();
					log.error(this.getClass().getName()+".query() --> pageSize :"+pageSize);
					if(rowCount > 0){
						//獲取查詢結果
						results = this.dmmRepositoryHistoryDAO.list(null, querySerialNumber, queryTID, queryDTID, sort, order, currentPage, pageSize);
						message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
					}else{
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
					}
					formDTO.setList(results);
				}
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(Exception e){
			log.error(this.getClass().getName()+".query() is error:"+e,e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IRepoHistoryService#export(cafe.core.context.SessionContext)
	 */
	public SessionContext export(SessionContext sessionContext)
			throws ServiceException {
		try{
			RepoHistoryFormDTO formDTO = null;
			List<DmmRepositoryHistoryDTO> results = null;
			DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = null;
			if(sessionContext != null){
				formDTO = (RepoHistoryFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					String[] histIds = formDTO.getHistIds();
					if(histIds != null && histIds.length > 0){
						results = new ArrayList<DmmRepositoryHistoryDTO>();
						for(String histId : histIds){
							dmmRepositoryHistoryDTO = this.dmmRepositoryHistoryDAO.list(histId, null, null, null, null, null, -1, -1).get(0);
							results.add(dmmRepositoryHistoryDTO);
						}
					}
					formDTO.setList(results);
				}
				sessionContext.setResponseResult(formDTO);
			}
		}catch(Exception e){
			log.error(this.getClass().getName()+".export() is error:"+e,e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		}
		return sessionContext;
	}

	/**
	 * @return the dmmRepositoryHistoryDAO
	 */
	public IDmmRepositoryHistoryDAO getRepoHistoryDAO() {
		return dmmRepositoryHistoryDAO;
	}

	/**
	 * @param dmmRepositoryHistoryDAO the dmmRepositoryHistoryDAO to set
	 */
	public void setRepoHistoryDAO(IDmmRepositoryHistoryDAO dmmRepositoryHistoryDAO) {
		this.dmmRepositoryHistoryDAO = dmmRepositoryHistoryDAO;
	}
}
