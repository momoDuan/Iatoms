package com.cybersoft4u.xian.iatoms.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import cafe.core.bean.Message;
import cafe.core.bean.Parameter;
import cafe.core.bean.identity.LogonUser;
import cafe.core.config.GenericConfigManager;
import cafe.core.config.SystemConfigManager;
import cafe.core.context.MultiParameterInquiryContext;
import cafe.core.context.SessionContext;
import cafe.core.dao.DataAccessException;
import cafe.core.dao.parameter.IBaseParameterItemDefDAO;
import cafe.core.service.AtomicService;
import cafe.core.service.ServiceException;
import cafe.core.util.DateTimeUtils;
import cafe.core.util.i18NUtil;
import cafe.core.util.log.CafeLog;
import cafe.core.util.log.CafeLogFactory;

import com.cybersoft4u.xian.iatoms.common.IATOMS_PARAM_TYPE;
import com.cybersoft4u.xian.iatoms.common.IAtomsConstants;
import com.cybersoft4u.xian.iatoms.common.IAtomsMessageCode;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseHandleInfoDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTemplatesDTO;
import com.cybersoft4u.xian.iatoms.common.bean.dto.SrmCaseTransactionParameterDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.BaseParameterManagerFormDTO;
import com.cybersoft4u.xian.iatoms.common.bean.formDTO.SrmCaseTemplatesFormDTO;
import com.cybersoft4u.xian.iatoms.common.utils.FileUtils;
import com.cybersoft4u.xian.iatoms.services.IBaseParameterManagerService;
import com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmCaseTemplatesDAO;
import com.cybersoft4u.xian.iatoms.services.dao.ISrmTransactionParameterDetailDAO;
import com.cybersoft4u.xian.iatoms.services.dmo.SrmCaseTemplates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Purpose: 工單範本維護Service
 * @author HermanWang
 * @since  JDK 1.6
 * @date   2016/9/23
 * @MaintenancePersonnel HermanWang
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked"})
public class SrmCaseTemplatesService extends AtomicService implements ISrmCaseTemplatesService {

