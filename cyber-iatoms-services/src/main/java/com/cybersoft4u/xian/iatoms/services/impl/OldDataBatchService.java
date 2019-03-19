package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationAssetLinkDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.ApplicationDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarDayDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.BimCalendarYearDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.CompanyDTO;
import com.cybersoft4u.xian.iatoms.services.IOldDataBatchService;
import com.cybersoft4u.xian.iatoms.services.dao.IApplicationAssetLinkDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IAssetTypeDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarDayDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICalendarYearDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ICompanyDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IOldDataBatchDAO;
import com.cybersoft4u.xian.iatoms.services.dao.IPvmApplicationDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarDay;
import com.cybersoft4u.xian.iatoms.services.dmo.BimCalendarYear;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplication;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplicationAssetLink;
import com.cybersoft4u.xian.iatoms.services.dmo.PvmApplicationAssetLinkId;

import cafe.core.bean.Parameter;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.StringUtils;
import cafe.core.util.convert.SimpleDtoDmoTransformer;
import cafe.core.util.convert.Transformer;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

/**
 * Purpose: 舊資料批次業務邏輯層實現類
 * @author CrissZhang
 * @since  JDK 1.6
 * @date   2017-7-13
 * @MaintenancePersonnel CrissZhang
 */
public class OldDataBatchService extends AtomicService implements IOldDataBatchService,ApplicationContextAware{

	/**
	 * 注入舊資料轉檔DAO
	 */
	private IOldDataBatchDAO oldDataBatchDAO;
	/**
	 * 注入行事歷年份DAO
	 */
	private ICalendarYearDAO calendarYearDAO;
	/**
	 * 注入行事歷日檔DAO
	 */
	private ICalendarDayDAO calendarDayDAO;
	/**
	 * 公司DAO注入
	 */
	private ICompanyDAO companyDAO;
	/**
	 * 程式版本DAO
	 */
	private IPvmApplicationDAO applicationDAO;
	/**
	 * 程式版本設備鏈接DAO
	 */
	private IApplicationAssetLinkDAO applicationAssetLinkDAO;
	/**
	 * 設備品項DAO
	 */
	private IAssetTypeDAO assetTypeDAO;
	/**
	 * 公司代號集合
	 */
	private List<Parameter> companyCodeList;
	/**
	 * 程式版本集合
	 */
	private Map<String, List<ApplicationDTO>> applicationMap;
	/**
	 * 程式版本維護設備鏈接DTO
	 */
	private List<ApplicationAssetLinkDTO> applicationAssetLinkList;
	
	/**
	 * ApplicationContext對象
	 */
	private ApplicationContext applicationContext;
	
	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, OldDataBatchService.class);
	
	/**
	 * Purpose:批次處理foms行事曆
	 * @author CrissZhang
	 * @throws Exception
	 * @return void
	 */
	public void transferCalendar() throws Exception {
		// 批次
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) this.applicationContext.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
		TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
		try {
			Transformer transformer = new SimpleDtoDmoTransformer();
			// 刪除行事歷資料
			this.calendarDayDAO.deleteTransferData();
			
			// 得到行事歷年份集合
			List<BimCalendarYearDTO> calendarYearDTOs = this.oldDataBatchDAO.listCalendarYear();
			if(!CollectionUtils.isEmpty(calendarYearDTOs)){
				// 行事歷DMO對象
				BimCalendarYear calendarYear = null;
				// 遍歷保存
				for(BimCalendarYearDTO calendarYearDTO : calendarYearDTOs){
					calendarYear = (BimCalendarYear) transformer.transform(calendarYearDTO, new BimCalendarYear());
					// 保存行事歷年檔
					this.calendarYearDAO.insert(calendarYear);
				}
			} else {
				LOGGER.debug(".transferApplicatonTms() batchMessage：foms行事曆年檔無資料。。。。");
			}
			// 得到行事歷年份集合
			List<BimCalendarDayDTO> calendarDayDTOs = this.oldDataBatchDAO.listCalendarDate();
			if(!CollectionUtils.isEmpty(calendarDayDTOs)){
				// 行事歷DMO對象
				BimCalendarDay calendarDay = null;
				// 遍歷保存
				for(BimCalendarDayDTO calendarDayDTO : calendarDayDTOs){
					calendarDay = (BimCalendarDay) transformer.transform(calendarDayDTO, new BimCalendarDay());
					// 保存行事歷日檔
					this.calendarDayDAO.insert(calendarDay);
				}
			} else {
				LOGGER.debug(".transferApplicatonTms() batchMessage：foms行事曆日檔無資料。。。。");
			}
			// 事務提交
			transactionManager.commit(status);
			LOGGER.debug(".transferApplicatonTms() batchMessage：執行行事歷批次成功。。。。");
		} catch (DataAccessException e) {
			LOGGER.error(".transferApplicatonTms() DataAccess Exception:" + e, e);
			LOGGER.error(".transferApplicatonTms() batchMessage：執行行事歷批次錯誤 Exception:" + e, e);
			transactionManager.rollback(status);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferApplicatonTms() Service Exception--->" + e, e);
			LOGGER.error(".transferApplicatonTms() batchMessage：執行行事歷批次錯誤 Exception:" + e, e);
			transactionManager.rollback(status);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
		
	}


	/**
	 * Purpose:轉移TMS程式版本數據
	 * @author CrissZhang
	 * @param sessionContext
	 * @throws ServiceException
	 * @return SessionContext
	 */
	public void transferApplicatonTms() throws ServiceException {
		// 批次
		HibernateTransactionManager transactionManager = (HibernateTransactionManager) this.applicationContext.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
		TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
		try {
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
				}
			} 
			// 事務提交
			transactionManager.commit(status);
			LOGGER.debug(".transferApplicatonTms() batchMessage：執行TMS程式版本批次成功。。。。");
		} catch (DataAccessException e) {
			LOGGER.error(".transferApplicatonTms() DataAccess Exception:" + e, e);
			LOGGER.error(".transferApplicatonTms() batchMessage：執行TMS程式版本批次錯誤 Exception:" + e, e);
			transactionManager.rollback(status);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			LOGGER.error(".transferApplicatonTms() Service Exception--->" + e, e);
			LOGGER.error(".transferApplicatonTms() batchMessage：執行TMS程式版本批次錯誤 Exception:" + e, e);
			transactionManager.rollback(status);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED,e);
		}
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
					}
				} else {
					LOGGER.error(".getApplicationDtoList() batchMessage：未找到程式版本TMS資料或OK文件 file is not exists!!!");
				}
			} else {
				LOGGER.error(".getApplicationDtoList() pathError --> path is not exist!!!");
			}
		} catch (Exception e) {
			LOGGER.error(".getApplicationDtoList()", e.getMessage(), e);
			throw new ServiceException( IAtomsMessageCode.INIT_PAGE_FAILURE, new String[]{this.getMyName()}, e);
		}
		return result;
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
		// 公司代號集合
		if(CollectionUtils.isEmpty(this.companyCodeList)){
			this.companyCodeList = this.companyDAO.getCompanyList(true);
		}
		parameterList = this.companyCodeList;
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
	 * @return the oldDataBatchDAO
	 */
	public IOldDataBatchDAO getOldDataBatchDAO() {
		return oldDataBatchDAO;
	}


	/**
	 * @param oldDataBatchDAO the oldDataBatchDAO to set
	 */
	public void setOldDataBatchDAO(IOldDataBatchDAO oldDataBatchDAO) {
		this.oldDataBatchDAO = oldDataBatchDAO;
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
