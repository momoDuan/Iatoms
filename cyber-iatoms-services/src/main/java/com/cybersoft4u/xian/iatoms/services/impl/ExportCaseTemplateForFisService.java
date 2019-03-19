package com.cybersoft4u.xian.iatoms.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.dao.DataAccessException;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmTransactionParameterDetailDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.CaseManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.common.utils.POIUtils;
import com.cybersoft4u.xian.iatoms.services.IExportCaseTemplateForFisService;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseHandleInfoDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTransactionParameterDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterDetailDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
/**
 * Purpose:批次匯出案件資訊範本(派工給華經資訊)
 * @author nicklin
 * @since  JDK 1.7
 * @date   2018/03/26
 * @MaintenancePersonnel cybersoft
 */
public class ExportCaseTemplateForFisService extends AtomicService implements IExportCaseTemplateForFisService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog logger = CafeLogFactory.getLog(GenericConfigManager.SERVICE, ExportCaseTemplateForFisService.class);
	
	/**
	 * 案件處理資料檔DAO
	 */
	private ISrmCaseHandleInfoDAO srmCaseHandleInfoDAO;
	
	/**
	 * 案件交易參數DAO
	 */
	private ISrmCaseTransactionParameterDAO srmCaseTransactionParameterDAO;
	
	/**
	 * 案件交易參數細項DAO
	 */
	private ISrmTransactionParameterDetailDAO srmTransactionParameterDetailDAO;
	
	/**
	 * Constructor:無參數建構子
	 */
	public ExportCaseTemplateForFisService() {
	}
	
	/** 
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.IExportCaseTemplateForFisService#exportCaseTemplate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void exportCaseTemplate() throws ServiceException {
		// TODO Auto-generated method stub
		try {
			logger.info(this.getClass().getSimpleName() + "。。。iAtoms批次服務-匯出案件資訊範本(派工給華經資訊)開始。。。");
			//從config 讀取模板匯入路徑、案件資訊範本匯出路徑、派工廠商、派工部門
			String importPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.CASE_TEMPLATES_IMPORT_PATH);
			String exportPath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.CASE_TEMPLATES_EXPORT_PATH);
			String dispatchCompanyId = SystemConfigManager.getProperty(IAtomsConstants.DISPATCH_INFORMATION, IAtomsConstants.DISPATCH_COMPANY_ID);
			String dispatchDeptId = SystemConfigManager.getProperty(IAtomsConstants.DISPATCH_INFORMATION, IAtomsConstants.DISPATCH_DEPT_ID);
			List<SrmCaseHandleInfoDTO> caseHandleInfoDTOs = new ArrayList<SrmCaseHandleInfoDTO>();
			//根據派工廠商、派工部門獲得前一天異動案件的案件編號
			List<String> caseIdList = this.srmCaseHandleInfoDAO.getCaseIdListByDispatching(dispatchCompanyId, dispatchDeptId);
			//可編輯字段的map集合List
			List<Map<String,List<String>>> editFieldsMapList = new ArrayList<Map<String,List<String>>>();

			for(String caseId : caseIdList) {
				SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
				List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs = null;
				//srmCaseHandleInfoDTO = this.srmCaseHandleInfoDAO.getCaseInfoById(caseId, null).get(0);
				//取得案件資訊
				CaseManagerFormDTO formDTO = new CaseManagerFormDTO();
				formDTO.setQueryCaseId(caseId);
				formDTO.setIsExport(true);
				formDTO.setIsInstant(true);
				formDTO.setQueryColumnMap(this.getQueryColumnMap());
				srmCaseHandleInfoDTO = this.srmCaseHandleInfoDAO.queryListBy(formDTO).get(0);
				
				String versionDate = DateTimeUtils.toString(srmCaseHandleInfoDTO.getCreatedDate(), DateTimeUtils.DT_FMT_YYYYMMDD_DASH);
				//可編輯字段的map集合
				Map<String,List<String>> editFieldsMap = this.getEditFieldMap(versionDate);
				editFieldsMapList.add(editFieldsMap);
				
				//取得交易參數
				srmCaseTransactionParameterDTOs = this.srmCaseTransactionParameterDAO.listByCaseId(caseId, null);
				//所有類別都要能列印 無交易參數也可列印
				if (CollectionUtils.isEmpty(srmCaseTransactionParameterDTOs)) {
					srmCaseTransactionParameterDTOs.add(new SrmCaseTransactionParameterDTO());
				} else {
					//交易參數，可能有多筆
					for(SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO : srmCaseTransactionParameterDTOs) {
						String itemValue = srmCaseTransactionParameterDTO.getItemValue();
						srmCaseTransactionParameterDTO.setDTID(srmCaseHandleInfoDTO.getDtid());
						Gson gsonss = new GsonBuilder().create();
						//將item_value轉化為map，存在交易參數dto裡面
						Map<String, String> srmCaseTransactionParametermap = (Map<String, String>) gsonss.fromJson(
										itemValue, new TypeToken<Map<String, String>>(){}.getType());
						srmCaseTransactionParameterDTO.setSrmCaseTransactionParametermap(srmCaseTransactionParametermap);
					}
				}
				srmCaseHandleInfoDTO.setCaseTransactionParameterDTOs(srmCaseTransactionParameterDTOs);
				caseHandleInfoDTOs.add(srmCaseHandleInfoDTO);
			}

			//創建案件資訊範本
			String exportFileName = null;
			int index = 0;
			try {
				if (!CollectionUtils.isEmpty(caseHandleInfoDTOs)) {
					for(SrmCaseHandleInfoDTO caseHandleInfoDTO : caseHandleInfoDTOs) {
			            //產生隨機數給臨時文件拼接文件名
						int x = (int)(Math.random() * 10000);
						exportFileName = caseHandleInfoDTO.getCaseId() + IAtomsConstants.MARK_UNDER_LINE + IAtomsConstants.EXPORT_CASE_TEMPLATE;
						//暫存創建案件資訊範本所需的案件資訊集合
						List<SrmCaseHandleInfoDTO> tempList = new ArrayList<SrmCaseHandleInfoDTO>();
						tempList.add(caseHandleInfoDTO);
						//先在存放模板的路徑創建案件資訊範本(英文檔名:XXXXcaseExportTemplate.xls)
						//再將案件資訊範本複製到匯出路徑並改名(中文檔名:caseId_案件資訊範本.xls)
						//最後將英文檔名的案件資訊範本刪除
			            POIUtils pOIUtils = new POIUtils();
			            pOIUtils.createExcel(IAtomsConstants.EXPORT_CASE_TEMPLATE, importPath, tempList, editFieldsMapList.get(index), x);
						FileUtils.copyFile(importPath + x + CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN, exportPath, exportFileName);
						FileUtils.removeFile(importPath + x + CaseManagerFormDTO.CASE_EXPORT_TEMPLATE_NAME_FOR_EN);
						index++;
					}
				}
			} catch (Exception e) {
				logger.error("exportCaseTemplate() ", "Exception:", e);
				throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
			}
			logger.info(this.getClass().getSimpleName() + "。。。iAtoms批次服務-匯出案件資訊範本(派工給華經資訊)完成。。。");
		} catch (DataAccessException e) {
			logger.error("exportCaseTemplate() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			logger.error("exportCaseTemplate() ", "Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
	}
	
	/**
	 * Purpose:獲取交易參數可以編輯的列名
	 * @author evanliu
	 * @param  versionDate:案件進件日期
	 * @throws ServiceException：出錯時抛出出ServiceException
	 * @return Map<String,List<String>>：交易參數分組的可以編輯的列集合。key：交易參數code，value：可以編輯的列名list
	 */
	public Map<String, List<String>> getEditFieldMap(String versionDate) {
		Map<String, List<String>> result = null;
		try {
			List<SrmTransactionParameterDetailDTO> parameterDetailDTOs = this.srmTransactionParameterDetailDAO.listby(null, null, IAtomsConstants.YES, versionDate);
			result = new HashMap<String, List<String>>();
			String prevType = null;
			String currentType = null;
			int length = parameterDetailDTOs.size();
			SrmTransactionParameterDetailDTO srmTransactionParameterDetailDTO = null;
			List<String> editFields = null;
			for (int i = 0; i < length; i++) {
				srmTransactionParameterDetailDTO = parameterDetailDTOs.get(i);
				currentType = srmTransactionParameterDetailDTO.getTransactionType();
				if (i == 0) {
					prevType = currentType;
					editFields = new ArrayList<String>();
				}
				else if (!currentType.equals(prevType)) {
					result.put(prevType, editFields);
					prevType = srmTransactionParameterDetailDTO.getTransactionType();
					editFields = new ArrayList<String>();					
				}
				editFields.add(srmTransactionParameterDetailDTO.getParamterItemCode());
				if (i == length - 1) {
					result.put(prevType, editFields);
				}
			}
		} catch (DataAccessException e) {
			logger.error("getEditFieldMap() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, e);
		} catch (Exception e) {
			logger.error("getEditFieldMap() ", "DataAccess Exception:", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, e);
		}
		return result;
	}
	
	/**
	 * Purpose:得到查詢列集合
	 * @author CrissZhang
	 * @return Map<String,String>
	 */
	private Map<String, String> getQueryColumnMap(){
		// 查詢條件map集合
		Map<String, String> queryColumnMap = new HashMap<String, String>();
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_ID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.REQUIREMENT_NO.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_CATEGORY.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CUSTOMER_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTRACT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALL_TYPE_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.UNINSTALL_TYPE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DTID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.IS_PROJECT.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROJECT_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.REPAIR_REASON_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPANY_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DEPARTMENT_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.SAME_INSTALLED.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.OLD_MERCHANT_CODE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CASE_TYPE_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.MERCHANT_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.HEADER_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.EXPECTED_COMPLETION_DATE.getValue());
		// Task #3081 列印顯示應完修時間
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ACCEPTABLE_FINISH_DATE.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.AREA_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.IS_VIP.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_TEL2.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_MOBILE_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_CONTACT_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_HOURS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.AO_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.AO_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.BUSINESS_ADDRESS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.INSTALLED_ADRESS.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_ADDRESS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_MOBILE_PHONE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONTACT_USER_EMAIL.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_TYPE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.APPLICATION_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.OPEN_FUNCTION_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.MULTI_MODULE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ECR_CONNECTION_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION2_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CONNECTION_TYPE_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_FUNCTION3_NAME.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.LOGO_STYLE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.IS_OPEN_ENCRYPT.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_PAY_PLATFORM.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.ELECTRONIC_INVOICE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.CUP_QUICK_PASS.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.LOCALHOST_IP.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.NET_VENDOR_NAME.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.GATEWAY.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.NETMASK.getValue());
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DESCRIPTION.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.TMS_PARAM_DESC.getValue());
		
		
		
		
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.DISPATCH_DATE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.COMPLETE_DATE.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.TID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.EDC_SERIAL_NUMBER.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS2_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
		queryColumnMap.put(SrmCaseHandleInfoDTO.ATTRIBUTE.PERIPHERALS3_SERIAL_NUMBER.getValue(), SrmCaseHandleInfoDTO.ATTRIBUTE.PROPERTY_ID.getValue());
		return queryColumnMap;
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
	 * @return the srmTransactionParameterDetailDAO
	 */
	public ISrmTransactionParameterDetailDAO getSrmTransactionParameterDetailDAO() {
		return srmTransactionParameterDetailDAO;
	}

	/**
	 * @param srmTransactionParameterDetailDAO the srmTransactionParameterDetailDAO to set
	 */
	public void setSrmTransactionParameterDetailDAO(
			ISrmTransactionParameterDetailDAO srmTransactionParameterDetailDAO) {
		this.srmTransactionParameterDetailDAO = srmTransactionParameterDetailDAO;
	}

}
