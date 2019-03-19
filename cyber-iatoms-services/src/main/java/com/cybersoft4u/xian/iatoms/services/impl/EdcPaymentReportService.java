package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.config.GenericConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.EdcPaymentReportFormDTO;
import com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
/**
 * Purpose: EDC維護費用付款報表Service
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2017/6/16
 * @MaintenancePersonnel HermanWang
 */
public class EdcPaymentReportService extends AtomicService implements IEdcPaymentReportService {
	/**
	 * 日誌記錄
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, EdcPaymentReportService.class);
	/**
	 * 
	 * Constructor:空構造
	 */
	public EdcPaymentReportService() {
	}
	/**
	 * SRM_案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext)
			throws ServiceException {
		try {
			EdcPaymentReportFormDTO edcPaymentReportFormDTO = null;
			if(sessionContext != null){
				edcPaymentReportFormDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
				if(edcPaymentReportFormDTO != null) {
					Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
					IAtomsLogonUser logonUser = (IAtomsLogonUser) edcPaymentReportFormDTO.getLogonUser();
					//當前登入者對應之公司
					String  logonUserCompanyId = logonUser.getAdmUserDTO().getCompanyId();
					//當前登入者角色屬性
					String userAttribute = null;
					//得到用戶角色列表
					List<AdmRoleDTO> userRoleList = logonUser.getUserFunctions();
					boolean isCustomerAttribute = false;
					for (AdmRoleDTO admRoleDTO : userRoleList) {
						userAttribute = admRoleDTO.getAttribute();
						//是廠商角色
						if (IAtomsConstants.VECTOR_ROLE_ATTRIBUTE.equals(userAttribute)) {
							//如果即是客戶角色又是廠商角色則設置為廠商角色
							edcPaymentReportFormDTO.setRoleAttribute(IAtomsConstants.VECTOR_ROLE_ATTRIBUTE);
							break;
						} else if (IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE.equals(userAttribute)){
							edcPaymentReportFormDTO.setRoleAttribute(IAtomsConstants.CUSTOMER_ROLE_ATTRIBUTE);
							isCustomerAttribute = true;
						} else {
							isCustomerAttribute = false;
						}
					}
					//登入者如果是客戶屬性，則顯示對應之公司信息
					if (isCustomerAttribute) {
						edcPaymentReportFormDTO.setLogonUserCompanyId(logonUserCompanyId);
					}
					sessionContext.setReturnMessage(msg);
					sessionContext.setResponseResult(edcPaymentReportFormDTO);
				}
			}
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService#queryInstall(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryInstall(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			Map map = new HashMap();
			EdcPaymentReportFormDTO edcPaymentReportFormDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
			//查詢條件 客戶id
			String queryCustomerId = edcPaymentReportFormDTO.getQueryCustomer();
			LOGGER.debug(".queryInstall() --> queryCustomerId: ", queryCustomerId);	
			//查詢條件 完成時間起
			String queryCompleteDateStart = edcPaymentReportFormDTO.getQueryCompleteDateStart();
			LOGGER.debug(".queryInstall() --> queryCompleteDateStart: ", queryCompleteDateStart);
			//查詢條件 完成時間迄
			String queryCompleteDateEnd = edcPaymentReportFormDTO.getQueryCompleteDateEnd();
			LOGGER.debug(".queryInstall() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			//查詢條件 當前頁
			Integer pageIndex = edcPaymentReportFormDTO.getPage();
			//查詢條件 每頁條數
			Integer pageSize = edcPaymentReportFormDTO.getRows();
			//排序字段
			String sort = edcPaymentReportFormDTO.getSort();
			//排序方式
			String order = edcPaymentReportFormDTO.getOrder();
			//標記
			String isInstant = edcPaymentReportFormDTO.getIsInstant();
			//條數
			List<SrmCaseHandleInfoDTO> installSrmCaseHandleInfoDTOs = this.srmCaseHandleInfoDAO.installListBy(queryCustomerId, queryCompleteDateStart, queryCompleteDateEnd, isInstant, 
					pageIndex, pageSize, sort, order);
			if(!CollectionUtils.isEmpty(installSrmCaseHandleInfoDTOs)) {
				edcPaymentReportFormDTO.setList(installSrmCaseHandleInfoDTOs);
				edcPaymentReportFormDTO.setInstallSrmCaseHandleInfoDTOs(installSrmCaseHandleInfoDTOs);
				int installCount = this.srmCaseHandleInfoDAO.installCount(queryCustomerId, queryCompleteDateStart, queryCompleteDateEnd, isInstant);
				//設置返回總條數
				edcPaymentReportFormDTO.getPageNavigation().setRowCount(installCount);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(installCount));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, installSrmCaseHandleInfoDTOs);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, installSrmCaseHandleInfoDTOs);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
			}
			//設置返回結果
			sessionContext.setResponseResult(edcPaymentReportFormDTO);
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".queryInstall() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryInstall() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService#queryUnInstall(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryUnInstall(SessionContext sessionContext)
			throws ServiceException {
		Message msg = null;
		try {
			Map map = new HashMap();
			EdcPaymentReportFormDTO edcPaymentReportFormDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
			//查詢條件 客戶id
			String queryCustomerId = edcPaymentReportFormDTO.getQueryCustomer();
			LOGGER.debug(".queryUnInstall() --> queryCustomerId: ", queryCustomerId);	
			//查詢條件 完成時間起
			String queryCompleteDateStart = edcPaymentReportFormDTO.getQueryCompleteDateStart();
			LOGGER.debug(".queryUnInstall() --> queryCompleteDateStart: ", queryCompleteDateStart);
			//查詢條件 完成時間迄
			String queryCompleteDateEnd = edcPaymentReportFormDTO.getQueryCompleteDateEnd();
			LOGGER.debug(".queryUnInstall() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			//查詢條件 當前頁
			Integer pageIndex = edcPaymentReportFormDTO.getPage();
			//查詢條件 每頁條數
			Integer pageSize = edcPaymentReportFormDTO.getRows();
			//排序字段
			String sort = edcPaymentReportFormDTO.getSort();
			//排序方式
			String order = edcPaymentReportFormDTO.getOrder();
			//標記
			String isInstant = edcPaymentReportFormDTO.getIsInstant();
			//條數
			List<SrmCaseHandleInfoDTO> unInstallSrmCaseHandleInfoDTOs = this.srmCaseHandleInfoDAO.unInstallListBy(queryCustomerId, queryCompleteDateStart,
					queryCompleteDateEnd, isInstant, pageIndex, pageSize, sort, order);
			if(!CollectionUtils.isEmpty(unInstallSrmCaseHandleInfoDTOs)) {
				edcPaymentReportFormDTO.setList(unInstallSrmCaseHandleInfoDTOs);
				edcPaymentReportFormDTO.setUnInstallSrmCaseHandleInfoDTOs(unInstallSrmCaseHandleInfoDTOs);
				int unInstallCount = this.srmCaseHandleInfoDAO.unInstallCount(queryCustomerId, 
						queryCompleteDateStart, queryCompleteDateEnd, isInstant);
				//設置返回總條數
				edcPaymentReportFormDTO.getPageNavigation().setRowCount(unInstallCount);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(unInstallCount));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, unInstallSrmCaseHandleInfoDTOs);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, unInstallSrmCaseHandleInfoDTOs);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
			}
			//設置返回結果
			sessionContext.setResponseResult(edcPaymentReportFormDTO);
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".queryUnInstall() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryUnInstall() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService#queryOther(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryOther(SessionContext sessionContext)
			throws ServiceException {
		Message msg = null;
		try {
			Map map = new HashMap();
			EdcPaymentReportFormDTO edcPaymentReportFormDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
			//查詢條件 客戶id
			String queryCustomerId = edcPaymentReportFormDTO.getQueryCustomer();
			LOGGER.debug(".queryOther() --> queryCustomerId: ", queryCustomerId);	
			//查詢條件 完成時間起
			String queryCompleteDateStart = edcPaymentReportFormDTO.getQueryCompleteDateStart();
			LOGGER.debug(".queryOther() --> queryCompleteDateStart: ", queryCompleteDateStart);
			//查詢條件 完成時間迄
			String queryCompleteDateEnd = edcPaymentReportFormDTO.getQueryCompleteDateEnd();
			LOGGER.debug(".queryOther() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			//查詢條件 當前頁
			Integer pageIndex = edcPaymentReportFormDTO.getPage();
			//查詢條件 每頁條數
			Integer pageSize = edcPaymentReportFormDTO.getRows();
			//排序字段
			String sort = edcPaymentReportFormDTO.getSort();
			//排序方式
			String order = edcPaymentReportFormDTO.getOrder();
			//標記
			String isInstant = edcPaymentReportFormDTO.getIsInstant();
			//條數
			List<SrmCaseHandleInfoDTO> otherSrmCaseHandleInfoDTOs = this.srmCaseHandleInfoDAO.otherListBy(queryCustomerId, queryCompleteDateStart, 
					queryCompleteDateEnd, isInstant, pageIndex, pageSize, sort, order);
			if(!CollectionUtils.isEmpty(otherSrmCaseHandleInfoDTOs)) {
				edcPaymentReportFormDTO.setList(otherSrmCaseHandleInfoDTOs);
				edcPaymentReportFormDTO.setOtherSrmCaseHandleInfoDTOs(otherSrmCaseHandleInfoDTOs);
				int otherCount = this.srmCaseHandleInfoDAO.otherCount(queryCustomerId, queryCompleteDateStart, queryCompleteDateEnd, isInstant);
				//設置返回總條數
				edcPaymentReportFormDTO.getPageNavigation().setRowCount(otherCount);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(otherCount));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, otherSrmCaseHandleInfoDTOs);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, otherSrmCaseHandleInfoDTOs);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
			}
			//設置返回結果
			sessionContext.setResponseResult(edcPaymentReportFormDTO);
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".queryOther() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryOther() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService#queryCheck(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryCheck(SessionContext sessionContext)
			throws ServiceException {
		Message msg = null;
		Map map = new HashMap();
		try {
			EdcPaymentReportFormDTO edcPaymentReportFormDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
			//查詢條件 客戶id
			String queryCustomerId = edcPaymentReportFormDTO.getQueryCustomer();
			LOGGER.debug(".queryCheck() --> queryCustomerId: ", queryCustomerId);	
			//查詢條件 完成時間起
			String queryCompleteDateStart = edcPaymentReportFormDTO.getQueryCompleteDateStart();
			LOGGER.debug(".queryCheck() --> queryCompleteDateStart: ", queryCompleteDateStart);
			//查詢條件 完成時間迄
			String queryCompleteDateEnd = edcPaymentReportFormDTO.getQueryCompleteDateEnd();
			LOGGER.debug(".queryCheck() --> queryCompleteDateEnd: ", queryCompleteDateEnd);
			//查詢條件 當前頁
			Integer pageIndex = edcPaymentReportFormDTO.getPage();
			//查詢條件 每頁條數
			Integer pageSize = edcPaymentReportFormDTO.getRows();
			//排序字段
			String sort = edcPaymentReportFormDTO.getSort();
			//排序方式
			String order = edcPaymentReportFormDTO.getOrder();
			//標記
			String isInstant = edcPaymentReportFormDTO.getIsInstant();
			//條數
			List<SrmCaseHandleInfoDTO> checkSrmCaseHandleInfoDTOs = this.srmCaseHandleInfoDAO.checkListBy(queryCustomerId, queryCompleteDateStart, queryCompleteDateEnd, isInstant,
					pageIndex, pageSize, sort, order);
			if(!CollectionUtils.isEmpty(checkSrmCaseHandleInfoDTOs)) {
				edcPaymentReportFormDTO.setList(checkSrmCaseHandleInfoDTOs);
				edcPaymentReportFormDTO.setCheckSrmCaseHandleInfoDTOs(checkSrmCaseHandleInfoDTOs);
				int checkCount = this.srmCaseHandleInfoDAO.checkCount(queryCustomerId, queryCompleteDateStart, queryCompleteDateEnd, isInstant);
				//設置返回總條數
				edcPaymentReportFormDTO.getPageNavigation().setRowCount(checkCount);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(checkCount));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, checkSrmCaseHandleInfoDTOs);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(0));
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, checkSrmCaseHandleInfoDTOs);
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_MSG, i18NUtil.getName(IAtomsMessageCode.DATA_NOT_FOUND));
			}
			//設置返回結果
			sessionContext.setResponseResult(edcPaymentReportFormDTO);
			sessionContext.setReturnMessage(msg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".queryCheck() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryCheck() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IEdcPaymentReportService#export(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext export(SessionContext sessionContext)
			throws ServiceException {
		try {
			if(sessionContext != null) {
				EdcPaymentReportFormDTO edcPaymentReportFormDTO = (EdcPaymentReportFormDTO) sessionContext.getRequestParameter();
				edcPaymentReportFormDTO.setIsInstant(edcPaymentReportFormDTO.getExportFlag());
				edcPaymentReportFormDTO.setPage(Integer.valueOf(-1));
				edcPaymentReportFormDTO.setRows(Integer.valueOf(-1));
				sessionContext = this.queryInstall(sessionContext);
				sessionContext = this.queryUnInstall(sessionContext);
				sessionContext = this.queryOther(sessionContext);
				sessionContext = this.queryCheck(sessionContext);
			}
		} catch (DataAccessException e) {
			LOGGER.error(".queryCheck() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryCheck() Exception:", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}
	/**
	 * @return the srmCaseHandleInfoDAO
	 */
	public ISrmCaseHandleInfoDAO getSrmCaseHandleInfoDAO() {
		return srmCaseHandleInfoDAO;
	}
	/**
	 * @param srmCaseHandleInfoDAO the srmCaseHandleInfoDAO to set
	 */
	public void setSrmCaseHandleInfoDAO(ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO) {
		this.srmCaseHandleInfoDAO = srmCaseHandleInfoDAO;
	}
}
