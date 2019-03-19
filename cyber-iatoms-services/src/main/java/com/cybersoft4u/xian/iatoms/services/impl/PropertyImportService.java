package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsLogonUser;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AdmUserDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetInListDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.AssetTypeDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimContractDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimMerchantHeaderDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.DmmRepositoryDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.MerchantDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.PropertyNumberImportFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.report.JasperReportCriteriaDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.ValidateUtils;
import com.cybersoft4u.xian.iatoms.services.IPropertyImportService;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAdmUserWarehouseDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetInListDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IContractDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IDmmRepositoryHistoryMonthlyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IMerchantHeaderDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.AdmUser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** 
 * Purpose: 財產批號匯入service
 * @author CarrieDuan
 * @since  JDK 1.6
 * @date   2016年7月4日
 * @MaintenancePersonnel CarrieDuan
 */
public class PropertyImportService extends AtomicService implements IPropertyImportService, ApplicationContextAware {

	/**
	 * 系统日志文件控件
	 */
	private static final Log LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, PropertyImportService.class);
	/**
	 * 庫存表
	 */
	private IDmmRepositoryDAO repositoryDAO;
	/**
	 * 庫存歷史表
	 */
	private IDmmRepositoryHistoryDAO repositoryHistoryDAO;
	
	/**
	 * 合約DAO
	 */
	private IContractDAO contractDAO;
	
	/**
	 * 公司類型DAO
	 */
	private ICompanyTypeDAO companyTypeDAO;
	
	/**
	 * 設備類別DAO
	 */
	private IAssetTypeDAO assetTypeDAO;
	
	/**
	 * 使用者帳號DAO
	 */
	private IAdmUserDAO userDAO;
	/**
	 * 使用者控管資料dao
	 */
	private IAdmUserWarehouseDAO userWarehouseDAO;
	
	/**
	 * 公司DAO
	 */
	private ICompanyDAO companyDAO;
	/**
	 * 入庫DAO
	 */
	private IAssetInInfoDAO assetInInfoDAO;
	/**
	 * 庫存明細檔
	 */
	private IAssetInListDAO assetInListDAO;
	/**
	 * 特店DAO
	 */
	private IMerchantDAO merchantDAO;
	/**
	 * 特店表头DAO
	 */
	private IMerchantHeaderDAO merchantHeaderDAO;
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * 
	 */
	private ApplicationContext applicationContext;
	/**
	 * 庫存月檔表
	 */
	private IDmmRepositoryHistoryMonthlyDAO monthlyDAO;
	/**
	 * 無慘構造函數
	 */
	public PropertyImportService() {
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IMailListService#init(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		// 页面显示信息
		Message msg = null;
		try {
			PropertyNumberImportFormDTO formDTO = (PropertyNumberImportFormDTO) sessionContext.getRequestParameter();
			String size = SystemConfigManager.getProperty(IAtomsConstants.SETTING, IAtomsConstants.UPLOAD_FILE_SIZE);
			if (!StringUtils.hasText(size)) {
				size = IAtomsConstants.PARAM_UPLOAD_FILE_SIZE;
			}
			int rate = Integer.valueOf(IAtomsConstants.PARAM_UPLOAD_CONVERSION_RATE);
			size = String.valueOf(Integer.valueOf(size) * rate);
			formDTO.setUploadFileSize(size);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.INIT_PAGE_SUCCESS);
			sessionContext.setReturnMessage(msg);
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPropertyImportService#upload(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Override
	public SessionContext checkAssetData(SessionContext sessionContext) throws ServiceException {
		Message msg = new Message();
		try {
			//根據設備編號查詢設備的設備類型，設備名稱，原財產編號
			AssetInListDTO assetInListDTO  = null;
			String serialNumber = null;//設備編號
			String assetName = null;//設備名稱
			String proteryId = null;//財產編號
			boolean isErrorData = false;//是否有错误格式的资料
			boolean isError = false;//资料是否全部合格
			//汇入返回页面的设备明细DTO
			List<DmmRepositoryDTO> repositoryDTOs  = new ArrayList<DmmRepositoryDTO>();
			//汇入台新租認維護dto
			List<DmmRepositoryDTO> repositoryDTOTaixins  = new ArrayList<DmmRepositoryDTO>();
			//汇入捷達威維護dto
			List<DmmRepositoryDTO> repositoryDTOJdws  = new ArrayList<DmmRepositoryDTO>();
			//沒有錯誤格式的財產編號DMO集合
			List<AssetInListDTO> list = new ArrayList<AssetInListDTO>();
			PropertyNumberImportFormDTO formDTO = (PropertyNumberImportFormDTO) sessionContext.getRequestParameter();
			//獲取異動欄位
			Map<String, String> updateColumns = formDTO.getAssetMap();
			//匯入的文件
			MultipartFile uploadFiled = formDTO.getUploadFiled();
			//獲取當前登錄者信息
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			//下載的錯誤文件信息名稱
			Map errorFileMap = new HashMap();
			// 用於記錄錯誤信息以及錯誤行號
			List<String> errorMsgs = new ArrayList<String>();
			//保存核檢成功後的數據的文件名稱
			String fileName = formDTO.getFileName();
			if (uploadFiled != null) {
				//沒有出錯匯入的設備財產編號
				msg  = this.checkUploadFiled(uploadFiled, repositoryDTOs, repositoryDTOTaixins, repositoryDTOJdws, errorMsgs, updateColumns, 
						formDTO);
				//如果超過500行，直接返回錯誤信息。
				if (msg == null) {
					if (!CollectionUtils.isEmpty(errorMsgs)) {
						//有錯誤記錄，則將錯誤記錄保存到TXT文件中。
						errorFileMap = this.saveErrorMsg(errorMsgs);
						msg = new Message(Message.STATUS.FAILURE);
					}else {
						msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPLOAD_SECCUSS);
					}
				}
			}
			
			Map map = new HashMap();
			if (!errorFileMap.isEmpty()) {
				map.put(PropertyNumberImportFormDTO.ERROR_FILE_NAME, errorFileMap.get(PropertyNumberImportFormDTO.ERROR_FILE_NAME));
				map.put(PropertyNumberImportFormDTO.ERROR_FILE_PATH, errorFileMap.get(PropertyNumberImportFormDTO.ERROR_FILE_PATH));
			}
			if (Message.STATUS.FAILURE.equals(msg.getStatus())) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.FALSE);
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.TRUE);
				//保存文件致臨時目錄
				fileName = this.saveFileInfo(fileName, repositoryDTOs, repositoryDTOTaixins, repositoryDTOJdws);
				map.put("fileName", fileName);
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
			map.put("taixinListTotal", repositoryDTOTaixins.size());
			map.put("taixinList", repositoryDTOTaixins);
			map.put("jdwMaintenanceTotal", repositoryDTOJdws.size());
			map.put("jdwMaintenances", repositoryDTOJdws);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, repositoryDTOs);
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_TOTAL, Integer.valueOf(repositoryDTOs.size()));
			map.put(IAtomsConstants.PARAM_QUERY_RESULT_ROWS, repositoryDTOs);
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (Exception e) {
			LOGGER.error(".upload is error!!!", e);
			throw new ServiceException(IAtomsMessageCode.UPLOAD_FAILURE, e);
		}
		return sessionContext;
	}
	
	private String getFilePath(String fileName) {
		String path = null;
		try {
			String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			path = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH);
			path += File.separator + yearMonthDay + File.separator + IAtomsConstants.UC_NO_DMM_03030 + File.separator 
					+ fileName + JasperReportCriteriaDTO.REPORT_EXT_NAME_TXT;
		} catch (Exception e) {
			LOGGER.error(".getFilePath is error!!!", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return path;
	}
	
	private String saveFileInfo(String fileName, List<DmmRepositoryDTO> repositoryDTOs,
			List<DmmRepositoryDTO> repositoryDTOTaixins, List<DmmRepositoryDTO> repositoryDTOJdws) throws ServiceException {
		try {
			// 获取实体类的所有属性，返回Field数组    
			Field[] fields = DmmRepositoryDTO.class.getDeclaredFields();
			String saveInfo = null;
			if (!StringUtils.hasText(fileName)) {
				fileName = UUID.randomUUID().toString();
			}
			String path = this.getFilePath(fileName.concat("_1"));
			saveInfo = this.getAlixIntegrity(repositoryDTOs, fields);
			FileUtils.contentToTxt(path.toString(), saveInfo);
			path = this.getFilePath(fileName.concat("_2"));
			saveInfo = this.getAlixIntegrity(repositoryDTOTaixins, fields);
			FileUtils.contentToTxt(path.toString(), saveInfo);
			path = this.getFilePath(fileName.concat("_3"));
			saveInfo = this.getAlixIntegrity(repositoryDTOJdws, fields);
			FileUtils.contentToTxt(path.toString(), saveInfo);
		} catch (Exception e) {
			LOGGER.error(".saveFileInfo is error!!!", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		}
		return fileName;
	}
	
	public String getAlixIntegrity(List<DmmRepositoryDTO> dmmRepositoryDTOs, Field[] fields) throws NoSuchMethodException, Exception{ 
		StringBuffer saveInfo = new StringBuffer();
		if (fields != null && !CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
			for (DmmRepositoryDTO dto : dmmRepositoryDTOs) {
				for (Field field : fields) {
					// 获取属性的名字    
					String name = field.getName();
					if ("serialVersionUID".equals(name) || "tSam".equals(name)) {
						continue;
					}
					// 获取属性的类型    
					String type = field.getGenericType().toString();  
					// 将属性的首字符大写，方便构造get，set方法
					name = name.substring(0, 1).toUpperCase() + name.substring(1);
					Method m = dto.getClass().getMethod("get" + name);
					// 调用getter方法获取属性值
					Object value =  m.invoke(dto);
					if (value != null && value != "") {
						if (type.equals("class java.sql.Timestamp")
								|| type.equals("class java.util.Date")) {
							saveInfo.append(field.getName()).append("(=)").append(DateTimeUtils.toString((Date)value, DateTimeUtils.DT_FMT_YYYYMMDD_SLASH))
								.append("(P;.)");
						} else if (type.equals("class java.lang.Integer") || type.equals("int")) {
							
						} else {
							if (StringUtils.hasText(value.toString())) {
								saveInfo.append(field.getName()).append("(=)").append(value.toString()).append("(P;.)");
							}
						}
					}
				}
				saveInfo.append("(M;.)");
			}
			return saveInfo.substring(0, saveInfo.length() - 5);
		}
		return saveInfo.toString();
	}
	
	public List<DmmRepositoryDTO> setAlixIntegrity(String textInfo) throws NoSuchMethodException, Exception{
		List<DmmRepositoryDTO> dmmRepositoryDTOs = new ArrayList<DmmRepositoryDTO>();
		if (StringUtils.hasText(textInfo)) {
			String[] infos = textInfo.split("\\(M;.\\)");
			DmmRepositoryDTO dmmRepositoryDTO = null;
			for (String info : infos) {
				String[] maps = info.split("\\(P;.\\)");
				dmmRepositoryDTO = new DmmRepositoryDTO();
				for (String map : maps) {
					String key = map.split("\\(=\\)")[0];
					String value = map.split("\\(=\\)")[1];
					//调用getDeclaredField("name") 取得name属性对应的Field对象
					Field field = dmmRepositoryDTO.getClass().getDeclaredField(key);
					// 取消属性的访问权限控制，即使private属性也可以进行访问。
					field.setAccessible(true);
					// 调用set()方法给对应属性赋值。
					// 获取属性的类型    
					String type = field.getGenericType().toString();
					if (type.equals("class java.sql.Timestamp")
							|| type.equals("class java.util.Date")) {
						field.set(dmmRepositoryDTO, DateTimeUtils.toTimestamp(value));
					} else if (type.equals("class java.lang.Integer") || type.equals("int")) {
						field.set(dmmRepositoryDTO, Integer.valueOf(value));
					} else if (type.equals("class java.lang.Double")) {
						field.set(dmmRepositoryDTO, Double.valueOf(value));
					} else {
						field.set(dmmRepositoryDTO, value);
					}
				}
				dmmRepositoryDTOs.add(dmmRepositoryDTO);
			}
		}
		
		return dmmRepositoryDTOs;
	}
	
	private String getTxtInfo(File file) throws ServiceException {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			if (br != null) {
				while((s = br.readLine())!=null){//使用readLine方法，一次读一行
					result.append(s);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(".saveFileInfo is error!!!", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		}
		return result.toString();
	}
	private Map<String, List<DmmRepositoryDTO>> getFileInfo(String fileName) throws ServiceException {
		Map<String, List<DmmRepositoryDTO>> map = new HashMap<String, List<DmmRepositoryDTO>>();
		try {
			String path = this.getFilePath(fileName+"_1");
			File file = new File(path);
			String saveInfo = this.getTxtInfo(file);
			map.put("repositoryDTOs", this.setAlixIntegrity(saveInfo));
			path = this.getFilePath(fileName+"_2");
			File file1 = new File(path);
			saveInfo = this.getTxtInfo(file1);
			map.put("taixin", this.setAlixIntegrity(saveInfo));
			path = this.getFilePath(fileName+"_3");
			File file2 = new File(path);
			saveInfo = this.getTxtInfo(file2);
			map.put("jdw", this.setAlixIntegrity(saveInfo));
		} catch (Exception e) {
			LOGGER.error(".saveFileInfo is error!!!", e);
			throw new ServiceException(IAtomsMessageCode.SAVE_FAILURE, e);
		}
		return map;
	}
	/**
	 * 
	 * Purpose:
	 * @author allenchen
	 * @param uploadFiled:上傳文件
	 * @param errorAssetInList:返回所有資料
	 * @param isErrorData:返回結果:true資料有錯誤,false資料格式正確
	 * @return boolean:是否出錯
	 */
	private Message checkUploadFiled(MultipartFile uploadFiled, List<DmmRepositoryDTO> repositoryDTOs, 
			List<DmmRepositoryDTO> repositoryDTOTaixins, List<DmmRepositoryDTO> repositoryDTOJdws, List<String> errorMsgs, Map<String, String> updateColumns, 
			PropertyNumberImportFormDTO formDTO) {
		DmmRepositoryDTO dmmRepositoryDTO = null;
		Message message = null;
		try {
			if (uploadFiled != null) {
				//獲取當前登錄者信息
				IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
				// 获取上传文件输入流
				InputStream inputStream = uploadFiled.getInputStream();
				Workbook workbook = null;
				Sheet sheet = null;
				Row row = null;
				int rowCount = 0;
				String fileName = uploadFiled.getOriginalFilename();
				String fileTxt = fileName.substring(fileName.lastIndexOf(IAtomsConstants.MARK_NO) + 1);
				if (IAtomsConstants.FILE_TXT_MSEXCEL.equals(fileTxt)) {
					//2003版本
					workbook = new HSSFWorkbook(inputStream);
				} else if (IAtomsConstants.FILE_TXT_MSEXCEL_X.equals(fileTxt)) {
					//2007版本
					workbook = new XSSFWorkbook(inputStream);
				}
				if (workbook != null) {
					sheet = workbook.getSheetAt(0);
				} else {
					String errorMsg = i18NUtil.getName(IAtomsMessageCode.FILE_FORMAT_ERROR);
					LOGGER.error("workbook is null >>> ");
					throw new ServiceException();
				}
				//設備序號
				String serialNumber = null;
				//財產編號
				String propertyId = null;
				String assetName=null;
				String contractCode=null;
				String simEnableNo=null;
				String enableDate=null;
				String assetOwner=null;
				String assetUser=null;
				String cyberApproveDate=null;
				String assetInTime=null;
				String simEnableDate = null;
				String counter = null;
				String cartonNo = null;
				String brand = null;
				String model = null;
				String updateTableName = null;
				//Task #3393 異動欄位-新增維護模式
				String maintenanceMode = null;
				boolean isProperIdExist = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.PROPERTY_ID.getValue()) != null;
				boolean isAssetNameExist = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.ASSET_TYPE_NAME.getValue()) != null;
				boolean isContractCodeExist = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.CONTRACT_CODE.getValue()) != null;
				boolean isSimEnableNoExist = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_NO.getValue()) != null;
				boolean isEnableDateExist = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue()) != null;
				boolean isAssetOwnerExist= updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.ASSET_OWNER_NAME.getValue()) != null;
				boolean isAssetUserExist= updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.ASSET_USER_NAME.getValue()) != null;
				boolean isCyberApproveDateExist = updateColumns.get(AssetInInfoDTO.ATTRIBUTE.CYBER_APPROVE_DATE.getValue()) != null;
				boolean isAssetInTimeExist = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.ASSET_IN_TIME.getValue()) != null;
				boolean isSimEnableDate = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.SIM_ENABLE_DATE.getValue()) != null;
				boolean isCounter = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.COUNTER.getValue()) != null;
				boolean isCartonNo = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.CARTON_NO.getValue()) != null;
				boolean isTaixinMaintenance = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.TAI_XIN_MAINTENANCE.getValue()) != null;
				boolean isJdwMaintenance = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.JDW_MAINTENANCE.getValue()) != null;
				boolean isBrand = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.BRAND.getValue()) != null;
				boolean isModel = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.MODEL.getValue()) != null;
				boolean isMaintenanceMode = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.MAINTENANCE_MODE.getValue()) != null;
				List<String> tempErrorMsg = new ArrayList<String>();
				//核檢需要修改的表
				String updateTable = formDTO.getUpdateTable();
				String monthYear = formDTO.getMonthYear();
				if (IAtomsConstants.LEAVE_CASE_STATUS_ONE.equals(updateTable)) {
					updateTableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY;
				} else if (IAtomsConstants.LEAVE_CASE_STATUS_TWO.equals(updateTable)) {
					updateTableName = IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY;
					if (!StringUtils.hasText(monthYear)) {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_INPUT_MONTH_YEAR);
					}
				} else {
					message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPDATE_TABLE_INPUT_ERROR);
				}
				// 獲取行數
				//rowCount = sheet.getPhysicalNumberOfRows();
				if ((isProperIdExist || isAssetNameExist || isContractCodeExist || isSimEnableNoExist || isEnableDateExist || isAssetOwnerExist
							|| isAssetUserExist || isCyberApproveDateExist || isAssetInTimeExist || isSimEnableDate || isCounter || isCartonNo
							|| isBrand || isModel || isMaintenanceMode) && message == null) {
					rowCount = this.getExcelRealRowCount(sheet);
					if (rowCount <= 1) {
						errorMsgs.add("");
						errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ASSET_UPDATE_ERROR_MESSAGE));
						errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.IATOMS_MSG_NONE_DATA));
					} else if (rowCount > 501) {
						message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT, new String[]{PropertyNumberImportFormDTO.UPLOAD_NUMBER });
					} else {
						DmmRepositoryDTO repositoryDTO = new DmmRepositoryDTO();
						//標記是否有錯誤信息
						boolean isError = false;
						Map<String, Integer> propertyIdRepeatMap = new HashMap<String, Integer>();
						//記錄設備序號重複的行號
						Map<String, Integer> serialNumberRepeatMap = new HashMap<String, Integer>();
						//獲取合約列表
						List<Parameter> contracts = this.contractDAO.getContractCodeList("", null, Boolean.FALSE, "", "", Boolean.TRUE);
						//修改使用人，資產owner驗證--update by 2017-05-16
						List<String> params = new ArrayList<String>();
						params.add(IAtomsConstants.PARAM_COMPANY_TYPE_CUSTOMER);
						params.add(IAtomsConstants.PARAM_COMPANY_TYPE_HARDWARE_VENDOR);
						//獲取公司列表--update by 2017-05-16
						List<Parameter> companys = this.companyDAO.getCompanyList(params, null, false, null);
						//獲取設備列表
						List<Parameter> assetTypes = this.assetTypeDAO.getAssetNameList(null, null, null, null, IAtomsConstants.NO);
						//Task #3393 獲取維護模式列表
						List<Parameter> maintenanceModeList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.MA_TYPE.getCode(), null);
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd ");
						
						for (int i = 1; i <= rowCount && message == null; i++) {
							isError = false;
							dmmRepositoryDTO = new DmmRepositoryDTO();
							//獲取行
							row = sheet.getRow(i);
							if (isRowEmpty(row).booleanValue()) {
								continue;
							} 
							//获取设备編號
							serialNumber = this.getCellFormatValue(row.getCell(0), null, Boolean.TRUE);
							//判斷設備序號是否為空
							if(!StringUtils.hasText(serialNumber)) {
								tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
										.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)}, null)) );
							} else {
								//判斷是有具有控管權限
								if (checkDataAcl(serialNumber, logonUser)) {
									//获取财产编号
									propertyId = this.getCellFormatValue(row.getCell(2), null, Boolean.TRUE);
									contractCode = this.getCellFormatValue(row.getCell(3), null, Boolean.TRUE);
									assetName = this.getCellFormatValue(row.getCell(1), null, Boolean.TRUE);
									assetUser = this.getCellFormatValue(row.getCell(7), null, Boolean.TRUE);
									assetOwner = this.getCellFormatValue(row.getCell(6), null, Boolean.TRUE);
									//驗證設備序號是否檔內重複
									if (serialNumberRepeatMap.containsKey(serialNumber)) {
										tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), serialNumberRepeatMap.get(serialNumber).toString()}, null) + 
												i18NUtil.getName(IAtomsMessageCode.SERIAL_NUMBER_REPETITION));
										continue;
									} else {
										serialNumberRepeatMap.put(serialNumber, i + 1);
									}
									dmmRepositoryDTO.setUpdateTable(updateTableName);
									dmmRepositoryDTO.setMonthYear(monthYear);
									if (IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY.equals(dmmRepositoryDTO.getUpdateTable())) {
										repositoryDTO = this.monthlyDAO.getRepositoryBySerialNumber(serialNumber, dmmRepositoryDTO.getMonthYear(), null);
									} else {
										repositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, null, null);
									}
									if (repositoryDTO == null) {
										tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i + 1))}, null) 
														.concat(PropertyNumberImportFormDTO.NOT_ASSET_INFO));
									} else {
										//核檢財產編號
										if (isProperIdExist) {
											if (StringUtils.hasText(propertyId)) {
												//核檢財產編號是否檔內重複
												if (propertyIdRepeatMap.containsKey(propertyId)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), propertyIdRepeatMap.get(propertyId).toString()}, null) + 
															i18NUtil.getName(IAtomsMessageCode.PROPERTY_ID_REPETITION));
													continue;
												} else {
													propertyIdRepeatMap.put(propertyId, i+1);
												}
												//核檢財產編號是否重複
												//核檢財產編號與設備入庫資料明細檔是否重複
												boolean isAssetInListRepeat = this.assetInListDAO.isCheckRropertyIdRepeat(propertyId);
												//核檢財產編號與庫存主檔是否重複
												boolean isRepositoryRepeat = this.repositoryDAO.isPropertyIdRepeat(propertyId, null);
												if (isAssetInListRepeat || isRepositoryRepeat) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PROPERTY_ID_IS_REPETITION));
													isError = true;
												}
												//判斷財產編號的長度是否符合要求
												if(propertyId.trim().length() > Integer.valueOf(IAtomsConstants.PROPERTY_ID_LENGTH)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID), IAtomsConstants.PROPERTY_ID_LENGTH}, null));
													isError = true;
												}
												//核檢財產編號格式
												if (!ValidateUtils.numberOrEnglish(propertyId)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i + 1))}, null) 
															+ i18NUtil.getName(IAtomsMessageCode.NUMBER_OR_ENGLISH, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)}, null));
													isError = true;
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_PROPERTY_ID)}, null));
												isError = true;
											}
										}
										//核檢設備名稱
										if(isAssetNameExist) {
											if (StringUtils.hasText(assetName)) {
												String assetTypeId = this.getValueByName(assetTypes, assetName);
												if (!StringUtils.hasText(assetTypeId)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_NAME)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setAssetTypeName(assetName);
													dmmRepositoryDTO.setAssetTypeId(assetTypeId);
													dmmRepositoryDTO.setOldAssetType(repositoryDTO.getName());
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_NAME)}, null));
												isError = true;
											}
										}
										//驗證合約編號
										if (isContractCodeExist) {
											//判斷合約編號使否輸入
											if (StringUtils.hasText(contractCode)) {
												String contractId = this.getValueByName(contracts, contractCode);
												//如果不重複，表示合約編號不存在
												if (!StringUtils.hasText(contractId)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CONTRACT_CODE)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setContractCode(contractCode);
													dmmRepositoryDTO.setContractId(contractId);
													dmmRepositoryDTO.setOldContractCode(repositoryDTO.getContractCode());
													dmmRepositoryDTO.setOldCyberApprovedDate(DateTimeUtils.toDate(formatter.format(repositoryDTO.getCyberApprovedDate())));
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_CONTRACT_CONTRACT_CODE)}, null));
												isError = true;
											}
										}
										//核檢租賃編號
										if (isSimEnableNoExist) {
											//獲取匯入文件租賃編號
											simEnableNo = this.getCellFormatValue(row.getCell(4), null, Boolean.TRUE);
											if (StringUtils.hasText(simEnableNo)) {
												//核檢租賃編號長度
												if (simEnableNo.trim().length() > Integer.valueOf(IAtomsConstants.PROPERTY_ID_LENGTH)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_SIM_ENABLE_NO), IAtomsConstants.PROPERTY_ID_LENGTH}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setSimEnableNo(simEnableNo);
													dmmRepositoryDTO.setOldSimEnableNo(repositoryDTO.getSimEnableNo());
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_SIM_ENABLE_NO)}, null));
												isError = true;
											}
										}
										//核檢設備啟用日
										if (isEnableDateExist) {
											//獲取設備啟用日
											enableDate = this.getCellFormatValue(row.getCell(5), "yyyy/MM/dd", Boolean.FALSE);
											if (StringUtils.hasText(enableDate)) {
												//核檢日期格式是否符合YYYY/MM/DD
												if (!ValidateUtils.checkDate(enableDate)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_ENABLE_DATE)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setEnableDate(DateTimeUtils.toTimestamp(enableDate));
													dmmRepositoryDTO.setOldEnableDate(repositoryDTO.getEnableDate());
												}
											} else {
												//Task2554-設備啟用日 欄位，可以異動成空白
												dmmRepositoryDTO.setEnableDate(null);
												dmmRepositoryDTO.setOldEnableDate(repositoryDTO.getEnableDate());
											}
										}
										//核檢資產owner
										if (isAssetOwnerExist) {
											if (StringUtils.hasText(assetOwner)) {
												String assetOwnerId = this.getValueByName(companys, assetOwner);
												if (!StringUtils.hasText(assetOwnerId)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_OWNER)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setAssetOwnerName(assetOwner);
													dmmRepositoryDTO.setAssetOwner(assetOwnerId);
													dmmRepositoryDTO.setOldAssetOwner(repositoryDTO.getAssetOwnerName());
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_OWNER)}, null));
												isError = true;
											}
										}
										//核檢使用人
										if (isAssetUserExist) {
											if (StringUtils.hasText(assetUser)) {
												String userId = this.getValueByName(companys, assetUser);
												if (!StringUtils.hasText(userId)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_USER_ID)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setAssetUserName(assetUser);
													dmmRepositoryDTO.setAssetUser(userId);
													dmmRepositoryDTO.setOldAssetUser(repositoryDTO.getAssetUserName());
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_USER_ID)}, null));
												isError = true;
											}
										}
										//核檢Cyber驗收日期
										if (isCyberApproveDateExist) {
											//獲取cyber驗收日期
											cyberApproveDate = this.getCellFormatValue(row.getCell(8), "yyyy/MM/dd", Boolean.FALSE);
											if (StringUtils.hasText(cyberApproveDate)) {
												//核檢日期格式是否符合YYYY/MM/DD
												if (!ValidateUtils.checkDate(cyberApproveDate)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_CYBER_APPROVED_DATE)}, null));
													isError = true;
												} else {
													String oldCyberDate = null;
													if (repositoryDTO.getCyberApprovedDate() != null) {
														oldCyberDate = formatter.format(repositoryDTO.getCyberApprovedDate());
														dmmRepositoryDTO.setOldCyberApprovedDate(DateTimeUtils.toDate(oldCyberDate));
													}
													dmmRepositoryDTO.setCyberApprovedDate(DateTimeUtils.toDate(cyberApproveDate));
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_CYBER_APPROVED_DATE)}, null));
												isError = true;
											}
										}
										//核檢入庫日期
										if (isAssetInTimeExist) {
											//獲取入庫日期
											assetInTime = this.getCellFormatValue(row.getCell(9), "yyyy/MM/dd", Boolean.FALSE);
											if (StringUtils.hasText(assetInTime)) {
												//核檢日期格式是否符合YYYY/MM/DD
												if (!ValidateUtils.checkDate(assetInTime)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_ASSET_IN_TIME)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setAssetInTime(DateTimeUtils.toTimestamp(assetInTime));
													dmmRepositoryDTO.setOldAssetInTime(repositoryDTO.getAssetInTime());
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_ASSET_IN_TIME)}, null));
												isError = true;
											}
										}
										//租賃期滿日
										if (isSimEnableDate) {
											//獲取租賃期滿日
											simEnableDate = this.getCellFormatValue(row.getCell(10), "yyyy/MM/dd", Boolean.FALSE);
											if (StringUtils.hasText(simEnableDate)) {
												//核檢日期格式是否符合YYYY/MM/DD
												if (!ValidateUtils.checkDate(simEnableDate)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_SIM_ENABLE_DATE)}, null));
													isError = true;
												} else {
													dmmRepositoryDTO.setSimEnableDate(DateTimeUtils.toTimestamp(simEnableDate));
													if (repositoryDTO.getSimEnableDate() != null) {
														String oldSimEnableDate = formatter.format(repositoryDTO.getSimEnableDate());
														dmmRepositoryDTO.setOldSimEnableDate(DateTimeUtils.toDate(oldSimEnableDate));
													}
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_SIM_ENABLE_DATE)}, null));
												isError = true;
											}
										}
										//核檢櫃位
										if (isCounter) {
											//獲取櫃號
											counter = this.getCellFormatValue(row.getCell(11), null, Boolean.TRUE);
											dmmRepositoryDTO.setOldCounter(repositoryDTO.getCounter());
											if (StringUtils.hasText(counter)) {
												//核檢租賃編號長度
												if (counter.trim().length() > Integer.valueOf(IAtomsConstants.PROPERTY_ID_LENGTH)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_COUNTER), IAtomsConstants.PROPERTY_ID_LENGTH}, null));
													isError = true;
												} 
												if (!ValidateUtils.inputCharacter(counter, Boolean.FALSE)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.INPUT_ENGLISH_NUMBER_OR_ENGLISH_SYMBOLS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_COUNTER)}, null));
													isError = true;
												} 
												if (!isError) {
													dmmRepositoryDTO.setCounter(counter);
												}
											}
										}
										//核檢箱號
										if (isCartonNo) {
											//獲取箱號
											cartonNo = this.getCellFormatValue(row.getCell(12), null, Boolean.TRUE);
											dmmRepositoryDTO.setOldCartonNo(repositoryDTO.getCartonNo());
											if (StringUtils.hasText(cartonNo)) {
												//核檢租賃編號長度
												if (cartonNo.trim().length() > Integer.valueOf(IAtomsConstants.CONTACT_USER_LENGTH)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CARTON_NO), IAtomsConstants.CONTACT_USER_LENGTH}, null));
													isError = true;
												} 
												if (!ValidateUtils.inputCharacter(cartonNo, Boolean.FALSE)) {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.INPUT_ENGLISH_NUMBER_OR_ENGLISH_SYMBOLS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CARTON_NO)}, null));
													isError = true;
												}
												if (!isError) {
													dmmRepositoryDTO.setCartonNo(cartonNo);
												}
											}
										}
										String assetTypeId = null;
										if (StringUtils.hasText(dmmRepositoryDTO.getAssetTypeId())) {
											assetTypeId = dmmRepositoryDTO.getAssetTypeId();
										} else {
											assetTypeId = repositoryDTO.getAssetTypeId();
										}
										//核檢設備廠牌
										if (isBrand) {
											//獲取設備廠牌
											brand = this.getCellFormatValue(row.getCell(13), null, Boolean.TRUE);
											if (StringUtils.hasText(brand)) {
												brand = brand.toLowerCase();
												String brands = this.assetTypeDAO.getAssetInfo(assetTypeId, AssetTypeDTO.ATTRIBUTE.BRAND.getValue());
												String tempBrand = null;
												if (StringUtils.hasText(brands)) {
													List<String> tempBrands = StringUtils.toList(brands, IAtomsConstants.MARK_SEPARATOR);
													for (String temp : tempBrands) {
														if (brand.equals(temp.toLowerCase())) {
															tempBrand = temp;
															break;
														}
													}
													if (StringUtils.hasText(tempBrand)) {
														dmmRepositoryDTO.setBrand(tempBrand);
														dmmRepositoryDTO.setOldBrand(repositoryDTO.getBrand());
													} else {
														tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
																+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_BRAND)}, null));
														isError = true;
													}
												} else {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_BRAND)}, null));
													isError = true;
												}
											} else {
												dmmRepositoryDTO.setBrand("");
												dmmRepositoryDTO.setOldBrand(repositoryDTO.getBrand());
											}
										}
										//核檢設備型號
										if (isModel) {
											//獲取設備型號
											model = this.getCellFormatValue(row.getCell(14), null, Boolean.TRUE);
											if (StringUtils.hasText(model)) {
												String models = this.assetTypeDAO.getAssetInfo(assetTypeId, AssetTypeDTO.ATTRIBUTE.MODEL.getValue());
												String tempModel = null;
												model = model.toLowerCase();
												if (StringUtils.hasText(models)) {
													List<String> tempModels = StringUtils.toList(models, IAtomsConstants.MARK_SEPARATOR);
													for (String temp : tempModels) {
														if (model.equals(temp.toLowerCase())) {
															tempModel = temp;
															break;
														}
													}
													if (StringUtils.hasText(tempModel)) {
														dmmRepositoryDTO.setModel(tempModel);
														dmmRepositoryDTO.setOldModel(repositoryDTO.getAssetModel());
													} else {
														tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
																+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_MODEL)}, null));
														isError = true;
													}
												} else {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_TYPE_MODEL)}, null));
													isError = true;
												}
											} else {
												dmmRepositoryDTO.setModel("");
												dmmRepositoryDTO.setOldModel(repositoryDTO.getAssetModel());
											}
										}
										//Task #3393 異動欄位-新增維護模式
										//核檢維護模式
										if (isMaintenanceMode) {
											//獲取維護模式
											maintenanceMode = this.getCellFormatValue(row.getCell(15), null, Boolean.TRUE);
											Boolean hasMaintenanceMode = false;
											String maintenanceModeCode = null;
											//獲取OldMaintenanceMode
											String oldMaintenanceMode = null;
											if(StringUtils.hasText(repositoryDTO.getMaType())){
												for (Parameter temp : maintenanceModeList) {
													if ((repositoryDTO.getMaType()).equals(temp.getValue())) {
														oldMaintenanceMode = (String) temp.getName();
														break;
													}
												}
											}
											if (StringUtils.hasText(maintenanceMode)) {
												maintenanceMode = maintenanceMode.toLowerCase();
												for (Parameter temp : maintenanceModeList) {
													if (maintenanceMode.equals(temp.getName())) {
														hasMaintenanceMode = true;
														maintenanceModeCode = (String) temp.getValue();
														break;
													}
												}
												if(hasMaintenanceMode){
													dmmRepositoryDTO.setMaintenanceMode(maintenanceMode);
													dmmRepositoryDTO.setOldMaintenanceMode(oldMaintenanceMode);
													dmmRepositoryDTO.setMaintenanceModeCode(maintenanceModeCode);
												} else {
													tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
															+ i18NUtil.getName(IAtomsMessageCode.PARAM_NOT_EXISTS, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_MA_TYPE)}, null));
													isError = true;
												}
											} else {
												tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
														+ i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_INFO_MA_TYPE)}, null));
												isError = true;
											}
										}
										if (!isError) {
											dmmRepositoryDTO.setSerialNumber(serialNumber);
											if (isProperIdExist) {
												dmmRepositoryDTO.setPropertyId(propertyId);
												dmmRepositoryDTO.setOldPropertyId(repositoryDTO.getPropertyId());
											}
											if (!isAssetNameExist) {
												dmmRepositoryDTO.setAssetTypeName(repositoryDTO.getName());
											}
											if (!isContractCodeExist && isCyberApproveDateExist){
												dmmRepositoryDTO.setOldContractCode(repositoryDTO.getContractId());
											}
											dmmRepositoryDTO.setAssetId(repositoryDTO.getAssetId());
											dmmRepositoryDTO.setAssetInId(repositoryDTO.getAssetInId());
											dmmRepositoryDTO.setMessage(PropertyNumberImportFormDTO.SUCCESS_EXAMINE);
											repositoryDTOs.add(dmmRepositoryDTO);
										}
									}
								} else {
									tempErrorMsg.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
											+ i18NUtil.getName(IAtomsMessageCode.NO_UPDATE_PERMISSIONS));
								}
							}
						}
					}
					if (!CollectionUtils.isEmpty(tempErrorMsg)) {
						errorMsgs.add("");
						errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ASSET_UPDATE_ERROR_MESSAGE));
						for (String errorMsg : tempErrorMsg) {
							errorMsgs.add(errorMsg);
						}
					}
				}
				//核檢台新租認維護租賃維護頁籤
				if (isTaixinMaintenance && message == null) {
					message = this.checkTaixinAndJdwMaintenance(workbook.getSheetAt(1), repositoryDTOTaixins, errorMsgs, Boolean.TRUE, logonUser, updateTableName, monthYear);
				}
				//核檢捷達威維護維護頁籤
				if (isJdwMaintenance && message == null) {
					message = this.checkTaixinAndJdwMaintenance(workbook.getSheetAt(2), repositoryDTOJdws, errorMsgs, Boolean.FALSE, logonUser, updateTableName, monthYear);
				}
			}
		} catch (Exception e) {
			LOGGER.error(".checkUploadFiled()---->", e);
			throw new ServiceException(".checkUploadFiled() is error!", e);
		}
		return message;
	}

	/**
	 * Purpose:通过name值得到value值，若得不到給出錯誤信息
	 * @author CrissZhang
	 * @param parameterList:下拉框列表
	 * @param name ：name值
	 * @return String
	 */
	private String getValueByName(List<Parameter> parameterList, String name){
		String value = null;
		// 错误信息
		for (Parameter param : parameterList){
			if((param.getName()).equals(name)){
				value = (String) param.getValue();
				break;
			}
		}
		return value;
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
				        if (!StringUtils.hasText(this.getCellFormatValue(tempRow.getCell(j), null, Boolean.FALSE))) {  
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
		        if (!StringUtils.hasText(this.getCellFormatValue(row.getCell(i), null, Boolean.FALSE))) {  
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
	 * Purpose:依據Cell類型獲取數據
	 * @param cell:取得Excel cell單元值
	 * @param getTime 日期格式
	 * @return String:单元格数据内容
	 */
	private String getCellFormatValue(Cell cell, String getTime, Boolean isString) {
	    String cellvalue = "";
	    if (cell != null) {
	    	if (isString) {
	    		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    	}
	        // 判断当前Cell的Type
	        switch (cell.getCellType()) {
	            // 如果当前Cell的Type为NUMERIC
	            case Cell.CELL_TYPE_NUMERIC:
	            case Cell.CELL_TYPE_FORMULA: {
	                // 判断当前的cell是否为Date
	                if (DateUtil.isCellDateFormatted(cell)) {
	                	SimpleDateFormat sdf = null;
	                	Date date = cell.getDateCellValue();
	                	if (StringUtils.hasText(getTime)) {
	                		sdf = new SimpleDateFormat(getTime);
	                	} else {
	                		sdf = new SimpleDateFormat("yyyy-MM-dd");
	                	}
	                    cellvalue = sdf.format(date);
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
	    return cellvalue.trim();
	}
	/**
	 * Purpose: 核檢台新租認維護和捷達威維護數據
	 * @author CarrieDuan
	 * @param sheet：頁籤
	 * @param errorMsgs：錯誤信息集合
	 * @param isTaiXin：是否核檢台新租認維護頁籤
	 * @throws ServiceException
	 * @return Message
	 */
	public Message checkTaixinAndJdwMaintenance(Sheet sheet, List<DmmRepositoryDTO> dmmRepositoryDTOs, List<String> errorMsgs, boolean isTaiXin, IAtomsLogonUser logonUser,
			String updateTableName, String monthYear) throws ServiceException{
		Message message = null;
		try {
			List<String> tempErrorMsgs = new ArrayList<String>();
			int rowCount = this.getExcelRealRowCount(sheet);
			if (rowCount <= 1) {
				errorMsgs.add("");
				if (isTaiXin) {
					errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.TAI_XIN_ERROR_MESSAGE));
				} else {
					errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.JDW_ERROR_MESSAGE));
				}
				errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.IATOMS_MSG_NONE_DATA));
			} else if (rowCount > 501) {
				message = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.ABOUCHEMENT_FAILURE_FOR_OVER_LIMIT, new String[]{PropertyNumberImportFormDTO.UPLOAD_NUMBER });
			} else {
				DmmRepositoryDTO repositoryDTO = new DmmRepositoryDTO();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd ");
				Row row = null;
				String serialNumber = null;
				String dtid = null;
				String caseId = null;
				String merchantCode = null;
				String merchantHeader = null;
				String maintainCompany = null;
				String enableDate = null;
				String maintainUser = null;
				String caseCompletionDate = null;
				String analyzeDate = null;
				String isBussinessAddress = null;
				String installedAdressLocation = null;
				String installAddress = null;
				String iscup = null;
				DmmRepositoryDTO dmmRepositoryDTO = null;
				//记录使用人下的特店代号
				MerchantDTO merchantDTO = null;
				//记录使用人下的特点表头
				BimMerchantHeaderDTO merchantHeaderDTO = null;
				//使用者账号
				AdmUserDTO admUserDTO = null;
				//获取维护厂商数据
				List<String> params = new ArrayList<String>();
				params.add(IAtomsConstants.PARAM_COMPANY_TYPE_VENDOR);
				//獲取公司列表--update by 2017-05-16
				List<Parameter> companys = this.companyDAO.getCompanyList(params, null, false, null);
				//獲取縣市下拉列表
				List<Parameter> locations = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.LOCATION.getCode(), null);
				//記錄設備序號重複的行號
				Map<String, Integer> serialNumberRepeatMap = new HashMap<String, Integer>();
				//是否错误
				boolean isError = Boolean.FALSE;
				//记录临时变量
				String temp = null;
				for (int i = 1; i <= rowCount; i++) {
					row = sheet.getRow(i);
					if (isRowEmpty(row).booleanValue()) {
						continue;
					} 
					//获取设备編號
					serialNumber = this.getCellFormatValue(row.getCell(0), null, Boolean.TRUE);
					//判斷設備序號是否為空
					if(!StringUtils.hasText(serialNumber)) {
						tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
								.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_ASSET_IN_LIST_SERIAL_NUMBER)}, null)) );
					} else {
						if (IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY.equals(updateTableName)) {
							repositoryDTO = this.monthlyDAO.getRepositoryBySerialNumber(serialNumber,monthYear, null);
						} else {
							repositoryDTO = this.repositoryDAO.getRepositoryBySerialNumber(serialNumber, null, null);
						}
						if (repositoryDTO == null) {
							tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i + 1))}, null) 
											.concat(PropertyNumberImportFormDTO.NOT_ASSET_INFO));
						} else {
							//判斷是有具有控管權限
							if (checkDataAcl(serialNumber, logonUser)) {
								if (!IAtomsConstants.PARAM_ASSET_STATUS_IN_USE.equals(repositoryDTO.getStatus())
										&& !IAtomsConstants.PARAM_ASSET_STATUS_IN_APPLY.equals(repositoryDTO.getStatus())) {
									if (isTaiXin) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsConstants.ASSET_STATUS_CANNOT_REPAIR));
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsConstants.ASSET_STATUS_CANNOT_USE_AND_APPLY));
									}
									continue;
								}
								if (isTaiXin) {
									if (!IAtomsConstants.PARAM_TSB_EDC.equals(repositoryDTO.getCompanyCode())) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsConstants.ASSET_CANNOT_TSB));
										continue;
									}
								} else {
									if (!IAtomsConstants.PARAMTER_COMPANY_CODE_JDW_EDC.equals(repositoryDTO.getCompanyCode())) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsConstants.ASSET_CANNOT_JDW));
										continue;
									}
								}
								dmmRepositoryDTO = new DmmRepositoryDTO();
								//驗證設備序號是否檔內重複
								if (serialNumberRepeatMap.containsKey(serialNumber)) {
									tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_AND_ROW, new String[]{String.valueOf((i+1)), serialNumberRepeatMap.get(serialNumber).toString()}, null) + 
											i18NUtil.getName(IAtomsMessageCode.SERIAL_NUMBER_REPETITION));
									continue;
								} else {
									serialNumberRepeatMap.put(serialNumber, i+1);
								}
								dmmRepositoryDTO.setSerialNumber(serialNumber);
								//核檢dtid
								dtid = this.getCellFormatValue(row.getCell(1), null, Boolean.TRUE);
								if (StringUtils.hasText(dtid)) {
									//核检DTID长度
									if(dtid.trim().length() > Integer.valueOf(IAtomsConstants.LEAVE_CASE_STATUS_EIGHT)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID), IAtomsConstants.LEAVE_CASE_STATUS_EIGHT}, null));
										isError = Boolean.TRUE;
									}
									//核檢DTID格式
									if (!ValidateUtils.numberOrEnglish(dtid)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf((i + 1))}, null) 
												+ i18NUtil.getName(IAtomsMessageCode.NUMBER_OR_ENGLISH, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID)}, null));
										isError = Boolean.TRUE;
									}
									if (!isError) {
										dmmRepositoryDTO.setDtid(dtid);
										dmmRepositoryDTO.setOldDtid(repositoryDTO.getDtid());
									} else {
										isError = Boolean.FALSE;
									}
								} else {
									tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
											.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_DTID)}, null)) );
								}
								//核檢案件編號
								caseId = this.getCellFormatValue(row.getCell(2), null, Boolean.TRUE);
								if (StringUtils.hasText(caseId)) {
									//核检案件編號长度
									if(caseId.trim().length() > Integer.valueOf(IAtomsConstants.LEAVE_CASE_STATUS_FIFTEEN)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CASE_ID), IAtomsConstants.LEAVE_CASE_STATUS_FIFTEEN}, null));
									} else {
										dmmRepositoryDTO.setCaseId(caseId);
										dmmRepositoryDTO.setOldCaseId(repositoryDTO.getCaseId());
									}
								} else {
									tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
											.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CASE_ID)}, null)) );
								}
								//核檢特店代號
								merchantCode = this.getCellFormatValue(row.getCell(3), null, Boolean.TRUE);
								if (StringUtils.hasText(merchantCode)) {
									if (isTaiXin) {
										merchantDTO = this.merchantDAO.getMerchantInfo(null, merchantCode, null, IAtomsConstants.PARAM_TSB_EDC);
									} else {
										merchantDTO = this.merchantDAO.getMerchantInfo(null, merchantCode, repositoryDTO.getAssetUser(), null);;
									}
									if (merchantDTO != null) {
										dmmRepositoryDTO.setMerchantId(merchantDTO.getMerchantId());
										dmmRepositoryDTO.setMerchantCode(merchantCode);
										dmmRepositoryDTO.setOldMerchantCode(repositoryDTO.getMerchantCode());
									} else {
										if (isTaiXin) {
											tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
													+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MATCHING, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE), merchantCode, i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_IASSET_USER), i18NUtil.getName(IAtomsConstants.PARAM_TSB_EDC)}, null));
										} else {
											tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
													+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MATCHING, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE), merchantCode, i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_IASSET_USER), repositoryDTO.getAssetUserName()}, null));
										}
										
									}
								} else {
									tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
											.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE)}, null)) );
								}
								//核檢特點表頭
								merchantHeader = this.getCellFormatValue(row.getCell(4), null, Boolean.TRUE);
								if (StringUtils.hasText(merchantHeader) && StringUtils.hasText(dmmRepositoryDTO.getMerchantId())) {
									merchantHeaderDTO = this.merchantHeaderDAO.getMerchantHeaderDTOBy(dmmRepositoryDTO.getMerchantId(), merchantHeader, null);
									if (merchantHeaderDTO != null) {
										dmmRepositoryDTO.setMerchantHeaderId(merchantHeaderDTO.getMerchantHeaderId());
										dmmRepositoryDTO.setHeaderName(merchantHeaderDTO.getHeaderName());
										dmmRepositoryDTO.setOldMerchantHeaderId(repositoryDTO.getHeaderName());
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MATCHING, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_NAME), merchantHeader, i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_MERCHANT_CODE), merchantCode}, null));
									}
								} else {
									if (StringUtils.hasText(dmmRepositoryDTO.getMerchantId())) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_BIM_MERCHANT_HEADER_NAME)}, null)) );
									}
								}
								//維護廠商
								maintainCompany = this.getCellFormatValue(row.getCell(5), null, Boolean.TRUE);
								if (StringUtils.hasText(maintainCompany)) {
									String maintainCompanyId = this.getValueByName(companys, maintainCompany);
									if (StringUtils.hasText(maintainCompanyId)) {
										dmmRepositoryDTO.setMaintainCompany(maintainCompanyId);
										dmmRepositoryDTO.setMaintainCompanyName(maintainCompany);
										dmmRepositoryDTO.setOldMaintainCompany(repositoryDTO.getMaintainCompanyName());
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_ERROR, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_MAINTAIN_COMPANY)}, null));
									}
								} else {
									tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
											.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_MAINTAIN_COMPANY)}, null)) );
								}
								//設備啟用日
								enableDate = this.getCellFormatValue(row.getCell(6), "yyyy/MM/dd", Boolean.FALSE);
								if (StringUtils.hasText(enableDate)) {
									//核檢日期格式是否符合YYYY/MM/DD
									if (!ValidateUtils.checkDate(enableDate)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_ENABLE_DATE)}, null));
									} else {
										dmmRepositoryDTO.setEnableDate(DateTimeUtils.toTimestamp(enableDate));
										if (repositoryDTO.getEnableDate() != null) {
											String oldEnableDate = formatter.format(repositoryDTO.getEnableDate());
											dmmRepositoryDTO.setOldEnableDate(DateTimeUtils.toTimestamp(oldEnableDate));
										}
										//dmmRepositoryDTO.setOldEnableDate(repositoryDTO.getEnableDate());
									}
								}
								//維護工程師
								maintainUser = this.getCellFormatValue(row.getCell(7), null, Boolean.TRUE);
								if (StringUtils.hasText(maintainUser) && StringUtils.hasText(dmmRepositoryDTO.getMaintainCompany())) {
									admUserDTO = this.userDAO.getUserDTOByCompanyIdAndAccount(dmmRepositoryDTO.getMaintainCompany(), maintainUser, null);
									if (admUserDTO != null) {
										dmmRepositoryDTO.setMaintainUser(admUserDTO.getUserId());
										StringBuffer tempBuffer = new StringBuffer();
										tempBuffer.append(admUserDTO.getCname()).append(IAtomsConstants.MARK_BRACKETS_LEFT)
											.append(admUserDTO.getEname()).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
										dmmRepositoryDTO.setMaintainUserName(tempBuffer.toString());
										tempBuffer.delete(0, tempBuffer.length());
										if (StringUtils.hasText(repositoryDTO.getMaintainUserName()) && StringUtils.hasText(repositoryDTO.getEname())) {
											tempBuffer.append(repositoryDTO.getMaintainUserName()).append(IAtomsConstants.MARK_BRACKETS_LEFT)
											.append(repositoryDTO.getEname()).append(IAtomsConstants.MARK_BRACKETS_RIGHT);
											dmmRepositoryDTO.setOldMaintainUser(tempBuffer.toString());
										}
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_MATCHING, 
														new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_MAINTAIN_USER), maintainUser, i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_MAINTAIN_COMPANY), maintainCompany}, null));
									}
								}
								//完修日期
								caseCompletionDate = this.getCellFormatValue(row.getCell(8), "yyyy/MM/dd", Boolean.FALSE);
								if (StringUtils.hasText(caseCompletionDate)) {
									//核檢日期格式是否符合YYYY/MM/DD
									if (!ValidateUtils.checkDate(caseCompletionDate)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_CASE_COMPLETION_DATE)}, null));
									} else {
										dmmRepositoryDTO.setCaseCompletionDate(DateTimeUtils.toTimestamp(caseCompletionDate));
										if (repositoryDTO.getCaseCompletionDate() != null) {
											String oldCaseCompletionDate = formatter.format(repositoryDTO.getCaseCompletionDate());
											dmmRepositoryDTO.setOldCaseCompletionDate(DateTimeUtils.toTimestamp(oldCaseCompletionDate));
										}
									}
								}
								//签收日期
								analyzeDate = this.getCellFormatValue(row.getCell(9), "yyyy/MM/dd", Boolean.FALSE);
								if (StringUtils.hasText(analyzeDate)) {
									//核檢日期格式是否符合YYYY/MM/DD
									if (!ValidateUtils.checkDate(analyzeDate)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_NOT_DATE_FORMAT_YYYY_MM_DD, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_ANALYZE_DATE)}, null));
									} else {
										dmmRepositoryDTO.setAnalyzeDate(DateTimeUtils.toTimestamp(analyzeDate));
										if (repositoryDTO.getAnalyzeDate() != null) {
											String oldAnalyzeDate = formatter.format(repositoryDTO.getAnalyzeDate());
											dmmRepositoryDTO.setOldAnalyzeDate(DateTimeUtils.toTimestamp(oldAnalyzeDate));
										}
										//dmmRepositoryDTO.setOldAnalyzeDate(repositoryDTO.getAnalyzeDate());
									}
								}
								//裝機地址-同營業地址
								isBussinessAddress = this.getCellFormatValue(row.getCell(10), null, Boolean.TRUE);
								if (StringUtils.hasText(isBussinessAddress) && StringUtils.hasText(dmmRepositoryDTO.getMerchantHeaderId())) {
									if (i18NUtil.getName(IAtomsConstants.NO).equals(isBussinessAddress)) {
										dmmRepositoryDTO.setIsBussinessAddress(IAtomsConstants.NO);
										//選擇是，則帶入特點信息
									} else if (i18NUtil.getName(IAtomsConstants.YES).equals(isBussinessAddress)){
										dmmRepositoryDTO.setIsBussinessAddress(IAtomsConstants.YES);
										dmmRepositoryDTO.setInstalledAdressLocation(merchantHeaderDTO.getLocation());
										dmmRepositoryDTO.setInstalledAdressLocationName(merchantHeaderDTO.getMerchantLocationName());
										dmmRepositoryDTO.setInstalledAdress(merchantHeaderDTO.getBusinessAddress());
										dmmRepositoryDTO.setOldInstalledAdressLocation(repositoryDTO.getInstalledAdressLocationName());
										dmmRepositoryDTO.setOldInstalledAdress(repositoryDTO.getInstallAddress());
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_YES_OR_NO, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_HANDLE_IS_BUSSINESS_ADDRESS)}, null));
									}
								} else {
									if (StringUtils.hasText(dmmRepositoryDTO.getMerchantHeaderId())) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_SRM_HANDLE_IS_BUSSINESS_ADDRESS)}, null)) );
									}
								}
								//裝機地址-縣市
								installedAdressLocation = this.getCellFormatValue(row.getCell(11), null, Boolean.TRUE);
								if (StringUtils.hasText(installedAdressLocation) && i18NUtil.getName(IAtomsConstants.NO).equals(isBussinessAddress)) {
									temp = this.getValueByName(locations, installedAdressLocation);
									if (StringUtils.hasText(temp)) {
										dmmRepositoryDTO.setInstalledAdressLocation(temp);
										dmmRepositoryDTO.setInstalledAdressLocationName(installedAdressLocation);
										dmmRepositoryDTO.setOldInstalledAdressLocation(repositoryDTO.getInstalledAdressLocationName());
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_ERROR, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS_LOCATION)}, null));
									}
								} else {
									if (StringUtils.hasText(dmmRepositoryDTO.getMerchantHeaderId()) && i18NUtil.getName(IAtomsConstants.NO).equals(isBussinessAddress)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS_LOCATION)}, null)) );
									}
								}
								//装机地址
								installAddress = this.getCellFormatValue(row.getCell(12), null, Boolean.TRUE);
								if (StringUtils.hasText(installAddress) && i18NUtil.getName(IAtomsConstants.NO).equals(isBussinessAddress)) {
									if (installAddress.trim().length() > Integer.valueOf(IAtomsConstants.CONTACT_ADDRESS_LENGTH)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.PARAM_LENGTH_IS_INVALID, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS), IAtomsConstants.CONTACT_ADDRESS_LENGTH}, null));
									} else {
										dmmRepositoryDTO.setInstalledAdress(installAddress);
										dmmRepositoryDTO.setOldInstalledAdress(repositoryDTO.getInstallAddress());
									}
								} else {
									if (StringUtils.hasText(dmmRepositoryDTO.getMerchantHeaderId()) && i18NUtil.getName(IAtomsConstants.NO).equals(isBussinessAddress)) {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												.concat(i18NUtil.getName(IAtomsMessageCode.INPUT_BLANK, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_INSTALLED_ADRESS)}, null)) );
									}
								}
								//核检银联
								iscup = this.getCellFormatValue(row.getCell(13), null, Boolean.TRUE);
								if (StringUtils.hasText(iscup)) {
									if (i18NUtil.getName(IAtomsConstants.NO).equals(iscup)) {
										dmmRepositoryDTO.setIsCup(IAtomsConstants.NO);
										dmmRepositoryDTO.setOldIsCup(repositoryDTO.getIsCup());
										//選擇是，則帶入特點信息
									} else if (i18NUtil.getName(IAtomsConstants.YES).equals(iscup)){
										dmmRepositoryDTO.setIsCup(IAtomsConstants.YES);
										dmmRepositoryDTO.setOldIsCup(repositoryDTO.getIsCup());
									} else {
										tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
												+ i18NUtil.getName(IAtomsMessageCode.INPUT_YES_OR_NO, new String[]{i18NUtil.getName(IAtomsConstants.FIELD_DMM_REPOSITORY_IS_CUP)}, null));
									}
								} else {
									dmmRepositoryDTO.setIsCup(IAtomsConstants.NO);
									dmmRepositoryDTO.setOldIsCup(repositoryDTO.getIsCup());
								}
								if (CollectionUtils.isEmpty(tempErrorMsgs)) {
									dmmRepositoryDTO.setStatus(repositoryDTO.getStatus());
									dmmRepositoryDTO.setIsEnabled(repositoryDTO.getIsEnabled());
									dmmRepositoryDTO.setAssetId(repositoryDTO.getAssetId());
									dmmRepositoryDTO.setUpdateTable(updateTableName);
									dmmRepositoryDTO.setMonthYear(monthYear);
									dmmRepositoryDTOs.add(dmmRepositoryDTO);
								}
							} else {
								tempErrorMsgs.add(i18NUtil.getName(IAtomsMessageCode.ROW_INDEX, new String[]{String.valueOf(i + 1)}, null)
										+ i18NUtil.getName(IAtomsMessageCode.NO_UPDATE_PERMISSIONS));
							}
						}
					}
				}
				if (!CollectionUtils.isEmpty(tempErrorMsgs)) {
					errorMsgs.add("");
					if (isTaiXin) {
						errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.TAI_XIN_ERROR_MESSAGE));
					} else {
						errorMsgs.add(i18NUtil.getName(IAtomsMessageCode.JDW_ERROR_MESSAGE));
					}
					for (String errorMsg : tempErrorMsgs) {
						errorMsgs.add(errorMsg);
					}
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(".checkTaixinAndJdwMaintenance() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".checkTaixinAndJdwMaintenance(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()}, e);
		}
		return message;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IPropertyImportService#upload(cafe.core.context.SessionContext)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SessionContext upload(SessionContext sessionContext)throws ServiceException {		
		try {
			Message msg = null;
			Object[] obj = null;
			List<DmmRepositoryDTO> repositoryDTOs = null;
			List<DmmRepositoryDTO> repositoryDTOTaiXins = null;
			List<DmmRepositoryDTO> repositoryDTOJdws = null;
			PropertyNumberImportFormDTO formDTO = (PropertyNumberImportFormDTO) sessionContext.getRequestParameter();
			String fileName = formDTO.getFileName();
			IAtomsLogonUser logonUser = (IAtomsLogonUser) formDTO.getLogonUser();
			Map<String, List<DmmRepositoryDTO>> map = this.getFileInfo(fileName);
			if (map.size() != 0) {
				repositoryDTOs = map.get("repositoryDTOs");
				repositoryDTOTaiXins = map.get("taixin");
				repositoryDTOJdws = map.get("jdw");
			}
			//獲取異動欄位
			Map<String, String> updateColumns = formDTO.getAssetMap();
			Boolean isCounter = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.COUNTER.getValue()) != null;
			Boolean isCartonNo = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.CARTON_NO.getValue()) != null;
			Boolean isEnableDate = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.ENABLE_DATE.getValue()) != null;
			Boolean isTaixinMaintenance = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.TAI_XIN_MAINTENANCE.getValue()) != null;
			Boolean isJdwMaintenance = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.JDW_MAINTENANCE.getValue()) != null;
			
			Boolean isBrand = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.BRAND.getValue()) != null;
			Boolean isModel = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.MODEL.getValue()) != null;
			Boolean isMaintenanceMode = updateColumns.get(DmmRepositoryDTO.ATTRIBUTE.MAINTENANCE_MODE.getValue()) != null;
			List<String> repositoryIds = new ArrayList<String>();
			List<String> contractIds = new ArrayList<String>();
			List<String> assetIds = new ArrayList<String>();
			List<BimContractDTO> contractDTOs = null;
			List<DmmRepositoryDTO> dmmRepositoryDTOs = null;
			String historyId = this.generateGeneralUUID(IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY);
			Integer customerWarranty = null;
			Integer factoryWarranty = null;
			Date factoryWarrantyDate = null;
			Date customerWarrantyDate = null;
			Date cyberApprovedDate = null;
			HibernateTransactionManager transactionManager = null;
			TransactionStatus status = null;
			transactionManager = (HibernateTransactionManager) this.applicationContext.getBean("transactionManager");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
			status = transactionManager.getTransaction(def); // 获得事务状态
			try {
				if(!CollectionUtils.isEmpty(repositoryDTOs)) {
					for (DmmRepositoryDTO repositoryDTO : repositoryDTOs) {
						//如果異動了合約編號，則取出最新合約編號與轉倉批號
						if (StringUtils.hasText(repositoryDTO.getContractId())) {
							contractIds.add(repositoryDTO.getContractId());
							assetIds.add(repositoryDTO.getAssetId());
						} else {
							//如果沒有異動合約編號，異動cyber驗收日期，則收集合約編號
							if (repositoryDTO.getCyberApprovedDate() != null) {
								contractIds.add(repositoryDTO.getOldContractCode());
							}
						}
					}
					if (!CollectionUtils.isEmpty(contractIds)) {
						contractDTOs = this.contractDAO.listContractDTOByIds(contractIds);
					}
					if (!CollectionUtils.isEmpty(assetIds)) {
						dmmRepositoryDTOs = this.repositoryDAO.listByAssetId(assetIds);
					}
					DmmRepositoryDTO dmmRepositoryDTO = null;
					BimContractDTO bimContractDTO = null;
					for (DmmRepositoryDTO repositoryDTO : repositoryDTOs) {
						//如果異動了合約編號或者cyber驗收日期
						if (StringUtils.hasText(repositoryDTO.getContractId()) || repositoryDTO.getCyberApprovedDate() != null) {
							//獲取該筆資料對應的合約信息
							if (StringUtils.hasText(repositoryDTO.getContractId())) {
								bimContractDTO = this.getBimContractDTOById(contractDTOs, repositoryDTO.getContractId());
							} else {
								bimContractDTO = this.getBimContractDTOById(contractDTOs, repositoryDTO.getOldContractCode());
							}
							//獲取原廠保固期限
							factoryWarranty = bimContractDTO.getFactoryWarranty();
							//如果異動了合約編號，則需異動原廠保固期與客戶保固期
							if (StringUtils.hasText(repositoryDTO.getContractId())) {
								//獲取對應的設備信息
								dmmRepositoryDTO = this.getAssetInInfoDTOById(dmmRepositoryDTOs, repositoryDTO.getAssetId());
								//獲取客戶保固期限
								customerWarranty = bimContractDTO.getCustomerWarranty();
								//獲取cyber驗收日期
								factoryWarrantyDate = repositoryDTO.getOldCyberApprovedDate();
								//獲取客戶驗收日期
								customerWarrantyDate = dmmRepositoryDTO.getCheckedDate();
								if (!dmmRepositoryDTO.getMaType().equals(IAtomsConstants.MA_TYPE_LEASE)) {
									if (customerWarranty == null) {
										customerWarranty = 0;
									}
									if (customerWarrantyDate != null) {
										customerWarrantyDate.setMonth(customerWarrantyDate.getMonth() + customerWarranty);
										repositoryDTO.setCustomerWarrantyDate(customerWarrantyDate);
									}
								}
							}
							//如果異動了cyber驗收日期，則根據最新的cyber驗收日期計算客戶保固期限。
							if (repositoryDTO.getCyberApprovedDate() != null) {
								factoryWarrantyDate = repositoryDTO.getCyberApprovedDate();
								cyberApprovedDate = (Date) repositoryDTO.getCyberApprovedDate().clone();
							}
							if (factoryWarranty == null) {
								factoryWarranty = 0;
							}
							factoryWarrantyDate.setMonth(factoryWarrantyDate.getMonth() + factoryWarranty);
							repositoryDTO.setFactoryWarrantyDate(factoryWarrantyDate);
							repositoryDTO.setCyberApprovedDate(cyberApprovedDate);
						}
						repositoryDTO.setUpdateDate(DateTimeUtils.getCurrentTimestamp());
						repositoryDTO.setUpdateUser(logonUser.getId());
						repositoryDTO.setUpdateUserName(logonUser.getName());
						this.repositoryDAO.updateRepository(repositoryDTO, isCounter, isCartonNo, isEnableDate, isBrand, isModel, isMaintenanceMode);
						if (!IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY_HISTORY_MONTHLY.equals(repositoryDTO.getUpdateTable())) {
							repositoryIds.add(repositoryDTO.getAssetId());
						}
					}
					if (repositoryIds.size() > 0) {
						this.repositoryHistoryDAO.insertRepositoryHistory(repositoryIds, historyId);
					}
				}		
				int index = repositoryIds.size() + 1;
				String tempId = historyId;
				if (isTaixinMaintenance && msg == null) {
					if (!CollectionUtils.isEmpty(repositoryDTOTaiXins)) {
						for (DmmRepositoryDTO dto : repositoryDTOTaiXins) {
							historyId = tempId.concat(IAtomsConstants.MARK_UNDER_LINE).concat(Integer.toString(index++));
							obj = this.repositoryDAO.repositoryAssetManageTSBAndJdw(dto.getAssetId(), DateTimeUtils.getCurrentTimestamp(), logonUser.getId(),
								logonUser.getName(), null, historyId, IAtomsConstants.PARAM_ACTION_BATCH_UPDATE_TSB_RENT, dto.getStatus(), dto.getEnableDate(), 
								dto.getIsEnabled(), dto.getDtid(), dto.getCaseId(), dto.getMerchantId(), dto.getMerchantHeaderId(), 
								dto.getInstalledAdress(), dto.getInstalledAdressLocation(), dto.getIsCup(), dto.getMaintainCompany(), 
								dto.getAnalyzeDate(), dto.getCaseCompletionDate(), dto.getMaintainUser(), IAtomsConstants.NO,
								StringUtils.hasText(dto.getUpdateTable())?IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY.equals(dto.getUpdateTable())?"1":"u":"",
										dto.getMonthYear());
							if (obj[0] != null && obj[1] != null) {
								if ("assetStatusCannotRepair".equals(obj[0])) {
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.TAI_XIN_STATUS_ERROR, new String[]{dto.getSerialNumber()});
								} else if ("assetCannotTSB".equals(obj[0])) {
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.TAI_XIN_ASSET_USER_ERROR, new String[]{dto.getSerialNumber()});
								}
								break;
							}
						}
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()});
					}
				}
				if (isJdwMaintenance && msg == null) {
					if (!CollectionUtils.isEmpty(repositoryDTOJdws)) {
						for (DmmRepositoryDTO dto : repositoryDTOJdws) {
							historyId = tempId.concat(IAtomsConstants.MARK_MIDDLE_LINE).concat(Integer.toString(index++));
							obj = this.repositoryDAO.repositoryAssetManageTSBAndJdw(dto.getAssetId(), DateTimeUtils.getCurrentTimestamp(), logonUser.getId(),
								logonUser.getName(), null, historyId, IAtomsConstants.PARAM_ACTION_BATCH_UPDATE_JDW_MAINTENANCE, dto.getStatus(), dto.getEnableDate(), 
								dto.getIsEnabled(), dto.getDtid(), dto.getCaseId(), dto.getMerchantId(), dto.getMerchantHeaderId(), 
								dto.getInstalledAdress(), dto.getInstalledAdressLocation(), dto.getIsCup(), dto.getMaintainCompany(), 
								dto.getAnalyzeDate(), dto.getCaseCompletionDate(), dto.getMaintainUser(), IAtomsConstants.YES,
								StringUtils.hasText(dto.getUpdateTable())?IAtomsConstants.IATOMS_TB_NAME_DMM_REPOSITORY.equals(dto.getUpdateTable())?"1":"2":"",
										dto.getMonthYear());
							if (obj[0] != null && obj[1] != null) {
								if ("assetStatusCannotUseAndApply".equals(obj[0])) {
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.JDW_STATUS_ERROR, new String[]{dto.getSerialNumber()});
								} else if ("assetCannotJDW".equals(obj[0])) {
									msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.JDW_ASSET_USER_ERROR, new String[]{dto.getSerialNumber()});
								}
								break;
							}
						}
					} else {
						msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()});
					}
				}
				if (msg == null) {
					transactionManager.commit(status);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPLOAD_SECCUSS_TION, new String[]{this.getMyName()});
				} else {
					transactionManager.rollback(status);
				}
			} catch (Exception e) {
				transactionManager.rollback(status);
				LOGGER.error(".upload() DataAccess Exception:", e);
				throw new ServiceException(IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()}, e);
			}			
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".upload() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".upload(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.UPLOAD_FAILURE_TION, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	
	private Map saveErrorMsg(List<String> errorMsgs) {
		PrintWriter printWriter = null;
		String fileName = null; 
		Map map = new HashMap();
		try {
			if (!CollectionUtils.isEmpty(errorMsgs)) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(i18NUtil.getName(IAtomsMessageCode.PARAM_ERROR_INFORMATION) + IAtomsConstants.MARK_ENTER);
				for (String errorMsg : errorMsgs) {
					stringBuffer.append(errorMsg + IAtomsConstants.MARK_ENTER);
				}
				//文件路徑
				String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
				String errorFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_TEMP_PATH)
						+ File.separator + yearMonthDay + File.separator + IAtomsConstants.UC_NO_DMM_03030 + File.separator + IAtomsConstants.PARAM_STRING_IMPORT;
				fileName = UUID.randomUUID().toString() + IAtomsConstants.MARK_NO + IAtomsConstants.PARAM_FILE_SUFFIX_TXT;
				//錯誤信息文件名
				File filePath = new File(errorFilePath);
				if (!filePath.exists() || !filePath.isDirectory()) {
					filePath.mkdirs();
				}
				File saveFile = new File(filePath, fileName);
				printWriter = new PrintWriter(saveFile, IAtomsConstants.ENCODE_UTF_8);
				printWriter.print(stringBuffer.toString());
				map.put(PropertyNumberImportFormDTO.ERROR_FILE_PATH, errorFilePath);
				map.put(PropertyNumberImportFormDTO.ERROR_FILE_NAME, fileName);
			}
		} catch (Exception e) {
			LOGGER.error("saveEorrorMsg() Error -->", e);
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return  map;
	}
	
	/**
	 * Purpose: 核檢該當前登錄者是否可以異動該筆設備資料
	 * @author CarrieDuan
	 * @param serialNumber :設備序號
	 * @throws ServiceException
	 * @return boolean ：是否可以異動
	 */
	public boolean checkDataAcl(String serialNumber, IAtomsLogonUser logonUser) throws ServiceException{
		try {
			AdmUser user = (AdmUser) this.userDAO.findByPrimaryKey(AdmUser.class, logonUser.getId());
			if (user != null && IAtomsConstants.NO.equals(user.getDataAcl())) {
				return true;
			} else {
				return this.userWarehouseDAO.isUserWarehouse(serialNumber, logonUser.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.error(".checkDataAcl() DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".checkDataAcl(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
	}
	
	private BimContractDTO getBimContractDTOById (List<BimContractDTO> contractDTOs, String contractId) {
		try {
			if (!CollectionUtils.isEmpty(contractDTOs)) {
				for (BimContractDTO bimContractDTO : contractDTOs) {
					if (bimContractDTO.getContractId().equals(contractId)) {
						return bimContractDTO;
					}
				}
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	private DmmRepositoryDTO getAssetInInfoDTOById (List<DmmRepositoryDTO> dmmRepositoryDTOs, String assetId) {
		try {
			if (!CollectionUtils.isEmpty(dmmRepositoryDTOs)) {
				for (DmmRepositoryDTO assetInInfoDTO : dmmRepositoryDTOs) {
					if (assetInInfoDTO.getAssetId().equals(assetId)) {
						return assetInInfoDTO;
					}
				}
			}
		} catch (Exception e) {
			
		}
		return null;
	}

	/**
	 * 獲取當前時間前六個月。格式爲YYYYMM
	 * @return
	 */
	private List<String> getUpdateMonthYear() {
		List<String> monthYears = new ArrayList<String>();
		String monthYear = null;
		for (int i = 1; i < 7; i++) {
			monthYear = DateTimeUtils.toString(DateTimeUtils.addCalendar(DateTimeUtils.getCurrentTimestamp(), 0, -i, 0), DateTimeUtils.DT_FMT_YYYYMM);
			monthYears.add(monthYear);
		}
		return monthYears;
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
	 * @return the companyTypeDAO
	 */
	public ICompanyTypeDAO getCompanyTypeDAO() {
		return companyTypeDAO;
	}

	/**
	 * @param companyTypeDAO the companyTypeDAO to set
	 */
	public void setCompanyTypeDAO(ICompanyTypeDAO companyTypeDAO) {
		this.companyTypeDAO = companyTypeDAO;
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
	 * @return the userDAO
	 */
	public IAdmUserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(IAdmUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @return the userWarehouseDAO
	 */
	public IAdmUserWarehouseDAO getUserWarehouseDAO() {
		return userWarehouseDAO;
	}

	/**
	 * @param userWarehouseDAO the userWarehouseDAO to set
	 */
	public void setUserWarehouseDAO(IAdmUserWarehouseDAO userWarehouseDAO) {
		this.userWarehouseDAO = userWarehouseDAO;
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
	 * @return the assetInInfoDAO
	 */
	public IAssetInInfoDAO getAssetInInfoDAO() {
		return assetInInfoDAO;
	}

	/**
	 * @param assetInInfoDAO the assetInInfoDAO to set
	 */
	public void setAssetInInfoDAO(IAssetInInfoDAO assetInInfoDAO) {
		this.assetInInfoDAO = assetInInfoDAO;
	}

	/**
	 * @return the repositoryHistoryDAO
	 */
	public IDmmRepositoryHistoryDAO getRepositoryHistoryDAO() {
		return repositoryHistoryDAO;
	}

	/**
	 * @param repositoryHistoryDAO the repositoryHistoryDAO to set
	 */
	public void setRepositoryHistoryDAO(
			IDmmRepositoryHistoryDAO repositoryHistoryDAO) {
		this.repositoryHistoryDAO = repositoryHistoryDAO;
	}

	/**
	 * @return the assetInListDAO
	 */
	public IAssetInListDAO getAssetInListDAO() {
		return assetInListDAO;
	}

	/**
	 * @param assetInListDAO the assetInListDAO to set
	 */
	public void setAssetInListDAO(IAssetInListDAO assetInListDAO) {
		this.assetInListDAO = assetInListDAO;
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
	 * @return the merchantHeaderDAO
	 */
	public IMerchantHeaderDAO getMerchantHeaderDAO() {
		return merchantHeaderDAO;
	}

	/**
	 * @param merchantHeaderDAO the merchantHeaderDAO to set
	 */
	public void setMerchantHeaderDAO(IMerchantHeaderDAO merchantHeaderDAO) {
		this.merchantHeaderDAO = merchantHeaderDAO;
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

	/**
	 * @return the monthlyDAO
	 */
	public IDmmRepositoryHistoryMonthlyDAO getMonthlyDAO() {
		return monthlyDAO;
	}

	/**
	 * @param monthlyDAO the monthlyDAO to set
	 */
	public void setMonthlyDAO(IDmmRepositoryHistoryMonthlyDAO monthlyDAO) {
		this.monthlyDAO = monthlyDAO;
	}
	
	
}
