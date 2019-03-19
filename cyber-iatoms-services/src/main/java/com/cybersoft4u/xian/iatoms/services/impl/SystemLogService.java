package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import cafe.core.bean.Message;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmSystemLoggingDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.PermissionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SystemLogFormDTO;
import com.cybersoft4u.xian.iatoms.services.ISystemLogService;
import com.cybersoft4u.xian.iatoms.services.dao.IPermissionDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISystemLogDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmSystemLogging;

/**
 * 
 * Purpose: 系統日志Service 
 * @author amandawang
 * @since  JDK 1.7
 * @date   2016年7月5日
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SystemLogService extends AtomicService implements ISystemLogService {

	/**
	 * 日志記錄
	 */
	private static final CafeLog log = CafeLogFactory.getLog(SystemLogService.class);
	
	/**
	 * 系统日志DAO
	 */
	private ISystemLogDAO systemLogDAO;
	/**
	 * 功能权限DAO
	 */
	private IPermissionDAO permissionDAO;

	/**
	 * Constructor:无參构造方法
	 */
	public SystemLogService() {
		super();
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISystemLogService#init(cafe.core.context.SessionContext)
	 */
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		try{
			SystemLogFormDTO formDTO = null;
			Message message = null;
			if(sessionContext != null){
				formDTO = (SystemLogFormDTO) sessionContext.getRequestParameter();
				message = new Message(Message.STATUS.SUCCESS,IAtomsMessageCode.INIT_PAGE_SUCCESS);
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(Exception e){
			log.error(this.getClass().getName()+".init() is error:"+e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISystemLogService#query(cafe.core.context.SessionContext)
	 */
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		try{
			List<AdmSystemLoggingDTO> results = null;
			Message message = null;
			SystemLogFormDTO formDTO = null;
			String logCategre = null;
			if(sessionContext != null){
				formDTO = (SystemLogFormDTO) sessionContext.getRequestParameter();
				if(formDTO != null){
					//查詢條件
					String queryFromDate = formDTO.getQueryFromDate();
					log.debug(this.getClass().getName() + ".query() --> queryFromDate :" + queryFromDate);
					String queryToDate = formDTO.getQueryToDate();
					log.debug(this.getClass().getName() + ".query() --> queryToDate :" + queryToDate);
					String queryAccount = formDTO.getQueryAccount();
					log.debug(this.getClass().getName() + ".query() --> queryAccount :" + queryAccount);
					String sort = formDTO.getSort();
					log.debug(this.getClass().getName() + ".query() --> sort :" + sort);
					String order = formDTO.getOrder();
					log.debug(this.getClass().getName() + ".query() --> order :" + order);
					Integer currentPage = formDTO.getPageNavigation().getCurrentPage();
					log.debug(this.getClass().getName() + ".query() --> currentPage :" + currentPage);
					Integer pageSize = formDTO.getPageNavigation().getPageSize();
					log.debug(this.getClass().getName() + ".query() --> pageSize :" + pageSize);
					//統計結果數
					Integer rowCount = this.systemLogDAO.count(queryAccount, queryFromDate, queryToDate);
					log.debug(this.getClass().getName() + ".query() --> rowCount :" + rowCount);
					if(rowCount > 0){
						//獲取查詢結果
						results = this.systemLogDAO.listBy(queryAccount, queryFromDate, queryToDate, sort, order, currentPage, pageSize);
						//bug2359 拼接記錄類別 update by 2017/09/06
						for (AdmSystemLoggingDTO admSystemLoggingDTO : results) {
							if (cafe.core.util.StringUtils.hasText(admSystemLoggingDTO.getLogCategre())
									&& admSystemLoggingDTO.getLogCategre().indexOf(".")>=0) {
								logCategre = admSystemLoggingDTO.getLogCategre().substring(0, admSystemLoggingDTO.getLogCategre().indexOf("."));
								if (IAtomsConstants.MGS_CREATE_LOG.indexOf(logCategre)>=0) {
									admSystemLoggingDTO.setLogCategreName(i18NUtil.getName(IAtomsConstants.MGS_CREATE_LOG) + (IAtomsConstants.MARK_MIDDLE_LINE) + admSystemLoggingDTO.getLogCategreName());
								} else if (IAtomsConstants.MGS_UPDATE_LOG.indexOf(logCategre)>=0) {
									admSystemLoggingDTO.setLogCategreName(i18NUtil.getName(IAtomsConstants.MGS_UPDATE_LOG) + (IAtomsConstants.MARK_MIDDLE_LINE) + admSystemLoggingDTO.getLogCategreName());
								}
							}
						}
						message = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
					}else{
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
					}
					formDTO.setList(results);
					formDTO.getPageNavigation().setRowCount(rowCount);
				}
				sessionContext.setResponseResult(formDTO);
				sessionContext.setReturnMessage(message);
			}
		}catch(DataAccessException e){
			log.error(this.getClass().getName()+".query() is error in DataAceess: "+e,e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		}catch(Exception e){
			log.error(this.getClass().getName()+".query() is error in Service: "+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISystemLogService#export(cafe.core.context.SessionContext)
	 */
	public SessionContext export(SessionContext sessionContext)
			throws ServiceException {
		try{
			SystemLogFormDTO formDTO = null;
			List<AdmSystemLoggingDTO> results = null;
			if(sessionContext != null){
				formDTO = (SystemLogFormDTO) sessionContext.getRequestParameter();
				//查詢條件
				String queryAccount = formDTO.getQueryAccount();
				log.debug(this.getClass().getName() + ".export() --> queryAccount :" + queryAccount);
				String queryFromDate = formDTO.getQueryFromDate();
				log.debug(this.getClass().getName() + ".export() --> :" + queryFromDate);
				String queryToDate = formDTO.getQueryToDate();
				log.debug(this.getClass().getName() + ".export() --> queryToDate :" + queryToDate);
				//查詢匯出資料
				results = this.systemLogDAO.listBy(queryAccount, queryFromDate, queryToDate, AdmSystemLoggingDTO.ATTRIBUTE.OPERATION_TIME.getValue(), formDTO.getOrder(), -1, -1);
				formDTO.setList(results);
				sessionContext.setResponseResult(formDTO);
			}
		}catch(DataAccessException e){
			log.error(this.getClass().getName()+".export() is error in DataAccess:"+e,e);
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE,e);
		}catch(Exception e){
			log.error(this.getClass().getName()+".export() is error in Service:"+e,e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISystemLogService#openLogDialog(cafe.core.context.SessionContext)
	 */
	public SessionContext openLogDialog(SessionContext sessionContext) throws ServiceException{
		try {
			SystemLogFormDTO formDTO = (SystemLogFormDTO)sessionContext.getRequestParameter();
			Integer logId = formDTO.getLogId();
			AdmSystemLogging admSystemLogging = (AdmSystemLogging) this.systemLogDAO.findByPrimaryKey(AdmSystemLogging.class, logId);
			if(admSystemLogging != null){
				String logContent = admSystemLogging.getContent();
				if(StringUtils.hasText(logContent)){
					formDTO.setLogContent(logContent);
				}
				formDTO.setLogCategre(admSystemLogging.getLogCategre());
				formDTO.setFunctionName(admSystemLogging.getFunctionName());
			}
			if((formDTO.getLogCategre().equals(IAtomsConstants.ACTION_INIT_DETAIL) || formDTO.getLogCategre().equals(IAtomsConstants.ACTION_SAVE_DETAIL)) && (IAtomsConstants.UC_NO_ADM_01030.equals(formDTO.getFunctionName()))){
				//獲取角色權限
				List<PermissionDTO> permissionDTOs = this.permissionDAO.listByFunctionIds(null);
				Map<String, List<String>> result = new HashMap<String, List<String>>();
				String prevType = null;
				String functionId = null;
				int length = permissionDTOs.size();
				PermissionDTO permissionDTO = null;
				List<String> editFields = null;
				for (int i = 0; i < length; i++) {
					permissionDTO = permissionDTOs.get(i);
					functionId = permissionDTO.getFunctionId();
					if (i == 0) {
						prevType = functionId;
						editFields = new ArrayList<String>();
					}
					else if (!functionId.equals(prevType)) {
						result.put(prevType, editFields);
						prevType = permissionDTO.getFunctionId();
						editFields = new ArrayList<String>();					
					}
					editFields.add(permissionDTO.getAccessRight().toLowerCase());
					if (i == length - 1) {
						result.put(prevType, editFields);
					}
				}
				formDTO.setFunctionPermissions(result);
			}
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(formDTO);
			return sessionContext;
		} catch (DataAccessException e) {
			log.error(this.getClass().getName() + ".openLogDialog() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			log.error(this.getClass().getName() + ".openLogDialog() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * @return the systemLogDAO
	 */
	public ISystemLogDAO getSystemLogDAO() {
		return systemLogDAO;
	}

	/**
	 * @param systemLogDAO the systemLogDAO to set
	 */
	public void setSystemLogDAO(ISystemLogDAO systemLogDAO) {
		this.systemLogDAO = systemLogDAO;
	}

	/**
	 * @return the permissionDAO
	 */
	public IPermissionDAO getPermissionDAO() {
		return permissionDAO;
	}

	/**
	 * @param permissionDAO the permissionDAO to set
	 */
	public void setPermissionDAO(IPermissionDAO permissionDAO) {
		this.permissionDAO = permissionDAO;
	}
	
}
