package com.cybersoft4u.xian.iatoms.dataTransfer.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BaseParameterItemDefDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimDepartmentDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryHistoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseAttFileDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseNewTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.WarehouseDTO;
import com.cybersoft4u.xian.iatoms.common.utils.IAtomsDateTimeUtils;
import com.cybersoft4u.xian.iatoms.common.web.taglib.iAtomsConstantsTag;
import com.cybersoft4u.xian.iatoms.dataTransfer.dao.IOldDataTransferDAO;
import com.cybersoft4u.xian.iatoms.dataTransfer.dto.TransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.dataTransfer.formDTO.OldDataTransferFormDTO;
import com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IApplicationAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IBaseParameterManagerDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarYearDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractVendorDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDepartmentDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryFaultComDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryFaultDescDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryCommDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDescDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseAttFileDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseCommModeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetFunctionDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewCommModeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseNewTransactionParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISuppliesTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDef;
import com.cybersoft4u.xian.iatoms.services.dmo.BaseParameterItemDefId;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarDay;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarYear;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompany;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompanyType;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCompanyTypeId;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContract;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractVendor;
import com.cybersoft4u.xian.iatoms.services.dmo.BimContractVendorId;
import com.cybersoft4u.xian.iatoms.services.dmo.BimDepartment;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchant;
import com.cybersoft4u.xian.iatoms.services.dmo.BimMerchantHeader;
import com.cybersoft4u.xian.iatoms.services.dmo.BimWarehouse;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedComm;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedCommId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedFunction;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetSupportedFunctionId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmAssetType;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepository;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultCom;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultComId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultDesc;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryFaultDescId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistory;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistoryComm;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistoryCommId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistoryDesc;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmRepositoryHistoryDescId;
import com.cybersoft4u.xian.iatoms.services.dmo.DmmSupplies;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplication;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplicationAssetLink;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplicationAssetLinkId;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseAttFile;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseCommMode;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseHandleInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewAssetFunction;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewAssetLink;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewCommMode;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewHandleInfo;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseNewTransactionParameter;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTransaction;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTransactionParameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Purpose: 舊資料轉檔業務邏輯實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public class OldDataTransferService extends AtomicService implements IOldDataTransferService,ApplicationContextAware{

	/**
	 * 注入舊資料轉檔DAO
	 */
	private IOldDataTransferDAO oldDataTransferDAO;
	/**
	 * 注入行事歷年份DAO
	 */
	private ICalendarYearDAO calendarYearDAO;
	/**
	 * 注入行事歷日檔DAO
	 */
	private ICalendarDayDAO calendarDayDAO;
	/**
	 * 参数维护DAO注入
	 */
	private IBaseParameterManagerDAO baseParameterManagerDAO;
	/**
	 * 公司DAO注入
	 */
	private ICompanyDAO companyDAO;
	/**
	 * 公司類型DAO注入
	 */
	private ICompanyTypeDAO companyTypeDAO;
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * 部門維護DAO
	 */
	private IDepartmentDAO departmentDAO;
	/**
	 * 倉庫據點DAO
	 */
	private IWarehouseDAO warehouseDAO;
	/**
	 * 特店資料DAO
	 */
	private IMerchantDAO merchantDAO;
	/**
	 * 特店表頭DAO
	 */
	private IMerchantHeaderDAO merchantHeaderDAO;
	/**
	 * 程式版本DAO
	 */
	private IPvmApplicationDAO applicationDAO;
	/**
	 * 程式版本設備鏈接DAO
	 */
	private IApplicationAssetLinkDAO applicationAssetLinkDAO;
	/**
	 * 合約維護DAO
	 */
	private IContractDAO contractDAO;
	/**
	 * 合約維護廠商DAO
	 */
	private IContractVendorDAO contractVendorDAO;
	/**
	 * 設備品項DAO
	 */
	private IAssetTypeDAO assetTypeDAO;
	/**
	 * 設備庫存DAO
	 */
	private IDmmRepositoryDAO repositoryDAO;
	/**
	 * 設備庫存歷史DAO
	 */
	private IDmmRepositoryHistoryDAO dmmRepositoryHistDAO;
	/**
	 * 設備庫存故障組件DAO
	 */
	private IDmmRepositoryFaultComDAO repositoryFaultComDAO;
	/**
	 * 設備庫存故障現象DAO
	 */
	private IDmmRepositoryFaultDescDAO repositoryFaultDescDAO;
	
	/**
	 * 設備庫存歷史故障現象DAO
	 */
	private IDmmRepositoryHistoryDescDAO dmmRepositoryHistoryDescDAO;
	/**
	 * 設備庫存歷史故障現象DAO
	 */
	private IDmmRepositoryHistoryCommDAO dmmRepositoryHistoryCommDAO;
	/**
	 * SRM_案件處理資料檔DAO接口
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	/**
	 * SRM_案件附加資料檔DAO接口
	 */
	private ISrmCaseAttFileDAO srmCaseAttFileDAO;
	/**
	 * 案件處理記錄DAO
	 */
	private ISrmCaseTransactionDAO srmCaseTransactionDAO;
	/**
	 * 案件交易參數 DAO
	 */
	private ISrmCaseTransactionParameterDAO srmCaseTransactionParameterDAO;
	/**
	 * 使用者帳號管理 DAO
	 */
	private IAdmUserDAO admUserDAO;
	/**
	 * SRM_案件處理最新資料檔DAO
	 */
	private ISrmCaseNewHandleInfoDAO srmCaseNewHandleInfoDAO;
	/**
	 * SRM_案件最新交易參數資料檔DAO
	 */
	private ISrmCaseNewTransactionParameterDAO srmCaseNewTransactionParameterDAO;
	/**
	 * SRM_案件最新設備連接檔數據訪問DAO
	 */
	private ISrmCaseNewAssetLinkDAO srmCaseNewAssetLinkDAO;
	/**
	 * SRM_案件最新設備支援功能數據訪問接口
	 */
	private ISrmCaseNewAssetFunctionDAO srmCaseNewAssetFunctionDAO;
	/**
	 * location 市縣區域集合
	 */
	private List<Parameter> locationList;
	/**
	 * 公司代號集合
	 */
	private List<Parameter> companyCodeList;
	/**
	 * 公司簡稱集合
	 */
	private List<Parameter> companyNameList;
	/**
	 * 設筆品項集合
	 */
	private List<Parameter> assetTypeList;
	/**
	 * edc設筆品項集合
	 */
	private List<Parameter> edcTypeList;
	/**
	 * 支援功能集合
	 */
	private List<Parameter> supportedFunctionList;
	/**
	 * 通訊方式集合
	 */
	private List<Parameter> supportedCommList;
	/**
	 * 程序版本集合
	 */
	private List<Parameter> applicationVersionList;
	/**
	 * 倉庫集合
	 */
	private List<Parameter> warehouseList;
	/**
	 * 合約集合
	 */
	private volatile List<Parameter> contractList;
	/**
	 * 裝機類型集合
	 */
	private List<Parameter> installTypeList;
	/**
	 * 故障說明集合
	 */
	private List<Parameter> faultDescriptionList;
	/**
	 * 故障組件集合
	 */
	private List<Parameter> faultComponentList;
	/**
	 * 執行作業集合
	 */
	private List<Parameter> actionList;
	/**
	 * 設備狀態集合
	 */
	private List<Parameter> assetStatusList;
	/**
	 * 維護模式集合
	 */
	private List<Parameter> maTypeList;
	/**
	 * 報廢原因集合
	 */
	private List<Parameter> retireReasonList;
	/**
	 * 周邊設備集合
	 */
	private List<Parameter> peripheralsList;
	/**
	 * 程式版本集合
	 */
	private List<Parameter> applicationList;
	/**
	 * 帳號集合
	 */
	private volatile List<Parameter> userList;
	/**
	 * 判斷錯誤標誌位
	 */
	public volatile String errorFlag;
	/**
	 * 程式版本集合
	 */
	private Map<String, List<ApplicationDTO>> applicationMap;
	/**
	 * 支援功能預設值
	 * key：設備主鍵
	 * value:支持的支援功能
	 */
	private Map<String, String> assetSupportedFunctionMap;
	/**
	 * 通訊模式預設值
	 * key：設備主鍵
	 * value:支持的通訊方式
	 */
	private Map<String, String> assetSupportedCommMap;
	/**
	 * foms設備與aims關系
	 * key：foms設備名稱
	 * value:aims設備名稱
	 */
	private Map<String, String> fomsAssetTypeMap;
	/**
	 * 程式版本維護設備鏈接DTO
	 */
	private List<ApplicationAssetLinkDTO> applicationAssetLinkList;
	/**
	 * 部門預設值
	 * key：公司id
	 * value:部門list
	 */
	private Map<String, List<Parameter>> departmentMap;
	/**
	 * 特店預設值
	 * key：公司id
	 * value:特店list
	 */
	private volatile Map<String, List<Parameter>> merchantMap;
	/**
	 * 特店表頭預設值
	 * key：特店id
	 * value:特點表頭list
	 */
	private volatile Map<String, List<Parameter>> merchantHeaderMap;
	/**
	 * 合約信息map集合
	 * key：公司信息
	 * value:合約表頭list
	 */
	private Map<String, List<Parameter>> contractMap;
	/**
	 * 轉入資料結果信息
	 */
	private StringBuilder resultMsg;
	/**
	 * activiti service
	 */
	private RepositoryService repositoryService;
	
	/**
	 * 需要添加兩筆公司類型的公司統一編號
	 */
	private static final String COMPANY_ID_FOR_COMPANY_TYPE														= "70543978";
	/**
	 * 需要添加部門的公司統一編號
	 */
	private static final String COMPANY_ID_FOR_ADD_DEPT															= "12786785";
	/**
	 * 需要添加部門的公司統一編號
	 */
	public static StringBuilder PARAM_LOG_MESSAGE																= new StringBuilder();
	
	/**
	 * tms程式版本ok標記
	 */
	private boolean isOkTmsApp;
	/**
	 * 耗材分類集合
	 */
	private List<Parameter> suppliesTypeList;
	/**
	 * 耗材品DAO
	 */
	private ISuppliesTypeDAO suppliesDAO;
	
	
	private ApplicationContext applicationContext;
	
	/**
	 * 部門集合
	 */
	private List<Parameter> departmentList;
	
	/**
	 * SRM_案件處理中通訊模式維護檔 DAO
	 */
	private ISrmCaseCommModeDAO srmCaseCommModeDAO;
	/**
	 * SRM_案件最新通訊模式維護檔 DAO
	 */
	private ISrmCaseNewCommModeDAO srmCaseNewCommModeDAO;
	/**
	 * 需要添加部門的公司統一編號:12115702
	 */
	private static final String CEPT_BY_UNITY_NUMBER												= "12115702";
	/**
	 * edc設筆品項集合
	 */
	private List<Parameter> edcCategoryList;
	/**
	 * 周邊設備設筆品項集合
	 */
	private List<Parameter> RelatedProductsCategoryList;
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, OldDataTransferService.class);
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#setup(cafe.core.context.SessionContext)
	 */
	public SessionContext setup(SessionContext sessionContext) throws ServiceException {
		try {
			InputStream fileInputStream = OldDataTransferService.class.getResourceAsStream("/IATOMS_BP_CASE_PROCESS.bpmn");
			//File file = new File("C:\\temp\\IATOMS_BP_CASE_PROCESS.bpmn");
			//InputStream fileInputStream = new FileInputStream(file);
			DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
			deploymentBuilder.addInputStream("IATOMS_BP_CASE_PROCESS.bpmn", fileInputStream);
			deploymentBuilder.deploy();
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".setup(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}		
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#testSpeed()
	 */
	@Override
	public void testSpeed() throws ServiceException {
		try {
			long startTime = System.currentTimeMillis();
			this.oldDataTransferDAO.testSpeed();
			long endTime = System.currentTimeMillis();
			LOGGER.debug("testSpeed", "testSpeedTime Service totalTime:" + (endTime - startTime));
		} catch (Exception e) {
			LOGGER.error(".testSpeed(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		try {
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			
			oldDataTransferFormDTO.setLogMessage(this.PARAM_LOG_MESSAGE.toString());
			
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(oldDataTransferFormDTO);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferCalendar(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferCalendar(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 舊資料轉移
			saveCalendarData();
			
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferCalendar"));
			
			LOGGER.error(".transferCalendar() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferCalendar"));
			
			LOGGER.error(".transferCalendar() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}

	/**
	 * Purpose:保存行事曆資料
	 * @author CrissZhang
	 * @return void
	 */
	public void saveCalendarData()throws Exception {
		try {
			Transformer transformer = new SimpleDtoDmoTransformer();
			// 刪除行事歷資料
			this.calendarDayDAO.deleteTransferData();
			
			// 得到行事歷年份集合
			List<BimCalendarYearDTO> calendarYearDTOs = this.oldDataTransferDAO.listCalendarYear();
			if(!CollectionUtils.isEmpty(calendarYearDTOs)){
				// 行事歷DMO對象
				BimCalendarYear calendarYear = null;
				// 遍歷保存
				for(BimCalendarYearDTO calendarYearDTO : calendarYearDTOs){
					calendarYear = (BimCalendarYear) transformer.transform(calendarYearDTO, new BimCalendarYear());
					// 保存行事歷年檔
					this.calendarYearDAO.insert(calendarYear);
				}
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "行事歷年檔資料轉入成功").append("</br>");
				}
			} else {
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無行事歷年檔資料轉入").append("</br>");
				}
			}
			// 得到行事歷年份集合
			List<BimCalendarDayDTO> calendarDayDTOs = this.oldDataTransferDAO.listCalendarDate();
			if(!CollectionUtils.isEmpty(calendarDayDTOs)){
				// 行事歷DMO對象
				BimCalendarDay calendarDay = null;
				// 遍歷保存
				for(BimCalendarDayDTO calendarDayDTO : calendarDayDTOs){
					calendarDay = (BimCalendarDay) transformer.transform(calendarDayDTO, new BimCalendarDay());
					// 保存行事歷日檔
					this.calendarDayDAO.insert(calendarDay);
				}
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "行事歷日檔資料轉入成功").append("</br>");
				}
			} else {
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無行事歷日檔資料轉入").append("</br>");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(".transferCalendar() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferCalendar() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferFaultParamData(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferFaultParamData(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			StringBuilder bptdCodeBuilder = new StringBuilder();
			bptdCodeBuilder.append(IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append(IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
			// 刪除基本參數信息
			this.baseParameterManagerDAO.deleteTransferData(bptdCodeBuilder.toString());
			
			// 儲存基本參數對象
			BaseParameterItemDef baseParameterItemDef = null;
			// 儲存基本參數對象Id
			BaseParameterItemDefId baseParameterItemDefId = null;
			// 固定的生效日期
			Date effectiveDate = DateTimeUtils.parseDate(IAtomsConstants.STATIC_VERSION_DATE_FOR_BASE_PARAMETER, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			
			// 故障組件數據
			List<BaseParameterItemDefDTO> faultComponentDTOs = this.oldDataTransferDAO.listFaultComponentData();
			if(!CollectionUtils.isEmpty(faultComponentDTOs)){
			//	bpidId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF);
				int i = 0;
				// 故障組件主鍵
				String faultComponentId = null;
				// 故障組件code
				String faultComponentCode = IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode();
				for(BaseParameterItemDefDTO baseParameterItemDefDTO : faultComponentDTOs){
					i ++;
					// id拼接
				//	faultComponentId = bpidId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i);
					if(i > 9){
						faultComponentId = faultComponentCode + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i);
					} else {
						faultComponentId = faultComponentCode + IAtomsConstants.MARK_UNDER_LINE + String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i);
					}
					baseParameterItemDefId = new BaseParameterItemDefId(faultComponentId, faultComponentCode, effectiveDate);
					baseParameterItemDef = new BaseParameterItemDef();
					// 主鍵
					baseParameterItemDef.setId(baseParameterItemDefId);
					// 參數項目
					baseParameterItemDef.setItemName(baseParameterItemDefDTO.getItemName());
					// 參數項目值
					if(i > 9){
						baseParameterItemDef.setItemValue(String.valueOf(i));
					} else {
						// 2位數字，不足補0
						baseParameterItemDef.setItemValue(String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i));
					}
					// 參數項目順序
					baseParameterItemDef.setItemOrder(i);
					// 是否已核可
			//		baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
					// 新增日期
			//		baseParameterItemDef.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					// 異動日期
					baseParameterItemDef.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					// 刪除標記
					baseParameterItemDef.setDeleted(IAtomsConstants.NO);
					this.baseParameterManagerDAO.insert(baseParameterItemDef);
				}
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "故障組件資料轉入成功").append("</br>");
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無故障組件資料轉入").append("</br>");
			}
			
			// 故障現象數據
			List<BaseParameterItemDefDTO> faultDescriptionDTOs = this.oldDataTransferDAO.listFaultDescriptionData();
			if(!CollectionUtils.isEmpty(faultDescriptionDTOs)){
		//		bpidId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BASE_PARAMETER_ITEM_DEF);
				int i = 0;
				// 故障現象主鍵
				String faultDescriptionId = null;
				// 故障現象code
				String faultDescriptionCode = IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode();
				for(BaseParameterItemDefDTO baseParameterItemDefDTO : faultDescriptionDTOs){
					i ++;
					// id拼接
				//	faultDescriptionId = bpidId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i);
					if(i > 9){
						faultDescriptionId = faultDescriptionCode + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i);
					} else {
						faultDescriptionId = faultDescriptionCode + IAtomsConstants.MARK_UNDER_LINE + String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i);
					}
					baseParameterItemDefId = new BaseParameterItemDefId(faultDescriptionId, faultDescriptionCode, effectiveDate);
					baseParameterItemDef = new BaseParameterItemDef();
					// 主鍵
					baseParameterItemDef.setId(baseParameterItemDefId);
					// 參數項目
					baseParameterItemDef.setItemName(baseParameterItemDefDTO.getItemName());
					// 參數項目值
					if(i > 9){
						baseParameterItemDef.setItemValue(String.valueOf(i));
					} else {
						// 2位數字，不足補0
						baseParameterItemDef.setItemValue(String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i));
					}
					// 參數項目順序
					baseParameterItemDef.setItemOrder(i);
					// 是否已核可
		//			baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
					// 新增日期
			//		baseParameterItemDef.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
					// 異動日期
					baseParameterItemDef.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					// 刪除標記
					baseParameterItemDef.setDeleted(IAtomsConstants.NO);
					this.baseParameterManagerDAO.insert(baseParameterItemDef);
				}
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "故障現象資料轉入成功").append("</br>");
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無故障現象資料轉入").append("</br>");
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferFaultParamData"));
			
			LOGGER.error(".transferFaultParamData() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferFaultParamData"));
			
			LOGGER.error(".transferFaultParamData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferCompanyData(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferCompanyData(SessionContext sessionContext)throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除公司信息
			this.companyDAO.deleteTransferData();
			
			Transformer transformer = new SimpleDtoDmoTransformer();
			List<CompanyDTO> companyDTOs = this.oldDataTransferDAO.listCompanyData();
			if(!CollectionUtils.isEmpty(companyDTOs)){
				BimCompany company = null;
				BimCompanyType companyType = null;
				BimCompanyTypeId companyTypeId = null;
				BimDepartment department = null;
				Parameter parameter = null;
				String deptId = null;
				String companyId = null;
				// 公司代號
				this.companyCodeList = new ArrayList<Parameter>();
				// 公司簡稱
				this.companyNameList = new ArrayList<Parameter>();
				companyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_COMPANY);
				int i = 0;
//				this.companyCodeList.add(new Parameter("CYB", "10000000-01"));
//				this.companyNameList.add(new Parameter("Cybersoft", "10000000-01"));
				// 耗材分類集合
				if(CollectionUtils.isEmpty(this.suppliesTypeList)){
					this.suppliesTypeList =  (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.SUPPLIES_TYPE.getCode(), null);
				}
				DmmSupplies dmmSupplies = null;
				String suppliesId = null;
				for(CompanyDTO companyDTO : companyDTOs){
					if("CYB".equals(companyDTO.getCompanyCode())){
						this.companyCodeList.add(new Parameter("CYB", "10000000-01"));
						this.companyNameList.add(new Parameter("Cybersoft", "10000000-01"));
						// Task #2666 
						continue;
					}
					i++;
					company = (BimCompany) transformer.transform(companyDTO, new BimCompany());
					company.setCompanyId(companyId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
					// 公司地址 市縣區域
					company.setAddressLocation(this.getValueByName(company.getAddressLocation(), IATOMS_PARAM_TYPE.LOCATION.getCode()));
					// 發票地址 市縣區域
					company.setInvoiceAddressLocation(this.getValueByName(company.getInvoiceAddressLocation(), IATOMS_PARAM_TYPE.LOCATION.getCode()));
					// 公司email
					if(StringUtils.hasText(company.getCompanyEmail())){
						company.setCompanyEmail(company.getCompanyEmail().replace(IAtomsConstants.MARK_SEPARATOR, IAtomsConstants.MARK_SEMICOLON));
					}
					if("TCB_EDC".equals(companyDTO.getCompanyCode())){
						company.setIsNotifyAo(IAtomsConstants.YES);
					} else {
						company.setIsNotifyAo(IAtomsConstants.NO);
					}
					// 保存公司信息
					this.companyDAO.insert(company);
					
					// 增加公司代碼信息列表
					parameter = new Parameter(company.getCompanyCode(), company.getCompanyId());
					this.companyCodeList.add(parameter);
					// 增加公司簡稱信息列表
					parameter = new Parameter(company.getShortName(), company.getCompanyId());
					this.companyNameList.add(parameter);
					
					//保存公司類型信息
					if(StringUtils.hasText(companyDTO.getCompanyType())){
						companyType = new BimCompanyType();
						companyTypeId = new BimCompanyTypeId(company.getCompanyId(), companyDTO.getCompanyType());
						companyType.setId(companyTypeId);
						this.companyTypeDAO.insert(companyType);
					}
					
					/*
					 * Task #2344
					 * 每個客戶(取得【公司基本訊息維護】資料且【公司類型】為“客戶” 
					 * 且【登入驗證方式】為“iAtoms驗證”之【公司簡稱】)
					 * 依據耗材分類，都有一個同耗材分類的耗材名稱，金額為0
					 * 列為初始資料
					 */
					if ("CUSTOMER".equals(companyDTO.getCompanyType()) && "IATOMS_AUTHEN".equals(companyDTO.getAuthenticationType())) {
						LOGGER.debug("new dmmSupplies for company -->" + company.getCompanyId());
						suppliesId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_SUPPLIES);
						BigDecimal zreo = new BigDecimal(0);
						int j = 1;
						for (Parameter paramter : suppliesTypeList) {
							dmmSupplies = new DmmSupplies();
							dmmSupplies.setSuppliesId(suppliesId + "-" + j);
							j++;
							dmmSupplies.setSuppliesType(paramter.getValue().toString());
							dmmSupplies.setPrice(zreo);
							dmmSupplies.setCompanyId(company.getCompanyId());
							dmmSupplies.setSuppliesName(paramter.getName());
							this.suppliesDAO.insert(dmmSupplies);
						}
					}
					
					// 若資料CORPORATION_ID=70543978，多寫入二筆，公司類型= MAINTENANCE_VENDOR、HARDWARE_VENDOR
					if(this.COMPANY_ID_FOR_COMPANY_TYPE.equals(company.getUnityNumber())){
						if(!"CYB".equals(companyDTO.getCompanyCode())){
							// 寫入公司類型= MAINTENANCE_VENDOR
							if(!IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR.equals(companyDTO.getCompanyType())){
								companyType = new BimCompanyType();
								companyTypeId = new BimCompanyTypeId(company.getCompanyId(), IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
								companyType.setId(companyTypeId);
								this.companyTypeDAO.insert(companyType);
							}
							// 寫入公司類型= HARDWARE_VENDOR
							if(!IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR.equals(companyDTO.getCompanyType())){
								companyType = new BimCompanyType();
								companyTypeId = new BimCompanyTypeId(company.getCompanyId(), IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
								companyType.setId(companyTypeId);
								this.companyTypeDAO.insert(companyType);
							}
						}
						// 部門資料設定檔 若CORPORATION_ID=70543978，寫入一筆
						// CORPORATION_ID=70543978 EDC管理部
						department = new BimDepartment();
						deptId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_DEPARTMENT);
						department.setDeptCode(deptId);
						department.setCompanyId(company.getCompanyId());
						department.setDeptName("EDC管理部");
						department.setDeleted(IAtomsConstants.NO);
						department.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						department.setUpdatedDate(department.getCreatedDate());
						this.departmentDAO.insert(department);
					} else if(this.COMPANY_ID_FOR_ADD_DEPT.equals(company.getUnityNumber())){
						// 部門資料設定檔 若CORPORATION_ID= 12786785，寫入三筆
						// CORPORATION_ID= 12786785維護四組、維護五組、維護八組
						department = new BimDepartment();
						deptId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_DEPARTMENT);
						department.setDeptCode(deptId);
						department.setCompanyId(company.getCompanyId());
						department.setDeptName("維護四組");
						department.setDeleted(IAtomsConstants.NO);
						department.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						department.setUpdatedDate(department.getCreatedDate());
						this.departmentDAO.insert(department);
						
						department = new BimDepartment();
						deptId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_DEPARTMENT);
						department.setDeptCode(deptId);
						department.setCompanyId(company.getCompanyId());
						department.setDeptName("維護五組");
						department.setDeleted(IAtomsConstants.YES);
						department.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						department.setUpdatedDate(department.getCreatedDate());
						this.departmentDAO.insert(department);
						
						department = new BimDepartment();
						deptId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_DEPARTMENT);
						department.setDeptCode(deptId);
						department.setCompanyId(company.getCompanyId());
						department.setDeptName("維護八組");
						department.setDeleted(IAtomsConstants.YES);
						department.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						department.setUpdatedDate(department.getCreatedDate());
						this.departmentDAO.insert(department);
					} else if(this.CEPT_BY_UNITY_NUMBER.equals(company.getUnityNumber())){
						//Task #2756 2017/11/03
						// 部門資料設定檔 若CORPORATION_ID= 12115702新增部門 --> 維護六組
						department = new BimDepartment();
						deptId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_DEPARTMENT);
						department.setDeptCode(deptId);
						department.setCompanyId(company.getCompanyId());
						department.setDeptName("維護六組");
						department.setDeleted(IAtomsConstants.NO);
						department.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						department.setUpdatedDate(department.getCreatedDate());
						this.departmentDAO.insert(department);
					}
				}
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "公司基本資料轉入成功").append("</br>");
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無公司基本資料轉入").append("</br>");
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferCompanyData"));
			
			LOGGER.error(".transferCompanyData() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferCompanyData"));
			
			LOGGER.error(".transferCompanyData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferWarehouse(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferWarehouse(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			Map map = new HashMap();
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除倉庫據點信息
			this.warehouseDAO.deleteTransferData();
			
			// 判斷DB倉管資料是否爲空
//			boolean isNoData = this.warehouseDAO.isNoData();
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
			}
			// 無公司信息
			if(!CollectionUtils.isEmpty(this.companyCodeList)){
				Transformer transformer = new SimpleDtoDmoTransformer();
				// 倉庫據點信息集合
				List<WarehouseDTO> warehouseDTOs = this.oldDataTransferDAO.listWarehouseData();
				if(!CollectionUtils.isEmpty(warehouseDTOs)){
					// 倉庫主鍵
					String wareHouseId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_WAREHOUSE);
					BimWarehouse warehouse = null;
					int i = 0;
					for(WarehouseDTO warehouseDTO : warehouseDTOs){
						i ++;
						warehouse = (BimWarehouse) transformer.transform(warehouseDTO, new BimWarehouse());
						warehouse.setWarehouseId(wareHouseId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						// 聯絡人
						if(!StringUtils.hasText(warehouse.getContact())){
							warehouse.setContact(IAtomsConstants.MARK_SPACE);
						}
						// 電話
						if(!StringUtils.hasText(warehouse.getTel())){
							warehouse.setTel(IAtomsConstants.MARK_SPACE);
						}
						// 轉化公司信息
						warehouse.setCompanyId(this.getValueByName(warehouse.getCompanyId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()));
						// 轉化地區信息
						warehouse.setLocation(this.getValueByName(warehouse.getLocation(), IATOMS_PARAM_TYPE.LOCATION.getCode()));
						this.warehouseDAO.insert(warehouse);
					}
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "倉庫據點資料轉入成功").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無倉庫據點資料轉入").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "倉庫據點資料轉入失敗").append("</br>");
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferWarehouse"));
			
			LOGGER.error(".transferWarehouse() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferWarehouse"));
			
			LOGGER.error(".transferWarehouse() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferMerchant(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferMerchant(SessionContext sessionContext)throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			Map map = new HashMap();
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除特店資料
			this.merchantDAO.deleteTransferData();
			
			// 判斷DB特店資料是否爲空
//			boolean isNoData = this.merchantDAO.isNoData();
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
			}
			// 無公司信息
			if(!CollectionUtils.isEmpty(this.companyCodeList)){
				Transformer transformer = new SimpleDtoDmoTransformer();
				// 特店信息集合
				List<MerchantDTO> merchantDTOs = this.oldDataTransferDAO.listMerchantData();
				if(!CollectionUtils.isEmpty(merchantDTOs)){
					// 所有的表頭信息
					List<BimMerchantHeaderDTO> tempAllHeaderDTOs = this.oldDataTransferDAO.listMerchantHanderData();
					BimMerchant merchant = null;
					BimMerchantHeader merchantHeader = null;
					for(MerchantDTO merchantDTO : merchantDTOs){
						merchant = (BimMerchant) transformer.transform(merchantDTO, new BimMerchant());
						// 轉換公司信息
						merchant.setCompanyId(this.getValueByName(merchant.getCompanyId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()));
						// 保存特店信息
					//	this.merchantDAO.insert(merchant);
						this.merchantDAO.getDaoSupport().save(merchant);
					}
					// 有無特店信息
					if(!CollectionUtils.isEmpty(tempAllHeaderDTOs)){
						for(BimMerchantHeaderDTO merchantHeaderDTO : tempAllHeaderDTOs){
							merchantHeader = (BimMerchantHeader) transformer.transform(merchantHeaderDTO, new BimMerchantHeader());
							// 營業地址-縣市
							merchantHeader.setLocation(this.getValueByName(merchantHeader.getLocation(),IATOMS_PARAM_TYPE.LOCATION.getCode()));
							// 保存特店表頭信息
						//	this.merchantHeaderDAO.insert(merchantHeader);
							this.merchantDAO.getDaoSupport().save(merchantHeader);
						}
					}
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "客戶特店、表頭資料轉入成功").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無客戶特店資料轉入").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "客戶特店、表頭資料轉入失敗").append("</br>");
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferMerchant"));
			
			LOGGER.error(".transferMerchant() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferMerchant"));
			
			LOGGER.error(".transferMerchant() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferApplicaton(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferApplicaton(SessionContext sessionContext)throws ServiceException {
		try {
			this.resultMsg = new StringBuilder();
			Map map = new HashMap();
			StringBuilder tempBuilder = new StringBuilder();
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除程式版本資料
			this.applicationDAO.deleteTransferData();
			
			boolean flag = false;
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
			}
			// 若無設備信息則去DB查詢
			if(CollectionUtils.isEmpty(this.assetTypeList)){
				this.assetTypeList = this.assetTypeDAO.listAssetByName(null, false, false);
			}
			// 無公司信息
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				tempBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
				flag = true;
			}
			// 無設備信息
			if(CollectionUtils.isEmpty(this.assetTypeList)){
				tempBuilder.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
				flag = true;
			}
			if(!flag){
			//	this.applicationList = new ArrayList<Parameter>();
				this.applicationMap = new HashMap<String, List<ApplicationDTO>>();
				this.isOkTmsApp = true;
				// tms
				// 舊資料轉移
				transferApplicatonTms();
				if(this.isOkTmsApp){
					// foms
					this.transferApplicatonFoms(sessionContext);
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "程式版本資料轉入失敗").append("</br>");
					this.applicationMap = new HashMap<String, List<ApplicationDTO>>();
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "程式版本資料轉入失敗").append("</br>");
				this.resultMsg.append(tempBuilder.toString());
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferApplicaton"));
			
			LOGGER.error(".transferApplicaton() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferApplicaton"));
			
			LOGGER.error(".transferApplicaton() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:轉移foms程式版本數據
	 * @author CrissZhang
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public SessionContext transferApplicatonFoms(SessionContext sessionContext)throws ServiceException {
		try {
			// 判斷DB程式版本資料是否爲空
		//	boolean isNoData = this.applicationDAO.isNoData();
			Transformer transformer = new SimpleDtoDmoTransformer();
			// 程式版本集合
			List<ApplicationDTO> applicationAssetDTOs = this.oldDataTransferDAO.listApplicationData();
			if(!CollectionUtils.isEmpty(applicationAssetDTOs)){
				Map<String, Map<String, List<String>>> tempAppMap = new HashMap<String, Map<String,List<String>>>();
				List<String> tempString = null;
				Map<String, List<String>> tempMap = null;
				for(ApplicationDTO tempApplicationDTO : applicationAssetDTOs){
					if(tempAppMap.containsKey(tempApplicationDTO.getCustomerId())){
						tempMap = tempAppMap.get(tempApplicationDTO.getCustomerId());
						if(CollectionUtils.isEmpty(tempMap)){
							tempString = new ArrayList<String>();
							tempString.add(tempApplicationDTO.getAssetTypeId());
							tempMap = new HashMap<String, List<String>>();
						} else {
							tempString = tempMap.get(tempApplicationDTO.getVersion());
							if(CollectionUtils.isEmpty(tempString)){
								tempString = new ArrayList<String>();
							}
							tempString.add(tempApplicationDTO.getAssetTypeId());
						}
						tempMap.put(tempApplicationDTO.getVersion(), tempString);
					} else {
						tempString = new ArrayList<String>();
						tempString.add(tempApplicationDTO.getAssetTypeId());
						tempMap = new HashMap<String, List<String>>();
						tempMap.put(tempApplicationDTO.getVersion(), tempString);
					}
					tempAppMap.put(tempApplicationDTO.getCustomerId(), tempMap);
				}
				List<ApplicationDTO> applicationDTOs = new ArrayList<ApplicationDTO>();
				ApplicationDTO tempApplicationDTO = null;
				if(!CollectionUtils.isEmpty(tempAppMap)){
					for(String customerId : tempAppMap.keySet()){
						if(!CollectionUtils.isEmpty(tempAppMap.get(customerId))){
							for(String version : tempAppMap.get(customerId).keySet()){
								tempApplicationDTO = new ApplicationDTO(customerId, version);
								applicationDTOs.add(tempApplicationDTO);
							}
						}
					}
				}
				if(!CollectionUtils.isEmpty(applicationDTOs)){
					PvmApplication pvmApplication = null;
					PvmApplicationAssetLink applicationAssetLink = null;
					PvmApplicationAssetLinkId applicationAssetLinkId = null;
					String applicationId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_PVM_APPLICATION);
					int i = 0;
					List<String> assetList = null;
					List<ApplicationDTO> tempApplicationList = null;
					for(ApplicationDTO applicationDTO : applicationDTOs){
						i ++;
						pvmApplication = (PvmApplication) transformer.transform(applicationDTO, new PvmApplication());
						// 客戶
						pvmApplication.setCustomerId(this.getValueByName(pvmApplication.getCustomerId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()));
						// 主鍵
						pvmApplication.setApplicationId(applicationId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						// 設備類別
						pvmApplication.setAssetCategory(IAtomsConstants.ASSET_CATEGORY_EDC);
						// 程式名稱
						pvmApplication.setName(IAtomsConstants.MARK_SPACE);
						pvmApplication.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
						pvmApplication.setUpdatedDate(pvmApplication.getCreatedDate());
						// 刪除標記
						pvmApplication.setDeleted(IAtomsConstants.YES);
						
						this.applicationDAO.insert(pvmApplication);
						
						assetList = tempAppMap.get(applicationDTO.getCustomerId()).get(applicationDTO.getVersion());
						if(!CollectionUtils.isEmpty(assetList)){
							Map<String, String> tempRepeatMap = new HashMap<String, String>();
							for(String assetId : assetList){
								applicationAssetLink = new PvmApplicationAssetLink();
								if(this.fomsAssetTypeMap.containsKey(assetId)){
									assetId = this.fomsAssetTypeMap.get(assetId);
									assetId = this.getValueByName(assetId, "assetType");
									if(StringUtils.hasText(assetId)){
										if(!tempRepeatMap.containsKey(assetId)){
											tempRepeatMap.put(assetId, assetId);
											
											applicationAssetLinkId = new PvmApplicationAssetLinkId(pvmApplication.getApplicationId(), assetId);
											applicationAssetLink.setId(applicationAssetLinkId);
											
											if(this.applicationMap.containsKey(assetId)){
												tempApplicationList = this.applicationMap.get(assetId);
											} else {
												tempApplicationList = new ArrayList<ApplicationDTO>();
											}
										//	tempApplicationDTO = (ApplicationDTO) transformer.transform(pvmApplication, new ApplicationDTO());
											tempApplicationList.add(applicationDTO);
											this.applicationMap.put(assetId, tempApplicationList);
											// 存程式版本設備鏈接
											this.applicationAssetLinkDAO.insert(applicationAssetLink);
										}
									}
								}
							}
						}
					}
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "FOMS程式版本資料轉入成功").append("</br>");
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無FOMS程式版本資料轉入").append("</br>");
			}
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".transferApplicatonFoms() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferApplicatonFoms() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}

	/**
	 * 轉移TMS程式版本數據
	 * @throws Exception
	 */
	public void transferApplicatonTms()throws ServiceException {
		try{
			List<ApplicationAssetLinkDTO> applicationAssetLinkDTOs = this.applicationAssetLinkList;
			// 當前程式版本
			List<ApplicationDTO> nowApplicationDTOs = this.applicationDAO.getApplicationList();
			if(!CollectionUtils.isEmpty(applicationAssetLinkDTOs)){
				Transformer transformer = new SimpleDtoDmoTransformer();
				PvmApplication pvmApplication = null;
				String applicationId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_PVM_APPLICATION);
				int i = 0;
				List<Parameter> tempList = null;
				PvmApplicationAssetLink applicationAssetLink = null;
				PvmApplicationAssetLinkId applicationAssetLinkId = null;
				String applicationVersions = null;
				List<ApplicationDTO> tempApplicationList = null;
				// 讀程式版本資料
				List<ApplicationDTO> tmsApplicationDTOs = this.getApplicationDtoList();
				if(!CollectionUtils.isEmpty(tmsApplicationDTOs)){
					boolean isRepeat = false;
					boolean isOk = false;
					String tempApplicationName = null;
					for(ApplicationAssetLinkDTO applicationAssetLinkDTO : applicationAssetLinkDTOs){
						ApplicationDTO tempApplicationDTO = null;
						for(ApplicationDTO tempTmsAppDto : tmsApplicationDTOs){
							if(StringUtils.hasText(applicationAssetLinkDTO.getApplicationId())){
								// Task #3057 程式版本 環匯認GP
								isOk = false;
								tempApplicationName = tempTmsAppDto.getApplicationId().toUpperCase();
								if(StringUtils.hasText(tempApplicationName) 
										&& (applicationAssetLinkDTO.getApplicationId().toUpperCase().equals(tempApplicationName)
										|| (IAtomsConstants.PARAM_GP.equals(applicationAssetLinkDTO.getCustomerId()) && tempApplicationName.length() >= 2
										&& IAtomsConstants.PARAM_GP.equals(tempApplicationName.substring(0, 2))) )){
									isOk = true;
								}
								// 不滿足不保存
								if(!isOk){
									continue;
								}
								isRepeat = false;
								// 判斷與當前是否重復，若重復則不轉入
								for(ApplicationDTO applicationDTO : nowApplicationDTOs){
									if(StringUtils.hasText(applicationDTO.getName()) && StringUtils.hasText(applicationDTO.getVersion())){
										if((applicationDTO.getName().equals(applicationAssetLinkDTO.getApplicationId())
												|| applicationDTO.getName().equals(tempTmsAppDto.getApplicationId()))
												&& applicationDTO.getVersion().equals(tempTmsAppDto.getVersion())){
											isRepeat = true;
											break;
										}
									}
								}
								// 重復不保存
								if(isRepeat){
									continue;
								}
								i++;
								pvmApplication = new PvmApplication();
								// 程式代碼
								pvmApplication.setApplicationId(applicationId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
								
								// Task #3057 程式版本 環匯認GP
								if(IAtomsConstants.PARAM_GP.equals(applicationAssetLinkDTO.getCustomerId()) && tempApplicationName.length() >= 2 && IAtomsConstants.PARAM_GP.equals(tempApplicationName.substring(0, 2))){
									pvmApplication.setName(tempTmsAppDto.getApplicationId());
								} else {
									pvmApplication.setName(applicationAssetLinkDTO.getApplicationId());
								}
								
								// 程式名稱
							//	pvmApplication.setName(applicationAssetLinkDTO.getApplicationId());
								// 程式版本編號
								pvmApplication.setVersion(tempTmsAppDto.getVersion());
								// 轉換客戶信息
								pvmApplication.setCustomerId(this.getValueByName(applicationAssetLinkDTO.getCustomerId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()));
								// 設備類型
								pvmApplication.setAssetCategory(IAtomsConstants.ASSET_CATEGORY_EDC);
								pvmApplication.setCreatedDate(tempTmsAppDto.getCreatedDate());
								pvmApplication.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								// 刪除標記
								pvmApplication.setDeleted(IAtomsConstants.NO);
								
								this.applicationDAO.insert(pvmApplication);
								
								if(StringUtils.hasText(applicationAssetLinkDTO.getAssetTypeId())){
									// 按照S80 S90等模糊查詢
									for(String temp : StringUtils.toList(applicationAssetLinkDTO.getAssetTypeId(), IAtomsConstants.MARK_SEPARATOR)){
										// 查某一系列設備信息
										tempList = this.assetTypeDAO.listAssetByName(temp, true, false);
										if(!CollectionUtils.isEmpty(tempList)){
											for(Parameter parameter : tempList){
												applicationAssetLink = new PvmApplicationAssetLink();
												applicationAssetLinkId = new PvmApplicationAssetLinkId(pvmApplication.getApplicationId(), (String)parameter.getValue());
												applicationAssetLink.setId(applicationAssetLinkId);
												if(this.applicationMap != null){
													if(this.applicationMap.containsKey(parameter.getValue())){
														tempApplicationList = this.applicationMap.get(parameter.getValue());
													} else {
														tempApplicationList = new ArrayList<ApplicationDTO>();
													}
													tempApplicationDTO = (ApplicationDTO) transformer.transform(pvmApplication, new ApplicationDTO());
													tempApplicationList.add(tempApplicationDTO);
													this.applicationMap.put((String)parameter.getValue(), tempApplicationList);
												}
												
												// 存程式版本設備鏈接
												this.applicationAssetLinkDAO.insert(applicationAssetLink);
											}
										}
									}
								}
							
							}
						}
					}
					if(this.resultMsg != null){
						this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "TMS程式版本資料轉入成功").append("</br>");
					}
				} else {
					if(this.resultMsg != null){
						//this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "TMS程式版本資料轉入失敗").append("</br>");
						this.isOkTmsApp = false;
					}
				}
			} else {
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無TMS程式版本資料轉入").append("</br>");
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(".transferApplicatonTms() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferApplicatonTms() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferContract(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferContract(SessionContext sessionContext)throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			Map map = new HashMap();
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除合約信息
			this.contractDAO.deleteTransferData();
			
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
			}
			// 無公司信息
			if(!CollectionUtils.isEmpty(this.companyCodeList)){
				Transformer transformer = new SimpleDtoDmoTransformer();
				// 程式版本集合
				List<BimContractDTO> contractDTOs = this.oldDataTransferDAO.listContractData();
				// 合約map信息集合
				this.contractMap = new HashMap<String, List<Parameter>>();
				// 合約信息集合
				List<Parameter> tempContractList = null;
				Parameter tempParameter = null;
				if(!CollectionUtils.isEmpty(contractDTOs)){
					BimContract contract = null;
					BimContractVendor contractVendor = null;
					BimContractVendorId contractVendorId = null;
					String contractId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_CONTRACT);
					int i = 0;
					// 所有維護廠商
					List<Parameter> vendors = this.companyDAO.getCompanyList(StringUtils.toList(IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR, IAtomsConstants.MARK_SEPARATOR), null, false, null);
					for(BimContractDTO contractDTO : contractDTOs){
						i ++;
						contract = (BimContract) transformer.transform(contractDTO, new BimContract());
						// 主鍵
						contract.setContractId(contractId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						// 客戶
						contract.setCompanyId(this.getValueByName(contract.getCompanyId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue()));
						// 保存合約信息
						this.contractDAO.insert(contract);
						
						// 綁定客戶合約信息
						if(!IAtomsConstants.YES.equals(contract.getDeleted())){
							if(StringUtils.hasText(contract.getCompanyId())){
								tempParameter = new Parameter(contract.getContractCode(), contract.getContractId());
								if(this.contractMap.containsKey(contract.getCompanyId())){
									tempContractList = this.contractMap.get(contract.getCompanyId());
								} else {
									tempContractList = new ArrayList<Parameter>();
								}
								tempContractList.add(tempParameter);
								this.contractMap.put(contract.getCompanyId(), tempContractList);
							}
						}
						
						// 存合約廠商關系
						if(!CollectionUtils.isEmpty(vendors)){
							for(Parameter parameter : vendors){
								contractVendor = new BimContractVendor();
								contractVendorId = new BimContractVendorId(contract.getContractId(), (String)parameter.getValue());
								contractVendor.setId(contractVendorId);
								// 存合約廠商
								this.contractVendorDAO.insert(contractVendor);
							}
						}
					}
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "合約維護資料轉入成功").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無合約維護資料轉入").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "合約資料轉入失敗").append("</br>");
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
		
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferContract"));
			
			LOGGER.error(".transferContract() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferContract"));
			
			LOGGER.error(".transferContract() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferAssetType(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferAssetType(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除設備品項信息
			this.assetTypeDAO.deleteTransferData();
			
			// 判斷DB設備品項資料是否爲空
		//	boolean isNoData = this.assetTypeDAO.isNoData();
			Transformer transformer = new SimpleDtoDmoTransformer();
			// 設備品項集合
			List<AssetTypeDTO> assetTypeDTOs = this.oldDataTransferDAO.listAssetTypeData();
			if(!CollectionUtils.isEmpty(assetTypeDTOs)){
				// 設備品項
				DmmAssetType assetType = null;
				// 設備品項支援功能
				DmmAssetSupportedFunction assetSupportedFunction = null;
				DmmAssetSupportedFunctionId assetSupportedFunctionId = null;
				// 設備品項通訊模式
				DmmAssetSupportedComm assetSupportedComm = null;
				DmmAssetSupportedCommId assetSupportedCommId = null;
				String assetTypeId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_ASSET_TYPE);
				int i = 0;
				List<String> brandList = null;
				List<String> modelList = null;
				
				String supportedFunction = null;
				String supportedComm = null;
				// 設備品項
				this.assetTypeList = new ArrayList<Parameter>();
				// edc設備集合
				this.edcTypeList = new ArrayList<Parameter>();
				// 周邊設備集合
				this.peripheralsList = new ArrayList<Parameter>();
				Parameter parameter = null;
				for(AssetTypeDTO assetTypeDTO : assetTypeDTOs){
					i ++;
					assetType = (DmmAssetType) transformer.transform(assetTypeDTO, new DmmAssetType());
					if(StringUtils.hasText(assetType.getName())){
						assetType.setName(assetType.getName().trim());
					}
					// 主鍵
					assetType.setAssetTypeId(assetTypeId + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
					// 廠牌集合
					brandList = this.oldDataTransferDAO.listBrandById(assetTypeDTO.getAssetTypeId());
					// 廠牌
					assetType.setBrand(this.removeDuplicate(brandList, assetTypeDTO.getBrand()));
					// 模型集合
					modelList = this.oldDataTransferDAO.listModelById(assetTypeDTO.getAssetTypeId());
					// 模型
					assetType.setModel(this.removeDuplicate(modelList, assetTypeDTO.getModel()));
				//	this.assetTypeDAO.insert(assetType);
					this.assetTypeDAO.getDaoSupport().save(assetType);
					
					// 增加設備品項列表 Bug #2880 不包含已刪除
					if(IAtomsConstants.NO.equals(assetType.getDeleted())){
						parameter = new Parameter(assetType.getName(), assetType.getAssetTypeId());
						this.assetTypeList.add(parameter);
					}
					// 增加edc設備集合
					if(IAtomsConstants.ASSET_CATEGORY_EDC.equals(assetType.getAssetCategory())){
						parameter = new Parameter(assetType.getName(), assetType.getAssetTypeId());
						this.edcTypeList.add(parameter);
					}
					// 增加周邊設備集合
					if(IAtomsConstants.ASSET_CATEGORY_RODUND_ASSET.equals(assetType.getAssetCategory())){
						parameter = new Parameter(assetType.getName(), assetType.getAssetTypeId());
						this.peripheralsList.add(parameter);
					}
					// 存支援功能
					if(!CollectionUtils.isEmpty(this.assetSupportedFunctionMap)){
						if(this.assetSupportedFunctionMap.containsKey(assetTypeDTO.getAssetTypeId())){
							// 配置的支援功能進行分離、轉換、保存 如：Pinpad、Dongle、電子簽名
							supportedFunction = this.assetSupportedFunctionMap.get(assetTypeDTO.getAssetTypeId());
							if(StringUtils.hasText(supportedFunction)){
								for(String temp : StringUtils.toList(supportedFunction, IAtomsConstants.MARK_STOP)){
									// 轉換支援功能
									temp = this.getValueByName(temp, IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode());
									if(StringUtils.hasText(temp)){
										assetSupportedFunction = new DmmAssetSupportedFunction();
										assetSupportedFunctionId = new DmmAssetSupportedFunctionId(temp, assetType.getAssetTypeId());
										assetSupportedFunction.setId(assetSupportedFunctionId);
										this.assetTypeDAO.getDaoSupport().save(assetSupportedFunction);
									}
								}
						//		this.assetTypeDAO.getDaoSupport().flush();
							}
						}
					}
					
					// 存通訊模式
					if(!CollectionUtils.isEmpty(this.assetSupportedCommMap)){
						if(this.assetSupportedCommMap.containsKey(assetTypeDTO.getAssetTypeId())){
							// 配置的通訊模式進行分離、轉換、保存 如：TCP/IP、GPRS、3G、Wifi
							supportedComm = this.assetSupportedCommMap.get(assetTypeDTO.getAssetTypeId());
							if(StringUtils.hasText(supportedComm)){
								for(String temp : StringUtils.toList(supportedComm, IAtomsConstants.MARK_STOP)){
									// 轉換通訊模式
									temp = this.getValueByName(temp, IATOMS_PARAM_TYPE.COMM_MODE.getCode());
									if(StringUtils.hasText(temp)){
										assetSupportedComm = new DmmAssetSupportedComm();
										assetSupportedCommId = new DmmAssetSupportedCommId(assetType.getAssetTypeId(), temp);
										assetSupportedComm.setId(assetSupportedCommId);
										this.assetTypeDAO.getDaoSupport().save(assetSupportedComm);
									}
								}
						//		this.assetTypeDAO.getDaoSupport().flush();
							}
						}
					}
				}
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備品項資料轉入成功").append("</br>");
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無設備品項資料轉入").append("</br>");
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferAssetType"));
			
			LOGGER.error(".transferAssetType() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferAssetType"));
			
			LOGGER.error(".transferAssetType() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
		
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferRepository(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferRepository(SessionContext sessionContext)
			throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			// 刪除所有庫存資料
			this.repositoryDAO.deleteTransferData();
			Integer queryRepositorySize = null;
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			LOGGER.debug("dmmRepository -- is begin ...");
			Map map = new HashMap();
			OldDataTransferFormDTO formDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();

			// 若無設備信息則去DB查詢
			if(CollectionUtils.isEmpty(this.edcCategoryList) || CollectionUtils.isEmpty(this.RelatedProductsCategoryList)){
				this.edcCategoryList = this.assetTypeDAO.listAssetByNameAndType(null, true, false);
				this.RelatedProductsCategoryList = this.assetTypeDAO.listAssetByNameAndType(null, false, true);
				if(CollectionUtils.isEmpty(this.edcCategoryList) && CollectionUtils.isEmpty(this.RelatedProductsCategoryList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				} else if (CollectionUtils.isEmpty(this.edcCategoryList)) {
					this.edcCategoryList = new ArrayList<Parameter>();
				} else if (CollectionUtils.isEmpty(this.RelatedProductsCategoryList)) {
					this.RelatedProductsCategoryList = new ArrayList<Parameter>();
				} 
			}
			
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.warehouseList)){
				this.warehouseList = this.warehouseDAO.listWarehouseByName(null);
				if(CollectionUtils.isEmpty(this.warehouseList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入倉庫據點資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			if (CollectionUtils.isEmpty(this.contractList)) {
				this.contractList = this.contractDAO.getContractByCode(null);
				if(CollectionUtils.isEmpty(this.getContractList())){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入合約資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
				if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無帳號則去DB查詢
			if(CollectionUtils.isEmpty(this.userList)){
				this.userList = this.admUserDAO.getUserAll();
				if(CollectionUtils.isEmpty(this.userList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入使用者帳號資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			Timestamp startDate = DateTimeUtils.getCurrentTimestamp();
			// 得到TMS資料
			Map<String, SrmCaseHandleInfoDTO> tmsCaseInfoMap = this.getCaseInfoList(formDTO, false);
			if(CollectionUtils.isEmpty(tmsCaseInfoMap)){
				this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "TMS資料為空").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				return sessionContext;
			}
			List<ApplicationDTO> allApplicationDTOs = this.applicationDAO.getApplicationList();
			if(CollectionUtils.isEmpty(allApplicationDTOs)){
				this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入程式版本資料").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				return sessionContext;
			}
			//若無程式版本信息去DB中查詢
			/*if(CollectionUtils.isEmpty(this.applicationMap)){
				this.applicationMap = new HashMap<String, List<ApplicationDTO>>();
				List<ApplicationDTO> allApplicationDTOs = this.applicationDAO.getApplicationList();
				List<ApplicationDTO> tempApplicationList = null;
				if (!CollectionUtils.isEmpty(allApplicationDTOs)) {
					for(ApplicationDTO tempApplicationDTO : allApplicationDTOs){
						if(StringUtils.hasText(tempApplicationDTO.getAssetTypeId())){
							if(this.applicationMap.containsKey(tempApplicationDTO.getAssetTypeId())){
								tempApplicationList = this.applicationMap.get(tempApplicationDTO.getAssetTypeId());
							} else {
								tempApplicationList = new ArrayList<ApplicationDTO>();
							}
							tempApplicationList.add(tempApplicationDTO);
							this.applicationMap.put(tempApplicationDTO.getAssetTypeId(), tempApplicationList);
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入程式版本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}*/
			// 若無部門信息則去DB查詢 依公司查找放入map中
			/*if(CollectionUtils.isEmpty(this.departmentMap)){
				this.departmentMap = new HashMap<String, List<Parameter>>();
				List<Parameter> departmentList = null;
				if (!CollectionUtils.isEmpty(this.companyCodeList)) {
					for (Parameter company : companyCodeList) {
						if (!departmentMap.containsKey(company.getValue().toString())) {
							departmentList = this.departmentDAO.getDepts(company.getValue().toString());
							if (!CollectionUtils.isEmpty(departmentList)) {
								departmentMap.put(company.getValue().toString(), departmentList);
							}
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}*/
			// 若無帳號則去DB查詢
			if(CollectionUtils.isEmpty(this.departmentList)){
				this.departmentList = this.departmentDAO.getDepts(null);
				if(CollectionUtils.isEmpty(this.departmentList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入部門基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//Task #2584
			if(CollectionUtils.isEmpty(this.merchantMap)){
				this.merchantMap = new HashMap<String, List<Parameter>>();
				List<Parameter> merchantList = null;
				if (CollectionUtils.isEmpty(this.companyCodeList)) {
					this.companyCodeList = this.companyDAO.getCompanyList(true);
				}
				for (Parameter company : companyCodeList) {
					if (!merchantMap.containsKey(company.getValue().toString())) {
						merchantList = this.merchantDAO.getMerchantsByCodeAndCompamyId(null, company.getValue().toString());
						if (!CollectionUtils.isEmpty(merchantList)) {
							merchantMap.put(company.getValue().toString(), merchantList);
						}
					}
				}
				if(CollectionUtils.isEmpty(this.merchantMap)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入客戶特店基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//Task #2584
			if(CollectionUtils.isEmpty(this.merchantHeaderMap)){
				this.merchantHeaderMap = new HashMap<String, List<Parameter>>();
				List<Parameter> merchantHeadertList = null;
				for (String key : merchantMap.keySet()) {
					for (Parameter parameter : merchantMap.get(key)) {
						if (!merchantHeaderMap.containsKey(parameter.getValue()!=" "?String.valueOf(parameter.getValue()):" ")) {
							merchantHeadertList = this.merchantHeaderDAO.getMerchantHeadersBy(parameter.getValue()!=" "?String.valueOf(parameter.getValue()):" ", null);
							if (!CollectionUtils.isEmpty(merchantHeadertList)) {
								merchantHeaderMap.put(parameter.getValue()!=" "?String.valueOf(parameter.getValue()):" ", merchantHeadertList);
							}
						}
					}
				}
				if(CollectionUtils.isEmpty(this.merchantHeaderMap)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入特店表頭基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			
			if (((!CollectionUtils.isEmpty(this.edcCategoryList)) || (!CollectionUtils.isEmpty(this.RelatedProductsCategoryList)))
					&& !CollectionUtils.isEmpty(this.warehouseList)
					&& !CollectionUtils.isEmpty(this.contractList)
					&& !CollectionUtils.isEmpty(this.companyCodeList)
					&& !CollectionUtils.isEmpty(this.departmentList)
					&& !CollectionUtils.isEmpty(this.userList)
					&& !CollectionUtils.isEmpty(allApplicationDTOs)
					&& !CollectionUtils.isEmpty(tmsCaseInfoMap)
					&& !CollectionUtils.isEmpty(this.merchantMap)) {
				// 清空返回消息值
				this.resultMsg = new StringBuilder();
				
				//if(isNoData){
					Transformer transformer = new SimpleDtoDmoTransformer();
					// 設備庫存集合
					List<DmmRepositoryDTO> dmmRepositoryDTOs = this.oldDataTransferDAO.listRepository();
					if(!CollectionUtils.isEmpty(dmmRepositoryDTOs)){
						queryRepositorySize = dmmRepositoryDTOs.size();
						int i = 1;
						DmmRepository dmmRepository = null;
						String assetTypeId = null;
						String warehouseId = null;
						String contractId = null;
						String maType = null;
						String assetStatus = null;
						String action = null;
						String faultComponent = null;
						String faultDescription = null;
						String installType = null;
						String installedAdressLocation = null;
						String departmentId = null;
						String companyId = null;
						String retireReasonCode = null;
						String maintainCompany = null;
						String carrier = null;
						String borrower = null;
						Parameter parameter = null;
						String merId = null;
						BimMerchant merchant = null;
						BimMerchantHeader merchantHeader = null;
						String merhantId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT);
						String headerId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT_HEADER);
						DmmRepositoryFaultCom repositoryFaultCom = null;
						DmmRepositoryFaultDesc repositoryFaultDesc = null;
						DmmRepositoryFaultComId repositoryFaultComId = null;
						DmmRepositoryFaultDescId repositoryFaultDescId = null;
						AdmUser admUser = null;
						String maintainUser = null;
						List<ApplicationDTO> tempApplicationList = null;
						// 臨時軟體版本案件DTO
						SrmCaseHandleInfoDTO tempSrmCaseHandleInfoDTO = null;
						// 臨時軟體版本
						String tempSoftVersion = null;
						
						Boolean appFlag = false;
						int k = 1;
						//最後一個使用者id
						//Task #2566 領用人、借用人，對應不到，則為NULL update by 2017/09/30
						/*int userId = 0;
						Boolean userFlag = true;
						try {
							//最後一個使用者
							parameter = this.admUserDAO.getLastUser();
							LOGGER.debug("user == "+parameter.getValue()+","+parameter.getName());
							userId = Integer.valueOf(parameter.getValue().toString());
						} catch (Exception e) {
							userFlag = false;
							this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "使用者帳號資料異常").append("</br>設備庫存資料轉入失敗</br>");
							map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
							map.put("resultMsg", this.resultMsg);
							sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
							return sessionContext;
						}*/
						//if (userFlag) {
							for (DmmRepositoryDTO dmmRepositoryDTO : dmmRepositoryDTOs) {
								//當設備序號為64998216 64998215時不轉 2017/11/17
								if ("64998216".equals(dmmRepositoryDTO.getSerialNumber()) || "64998215".equals(dmmRepositoryDTO.getSerialNumber())) {
									continue;
								}
								dmmRepository = (DmmRepository) transformer.transform(dmmRepositoryDTO, new DmmRepository());
								//i++;
								//設備品項id
								if (StringUtils.hasText(dmmRepositoryDTO.getAssetTypeId())) {
									//Bug #2941
									if ("EDC".equals(dmmRepositoryDTO.getAssetCateGory())) {
										assetTypeId = this.getValueByName(dmmRepositoryDTO.getAssetTypeId(), "edcCategory");
									} else if ("Related_Products".equals(dmmRepositoryDTO.getAssetCateGory())) {
										assetTypeId = this.getValueByName(dmmRepositoryDTO.getAssetTypeId(), "RelatedProductsCategory");
									}
									//assetTypeId = this.getValueByName(dmmRepositoryDTO.getAssetTypeId(), "assetType");
									if (StringUtils.hasText(assetTypeId)) {
										dmmRepository.setAssetTypeId(assetTypeId);
									} else {
										 dmmRepository.setAssetTypeId(null);
										LOGGER.error("設備庫存資料轉入失敗</br>");
										LOGGER.error(dmmRepositoryDTO.getAssetTypeId() + "assetTypeId 無對應資料</br>");
									}
								}
								//倉庫據點id
								if (StringUtils.hasText(dmmRepositoryDTO.getWarehouseId())) {
									warehouseId = this.getValueByName(dmmRepositoryDTO.getWarehouseId(), WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
									if (StringUtils.hasText(warehouseId)) {
										dmmRepository.setWarehouseId(warehouseId);
									} else {
										dmmRepository.setWarehouseId(null);
										LOGGER.error("設備庫存資料轉入失敗</br>");
										LOGGER.error(dmmRepositoryDTO.getWarehouseId() + "--warehouseId 無對應資料</br>");
									}
								}
								
								//合約id
								if (StringUtils.hasText(dmmRepositoryDTO.getContractId())) {
									contractId = this.getValueByName(dmmRepositoryDTO.getContractId(), BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
									if (StringUtils.hasText(contractId)) {
										dmmRepository.setContractId(contractId);
									} else {
										dmmRepository.setContractId(null);
										LOGGER.debug("設備庫存資料轉入失敗LOGGER.debug(</br>");
										LOGGER.debug(dmmRepositoryDTO.getContractId() + "--contractId 無對應資料</br>");
									}
								}
								//程式版本
								if (StringUtils.hasText(dmmRepositoryDTO.getDtid())) {
									tempSrmCaseHandleInfoDTO = tmsCaseInfoMap.get(dmmRepositoryDTO.getDtid());
									if(tempSrmCaseHandleInfoDTO != null){
										// 軟體版本
										tempSoftVersion = tempSrmCaseHandleInfoDTO.getSoftwareVersion();
										if(StringUtils.hasText(tempSoftVersion)){
											// 軟體版本
											if(StringUtils.hasText(tempSoftVersion)
													&& tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) > 0
													&& tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT) > 0){
												if(!CollectionUtils.isEmpty(allApplicationDTOs)){
													for(ApplicationDTO tempApplication : allApplicationDTOs){
														// 當前行程式名稱
														if((tempSoftVersion.substring(tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) + 1,
																tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT))).equals(tempApplication.getVersion())
																&& (tempSoftVersion.substring(0, tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_LEFT))).equals(tempApplication.getName())){
															dmmRepository.setApplicationId(tempApplication.getApplicationId());
															appFlag = true;
															break;
														}
													}
												}
											}
										}
									}
									if (!appFlag) {
										dmmRepository.setApplicationId(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getDtid() + "--applicationId tms and dtid 無對應資料</br>");
									}
								}
								
								// 故障組件 
								if (StringUtils.hasText(dmmRepositoryDTO.getFaultComponent())) {
									faultComponent = this.getValueByName(dmmRepositoryDTO.getFaultComponent(), IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
									if (StringUtils.hasText(faultComponent)) {
										dmmRepository.setFaultComponent(faultComponent);
									} else {
										dmmRepository.setFaultComponent(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getFaultComponent() + "--faultComponent 無對應資料</br>");
									}
								}
								
								// 故障說明 
								if (StringUtils.hasText(dmmRepositoryDTO.getFaultDescription())) {
									faultDescription = this.getValueByName(dmmRepositoryDTO.getFaultDescription(), IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
									if (StringUtils.hasText(faultDescription)) {
										dmmRepository.setFaultDescription(faultDescription);
									} else {
										dmmRepository.setFaultDescription(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getFaultDescription() + "--faultDescription 無對應資料</br>");
									}
								}

								// 裝機地址 市縣區域
								if (StringUtils.hasText(dmmRepositoryDTO.getInstalledAdressLocation())) {
									
									installedAdressLocation = this.getValueByName(dmmRepositoryDTO.getInstalledAdressLocation(), IATOMS_PARAM_TYPE.LOCATION.getCode());
									if (StringUtils.hasText(installedAdressLocation)) {
										dmmRepository.setInstalledAdressLocation(installedAdressLocation);
									} else {
										dmmRepository.setInstalledAdressLocation(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getInstalledAdressLocation() + "--installedAdressLocation 無對應資料</br>");
									}
								}
								//資產Owner
								if (StringUtils.hasText(dmmRepositoryDTO.getAssetOwner())) {
									companyId = this.getValueByName(dmmRepositoryDTO.getAssetOwner(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
									if (StringUtils.hasText(companyId)) {
										dmmRepository.setAssetOwner(companyId);
									} else {
										dmmRepository.setAssetOwner(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getAssetOwner() + "--assetOwner 無對應資料</br>");
									}
								}
								//使用人
								if (StringUtils.hasText(dmmRepositoryDTO.getAssetUser())) {
									companyId = this.getValueByName(dmmRepositoryDTO.getAssetUser(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
									if (StringUtils.hasText(companyId)) {
										dmmRepository.setAssetUser(companyId);
									} else {
										dmmRepository.setAssetUser(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getAssetUser() + "--assetUser 無對應資料</br>");
									}
								}
								//特店  Task #2584
								if ("Y".equals(dmmRepositoryDTO.getIsNoMerId()) ){
									dmmRepository.setMerchantId(null);
									if (StringUtils.hasText(dmmRepository.getAssetUser()) && StringUtils.hasText(dmmRepositoryDTO.getMerchantId())) {
										merId = this.getValueByIdAndName(dmmRepositoryDTO.getMerchantId(), dmmRepository.getAssetUser(), MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
										if (StringUtils.hasText(merId)) {
											dmmRepository.setMerchantId(merId);
										} else {
											LOGGER.debug(dmmRepositoryDTO.getMerchantId() + "--merId 無對應資料 新增特點 -- </br>");
											merchant = new BimMerchant();
											merchant.setMerchantId(merhantId+"-"+k);
											merchant.setCompanyId(dmmRepository.getAssetUser());
											merchant.setMerchantCode(dmmRepositoryDTO.getMerchantId());
											merchant.setName(dmmRepositoryDTO.getMerName());
											merchant.setDeleted("Y");
											this.merchantDAO.insert(merchant);
											dmmRepository.setMerchantId(merhantId + "-" + k);
											LOGGER.debug(merchant.getMerchantCode() + "," + merchant.getMerchantId() + "--merchantId new paramter </br>");
											parameter = new Parameter(merchant.getMerchantCode(), merchant.getMerchantId());
											if (this.merchantMap.containsKey(dmmRepository.getAssetUser())) {
												this.merchantMap.get(dmmRepository.getAssetUser()).add(parameter);
											} else {
												this.merchantMap.put(dmmRepository.getAssetUser(),new ArrayList<Parameter>());
												this.merchantMap.get(dmmRepository.getAssetUser()).add(parameter);
											}
											//this.merchantMap.get(dmmRepository.getAssetUser()).add(parameter);
											this.merchantHeaderMap.put(merhantId + "-" + k, new ArrayList<Parameter>());
											k++;
										}
									}
								}
								
								//特店  表頭 Task #2584
								if ("Y".equals(dmmRepositoryDTO.getIsNoMerHeaderId()) ){
									dmmRepository.setMerchantHeaderId(null);
									if (StringUtils.hasText(dmmRepository.getMerchantId()) && StringUtils.hasText(dmmRepositoryDTO.getMerchantHeaderId())) {
										merId = this.getValueByIdAndName(dmmRepositoryDTO.getMerchantHeaderId(), dmmRepository.getMerchantId(), BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
										if (StringUtils.hasText(merId)) {
											dmmRepository.setMerchantHeaderId(merId);
										} else {
											LOGGER.debug(dmmRepositoryDTO.getMerchantHeaderId() + "--merchantHeaderId 無對應資料 新增 - 特點 表頭 -- </br>");
											merchantHeader = new BimMerchantHeader();
											merchantHeader.setMerchantHeaderId(headerId + "-" + k);
											merchantHeader.setId(headerId + "-" + k);
											merchantHeader.setMerchantId(dmmRepository.getMerchantId());
											merchantHeader.setHeaderName(dmmRepositoryDTO.getMerchantHeaderId());
											merchantHeader.setIsVip("N");
											merchantHeader.setDeleted("Y");
											this.merchantHeaderDAO.insert(merchantHeader);
											dmmRepository.setMerchantHeaderId(headerId + "-" + k);
											LOGGER.debug(merchantHeader.getMerchantId() + "," + merchantHeader.getMerchantHeaderId() + "--merchantHeaderId new paramter </br>");
											parameter = new Parameter(merchantHeader.getMerchantId(), merchantHeader.getMerchantHeaderId());
											if (this.merchantHeaderMap.containsKey(dmmRepository.getMerchantId())) {
												this.merchantHeaderMap.get(dmmRepository.getMerchantId()).add(parameter);
											} else {
												this.merchantHeaderMap.put(dmmRepository.getMerchantId(), new ArrayList<Parameter>());
												this.merchantHeaderMap.get(dmmRepository.getMerchantId()).add(parameter);
											}
											//this.merchantHeaderMap.get(dmmRepository.getMerchantId()).add(parameter);
											k++;
										}
									}
								}
								
								//維護廠商
								if (StringUtils.hasText(dmmRepositoryDTO.getMaintainCompany())) {
									maintainCompany = this.getValueByName(dmmRepositoryDTO.getMaintainCompany(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
									if (StringUtils.hasText(maintainCompany)) {
										dmmRepository.setMaintainCompany(maintainCompany);
									} else {
										dmmRepository.setMaintainCompany(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getMaintainCompany() + "--maintainCompany 無對應資料</br>");
									}
								}
								//維修廠商
								if (StringUtils.hasText(dmmRepositoryDTO.getRepairVendor())) {
									companyId = this.getValueByName(dmmRepositoryDTO.getRepairVendor(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
									if (StringUtils.hasText(companyId)) {
										dmmRepository.setRepairVendor(companyId);
									} else {
										dmmRepository.setRepairVendor(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getRepairVendor() + "--repairVendor 無對應資料</br>");
									}
								}
								//領用人
								if (StringUtils.hasText(dmmRepositoryDTO.getCarrier())) {
									//Task #2866 2017/11/20
									carrier = this.getValueByName(dmmRepositoryDTO.getCarrier().length() > 2 ? dmmRepositoryDTO.getCarrier().substring(0, 3) : dmmRepositoryDTO.getCarrier(), AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
									if (StringUtils.hasText(carrier)) {
										dmmRepository.setCarrier(carrier);
									} else {
										dmmRepository.setCarrier(null);
										LOGGER.debug(dmmRepositoryDTO.getCarrier() + "--carrier 無對應資料 </br>");
										/*admUser = new AdmUser(StringUtils.toFixString(10, userId + k), " ", "N");
										dmmRepository.setCarrier(StringUtils.toFixString(10, userId + k));
										admUser.setCname(dmmRepositoryDTO.getCarrier());
										admUser.setId(admUser.getUserId());
										k ++;
										this.userList.add(new Parameter(admUser.getCname(), admUser.getUserId()));
										this.admUserDAO.insert(admUser);
										LOGGER.debug(dmmRepositoryDTO.getCarrier() + "--carrier 無對應資料,新增帳號</br>");*/
									}
								}
								//借用人
								if (StringUtils.hasText(dmmRepositoryDTO.getBorrower())) {
									//Task #2866 2017/11/20
									borrower = this.getValueByName(dmmRepositoryDTO.getBorrower().length() > 2 ? dmmRepositoryDTO.getBorrower().substring(0, 3) : dmmRepositoryDTO.getBorrower(), AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
									if (StringUtils.hasText(borrower)) {
										dmmRepository.setBorrower(borrower);
									} else {
										dmmRepository.setBorrower(null);
										LOGGER.debug(dmmRepositoryDTO.getBorrower() + "--borrower 無對應資料 </br>");
										/*admUser = new AdmUser(StringUtils.toFixString(10, userId + k), " ", "N");
										dmmRepository.setBorrower(StringUtils.toFixString(10, userId + k));
										admUser.setCname(dmmRepositoryDTO.getBorrower());
										admUser.setId(admUser.getUserId());
										k ++;
										this.userList.add(new Parameter(admUser.getCname(), admUser.getUserId()));
										this.admUserDAO.insert(admUser);
										LOGGER.debug(dmmRepositoryDTO.getBorrower() + "--borrower 無對應資料,新增帳號</br>");*/
									}
								}
								//維護人員
								if (StringUtils.hasText(dmmRepositoryDTO.getMaintainUser())) {
									maintainUser = this.getValueByName(dmmRepositoryDTO.getMaintainUser(), AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
									if (StringUtils.hasText(maintainUser)) {
										dmmRepository.setMaintainUser(maintainUser);
									} else {
										dmmRepository.setMaintainUser(null);
										LOGGER.debug(dmmRepositoryDTO.getMaintainUser() + "--maintainUser 無對應資料 </br>");
										/*admUser = new AdmUser(StringUtils.toFixString(10, userId + k), " ", "N");
										dmmRepository.setMaintainUser(StringUtils.toFixString(10, userId + k));
										admUser.setCname(dmmRepositoryDTO.getMaintainUser());
										admUser.setId(admUser.getUserId());
										k ++;
										this.userList.add(new Parameter(admUser.getCname(), admUser.getUserId()));
										this.admUserDAO.insert(admUser);
										LOGGER.debug(dmmRepositoryDTO.getMaintainUser() + "--maintainUser 無對應資料,新增帳號</br>");*/
									}
								}
								//原廠
								if (StringUtils.hasText(dmmRepositoryDTO.getRepairCompany())) {
									companyId = this.getValueByName(dmmRepositoryDTO.getRepairCompany(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
									if (StringUtils.hasText(companyId)) {
										dmmRepository.setRepairCompany(companyId);
									} else {
										dmmRepository.setRepairCompany(null);
										LOGGER.debug("設備庫存資料轉入失敗</br>");
										LOGGER.debug(dmmRepositoryDTO.getRepairCompany() + "--repairCompany 無對應資料</br>");
									}
								}
								//維護部門
								if (StringUtils.hasText(dmmRepositoryDTO.getDepartmentId())) {
									if (!dmmRepositoryDTO.getDepartmentId().equals(IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode())) {
										departmentId = this.getValueByName(dmmRepositoryDTO.getDepartmentId(), BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
										if (StringUtils.hasText(departmentId)) {
											dmmRepository.setDepartmentId(departmentId);
										} else {
											dmmRepository.setDepartmentId(null);
											LOGGER.debug("設備庫存資料轉入失敗</br>");
											LOGGER.debug(dmmRepositoryDTO.getDepartmentId() + "--departmentId 無對應資料</br>");
										}
									}
								}
								//裝機部門
								//Bug #2583
								if (StringUtils.hasText(dmmRepositoryDTO.getInstalledDeptId())) {
									//if (!dmmRepositoryDTO.getInstalledDeptId().equals(IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode())) {
										departmentId = this.getValueByName(dmmRepositoryDTO.getInstalledDeptId(), BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue());
										if (StringUtils.hasText(departmentId)) {
											dmmRepository.setInstalledDeptId(departmentId);
										} else {
											dmmRepository.setInstalledDeptId(null);
											LOGGER.debug("設備庫存資料轉入失敗</br>");
											LOGGER.debug(dmmRepositoryDTO.getInstalledDeptId() + "--installedDeptId 無對應資料</br>");
										}
								//	}
								}
								//原廠
								/*if (StringUtils.hasText(dmmRepositoryDTO.getRetireReasonCode())) {
									retireReasonCode = this.getValueByName(dmmRepositoryDTO.getRetireReasonCode(), IATOMS_PARAM_TYPE.RETIRE_REASON.getCode());
									if (StringUtils.hasText(retireReasonCode)) {
										dmmRepository.setRetireReasonCode(retireReasonCode);
									} else {
										this.resultMsg.append("設備庫存資料轉入失敗").append("</br>");
										this.resultMsg.append(dmmRepositoryDTO.getRetireReasonCode() + "--retireReasonCode 無對應資料</br>");
									}
								}*/
								LOGGER.debug("serialNumber --------> "+dmmRepositoryDTO.getSerialNumber());
								LOGGER.debug("dmmRepositoryDTO tostring --------> "+dmmRepositoryDTO.toString());
								//保存庫存主檔
								this.repositoryDAO.getDaoSupport().save(dmmRepository);
								//
								if (StringUtils.hasText(dmmRepository.getFaultComponent())) {
									repositoryFaultCom = new DmmRepositoryFaultCom(repositoryFaultComId);
									repositoryFaultComId = new DmmRepositoryFaultComId(dmmRepository.getAssetId(), dmmRepository.getFaultComponent());
									repositoryFaultCom.setId(repositoryFaultComId);
									this.repositoryFaultComDAO.getDaoSupport().save(repositoryFaultCom);
								}
								//
								if (StringUtils.hasText(dmmRepository.getFaultDescription())) {
									repositoryFaultDesc = new DmmRepositoryFaultDesc();
									repositoryFaultDescId = new DmmRepositoryFaultDescId(dmmRepository.getAssetId(), dmmRepository.getFaultDescription());
									repositoryFaultDesc.setId(repositoryFaultDescId);
									this.repositoryFaultDescDAO.getDaoSupport().save(repositoryFaultDesc);
								}
								LOGGER.debug("dmmRepositoryDTO after tostring --------> "+dmmRepositoryDTO.toString());
							}
							this.repositoryDAO.getDaoSupport().flush();
							if (repositoryFaultCom!= null) {
								this.repositoryFaultComDAO.getDaoSupport().flush();
							}
							if (repositoryFaultDesc != null) {
								this.repositoryFaultDescDAO.getDaoSupport().flush();
							}	
							// 判斷DB設備庫存資料數量
							Integer count = this.repositoryDAO.countData();
							if (!count.equals(queryRepositorySize)) {
								this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存轉移成功:共"+queryRepositorySize+"筆，轉入"+count+"筆").append("</br>");
							} else {
								this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存轉移成功:共轉入"+count+"筆").append("</br>");
							}
						} else {
							this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無設備庫存資料轉入").append("</br>");
						}
					//} else {
 					//	this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存資料已轉入，不能重復轉入").append("</br>");
					//}
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存資料轉入所需資料不完整").append("</br>");
				}
			//}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			LOGGER.debug(this.resultMsg);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferRepository"));
			
			LOGGER.debug(this.resultMsg);
			LOGGER.error(".transferRepository() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferRepository"));
			
			LOGGER.debug(this.resultMsg);
			LOGGER.error(".transferRepository() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#deleteHistoryRepository()
	 */
	@Override
	public Boolean deleteHistoryRepository(String tempLogMessage) throws ServiceException{
		Boolean isDelete = false;
		LOGGER.debug("OldDataTransferService do deleteHistoryRepository start...");
		try {
			this.dmmRepositoryHistDAO.deleteTransferData();
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(tempLogMessage);
			isDelete = true;
		} catch (DataAccessException e) {
			LOGGER.debug(this.resultMsg);
			LOGGER.error(".deleteHistoryRepository() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.debug(this.resultMsg);
			LOGGER.error(".deleteHistoryRepository() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return isDelete;
	}
	/**
	 * 
	 * Purpose:多線程處理庫存歷史數據轉換
	 * @author amandawang
	 * @param formDTO
	 * @return void
	 */
	public void repositoryHistoryDataThread(OldDataTransferFormDTO formDTO) throws ServiceException{
		if (!StringUtils.hasText(this.errorFlag)) {
			this.setErrorFlag("N");
		}
		try {
			if ("N".equals(this.errorFlag)) {
				Timestamp startDate = DateTimeUtils.getCurrentTimestamp();
				// 案件資料集合
				List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = formDTO.getDmmRepositoryHistoryDTOs();
				Transformer transformer = new SimpleDtoDmoTransformer();
				int i = 1;
				DmmRepositoryHistory dmmRepositoryHistory = null;
				String assetTypeId = null;
				String warehouseId = null;
				String contractId = null;
				String maType = null;
				String assetStatus = null;
				String action = null;
				String faultComponent = null;
				String faultDescription = null;
				String installType = null;
				String installedAdressLocation = null;
				String departmentId = null;
				String companyId = null;
				String retireReasonCode = null;
				String maintainCompany = null;
				String userCompany = null;
				String merchantId = null;
				String merchantHeaderId = null;
				Parameter parameter = null;
				String merId = null;
				BimMerchant merchant = null;
				BimMerchantHeader merchantHeader = null;
				String merhantId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT);
				String headerId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_BIM_MERCHANT_HEADER);
				BimContract bimContract = null;
				String carrier = null;
				String borrower = null;
				AdmUser admUser = null;
				String maintainUser = null;
				DmmRepositoryHistoryComm repositoryFaultCom = null;
				DmmRepositoryHistoryDesc repositoryFaultDesc = null;
				DmmRepositoryHistoryCommId repositoryFaultComId = null;
				DmmRepositoryHistoryDescId repositoryFaultDescId = null;
				List<Parameter> contracts = null;
				String owner = null;
				Boolean merFlag = false;
				int k = 1;
				//String userId = null;
				//userId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_ADM_USER);
				Boolean flag = false;
				List<ApplicationDTO> tempApplicationList = null;
				for (DmmRepositoryHistoryDTO dmmRepositoryHistoryDTO : dmmRepositoryHistoryDTOs) {
					if ("N".equals(this.errorFlag)) {
						//當設備序號為64998216 64998215時不轉 2017/11/17
						if ("64998216".equals(dmmRepositoryHistoryDTO.getSerialNumber()) || "64998215".equals(dmmRepositoryHistoryDTO.getSerialNumber())) {
							continue;
						}
						dmmRepositoryHistory = (DmmRepositoryHistory) transformer.transform(dmmRepositoryHistoryDTO, new DmmRepositoryHistory());
						//設備品項id
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getAssetTypeId())) {
							//Bug #2941
							if ("EDC".equals(dmmRepositoryHistoryDTO.getAssetCategory())) {
								assetTypeId = this.getValueByName(dmmRepositoryHistoryDTO.getAssetTypeId(), "edcCategory");
							} else if ("Related_Products".equals(dmmRepositoryHistoryDTO.getAssetCategory())) {
								assetTypeId = this.getValueByName(dmmRepositoryHistoryDTO.getAssetTypeId(), "RelatedProductsCategory");
							}
							//assetTypeId = this.getValueByName(dmmRepositoryHistoryDTO.getAssetTypeId(), "assetType");
							if (StringUtils.hasText(assetTypeId)) {
								dmmRepositoryHistory.setAssetTypeId(assetTypeId);
							} else {
								dmmRepositoryHistory.setAssetTypeId(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getAssetTypeId() + "assetTypeId 無對應資料</br>");
							}
						}
						//倉庫據點id
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getWarehouseId())) {
							warehouseId = this.getValueByName(dmmRepositoryHistoryDTO.getWarehouseId(), WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue());
							if (StringUtils.hasText(warehouseId)) {
								dmmRepositoryHistory.setWarehouseId(warehouseId);
							} else {
								dmmRepositoryHistory.setWarehouseId(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getWarehouseId() + "--warehouseId 無對應資料</br>");
							}
						}
						
						//合約id
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getContractId())) {
							contractId = this.getValueByName(dmmRepositoryHistoryDTO.getContractId(), BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue());
							if (StringUtils.hasText(contractId)) {
								dmmRepositoryHistory.setContractId(contractId);
							} else {
								//判斷是否為 PRE2015-01
								if ("PRE2015-01".equals(dmmRepositoryHistoryDTO.getContractId()) && !flag) {
									contractId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_CONTRACT);
									synchronized (this) {
										if (!flag && !StringUtils.hasText(this.getValueByName(dmmRepositoryHistoryDTO.getContractId(), BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue()))) {
											bimContract = new BimContract(contractId);
											bimContract.setDeleted("Y");
											bimContract.setContractCode(dmmRepositoryHistoryDTO.getContractId());
											this.contractList.add(new Parameter(dmmRepositoryHistoryDTO.getContractId(), contractId));
											flag = true;
											contractDAO.getDaoSupport().save(bimContract);
											dmmRepositoryHistory.setContractId(contractId);
											LOGGER.debug(dmmRepositoryHistoryDTO.getContractId() + "--contractId 無對應資料,新增帳號</br>");
										}
									}
								} else {
									dmmRepositoryHistory.setContractId(null);
									LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
									LOGGER.debug(dmmRepositoryHistoryDTO.getContractId() + "--contractId 無對應資料</br>");
								}
							}
						}
						//舊裝機地址 Task #2588 2017/10/17
						/*if (StringUtils.hasText(dmmRepositoryHistoryDTO.getOldMerchantHeader())) {
						//	dmmRepositoryHistory.setOldMerchantAddress(dmmRepositoryHistory.getInstalledAdressLocation() + dmmRepositoryHistory.getInstalledAdress());
						}*/
						// 故障組件 
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getFaultComponent())) {
							faultComponent = this.getValueByName(dmmRepositoryHistoryDTO.getFaultComponent(), IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode());
							if (StringUtils.hasText(faultComponent)) {
								dmmRepositoryHistory.setFaultComponent(faultComponent);
							} else {
								dmmRepositoryHistory.setFaultComponent(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getFaultComponent() + "--faultComponent 無對應資料");
							}
						}
						
						// 故障說明 
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getFaultDescription())) {
							faultDescription = this.getValueByName(dmmRepositoryHistoryDTO.getFaultDescription(), IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode());
							if (StringUtils.hasText(faultDescription)) {
								dmmRepositoryHistory.setFaultDescription(faultDescription);
							} else {
								dmmRepositoryHistory.setFaultDescription(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getFaultDescription() + "--faultDescription 無對應資料</br>");
							}
						}
						// 裝機地址 市縣區域
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getInstalledAdressLocation())) {
							
							installedAdressLocation = this.getValueByName(dmmRepositoryHistoryDTO.getInstalledAdressLocation(), IATOMS_PARAM_TYPE.LOCATION.getCode());
							if (StringUtils.hasText(installedAdressLocation)) {
								dmmRepositoryHistory.setInstalledAdressLocation(installedAdressLocation);
							} else {
								dmmRepositoryHistory.setInstalledAdressLocation(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getInstalledAdressLocation() + "--installedAdressLocation 無對應資料</br>");
							}
						}
						
						//資產Owner
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getAssetOwner())) {
							companyId = this.getValueByName(dmmRepositoryHistoryDTO.getAssetOwner(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
							if (StringUtils.hasText(companyId)) {
								dmmRepositoryHistory.setAssetOwner(companyId);
							} else {
								dmmRepositoryHistory.setAssetOwner(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getAssetOwner() + "--assetOwner 無對應資料</br>");
							}
						}
						//使用人
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getAssetUser())) {
							userCompany = this.getValueByName(dmmRepositoryHistoryDTO.getAssetUser(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
							if (StringUtils.hasText(userCompany)) {
								dmmRepositoryHistory.setAssetUser(userCompany);
							} else {
								dmmRepositoryHistory.setAssetUser(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getAssetUser() + "--assetUser 無對應資料</br>");
							}
						}
						
						//特店  Task #2588
						// if AssetUser is null or AssetUser='' then AssetOwner 2017/10/23
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getOldMerchantCode()) ){
							dmmRepositoryHistory.setMerchantId(null);
							if (StringUtils.hasText(dmmRepositoryHistory.getAssetUser())
									|| StringUtils.hasText(dmmRepositoryHistory.getAssetOwner())) {
								owner = (StringUtils.hasText(dmmRepositoryHistory.getAssetUser()) ? dmmRepositoryHistory.getAssetUser() : dmmRepositoryHistory.getAssetOwner());
								merId = this.getValueByIdAndName(dmmRepositoryHistoryDTO.getOldMerchantCode(), owner, MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
								if (StringUtils.hasText(merId)) {
									dmmRepositoryHistory.setMerchantId(merId);
								} else {
									synchronized (this) {
										merId = this.getValueByIdAndName(dmmRepositoryHistoryDTO.getOldMerchantCode(), owner, MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue());
										if (StringUtils.hasText(merId)) {
											dmmRepositoryHistory.setMerchantId(merId);
										} else {
											parameter = new Parameter(dmmRepositoryHistoryDTO.getOldMerchantCode(), merhantId+"-"+k);
											if (this.merchantMap.containsKey(owner)) {
												this.merchantMap.get(owner).add(parameter);
											} else {
												this.merchantMap.put(owner, new ArrayList<Parameter>());
												this.merchantMap.get(owner).add(parameter);
											}
											this.merchantHeaderMap.put(merhantId + "-" + k, new ArrayList<Parameter>());
											merFlag = true;
										}
									}
									if (merFlag) {
										LOGGER.debug(dmmRepositoryHistoryDTO.getOldMerchantCode() + "--merId 無對應資料 新增特點 -- </br>");
										merchant = new BimMerchant();
										merchant.setMerchantId(merhantId+"-"+k);
										merchant.setCompanyId(owner);
										merchant.setMerchantCode(dmmRepositoryHistoryDTO.getOldMerchantCode());
										merchant.setDeleted("Y");
										merchant.setName(dmmRepositoryHistoryDTO.getOldMerchantName());
										this.merchantDAO.getDaoSupport().save(merchant);
										LOGGER.debug(merchant.getMerchantCode() + "," + merchant.getMerchantId() + "--merchantId new paramter </br>");
										dmmRepositoryHistory.setMerchantId(merhantId+"-"+k);
										merchant = null;
										merFlag = false;
										k++;
									}
								}
							}
						}
						
						//特店  表頭 Task #2588
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getOldMerchantHeader())){
							dmmRepositoryHistory.setMerchantHeaderId(null);
							if (StringUtils.hasText(dmmRepositoryHistory.getMerchantId())) {
								merId = this.getValueByIdAndName(dmmRepositoryHistoryDTO.getOldMerchantHeader(), dmmRepositoryHistory.getMerchantId(), BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
								if (StringUtils.hasText(merId)) {
									dmmRepositoryHistory.setMerchantHeaderId(merId);
								} else {
									synchronized (this) {
										merId = this.getValueByIdAndName(dmmRepositoryHistoryDTO.getOldMerchantHeader(), dmmRepositoryHistory.getMerchantId(), BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue());
										if (StringUtils.hasText(merId)) {
											dmmRepositoryHistory.setMerchantHeaderId(merId);
										} else {
											parameter = new Parameter(dmmRepositoryHistory.getMerchantId(), headerId + "-" + k);
											if (this.merchantHeaderMap.containsKey(dmmRepositoryHistory.getMerchantId())) {
												this.merchantHeaderMap.get(dmmRepositoryHistory.getMerchantId()).add(parameter);
											} else {
												this.merchantHeaderMap.put(dmmRepositoryHistory.getMerchantId(), new ArrayList<Parameter>());
												this.merchantHeaderMap.get(dmmRepositoryHistory.getMerchantId()).add(parameter);
											}
											merFlag = true;
										}
									}
									if (merFlag) {
										LOGGER.debug(dmmRepositoryHistoryDTO.getOldMerchantHeader() + "--merchantHeaderId 無對應資料 新增 - 特點 表頭 -- </br>");
										merchantHeader = new BimMerchantHeader();
										merchantHeader.setMerchantHeaderId(headerId + "-" + k);
										merchantHeader.setMerchantId(dmmRepositoryHistory.getMerchantId());
										merchantHeader.setHeaderName(dmmRepositoryHistoryDTO.getOldMerchantHeader());
										merchantHeader.setIsVip("N");
										merchantHeader.setDeleted("Y");
										this.merchantHeaderDAO.getDaoSupport().save(merchantHeader);
										LOGGER.debug(merchantHeader.getMerchantId() + "," + merchantHeader.getMerchantHeaderId() + "--merchantHeaderId new paramter </br>");
										dmmRepositoryHistory.setMerchantHeaderId(headerId + "-" + k);
										merchantHeader = null;
										merFlag = false;
										k++;
									}
								}
							}
						}
						
						//維護廠商
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getMaintainCompany())) {
							maintainCompany = this.getValueByName(dmmRepositoryHistoryDTO.getMaintainCompany(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
							if (StringUtils.hasText(maintainCompany)) {
								dmmRepositoryHistory.setMaintainCompany(maintainCompany);
							} else {
								dmmRepositoryHistory.setMaintainCompany(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getMaintainCompany() + "--maintainCompany 無對應資料</br>");
							}
						}
						//維修廠商
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getRepairVendor())) {
							companyId = this.getValueByName(dmmRepositoryHistoryDTO.getRepairVendor(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
							if (StringUtils.hasText(companyId)) {
								dmmRepositoryHistory.setRepairVendor(companyId);
							} else {
								dmmRepositoryHistory.setRepairVendor(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getRepairVendor() + "--repairVendor 無對應資料</br>");
							}
						}
						//原廠
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getRepairCompany())) {
							companyId = this.getValueByName(dmmRepositoryHistoryDTO.getRepairCompany(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
							if (StringUtils.hasText(companyId)) {
								dmmRepositoryHistory.setRepairCompany(companyId);
							} else {
								dmmRepositoryHistory.setRepairCompany(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getRepairCompany() + "--repairCompany 無對應資料</br>");
							}
						}
						
						
						//領用人
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getCarrier())) {
							//synchronized (this) {
							 	//Task #2866 2017/11/20
								carrier = this.getValueByName(dmmRepositoryHistoryDTO.getCarrier().length() > 2 ? dmmRepositoryHistoryDTO.getCarrier().substring(0, 3) : dmmRepositoryHistoryDTO.getCarrier(), AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
								if (StringUtils.hasText(carrier)) {
									dmmRepositoryHistory.setCarrier(carrier);
								} else {
									dmmRepositoryHistory.setCarrier(null);
									LOGGER.debug(dmmRepositoryHistoryDTO.getCarrier() + "--carrier 無對應資料 </br>");
									/*admUser = new AdmUser(userId + "-" + k, " ", "N");
									dmmRepositoryHistory.setCarrier(admUser.getUserId());
									admUser.setId(admUser.getUserId());
									admUser.setCname(dmmRepositoryHistoryDTO.getCarrier());
									k ++;
									this.userList.add(new Parameter(admUser.getCname(), admUser.getUserId()));
									this.admUserDAO.getDaoSupport().save(admUser);
									// this.resultMsg.append("設備庫存資料轉入失敗").append("</br>");
									LOGGER.debug(dmmRepositoryHistoryDTO.getCarrier() + "--carrier 無對應資料,新增帳號</br>");*/
								}
							//}
						}
						//借用人
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getBorrower())) {
							//synchronized(this) {
								//Task #2866 2017/11/20
								borrower = this.getValueByName(dmmRepositoryHistoryDTO.getBorrower().length() > 2 ? dmmRepositoryHistoryDTO.getBorrower().substring(0, 3) : dmmRepositoryHistoryDTO.getBorrower(), AdmUserDTO.ATTRIBUTE.USER_ID.getValue());
								if (StringUtils.hasText(borrower)) {
									dmmRepositoryHistory.setBorrower(borrower);
								} else {
									dmmRepositoryHistory.setBorrower(null);
									LOGGER.debug(dmmRepositoryHistoryDTO.getBorrower() + "--borrower 無對應資料 </br>");
									/*admUser = new AdmUser(userId + "-" + k, " ", "N");
									dmmRepositoryHistory.setBorrower(admUser.getUserId());
									admUser.setId(admUser.getUserId());
									admUser.setCname(dmmRepositoryHistoryDTO.getBorrower());
									k ++;
									this.userList.add(new Parameter(admUser.getCname(), admUser.getUserId()));
									this.admUserDAO.getDaoSupport().save(admUser);
									//this.resultMsg.append("設備庫存資料轉入失敗").append("</br>");
									LOGGER.debug(dmmRepositoryHistoryDTO.getBorrower() + "--borrower 無對應資料,新增帳號</br>");*/
								}
					       // }
						}
						//報廢原因
						if (StringUtils.hasText(dmmRepositoryHistoryDTO.getRetireReasonCode())) {
							retireReasonCode = this.getValueByName(dmmRepositoryHistoryDTO.getRetireReasonCode(), IATOMS_PARAM_TYPE.RETIRE_REASON.getCode());
							if (StringUtils.hasText(retireReasonCode)) {
								dmmRepositoryHistory.setRetireReasonCode(retireReasonCode);
							} else {
								dmmRepositoryHistory.setRetireReasonCode(null);
								LOGGER.debug("設備庫存歷史資料轉入失敗</br>");
								LOGGER.debug(dmmRepositoryHistoryDTO.getRetireReasonCode() + "--retireReasonCode 無對應資料</br>");
							}
						}
						LOGGER.debug("dmmRepositoryHistory -------> " + dmmRepositoryHistory.getAssetId()+","+dmmRepositoryHistory.getHistoryId());

						//保存庫存主檔
						this.dmmRepositoryHistDAO.getDaoSupport().save(dmmRepositoryHistory);
						//保存設備庫存歷史故障現象
						if (StringUtils.hasText(dmmRepositoryHistory.getFaultComponent())) {
							repositoryFaultCom = new DmmRepositoryHistoryComm();
							repositoryFaultComId = new DmmRepositoryHistoryCommId(dmmRepositoryHistory.getHistoryId(), dmmRepositoryHistory.getFaultComponent());
							repositoryFaultCom.setId(repositoryFaultComId);
							this.dmmRepositoryHistoryCommDAO.getDaoSupport().save(repositoryFaultCom);
						}
						//保存設備庫存歷史故障說明
						if (StringUtils.hasText(dmmRepositoryHistory.getFaultDescription())) {
							repositoryFaultDesc = new DmmRepositoryHistoryDesc();
							repositoryFaultDescId = new DmmRepositoryHistoryDescId(dmmRepositoryHistory.getHistoryId(), dmmRepositoryHistory.getFaultDescription());
							repositoryFaultDesc.setId(repositoryFaultDescId);
							this.dmmRepositoryHistoryDescDAO.getDaoSupport().save(repositoryFaultDesc);
						}
						LOGGER.debug("dmmRepositoryHistory --" + dmmRepositoryHistory.getAssetId()+","+dmmRepositoryHistory.getHistoryId());
					
					} else {
						LOGGER.debug(this.resultMsg);
						formDTO.setSaveFlag(false);
						LOGGER.debug("dmmRepositoryHistory -- one thread is error ..." + dmmRepositoryHistoryDTO.getAssetId()+","+dmmRepositoryHistoryDTO.getHistoryId());
						return;
					}
				}
				formDTO.setSaveFlag(true);
				LOGGER.debug(this.resultMsg);
				LOGGER.debug("dmmRepositoryHistory -- one thread end" );

			}
		} catch (DataAccessException e) {
			this.setErrorFlag("Y");
			formDTO.setSaveFlag(false);
			LOGGER.error(".dmmRepositoryHistory() DataAccess Exception:" + e, e);
			LOGGER.debug(this.resultMsg);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			this.setErrorFlag("Y");
			formDTO.setSaveFlag(false);
			LOGGER.error(".dmmRepositoryHistory() Service Exception--->" + e, e);
			LOGGER.debug(this.resultMsg);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		} 
		
}  

	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferHistoryRepository(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferHistoryRepository(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			// 刪除所有庫存歷史資料
			this.dmmRepositoryHistDAO.deleteTransferData();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			Map map = new HashMap();
			// 若無設備信息則去DB查詢
			if(CollectionUtils.isEmpty(this.edcCategoryList) || CollectionUtils.isEmpty(this.RelatedProductsCategoryList)){
				this.edcCategoryList = this.assetTypeDAO.listAssetByNameAndType(null, true, false);
				this.RelatedProductsCategoryList = this.assetTypeDAO.listAssetByNameAndType(null, false, true);
				if(CollectionUtils.isEmpty(this.edcCategoryList) && CollectionUtils.isEmpty(this.RelatedProductsCategoryList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				} else if (CollectionUtils.isEmpty(this.edcCategoryList)) {
					this.edcCategoryList = new ArrayList<Parameter>();
				} else if (CollectionUtils.isEmpty(this.RelatedProductsCategoryList)) {
					this.RelatedProductsCategoryList = new ArrayList<Parameter>();
				} 
			}
			
			// 若無倉庫據點信息則去DB查詢
			if(CollectionUtils.isEmpty(this.warehouseList)){
				this.warehouseList = this.warehouseDAO.listWarehouseByName(null);
				if(CollectionUtils.isEmpty(this.warehouseList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入倉庫據點資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			if (CollectionUtils.isEmpty(this.getContractList())) {
				this.setContractList(this.contractDAO.getContractByCode(null));
				if(CollectionUtils.isEmpty(this.getContractList())){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入合約資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
				if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無帳號則去DB查詢
			if(CollectionUtils.isEmpty(this.userList)){
				this.userList = this.admUserDAO.getUserAll();
				if(CollectionUtils.isEmpty(this.userList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入使用者帳號資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//Task #2584
			if(CollectionUtils.isEmpty(this.merchantMap)){
				this.merchantMap = new HashMap<String, List<Parameter>>();
				List<Parameter> merchantList = null;
				if (CollectionUtils.isEmpty(this.companyCodeList)) {
					this.companyCodeList = this.companyDAO.getCompanyList(true);
				}
				for (Parameter company : companyCodeList) {
					if (!merchantMap.containsKey(company.getValue().toString())) {
						merchantList = this.merchantDAO.getMerchantsByCodeAndCompamyId(null, company.getValue().toString());
						if (!CollectionUtils.isEmpty(merchantList)) {
							merchantMap.put(company.getValue().toString(), merchantList);
						}
					}
				}
				if(CollectionUtils.isEmpty(this.merchantMap)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入客戶特店基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//Task #2584
			if(CollectionUtils.isEmpty(this.merchantHeaderMap)){
				this.merchantHeaderMap = new HashMap<String, List<Parameter>>();
				List<Parameter> merchantHeadertList = null;
				for (String key : merchantMap.keySet()) {
					for (Parameter parameter : merchantMap.get(key)) {
						if (!merchantHeaderMap.containsKey(parameter.getValue()!=" "?String.valueOf(parameter.getValue()):" ")) {
							merchantHeadertList = this.merchantHeaderDAO.getMerchantHeadersBy(parameter.getValue()!=" "?String.valueOf(parameter.getValue()):" ", null);
							if (!CollectionUtils.isEmpty(merchantHeadertList)) {
								merchantHeaderMap.put(parameter.getValue()!=" "?String.valueOf(parameter.getValue()):" ", merchantHeadertList);
							}
						}
					}
				}
				if(CollectionUtils.isEmpty(this.merchantHeaderMap)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入特店表頭基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//user_id cname
			
			if (((!CollectionUtils.isEmpty(this.edcCategoryList)) || (!CollectionUtils.isEmpty(this.RelatedProductsCategoryList)))
					&& !CollectionUtils.isEmpty(warehouseList)
					&& !CollectionUtils.isEmpty(contractList)
					&& !CollectionUtils.isEmpty(companyCodeList)
					&& !CollectionUtils.isEmpty(userList)
					&& !CollectionUtils.isEmpty(this.merchantMap)
					&& !CollectionUtils.isEmpty(this.merchantHeaderMap)) {
				// 清空返回消息值
				this.resultMsg = new StringBuilder();
				
				//if(isNoData){
					Transformer transformer = new SimpleDtoDmoTransformer();
					// 設備庫存集合
					List<DmmRepositoryHistoryDTO> dmmRepositoryHistoryDTOs = this.oldDataTransferDAO.listHistoryRepository();
					if(!CollectionUtils.isEmpty(dmmRepositoryHistoryDTOs)){
						OldDataTransferFormDTO formDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
						formDTO.setDmmRepositoryHistoryDTOs(dmmRepositoryHistoryDTOs);
						sessionContext.setResponseResult(formDTO);
						this.resultMsg = this.resultMsg.append("設備庫存歷史轉移成功");
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
						map.put("resultMsg", this.resultMsg);
						sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
						sessionContext.setAttribute(IAtomsConstants.COUNT, dmmRepositoryHistoryDTOs.size());
						this.errorFlag = "N";
					} else {
						this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無設備歷史庫存資料").append("</br>");
						map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
						map.put("resultMsg", this.resultMsg);
						sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
						return sessionContext;
					}
					return sessionContext;
//				} else {
//					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存歷史資料已轉入，不能重復轉入").append("</br>");
//					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
//					map.put("resultMsg", this.resultMsg);
//					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
//					return sessionContext;
//				}
			}
			LOGGER.debug(this.resultMsg);
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".transferRepository() DataAccess Exception:" + e, e);
			LOGGER.debug(this.resultMsg);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferRepository() Service Exception--->" + e, e);
			LOGGER.debug(this.resultMsg);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:根據name與id獲取value
	 * @author amandawang
	 * @param initName：名稱
	 * @param initId:id
	 * @param initCode
	 * @throws ServiceException
	 * @return String：公司id
	 */
	private String getValueByIdAndName(String initName, String initId, String initCode) throws ServiceException {
		LOGGER.debug(".getValueByIdAndName()", "parameters : initName=" + initName);
		LOGGER.debug(".getValueByIdAndName()", "parameters : initId=" + initId);
		LOGGER.debug(".getValueByIdAndName()", "parameters : initCode=" + initCode);
		List<Parameter> parameterList = null;
		//部門id
		if (BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue().equals(initCode)) {
			if(CollectionUtils.isEmpty(this.departmentMap)){
				this.departmentMap = new HashMap<String, List<Parameter>>();
				List<Parameter> departmentList = null;
				if (CollectionUtils.isEmpty(this.companyCodeList)) {
					this.companyCodeList = this.companyDAO.getCompanyList(true);
				}
				for (Parameter company : companyCodeList) {
					if (!departmentMap.containsKey(company.getValue().toString())) {
						departmentList = this.departmentDAO.getDepts(company.getValue().toString());
						if (!CollectionUtils.isEmpty(departmentList)) {
							departmentMap.put(company.getValue().toString(), departmentList);
						}
					}
				}
			}
			parameterList = departmentMap.get(initId);
			//特店id
		} else if (MerchantDTO.ATTRIBUTE.MERCHANT_ID.getValue().equals(initCode)) {
			if(CollectionUtils.isEmpty(this.merchantMap)){
				this.merchantMap = new HashMap<String, List<Parameter>>();
				List<Parameter> merchantList = null;
				if (CollectionUtils.isEmpty(this.companyCodeList)) {
					this.companyCodeList = this.companyDAO.getCompanyList(true);
				}
				for (Parameter company : companyCodeList) {
					if (!merchantMap.containsKey(company.getValue().toString())) {
						merchantList = this.merchantDAO.getMerchantsByCodeAndCompamyId(null, company.getValue().toString());
						if (!CollectionUtils.isEmpty(merchantList)) {
							merchantMap.put(company.getValue().toString(), merchantList);
						}
					}
				}
			}
			parameterList = merchantMap.get(initId);
			//特店表頭id
		} else if (BimMerchantHeaderDTO.ATTRIBUTE.MERCHANT_HEADER_ID.getValue().equals(initCode)) {
			if(CollectionUtils.isEmpty(this.merchantHeaderMap)){
				this.merchantHeaderMap = new HashMap<String, List<Parameter>>();
				List<Parameter> merchantList = null;
				//
				if(CollectionUtils.isEmpty(this.merchantMap)){
					this.merchantMap = new HashMap<String, List<Parameter>>();
					if (CollectionUtils.isEmpty(this.companyCodeList)) {
						this.companyCodeList = this.companyDAO.getCompanyList(true);
					}
					for (Parameter company : companyCodeList) {
						if (!merchantMap.containsKey(company.getValue().toString())) {
							merchantList = this.merchantDAO.getMerchantsByCodeAndCompamyId(null, company.getValue().toString());
							if (!CollectionUtils.isEmpty(merchantList)) {
								merchantMap.put(company.getValue().toString(), merchantList);
							}
						}
					}
				}
				List<Parameter> merchantHeadertList = null;
				//
				for (String key : merchantMap.keySet()) {
					for (Parameter parameter : merchantMap.get(key)) {
						if (!merchantHeaderMap.containsKey(parameter.getValue().toString())) {
							merchantHeadertList = this.merchantHeaderDAO.getMerchantHeadersBy(parameter.getValue().toString(), null);
							if (!CollectionUtils.isEmpty(merchantHeadertList)) {
								merchantHeaderMap.put(parameter.getValue().toString(), merchantHeadertList);
							}
						}
					}
				}
			}
			parameterList = merchantHeaderMap.get(initId);
		}
		
		LOGGER.debug(".getValueByIdAndName()", "parameters : parameterList=" + parameterList);
		String value = null;
		if(StringUtils.hasText(initName) && StringUtils.hasText(initId) && !CollectionUtils.isEmpty(parameterList)){
			for (Parameter param : parameterList){
				if((param.getName()).equals(initName)){
					value = (String) param.getValue();
					break;
				}
			}
		}
		LOGGER.debug(".getValueByIdAndName()", "parameters : value=" + value);
		
		if(StringUtils.hasText(initName) && !StringUtils.hasText(value)){
			LOGGER.error(".getValueByIdAndName() haveNoMatch", "initCode:" + initCode, "initName:" + initName);
		}
		return value;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferCaseHandleInfo(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferCaseHandleInfo(SessionContext sessionContext) throws ServiceException {
		OldDataTransferFormDTO formDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
		try {
			Map map = new HashMap();
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 轉入案件編號
			String transferCaseId = oldDataTransferFormDTO.getTransferCaseId();
			
			// Task #3066 只轉入特定案件編號案件
			// 刪除最新案件資料信息
		//	this.srmCaseHandleInfoDAO.deleteTransferData();
			
			Transformer transformer = new SimpleDtoDmoTransformer();
			// 若無設備信息則去DB查詢
			if(CollectionUtils.isEmpty(this.edcTypeList)){
				this.edcTypeList = this.assetTypeDAO.listAssetByName(null, false, true);
				if(CollectionUtils.isEmpty(this.edcTypeList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無周邊設備信息則去DB查詢
			if(CollectionUtils.isEmpty(this.peripheralsList)){
				this.peripheralsList = this.assetTypeDAO.listPeripherals();
				if(CollectionUtils.isEmpty(this.peripheralsList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無合約信息則去DB查詢
			if (CollectionUtils.isEmpty(this.contractList)) {
				this.contractList = this.contractDAO.getContractByCode(null);
				if(CollectionUtils.isEmpty(this.contractList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入合约维护資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
				if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無部門信息則去DB查詢 依公司查找放入map中
			if(CollectionUtils.isEmpty(this.departmentMap)){
				this.departmentMap = new HashMap<String, List<Parameter>>();
				List<Parameter> departmentList = null;
				if (!CollectionUtils.isEmpty(this.companyCodeList)) {
					for (Parameter company : companyCodeList) {
						if (!departmentMap.containsKey(company.getValue().toString())) {
							departmentList = this.departmentDAO.getDepts(company.getValue().toString());
							if (!CollectionUtils.isEmpty(departmentList)) {
								departmentMap.put(company.getValue().toString(), departmentList);
							}
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//若無程式版本信息去DB中查詢
			if(CollectionUtils.isEmpty(this.applicationMap)){
				this.applicationMap = new HashMap<String, List<ApplicationDTO>>();
				List<ApplicationDTO> allApplicationDTOs = this.applicationDAO.getApplicationList();
				List<ApplicationDTO> tempApplicationList = null;
				if (!CollectionUtils.isEmpty(allApplicationDTOs)) {
					for(ApplicationDTO tempApplicationDTO : allApplicationDTOs){
						if(StringUtils.hasText(tempApplicationDTO.getAssetTypeId())){
							if(this.applicationMap.containsKey(tempApplicationDTO.getAssetTypeId())){
								tempApplicationList = this.applicationMap.get(tempApplicationDTO.getAssetTypeId());
							} else {
								tempApplicationList = new ArrayList<ApplicationDTO>();
							}
							tempApplicationList.add(tempApplicationDTO);
							this.applicationMap.put(tempApplicationDTO.getAssetTypeId(), tempApplicationList);
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入程式版本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 案件信息集合
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = this.oldDataTransferDAO.listCaseHandleInfo(transferCaseId);
			// 案件歷程資料集合
			List<SrmCaseTransactionDTO> srmCaseTransactionDTOs = this.oldDataTransferDAO.listCaseTransactions(transferCaseId);
			// 案件附加資料集合
			List<SrmCaseAttFileDTO> srmCaseAttFileDTOs = this.oldDataTransferDAO.listCaseFiles(transferCaseId);
			if(!CollectionUtils.isEmpty(srmCaseHandleInfoDTOs)){
				// 按照案件編號得到歷程信息
				Map<String, List<SrmCaseTransactionDTO>> caseTransactionMap = new HashMap<String, List<SrmCaseTransactionDTO>>();
				// 存放每筆案件歷程信息集合
				List<SrmCaseTransactionDTO> tempCaseTransactionList = null;
				if(!CollectionUtils.isEmpty(srmCaseTransactionDTOs)){
					for(SrmCaseTransactionDTO srmCaseTransactionDTO : srmCaseTransactionDTOs){
						if(caseTransactionMap.containsKey(srmCaseTransactionDTO.getCaseId())){
							tempCaseTransactionList = caseTransactionMap.get(srmCaseTransactionDTO.getCaseId());
						} else {
							tempCaseTransactionList = new ArrayList<SrmCaseTransactionDTO>();
						}
						tempCaseTransactionList.add(srmCaseTransactionDTO);
						caseTransactionMap.put(srmCaseTransactionDTO.getCaseId(), tempCaseTransactionList);
					}
				}
				// 按照案件編號得到歷程信息
				Map<String, List<SrmCaseAttFileDTO>> caseAttFileMap = new HashMap<String, List<SrmCaseAttFileDTO>>();
				// 存放每筆案件歷程信息集合
				List<SrmCaseAttFileDTO> tempCaseAttFileList = null;
				if(!CollectionUtils.isEmpty(srmCaseAttFileDTOs)){
					for(SrmCaseAttFileDTO srmCaseAttFileDTO : srmCaseAttFileDTOs){
						if(caseTransactionMap.containsKey(srmCaseAttFileDTO.getCaseId())){
							tempCaseAttFileList = caseAttFileMap.get(srmCaseAttFileDTO.getCaseId());
							if(tempCaseAttFileList == null){
								tempCaseAttFileList = new ArrayList<SrmCaseAttFileDTO>();
							}
						} else {
							tempCaseAttFileList = new ArrayList<SrmCaseAttFileDTO>();
						}
						tempCaseAttFileList.add(srmCaseAttFileDTO);
						caseAttFileMap.put(srmCaseAttFileDTO.getCaseId(), tempCaseAttFileList);
					}
				}
				// 案件主檔
				formDTO.setSrmCaseHandleInfoDTOs(srmCaseHandleInfoDTOs);
				// 案件與歷程關系
				formDTO.setCaseTransactionMap(caseTransactionMap);
				// 案件與附加檔案關系
				formDTO.setCaseAttFileMap(caseAttFileMap);
				// 設置formdto值
				sessionContext.setResponseResult(formDTO);
				
				
				// 案件轉移筆數
				if(!CollectionUtils.isEmpty(srmCaseHandleInfoDTOs)){
					formDTO.setTransferCaseInfoNum(srmCaseHandleInfoDTOs.size());
				} else {
					formDTO.setTransferCaseInfoNum(0);
				}
				// 案件歷程轉移筆數
				if(!CollectionUtils.isEmpty(srmCaseTransactionDTOs)){
					formDTO.setTransferCaseTransactionsNum(srmCaseTransactionDTOs.size());
				} else {
					formDTO.setTransferCaseTransactionsNum(0);
				}
				// 案件附加資料轉移筆數
				if(!CollectionUtils.isEmpty(srmCaseAttFileDTOs)){
					formDTO.setTransferCaseFilesNum(srmCaseAttFileDTOs.size());
				} else {
					formDTO.setTransferCaseFilesNum(0);
				}
				this.resultMsg = this.resultMsg.append("案件資料轉入完成").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			} else {
				this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無處理中案件資料").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				return sessionContext;
			}
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".transferCaseHandleInfo() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferCaseHandleInfo() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:多線程處理案件數據轉換
	 * @author CrissZhang
	 * @param formDTO
	 * @return void
	 */
	public void saveCaseDataThread(OldDataTransferFormDTO formDTO) throws ServiceException{
		try {
			// 案件資料集合
			List<SrmCaseHandleInfoDTO> srmCaseHandleInfoDTOs = formDTO.getSrmCaseHandleInfoDTOs();
			// 案件與歷程信息集合
			Map<String, List<SrmCaseTransactionDTO>> caseTransactionMap = formDTO.getCaseTransactionMap();
			// 案件與附加資料集合
			Map<String, List<SrmCaseAttFileDTO>> caseAttFileMap  = formDTO.getCaseAttFileMap();
			Transformer transformer = new SimpleDtoDmoTransformer();
			int i = 0;
			// 案件信息
			SrmCaseHandleInfo srmCaseHandleInfo = null;
			SrmCaseTransactionParameter srmCaseTransactionParameter = null;
			SrmCaseAttFile srmCaseAttFile = null;
			SrmCaseTransaction srmCaseTransaction = null;
			String tempCustomerId = null;
			String tempCompanyId = null;
			String peripherals = null;
			String peripherals2 = null;
			String peripherals3 = null;
			
			// 存放每筆案件歷程信息集合
			List<SrmCaseTransactionDTO> tempCaseTransactionList = null;
			// 按照案件編號得到歷程信息
			Map<String, List<SrmCaseTransactionDTO>> singleCaseActionMap = null;
			// 
			List<SrmCaseTransactionDTO> tempCaseActionList = null;
			
			List<ApplicationDTO> tempApplicationList = null;
			for(SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : srmCaseHandleInfoDTOs){
				tempCaseTransactionList = null;
				peripherals = null;
				peripherals2 = null;
				peripherals3 = null;
				// 無案件類別 不保存
				if(!StringUtils.hasText(srmCaseHandleInfoDTO.getCaseCategory())){
					LOGGER.error(".saveCaseDataThread() save case error!!! caseCategory is null ", "caseId:" + srmCaseHandleInfoDTO.getCaseId());
					continue;
				}
				srmCaseHandleInfo = (SrmCaseHandleInfo) transformer.transform(srmCaseHandleInfoDTO, new SrmCaseHandleInfo());
				tempCustomerId = this.getValueByName(srmCaseHandleInfo.getCustomerId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
				// 客戶編號
				srmCaseHandleInfo.setCustomerId(tempCustomerId);
				// 合約ID
				srmCaseHandleInfo.setContractId(this.getValueByName(srmCaseHandleInfo.getContractId(), BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue()));
				tempCompanyId = this.getValueByName(srmCaseHandleInfo.getCompanyId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
				// 維護廠商
				srmCaseHandleInfo.setCompanyId(tempCompanyId);
				// 維護部門
				/*if (!IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(srmCaseHandleInfo.getDepartmentId())) {
					if(StringUtils.hasText(tempCompanyId)){
						if (!departmentMap.containsKey(tempCompanyId)) {
							srmCaseHandleInfo.setDepartmentId(null);
							LOGGER.error(".transferCaseHandleInfo() haveNoMatch", "departmentId:" + srmCaseHandleInfo.getDepartmentId());
						} else {
							srmCaseHandleInfo.setDepartmentId(getValueByIdAndName(srmCaseHandleInfo.getDepartmentId(), tempCompanyId,BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()));
						}
					} else {
						srmCaseHandleInfo.setDepartmentId(null);
						LOGGER.error(".transferCaseHandleInfo() haveNoMatch", "departmentId:" + srmCaseHandleInfo.getDepartmentId());
					}
				}*/
				if (!IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(srmCaseHandleInfo.getDepartmentId())) {
					if(StringUtils.hasText(tempCompanyId)){
						if (!departmentMap.containsKey(tempCompanyId)) {
							srmCaseHandleInfo.setDepartmentId(null);
							LOGGER.error(".saveCaseDataThread() company not exists dept", "departmentId:" + srmCaseHandleInfoDTO.getDepartmentId());
						} else {
							srmCaseHandleInfo.setDepartmentId(this.getValueByIdAndName(srmCaseHandleInfo.getDepartmentId(), tempCompanyId,BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()));
						}
					} else {
						srmCaseHandleInfo.setDepartmentId(null);
						LOGGER.error(".saveCaseDataThread() company is null", "departmentId:" + srmCaseHandleInfoDTO.getDepartmentId());
					}
				}
				// 裝機地址(縣市)
				srmCaseHandleInfo.setInstalledAdressLocation(this.getValueByName(srmCaseHandleInfo.getInstalledAdressLocation(),IATOMS_PARAM_TYPE.LOCATION.getCode()));
				// 刷卡機類型
				if(StringUtils.hasText(srmCaseHandleInfo.getEdcType()) && fomsAssetTypeMap.containsKey(srmCaseHandleInfo.getEdcType())){
					srmCaseHandleInfo.setEdcType(fomsAssetTypeMap.get(srmCaseHandleInfo.getEdcType()));
				}
				srmCaseHandleInfo.setEdcType(this.getValueByName(srmCaseHandleInfo.getEdcType(), "edcType"));
				// 軟體版本
				if(!CollectionUtils.isEmpty(applicationMap) && StringUtils.hasText(srmCaseHandleInfo.getEdcType())
						&& applicationMap.containsKey(srmCaseHandleInfo.getEdcType())
						&& StringUtils.hasText(srmCaseHandleInfo.getSoftwareVersion())
						&& StringUtils.hasText(srmCaseHandleInfo.getCustomerId())){
					tempApplicationList = applicationMap.get(srmCaseHandleInfo.getEdcType());
					if(!CollectionUtils.isEmpty(tempApplicationList)){
						for(ApplicationDTO tempApplication : tempApplicationList){
							if(srmCaseHandleInfo.getSoftwareVersion().equals(tempApplication.getVersion())
									&& srmCaseHandleInfo.getCustomerId().equals(tempApplication.getCustomerId())){
								srmCaseHandleInfo.setSoftwareVersion(tempApplication.getApplicationId());
								break;
							}
						}
					}
				}
				// 週邊設備
			//	srmCaseHandleInfo.setPeripherals(this.getValueByName("R50", "Related_Products"));
				// Task #2597 a.R50 改為a.R50 as S300Q
				if(String.valueOf(1).equals(srmCaseHandleInfo.getPeripherals())){
					srmCaseHandleInfo.setPeripherals(this.getValueByName("S300Q", "Related_Products"));
				} else {
					srmCaseHandleInfo.setPeripherals(null);
				}
				// 週邊設備2
				if(String.valueOf(1).equals(srmCaseHandleInfo.getPeripherals2())){
					srmCaseHandleInfo.setPeripherals2(this.getValueByName("SP20", "Related_Products"));
				} else {
					srmCaseHandleInfo.setPeripherals2(null);
				}
				// 週邊設備3
				if(String.valueOf(1).equals(srmCaseHandleInfo.getPeripherals3())){
					srmCaseHandleInfo.setPeripherals3(this.getValueByName("S200", "Related_Products"));
				} else {
					srmCaseHandleInfo.setPeripherals3(null);
				}
				// Task #2750 上述欄位依序放入，週邊設備、週邊設備2、週邊設備3
				// 若周邊設備1有值賦值至臨時變量周邊設備1
				if(StringUtils.hasText(srmCaseHandleInfo.getPeripherals())){
					peripherals = srmCaseHandleInfo.getPeripherals();
					// 若周邊設備2有值賦值至臨時變量周邊設備2、若周邊設備3有值賦值至臨時變量周邊設備3
					if(StringUtils.hasText(srmCaseHandleInfo.getPeripherals2())){
						peripherals2 = srmCaseHandleInfo.getPeripherals2();
						peripherals3 = srmCaseHandleInfo.getPeripherals3();
					// 若周邊設備2無值、周邊設備3有值賦值至臨時變量周邊設備2
					} else {
						peripherals2 = srmCaseHandleInfo.getPeripherals3();
					} 
				} else {
					// 若周邊設備1無值、周邊設備2有值賦值至臨時變量周邊設備1
					if(StringUtils.hasText(srmCaseHandleInfo.getPeripherals2())){
						peripherals = srmCaseHandleInfo.getPeripherals2();
						peripherals2 = srmCaseHandleInfo.getPeripherals3();
					// 若周邊設備1無值、周邊設備2無值、周邊設備3有值賦值至臨時變量周邊設備1
					} else {
						peripherals = srmCaseHandleInfo.getPeripherals3();
					} 
				}
				// 週邊設備
				srmCaseHandleInfo.setPeripherals(peripherals);
				// 週邊設備2
				srmCaseHandleInfo.setPeripherals2(peripherals2);
				// 週邊設備3
				srmCaseHandleInfo.setPeripherals3(peripherals3);
				
				// 聯絡地址(縣市)
				srmCaseHandleInfo.setContactAddressLocation(this.getValueByName(srmCaseHandleInfo.getContactAddressLocation(),IATOMS_PARAM_TYPE.LOCATION.getCode()));
				// 派工部門代碼
				srmCaseHandleInfo.setDispatchDeptId(srmCaseHandleInfo.getDepartmentId());
//				if (!IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(srmCaseHandleInfo.getDispatchDeptId())) {
//					if(StringUtils.hasText(tempCompanyId)){
//						if (!departmentMap.containsKey(tempCompanyId)) {
//							srmCaseHandleInfo.setDispatchDeptId(null);
//							LOGGER.error(".transferCaseHandleInfo() haveNoMatch", "dispatchDeptId:" + srmCaseHandleInfo.getDispatchDeptId());
//						} else {
//							srmCaseHandleInfo.setDispatchDeptId(this.getValueByIdAndName(srmCaseHandleInfo.getDispatchDeptId(), tempCompanyId,BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()));
//						}
//					} else {
//						srmCaseHandleInfo.setDispatchDeptId(null);
//						LOGGER.error(".transferCaseHandleInfo() haveNoMatch", "dispatchDeptId:" + srmCaseHandleInfo.getDispatchDeptId());
//					}
//				}
				
				// 案件歷程資料
				if(!CollectionUtils.isEmpty(caseTransactionMap) && StringUtils.hasText(srmCaseHandleInfo.getCaseId())
						&& caseTransactionMap.containsKey(srmCaseHandleInfo.getCaseId())){
					tempCaseTransactionList = caseTransactionMap.get(srmCaseHandleInfo.getCaseId());
					// 案件歷程描述
					srmCaseHandleInfo = this.getCaseDescription(tempCaseTransactionList, srmCaseHandleInfo);
				}
				
				this.srmCaseHandleInfoDAO.getDaoSupport().save(srmCaseHandleInfo);
				
				// 案件通訊模式維護檔保存
				if(StringUtils.hasText(srmCaseHandleInfo.getConnectionType())){
					i = 0;
					SrmCaseCommMode srmCaseCommMode = null;
					for(String tempId : StringUtils.toList(srmCaseHandleInfo.getConnectionType(), IAtomsConstants.MARK_SEPARATOR)){
						i++;
						srmCaseCommMode = new SrmCaseCommMode();
						srmCaseCommMode.setCaseId(srmCaseHandleInfo.getCaseId());
						srmCaseCommMode.setId(srmCaseHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						srmCaseCommMode.setCommModeId(tempId);
						this.srmCaseCommModeDAO.getDaoSupport().save(srmCaseCommMode);
					}
				}
				// Task #2747 每筆案件，皆 多新增一筆 一般交易
				i = 1;
				srmCaseTransactionParameter = new SrmCaseTransactionParameter();
				srmCaseTransactionParameter.setCaseId(srmCaseHandleInfo.getCaseId());
				srmCaseTransactionParameter.setParamterValueId(srmCaseHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
				srmCaseTransactionParameter.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.COMMON.getCode());
				// 一般交易的MID，是案件的MID
				srmCaseTransactionParameter.setMerchantCode(srmCaseHandleInfoDTO.getAeMid());
				// 存tid值
				srmCaseTransactionParameter.setTid(srmCaseHandleInfoDTO.getTid());
				this.srmCaseTransactionParameterDAO.getDaoSupport().save(srmCaseTransactionParameter);
				
				// 若unionPayFlag=1表示有銀聯交易 若smartpay=1表示有smartpay交易，一筆案件最多兩筆資料
				if(String.valueOf(1).equals(srmCaseHandleInfoDTO.getUnionPayFlag())
						|| String.valueOf(1).equals(srmCaseHandleInfoDTO.getSmartPayFlag())){
				//	transactionParameters = new ArrayList<SrmCaseTransactionParameter>();
					// 新增unionPay交易
					if(String.valueOf(1).equals(srmCaseHandleInfoDTO.getUnionPayFlag())){
						i ++;
						srmCaseTransactionParameter = new SrmCaseTransactionParameter();
						srmCaseTransactionParameter.setCaseId(srmCaseHandleInfo.getCaseId());
						srmCaseTransactionParameter.setParamterValueId(srmCaseHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						srmCaseTransactionParameter.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.CUP.getCode());
						
						// Task #2865 若unionPayFlag=1表示有銀聯交易，CUP交易，的MID永遠是空值
					//	srmCaseTransactionParameter.setMerchantCode(srmCaseHandleInfoDTO.getSmid());
						
						srmCaseTransactionParameter.setTid(srmCaseHandleInfoDTO.getStid());
						this.srmCaseTransactionParameterDAO.getDaoSupport().save(srmCaseTransactionParameter);
					}
					// 新增smartpay交易
					if(String.valueOf(1).equals(srmCaseHandleInfoDTO.getSmartPayFlag())){
						i ++;
						srmCaseTransactionParameter = new SrmCaseTransactionParameter();
						srmCaseTransactionParameter.setCaseId(srmCaseHandleInfo.getCaseId());
						srmCaseTransactionParameter.setParamterValueId(srmCaseHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						srmCaseTransactionParameter.setTransactionType(IAtomsConstants.TRANSACTION_CATEGORY.SMART_PAY.getCode());
						srmCaseTransactionParameter.setMerchantCode(srmCaseHandleInfoDTO.getSmid());
						srmCaseTransactionParameter.setTid(srmCaseHandleInfoDTO.getStid());
						this.srmCaseTransactionParameterDAO.getDaoSupport().save(srmCaseTransactionParameter);
					}
				}
				// 案件附加資料
				if(!CollectionUtils.isEmpty(caseAttFileMap) && StringUtils.hasText(srmCaseHandleInfo.getCaseId())
						&& caseAttFileMap.containsKey(srmCaseHandleInfo.getCaseId())){
					i = 0;
					for(SrmCaseAttFileDTO srmCaseAttFileDTO : caseAttFileMap.get(srmCaseHandleInfo.getCaseId())){
						i++;
						srmCaseAttFile = (SrmCaseAttFile) transformer.transform(srmCaseAttFileDTO, new SrmCaseAttFile());
						srmCaseAttFile.setAttFileId(srmCaseAttFile.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
					//	srmCaseAttFile.setAttFileId(StringUtils.toFixString(10, j)); 
						srmCaseAttFile.setFilePath("C:\\FOMS_FILES\\APPENDFILES\\" + srmCaseAttFile.getFileName());
						this.srmCaseAttFileDAO.getDaoSupport().save(srmCaseAttFile);
					}
				}
				// 案件歷程資料
				if(!CollectionUtils.isEmpty(tempCaseTransactionList)){
					i = 0;
					for(SrmCaseTransactionDTO srmCaseTransactionDTO : tempCaseTransactionList){
						i++;
						srmCaseTransaction = (SrmCaseTransaction) transformer.transform(srmCaseTransactionDTO, new SrmCaseTransaction());
						srmCaseTransaction.setTransactionId(srmCaseTransaction.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						this.srmCaseTransactionDAO.getDaoSupport().save(srmCaseTransaction);
					}
				}
			}
			formDTO.setSaveFlag(true);
		} catch (DataAccessException e) {
			formDTO.setSaveFlag(false);
			LOGGER.error(".saveCaseDataThread() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			formDTO.setSaveFlag(false);
			LOGGER.error(".saveCaseDataThread() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferCaseNewHandleInfo(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferCaseNewHandleInfo(SessionContext sessionContext) throws ServiceException {
		OldDataTransferFormDTO formDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
		Timestamp startDate = DateTimeUtils.getCurrentTimestamp();
		LOGGER.debug(".transferCaseNewHandleInfo()", "Service startDate" + DateTimeUtils.getCurrentTimestamp());
		try {
			Map map = new HashMap();
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除最新案件資料信息
			this.srmCaseNewHandleInfoDAO.deleteTransferData();
			
			// 若無設備信息則去DB查詢
			if (CollectionUtils.isEmpty(this.edcTypeList)) {
				this.edcTypeList = this.assetTypeDAO.listAssetByName(null, false, true);
				if (CollectionUtils.isEmpty(this.edcTypeList)) {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無周邊設備信息則去DB查詢
			if(CollectionUtils.isEmpty(this.peripheralsList)){
				this.peripheralsList = this.assetTypeDAO.listPeripherals();
				if(CollectionUtils.isEmpty(this.peripheralsList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入設備品項資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無公司信息則去DB查詢
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
				if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先轉入公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無合約信息則去DB查詢
			if (CollectionUtils.isEmpty(this.contractList)) {
				this.contractList = this.contractDAO.getContractByCode(null);
				if(CollectionUtils.isEmpty(this.contractList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先合約基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 綁定合約與公司信息
			if(CollectionUtils.isEmpty(this.contractMap)){
				List<BimContractDTO> contractDTOs = this.contractDAO.getContractList();
				if(!CollectionUtils.isEmpty(contractDTOs)){
					this.contractMap = new HashMap<String, List<Parameter>>();
					List<Parameter> tempContractList = null;
					Parameter tempParameter = null;
					for(BimContractDTO contractDTO : contractDTOs){
						if(StringUtils.hasText(contractDTO.getCompanyId())){
							tempParameter = new Parameter(contractDTO.getContractCode(), contractDTO.getContractId());
							if(this.contractMap.containsKey(contractDTO.getCompanyId())){
								tempContractList = this.contractMap.get(contractDTO.getCompanyId());
							} else {
								tempContractList = new ArrayList<Parameter>();
							}
							tempContractList.add(tempParameter);
							this.contractMap.put(contractDTO.getCompanyId(), tempContractList);
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 若無部門信息則去DB查詢 依公司查找放入map中
			if(CollectionUtils.isEmpty(this.departmentMap)){
				this.departmentMap = new HashMap<String, List<Parameter>>();
				List<Parameter> departmentList = null;
				if (!CollectionUtils.isEmpty(this.companyCodeList)) {
					for (Parameter company : companyCodeList) {
						if (!departmentMap.containsKey(company.getValue().toString())) {
							departmentList = this.departmentDAO.getDepts(company.getValue().toString());
							if (!CollectionUtils.isEmpty(departmentList)) {
								departmentMap.put(company.getValue().toString(), departmentList);
							}
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先公司基本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			//若無程式版本信息去DB中查詢
			if(CollectionUtils.isEmpty(this.applicationMap)){
				this.applicationMap = new HashMap<String, List<ApplicationDTO>>();
				List<ApplicationDTO> allApplicationDTOs = this.applicationDAO.getApplicationList();
				List<ApplicationDTO> tempApplicationList = null;
				if (!CollectionUtils.isEmpty(allApplicationDTOs)) {
					for(ApplicationDTO tempApplicationDTO : allApplicationDTOs){
						if(StringUtils.hasText(tempApplicationDTO.getAssetTypeId())){
							if(this.applicationMap.containsKey(tempApplicationDTO.getAssetTypeId())){
								tempApplicationList = this.applicationMap.get(tempApplicationDTO.getAssetTypeId());
							} else {
								tempApplicationList = new ArrayList<ApplicationDTO>();
							}
							tempApplicationList.add(tempApplicationDTO);
							this.applicationMap.put(tempApplicationDTO.getAssetTypeId(), tempApplicationList);
						}
					}
				} else {
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先程式版本資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 倉庫據點
			if(CollectionUtils.isEmpty(this.warehouseList)){
				this.warehouseList = this.warehouseDAO.listWarehouseByName(null);
				if(CollectionUtils.isEmpty(this.warehouseList)){
					this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "請先倉庫據點資料").append("</br>");
					map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
					map.put("resultMsg", this.resultMsg);
					sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
					return sessionContext;
				}
			}
			// 得到最新案件信息集合
		//	List<SrmCaseHandleInfoDTO> srmCaseNewHandleInfoDTOs = this.oldDataTransferDAO.listCaseNewHandleInfo(true);
			
			// Task #2964 案件信息集合
			List<SrmCaseHandleInfoDTO> srmCaseNewHandleInfoDTOs = new ArrayList<SrmCaseHandleInfoDTO>();
			// 查詢有設備的最新案件
			List<SrmCaseHandleInfoDTO> isHaveAssetCaseList = this.oldDataTransferDAO.listCaseNewHandleInfo(false);
			if(!CollectionUtils.isEmpty(isHaveAssetCaseList)){
				srmCaseNewHandleInfoDTOs.addAll(isHaveAssetCaseList);
			}
			// 查詢無設備的最新案件
			List<SrmCaseHandleInfoDTO> isNoAssetCaseList = this.oldDataTransferDAO.listCaseNewHandleInfo(true);
			if(!CollectionUtils.isEmpty(isNoAssetCaseList)){
				srmCaseNewHandleInfoDTOs.addAll(isNoAssetCaseList);
			}
			
			// 額到案件最新鏈接檔信息
			List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs = this.oldDataTransferDAO.listCaseNewAssetLink();
			// 得到案件相關的周邊設備信息
	//		List<SrmCaseHandleInfoDTO> listAssetCaseInfoDTOs = this.oldDataTransferDAO.listAssetForCase();
			
			// 查詢可轉入的設備
			int enableCaseNewLik = this.oldDataTransferDAO.queryEnableCaseNewLik();
			LOGGER.debug(".saveNewCaseDataThread() caseAssetLink enable transfer count", "enableCount:" + enableCaseNewLik);
			
			// 可編輯字段的map集合
			Map<String,List<String>> editFildsMap = formDTO.getEditFildsMap();
			// 得到TMS資料
			Map<String, SrmCaseHandleInfoDTO> tmsCaseInfoMap = this.getCaseInfoList(formDTO, true);
			if(tmsCaseInfoMap == null){
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				return sessionContext;
			}
			if(!CollectionUtils.isEmpty(srmCaseNewHandleInfoDTOs)){
				// 案件最新設備鏈接檔集合
				Map<String, List<SrmCaseAssetLinkDTO>> caseAssetLinkMap = new HashMap<String, List<SrmCaseAssetLinkDTO>>();
				// 設備鏈接檔集合
				List<SrmCaseAssetLinkDTO> caseAssetLinkList = null;
				// 案件周邊設備信息集合
				Map<String, SrmCaseHandleInfoDTO> assetCaseInfoListMap = new HashMap<String, SrmCaseHandleInfoDTO>();
				SrmCaseHandleInfoDTO assetCaseInfoDTO = null;
				
				int i = 0;
				// 處理案件設備信息集合
				/*if(!CollectionUtils.isEmpty(listAssetCaseInfoDTOs)){
					for(SrmCaseHandleInfoDTO tempAssetCaseInfoDTO : listAssetCaseInfoDTOs){
						if(StringUtils.hasText(tempAssetCaseInfoDTO.getDtid())){
							if(assetCaseInfoListMap.containsKey(tempAssetCaseInfoDTO.getDtid())){
								assetCaseInfoDTO = assetCaseInfoListMap.get(tempAssetCaseInfoDTO.getDtid());
							} else {
								assetCaseInfoDTO = new SrmCaseHandleInfoDTO();
							}
							if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals())){
								if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals2())){
									if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals3())){
										LOGGER.error(".transferCaseNewHandleInfo() Peripherals is over 3!!!" + assetCaseInfoDTO.getDtid());
									} else {
										assetCaseInfoDTO.setPeripherals3(tempAssetCaseInfoDTO.getAssetTypeId());
									}
								} else {
									assetCaseInfoDTO.setPeripherals2(tempAssetCaseInfoDTO.getAssetTypeId());
								}
							} else {
								assetCaseInfoDTO.setPeripherals(tempAssetCaseInfoDTO.getAssetTypeId());
							}
							assetCaseInfoListMap.put(tempAssetCaseInfoDTO.getDtid(), assetCaseInfoDTO);
						}
					}
				}*/
				// 處理最新案件鏈接檔信息
				if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)){
					// 設備鏈接檔集合
					for(SrmCaseAssetLinkDTO tempCaseAssetLinkDTO : srmCaseAssetLinkDTOs){
						if(StringUtils.hasText(tempCaseAssetLinkDTO.getDtid())){
							// 放置設備鏈接當數據
							if(caseAssetLinkMap.containsKey(tempCaseAssetLinkDTO.getDtid())){
								caseAssetLinkList = caseAssetLinkMap.get(tempCaseAssetLinkDTO.getDtid());
							} else {
								caseAssetLinkList = new ArrayList<SrmCaseAssetLinkDTO>();
							}
							caseAssetLinkList.add(tempCaseAssetLinkDTO);
							caseAssetLinkMap.put(tempCaseAssetLinkDTO.getDtid(), caseAssetLinkList);
							
							
							// 放置設備鏈接當數據
							if(assetCaseInfoListMap.containsKey(tempCaseAssetLinkDTO.getDtid())){
								assetCaseInfoDTO = assetCaseInfoListMap.get(tempCaseAssetLinkDTO.getDtid());
							} else {
								assetCaseInfoDTO = new SrmCaseHandleInfoDTO();
							}
							// edc
							if(String.valueOf(7).equals(tempCaseAssetLinkDTO.getItemType())){
								if(StringUtils.hasText(assetCaseInfoDTO.getEdcType())){
									LOGGER.error(".transferCaseNewHandleInfo() EDC is over 1!!!" + tempCaseAssetLinkDTO.getDtid());
								} else {
									assetCaseInfoDTO.setEdcType(tempCaseAssetLinkDTO.getItemId());
								}
							// 周邊設備
							} else {
								if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals())){
									if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals2())){
										if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals3())){
											LOGGER.error(".transferCaseNewHandleInfo() Peripherals is over 3!!!" + tempCaseAssetLinkDTO.getDtid());
										} else {
											assetCaseInfoDTO.setPeripherals3(tempCaseAssetLinkDTO.getItemId());
										}
									} else {
										assetCaseInfoDTO.setPeripherals2(tempCaseAssetLinkDTO.getItemId());
									}
								} else {
									assetCaseInfoDTO.setPeripherals(tempCaseAssetLinkDTO.getItemId());
								}
							}
							assetCaseInfoListMap.put(tempCaseAssetLinkDTO.getDtid(), assetCaseInfoDTO);
						}
					}
				}
				formDTO.setSrmCaseHandleInfoDTOs(srmCaseNewHandleInfoDTOs);
				formDTO.setTmsCaseInfoMap(tmsCaseInfoMap);
				formDTO.setAssetCaseInfoListMap(assetCaseInfoListMap);
				formDTO.setCaseAssetLinkMap(caseAssetLinkMap);
				sessionContext.setResponseResult(formDTO);
				
				if(!CollectionUtils.isEmpty(assetCaseInfoListMap)){
					LOGGER.debug(".saveNewCaseDataThread() caseAssetLink dtid total", "total:" + assetCaseInfoListMap.size());
				}
				if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)){
					LOGGER.debug(".saveNewCaseDataThread() caseAssetLink total", "total:" + srmCaseAssetLinkDTOs.size());
				}
				// 最新案件轉移筆數
				if(!CollectionUtils.isEmpty(srmCaseNewHandleInfoDTOs)){
					formDTO.setTransferCaseNewInfoNum(srmCaseNewHandleInfoDTOs.size());
				} else {
					formDTO.setTransferCaseNewInfoNum(0);
				}
				// 最新案件設備鏈接轉移筆數
				/*if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)){
					formDTO.setTransferCaseNewLinkNum(srmCaseAssetLinkDTOs.size());
				} else {
					formDTO.setTransferCaseNewLinkNum(0);
				}*/
				formDTO.setTransferCaseNewLinkNum(enableCaseNewLik);
				
				
				this.resultMsg = this.resultMsg.append("最新案件資料轉入完成").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);			
			} else {
				this.resultMsg = this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無最新案件資料").append("</br>");
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
				map.put("resultMsg", this.resultMsg);
				sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
				return sessionContext;
			}
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".transferCaseNewHandleInfo() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferCaseNewHandleInfo() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:多線程處理案件數據轉換
	 * @author CrissZhang
	 * @param formDTO
	 * @return void
	 */
	public void saveNewCaseDataThread(OldDataTransferFormDTO formDTO) throws ServiceException{
		try {
			Transformer transformer = new SimpleDtoDmoTransformer();
			// 得到最新案件信息集合
			List<SrmCaseHandleInfoDTO> srmCaseNewHandleInfoDTOs = formDTO.getSrmCaseHandleInfoDTOs();
			// 得到TMS資料
			Map<String, SrmCaseHandleInfoDTO> tmsCaseInfoMap = formDTO.getTmsCaseInfoMap();
			// 案件周邊設備信息集合
			Map<String, SrmCaseHandleInfoDTO> assetCaseInfoListMap = formDTO.getAssetCaseInfoListMap();
			// 案件最新設備鏈接檔集合
			Map<String, List<SrmCaseAssetLinkDTO>> caseAssetLinkMap = formDTO.getCaseAssetLinkMap();
			
			// 最新案件DMO信息
			SrmCaseNewHandleInfo srmCaseNewHandleInfo = null;
			// 臨時客戶編號
			String tempCustomerId = null;
			
			String tempCompanyId = null;
			// 臨時軟體版本
			String tempSoftVersion = null;
			// 臨時軟體版本
			SrmCaseHandleInfoDTO tempSrmCaseHandleInfoDTO = null;
			// 程式版本集合
			List<ApplicationDTO> tempApplicationList = null;
			SrmCaseHandleInfoDTO assetCaseInfoDTO = null;
			// 交易參數集合
			List<SrmCaseTransactionParameterDTO> tempSrmCaseTransactionParameterDTOs = null;
			// 交易參數DMO
			SrmCaseNewTransactionParameter srmCaseNewTransactionParameter = null;
			// 案件支援功能DMO
			SrmCaseNewAssetFunction srmCaseNewAssetFunction = null;
			// 案件最新鏈接檔DMO
			SrmCaseNewAssetLink srmCaseNewAssetLink = null;
			// 設備鏈接檔集合
			List<SrmCaseAssetLinkDTO> caseAssetLinkList = null;
			int i = 0;
			// 遍歷保存
			for(SrmCaseHandleInfoDTO srmCaseNewHandleInfoDTO : srmCaseNewHandleInfoDTOs){
				// dtid為 42833201 不轉
				if("42833201".equals(srmCaseNewHandleInfoDTO.getDtid())){
					LOGGER.debug(".saveNewCaseDataThread() dtid is 42833201", "caseId:" + srmCaseNewHandleInfoDTO.getCaseId());
					continue;
				}
				// DTO刷卡機類型
				/*if(this.fomsAssetTypeMap.containsKey(srmCaseNewHandleInfoDTO.getEdcType())){
					srmCaseNewHandleInfoDTO.setEdcType(this.fomsAssetTypeMap.get(srmCaseNewHandleInfoDTO.getEdcType()));
				}*/
				// 判斷是捷達威標誌位
				boolean isJdw = false;
				// 捷達威客戶
				if("JDW-EDC".equals(srmCaseNewHandleInfoDTO.getCustomerId())){
					isJdw = true;
					LOGGER.debug(".saveNewCaseDataThread() customer is jdw", "caseId:" + srmCaseNewHandleInfoDTO.getCaseId() + ",dtid:" + srmCaseNewHandleInfoDTO.getDtid());
				}
				srmCaseNewHandleInfo = (SrmCaseNewHandleInfo) transformer.transform(srmCaseNewHandleInfoDTO, new SrmCaseNewHandleInfo());
			//	tempCustomerId = getValueByName(srmCaseNewHandleInfo.getCustomerId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
				tempCustomerId = this.getValueByName(srmCaseNewHandleInfo.getCustomerId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
				// 客戶編號
				srmCaseNewHandleInfo.setCustomerId(tempCustomerId);
				// 合約ID
				if(StringUtils.hasText(tempCustomerId) && !CollectionUtils.isEmpty(this.contractMap)
						&& (!CollectionUtils.isEmpty(this.contractMap.get(tempCustomerId)))){
					srmCaseNewHandleInfo.setContractId((String)((this.contractMap.get(tempCustomerId)).get(0).getValue()));
				}
				// 維護廠商
				tempCompanyId = this.getValueByName(srmCaseNewHandleInfo.getCompanyId(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
				srmCaseNewHandleInfo.setCompanyId(tempCompanyId);
				// 維護部門
				if (!IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(srmCaseNewHandleInfo.getDepartmentId())) {
					if(StringUtils.hasText(tempCompanyId)){
						if (!departmentMap.containsKey(tempCompanyId)) {
							srmCaseNewHandleInfo.setDepartmentId(null);
							LOGGER.error(".saveNewCaseDataThread() company not exists dept", "departmentId:" + srmCaseNewHandleInfoDTO.getDepartmentId());
						} else {
							srmCaseNewHandleInfo.setDepartmentId(this.getValueByIdAndName(srmCaseNewHandleInfo.getDepartmentId(), tempCompanyId,BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()));
						}
					} else {
						srmCaseNewHandleInfo.setDepartmentId(null);
						LOGGER.error(".saveNewCaseDataThread() company is null", "departmentId:" + srmCaseNewHandleInfoDTO.getDepartmentId());
					}
				}
				// 裝機地址(縣市)
				srmCaseNewHandleInfo.setInstalledAdressLocation(this.getValueByName(srmCaseNewHandleInfo.getInstalledAdressLocation(),IATOMS_PARAM_TYPE.LOCATION.getCode()));
				// 聯絡地址縣市
				srmCaseNewHandleInfo.setContactAddressLocation(srmCaseNewHandleInfo.getInstalledAdressLocation());
				// DMO刷卡機類型
			//	srmCaseNewHandleInfo.setEdcType(this.getValueByName(srmCaseNewHandleInfo.getEdcType(), "assetType"));
				
				// 若鏈接當不存在edc
				boolean isNoEdc = false;
				// 案件設備信息處理 捷達威不轉入設備信息
				if(!isJdw && !CollectionUtils.isEmpty(assetCaseInfoListMap)
						&& assetCaseInfoListMap.containsKey(srmCaseNewHandleInfo.getDtid())
						&& assetCaseInfoListMap.get(srmCaseNewHandleInfo.getDtid()) != null){
					assetCaseInfoDTO = assetCaseInfoListMap.get(srmCaseNewHandleInfo.getDtid());
					if(StringUtils.hasText(assetCaseInfoDTO.getEdcType())){
						// 刷卡機型
						srmCaseNewHandleInfoDTO.setEdcType(assetCaseInfoDTO.getEdcType());
						srmCaseNewHandleInfo.setEdcType(this.getValueByName(srmCaseNewHandleInfoDTO.getEdcType(), "edcType"));
					} else {
						isNoEdc = true;
						// 若為foms設備匹配設備
						if(this.fomsAssetTypeMap.containsKey(srmCaseNewHandleInfoDTO.getEdcType())){
							srmCaseNewHandleInfoDTO.setEdcType(this.fomsAssetTypeMap.get(srmCaseNewHandleInfoDTO.getEdcType()));
						}
						srmCaseNewHandleInfo.setEdcType(this.getValueByName(srmCaseNewHandleInfoDTO.getEdcType(), "edcType"));
					}
					
					// 周邊設備
					srmCaseNewHandleInfoDTO.setPeripherals(assetCaseInfoDTO.getPeripherals());
					srmCaseNewHandleInfoDTO.setPeripherals2(assetCaseInfoDTO.getPeripherals2());
					srmCaseNewHandleInfoDTO.setPeripherals3(assetCaseInfoDTO.getPeripherals3());
					// 周邊設備 Task #2681 S300Q 取代 R50
					if("R50".equals(srmCaseNewHandleInfoDTO.getPeripherals())){
						srmCaseNewHandleInfo.setPeripherals(this.getValueByName("S300Q", "Related_Products"));
					} else {
						srmCaseNewHandleInfo.setPeripherals(this.getValueByName(srmCaseNewHandleInfoDTO.getPeripherals(), "Related_Products"));
					}
					if("R50".equals(srmCaseNewHandleInfoDTO.getPeripherals2())){
						srmCaseNewHandleInfo.setPeripherals2(this.getValueByName("S300Q", "Related_Products"));
					} else {
						srmCaseNewHandleInfo.setPeripherals2(this.getValueByName(srmCaseNewHandleInfoDTO.getPeripherals2(), "Related_Products"));
					}
					if("R50".equals(srmCaseNewHandleInfoDTO.getPeripherals3())){
						srmCaseNewHandleInfo.setPeripherals3(this.getValueByName("S300Q", "Related_Products"));
					} else {
						srmCaseNewHandleInfo.setPeripherals3(this.getValueByName(srmCaseNewHandleInfoDTO.getPeripherals3(), "Related_Products"));
					}
				// 為空則置為null
				} else {
					// 若為foms設備匹配設備
					if(this.fomsAssetTypeMap.containsKey(srmCaseNewHandleInfoDTO.getEdcType())){
						srmCaseNewHandleInfoDTO.setEdcType(this.fomsAssetTypeMap.get(srmCaseNewHandleInfoDTO.getEdcType()));
					}
					srmCaseNewHandleInfo.setEdcType(this.getValueByName(srmCaseNewHandleInfoDTO.getEdcType(), "edcType"));
				}
				
				// 轉入部分TMS資料
				if(!CollectionUtils.isEmpty(tmsCaseInfoMap) && tmsCaseInfoMap.containsKey(srmCaseNewHandleInfo.getDtid())){
					tempSrmCaseHandleInfoDTO = tmsCaseInfoMap.get(srmCaseNewHandleInfo.getDtid());
					if(tempSrmCaseHandleInfoDTO != null){
						// 軟體版本
						tempSoftVersion = tempSrmCaseHandleInfoDTO.getSoftwareVersion();
						if(StringUtils.hasText(tempSoftVersion)){
							// 軟體版本
							if(!CollectionUtils.isEmpty(this.applicationMap) && StringUtils.hasText(srmCaseNewHandleInfo.getEdcType())
									&& this.applicationMap.containsKey(srmCaseNewHandleInfo.getEdcType())
									&& StringUtils.hasText(tempSoftVersion)
									&& StringUtils.hasText(srmCaseNewHandleInfo.getCustomerId())
									&& tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) > 0
									&& tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT) > 0){
								tempApplicationList = this.applicationMap.get(srmCaseNewHandleInfo.getEdcType());
								if(!CollectionUtils.isEmpty(tempApplicationList)){
									for(ApplicationDTO tempApplication : tempApplicationList){
										// 當前行程式名稱
										if((tempSoftVersion.substring(tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) + 1,
												tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT))).equals(tempApplication.getVersion())
												&& srmCaseNewHandleInfo.getCustomerId().equals(tempApplication.getCustomerId())
												&& (tempSoftVersion.substring(0, tempSoftVersion.indexOf(IAtomsConstants.MARK_BRACKET_LEFT))).equals(tempApplication.getName())){
											srmCaseNewHandleInfo.setSoftwareVersion(tempApplication.getApplicationId());
											break;
										}
									}
								}
							}
						}
						// 內建功能 若無主要週邊， Enable CLSS(感應交易)=Y，則內建功能有Dongle，Pinpad Extern(外接PINPAD) =Y，則內建功能有Pinpad
						if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
								&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
							srmCaseNewHandleInfo.setBuiltInFeature("Dongle,Pinpad");
						} else if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
								&& IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
							srmCaseNewHandleInfo.setBuiltInFeature("Dongle");
						} else if(IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
								&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
							srmCaseNewHandleInfo.setBuiltInFeature("Pinpad");
						}
						// 若有主要週邊，Enable CLSS(感應交易)、Pinpad Extern(外接PINPAD)反映在主要週邊功能，若無法完全反映，這反映在內建功能
						// 捷達威不轉入周邊設備功能邏輯
						if(!isJdw && !CollectionUtils.isEmpty(assetCaseInfoListMap)
								&& assetCaseInfoListMap.containsKey(srmCaseNewHandleInfo.getDtid())
								&& assetCaseInfoListMap.get(srmCaseNewHandleInfo.getDtid()) != null){
							assetCaseInfoDTO = assetCaseInfoListMap.get(srmCaseNewHandleInfo.getDtid());
							// 臨時處理支援功能
							String tempAssetFunction = null;
							// 周邊設備1
							if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals())){
								if("R50".equals(assetCaseInfoDTO.getPeripherals())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										srmCaseNewHandleInfo.setPeripheralsFunction("Dongle");
									}
								} else if("SP20".equals(assetCaseInfoDTO.getPeripherals())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										srmCaseNewHandleInfo.setPeripheralsFunction("Pinpad");
									}
								} else if("S200".equals(assetCaseInfoDTO.getPeripherals())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction("Dongle,Pinpad");
										} else if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction("Dongle");
										} else if(IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction("Pinpad");
										}
									}
								}
								tempAssetFunction = srmCaseNewHandleInfo.getPeripheralsFunction();
							}
							// 周邊設備2
							if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals2())){
								if("R50".equals(assetCaseInfoDTO.getPeripherals2())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										srmCaseNewHandleInfo.setPeripheralsFunction2("Dongle");
									}
								} else if("SP20".equals(assetCaseInfoDTO.getPeripherals2())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										srmCaseNewHandleInfo.setPeripheralsFunction2("Pinpad");
									}
								} else if("S200".equals(assetCaseInfoDTO.getPeripherals2())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction2("Dongle,Pinpad");
										} else if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction2("Dongle");
										} else if(IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction2("Pinpad");
										}
									}
								}
								// 處理重復的功能
								if(StringUtils.hasText(tempAssetFunction)){
									if("Dongle,Pinpad".equals(tempAssetFunction)){
										srmCaseNewHandleInfo.setPeripheralsFunction2(null);
									} else if("Dongle".equals(tempAssetFunction)){
										if("Dongle,Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction2())
												|| "Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction2())){
											srmCaseNewHandleInfo.setPeripheralsFunction2("Pinpad");
											tempAssetFunction = "Dongle,Pinpad";
										} else if("Dongle".equals(srmCaseNewHandleInfo.getPeripheralsFunction2())){
											srmCaseNewHandleInfo.setPeripheralsFunction2(null);
										}
									} else if("Pinpad".equals(tempAssetFunction)){
										if("Dongle,Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction2())
												|| "Dongle".equals(srmCaseNewHandleInfo.getPeripheralsFunction2())){
											srmCaseNewHandleInfo.setPeripheralsFunction2("Dongle");
											tempAssetFunction = "Dongle,Pinpad";
										} else if("Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction2())){
											srmCaseNewHandleInfo.setPeripheralsFunction2(null);
										}
									}
								} else {
									tempAssetFunction = srmCaseNewHandleInfo.getPeripheralsFunction2();
								}
							}
							// 周邊設備3
							if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals3())){
								if("R50".equals(assetCaseInfoDTO.getPeripherals3())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										srmCaseNewHandleInfo.setPeripheralsFunction3("Dongle");
									}
								} else if("SP20".equals(assetCaseInfoDTO.getPeripherals3())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										srmCaseNewHandleInfo.setPeripheralsFunction3("Pinpad");
									}
								} else if("S200".equals(assetCaseInfoDTO.getPeripherals3())){
									if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())){
										if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction3("Dongle,Pinpad");
										} else if(IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction3("Dongle");
										} else if(IAtomsConstants.NO.equals(tempSrmCaseHandleInfoDTO.getEnableClss())
												&& IAtomsConstants.YES.equals(tempSrmCaseHandleInfoDTO.getPinpadExtern())){
											srmCaseNewHandleInfo.setPeripheralsFunction3("Pinpad");
										}
									}
								}
								// 處理重復的功能
								if(StringUtils.hasText(tempAssetFunction)){
									if("Dongle,Pinpad".equals(tempAssetFunction)){
										srmCaseNewHandleInfo.setPeripheralsFunction3(null);
									} else if("Dongle".equals(tempAssetFunction)){
										if("Dongle,Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction3())
												|| "Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction3())){
											srmCaseNewHandleInfo.setPeripheralsFunction3("Pinpad");
											tempAssetFunction = "Dongle,Pinpad";
										} else if("Dongle".equals(srmCaseNewHandleInfo.getPeripheralsFunction3())){
											srmCaseNewHandleInfo.setPeripheralsFunction3(null);
										}
									} else if("Pinpad".equals(tempAssetFunction)){
										if("Dongle,Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction3())
												|| "Dongle".equals(srmCaseNewHandleInfo.getPeripheralsFunction3())){
											srmCaseNewHandleInfo.setPeripheralsFunction3("Dongle");
											tempAssetFunction = "Dongle,Pinpad";
										} else if("Pinpad".equals(srmCaseNewHandleInfo.getPeripheralsFunction3())){
											srmCaseNewHandleInfo.setPeripheralsFunction3(null);
										}
									}
								} else {
									tempAssetFunction = srmCaseNewHandleInfo.getPeripheralsFunction2();
								}
							}
							// R50、S200 Enable CLSS(感應交易)=Y，則週邊功能有Dongle
							// SP20、S200 Pinpad Extern(外接PINPAD) =Y，則週邊功能有Pinpad
							
							// 處理重復的功能
							if(StringUtils.hasText(tempAssetFunction)){
								if("Dongle,Pinpad".equals(tempAssetFunction)){
									srmCaseNewHandleInfo.setBuiltInFeature(null);
								} else if("Dongle".equals(tempAssetFunction)){
									if("Dongle,Pinpad".equals(srmCaseNewHandleInfo.getBuiltInFeature())
											|| "Pinpad".equals(srmCaseNewHandleInfo.getBuiltInFeature())){
										srmCaseNewHandleInfo.setBuiltInFeature("Pinpad");
										tempAssetFunction = "Dongle,Pinpad";
									} else if("Dongle".equals(srmCaseNewHandleInfo.getBuiltInFeature())){
										srmCaseNewHandleInfo.setBuiltInFeature(null);
									}
								} else if("Pinpad".equals(tempAssetFunction)){
									if("Dongle,Pinpad".equals(srmCaseNewHandleInfo.getBuiltInFeature())
											|| "Dongle".equals(srmCaseNewHandleInfo.getBuiltInFeature())){
										srmCaseNewHandleInfo.setBuiltInFeature("Dongle");
										tempAssetFunction = "Dongle,Pinpad";
									} else if("Pinpad".equals(srmCaseNewHandleInfo.getBuiltInFeature())){
										srmCaseNewHandleInfo.setBuiltInFeature(null);
									}
								}
							}
						}
						// 雙模組模式
						srmCaseNewHandleInfo.setMultiModule(tempSrmCaseHandleInfoDTO.getMultiModule());
						// ECR連接
						srmCaseNewHandleInfo.setEcrConnection(tempSrmCaseHandleInfoDTO.getEcrConnection());
						// 通訊模式
						srmCaseNewHandleInfo.setConnectionType(tempSrmCaseHandleInfoDTO.getConnectionType());
						// 電子發票載具
						srmCaseNewHandleInfo.setElectronicInvoice(tempSrmCaseHandleInfoDTO.getElectronicInvoice());
						// 銀聯閃付
						srmCaseNewHandleInfo.setCupQuickPass(tempSrmCaseHandleInfoDTO.getCupQuickPass());
						// LOGO
						srmCaseNewHandleInfo.setLogoStyle(tempSrmCaseHandleInfoDTO.getLogoStyle());
						// 是否開啟加密
						srmCaseNewHandleInfo.setIsOpenEncrypt(tempSrmCaseHandleInfoDTO.getIsOpenEncrypt());
						// 電子化繳費平台
						srmCaseNewHandleInfo.setElectronicPayPlatform(tempSrmCaseHandleInfoDTO.getElectronicPayPlatform());
						
						// Task #2937 寬頻連線
						srmCaseNewHandleInfo.setNetVendorId(tempSrmCaseHandleInfoDTO.getNetVendorId());
					}
				}
				// 廠商工程部門代碼
				srmCaseNewHandleInfo.setDispatchDeptId(srmCaseNewHandleInfo.getDepartmentId());
/*				if (!IAtomsConstants.CASE_ROLE.CUSTOMER_SERVICE.getCode().equals(srmCaseNewHandleInfo.getDispatchDeptId())) {
					if(StringUtils.hasText(tempCompanyId)){
						if (!departmentMap.containsKey(tempCompanyId)) {
							srmCaseNewHandleInfo.setDispatchDeptId(null);
							LOGGER.error(".transferCaseHandleInfo() haveNoMatch", "dispatchDeptId:" + srmCaseNewHandleInfo.getDispatchDeptId());
						} else {
							srmCaseNewHandleInfo.setDispatchDeptId(getValueByIdAndName(srmCaseNewHandleInfo.getDispatchDeptId(), tempCompanyId,BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue()));
						}
					} else {
						srmCaseNewHandleInfo.setDispatchDeptId(null);
						LOGGER.error(".transferCaseHandleInfo() haveNoMatch", "dispatchDeptId:" + srmCaseNewHandleInfo.getDispatchDeptId());
					}
				}*/
				this.srmCaseNewHandleInfoDAO.getDaoSupport().save(srmCaseNewHandleInfo);
				
				// 案件通訊模式維護檔保存
				if(StringUtils.hasText(srmCaseNewHandleInfo.getConnectionType())){
					i = 0;
					SrmCaseNewCommMode srmCaseNewCommMode = null;
					for(String tempId : StringUtils.toList(srmCaseNewHandleInfo.getConnectionType(), IAtomsConstants.MARK_SEPARATOR)){
						i++;
						srmCaseNewCommMode = new SrmCaseNewCommMode();
						srmCaseNewCommMode.setCaseId(srmCaseNewHandleInfo.getCaseId());
						srmCaseNewCommMode.setId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
						srmCaseNewCommMode.setCommModeId(tempId);
						this.srmCaseNewCommModeDAO.getDaoSupport().save(srmCaseNewCommMode);
					}
				}
				
				// 轉入部分TMS交易參數資料
				if(!CollectionUtils.isEmpty(tmsCaseInfoMap) && tmsCaseInfoMap.containsKey(srmCaseNewHandleInfo.getDtid())){
					tempSrmCaseHandleInfoDTO = tmsCaseInfoMap.get(srmCaseNewHandleInfo.getDtid());
					if(tempSrmCaseHandleInfoDTO != null){
						tempSrmCaseTransactionParameterDTOs = tempSrmCaseHandleInfoDTO.getCaseTransactionParameterDTOs();
						if(!CollectionUtils.isEmpty(tempSrmCaseTransactionParameterDTOs)){
							i = 0;
							for(SrmCaseTransactionParameterDTO tempSrmCaseTransactionParameterDTO : tempSrmCaseTransactionParameterDTOs){
								if(!StringUtils.hasText(tempSrmCaseTransactionParameterDTO.getTransactionType())){
									continue;
								}
								i ++;
								srmCaseNewTransactionParameter = (SrmCaseNewTransactionParameter) transformer.transform(tempSrmCaseTransactionParameterDTO, new SrmCaseNewTransactionParameter());
								srmCaseNewTransactionParameter.setParamterValueId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
								srmCaseNewTransactionParameter.setCaseId(srmCaseNewHandleInfo.getCaseId());
								srmCaseNewTransactionParameter.setDtid(srmCaseNewHandleInfo.getDtid());
								this.srmCaseNewTransactionParameterDAO.getDaoSupport().save(srmCaseNewTransactionParameter);
							}
						}
					}
				}
				// 轉入設備支援供功能資料
				if(StringUtils.hasText(srmCaseNewHandleInfo.getBuiltInFeature())
						|| StringUtils.hasText(srmCaseNewHandleInfo.getPeripheralsFunction())
						|| StringUtils.hasText(srmCaseNewHandleInfo.getPeripheralsFunction2())
						|| StringUtils.hasText(srmCaseNewHandleInfo.getPeripheralsFunction3())){
					i = 0;
					if(StringUtils.hasText(srmCaseNewHandleInfo.getBuiltInFeature())){
						for(String tempFunction : StringUtils.toList(srmCaseNewHandleInfo.getBuiltInFeature(), ",")){
							i ++;
							srmCaseNewAssetFunction = new SrmCaseNewAssetFunction();
							srmCaseNewAssetFunction.setId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
							srmCaseNewAssetFunction.setCaseId(srmCaseNewHandleInfo.getCaseId());
							srmCaseNewAssetFunction.setFunctionId(tempFunction);
							srmCaseNewAssetFunction.setFunctionCategory(String.valueOf(10));
							this.srmCaseNewAssetFunctionDAO.getDaoSupport().save(srmCaseNewAssetFunction);
						}
					}
					if(StringUtils.hasText(srmCaseNewHandleInfo.getPeripheralsFunction())){
						for(String tempFunction : StringUtils.toList(srmCaseNewHandleInfo.getPeripheralsFunction(), ",")){
							i ++;
							srmCaseNewAssetFunction = new SrmCaseNewAssetFunction();
							srmCaseNewAssetFunction.setId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
							srmCaseNewAssetFunction.setCaseId(srmCaseNewHandleInfo.getCaseId());
							srmCaseNewAssetFunction.setFunctionId(tempFunction);
							srmCaseNewAssetFunction.setFunctionCategory(String.valueOf(11));
							this.srmCaseNewAssetFunctionDAO.getDaoSupport().save(srmCaseNewAssetFunction);
						}
					}
					if(StringUtils.hasText(srmCaseNewHandleInfo.getPeripheralsFunction2())){
						for(String tempFunction : StringUtils.toList(srmCaseNewHandleInfo.getPeripheralsFunction2(), ",")){
							i ++;
							srmCaseNewAssetFunction = new SrmCaseNewAssetFunction();
							srmCaseNewAssetFunction.setId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
							srmCaseNewAssetFunction.setCaseId(srmCaseNewHandleInfo.getCaseId());
							srmCaseNewAssetFunction.setFunctionId(tempFunction);
							srmCaseNewAssetFunction.setFunctionCategory(String.valueOf(12));
							this.srmCaseNewAssetFunctionDAO.getDaoSupport().save(srmCaseNewAssetFunction);
						}
					}
					if(StringUtils.hasText(srmCaseNewHandleInfo.getPeripheralsFunction3())){
						for(String tempFunction : StringUtils.toList(srmCaseNewHandleInfo.getPeripheralsFunction3(), ",")){
							i ++;
							srmCaseNewAssetFunction = new SrmCaseNewAssetFunction();
							srmCaseNewAssetFunction.setId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
							srmCaseNewAssetFunction.setCaseId(srmCaseNewHandleInfo.getCaseId());
							srmCaseNewAssetFunction.setFunctionId(tempFunction);
							srmCaseNewAssetFunction.setFunctionCategory(String.valueOf(13));
							this.srmCaseNewAssetFunctionDAO.getDaoSupport().save(srmCaseNewAssetFunction);
						}
					}
				}
				// 轉入最新設備鏈接檔資料 捷達威客戶不轉入設備
				if(!isJdw && !CollectionUtils.isEmpty(caseAssetLinkMap) && caseAssetLinkMap.containsKey(srmCaseNewHandleInfo.getDtid())
						&& !CollectionUtils.isEmpty(caseAssetLinkMap.get(srmCaseNewHandleInfo.getDtid()))){
					caseAssetLinkList = caseAssetLinkMap.get(srmCaseNewHandleInfo.getDtid());
					
					i = 0;
					// 無EDC標記 匹配AIMS edc
					if(isNoEdc){
						if(StringUtils.hasText(srmCaseNewHandleInfo.getEdcType())){
							i ++;
							// 記錄一筆edc
							srmCaseNewAssetLink = new SrmCaseNewAssetLink();
							srmCaseNewAssetLink.setItemType(String.valueOf(10));
							srmCaseNewAssetLink.setItemId(srmCaseNewHandleInfo.getEdcType());
							srmCaseNewAssetLink.setItemCategory("EDC");
							srmCaseNewAssetLink.setAssetLinkId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + i);
							srmCaseNewAssetLink.setCaseId(srmCaseNewHandleInfo.getCaseId());
							srmCaseNewAssetLink.setIsLink(IAtomsConstants.NO);
							this.srmCaseNewAssetLinkDAO.getDaoSupport().save(srmCaseNewAssetLink);
						} else {
							LOGGER.error(".saveNewCaseDataThread() caseInfo not have edc", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}
					}
					for(SrmCaseAssetLinkDTO caseAssetLinkDTO : caseAssetLinkList){
						if(srmCaseNewHandleInfoDTO != null){
							if(StringUtils.hasText(srmCaseNewHandleInfoDTO.getEdcType())
									|| StringUtils.hasText(srmCaseNewHandleInfoDTO.getPeripherals())
									|| StringUtils.hasText(srmCaseNewHandleInfoDTO.getPeripherals2())
									|| StringUtils.hasText(srmCaseNewHandleInfoDTO.getPeripherals3())){
								srmCaseNewAssetLink = null;
								// 刷卡機型
								if(StringUtils.hasText(caseAssetLinkDTO.getItemId()) && caseAssetLinkDTO.getItemId().equals(srmCaseNewHandleInfoDTO.getEdcType())
										&& StringUtils.hasText(srmCaseNewHandleInfo.getEdcType())){
									i ++;
									srmCaseNewAssetLink = (SrmCaseNewAssetLink) transformer.transform(caseAssetLinkDTO, new SrmCaseNewAssetLink());
									srmCaseNewAssetLink.setItemType(String.valueOf(10));
									srmCaseNewAssetLink.setItemId(srmCaseNewHandleInfo.getEdcType());
									srmCaseNewAssetLink.setItemCategory("EDC");
								// 周邊設備1
								} else if(StringUtils.hasText(caseAssetLinkDTO.getItemId()) && caseAssetLinkDTO.getItemId().equals(srmCaseNewHandleInfoDTO.getPeripherals())
										&& StringUtils.hasText(srmCaseNewHandleInfo.getPeripherals())){
									i ++;
									srmCaseNewAssetLink = (SrmCaseNewAssetLink) transformer.transform(caseAssetLinkDTO, new SrmCaseNewAssetLink());
									srmCaseNewAssetLink.setItemType(String.valueOf(11));
									srmCaseNewAssetLink.setItemId(srmCaseNewHandleInfo.getPeripherals());
									srmCaseNewAssetLink.setItemCategory("Related_Products");
								// 周邊設備2
								} else if(StringUtils.hasText(caseAssetLinkDTO.getItemId()) && caseAssetLinkDTO.getItemId().equals(srmCaseNewHandleInfoDTO.getPeripherals2())
										&& StringUtils.hasText(srmCaseNewHandleInfo.getPeripherals2())){
									i ++;
									srmCaseNewAssetLink = (SrmCaseNewAssetLink) transformer.transform(caseAssetLinkDTO, new SrmCaseNewAssetLink());
									srmCaseNewAssetLink.setItemType(String.valueOf(12));
									srmCaseNewAssetLink.setItemId(srmCaseNewHandleInfo.getPeripherals2());
									srmCaseNewAssetLink.setItemCategory("Related_Products");
								// 周邊設備3
								} else if(StringUtils.hasText(caseAssetLinkDTO.getItemId()) && caseAssetLinkDTO.getItemId().equals(srmCaseNewHandleInfoDTO.getPeripherals3())
										&& StringUtils.hasText(srmCaseNewHandleInfo.getPeripherals3())){
									i ++;
									srmCaseNewAssetLink = (SrmCaseNewAssetLink) transformer.transform(caseAssetLinkDTO, new SrmCaseNewAssetLink());
									srmCaseNewAssetLink.setItemType(String.valueOf(13));
									srmCaseNewAssetLink.setItemId(srmCaseNewHandleInfo.getPeripherals3());
									srmCaseNewAssetLink.setItemCategory("Related_Products");
								} else {
									LOGGER.error(".saveNewCaseDataThread() caseLink is error", "dtid:" + srmCaseNewHandleInfo.getDtid());
								}
								
								if(srmCaseNewAssetLink != null){
									// 合約信息
									srmCaseNewAssetLink.setContractId(this.getValueByName(srmCaseNewAssetLink.getContractId(), BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue()));
									//倉庫代碼
									srmCaseNewAssetLink.setWarehouseId(this.getValueByName(srmCaseNewAssetLink.getWarehouseId(), WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue()));
									srmCaseNewAssetLink.setAssetLinkId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i));
									srmCaseNewAssetLink.setCaseId(srmCaseNewHandleInfo.getCaseId());
									this.srmCaseNewAssetLinkDAO.getDaoSupport().save(srmCaseNewAssetLink);
								}
							}
						}
					}
					// 若無edc
					if(isNoEdc){
						if(i != (caseAssetLinkList.size() + 1)){
							LOGGER.error(".saveNewCaseDataThread() caseAssetLink size not equals caseAsset size", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}
					} else {
						if(i != caseAssetLinkList.size()){
							LOGGER.error(".saveNewCaseDataThread() caseAssetLink size not equals caseAsset size", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}
					}
				} else {
					// 捷達威客戶不轉入設備
					if(!isJdw){
						// 對不存在該dtid的設備鏈接當進行記錄
						if(!CollectionUtils.isEmpty(caseAssetLinkMap) && !caseAssetLinkMap.containsKey(srmCaseNewHandleInfo.getDtid())){
							LOGGER.error(".saveNewCaseDataThread() caseAssetLink not exists this dtid", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}
						// 若該案件設備鏈接檔為空則進行記錄進行記錄
						if(!CollectionUtils.isEmpty(caseAssetLinkMap) && CollectionUtils.isEmpty(caseAssetLinkMap.get(srmCaseNewHandleInfo.getDtid()))){
							LOGGER.debug(".saveNewCaseDataThread() caseAssetLink is null", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}
						/*// 若案件存在設備，設備鏈接當不存在
						if(StringUtils.hasText(srmCaseNewHandleInfo.getEdcType()) || StringUtils.hasText(srmCaseNewHandleInfo.getPeripherals())
								|| StringUtils.hasText(srmCaseNewHandleInfo.getPeripherals2()) || StringUtils.hasText(srmCaseNewHandleInfo.getPeripherals3())){
							LOGGER.error(".saveNewCaseDataThread() caseAssetLink is null", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}*/
						// 有刷卡機
						if(StringUtils.hasText(srmCaseNewHandleInfo.getEdcType())){
							// 記錄一筆edc
							srmCaseNewAssetLink = new SrmCaseNewAssetLink();
							srmCaseNewAssetLink.setItemType(String.valueOf(10));
							srmCaseNewAssetLink.setItemId(srmCaseNewHandleInfo.getEdcType());
							srmCaseNewAssetLink.setItemCategory("EDC");
							srmCaseNewAssetLink.setAssetLinkId(srmCaseNewHandleInfo.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + 1);
							srmCaseNewAssetLink.setCaseId(srmCaseNewHandleInfo.getCaseId());
							srmCaseNewAssetLink.setIsLink(IAtomsConstants.NO);
							this.srmCaseNewAssetLinkDAO.getDaoSupport().save(srmCaseNewAssetLink);
							LOGGER.error(".saveNewCaseDataThread() caseInfo have edc", "dtid:" + srmCaseNewHandleInfo.getDtid());
						} else {
							LOGGER.error(".saveNewCaseDataThread() caseInfo not have edc", "dtid:" + srmCaseNewHandleInfo.getDtid());
						}
					}
				}
			}
			formDTO.setSaveFlag(true);
		} catch (DataAccessException e) {
			formDTO.setSaveFlag(false);
			LOGGER.error(".saveNewCaseDataThread() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			formDTO.setSaveFlag(false);
			LOGGER.error(".saveNewCaseDataThread() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:刪除處理中案件資料
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	public void deleteCaseInfoData(String tempLogMessage) throws ServiceException{
		try{
			// 刪除處理中案件資料
			this.srmCaseHandleInfoDAO.deleteTransferData();
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(tempLogMessage);
			
		} catch (DataAccessException e) {
			LOGGER.error(".deleteCaseInfoData() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".deleteCaseInfoData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:刪除最新案件資料
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return void
	 */
	public void deleteNewCaseData(String tempLogMessage) throws ServiceException{
		try{
			// 刪除最新案件資料
			this.srmCaseNewHandleInfoDAO.deleteTransferData();
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(tempLogMessage);
			
		} catch (DataAccessException e) {
			LOGGER.error(".deleteNewCaseData() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".deleteNewCaseData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:保存成功消息
	 * @author CrissZhang
	 * @param tempLogMessage
	 * @throws ServiceException
	 * @return void
	 */
	public void saveSuccessLog(String tempLogMessage) throws ServiceException{
		try{
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(tempLogMessage);
		}catch (Exception e) {
			LOGGER.error(".deleteNewCaseData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:查看轉移案件筆數
	 * @author CrissZhang
	 * @param tempLogMessage
	 * @throws ServiceException
	 * @return void
	 */
	public Map queryTransferCaseNum(String transferCaseId) throws ServiceException{
		try{
			Map<String, Integer> resultMap = new HashMap<String, Integer>();
		//	String caseId = null;
			
			// Task #3066 只轉入特定案件編號案件
		//	caseId = "HS1712206527" + IAtomsConstants.MARK_SEPARATOR + "HS1712206528";
			
			// Task #3091 根據案件編號轉入案件
			List<Integer> result = this.srmCaseHandleInfoDAO.queryTransferCaseNum(transferCaseId);
			if(!CollectionUtils.isEmpty(result)){
				resultMap.put("transferCaseInfoNum", result.get(0));
				resultMap.put("transferCaseTransactionsNum", result.get(1));
				resultMap.put("transferCaseFilesNum", result.get(2));
			}
			return resultMap;
		}catch (Exception e) {
			LOGGER.error(".queryTransferCaseNum() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:查看轉移最新案件筆數
	 * @author CrissZhang
	 * @param tempLogMessage
	 * @throws ServiceException
	 * @return void
	 */
	public Map queryTransferNewCaseNum() throws ServiceException{
		try{
			Map<String, Integer> resultMap = new HashMap<String, Integer>();
			List<Integer> result = this.srmCaseNewHandleInfoDAO.queryTransferNewCaseNum();
			if(!CollectionUtils.isEmpty(result)){
				resultMap.put("transferCaseNewInfoNum", result.get(0));
				resultMap.put("transferCaseNewLinkNum", result.get(1));
			}
			return resultMap;
		}catch (Exception e) {
			LOGGER.error(".queryTransferNewCaseNum() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:獲取tms案件信息
	 * @author CrissZhang
	 * @throws ServiceException
	 * @return Map<String,SrmCaseHandleInfoDTO>
	 */
	private Map<String, SrmCaseHandleInfoDTO> getCaseInfoList(OldDataTransferFormDTO formDTO, Boolean repositoryHistFlag) throws ServiceException {
		// 可編輯字段的map集合
		Map<String,List<String>> editFildsMap = formDTO.getEditFildsMap();
		Map<String, SrmCaseHandleInfoDTO> resultMap = null;
		try {
			String filePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TRANSFER_PATH);
			LOGGER.debug(".getCaseInfoList()", "parameters : filePath=" + filePath);
		//	String filePath = "/home/cybersoft/iAtoms/dataTransfer/";
			if(StringUtils.hasText(filePath)){
				String iatomsTxtPath = filePath + File.separator + "iAtoms.txt";
				String okTxtPath = filePath + File.separator + "OK.txt";
				File iatomsTxtFile = new File(iatomsTxtPath);
				File okTxtFile = new File(okTxtPath);
				if(iatomsTxtFile.isFile() && iatomsTxtFile.exists()
						&& okTxtFile.isFile() && okTxtFile.exists()){
					// 构造一个BufferedReader类来读取文件 okTxtPath
					BufferedReader br = new BufferedReader(new FileReader(okTxtFile));
					String countString = null;
					String tempString = null;
					int caseCount = 0;
					int lineCount = 0;
					while((tempString = br.readLine())!=null){
						lineCount ++;
						countString = tempString;
					}
					br.close();
					if(lineCount == 1){
						String regex = "^\\d{10}$"; 
						if(StringUtils.hasText(countString) && Pattern.matches(regex, countString)){
							caseCount = Integer.parseInt(countString);
						}
					}
					// 构造一个BufferedReader类来读取文件 iatomsTxtPath
					br = new BufferedReader(new FileReader(iatomsTxtFile));
					String lineString = null;
					List<String> tmsInfoList = new ArrayList<String>();
					// 使用readLine方法，一次读一行
					lineCount = 0;
					while((lineString = br.readLine())!=null){
						lineCount ++;
						tmsInfoList.add(lineString);
					}
					br.close();
					if(tmsInfoList.size() == caseCount){
						// DTID char(10),MODEL_NAME char(20),軟體版本 char(30),COMM_MODE char(8)
						// LOGO char(5),表頭 char(5),雙模組模式 char(5),ECR連線 char(5),電子發票載具 char(5)
						// 是否開啟加密 char(5),電子化繳費平台 char(5),Enable CLSS(感應交易) char(5)
						// Pinpad Extern(外接PINPAD) char(5),銀聯閃付 char(10)
					//	int caseInfoLength = 123;//10 + 20 + 30 + 8 + 5 * 9 + 10;
						int caseInfoLength = 118;//10 + 20 + 30 + 8 + 5 * 9 + 5;
						// MID char(15),MID2 char(15),TID char(8),台新TID (綠界用) char(8)
						// 台新MID (綠界用) char(15),交易項目 char(15),卡別 char(25),PARAMETER char(35)
						int transParameterLength = 136;//15 + 15 + 8 + 8 + 15 + 15 + 25 + 35;
						// 交易參數總長度
						int transParameterTotalLength = 2420;
						
						// 案件信息集合
						String caseInfoString = null;
						// 案件交易參數集合
						String transParameterString = null;
						// 案件DTO
						SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = null;
						// 交易參數DTO
						SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO = null;
						// 交易參數集合DTO
						List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs = null;
						// 交易參數值集合
						StringBuilder itemValueBuilder = new StringBuilder();
						// 分割的交易參數
						String tempSubParameter = null;
						// 結果map
						resultMap = new HashMap<String, SrmCaseHandleInfoDTO>();
						// 臨時交易類別
						String tempTransType = null;
						// 臨時COMM_MODE
						String tempCommMode = null;
					//	List<String> commModeList = null;
						StringBuilder commModeBuilder = new StringBuilder();
						// 可編輯交易參數
						String tempEditFilds = null;
						// 寬頻鏈接
						String tempNetVendor = null;
						for(String tempTmsInfo : tmsInfoList){
							if(tempTmsInfo.length() >= caseInfoLength){
								srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
								// 案件信息
								caseInfoString = tempTmsInfo.substring(0, caseInfoLength);
								if (!repositoryHistFlag) {
									// DTID
									srmCaseHandleInfoDTO.setDtid(caseInfoString.substring(0, 10).trim());
									// MODEL_NAME
									// 軟體版本
									srmCaseHandleInfoDTO.setSoftwareVersion(caseInfoString.substring(30, 60).trim());
									// 放置結果
									resultMap.put(srmCaseHandleInfoDTO.getDtid(), srmCaseHandleInfoDTO);
								} else {
									// DTID
									srmCaseHandleInfoDTO.setDtid(caseInfoString.substring(0, 10).trim());
									// MODEL_NAME
									// 軟體版本
									srmCaseHandleInfoDTO.setSoftwareVersion(caseInfoString.substring(30, 60).trim());
									// COMM_MODE Dial-up = M,TCP/IP = L,3G、GPRS = G
									tempCommMode = caseInfoString.substring(60, 68).trim();
									// Task #2944
//									連線方式
//									Dial-up = M,TCP/IP = L,
//									3G、GPRS = G
//									新增下述判斷
//									TLS = MLG
//									ML、LM = Dial-up、TCP/IP
//									MG、GM = Dial-up、3G
									if(StringUtils.hasText(tempCommMode)){
										if("MLG".equals(tempCommMode.toUpperCase())){
											srmCaseHandleInfoDTO.setConnectionType("TLS");
										} else {
											commModeBuilder.delete(0, commModeBuilder.length());
										//	commModeList = StringUtils.toList(tempCommMode.toUpperCase(), ",");
											char[] commModeList= tempCommMode.toUpperCase().toCharArray();
										//	if(!CollectionUtils.isEmpty(commModeList)){
												for(int i=0; i < commModeList.length; i++){
													if(i != 0){
														commModeBuilder.append(",");
													}
													if("M".equals(String.valueOf(commModeList[i]))){
														commModeBuilder.append("Dial_Up");
													} else if ("L".equals(String.valueOf(commModeList[i]))){
														commModeBuilder.append("TCP_IP");
													} else if ("G".equals(String.valueOf(commModeList[i]))){
														commModeBuilder.append("3G");
													}
												}
										//	}
											srmCaseHandleInfoDTO.setConnectionType(commModeBuilder.toString());
										}
									}
									
									// LOGO
									if(IAtomsConstants.YES.equals(caseInfoString.substring(68, 73).trim())
											&& IAtomsConstants.YES.equals(caseInfoString.substring(73, 78).trim())){
										srmCaseHandleInfoDTO.setLogoStyle("LOGO_AND_MERCHANT_HEADER");
									} else if(IAtomsConstants.YES.equals(caseInfoString.substring(68, 73).trim())
											&& IAtomsConstants.NO.equals(caseInfoString.substring(73, 78).trim())){
										srmCaseHandleInfoDTO.setLogoStyle("ONLY_LOGO");
									} else if(IAtomsConstants.NO.equals(caseInfoString.substring(68, 73).trim())
											&& IAtomsConstants.YES.equals(caseInfoString.substring(73, 78).trim())){
										srmCaseHandleInfoDTO.setLogoStyle("ONLY_MERCHANT_HEADER");
									}
									// 雙模組模式
									if(IAtomsConstants.NO.equals(caseInfoString.substring(78, 83).trim())){
										srmCaseHandleInfoDTO.setMultiModule("NO_DOUBLE_MODULE");
									} else if(IAtomsConstants.YES.equals(caseInfoString.substring(78, 83).trim())){
										srmCaseHandleInfoDTO.setMultiModule("AUTOMATIC_SWITCH");
									}
									// ECR連線
									if(IAtomsConstants.NO.equals(caseInfoString.substring(83, 88).trim())){
										srmCaseHandleInfoDTO.setEcrConnection("noEcrLine");
									} else if(IAtomsConstants.YES.equals(caseInfoString.substring(83, 88).trim())){
										srmCaseHandleInfoDTO.setEcrConnection("haveEcrLine");
									}
									// 電子發票載具
									srmCaseHandleInfoDTO.setElectronicInvoice(caseInfoString.substring(88, 93).trim());
									// 是否開啟加密 
									srmCaseHandleInfoDTO.setIsOpenEncrypt(caseInfoString.substring(93, 98).trim());
									// 電子化繳費平台
									srmCaseHandleInfoDTO.setElectronicPayPlatform(caseInfoString.substring(98, 103).trim());
									// Enable CLSS(感應交易)
									srmCaseHandleInfoDTO.setEnableClss(caseInfoString.substring(103, 108).trim());
									// Pinpad Extern(外接PINPAD)
									srmCaseHandleInfoDTO.setPinpadExtern(caseInfoString.substring(108, 113).trim());
									// 銀聯閃付
								//	srmCaseHandleInfoDTO.setCupQuickPass(caseInfoString.substring(113, 123).trim());
									srmCaseHandleInfoDTO.setCupQuickPass(caseInfoString.substring(113, 118).trim());
									// 交易參數信息
								//	transParameterString = tempTmsInfo.substring(caseInfoLength, tempTmsInfo.length());
									if(tempTmsInfo.length() >= (caseInfoLength + transParameterTotalLength)){
										transParameterString = tempTmsInfo.substring(caseInfoLength, caseInfoLength + transParameterTotalLength);
									} else {
										transParameterString = tempTmsInfo.substring(caseInfoLength, tempTmsInfo.length());
									}
									
									// 寬頻連線
									if(tempTmsInfo.length() >= (caseInfoLength + transParameterTotalLength + 15)){
										tempNetVendor = tempTmsInfo.substring(caseInfoLength + transParameterTotalLength, caseInfoLength + transParameterTotalLength + 15);
									} else {
										tempNetVendor = null;
									}
									if(StringUtils.hasText(tempNetVendor)){
										if("CHT".equals(tempNetVendor.trim().toUpperCase())){
											srmCaseHandleInfoDTO.setNetVendorId("2");
										} else if("FET".equals(tempNetVendor.trim().toUpperCase())){
											srmCaseHandleInfoDTO.setNetVendorId("9");
										} else if("TWM".equals(tempNetVendor.trim().toUpperCase())){
											srmCaseHandleInfoDTO.setNetVendorId("10");
										} else if("SaveCom".toUpperCase().equals(tempNetVendor.trim().toUpperCase())){
											srmCaseHandleInfoDTO.setNetVendorId("7");
										} else if("DHCP".toUpperCase().equals(tempNetVendor.trim().toUpperCase())){
											srmCaseHandleInfoDTO.setNetVendorId("11");
										}
									}
									
									if(transParameterString.length() != 0){
										srmCaseTransactionParameterDTOs = new ArrayList<SrmCaseTransactionParameterDTO>();
										// 處理交易參數集合
										for(int i = 0; i < transParameterString.length(); i += transParameterLength){
											tempSubParameter = transParameterString.substring(i, i + transParameterLength);
											if(!StringUtils.hasText(tempSubParameter)){
												break;
											}
											srmCaseTransactionParameterDTO = new SrmCaseTransactionParameterDTO();
											itemValueBuilder.delete(0, itemValueBuilder.length());
											// MID char(15),MID2 char(15),TID char(8),台新TID (綠界用) char(8)
											// 台新MID (綠界用) char(15),交易項目 char(15),卡別 char(25),PARAMETER char(35)
											// MID
											srmCaseTransactionParameterDTO.setMerchantCode(transParameterString.substring(0 + i, 15 + i).trim());
											// MID2
											srmCaseTransactionParameterDTO.setMerchantCodeOther(transParameterString.substring(15 + i, 30 + i).trim());
											// TID
											srmCaseTransactionParameterDTO.setTid(transParameterString.substring(30 + i, 38 + i).trim());
											itemValueBuilder.append("{");
											// 台新TID (綠界用)
											itemValueBuilder.append("\"TSTID\"").append(":").append("\"").append(transParameterString.substring(38 + i, 46 + i).trim()).append("\"");
											// 台新MID (綠界用)
											itemValueBuilder.append(",").append("\"TSMID\"").append(":").append("\"").append(transParameterString.substring(46 + i, 61 + i).trim()).append("\"");
											// 交易項目 一般交易(G)\分期紅利(IN)\紅利交易(RE)\AE\DINERS\DCC\CUP\MailOrder(MO)\Smartpay
											if("G".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												// 
												if(StringUtils.hasText(transParameterString.substring(76 + i, 101 + i).trim())){
													// 一般VM
													if((StringUtils.toList(transParameterString.substring(76 + i, 101 + i).trim(), ",")).size() == 2){
														srmCaseTransactionParameterDTO.setTransactionType("COMMON_VM");
													// 一般VMJ
													} else if((StringUtils.toList(transParameterString.substring(76 + i, 101 + i).trim(), ",")).size() == 3){
														srmCaseTransactionParameterDTO.setTransactionType("COMMON_VMJ");
													// 一般VMJU
													} else if((StringUtils.toList(transParameterString.substring(76 + i, 101 + i).trim(), ",")).size() == 4){
														srmCaseTransactionParameterDTO.setTransactionType("COMMON_VMJU");
													}
												}
											} else if("IN".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("Installment(ChoiceCard)");
											} else if("RE".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("BONUS");
											// 	Bug #2649 AMEX --> AE Task #2864 AE --> AE
											} else if("AMEX".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())
													|| "AE".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("AE");
											} else if("DINERS".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("DINERS");
											} else if("DCC".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("DCC");
											} else if("CUP".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("CUP");
											} else if("MO".equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("MailOrder");
											} else if("Smartpay".toUpperCase().equals(transParameterString.substring(61 + i, 76 + i).trim().toUpperCase())){
												srmCaseTransactionParameterDTO.setTransactionType("SmartPay");
											}
											// 若案件類別爲空，跳出
											if(!StringUtils.hasText(srmCaseTransactionParameterDTO.getTransactionType())){
//												Bug #2649
												continue;
											}
											// PARAMETER
											if((transParameterString.length()-101) >= 20){
												if(!CollectionUtils.isEmpty(editFildsMap) && StringUtils.hasText(srmCaseTransactionParameterDTO.getTransactionType())
														&& editFildsMap.containsKey(srmCaseTransactionParameterDTO.getTransactionType())){
													// 得到每項交易參數可編輯字段
													tempEditFilds = editFildsMap.get(srmCaseTransactionParameterDTO.getTransactionType()).toString();
												}
												if(StringUtils.hasText(tempEditFilds)){
													// 銷售交易
													if(String.valueOf(1).equals(transParameterString.substring(101 + i, 102 + i))){
														if(tempEditFilds.contains("saleTransaction")){
															itemValueBuilder.append(",").append("\"saleTransaction\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"saleTransaction\"").append(":").append("\"V\"");
													}
													// 取消交易
													if(String.valueOf(1).equals(transParameterString.substring(102 + i, 103 + i))){
														if(tempEditFilds.contains("cancelTransaction")){
															itemValueBuilder.append(",").append("\"cancelTransaction\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"cancelTransaction\"").append(":").append("\"V\"");
													}
													// 結帳交易
													if(String.valueOf(1).equals(transParameterString.substring(103 + i, 104 + i))){
														if(tempEditFilds.contains("checkoutTransaction")){
															itemValueBuilder.append(",").append("\"checkoutTransaction\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"checkoutTransaction\"").append(":").append("\"V\"");
													}
													// 退貨交易
													if(String.valueOf(1).equals(transParameterString.substring(104 + i, 105 + i))){
														if(tempEditFilds.contains("returnTransaction")){
															itemValueBuilder.append(",").append("\"returnTransaction\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"returnTransaction\"").append(":").append("\"V\"");
													}
													// Task #2722 人工輸入
													if(String.valueOf(1).equals(transParameterString.substring(105 + i, 106 + i))){
														if(tempEditFilds.contains("manualInput")){
															itemValueBuilder.append(",").append("\"manualInput\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"returnTransaction\"").append(":").append("\"V\"");
													}
													// 交易補登
													if(String.valueOf(1).equals(transParameterString.substring(106 + i, 107 + i))){
														if(tempEditFilds.contains("transactionFill")){
															itemValueBuilder.append(",").append("\"transactionFill\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"transactionFill\"").append(":").append("\"V\"");
													}
													// 交易補登(Online)
													if(String.valueOf(1).equals(transParameterString.substring(107 + i, 108 + i))){
														if(tempEditFilds.contains("transactionFillOnline")){
															itemValueBuilder.append(",").append("\"transactionFillOnline\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"transactionFillOnline\"").append(":").append("\"V\"");
													}
													// 開放櫃號
													if(String.valueOf(1).equals(transParameterString.substring(108 + i, 109 + i))){
														if(tempEditFilds.contains("openNumber")){
															itemValueBuilder.append(",").append("\"openNumber\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"openNumber\"").append(":").append("\"V\"");
													}
													// 調整金額
													if(String.valueOf(1).equals(transParameterString.substring(109 + i, 110 + i))){
														if(tempEditFilds.contains("adjustmentAmount")){
															itemValueBuilder.append(",").append("\"adjustmentAmount\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"adjustmentAmount\"").append(":").append("\"V\"");
													}
													// 預先授權
													if(String.valueOf(1).equals(transParameterString.substring(110 + i, 111 + i))){
														if(tempEditFilds.contains("preAuthorization")){
															itemValueBuilder.append(",").append("\"preAuthorization\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"preAuthorization\"").append(":").append("\"V\"");
													}
													// 預授完成
													if(String.valueOf(1).equals(transParameterString.substring(111 + i, 112 + i))){
														if(tempEditFilds.contains("preAuthorized")){
															itemValueBuilder.append(",").append("\"preAuthorized\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"preAuthorized\"").append(":").append("\"V\"");
													}
													// 銀聯預先授權取消
													if(String.valueOf(1).equals(transParameterString.substring(112 + i, 113 + i))){
														if(tempEditFilds.contains("cupCancelPreAuth")){
															itemValueBuilder.append(",").append("\"cupCancelPreAuth\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"cupCancelPreAuth\"").append(":").append("\"V\"");
													}
													// 銀聯預先授權完成取消
													if(String.valueOf(1).equals(transParameterString.substring(113 + i, 114 + i))){
														if(tempEditFilds.contains("cupCancelPreAuthed")){
															itemValueBuilder.append(",").append("\"cupCancelPreAuthed\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"cupCancelPreAuthed\"").append(":").append("\"V\"");
													}
													// 自動撥號
													if(String.valueOf(1).equals(transParameterString.substring(114 + i, 115 + i))){
														if(tempEditFilds.contains("autoCall")){
															itemValueBuilder.append(",").append("\"autoCall\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"autoCall\"").append(":").append("\"V\"");
													}
													// 小費交易
													if(String.valueOf(1).equals(transParameterString.substring(115 + i, 116 + i))){
														if(tempEditFilds.contains("tipTransaction")){
															itemValueBuilder.append(",").append("\"tipTransaction\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"tipTransaction\"").append(":").append("\"V\"");
													}
													// 4DBC
													if(String.valueOf(1).equals(transParameterString.substring(116 + i, 117 + i))){
														if(tempEditFilds.contains("4DBC")){
															itemValueBuilder.append(",").append("\"4DBC\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"4DBC\"").append(":").append("\"V\"");
													}
													// VEPS(免簽)$800
													if(String.valueOf(1).equals(transParameterString.substring(117 + i, 118 + i))){
														if(tempEditFilds.contains("veps")){
															itemValueBuilder.append(",").append("\"veps\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"veps\"").append(":").append("\"V\"");
													}
													// MASTER(免簽)S1,500
													if(String.valueOf(1).equals(transParameterString.substring(118 + i, 119 + i))){
														if(tempEditFilds.contains("master")){
															itemValueBuilder.append(",").append("\"master\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"master\"").append(":").append("\"V\"");
													}
													// JCB(免簽)$700
													if(String.valueOf(1).equals(transParameterString.substring(119 + i, 120 + i))){
														if(tempEditFilds.contains("jcb")){
															itemValueBuilder.append(",").append("\"jcb\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"jcb\"").append(":").append("\"V\"");
													}
													// CUP(免簽)S800
													if(String.valueOf(1).equals(transParameterString.substring(120 + i, 121 + i))){
														if(tempEditFilds.contains("cup")){
															itemValueBuilder.append(",").append("\"cup\"").append(":").append("\"V\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													//	itemValueBuilder.append(",").append("\"cup\"").append(":").append("\"V\"");
													}
													// 分行代碼
													if(StringUtils.hasText(transParameterString.substring(121 + i, 125 + i))){
														if(tempEditFilds.contains("branch")){
															itemValueBuilder.append(",").append("\"branch\"").append(":").append("\"").append(transParameterString.substring(121 + i, 125 + i)).append("\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													}
													// MCC
													if(StringUtils.hasText(transParameterString.substring(125 + i, 129 + i))){
														if(tempEditFilds.contains("MCC")){
															itemValueBuilder.append(",").append("\"MCC\"").append(":").append("\"").append(transParameterString.substring(125 + i, 129 + i)).append("\"");
														} else {
															LOGGER.error(".getCaseInfoList() ConflictEditFild(違反編輯字段) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
														}
													}
												} else {
													LOGGER.error(".getCaseInfoList() EmptyEditFild(編輯字段爲空) dtid:" + srmCaseHandleInfoDTO.getDtid() + ",transactionType:" + srmCaseTransactionParameterDTO.getTransactionType());
												}
												itemValueBuilder.append("}");
											}
											srmCaseTransactionParameterDTO.setItemValue(itemValueBuilder.toString());
											// 放到交易參數集合
											srmCaseTransactionParameterDTOs.add(srmCaseTransactionParameterDTO);
										}
									}
									if(!CollectionUtils.isEmpty(srmCaseTransactionParameterDTOs) && srmCaseTransactionParameterDTOs.size() != 1){
										// 臨時交易類別
										tempTransType = srmCaseTransactionParameterDTOs.get(0).getTransactionType();
										for(int i = 1; i < srmCaseTransactionParameterDTOs.size(); i++){
											if(StringUtils.hasText(tempTransType) && tempTransType.equals(srmCaseTransactionParameterDTOs.get(i).getTransactionType())){
												LOGGER.error(".getCaseInfoList() RepeatTransactionType(交易類別重復) dtid:", srmCaseTransactionParameterDTOs.get(i).getDTID());
											}
										}
									}
									srmCaseHandleInfoDTO.setCaseTransactionParameterDTOs(srmCaseTransactionParameterDTOs);
									// 放置結果
									resultMap.put(srmCaseHandleInfoDTO.getDtid(), srmCaseHandleInfoDTO);
								}						
							}
						}
					} else {
						this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "TMS Folder下的資料筆數不符，請重新檢查后轉入").append("</br>");
						LOGGER.error(".getCaseInfoList() dataError");
					}
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "TMS Folder下的文檔缺失，請重新檢查后轉入").append("</br>");
				}
			} else {
				LOGGER.error(".getCaseInfoList() pathError --> path is not exist!!!");
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "config文件配置有誤").append("</br>");
				}
			}
		} catch (Exception e) {
			LOGGER.error(".getCaseInfoList(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return resultMap;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferAdmUser(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext transferAdmUser(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			// 刪除使用者信息
			this.admUserDAO.deleteTransferData();
			// 當前所有用戶列表
			this.userList = this.admUserDAO.getUserAll();
			
			// 得到使用者帳號集合
			List<AdmUser> admUsers = this.oldDataTransferDAO.listAdmUser();
			if(!CollectionUtils.isEmpty(admUsers)){
				int i = 0;
				// 臨時使用者賬號
				String tempUserId = null;
				// 遍歷保存
				for(AdmUser admUser : admUsers){
					tempUserId = null;
					if(StringUtils.hasText(admUser.getCname()) && !CollectionUtils.isEmpty(this.userList)){
						for (Parameter param : this.userList){
							if((param.getName()).equals(admUser.getCname().trim())){
								tempUserId = (String) param.getValue();
								break;
							}
						}
					}
					if(!StringUtils.hasText(tempUserId)){
						i++;
						admUser.setId(StringUtils.toFixString(10, i));
						admUser.setAccount(IAtomsConstants.MARK_SPACE);
						admUser.setDeleted(IAtomsConstants.NO);
						// 保存使用者帳號資料
						this.admUserDAO.insert(admUser);
					}
				}
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "使用者帳號資料轉入成功").append("</br>");
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無使用者帳號資料轉入").append("</br>");
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferAdmUser"));
			
			LOGGER.error(".transferAdmUser() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferAdmUser"));
			
			LOGGER.error(".transferAdmUser() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	/**
	 * Purpose:通过name值得到value值，若得不到給出錯誤信息
	 * @author CrissZhang
	 * @param parameterList:下拉框列表
	 * @param name ：name值
	 * @return String
	 */
	private String getValueByName(String initName, String initCode) throws ServiceException {
		List<Parameter> parameterList = null;
		if(IATOMS_PARAM_TYPE.LOCATION.getCode().equals(initCode)){
			// 市縣區域
			if(CollectionUtils.isEmpty(this.locationList)){
				this.locationList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.LOCATION.getCode(), null);
			}
			parameterList = this.locationList;
		} else if(CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue().equals(initCode)){
			// 公司代號集合
			if(CollectionUtils.isEmpty(this.companyCodeList) || (!CollectionUtils.isEmpty(this.companyCodeList) && this.companyCodeList.size() == 1)){
				this.companyCodeList = this.companyDAO.getCompanyList(true);
			}
			parameterList = this.companyCodeList;
		} else if(CompanyDTO.ATTRIBUTE.SHORT_NAME.getValue().equals(initCode)){
			// 公司簡稱集合
			if(CollectionUtils.isEmpty(this.companyNameList) || (!CollectionUtils.isEmpty(this.companyNameList) && this.companyNameList.size() == 1)){
				this.companyNameList = this.companyDAO.getCompanyList(false);
			}
			parameterList = this.companyNameList;
		} else if(IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode().equals(initCode)){
			// 支援功能
			if(CollectionUtils.isEmpty(this.supportedFunctionList)){
				this.supportedFunctionList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.SUPPORTED_FUNCTION.getCode(), null);
			}
			parameterList = this.supportedFunctionList;
		} else if(IATOMS_PARAM_TYPE.COMM_MODE.getCode().equals(initCode)){
			// 通訊方式
			if(CollectionUtils.isEmpty(this.supportedCommList)){
				this.supportedCommList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.COMM_MODE.getCode(), null);
			}
			parameterList = this.supportedCommList;
		} else if("assetType".equals(initCode)){
			// 設備品項
			if(!CollectionUtils.isEmpty(this.assetTypeList)){
				parameterList = this.assetTypeList;
			}
		} else if("edcType".equals(initCode)){
			// edc設備品項
			if(!CollectionUtils.isEmpty(this.edcTypeList)){
				parameterList = this.edcTypeList;
			}
		} else if("edcType".equals(initCode)){
			// edc設備品項
			if(!CollectionUtils.isEmpty(this.edcTypeList)){
				parameterList = this.edcTypeList;
			}
		} else if("Related_Products".equals(initCode)){
			// 周邊設備
			if(!CollectionUtils.isEmpty(this.peripheralsList)){
				parameterList = this.peripheralsList;
			}
		} else if (WarehouseDTO.ATTRIBUTE.WAREHOUSE_ID.getValue().equals(initCode)) {
			// 倉庫據點
			if(CollectionUtils.isEmpty(this.warehouseList)){
				this.warehouseList = this.warehouseDAO.listWarehouseByName(null);
			}
			parameterList = this.warehouseList;
		} else if (BimContractDTO.ATTRIBUTE.CONTRACT_ID.getValue().equals(initCode)) {
			// 合約id
			if(CollectionUtils.isEmpty(this.contractList)){
				this.contractList = this.contractDAO.getContractByCode(null);
			}
			parameterList = this.contractList;
		} else if(IATOMS_PARAM_TYPE.MA_TYPE.getCode().equals(initCode)){
			// 維護模式
			if(CollectionUtils.isEmpty(this.maTypeList)){
				this.maTypeList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.MA_TYPE.getCode(), null);
			}
			parameterList = this.maTypeList;
		}  else if(IATOMS_PARAM_TYPE.ASSET_STATUS.getCode().equals(initCode)){
			// 設備狀態 
			if(CollectionUtils.isEmpty(this.assetStatusList)){
				this.assetStatusList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.ASSET_STATUS.getCode(), null);
			}
			parameterList = this.assetStatusList;
		}  else if(IATOMS_PARAM_TYPE.ACTION.getCode().equals(initCode)){
			// 執行作業
			if(CollectionUtils.isEmpty(this.actionList)){
				this.actionList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.ACTION.getCode(), null);
			}
			parameterList = this.actionList;
		}  else if(IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode().equals(initCode)){
			// 故障組件
			if(CollectionUtils.isEmpty(this.faultComponentList)){
				this.faultComponentList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.FAULT_COMPONENT.getCode(), null);
			}
			parameterList = this.faultComponentList;
		}  else if(IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode().equals(initCode)){
			// 故障說明
			if(CollectionUtils.isEmpty(this.faultDescriptionList)){
				this.faultDescriptionList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.FAULT_DESCRIPTION.getCode(), null);
			}
			parameterList = this.faultDescriptionList;
		}  else if(IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode().equals(initCode)){
			// 裝機類型
			if(CollectionUtils.isEmpty(this.installTypeList)){
				this.installTypeList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.INSTALL_TYPE.getCode(), null);
			}
			parameterList = this.installTypeList;
		}  else if(IATOMS_PARAM_TYPE.RETIRE_REASON.getCode().equals(initCode)){
			// 報廢原因
			if(CollectionUtils.isEmpty(this.retireReasonList)){
				this.retireReasonList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.RETIRE_REASON.getCode(), null);
			}
			parameterList = this.retireReasonList;
		}  else if(AdmUserDTO.ATTRIBUTE.USER_ID.getValue().equals(initCode)){
			// 使用者帳號
			if(CollectionUtils.isEmpty(this.userList)){
				this.userList = this.admUserDAO.getUserAll();
			}
			parameterList = this.userList;
		} else if(BimDepartmentDTO.ATTRIBUTE.DEPT_CODE.getValue().equals(initCode)){
			// 部門
			if(CollectionUtils.isEmpty(this.departmentList)){
				this.departmentList = (List<Parameter>)this.departmentDAO.getDepts(null);
			}
			parameterList = this.departmentList;
		} else if("edcCategory".equals(initCode)){
			// edc設備品項
			if(CollectionUtils.isEmpty(this.edcCategoryList)){
				parameterList = this.assetTypeDAO.listAssetByNameAndType(null, true, false);
			} else {
				parameterList = this.edcCategoryList;
			}
			if(CollectionUtils.isEmpty(parameterList)){
				parameterList = new ArrayList<Parameter>();
			}
		} else if("RelatedProductsCategory".equals(initCode)){
			// 周邊設備設備品項
			if(CollectionUtils.isEmpty(this.RelatedProductsCategoryList)){
				parameterList = this.assetTypeDAO.listAssetByNameAndType(null, false, true);
			} else {
				parameterList = this.RelatedProductsCategoryList;
			}
			if(CollectionUtils.isEmpty(parameterList)){
				parameterList = new ArrayList<Parameter>();
			}
		}
		
		String value = null;
		if(StringUtils.hasText(initName) && !CollectionUtils.isEmpty(parameterList)){
			String initNameTrimString = initName.trim();
			for (Parameter param : parameterList){
				if((param.getName()).equals(initNameTrimString)){
					value = (String) param.getValue();
					break;
				}
			}
		}
		if(StringUtils.hasText(initName) && !StringUtils.hasText(value)){
			LOGGER.error(".getValueByName() haveNoMatch", "initCode:" + initCode, "initName:" + initName);
		}
		return value;
	}
	
	/**
	 * Purpose:讀取程式版本txt文檔內容
	 * @author CrissZhang
	 * @return List<Parameter> ： 返回一個Parameter集合
	 */
	private List<ApplicationDTO> getApplicationDtoList() throws ServiceException {
		List<ApplicationDTO> result = null;
		try {
			String filePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TRANSFER_PATH);
			LOGGER.debug(".getApplicationList()", "parameters : filePath=" + filePath);
			if(StringUtils.hasText(filePath)){
				String appTxtPath = filePath + File.separator + "Application.txt";
				String okTxtPath = filePath + File.separator + "app_OK.txt";
				File appTxtFile = new File(appTxtPath);
				File okTxtFile = new File(okTxtPath);
				if(appTxtFile.isFile() && appTxtFile.exists()
						&& okTxtFile.isFile() && okTxtFile.exists()){
					// 构造一个BufferedReader类来读取文件 okTxtPath
					BufferedReader br = new BufferedReader(new FileReader(okTxtFile));
					String countString = null;
					String tempString = null;
					int caseCount = 0;
					int lineCount = 0;
					while((tempString = br.readLine())!=null){
						lineCount ++;
						countString = tempString;
					}
					br.close();
					if(lineCount == 1){
						String regex = "^\\d{10}$"; 
						if(StringUtils.hasText(countString) && Pattern.matches(regex, countString)){
							caseCount = Integer.parseInt(countString);
						}
					}
					// 构造一个BufferedReader类来读取文件 iatomsTxtPath
					br = new BufferedReader(new FileReader(appTxtFile));
					String lineString = null;
					List<String> tmsInfoList = new ArrayList<String>();
					// 使用readLine方法，一次读一行
					lineCount = 0;
					while((lineString = br.readLine())!=null){
						lineCount ++;
						tmsInfoList.add(lineString);
					}
					br.close();
					if(tmsInfoList.size() == caseCount){
						result = new ArrayList<ApplicationDTO>();
						Map<String, String> tempMap = new HashMap<String, String>();
						ApplicationDTO applicationDTO = null;
						Parameter parameter = null;
						
						lineString = null;
						String lineName = null;
						String lineValue = null;
						String lineDate = null;
						Timestamp createDate = null;
						// 使用readLine方法，一次读一行
						br = new BufferedReader(new FileReader(appTxtFile));
						while((lineString = br.readLine())!=null){
							if(StringUtils.hasText(lineString) && lineString.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) > 0 && lineString.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT) > 0
									&& (lineString.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) != lineString.lastIndexOf(IAtomsConstants.MARK_BRACKET_LEFT))
									&& (lineString.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT) != lineString.lastIndexOf(IAtomsConstants.MARK_BRACKET_RIGHT))){
								// 當前行程式名稱
								lineName = lineString.substring(0, lineString.indexOf(IAtomsConstants.MARK_BRACKET_LEFT));
								// 當前行程式版本
								lineValue = lineString.substring(lineString.indexOf(IAtomsConstants.MARK_BRACKET_LEFT) + 1, lineString.indexOf(IAtomsConstants.MARK_BRACKET_RIGHT));
								// 當前行程式創建日期
								lineDate = lineString.substring(lineString.lastIndexOf(IAtomsConstants.MARK_BRACKET_LEFT) + 1, lineString.lastIndexOf(IAtomsConstants.MARK_BRACKET_RIGHT));
								createDate = DateTimeUtils.toTimestamp(lineDate);
								applicationDTO = new ApplicationDTO();
								applicationDTO.setApplicationId(lineName);
								applicationDTO.setVersion(lineValue);
								applicationDTO.setCreatedDate(createDate);
								result.add(applicationDTO);
							}
						}
						br.close();
						LOGGER.debug(".getApplicationDtoList()", "parameters : result=" + result);
					} else {
						LOGGER.error(".getApplicationDtoList() dataError:程式版本與OK文件的資料筆數不符，請重新檢查后轉入");
						if(this.resultMsg != null){
							this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "程式版本與OK文件的資料筆數不符，請重新檢查后轉入").append("</br>");
						}
					}
				} else {
					LOGGER.error(".getApplicationDtoList() batchMessage：未找到程式版本TMS資料或OK文件 file is not exists!!!");
					if(this.resultMsg != null){
						this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "未找到程式版本TMS資料或OK文件").append("</br>");
					}
				}
			} else {
				LOGGER.error(".getApplicationDtoList() pathError --> path is not exist!!!");
				if(this.resultMsg != null){
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "config文件配置有誤").append("</br>");
				}
			}
		} catch (Exception e) {
			LOGGER.error(".getApplicationDtoList()", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return result;
	}
	/**
	 * Purpose: 參數加入集合後去重得到一個字符串
	 * @author CrissZhang
	 * @param tempList ：傳入臨時使用集合
	 * @param param ： 單個參數
	 * @return String ： 返回去重結果的字符串
	 */
	private String removeDuplicate(List<String> tempList, String param){
		LOGGER.debug(".removeDuplicate()", "parameters : tempList=" + tempList);
		LOGGER.debug(".removeDuplicate()", "parameters : param=" + param);
		String result = null;
		// 判斷集合是否有值
		if(!CollectionUtils.isEmpty(tempList)){
			// 新建List對象 用來存儲無重復的結果集合
			List<String> resultList = new ArrayList<String>();
			// 若傳入的參數有值則添加當前參數置需要處理的集合裏
			if(StringUtils.hasText(param)){
			//	tempList.add(param.toUpperCase());
				tempList.add(param);
			}
			boolean flag = false;
			// 處理集合的去重 將不重復的值放置新的集合中
			for(String temp : tempList){  
				/*if(!resultList.contains(temp.toUpperCase()) && !resultList.contains(temp) && !resultList.contains(temp.toLowerCase())){  
					resultList.add(temp);
				}*/
				flag = false;
				for(String str : resultList){
					if(str.toUpperCase().equals(temp.toUpperCase())
							|| str.replace(IAtomsConstants.MARK_SPACE, IAtomsConstants.MARK_EMPTY_STRING).equals(temp.replace(IAtomsConstants.MARK_SPACE, IAtomsConstants.MARK_EMPTY_STRING))){
						flag = true;
						break;
					}
				}
				if(!flag){
					resultList.add(temp);
				}
			}
			// 如果結果的集合對象不爲空 則處理結合對象
			if(!CollectionUtils.isEmpty(resultList)){
				result = resultList.toString();
				// 處理tostring結果
				result = result.substring(1, result.length() - 1);
			}
		} else {
			result = param;
		}
		LOGGER.debug(".removeDuplicate()", "parameters : result=" + result);
		return result;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#uploadNotClosedCaseInfo(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext uploadNotClosedCaseInfo(SessionContext sessionContext) throws ServiceException {
		OldDataTransferFormDTO formDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
		try {
			String caseCategory = formDTO.getCaseCategory();
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = null;
			if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory) 
					|| IAtomsConstants.CASE_CATEGORY.UPDATE.getCode().equals(caseCategory)) {
				caseHandleInfoDTOs = this.oldDataTransferDAO.listNotCloseCaseInstallOrUpdate(caseCategory);
			} else {
				caseHandleInfoDTOs = this.oldDataTransferDAO.listNotCloseCaseOthers(caseCategory);
			}
			String tempCustomerId = null;
			if (IAtomsConstants.CASE_CATEGORY.INSTALL.getCode().equals(caseCategory)) {
				// 若無公司信息則去DB查詢
				this.companyCodeList = this.companyDAO.getCompanyList(Boolean.FALSE);
				// 綁定合約與公司信息
				List<BimContractDTO> contractDTOs = this.contractDAO.getContractList();
				if(!CollectionUtils.isEmpty(contractDTOs)){
				this.contractMap = new HashMap<String, List<Parameter>>();
				List<Parameter> tempContractList = null;
				Parameter tempParameter = null;
					for(BimContractDTO contractDTO : contractDTOs){
						if(StringUtils.hasText(contractDTO.getCompanyId())){
							tempParameter = new Parameter(contractDTO.getContractCode(), contractDTO.getContractId());
							if(this.contractMap.containsKey(contractDTO.getCompanyId())){
								tempContractList = this.contractMap.get(contractDTO.getCompanyId());
							} else {
								tempContractList = new ArrayList<Parameter>();
							}
							tempContractList.add(tempParameter);
							this.contractMap.put(contractDTO.getCompanyId(), tempContractList);
						}
					}
				}
				for (SrmCaseHandleInfoDTO srmCaseHandleInfoDTO : caseHandleInfoDTOs) {
					tempCustomerId = getValueByName(srmCaseHandleInfoDTO.getCustomerName(), CompanyDTO.ATTRIBUTE.COMPANY_CODE.getValue());
					// 合約ID
					if(StringUtils.hasText(tempCustomerId) && !CollectionUtils.isEmpty(this.contractMap)
							&& (!CollectionUtils.isEmpty(this.contractMap.get(tempCustomerId)))){
						srmCaseHandleInfoDTO.setContractCode((String)((this.contractMap.get(tempCustomerId)).get(0).getName()));
					}
					// 刷卡機類型
					if(StringUtils.hasText(srmCaseHandleInfoDTO.getEdcTypeName()) && this.fomsAssetTypeMap.containsKey(srmCaseHandleInfoDTO.getEdcTypeName())){
						srmCaseHandleInfoDTO.setEdcTypeName(this.fomsAssetTypeMap.get(srmCaseHandleInfoDTO.getEdcTypeName()));
					}
				}
			}
			
			formDTO.setSrmCaseHandleInfoDTOs(caseHandleInfoDTOs);
			
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.QUERY_SUCCESS);
			sessionContext.setReturnMessage(msg);
			sessionContext.setResponseResult(formDTO);
		} catch (DataAccessException e) {
			LOGGER.error(".uploadNotClosedCaseInfo() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".uploadNotClosedCaseInfo() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		return sessionContext;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#transferProblemData(cafe.core.context.SessionContext)
	 */
	public SessionContext transferProblemData(SessionContext sessionContext) throws ServiceException {
		try {
			// 清空返回消息值
			this.resultMsg = new StringBuilder();
			
			// 拼接當前轉入消息
			OldDataTransferFormDTO oldDataTransferFormDTO = (OldDataTransferFormDTO) sessionContext.getRequestParameter();
			this.PARAM_LOG_MESSAGE.append(oldDataTransferFormDTO.getLogMessage()).append("</br>");
			
			StringBuilder bptdCodeBuilder = new StringBuilder();
			bptdCodeBuilder.append("PROBLEM_REASON_EDC");
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_REASON_LINE");
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_REASON_MER");
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_REASON_OTHER");
			
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_SOLUTION_EDC");
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_SOLUTION_LINE");
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_SOLUTION_MER");
			bptdCodeBuilder.append(IAtomsConstants.MARK_SEPARATOR);
			bptdCodeBuilder.append("PROBLEM_SOLUTION_OTHER");
			// 刪除基本參數信息
			this.baseParameterManagerDAO.deleteTransferData(bptdCodeBuilder.toString());

			// 儲存基本參數對象
			BaseParameterItemDef baseParameterItemDef = null;
			// 儲存基本參數對象Id
			BaseParameterItemDefId baseParameterItemDefId = null;
			// 固定的生效日期
			Date effectiveDate = DateTimeUtils.parseDate(IAtomsConstants.STATIC_VERSION_DATE_FOR_BASE_PARAMETER, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH);
			int i = 0;
			// 報修問題數據
			List<BaseParameterItemDefDTO> problemReasonDTOs = this.oldDataTransferDAO.listProblemReasonData();
			if(!CollectionUtils.isEmpty(problemReasonDTOs)){
				Map<String, List<String>> problemReasonMap = new HashMap<String, List<String>>();
				List<String> tempProblemReasonList = null;
				// 將報修問題按類別放入map當中
				for(BaseParameterItemDefDTO baseParameterItemDefDTO : problemReasonDTOs){
					if(StringUtils.hasText(baseParameterItemDefDTO.getBptdCode())){
						if(problemReasonMap.containsKey(baseParameterItemDefDTO.getBptdCode())){
							tempProblemReasonList = problemReasonMap.get(baseParameterItemDefDTO.getBptdCode());
						} else {
							tempProblemReasonList = new ArrayList<String>();
						}
						tempProblemReasonList.add(baseParameterItemDefDTO.getItemName());
						problemReasonMap.put(baseParameterItemDefDTO.getBptdCode(), tempProblemReasonList);
					}
				}
				// 報修問題組件主鍵
				String problemReasonId = null;
				if(!CollectionUtils.isEmpty(problemReasonMap)){
					// 遍歷map存儲不同類別的報修問題
					for(String key : problemReasonMap.keySet()){
						tempProblemReasonList = problemReasonMap.get(key);
						// 儲存每個類別的報修問題
						if(!CollectionUtils.isEmpty(tempProblemReasonList)){
							i = 0;
							for(String tempName : tempProblemReasonList){
								i ++;
								// id拼接
								if(i > 9){
									problemReasonId = key + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i);
								} else {
									problemReasonId = key + IAtomsConstants.MARK_UNDER_LINE + String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i);
								}
								baseParameterItemDefId = new BaseParameterItemDefId(problemReasonId, key, effectiveDate);
								baseParameterItemDef = new BaseParameterItemDef();
								// 主鍵
								baseParameterItemDef.setId(baseParameterItemDefId);
								// 參數項目
								baseParameterItemDef.setItemName(tempName);
								// 參數項目值
								if(i > 9){
									baseParameterItemDef.setItemValue(String.valueOf(i));
								} else {
									// 2位數字，不足補0
									baseParameterItemDef.setItemValue(String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i));
								}
								// 參數項目順序
								baseParameterItemDef.setItemOrder(i);
								// 是否已核可
		//						baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
								// 新增日期
						//		baseParameterItemDef.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
								// 異動日期
								baseParameterItemDef.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								// 刪除標記
								baseParameterItemDef.setDeleted(IAtomsConstants.NO);
								this.baseParameterManagerDAO.insert(baseParameterItemDef);
							}
						}
					}
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "報修問題資料轉入成功").append("</br>");
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無報修問題資料轉入").append("</br>");
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無報修問題資料轉入").append("</br>");
			}
			// 報修解決方式數據
			List<BaseParameterItemDefDTO> problemSolutionDTOs = this.oldDataTransferDAO.listProblemSolutionData();
			if(!CollectionUtils.isEmpty(problemSolutionDTOs)){
				Map<String, List<String>> problemSolutionMap = new HashMap<String, List<String>>();
				List<String> tempProblemSolutionList = null;
				// 將報修問題按類別放入map當中
				for(BaseParameterItemDefDTO baseParameterItemDefDTO : problemSolutionDTOs){
					if(StringUtils.hasText(baseParameterItemDefDTO.getBptdCode())){
						if(problemSolutionMap.containsKey(baseParameterItemDefDTO.getBptdCode())){
							tempProblemSolutionList = problemSolutionMap.get(baseParameterItemDefDTO.getBptdCode());
						} else {
							tempProblemSolutionList = new ArrayList<String>();
						}
						tempProblemSolutionList.add(baseParameterItemDefDTO.getItemName());
						problemSolutionMap.put(baseParameterItemDefDTO.getBptdCode(), tempProblemSolutionList);
					}
				}
				// 報修問題組件主鍵
				String problemSolutionId = null;
				if(!CollectionUtils.isEmpty(problemSolutionMap)){
					// 遍歷map存儲不同類別的報修問題
					for(String key : problemSolutionMap.keySet()){
						tempProblemSolutionList = problemSolutionMap.get(key);
						// 儲存每個類別的報修問題
						if(!CollectionUtils.isEmpty(tempProblemSolutionList)){
							i = 0;
							for(String tempName : tempProblemSolutionList){
								i ++;
								// id拼接
								if(i > 9){
									problemSolutionId = key + IAtomsConstants.MARK_UNDER_LINE + String.valueOf(i);
								} else {
									problemSolutionId = key + IAtomsConstants.MARK_UNDER_LINE + String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i);
								}
								baseParameterItemDefId = new BaseParameterItemDefId(problemSolutionId, key, effectiveDate);
								baseParameterItemDef = new BaseParameterItemDef();
								// 主鍵
								baseParameterItemDef.setId(baseParameterItemDefId);
								// 參數項目
								baseParameterItemDef.setItemName(tempName);
								// Task #2797
								if("PROBLEM_SOLUTION_MER".equals(key) && "提供新刷卡機".equals(tempName)){
									baseParameterItemDef.setItemValue("PROVIDE_NEW_EDC");
								// Task #2992
								} else if("PROBLEM_SOLUTION_EDC".equals(key) && "更換刷卡機".equals(tempName)){
									baseParameterItemDef.setItemValue("CHANGE_EDC");
								} else {
									// 參數項目值
									if(i > 9){
										baseParameterItemDef.setItemValue(String.valueOf(i));
									} else {
										// 2位數字，不足補0
										baseParameterItemDef.setItemValue(String.format(IAtomsConstants.PARAM_SUPPLEMENT_ZERO_FOR_NUMBER_TWO, i));
									}
								}
								
								// 參數項目順序
								baseParameterItemDef.setItemOrder(i);
								// 是否已核可
		//						baseParameterItemDef.setApprovedFlag(IAtomsConstants.YES);
								// 新增日期
						//		baseParameterItemDef.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
								// 異動日期
								baseParameterItemDef.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
								// 刪除標記
								baseParameterItemDef.setDeleted(IAtomsConstants.NO);
								this.baseParameterManagerDAO.insert(baseParameterItemDef);
							}
						}
					}
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "報修解決方式資料轉入成功").append("</br>");
				} else {
					this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無報修解決方式資料轉入").append("</br>");
				}
			} else {
				this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "無報修解決方式資料轉入").append("</br>");
			}
			// 拼接所有消息
			this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
			
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			map.put("resultMsg", this.resultMsg);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferFaultParamData"));
			
			LOGGER.error(".transferFaultParamData() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferFaultParamData"));
			
			LOGGER.error(".transferFaultParamData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}
	
	/**
	 * Purpose:得到錯婿消息的map集合
	 * @author CrissZhang
	 * @return Map
	 */
	private Map getErrorMsgMap(String actionId){
		Map map = new HashMap();
		if(actionId == "transferCalendar"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "行事曆資料轉入失敗").append("</br>");
		} else if(actionId == "transferFaultParamData"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "故障參數資料轉入失敗").append("</br>");
		} else if(actionId == "transferCompanyData"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "公司資料轉入失敗").append("</br>");
		} else if(actionId == "transferWarehouse"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "倉庫據點資料轉入失敗").append("</br>");
		} else if(actionId == "transferAssetType"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備品項資料轉入失敗").append("</br>");
		} else if(actionId == "transferApplicaton"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "程式版本資料轉入失敗").append("</br>");
		} else if(actionId == "transferMerchant"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "客戶特店、表頭資料轉入失敗").append("</br>");
		} else if(actionId == "transferContract"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "合約資料轉入失敗").append("</br>");
		} else if(actionId == "transferRepository"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "設備庫存資料轉入失敗").append("</br>");
		} else if(actionId == "transferAdmUser"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "使用者帳號資料轉入失敗").append("</br>");
		} else if(actionId == "transferProblemData"){
			this.resultMsg.append("(" + DateTimeUtils.toString(DateTimeUtils.getCurrentTimestamp(), DateTimeUtils.DT_FMT_YYYYMMDDHHMMSS_DASH) + ")" + "報修參數資料轉入失敗").append("</br>");
		}
		
		map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, false);
		this.PARAM_LOG_MESSAGE.append(this.resultMsg.toString());
		map.put("resultMsg", this.resultMsg);
		return map;
	}
	
	/** (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#countRepositoryHistData(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext countRepositoryHistData(SessionContext sessionContext) throws ServiceException {
		try {
			// 判斷DB設備庫存資料數量
			Integer count = this.dmmRepositoryHistDAO.countData();
			sessionContext.setAttribute(IAtomsConstants.COUNT, count);
			return sessionContext;
		} catch (DataAccessException e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferFaultParamData"));
			LOGGER.error(".countRepositoryHistData() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, this.getErrorMsgMap("transferFaultParamData"));
			LOGGER.error(".countRepositoryHistData() Service Exception--->" + e, e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
	}

	/**
	 * 得到案件歷程描述集合
	 * @param srmCaseTransactionDTOs
	 * @param srmCaseHandleInfo
	 * @return
	 */
	private SrmCaseHandleInfo getCaseDescription(List<SrmCaseTransactionDTO> srmCaseTransactionDTOs, SrmCaseHandleInfo srmCaseHandleInfo){
		Map map = new HashMap();
		List<SrmCaseTransactionDTO> tempSrmCaseTransactionDTOs = new ArrayList<SrmCaseTransactionDTO>();
		if(!CollectionUtils.isEmpty(srmCaseTransactionDTOs)){
			for(SrmCaseTransactionDTO srmCaseTransactionDTO : srmCaseTransactionDTOs){
				tempSrmCaseTransactionDTOs.add(srmCaseTransactionDTO);
			}
		}
		// 得到案件歷程描述集合
		map = caseDescriptionReturnFun(tempSrmCaseTransactionDTOs, map, 0);
		if(!CollectionUtils.isEmpty(map)){
			srmCaseHandleInfo.setFirstDescription((String)map.get(1));
			srmCaseHandleInfo.setSecondDescription((String)map.get(2));
			srmCaseHandleInfo.setThirdDescription((String)map.get(3));
		}
		return srmCaseHandleInfo;
	}
	/**
	 * 得到案件描述信息集合
	 * @param srmCaseTransactionDTOs
	 * @param map
	 * @param count
	 * @return
	 */
	private Map caseDescriptionReturnFun(List<SrmCaseTransactionDTO> srmCaseTransactionDTOs, Map map, int count){
		if(count >= 3){
			return map;
		}
		SrmCaseTransactionDTO tempCaseTransactionDTO = null;
		if(!CollectionUtils.isEmpty(srmCaseTransactionDTOs)){
			tempCaseTransactionDTO = getMaxDateDTO(srmCaseTransactionDTOs);
			if(tempCaseTransactionDTO != null){
				count++;
				map.put(count, tempCaseTransactionDTO.getDescription());
				srmCaseTransactionDTOs.remove(tempCaseTransactionDTO);
				return caseDescriptionReturnFun(srmCaseTransactionDTOs, map, count);
			}
		}
		return map;
	}
	
	/**
	 * 獲取案件歷程集合里處理時間最大的一筆歷程
	 * @param srmCaseTransactionDTOs : 案件歷程集合
	 * @return 返回一個處理時間最大的案件對象
	 */
	private SrmCaseTransactionDTO getMaxDateDTO(List<SrmCaseTransactionDTO> srmCaseTransactionDTOs){
		if(!CollectionUtils.isEmpty(srmCaseTransactionDTOs)){
			Timestamp tempDate = srmCaseTransactionDTOs.get(0).getCreatedDate();
			SrmCaseTransactionDTO tempCaseTransactionDTO = null;
			for(SrmCaseTransactionDTO srmCaseTransactionDTO : srmCaseTransactionDTOs){
				if(IAtomsDateTimeUtils.dateSubtractionMillis(tempDate, srmCaseTransactionDTO.getCreatedDate()) >= 0){
					tempDate = srmCaseTransactionDTO.getCreatedDate();
					tempCaseTransactionDTO = srmCaseTransactionDTO;
				}
			}
			return tempCaseTransactionDTO;
		}
		return null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#updateTransaction()
	 */
	@Override
	public SessionContext updateTransaction(SessionContext sessionContext) throws ServiceException {
		try {
			// 获取文件输入流
			InputStream fileInputStream = OldDataTransferService.class.getResourceAsStream("/transactionList.xlsx");
			// 2007版本
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = null;
			int rowCount = 0;
			Row row = null;
			if (workbook != null) {
				sheet = workbook.getSheetAt(0);
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					LOGGER.error("is no data >>> ");
				} else {
					// 交易參數更改項
					List<TransactionParameterDTO> transactionParameterDTOs = new ArrayList<TransactionParameterDTO>();
					TransactionParameterDTO transactionParameterDTO = null;
					//獲取行
					for (int i = 1; i <= rowCount; i++) {
						row = sheet.getRow(i);
						if (isRowEmpty(row)) {
			                continue;
			            } else {
			            	transactionParameterDTO = new TransactionParameterDTO();
			            	// 取出各个单元格的值
			            	transactionParameterDTO.setDtid(this.getCellFormatValue(row.getCell(0), true));
			            	/*transactionParameterDTO.setTid(this.getCellFormatValue(row.getCell(1), true));
			            	transactionParameterDTO.setMaster(this.getCellFormatValue(row.getCell(2), true));*/
			            	transactionParameterDTO.setTipTransaction(this.getCellFormatValue(row.getCell(1), true));
			            	transactionParameterDTOs.add(transactionParameterDTO);
			            }
					}
					List<SrmCaseNewTransactionParameterDTO> srmCaseTransactionParameterDTOs = null;
					String itemValue = null;
					Gson gsonss = new GsonBuilder().create();
					// 解析item_value集合
					Map<String, String> srmCaseTransactionParametermap = null;
					// 是否有一般交易
					SrmCaseNewTransactionParameterDTO srmCaseTransactionParameterDTO = null;
					Transformer transformer = new SimpleDtoDmoTransformer();
					SrmCaseNewTransactionParameter srmCaseNewTransactionParameter = null;
					for(TransactionParameterDTO tempTransactionParameterDTO : transactionParameterDTOs){
						srmCaseTransactionParameterDTO = null;
						// 當前交易參數
						srmCaseTransactionParameterDTOs = this.getSrmCaseNewTransactionParameterDAO().listTransactionParameterDTOsByDtid(tempTransactionParameterDTO.getDtid(), true);
						if(!CollectionUtils.isEmpty(srmCaseTransactionParameterDTOs)){
							for(SrmCaseNewTransactionParameterDTO tempCaseTransactionParameterDTO : srmCaseTransactionParameterDTOs) {
								/*// 判斷含有一般交易
								if(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VM.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())
										|| IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJ.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())
										|| IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJU.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())){
									srmCaseTransactionParameterDTO = tempCaseTransactionParameterDTO;
									break;
								}*/
								itemValue = tempCaseTransactionParameterDTO.getItemValue();
								if(StringUtils.hasText(itemValue)){
									//將item_value轉化為map，存在交易參數dto裡面
									srmCaseTransactionParametermap = (Map<String, String>) gsonss.fromJson(
													itemValue, new TypeToken<Map<String, String>>(){}.getType());
									// 判斷是否有itemValue
									if(CollectionUtils.isEmpty(srmCaseTransactionParametermap)){
										srmCaseTransactionParametermap = new HashMap<String, String>();
									}
									// 有tipTransaction
									if(StringUtils.hasText(tempTransactionParameterDTO.getTipTransaction())){
										if("1".equals(tempTransactionParameterDTO.getTipTransaction())){
											srmCaseTransactionParametermap.put("tipTransaction", "V");
										} else {
											srmCaseTransactionParametermap.put("tipTransaction", "");
										}
									}
									itemValue = gsonss.toJson(srmCaseTransactionParametermap);
									// itemValue
									tempCaseTransactionParameterDTO.setItemValue(itemValue);
								}
								// dmo
								srmCaseNewTransactionParameter = (SrmCaseNewTransactionParameter) transformer.transform(tempCaseTransactionParameterDTO, new SrmCaseNewTransactionParameter());
								// 更新
								this.srmCaseNewTransactionParameterDAO.getDaoSupport().saveOrUpdate(srmCaseNewTransactionParameter);
							}
							// 無一般交易
							/*if(srmCaseTransactionParameterDTO != null){
								itemValue = srmCaseTransactionParameterDTO.getItemValue();
								if(StringUtils.hasText(itemValue)){
									//將item_value轉化為map，存在交易參數dto裡面
									srmCaseTransactionParametermap = (Map<String, String>) gsonss.fromJson(
													itemValue, new TypeToken<Map<String, String>>(){}.getType());
									// 判斷是否有itemValue
									if(CollectionUtils.isEmpty(srmCaseTransactionParametermap)){
										srmCaseTransactionParametermap = new HashMap<String, String>();
									}
									// 有master
									if(StringUtils.hasText(tempTransactionParameterDTO.getMaster())){
										if("1".equals(tempTransactionParameterDTO.getMaster())){
											srmCaseTransactionParametermap.put("master", "V");
										} else {
											srmCaseTransactionParametermap.put("master", "");
										}
									}
									// 有jcb
									if(StringUtils.hasText(tempTransactionParameterDTO.getJcb())){
										if("1".equals(tempTransactionParameterDTO.getJcb())){
											srmCaseTransactionParametermap.put("jcb", "V");
										} else {
											srmCaseTransactionParametermap.put("jcb", "");
										}
									}
									itemValue = gsonss.toJson(srmCaseTransactionParametermap);
									// itemValue
									srmCaseTransactionParameterDTO.setItemValue(itemValue);
								}
								// tid
								srmCaseTransactionParameterDTO.setTid(tempTransactionParameterDTO.getTid());
								
								// dmo
								srmCaseNewTransactionParameter = (SrmCaseNewTransactionParameter) transformer.transform(srmCaseTransactionParameterDTO, new SrmCaseNewTransactionParameter());
								// 更新
								this.srmCaseNewTransactionParameterDAO.getDaoSupport().saveOrUpdate(srmCaseNewTransactionParameter);
							} else {
								// 無一般交易
								LOGGER.error("not have common >>> ");
							}*/
						}
					}
				}
			} else {
				LOGGER.error("workbook is null >>> ");
				throw new ServiceException();
			}
			
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".updateTransaction() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".updateTransaction(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#updateSmartpayTrans(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext updateSmartpayTrans(SessionContext sessionContext) throws ServiceException {
		try {
			// 获取文件输入流
			InputStream fileInputStream = OldDataTransferService.class.getResourceAsStream("/smartpayTransaction.xlsx");
			// 2007版本
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = null;
			int rowCount = 0;
			Row row = null;
			if (workbook != null) {
				sheet = workbook.getSheetAt(0);
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					LOGGER.error("is no data >>> ");
				} else {
					// 交易參數更改項
					List<TransactionParameterDTO> transactionParameterDTOs = new ArrayList<TransactionParameterDTO>();
					TransactionParameterDTO transactionParameterDTO = null;
					//獲取行
					for (int i = 1; i <= rowCount; i++) {
						row = sheet.getRow(i);
						if (isRowEmpty(row)) {
			                continue;
			            } else {
			            	transactionParameterDTO = new TransactionParameterDTO();
			            	// 取出各个单元格的值
			            	transactionParameterDTO.setDtid(this.getCellFormatValue(row.getCell(0), true));
			            	transactionParameterDTO.setReturnTransaction(this.getCellFormatValue(row.getCell(1), true));
			            	transactionParameterDTOs.add(transactionParameterDTO);
			            }
					}
					List<SrmCaseNewTransactionParameterDTO> srmCaseTransactionParameterDTOs = null;
					String itemValue = null;
					Gson gsonss = new GsonBuilder().create();
					// 解析item_value集合
					Map<String, String> srmCaseTransactionParametermap = null;
					// 是否有Smartpay交易
					SrmCaseNewTransactionParameterDTO srmCaseTransactionParameterDTO = null;
					Transformer transformer = new SimpleDtoDmoTransformer();
					SrmCaseNewTransactionParameter srmCaseNewTransactionParameter = null;
					for(TransactionParameterDTO tempTransactionParameterDTO : transactionParameterDTOs){
						srmCaseTransactionParameterDTO = null;
						// 當前交易參數
						srmCaseTransactionParameterDTOs = this.getSrmCaseNewTransactionParameterDAO().listTransactionParameterDTOsByDtid(tempTransactionParameterDTO.getDtid(), true);
						if(!CollectionUtils.isEmpty(srmCaseTransactionParameterDTOs)){
							for(SrmCaseNewTransactionParameterDTO tempCaseTransactionParameterDTO : srmCaseTransactionParameterDTOs) {
								// 判斷含有Smartpay交易
								if(IAtomsConstants.TRANSACTION_CATEGORY.SMART_PAY.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())){
									srmCaseTransactionParameterDTO = tempCaseTransactionParameterDTO;
									break;
								}
							}
							// 無Smartpay交易
							if(srmCaseTransactionParameterDTO != null){
								itemValue = srmCaseTransactionParameterDTO.getItemValue();
								if(StringUtils.hasText(itemValue)){
									//將item_value轉化為map，存在交易參數dto裡面
									srmCaseTransactionParametermap = (Map<String, String>) gsonss.fromJson(
													itemValue, new TypeToken<Map<String, String>>(){}.getType());
									// 判斷是否有itemValue
									if(CollectionUtils.isEmpty(srmCaseTransactionParametermap)){
										srmCaseTransactionParametermap = new HashMap<String, String>();
									}
									// 有退貨交易
									if(StringUtils.hasText(tempTransactionParameterDTO.getReturnTransaction())){
										if("1".equals(tempTransactionParameterDTO.getReturnTransaction())){
											srmCaseTransactionParametermap.put("returnTransaction", "V");
										} else {
											srmCaseTransactionParametermap.put("returnTransaction", "");
										}
									}
									itemValue = gsonss.toJson(srmCaseTransactionParametermap);
								} else {
									// 退貨交易 標記為1
									if("1".equals(tempTransactionParameterDTO.getReturnTransaction())){
										itemValue = "{\"returnTransaction\":\"V\"}";
									}
								}
								// itemValue
								srmCaseTransactionParameterDTO.setItemValue(itemValue);
								// dmo
								srmCaseNewTransactionParameter = (SrmCaseNewTransactionParameter) transformer.transform(srmCaseTransactionParameterDTO, new SrmCaseNewTransactionParameter());
								// 更新
								this.srmCaseNewTransactionParameterDAO.getDaoSupport().saveOrUpdate(srmCaseNewTransactionParameter);
							} else {
								// 無Smartpay交易
								LOGGER.error("not have Smartpay >>> ");
							}
						}
					}
				}
			} else {
				LOGGER.error("workbook is null >>> ");
				throw new ServiceException();
			}
			
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".updateTransaction() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".updateTransaction(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#insertAssetLink(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext insertAssetLink(SessionContext sessionContext) throws ServiceException {
		try{
			// 获取文件输入流
			InputStream fileInputStream = OldDataTransferService.class.getResourceAsStream("/assetLink.xlsx");
			// 2007版本
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = null;
			int rowCount = 0;
			Row row = null;
			if (workbook != null) {
				sheet = workbook.getSheetAt(0);
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					LOGGER.error("is no data >>> ");
				} else {
					// 交易參數更改項
					List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs = new ArrayList<SrmCaseAssetLinkDTO>();
					SrmCaseAssetLinkDTO srmCaseAssetLinkDTO = null;
					//獲取行
					for (int i = 1; i <= rowCount; i++) {
						row = sheet.getRow(i);
						if (isRowEmpty(row)) {
			                continue;
			            } else {
			            	srmCaseAssetLinkDTO = new SrmCaseAssetLinkDTO();
			            	// 取出各个单元格的值
			            	srmCaseAssetLinkDTO.setDtid(this.getCellFormatValue(row.getCell(0), true));
			            	srmCaseAssetLinkDTO.setSerialNumber(this.getCellFormatValue(row.getCell(1), true));
			            	srmCaseAssetLinkDTO.setItemCategory(this.getCellFormatValue(row.getCell(3), true));
			            	srmCaseAssetLinkDTOs.add(srmCaseAssetLinkDTO);
			            }
					}
					
					// 案件最新設備鏈接檔集合
					Map<String, List<SrmCaseAssetLinkDTO>> caseAssetLinkMap = new HashMap<String, List<SrmCaseAssetLinkDTO>>();
					// 案件周邊設備信息集合
					Map<String, SrmCaseHandleInfoDTO> assetCaseInfoListMap = new HashMap<String, SrmCaseHandleInfoDTO>();
					SrmCaseHandleInfoDTO assetCaseInfoDTO = null;
					
					// 處理最新案件鏈接檔信息
					if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)){
						// 設備鏈接檔集合
						for(SrmCaseAssetLinkDTO tempCaseAssetLinkDTO : srmCaseAssetLinkDTOs){
							if(StringUtils.hasText(tempCaseAssetLinkDTO.getDtid())){
								// 放置設備鏈接當數據
								if(assetCaseInfoListMap.containsKey(tempCaseAssetLinkDTO.getDtid())){
									assetCaseInfoDTO = assetCaseInfoListMap.get(tempCaseAssetLinkDTO.getDtid());
								} else {
									assetCaseInfoDTO = new SrmCaseHandleInfoDTO();
								}
								// edc
								if(IAtomsConstants.ASSET_CATEGORY_EDC.equals(tempCaseAssetLinkDTO.getItemCategory())){
									if(StringUtils.hasText(assetCaseInfoDTO.getEdcSerialNumber())){
										LOGGER.error(".insertAssetLink() EDC SerialNumber is over 1!!!" + tempCaseAssetLinkDTO.getDtid());
									} else {
										assetCaseInfoDTO.setEdcSerialNumber(tempCaseAssetLinkDTO.getSerialNumber());
									}
								// 周邊設備
								} else {
									if(StringUtils.hasText(assetCaseInfoDTO.getPeripheralsSerialNumber())){
										if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals2SerialNumber())){
											if(StringUtils.hasText(assetCaseInfoDTO.getPeripherals3SerialNumber())){
												LOGGER.error(".insertAssetLink() Peripherals SerialNumber is over 3!!!" + tempCaseAssetLinkDTO.getDtid());
											} else {
												assetCaseInfoDTO.setPeripherals3SerialNumber(tempCaseAssetLinkDTO.getSerialNumber());
											}
										} else {
											assetCaseInfoDTO.setPeripherals2SerialNumber(tempCaseAssetLinkDTO.getSerialNumber());
										}
									} else {
										assetCaseInfoDTO.setPeripheralsSerialNumber(tempCaseAssetLinkDTO.getSerialNumber());
									}
								}
								assetCaseInfoListMap.put(tempCaseAssetLinkDTO.getDtid(), assetCaseInfoDTO);
							}
						}
						
						// 設備鏈接保存
						if(!CollectionUtils.isEmpty(assetCaseInfoListMap)){
							for(String dtid : assetCaseInfoListMap.keySet()){
								this.srmCaseNewAssetLinkDAO.insertAssetLink(dtid, assetCaseInfoListMap.get(dtid));
							}
						}
					}
				}
			} else {
				LOGGER.error("workbook is null >>> ");
				throw new ServiceException();
			}
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".insertAssetLink() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".insertAssetLink(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#updateSimAssetLink(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext updateSimAssetLink(SessionContext sessionContext) throws ServiceException {
		try{
			// 获取文件输入流
			InputStream fileInputStream = OldDataTransferService.class.getResourceAsStream("/simAssetLink.xls");
			// 2007版本
			Workbook workbook = new HSSFWorkbook(fileInputStream);
			Sheet sheet = null;
			int rowCount = 0;
			Row row = null;
			if (workbook != null) {
				sheet = workbook.getSheetAt(0);
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					LOGGER.error("is no data >>> ");
				} else {
					// 交易參數更改項
					List<SrmCaseAssetLinkDTO> srmCaseAssetLinkDTOs = new ArrayList<SrmCaseAssetLinkDTO>();
					SrmCaseAssetLinkDTO srmCaseAssetLinkDTO = null;
					//獲取行
					for (int i = 1; i <= rowCount; i++) {
						row = sheet.getRow(i);
						if (isRowEmpty(row)) {
			                continue;
			            } else {
			            	srmCaseAssetLinkDTO = new SrmCaseAssetLinkDTO();
			            	// 取出各个单元格的值
			            	srmCaseAssetLinkDTO.setSerialNumber(this.getCellFormatValue(row.getCell(2), true));
			            	srmCaseAssetLinkDTO.setAssetTypeId(this.getCellFormatValue(row.getCell(4), true));
			            	srmCaseAssetLinkDTOs.add(srmCaseAssetLinkDTO);
			            }
					}
					// 處理最新案件鏈接檔信息
					if(!CollectionUtils.isEmpty(srmCaseAssetLinkDTOs)){
						// 設備鏈接檔集合
						for(SrmCaseAssetLinkDTO tempCaseAssetLinkDTO : srmCaseAssetLinkDTOs){
							this.srmCaseNewAssetLinkDAO.updateSimAssetLink(tempCaseAssetLinkDTO.getSerialNumber(), tempCaseAssetLinkDTO.getAssetTypeId());
						}
					}
				}
			} else {
				LOGGER.error("workbook is null >>> ");
				throw new ServiceException();
			}
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".updateSimAssetLink() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".updateSimAssetLink(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.dataTransfer.service.IOldDataTransferService#updateManualInput(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext updateManualInput(SessionContext sessionContext) throws ServiceException {
		try {
			// 获取文件输入流
			InputStream fileInputStream = OldDataTransferService.class.getResourceAsStream("/QBEdata-manualInput.xlsx");
			// 2007版本
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = null;
			int rowCount = 0;
			Row row = null;
			if (workbook != null) {
				sheet = workbook.getSheetAt(0);
				rowCount = this.getExcelRealRowCount(sheet);
				if (rowCount <= 1) {
					LOGGER.error("QBEdata-manualInput.xlsx has no data >>> ");
				} else {
					// 交易參數更改項
					List<TransactionParameterDTO> transactionParameterDTOs = new ArrayList<TransactionParameterDTO>();
					TransactionParameterDTO transactionParameterDTO = null;
					//獲取行
					for (int i = 1; i <= rowCount; i++) {
						row = sheet.getRow(i);
						if (isRowEmpty(row)) {
			                continue;
			            } else {
			            	transactionParameterDTO = new TransactionParameterDTO();
			            	// 取出各个单元格的值
			            	transactionParameterDTO.setDtid(this.getCellFormatValue(row.getCell(0), true));
			            	transactionParameterDTO.setManualInput(this.getCellFormatValue(row.getCell(1), true));
			            	transactionParameterDTOs.add(transactionParameterDTO);
			            }
					}
					List<SrmCaseNewTransactionParameterDTO> srmCaseTransactionParameterDTOs = null;
					String itemValue = null;
					Gson gsonss = new GsonBuilder().create();
					// 解析item_value集合
					Map<String, String> srmCaseTransactionParametermap = null;
					// 是否有一般交易
					SrmCaseNewTransactionParameterDTO srmCaseTransactionParameterDTO = null;
					Transformer transformer = new SimpleDtoDmoTransformer();
					SrmCaseNewTransactionParameter srmCaseNewTransactionParameter = null;
					for(TransactionParameterDTO tempTransactionParameterDTO : transactionParameterDTOs){
						srmCaseTransactionParameterDTO = null;
						// 當前交易參數
						srmCaseTransactionParameterDTOs = this.getSrmCaseNewTransactionParameterDAO().listTransactionParameterDTOsByDtid(tempTransactionParameterDTO.getDtid(), true);
						if(!CollectionUtils.isEmpty(srmCaseTransactionParameterDTOs)){
							for(SrmCaseNewTransactionParameterDTO tempCaseTransactionParameterDTO : srmCaseTransactionParameterDTOs) {
								// 判斷含有一般交易
								if(IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VM.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())){
									srmCaseTransactionParameterDTO = tempCaseTransactionParameterDTO;
									break;
								} else if (IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJ.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())) {
									srmCaseTransactionParameterDTO = tempCaseTransactionParameterDTO;
									break;
								} else if (IAtomsConstants.TRANSACTION_CATEGORY.COMMON_VMJU.getCode().equals(tempCaseTransactionParameterDTO.getTransactionType())) {
									srmCaseTransactionParameterDTO = tempCaseTransactionParameterDTO;
									break;
								}
							}
							// 一般交易
							if(srmCaseTransactionParameterDTO != null){
								itemValue = srmCaseTransactionParameterDTO.getItemValue();
								if(StringUtils.hasText(itemValue)){
									//將item_value轉化為map，存在交易參數dto裡面
									srmCaseTransactionParametermap = (Map<String, String>) gsonss.fromJson(
													itemValue, new TypeToken<Map<String, String>>(){}.getType());
									// 判斷是否有itemValue
									if(CollectionUtils.isEmpty(srmCaseTransactionParametermap)){
										srmCaseTransactionParametermap = new HashMap<String, String>();
									}
									// 有人工輸入
									if(StringUtils.hasText(tempTransactionParameterDTO.getManualInput())){
										if("1".equals(tempTransactionParameterDTO.getManualInput())){
											srmCaseTransactionParametermap.put("manualInput", "V");
										} else {
											srmCaseTransactionParametermap.put("manualInput", "");
										}
									}
									itemValue = gsonss.toJson(srmCaseTransactionParametermap);
								} else {
									// 人工輸入 標記為1
									if("1".equals(tempTransactionParameterDTO.getManualInput())){
										itemValue = "{\"manualInput\":\"V\"}";
									}
								}
								// itemValue
								srmCaseTransactionParameterDTO.setItemValue(itemValue);
								// dmo
								srmCaseNewTransactionParameter = (SrmCaseNewTransactionParameter) transformer.transform(srmCaseTransactionParameterDTO, new SrmCaseNewTransactionParameter());
								// 更新
								this.srmCaseNewTransactionParameterDAO.getDaoSupport().saveOrUpdate(srmCaseNewTransactionParameter);
							} else {
								// 無一般交易
								LOGGER.error("not have manualInput >>> ");
							}
						}
					}
				}
			} else {
				LOGGER.error("workbook is null >>> ");
				throw new ServiceException();
			}
			
			Message msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, true);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
			return sessionContext;
		} catch (DataAccessException e) {
			LOGGER.error(".updateManualInput() DataAccess Exception:" + e, e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".updateManualInput(SessionContext sessionContext)", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
	}
	/**
	 * Purpose:判斷Row是否為空
	 * @author CrissZhang
	 * @return Boolean
	 */
	private Boolean isRowEmpty(Row row){
		Boolean flag = true;
		if(row != null){
		  int beginCell = row.getFirstCellNum();
		  int endCell = row.getLastCellNum();
		  for(int i = beginCell; i <= endCell; i++){
		        if (!StringUtils.hasText(this.getCellFormatValue(row.getCell(i), true))) {  
		            continue;  
		        } else {
		        	flag = false;
		        	break;
		        }
	    	}
		} 
		return flag;
	}
	
	/**
	 * Purpose:獲取excel表格真實行數
	 * @author CrissZhang
	 * @param sheet ： 傳入參數sheet
	 * @return int : 返回行數
	 */
	private int getExcelRealRowCount(Sheet sheet) {
		int rowCount = 0;
		if(sheet != null){
			int beginRow = sheet.getFirstRowNum();  
		    int endRow = sheet.getLastRowNum();  
		    int beginCell = 0;
		    int endCell = 0;
		    Row tempRow = null;
		    Boolean emptyRow = false;
		    for (int i = beginRow; i <= endRow; i++) {  
		    	tempRow = sheet.getRow(i);
		    	emptyRow = false;
		    	if(tempRow != null){
		    		beginCell = tempRow.getFirstCellNum();
			    	endCell = tempRow.getLastCellNum();
			    	for(int j = beginCell; j <= endCell; j++){
				        if (!StringUtils.hasText(this.getCellFormatValue(tempRow.getCell(j), true))) {  
				            continue;  
				        } else {
				        	emptyRow = true;
				        	break;
				        }
			    	}
			    	if(emptyRow){
			    		rowCount ++;
			    	} else {
			    		break;
			    	}
		    	}
		    }  
		}
		return rowCount;
	}
	
	private String getCellFormatValue(Cell cell, boolean getTime) {
	    String cellvalue = "";
	    if (cell != null) {
	    	// 默認以文本讀
	    	cell.setCellType(Cell.CELL_TYPE_STRING);
	    	
	        // 判断当前Cell的Type
	        switch (cell.getCellType()) {
	            // 如果当前Cell的Type为NUMERIC
	            case Cell.CELL_TYPE_NUMERIC:
	            case Cell.CELL_TYPE_FORMULA: {
	                // 判断当前的cell是否为Date
	                if (DateUtil.isCellDateFormatted(cell)) {
	                	Date date = cell.getDateCellValue();
	                	if (getTime) {
	                		cellvalue = DateTimeUtils.toString(date, IAtomsConstants.PARAM_HOUR_MINUTE_DATE_FORMAT);
	                	} else {
	                		cellvalue = DateTimeUtils.toString(date, DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
	                	}
	                }else {
	                    // 取得当前Cell的数值
	                    cellvalue = String.valueOf((((Double)cell.getNumericCellValue())));
	                    if (StringUtils.hasText(cellvalue)) {
	                    	String[] cellValues = cellvalue.split("\\.");
	                    	if (IAtomsConstants.LEAVE_CASE_STATUS_ZERO.equals(cellValues[1])) {
	                    		cellvalue = cellValues[0];
	                    	}
	                    }
	                }
	                break;
	            }
	            case Cell.CELL_TYPE_STRING:
	                cellvalue = cell.getRichStringCellValue().getString();
	                break;
	            case Cell.CELL_TYPE_BOOLEAN:
	            	cellvalue = String.valueOf(cell.getBooleanCellValue());
	    		    break;
	    		case Cell.CELL_TYPE_BLANK:
	    			cellvalue = "";
	    		    break;
	            default:
	                cellvalue = " ";
	        }
	    } else {
	        cellvalue = "";
	    }
	    return cellvalue;
	}
	
	/**
	 * @return the oldDataTransferDAO
	 */
	public IOldDataTransferDAO getOldDataTransferDAO() {
		return oldDataTransferDAO;
	}
	/**
	 * @param oldDataTransferDAO the oldDataTransferDAO to set
	 */
	public void setOldDataTransferDAO(IOldDataTransferDAO oldDataTransferDAO) {
		this.oldDataTransferDAO = oldDataTransferDAO;
	}
	/**
	 * @return the locationList
	 */
	public List<Parameter> getLocationList() {
		return locationList;
	}
	/**
	 * @param locationList the locationList to set
	 */
	public void setLocationList(List<Parameter> locationList) {
		this.locationList = locationList;
	}
	/**
	 * @return the calendarYearDAO
	 */
	public ICalendarYearDAO getCalendarYearDAO() {
		return calendarYearDAO;
	}
	/**
	 * @param calendarYearDAO the calendarYearDAO to set
	 */
	public void setCalendarYearDAO(ICalendarYearDAO calendarYearDAO) {
		this.calendarYearDAO = calendarYearDAO;
	}
	/**
	 * @return the calendarDayDAO
	 */
	public ICalendarDayDAO getCalendarDayDAO() {
		return calendarDayDAO;
	}
	/**
	 * @param calendarDayDAO the calendarDayDAO to set
	 */
	public void setCalendarDayDAO(ICalendarDayDAO calendarDayDAO) {
		this.calendarDayDAO = calendarDayDAO;
	}
	/**
	 * @return the baseParameterManagerDAO
	 */
	public IBaseParameterManagerDAO getBaseParameterManagerDAO() {
		return baseParameterManagerDAO;
	}
	/**
	 * @param baseParameterManagerDAO the baseParameterManagerDAO to set
	 */
	public void setBaseParameterManagerDAO(IBaseParameterManagerDAO baseParameterManagerDAO) {
		this.baseParameterManagerDAO = baseParameterManagerDAO;
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
	 * @return the companyTypeDAO
	 */
	public ICompanyTypeDAO getCompanyTypeDAO() {
		return companyTypeDAO;
	}
	/**
	 * @return the baseParameterItemDefDAO
	 */
	public IBaseParameterItemDefDAO getBaseParameterItemDefDAO() {
		return baseParameterItemDefDAO;
	}
	/**
	 * @return the departmentDAO
	 */
	public IDepartmentDAO getDepartmentDAO() {
		return departmentDAO;
	}
	/**
	 * @param companyTypeDAO the companyTypeDAO to set
	 */
	public void setCompanyTypeDAO(ICompanyTypeDAO companyTypeDAO) {
		this.companyTypeDAO = companyTypeDAO;
	}
	/**
	 * @param baseParameterItemDefDAO the baseParameterItemDefDAO to set
	 */
	public void setBaseParameterItemDefDAO(
			IBaseParameterItemDefDAO baseParameterItemDefDAO) {
		this.baseParameterItemDefDAO = baseParameterItemDefDAO;
	}
	/**
	 * @param departmentDAO the departmentDAO to set
	 */
	public void setDepartmentDAO(IDepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}
	/**
	 * @return the companyCodeList
	 */
	public List<Parameter> getCompanyCodeList() {
		return companyCodeList;
	}
	/**
	 * @param companyCodeList the companyCodeList to set
	 */
	public void setCompanyCodeList(List<Parameter> companyCodeList) {
		this.companyCodeList = companyCodeList;
	}
	/**
	 * @return the warehouseDAO
	 */
	public IWarehouseDAO getWarehouseDAO() {
		return warehouseDAO;
	}
	/**
	 * @param warehouseDAO the warehouseDAO to set
	 */
	public void setWarehouseDAO(IWarehouseDAO warehouseDAO) {
		this.warehouseDAO = warehouseDAO;
	}
	/**
	 * @return the merchantDAO
	 */
	public IMerchantDAO getMerchantDAO() {
		return merchantDAO;
	}
	/**
	 * @return the merchantHeaderDAO
	 */
	public IMerchantHeaderDAO getMerchantHeaderDAO() {
		return merchantHeaderDAO;
	}
	/**
	 * @param merchantDAO the merchantDAO to set
	 */
	public void setMerchantDAO(IMerchantDAO merchantDAO) {
		this.merchantDAO = merchantDAO;
	}
	/**
	 * @param merchantHeaderDAO the merchantHeaderDAO to set
	 */
	public void setMerchantHeaderDAO(IMerchantHeaderDAO merchantHeaderDAO) {
		this.merchantHeaderDAO = merchantHeaderDAO;
	}
	/**
	 * @return the applicationDAO
	 */
	public IPvmApplicationDAO getApplicationDAO() {
		return applicationDAO;
	}
	/**
	 * @param applicationDAO the applicationDAO to set
	 */
	public void setApplicationDAO(IPvmApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}
	/**
	 * @return the contractDAO
	 */
	public IContractDAO getContractDAO() {
		return contractDAO;
	}
	/**
	 * @param contractDAO the contractDAO to set
	 */
	public void setContractDAO(IContractDAO contractDAO) {
		this.contractDAO = contractDAO;
	}
	/**
	 * @return the contractVendorDAO
	 */
	public IContractVendorDAO getContractVendorDAO() {
		return contractVendorDAO;
	}
	/**
	 * @param contractVendorDAO the contractVendorDAO to set
	 */
	public void setContractVendorDAO(IContractVendorDAO contractVendorDAO) {
		this.contractVendorDAO = contractVendorDAO;
	}
	/**
	 * @return the assetTypeDAO
	 */
	public IAssetTypeDAO getAssetTypeDAO() {
		return assetTypeDAO;
	}
	/**
	 * @param assetTypeDAO the assetTypeDAO to set
	 */
	public void setAssetTypeDAO(IAssetTypeDAO assetTypeDAO) {
		this.assetTypeDAO = assetTypeDAO;
	}
	/**
	 * @return the assetTypeList
	 */
	public List<Parameter> getAssetTypeList() {
		return assetTypeList;
	}
	/**
	 * @param assetTypeList the assetTypeList to set
	 */
	public void setAssetTypeList(List<Parameter> assetTypeList) {
		this.assetTypeList = assetTypeList;
	}
	/**
	 * @return the assetSupportedFunctionMap
	 */
	public Map<String, String> getAssetSupportedFunctionMap() {
		return assetSupportedFunctionMap;
	}
	/**
	 * @return the assetSupportedCommMap
	 */
	public Map<String, String> getAssetSupportedCommMap() {
		return assetSupportedCommMap;
	}
	/**
	 * @param assetSupportedFunctionMap the assetSupportedFunctionMap to set
	 */
	public void setAssetSupportedFunctionMap(Map<String, String> assetSupportedFunctionMap) {
		this.assetSupportedFunctionMap = assetSupportedFunctionMap;
	}
	/**
	 * @param assetSupportedCommMap the assetSupportedCommMap to set
	 */
	public void setAssetSupportedCommMap(Map<String, String> assetSupportedCommMap) {
		this.assetSupportedCommMap = assetSupportedCommMap;
	}
	/**
	 * @return the supportedFunctionList
	 */
	public List<Parameter> getSupportedFunctionList() {
		return supportedFunctionList;
	}
	/**
	 * @return the supportedCommList
	 */
	public List<Parameter> getSupportedCommList() {
		return supportedCommList;
	}
	/**
	 * @param supportedFunctionList the supportedFunctionList to set
	 */
	public void setSupportedFunctionList(List<Parameter> supportedFunctionList) {
		this.supportedFunctionList = supportedFunctionList;
	}
	/**
	 * @param supportedCommList the supportedCommList to set
	 */
	public void setSupportedCommList(List<Parameter> supportedCommList) {
		this.supportedCommList = supportedCommList;
	}
	/**
	 * @return the applicationAssetLinkList
	 */
	public List<ApplicationAssetLinkDTO> getApplicationAssetLinkList() {
		return applicationAssetLinkList;
	}
	/**
	 * @param applicationAssetLinkList the applicationAssetLinkList to set
	 */
	public void setApplicationAssetLinkList(List<ApplicationAssetLinkDTO> applicationAssetLinkList) {
		this.applicationAssetLinkList = applicationAssetLinkList;
	}
	/**
	 * @return the applicationVersionList
	 */
	public List<Parameter> getApplicationVersionList() {
		return applicationVersionList;
	}
	/**
	 * @param applicationVersionList the applicationVersionList to set
	 */
	public void setApplicationVersionList(List<Parameter> applicationVersionList) {
		this.applicationVersionList = applicationVersionList;
	}
	/**
	 * @return the applicationAssetLinkDAO
	 */
	public IApplicationAssetLinkDAO getApplicationAssetLinkDAO() {
		return applicationAssetLinkDAO;
	}
	/**
	 * @param applicationAssetLinkDAO the applicationAssetLinkDAO to set
	 */
	public void setApplicationAssetLinkDAO(IApplicationAssetLinkDAO applicationAssetLinkDAO) {
		this.applicationAssetLinkDAO = applicationAssetLinkDAO;
	}
	/**
	 * @return the resultMsg
	 */
	public StringBuilder getResultMsg() {
		return resultMsg;
	}
	/**
	 * @param resultMsg the resultMsg to set
	 */
	public void setResultMsg(StringBuilder resultMsg) {
		this.resultMsg = resultMsg;
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
	public void setRepositoryDAO(IDmmRepositoryDAO repositoryDAO) {
		this.repositoryDAO = repositoryDAO;
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
	public void setDmmRepositoryHistDAO(
			IDmmRepositoryHistoryDAO dmmRepositoryHistDAO) {
		this.dmmRepositoryHistDAO = dmmRepositoryHistDAO;
	}
	/**
	 * @return the warehouseList
	 */
	public List<Parameter> getWarehouseList() {
		return warehouseList;
	}
	/**
	 * @param warehouseList the warehouseList to set
	 */
	public void setWarehouseList(List<Parameter> warehouseList) {
		this.warehouseList = warehouseList;
	}
	/**
	 * @return the contractList
	 */
	public List<Parameter> getContractList() {
		return contractList;
	}
	/**
	 * @param contractList the contractList to set
	 */
	public void setContractList(List<Parameter> contractList) {
		this.contractList = contractList;
	}
	/**
	 * @return the installTypeList
	 */
	public List<Parameter> getInstallTypeList() {
		return installTypeList;
	}
	/**
	 * @param installTypeList the installTypeList to set
	 */
	public void setInstallTypeList(List<Parameter> installTypeList) {
		this.installTypeList = installTypeList;
	}
	/**
	 * @return the faultDescriptionList
	 */
	public List<Parameter> getFaultDescriptionList() {
		return faultDescriptionList;
	}
	/**
	 * @param faultDescriptionList the faultDescriptionList to set
	 */
	public void setFaultDescriptionList(List<Parameter> faultDescriptionList) {
		this.faultDescriptionList = faultDescriptionList;
	}
	/**
	 * @return the faultComponentList
	 */
	public List<Parameter> getFaultComponentList() {
		return faultComponentList;
	}
	/**
	 * @param faultComponentList the faultComponentList to set
	 */
	public void setFaultComponentList(List<Parameter> faultComponentList) {
		this.faultComponentList = faultComponentList;
	}
	/**
	 * @return the actionList
	 */
	public List<Parameter> getActionList() {
		return actionList;
	}
	/**
	 * @param actionList the actionList to set
	 */
	public void setActionList(List<Parameter> actionList) {
		this.actionList = actionList;
	}
	/**
	 * @return the assetStatusList
	 */
	public List<Parameter> getAssetStatusList() {
		return assetStatusList;
	}
	/**
	 * @param assetStatusList the assetStatusList to set
	 */
	public void setAssetStatusList(List<Parameter> assetStatusList) {
		this.assetStatusList = assetStatusList;
	}
	/**
	 * @return the maTypeList
	 */
	public List<Parameter> getMaTypeList() {
		return maTypeList;
	}
	/**
	 * @param maTypeList the maTypeList to set
	 */
	public void setMaTypeList(List<Parameter> maTypeList) {
		this.maTypeList = maTypeList;
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
	public void setRepositoryFaultComDAO(
			IDmmRepositoryFaultComDAO repositoryFaultComDAO) {
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
	public void setRepositoryFaultDescDAO(
			IDmmRepositoryFaultDescDAO repositoryFaultDescDAO) {
		this.repositoryFaultDescDAO = repositoryFaultDescDAO;
	}
	/**
	 * @return the retireReasonList
	 */
	public List<Parameter> getRetireReasonList() {
		return retireReasonList;
	}
	/**
	 * @param retireReasonList the retireReasonList to set
	 */
	public void setRetireReasonList(List<Parameter> retireReasonList) {
		this.retireReasonList = retireReasonList;
	}
	/**
	 * @return the departmentMap
	 */
	public Map<String, List<Parameter>> getDepartmentMap() {
		return departmentMap;
	}
	/**
	 * @param departmentMap the departmentMap to set
	 */
	public void setDepartmentMap(Map<String, List<Parameter>> departmentMap) {
		this.departmentMap = departmentMap;
	}
	/**
	 * @return the merchantMap
	 */
	public Map<String, List<Parameter>> getMerchantMap() {
		return merchantMap;
	}
	/**
	 * @param merchantMap the merchantMap to set
	 */
	public void setMerchantMap(Map<String, List<Parameter>> merchantMap) {
		this.merchantMap = merchantMap;
	}
	/**
	 * @return the merchantHeaderMap
	 */
	public Map<String, List<Parameter>> getMerchantHeaderMap() {
		return merchantHeaderMap;
	}
	/**
	 * @param merchantHeaderMap the merchantHeaderMap to set
	 */
	public void setMerchantHeaderMap(Map<String, List<Parameter>> merchantHeaderMap) {
		this.merchantHeaderMap = merchantHeaderMap;
	}
	/**
	 * @return the dmmRepositoryHistoryDescDAO
	 */
	public IDmmRepositoryHistoryDescDAO getDmmRepositoryHistoryDescDAO() {
		return dmmRepositoryHistoryDescDAO;
	}
	/**
	 * @param dmmRepositoryHistoryDescDAO the dmmRepositoryHistoryDescDAO to set
	 */
	public void setDmmRepositoryHistoryDescDAO(
			IDmmRepositoryHistoryDescDAO dmmRepositoryHistoryDescDAO) {
		this.dmmRepositoryHistoryDescDAO = dmmRepositoryHistoryDescDAO;
	}
	/**
	 * @return the dmmRepositoryHistoryCommDAO
	 */
	public IDmmRepositoryHistoryCommDAO getDmmRepositoryHistoryCommDAO() {
		return dmmRepositoryHistoryCommDAO;
	}
	/**
	 * @param dmmRepositoryHistoryCommDAO the dmmRepositoryHistoryCommDAO to set
	 */
	public void setDmmRepositoryHistoryCommDAO(
			IDmmRepositoryHistoryCommDAO dmmRepositoryHistoryCommDAO) {
		this.dmmRepositoryHistoryCommDAO = dmmRepositoryHistoryCommDAO;
	}
	/**
	 * @return the peripheralsList
	 */
	public List<Parameter> getPeripheralsList() {
		return peripheralsList;
	}
	/**
	 * @param peripheralsList the peripheralsList to set
	 */
	public void setPeripheralsList(List<Parameter> peripheralsList) {
		this.peripheralsList = peripheralsList;
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
	/**
	 * @return the srmCaseAttFileDAO
	 */
	public ISrmCaseAttFileDAO getSrmCaseAttFileDAO() {
		return srmCaseAttFileDAO;
	}
	/**
	 * @param srmCaseAttFileDAO the srmCaseAttFileDAO to set
	 */
	public void setSrmCaseAttFileDAO(ISrmCaseAttFileDAO srmCaseAttFileDAO) {
		this.srmCaseAttFileDAO = srmCaseAttFileDAO;
	}
	/**
	 * @return the srmCaseTransactionParameterDAO
	 */
	public ISrmCaseTransactionParameterDAO getSrmCaseTransactionParameterDAO() {
		return srmCaseTransactionParameterDAO;
	}
	/**
	 * @param srmCaseTransactionParameterDAO the srmCaseTransactionParameterDAO to set
	 */
	public void setSrmCaseTransactionParameterDAO(
			ISrmCaseTransactionParameterDAO srmCaseTransactionParameterDAO) {
		this.srmCaseTransactionParameterDAO = srmCaseTransactionParameterDAO;
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
	 * @param applicationList the applicationList to set
	 */
	public void setApplicationList(List<Parameter> applicationList) {
		this.applicationList = applicationList;
	}
	/**
	 * @return the applicationMap
	 */
	public Map<String, List<ApplicationDTO>> getApplicationMap() {
		return applicationMap;
	}
	/**
	 * @param applicationMap the applicationMap to set
	 */
	public void setApplicationMap(Map<String, List<ApplicationDTO>> applicationMap) {
		this.applicationMap = applicationMap;
	}
	/**
	 * @return the admUserDAO
	 */
	public IAdmUserDAO getAdmUserDAO() {
		return admUserDAO;
	}
	/**
	 * @param admUserDAO the admUserDAO to set
	 */
	public void setAdmUserDAO(IAdmUserDAO admUserDAO) {
		this.admUserDAO = admUserDAO;
	}
	/**
	 * @return the userList
	 */
	public List<Parameter> getUserList() {
		return userList;
	}
	/**
	 * @param userList the userList to set
	 */
	public void setUserList(List<Parameter> userList) {
		this.userList = userList;
	}
	/**
	 * @return the fomsAssetTypeMap
	 */
	public Map<String, String> getFomsAssetTypeMap() {
		return fomsAssetTypeMap;
	}
	/**
	 * @param fomsAssetTypeMap the fomsAssetTypeMap to set
	 */
	public void setFomsAssetTypeMap(Map<String, String> fomsAssetTypeMap) {
		this.fomsAssetTypeMap = fomsAssetTypeMap;
	}
	/**
	 * @return the contractMap
	 */
	public Map<String, List<Parameter>> getContractMap() {
		return contractMap;
	}
	/**
	 * @param contractMap the contractMap to set
	 */
	public void setContractMap(Map<String, List<Parameter>> contractMap) {
		this.contractMap = contractMap;
	}
	/**
	 * @return the srmCaseNewHandleInfoDAO
	 */
	public ISrmCaseNewHandleInfoDAO getSrmCaseNewHandleInfoDAO() {
		return srmCaseNewHandleInfoDAO;
	}
	/**
	 * @return the srmCaseNewTransactionParameterDAO
	 */
	public ISrmCaseNewTransactionParameterDAO getSrmCaseNewTransactionParameterDAO() {
		return srmCaseNewTransactionParameterDAO;
	}
	/**
	 * @return the srmCaseNewAssetLinkDAO
	 */
	public ISrmCaseNewAssetLinkDAO getSrmCaseNewAssetLinkDAO() {
		return srmCaseNewAssetLinkDAO;
	}
	/**
	 * @return the srmCaseNewAssetFunctionDAO
	 */
	public ISrmCaseNewAssetFunctionDAO getSrmCaseNewAssetFunctionDAO() {
		return srmCaseNewAssetFunctionDAO;
	}
	/**
	 * @param srmCaseNewHandleInfoDAO the srmCaseNewHandleInfoDAO to set
	 */
	public void setSrmCaseNewHandleInfoDAO(
			ISrmCaseNewHandleInfoDAO srmCaseNewHandleInfoDAO) {
		this.srmCaseNewHandleInfoDAO = srmCaseNewHandleInfoDAO;
	}
	/**
	 * @param srmCaseNewTransactionParameterDAO the srmCaseNewTransactionParameterDAO to set
	 */
	public void setSrmCaseNewTransactionParameterDAO(
			ISrmCaseNewTransactionParameterDAO srmCaseNewTransactionParameterDAO) {
		this.srmCaseNewTransactionParameterDAO = srmCaseNewTransactionParameterDAO;
	}
	/**
	 * @param srmCaseNewAssetLinkDAO the srmCaseNewAssetLinkDAO to set
	 */
	public void setSrmCaseNewAssetLinkDAO(
			ISrmCaseNewAssetLinkDAO srmCaseNewAssetLinkDAO) {
		this.srmCaseNewAssetLinkDAO = srmCaseNewAssetLinkDAO;
	}
	/**
	 * @param srmCaseNewAssetFunctionDAO the srmCaseNewAssetFunctionDAO to set
	 */
	public void setSrmCaseNewAssetFunctionDAO(
			ISrmCaseNewAssetFunctionDAO srmCaseNewAssetFunctionDAO) {
		this.srmCaseNewAssetFunctionDAO = srmCaseNewAssetFunctionDAO;
	}
	/**
	 * @return the companyNameList
	 */
	public List<Parameter> getCompanyNameList() {
		return companyNameList;
	}
	/**
	 * @param companyNameList the companyNameList to set
	 */
	public void setCompanyNameList(List<Parameter> companyNameList) {
		this.companyNameList = companyNameList;
	}
	/**
	 * @return the errorFlag
	 */
	public String getErrorFlag() {
		return errorFlag;
	}
	/**
	 * @param errorFlag the errorFlag to set
	 */
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	/**
	 * @return the repositoryService
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * @param repositoryService the repositoryService to set
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * @return the suppliesTypeList
	 */
	public List<Parameter> getSuppliesTypeList() {
		return suppliesTypeList;
	}

	/**
	 * @param suppliesTypeList the suppliesTypeList to set
	 */
	public void setSuppliesTypeList(List<Parameter> suppliesTypeList) {
		this.suppliesTypeList = suppliesTypeList;
	}

	/**
	 * @return the suppliesDAO
	 */
	public ISuppliesTypeDAO getSuppliesDAO() {
		return suppliesDAO;
	}

	/**
	 * @param suppliesDAO the suppliesDAO to set
	 */
	public void setSuppliesDAO(ISuppliesTypeDAO suppliesDAO) {
		this.suppliesDAO = suppliesDAO;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}

	/**
	 * @return the isOkTmsApp
	 */
	public boolean isOkTmsApp() {
		return isOkTmsApp;
	}

	/**
	 * @param isOkTmsApp the isOkTmsApp to set
	 */
	public void setOkTmsApp(boolean isOkTmsApp) {
		this.isOkTmsApp = isOkTmsApp;
	}

	/**
	 * @return the srmCaseCommModeDAO
	 */
	public ISrmCaseCommModeDAO getSrmCaseCommModeDAO() {
		return srmCaseCommModeDAO;
	}

	/**
	 * @param srmCaseCommModeDAO the srmCaseCommModeDAO to set
	 */
	public void setSrmCaseCommModeDAO(ISrmCaseCommModeDAO srmCaseCommModeDAO) {
		this.srmCaseCommModeDAO = srmCaseCommModeDAO;
	}

	/**
	 * @return the srmCaseNewCommModeDAO
	 */
	public ISrmCaseNewCommModeDAO getSrmCaseNewCommModeDAO() {
		return srmCaseNewCommModeDAO;
	}

	/**
	 * @param srmCaseNewCommModeDAO the srmCaseNewCommModeDAO to set
	 */
	public void setSrmCaseNewCommModeDAO(ISrmCaseNewCommModeDAO srmCaseNewCommModeDAO) {
		this.srmCaseNewCommModeDAO = srmCaseNewCommModeDAO;
	}

	/**
	 * @return the edcTypeList
	 */
	public List<Parameter> getEdcTypeList() {
		return edcTypeList;
	}

	/**
	 * @param edcTypeList the edcTypeList to set
	 */
	public void setEdcTypeList(List<Parameter> edcTypeList) {
		this.edcTypeList = edcTypeList;
	}

	/**
	 * @return the edcCategoryList
	 */
	public List<Parameter> getEdcCategoryList() {
		return edcCategoryList;
	}

	/**
	 * @param edcCategoryList the edcCategoryList to set
	 */
	public void setEdcCategoryList(List<Parameter> edcCategoryList) {
		this.edcCategoryList = edcCategoryList;
	}

	/**
	 * @return the relatedProductsCategoryList
	 */
	public List<Parameter> getRelatedProductsCategoryList() {
		return RelatedProductsCategoryList;
	}

	/**
	 * @param relatedProductsCategoryList the relatedProductsCategoryList to set
	 */
	public void setRelatedProductsCategoryList(
			List<Parameter> relatedProductsCategoryList) {
		RelatedProductsCategoryList = relatedProductsCategoryList;
	}

}
