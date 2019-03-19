package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.List;

import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiLogDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.ApiLogFormDTO;
import com.cybersoft4u.xian.iatoms.services.IApiLogService;
import com.cybersoft4u.xian.iatoms.services.dao.IApiLogDAO;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose: 客訴管理Service 
 * @author	NickLin
 * @since	JDK 1.7
 * @date	2018/03/06
 * @MaintenancePersonnel CyberSoft
 */
public class ApiLogService extends AtomicService implements IApiLogService {

	/**
	 * 系统log物件  
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.DAO, ApiLogService.class);
	
	/**
	 * 客訴管理DAO
	 */
	private IApiLogDAO apiLogDAO;
	
	/**
	 * Constructor:無參數建構子
	 */
	public ApiLogService() {
		super();
	}
	
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApiLogService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		ApiLogFormDTO apiLogFormDTO = null;
		try {
			apiLogFormDTO = (ApiLogFormDTO) sessionContext.getRequestParameter();
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS));
			sessionContext.setResponseResult(apiLogFormDTO);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+".init():" + e, e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IApiLogService#query(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext query(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		Message msg = null;
		try {
			ApiLogFormDTO apiLogFormDTO = (ApiLogFormDTO) sessionContext.getRequestParameter();
			if (sessionContext != null){
				//查詢條件
				String queryClientCode = apiLogFormDTO.getQueryClientCode();
				String queryStartDate = apiLogFormDTO.getQueryStartDate();
				String queryEndDate = apiLogFormDTO.getQueryEndDate();
				Integer pageIndex = apiLogFormDTO.getPage();
				Integer pageSize = apiLogFormDTO.getRows();
				String orderby = apiLogFormDTO.getOrder();
				String sort = apiLogFormDTO.getSort();
				Integer count = null;
				count = this.apiLogDAO.count(queryClientCode, queryStartDate, queryEndDate);
				if (count.intValue() > 0) {
					//查詢結果List
					List<ApiLogDTO> apiLogDTOs = this.apiLogDAO.listBy(queryClientCode, queryStartDate, 
							queryEndDate, pageSize, pageIndex, sort, orderby);
					apiLogFormDTO.getPageNavigation().setRowCount(count.intValue());
					apiLogFormDTO.setList(apiLogDTOs);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
				}
				sessionContext.setReturnMessage(msg);
				sessionContext.setResponseResult(apiLogFormDTO);
			}
		} catch (DataAccessException e) {
			LOGGER.error(this.getClass().getName() + ".query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + ".query() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * @return the apiLogDAO
	 */
	public IApiLogDAO getApiLogDAO() {
		return apiLogDAO;
	}

	/**
	 * @param apiLogDAO the apiLogDAO to set
	 */
	public void setApiLogDAO(IApiLogDAO apiLogDAO) {
		this.apiLogDAO = apiLogDAO;
	}

}