	/**
	 * 系统日志文件控件
	 */
	private static final CafeLog LOGGER = CafeLogFactory.getLog(GenericConfigManager.SERVICE, SrmCaseTemplatesService.class);
	/**
	 * Constructor:
	 */
	public SrmCaseTemplatesService() {
	}
	/**
	 * 工單範本維護DAO
	 */
	private ISrmCaseTemplatesDAO srmCaseTemplatesDAO;
	/**
	 * SRM_交易參數項目 DAO 
	 */
	private ISrmTransactionParameterDetailDAO srmTransactionParameterDetailDAO;
	/**
	 * 基本參數DAO
	 */
	private IBaseParameterItemDefDAO baseParameterItemDefDAO;
	/**
	 * 系統參數維護Service
	 */
	private IBaseParameterManagerService baseParameterManagerService;
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#init(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext init(SessionContext sessionContext) throws ServiceException {
		Message msg=null;
		try {
			SrmCaseTemplatesFormDTO formDTO = null;
			Map map = new HashMap();
			formDTO = (SrmCaseTemplatesFormDTO) sessionContext.getRequestParameter();
			List<SrmCaseTemplatesDTO> srmCaseTemplatesDTOList = this.srmCaseTemplatesDAO.listBy(null, null);
			formDTO.setSrmCaseTemplatesDTOList(srmCaseTemplatesDTOList);
			//交易參數dtolist
			//List<SrmTransactionParameterDetailDTO> srmTransactionParameterDetailDTOList = this.srmTransactionParameterDetailDAO.getSrmTransactionParameterDetailDTOList();
			//for (SrmTransactionParameterDetailDTO srmTransactionParameterDetailDTO : srmTransactionParameterDetailDTOList) {
				//if(srmTransactionParameterDetailDTO.getTransactionType() != null) {
					
				//}
			//}
			//formDTO.setSrmTransactionParameterDetailDTO(srmTransactionParameterDetailDTOList);
			Date versionDate = DateTimeUtils.getCurrentTimestamp();
			MultiParameterInquiryContext param = new MultiParameterInquiryContext();
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_BPTD_CODE, IATOMS_PARAM_TYPE.TICKET_TYPE.getCode());
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_PARENT_ITEM_VALUE, "INSTALL");
			param.addParameter(BaseParameterManagerFormDTO.PARAMETER_CHILDREN_BPTD_CODE, IATOMS_PARAM_TYPE.TRANSACTION_CATEGORY.getCode());
			// 所有交易參數的集合
			List<Parameter> srmTransactionParameterList = (List<Parameter>) this.baseParameterManagerService.getParametersByParent(param);
			SrmCaseHandleInfoDTO srmCaseHandleInfoDTO = new SrmCaseHandleInfoDTO();
			//案件交易參數dtoList
			List<SrmCaseTransactionParameterDTO> srmCaseTransactionParameterDTOs = new ArrayList<SrmCaseTransactionParameterDTO>();
			for (Parameter parameter : srmTransactionParameterList) {
				//案件交易參數dto
				SrmCaseTransactionParameterDTO srmCaseTransactionParameterDTO = new SrmCaseTransactionParameterDTO();
				srmCaseTransactionParameterDTO.setTransactionType(parameter.getValue().toString());
				srmCaseTransactionParameterDTO.setTransactionTypeName(parameter.getName());
				//循環驗證是否重複
				srmCaseTransactionParameterDTOs.add(srmCaseTransactionParameterDTO);
			}
			Gson gsonss = null;
			if(srmCaseTransactionParameterDTOs != null) {
				gsonss = new GsonBuilder().create();
				//轉成json字符串
				String caseTransactionParameterStr = gsonss.toJson(srmCaseTransactionParameterDTOs);
				if(caseTransactionParameterStr != null) {
					srmCaseHandleInfoDTO.setCaseTransactionParameterStr(caseTransactionParameterStr);
					//有交易參數
					srmCaseHandleInfoDTO.setHaveTransParameter("Y");
					// Bug #2598 工單範本為Y
					srmCaseHandleInfoDTO.setIsCaseTemplate(IAtomsConstants.YES);
					formDTO.setSrmCaseHandleInfoDTO(srmCaseHandleInfoDTO);
				}
			}
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
			return sessionContext;
		} catch (Exception e) {
			LOGGER.error(".init(SessionContext sessionContext):", e);
			throw new ServiceException(IAtomsMessageCode.INIT_PAGE_FAILURE, e);
		}
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#upload(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext upload(SessionContext sessionContext)
			throws ServiceException {
		// 页面显示信息
		Message msg = null;
		PrintWriter printWriter = null;
		LogonUser logonUser = null;
		//Map<String, String> map = new HashMap<String, String>();
		try {
			SrmCaseTemplatesFormDTO formDTO = (SrmCaseTemplatesFormDTO) sessionContext.getRequestParameter();
			logonUser = formDTO.getLogonUser();
			//獲得上傳文件
			MultipartFile uploadFiled = formDTO.getUploadFiled();	
			//輸入流
			InputStream inputStream = uploadFiled.getInputStream();
			Workbook workbook = null;
			Sheet sheet = null;
			//獲取文件的名稱
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
				String errorMsg = i18NUtil.getName(IAtomsMessageCode.UPLOAD_FORMAT_ERROR);
				LOGGER.error("workbook is null >>> ");
				throw new ServiceException( IAtomsMessageCode.UPLOAD_FORMAT_ERROR);
			}
			boolean isRepent = this.srmCaseTemplatesDAO.isRepeat(fileName, formDTO.getCategoryId());
			if(isRepent) {
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.PARAM_EXISTS, new String[]{fileName});
				sessionContext.setReturnMessage(msg);
				return sessionContext;
			}
			//List<Parameter> categoryParameterList = (List<Parameter>) this.baseParameterItemDefDAO.getAvailableParameterItems(IATOMS_PARAM_TYPE.CASE_TEMPLATE_CATEGORY.getCode(), null);
			//當前日期
			//String yearMonthDay = DateTimeUtils.toString(DateTimeUtils.getCurrentDate(),  DateTimeUtils.DT_FMT_YYYYMMDD);
			String categoryName = "";
			//文件路徑
			String tempFilePath = SystemConfigManager.getProperty(IAtomsConstants.FILE_PATH, IAtomsConstants.FILE_UPLOAD_PATH) + File.separator 
					+ IAtomsConstants.UC_NO_SRM_05010 + File.separator + formDTO.getCategoryId() + File.separator;
			File filePath = new File(tempFilePath);
			if (!filePath.exists() || !filePath.isDirectory()) {
				filePath.mkdirs();
			}
			File saveFile = new File(tempFilePath, fileName);
			printWriter = new PrintWriter(saveFile, IAtomsConstants.ENCODE_UTF_8);
			FileUtils.upload(tempFilePath, uploadFiled, fileName);
			
			//保存到數據庫
			String id = this.generateGeneralUUID(IAtomsConstants.RSB_IATOMS_TB_NAME_SRM_CASE_TEMPLATES);
			SrmCaseTemplates srmCaseTemplates = new SrmCaseTemplates();
			//id
			srmCaseTemplates.setId(id);
			//範本類別
			srmCaseTemplates.setCategory(formDTO.getCategoryId());
			//路徑
			srmCaseTemplates.setFilePath(tempFilePath);
			//範本名稱
			srmCaseTemplates.setFileName(fileName);
			//創建人id
			srmCaseTemplates.setCreatedById(logonUser.getId());
			//創建人name
			srmCaseTemplates.setCreatedByName(logonUser.getName());
			//創建時間
			srmCaseTemplates.setCreatedDate(DateTimeUtils.getCurrentTimestamp());
			//更新人id
			srmCaseTemplates.setUpdatedById(logonUser.getId());
			//更新人name
			srmCaseTemplates.setUpdatedByName(logonUser.getName());
			//更新日期
			srmCaseTemplates.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
			//刪除標誌位
			srmCaseTemplates.setDeleted(IAtomsConstants.NO);
			this.srmCaseTemplatesDAO.getDaoSupport().saveOrUpdate(srmCaseTemplates);
			this.srmCaseTemplatesDAO.getDaoSupport().flush();
			//List<SrmCaseTemplatesDTO> srmCaseTemplatesDTOList = this.srmCaseTemplatesDAO.listBy(null, null);
			//formDTO.setSrmCaseTemplatesDTOList(srmCaseTemplatesDTOList);
			sessionContext.setResponseResult(formDTO);
			msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.UPLOAD_SECCUSS_TION, new String[]{fileName});
			sessionContext.setReturnMessage(msg);
			Map map = new HashMap();
			if (Message.STATUS.SUCCESS.equals(msg.getStatus())) {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(true));
			} else {
				map.put(IAtomsConstants.PARAM_ACTION_RESULT_SUCCESS, Boolean.valueOf(false));
			}
			sessionContext.setAttribute(IAtomsConstants.PARAM_ACTION_RESULT, map);
		} catch (Exception e) {
			LOGGER.error("upload(SessionContext sessionContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}

	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#delete(cafe.core.context.SessionContext)
	 */
	@Override
	public SessionContext delete(SessionContext sessionContext)
			throws ServiceException {
		Message msg=null;
		LogonUser logonUser = null;
		try {
			//從sessionContext中拿到formdto
			SrmCaseTemplatesFormDTO formDTO = (SrmCaseTemplatesFormDTO) sessionContext.getRequestParameter();
			logonUser = formDTO.getLogonUser();
			//從formdto中拿到要刪除行的id
			String fileId = formDTO.getFileId();
			if (StringUtils.hasText(fileId)) {
				//通過此條數據的主鍵拿到數據
				SrmCaseTemplates srmCaseTemplates = this.srmCaseTemplatesDAO.findByPrimaryKey(SrmCaseTemplates.class, fileId);
				//如果通過ID從數據庫查詢到的dmo不為空
				File filePath = null;
				if (srmCaseTemplates != null) {
					//修改刪除標誌位N
					srmCaseTemplates.setDeleted(IAtomsConstants.YES);
					//更新人id
					srmCaseTemplates.setUpdatedById(logonUser.getId());
					//更新人name
					srmCaseTemplates.setUpdatedByName(logonUser.getName());
					//更新日期
					srmCaseTemplates.setUpdatedDate(DateTimeUtils.getCurrentTimestamp());
					this.srmCaseTemplatesDAO.getDaoSupport().saveOrUpdate(srmCaseTemplates);
					msg = new Message(Message.STATUS.SUCCESS, IAtomsMessageCode.DELETE_SUCCESS, new String[]{srmCaseTemplates.getFileName()});
					//從臨時路徑下刪除文件
					FileUtils.removeFile(srmCaseTemplates.getFilePath().concat(File.separator).concat(srmCaseTemplates.getFileName()));
					filePath = new File(srmCaseTemplates.getFilePath());
					File[] fa = filePath.listFiles();
					if (fa != null && fa.length == 0) {
						//從服務器刪除文件
						FileUtils.removeFile(srmCaseTemplates.getFilePath());
					}
				}else{
					msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
				}
			}else{
				msg = new Message(Message.STATUS.FAILURE, IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()});
			}
			//List<SrmCaseTemplatesDTO> srmCaseTemplatesDTOList = this.srmCaseTemplatesDAO.listBy(null, null);
			//formDTO.setSrmCaseTemplatesDTOList(srmCaseTemplatesDTOList);
			sessionContext.setResponseResult(formDTO);
			sessionContext.setReturnMessage(msg);
		} catch (DataAccessException e) {
			LOGGER.error(".delete(SessionContext sessionContext):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.DELETE_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".delete(SessionContext sessionContext):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#getFilePath(com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO)
	 */
	public SessionContext getFilePath(SrmCaseTemplatesFormDTO command) throws ServiceException {
		SessionContext sessionContext = new SessionContext();
		try {
			//範本id
			String fileId = command.getFileId();
			SrmCaseTemplates srmCaseTemplates = this.srmCaseTemplatesDAO.findByPrimaryKey(SrmCaseTemplates.class, fileId);
			//設置路徑+/+文件名稱
			command.setPath(srmCaseTemplates.getFilePath() + File.separator +  srmCaseTemplates.getFileName());//
			sessionContext.setResponseResult(command);
		} catch (Exception e) {
			LOGGER.error("getFilePath(ContractManageFormDTO command):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return sessionContext;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#getFilePath(com.cybersoft4u.xian.iatoms.common.bean.formDTO.ContractManageFormDTO)
	 */
	public String getFilePath(MultiParameterInquiryContext inquiryContext) throws ServiceException {
		String filePath = "";
		try {
			//id
			String fileId = (String) inquiryContext.getParameter(SrmCaseTemplatesDTO.ATTRIBUTE.ID.getValue());
			if (StringUtils.hasText(fileId)) {
				SrmCaseTemplates srmCaseTemplates = this.srmCaseTemplatesDAO.findByPrimaryKey(SrmCaseTemplates.class, fileId);
				if (srmCaseTemplates != null) {
					//path
					filePath = srmCaseTemplates.getFilePath() + File.separator +  srmCaseTemplates.getFileName();// + attachedFile.getFileName();
				}
			}
		} catch (Exception e) {
			LOGGER.error("getFilePath(MultiParameterInquiryContext inquiryContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return filePath;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#getUploadTemplatesId(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public String getUploadTemplatesId(MultiParameterInquiryContext inquiryContext)
			throws ServiceException {
		String templatesId = "";
		try {
			//範本類別
			String category = (String) inquiryContext.getParameter(SrmCaseTemplatesDTO.ATTRIBUTE.CATEGORY.getValue());
			//範本名稱
			String fileName = (String) inquiryContext.getParameter(SrmCaseTemplatesDTO.ATTRIBUTE.FILE_NAME.getValue());
			if (StringUtils.hasText(category) && StringUtils.hasText(fileName)) {
				List<SrmCaseTemplatesDTO> srmCaseTemplatesDTO = this.srmCaseTemplatesDAO.getUploadTemplatesId(category, fileName);
				if(srmCaseTemplatesDTO != null) {
					//拿到範本id
					templatesId = srmCaseTemplatesDTO.get(0).getId();
				}
			}
		} catch (Exception e) {
			LOGGER.error("getFilePath(MultiParameterInquiryContext inquiryContext):", e);
			throw new ServiceException( IAtomsMessageCode.SYSTEM_FAILED);
		}
		return templatesId;
	}
	/**
	 * (non-Javadoc)
	 * @see com.cybersoft4u.xian.iatoms.services.ISrmCaseTemplatesService#getTemplatesList(cafe.core.context.MultiParameterInquiryContext)
	 */
	@Override
	public List<Parameter> getTemplatesList(MultiParameterInquiryContext inquiryContext)
			throws ServiceException {
		long startQueryTemplatesTime = System.currentTimeMillis();
		List<Parameter> list = null;
		try {
			//範本list
			list = this.srmCaseTemplatesDAO.getTemplatesList();
		} catch (DataAccessException e) {
			LOGGER.error(".getSuppliesTypeNameList(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.QUERY_FAILURE, new String[]{this.getMyName()}, e);
		} catch (Exception e) {
			LOGGER.error(".getSuppliesTypeNameList(MultiParameterInquiryContext param):", "service Exception", e);
			throw new ServiceException(IAtomsMessageCode.SYSTEM_FAILED, new String[]{this.getMyName()}, e);
		}
		long endQueryTemplatesTime = System.currentTimeMillis();
		LOGGER.debug("calculate time --> load", "Service getTemplatesList:" + (endQueryTemplatesTime - endQueryTemplatesTime));
		return list;
	}
/*	*//**
	 * Purpose:根據name拿到value
	 * @author HermanWang
	 * @param parameters：下拉框列表
	 * @param value 傳入的value
	 * @return 得到的name
	 *//*
	private String getValueByName(List<Parameter> parameterList, String value){
		String name = null;
		// 错误信息
		for (Parameter param : parameterList){
			if((param.getValue()).equals(value)){
				name = (String) param.getName();
				break;
			}
		}
		return name;
	}*/
	/**
	 * @return the srmCaseTemplatesDAO
	 */
	public ISrmCaseTemplatesDAO getSrmCaseTemplatesDAO() {
		return srmCaseTemplatesDAO;
	}
	/**
	 * @param srmCaseTemplatesDAO the srmCaseTemplatesDAO to set
	 */
	public void setSrmCaseTemplatesDAO(ISrmCaseTemplatesDAO srmCaseTemplatesDAO) {
		this.srmCaseTemplatesDAO = srmCaseTemplatesDAO;
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
	 * @return the baseParameterManagerService
	 */
	public IBaseParameterManagerService getBaseParameterManagerService() {
		return baseParameterManagerService;
	}
	/**
	 * @param baseParameterManagerService the baseParameterManagerService to set
	 */
	public void setBaseParameterManagerService(
			IBaseParameterManagerService baseParameterManagerService) {
		this.baseParameterManagerService = baseParameterManagerService;
	}
	
}
