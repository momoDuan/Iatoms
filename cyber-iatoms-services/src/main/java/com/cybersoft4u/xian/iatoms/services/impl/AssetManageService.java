package com.cybersoft4u.xian.iatoms.services.impl;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.exception.CommonException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.BeanUtils;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;
import cafe.workflow.config.WfSystemConfigManager;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmRoleDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApiCmsRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.AssetManageFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.CrossTabReportDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.CSVUtils;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.FtpClient;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsUtils;
import com.cybersoft4u.xian.iatoms.common.utils.PasswordEncoderUtilities;
import com.cybersoft4u.xian.iatoms.common.utils.ReportExporter;
import com.cybersoft4u.xian.iatoms.common.utils.mail.MailComponent;
import com.cybersoft4u.xian.iatoms.services.IAssetManageService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IApiCmsRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryFaultComDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryFaultDescDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.ApiCmsRepository;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepository;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultCom;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultComId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultDesc;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultDescId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistory;
/**
 * Purpose: 設備管理作業Service
 * @author amandawang
 * @since  JDK 1.6
 * @date   2016/7/22
 * @MaintenancePersonnel amandawang
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AssetManageService extends AtomicService implements IAssetManageService, ApplicationContextAware{

	/**
	 * 系统日志文件控件
	 */
	private static CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, AssetManageService.class);
	
	/**
	 * Constructor:無參構造函數
	 */
	public AssetManageService() {
		super();
	}

	/**
	 * 庫存主檔DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	
	/**
	 * 庫存歷史檔DAO
	 */
	private IDmmRepositoryHistoryDAO dmmRepositoryHistDAO;
	
	/**
	 * 發送mail組件
	 */
	private MailComponent mailComponent;

	/**
	 * 使用者DAO
	 */
	private IAdmUserDAO admUserDAO;
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	
	/**
	 * 用戶倉庫鏈接DAO
	 */
	private IAdmUserWarehouseDAO admUserWarehouseDAO;
	/**
	 * 設備庫存故障組件DAO
	 */
	private IDmmRepositoryFaultComDAO repositoryFaultComDAO;
	/**
	 * 設備庫存故障現象DAO
	 */
	private IDmmRepositoryFaultDescDAO repositoryFaultDescDAO;
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * 案件處理記錄
	 */
	private ISrmCaseTransactionDAO srmCaseTransactionDAO;
	/**
	 * 案件處理中設備連接檔
	 */
	private ISrmCaseAssetLinkDAO srmCaseAssetLinkDAO;
	/**
	 * SRM_案件最新設備連接檔DAO
	 */
	private ISrmCaseNewAssetLinkDAO srmCaseNewAssetLinkDAO;
	/**
	 * SRM_案件處理最新資料檔DAO
	 */
	private ISrmCaseNewHandleInfoDAO srmCaseNewHandleInfoDAO;
	/**
	 * 部門維護DAO
	 */
	private IDepartmentDAO departmentDAO;
	/**
	 * CMS通知IATOMS設備DAO
	 */
	private IApiCmsRepositoryDAO apiCmsRepositoryDAO;
	/**
	 * 特點DAO
	 */
	private IMerchantDAO merchantDAO;
	/**
	 * ApplicationContext對象
	 */
	private ApplicationContext applicationContext;
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#query(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext query(SessionContext sessionContext) throws ServiceException {
		long startTime = System.currentTimeMillis();
		//庫存清單集合
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		//庫存清單總條數
		Integer count = null;
		Message msg = null;
		String userId = null;
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//獲取用戶ID
			userId = logonUser.getId();
			//獲取登入者角色權限
			List<AdmRoleDTO> roleCodes =logonUser.getUserFunctions();
			
			if(!CollectionUtils.isEmpty(roleCodes)) {
				String roleFlag = IAtomsConstants.PARAM_YES;
				//如果包含廠商角色，則按廠商角色查詢
				for (AdmRoleDTO admRoleDTO : roleCodes) {
					if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
						roleFlag = IAtomsConstants.PARAM_NO;
						break;
					} 
				}
				//廠商角色
				if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
					boolean hasWarehouse = false;
					if (IAtomsConstants.PARAM_YES.equals(logonUser.getAdmUserDTO().getDataAcl())) {
						hasWarehouse = true;
					}
					
					//未設定控管權限，則可查詢所有倉庫之設備資料
					if (!hasWarehouse) {
						formDTO.setUserId(null);
					}
				} else {
					//客戶角色--顯示登入者所屬公司之設備資料
					String customerId = logonUser.getAdmUserDTO().getCompanyId();
					formDTO.setCompanyId(customerId);
					formDTO.setUserId(null);
				}
			}
			//如果是掃碼槍查詢
			if ((DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(formDTO.getCodeGunFlag())
					|| DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(formDTO.getCodeGunFlag()))
					&& formDTO.getTotalSize()!= null) {
				count = formDTO.getTotalSize();
			} else {
				//查詢庫存清單總條數
				count = this.repositoryDAO.count(formDTO);
			}
			
			if (count.intValue() > 0) {
				//如果是掃碼槍查詢
				/*if (DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(formDTO.getCodeGunFlag())
						|| DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(formDTO.getCodeGunFlag())) {
					int a = count.intValue();
					int b = formDTO.getRows();
					int c = a%b > 0 ? a/b + 1 : a/b;
					formDTO.setPage(c);
				}*/
				//查詢集合
				dmmRepositoryDTOs = this.repositoryDAO.listBy(formDTO);
				if (CollectionUtils.isEmpty(dmmRepositoryDTOs) && count > 0) {
					formDTO.setPage(formDTO.getPage() - 1);
					dmmRepositoryDTOs = this.repositoryDAO.listBy(formDTO);
				} 
				if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
					 for (DmmRepositoryDTO dmmRepositoryDTO : dmmRepositoryDTOs) {
						if (StringUtils.hasText(dmmRepositoryDTO.getActionValue())) {
							if (dmmRepositoryDTO.getActionValue().indexOf("-") != -1) {
								dmmRepositoryDTO.setAction(i18NUtil.getName(dmmRepositoryDTO.getActionValue().substring(0, dmmRepositoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryDTO.getAction());
							}
						}
					}
					formDTO.setList(dmmRepositoryDTOs);
				}
			}
			formDTO.setUserId(userId);
			//設置返回總條數
			formDTO.getPageNavigation().setRowCount(count.intValue());
			//設置返回結果
			sessionContext.setResponseResult(formDTO);
			//設置返回消息
			if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			long endTime = System.currentTimeMillis();
			LOGGER.debug(this.getClass().getName() + "query", " end ", (endTime - startTime));
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.query() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.query() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#detail(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext detail(SessionContext sessionContext)
			throws ServiceException {
		try {
			long startTime = System.currentTimeMillis();
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			String assetId = formDTO.getQueryAssetId();
			formDTO.setUserId(null);
			String historyId = formDTO.getQueryHistId();
			if (StringUtils.hasText(assetId)) {
				//如果查詢歷史檔，則庫存歷史ID有值，返回詳情為歷史資料詳情
				if (StringUtils.hasText(historyId)) {
					DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO =this.repositoryDAO.getListByAssetId(formDTO).get(0);
					if (dmmRepositoryHistoryDTO != null) {
						// bug2272 update by 2017/08/28
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getActionValue())) {
							if (dmmRepositoryHistoryDTO.getActionValue().indexOf("-") != -1) {
								dmmRepositoryHistoryDTO.setAction(i18NUtil.getName(dmmRepositoryHistoryDTO.getActionValue().substring(0, dmmRepositoryHistoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryHistoryDTO.getAction());
							}
						}
						formDTO.setRepositoryHistDTO(dmmRepositoryHistoryDTO);
					}
				//如果查詢庫存檔，則庫存歷史ID無值，返回詳情為庫存資料詳情	
				} else {
					DmmRepositoryDTO dmmRepositoryDTO = this.repositoryDAO.listBy(formDTO).get(0);
					if (dmmRepositoryDTO != null) {
						// bug2272 update by 2017/08/28
						if (StringUtils.hasText(dmmRepositoryDTO.getActionValue())) {
							if (dmmRepositoryDTO.getActionValue().indexOf("-") != -1) {
								dmmRepositoryDTO.setAction(i18NUtil.getName(dmmRepositoryDTO.getActionValue().substring(0, dmmRepositoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryDTO.getAction());
							}
						}
						formDTO.setDmmRepositoryDTO(dmmRepositoryDTO);
					}
				}
				//獲得庫存主檔詳情
				sessionContext.setResponseResult(formDTO);
				long endTime = System.currentTimeMillis();
				LOGGER.debug(this.getClass().getName() + "detail", " end ", (endTime - startTime));
				return sessionContext;
			}
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.detail(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.detail(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#history(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext history(SessionContext sessionContext)
			throws ServiceException {
		long startTime = System.currentTimeMillis();
		AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
		//庫存清單歷史集合
		List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = null;
		//庫存歷史清單總條數
		Integer count = null;
		Message msg = null;
		try {
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//獲取登入者角色權限
			List<AdmRoleDTO> roleCodes =logonUser.getUserFunctions();
			if(!CollectionUtils.isEmpty(roleCodes)) {
				String roleFlag = IAtomsConstants.PARAM_YES;
				//如果包含廠商角色，則按廠商角色查詢
				for (AdmRoleDTO admRoleDTO : roleCodes) {
					if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
						roleFlag = IAtomsConstants.PARAM_NO;
						break;
					} 
				}
				//廠商角色
				if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
					boolean hasWarehouse = false;
					if (IAtomsConstants.PARAM_YES.equals(logonUser.getAdmUserDTO().getDataAcl())) {
						hasWarehouse = true;
					}
					//未設定控管權限，則可查詢所有倉庫之設備資料
					if (!hasWarehouse) {
						formDTO.setUserId(null);
					}
				} else {
					//客戶角色--顯示登入者所屬公司之設備資料
					String customerId = logonUser.getAdmUserDTO().getCompanyId();
					formDTO.setCompanyId(customerId);
					formDTO.setUserId(null);
				}
			}
			//如果是掃碼槍查詢
			if ((DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(formDTO.getCodeGunFlag())
					|| DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(formDTO.getCodeGunFlag()))
					&& formDTO.getTotalSize()!= null) {
				count = formDTO.getTotalSize();
			} else {
				//查詢庫存歷史清單總條數
				count = this.repositoryDAO.getCountByAssetId(formDTO);
			}
			if (count.intValue() > 0) {
				//如果是掃碼槍查詢
				
				//查詢集合
				dmmRepositoryHistoryDTOs = this.repositoryDAO.getListByAssetId(formDTO);
				//bug2272 update by 2017/08/28
				if (!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOs)) {
					for (DmmRepositoryHistoryDTO dmmRepositoryDTO : dmmRepositoryHistoryDTOs) {
						if(StringUtils.hasText(dmmRepositoryDTO.getActionValue())) {
							if (dmmRepositoryDTO.getActionValue().indexOf("-") != -1) {
								dmmRepositoryDTO.setAction(i18NUtil.getName(dmmRepositoryDTO.getActionValue().substring(0, dmmRepositoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryDTO.getAction());
							}
						}
					}
					formDTO.setRepositoryHistDTOs(dmmRepositoryHistoryDTOs);
				} 
			}
			//設置返回總條數
			formDTO.setTotalSize(count);
			//設置返回結果
			sessionContext.setResponseResult(formDTO);
			//設置返回消息
			if (!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOs)) {
				msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			} else {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DATA_NOT_FOUND);
			}
			sessionContext.setReturnMessage(msg);
			long endTime = System.currentTimeMillis();
			LOGGER.debug(this.getClass().getName() + "history", " end ", (endTime - startTime));
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.history() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.history() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#getRepositoryHistDTOByHistId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public DmmRepositoryHistoryDTO getRepositoryHistDTOByHistId(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO = null;
		try {
			String histId = (String) inquiryContext.getParameter(DmmRepositoryHistoryDTO.ATTRIBUTE.HISTORY_ID.getValue());
			AssetManageFormDTO assetManageFormDTO =new AssetManageFormDTO();
			assetManageFormDTO.setQueryHistId(histId);
			//Task #2675 案件編號，若沒有對應案件可檢視，不要提供超連結 2017/10/23
			assetManageFormDTO.setQueryCaseFlag("Y");
			//獲取庫存歷史DTO
			dmmRepositoryHistoryDTO = this.repositoryDAO.getListByAssetId(assetManageFormDTO).get(0);
			if(StringUtils.hasText(dmmRepositoryHistoryDTO.getActionValue())) {
				if (dmmRepositoryHistoryDTO.getActionValue().indexOf("-") != -1) {
					dmmRepositoryHistoryDTO.setAction(i18NUtil.getName(dmmRepositoryHistoryDTO.getActionValue().substring(0, dmmRepositoryHistoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryHistoryDTO.getAction());
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.getRepositoryHistDTOByHistId() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.getRepositoryHistDTOByHistId() is failed!!!  ", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		}
		return dmmRepositoryHistoryDTO;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#initEdit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initEdit(SessionContext sessionContext)
			throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			String assetId = formDTO.getQueryAssetId();
			AssetManageFormDTO assetManageFormDTO = new AssetManageFormDTO();
			//如果存在則賦值
			if (StringUtils.hasText(assetId)) {
				assetManageFormDTO.setQueryAssetId(assetId);
				//查找該設備相關資料
				DmmRepositoryDTO dmmRepositoryDTO = this.repositoryDAO.listBy(assetManageFormDTO).get(0);
				if (dmmRepositoryDTO != null) {
					formDTO.setDmmRepositoryDTO(dmmRepositoryDTO);
				}
				sessionContext.setResponseResult(formDTO);
				return sessionContext;
			}
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.initEdit(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.initEdit(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#getRepositoryByAssetId(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext getRepositoryByAssetId(SessionContext sessionContext) throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			List<DmmRepositoryDTO> repositoryList = new ArrayList<DmmRepositoryDTO>();
			Transformer transformer = new SimpleDtoDmoTransformer();
			if (StringUtils.hasText(formDTO.getQueryAssetIds())) {
				DmmRepository dmmRepository = new DmmRepository();
				String [] assetIds = formDTO.getQueryAssetIds().split(IAtomsConstants.MARK_SEPARATOR);
				for (int i = 0; i < assetIds.length; i++) {
					dmmRepository = this.repositoryDAO.findByPrimaryKey(DmmRepository.class, assetIds[i]);
					DmmRepositoryDTO repositoryDTO = (DmmRepositoryDTO) transformer.transform(dmmRepository, new DmmRepositoryDTO());
					repositoryList.add(repositoryDTO);
				}
			}
			sessionContext.setResponseResult(formDTO);
		}catch (DataAccessException e) {
			LOGGER.error("AssetManageService.initEdit(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.initEdit(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/**
	 * Purpose:；列印借用單時候給庫存歷史檔儲存一條記錄
	 * @author HermanWang
	 * @param sessionContext:上下文SessionContext 
	 * @return SessionContext：:上下文SessionContext 
	 */
	public SessionContext saveRepositoryHist(SessionContext sessionContext){
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			LogonUser logonUser = null;
			if(formDTO != null) {
				logonUser = formDTO.getLogonUser();
				String [] repositoryDTOList = formDTO.getQueryAssetIds().split(IAtomsConstants.MARK_SEPARATOR);
				String assetIds = null;
				Timestamp nowTime = DateTimeUtils.getCurrentTimestamp();
				int size = repositoryDTOList.length;
				String historyId = null;
				int i = 0;
				if (size>300) {
					for (String dmmRepositoryDTO2 : repositoryDTOList) {
						if ((i%300 == 1 || i == 0 ) && i!=1) {
							assetIds = dmmRepositoryDTO2;
						} else if (i < repositoryDTOList.length) {
							assetIds +=  "," + dmmRepositoryDTO2;
						}
						if ((i%300 == 0 || i == repositoryDTOList.length-1) && i>1) {
							historyId =this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
							this.repositoryDAO.saveRepositoryByBorrowZip(assetIds, nowTime, logonUser.getId(), logonUser.getName(), historyId);
							assetIds = "";
						}
						i++;
					}
				} else {
					if (size<2) {
						assetIds = repositoryDTOList[0] + ",";
					} else {
						for (String dmmRepositoryDTO2 : repositoryDTOList) {
							if (i == 0) {
								assetIds = dmmRepositoryDTO2;
							} else if (i < repositoryDTOList.length) {
								assetIds +=  "," + dmmRepositoryDTO2;
							}
							i++;
						}
					}
					historyId =this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
					this.repositoryDAO.saveRepositoryByBorrowZip(assetIds, nowTime, logonUser.getId(), logonUser.getName(), historyId);
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.saveRepositoryHist(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.saveRepositoryHist(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#edit(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext edit(SessionContext sessionContext) throws ServiceException {
		try {
			Message msg = null;
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			DmmRepositoryDTO dmmRepositoryDTO = formDTO.getDmmRepositoryDTO();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			String changeFlag = null;
			if (dmmRepositoryDTO != null) {
				/*List<DmmRepository> repositoryList = new ArrayList<DmmRepository>();
				if (StringUtils.hasText(formDTO.getQueryAssetIds())) {
					DmmRepository dmmRepositoryDTO2 = new DmmRepository();
					String [] assetIds = formDTO.getQueryAssetIds().split(IAtomsConstants.MARK_SEPARATOR);
					for (int i = 0; i < assetIds.length; i++) {
						dmmRepositoryDTO2 = this.repositoryDAO.findByPrimaryKey(DmmRepository.class, assetIds[i]);
						repositoryList.add(dmmRepositoryDTO2);
					}
				}
				//如果存在則修改
				if (!CollectionUtils.isEmpty(repositoryList)) {
					List<DmmRepository> dmmRepositories = new ArrayList<DmmRepository>();
					for (DmmRepository dmmRepository : repositoryList) {
						dmmRepository.setUpdateUser(logonUser.getId());
						dmmRepository.setUpdateUserName(logonUser.getName());
						dmmRepository.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
						dmmRepositories.add(dmmRepository);
					}
					List<DmmRepositoryHistory> repositoryHists = new ArrayList<DmmRepositoryHistory>();
					DmmRepositoryHistory repositoryHist = new DmmRepositoryHistory();
					//領用作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_CARRY)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為庫存，不可進行領用作業
							if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							//設備無財產編號，不可進行領用作業
							if (!StringUtils.hasText(dmmRepository.getPropertyId())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setCarrier(dmmRepositoryDTO.getCarryAccount());
							dmmRepository.setBackDate(null);
							//說明
							dmmRepository.setDescription(dmmRepositoryDTO.getCarryComment());
							dmmRepository.setCarryDate(dmmRepositoryDTO.getCarryDate());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_APPLY);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將借用結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_USE_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_USE_FAILURE, new String[]{});
						}
					//台新租賃
					} else if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_TAIXIN_RENT)) {
						AdmUser admUser = null;
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為領用中或使用中，不可進行台新租賃維護作業
							if ((!IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(dmmRepository.getStatus())) && (!IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY.equals(dmmRepository.getStatus()))) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							//cr2238 update by 2017/08/28
							//if(dmmRepositoryDTO.getAnalyzeDate() != null){
								dmmRepository.setAnalyzeDate(dmmRepositoryDTO.getAnalyzeDate());
							//}
							//cr2238 update by 2017/08/28
							//if(dmmRepositoryDTO.getCaseCompletionDate() != null){
								dmmRepository.setCaseCompletionDate(dmmRepositoryDTO.getCaseCompletionDate());
							//}
							//bug2235 cr2236 update by 2017/08/25
							dmmRepository.setMaintainUser(dmmRepositoryDTO.getMaintainUser());
							if(StringUtils.hasText(dmmRepositoryDTO.getMaintainUser())){
								admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, dmmRepositoryDTO.getMaintainUser());
								dmmRepository.setDepartmentId(admUser.getDeptCode());
							}
							//cr2238  該設備無設備啟用日時，更新該欄位 與是否啟用欄位 update by 2017/08/29
							if (dmmRepository.getEnableDate() == null && dmmRepositoryDTO.getEnableDate() != null) {
								dmmRepository.setEnableDate(dmmRepositoryDTO.getEnableDate());
								dmmRepository.setIsEnabled(IAtomsConstants.PARAM_YES);
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setBackDate(null);
							dmmRepository.setDtid(dmmRepositoryDTO.getDtid());
							dmmRepository.setCaseId(dmmRepositoryDTO.getCaseId());
							dmmRepository.setMerchantId(dmmRepositoryDTO.getMerchantId());
							dmmRepository.setMerchantHeaderId(dmmRepositoryDTO.getMerchantHeaderId());
							dmmRepository.setMaintainCompany(dmmRepositoryDTO.getMaintainCompany());
							dmmRepository.setInstalledAdress(dmmRepositoryDTO.getInstalledAdress());
							dmmRepository.setInstalledAdressLocation(dmmRepositoryDTO.getInstalledAdressLocation());
							dmmRepository.setIsCup(dmmRepositoryDTO.getIsCup());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_IN_USE);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_TSB_RENT);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將借用結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_TAIXIN_RENT_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_TAIXIN_RENT_FAILURE, new String[]{});
						}
					}
					//借用作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為庫存，不可進行借用作業
							if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							//設備無財產編號，不可進行借用作業
							if (!StringUtils.hasText(dmmRepository.getPropertyId())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setBackDate(null);
							dmmRepository.setBorrower(dmmRepositoryDTO.getUserId());
							dmmRepository.setBorrowerEnd(dmmRepositoryDTO.getBorrowerEnd());
							dmmRepository.setBorrowerMgrEmail(dmmRepositoryDTO.getBorrowerMgrEmail());
							//借用說明
							dmmRepository.setDescription(dmmRepositoryDTO.getBorrowerComment());
							dmmRepository.setBorrowerEmail(dmmRepositoryDTO.getBorrowerEmail());
							dmmRepository.setBorrowerStart(dmmRepositoryDTO.getBorrowerStart());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_BORROWING);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_BORROW);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							AssetManageFormDTO assetManageFormDTO = new AssetManageFormDTO();
							assetManageFormDTO.setQueryAssetIds(formDTO.getQueryAssetIds());
							List<DmmRepositoryDTO> dmmRepositoryDTOs = this.repositoryDAO.listBy(assetManageFormDTO);
							try {
								this.sendMail(dmmRepositoryDTO.getUserId(), logonUser.getId(), dmmRepositoryDTO.getBorrowerEmail(), formDTO, dmmRepositoryDTOs);
							} catch (Exception e) {
								LOGGER.error("AssetManageService.edit() doing borrowing action sendMail is error" + e);
							}
							//將借用結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_BORROW_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_BORROW_FAILURE, new String[]{});
						}
					}
					//報廢作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRED)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為待報廢，不可進行報廢作業
							if (!IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setBackDate(null);
							dmmRepository.setDescription(dmmRepositoryDTO.getRetireComment());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_DISABLED);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_DISABLED);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將報廢結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_SCRAP_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_SCRAP_FAILURE, new String[]{});
						}
					}
					//待報廢作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRE)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//狀態若不為“庫存、維修中、送修中、已遺失”，則顯示訊息「設備狀態不為庫存或維修中或送修中或已遺失，不可進行待報廢作業」
							if ((!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepository.getStatus()))
									&& (!IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(dmmRepository.getStatus()))
									&& (!IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE.equals(dmmRepository.getStatus()))
									&& (!IAtomsConstants.PARAM_ASSET_STATUS_LOST.equals(dmmRepository.getStatus()))) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							dmmRepository.setBackDate(null);
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setDescription(dmmRepositoryDTO.getRetireComment());
							dmmRepository.setRetireReasonCode(dmmRepositoryDTO.getRetireReasonCode());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_PENDING_DISABLED);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_TO_BE_SCRAPPED_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_TO_BE_SCRAPPED_FAILURE, new String[]{});
						}
					}
					//歸還作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BACK)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為借用中，不可進行歸還作業
							if (!IAtomsConstants.PARAM_ASSET_STATUS_BORROWING.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
							dmmRepository.setBorrower(null);
							dmmRepository.setBorrowerEnd(null);
							dmmRepository.setBorrowerMgrEmail(null);
							dmmRepository.setBorrowerEmail(null);
							dmmRepository.setBorrowerStart(null);
							dmmRepository.setBackDate(DateTimeUtils.getCurrentTimestamp());
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_BACK);
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_BACK_SUCCESS, new String[]{});
						} else {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_BACK_FAILURE, new String[]{});
						}
					}
					//入庫作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_ASSET_IN)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為領用中或維修中或送修中或已拆回，不可進行入庫作業
							if ((!IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY.equals(dmmRepository.getStatus()))
									&& (!IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(dmmRepository.getStatus()))
									&& (!IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE.equals(dmmRepository.getStatus()))
									&& (!IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(dmmRepository.getStatus()))) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							dmmRepository.setBackDate(null);
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
							//領用中
							if (StringUtils.pathEquals(dmmRepositoryDTO.getStatus(), IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY)) {
								dmmRepository.setCarrier(null);
								dmmRepository.setCarryDate(null);
							//維修中
							} else if (IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(dmmRepositoryDTO.getStatus())) {
								dmmRepository.setFaultComponent(null);
								dmmRepository.setRepairVendor(null);
								dmmRepository.setFaultDescription(null);
							//送修中
							} else if (IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(dmmRepositoryDTO.getStatus())) {
								dmmRepository.setRepairVendor(null);
							}
							//Bug #2272 待設備入庫時，再將上述資料清除    --改為清除8個 update by 2017/09/08
							dmmRepository.setCaseId(null);
							dmmRepository.setCaseCompletionDate(null);
							dmmRepository.setDepartmentId(null);
							dmmRepository.setMaintainCompany(null);
							dmmRepository.setAnalyzeDate(null);
							dmmRepository.setMaintainUser(null);
							dmmRepository.setUninstallOrRepairReason(null);
							dmmRepository.setDescription(null);
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_IN_ASSET);

							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.STORAGE_SUCCESS, new String[]{});
						} else {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.STORAGE_FAILURE, new String[]{});
						}
					}
					//退回作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETURN)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為待報廢，不可進行退回作業
							if (!IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setBackDate(null);
							dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_RETURN);
							
							dmmRepository.setFaultComponent(null);
							dmmRepository.setRepairVendor(null);
							dmmRepository.setFaultDescription(null);
							dmmRepository.setRetireReasonCode(null);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_RETURN_SUCCESS, new String[]{});
						} else {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_RETURN_FAILURE, new String[]{});
						}
					}
					//銷毀作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_DELETE)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//設備狀態不為報廢，不可進行銷毀作業
							if (!IAtomsConstants.PARAM_ASSET_STATUS_DISABLED.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setBackDate(null);
							dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
							//dmmRepository.setDeleted(IAtomsConstants.PARAM_YES);
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_DESTROY);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_DESTROY);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_DESTROY_SUCCESS, new String[]{});
						} else {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_DESTROY_FAILURE, new String[]{});
						}
					}
					//送修作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIRED)) {
						for (DmmRepository dmmRepository : dmmRepositories) {
							//狀態若不為“維修中”，則顯示訊息「設備狀態不為維修中，不可進行送修作業」
							if (!IAtomsConstants.PARAM_ASSET_STATUS_REPAIR.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							repositoryHist = new DmmRepositoryHistory();
							dmmRepository.setBackDate(null);//?
							dmmRepository.setRepairCompany(dmmRepositoryDTO.getRepairVendorId());
							dmmRepository.setRepairVendor(dmmRepositoryDTO.getRepairVendorId());
							dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
							dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_MAINTENANCE);
							dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_REPAIRED);
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
						}
						if (!CollectionUtils.isEmpty(repositoryHists) && !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_REPAIRED_SUCCESS, new String[]{});
						} else {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_REPAIRED_FAILURE, new String[]{});
						}
					}
					//維修作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIR)) {
						String faultComponent = IAtomsConstants.MARK_EMPTY_STRING;
						String faultDesc = IAtomsConstants.MARK_EMPTY_STRING;
						DmmRepositoryFaultComId dmmRepositoryFaultComId = null;
						DmmRepositoryFaultCom repositoryFaultCom = new DmmRepositoryFaultCom();
						if (!CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								//設備狀態不為庫存，不可進行維修作業
								if (!IAtomsConstants.PARAM_ASSET_STATUS_REPERTORY.equals(dmmRepository.getStatus())) {
									changeFlag = IAtomsConstants.PARAM_YES;
									break;
								}
								repositoryHist = new DmmRepositoryHistory();
								faultComponent = dmmRepository.getFaultComponent();
								faultDesc = dmmRepository.getFaultDescription();
								dmmRepository.setBackDate(null);
								dmmRepository.setFaultComponent(dmmRepositoryDTO.getFaultComponentId());
								dmmRepository.setRepairVendor(dmmRepositoryDTO.getRepairVendorId());
								dmmRepository.setFaultDescription(dmmRepositoryDTO.getFaultDescriptionId());
								dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
								dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_REPAIR);
								dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_REPAIR);
								//設置庫存id
								repositoryHist.setAssetId(dmmRepository.getAssetId());
								repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
								if (dmmRepository != null) {
									this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
									this.repositoryDAO.getDaoSupport().flush();
									//判斷舊故障組件是否存在，如不存在則刪除舊資料
									if (StringUtils.hasText(faultComponent)) {
										String[] dmmRepositoryFaultComArray = faultComponent.split(IAtomsConstants.MARK_SEPARATOR);
										for (int i = 0; i < dmmRepositoryFaultComArray.length; i++) {
											dmmRepositoryFaultComId = new DmmRepositoryFaultComId(dmmRepository.getAssetId(), dmmRepositoryFaultComArray[i]);
											repositoryFaultCom = this.repositoryFaultComDAO.findByPrimaryKey(DmmRepositoryFaultCom.class, dmmRepositoryFaultComId);
											if (repositoryFaultCom!= null && !dmmRepository.getFaultComponent().contains(dmmRepositoryFaultComArray[i])) {
												//repositoryFaultCom = new DmmRepositoryFaultCom(dmmRepositoryFaultComId);
												this.repositoryFaultComDAO.getDaoSupport().delete(repositoryFaultCom);
												this.repositoryFaultComDAO.getDaoSupport().flush();
											}
										}
									} 
									//判斷舊故障現象是否存在，如不存在則刪除舊資料
									DmmRepositoryFaultDescId dmmRepositoryFaultDescId = null;
									DmmRepositoryFaultDesc repositoryFaultDesc = new DmmRepositoryFaultDesc();
									if (StringUtils.hasText(faultDesc)) {
										String[] dmmRepositoryFaultDescArray = faultDesc.split(IAtomsConstants.MARK_SEPARATOR);
										for (int i = 0; i < dmmRepositoryFaultDescArray.length; i++) {
											dmmRepositoryFaultDescId = new DmmRepositoryFaultDescId(dmmRepository.getAssetId(), dmmRepositoryFaultDescArray[i]);
											repositoryFaultDesc = this.repositoryFaultDescDAO.findByPrimaryKey(DmmRepositoryFaultDesc.class, dmmRepositoryFaultDescId);
											if (repositoryFaultDesc!= null && !dmmRepository.getFaultDescription().contains(dmmRepositoryFaultDescArray[i])) {
												//repositoryFaultDesc = new DmmRepositoryFaultDesc(dmmRepositoryFaultDescId);
												this.repositoryFaultDescDAO.getDaoSupport().delete(repositoryFaultDesc);
												this.repositoryFaultDescDAO.getDaoSupport().flush();
											}
										}
									}
									//保存故障組件
									if (StringUtils.hasText(dmmRepositoryDTO.getFaultComponentId())) {
										String[] dmmRepositoryFaultComArray = dmmRepositoryDTO.getFaultComponentId().split(IAtomsConstants.MARK_SEPARATOR);
										for (int i = 0; i < dmmRepositoryFaultComArray.length; i++) {
											dmmRepositoryFaultComId = new DmmRepositoryFaultComId(dmmRepository.getAssetId(), dmmRepositoryFaultComArray[i]);
											repositoryFaultCom = this.repositoryFaultComDAO.findByPrimaryKey(DmmRepositoryFaultCom.class, dmmRepositoryFaultComId);
											if (repositoryFaultCom == null) {
												repositoryFaultCom = new DmmRepositoryFaultCom(dmmRepositoryFaultComId);
											}
											this.repositoryFaultComDAO.getDaoSupport().saveOrUpdate(repositoryFaultCom);
											this.repositoryFaultComDAO.getDaoSupport().flush();
										}
									}
									//保存故障現象
									if (StringUtils.hasText(dmmRepositoryDTO.getFaultDescriptionId())) {
										String[] dmmRepositoryFaultDescArray = dmmRepositoryDTO.getFaultDescriptionId().split(IAtomsConstants.MARK_SEPARATOR);
										for (int i = 0; i < dmmRepositoryFaultDescArray.length; i++) {
											dmmRepositoryFaultDescId = new DmmRepositoryFaultDescId(dmmRepository.getAssetId(), dmmRepositoryFaultDescArray[i]);
											repositoryFaultDesc = this.repositoryFaultDescDAO.findByPrimaryKey(DmmRepositoryFaultDesc.class, dmmRepositoryFaultDescId);
											if (repositoryFaultDesc == null) {
												repositoryFaultDesc = new DmmRepositoryFaultDesc(dmmRepositoryFaultDescId);
											}
											this.repositoryFaultDescDAO.getDaoSupport().saveOrUpdate(repositoryFaultDesc);
											this.repositoryFaultDescDAO.getDaoSupport().flush();
										}
									}
									this.repositoryDAO.saveRepositoryHist(dmmRepository.getAssetId(), repositoryHist.getHistoryId(), dmmRepository.getStatus());
								}
							}
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_REPAIR_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_REPAIR_FAILURE, new String[]{});
						}
					}*/
					//解除綁定
					//if (AssetManageFormDTO.ACTION_REMOVE.equals(formDTO.getEditFlag())) {
						// 案件處理記錄主鍵id
						String transactionId = null;
						String historyId = null;
						Object[] objects = null;
						String action = null;
						//入庫作業 歸還作業 報廢作業 退回作業 銷毀作業 存儲欄位相同
						if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_ASSET_IN)) {
							action = IAtomsConstants.PARAM_ACTION_IN_ASSET;
						}
						if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BACK)) {
							action = IAtomsConstants.PARAM_ACTION_BACK;
						}
						if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRED)) {
							action = IAtomsConstants.PARAM_ACTION_DISABLED;
						}
						if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETURN)) {
							action = IAtomsConstants.PARAM_ACTION_RETURN;
						}
						if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_DELETE)) {
							action = IAtomsConstants.PARAM_ACTION_DESTROY;
						}
						String assetForBorrow = null;
						//CR #2419 如果是批量操作
						if (IAtomsConstants.PARAM_YES.equals(formDTO.getQueryAllSelected())) {
							IAtomsLogonUser user = (IAtomsLogonUser) formDTO.getLogonUser();
							//獲取登入者角色權限
							List<AdmRoleDTO> roleCodes =user.getUserFunctions();
							if(!CollectionUtils.isEmpty(roleCodes)) {
								String roleFlag = IAtomsConstants.PARAM_YES;
								//如果包含廠商角色，則按廠商角色查詢
								for (AdmRoleDTO admRoleDTO : roleCodes) {
									if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
										roleFlag = IAtomsConstants.PARAM_NO;
										break;
									} 
								}
								//廠商角色
								if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
									boolean hasWarehouse = false;
									if (IAtomsConstants.PARAM_YES.equals(user.getAdmUserDTO().getDataAcl())) {
										hasWarehouse = true;
									}
									
									//未設定控管權限，則可查詢所有倉庫之設備資料
									if (!hasWarehouse) {
										formDTO.setUserId(null);
									}
								} else {
									//客戶角色--顯示登入者所屬公司之設備資料
									String customerId = user.getAdmUserDTO().getCompanyId();
									formDTO.setCompanyId(customerId);
									formDTO.setUserId(null);
								}
							}
							String assetIds = null;
							List<String> repositoryDTOList = this.repositoryDAO.getAssetIdList(formDTO);
							
							int size = repositoryDTOList.size();
							int j=0;
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
								for (String dmmRepositoryDTO2 : repositoryDTOList) {
									if (j == 0) {
										assetForBorrow = dmmRepositoryDTO2;
									} else if (j < repositoryDTOList.size()) {
										assetForBorrow +=  "," + dmmRepositoryDTO2;
									}
									j++;
								}
							}
							int i = 0;
							if (size>300) {
								for (String dmmRepositoryDTO2 : repositoryDTOList) {
									if ((i%300 == 1 || i == 0 ) && i!=1) {
										assetIds = dmmRepositoryDTO2;
									} else if (i < repositoryDTOList.size()) {
										assetIds +=  "," + dmmRepositoryDTO2;
									}
									if ((i%300 == 0 || i == repositoryDTOList.size()-1) && i>1) {
										// 案件處理記錄主鍵id
										transactionId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_CASE_TRANSACTION);
										historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
										//解除綁定
										if (AssetManageFormDTO.ACTION_REMOVE.equals(formDTO.getEditFlag())) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REMOVE_OR_LOSS,
													assetIds, transactionId, historyId, logonUser.getId(), logonUser.getName(),
													dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(), 
													dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
													dmmRepositoryDTO.getDescription(),null, DateTimeUtils.getCurrentTimestamp(),
													formDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, null,
													null, null, null, null,null, null, null, null, null);
										}
										//維修作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIR)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REPAIR,
													assetIds, transactionId, historyId, logonUser.getId(), logonUser.getName(),
													dmmRepositoryDTO.getMaintainCompany(), null, null, null,
													dmmRepositoryDTO.getDescription(),null, DateTimeUtils.getCurrentTimestamp(),
													formDTO.getStatus(), dmmRepositoryDTO.getFaultComponentId(), dmmRepositoryDTO.getRepairVendorId(),
													dmmRepositoryDTO.getFaultDescriptionId(), null, null, null, null, null, null, null, null,
													null, null, null, null,null, null, null, null, null);
										}
										//入庫作業 歸還作業 報廢作業 退回作業 銷毀作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_ASSET_IN)
												|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BACK)
												|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRED)
												|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETURN)
												|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_DELETE)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(action,assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), null, null, null, null,
													dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
													dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, null,
													null, null, null, null,null, null, null, null, null);
										}
										//領用作業 
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_CARRY)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_APPLY, assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), null, null, null, null,
													dmmRepositoryDTO.getCarryComment(), null, DateTimeUtils.getCurrentTimestamp(),
													dmmRepositoryDTO.getStatus(), null, null, null, dmmRepositoryDTO.getCarryAccount(), dmmRepositoryDTO.getCarryDate(), null, null, null, null, null, null,
													null, null, null, null,null, null, null, null, null);
										}
										//借用作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), null, null, null, null,
													dmmRepositoryDTO.getBorrowerComment(), null, DateTimeUtils.getCurrentTimestamp(),
													dmmRepositoryDTO.getStatus(), null, null, null, null, null, dmmRepositoryDTO.getUserId(),
													dmmRepositoryDTO.getBorrowerEnd(), dmmRepositoryDTO.getBorrowerMgrEmail(), 
													dmmRepositoryDTO.getBorrowerEmail(), dmmRepositoryDTO.getBorrowerStart(), null,
													null, null, null, null,null, null, null, null, null);
										}
										//送修作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIRED)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REPAIRED, assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), null, null, null, null,
													dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
													dmmRepositoryDTO.getStatus(), null, dmmRepositoryDTO.getRepairVendorId(), null, null, 
													null, null, null, null, null, null, null,
													null, null, null, null,null, null, null, null, null);
										}
										//台新租賃作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_TAIXIN_RENT)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_TSB_RENT, assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(),
													dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
													dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
													dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, dmmRepositoryDTO.getEnableDate(),
													IAtomsConstants.PARAM_YES, dmmRepositoryDTO.getDtid(), dmmRepositoryDTO.getCaseId(), dmmRepositoryDTO.getMerchantId(),
													dmmRepositoryDTO.getMerchantHeaderId(), dmmRepositoryDTO.getInstalledAdress(), 
													dmmRepositoryDTO.getInstalledAdressLocation(), dmmRepositoryDTO.getIsCup(), null);
										}
										//CR #2696 增加捷達威維護 2017/10/25
										//捷達威維護作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_JDW)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_JDW_MAINTENANCE, assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(),
													dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
													dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
													dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, dmmRepositoryDTO.getEnableDate(),
													IAtomsConstants.PARAM_YES, dmmRepositoryDTO.getDtid(), dmmRepositoryDTO.getCaseId(), dmmRepositoryDTO.getMerchantId(),
													dmmRepositoryDTO.getMerchantHeaderId(), dmmRepositoryDTO.getInstalledAdress(), 
													dmmRepositoryDTO.getInstalledAdressLocation(), dmmRepositoryDTO.getIsCup(), null);
										}
										//待報廢作業
										if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRE)) {
											objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_PENDING_DISABLED, assetIds, transactionId, historyId, 
													logonUser.getId(), logonUser.getName(), null, null, null, null,
													dmmRepositoryDTO.getRetireComment(), null, DateTimeUtils.getCurrentTimestamp(),
													null, null, null, null, null, 
													null, null, null, null, null, null, null,
													null, null, null, null,null, null, null, null, dmmRepositoryDTO.getRetireReasonCode());
										}
										if (objects==null || objects[1]==null || "N".equals(objects[1])) {
											break;
										}
										assetIds = "";
									}
									i++;
								}
							} else {
								if (size<2) {
									assetIds = repositoryDTOList.get(0) + ",";
								} else {
									for (String dmmRepositoryDTO2 : repositoryDTOList) {
										if (i == 0) {
											assetIds = dmmRepositoryDTO2;
										} else if (i < repositoryDTOList.size()) {
											assetIds +=  "," + dmmRepositoryDTO2;
										}
										i++;
									}
								}
								// 案件處理記錄主鍵id
								transactionId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_CASE_TRANSACTION);
								historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
								//解除綁定
								if (AssetManageFormDTO.ACTION_REMOVE.equals(formDTO.getEditFlag())) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REMOVE_OR_LOSS,
											assetIds, transactionId, historyId, logonUser.getId(), logonUser.getName(),
											dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(), 
											dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
											dmmRepositoryDTO.getDescription(),null, DateTimeUtils.getCurrentTimestamp(),
											formDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, null,
											null, null, null, null,null, null, null, null, null);
								}
								//維修作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIR)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REPAIR,
											assetIds, transactionId, historyId, logonUser.getId(), logonUser.getName(),
											null, null, null, null,
											dmmRepositoryDTO.getDescription(),null, DateTimeUtils.getCurrentTimestamp(),
											formDTO.getStatus(), dmmRepositoryDTO.getFaultComponentId(), dmmRepositoryDTO.getRepairVendorId(),
											dmmRepositoryDTO.getFaultDescriptionId(), null, null, null, null, null, null, null, null,
											null, null, null, null,null, null, null, null, null);
								}
								//入庫作業 歸還作業 報廢作業 退回作業 銷毀作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_ASSET_IN)
										|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BACK)
										|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRED)
										|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETURN)
										|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_DELETE)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(action,assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), null, null, null, null,
											dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
											dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, null,
											null, null, null, null,null, null, null, null, null);
								}
								//領用作業 
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_CARRY)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_APPLY,assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), null, null, null, null,
											dmmRepositoryDTO.getCarryComment(), null, DateTimeUtils.getCurrentTimestamp(),
											dmmRepositoryDTO.getStatus(), null, null, null, dmmRepositoryDTO.getCarryAccount(), dmmRepositoryDTO.getCarryDate(), null, null, null, null, null, null,
											null, null, null, null,null, null, null, null, null);
								}
								//借用作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), null, null, null, null,
											dmmRepositoryDTO.getBorrowerComment(), null, DateTimeUtils.getCurrentTimestamp(),
											dmmRepositoryDTO.getStatus(), null, null, null, null, null, dmmRepositoryDTO.getUserId(),
											dmmRepositoryDTO.getBorrowerEnd(), dmmRepositoryDTO.getBorrowerMgrEmail(), 
											dmmRepositoryDTO.getBorrowerEmail(), dmmRepositoryDTO.getBorrowerStart(), null,
											null, null, null, null,null, null, null, null, null);
								}
								//送修作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIRED)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REPAIRED, assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), null, null, null, null,
											dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
											dmmRepositoryDTO.getStatus(), null, dmmRepositoryDTO.getRepairVendorId(), null, null, 
											null, null, null, null, null, null, null,
											null, null, null, null,null, null, null, null, null);
								}
								//台新租賃作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_TAIXIN_RENT)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_TSB_RENT, assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(),
											dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
											dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
											dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, dmmRepositoryDTO.getEnableDate(),
											IAtomsConstants.PARAM_YES, dmmRepositoryDTO.getDtid(), dmmRepositoryDTO.getCaseId(), dmmRepositoryDTO.getMerchantId(),
											dmmRepositoryDTO.getMerchantHeaderId(), dmmRepositoryDTO.getInstalledAdress(), dmmRepositoryDTO.getInstalledAdressLocation(), dmmRepositoryDTO.getIsCup(), null);
								}
								//CR #2696 增加捷達威維護 2017/10/25
								//捷達威維護作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_JDW)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_JDW_MAINTENANCE, assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(),
											dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
											dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
											dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, dmmRepositoryDTO.getEnableDate(),
											IAtomsConstants.PARAM_YES, dmmRepositoryDTO.getDtid(), dmmRepositoryDTO.getCaseId(), dmmRepositoryDTO.getMerchantId(),
											dmmRepositoryDTO.getMerchantHeaderId(), dmmRepositoryDTO.getInstalledAdress(), dmmRepositoryDTO.getInstalledAdressLocation(), dmmRepositoryDTO.getIsCup(), null);
								}
								//待報廢作業
								if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRE)) {
									objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_PENDING_DISABLED, assetIds, transactionId, historyId, 
											logonUser.getId(), logonUser.getName(), null, null, null, null,
											dmmRepositoryDTO.getRetireComment(), null, DateTimeUtils.getCurrentTimestamp(),
											null, null, null, null, null, 
											null, null, null, null, null, null, null,
											null, null, null, null,null, null, null, null, dmmRepositoryDTO.getRetireReasonCode());
								}
							}
							//沒有勾選批量操作
						} else {
							//借用作業
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
								assetForBorrow = formDTO.getQueryAssetIds();
							}
							if (formDTO.getQueryAssetIds().indexOf(",") < 0) {
								formDTO.setQueryAssetIds(formDTO.getQueryAssetIds() + ",");
							}
							// 案件處理記錄主鍵id
							transactionId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_CASE_TRANSACTION);
							historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
							//解除綁定
							if (AssetManageFormDTO.ACTION_REMOVE.equals(formDTO.getEditFlag())) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REMOVE_OR_LOSS,
										formDTO.getQueryAssetIds(), transactionId, historyId, logonUser.getId(), logonUser.getName(),
										dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(), 
										dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
										dmmRepositoryDTO.getDescription(),null, DateTimeUtils.getCurrentTimestamp(),
										formDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, null,
										null, null, null, null,null, null, null, null, null);
							//維修作業
							} else if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIR)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REPAIR,
										formDTO.getQueryAssetIds(), transactionId, historyId, logonUser.getId(), logonUser.getName(),
										null, null,null, null, dmmRepositoryDTO.getDescription(),null, DateTimeUtils.getCurrentTimestamp(),
										formDTO.getStatus(), dmmRepositoryDTO.getFaultComponentId(), dmmRepositoryDTO.getRepairVendorId(),
										dmmRepositoryDTO.getFaultDescriptionId(), null, null, null, null, null, null, null, null,
										null, null, null, null,null, null, null, null, null);
							}
							//入庫作業 歸還作業 報廢作業 退回作業 銷毀作業
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_ASSET_IN)
									|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BACK)
									|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRED)
									|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETURN)
									|| StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_DELETE)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(action, formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), null, null, null, null,
										dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
										dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, null,
										null, null, null, null,null, null, null, null, null);
							}
							//領用作業 
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_CARRY)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_APPLY,formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), null, null, null, null,
										dmmRepositoryDTO.getCarryComment(), null, DateTimeUtils.getCurrentTimestamp(),
										dmmRepositoryDTO.getStatus(), null, null, null, dmmRepositoryDTO.getCarryAccount(), dmmRepositoryDTO.getCarryDate(),
										null, null, null, null, null, null,
										null, null, null, null,null, null, null, null, null);
							}
							//借用作業
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), null, null, null, null,
										dmmRepositoryDTO.getBorrowerComment(), null, DateTimeUtils.getCurrentTimestamp(),
										dmmRepositoryDTO.getStatus(), null, null, null, null, null, dmmRepositoryDTO.getUserId(),
										dmmRepositoryDTO.getBorrowerEnd(), dmmRepositoryDTO.getBorrowerMgrEmail(), 
										dmmRepositoryDTO.getBorrowerEmail(), dmmRepositoryDTO.getBorrowerStart(), null,
										null, null, null, null, null, null, null, null, null);
							}
							//送修作業
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_REPAIRED)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_REPAIRED, formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), null, null, null, null,
										dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
										dmmRepositoryDTO.getStatus(), null, dmmRepositoryDTO.getRepairVendorId(), null, null, 
										null, null, null, null, null, null, null,
										null, null, null, null, null, null, null, null, null);
							}
							//台新租賃作業
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_TAIXIN_RENT)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_TSB_RENT, formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(),
										dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
										dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
										dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, dmmRepositoryDTO.getEnableDate(),
										IAtomsConstants.PARAM_YES, dmmRepositoryDTO.getDtid(), dmmRepositoryDTO.getCaseId(), dmmRepositoryDTO.getMerchantId(),
										dmmRepositoryDTO.getMerchantHeaderId(), dmmRepositoryDTO.getInstalledAdress(), dmmRepositoryDTO.getInstalledAdressLocation(), dmmRepositoryDTO.getIsCup(), null);
							}
							//CR #2696 增加捷達威維護 2017/10/25
							//捷達威維護作業
							if (AssetManageFormDTO.ACTION_JDW.equals(formDTO.getEditFlag())) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_JDW_MAINTENANCE, formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), dmmRepositoryDTO.getMaintainCompany(), dmmRepositoryDTO.getAnalyzeDate(),
										dmmRepositoryDTO.getCaseCompletionDate(), dmmRepositoryDTO.getMaintainUser(),
										dmmRepositoryDTO.getDescription(), null, DateTimeUtils.getCurrentTimestamp(),
										dmmRepositoryDTO.getStatus(), null, null, null, null, null, null, null, null, null, null, dmmRepositoryDTO.getEnableDate(),
										IAtomsConstants.PARAM_YES, dmmRepositoryDTO.getDtid(), dmmRepositoryDTO.getCaseId(), dmmRepositoryDTO.getMerchantId(),
										dmmRepositoryDTO.getMerchantHeaderId(), dmmRepositoryDTO.getInstalledAdress(), dmmRepositoryDTO.getInstalledAdressLocation(), dmmRepositoryDTO.getIsCup(), null);
							}
							//待報廢作業
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_RETIRE)) {
								objects = this.repositoryDAO.saveRepositoryByProcedure(IAtomsConstants.PARAM_ACTION_PENDING_DISABLED, formDTO.getQueryAssetIds(), transactionId, historyId, 
										logonUser.getId(), logonUser.getName(), null, null, null, null,
										dmmRepositoryDTO.getRetireComment(), null, DateTimeUtils.getCurrentTimestamp(),
										null, null, null, null, null, 
										null, null, null, null, null, null, null,
										null, null, null, null,null, null, null, null, dmmRepositoryDTO.getRetireReasonCode());
							}
						}
						if (objects[0] != null && objects[1] != null) {
							if (objects[1] != null && "Y".equals(objects[1].toString())) {
								msg = new Message(Message.STATUS.SUCCESS, objects[0].toString(), new String[]{});
							} else {
								msg = new Message(Message.STATUS.FAILURE, objects[0].toString(), new String[]{});
								LOGGER.error("AssetManageService.edit() is failure by " +formDTO.getEditFlag() +", msg: "+ objects[0].toString());
							}
						} else {
							msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_FAILURE, new String[]{});
							if (objects[0] == null) {
								LOGGER.error("AssetManageService.edit() is failure by " +formDTO.getEditFlag() +", objects[0] is null ");
							}
							if (objects[1] == null) {
								LOGGER.error("AssetManageService.edit() is failure by " +formDTO.getEditFlag() +", objects[1] is null ");
							}
						}
						//借用作業 --發送mail
						if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
							try {
								AssetManageFormDTO assetManageFormDTO = new AssetManageFormDTO();
								assetManageFormDTO.setQueryAssetIds(assetForBorrow);
								assetManageFormDTO.setUserId(null);
								assetManageFormDTO.setDmmRepositoryDTO(dmmRepositoryDTO);
								this.sendBorrowMail(dmmRepositoryDTO.getUserId(), logonUser.getId(), dmmRepositoryDTO.getBorrowerEmail(), assetManageFormDTO, null);
							} catch (Exception e) {
								LOGGER.error("AssetManageService.edit() doing borrowing action sendMail is error" + e);
							}
						}
						/*
						String returnedName = null;
						String lostName = null;
						List<Parameter> statusList = null;
						//cr2236 原因=已拆回，須將 原因，寫入 拆機/報修原因 欄位中 --update by 2017/08/23
						//if (IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(formDTO.getStatus())) {
							statusList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.ASSET_STATUS.getCode(), null);
							for (Parameter parameter : statusList) {
								if (IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(parameter.getValue())) {
									returnedName = parameter.getName();
								} else if (IAtomsConstants.PARAM_ASSET_STATUS_LOST.equals(parameter.getValue())) {
									lostName = parameter.getName();
								}
							}
						//}
						// 案件處理記錄主鍵id
						String transactionId = null;
						AdmUser admUser = null;
						// 案件處理記錄DMO對象
						SrmCaseTransaction caseTransaction = new SrmCaseTransaction();
						List<SrmCaseTransaction> caseTransactionList = new ArrayList<SrmCaseTransaction>();
						// 案件處理記錄主鍵id
						transactionId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_SRM_CASE_TRANSACTION);
						int count = 0;
						Boolean isUpdate = null;
						// 案件處理處理中資料DMO對象
						SrmCaseNewHandleInfo caseNewHandleInfo = null;
						SrmCaseNewAssetLink srmCaseNewAssetLink = null;
						SrmCaseAssetLink srmCaseAssetLink = null;
						List<SrmCaseNewAssetLink> srmCaseNewAssetLinkList = new ArrayList<SrmCaseNewAssetLink>();
						List<SrmCaseAssetLink> srmCaseAssetLinkList = new ArrayList<SrmCaseAssetLink>();
						for (DmmRepository dmmRepository : dmmRepositories) {
							//若狀態不為“使用中”，則顯示訊息「設備狀態不為使用中，不可進行解除綁定作業」
							if (!IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(dmmRepository.getStatus())) {
								changeFlag = IAtomsConstants.PARAM_YES;
								break;
							}
							dmmRepository.setBackDate(null);
							repositoryHist = new DmmRepositoryHistory();
							
							 * bug 2235  若使用人不為台新，且設備不為edc，則異動案件資料
							 * 修改案件設備最新連接檔與處理中連接檔相關欄位
							 
							int i = 0;
							//
							BimCompany company= this.companyDAO.findByPrimaryKey(BimCompany.class, dmmRepository.getAssetUser());
							//若使用人不為台新

							if (!IAtomsConstants.PARAM_TSB_EDC.equals(company.getCompanyCode())) {
								
								count = this.srmCaseNewAssetLinkDAO.countByCaseId(dmmRepository.getCaseId());
								//如果解綁的edc不存在，則提示XXXXX
								if (count == 0) {
									//該周邊設備所屬主機未拆除，不可進行解除綁定作業
									msg = new Message(Message.STATUS.FAILURE,IAtomsConstants.FIELD_ASSET_UN_REMOVE);
									sessionContext.setResponseResult(formDTO);
									sessionContext.setReturnMessage(msg);
									Map map = new HashMap();
									if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
										map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
									} else {
										map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
									}
									sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
									return sessionContext;
								} else {
									//如果edc已解綁，更新該設備連接檔，最新案件設備連接檔
									if (IAtomsConstants.PARAM_ASSET_STATUS_LOST.equals(formDTO.getStatus())) {
										isUpdate = this.srmCaseNewAssetLinkDAO.updateCaseNewSerialNumberInfo(dmmRepository.getSerialNumber(), dmmRepository.getCaseId(), dmmRepositoryDTO.getDescription(), IAtomsConstants.ACTION_LOSS);
										isUpdate = this.srmCaseNewAssetLinkDAO.updateCaseSerialNumberInfo(dmmRepository.getSerialNumber(), dmmRepository.getCaseId(), dmmRepositoryDTO.getDescription(), IAtomsConstants.ACTION_LOSS);
									}if (IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(formDTO.getStatus())) {
										isUpdate = this.srmCaseNewAssetLinkDAO.updateCaseNewSerialNumberInfo(dmmRepository.getSerialNumber(), dmmRepository.getCaseId(), dmmRepositoryDTO.getDescription(), IAtomsConstants.ACTION_REMOVE);
										isUpdate = this.srmCaseNewAssetLinkDAO.updateCaseSerialNumberInfo(dmmRepository.getSerialNumber(), dmmRepository.getCaseId(), dmmRepositoryDTO.getDescription(), IAtomsConstants.ACTION_REMOVE);
									}
								}
								
								caseNewHandleInfo = this.srmCaseNewHandleInfoDAO.findByPrimaryKey(SrmCaseNewHandleInfo.class, dmmRepository.getCaseId());
								// 保存案件記錄信息
								i++;
								caseTransaction.setActionId(IAtomsConstants.PARAM_ACTION_REMOVE_OR_LOSS);
								caseTransaction.setTransactionId(transactionId + IAtomsConstants.MARK_UNDER_LINE +String.valueOf(i));
								caseTransaction.setCaseId(caseNewHandleInfo.getCaseId());
								// 當前關卡
								caseTransaction.setCaseStage(caseNewHandleInfo.getCaseStatus());
								caseTransaction.setCaseStageName(i18NUtil.getName(caseNewHandleInfo.getCaseStatus()));
								// 下一關關卡代碼
								caseTransaction.setNextCaseStage(caseNewHandleInfo.getCaseStatus());
								caseTransaction.setNextCaseStageName(i18NUtil.getName(caseNewHandleInfo.getCaseStatus()));
								caseTransaction.setCreatedById(logonUser.getId());
								caseTransaction.setCreatedByName(logonUser.getName());
								caseTransaction.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
								//如果edc已解綁，更新該設備連接檔，最新案件設備連接檔
								if (IAtomsConstants.PARAM_ASSET_STATUS_LOST.equals(formDTO.getStatus())) {
									//周邊設備(xxx) 通過設備管理作業解除綁定 原因:已拆回/已遺失
									caseTransaction.setDescription(i18NUtil.getName(IAtomsMessageCode.ASSET_REMOVE_OR_LOSS, new String[]{dmmRepository.getSerialNumber(), lostName}, null));

								}
								//如果edc已解綁，更新該設備連接檔，最新案件設備連接檔
								if (IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(formDTO.getStatus())) {
									//周邊設備(xxx) 通過設備管理作業解除綁定 原因:已拆回/已遺失
									caseTransaction.setDescription(i18NUtil.getName(IAtomsMessageCode.ASSET_REMOVE_OR_LOSS, new String[]{dmmRepository.getSerialNumber(), returnedName}, null));

								}
								caseTransaction.setDealDate(new Timestamp(setHourForDate(DateTimeUtils.getCurrentTimestamp(), null, null, 0, 0).getTime()));
								caseTransaction.setCaseStatus(caseNewHandleInfo.getCaseStatus());
								caseTransactionList.add(caseTransaction);
							}
							//bug2235 update by 2017/08/25
							//if(StringUtils.hasText(dmmRepositoryDTO.getMaintainCompany())){
								dmmRepository.setMaintainCompany(dmmRepositoryDTO.getMaintainCompany());
						//	}
							//cr2236 update by 2017/08/28
							//if(dmmRepositoryDTO.getAnalyzeDate() != null){
								dmmRepository.setAnalyzeDate(dmmRepositoryDTO.getAnalyzeDate());
							//}
							//cr2236 update by 2017/08/28
							//if(dmmRepositoryDTO.getCaseCompletionDate() != null){
								dmmRepository.setCaseCompletionDate(dmmRepositoryDTO.getCaseCompletionDate());
							//}
								dmmRepository.setMaintainUser(dmmRepositoryDTO.getMaintainUser());
							if(StringUtils.hasText(dmmRepositoryDTO.getMaintainUser())){
								admUser = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, dmmRepositoryDTO.getMaintainUser());
								dmmRepository.setDepartmentId(admUser.getDeptCode());
							}
							//bug2411 新增該欄位 解除綁定時 清除edc流通在外報表所需欄位 update by 2017/09/15
							dmmRepository.setInstalledDeptId(null);
							//(a)若原因為已拆回
							if (IAtomsConstants.PARAM_ASSET_STATUS_RETURNED.equals(formDTO.getStatus())) {
								dmmRepository.setDtid(null);
								dmmRepository.setSimEnableDate(null);
								//bug 2272 update by 2017/08/25 保留一些案件資料
								//dmmRepository.setCaseId(null);
								//dmmRepository.setCaseCompletionDate(null);
								dmmRepository.setApplicationId(null);
								dmmRepository.setTid(null);
								dmmRepository.setMerchantHeaderId(null);
								dmmRepository.setMerchantId(null);
								dmmRepository.setInstalledAdress(null);
								dmmRepository.setInstalledAdressLocation(null);
								dmmRepository.setInstallType(null);
								//dmmRepository.setDepartmentId(null);
								dmmRepository.setStatus(formDTO.getStatus());
								dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
								//執行作業--解除綁定
								dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_REMOVE_OR_LOSS);
								//原因=已拆回，須將 原因，寫入 拆機/報修原因 欄位中 --update by 2017/08/23
								dmmRepository.setUninstallOrRepairReason(returnedName);
								//若使用人不為台新
								if (!IAtomsConstants.PARAM_TSB_EDC.equals(company.getCompanyCode())) {
									srmCaseNewAssetLink.setAction(IAtomsConstants.ACTION_REMOVE);
									srmCaseAssetLink.setAction(IAtomsConstants.ACTION_REMOVE);
								}
							}
							//(b)若原因為已遺失,狀態更新為已遺失,記錄設備資料歷史紀錄
							if (IAtomsConstants.PARAM_ASSET_STATUS_LOST.equals(formDTO.getStatus())) {
								dmmRepository.setStatus(dmmRepositoryDTO.getStatus());
								dmmRepository.setDescription(dmmRepositoryDTO.getDescription());
								//bug2274 原因=已遺失 寫入 拆機/報修原因 欄位中
								dmmRepository.setUninstallOrRepairReason(lostName);
								//執行作業--解除綁定
								dmmRepository.setAction(IAtomsConstants.PARAM_ACTION_REMOVE_OR_LOSS);
								this.copyProperties(dmmRepository, repositoryHist, null);
								repositoryHist.setAssetId(dmmRepository.getAssetId());
								repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(repositoryHist);
								this.dmmRepositoryHistDAO.getDaoSupport().flush();
								//然後狀態更新為待報廢，解除設備與DTID設定
								dmmRepository.setStatus(IAtomsConstants.PARAM_ASSET_STATUS_PENDING_DISABLED);
								dmmRepository.setDtid(null);
								dmmRepository.setSimEnableDate(null);
								dmmRepository.setApplicationId(null);
								dmmRepository.setTid(null);
								dmmRepository.setMerchantHeaderId(null);
								dmmRepository.setMerchantId(null);
								dmmRepository.setInstalledAdress(null);
								dmmRepository.setInstalledAdressLocation(null);
								dmmRepository.setInstallType(null);
								//Bug #2274 已遺失保留，待報廢清除 7個欄位 --update by 2017/08/30 --改為8個 update by 2017/09/08
								dmmRepository.setCaseId(null);
								dmmRepository.setCaseCompletionDate(null);
								dmmRepository.setDepartmentId(null);
								dmmRepository.setMaintainCompany(null);
								dmmRepository.setAnalyzeDate(null);
								dmmRepository.setMaintainUser(null);
								dmmRepository.setUninstallOrRepairReason(null);
								dmmRepository.setDescription(null);
								dmmRepository.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
								
								repositoryHist = new DmmRepositoryHistory();
								//若使用人不為台新
								if (!IAtomsConstants.PARAM_TSB_EDC.equals(company.getCompanyCode())) {
									srmCaseNewAssetLink.setAction(IAtomsConstants.ACTION_LOSS);
									srmCaseAssetLink.setAction(IAtomsConstants.ACTION_LOSS);
								}
							}
							
							//複製庫存主檔內容到庫存歷史檔中
							this.copyProperties(dmmRepository, repositoryHist, null);
							repositoryHist.setAssetId(dmmRepository.getAssetId());
							repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
							repositoryHists.add(repositoryHist);
							//若使用人不為台新
							if (!IAtomsConstants.PARAM_TSB_EDC.equals(company.getCompanyCode())) {
								srmCaseNewAssetLinkList.add(srmCaseNewAssetLink);
								srmCaseAssetLinkList.add(srmCaseAssetLink);
							}
						}
						if (!CollectionUtils.isEmpty(repositoryHists) 
								&& !CollectionUtils.isEmpty(dmmRepositories)) {
							for (DmmRepository dmmRepository : dmmRepositories) {
								this.repositoryDAO.getDaoSupport().saveOrUpdate(dmmRepository);
							}
							for (DmmRepositoryHistory dmmRepositoryHistory : repositoryHists) {
								this.dmmRepositoryHistDAO.getDaoSupport().saveOrUpdate(dmmRepositoryHistory);
							}
							
							for (SrmCaseNewAssetLink caseNewAssetLink : srmCaseNewAssetLinkList) {
								this.srmCaseNewAssetLinkDAO.getDaoSupport().saveOrUpdate(caseNewAssetLink);
							}
							for (SrmCaseAssetLink caseAssetLink : srmCaseAssetLinkList) {
								this.srmCaseAssetLinkDAO.getDaoSupport().saveOrUpdate(caseAssetLink);
							}
							for (SrmCaseTransaction srmcaseTransaction : caseTransactionList) {
								this.srmCaseTransactionDAO.getDaoSupport().saveOrUpdate(srmcaseTransaction);
							}
							this.repositoryDAO.getDaoSupport().flush();
							this.dmmRepositoryHistDAO.getDaoSupport().flush();
							//若使用人不為台新
							if (!CollectionUtils.isEmpty(caseTransactionList)) {
								this.srmCaseNewAssetLinkDAO.getDaoSupport().flush();
								this.srmCaseAssetLinkDAO.getDaoSupport().flush();
								this.srmCaseTransactionDAO.getDaoSupport().flush();
							}
							
							//將結果放入message中
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_REMOVE_SUCCESS, new String[]{});
						} else {
							msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.PARAM_REMOVE_FAILURE, new String[]{});
						}
					}*/
				//}
			}
			sessionContext.setResponseResult(formDTO);
			//如果經過驗證資料已更新，則返回消息提示用戶
			if (IAtomsConstants.PARAM_YES.equals(changeFlag)) {
				msg = new Message(Message.STATUS.FAILURE,IAtomsMessageCode.DATA_ALERDAY_UPDATE);
			}
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.edit(SessionContext sessionContext) is error" + e);
			throw new ServiceException(IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.edit(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#getAssetIdList(cafe.core.context.MultiParameterInquiryContext)
	 *//*
	@Override
	public String getAssetIdList(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		String returnMsg = null;
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) inquiryContext.getParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			Object[] objects = null;
			String action = null;
			
			//CR #2419 如果是批量操作
			if (IAtomsConstants.PARAM_YES.equals(formDTO.getQueryAllSelected())) {
				IAtomsLogonUser user = (IAtomsLogonUser) formDTO.getLogonUser();
				//獲取登入者角色權限
				List<AdmRoleDTO> roleCodes =user.getUserFunctions();
				if(!CollectionUtils.isEmpty(roleCodes)) {
					String roleFlag = IAtomsConstants.PARAM_YES;
					//如果包含廠商角色，則按廠商角色查詢
					for (AdmRoleDTO admRoleDTO : roleCodes) {
						if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
							roleFlag = IAtomsConstants.PARAM_NO;
							break;
						} 
					}
					//廠商角色
					if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
						boolean hasWarehouse = false;
						if (IAtomsConstants.PARAM_YES.equals(user.getAdmUserDTO().getDataAcl())) {
							hasWarehouse = true;
						}
						//未設定控管權限，則可查詢所有倉庫之設備資料
						if (!hasWarehouse) {
							
						}
					} else {
						//客戶角色--顯示登入者所屬公司之設備資料
						String customerId = user.getAdmUserDTO().getCompanyId();
						formDTO.setCompanyId(customerId);
						formDTO.setUserId(null);
					}
				}
				String assetIds = null;
				formDTO.setSort("r.SERIAL_NUMBER");
				formDTO.setOrder("asc");
				List<String> repositoryDTOList = this.repositoryDAO.getAssetIdList(formDTO);
				
				int size = repositoryDTOList.size();
				if (size>1000) {
					returnMsg = i18NUtil.getName(IAtomsConstants.PARAM_MORE_THAN＿ONE_THOUSAND);
					return returnMsg;
				}
				int i = 0;
				if (size>300) {
					for (String dmmRepositoryDTO2 : repositoryDTOList) {
						if ((i%300 == 1 || i == 0 ) && i!=1) {
							assetIds = dmmRepositoryDTO2;
						} else if (i < repositoryDTOList.size()) {
							assetIds +=  "," + dmmRepositoryDTO2;
						}
						if ((i%300 == 0 || i == repositoryDTOList.size()-1) && i>1) {
							//列印壓縮單
							if (StringUtils.pathEquals(formDTO.getEditFlag(), "downloadZip")) {
								objects = this.repositoryDAO.assetValidateByProcedure("downloadZip", assetIds);
							}
							//列印借用單
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
								objects = this.repositoryDAO.assetValidateByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, assetIds);
							}
							if (objects==null || objects[1]==null || "N".equals(objects[1])) {
								break;
							}
							assetIds = "";
						}
						i++;
					}
				} else {
					if (size<2) {
						assetIds = repositoryDTOList.get(0) + ",";
					} else {
						for (String dmmRepositoryDTO2 : repositoryDTOList) {
							if (i == 0) {
								assetIds = dmmRepositoryDTO2;
							} else if (i < repositoryDTOList.size()) {
								assetIds +=  "," + dmmRepositoryDTO2;
							}
							i++;
						}
					}
					//列印壓縮單
					if (StringUtils.pathEquals(formDTO.getEditFlag(), "downloadZip")) {
						objects = this.repositoryDAO.assetValidateByProcedure("downloadZip",assetIds);
					}
					//借用作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
						objects = this.repositoryDAO.assetValidateByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, assetIds);
					}
				}
				//沒有勾選批量操作
			} else {
				if (formDTO.getQueryAssetIds().indexOf(",") < 0) {
					formDTO.setQueryAssetIds(formDTO.getQueryAssetIds() + ",");
				}
				//入庫作業 歸還作業 報廢作業 退回作業 銷毀作業
				if (StringUtils.pathEquals(formDTO.getEditFlag(), "downloadZip")) {
					objects = this.repositoryDAO.assetValidateByProcedure("downloadZip",formDTO.getQueryAssetIds());
				}
				//借用作業
				if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
					objects = this.repositoryDAO.assetValidateByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, formDTO.getQueryAssetIds());
				}
			}
			if (objects[1] != null && "Y".equals(objects[1].toString())) {
				List<String> repositoryDTOList = this.repositoryDAO.getAssetIdList(formDTO);
				int i = 0;
				for (String assetId : repositoryDTOList) {
					if (i == 0) {
						returnMsg = assetId;
					} else if (i < repositoryDTOList.size()) {
						returnMsg +=  "," + assetId;
					}
					i++;
				}
			} else {
				if (objects[0] != null){
					returnMsg = i18NUtil.getName(objects[0].toString());
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.save(SessionContext sessionContext) is error" + e);
			throw new ServiceException(IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.save(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return returnMsg;
	}*/
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#update(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext update(SessionContext sessionContext)
			throws ServiceException {
		try {
			Message msg = null;
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			//獲取其他修改相關內容
			DmmRepositoryDTO dmmRepositoryDTO = formDTO.getDmmRepositoryDTO();
			LogonUser logonUser = formDTO.getLogonUser();
			if (dmmRepositoryDTO != null) {
				DmmRepository repository = new DmmRepository();
				repository = this.repositoryDAO.findByPrimaryKey(DmmRepository.class, dmmRepositoryDTO.getAssetId());
				String faultComponent = IAtomsConstants.MARK_EMPTY_STRING;
				String faultDesc = IAtomsConstants.MARK_EMPTY_STRING;
				//獲取故障組件與故障現象
				if (repository != null) {
					faultComponent = repository.getFaultComponent();
					faultDesc = repository.getFaultDescription();
				}
				//如果查詢存在則賦值
				if (repository != null) {
					//設置異動人員及時間
					repository.setUpdateUser(logonUser.getId());
					repository.setUpdateUserName(logonUser.getName());
					repository.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
					//設置異動項
					//repository.setIsSimEnable(dmmRepositoryDTO.getIsSimEnable());
					//租賃期滿日
					repository.setSimEnableDate(dmmRepositoryDTO.getSimEnableDate());
					//當租賃期滿日有值，將維護模式改為租賃轉買斷 --update by 2017-07-03
					if(StringUtils.hasText(dmmRepositoryDTO.getSimEnableDate() != null ? dmmRepositoryDTO.getSimEnableDate().toString() : "")){
						repository.setMaType(IAtomsConstants.MA_TYPE_LEASE_TO_BUYOUT);
					}
					//cyber驗收日期
					repository.setCyberApprovedDate(dmmRepositoryDTO.getCheckedDate());
					repository.setDescription(dmmRepositoryDTO.getRepairComment());
					repository.setPropertyId(dmmRepositoryDTO.getPropertyId());
					repository.setIsEnabled(dmmRepositoryDTO.getIsEnabled());
					repository.setAssetUser(dmmRepositoryDTO.getAssetUser());
					repository.setAssetOwner(dmmRepositoryDTO.getAssetOwner());
					
					repository.setTid(dmmRepositoryDTO.getTid());
					repository.setDtid(dmmRepositoryDTO.getDtid());
					repository.setContractId(dmmRepositoryDTO.getContractId());
					repository.setEnableDate(dmmRepositoryDTO.getEnableDate());
					repository.setFaultComponent(dmmRepositoryDTO.getFaultComponentId());
					repository.setFaultDescription(dmmRepositoryDTO.getFaultDescriptionId());
					repository.setFactoryWarrantyDate(dmmRepositoryDTO.getFactoryWarrantyDate());
					repository.setCustomerWarrantyDate(dmmRepositoryDTO.getCustomerWarrantyDate());
					repository.setAction(IAtomsConstants.PARAM_ACTION_OTHER_EDIT);
					repository.setDescription(dmmRepositoryDTO.getDescription());
					repository.setSimEnableNo(dmmRepositoryDTO.getSimEnableNo());
					repository.setAssetId(dmmRepositoryDTO.getAssetId());
					DmmRepositoryHistory repositoryHist = new DmmRepositoryHistory();
					//每次異動均在庫存歷史檔存一筆記錄
					repositoryHist.setAssetId(dmmRepositoryDTO.getAssetId());
					this.repositoryDAO.getDaoSupport().update(repository);
					this.repositoryDAO.getDaoSupport().flush();
					DmmRepositoryFaultComId dmmRepositoryFaultComId = null;
					DmmRepositoryFaultCom repositoryFaultCom = new DmmRepositoryFaultCom();
					//判斷舊故障組件是否存在，如不存在則刪除舊資料
					if (StringUtils.hasText(faultComponent)) {
						String[] dmmRepositoryFaultComArray = faultComponent.split(",");
						for (int i = 0; i < dmmRepositoryFaultComArray.length; i++) {
							dmmRepositoryFaultComId = new DmmRepositoryFaultComId(dmmRepositoryDTO.getAssetId(), dmmRepositoryFaultComArray[i]);
							repositoryFaultCom = this.repositoryFaultComDAO.findByPrimaryKey(DmmRepositoryFaultCom.class, dmmRepositoryFaultComId);
							if (repositoryFaultCom!= null && !dmmRepositoryDTO.getFaultComponentId().contains(dmmRepositoryFaultComArray[i])) {
								this.repositoryFaultComDAO.getDaoSupport().delete(repositoryFaultCom);
								this.repositoryFaultComDAO.getDaoSupport().flush();
							}
						}
					} 
					//判斷舊故障現象是否存在，如不存在則刪除舊資料
					DmmRepositoryFaultDescId dmmRepositoryFaultDescId = null;
					DmmRepositoryFaultDesc repositoryFaultDesc = new DmmRepositoryFaultDesc();
					if (StringUtils.hasText(faultDesc)) {
						String[] dmmRepositoryFaultDescArray = faultDesc.split(",");
						for (int i = 0; i < dmmRepositoryFaultDescArray.length; i++) {
							dmmRepositoryFaultDescId = new DmmRepositoryFaultDescId(dmmRepositoryDTO.getAssetId(), dmmRepositoryFaultDescArray[i]);
							repositoryFaultDesc = this.repositoryFaultDescDAO.findByPrimaryKey(DmmRepositoryFaultDesc.class, dmmRepositoryFaultDescId);
							if (repositoryFaultDesc!= null && !dmmRepositoryDTO.getFaultDescriptionId().contains(dmmRepositoryFaultDescArray[i])) {
								this.repositoryFaultDescDAO.getDaoSupport().delete(repositoryFaultDesc);
								this.repositoryFaultDescDAO.getDaoSupport().flush();
							}
						}
					}
					//保存故障組件
					if (StringUtils.hasText(dmmRepositoryDTO.getFaultComponentId())) {
						String[] dmmRepositoryFaultComArray = dmmRepositoryDTO.getFaultComponentId().split(",");
						for (int i = 0; i < dmmRepositoryFaultComArray.length; i++) {
							dmmRepositoryFaultComId = new DmmRepositoryFaultComId(dmmRepositoryDTO.getAssetId(), dmmRepositoryFaultComArray[i]);
							repositoryFaultCom = this.repositoryFaultComDAO.findByPrimaryKey(DmmRepositoryFaultCom.class, dmmRepositoryFaultComId);
							if (repositoryFaultCom == null) {
								repositoryFaultCom = new DmmRepositoryFaultCom(dmmRepositoryFaultComId);
							}
							this.repositoryFaultComDAO.getDaoSupport().saveOrUpdate(repositoryFaultCom);
							this.repositoryFaultComDAO.getDaoSupport().flush();
						}
					}
					//保存故障現象
					if (StringUtils.hasText(dmmRepositoryDTO.getFaultDescriptionId())) {
						String[] dmmRepositoryFaultDescArray = dmmRepositoryDTO.getFaultDescriptionId().split(",");
						for (int i = 0; i < dmmRepositoryFaultDescArray.length; i++) {
							dmmRepositoryFaultDescId = new DmmRepositoryFaultDescId(dmmRepositoryDTO.getAssetId(), dmmRepositoryFaultDescArray[i]);
							repositoryFaultDesc = this.repositoryFaultDescDAO.findByPrimaryKey(DmmRepositoryFaultDesc.class, dmmRepositoryFaultDescId);
							if (repositoryFaultDesc == null) {
								repositoryFaultDesc = new DmmRepositoryFaultDesc(dmmRepositoryFaultDescId);
							}
							this.repositoryFaultDescDAO.getDaoSupport().saveOrUpdate(repositoryFaultDesc);
							this.repositoryFaultDescDAO.getDaoSupport().flush();
						}
					}
					repositoryHist.setHistoryId(this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY));
					this.repositoryDAO.saveRepositoryHist(repository.getAssetId(), repositoryHist.getHistoryId(), repository.getStatus());
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.RESPOSTORY_UPDATE_SUCCESS, new String[]{this.getMyName()});
				} else {
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.RESPOSTORY_UPDATE_FAILURE, new String[]{this.getMyName()});
				}
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.save(SessionContext sessionContext) is error" + e);
			throw new ServiceException(IAtomsMessageCode.UPDATE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.save(SessionContext sessionContext):" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#getEmailByUserId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public AdmUserDTO getEmailByUserId(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		AdmUserDTO reault = null;
		try {
			//獲得借用者ID
			String userId = (String) inquiryContext.getParameter(AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
			String maintainCompany = (String) inquiryContext.getParameter(DmmRepositoryDTO.ATTRIBUTE.MAINTAIN_COMPANY.getValue());
			if (StringUtils.hasText(userId)) {
				reault = this.repositoryDAO.getEmailByUserId(userId);
				if(reault != null){
					//bug2235 
					if ((StringUtils.hasText(maintainCompany))
							&&((!StringUtils.hasText(reault.getCompanyId()))
							|| (!reault.getCompanyId().equals(maintainCompany)))) {
						reault = null;
					}
				}
			}
			return reault;
		} catch (Exception e) {
			LOGGER.error("AssetManageService.getEmailByUserId() Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
	}
	
	/**
	 * Purpose:發送到期未歸還mail
	 * @author amandawang
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private boolean sendUnBackMail() throws ServiceException {
		boolean result = false;
		try {
			List<DmmRepositoryDTO> unBackDTOList = this.repositoryDAO.getUnBackBorrowers();
			String fromMail = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
			//发件人姓名
			String fromName = WfSystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_NAME);
			//map<天，<mail，<人,List<DmmRepositoryDTO>>>>
			Map<String, Map<String, Map<String, List<DmmRepositoryDTO>>>> borrowerMap = new HashMap<String, Map<String,Map<String,List<DmmRepositoryDTO>>>>();
			//<mail，<人,List<DmmRepositoryDTO>>>
			Map<String, Map<String, List<DmmRepositoryDTO>>> mailMap = new HashMap<String, Map<String, List<DmmRepositoryDTO>>>();
			//map<人,list>
			Map<String, List<DmmRepositoryDTO>> boorowMap = new HashMap<String, List<DmmRepositoryDTO>>();
			List<DmmRepositoryDTO> list = new ArrayList<DmmRepositoryDTO>();
			if (!CollectionUtils.isEmpty(unBackDTOList)) {
				for (DmmRepositoryDTO dmmRepositoryDTO : unBackDTOList) {
					//如果沒有這一天
					if (!borrowerMap.containsKey(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10))) {
						//new 這一天的map
						mailMap = new HashMap<String, Map<String,List<DmmRepositoryDTO>>>();
						//如果這一天沒有這個mail
						if (!mailMap.containsKey(dmmRepositoryDTO.getBorrowerEmail())) {
							//new 這個mail的map
							boorowMap = new HashMap<String, List<DmmRepositoryDTO>>();
							//將這個mail的map放進這一天的map之中
							mailMap.put(dmmRepositoryDTO.getBorrowerEmail(), boorowMap);
							//如果這個mail的map沒有這個人
							if (!boorowMap.containsKey(dmmRepositoryDTO.getBorrower())) {
								list = new ArrayList<DmmRepositoryDTO>();
								list.add(dmmRepositoryDTO);
								//將這個人 list 放進 這個mail的map中
								boorowMap.put(dmmRepositoryDTO.getBorrower(), list);
							} else {
								//如果這個mail的map有這個人 將dto放進這個人對應的list中
								boorowMap.get(dmmRepositoryDTO.getBorrower()).add(dmmRepositoryDTO);
							}
							//如果這一天有這個mail
						} else {
							//如果這個mail的map有這個人
							if (mailMap.get(dmmRepositoryDTO.getBorrowerEmail()).containsKey(dmmRepositoryDTO.getBorrower())) {
								//將dto放進這個人對應的list中
								mailMap.get(dmmRepositoryDTO.getBorrowerEmail()).get(dmmRepositoryDTO.getBorrower()).add(dmmRepositoryDTO);
								//如果這個mail的map沒有這個人
							} else {
								list = new ArrayList<DmmRepositoryDTO>();
								list.add(dmmRepositoryDTO);
								//將這個人 list 放進 這個mail的map中
								mailMap.get(dmmRepositoryDTO.getBorrowerEmail()).put(dmmRepositoryDTO.getBorrower(), list);
							}
						}
						//如果沒有這一天 將這一天放入大map中
						borrowerMap.put(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10), mailMap);
						//如果有這一天
					} else {
						//如果這一天有這個mail
						if (borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).containsKey(dmmRepositoryDTO.getBorrowerEmail())) {
							//如果這個mail的map有這個人
							if (borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).get(dmmRepositoryDTO.getBorrowerEmail()).containsKey(dmmRepositoryDTO.getBorrower())) {
								//將dto放進這個人對應的list中
								borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).get(dmmRepositoryDTO.getBorrowerEmail()).get(dmmRepositoryDTO.getBorrower()).add(dmmRepositoryDTO);
								//如果這個mail的map沒有這個人
							} else {
								list = new ArrayList<DmmRepositoryDTO>();
								list.add(dmmRepositoryDTO);
								//將這個人 list 放進 這個mail的map中
								borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).get(dmmRepositoryDTO.getBorrowerEmail()).put(dmmRepositoryDTO.getBorrower(), list);
							}
							//如果這一天沒有這個mail
						} else {
							//new 這個mail的map
							boorowMap = new HashMap<String, List<DmmRepositoryDTO>>();
							//將這個mail的map放進這一天的map之中
							borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).put(dmmRepositoryDTO.getBorrowerEmail(), boorowMap);
							//如果這個mail的map有這個人
							if (borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).get(dmmRepositoryDTO.getBorrowerEmail()).containsKey(dmmRepositoryDTO.getBorrower())) {
								//將dto放進這個人對應的list中
								borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).get(dmmRepositoryDTO.getBorrowerEmail()).get(dmmRepositoryDTO.getBorrower()).add(dmmRepositoryDTO);
								//如果這個mail的map沒有這個人	
							} else {
								list = new ArrayList<DmmRepositoryDTO>();
								list.add(dmmRepositoryDTO);
								//將這個人 list 放進 這個mail的map中
								borrowerMap.get(dmmRepositoryDTO.getBorrowerStart().toString().substring(0, 10)).get(dmmRepositoryDTO.getBorrowerEmail()).put(dmmRepositoryDTO.getBorrower(), list);
							}
						}
					}
				}
				//邮件主题
				String mailSubject = i18NUtil.getName(IAtomsConstants.ASSET_BORROW_UNBACK_MAIL_NAME);
				//发送人Mail地址
				String fromMailAddress = fromMail;
				//接收人Mail地址
				String toMailAddress = null;
				//收件人姓名
				String toName = null;
				StringBuffer strBuffer= null;
				Set<String> borrowerSet = borrowerMap.keySet();
				for (String borrowerKey : borrowerSet) {
					Set<String> mailSet = borrowerMap.get(borrowerKey).keySet();
					for (String mailKey : mailSet) {
						Set<String> boorowSet = borrowerMap.get(borrowerKey).get(mailKey).keySet();
						for (String boorowKey : boorowSet) {
							List<DmmRepositoryDTO> dmmRepositoryDTOs = borrowerMap.get(borrowerKey).get(mailKey).get(boorowKey);
							//sort方法排序
							Collections.sort(dmmRepositoryDTOs, new Comparator<DmmRepositoryDTO>(){  
								public int compare(DmmRepositoryDTO repositoryDTO, DmmRepositoryDTO dmmepositoryDTO) {  
	                                int x = repositoryDTO.getName().compareTo(dmmepositoryDTO.getName());  
	                                int y = repositoryDTO.getSerialNumber().compareTo(dmmepositoryDTO.getSerialNumber()); 
	                                if(x > 0){ 
	                                	return 1;
	                                } else if ( x == 0 ) {
										if (y > 0) {
											return 1;
										} else if (y == 0) {
											return 0;
										} else {
											return -1;
										}
									} else {
										return -1;
									}            
			                     }  
			                });  
							if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
								toMailAddress = dmmRepositoryDTOs.get(0).getBorrowerEmail();
								if (!StringUtils.hasText(toMailAddress)) {
									continue;
								}
								toName = dmmRepositoryDTOs.get(0).getBorrowerName();
								/*您于【借用日期起】 借用 XX台機器，歸還日期已到，請盡速辦理歸還作業，謝謝！
								機型：XX 序號：XXXX*/
								if (StringUtils.hasText(borrowerKey)) {
									String borrowerStart = StringUtils.replace(borrowerKey, IAtomsConstants.MARK_MIDDLE_LINE, IAtomsConstants.MARK_BACKSLASH);
									
									strBuffer = new StringBuffer();
						            for (int i = 0; i < dmmRepositoryDTOs.size(); i++) {
										strBuffer.append("<tr><td ></td><td width='3%'></td><td align='center' width='7%'>機型：</td><td width='25%'>" + dmmRepositoryDTOs.get(i).getName());
										strBuffer.append("</td><td align='center' width='7%'>序號：</td><td width='43%'>" + dmmRepositoryDTOs.get(i).getSerialNumber() + "</td></tr>");
									}
						            //獲取倉管的mail -- Bug #2350  CC 借用通知(倉管)(系統參數維護可設定)
									List<Parameter> ccMailList  = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.BORROW_ADVICE.getCode(), null);
									String ccmailString = IAtomsConstants.MARK_EMPTY_STRING;
									if(!CollectionUtils.isEmpty(ccMailList)){
										ccmailString = IAtomsConstants.MARK_SEMICOLON + listToString(ccMailList, ';');
									}
						            //邮件内容
									String mailContext = strBuffer.toString();
									//邮件主题模板
									String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + AssetManageFormDTO.MAIL_EXAMPLE_SUBJECT_TEXT;
									//邮件内容模板
									String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + AssetManageFormDTO.ASSET_BORROW_UNBACK_MAIL;
									Map<String, Object> variables = new HashMap<String, Object>();
									//接收人名称 
									variables.put(IAtomsConstants.MAIL_TO_NAME, StringUtils.hasText(toName) ? toName : "XXX");
									//接收人Maill地址
									variables.put(IAtomsConstants.MAIL_TO_MAIL, StringUtils.hasText(toMailAddress) ? toMailAddress : "XXX");
									//发件人Maill地址
									variables.put(IAtomsConstants.MAIL_FROM_MAIL, StringUtils.hasText(fromMailAddress) ? fromMailAddress : "XXX");
									//发件人名称 
									variables.put(IAtomsConstants.MAIL_FROM_NAME, StringUtils.hasText(fromName) ? fromName : "XXX");
									//抄送郵件地址 --Bug #2350 也要 CC 借用通知(倉管) (系統參數維護可設定) update by 2017/09/04
									variables.put(IAtomsConstants.MAIL_CC_MAIL, StringUtils.hasText(dmmRepositoryDTOs.get(0).getBorrowerMgrEmail()) ? dmmRepositoryDTOs.get(0).getBorrowerMgrEmail() + ccmailString : ccmailString + "XXX");
									//邮件主题 
									variables.put(IAtomsConstants.MAIL_SUBJECT, mailSubject);
									//邮件内容 -- 借用時間起
									variables.put(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue(), borrowerStart);
									//邮件内容 -- 借用數量
									variables.put(IAtomsConstants.DTAT_TYPE_NUMBER, dmmRepositoryDTOs.size());
									//邮件内容2
									variables.put(IAtomsConstants.MAIL_CONTEXT, mailContext);
									this.mailComponent.sendMailTo(fromMailAddress, toMailAddress, subjectTemplate, textTemplate, variables);
								}
							}
						}
					}
				}
				result = true;
			} else {
				result = false;
				LOGGER.debug(this.getClass().getSimpleName() + ".sendUnBackMail() unBackDTOList is null... ");
			}
			
			
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getSimpleName() + ".sendUnBackMail() is error " + e, e);
		}
		return result;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#check(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext check(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			DmmRepositoryDTO dmmRepositoryDTO = formDTO.getDmmRepositoryDTO();
			boolean isRepeat = this.repositoryDAO.isRepeatPropertyId(dmmRepositoryDTO.getPropertyId(), dmmRepositoryDTO.getAssetId());
		//判斷是否重複，返回
		if (isRepeat) {
			msg = new Message(Message.STATUS.SUCCESS, null, new String[]{});
		} else {
			msg = new Message(Message.STATUS.FAILURE, null, new String[]{});
		}
		//sessionContext.setResponseResult(formDTO);
		sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.check() Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		}
		return sessionContext;
	}

	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#checkDtid(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext checkDtid(SessionContext sessionContext) throws ServiceException {
		Message msg = null;
		Map map = new HashMap();
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			if (formDTO!=null) {
				DmmRepositoryDTO dmmRepositoryDTO = formDTO.getDmmRepositoryDTO();
				boolean isRepeat = this.repositoryDAO.isRepeatDtid(dmmRepositoryDTO.getDtid(),dmmRepositoryDTO.getAssetId());
				//判斷是否重複，返回
				if (isRepeat) {
					msg = new Message(Message.STATUS.SUCCESS,null, new String[]{});
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS);
				} else {
					msg = new Message(Message.STATUS.FAILURE,null, new String[]{});
				}
			}
			
		//sessionContext.setResponseResult(formDTO);
		sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.check() Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE,e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#exportAsset(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext exportAsset(SessionContext sessionContext)
			throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			DmmRepositoryDTO dmmRepositoryDTO = formDTO.getDmmRepositoryDTO();
			//獲取要匯出的集合
			List<DmmRepositoryDTO> list = new ArrayList<DmmRepositoryDTO>();
			list.add(dmmRepositoryDTO);
			formDTO.setList(list);
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.exportAsset() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#getUserByDept(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getUserByDept(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			String departmentId = (String) inquiryContext.getParameter(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
			List<Parameter> parameterList = this.admUserDAO.getUserByDept(departmentId);
			return parameterList;
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.getUserByDept() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":Error in getUserByDept,error - " + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#countAsset(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public Integer countAsset(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			Integer count = 0;
			AssetManageFormDTO formDTO = (AssetManageFormDTO) inquiryContext.getParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			if(IAtomsConstants.ACTION_HISTORY.equals(formDTO.getActionId())){
				count = this.repositoryDAO.getCountByAssetId(formDTO);
			} else {
				count = this.repositoryDAO.count(formDTO);
			}
			return count;
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.countAsset() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName()+":Error in countAsset,error - " + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	/**
	 * Purpose:多線程發送mail
	 * @author amandawang
	 */
	private void sendBorrowMail(final String toId, final String fromId, final String toEmail, final AssetManageFormDTO assetManageFormDTO, final String path) throws ServiceException {
		try {
			//借用通知
			if(StringUtils.hasText(fromId) && StringUtils.hasText(toId)){
				new Thread(
					new Runnable() {
						public void run() {
							sendMail(toId, fromId, toEmail, assetManageFormDTO);
						}
					}
				).start();
			} else {
				new Thread(
					new Runnable() {
						public void run() {
							try {
								//exportByAction(assetManageFormDTO);
								sendMailByAction(assetManageFormDTO, path);
							} catch (Exception e) {
								LOGGER.error(this.getClass().getName()+".exportByAction() is error in Service:" + e, e);
								throw new ServiceException(e);
							}
						}
					}
				).start();
			}
		}catch(Exception e){
			LOGGER.error(this.getClass().getName()+".sendBorrowMail() is error in Service:" + e, e);
			throw new ServiceException(e);
		}
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#sendQueryMail(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext sendQueryMail(SessionContext sessionContext) throws ServiceException {
		long startTime = System.currentTimeMillis();
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//獲取登入者角色權限
			List<AdmRoleDTO> roleCodes =logonUser.getUserFunctions();
			if(!CollectionUtils.isEmpty(roleCodes)) {
				String roleFlag = IAtomsConstants.PARAM_YES;
				//如果包含廠商角色，則按廠商角色查詢
				for (AdmRoleDTO admRoleDTO : roleCodes) {
					if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
						roleFlag = IAtomsConstants.PARAM_NO;
						break;
					} 
				}
				//廠商角色
				if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
					boolean hasWarehouse = false;
					if (IAtomsConstants.PARAM_YES.equals(logonUser.getAdmUserDTO().getDataAcl())) {
						hasWarehouse = true;
					}
					//未設定控管權限，則可查詢所有倉庫之設備資料
					if (!hasWarehouse) {
						formDTO.setUserId(null);
					}
				} else {
					//客戶角色--顯示登入者所屬公司之設備資料
					String customerId = logonUser.getAdmUserDTO().getCompanyId();
					formDTO.setCompanyId(customerId);
					formDTO.setUserId(null);
				}
			}
			exportByAction(formDTO);
			long endTime = System.currentTimeMillis();
			LOGGER.debug(this.getClass().getName() + "sendQueryMail", " end ", (endTime - startTime));
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsConstants.MSG_ASSET_MAIL_IS_SENDED));
			//msg = new Message(Message.STATUS.SUCCESS, IAtomsConstants.MSG_ASSET_MAIL_IS_SENDED, new String[]{});

			return sessionContext;
		} catch (Exception e) {
			LOGGER.error("AssetManageService.sendQueryMail() Service Exception--->" + e, e);
			sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, null));
		}
		return sessionContext;
	}
	
	/**
	 * Purpose:cr2563 查詢出來的資料，可以以附件方式，寄給 通知人員(比照轉倉)
	 * @param command
	 * @return
	 * @throws Exception 
	 * @throws CommonException
	 */
	public void exportByAction(AssetManageFormDTO command) throws Exception{
		//附件暫存歷經
		String path = null;
		//汇出后的xsl名称
		String reportFileName = null;
		String name = null;
		String tempPath = null;
		try{
			String ucNo = command.getUseCaseNo();
			String uuid = UUID.randomUUID().toString();
			List<String> fileList = null;
			//附件暫存歷經
			path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH) + File.separator + ucNo + File.separator + command.getActionId() + File.separator + uuid + File.separator ;
			//汇出后的xsl名称
			reportFileName = AssetManageFormDTO.REPORT_FILE_NAME;
			name = reportFileName + JasperReportCriteriaDTO.REPORT_EXT_NAME_MSEXCEL;
			IAtomsLogonUser logonUser = (IAtomsLogonUser) command.getLogonUser();
			
			String[] exportFileds = command.getExportFields();
			String historyExport = command.getHistoryExport();
			SessionContext returnCtx = null;
			//JasperReport條件設定DTO
			JasperReportCriteriaDTO criteria = new JasperReportCriteriaDTO();
			List<CrossTabReportDTO> crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
			// 是否使用iReport设计jrxml的方式（true:不使用iReport, false:使用iReport）
			criteria.setAutoBuildJasper(false);
			//报表模板名称
			String jrxmlName =AssetManageFormDTO.EXPORT_JRXML_NAME_LIST;
			
			//掃碼槍后匯出
			if (DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue().equals(command.getCodeGunFlag())){
				if (!cafe.core.util.StringUtils.hasText(command.getQuerySerialNumbers())) {
					command.setQuerySerialNumbers(command.getExportCodeGunSerialNumbers());
					command.setTotalSize(null);
				}
			}
			if (DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue().equals(command.getCodeGunFlag())){
				if (!cafe.core.util.StringUtils.hasText(command.getQueryPropertyIds())) {
					command.setQueryPropertyIds(command.getExportCodeGunPropertyIds());
					command.setTotalSize(null);
				}
			}
			command.setRows(null);
			command.setPage(null);
			
			//歷史資料查詢
			if (StringUtils.pathEquals(historyExport, IAtomsConstants.PARAM_REPOSITORY_STATUS_IN_STORAGE)) {
				List<DmmRepositoryHistoryDTO> results = this.repositoryDAO.getListByAssetId(command);
				if (!CollectionUtils.isEmpty(results)) {
					 for (DmmRepositoryHistoryDTO dmmRepositoryDTO : results) {
						if (StringUtils.hasText(dmmRepositoryDTO.getActionValue())) {
							if (dmmRepositoryDTO.getActionValue().indexOf("-") != -1) {
								dmmRepositoryDTO.setAction(i18NUtil.getName(dmmRepositoryDTO.getActionValue().substring(0, dmmRepositoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryDTO.getAction());
							}
						}
					}
				}
				if (!CollectionUtils.isEmpty(results)) {
					crossTabReportDTOList = getCrossTabReportList(results, null, exportFileds, command.getFieldMap());
				}
				
			} else {
				List<DmmRepositoryDTO> results = this.repositoryDAO.listBy(command);
				if (!CollectionUtils.isEmpty(results)) {
					 for (DmmRepositoryDTO dmmRepositoryDTO : results) {
						if (StringUtils.hasText(dmmRepositoryDTO.getActionValue())) {
							if (dmmRepositoryDTO.getActionValue().indexOf("-") != -1) {
								dmmRepositoryDTO.setAction(i18NUtil.getName(dmmRepositoryDTO.getActionValue().substring(0, dmmRepositoryDTO.getActionValue().indexOf("-"))) + "-" +  dmmRepositoryDTO.getAction());
							}
						}
					}
				}
				if (!CollectionUtils.isEmpty(results)) {
					crossTabReportDTOList = getCrossTabReportList(null, results, exportFileds, command.getFieldMap());
				}
			}
			criteria.setResult(crossTabReportDTOList);
			//設置所需報表的Name
			criteria.setJrxmlName(jrxmlName);
			//設置報表路徑
			criteria.setJrxmlPath(IAtomsConstants.JRXML_PATH);
			//設置匯出格式
			criteria.setType(JasperReportCriteriaDTO.REPORT_TYPE_MSEXCEL);			
			//設置報表Name
			criteria.setReportFileName(reportFileName);
			criteria.setSheetName(reportFileName);
			ReportExporter.exportReportUnDownload(criteria, path, name);
			
			fileList = new ArrayList<String>();
			File file = new File(path + name);
			tempPath = path;
			if (file.exists() && file.isFile()) {
				fileList.add(path +  name);
			}
			FileUtils.compressByMs950(path, AssetManageFormDTO.REPORT_FILE_NAME + ".zip", fileList);
			if(file!=null && file.isFile()){
				FileUtils.removeFile(path +  name);
			}
			try {
				sendBorrowMail(null, null, null, command, path + AssetManageFormDTO.REPORT_FILE_NAME + ".zip");
			} catch (Exception e) {
				LOGGER.error("AssetManageController exportByAction - sendBorrowMail() is error: " + e);
			}
			//sendMailByAction(command, logonUser, path + AssetManageFormDTO.REPORT_FILE_NAME + ".zip");
		} catch(Exception e) {
			LOGGER.error("AssetManageController exportByAction() is error: " + e);
			File file = new File(tempPath +  name);
			if(file!=null && file.isFile()){
				FileUtils.removeFile(tempPath +  name);
			}
			file = new File(tempPath + AssetManageFormDTO.REPORT_FILE_NAME + ".zip");
			if(file!=null && file.isFile()){
				FileUtils.removeFile(tempPath + AssetManageFormDTO.REPORT_FILE_NAME + ".zip");
			}
			throw new ServiceException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
	}
	/**
	 * Purpose:發送有副當的查詢結果mail--cr2563 查詢出來的資料，可以以附件方式，寄給 通知人員(比照轉倉)
	 * @param assetManageFormDTO
	 * @param path：附件路徑
	 * @throws Exception 
	 * @throws CommonException
	 */
	private void sendMailByAction(AssetManageFormDTO assetManageFormDTO, String path) throws Exception{
		File file = new File(path);
		try{
			String fromMail = SystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
			String[] toMailIds = assetManageFormDTO.getToMail().split(IAtomsConstants.MARK_SEPARATOR);
			IAtomsLogonUser logonUser = (IAtomsLogonUser) assetManageFormDTO.getLogonUser();
			if (logonUser == null) {
				return;
			}
			String toMail = null;
			String toMailUserId = null;
			String toName = null;
			AdmUser to = null;
			String[] attachments = null;
			//郵件內容
			String mailContext = null;
			//郵件主題模板
			String subjectTemplate = null;
			//郵件內容模板
			String textTemplate = null;
			//郵件內容
			Map<String, Object> variables = null;
			//邮件主题
			String mailSubject = i18NUtil.getName(IAtomsConstants.ASSET_SEND_MAIL_NAME);
			for (int i = 0; i < toMailIds.length; i++) {
				toMailUserId = toMailIds[i];
				to = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, toMailUserId);
				toMail = to.getEmail();
				toName = to.getCname();
				//如果收件人沒有email則不發送郵件
				if (!StringUtils.hasText(toMail)) {
					continue;
				}
				attachments = new String[]{path};
				//郵件內容
				//CR #2563 修改mail通知格式 2017/10/20
				mailContext = logonUser.getName() + " 寄送設備資料，詳見附檔。";
				//郵件主題模板
				subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + "mailExampleSubjectText.txt";
				//郵件內容模板
				textTemplate = MailComponent.MAIL_TEMPLATE_ADD + "sendAssetQueryContext.html";
				//郵件內容
				variables = new HashMap<String, Object>();
				//接收人名称 
				variables.put(IAtomsConstants.MAIL_TO_NAME, toName);
				//接收人Maill地址
				variables.put(IAtomsConstants.MAIL_TO_MAIL, toMail);
				//发件人Maill地址
				variables.put(IAtomsConstants.MAIL_FROM_MAIL, StringUtils.hasText(fromMail) ? fromMail : "XXX");
				//发件人名称 
				//variables.put(IAtomsConstants.MAIL_FROM_NAME, StringUtils.hasText(fromName)?fromName:"XXX");
				//邮件主题 
				variables.put(IAtomsConstants.MAIL_SUBJECT, mailSubject);
				//邮件内容2
				variables.put(IAtomsConstants.MAIL_CONTEXT, mailContext);
				variables.put(IAtomsConstants.MAIL_CC_MAIL, "XXX");
				try{
					this.mailComponent.sendMailTo(fromMail, toMail, subjectTemplate, attachments, textTemplate, variables);
				} catch (Exception e) {
					LOGGER.error("sendMailByAction:mailComponent--->sendMailTo(): ", "DataAccess Exception:", e);
				}
			}
			if(file!=null && file.isFile()){
				FileUtils.removeFile(path);
			}
		} catch (ServiceException e) {
			if(file!=null && file.isFile()){
				FileUtils.removeFile(path);
			}
			LOGGER.error(this.getClass().getSimpleName() + ".sendMailByAction()" + e, e);
		}
	}
	/**
	 * Purpose:發送mail
	 * @author amandawang
	 * @param toId：收件人
	 * @param fromId：發件人
	 * @param toEmail:收件人email
	 * @param assetTransId：轉倉批號
	 * @param refuseReason：
	 * @return boolean
	 */
	private boolean sendMail(String toId, String fromId, String toEmail, AssetManageFormDTO assetManageFormDTO){
		boolean result = false;
		List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
		try {
			if(StringUtils.hasText(fromId) && StringUtils.hasText(toId)){
				dmmRepositoryDTOs = this.repositoryDAO.listBy(assetManageFormDTO);
				String fromMail = SystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_MAIL);
				String fromName = SystemConfigManager.getProperty(IAtomsConstants.MAIL, IAtomsConstants.MAIL_FROM_NAME);
				AdmUser from = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, fromId);
				AdmUser to = (AdmUser) this.admUserDAO.findByPrimaryKey(AdmUser.class, toId);
				if(from != null && to != null){
					//发送人Mail地址
					String fromMailAddress = fromMail;
					//接收人Mail地址
					String toMailAddress = toEmail;
					//如果收件人沒有email則不發送郵件
					if (!StringUtils.hasText(toMailAddress)) {
						return true;
					}
					//獲取倉管的mail
					List<Parameter> ccMailList  = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.BORROW_ADVICE.getCode(), null);
					String ccmailString = listToString(ccMailList, ';');
					//收件人姓名
					String toName = to.getCname();
					//邮件主题
					String mailSubject = i18NUtil.getName(IAtomsConstants.ASSET_BORROW_MAIL_NAME);
					DmmRepositoryDTO dmmRepositoryDTO = assetManageFormDTO.getDmmRepositoryDTO();
					Date borrowEnd = dmmRepositoryDTO.getBorrowerEnd();
					StringBuffer strBuffer= new StringBuffer();
					String mailContext = null;
					/*iATOMS-資產借用通知，Mail內容：您於【借用日期起】 借用 XX台機器，請於【借用日期迄】前辦理歸還作業，謝謝！
					機型：XX 序號：XXXX*/
					if(assetManageFormDTO != null){
						if (dmmRepositoryDTO!= null) {
							//DateTimeUtils.DT_FMT_CH_YYYYMMDD_SLASH.hashCode();
							String borrowerStart = new SimpleDateFormat("yyyy/MM/dd").format(dmmRepositoryDTO.getBorrowerStart());
							String borrowerEnd = new SimpleDateFormat("yyyy/MM/dd").format(borrowEnd);
							strBuffer = new StringBuffer();
							//sort方法排序
							Collections.sort(dmmRepositoryDTOs, new Comparator<DmmRepositoryDTO>(){  
								public int compare(DmmRepositoryDTO repositoryDTO, DmmRepositoryDTO dmmepositoryDTO) {  
	                                int x = repositoryDTO.getName().compareTo(dmmepositoryDTO.getName());  
	                                int y = repositoryDTO.getSerialNumber().compareTo(dmmepositoryDTO.getSerialNumber()); 
	                                if(x > 0){ 
	                                	return 1;
	                                } else if ( x == 0 ) {
										if (y > 0) {
											return 1;
										} else if (y == 0) {
											return 0;
										} else {
											return -1;
										}
									} else {
										return -1;
									}            
			                     }  
			                });  
				            for (int i = 0; i < dmmRepositoryDTOs.size(); i++) {
								strBuffer.append("<tr><td ></td><td width='3%'></td><td align='center' width='7%'>機型：</td><td width='25%'>" + dmmRepositoryDTOs.get(i).getName());
								strBuffer.append("</td><td align='center' width='7%'>序號：</td><td width='43%'>" + dmmRepositoryDTOs.get(i).getSerialNumber() + "</td></tr>");
							}
				            mailContext = strBuffer.toString();
				            //邮件主题模板
							String subjectTemplate = MailComponent.MAIL_TEMPLATE_ADD + AssetManageFormDTO.MAIL_EXAMPLE_SUBJECT_TEXT;
							//邮件内容模板
							String textTemplate = MailComponent.MAIL_TEMPLATE_ADD + AssetManageFormDTO.ASSET_BORROW_CONTEXT;
							Map<String, Object> variables = new HashMap<String, Object>();
							//接收人名称 
							variables.put(IAtomsConstants.MAIL_TO_NAME, toName);
							//接收人Maill地址
							variables.put(IAtomsConstants.MAIL_TO_MAIL, toMailAddress);
							//发件人Maill地址
							variables.put(IAtomsConstants.MAIL_FROM_MAIL, StringUtils.hasText(fromMailAddress)?fromMailAddress : "XXX");
							//发件人名称 
							variables.put(IAtomsConstants.MAIL_FROM_NAME, StringUtils.hasText(fromName)?fromName:"XXX");
							//抄送郵件地址 增加倉管人員mail --update by 2017-07-28
							variables.put(IAtomsConstants.MAIL_CC_MAIL, dmmRepositoryDTO.getBorrowerMgrEmail() + ";" + ccmailString);
							//邮件主题 
							variables.put(IAtomsConstants.MAIL_SUBJECT, mailSubject);
							//邮件内容 -- 借用時間起
							variables.put(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue(), borrowerStart);
							//邮件内容 -- 借用時間迄
							variables.put(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue(), borrowerEnd);
							//邮件内容 -- 借用數量
							variables.put(IAtomsConstants.DTAT_TYPE_NUMBER, dmmRepositoryDTOs.size());
							//邮件内容2
							variables.put(IAtomsConstants.MAIL_CONTEXT, mailContext);
							this.mailComponent.sendMailTo(fromMailAddress, toMailAddress, subjectTemplate, textTemplate, variables);
						}
					}
				}
			}
		} catch (ServiceException e) {
			LOGGER.error(this.getClass().getSimpleName() + ".sendMail()" + e, e);
		}
		return result;
	}
	
	/**
	 * Purpose:獲取交叉報表所需集合
	 * @author amandawang
	 * @param repositoryHistoryDTOs：庫存歷史集合
	 * @param repositoryDTOs：庫存集合
	 * @param exportFileds：需要的字段
	 * @throws CommonException
	 * @return List<CrossTabReportDTO>：交叉報表集合
	 */
	public List<CrossTabReportDTO> getCrossTabReportList(List<DmmRepositoryHistoryDTO> repositoryHistoryDTOs, List<DmmRepositoryDTO> repositoryDTOs, String[] exportFileds, Map<String, String> fieldMap) throws CommonException {
		int size = 0;
		//庫存歷史list不為空則匯出庫存歷史檔資料
		if (!CollectionUtils.isEmpty(repositoryHistoryDTOs)) { 
			size = repositoryHistoryDTOs.size();
		} else {
			//反之，匯出庫存檔資料
			size = repositoryDTOs.size();
		}
		String columnName = "";
		String content = "";
		CrossTabReportDTO crossTabDTO = null;
		List<CrossTabReportDTO> crossTabReportDTOList = new ArrayList<CrossTabReportDTO>();
		try {
			//日期格式需要轉換
			for(int i=0; i < size; i++){
				for(int j=0; j < exportFileds.length; j++){
					columnName = exportFileds[j];
					if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.BORROWER_START.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.BORROWER_END.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.BACK_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.CASE_COMPLETION_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue())) {
						content = IAtomsUtils.getFiledValueByName(DmmRepositoryDTO.ATTRIBUTE.CHECKED_DATE.getValue(), !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
						content = DateTimeUtils.toString(DateTimeUtils.toDate(content), DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
					} else if (StringUtils.pathEquals(columnName, DmmRepositoryDTO.ATTRIBUTE.WAREHOUSE_CONFIRM.getValue())) {
						content = "";
					} else {
						content = IAtomsUtils.getFiledValueByName(columnName, !CollectionUtils.isEmpty(repositoryHistoryDTOs) ? repositoryHistoryDTOs.get(i) : repositoryDTOs.get(i));
					}
					crossTabDTO = new CrossTabReportDTO();
					crossTabDTO.setRowNo(i + 1);
					crossTabDTO.setColumnName(fieldMap.get(columnName));
					crossTabDTO.setContent(content);
					crossTabReportDTOList.add(crossTabDTO);
				}
			}
		} catch (Exception e) {
			LOGGER.error("AssetManageController export() getCrossTabReportList() is error: " + e);
			throw new CommonException(IAtomsMessageCode.EXPORT_REPORT_FAILURE);
		}
		
		return crossTabReportDTOList;
	}
	
	/**
	 * Purpose:將list拼入分隔符轉為字符串
	 * @author amandawang
	 * @param list
	 * @param separator:分隔符
	 * @return String
	 */
	public String listToString(List<Parameter> list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getValue()).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	/** Purpose: 複製對象屬性
	 * @author FelixLi
	 * @param source:源對象
	 * @param target:需要複製屬性的對象
	 * @param ignoreProperties
	 * @throws BeansException
	 * @return void
	 */
	public void copyProperties(Object source, Object target,String[] ignoreProperties) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		for (int i = 0; i < targetPds.length; i++) {
			PropertyDescriptor targetPd = targetPds[i];
			if (targetPd.getWriteMethod() != null
					&& (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source, new Object[0]);
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, new Object[] { value });
						}
					} catch (Throwable ex) {
						throw new FatalBeanException(this.getClass().getName() + "Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#queryEDC(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext queryEDC(SessionContext sessionContext) throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			//查詢條件
			String queryAssetType = formDTO.getQueryAssetType();
			String queryAssetName = formDTO.getQueryAssetName();
			String queryPeople = formDTO.getQueryPeople();
			String queryCommet = formDTO.getQueryCommet();
			String queryHouse = formDTO.getQueryHouse();
			String queryNumber = formDTO.getQueryNumber();
			//排序列名
			String sort = formDTO.getSort();
			String order = formDTO.getOrder();
			//當前頁碼
			Integer currentPage = formDTO.getPage();
			//一頁的條數
			Integer pageSize = formDTO.getRows();
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
			//獲取查詢結果
			List<DmmRepositoryDTO> EDCList = this.repositoryDAO.getEDC(queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber, sort, order, currentPage, pageSize);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, EDCList);
			if (!CollectionUtils.isEmpty(EDCList)) {
				//如果查詢結果不為空，放入formDTO
				formDTO.setList(EDCList);
				//查詢總筆數
				int count = this.repositoryDAO.getEDCCount(queryAssetType, queryAssetName, queryPeople, queryCommet, queryHouse, queryNumber);
				//總筆數放入formDTO
				formDTO.getPageNavigation().setRowCount(count);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, count);
				map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, EDCList);
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
			}else{
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DATA_NOT_FOUND));
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setResponseResult(formDTO);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.list() Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String []{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#initSearchEmployee(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initSearchEmployee(SessionContext sessionContext)
			throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			if (StringUtils.hasText(formDTO.getMaintenanceUserFlag())) {
				List<Parameter> deptList = this.departmentDAO.getDeptList(formDTO.getMaintenanceUserFlag(), true);
				formDTO.setDepartmentList(deptList);
			}
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.initSearchEmployee(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.initSearchEmployee(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#initMid(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext initMid(SessionContext sessionContext)
			throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			String companyId = null;
			if (formDTO.getMerchantFormDTO() != null && formDTO.getMerchantFormDTO().getCompanyDTO()!=null && StringUtils.hasText(formDTO.getMerchantFormDTO().getCompanyDTO().getCompanyId())) {
				companyId = formDTO.getMerchantFormDTO().getCompanyDTO().getCompanyId();
				BimCompany company = this.companyDAO.findByPrimaryKey(BimCompany.class, companyId);
				formDTO.getMerchantFormDTO().getCompanyDTO().setShortName(company.getShortName());
			}
			
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.initMid(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.initMid(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#checkAssetUserIsTaixinRent(cafe.core.context.MultiParameterInquiryContext)
	 */
	/*public SessionContext checkAssetUserIsTaixinRent(MultiParameterInquiryContext inquiryContext)throws ServiceException {
		try {
			String serialNumber = (String) inquiryContext.getParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
			if (StringUtils.hasText(serialNumber)) {
				List<String> serialNumbers = StringUtils.toList(serialNumber, IAtomsConstants.MARK_SEPARATOR);
				List<DmmRepositoryDTO> dmmRepositoryDTOs = this.repositoryDAO.checkAssetUserIsTaixinRent(serialNumbers);
				if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}*/
	/**
	 * Purpose:計算增加時分秒后的實際日期
	 * @author CrissZhang
	 * @return Date ：返回Date類型
	 */
	private Date setHourForDate(Date currentDate, Integer hour, Integer minute, Integer second, Integer millis){
		Calendar now = Calendar.getInstance();
		now.setTime(currentDate);
		// 如果小時數不為空，則放置小時數
		if(hour != null){
			now.set(Calendar.HOUR_OF_DAY, hour);
		}
		// 如果分鐘數不為空，則放置分鐘數
		if(minute != null){
			now.set(Calendar.MINUTE, minute);
		}
		// 如果秒不為空，則放置秒
		if(second != null){
			now.set(Calendar.SECOND, second);
		}
		// 
		if(millis != null){
			now.set(Calendar.MILLISECOND, second);
		}
		return now.getTime();
	}
	
	@Override
	public SessionContext getCountByAsset(SessionContext sessionContext)
			throws ServiceException {
		try {
			Integer count = 0;
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			Map map = new HashMap();
			if (formDTO != null) {
				if(IAtomsConstants.PARAM_REPOSITORY_STATUS_IN_STORAGE.equals(formDTO.getQueryHistory())){
					count = this.repositoryDAO.getCountByAssetId(formDTO);
				} else {
					count = this.repositoryDAO.count(formDTO);
				}
				sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
			} else {
				sessionContext.setReturnMessage(new Message(Message.STATUS.FAILURE, IAtomsMessageCode.QUERY_FAILURE));
			}
			formDTO.getPageNavigation().setRowCount(count);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, count);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<DmmRepositoryDTO>());
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.getCountByAsset(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.getCountByAsset(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	@Override
	public SessionContext getAssetIdList(SessionContext sessionContext) throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			Object[] objects = null;
			String returnMsg = null;
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, 0);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, new ArrayList<DmmRepositoryDTO>());
			//CR #2419 如果是批量操作
			if (IAtomsConstants.PARAM_YES.equals(formDTO.getQueryAllSelected())) {
				IAtomsLogonUser user = (IAtomsLogonUser) formDTO.getLogonUser();
				//獲取登入者角色權限
				List<AdmRoleDTO> roleCodes =user.getUserFunctions();
				if(!CollectionUtils.isEmpty(roleCodes)) {
					String roleFlag = IAtomsConstants.PARAM_YES;
					//如果包含廠商角色，則按廠商角色查詢
					for (AdmRoleDTO admRoleDTO : roleCodes) {
						if (StringUtils.pathEquals(admRoleDTO.getAttribute(), IAtomsConstants.VECTOR_ROLE_ATTRIBUTE)) {
							roleFlag = IAtomsConstants.PARAM_NO;
							break;
						} 
					}
					//廠商角色
					if (StringUtils.pathEquals(roleFlag, IAtomsConstants.PARAM_NO)) {
						boolean hasWarehouse = false;
						if (IAtomsConstants.PARAM_YES.equals(user.getAdmUserDTO().getDataAcl())) {
							hasWarehouse = true;
						}
						//未設定控管權限，則可查詢所有倉庫之設備資料
						if (!hasWarehouse) {
							
						}
					} else {
						//客戶角色--顯示登入者所屬公司之設備資料
						String customerId = user.getAdmUserDTO().getCompanyId();
						formDTO.setCompanyId(customerId);
						formDTO.setUserId(null);
					}
				}
				String assetIds = null;
				formDTO.setSort("r.SERIAL_NUMBER");
				formDTO.setOrder("asc");
				List<String> repositoryDTOList = this.repositoryDAO.getAssetIdList(formDTO);
				
				int size = repositoryDTOList.size();
				if (size>1000) {
					returnMsg = i18NUtil.getName(IAtomsConstants.PARAM_MORE_THAN＿ONE_THOUSAND);
					sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
					map.put("returnMsg", returnMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					sessionContext.setResponseResult(formDTO);
					return sessionContext;
				}
				int i = 0;
				if (size>300) {
					for (String dmmRepositoryDTO2 : repositoryDTOList) {
						if ((i%300 == 1 || i == 0 ) && i!=1) {
							assetIds = dmmRepositoryDTO2;
						} else if (i < repositoryDTOList.size()) {
							assetIds +=  "," + dmmRepositoryDTO2;
						}
						if ((i%300 == 0 || i == repositoryDTOList.size()-1) && i>1) {
							//列印壓縮單
							if (StringUtils.pathEquals(formDTO.getEditFlag(), "downloadZip")) {
								objects = this.repositoryDAO.assetValidateByProcedure("downloadZip", assetIds);
							}
							//列印借用單
							if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
								objects = this.repositoryDAO.assetValidateByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, assetIds);
							}
							if (objects==null || objects[1]==null || "N".equals(objects[1])) {
								break;
							}
							assetIds = "";
						}
						i++;
					}
				} else {
					if (size<2) {
						assetIds = repositoryDTOList.get(0) + ",";
					} else {
						for (String dmmRepositoryDTO2 : repositoryDTOList) {
							if (i == 0) {
								assetIds = dmmRepositoryDTO2;
							} else if (i < repositoryDTOList.size()) {
								assetIds +=  "," + dmmRepositoryDTO2;
							}
							i++;
						}
					}
					//列印壓縮單
					if (StringUtils.pathEquals(formDTO.getEditFlag(), "downloadZip")) {
						objects = this.repositoryDAO.assetValidateByProcedure("downloadZip",assetIds);
					}
					//借用作業
					if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
						objects = this.repositoryDAO.assetValidateByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, assetIds);
					}
				}
				//沒有勾選批量操作
			} else {
				if (formDTO.getQueryAssetIds().indexOf(",") < 0) {
					formDTO.setQueryAssetIds(formDTO.getQueryAssetIds() + ",");
				}
				//入庫作業 歸還作業 報廢作業 退回作業 銷毀作業
				if (StringUtils.pathEquals(formDTO.getEditFlag(), "downloadZip")) {
					objects = this.repositoryDAO.assetValidateByProcedure("downloadZip",formDTO.getQueryAssetIds());
				}
				//借用作業
				if (StringUtils.pathEquals(formDTO.getEditFlag(), AssetManageFormDTO.ACTION_BORROW)) {
					objects = this.repositoryDAO.assetValidateByProcedure(IAtomsConstants.PARAM_ACTION_BORROW, formDTO.getQueryAssetIds());
				}
			}
			if (objects[1] != null && "Y".equals(objects[1].toString())) {
				List<String> repositoryDTOList = this.repositoryDAO.getAssetIdList(formDTO);
				int i = 0;
				for (String assetId : repositoryDTOList) {
					if (i == 0) {
						returnMsg = assetId;
					} else if (i < repositoryDTOList.size()) {
						returnMsg +=  "," + assetId;
					}
					i++;
				}
			} else {
				if (objects[0] != null){
					returnMsg = i18NUtil.getName(objects[0].toString());
				}
			}
			map.put("returnMsg", returnMsg);
			sessionContext.setReturnMessage(new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS));
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.getAssetIdList(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.getAssetIdList(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	
	@Override
	public SessionContext changeAssetStatus(SessionContext sessionContext) throws ServiceException {
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			Transformer transformer = new SimpleDtoDmoTransformer();
			Message msg = null;
			List<String> roles = new ArrayList<String>();
			//系統管理員
			roles.add("SYSTEM");
			AdmUserDTO user = this.admUserDAO.getUserDTOsBy(roles).get(0);
			DmmRepositoryDTO  dmmRepositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(formDTO.getQuerySerialNumbers(), null, null);
			if (dmmRepositoryDTO==null) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.IMPORT_VALUE_NOT_FOUND);
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			DmmRepository dmmRepository = (DmmRepository) transformer.transform(dmmRepositoryDTO, new DmmRepository());
			//庫存歷史檔id
			String historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
			if ("DEVICE_DISABLE".equals(formDTO.getQueryAction())) {
				dmmRepository.setStatus("STOP");
			} else if("DEVICE_ENABLE".equals(formDTO.getQueryAction())) {
				dmmRepository.setStatus("IN_USE");
			}
			dmmRepository.setAction(formDTO.getQueryAction());
			dmmRepository.setDescription(formDTO.getQueryCommet());
			dmmRepository.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
			if (user != null) {
				LOGGER.debug(".changeAssetStatus() -->userId="+user.getUserId());
				dmmRepository.setUpdateUser(user.getUserId());
				dmmRepository.setUpdateUserName(user.getCname());
			}
			this.repositoryDAO.update(dmmRepository);
			DmmRepositoryHistory repositoryHist = new DmmRepositoryHistory();
			//複製庫存主檔內容到庫存歷史檔中
			this.copyProperties(dmmRepository, repositoryHist, null);
			repositoryHist.setAssetId(dmmRepository.getAssetId());
			repositoryHist.setHistoryId(historyId);
			this.dmmRepositoryHistDAO.save(repositoryHist);
			//Task #3502
			ApiCmsRepository apiCmsRepository = null;
			//查詢台新銀行id
			CompanyDTO companyDTO = this.companyDAO.getCompanyByCompanyCode(IAtomsConstants.PARAM_TSB_EDC);
			if (companyDTO != null) {
				List<Parameter> merchant = this.merchantDAO.getMerchantsByCodeAndCompamyId(null, companyDTO.getCompanyId());
				for (Parameter parameter : merchant) {
					//判斷特店代號是否歸屬台新銀行
					if (StringUtils.hasText(parameter.getName()) && parameter.getName().equals(formDTO.getQueryMerchantCode())) {
						//若是，將資料寫入apiCmsRepository
						apiCmsRepository = new ApiCmsRepository();
						apiCmsRepository.setSystemId(this.generateGeneralUUID("API_CMS_REPOSITORY"));
						LOGGER.debug(".changeAssetStatus() new apiCmsRepository ==> systemId=" + apiCmsRepository.getSystemId());
						apiCmsRepository.setSerialNumber(formDTO.getQuerySerialNumbers());
						apiCmsRepository.setDtid(formDTO.getQueryDtid());
						apiCmsRepository.setNotifyFlag("0");
						apiCmsRepository.setMerchantCode(formDTO.getQueryMerchantCode());
						apiCmsRepository.setMemo(formDTO.getQueryCommet());
						apiCmsRepository.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						if ("DEVICE_DISABLE".equals(formDTO.getQueryAction())) {
							apiCmsRepository.setJobType("05");
						} else if("DEVICE_ENABLE".equals(formDTO.getQueryAction())) {
							apiCmsRepository.setJobType("06");
						}
						this.apiCmsRepositoryDAO.getDaoSupport().saveOrUpdate(apiCmsRepository);
						LOGGER.debug(".changeAssetStatus() save apiCmsRepository...");
						break;
					}
				}
			} else {
				LOGGER.debug(".changeAssetStatus() companyDTO is null...");
			}
			//Task #3519
			sessionContext.setAttribute("historyId", historyId);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.IATOMS_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.changeAssetStatus(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.changeAssetStatus(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}
	/**
	 * Purpose: 查詢台新銀行設備停用、啟用資料上傳ftps
	 * @author amandawang
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 */
	public void queryAssetInfo() throws ServiceException{
		List<String> pathList = new ArrayList<String>();
		try {
			List<ApiCmsRepositoryDTO> apiCmsRepositoryDTOs = this.apiCmsRepositoryDAO.listBy();
			if (!CollectionUtils.isEmpty(apiCmsRepositoryDTOs) && apiCmsRepositoryDTOs.get(0) != null) {
				String[] assetRow = null;
				List<String> assetRowsList = new ArrayList<String>();
				for (ApiCmsRepositoryDTO apiCmsRepositoryDTO : apiCmsRepositoryDTOs) {
					assetRow = new String[5];
					assetRow[0] = apiCmsRepositoryDTO.getMerchantCode();
					assetRow[1] = apiCmsRepositoryDTO.getDtid();
					assetRow[2] = apiCmsRepositoryDTO.getSerialNumber();
					assetRow[3] = apiCmsRepositoryDTO.getStatus();
					assetRow[4] = apiCmsRepositoryDTO.getMemo();
					assetRowsList.add(org.apache.commons.lang3.StringUtils.join(assetRow, "\t"));
				}
				String tempPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
				String assetFname = tempPath+ File.separator + "CMS_TO_ATOMS_STATUS_NOTIFY.CSV";
				pathList.add(assetFname);
				for (String path : pathList) {
					FileUtils.removeFile(path);
				}
				String[] heads = {"MID	TID	SERIAL_NUMBER	STATUS	COMMENT"};
				CSVUtils.writeCsv(heads, assetRowsList, assetFname);
				String name = SystemConfigManager.getProperty("ATOMS_FTPS", "name");
				name = PasswordEncoderUtilities.decodePassword(name);
				String pwd = SystemConfigManager.getProperty("ATOMS_FTPS", "pwd");
				pwd = PasswordEncoderUtilities.decodePassword(pwd);
				String ip = SystemConfigManager.getProperty("ATOMS_FTPS", "ip");
				String port = SystemConfigManager.getProperty("ATOMS_FTPS", "port");
				String ftpsPath = SystemConfigManager.getProperty("ATOMS_FTPS", "ftpsPath");
						
				boolean uploaded = FtpClient.uploadFile(ip, Integer.parseInt(port), name, pwd, ftpsPath, pathList);
				if (!uploaded) {
					LOGGER.error(this.getClass().getName() + ".queryAssetInfo() uploaded iatoms case is failed...");
				} else {
					LOGGER.debug(this.getClass().getName() + ".queryAssetInfo() upload success ");
					//通知成功，註記已傳送，表示傳過後不要再傳
					this.changeAssetNotify(apiCmsRepositoryDTOs);
					LOGGER.debug(".queryAssetInfo() changeAssetNotify end...");
				}
				for (String path : pathList) {
					FileUtils.removeFile(path);
					LOGGER.debug(".queryAssetInfo() removeFile end...path=" + path);
				}
			} else {
				 //若無可傳之資料，就不要產生檔案
				LOGGER.debug(".queryAssetInfo() apiCmsRepositoryDTOs is empty...");
			}
		} catch (DataAccessException e) {
			LOGGER.error(".queryAssetInfo() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".queryAssetInfo() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		} finally {
			for (String path : pathList) {
				try {
					FileUtils.removeFile(path);
				} catch (Exception e) {
					LOGGER.error(this.getClass().getName() + ".queryAssetInfo() removeFile is error" + e);
					throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
				}
			}
		}
		
	}
	/**
	 * Purpose: 通知成功后註記已傳送
	 * @author amandawang
	 * @param apiCmsRepositoryDTOs :未傳送list
	 * @throws ServiceException ： 出錯時拋出ServiceException
	 */
	private void changeAssetNotify(List<ApiCmsRepositoryDTO> apiCmsRepositoryDTOs) throws ServiceException{
		// 批次
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) this.applicationContext.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
		TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
		try {
			LOGGER.debug(".changeAssetNotify() PropagationBehavior--> " + TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			Transformer transformer = new SimpleDtoDmoTransformer();
			for (ApiCmsRepositoryDTO apiCmsRepositoryDTO : apiCmsRepositoryDTOs) {
				ApiCmsRepository apiCmsRepository = (ApiCmsRepository) transformer.transform(apiCmsRepositoryDTO, new ApiCmsRepository());
				apiCmsRepository.setNotifyFlag("1");
				apiCmsRepository.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
				this.apiCmsRepositoryDAO.save(apiCmsRepository);
			}
			// 事務提交
			transactionManager.commit(status);
			LOGGER.debug(".changeAssetNotify() commit... ");
		} catch (DataAccessException e) {
			LOGGER.error(".changeAssetNotify() DataAccess Exception:" + e, e);
			LOGGER.error(".changeAssetNotify() batchMessage： Exception:" + e, e);
			transactionManager.rollback(status);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".changeAssetNotify() Service Exception--->" + e, e);
			LOGGER.error(".changeAssetNotify() batchMessage： Exception:" + e, e);
			transactionManager.rollback(status);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#exportBorrowDetail(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext exportBorrowDetail(SessionContext sessionContext)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			AssetManageFormDTO formDTO = (AssetManageFormDTO) sessionContext.getRequestParameter();
			List<DmmRepositoryDTO> repositoryDTOs = this.repositoryDAO.getBorrowDetail();
			formDTO.setList(repositoryDTOs);
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS, new String[]{this.getMyName()});
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.exportBorrowDetail(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.exportBorrowDetail(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IAssetManageService#checkBorrowSerialNumber(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public boolean checkBorrowSerialNumber(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		try {
			if (inquiryContext != null) {
				String assetTypeId = (String) inquiryContext.getParameter(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_ID.getValue());
				String serialNumber = (String) inquiryContext.getParameter(DmmRepositoryDTO.ATTRIBUTE.SERIAL_NUMBER.getValue());
				if (StringUtils.hasText(assetTypeId) && StringUtils.hasText(serialNumber)) {
					List<DmmRepositoryDTO> dmmRepositoryDTOs = this.repositoryDAO.checkBorrowSerialNumber(assetTypeId, serialNumber);
					if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
						return true;
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error("AssetManageService.exportBorrowDetail(SessionContext sessionContext) is error " + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error("AssetManageService.exportBorrowDetail(SessionContext sessionContext) Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return false;
	}
	
	/**
	 * @return the repositoryDAO
	 */
	public IDmmRepositoryDAO getRepositoryDAO() {
		return repositoryDAO;
	}

	/**
	 * @param repositoryDAO the repositoryDAO to set
	 */
	public void setRepositoryDAO(IDmmRepositoryDAO dmmRepositoryDAO) {
		this.repositoryDAO = dmmRepositoryDAO;
	}

	/**
	 * @return the dmmRepositoryHistDAO
	 */
	public IDmmRepositoryHistoryDAO getDmmRepositoryHistDAO() {
		return dmmRepositoryHistDAO;
	}

	/**
	 * @param dmmRepositoryHistDAO the dmmRepositoryHistDAO to set
	 */
	public void setDmmRepositoryHistDAO(IDmmRepositoryHistoryDAO dmmRepositoryHistDAO) {
		this.dmmRepositoryHistDAO = dmmRepositoryHistDAO;
	}
	/**
	 * @return the mailComponent
	 */
	public MailComponent getMailComponent() {
		return mailComponent;
	}

	/**
	 * @param mailComponent the mailComponent to set
	 */
	public void setMailComponent(MailComponent mailComponent) {
		this.mailComponent = mailComponent;
	}

	/**
	 * @return the admUserDA
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}

	/**
	 * @param admUserDA the admUserDA to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}

	/**
	 * @return the repositoryFaultComDAO
	 */
	public IDmmRepositoryFaultComDAO getRepositoryFaultComDAO() {
		return repositoryFaultComDAO;
	}

	/**
	 * @param repositoryFaultComDAO the repositoryFaultComDAO to set
	 */
	public void setRepositoryFaultComDAO(IDmmRepositoryFaultComDAO repositoryFaultComDAO) {
		this.repositoryFaultComDAO = repositoryFaultComDAO;
	}

	/**
	 * @return the repositoryFaultDescDAO
	 */
	public IDmmRepositoryFaultDescDAO getRepositoryFaultDescDAO() {
		return repositoryFaultDescDAO;
	}

	/**
	 * @param repositoryFaultDescDAO the repositoryFaultDescDAO to set
	 */
	public void setRepositoryFaultDescDAO(IDmmRepositoryFaultDescDAO repositoryFaultDescDAO) {
		this.repositoryFaultDescDAO = repositoryFaultDescDAO;
	}
	
	/**
	 * @return the admUserWarehouseDAO
	 */
	public IAdmUserWarehouseDAO getAdmUserWarehouseDAO() {
		return admUserWarehouseDAO;
	}

	/**
	 * @param admUserWarehouseDAO the admUserWarehouseDAO to set
	 */
	public void setAdmUserWarehouseDAO(IAdmUserWarehouseDAO admUserWarehouseDAO) {
		this.admUserWarehouseDAO = admUserWarehouseDAO;
	}

	/**
	 * @return the companyDAO
	 */
	public ICompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	/**
	 * @param companyDAO the companyDAO to set
	 */
	public void setCompanyDAO(ICompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	/**
	 * @return the baseParameterItemDefDAO
	 */
	public IBaseParameterItemDefDAO getBaseParameterItemDefDAO() {
		return baseParameterItemDefDAO;
	}

	/**
	 * @param baseParameterItemDefDAO the baseParameterItemDefDAO to set
	 */
	public void setBaseParameterItemDefDAO(
			IBaseParameterItemDefDAO baseParameterItemDefDAO) {
		this.baseParameterItemDefDAO = baseParameterItemDefDAO;
	}

	/**
	 * @return the srmCaseTransactionDAO
	 */
	public ISrmCaseTransactionDAO getSrmCaseTransactionDAO() {
		return srmCaseTransactionDAO;
	}

	/**
	 * @param srmCaseTransactionDAO the srmCaseTransactionDAO to set
	 */
	public void setSrmCaseTransactionDAO(
			ISrmCaseTransactionDAO srmCaseTransactionDAO) {
		this.srmCaseTransactionDAO = srmCaseTransactionDAO;
	}

	/**
	 * @return the srmCaseAssetLinkDAO
	 */
	public ISrmCaseAssetLinkDAO getSrmCaseAssetLinkDAO() {
		return srmCaseAssetLinkDAO;
	}

	/**
	 * @param srmCaseAssetLinkDAO the srmCaseAssetLinkDAO to set
	 */
	public void setSrmCaseAssetLinkDAO(ISrmCaseAssetLinkDAO srmCaseAssetLinkDAO) {
		this.srmCaseAssetLinkDAO = srmCaseAssetLinkDAO;
	}

	/**
	 * @return the srmCaseNewAssetLinkDAO
	 */
	public ISrmCaseNewAssetLinkDAO getSrmCaseNewAssetLinkDAO() {
		return srmCaseNewAssetLinkDAO;
	}

	/**
	 * @param srmCaseNewAssetLinkDAO the srmCaseNewAssetLinkDAO to set
	 */
	public void setSrmCaseNewAssetLinkDAO(
			ISrmCaseNewAssetLinkDAO srmCaseNewAssetLinkDAO) {
		this.srmCaseNewAssetLinkDAO = srmCaseNewAssetLinkDAO;
	}

	/**
	 * @return the srmCaseNewHandleInfoDAO
	 */
	public ISrmCaseNewHandleInfoDAO getSrmCaseNewHandleInfoDAO() {
		return srmCaseNewHandleInfoDAO;
	}

	/**
	 * @param srmCaseNewHandleInfoDAO the srmCaseNewHandleInfoDAO to set
	 */
	public void setSrmCaseNewHandleInfoDAO(
			ISrmCaseNewHandleInfoDAO srmCaseNewHandleInfoDAO) {
		this.srmCaseNewHandleInfoDAO = srmCaseNewHandleInfoDAO;
	}

	/**
	 * @return the departmentDAO
	 */
	public IDepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}

	/**
	 * @param departmentDAO the departmentDAO to set
	 */
	public void setDepartmentDAO(IDepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}

	/**
	 * @return the apiCmsRepositoryDAO
	 */
	public IApiCmsRepositoryDAO getApiCmsRepositoryDAO() {
		return apiCmsRepositoryDAO;
	}

	/**
	 * @param apiCmsRepositoryDAO the apiCmsRepositoryDAO to set
	 */
	public void setApiCmsRepositoryDAO(IApiCmsRepositoryDAO apiCmsRepositoryDAO) {
		this.apiCmsRepositoryDAO = apiCmsRepositoryDAO;
	}

	/**
	 * @return the merchantDAO
	 */
	public IMerchantDAO getMerchantDAO() {
		return merchantDAO;
	}

	/**
	 * @param merchantDAO the merchantDAO to set
	 */
	public void setMerchantDAO(IMerchantDAO merchantDAO) {
		this.merchantDAO = merchantDAO;
	}
	
	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param applicationContext the applicationContext to set
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}